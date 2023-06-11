package design.kfu.sunrise.service.payment;

import design.kfu.sunrise.domain.dto.payment.PaymentDTO;
import design.kfu.sunrise.domain.event.PaymentEvent;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.repository.payment.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RequisiteService requisiteService;

    @Override
    public Payment makePayment(Account account, Club club) {
        Payment payment = PaymentDTO.toPayment(club, account);
        payment = paymentRepository.save(payment);
        publisher.publishEvent(new PaymentEvent(Payment.class.getName(), PaymentEvent.Event.NEW.getName(), payment));
        return payment;
    }

    @Override
    public Payment confirm(Payment payment) {
        payment.setStatus(Payment.Status.CONFIRMED);
        paymentRepository.save(payment);
        publisher.publishEvent(new PaymentEvent(Payment.class.getName(), PaymentEvent.Event.CONFIRM.getName(), payment));
        return payment;
    }

    @Override
    public Payment reject(Payment payment) {
        payment.setStatus(Payment.Status.REJECTED);
        paymentRepository.save(payment);
        publisher.publishEvent(new PaymentEvent(Payment.class.getName(), PaymentEvent.Event.REJECT.getName(), payment));
        return payment;
    }

    @Override
    public Payment refund(Payment payment) {
        payment.setStatus(Payment.Status.REFUND);
        paymentRepository.save(payment);
        publisher.publishEvent(new PaymentEvent(Payment.class.getName(), PaymentEvent.Event.REFUND.getName(), payment));
        return payment;
    }

    @Override
    public Payment send(Payment payment) {
        payment.setStatus(Payment.Status.SEND);
        paymentRepository.save(payment);
        publisher.publishEvent(new PaymentEvent(Payment.class.getName(), PaymentEvent.Event.SEND.getName(), payment));
        return payment;
    }

    @Override
    public Page<Payment> getPayments(Account account, Pageable pageable) {
        return paymentRepository.findAllByAccount(account, pageable);
    }

    @Override
    public Optional<Payment> findPaymentForClub(Account account, Club club) {
        return paymentRepository.findByAccountAndPaymentInfo_ClubId(account, club.getId());
    }
}
