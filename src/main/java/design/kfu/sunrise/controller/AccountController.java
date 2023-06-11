package design.kfu.sunrise.controller;

import design.kfu.sunrise.domain.dto.account.AccountCDTO;
import design.kfu.sunrise.domain.dto.account.AccountPartnerCDTO;
import design.kfu.sunrise.domain.dto.account.AccountUpdateDTO;
import design.kfu.sunrise.domain.dto.account.AccountVDTO;
import design.kfu.sunrise.domain.dto.club.ClubVDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import design.kfu.sunrise.service.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(description = "Register account", summary = "Returns registered account with DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "Account is already exist")
    })
    @Limit(url = "/account/add", limitType = LimitType.PER_DAY, requestCount = 4)
    @PermitAll
    @PostMapping("/account")
    public AccountVDTO addAccount(@RequestBody @Valid AccountCDTO accountCDTO) {
        log.info("accountDTO {}",accountCDTO);
        return accountService.addAccount(accountCDTO);
    }

    @Operation(description = "Get account info", summary = "Returns account's info")
    @PermitAll
    @GetMapping("/account/me")
    public AccountVDTO getMyAccount(@AuthenticationPrincipal(expression = "account") Account account) {
        AccountVDTO d = AccountVDTO.meFrom(account);
        log.info("accountDTO {}",d.toString());
        return AccountVDTO.meFrom(account);
    }

    @Operation(description = "Get account's clubs", summary = "Returns account's clubs")
    @PermitAll
    @GetMapping("/account/{account_id}/clubs")
    public Set<ClubVDTO> getAccountClubs(@PathVariable("account_id") Account account) {
        return accountService.getAccountClubs(account).stream().map(ClubVDTO::from).collect(Collectors.toSet());
    }

    @Operation(description = "Get account info", summary = "Returns account's info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PermitAll
    @GetMapping("/account/{account_id}")
    public AccountVDTO getAccount(@PathVariable("account_id") Account account) {
        return AccountVDTO.from(account);
    }

    @Operation(description = "Register partner's account (for ADMIN role)", summary = "Returns account's info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "Account is already exist")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/account/partner")
    public AccountVDTO addPartnerAccount(@RequestBody @Valid AccountPartnerCDTO accountPartnerCDTO) {
        return AccountVDTO.from(accountService.addPartnerAccount(accountPartnerCDTO));
    }

    @Operation(description = "Update account info", summary = "Returns account's info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Email already in use")
    })
    @Limit(url = "/account/update", limitType = LimitType.PER_DAY, requestCount = 10)
    @PutMapping("/account")
    public AccountVDTO updateAccount(@RequestBody @Valid AccountUpdateDTO accountUpdateDTO, @AuthenticationPrincipal(expression = "account") Account account) {
        if (!accountUpdateDTO.getEmail().equals(account.getEmail())) {
            account.setEmail(accountUpdateDTO.getEmail());
            account.getAccountInfo().setEmailConfirmed(false);
        }
        account.getAccountInfo().setPhone(accountUpdateDTO.getPhone());
        account.getAccountInfo().setTelegramNickname(accountUpdateDTO.getTelegramNickname());
        if (account.getHashPassword().equals(passwordEncoder.encode(accountUpdateDTO.getOldPassword()))) {
            account.setHashPassword(passwordEncoder.encode(accountUpdateDTO.getNewPassword()));
        }
        return AccountVDTO.from(accountService.updateAccount(account));
    }

    @Operation(description = "Ban account to write comments", summary = "Returns boolean")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@access.hasAccessToBan(#account)")
    @PutMapping("/account/{account_id}/ban/comment")
    public Boolean banAccountToWriteComment(@RequestBody Boolean banned, @PathVariable("account_id") Account toBan, @AuthenticationPrincipal(expression = "account") Account account) {
        toBan.getAccountInfo().setBannedWriteComment(banned);
        accountService.updateAccount(toBan);
        return true;
    }

    @Operation(description = "Is account banned to write comments", summary = "Returns boolean")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@access.hasAccessToBan(#account)")
    @GetMapping("/account/{account_id}/ban/comment")
    public Boolean isAccountBannedToWriteComment(@PathVariable("account_id") Account toBan, @AuthenticationPrincipal(expression = "account") Account account) {
        return toBan.getAccountInfo().isBannedWriteComment();
    }

}
