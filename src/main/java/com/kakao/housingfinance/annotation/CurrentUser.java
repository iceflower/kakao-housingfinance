package com.kakao.housingfinance.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * 커스텀 어노테이션. 현재 인증받은 유저의 정보를 가져오기 위한 어노테이션.
 * AuthenticationPrincipal 과  동일하게 작동
 * AuthenticationPrincipal과 스프링 시큐리티에 의존적인 어노테이션.
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
public @interface CurrentUser {
}
