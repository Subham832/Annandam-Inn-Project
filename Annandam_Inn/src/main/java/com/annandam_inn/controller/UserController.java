package com.annandam_inn.controller;

import com.annandam_inn.dto.LoginDto;
import com.annandam_inn.dto.PropertyUserDto;
import com.annandam_inn.dto.TokenResponse;
import com.annandam_inn.entity.PropertyUser;
import com.annandam_inn.service.UserSerivce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserSerivce userSerivce;

    public UserController(UserSerivce userSerivce) {
        this.userSerivce = userSerivce;
    }


    //http://localhost:8080/api/v1/users/addUser
    @PostMapping("/addUser") //Registration Part
    public ResponseEntity<String> addUser(@RequestBody PropertyUserDto propertyUserDto) {

        PropertyUser propertyUser = userSerivce.addUser(propertyUserDto);
        if (propertyUser != null) {
            return new ResponseEntity<>("Registration is Successfull", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something Went Worng", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    //http://localhost:8080/api/v1/users/login
    @PostMapping("/login") //Login Part
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        String token = userSerivce.verifyLogin(loginDto);
        if (token != null) {

            TokenResponse tokenResponse = new TokenResponse(); //dto layer "TokenResponse".
            tokenResponse.setToken(token);

            //return new ResponseEntity<>("User Signed In Successfully", HttpStatus.OK); //1

            // return new ResponseEntity<>(token, HttpStatus.OK); //2

            return new ResponseEntity<>(tokenResponse, HttpStatus.OK); //Object //3
        }
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED); //String

    }

    //http://localhost:8080/api/v1/users/profile
    @GetMapping("/profile") //this is to allocate the CurrentUser LoggedIn Details
    public PropertyUser getCurrentUserProfile(@AuthenticationPrincipal PropertyUser propertyUser) { //When we access this URL this will automatically now from that URL extract the Session ID and it will check the session ID which spring boot which it has generated eariler and if that matches it means it is a currentUser interacting.

        return propertyUser; // return back the user object.

    }
}