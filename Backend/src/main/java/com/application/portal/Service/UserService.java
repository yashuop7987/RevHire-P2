package com.application.portal.Service;

import com.application.portal.Model.User;
import com.application.portal.Repository.UserRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByUsername(String username)
    {
       return userRepository.findByUsername(username);
    }

}
