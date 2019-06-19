package com.kakao.housingfinance.controller;

import com.kakao.housingfinance.annotation.CurrentUser;
import com.kakao.housingfinance.event.OnGenerateResetLinkEvent;
import com.kakao.housingfinance.event.OnRegenerateEmailVerificationEvent;
import com.kakao.housingfinance.event.OnUserAccountChangeEvent;
import com.kakao.housingfinance.event.OnUserRegistrationCompleteEvent;
import com.kakao.housingfinance.exception.*;
import com.kakao.housingfinance.model.CustomUserDetails;
import com.kakao.housingfinance.model.payload.*;
import com.kakao.housingfinance.model.token.EmailVerificationToken;
import com.kakao.housingfinance.model.token.RefreshToken;
import com.kakao.housingfinance.security.JwtTokenProvider;
import com.kakao.housingfinance.service.AuthService;
import com.kakao.housingfinance.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Api(value = "인증 REST API")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    //private UserService userService;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider tokenProvider, ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.tokenProvider = tokenProvider;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/checkInUse/email")
    @ApiOperation(value = "이메일 중복여부 체크")
    public ResponseEntity checkEmailInUse(@ApiParam(value = "중복체크 대상 이메일") @RequestParam("email") String email) {
        Boolean emailExists = authService.emailAlreadyExists(email);
        return ResponseEntity.ok(new ApiResponse(emailExists.toString(), true));
    }

    @ApiOperation(value = "사용자명 중복여부 체크")
    @GetMapping("/checkInUse/username")
    public ResponseEntity checkUsernameInUse(@ApiParam(value = "중복체크 대상 계정명") @RequestParam("username") String username) {
        Boolean usernameExists = authService.usernameAlreadyExists(username);
        return ResponseEntity.ok(new ApiResponse(usernameExists.toString(), true));
    }


    @PostMapping("/signin")
    @ApiOperation(value = "사용자 로그인. 성공시 인증토큰 리턴")
    public ResponseEntity authenticateUser(@ApiParam(value = "로그인 요청 payload") @Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authService.authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("[" + loginRequest + "] 로 로그인을 실패하였습니다."));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("로그인 성공한 사용자명 [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    String jwtToken = authService.generateToken(customUserDetails);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new UserLoginException("리프레시 토큰 리턴에 실패하였습니다.: [" + loginRequest + "]"));
    }

    @PostMapping("/signup")
    @ApiOperation(value = "사용자 등록. 등록 성공시 인증메일 발송")
    public ResponseEntity registerUser(@ApiParam(value = "등록요청 payload") @Valid @RequestBody RegistrationRequest registrationRequest) {

        return authService.registerUser(registrationRequest)
                .map(user -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(user, urlBuilder);
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    logger.info("사용자 등록 완료됨. : " + user);
                    return ResponseEntity.ok(new ApiResponse("사용자 등록이 완료되었습니다. 인증을 위해 메일을 확인하여 주세요.", true));
                })
                .orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(), "고객 객체의 정보가 유실되었습니다."));
    }

    @PostMapping("/password/resetlink")
    @ApiOperation(value = "비밀번호 초기화 링크 요청 API. 요청 성공시 비밀번호 초기화 링크를 담은 메일 발송")
    public ResponseEntity resetLink(@ApiParam(value = "비밀번호 초기화 링크 요청 payload") @Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {

        return authService.generatePasswordResetToken(passwordResetLinkRequest)
                .map(passwordResetToken -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
                    OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
                            urlBuilder);
                    applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
                    return ResponseEntity.ok(new ApiResponse("비밀번호 초기화 링크 전송 성공", true));
                })
                .orElseThrow(() -> new PasswordResetLinkException(passwordResetLinkRequest.getEmail(), "유효한 토큰 생성 실패"));
    }

    @PostMapping("/password/reset")
    @ApiOperation(value = "비밀번호 리셋 요청 받는 API, 비밀번호 초기화 요청 후 받은 인증메일로부터 접근하는 API")
    public ResponseEntity resetPassword(@ApiParam(value = "비밀번호 초기화 요청 payload") @Valid @RequestBody PasswordResetRequest passwordResetRequest) {

        return authService.resetPassword(passwordResetRequest)
                .map(changedUser -> {
                    OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "비밀번호 변경 성공",
                            "비밀번호 변경 성공");
                    applicationEventPublisher.publishEvent(onPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiResponse("비밀번호 변경 성공", true));
                })
                .orElseThrow(() -> new PasswordResetException(passwordResetRequest.getToken(), "Error in resetting password"));
    }


    @GetMapping("/registrationConfirmation")
    @ApiOperation(value = "등록 확인 메일을 유효성 검증 토큰과 함께 고객의 이메일로 전송함.")
    public ResponseEntity confirmRegistration(@ApiParam(value = "사용자의 이메일로 전송했던 토큰") @RequestParam("token") String token) {

        return authService.confirmEmailRegistration(token)
                .map(user -> ResponseEntity.ok(new ApiResponse("유저 등록 검증 성공", true)))
                .orElseThrow(() -> new InvalidTokenRequestException("이메일 유효성 검증 토큰", token, "유효성 검증에 실패하였습니다. "));
    }

    @GetMapping("/resendRegistrationToken")
    @ApiOperation(value = "등록 인증메일 재전송 요청 api")
    public ResponseEntity resendRegistrationToken(@ApiParam(value = "이미 발급한 이메일 인증 토큰") @RequestParam("token") String existingToken) {

        EmailVerificationToken newEmailToken = authService.recreateRegistrationToken(existingToken)
                .orElseThrow(() -> new InvalidTokenRequestException("이메일 인증 토큰", existingToken, "이미 등록한 유저의"));

        return Optional.ofNullable(newEmailToken.getUser())
                .map(registeredUser -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new OnRegenerateEmailVerificationEvent(registeredUser, urlBuilder, newEmailToken);
                    applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);
                    return ResponseEntity.ok(new ApiResponse("인증메일 재전송 성공", true));
                })
                .orElseThrow(() -> new InvalidTokenRequestException("이메일 인증 토큰", existingToken, "유저 정보를 찾을 수 없습니다. 재인증이 거부되었습니다."));
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "만료된 인증토큰을 리프레시 토큰을 활용하여 재발급 처리 해주는 API")
    public ResponseEntity refreshJwtToken(@ApiParam(value = "리프레시 토큰 payload") @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        return authService.refreshJwtToken(tokenRefreshRequest)
                .map(updatedToken -> {
                    String refreshToken = tokenRefreshRequest.getRefreshToken();
                    logger.info("새로운 auth 토큰을 생성하였습니다." + updatedToken);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(), "리프레시 도중 예상치 못한 오류가 생겼습니다. 로그아웃 후 다시 로그인 해 주세요."));
    }


}
