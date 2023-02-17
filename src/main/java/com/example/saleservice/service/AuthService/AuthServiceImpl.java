package com.example.saleservice.service.AuthService;

import com.example.saleservice.service.AuthService.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.saleservice.repository.IUserRepository;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    IUserRepository userRepository;
    @Override
    public String findKeylimitByUserId(Long id) {
        return userRepository.findKeylimitByUserId(id);
    }
}
