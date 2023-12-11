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
public class BookingRequest {
	private LocalDateTime appointmentDateTime;
    private UserData userData;
    private String notes;
	private Double amount;
    private Long minutes;
    private String date;
    private Long therapistId;
    private Long timeSlotId;
    private BookingType bookingType; 
}