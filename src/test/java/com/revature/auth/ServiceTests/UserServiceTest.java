package com.revature.auth.ServiceTests;

import com.revature.auth.entities.User;
import com.revature.auth.repos.UserRepo;
import com.revature.auth.services.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;


// TODO UserServiceTest tests need to be created & implemented.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional
public class UserServiceTest {


    @Autowired
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepo userRepo;

    @Test
    @Order(1)
    void register_user() {
        Mockito.when(userRepo.save(any())) // when the userRepo.save() method is called with "any()" parameter
            .thenAnswer((Answer<User>) invocation -> {
                User u = invocation.getArgument(0); // get the first argument/parameter sent to the save() method

                // do whatever logic the save() method would normally do
                u.setUserId(1);

                // return what you expect the save() method to return
                return u;
            });


        // Here we do test logic
        User testUser = new User();
        testUser.setEmail("test@email.com");
        testUser.setRole("trainer");

        testUser = userService.register(testUser);
        Assertions.assertEquals("pending", testUser.getStatus());

        System.out.println(testUser);

    }
}
