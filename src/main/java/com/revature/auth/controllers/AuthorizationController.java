package com.revature.auth.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.revature.auth.aspects.Authorized;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.revature.auth.dtos.DecodedJwtDTO;
import com.revature.auth.dtos.JwtDTO;
import com.revature.auth.dtos.UserDTO;
import com.revature.auth.entities.User;
import com.revature.auth.exceptions.UnauthorizedException;
import com.revature.auth.services.UserService;
import com.revature.auth.utils.DtoUtil;
import com.revature.auth.utils.EmailUtil;
import com.revature.auth.utils.JwtUtil;
import com.revature.auth.utils.RandomUtil;
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
        user.setPassword(null);
        return ResponseEntity.status(201).body(userDTO);
    }

    @Authorized
    @GetMapping("/resolve")
    public ResponseEntity<Set<UserDTO>> getPendingUsers(){
        Set<UserDTO> userDTOS = DtoUtil.usersToDTOs(userService.getUsersByStatus("pending_approval"));
        for (UserDTO user : userDTOS) {
            user.setPassword(null);
        }
        return ResponseEntity.status(200).body(userDTOS);
    }

    @Authorized
    @PatchMapping("/resolve/{userId}")
    public ResponseEntity<UserDTO> updateStatusById(@RequestBody User user,@PathVariable int userId){
        UserDTO userDTO = new UserDTO(userService.getUserById(userId));
        String status = user.getStatus();
        String pass = RandomUtil.generate(30);
        userDTO.setPassword(pass);
        switch (status){
            case "denied":
                userDTO.setPassword(null);
                return ResponseEntity.status(200).body(new UserDTO(userService.denyUserById(userId)));
            case "approved":
                EmailUtil.notifyUser(userDTO, "https://ngassignforce.web.app/signin");
                UserDTO approvedUserDTO = new UserDTO(userService.approveUser(user, userDTO.getPassword()));
                approvedUserDTO.setPassword(null);
                return ResponseEntity.status(200).body(approvedUserDTO);
            default:
                throw new IllegalArgumentException("status not found");
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<JwtDTO> setPassword(@RequestBody User user, @RequestParam(name = "id", required = true) String jwtTemp){
        DecodedJWT decode = JwtUtil.isValidJWT(jwtTemp);
        user.setEmail(decode.getClaim("email").asString());
        
        UserDTO userDTO = new UserDTO(user);
        userService.setPassword(user, user.getPassword());
        String jwt = JwtUtil.generate(userDTO.getEmail(), userDTO.getRole(), userDTO.getUserId(), userDTO.getStatus());
        return ResponseEntity.status(200).body(new JwtDTO(jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@RequestBody UserDTO userDTO){
        if(userDTO!=null){
            User userCheck = userService.findUserByUsernameAndPassword(userDTO.getEmail(), userDTO.getPassword());
            if(userCheck == null) {
                throw new UnauthorizedException("Login failed");
            }
            userDTO = new UserDTO(userCheck);
            String jwtData = JwtUtil.generate(userDTO.getEmail(),userDTO.getRole(), userDTO.getUserId(), userDTO.getStatus());
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
