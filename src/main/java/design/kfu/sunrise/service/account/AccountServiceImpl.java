package design.kfu.sunrise.service.account;

import design.kfu.sunrise.domain.dto.account.AccountCDTO;
import design.kfu.sunrise.domain.dto.account.AccountPartnerCDTO;
import design.kfu.sunrise.domain.dto.account.AccountVDTO;
import design.kfu.sunrise.domain.event.AccountEvent;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.util.ActivationCode;
import design.kfu.sunrise.exception.ErrorType;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.mapper.AccountMapper;
import design.kfu.sunrise.repository.AccountRepository;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.service.mail.util.ActivationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {


    @Autowired
    private ApplicationEventPublisher publisher;

    @Lazy
    @Autowired
    private ClubService clubService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ActivationCodeService activationCodeService;

    /**
     * Добавляет аккаунт в бд и бросает ошибку, если такой уже существует.
     * @param accountCDTO
     * @return
     */
    @Override
    @Transactional
    public AccountVDTO addAccount(AccountCDTO accountCDTO){
        accountCDTO.setPassword(passwordEncoder.encode(accountCDTO.getPassword()));
        Account account = AccountCDTO.toAccount(accountCDTO);
        Optional<Account> accountOpt =accountRepository.getAccountByEmail(accountCDTO.getEmail());
        accountOpt.ifPresent(account1 -> {throw Exc.gen(ErrorType.ENTITY_ALREADY_EXISTS,"Аккаунт с таким email-ом уже существует");} );
        account.setRole(Account.Role.USER);
        Account saved = accountRepository.save(account);
        publisher.publishEvent(new AccountEvent(Account.class.getName(), AccountEvent.Event.CREATE.getName(), saved));
        return accountMapper.toVDTO(account);
    }

    @Override
    public Account addPartnerAccount(AccountPartnerCDTO accountPartnerCDTO) {
        accountPartnerCDTO.setPassword(passwordEncoder.encode(accountPartnerCDTO.getPassword()));
        Account account = AccountPartnerCDTO.toAccount(accountPartnerCDTO);
        account.setRole(Account.Role.PARTNER);
        Account saved = accountRepository.save(account);
        publisher.publishEvent(new AccountEvent(Account.class.getName(), AccountEvent.Event.CREATE.getName(), saved));
        return saved;
    }

    @Override
    @Transactional
    public Account getAccount(Long accountId){
        return accountRepository.getById(accountId);
    }

    @Override
    public Account findOrThrow(Long userId) {
        return accountRepository
                .findById(userId)
                .orElseThrow(Exc.sup(ErrorType.ENTITY_NOT_FOUND,"Сущность пользователя не найдена"));
    }

    @Override
    public Account updateAccount(Account account) {
        Account updated = accountRepository.save(account);
        publisher.publishEvent(new AccountEvent(Account.class.getName(), AccountEvent.Event.UPDATE.getName(), updated));
        return updated;
    }

    @Override
    public Set<Club> getCreatedClubs(Account account) {
        return clubService.findAllByAuthor(account);
    }

    @Override
    public Account getAccountByEmail(String name) {
        return accountRepository
                .getAccountByEmail(name).orElseThrow(Exc.sup(ErrorType.ENTITY_NOT_FOUND));
    }

    @Override
    public Boolean changePassword(String code, String password) {
        Optional<ActivationCode> activationCode = activationCodeService.findByCode(code);
        if (activationCode.isEmpty()) {
            return false;
        } else {
            Account account = findOrThrow(activationCode.get().getAccountId());
            account.setHashPassword(passwordEncoder.encode(password));
            accountRepository.save(account);
            activationCodeService.delete(activationCode.get());
            return true;
        }
    }

    @Override
    @Transactional
    public Set<Club> getAccountClubs(Account detachedAccount) {
        Account account = findOrThrow(detachedAccount.getId());
        return account.getClubs();
    }
}
