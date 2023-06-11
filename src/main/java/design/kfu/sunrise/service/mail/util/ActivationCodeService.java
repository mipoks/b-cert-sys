package design.kfu.sunrise.service.mail.util;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.util.ActivationCode;

import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
public interface ActivationCodeService {
    ActivationCode generate(Account account);

    ActivationCode save(ActivationCode code);

    Optional<ActivationCode> findByCode(String code);

    void delete(ActivationCode activationCode);
}
