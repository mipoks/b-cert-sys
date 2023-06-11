package design.kfu.sunrise.service.comment;

import design.kfu.sunrise.domain.dto.comment.CommentDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface CommentService {
    @Transactional
    Comment addComment(CommentDTO commentDTO, Club club, Account account);

    @Transactional
    void editComment(Comment comment);

    @Transactional
    void deleteComment(Comment comment);

    @Transactional
    Set<Comment> getComments(Club club);

    Comment editAllComment(Comment comment, CommentDTO commentDTO);

    Comment findOrNull(Long commentId);

    Comment findOrThrow(Long commentId);

    void saveAndFlush(Comment comment);
}
