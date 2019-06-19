package com.kakao.housingfinance.service;

import com.kakao.housingfinance.annotation.CurrentUser;
import com.kakao.housingfinance.exception.ResourceNotFoundException;
import com.kakao.housingfinance.exception.UserLogoutException;
import com.kakao.housingfinance.model.CustomUserDetails;
import com.kakao.housingfinance.model.Role;
import com.kakao.housingfinance.model.User;
import com.kakao.housingfinance.model.UserDevice;
import com.kakao.housingfinance.model.payload.LogOutRequest;
import com.kakao.housingfinance.model.payload.RegistrationRequest;
import com.kakao.housingfinance.model.payload.TokenRefreshRequest;
import com.kakao.housingfinance.model.token.RefreshToken;
import com.kakao.housingfinance.repository.UserRepository;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserDeviceService userDeviceService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleService roleService, UserDeviceService userDeviceService, RefreshTokenService refreshTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userDeviceService = userDeviceService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * username으로 사용자 검색
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 이메일로 사용자 검색
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * ID값으로 사용자 조회
     */
    public Optional<User> findById(Long Id) {
        return userRepository.findById(Id);
    }

    /**
     * 사용자 정보 저장
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * 이메일 중복체크
     */
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 유저명 중복체크
     */
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    /**
     * 요청받은 새로운 사용자 생성
     */
    public User createUser(RegistrationRequest registerRequest) {
        User newUser = new User();
        Boolean isNewUserAsAdmin = registerRequest.getRegisterAsAdmin();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setUsername(registerRequest.getEmail());
        newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin));
        newUser.setActive(true);
        newUser.setEmailVerified(false);
        return newUser;
    }

    /**
     * 새로운 사용자에게 role 부여
     *
     * @return list of roles for the new user
     */
    private Set<Role> getRolesForNewUser(Boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<Role>(roleService.findAll());
        if (!isToBeMadeAdmin) {
            newUserRoles.removeIf(Role::isAdminRole);
        }
        logger.info("사용자의 Role 설정 완료: " + newUserRoles);
        return newUserRoles;
    }

    /**
     * 요청받은 디바이스의 정보를 삭제함 (디바이스 정보 삭제 == 로그아웃)
     */
    public void logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutRequest logOutRequest) {
        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();

        System.out.println("currentUser id====>>"+ currentUser.getId());
        System.out.println("deviceId====>>"+ deviceId);

        UserDevice userDevice = userDeviceService.findByUserId(currentUser.getId())
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "유효하지 않은 디바이스 ID 값입니다."));

        logger.info("요청하신 디바이스의 정보를 성공적으로 삭제하였습니다. [" + userDevice + "]");
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
    }

    /**
     * 요청받은 디바이스의 정보를 삭제함 (디바이스 정보 삭제 == 로그아웃)
     */
    public void logoutUser(@CurrentUser CustomUserDetails currentUser, String refreshTokenStr) {

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr)
                .orElseThrow(()->new UserLogoutException(refreshTokenStr,"존재하지 않는 리프레시 토큰 값입니다."));

        UserDevice userDevice  = userDeviceService.findByRefreshToken(refreshToken)
                .orElseThrow(()->new UserLogoutException(refreshTokenStr,"존재하지 않는 리프레시 토큰 값입니다."));


        if(userDevice.getUser().getId() == currentUser.getId()) throw new UserLogoutException(userDevice.getDeviceId(), "유효하지 않은 디바이스 ID 값입니다.");

        logger.info("요청하신 디바이스의 정보를 성공적으로 삭제하였습니다. [" + userDevice + "]");
        refreshTokenService.deleteById(refreshToken.getId());

        //refreshTokenService.deleteByRefreshToken(refreshTokenStr);
    }
}
