package com.kakao.housingfinance.service;

import com.kakao.housingfinance.exception.TokenRefreshException;
import com.kakao.housingfinance.model.UserDevice;
import com.kakao.housingfinance.model.payload.DeviceInfo;
import com.kakao.housingfinance.model.token.RefreshToken;
import com.kakao.housingfinance.repository.UserDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    @Autowired
    public UserDeviceService(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    /**
     * 사용자 ID로 디바이스 정보 조회
     */
    public Optional<UserDevice> findByUserId(Long userId) {
        return userDeviceRepository.findByUserId(userId);
    }

    /**
     * 리프레시 토큰을 활용하여 디바이스 정보 조회
     */
    public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
        return userDeviceRepository.findByRefreshToken(refreshToken);
    }

    /**
     * 새로운 사용자 디바이스 정보를 생성함
     */
    public UserDevice createUserDevice(DeviceInfo deviceInfo) {
        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceId(deviceInfo.getDeviceId());
        userDevice.setDeviceType(deviceInfo.getDeviceType());
        userDevice.setNotificationToken(deviceInfo.getNotificationToken());
        userDevice.setRefreshActive(true);
        return userDevice;
    }

    /**
     * 리프레시 토큰을 검증함.
     */
    void verifyRefreshAvailability(RefreshToken refreshToken) {
        UserDevice userDevice = findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(), "해당 디바이스의 정보와 맞는 토큰이 존재하지 않습니다. 다시 로그인 해 주세요."));

        if (!userDevice.getRefreshActive()) {
            throw new TokenRefreshException(refreshToken.getToken(), "리프레시가 불가능한 디바이스입니다. 다른 디바이스로 로그인을 해 주시기 바랍니다.");
        }
    }
}
