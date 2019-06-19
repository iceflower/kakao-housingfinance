package com.kakao.housingfinance.service;

import com.kakao.housingfinance.exception.InvalidTokenRequestException;
import com.kakao.housingfinance.model.PasswordResetToken;
import com.kakao.housingfinance.repository.PasswordResetTokenRepository;
import com.kakao.housingfinance.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${app.token.password.reset.duration}")
    private Long expiration;

    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    /**
     * 생성한 비밀번호 리셋 토큰을 저장
     */
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    /**
     * DB에 있는 토큰을 가져옴
     */
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    /**
     * Creates and returns a new password token to which a user must be associated
     */
    public PasswordResetToken createToken() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        String token = Util.generateRandomUuid();
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(expiration));
        return passwordResetToken;
    }

    /**
     * 패스워드 초기화 토큰이 만료되었는지 체크하는 메소드
     */
    public void verifyExpiration(PasswordResetToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new InvalidTokenRequestException("패스워드 초기화 토큰", token.getToken(),
                    "이 만료되었습니다. 비밀번호 초기화 요청을 다시 해 주시기 바랍니다.");
        }
    }
}
