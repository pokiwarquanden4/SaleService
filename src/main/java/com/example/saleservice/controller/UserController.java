package com.example.saleservice.controller;

import com.example.saleservice.constant.URLConstance;
import com.example.saleservice.entity.User;
import com.example.saleservice.model.ResponseObject;
import com.example.saleservice.model.ResponseUtils;
import com.example.saleservice.service.FTPClient.FTPReaderService;
import com.example.saleservice.service.RateLimit.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.saleservice.repository.UserRepo;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RateLimitService rateLimitService;
    @Autowired
    private FTPReaderService ftpReaderService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;

    @PostMapping(URLConstance.createURL)
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> createStudent(@RequestBody User userInfo){
        System.out.println("in");
        //RateLimit
        boolean checkRate = rateLimitService.startRateLimit(
                URLConstance.createURL,
                URLConstance.createURL_LimitRequest,
                URLConstance.createURL_LimitSecond
        );
        if (!checkRate){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(0, "Query User Reach Limit", "")
            );
        }

        User user = new User(userInfo.getUserName(),userInfo.getPassWord(), userInfo.getRole());
        System.out.println(userInfo.getUserName() + userInfo.getPassWord() + userInfo.getRole());
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(0, "Create User Success", "")
        );
    }

    @GetMapping(URLConstance.getByIdURL)
    public ResponseEntity<ResponseObject> findById(@PathVariable int id){
        //RateLimit
        boolean checkRate = rateLimitService.startRateLimit(
                URLConstance.getByIdURL,
                URLConstance.getByIdURL_LimitRequest,
                URLConstance.getByIdURL_LimitSecond
        );
        if (!checkRate){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(0, "Query User Reach Limit", "")
            );
        }

        User user = userRepo.findUserByID(id);
        return user != null ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(1, "Query User Successfully", user)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(0, "Query User Failed", "")
                );
    }

    @GetMapping(URLConstance.getAllURL)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseObject> findAll(){
        //RateLimit
        boolean checkRate = rateLimitService.startRateLimit(
                URLConstance.getAllURL,
                URLConstance.getAllURL_LimitRequest,
                URLConstance.getAllURL_LimitSecond
        );
        if (!checkRate){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(0, "Query User Reach Limit", "")
            );
        }

        List<User> user = userRepo.findAll();
        return !user.isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(1, "Query All User Successfully", user)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(0, "Query All User Failed", "")
                );
    }

    @GetMapping(URLConstance.readFileURL)
    public ResponseEntity<ResponseObject> readFile(@PathVariable String serial) throws IOException {
        //RateLimit
        boolean checkRate = rateLimitService.startRateLimit(
                URLConstance.readFileURL,
                URLConstance.readFileURL_LimitRequest,
                URLConstance.readFileURL_LimitSecond
        );
        if (!checkRate){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(0, "Read File Reach Limit", "")
            );
        }

        String base64String = ftpReaderService.convertToBase64(serial);

        return base64String != null ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(1, "Convert PNG Successfully", new ResponseUtils(base64String))
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(0, "Convert PNG Failed", "")
                );
    }
}
