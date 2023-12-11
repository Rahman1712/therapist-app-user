package com.ar.therapist.user.entity.booking;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TherapistInfo {

	private Long therapistId;
	private String fullname;
	private String imageUrl;
	private String email;
    private String mobile;
	
}
