package design.kfu.sunrise.security.details;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired private AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("Loading account details login  is{}", username);
    Account account =
        accountRepository
                .getAccountByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    return new UserDetailsImpl(account);
  }
}
