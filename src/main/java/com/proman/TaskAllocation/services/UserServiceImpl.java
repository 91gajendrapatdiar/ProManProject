package com.proman.TaskAllocation.services;

import com.proman.TaskAllocation.config.JwtProvider;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);

        return findUserEmail(email);
    }

    @Override
    public User findUserEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw  new Exception("user not found");
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        // Check if the userId is null
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found with ID: " + userId);
        }
        return optionalUser.get();
    }


    @Override
    public User updateUsersProjectSize(User user, int number) {
        user.setProjectSize(user.getProjectSize()+number);

        return  userRepository.save(user);
    }
}
