package design.kfu.sunrise.service;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.dto.club.ClubVDTO;
import design.kfu.sunrise.domain.event.AccountEvent;
import design.kfu.sunrise.domain.event.CategoryEvent;
import design.kfu.sunrise.domain.event.ClubEvent;
import design.kfu.sunrise.domain.event.CommentEvent;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.domain.model.util.Notification;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.service.comment.CommentService;
import design.kfu.sunrise.service.mail.EmailService;
import design.kfu.sunrise.service.mail.util.ActivationCodeService;
import design.kfu.sunrise.service.mail.util.EmailContextGenerator;
import design.kfu.sunrise.service.notification.NotificationService;
import design.kfu.sunrise.service.review.ReviewService;
import design.kfu.sunrise.service.util.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
@Service
@Slf4j
public class DomainEventListener {

    public static final String ACCOUNT_TO_CLUB_ENTER = "В клуб вошел новый участник";
    public static final String COMMENT_ADD_TO_CLUB = "Участник оставил новый комментарий";
    public static final String COMMENT_DECLINED = "Комментарий не прошел проверку";
    public static final String CLUB_PUBLISHED = "Клуб был опубликован";
    public static final String CLUB_DECLINED = "Клуб не прошел проверку";
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ActivationCodeService activationCodeService;
    @Autowired
    private EmailContextGenerator emailContextGenerator;

    @Autowired
    private ClubService clubService;

    @Autowired
    private CommentService commentService;

//    @EventListener
//    public void handleClubEventSaveUpdate(ClubEvent event) {
//        if (event.getEvent().equals(ClubEvent.Event.SAVE.getName()) || event.getEvent().equals(CategoryEvent.Event.UPDATE.getName())) {
//            Club club = (Club) event.getObject();
//            reviewService.addClubForReview(club);
//        }
//    }
    @Autowired
    private SearchService searchService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void handleAccountEventCreate(AccountEvent event) {
        if (event.getEvent().equals(AccountEvent.Event.CREATE.getName())) {
            Account account = (Account) event.getObject();
            emailService.sendConfirmationEmail(account);
        }
    }

    @EventListener
    public void handleCategoryEventSaveUpdate(CategoryEvent event) {
        if (event.getEvent().equals(CategoryEvent.Event.SAVE.getName())
                || event.getEvent().equals(CategoryEvent.Event.UPDATE.getName())) {
            Category category = (Category) event.getObject();
            searchService.saveCategory(CategoryDTO.fromExcludeChilds(category));
        }
    }

    @EventListener
    public void handleCategoryEventDelete(CategoryEvent event) {
        if (event.getEvent().equals(CategoryEvent.Event.DELETE.getName())) {
            Category category = (Category) event.getObject();
            searchService.deleteCategory(CategoryDTO.fromExcludeChilds(category));
        }
    }

    @EventListener
    public void handleClubEventDeactivate(ClubEvent event) {
        if (event.getEvent().equals(ClubEvent.Event.CLUB_DEACTIVATE.getName())) {
            Club club = (Club) event.getObject();
            searchService.deleteClub(ClubVDTO.from(club));
        }
    }

    @EventListener
    public void handleClubEventAccountEnter(ClubEvent event) {
        if (event.getEvent().equals(ClubEvent.Event.ACCOUNT_ENTER.getName())) {
            Club club = (Club) event.getObject();

            Notification notification = Notification.builder()
                    .description(ACCOUNT_TO_CLUB_ENTER)
                    .name(club.getName())
                    .instant(Instant.now())
                    .read(false)
                    .url(club.getId().toString())
                    .build();
            notificationService.notifyClub(notification, club);
        }
    }

    @EventListener
    public void handleClubEventClubMove(ClubEvent event) {
        if (event.getEvent().equals(ClubEvent.Event.CLUB_MOVE.getName())) {
            Club club = (Club) event.getObject();
            searchService.saveClub(ClubVDTO.from(club));
        }
    }

