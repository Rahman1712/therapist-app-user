package com.ar.therapist.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

	@Size(min = 2, max = 50, message = "❌ invalid fullname")
	private String fullname;

	@Email(message = "❌ Invalid email id")
	private String email;

	@Pattern(regexp = "^\\d{10}$", message = "❌ Invalid Mobile Number ; valid only 10 digit number ✔️")
	private String mobile;

	@NotBlank
	@Size(min = 5, max = 15, message = "❌ Invalid Username ; must be 5 to 15 characters ✔️")
	private String username;

	@NotBlank
	@Size(min = 5, max = 15, message = "❌ Invalid Password ; must be 5 to 15 characters ✔️")
	private String password;

	private String role;
}

//	@Min(18)
//	@Max(60)
//	private int age;