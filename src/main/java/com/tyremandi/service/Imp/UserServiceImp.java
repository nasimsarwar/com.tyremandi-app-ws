package com.tyremandi.service.Imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tyremandi.dto.AddressDTO;
import com.tyremandi.dto.UserDto;
import com.tyremandi.exceptions.UserServiceException;
import com.tyremandi.io.entity.UserEntity;
import com.tyremandi.io.repository.UserRepository;
import com.tyremandi.service.UserService;
import com.tyremandi.ui.response.model.ErrorMessages;
import com.tyremandi.utility.Utilis;



@Service
public class UserServiceImp implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	Utilis utilis;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto userDetails) {

		// Predicate<UserEntity> userEntity = e ->
		// userRepository.findByEmail(userDetails.getEmail()) != null;

		if (userRepository.findByEmail(userDetails.getEmail()) != null)
			throw new RuntimeException("User already present");

		for (int i = 0; i < userDetails.getAddresses().size(); i++) {
			AddressDTO address = userDetails.getAddresses().get(i);
			address.setUserDetails(userDetails);
			address.setAddressId(utilis.generateAddressId(20));
			userDetails.getAddresses().set(i, address);
		}

		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		String userID = utilis.generateUserId(20);
		userDetails.setUserId(userID);
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		System.out.println(userDetails);
		userEntity.setEmailVerificationToken(utilis.generateEmailVerificationToken(userID));
		userEntity.setEmailVerificationStatus(false);
		UserEntity storedUser = userRepository.save(userEntity);
		UserDto returnValue = modelMapper.map(storedUser, UserDto.class);
		return returnValue;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userRepository.delete(userEntity);
	}

	@Override
	public UserDto updateUser(String userId, UserDto userdto) {
		// UserEntity userEntity = userRepository.findByEmail(userdto.getEmail());
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		// UserEntity userEntity = userRepository.findByEmail(userdto.getEmail());
		userEntity.setFirstName(userdto.getFirstName());
		userEntity.setLastName(userdto.getLastName());
		// userEntity.setEmail(userdto.getEmail());
		// userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));
		UserEntity updateUserEntity = userRepository.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(updateUserEntity, returnValue);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);
		if (userEntity == null)
			throw new UsernameNotFoundException("User does not exist");
		//return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
		return new User(username, userEntity.getEncryptedPassword(), 
				userEntity.getEmailVerificationStatus(), true,
				true, true,
				new ArrayList<>());
	}

	@Override
	public UserDto getUserByUserId(String id) {
		UserEntity userEntity = userRepository.findByUserId(id);
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> users = userRepository.findAll(pageableRequest);
		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}
		return returnValue;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		boolean returnValue = false;
		UserEntity userEntity =userRepository.findUserByEmailVerificationToken(token);
		if(userEntity!=null) {
			boolean hasTokenExpried = utilis.hasTokenExpired(token);
			if(!hasTokenExpried) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				returnValue = true;
			}
		}
		return returnValue;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		boolean returnValue = false;
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) {
			return returnValue;
		}
		
		String token = utilis.generatePasswordRestToken(userEntity.getUserId());
		return false;
	}

	@Override
	public boolean resetPassword(String token, String password) {
		// TODO Auto-generated method stub
		return false;
	}

}
