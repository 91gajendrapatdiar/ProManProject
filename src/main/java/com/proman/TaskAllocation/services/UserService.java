package com.proman.TaskAllocation.services;

import com.proman.TaskAllocation.model.User;

public interface UserService {

    User findUserProfileByJwt(String jwt) throws  Exception;


    User findUserEmail(String email) throws  Exception;

    User findUserById(Long userId) throws  Exception;

    User updateUsersProjectSize(User user, int number);
}
