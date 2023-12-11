package com.ar.therapist.user.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.therapist.user.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	public Optional<User> findByUsername(String username);
	
	public Optional<User> findByEmail(String email);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.enabled = :enabled WHERE u.id =:id")
	public void updateEnabledById(@Param("id") Long id,@Param("enabled") boolean enabled);


	@Modifying
	@Transactional
	@Query("UPDATE User u SET "
			+ "u.password = :newPassword "
			+ "WHERE u.id = :id")
	void updatePassword(@Param("id")Long id, @Param("newPassword")String newPassword);

//	@Modifying
//	@Transactional
//	@Query("update User u SET "
//			+ "u.image = :image, "
//			+ "u.imageName = :imageName, "
//			+ "u.imageType = :imageType "
//			+ "where u.id = :id")
//	public void updateImageById(
//			@Param("id") Long id, 
//			@Param("image") byte[] image,
//			@Param("imageName")String imageName,
//			@Param("imageType")String imageType);
	
	@Modifying
	@Transactional
	@Query("update User u SET "
			+ "u.imageUrl = :imageUrl "
			+ "where u.id = :id")
	public void updateImageById(
			@Param("id") Long id, 
			@Param("imageUrl") String imageUrl);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.nonLocked = :nonLocked WHERE u.id =:id")
	public void updateNonLockedById(@Param("id")Long id, @Param("nonLocked") boolean nonLocked);
	
	
	List<User> findByIdIn(List<Long> userIds);
	
}
