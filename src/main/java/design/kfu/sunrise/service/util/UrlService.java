package design.kfu.sunrise.service.util;

import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;

/**
 * @author Daniyar Zakiev
 */
public interface UrlService {
    String generateUrl(Club club);
    String generateUrl(Category category);
}
