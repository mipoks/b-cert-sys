package design.kfu.sunrise.controller;

import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import design.kfu.sunrise.domain.dto.comment.CommentDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class CommentController {

    private final CommentService commentService;


    @Operation(description = "Add comment in club", summary = "Returns created comment in DTO")
//    @Limit(url = "/club/comment", limitType = LimitType.PER_HOUR, requestCount = 10)
    @PreAuthorize("@access.hasAccessToWriteComment(#account, #club)")
    @PostMapping("/club/{club_id}/comment")
    public CommentDTO addComment(@PathVariable("club_id") Club club, @Valid @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal(expression = "account") Account account){
        return CommentDTO.from(commentService.addComment(commentDTO, club, account));
    }

    //ToDo дописать, комментарий должен стать невидимым
    @Operation(description = "Ban comment", summary = "Set comment invisible")
    @PreAuthorize("@access.hasAccessToBan(#account)")
    @GetMapping("/club/{club_id}/comment/{comment_id}")
    public CommentDTO banComment(@PathVariable("club_id") Club club, @PathVariable("comment_id") Comment comment, @AuthenticationPrincipal(expression = "account") Account account){
        return CommentDTO.from(comment);
    }

    @Operation(description = "Update comment", summary = "Returns updated comment in DTO")
    @PreAuthorize("@access.hasAccessToEditComment(#comment, #account) && @access.hasAccessToWriteComment(#account, #club)")
    @Limit(url = "/comment/update", limitType = LimitType.PER_HOUR, requestCount = 10)
    @PutMapping("/club/{club_id}/comment/{comment_id}")
    public CommentDTO updateComment(@PathVariable("club_id") Club club, @Valid @RequestBody CommentDTO commentDTO, @PathVariable("comment_id") Comment comment, @AuthenticationPrincipal(expression = "account") Account account){
        return CommentDTO.from(commentService.editAllComment(comment, commentDTO));
    }

    @Operation(description = "Delete comment", summary = "Returns boolean")
    @PreAuthorize("@access.hasAccessToEditComment(#comment, #account) && @access.hasAccessToWriteComment(#account, #club)")
    @DeleteMapping("/club/{club_id}/comment/{comment_id}")
    public Boolean deleteComment(@PathVariable("club_id") Club club, @PathVariable("comment_id") Comment comment, @AuthenticationPrincipal(expression = "account") Account account){
        commentService.deleteComment(comment);
        return true;
    }

    @Operation(description = "Get club's comments", summary = "Returns comments in club")
    @PreAuthorize("@access.hasAccessToReadComment(#account, #club)")
    @GetMapping("/club/{club_id}/comments")
    public List<CommentDTO> getClubComments(@PathVariable("club_id") Club club, @AuthenticationPrincipal(expression = "account") Account account) {
        return commentService.getComments(club).stream().sorted(Comparator.comparing(Comment::getCreated)).map(CommentDTO::from).collect(Collectors.toList());
    }
}
