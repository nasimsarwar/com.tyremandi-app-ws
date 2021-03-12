package com.tyremandi.userservice.imp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tyremandi.dto.UserDto;
import com.tyremandi.io.entity.UserEntity;
import com.tyremandi.io.repository.UserRepository;
import com.tyremandi.service.Imp.UserServiceImp;

class UserServiceImpTest {
    @InjectMocks
	UserServiceImp userServiceImp;
    @Mock
    UserRepository userRepository;
    @BeforeEach
	void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
	}

	
	@Test
	void testGetUser() {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Nasim");
		userEntity.setUserId("user1233");
		userEntity.setEncryptedPassword("jjfvzfzjdksn123");
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		UserDto userdto = userServiceImp.getUser("nasim@nasim.com");
	   assertNotNull(userdto);
	   assertEquals("Nasim", userdto.getFirstName());
	}

}
