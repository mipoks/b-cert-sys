package design.kfu.sunrise.repository.payment;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.payment.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAllByAccount(Account account, Pageable pageable);
    Optional<Payment> findByAccountAndPaymentInfo_ClubId(Account account, Long clubId);
}
