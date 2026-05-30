package com.portal.job.service.impl;

import com.portal.job.domain.UserRole;
import com.portal.job.domain.UserStatus;
import com.portal.job.exception.BadRequestException;
import com.portal.job.exception.UnauthorizedException;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public AuthResponse signup(SignupRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("An account with this email already exists.");
        }

        if (req.getRole() == UserRole.ROLE_ADMIN) {
            throw new BadRequestException("Cannot sign up as Admin.");
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
                savedUser.getEmail(),
                null,
                List.of(() -> savedUser.getRole().name())
        );
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
    public AuthResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new UnauthorizedException("Your account has been suspended. Please contact support.");
        }

        if (user.getStatus() == UserStatus.DELETED) {
            throw new UnauthorizedException("This account no longer exists.");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of(() -> user.getRole().name())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String jwt = jwtProvider.generateToken(authentication, user.getId());

        AuthResponse res = new AuthResponse();
        res.setTitle("Welcome back, " + user.getFullName());
        res.setMessage("Login successful");
        res.setJwt(jwt);
        res.setUser(UserMapper.toDTO(user));
        return res;
    }
}