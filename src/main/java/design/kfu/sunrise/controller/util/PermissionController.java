package design.kfu.sunrise.controller.util;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.service.access.AccountAccessService;
import design.kfu.sunrise.service.access.PermissionService;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;
import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
@RestController
@RequestMapping(value = "/v1")
public class PermissionController {

    @Autowired
    private AccountAccessService accountAccessService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PermissionService permissionService;


    @Operation(description = "Can account create club", summary = "Returns boolean")
    @PermitAll
    @GetMapping("/clubs/account")
    public boolean canCreateClub(@AuthenticationPrincipal(expression = "account") Account account){
        return account != null && accountAccessService.hasAccessToCreateClub(account);
    }

    @Operation(description = "Can account edit club", summary = "Returns boolean")
    @PermitAll
    @GetMapping("/club/{club_id}/account")
    public boolean canEditClub(@AuthenticationPrincipal(expression = "account") Account account, @PathVariable("club_id") Club club){
        return account != null && accountAccessService.hasAccessToEditClub(account, club);
    }

    @Operation(description = "Can account create category", summary = "Returns boolean")
    @PermitAll
    @GetMapping("/categories/account")
    public boolean canCreateCategory(@AuthenticationPrincipal(expression = "account") Account account){
        return account != null && accountAccessService.hasAccessToCreateCategory(account);
    }

    @Operation(description = "Can account edit category", summary = "Returns boolean")
    @PermitAll
    @GetMapping("/category/{category_id}/account")
    public boolean canEditCategory(@AuthenticationPrincipal(expression = "account") Account account, @PathVariable("category_id") Category category){
        return account != null && accountAccessService.hasAccessToCreateCategory(account);
    }


    @Operation(description = "Can account pay for club", summary = "Returns true or status of last payment for club")
    @PreAuthorize("@access.hasAccessToPayForClub(#account, #club)")
    @PostMapping("/club/{club_id}")
    public String canPay(@PathVariable("club_id") Club club, @AuthenticationPrincipal(expression = "account") Account account) {
        Optional<Payment> paymentOptional = paymentService.findPaymentForClub(account, club);
        if (paymentOptional.isPresent()) {
            return paymentOptional.get().getStatus().getName();
        } else {
            return "true";
        }
    }
}
