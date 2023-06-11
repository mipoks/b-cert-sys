package design.kfu.sunrise.domain.dto.account;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.service.StaticService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountVDTO {
    private Long id;
    private String role;
    private String email;
    private String created;
    private String telegramNickname;

    public static AccountVDTO from(Account account) {
        return AccountVDTO.builder()
                .email(StaticService.getAnonymousService().hideEmail(account.getEmail()))
                .id(account.getId())
                .role(account.getRole().name())
                .build();
    }

    public static AccountVDTO meFrom(Account account) {
        return AccountVDTO.builder()
                .email(account.getEmail())
                .id(account.getId())
                .telegramNickname(account.getAccountInfo().getTelegramNickname())
                .created(StaticService.getInstantConverter().convert(account.getCreated()))
                .role(account.getRole().name())
                .build();
    }

}
