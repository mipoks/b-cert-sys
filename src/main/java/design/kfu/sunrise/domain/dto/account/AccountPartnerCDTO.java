package design.kfu.sunrise.domain.dto.account;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Company;
import design.kfu.sunrise.domain.model.embedded.AccountInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Daniyar Zakiev
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPartnerCDTO {
    @NotNull
    @Size(min = 6, max = 25)
    private String password;
    @Email
    private String email;
    @NotNull
    @Size(min = 8, max = 15)
    private String phone;

    private String companyName;
    private String companyCity;
    private String companyAddress;

    public static Account toAccount(AccountPartnerCDTO accountCDTO) {
        return Account.builder()
                .accountInfo
                        (AccountInfo.builder()
                                .phone(accountCDTO.getPhone())
                                .build())
                .hashPassword(accountCDTO.getPassword())
                .email(accountCDTO.getEmail())
                .partnerInfo(Company.builder()
                        .address(accountCDTO.companyAddress)
                        .name(accountCDTO.companyName)
                        .city(accountCDTO.companyCity)
                        .build())
                .build();
    }
}
