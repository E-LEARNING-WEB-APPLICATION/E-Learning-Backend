package com.learnease.server.listeners;

import com.learnease.server.dto.invoice.CoursePurchasedInvoiceDto;
import com.learnease.server.dto.invoice.InvoiceDto;
import com.learnease.server.dto.notification.EmailEvent;
import com.learnease.server.events.BookingPaidEvent;
import com.learnease.server.model.Booking;
import com.learnease.server.model.enums.InvoiceStatus;
import com.learnease.server.model.enums.NotificationType;
import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import com.learnease.server.repository.BookingRepository;
import com.learnease.server.repository.UserAuthRepository;
import com.learnease.server.service.EmailService;
import com.learnease.server.service.InvoiceService;
import com.learnease.server.service.InvoiceStorageService;
import com.learnease.server.util.enums.InvoiceType;
import com.learnease.server.util.mappers.invoice.BookingInvoiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingPaidEventListener {

    private final BookingRepository bookingRepository;
    private final InvoiceService invoiceService;
    private final InvoiceStorageService invoiceStorageService;
    private final BookingInvoiceMapper bookingInvoiceMapper;
    private final EmailService emailService;
    private final UserAuthRepository userAuthRepository;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleBookingPaid(BookingPaidEvent event) {
        Booking booking = bookingRepository.findById(event.bookingId())
                .orElseThrow();

        try {

            InvoiceDto dto = bookingInvoiceMapper.fromBooking(booking);
            byte[] pdf = invoiceService.generateInvoice(
                    InvoiceType.COURSE_PURCHASE,
                    dto
            );
            String url = invoiceStorageService.storeInvoice(
                    InvoiceType.COURSE_PURCHASE,
                    dto.getInvoiceNumber(),
                    pdf
            );

            booking.setInvoiceUrl(url);
            booking.setInvoiceStatus(InvoiceStatus.GENERATED);
            bookingRepository.save(booking);

            CoursePurchasedInvoiceDto invoiceDto = (CoursePurchasedInvoiceDto) dto;
            emailService.sendEmail(
                    EmailEvent.builder()
                            .eventType(NotificationType.COURSE_PURCHASED)
                            .to(List.of(booking.getStudent().getUserDetails().getUserAuth().getEmail()))
                            .subject("Course purchase order confirmed")
                            .data(Map.of(
                                    "studentFullName", invoiceDto.getStudentFullName(),
                                    "courseTitle", invoiceDto.getCourseTitle(),
                                    "instructorName", invoiceDto.getInstructorName(),
                                    "amountPaid", invoiceDto.getAmountPaid(),
                                    "currency", invoiceDto.getCurrency(),
                                    "invoiceNumber", invoiceDto.getInvoiceNumber(),
                                    "invoiceDate", invoiceDto.getInvoiceDate(),
                                    "platformName", invoiceDto.getPlatformName(),
                                    "platformEmail", invoiceDto.getPlatformEmail()
                            ))
                            .attachments(Map.of(
                                    "Booking_invoice"+ LocalDateTime.now() + ".pdf", url
                            ))
                            .meta(Map.of())
                            .build());
        } catch (Exception ex) {
            // TODO: log + retry later
            booking.setInvoiceStatus(InvoiceStatus.FAILED);
            bookingRepository.save(booking);
            log.error("Invoice generation failed for booking {}", booking.getId(), ex);
        }
    }
}
