package com.revature.auth.utils;

import com.revature.auth.dtos.UserDTO;
import com.revature.auth.entities.User;

import java.util.HashSet;
import java.util.Set;

public class DtoUtil {

    public static UserDTO userToDTO(User user){
        return new UserDTO(user);
    }

    // A method for making userDtos from user objects
    public static Set<UserDTO> usersToDTOs(Set<User> users){
        Set<UserDTO> userDTOs = new HashSet<>();
        for (User user : users){
            userDTOs.add(new UserDTO(user));
        }
        return userDTOs;
    }
}
