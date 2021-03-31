package com.revature.auth.services;

import com.revature.auth.entities.User;
import com.revature.auth.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;


// TODO UserServiceImpl methods need to be implemented.
@Component
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepo userRepo;

    @Override
    public User register(User user) {
        user.setStatus("pending");
        user = userRepo.save(user);
        return user;
    }

    @Override
    public Set<User> getUsersByStatus(String status) {
        return null;
    }

    @Override
    public User approveUser(User user) {
        return null;
    }

    @Override
    public User approveUserById(int userId) {
        return null;
    }

    @Override
    public User denyUser(User user) {
        return null;
    }

    @Override
    public User denyUserById(int userId) {
        return null;
    }

    @Override
    public User setPassword(User user) {
        return null;
    }

    @Override
    public User setPasswordById(int userId, String password) {
        return null;
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        return null;
    }
}
