package com.tyremandi.ui.controller;
import org.springframework.hateoas.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tyremandi.dto.AddressDTO;
import com.tyremandi.dto.UserDto;
import com.tyremandi.exceptions.UserServiceException;
import com.tyremandi.service.AddressService;
import com.tyremandi.service.UserService;
import com.tyremandi.ui.request.model.PasswordResetRequestModel;
import com.tyremandi.ui.request.model.RequestOperationName;
import com.tyremandi.ui.request.model.UserDetailsRequestModel;
import com.tyremandi.ui.response.model.AddressesRest;
import com.tyremandi.ui.response.model.ErrorMessages;
import com.tyremandi.ui.response.model.OperationStatusModel;
import com.tyremandi.ui.response.model.RequestOperationStatus;
import com.tyremandi.ui.response.model.UserRest;

import java.lang.reflect.Type;
import java.util.*;

@RestController
@RequestMapping("/users") 
//// http://localhost:8080/mobile-aap/users
public class UserController {
	@Autowired          
	UserService userService;
	@Autowired
	AddressService addressSerive;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		if (userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty()
				|| userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()
				|| userDetails.getMobileNumber().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		// UserDto userDto = new UserDto();
		// BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto userCreateDetials = userService.createUser(userDto);
		UserRest returnValue = new UserRest();
		returnValue = modelMapper.map(userCreateDetials, UserRest.class);
		// BeanUtils.copyProperties(userCreateDetials, returnValue);

		return returnValue;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		UserDto UserDto1 = userService.getUserByUserId(id);
		// UserDto userDto= new UserDto();
		BeanUtils.copyProperties(userDetails, UserDto1);
		UserDto updateUserDto = userService.updateUser(id, UserDto1);
		BeanUtils.copyProperties(updateUserDto, returnValue);
		return returnValue;
	}

	@DeleteMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;

	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);
		for (UserDto userdto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userdto, userModel);
			returnValue.add(userModel);

		}
		return returnValue;
	}

	// http://localhost:8080/mobile-aap/users/userId/addresss
	@GetMapping(path = "/{id}/addresss", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public List<AddressesRest> getUserAddresses(@PathVariable String id) {
		List<AddressesRest> returnValue = new ArrayList<AddressesRest>();

		List<AddressDTO> addressDto = addressSerive.getAddresses(id);

		Type listType = new TypeToken<List<AddressesRest>>() {
		}.getType();

		returnValue = new ModelMapper().map(addressDto, listType);

		return returnValue;

	}

	// http://localhost:8080/mobileapp/users/userId/addresss/addressId
	@GetMapping(path = "/{id}/addresss/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public AddressesRest getUserAddress(@PathVariable String addressId) {
		AddressesRest returnValue = new AddressesRest();
		ModelMapper modelMapper = new ModelMapper();
      	//	Link addressLink = JlinkToolProvider
		AddressDTO addressDto = addressSerive.getAddressByAddressId(addressId);
		AddressesRest addressRest = modelMapper.map(addressDto, AddressesRest.class);
		//BeanUtils.copyProperties(addressDto, returnValue);

		return returnValue;

	}
	
	/*
     * http://localhost:8080/mobileapp/users/email-verification?token=sdfsdf
     * */
	@GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		boolean isVerified = userService.verifyEmailToken(token);
		if(isVerified) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
		else {
			returnValue.setOperationName(RequestOperationStatus.ERROR.name());
		}
		
		return returnValue;
	}
	
	   //http://localhost:8080/mobileapp/users/password-reset-request
	@PostMapping(path = "/password-reset-request",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
		    produces = { MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel requestRest(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
		OperationStatusModel returnValue = new OperationStatusModel();
		
		boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
		returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		if(operationResult) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());	
		}
		return returnValue;
		
	}
	
	
	
	
	
	
}
