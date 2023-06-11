package design.kfu.sunrise.service.club;

import design.kfu.sunrise.domain.dto.club.ClubCDTO;
import design.kfu.sunrise.domain.event.ClubEvent;
import design.kfu.sunrise.domain.model.*;
import design.kfu.sunrise.domain.model.embedded.ClubInfo;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.exception.ErrorType;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.feign.FeignIntegrateClient;
import design.kfu.sunrise.repository.AccountRepository;
import design.kfu.sunrise.repository.ClubRepository;
import design.kfu.sunrise.service.authority.AuthorityService;
import design.kfu.sunrise.service.category.CategoryService;
import design.kfu.sunrise.service.integrator.IntegratorService;
import design.kfu.sunrise.util.model.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IntegratorService integratorService;

    @Override
    public Club addClub(ClubCDTO clubDTO) {
        Club saved = clubRepository.save(ClubCDTO.toClub(clubDTO));
        publisher.publishEvent(new ClubEvent(Club.class.getName(), ClubEvent.Event.PUBLISH.getName(), saved));
        return saved;
    }

    @Override
    public Club findOrNull(Long clubId) {
        return clubRepository.findById(clubId).orElse(null);
    }

    @Override
    public Club findOrThrow(Long clubId) {
        return clubRepository
                .findById(clubId)
                .orElseThrow(Exc.sup(ErrorType.ENTITY_NOT_FOUND, "Сущность клуба не найдена"));
    }

    @Override
    public Set<Comment> updateComments(Club club) {
        Club saved = clubRepository.save(club);
        //Не перехватывается
        publisher.publishEvent(new ClubEvent(Club.class.getName(), ClubEvent.Event.COMMENT_UPDATE.getName(), saved));
        return club.getComments();
    }

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public Set<Account> addAccountToClub(Club club, Account detachedAccount) {

        Account account = accountRepository.findById(detachedAccount.getId()).get();
        account.addClub(club);
        clubRepository.saveAndFlush(club);

        publisher.publishEvent(new ClubEvent(Club.class.getName(), ClubEvent.Event.ACCOUNT_ENTER.getName(), club));

        Authority authority = authorityService.findOrThrow(account, club);
        authority
                .addAuthotityType(Authority.AuthorityType.READ_CLUB_COMMENTS)
                .addAuthotityType(Authority.AuthorityType.WRITE_CLUB_COMMENTS);

        authorityService.save(authority);
        return club.getAccounts();
    }

    @Override
    public void saveAndFlush(Club club) {
        clubRepository.saveAndFlush(club);
    }

    @Override
    public Club moveClub(Club club, Category category) {
        club.setCategory(category);
        Club saved = clubRepository.save(club);
        publisher.publishEvent(new ClubEvent(Club.class.getName(), ClubEvent.Event.CLUB_MOVE.getName(), saved));
        return saved;
    }

    @Override
    public Club deactivateClub(Club club) {
        club.getClubInfo().setExpired(true);
        Club saved = clubRepository.save(club);
        publisher.publishEvent(new ClubEvent(Club.class.getName(), ClubEvent.Event.CLUB_DEACTIVATE.getName(), saved));
        return saved;
    }

    @Override
    public List<Club> findClubs(Filter filter) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("key",new ExampleMatcher.GenericPropertyMatcher().exact())
                .withMatcher("instrument",new ExampleMatcher.GenericPropertyMatcher().exact());

        Club club = Club.builder().build();
        if (filter.getCategoryId() != null) {
            Category category = categoryService.findOrThrow(filter.getCategoryId());
            club.setCategory(category);
            matcher
                .withMatcher("categoryId", exact());
        }
        club.setClubInfo(ClubInfo.builder().expired(filter.getExpired()).build());
        matcher
                .withMatcher("expired", exact());
        Example<Club> example = Example.of(club, matcher);
        List<Club> clubs = clubRepository.findAll(example);
        if (filter.getSort() != null)
            switch (filter.getSort()) {
                case ("date"):
                    clubs = clubs.stream()
                            .sorted(Comparator.comparing(Club::getCreated).reversed())
                            .collect(Collectors.toList());
            }
        return clubs;
    }


    @Override
    public Set<Club> findAllByAuthor(Account account) {
        return clubRepository.findAllByAuthor(account);
    }

    @Override
    public Club updateClub(Club club) {
        Club saved = clubRepository.save(club);
        publisher.publishEvent(new ClubEvent(Club.class.getName(), ClubEvent.Event.UPDATE.getName(), saved));
        return saved;
    }

    @Override
    @Transactional
    public Set<Account> removeAccountFrom(Club club, Account detachedAccount) {
        Account account = accountRepository.findById(detachedAccount.getId()).get();
        Authority authority = authorityService.findOrThrow(account, club);
        authorityService.remove(authority);

        publisher.publishEvent(new ClubEvent(Club.class.getName(), ClubEvent.Event.ACCOUNT_EXIT.getName(), club));
        return club.getAccounts();
    }

    @Override
    public Integer getClubUserCount(Club club) {
        Set<Account> accounts = club.getAccounts();
        Long chatId = club.getClubInfo().getTelegramChatId();
        Integer userCount = integratorService.getUserCountFromChat(chatId);
        return accounts.size() + userCount;
    }

}
