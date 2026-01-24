package com.learnease.server.service.impl;

import com.learnease.server.dto.booking.CreateBookingRequestDto;
import com.learnease.server.dto.booking.CreateBookingResponseDto;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
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
}
