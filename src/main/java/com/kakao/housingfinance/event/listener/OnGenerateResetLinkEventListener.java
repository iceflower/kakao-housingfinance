package com.kakao.housingfinance.event.listener;

import com.kakao.housingfinance.event.OnGenerateResetLinkEvent;
import com.kakao.housingfinance.exception.MailSendException;
import com.kakao.housingfinance.model.PasswordResetToken;
import com.kakao.housingfinance.model.User;
import com.kakao.housingfinance.service.MailService;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
public class OnGenerateResetLinkEventListener implements ApplicationListener<OnGenerateResetLinkEvent> {

    private static final Logger logger = Logger.getLogger(OnGenerateResetLinkEventListener.class);
    private final MailService mailService;

    @Autowired
    public OnGenerateResetLinkEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * 비밀번호 초기화 요청을 받았을 시 작동하는 비밀번호 초기화 링크 생성 이벤트
     */
    @Override
    @Async
    public void onApplicationEvent(OnGenerateResetLinkEvent onGenerateResetLinkMailEvent) {
        sendResetLink(onGenerateResetLinkMailEvent);
    }

    /**
     * 비밀번호 초기화 메일을 비밀번호 초기화 링크를 담아 전송하는 메소드
     */
    private void sendResetLink(OnGenerateResetLinkEvent event) {
        PasswordResetToken passwordResetToken = event.getPasswordResetToken();
        User user = passwordResetToken.getUser();
        String recipientAddress = user.getEmail();
        String emailConfirmationUrl = event.getRedirectUrl().queryParam("token", passwordResetToken.getToken())
                .toUriString();
        try {
            mailService.sendResetLink(emailConfirmationUrl, recipientAddress);
        } catch (IOException | TemplateException | MessagingException e) {
            logger.error(e);
            throw new MailSendException(recipientAddress, "비밀번호 초기화 인증메일");
        }
    }

}
