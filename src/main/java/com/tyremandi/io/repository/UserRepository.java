package com.tyremandi.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tyremandi.io.entity.UserEntity;



@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String id);
	UserEntity findUserByEmailVerificationToken(String token);

}
