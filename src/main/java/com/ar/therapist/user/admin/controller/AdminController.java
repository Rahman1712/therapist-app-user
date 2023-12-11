package com.ar.therapist.user.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.user.dto.UserDto;
import com.ar.therapist.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user-to-admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final UserService userService;

	@GetMapping("/demo")
	public ResponseEntity<String> demoGet(){
		return ResponseEntity.ok("DEMO VAL");
	}
	
	@GetMapping("/get/allUsers")
	public ResponseEntity<List<UserDto>> getDetailsOfAllUsers(){
		return ResponseEntity.ok(userService.findAll());
	}
	
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("id")Long id){
		return ResponseEntity.ok(userService.findById(id));
	}
	
	@PutMapping("/update/nonlocked/byid/{userId}")
	public ResponseEntity<String> updateById(@PathVariable("userId")Long userId,
			@RequestParam("nonlocked") boolean nonlocked){
		userService.updateNonLockedById(userId, nonlocked);
		return ResponseEntity.ok("Updated Successfully");
	}
}