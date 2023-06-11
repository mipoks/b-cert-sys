package design.kfu.sunrise.requestlimiter.annotation;

import design.kfu.sunrise.requestlimiter.domain.limitation.LimitCount;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Daniyar Zakiev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface Limit {

    String url();

    int requestCount() default LimitCount.OPTIMAL;

    LimitType limitType() default LimitType.PER_MINUTE;

    HttpStatus status() default HttpStatus.TOO_MANY_REQUESTS;

}
