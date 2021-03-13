package com.tyremandi.userservice.imp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.tyremandi.dto.UserDto;
import com.tyremandi.io.entity.UserEntity;
import com.tyremandi.io.repository.UserRepository;
import com.tyremandi.service.Imp.UserServiceImp;
import com.tyremandi.utility.Utilis;



class UserServiceImpTest {
    @InjectMocks
	UserServiceImp userService;
    @Mock
    UserRepository userRepository;
    @Mock
    Utilis utilis;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    String userId = "hasdjf243r3r3";
    String encryptedPassword = "jjfvzfzjdksn123";
    
    UserEntity userEntity;
    
    @BeforeEach
	void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Nasim");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("nasim@nasim1.com");
		//userEntity.setEmailVerificationToken("ckcncnccjnc");
	}

	
	/*
	 * @Test void testGetUser() {
	 * 
	 * when(userRepository.findByEmail(anyString())).thenReturn(userEntity); UserDto
	 * userdto = userService.getUser("nasim@nasim.com"); assertNotNull(userdto);
	 * assertEquals("Nasim", userdto.getFirstName()); }
	 */
	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		 assertThrows(UsernameNotFoundException.class, ()->{
			 userService.getUser("nasim@nasim.com");
		                                                    }
		              );
	}
	
	
	/*
	 * //CreateUser Method Test case
	 * 
	 * @Test final void testCreateUser() {
	 * 
	 * when(userRepository.findByEmail(anyString())).thenReturn(null);
	 * when(utilis.generateUserId(anyInt())).thenReturn(userId);
	 * when(utilis.generateAddressId(anyInt())).thenReturn("hsfsjvbvbsjbshfbs122");
	 * when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword)
	 * ; when(((OngoingStubbing<UserEntity>) userRepository.save(new
	 * UserEntity())).thenReturn(userEntity)); AddressDTO addressDto = new
	 * AddressDTO(); addressDto.setType("shipping"); java.util.List<AddressDTO>
	 * addressess = new ArrayList<AddressDTO>(); addressess.add(addressDto); UserDto
	 * userDto = new UserDto(); userDto.setAddresses(addressess); UserDto
	 * StoreduserEntity = userService.createUser(userDto);
	 * assertNotNull(StoreduserEntity); assertEquals(userEntity.getFirstName(),
	 * StoreduserEntity.getFirstName());
	 * 
	 * }
	 */

}
