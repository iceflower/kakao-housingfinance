package com.kakao.housingfinance.service;

import com.kakao.housingfinance.exception.InvalidTokenRequestException;
import com.kakao.housingfinance.model.User;
import com.kakao.housingfinance.model.constant.TokenStatus;
import com.kakao.housingfinance.model.token.EmailVerificationToken;
import com.kakao.housingfinance.repository.EmailVerificationTokenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationTokenService {

    private static final Logger logger = Logger.getLogger(EmailVerificationTokenService.class);
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Value("${app.token.email.verification.duration}")
    private Long emailVerificationTokenExpiryDuration;

    @Autowired
    public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    /**
     * 이메일 인증 토큰을 생성하고 저장함. 사용자 이메일 인증시 사용할 예정임.
     */
    public void createVerificationToken(User user, String token) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(token);
        emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        emailVerificationToken.setUser(user);
        emailVerificationToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
        logger.info("이메일 인증 토큰을 생성하였습니다. [" + emailVerificationToken + "]");
        emailVerificationTokenRepository.save(emailVerificationToken);
    }

    /**
     * 새로운 이메일 인증 토큰 생성
     */
    public EmailVerificationToken updateExistingTokenWithNameAndExpiry(EmailVerificationToken existingToken) {
        existingToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        existingToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
        logger.info("이메일 인증 토큰을 수정하였씁니다. [" + existingToken + "]");
        return save(existingToken);
    }

    /**
     * 이메일 인증을 위해 토큰을 찾음
     */
    public Optional<EmailVerificationToken> findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token);
    }

    /**
     * 이메일 인증 토큰을 저장
     */
    public EmailVerificationToken save(EmailVerificationToken emailVerificationToken) {
        return emailVerificationTokenRepository.save(emailVerificationToken);
    }

    /**
     * UUID로 새로운 이메일 인증 토큰을 생성함
     */
    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * 만료가 되었는지 안 되었는지 검증함(서버시간 기준)
     */
    public void verifyExpiration(EmailVerificationToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new InvalidTokenRequestException("이메일 인증 토큰", token.getToken(), "만료된 토큰이므로 새롭게 생성해야 합니다.");
        }
    }

}
