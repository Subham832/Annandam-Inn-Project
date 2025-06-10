package com.annandam_inn.service;

import com.annandam_inn.dto.LoginDto;
import com.annandam_inn.dto.PropertyUserDto;
import com.annandam_inn.entity.PropertyUser;
import com.annandam_inn.repository.PropertyUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSerivce {

    private PropertyUserRepository propertyUserRepository;
    private JWTService jwtService;

    public UserSerivce(PropertyUserRepository propertyUserRepository, JWTService jwtService) {
        this.propertyUserRepository = propertyUserRepository;
        this.jwtService = jwtService;
    }

    public PropertyUser addUser(PropertyUserDto propertyUserDto){ //Registration Part

        PropertyUser user = new PropertyUser();
        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setUsername(propertyUserDto.getUsername());
        user.setEmail(propertyUserDto.getEmail());
       // user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(), BCrypt.gensalt(10)));
        user.setUserRole(propertyUserDto.getUserRole());
//        user.setUserRole("ROLE_USER");

        PropertyUser savedUser = propertyUserRepository.save(user);
        return savedUser;

    }


    public String verifyLogin(LoginDto loginDto) { //Excepted value //Login Part.

        Optional<PropertyUser> opUser = propertyUserRepository.findByUsername(loginDto.getUsername());

        if (opUser.isPresent()){
            PropertyUser propertyUser = opUser.get(); //It will convert the Optional object to Entity Object "propertyUser". In this Actual Value is there in "propertyUser".
            //Now we have to Compare the Values
           if(BCrypt.checkpw(loginDto.getPassword(), propertyUser.getPassword())){

               return jwtService.generateToken(propertyUser);

            }
        }
        return null;

    }
}