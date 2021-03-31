package com.revature.auth.services;

import com.revature.auth.entities.User;

import java.util.Set;

public interface UserService {

    public User register(User user);
    public Set<User> getUsersByStatus(String status);

    public User approveUser(User user);
    public User approveUserById(int userId);

    public User denyUser(User user);
    public User denyUserById(int userId);

    public User setPassword(User user);
    public User setPasswordById(int userId, String password);

    public User findUserByUsernameAndPassword();

}
