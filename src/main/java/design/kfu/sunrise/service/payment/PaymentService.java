package design.kfu.sunrise.service.payment;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.payment.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
public interface PaymentService {
    Payment makePayment(Account account, Club club);

    Payment confirm(Payment payment);

    Payment reject(Payment payment);

    Payment refund(Payment payment);

    Payment send(Payment payment);

    Page<Payment> getPayments(Account account, Pageable pageable);

    Optional<Payment> findPaymentForClub(Account account, Club club);
}
