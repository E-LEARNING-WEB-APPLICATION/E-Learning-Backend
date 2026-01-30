package com.learnease.server.dto.invoice;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CoursePurchasedInvoiceDto implements InvoiceDto {

    // ===== Invoice Meta =====
    private String invoiceNumber;
    private LocalDateTime invoiceDate;

    private String orderId;      // razorpayOrderId
    private String paymentId;    // razorpayPaymentId
    private String paymentMethod;

    private String currency;

    // ===== Seller (Platform) =====
    private String platformName;
    private String platformEmail;
    private String platformAddress;
    private String platformGstin; // optional

    // ===== Buyer (Student) =====
    private String studentFullName;
    private String studentEmail;

    // ===== Course Info =====
    private String courseTitle;
    private String instructorName;

    private BigDecimal coursePrice;   // snapshot MRP
    private BigDecimal discountAmount;
    private BigDecimal amountPaid;

    // ===== Tax & Totals =====
    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
}
