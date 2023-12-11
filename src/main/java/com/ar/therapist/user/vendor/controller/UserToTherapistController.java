package com.ar.therapist.user.vendor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.user.dto.UserData;
import com.ar.therapist.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user-to-therapist")
@RequiredArgsConstructor
public class UserToTherapistController {
	
	private final UserService userService;
	
	@GetMapping("/users-byids")
	public ResponseEntity<List<UserData>> getUserDatasByIds(@RequestParam("userIds") List<Long> userIds){
		return ResponseEntity.ok(userService.getUserDatasByIds(userIds));
	}
}