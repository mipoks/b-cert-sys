package design.kfu.sunrise.service.mail.context;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.util.ActivationCode;
import design.kfu.sunrise.exception.ErrorType;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.service.account.AccountService;
import design.kfu.sunrise.service.mail.EmailService;
import design.kfu.sunrise.service.mail.util.ActivationCodeService;
import design.kfu.sunrise.service.mail.util.EmailContextGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author Daniyar Zakiev
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {


    @Autowired
    private ActivationCodeService activationCodeService;

    @Autowired
    private EmailContextGenerator emailContextGenerator;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    //ToDo проверить Async
    @Async
    @Override
    public CompletableFuture<Boolean> sendEmail(AbstractEmailContext emailContext) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(emailContext.getContext());
        String emailContent = templateEngine.process(emailContext.getTemplateLocation(), context);

        mimeMessageHelper.setTo(emailContext.getTo());
        mimeMessageHelper.setSubject(emailContext.getSubject());
        mimeMessageHelper.setFrom(emailContext.getFrom());
        mimeMessageHelper.setText(emailContent, true);
        emailSender.send(message);
        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

    public static final String INCORRECT_CODE = "Вы перешли по недействительной ссылке";
    public static final String EMAIL_CONFIRMED = "Ваш Email подвержден";

    @Override
    public String confirmEmail(String code) {
        String result;
        Optional<ActivationCode> activationCode = activationCodeService.findByCode(code);
        if (activationCode.isEmpty()) {
            result = INCORRECT_CODE;
        } else {
            Account account = accountService.findOrThrow(activationCode.get().getAccountId());
            account.getAccountInfo().setEmailConfirmed(true);
            accountService.updateAccount(account);
            result = EMAIL_CONFIRMED;
            activationCodeService.delete(activationCode.get());
        }
        return result;
    }

    @Override
    @Async(value = "threadPoolTaskExecutor")
    public CompletableFuture<Boolean> sendConfirmationEmail(Account account) {
        ActivationCode activationCode = generateAndSaveActivationCode(account);
        AbstractEmailContext abstractEmailContext = emailContextGenerator.generateConfirmationContext(account, activationCode);
        try {
            return sendEmail(abstractEmailContext);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw Exc.gen(ErrorType.UNEXPECTED_ERROR,
                    "Не удалось отправить письмо с кодом подтверждения ", e.getCause());
        }
    }

    private ActivationCode generateAndSaveActivationCode(Account account) {
        ActivationCode activationCode = activationCodeService.generate(account);
        return activationCodeService.save(activationCode);
    }
    @Async(value = "threadPoolTaskExecutor")
    @Override
    public CompletableFuture<Boolean> sendForgotPasswordEmail(String email) {
        Account account = accountService.getAccountByEmail(email);
        ActivationCode activationCode = generateAndSaveActivationCode(account);

        AbstractEmailContext abstractEmailContext = emailContextGenerator.generatePasswordForgotContext(account, activationCode);
        try {
            return sendEmail(abstractEmailContext);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw Exc.gen(ErrorType.UNEXPECTED_ERROR,
                    "Не удалось отправить письмо с кодом подтверждения ", e.getCause());
        }
    }
}
