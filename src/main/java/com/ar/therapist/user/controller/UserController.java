package com.ar.therapist.user.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.user.dto.UserDto;
import com.ar.therapist.user.dto.UserUpdateRequest;
import com.ar.therapist.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@PutMapping(
			value =  "/update/profile-image/byId/{id}", 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE 
	)
	public ResponseEntity<String> updateImageById(
			@PathVariable("id")Long id,
			@RequestParam("file") MultipartFile file){
		try {
			return ResponseEntity.ok(userService.updateImageById(id, file));
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Updation Failed");
		}
	}
	
	@GetMapping(
			value =  "/{userId}/profile-image",
			produces = MediaType.IMAGE_JPEG_VALUE
			)
	public ResponseEntity<?> getUserProfileImage(@PathVariable("userId") Long userId) {
		 byte[] userProfileImageBytes = userService.getUserProfileImage(userId);
//		 return ResponseEntity.ok(userProfileImageBytes);
		 String base64 = Base64.getEncoder().encodeToString(userProfileImageBytes);
		 return ResponseEntity.ok(base64);
	}

	@PutMapping("/update/profile-data/byid/{userId}")
	public ResponseEntity<UserDto> updateProfileData(@PathVariable("userId") Long userId,@RequestBody UserUpdateRequest request){
		return ResponseEntity.ok(userService.updateProfileData(userId, request));
	}
	
}
