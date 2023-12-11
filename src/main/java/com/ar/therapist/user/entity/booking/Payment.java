package com.ar.therapist.user.entity.booking;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
	private String pid;
	@Column(name = "booking_id_payment")
	private String bookingId;
	@Column(name = "amount_val")
	private Double amount;
	private String razorPaymentId;
	private PaymentMethod paymentMethod;
	private LocalDateTime paymentDate;
}
