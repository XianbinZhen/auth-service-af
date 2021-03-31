package com.revature.auth.repos;

import com.revature.auth.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Component
@Repository
public interface UserRepo extends CrudRepository<User, Integer> {

    User findByEmail(String email);

    Set<User> findAllByStatus(String status);
}
