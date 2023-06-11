package design.kfu.sunrise.requestlimiter.repository;

import design.kfu.sunrise.requestlimiter.domain.RequestLimitation;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Daniyar Zakiev
 */
public interface RequestLimitationRepository extends CrudRepository<RequestLimitation, String> {
}
