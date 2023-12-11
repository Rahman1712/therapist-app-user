package com.ar.therapist.user.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    private Long id;
    private Long therapistId;
//    private Long userId;
    private UserData userData;
    private String bookingId; 
    private String content;
    private int rating;
    private LocalDate date;
    
}