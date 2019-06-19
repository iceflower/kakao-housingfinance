package com.kakao.housingfinance.model.payload;

import com.kakao.housingfinance.validation.annotation.MatchPassword;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@MatchPassword
@ApiModel(value = "비밀번호 초기화 요청", description = "비밀번호 초기화 요청용 payload")
public class PasswordResetRequest {

    @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "새로운 패스워드", required = true, allowableValues = "공백이 아닌 문자열")
    private String password;

    @NotBlank(message = "재입력 비밀번호는 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "비밀번호 프로퍼티와 값이 같지 않을 경우 예외 발생", required = true,
            allowableValues = "공백이 아닌 문자열이면서 비밀번호 프로퍼티와 값이 같아야 함.")
    private String confirmPassword;

    @NotBlank(message = "이메일 초기화 메일에 제공된 토큰이 들어와야 합니다.")
    @ApiModelProperty(value = "이메일로 전달받은 초기화 토큰", required = true, allowableValues = "공백이 아닌 문자열")
    private String token;

    public PasswordResetRequest() {
    }

    public PasswordResetRequest(String password, String confirmPassword, String token) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.token = token;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
