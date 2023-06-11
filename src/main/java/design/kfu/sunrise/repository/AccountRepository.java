package design.kfu.sunrise.repository;

import design.kfu.sunrise.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> getAccountByEmail(String login);
    Optional<Account> getAccountByAccountInfo_Phone(String phone);
}
