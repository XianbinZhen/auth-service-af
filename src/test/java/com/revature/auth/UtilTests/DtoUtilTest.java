package com.revature.auth.UtilTests;

import com.revature.auth.dtos.UserDTO;
import com.revature.auth.entities.User;
import com.revature.auth.services.UserService;
import com.revature.auth.utils.DtoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class DtoUtilTest {

    @Autowired
    UserService userService;

    @Test
    void users_to_dtos(){
        Set<User> users = userService.getUsersByRole("trainer");
        Set<UserDTO> userDTOs = DtoUtil.usersToDTOs(users);

        Assertions.assertNotNull(userDTOs);
        Assertions.assertTrue(users.size() == userDTOs.size());

    }
}
