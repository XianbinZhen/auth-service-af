package com.revature.auth.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.revature.auth.aspects.Authorized;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.revature.auth.dtos.DecodedJwtDTO;
import com.revature.auth.dtos.UserDTO;
import com.revature.auth.entities.User;
import com.revature.auth.services.UserService;
import com.revature.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@Component
@RestController
//@CrossOrigin
public class AuthorizationController {
//    `POST /register`
//     - `GET /resolve` <-- admin views registration requests to resolve//pending users
//- `PATCH /resolve/{userId}` <-- admin resolves registration//return user
//- `PATCH /password` <-- trainer uses to complete account after approval//
//- `POST /login`
//            - `POST /verify` <-- internal use only, called by our other services (returns a boolean with whether a JWT is valid or not)
    @Autowired
    UserService userService;

//    @Authorized
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user){
        UserDTO userDTO = new UserDTO(userService.register(user));
        return ResponseEntity.status(201).body(userDTO);
    }
    @GetMapping("/resolve")
    public ResponseEntity<Set<UserDTO>> getPendingUsers(){
        Set<UserDTO> userDTOS = new HashSet<>();
        Set<User> users = userService.getUsersByStatus("pending");
        for (User u : users) {
            userDTOS.add(new UserDTO(u));
        }
        return ResponseEntity.status(200).body(userDTOS);
    }
    @PatchMapping("/resolve/{userId}")
    public ResponseEntity<UserDTO> updateStatusById(@RequestBody User user,@PathVariable int userId){
        String status = user.getStatus();
        switch (status){
            case "denied":
                return ResponseEntity.status(200).body(new UserDTO(userService.denyUserById(userId)));
            case "approved":
                return ResponseEntity.status(200).body(new UserDTO(userService.approveUserById(userId)));
            default:
                throw new IllegalArgumentException("status not found");
        }
    }
    @PatchMapping("/password")
    public ResponseEntity<UserDTO> setPassword(@RequestBody User user){
        return ResponseEntity.status(200).body(new UserDTO(userService.setPassword(user,user.getPassword())));
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        if(!(user==null)){
            UserDTO userDTO = new UserDTO(userService.findUserByUsernameAndPassword(user.getEmail(),user.getPassword()));
            String jwt = JwtUtil.generate(userDTO.getEmail(),userDTO.getRole(), userDTO.getUserId());
            return ResponseEntity.status(200).body(jwt);
        }
        return ResponseEntity.status(403).body("incorrect credentials");
    }
    @PostMapping("/verify")
    public ResponseEntity<DecodedJwtDTO> verify(@RequestBody String jwt) throws JWTDecodeException {
        return ResponseEntity.status(200).body(new DecodedJwtDTO(JwtUtil.isValidJWT(jwt)));
    }

}
