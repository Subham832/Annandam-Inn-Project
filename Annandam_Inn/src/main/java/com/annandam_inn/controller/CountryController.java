package com.annandam_inn.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    //http://localhost:8080/api/v1/countries/addCountry --> This URL work only after the LogIn
    @PostMapping("/addCountry")
    public String addCountry(){
        return "done...";
    }

}
