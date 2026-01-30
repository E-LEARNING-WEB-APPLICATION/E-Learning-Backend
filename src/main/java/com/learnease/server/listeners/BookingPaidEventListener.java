package com.learnease.server.listeners;

import com.learnease.server.dto.invoice.InvoiceDto;
import com.learnease.server.events.BookingPaidEvent;
import com.learnease.server.model.Booking;
import com.learnease.server.model.enums.InvoiceStatus;
import com.learnease.server.repository.BookingRepository;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingPaidEventListener {

    private final BookingRepository bookingRepository;
    private final InvoiceService invoiceService;
    private final InvoiceStorageService invoiceStorageService;
    private final BookingInvoiceMapper bookingInvoiceMapper;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleBookingPaid(BookingPaidEvent event) {
        Booking booking = bookingRepository.findById(event.bookingId())
                .orElseThrow();

        try {

            InvoiceDto dto = bookingInvoiceMapper.fromBooking(booking);
            log.info("generating invoice pdf");
            byte[] pdf = invoiceService.generateInvoice(
                    InvoiceType.COURSE_PURCHASE,
                    dto
            );
            log.info("storing invoice pdf..");
            String url = invoiceStorageService.storeInvoice(
                    InvoiceType.COURSE_PURCHASE,
                    dto.getInvoiceNumber(),
                    pdf
            );
            log.info("stored invoice pdf..");

            booking.setInvoiceUrl(url);
            booking.setInvoiceStatus(InvoiceStatus.GENERATED);
            bookingRepository.save(booking);
            log.info("all done..");

        } catch (Exception ex) {
            // TODO: log + retry later
            booking.setInvoiceStatus(InvoiceStatus.FAILED);
            bookingRepository.save(booking);
            log.error("Invoice generation failed for booking {}", booking.getId(), ex);
        }
    }
}
