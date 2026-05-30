package com.portal.job.service;

import com.portal.job.payload.AuthResponse;
import com.portal.job.payload.LoginRequest;
import com.portal.job.payload.SignupRequest;

public interface AuthService {

    AuthResponse signup(SignupRequest req) ;

    AuthResponse login(LoginRequest req) ;

}
