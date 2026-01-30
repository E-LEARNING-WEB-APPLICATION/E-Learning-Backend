package com.learnease.server.util.mappers.invoice;

import com.learnease.server.dto.invoice.CoursePurchasedInvoiceDto;
import com.learnease.server.dto.invoice.InvoiceDto;
import com.learnease.server.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BookingInvoiceMapper {

    public InvoiceDto fromBooking(Booking booking) {

        Student student = booking.getStudent();
        UserDetails ud = student.getUserDetails();
        UserAuth ua = ud.getUserAuth();

        Course course = booking.getPurchasedCourse();
        Instructor instructor = booking.getInstructor();

        BigDecimal mrp = booking.getCoursePriceSnapShot();
        BigDecimal paid = booking.getPricePaid();
        BigDecimal discount = mrp.subtract(paid);

        return CoursePurchasedInvoiceDto.builder()
                .invoiceNumber("INV-" + booking.getId())
                .invoiceDate(booking.getPaidAt())

                .orderId(booking.getRazorpayOrderId())
                .paymentId(booking.getRazorpayPaymentId())
                .paymentMethod(booking.getPaymentMethod())
                .currency(booking.getCurrency())

                .platformName("LearnEase")
                .platformEmail("billing@learnease.com")
                .platformAddress("Pune, Maharashtra, India")

                .studentFullName(
                        ud.getFirstName() + " " + ud.getLastName()
                )
                .studentEmail(ua.getEmail())

                .courseTitle(course.getTitle())
                .instructorName(instructor.getUserDetails().getFirstName()
                        + " " +
                        instructor.getUserDetails().getLastName())

                .coursePrice(mrp)
                .discountAmount(discount)
                .amountPaid(paid)

                .subTotal(mrp)
                .taxAmount(BigDecimal.ZERO)
                .grandTotal(paid)

                .build();
    }
}

