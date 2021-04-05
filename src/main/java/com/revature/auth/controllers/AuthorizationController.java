package com.revature.auth.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.revature.auth.aspects.Authorized;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.revature.auth.dtos.DecodedJwtDTO;
import com.revature.auth.dtos.JwtDTO;
import com.revature.auth.dtos.UserDTO;
import com.revature.auth.entities.User;
import com.revature.auth.services.UserService;
import com.revature.auth.utils.DtoUtil;
import com.revature.auth.utils.EmailUtil;
import com.revature.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


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
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user){
        UserDTO userDTO = new UserDTO(userService.register(user));

        Set<UserDTO> adminDTOs = DtoUtil.usersToDTOs(userService.getUsersByRole("admin"));
        EmailUtil.notifyAdmins(adminDTOs);

        return ResponseEntity.status(201).body(userDTO);
    }

    @Authorized
    @GetMapping("/resolve")
    public ResponseEntity<Set<UserDTO>> getPendingUsers(){
        Set<UserDTO> userDTOS = DtoUtil.usersToDTOs(userService.getUsersByStatus("pending"));
        return ResponseEntity.status(200).body(userDTOS);
    }

    @Authorized
    @PatchMapping("/resolve/{userId}")
    public ResponseEntity<UserDTO> updateStatusById(@RequestBody User user,@PathVariable int userId){
        UserDTO userDTO = new UserDTO(userService.getUserById(userId));
        String status = user.getStatus();
        switch (status){
            case "denied":
                return ResponseEntity.status(200).body(new UserDTO(userService.denyUserById(userId)));
            case "approved":
                String jwt = JwtUtil.generate(user.getEmail(), user.getRole(), user.getUserId());
                EmailUtil.notifyUser(userDTO, "https://assignforce.revature.com/password?id="+jwt);
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
    public ResponseEntity<JwtDTO> login(@RequestBody UserDTO userDTO){
        System.out.println(userDTO);
        if(!(userDTO==null)){
            userDTO = new UserDTO(userService.findUserByUsernameAndPassword(userDTO.getEmail(), userDTO.getPassword()));
            String jwtData = JwtUtil.generate(userDTO.getEmail(),userDTO.getRole(), userDTO.getUserId());
            JwtDTO jwt = new JwtDTO(jwtData);
            return ResponseEntity.status(200).body(jwt);
        }
        throw new IllegalArgumentException("Invalid username and password provided.");
    }

    @PostMapping("/verify")
    public ResponseEntity<DecodedJwtDTO> verify(@RequestBody String jwt) throws JWTDecodeException {
        return ResponseEntity.status(200).body(new DecodedJwtDTO(JwtUtil.isValidJWT(jwt)));
    }

    @GetMapping("/health")
    public boolean health() {
        return true;
    }

}
