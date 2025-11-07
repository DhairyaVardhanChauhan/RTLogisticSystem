package com.dvc.RtLogistics.controller;

import com.dvc.RtLogistics.dto.RefreshTokenDto;
import com.dvc.RtLogistics.dto.Token;
import com.dvc.RtLogistics.entity.RefreshToken;
import com.dvc.RtLogistics.service.authServices.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class TokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    Token generateAccessToken(@RequestBody RefreshTokenDto requestBody){
        return refreshTokenService.generateNewTokenPairs(requestBody.getToken());
    }

}
