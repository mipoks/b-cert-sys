package design.kfu.sunrise.repository.payment;

import design.kfu.sunrise.domain.model.payment.Requisite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Daniyar Zakiev
 */
@Repository
public interface RequisiteRepository extends JpaRepository<Requisite, Long> {
    Page<Requisite> findAllByAuthorId(Long accountId, Pageable pageable);
}
