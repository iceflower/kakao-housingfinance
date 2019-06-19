package com.kakao.housingfinance.security;

import com.kakao.housingfinance.exception.InvalidTokenRequestException;
import com.kakao.housingfinance.model.CustomUserDetails;
import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Long jwtExpirationInMs;

    @Value("${app.jwt.claims.refresh.name}")
    private String jwtClaimRefreshName;

    /**
     * principal 객체를 가지고 토큰을 새롭게 만드는 메소드. (스프링 시큐리티의 UserDetails 객체를 활용하여 생성함)
     */
    public String generateToken(CustomUserDetails customUserDetails) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(customUserDetails.getId()))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * principal 객체를 가지고 토큰을 새롭게 만드는 메소드. (userId만 가지고 만듬)
     */
    public String generateTokenFromUserId(Long userId) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    /**
     * JWT로 캡슐화된 사용자 ID를 반환하는 메소드
     */
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * 유효하고, 올바른 형태이며, 만료되지 않은 토큰인지 검증하는 메소드
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("올바르지 않은 JWT 서명");
            throw new InvalidTokenRequestException("JWT", authToken, "올바르지 않은 서명입니다.");
        } catch (MalformedJwtException ex) {
            logger.error("올바르지 않은 JWT 토큰");
            throw new InvalidTokenRequestException("JWT", authToken, "올바르지 않은 jwt 토큰입니다.");
        } catch (ExpiredJwtException ex) {
            logger.error("만료된 JWT 토큰");
            throw new InvalidTokenRequestException("JWT", authToken, "만료된 토큰입니다. 토큰을 리프레시 하세요.");
        } catch (UnsupportedJwtException ex) {
            logger.error("지원하지 않는 JWT 토큰입니다.");
            throw new InvalidTokenRequestException("JWT", authToken, "지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims 문자열이 비어있습니다.");
            throw new InvalidTokenRequestException("JWT", authToken, "잘못된 토큰 인수입니다.");
        }
    }

    /**
     * JWT 만료시간 반환용 메소드.
     * 이것으로 토큰 리프레시 여부 판별
     */
    public Long getExpiryDuration() {
        return jwtExpirationInMs;
    }
}