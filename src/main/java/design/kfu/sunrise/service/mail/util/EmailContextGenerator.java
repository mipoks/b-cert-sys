package design.kfu.sunrise.service.mail.util;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.util.ActivationCode;
import design.kfu.sunrise.service.mail.context.AbstractEmailContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniyar Zakiev
 */
@Service
public class EmailContextGenerator {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${site.domain}")
    private String domain;

    public AbstractEmailContext generateConfirmationContext(Account account, ActivationCode activationCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", account.getEmail());
        map.put("verificationURL", domain + "v1/account/email?code=" + activationCode.getCode());
        return AbstractEmailContext.builder()
                .emailLanguage("en")
                .to(account.getEmail())
                .from(fromEmail)
                .subject("Email Confirmation")
                .templateLocation("EmailConfirmationTemplate.html")
                .context(map)
                .build();
    }

    public AbstractEmailContext generatePasswordForgotContext(Account account, ActivationCode activationCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", account.getEmail());
        map.put("verificationURL", domain + "v1/account/forgot/change?code=" + activationCode.getCode());
        return AbstractEmailContext.builder()
                .emailLanguage("en")
                .to(account.getEmail())
                .from(fromEmail)
                .subject("Reset Password")
                .templateLocation("ForgotPasswordTemplate.html")
                .context(map)
                .build();
    }
}
