package com.dvc.RtLogistics.controller;

import com.dvc.RtLogistics.dto.LoginDetails;
import com.dvc.RtLogistics.dto.Token;
import com.dvc.RtLogistics.dto.UserInfoDto;
import com.dvc.RtLogistics.entity.RefreshToken;
import com.dvc.RtLogistics.service.authServices.CustomUserDetailsService;
import com.dvc.RtLogistics.service.authServices.RefreshTokenService;
import com.dvc.RtLogistics.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @GetMapping("/signup")
    public ResponseEntity signUp(@RequestBody UserInfoDto requestBody){
        try{
            return customUserDetailsService.validateAndSignUpUser(requestBody);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginDetails loginDetails){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDetails.getUsername(),loginDetails.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.generateToken(loginDetails.getUsername());
            return new ResponseEntity<>(Token.builder().accessToken(jwtUtils.generateToken(loginDetails.getUsername())).refreshToken(refreshToken.getToken()).build(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("User doesn't have an account!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
