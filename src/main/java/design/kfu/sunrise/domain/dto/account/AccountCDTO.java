package design.kfu.sunrise.domain.dto.account;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.embedded.AccountInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCDTO {
    @NotNull
    @Size(min = 6, max = 25)
    private String password;
    @Email
    private String email;
    @NotNull
    @Size(min = 8, max = 15)
    private String phone;

    @AssertTrue
    private boolean useTerms;

    public static Account toAccount(AccountCDTO accountCDTO) {
        return Account.builder()
                .accountInfo
                        (AccountInfo.builder()
                                .phone(accountCDTO.getPhone())
                                .bannedWriteComment(false)
                                .build())
                .hashPassword(accountCDTO.getPassword())
                .email(accountCDTO.getEmail())
                .build();
    }
}
