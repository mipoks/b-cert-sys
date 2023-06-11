package design.kfu.sunrise;

import design.kfu.sunrise.domain.dto.comment.CommentDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.exception.RestException;
import design.kfu.sunrise.repository.AccountRepository;
import design.kfu.sunrise.repository.ClubRepository;
import design.kfu.sunrise.repository.CommentRepository;
import design.kfu.sunrise.service.comment.CommentService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceTests {

    /*
    Comment addComment(CommentDTO commentDTO, Club club, Account account);
    void editComment(Comment comment);
    void deleteComment(Comment comment);
    Set<Comment> getComments(Club club);
    Comment editAllComment(Comment comment, CommentDTO commentDTO);
    Comment findOrNull(Long commentId);
    Comment findOrThrow(Long commentId);
    */

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClubRepository clubRepository;

    Club getClubFromRepository() {
        return clubRepository.findAll().stream().findFirst().orElseThrow();
    }

    Account getAccountFromRepository() {
        return accountRepository.findAll().stream().findFirst().orElseThrow();
    }

    Comment getCommentFromRepository() {
        return commentRepository.findAll().stream().findFirst().orElseThrow();
    }

    List<CommentDTO> clubCDTOGenerator(int count) {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            commentDTOS.add(ModelGenerator.generateCommentDTO());
        }
        return commentDTOS;
    }

    @Test
    @Order(1)
    void testAddComment() {
        Club club = getClubFromRepository();
        Account account = getAccountFromRepository();
        List<CommentDTO> clubCDTOS = clubCDTOGenerator(10);
        for(CommentDTO commentDTO : clubCDTOS) {
            commentService.addComment(commentDTO, club, account);
        }
    }

    @Test
    void testEditComment() {
        Comment comment = getCommentFromRepository();
        Long id = comment.getId();
        String str = (ModelGenerator.generateString());
        comment.setValue(str);
        commentService.editComment(comment);
        Optional<Comment> optional = commentRepository.findById(id);
        assertEquals(str, optional.get().getValue());
    }

    @Test
    void testDeleteComment() {
        Comment comment = getCommentFromRepository();
        Long id = comment.getId();
        commentService.deleteComment(comment);
        boolean present = commentRepository.findById(id).isPresent();
        assertFalse(present);
    }

    @Test
    void testGetComments() {
        Comment comment = getCommentFromRepository();
        Club club = comment.getClub();
        Set<Comment> comments = commentService.getComments(club);
        assertTrue(comments.size() > 0);
    }

    @Test
    void testEditAllComment() {
        Comment comment = getCommentFromRepository();
        Long id = comment.getId();
        String str = (ModelGenerator.generateString());
        CommentDTO commentDTO = ModelGenerator.generateCommentDTO();
        commentDTO.setValue(str);
        commentService.editAllComment(comment, commentDTO);
        Optional<Comment> optional = commentRepository.findById(id);
        assertEquals(str, optional.get().getValue());
    }

    @Test
    void testFindOrNull() {
        Comment comment = getCommentFromRepository();
        Comment notNullComment = commentService.findOrNull(comment.getId());
        Assert.notNull(notNullComment, "Comment is null, but shouldn't");
        Comment nullComment = commentService.findOrNull(42147242L);
        Assert.isNull(nullComment, "Comment is not present, but it is not null");
    }

    @Test
    void testFindOrThrow() {
        Comment comment = getCommentFromRepository();
        Comment notNullComment = commentService.findOrThrow(comment.getId());
        Assert.notNull(notNullComment, "Comment is null, but shouldn't");

        Throwable exception = assertThrows(RestException.class, () -> commentService.findOrThrow(42147242L));
        assertNotNull(exception, "Exception did not throw");
    }
}
