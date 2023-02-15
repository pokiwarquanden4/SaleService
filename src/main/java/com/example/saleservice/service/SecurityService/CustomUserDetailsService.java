package com.example.saleservice.service.SecurityService;

import com.example.saleservice.config.CustomUserDetails.CustomUserDetails;
import com.example.saleservice.constant.URLConstance;
import com.example.saleservice.entity.User;
import com.example.saleservice.model.ResponseObject;
import com.example.saleservice.repository.UserRepo;
import com.example.saleservice.service.RateLimit.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RateLimitService rateLimitService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = null;
        boolean checkRate = rateLimitService.startRateLimit(
                username,
                URLConstance.login_LimitRequest,
                URLConstance.login_LimitSecond
        );
        if (checkRate){
            user = userRepo.findByUserName(username);
        }else {
            user = userRepo.findByUserName(null);
        }
        return user.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
    }
}
