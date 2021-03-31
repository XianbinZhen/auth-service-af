package com.revature.auth.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.revature.auth.entities.User;
import com.revature.auth.services.UserService;
import com.revature.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

// TODO Annotation & Aspect for security need to be created, implemented, & added to the appropriate controllers.
// TODO AuthorizationController mappings need to be created & implemented.
@Component
@RestController
@CrossOrigin
public class AuthorizationController {
//    `POST /register`
//     - `GET /resolve` <-- admin views registration requests to resolve//pending users
//- `PATCH /resolve/{userId}` <-- admin resolves registration//return user
//- `PATCH /password` <-- trainer uses to complete account after approval//
//- `POST /login`
//            - `POST /verify` <-- internal use only, called by our other services (returns a boolean with whether a JWT is valid or not)
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        user = userService.register(user);
        return ResponseEntity.status(201).body(user);
    }
    @GetMapping("/resolve")
    public ResponseEntity<Set<User>> getPendingUsers(){
        Set<User> users = userService.getUsersByStatus("pending");
        return ResponseEntity.status(200).body(users);
    }
    @PatchMapping("/resolve/{userId}")
    public ResponseEntity<User> updateStatusById(@RequestBody User user,@PathVariable int userId){
        String status = user.getStatus();
        switch (status){
            case "denied":
                return ResponseEntity.status(200).body(userService.denyUserById(userId));
            case "approved":
                return ResponseEntity.status(200).body(userService.approveUserById(userId));
            default:
                throw new IllegalArgumentException("status not found");
        }
    }
    @PatchMapping("/password")
    public ResponseEntity<User> setPassword(@RequestBody User user){
        User updatedUser = userService.setPassword(user,user.getPassword());
        return ResponseEntity.status(200).body(updatedUser);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        user = userService.findUserByUsernameAndPassword(user.getEmail(),user.getPassword());
        if(!(user==null)){
            String jwt = JwtUtil.generate(user.getEmail(),user.getRole(), user.getUserId());
            return ResponseEntity.status(200).body(jwt);
        }
        return ResponseEntity.status(403).body("incorrect credentials");
    }
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(@RequestBody String jwt) throws JWTDecodeException {
        JwtUtil.isValidJWT(jwt);
        return ResponseEntity.status(200).body(true);
    }

}
