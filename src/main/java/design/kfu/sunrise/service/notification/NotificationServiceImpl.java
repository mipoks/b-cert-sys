package design.kfu.sunrise.service.notification;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.util.Notification;
import design.kfu.sunrise.repository.util.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniyar Zakiev
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void notifyAccount(Notification notification, Account account) {
        notification.setAccountId(account.getId());
        notificationRepository.save(notification);
    }

    @Override
    public void notifyClub(Notification notification, Club club) {
        Set<Account> accounts = club.getAccounts();
        for (Account account : accounts) {
            notifyAccount(notification, account);
        }
        Account author = club.getAuthor();
        boolean authorInClub = accounts.stream().anyMatch(a -> a.equals(author));
        if (!authorInClub) {
            notifyAccount(notification, author);
        }
    }

    @Override
    public List<Notification> findByAccount(Account account) {
        return notificationRepository
                .findByAccountIdAndRead(account.getId(), false)
                .stream()
                .sorted(Comparator.comparing(Notification::getInstant))
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRed(Notification notification) {
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAsNotRed(Notification notification) {
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}
