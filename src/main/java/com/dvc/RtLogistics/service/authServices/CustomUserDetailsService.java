package com.dvc.RtLogistics.service.authServices;

import com.dvc.RtLogistics.entity.User;
import com.dvc.RtLogistics.repository.UserReporitory;
import com.dvc.RtLogistics.sercured.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserReporitory userReporitory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userReporitory.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User does not exist!"));
        return new CustomUserDetails(user);
    }
}
