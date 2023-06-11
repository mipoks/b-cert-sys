package design.kfu.sunrise.config;

import design.kfu.sunrise.requestlimiter.filter.LimitedSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author Daniyar Zakiev
 */
//@Configuration
//@ComponentScan(basePackages = {"design.kfu.sunrise.requestlimiter"})
//@Order(30)
//public class RequestLimiterConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new LimitedSecurityFilter(applicationContext), BasicAuthenticationFilter.class);
//    }
//}


@Configuration
@ComponentScan(basePackages = {"design.kfu.sunrise.requestlimiter"})
@Order(30)
public class RequestLimiterConfig {

//    @Autowired
//    private ApplicationContext applicationContext;
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new LimitedSecurityFilter(applicationContext), BasicAuthenticationFilter.class);
//        return http.build();
//    }

}