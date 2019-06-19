package com.kakao.housingfinance.event.listener;

import com.kakao.housingfinance.event.OnRegenerateEmailVerificationEvent;
import com.kakao.housingfinance.exception.MailSendException;
import com.kakao.housingfinance.model.User;
import com.kakao.housingfinance.model.token.EmailVerificationToken;
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
public class OnRegenerateEmailVerificationListener implements ApplicationListener<OnRegenerateEmailVerificationEvent> {

    private static final Logger logger = Logger.getLogger(OnRegenerateEmailVerificationListener.class);
    private final MailService mailService;

    @Autowired
    public OnRegenerateEmailVerificationListener(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * 회원가입 완료 이후 발동되는 이메일 인증메일 발송 이벤트
     */
    @Override
    @Async
    public void onApplicationEvent(OnRegenerateEmailVerificationEvent onRegenerateEmailVerificationEvent) {
        resendEmailVerification(onRegenerateEmailVerificationEvent);
    }

    /**
     * 인증메일 발송 및 이메일 인증 토큰을 DB에 저장하는 메소드
     */
    private void resendEmailVerification(OnRegenerateEmailVerificationEvent event) {
        User user = event.getUser();
        EmailVerificationToken emailVerificationToken = event.getToken();
        String recipientAddress = user.getEmail();

        String emailConfirmationUrl =
                event.getRedirectUrl().queryParam("token", emailVerificationToken.getToken()).toUriString();
        try {
            mailService.sendEmailVerification(emailConfirmationUrl, recipientAddress);
        } catch (IOException | TemplateException | MessagingException e) {
            logger.error(e);
            throw new MailSendException(recipientAddress, "회원가입 직후 발송되는 인증메일");
        }
    }

}
