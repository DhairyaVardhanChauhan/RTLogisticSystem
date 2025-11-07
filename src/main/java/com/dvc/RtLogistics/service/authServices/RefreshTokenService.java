package com.dvc.RtLogistics.service.authServices;

import com.dvc.RtLogistics.dto.Token;
import com.dvc.RtLogistics.entity.RefreshToken;
import com.dvc.RtLogistics.entity.User;
import com.dvc.RtLogistics.repository.RefreshTokenRepository;
import com.dvc.RtLogistics.repository.UserRepository;
import com.dvc.RtLogistics.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
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

    public Token generateNewTokenPairs(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(this::validateRefreshToken) // Step 1: validate token
                .map(validToken -> {
                    String userName = validToken.getUserInfo().getUserName();

                    // Step 2: generate new tokens
                    String accessToken = jwtUtils.generateToken(userName);
                    RefreshToken newRefreshToken = generateToken(userName);

                    // Step 3: return new token pair
                    return Token.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken.getToken())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found!"));
    }

    public RefreshToken validateRefreshToken(RefreshToken token){
        if(token != null && token.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " expired");
        }
        return token;
    }
}
