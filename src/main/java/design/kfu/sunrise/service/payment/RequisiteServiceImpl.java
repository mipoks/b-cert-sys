package design.kfu.sunrise.service.payment;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.payment.Requisite;
import design.kfu.sunrise.exception.ErrorType;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.repository.payment.RequisiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
@Service
public class RequisiteServiceImpl implements RequisiteService {

    @Autowired
    private RequisiteRepository requisiteRepository;

    @Override
    public Requisite getAvailableFor(Account account) {
        Optional<Requisite> one = requisiteRepository.findAll(Example.of(Requisite.builder()
                .active(true)
                .build())).stream().findFirst();
        return one.orElseThrow(Exc.sup(ErrorType.ENTITY_NOT_FOUND,"Нет доступных реквизитов для оплаты"));
    }

    @Override
    public Page<Requisite> getRequisites(Pageable pageable) {
        return requisiteRepository.findAll(pageable);
    }

    @Override
    public Page<Requisite> getRequisitesFor(Account account, Pageable pageable) {
        return requisiteRepository.findAllByAuthorId(account.getId(), pageable);
    }

    @Override
    public Requisite createRequisite(Account account, Requisite requisite) {
        requisite.setActive(true);
        requisite.setAuthorId(account.getId());
        return requisiteRepository.save(requisite);
    }

    @Override
    public Requisite updateRequisite(Requisite requisite) {
        return requisiteRepository.save(requisite);
    }
}
