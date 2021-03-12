package com.tyremandi.service.Imp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyremandi.dto.AddressDTO;
import com.tyremandi.io.entity.AddressEntity;
import com.tyremandi.io.entity.UserEntity;
import com.tyremandi.io.repository.AddressRepository;
import com.tyremandi.io.repository.UserRepository;
import com.tyremandi.service.AddressService;


@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	UserRepository userRespository;
	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDTO> getAddresses(String id) {
		List<AddressDTO> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = userRespository.findByUserId(id);
		if (userEntity == null)
			return returnValue;

		Iterable<AddressEntity> addressEntity = addressRepository.findAllByUserDetails(userEntity);
		for (AddressEntity address : addressEntity) {
			returnValue.add(modelMapper.map(address, AddressDTO.class));
		}
		return returnValue;
	}

	@Override
	public AddressDTO getAddressByAddressId(String id) {
		AddressEntity addressEntity = addressRepository.findByAddressId(id);
		AddressDTO returnValue = new AddressDTO();
		if (addressEntity == null) {
			return returnValue;
		}

		BeanUtils.copyProperties(addressEntity, returnValue);
		return returnValue;
	}

}
