package com.portal.job.service;

import com.portal.job.dto.response.UserResponse;
import com.portal.job.modal.User;
import com.portal.job.payload.UpdateUserRequest;

import java.util.List;

public interface UserService {

    User getUserByEmail(String email) ;

    User getUserById(Long id) ;

    List<User> getAllUsers();

    UserResponse updateProfile(String email, UpdateUserRequest req) ;


    //admin actions
    UserResponse suspendUser(Long id) ;
    UserResponse activateUser(Long id) ;

    UserResponse deleteUser(Long id) ;


}
