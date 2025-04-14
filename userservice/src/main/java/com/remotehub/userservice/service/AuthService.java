package com.remotehub.userservice.service;

import com.remotehub.userservice.dto.request.LoginRequest;
import com.remotehub.userservice.dto.request.RegisterRequest;
import com.remotehub.userservice.entity.User;
import com.remotehub.userservice.enums.Role;
import com.remotehub.userservice.exceptions.ErrorCreatingEntry;
import com.remotehub.userservice.exceptions.ResourceNotFoundException;
import com.remotehub.userservice.repository.UserRepository;
import com.remotehub.userservice.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    public void addNewUser(RegisterRequest request){
        try{
            LocalDateTime localDateTime = LocalDateTime.now();
            User user = new User();
            user.setUserEmail(request.getEmail());
            user.setUserFullName(request.getFullName());
            user.setUserPassword(passwordEncoder.encode(request.getPassword()));
            user.setUserStatus("online");
            user.setCreatedAt(localDateTime);
            user.setLastUpdated(localDateTime);
            user.setRole(Role.USER);
            user.setCreatedTeams(new ArrayList<>());
            user.setTeamMemberships(new ArrayList<>());
            userRepository.save(user);
        } catch(Exception e){
            log.error("Could not add new user!{}", e.getMessage());
            throw new ErrorCreatingEntry("Error creating user with email "+request.getEmail());
        }
    }

    public String getLoginToken(LoginRequest loginRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            return jwtUtil.generateToken(userDetails.getUsername());
        } catch(UsernameNotFoundException e){
            log.error("Could not find user with email!{}",loginRequest.getEmail());
            throw new ResourceNotFoundException("Could not fund user with email "+loginRequest.getEmail());
        }
    }
}
