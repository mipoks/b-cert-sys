package design.kfu.sunrise.service;

import design.kfu.sunrise.service.account.AccountService;
import design.kfu.sunrise.service.category.CategoryService;
import design.kfu.sunrise.service.conversation.ConversationService;
import design.kfu.sunrise.service.payment.RequisiteService;
import design.kfu.sunrise.service.util.AnonymousService;
import design.kfu.sunrise.service.util.UrlService;
import design.kfu.sunrise.util.converter.InstantConverter;
import org.springframework.context.ApplicationContext;

/**
 * @author Daniyar Zakiev
 */
public class StaticService {

    private static ApplicationContext applicationContext;

    public StaticService(ApplicationContext applicationContext) {
        StaticService.applicationContext = applicationContext;
    }

    private static <T> T getInstance(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static CategoryService getCategoryService() {
        return getInstance(CategoryService.class);
    }

    public static AccountService getAccountService() {
        return getInstance(AccountService.class);
    }

    public static AnonymousService getAnonymousService() {
        return getInstance(AnonymousService.class);
    }

    public static ConversationService getConversationService() {
        return getInstance(ConversationService.class);
    }

    public static RequisiteService getRequisiteService() {
        return getInstance(RequisiteService.class);
    }

    public static UrlService getUrlService() {
        return getInstance(UrlService.class);
    }

    public static InstantConverter getInstantConverter() {
        return getInstance(InstantConverter.class);
    }

}
