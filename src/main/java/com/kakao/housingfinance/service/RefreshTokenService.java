package com.kakao.housingfinance.service;

import com.kakao.housingfinance.exception.TokenRefreshException;
import com.kakao.housingfinance.model.token.RefreshToken;
import com.kakao.housingfinance.repository.RefreshTokenRepository;
import com.kakao.housingfinance.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.token.refresh.duration}")
    private Long refreshTokenDurationMs;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 리프레시 토큰을 이전에 생성한 토큰으로 조회.
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * 저장된 리프레시 토큰을 업데이트함
     */
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }


    /**
     * 토큰을 생성하고 반환함
     */
    public RefreshToken createRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(Util.generateRandomUuid());
        refreshToken.setRefreshCount(0L);
        return refreshToken;
    }

    /**
     * 토큰이 만료되었는지 서버시간 기준으로 검증함.
     */
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new TokenRefreshException(token.getToken(), "만료된 토큰이므로 새로 발급받아야 합니다.");
        }
    }

    /**
     * 리프레시 토큰을 사용자의 디바이스 정보를 활용하여 삭제함
     */
    public void deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
    }

    /**
     * 리프레시 횟수를 기록함. 추후 검증 목적으로 사용시 유용함
     */
    public void increaseCount(RefreshToken refreshToken) {
        refreshToken.incrementRefreshCount();
        save(refreshToken);
    }
}
