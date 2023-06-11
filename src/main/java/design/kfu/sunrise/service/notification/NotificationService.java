package design.kfu.sunrise.service.notification;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.util.Notification;

import java.util.List;

/**
 * @author Daniyar Zakiev
 */
public interface NotificationService {
    void notifyAccount(Notification notification, Account account);
    void notifyClub(Notification notification, Club club);
    List<Notification> findByAccount(Account account);
    void markAsRed(Notification notification);
    void markAsNotRed(Notification notification);
}
