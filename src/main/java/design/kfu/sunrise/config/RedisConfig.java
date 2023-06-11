package design.kfu.sunrise.config;

import design.kfu.sunrise.domain.model.util.ActivationCode;
import design.kfu.sunrise.domain.model.util.Notification;
import design.kfu.sunrise.serializer.CustomGenericJackson2JsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Daniyar Zakiev
 */
@Configuration
@EnableRedisRepositories(basePackageClasses = {ActivationCode.class, Notification.class})
public class RedisConfig {

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHostName);
        configuration.setPort(redisPort);
        configuration.setPassword(password);
        return new LettuceConnectionFactory(configuration);
    }

/*
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(lettuceConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

/*
    */
/**
     * Сериализует класс в json добавляя атрибут @class, указывающий на конкретный класс
     * @return
     *//*

    @Bean(name = "springSessionDefaultRedisSerializer")
    public GenericJackson2JsonRedisSerializer getGenericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
*/

    @Bean
    public RedisTemplate<String, Object> getRedisTemplate(
    ) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
       /* redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());*/

        redisTemplate.setDefaultSerializer(new CustomGenericJackson2JsonRedisSerializer());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }
}
