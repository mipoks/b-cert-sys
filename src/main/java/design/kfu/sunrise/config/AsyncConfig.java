package design.kfu.sunrise.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    /*
    Executor для асинхронной посылки сообщений на email
     */
    @Bean("threadPoolTaskExecutor")
    public AsyncTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Количество основных потоков 5: количество потоков, инициализированных при создании пула потоков
        executor.setCorePoolSize(5);
        // Максимальное количество потоков 5: максимальное количество потоков в пуле потоков, только после того, как буферная очередь будет заполнена, будет применяться к потокам, которые превышают количество основных потоков
        executor.setMaxPoolSize(5);
        // Буферная очередь 500: очередь, используемая для буферизации задач выполнения
//        executor.setQueueCapacity(500);
        // Разрешить потоку время простоя в течение 60 секунд: когда поток превышает основной поток, поток будет уничтожен после наступления времени простоя
        executor.setKeepAliveSeconds(60);
        // Префикс имени пула потоков: после его установки нам удобно найти пул потоков, в котором находится задача обработки
        executor.setThreadNamePrefix("DailyAsync-");
        executor.initialize();
        return executor;
    }

    /**
     * Interceptor для перехвата асинхронных потоковых ошибок в случае таймаута
     * @return Interceptor
     */
    @Bean
    public CallableProcessingInterceptor callableProcessingInterceptor() {
        return new TimeoutCallableProcessingInterceptor() {
            @Override
            public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
                log.error("timeout!");
                return super.handleTimeout(request, task);
            }
        };
    }

    /**
     * TaskExecutor для методов контроллера возвращаюих StreamingResponseBody
     * @return TaskExecutor
     */
    @Override
    @Bean (name = "taskExecutor")
    public AsyncTaskExecutor getAsyncExecutor() {
        log.debug("Creating Default MVC Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
    }

    /**
     * Внедрение в mvc конфигурацию настроенного AsyncExecutor-а вместо дефолтного, добавление Interceptor-а
     * @param taskExecutor- свой кастомный Executor
     * @param callableProcessingInterceptor - Interceptor для регистрации
     * @return настроенный webMvcConfigurerConfigurer
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurerConfigurer(@Qualifier(value = "taskExecutor") AsyncTaskExecutor taskExecutor,
                                                       CallableProcessingInterceptor callableProcessingInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
                configurer.setDefaultTimeout(360000).setTaskExecutor(taskExecutor);
                configurer.registerCallableInterceptors(callableProcessingInterceptor);
                WebMvcConfigurer.super.configureAsyncSupport(configurer);
            }
        };
    }

    /**
     * Простой хандлер для отлова непойманных асинхронных ошибок
     * @return  AsyncUncaughtExceptionHandler
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
