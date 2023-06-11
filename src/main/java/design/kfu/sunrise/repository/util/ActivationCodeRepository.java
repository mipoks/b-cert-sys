package design.kfu.sunrise.repository.util;

import design.kfu.sunrise.domain.model.util.ActivationCode;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Daniyar Zakiev
 */
public interface ActivationCodeRepository extends CrudRepository<ActivationCode, String> {
}
