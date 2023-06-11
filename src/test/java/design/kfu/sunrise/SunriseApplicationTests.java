package design.kfu.sunrise;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.dto.club.ClubCDTO;
import design.kfu.sunrise.domain.dto.comment.CommentDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.domain.model.embedded.AccountInfo;
import design.kfu.sunrise.domain.model.embedded.ClubInfo;
import design.kfu.sunrise.domain.model.embedded.CostInfo;
import design.kfu.sunrise.repository.AccountRepository;
import design.kfu.sunrise.repository.ClubRepository;
import design.kfu.sunrise.service.account.AccountService;
import design.kfu.sunrise.service.authority.AuthorityService;
import design.kfu.sunrise.service.category.CategoryService;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.service.comment.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
class SunriseApplicationTests {


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private ClubService clubService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CategoryService categoryService;

//    @Test
//    void testOneToMany() {
//        Club club = new Club();
//        Account account1 = new Account();
//        Account account2 = new Account();
//        club.getAccounts().add(account1);
//        club.getAccounts().add(account2);
//
//        club = clubRepository.save(club);
//
//        club.getAccounts().remove(account1);
//        clubRepository.save(club);
//    }

    @Test
    void createCategory() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("Детские мягкие игрушки")
                .description("В форме зверей")
                .build();

        categoryService.addCategory(categoryDTO);
    }

    @Test
    void createAccount() {
        Account account = Account.builder()
                .accountInfo(
                        AccountInfo.builder()
                                .phone("+79272422387")
                                .build()
                )
                .hashPassword("testPwd")
                .email("temp@mail.ru")
                .build();
        accountRepository.save(account);
    }

    @Test
    void createClub() {
        ClubCDTO clubCDTO = ClubCDTO.builder()
                .categoryId(1L)
                .authorId(1L)
                .name("Сертификат на игрушки")
                .description("Собираю деньги на сертификат. Казань")
                .costInfo(
                        CostInfo.builder()
                                .certificateCost(25000)
                                .entryCost(5000)
                                .build()
                )
                .clubInfo(ClubInfo.builder()
                        .expireCondition("Когда-то")
                        .build())
                .build();
        clubService.addClub(clubCDTO);
    }

    @Test
    void create40Clubs() {
        for (int i = 0; i < 40; i++) {
            ClubCDTO clubCDTO = ClubCDTO.builder()
                    .categoryId(1L)
                    .authorId(1L)
                    .name("Сертификат на игрушки" + i)
                    .description("Собираю деньги на сертификат. Казань" + i)
                    .costInfo(
                            CostInfo.builder()
                                    .certificateCost(25000)
                                    .entryCost(5000)
                                    .build()
                    )
                    .clubInfo(ClubInfo.builder()
                            .expireCondition("Когда-то")
                            .build())
                    .build();
            clubService.addClub(clubCDTO);
        }
    }

    @Test
    @Rollback(value = false)
    @Transactional
    void create10Comments() {
        Club club = null;
        for (int i = 0; i < 10; i++) {
            CommentDTO commentDTO = CommentDTO.builder()
                    .accountId(1L)
                    .value("Comment" + i)
                    .clubId(4L)
                    .build();
            club = clubRepository.findById(4L).get();
            Account account = accountRepository.findById(1L).get();
            commentService.addComment(commentDTO, club, account);
        }
        clubService.saveAndFlush(club);
    }

    @Test
    void testGetCommentsOfClub() {
        Club club = clubService.findOrThrow(4L);
        Set<Comment> commentSet = club.getComments();
        commentSet.stream().forEach(System.out::println);
    }

//    @Test
//    void testRelationShip() {
//        accountRepository.save(Account.builder().login("temp@mail.ru").build());
//        Account account = accountRepository.getAccountByEmail("temp@mail.ru").get();
//        clubService.addClub(ClubCDTO.builder().name("Club!").build());
//        Club club = clubRepository.findById(1L).get();
//        clubService.addAccountToClub(club, account);
//
//    }


//    @Test
////    @Transactional
//    void testAddClubs() {
//        Account account = accountRepository.getAccountByEmail("temp@mail.ru").get();
//
//        clubService.addClub(ClubCDTO.builder().name("Club2").build());
//        clubService.addClub(ClubCDTO.builder().name("Club3").build());
//
//        Club club2 = clubRepository.findById(2L).get();
//        Club club3 = clubRepository.findById(3L).get();
//        clubService.addAccountToClub(club2, account);
//        clubService.addAccountToClub(club3, account);
//
////        Club club = clubRepository.findById(1L).get();
////        Authority authority = authorityService.findOrThrow(account, club);
////        Set<Authority.AuthorityType> authorityTypeSet = authority.getAuthorityTypes();
////        authorityTypeSet.forEach(System.out::println);
//    }
//
//
//    @Test
//    @Transactional
//    void testGetAuthority() {
//        Account account = accountRepository.getAccountByEmail("temp@mail.ru").get();
//
//        account.getClubs().forEach(System.out::println);
////        Club club = clubRepository.findById(1L).get();
////        Authority authority = authorityService.findOrThrow(account, club);
////        Set<Authority.AuthorityType> authorityTypeSet = authority.getAuthorityTypes();
////        authorityTypeSet.forEach(System.out::println);
//    }

}
