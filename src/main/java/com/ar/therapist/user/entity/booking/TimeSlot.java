package com.ar.therapist.user.entity.booking;

import java.time.LocalTime;

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
public class TimeSlot {
    private Long tid;  //id
    private LocalTime time;
    private boolean isBooked;
}