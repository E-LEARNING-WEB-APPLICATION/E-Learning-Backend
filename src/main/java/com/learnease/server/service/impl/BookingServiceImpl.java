package com.learnease.server.service.impl;

import com.learnease.server.dto.booking.CreateBookingRequestDto;
import com.learnease.server.dto.booking.CreateBookingResponseDto;
import com.learnease.server.dto.booking.VerifyPaymentRequestDto;
import com.learnease.server.exception.custom_exception.BookingException;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.model.*;
import com.learnease.server.model.enums.*;
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
    private final WalletTransactionRepository walletTransactionRepository;
    private final CommissionConfigRepository  commissionConfigRepository;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    // ========================
    // CREATE BOOKING
    // =======================

    @Override
    public CreateBookingResponseDto createBooking(
            CreateBookingRequestDto request,
            UUID authId
    ) {

        UserAuth userAuth = resolveActiveStudent(authId);
        Student student = resolveStudent(authId);
        Course course = resolveCourse(request.getCourseId());
        Instructor instructor = resolveInstructor(request.getInstructorId());

        validateCourseInstructor(course, instructor);

        Optional<Booking> reusableBooking =
                handleExistingBooking(student, course);

        if (reusableBooking.isPresent()) {
            return bookingMapper.toCreateBookingResponse(
                    reusableBooking.get()
            );
        }

        Booking booking = createPendingBooking(
                student,
                course,
                instructor
        );

        createAndAttachRazorpayOrder(booking);

        return bookingMapper.toCreateBookingResponse(booking);
    }

    // ======================================================
    // VERIFY PAYMENT
    // ======================================================

    @Override
    public void verifyPayment(
            UUID bookingId,
            VerifyPaymentRequestDto request
    ) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException(
                        BookingErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found"
                ));

        validateBookingStateForPayment(booking);
        validateOrderId(booking, request.getRazorpayOrderId());
        validateSignature(request);

        markBookingAsPaid(booking, request.getRazorpayPaymentId());
        enrollStudentIfNotAlready(booking);
    }

    // ================================
    // AUTH / USER RESOLUTION
    // ================================

    private UserAuth resolveActiveStudent(UUID authId) {
        UserAuth userAuth = authRepository.findById(authId)
                .orElseThrow(() -> new BookingException(
                        BookingErrorCode.USER_NOT_FOUND,
                        "Authenticated user not found"
                ));

        if (userAuth.getRole() != Role.STUDENT) {
            throw new BookingException(
                    BookingErrorCode.USER_NOT_STUDENT,
                    "Only students can create bookings"
            );
        }

        if (userAuth.getStatus() != Status.ACTIVE) {
            throw new BookingException(
                    BookingErrorCode.USER_NOT_ACTIVE,
                    "Inactive users cannot create bookings"
            );
        }

        return userAuth;
    }

    private Student resolveStudent(UUID authId) {
        return studentRepository
                .findByUserDetails_UserAuth_Id(authId)
                .orElseThrow(() -> new BookingException(
                        BookingErrorCode.STUDENT_PROFILE_NOT_FOUND,
                        "Student profile not found"
                ));
    }

    // ============================
    // COURSE / INSTRUCTOR
    // ============================

    private Course resolveCourse(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new BookingException(
                        BookingErrorCode.COURSE_NOT_FOUND,
                        "Course not found"
                ));
    }

    private Instructor resolveInstructor(UUID instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new BookingException(
                        BookingErrorCode.INSTRUCTOR_NOT_FOUND,
                        "Instructor not found"
                ));
    }

    private void validateCourseInstructor(
            Course course,
            Instructor instructor
    ) {
        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new BookingException(
                    BookingErrorCode.INSTRUCTOR_NOT_ALLOWED,
                    "Instructor is not associated with this course"
            );
        }
    }

    // =============================
    // BOOKING CREATION HELPERS
    // =============================

    private Optional<Booking> handleExistingBooking(
            Student student,
            Course course
    ) {

        Optional<Booking> existingOpt =
                bookingRepository.findByStudentAndPurchasedCourse(
                        student,
                        course
                );

        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        Booking booking = existingOpt.get();

        if (booking.getStatus() == BookingStatus.PAID) {
            throw new BookingException(
                    BookingErrorCode.BOOKING_ALREADY_PAID,
                    "Course already purchased"
            );
        }

        if (booking.getStatus() == BookingStatus.PENDING &&
                booking.getExpiresAt().isAfter(LocalDateTime.now()) &&
                booking.getRazorpayOrderId() != null) {

            return Optional.of(booking);
        }

        if (booking.getStatus() == BookingStatus.PENDING &&
                booking.getExpiresAt().isBefore(LocalDateTime.now())) {

            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
        }

        return Optional.empty();
    }

    private Booking createPendingBooking(
            Student student,
            Course course,
            Instructor instructor
    ) {

        BigDecimal courseFees = BigDecimal.valueOf(course.getFees());
        BigDecimal discount =
                BigDecimal.valueOf(course.getDiscount())
                        .divide(BigDecimal.valueOf(100));

        BigDecimal finalPrice =
                courseFees.subtract(courseFees.multiply(discount));

        Booking booking = new Booking();
        booking.setStudent(student);
        booking.setPurchasedCourse(course);
        booking.setInstructor(instructor);
        booking.setCoursePriceSnapShot(courseFees);
        booking.setPricePaid(finalPrice);
        booking.setCurrency("INR");
        booking.setStatus(BookingStatus.PENDING);
        booking.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        return bookingRepository.save(booking);
    }

    private void createAndAttachRazorpayOrder(
            Booking booking
    ) {

        long amountInPaise =
                booking.getPricePaid()
                        .multiply(BigDecimal.valueOf(100))
                        .longValueExact();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", booking.getCurrency());
        orderRequest.put("receipt", booking.getId().toString());
        orderRequest.put("payment_capture", 1);

        try {
            Order order =
                    razorpayClient.orders.create(orderRequest);

            booking.setRazorpayOrderId(order.get("id"));
            bookingRepository.save(booking);

        } catch (RazorpayException ex) {
            throw new BookingException(
                    BookingErrorCode.PAYMENT_ORDER_CREATION_FAILED,
                    "Failed to create payment order. Please try again."
            );
        }
    }

    // ================================
    // PAYMENT VERIFICATION HELPERS
    // ================================

    private void validateBookingStateForPayment(
            Booking booking
    ) {

        if (booking.getStatus() == BookingStatus.PAID) {
            return;
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
    }

    private void validateOrderId(
            Booking booking,
            String orderId
    ) {
        if (!booking.getRazorpayOrderId().equals(orderId)) {
            rejectBooking(
                    booking,
                    BookingErrorCode.ORDER_ID_MISMATCH,
                    "Razorpay order ID mismatch"
            );
        }
    }

    private void validateSignature(
            VerifyPaymentRequestDto request
    ) {
        boolean valid = verifyRazorpaySignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        if (!valid) {
            rejectBooking(
                    null,
                    BookingErrorCode.INVALID_PAYMENT_SIGNATURE,
                    "Invalid Razorpay payment signature"
            );
        }
    }

    private void markBookingAsPaid(
            Booking booking,
            String paymentId
    ) {
        booking.setRazorpayPaymentId(paymentId);
        booking.setStatus(BookingStatus.PAID);
        booking.setPaidAt(LocalDateTime.now());
        bookingRepository.save(booking);
        createForPaidBooking(booking);
    }

    private void enrollStudentIfNotAlready(
            Booking booking
    ) {
        Student student = booking.getStudent();
        Course course = booking.getPurchasedCourse();

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            student.setTotalEnrolledCourses(
                    student.getTotalEnrolledCourses() + 1
            );
        }
    }

    private void rejectBooking(
            Booking booking,
            BookingErrorCode errorCode,
            String message
    ) {
        if (booking != null) {
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);
        }
        throw new BookingException(errorCode, message);
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

            return Utils.verifyPaymentSignature(
                    options,
                    razorpaySecret
            );
        } catch (Exception e) {
            return false;
        }
    }

    public void createForPaidBooking(Booking booking) {

        // Safety: prevent duplicate wallet entry
        if (walletTransactionRepository.existsByBooking(booking)) {
            return;
        }

        CommissionConfig commissionConfig = commissionConfigRepository.findFirst()
                .orElseGet(CommissionConfig::new);

        double companyPct = commissionConfig.getCommission();

        BigDecimal instructorAmount =
                booking.getPricePaid()
                        .multiply(BigDecimal.valueOf(100 - companyPct))
                        .divide(BigDecimal.valueOf(100));

        WalletTransaction tx = new WalletTransaction();
        tx.setBooking(booking);
        tx.setInstructor(booking.getInstructor());
        tx.setAmount(instructorAmount);
        tx.setPayoutStatus(PayoutStatus.AVAILABLE);

        walletTransactionRepository.save(tx);
    }

}

