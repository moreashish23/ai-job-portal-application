package com.portal.job.service.impl;

import com.portal.job.domain.UserRole;
import com.portal.job.domain.UserStatus;
import com.portal.job.mapper.UserMapper;
import com.portal.job.modal.User;
import com.portal.job.payload.AuthResponse;
import com.portal.job.payload.LoginRequest;
import com.portal.job.payload.SignupRequest;
import com.portal.job.repository.UserRepository;
import com.portal.job.security.CustomUserDetailsService;
import com.portal.job.security.JwtProvider;
import com.portal.job.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder  passwordEncoder;

    private final JwtProvider jwtProvider;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public AuthResponse signup(SignupRequest req) throws Exception {

        if(userRepository.existsByEmail(req.getEmail())){
            throw new Exception("Email already exists" + req.getEmail());
        }

        if(req.getRole() == UserRole.ROLE_ADMIN){
            throw new Exception("Cannot sign up as Admin");
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .phone(req.getPhone())
                .lastLogin(LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword() );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication, savedUser.getId());

        AuthResponse res = new AuthResponse();
        res.setTitle("Welcome " + savedUser.getFullName());
        res.setMessage("Signup successful");
        res.setJwt(jwt);
        res.setUser(UserMapper.toDTO(savedUser));

        return res;
    }

    @Override
    public AuthResponse login(LoginRequest req) throws Exception {

        Authentication authentication = authenticate(
                req.getEmail(), req.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(req.getEmail());

        String jwt = jwtProvider.generateToken(authentication, user.getId());

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse res = new AuthResponse();
        res.setTitle("Welcome back" + user.getFullName());
        res.setMessage("Login successful");
        res.setJwt(jwt);
        res.setUser(UserMapper.toDTO(user));

        return res;
    }

    private Authentication authenticate(String  email, String password) throws Exception {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if(userDetails == null){
            throw new Exception("User not found with email: " + email);
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new Exception("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
