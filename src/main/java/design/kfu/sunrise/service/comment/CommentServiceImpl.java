package design.kfu.sunrise.service.comment;

import design.kfu.sunrise.domain.dto.comment.CommentDTO;
import design.kfu.sunrise.domain.event.CommentEvent;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.exception.ErrorType;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.repository.CommentRepository;
import design.kfu.sunrise.service.account.AccountService;
import design.kfu.sunrise.service.club.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {


    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ClubService clubService;

    @Autowired
    private AccountService accountService;

    @Transactional
    public Comment addComment(CommentDTO commentDTO, Club club, Account detachedAccount) {
        Account account = accountService.findOrThrow(detachedAccount.getId());
        Comment comment = commentRepository.save(CommentDTO.toComment(commentDTO, club, account));
        club.addComment(comment);
        clubService.updateComments(club);

        publisher.publishEvent(new CommentEvent(Comment.class.getName(), CommentEvent.Event.SAVE.getName(), comment));
        return comment;
    }

    public void editComment(Comment comment) {
        Optional<Comment> commentOptional = commentRepository.findById(comment.getId());
        if (commentOptional.isPresent()) {
            Comment savedComment = commentOptional.get();
            savedComment.setValue(comment.getValue());
        }
        comment.getCommentInfo().setVisible(!comment.getAccount().getAccountInfo().isBannedWriteComment());
        Comment saved = commentRepository.save(comment);
        publisher.publishEvent(new CommentEvent(Comment.class.getName(), CommentEvent.Event.UPDATE.getName(), saved));
    }

    public void deleteComment(Comment comment) {
        Optional<Comment> commentOptional = commentRepository.findById(comment.getId());
        if (commentOptional.isPresent()) {
            Comment savedComment = commentOptional.get();
            commentRepository.delete(savedComment);
            //Не перехватывается
            publisher.publishEvent(new CommentEvent(Comment.class.getName(), CommentEvent.Event.DELETE.getName(), savedComment));
        }
    }

    //В клубе комментарии подтягиваются сразу, так что не detached
    public Set<Comment> getComments(Club detachedClub) {
        Club club = clubService.findOrThrow(detachedClub.getId());
        return club.getComments();
    }

    @Override
    public Comment editAllComment(Comment comment, CommentDTO commentDTO) {
        comment.setValue(commentDTO.getValue());
        if (commentDTO.getAnswered() != null) {
            Comment answered = findOrThrow(commentDTO.getAnswered());
            if (!answered.getId().equals(comment.getId())) {
                comment.setAnswered(answered);
            }
        }
        comment.getCommentInfo().setVisible(!comment.getAccount().getAccountInfo().isBannedWriteComment());
        Comment saved = commentRepository.save(comment);
        publisher.publishEvent(new CommentEvent(Comment.class.getName(), CommentEvent.Event.UPDATE.getName(), saved));
        return saved;
    }

    @Override
    public Comment findOrNull(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public Comment findOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(Exc.sup(ErrorType.ENTITY_NOT_FOUND,"Сущность комментария не найдена"));
    }

    @Override
    public void saveAndFlush(Comment comment) {
        commentRepository.saveAndFlush(comment);
    }
}
