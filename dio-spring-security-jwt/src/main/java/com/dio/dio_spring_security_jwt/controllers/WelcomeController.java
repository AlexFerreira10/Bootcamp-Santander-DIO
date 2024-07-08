package com.dio.dio_spring_security_jwt.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping
    public String welcome(){
        return "Welcome to My Spring Boot Web API";
    }

    //http://localhost:8080/users
    @GetMapping("/users")
    //@PreAuthorize("hasAnyRole('MANAGERS','USERS')")
    public String users() {
        return "Authorized user";
    }

    //http://localhost:8080/managers
    @GetMapping("/managers")
   // @PreAuthorize("hasRole('MANAGERS')")
    public String managers() {
        return "Authorized manager";
    }
}