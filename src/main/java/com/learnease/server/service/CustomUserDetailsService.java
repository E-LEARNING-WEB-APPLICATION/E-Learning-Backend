package com.learnease.server.service;

import com.learnease.server.model.UserAuth;
import com.learnease.server.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository authRepository;

    // this is the method that will get the user from the db and provide to DaoAuthenticationProvider
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Invoke dao's method
        System.out.println("******Load user by username ******");

        UserAuth myUser = authRepository.findByEmail(username)
                        .orElseThrow(()-> new UsernameNotFoundException("Email doesn't Exist"));
        System.out.println("Current User : " + myUser.toString());

        return myUser;
    }
}
