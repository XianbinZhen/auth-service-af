package com.revature.auth.controllers;

import com.revature.auth.aspects.Authorized;
import com.revature.auth.entities.User;
import com.revature.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// TODO Annotation & Aspect for security need to be created, implemented, & added to the appropriate controllers.
// TODO AuthorizationController mappings need to be created & implemented.
@Component
@RestController
@CrossOrigin
public class AuthorizationController {

    @Autowired
    UserService userService;

//    @Authorized
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        user = userService.register(user);
        return ResponseEntity.status(201).body(user);
    }

}
