package com.ar.therapist.user.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.user.cloudinary.CloudinaryImageServiceImpl;
import com.ar.therapist.user.dto.UserData;
import com.ar.therapist.user.dto.UserDto;
import com.ar.therapist.user.dto.UserUpdateRequest;
import com.ar.therapist.user.entity.AuthenticationProvider;
import com.ar.therapist.user.entity.Role;
import com.ar.therapist.user.entity.User;
import com.ar.therapist.user.exception.UserException;
import com.ar.therapist.user.repo.UserRepository;
import com.ar.therapist.user.s3.S3Buckets;
import com.ar.therapist.user.s3.S3Service;
import com.ar.therapist.user.utils.UserUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	@Value("${aws.region}")
	private String awsRegion;

	private final UserRepository userRepository;
	private final S3Service s3Service;
	private final S3Buckets s3Buckets;
	private final CloudinaryImageServiceImpl cloudService;
	private final PasswordEncoder passwordEncoder;
	
	public List<UserDto> findAll(){
		return userRepository.findAll()
			.stream()
			.map(UserUtils::userToUserDto)
			.collect(Collectors.toList());
	}
	
	public UserDto findById(Long id){
		return userRepository.findById(id)
				.map(UserUtils::userToUserDto)
				.orElse(null);
	}
	
	public UserDto findByUsername(String username) {
		return userRepository.findByUsername(username)
				.map(UserUtils::userToUserDto)
				.orElse(null);
	}
	
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElse(null);
	}
	
	public UserDto findByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(UserUtils::userToUserDto)
				.orElse(null);
	}
	
	public void updateEnabledById(Long id, boolean enabled) {
		userRepository.updateEnabledById(id, enabled);
	}
	
	public void updateNonLockedById(Long id, boolean nonLocked) {
		userRepository.updateNonLockedById(id, nonLocked);
	}

	public User createNewUserAfterOAuthLoginSuccess(String email, String name, Role role,AuthenticationProvider provider, String imageUrl) {
		User user = User.builder()
			.username(email)
			.email(email)
			.fullname(name)
			.imageUrl(imageUrl)
			.enabled(true)
			.nonLocked(true)
			.provider(provider)
			.role(role) //.role(Role.USER)
			.build();
		
		return userRepository.save(user); 
	}

	public User updateUserAfterOAuthLoginSuccess(User user, String name, AuthenticationProvider provider, String imageUrl) {
		user.setFullname(name);
		user.setImageUrl(imageUrl);
		user.setProvider(provider);
		
		return userRepository.save(user);
	}
	
//	public String updateImageById(Long id,MultipartFile file) throws IOException {
//		Map uploaded = cloudService.upload(file);
//		System.err.println(uploaded);
//		String imageUrl = (String) uploaded.get("url");
//		
//		userRepository.updateImageById(
//				id,
//				imageUrl
//				);
//		return "Image updated successfully ... "; 
//	}
	
	public String updateImageById(Long id,MultipartFile file) throws IOException {
		try {
			String key = "profile-images/users/%s/%s-%s".formatted(
					id, UUID.randomUUID().toString(), file.getOriginalFilename());
			
			Map<String, String> metadata = new HashMap<>();
			//	    metadata.put("Content-Type", file.getContentType()); 
			metadata.put("Content-Type", "image/jpeg"); 
			
			s3Service.putObject(
					s3Buckets.getTherapist(), 
					key, 
					file.getBytes(),
					metadata
					);
		
			String imageUrl = "https://%s.s3.%s.amazonaws.com/%s"
						.formatted(s3Buckets.getTherapist(), awsRegion, key);
				
			System.err.println(imageUrl);
			userRepository.updateImageById(id, imageUrl);
			
			return imageUrl; 
		}catch (IOException e) {
			throw new UserException("S3 Error Profile Image Upload");
		}
	}
	
	public byte[] getUserProfileImage(Long id) {
		var user = userRepository.findById(id)
				.orElseThrow(() -> new UserException("User not found with id "+id));
		
		
		//if(user.getImageUrl() == null) return null;
		if(user.getImageUrl().isBlank()) {
			throw new UserException("user with id [%s] not found".formatted(id));
		}
		
		String key = user.getImageUrl().replace(
				"https://%s.s3.%s.amazonaws.com/".formatted(s3Buckets.getTherapist(), 
				awsRegion), "");
		
		byte[] profileImage = s3Service.getObject(s3Buckets.getTherapist(), key);
		
		return profileImage;
	}
	
	public List<UserData> getUserDatasByIds(List<Long> ids) {
		return userRepository.findByIdIn(ids).stream().map(UserUtils::userToUserData).toList();
	}

	@Transactional
	public UserDto updateProfileData(Long userId, UserUpdateRequest request) {
		var user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with id "+userId));
		
		user.setFullname(request.getFullname());
		user.setMobile(request.getMobile());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		return Optional.of(userRepository.save(user)).map(UserUtils::userToUserDto).orElse(null);
	}

}
