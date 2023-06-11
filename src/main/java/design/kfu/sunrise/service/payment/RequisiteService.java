package design.kfu.sunrise.service.payment;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.payment.Requisite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Daniyar Zakiev
 */
public interface RequisiteService {
    Requisite getAvailableFor(Account account);

    Page<Requisite> getRequisites(Pageable pageable);

    Page<Requisite> getRequisitesFor(Account account, Pageable pageable);

    Requisite createRequisite(Account account, Requisite requisite);

    Requisite updateRequisite(Requisite requisite);
}
