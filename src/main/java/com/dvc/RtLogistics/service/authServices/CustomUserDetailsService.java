package com.dvc.RtLogistics.service.authServices;

import com.dvc.RtLogistics.dto.Token;
import com.dvc.RtLogistics.dto.UserInfoDto;
import com.dvc.RtLogistics.entity.RefreshToken;
import com.dvc.RtLogistics.entity.User;
import com.dvc.RtLogistics.entity.UserRole;
import com.dvc.RtLogistics.repository.RolesRepository;
import com.dvc.RtLogistics.repository.UserRepository;
import com.dvc.RtLogistics.sercured.CustomUserDetails;
import com.dvc.RtLogistics.utils.Constants;
import com.dvc.RtLogistics.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final RolesRepository rolesRepository;
    private final Constants constants = new Constants();
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User does not exist!"));
        return new CustomUserDetails(user);
    }

    public ResponseEntity validateAndSignUpUser(UserInfoDto requestBody) {

            Optional<User> user = userRepository.findByUserName(requestBody.getUsername());
            UserRole role =  rolesRepository.findByName(Constants.USER).orElseThrow(()-> new RuntimeException("Role not found!"));
            if(user.isPresent()){
                return new ResponseEntity<>("Already Exists", HttpStatus.BAD_REQUEST);
            }
            else{
                User user1 = User.builder()
                        .userId(UUID.randomUUID().toString())
                            .userName(requestBody.getUsername())
                            .password(passwordEncoder.encode(requestBody.getPassword()))
                        .email(requestBody.getEmail())
                        .phone(requestBody.getPhoneNumber())
                        .roles(Set.of(role))
                            .build();
                    userRepository.save(user1);

                Token token = Token.builder()
                        .refreshToken(refreshTokenService.generateToken(requestBody.getUsername()).getToken())
                        .accessToken(jwtUtils.generateToken(requestBody.getUsername()))
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(token);
            }

    }
}
