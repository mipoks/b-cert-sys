package design.kfu.sunrise.domain.dto.comment;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.domain.model.embedded.CommentInfo;
import design.kfu.sunrise.service.StaticService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {

    private Long id;
    @NotNull
    @Size(min = 2, max = 4096)
    private String value;
    private Long accountId;
    private Long clubId;
    private Long answered;
    private Boolean visible;
    private String created;
    private String updated;
    //Если сообщение отправлено с телеграма, то здесь будет айди отправителя
    private Long telegramAccountId;
    private List<String> images = new ArrayList<>();

    public static Comment toComment(CommentDTO commentDTO, Club club, Account account) {
        return Comment.builder()
                .value(commentDTO.getValue())
                .account(account)
                .club(club)
                .answered(commentDTO.getAnswered() != null ? club
                        .getComments()
                        .stream()
                        .filter(comment -> comment.getId().equals(commentDTO.getAnswered()))
                        .findFirst()
                        .orElse(null) : null)
                .commentInfo(
                        CommentInfo.builder()
                                .visible(!account.getAccountInfo().isBannedWriteComment())
                                .build()
                )
                .telegramAccountId(commentDTO.getTelegramAccountId())
                .build();
    }

    public static CommentDTO from(Comment comment) {
        return CommentDTO.builder()
                .value(comment.getValue())
                .id(comment.getId())
                .accountId(comment.getAccount().getId())
                .visible(comment.getCommentInfo().isVisible())
                .clubId(comment.getClub().getId())
                .answered(comment.getAnswered() != null ? comment.getAnswered().getId() : null)
                .created(StaticService.getInstantConverter().convert(comment.getCreated()))
                .updated(StaticService.getInstantConverter().convert(comment.getUpdated()))
                .images(comment.getImages())
                .build();
    }
}
