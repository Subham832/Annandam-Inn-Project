package com.annandam_inn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


@Configuration
public class SecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    //Now we will build here a Special Class
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{

        httpSecurity.csrf().disable().cors().disable();
        httpSecurity.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        httpSecurity.authorizeHttpRequests()
        .anyRequest().permitAll();



//        .requestMatchers("/api/v1/users/addUser","/api/v1/users/login")//This 2 url everyone can access by using permitAll()
//        .permitAll()
//
//         //This URL can be access only by an Admin
//        .requestMatchers("/api/v1/countries/addCountry").hasRole("ADMIN")
//
//        //For Giving the Access to Both the User in a "profile" URL
//        .requestMatchers("/api/v1/users/profile").hasAnyRole("ADMIN", "USER")
//
//       //Any Other URL form the Project other then this should be Authenticated/Secure rest URL.
//        .anyRequest().authenticated();

        return httpSecurity.build();
    }
}
