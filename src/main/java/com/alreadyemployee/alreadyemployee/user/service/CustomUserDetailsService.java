package com.alreadyemployee.alreadyemployee.user.service;

import com.alreadyemployee.alreadyemployee.user.entity.CustomUserDetails;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import com.alreadyemployee.alreadyemployee.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일을 찾을 수 없습니다."));

        return new CustomUserDetails(user);

    }
}


