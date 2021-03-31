package com.revature.auth.RepoTests;

import com.revature.auth.entities.User;
import com.revature.auth.repos.UserRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest

public class UserRepoTest {

    @Autowired
    UserRepo userRepo;

    @Test
    @Order(1)
    void create_user(){
        User testUser = new User();
        testUser.setEmail("test@email.com");
        testUser.setStatus("pending");

        userRepo.save(testUser);

        Assertions.assertNotEquals(0, testUser.getUserId());

        System.out.println(testUser);
    }

    @Test
    @Order(2)
    void get_all_users(){
        Set<User> users = new HashSet<>();
        userRepo.findAll().forEach(users::add);

        Assertions.assertTrue(users.size() > 0);

        System.out.println(users);
    }

    @Test
    @Order(3)
    void find_user_by_email(){
        User testUser = userRepo.findByEmail("test@email.com");
        Assertions.assertNotNull(testUser);
        Assertions.assertEquals("test@email.com", testUser.getEmail());

        System.out.println(testUser);
    }

    @Test
    @Order(4)
    void find_users_by_status(){
        Set<User> pendingUsers = new HashSet<>();
        userRepo.findAllByStatus("pending").forEach(pendingUsers::add);

        Assertions.assertTrue(pendingUsers.size() > 0);

        pendingUsers.forEach((User testUser) -> {
            Assertions.assertEquals("pending", testUser.getStatus());
        });

        System.out.println(pendingUsers);
    }

    @Test
    @Order(5)
    void change_user_status(){
        User testUser = userRepo.findByEmail("test@email.com");
        testUser.setStatus("pending_creation");
        userRepo.save(testUser);

        Assertions.assertEquals("pending_creation", testUser.getStatus());

        System.out.println(testUser);
    }

    @Test
    @Order(6)
    void change_user_role(){
        User testUser = userRepo.findByEmail("test@email.com");
        testUser.setRole("admin");
        userRepo.save(testUser);

        Assertions.assertEquals("admin", testUser.getRole());

        System.out.println(testUser);
    }

    @Test
    @Order(7)
    void delete_test_user(){
        User testUser = userRepo.findByEmail("test@email.com");
        userRepo.delete(testUser);

        testUser = userRepo.findByEmail("test@email.com");
        Assertions.assertNull(testUser);
    }


}
