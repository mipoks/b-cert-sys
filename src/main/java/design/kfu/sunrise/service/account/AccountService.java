package design.kfu.sunrise.service.account;

import design.kfu.sunrise.domain.dto.account.AccountCDTO;
import design.kfu.sunrise.domain.dto.account.AccountPartnerCDTO;
import design.kfu.sunrise.domain.dto.account.AccountVDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface AccountService {
    @Transactional
    AccountVDTO addAccount(AccountCDTO accountCDTO);

    @Transactional
    Account addPartnerAccount(AccountPartnerCDTO accountPartnerCDTO);

    @Transactional
    Account getAccount(Long accountId);

    Account findOrThrow(Long accountId);

    Account updateAccount(Account account);

    //Возвращает все созданные этим пользователем клубы
    Set<Club> getCreatedClubs(Account account);

    Account getAccountByEmail(String name);

    Boolean changePassword(String code, String password);

    //Возвращает все клубы, в которых есть данный участник
    Set<Club> getAccountClubs(Account account);
}
