package design.kfu.sunrise.repository.util;

import design.kfu.sunrise.domain.model.util.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Daniyar Zakiev
 */
public interface ReviewRepository extends CrudRepository<Review, String> {
    Page<Review> findAllByViewedOrderByInstant(boolean viewed, Pageable pageable);
    Page<Review> findAllByViewedAndObjectNameOrderByInstant(boolean viewed, String objectName, Pageable pageable);
}
