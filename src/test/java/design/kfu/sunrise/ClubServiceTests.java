package design.kfu.sunrise;

import design.kfu.sunrise.domain.dto.club.ClubCDTO;
import design.kfu.sunrise.domain.dto.comment.CommentDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.exception.RestException;
import design.kfu.sunrise.repository.AccountRepository;
import design.kfu.sunrise.repository.CategoryRepository;
import design.kfu.sunrise.repository.ClubRepository;
import design.kfu.sunrise.service.club.ClubService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClubServiceTests {
    /*
    Club addClub(ClubCDTO clubDTO);
    Club findOrNull(Long clubId);
    Club findOrThrow(Long clubId);
    Set<Comment> updateComments(Club club);
    Set<Account> addAccountToClub(Club club, Account account);
    void saveAndFlush(Club club);
    Club moveClub(Club club, Category category);
    Club deactivateClub(Club club);
    Set<Club> findClubs(Filter filter);
    Set<Club> findAllByAuthor(Account account);
    */

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    List<Club> clubGenerator(int count) {
        List<Club> clubs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            clubs.add(ModelGenerator.generateClub());
        }
        return clubs;
    }

    List<ClubCDTO> clubCDTOGenerator(int count) {
        List<ClubCDTO> clubCDTOS = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            clubCDTOS.add(ModelGenerator.generateClubCDTO());
        }
        return clubCDTOS;
    }


    Club getClubFromRepository() {
        return clubRepository.findAll().stream().findFirst().orElseThrow();
    }

    Account getAccountFromRepository() {
        return accountRepository.findAll().stream().findFirst().orElseThrow();
    }

    Category getCategoryFromRepository() {
        return categoryRepository.findAll().stream().findFirst().orElseThrow();
    }

    @Test
    @Order(1)
    void testAddClub() {
        List<ClubCDTO> clubCDTOS = clubCDTOGenerator(10);
        for(ClubCDTO clubCDTO : clubCDTOS) {
            clubService.addClub(clubCDTO);
        }
    }

    @Test
    void testFindOrNull() {
        Club Club = getClubFromRepository();
        Club notNullClub = clubService.findOrNull(Club.getId());
        Assert.notNull(notNullClub, "Club is null, but shouldn't");
        Club nullClub = clubService.findOrNull(42147242L);
        Assert.isNull(nullClub, "Club is not present, but it is not null");
    }

    @Test
    void testFindOrThrowMethod() {
        Club club = getClubFromRepository();
        Club notNullClub = clubService.findOrThrow(club.getId());
        Assert.notNull(notNullClub, "Club is null, but shouldn't");

        Throwable exception = assertThrows(RestException.class, () -> clubService.findOrThrow(42147242L));
        assertNotNull(exception, "Exception did not throw");
    }

    @Test
    void testUpdateCommentsMethod() {
        Club club = getClubFromRepository();
        Account account = getAccountFromRepository();

        CommentDTO commentDTO = ModelGenerator.generateCommentDTO();
        Comment comment = CommentDTO.toComment(commentDTO, club, account);

        club.getComments().add(comment);
        Set<Comment> comments = clubService.updateComments(club);
        assertTrue(comments.contains(comment));
    }

    @Test
    void testAddAccountToClub() {
        Account account = getAccountFromRepository();
        Club club = getClubFromRepository();
        Set<Account> accounts = clubService.addAccountToClub(club, account);
        assertTrue(accounts.contains(account));
    }

    @Test
    void testMoveClub() {
        Category category = getCategoryFromRepository();
        Club club = getClubFromRepository();
        Club movedClub = clubService.moveClub(club, category);
        assertSame(movedClub.getCategory().getId(), category.getId());
    }

    @Test
    void testDeactivateClub() {
        Club club = getClubFromRepository();
        Club deactivatedClub = clubService.deactivateClub(club);
        assertTrue(deactivatedClub.getClubInfo().isExpired());
    }

    @Test
    void testFindAllByAuthor() {
        Account account = getAccountFromRepository();
        Set<Club> clubs = clubService.findAllByAuthor(account);
        assertTrue(clubs.size() > 0);
    }
}
