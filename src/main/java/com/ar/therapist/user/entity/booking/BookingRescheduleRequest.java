package com.ar.therapist.user.entity.booking;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRescheduleRequest {
	
	private LocalDateTime rescheduleDateTime;
    private String notes;
    private String date;
    private Long timeSlotId;
    private BookingType bookingType;
}