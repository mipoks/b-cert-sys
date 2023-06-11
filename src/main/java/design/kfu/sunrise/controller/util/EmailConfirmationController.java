package design.kfu.sunrise.controller.util;

import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.service.account.AccountService;
import design.kfu.sunrise.service.mail.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.concurrent.CompletableFuture;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@RestController
@RequestMapping("v1")
public class EmailConfirmationController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AccountService accountService;


    @Operation(description = "Confirm email", summary = "Returns string with confirmation result")
    @Limit(url = "/account/email/get", limitType = LimitType.PER_DAY, requestCount = 5)
    @GetMapping("/account/email")
    public String confirmEmail(@RequestParam(name = "activate") @NotNull String code) {
        return emailService.confirmEmail(code);
    }

    @Operation(description = "Send confirmation email", summary = "Returns boolean. Long operation")
    @Limit(url = "/account/email/post", limitType = LimitType.PER_DAY, requestCount = 4)
    @PostMapping("/account/email")
    public CompletableFuture<Boolean> sendConfirmationEmail(@AuthenticationPrincipal(expression = "account") Account account) {
        return emailService.sendConfirmationEmail(account);
    }

    @Operation(description = "Forgot password of account", summary = "Send email if account exists")
    @Limit(url = "/account/forgot", limitType = LimitType.PER_DAY, requestCount = 3)
    @PermitAll
    @PostMapping("/account/forgot")
    public CompletableFuture<Boolean> sendForgotPasswordEmail(@RequestBody @Email String email) {
        return emailService.sendForgotPasswordEmail(email);
    }

    @Operation(description = "Change password if forgot", summary = "Returns true if password changed successfully")
    @Limit(url = "/account/forgot/change", limitType = LimitType.PER_DAY, requestCount = 5)
    @PermitAll
    @PostMapping("/account/forgot/change")
    public Boolean changePassword(@RequestBody @NotNull @Size(min = 6, max = 25) String password, @RequestParam(name = "code") @NotNull String code) {
        return accountService.changePassword(code, password);
    }

}
