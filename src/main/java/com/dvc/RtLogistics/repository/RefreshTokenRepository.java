package com.dvc.RtLogistics.repository;

import com.dvc.RtLogistics.entity.RefreshToken;
import com.dvc.RtLogistics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByUserInfo(User user);

    Optional<RefreshToken> findByToken(String token);
}
