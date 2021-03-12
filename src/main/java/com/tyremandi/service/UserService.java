package com.tyremandi.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tyremandi.dto.UserDto;



public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDetails);

	UserDto getUser(String email);

	UserDto getUserByUserId(String id);

	void deleteUser(String userId);

	UserDto updateUser(String id, UserDto userdto);

	List<UserDto> getUsers(int page, int limit);

	boolean verifyEmailToken(String token);

	boolean requestPasswordReset(String email);

	boolean resetPassword(String token, String password);
	default void m2() {
		
	}

}
