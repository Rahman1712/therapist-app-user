package com.ar.therapist.user.entity.booking;

import java.time.LocalDateTime;

import com.ar.therapist.user.dto.UserData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

	private String id;
	private LocalDateTime appointmentDateTime;
	private LocalDateTime rescheduleDateTime;
	private LocalDateTime cancellationDateTime;
	private String notes;
	private Double amount;
	private Long minutes;
	private String date;
	private TimeSlot timeSlot;
	private UserData userData;
	private TherapistInfo therapistInfo;
	private BookingStatus bookingStatus;
	private PaymentStatus paymentStatus;
	private BookingType bookingType;
	private Payment payment;
}