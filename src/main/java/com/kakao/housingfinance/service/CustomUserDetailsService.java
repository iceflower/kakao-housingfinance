package com.kakao.housingfinance.service;

import com.kakao.housingfinance.model.CustomUserDetails;
import com.kakao.housingfinance.model.User;
import com.kakao.housingfinance.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> dbUser = userRepository.findByEmail(email);
        logger.info(email+" 로 사용자 계정 정보["+ dbUser+"] (을)를 꺼냄" );
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("[" + email+"] 로 사용자 정보를 찾을 수 없습니다."));
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> dbUser = userRepository.findById(id);
        logger.info(id+" 로 사용자 계정 정보["+ dbUser+"] (을)를 꺼냄" );
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("["+id+"] 로 사용자 정보를 찾을 수 없습니다."));
    }
}
