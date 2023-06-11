package design.kfu.sunrise.service;

import design.kfu.sunrise.domain.event.ClubEvent;
import design.kfu.sunrise.domain.event.PaymentEvent;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.domain.model.util.Notification;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.service.notification.NotificationService;
import design.kfu.sunrise.service.review.ReviewService;
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
public class PaymentEventListener {

    @Autowired
    private ClubService clubService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ReviewService reviewService;

    public static final String PAYMENT = "Платеж";
    public static final String NEW_PAYMENT_CREATED = "Ожидает оплаты";
    public static final String PAYMENT_SEND = "Платеж отправлен. Менеджеры уже оповещены";
    public static final String PAYMENT_CONFIRM = "Платеж подтвержден. Вы уже в группе";
    public static final String PAYMENT_REJECT = "Платеж не подтвержден";
    public static final String PAYMENT_REFUND = "Платеж возвращен";

    @EventListener
    public void handlePaymentEventCreate(PaymentEvent event) {
        if (event.getEvent().equals(PaymentEvent.Event.NEW.getName())) {
            Payment payment = (Payment) event.getObject();
            Account account = payment.getAccount();

            Notification notification = Notification.builder()
                    .description(NEW_PAYMENT_CREATED)
                    .name(PAYMENT)
                    .instant(Instant.now())
                    .read(false)
                    .url(payment.getId().toString())
                    .build();
            notificationService.notifyAccount(notification, account);
        }
    }

    @EventListener
    public void handlePaymentEventSend(PaymentEvent event) {
        if (event.getEvent().equals(PaymentEvent.Event.SEND.getName())) {
            Payment payment = (Payment) event.getObject();
            Account account = payment.getAccount();

            Notification notification = Notification.builder()
                    .description(PAYMENT_SEND)
                    .name(PAYMENT)
                    .instant(Instant.now())
                    .read(false)
                    .url(payment.getId().toString())
                    .build();
            notificationService.notifyAccount(notification, account);
        }
    }

    @EventListener
    public void handlePaymentEventConfirm(PaymentEvent event) {
        if (event.getEvent().equals(PaymentEvent.Event.CONFIRM.getName())) {
            Payment payment = (Payment) event.getObject();
            Account account = payment.getAccount();

            Notification notification = Notification.builder()
                    .description(PAYMENT_CONFIRM)
                    .name(PAYMENT)
                    .instant(Instant.now())
                    .read(false)
                    .url(payment.getId().toString())
                    .build();
            notificationService.notifyAccount(notification, account);

            Club club = clubService.findOrThrow(payment.getPaymentInfo().getClubId());
            clubService.addAccountToClub(club, account);
        }
    }

    @EventListener
    public void handlePaymentEventReject(PaymentEvent event) {
        if (event.getEvent().equals(PaymentEvent.Event.REJECT.getName())) {
            Payment payment = (Payment) event.getObject();
            Account account = payment.getAccount();

            Notification notification = Notification.builder()
                    .description(PAYMENT_REJECT)
                    .name(PAYMENT)
                    .instant(Instant.now())
                    .read(false)
                    .url(payment.getId().toString())
                    .build();
            notificationService.notifyAccount(notification, account);

            Club club = clubService.findOrThrow(payment.getPaymentInfo().getClubId());
            clubService.addAccountToClub(club, account);
        }
    }

    @EventListener
    public void handlePaymentEventRefund(PaymentEvent event) {
        if (event.getEvent().equals(PaymentEvent.Event.REFUND.getName())) {
            Payment payment = (Payment) event.getObject();
            Account account = payment.getAccount();

            Notification notification = Notification.builder()
                    .description(PAYMENT_REFUND)
                    .name(PAYMENT)
                    .instant(Instant.now())
                    .read(false)
                    .url(payment.getId().toString())
                    .build();
            notificationService.notifyAccount(notification, account);

            Club club = clubService.findOrThrow(payment.getPaymentInfo().getClubId());
            clubService.removeAccountFrom(club, account);
        }
    }



    @Autowired
    private NotificationService notificationService;

    public static final String ACCOUNT_TO_CLUB_ENTER = "В клуб вошел новый участник";

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
}
