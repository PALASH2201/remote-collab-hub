package com.remotehub.userservice.service;

import com.remotehub.userservice.entity.User;
import com.remotehub.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(email);
        if(user != null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserEmail())
                    .password(user.getUserPassword())
                    .roles(String.valueOf(user.getRole()))
                    .build();
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
