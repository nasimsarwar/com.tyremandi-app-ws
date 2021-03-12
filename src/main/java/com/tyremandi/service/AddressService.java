package com.tyremandi.service;

import java.util.List;

import com.tyremandi.dto.AddressDTO;



public interface AddressService {
	
	List<AddressDTO> getAddresses(String id);

	AddressDTO getAddressByAddressId(String id);

}
