package design.kfu.sunrise.security.config;

import design.kfu.sunrise.requestlimiter.filter.LimitedSecurityFilter;
import design.kfu.sunrise.security.entrypoint.MyBasicAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
@Order(20)
public class SecurityConfig {

    @Autowired
    MyBasicAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        try {
//            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        } catch (Exception e) {
//            throw new IllegalStateException(e);
//        }
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//                .build();
//    }
//
//    @Bean
//    public UserDetailsManager users(DataSource dataSource) {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        users.createUser(user);
//        return users;
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Autowired
    private ApplicationContext applicationContext;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .addFilterBefore(new CharacterEncodingFilter("UTF-8", true), WebAsyncManagerIntegrationFilter.class)
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/resources/**", "/v1/account/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .anyRequest().permitAll()
                )

                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
                        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
                    }

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                    }
                })
//                .defaultSuccessUrl("/v1/account/me", false)
//                .failureForwardUrl("/unauthorized")
                .and()
                .addFilterBefore(new LimitedSecurityFilter(applicationContext), BasicAuthenticationFilter.class)
                .csrf()
                .disable()
                .cors();
        return httpSecurity.build();
    }
}


//@Configuration
//@EnableWebSecurity(debug = true)
//@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
//@Order(20)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    MyBasicAuthenticationEntryPoint authenticationEntryPoint;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Qualifier("userDetailsServiceImpl")
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        try {
//            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        } catch (Exception e) {
//            throw new IllegalStateException(e);
//        }
//    }
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080/"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setExposedHeaders(List.of("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.addFilterBefore(new CharacterEncodingFilter("UTF-8",true), WebAsyncManagerIntegrationFilter.class)
//                .httpBasic()
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                .cors().and()
//                .authorizeRequests()
//                        .antMatchers("/resources/**", "/v1/account/**")
//                        .permitAll().antMatchers(HttpMethod.GET, "/**")
//                        .permitAll()
//                        .anyRequest()
//                //ToDo нужно изменить, думаю
//                        .permitAll()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                .and()
//                .formLogin()
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/v1/account", false)
//                .failureForwardUrl("/unauthorized")
//                .and()
//                .csrf()
//                .disable();
//    }
//}
