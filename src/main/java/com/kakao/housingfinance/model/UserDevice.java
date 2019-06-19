package com.kakao.housingfinance.model;

import com.kakao.housingfinance.model.audit.DateAudit;
import com.kakao.housingfinance.model.constant.DeviceType;
import com.kakao.housingfinance.model.token.RefreshToken;

import javax.persistence.*;

@Entity(name = "USER_DEVICE")
public class UserDevice extends DateAudit {

    @Id
    @Column(name = "USER_DEVICE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_device_seq")
    @SequenceGenerator(name = "user_device_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "DEVICE_TYPE")
    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;


    /**
     * TODO: 필요시 각 디바이스별로 토큰 생성 및 유지할 것
     */
    @Column(name = "NOTIFICATION_TOKEN")
    private String notificationToken;

    /**
     * TODO: 안드로이드/iOS 네이티브 앱의 경우 기기의 GUID를 바로 획득하여 받아오면 된다.
     * TODO: PC웹 및 브라우저는 그렇게 할 수 없으므로, 다른 방법으로 GUID를 생성한 후 받아야 한다.
     * TODO: 참고할 것 (https://andywalpole.me/blog/140739/using-javascript-create-guid-from-users-browser-information)
     */
    @Column(name = "DEVICE_ID", nullable = false)
    private String deviceId;



    /**
     * TODO: 안드로이드/iOS 네이티브 앱의 경우 기기의 refreshToken을 바로 획득하여 받아오면 된다.
     * TODO: PC웹 및 브라우저는 그렇게 할 수 없으므로, 다른 방법으로 GUID를 생성한 후 받아야 한다.
     * TODO: 참고할 것 (https://andywalpole.me/blog/140739/using-javascript-create-guid-from-users-browser-information)
     */
    @OneToOne(optional = false, mappedBy = "userDevice")
    private RefreshToken refreshToken;

    @Column(name = "IS_REFRESH_ACTIVE")
    private Boolean isRefreshActive;

    public UserDevice() {
    }

    public UserDevice(Long id, User user, DeviceType deviceType, String notificationToken, String deviceId,
                      RefreshToken refreshToken, Boolean isRefreshActive) {
        this.id = id;
        this.user = user;
        this.deviceType = deviceType;
        this.notificationToken = notificationToken;
        this.deviceId = deviceId;
        this.refreshToken = refreshToken;
        this.isRefreshActive = isRefreshActive;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public DeviceType getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
    public String getNotificationToken() {
        return notificationToken;
    }
    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public RefreshToken getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
    public Boolean getRefreshActive() {
        return isRefreshActive;
    }
    public void setRefreshActive(Boolean refreshActive) {
        isRefreshActive = refreshActive;
    }
}
