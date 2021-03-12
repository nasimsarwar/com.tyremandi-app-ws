package com.tyremandi.exceptions;

public class UserServiceException extends RuntimeException {


	private static final long serialVersionUID = -6223861651516910630L;
	
	public UserServiceException(String message) {
		super(message);
	}

}

