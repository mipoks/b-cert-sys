package design.kfu.sunrise.service.aspect.sitemap;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.service.category.CategoryService;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.service.util.UrlService;
import design.kfu.sunrise.util.model.Filter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Daniyar Zakiev
 */
@Aspect
@Service
@Slf4j
public class SitemapService {

    @Value("${site.domain}")
    private String DOMAIN;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UrlService urlService;

    @Autowired
    private CategoryService categoryService;

    public String generateUrlForClub(Club club) {
        return DOMAIN + "v1/club/" + urlService.generateUrl(club);
    }

    public String generateUrlForCategory(Category category) {
        return DOMAIN + "v1/category/" + urlService.generateUrl(category);
    }

    //ToDo поставить на крон, сделать индексирование
    public void createMapForClub(JoinPoint jp, Object entity) throws MalformedURLException {
        File file = new File("").getAbsoluteFile();

        List<Club> clubs = clubService.findClubs(Filter.builder()
                .expired(false)
                .build());

        WebSitemapGenerator wsg = WebSitemapGenerator.builder(DOMAIN, file)
                .build();

        for (Club club : clubs) {
            String url = generateUrlForClub(club);
            wsg.addUrl(url);
        }
        wsg.write();
    }

    //ToDo поставить на крон, сделать индексирование, убрать аспект
    @AfterReturning(pointcut = "execution(* design.kfu.sunrise.controller.ClubController.getClub(..)))", returning = "entity")
    public void createMapForCategory(JoinPoint jp, Object entity) throws MalformedURLException {
        File file = new File("").getAbsoluteFile();
        List<Category> categories = categoryService.findAllCategories();

        WebSitemapGenerator wsg = WebSitemapGenerator.builder(DOMAIN, file)
                .build();

        for (Category category : categories) {
            String url = generateUrlForCategory(category);
            wsg.addUrl(url);
        }
        wsg.write();
    }
}
