package com.learnease.server.service.impl;

import com.learnease.server.dto.booking.CreateBookingRequestDto;
import com.learnease.server.dto.booking.CreateBookingResponseDto;
import com.learnease.server.dto.booking.VerifyPaymentRequestDto;
import com.learnease.server.exception.custom_exception.BookingException;
import com.learnease.server.model.*;
import com.learnease.server.model.enums.BookingErrorCode;
import com.learnease.server.model.enums.BookingStatus;
import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import com.learnease.server.repository.*;
import com.learnease.server.service.BookingService;
import com.learnease.server.util.mappers.BookingMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final UserAuthRepository authRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final BookingRepository bookingRepository;
    private final RazorpayClient razorpayClient;
    private final BookingMapper bookingMapper;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;


    @Override
    public CreateBookingResponseDto createBooking(
            CreateBookingRequestDto request, UUID authId
    ) {

//        -----------Resolved Authenticated User ----------

        //we need to find that user exist or not
        UserAuth userAuth = authRepository.findById(authId)
                .orElseThrow(()-> new BookingException(
                        BookingErrorCode.USER_NOT_FOUND,
                        "Authenticated user not found"
                ));
        //check for the user's role only student allowed
        if(userAuth.getRole() != Role.STUDENT){
            throw new BookingException(
                    BookingErrorCode.USER_NOT_STUDENT,
                    "Only students can create bookings"
            );
        }

        //checking status
        if(userAuth.getStatus() != Status.ACTIVE){
            throw new BookingException(
                    BookingErrorCode.USER_NOT_ACTIVE,
                    "Inactive users cannot create bookings"
            );
        }

        Student student = studentRepository.findByUserDetails_UserAuth_Id(authId)
                .orElseThrow(()-> new BookingException(
                        BookingErrorCode.STUDENT_PROFILE_NOT_FOUND,
                        "Student profile not found"
                ));

//        ---------- Load Course -----------

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(()-> new BookingException(
                        BookingErrorCode.COURSE_NOT_FOUND,
                        "Course not found"
                ));

//        ----------- Load Instructor -----------
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(()-> new BookingException(
                        BookingErrorCode.INSTRUCTOR_NOT_FOUND,
                        "Instructor not found"
                ));

//        ------------- Validate Course-Instructor relationship -----------

        if(!course.getInstructor().getId().equals(instructor.getId())){
            throw new BookingException(
                    BookingErrorCode.INSTRUCTOR_NOT_ALLOWED,
                    "Instructor is not associated with this course"
            );
        };

//        ------------------ Booking idempotency Check --------------

        Optional<Booking> existingBookingOpt = bookingRepository
                .findByStudentAndPurchasedCourse(student , course);

        if(existingBookingOpt.isPresent()) {
            Booking existingBooking = existingBookingOpt.get();

            //Already Paid -- Hard Stop
            if(existingBooking.getStatus() == BookingStatus.PAID){
                throw new BookingException(
                        BookingErrorCode.BOOKING_ALREADY_PAID,
                        "Course already purchased"
                );
            }

            //Booking is pending but not expired -- reuse
            if(existingBooking.getStatus() == BookingStatus.PENDING &&
                    existingBooking.getExpiresAt().isAfter(LocalDateTime.now()) &&
                    existingBooking.getRazorpayOrderId() != null
            ){

                // We will reuse this booking later
                return bookingMapper.toCreateBookingResponse(existingBooking);
            }

            if(existingBooking.getStatus() == BookingStatus.PENDING &&
                    existingBooking.getExpiresAt().isBefore(LocalDateTime.now())){
                existingBooking.setStatus(BookingStatus.EXPIRED);
                bookingRepository.save(existingBooking);
            }
        }

        BigDecimal courseFees = BigDecimal.valueOf(course.getFees());

        BigDecimal discountPercentage = BigDecimal.valueOf(course.getDiscount())
                .divide(BigDecimal.valueOf(100));

        BigDecimal discountAmount =
                courseFees.multiply(discountPercentage);

        BigDecimal finalPrice =
                courseFees.subtract(discountAmount);

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        Booking booking = new Booking();

        booking.setStudent(student);
        booking.setPurchasedCourse(course);
        booking.setInstructor(instructor);

        booking.setCoursePriceSnapShot(courseFees);
        booking.setPricePaid(finalPrice);
        booking.setCurrency("INR");

        booking.setStatus(BookingStatus.PENDING);
        booking.setExpiresAt(expiresAt);

        booking = bookingRepository.save(booking);

        long amountInPaise = booking
                .getPricePaid()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        // -----------------------------
        //  Create Razorpay Order
        // -----------------------------

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount" , amountInPaise);
        orderRequest.put("currency" , booking.getCurrency());
        orderRequest.put("receipt" , booking.getId().toString());
        orderRequest.put("payment_capture", 1);

        Order razorPayOrder;
        try {
            razorPayOrder = razorpayClient.orders.create(orderRequest);
        }catch (RazorpayException ex){
            throw new BookingException(
                    BookingErrorCode.PAYMENT_ORDER_CREATION_FAILED,
                    "Failed to create payment order. Please try again."
            );
        };

//        ------------- Link Razorpay Order to Booking ----------
        booking.setRazorpayOrderId(razorPayOrder.get("id"));
        bookingRepository.save(booking);

        return bookingMapper.toCreateBookingResponse(booking);
    }

    @Override
    @Transactional
    public void verifyPayment(
            UUID bookingId,
            VerifyPaymentRequestDto request
    ) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException(
                        BookingErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found"
                ));

       //----------- Idempotency check

        if (booking.getStatus() == BookingStatus.PAID) {
            return; // already verified, safe no-op
        }

        if (booking.getStatus() == BookingStatus.REJECTED) {
            throw new BookingException(
                    BookingErrorCode.PAYMENT_REJECTED,
                    "Payment already rejected for this booking"
            );
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException(
                    BookingErrorCode.INVALID_BOOKING_STATE,
                    "Booking is not in a payable state"
            );
        }


         // Order ID validation
        if (!booking.getRazorpayOrderId()
                .equals(request.getRazorpayOrderId())) {

            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);

            throw new BookingException(
                    BookingErrorCode.ORDER_ID_MISMATCH,
                    "Razorpay order ID mismatch"
            );
        }

        // Signature verification

        boolean isValidSignature = verifyRazorpaySignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        if (!isValidSignature) {

            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);

            throw new BookingException(
                    BookingErrorCode.INVALID_PAYMENT_SIGNATURE,
                    "Invalid Razorpay payment signature"
            );
        }

        // SUCCESS — finalize payment

        booking.setRazorpayPaymentId(request.getRazorpayPaymentId());
        booking.setStatus(BookingStatus.PAID);
        booking.setPaidAt(LocalDateTime.now());

        Student student = booking.getStudent();
        Course course = booking.getPurchasedCourse();

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            student.setTotalEnrolledCourses(
                    student.getTotalEnrolledCourses() + 1
            );
        }

        bookingRepository.save(booking);
        // student is managed — dirty checking will persist enrollment
    }


    private boolean verifyRazorpaySignature(
            String orderId,
            String paymentId,
            String razorpaySignature
    ) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", razorpaySignature);

            return Utils.verifyPaymentSignature(options, razorpaySecret);
        } catch (Exception e) {
            return false;
        }
    }

}
