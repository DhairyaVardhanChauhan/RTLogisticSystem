package com.dvc.RtLogistics.service.authServices;

import com.dvc.RtLogistics.dto.UserInfoDto;
import com.dvc.RtLogistics.entity.RefreshToken;
import com.dvc.RtLogistics.entity.User;
import com.dvc.RtLogistics.repository.RefreshTokenRepository;
import com.dvc.RtLogistics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken generateToken(String username) {
        User extractedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = refreshTokenRepository.findByUserInfo(extractedUser).orElse(generateFreshToken(extractedUser));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private RefreshToken generateFreshToken(User extractedUser){
        return RefreshToken.builder()
                .userInfo(extractedUser)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(Duration.ofDays(15)))
                .build();
    }
}
