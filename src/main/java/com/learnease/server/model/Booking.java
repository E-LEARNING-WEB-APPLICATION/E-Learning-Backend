package com.learnease.server.model;

import com.learnease.server.model.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "booking", uniqueConstraints = {@UniqueConstraint(name = "UK_student_course_booking",
		columnNames = {"course_id", "student_id"})})
@AttributeOverride(name = "id", column = @Column(name = "booking_id"))
public class Booking extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false)
	@NotNull
	private Course purchasedCourse;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	@NotNull
	private Student student;

	@ManyToOne
	@JoinColumn(name = "instructor_id", nullable = false)
	@NotNull
	private Instructor instructor;

	private LocalDateTime purchaseTime;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal coursePriceSnapShot;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal pricePaid;

	@Column(length = 3, nullable = false)
	private String currency;

	@Column(unique = true, nullable = false)
	private String paymentReference;

	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	private LocalDateTime expiresAt;
}
