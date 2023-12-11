package com.ar.therapist.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

	private Long id;
	private String username;
	private String fullname;
	private String email;
	private String mobile;
	private String role;
	private String imageUrl;
	private boolean nonLocked;
	private boolean enabled;
}

//	private byte[] image;
//	private String imageName;
//	private String imageType;