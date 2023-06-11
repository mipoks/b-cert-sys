package design.kfu.sunrise.requestlimiter.filter;

import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.service.LimitationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
public class LimitedSecurityFilter extends GenericFilterBean {

    private ApplicationContext applicationContext;
    private LimitationService limitationService;

    public LimitedSecurityFilter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.limitationService = applicationContext.getBean(LimitationService.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        RequestMappingHandlerMapping mappings1 = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        try {
            HandlerExecutionChain handler = mappings1.getHandler((HttpServletRequest) servletRequest);
            if (Objects.nonNull(handler)) {
                HandlerMethod handler1 = (HandlerMethod) handler.getHandler();

                //Метод в контроллере
                Method method = handler1.getMethod();
                Limit limit = method.getAnnotation(Limit.class);
                if (limit != null) {

                    boolean isNotLimited = limitationService.checkRequestLimit(limit, (HttpServletRequest) servletRequest);
                    if (isNotLimited) {
                        filterChain.doFilter(servletRequest, servletResponse);
                    } else {
                        HttpServletResponse limitedResponse = (HttpServletResponse) servletResponse;
                        limitedResponse.setStatus(limit.status().value());
                    }
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
