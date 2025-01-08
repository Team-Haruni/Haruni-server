package org.haruni.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user);
    }
}
