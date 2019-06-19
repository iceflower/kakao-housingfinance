package com.kakao.housingfinance.repository;

import com.kakao.housingfinance.model.User;
import com.kakao.housingfinance.model.token.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    //Optional<EmailVerificationToken> findByUser(User user);
    Optional<EmailVerificationToken> findById(long idd);
    Optional<EmailVerificationToken> findByUserId(long userId);
}
