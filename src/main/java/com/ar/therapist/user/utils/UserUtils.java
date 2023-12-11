package com.ar.therapist.user.utils;

import com.ar.therapist.user.dto.UserData;
import com.ar.therapist.user.dto.UserDto;
import com.ar.therapist.user.entity.User;

public class UserUtils {

	public static UserDto userToUserDto(User user) {
		return UserDto.builder()
				.id(user.getId())
				.username(user.getUsername())
				.fullname(user.getFullname())
				.email(user.getEmail())
				.mobile(user.getMobile())
				.role(user.getRole().name())
				.imageUrl(user.getImageUrl())
				.nonLocked(user.isNonLocked())
				.enabled(user.isEnabled())
				.build();
	}
	
	public static UserData userToUserData(User user) {
		return UserData.builder()
				.userId(user.getId())
				.fullname(user.getFullname())
				.email(user.getEmail())
				.mobile(user.getMobile())
				.imageUrl(user.getImageUrl())
				.build();
	}
}

//				.image(
//						user.getImage() == null ? 
//							user.getImage() : 
//							ImageUtils.decompress(user.getImage())
//				)
//				.imageName(user.getImageName())
//				.imageType(user.getImageType())