    @EventListener
    public void handleCommentEventSave(CommentEvent event) {
        if (event.getEvent().equals(CommentEvent.Event.SAVE.getName()) ||
                event.getEvent().equals(CommentEvent.Event.UPDATE.getName())) {
            Comment comment = (Comment) event.getObject();
            if (comment.getAccount().getAccountInfo().isBannedWriteComment()) {
                reviewService.addCommentForReview(comment);
            } else {
                event = new CommentEvent(Comment.class.getName(), CommentEvent.Event.PUBLISH.getName(), comment);
                publisher.publishEvent(event);
            }
        }
    }

    //Отправка уведомления
    @EventListener
    public void handleCommentEventPublish(CommentEvent event) {
        if (event.getEvent().equals(CommentEvent.Event.PUBLISH.getName())) {
            Comment comment = (Comment) event.getObject();

            Notification notification = Notification.builder()
                    .description(COMMENT_ADD_TO_CLUB)
                    .name(comment.getClub().getName())
                    .instant(Instant.now())
                    .read(false)
                    .url(comment.getId().toString())
                    .build();
            notificationService.notifyClub(notification, comment.getClub());
        }
    }

    @EventListener
    public void handleCommentEventDecline(CommentEvent event) {
        if (event.getEvent().equals(CommentEvent.Event.DECLINE.getName())) {
            Comment comment = (Comment) event.getObject();

            Notification notification = Notification.builder()
                    .description(COMMENT_DECLINED)
                    .name(comment.getClub().getName())
                    .instant(Instant.now())
                    .read(false)
                    .url(comment.getId().toString())
                    .build();
            notificationService.notifyAccount(notification, comment.getAccount());
        }
    }

    @EventListener
    public void handleClubEventPublish(ClubEvent event) {
        if (event.getEvent().equals(ClubEvent.Event.PUBLISH.getName())) {
            Club club = (Club) event.getObject();

            Notification notification = Notification.builder()
                    .description(CLUB_PUBLISHED)
                    .name(club.getName())
                    .instant(Instant.now())
                    .read(false)
                    .url(club.getId().toString())
                    .build();
            notificationService.notifyAccount(notification, club.getAuthor());

            searchService.saveClub(ClubVDTO.from(club));
        }
    }

    @EventListener
    public void handleClubEventDecline(ClubEvent event) {
        if (event.getEvent().equals(ClubEvent.Event.DECLINE.getName())) {
            Club club = (Club) event.getObject();

            Notification notification = Notification.builder()
                    .description(CLUB_DECLINED)
                    .name(club.getName())
                    .instant(Instant.now())
                    .read(false)
                    .url(club.getId().toString())
                    .build();
            notificationService.notifyAccount(notification, club.getAuthor());
        }
    }

    @EventListener
    public void handleClubEventImageAdd(ClubEvent event) {
        if (event.getEvent().equals(ClubEvent.Event.IMAGE_ADD.getName())
        || event.getEvent().equals(ClubEvent.Event.IMAGE_REMOVE.getName())) {
            Club club = (Club) event.getObject();
            clubService.saveAndFlush(club);
        }
    }

    @EventListener
    public void handleCommentEventImageAdd(CommentEvent event) {
        if (event.getEvent().equals(CommentEvent.Event.IMAGE_ADD.getName())
                || event.getEvent().equals(CommentEvent.Event.IMAGE_REMOVE.getName())) {
            Comment comment = (Comment) event.getObject();
            commentService.saveAndFlush(comment);
        }
    }

    //CommentEvent -> отправить в клуб

    //ReviewEvent -> отправить в аккаунт

    //разобрать с ReviewEvent и CommentEvent, так как при сохранении Comment, он должен быть invisible,
    //и только после Review станет видимым

    //AccountEvent -> отправить письмо

    //CategoryEvent -> ничего

    //ClubEvent -> отправить notification в клуб

    //ClubEvent -> сохранить в ES

    //CategoryEvent -> сохранить в ES


}
