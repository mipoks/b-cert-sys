package design.kfu.sunrise.service.mail;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.service.mail.context.AbstractEmailContext;
import jakarta.mail.MessagingException;

import jakarta.mail.MessagingException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Daniyar Zakiev
 */
public interface EmailService {
    CompletableFuture<Boolean> sendEmail(AbstractEmailContext emailContext) throws MessagingException, MessagingException;

    String confirmEmail(String code);

    CompletableFuture<Boolean> sendConfirmationEmail(Account account);

    CompletableFuture<Boolean> sendForgotPasswordEmail(String email);
}
