package design.kfu.sunrise.service.util;

import com.ibm.icu.text.Transliterator;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import org.springframework.stereotype.Service;

/**
 * @author Daniyar Zakiev
 */
@Service
public class UrlServiceImpl implements UrlService {

    public static final String CYRILLIC_TO_LATIN = "Cyrillic-Latin";
    private final Transliterator transliterator;

    public UrlServiceImpl() {
        transliterator = Transliterator.getInstance(CYRILLIC_TO_LATIN);
    }

    @Override
    public String generateUrl(Club club) {
        return generateUrl(club.getName(), club.getId());
    }

    @Override
    public String generateUrl(Category category) {
        return generateUrl(category.getName(), category.getId());
    }

    private String generateUrl(String name, Long id) {
        return transliterator.transliterate(name).replace(' ', '-') + '.' + id;
    }
}
