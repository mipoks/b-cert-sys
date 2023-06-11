package design.kfu.sunrise.config;

import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import design.kfu.sunrise.domain.model.util.ActivationCode;
import design.kfu.sunrise.domain.model.util.Notification;
import design.kfu.sunrise.serializer.CustomGenericJackson2JsonRedisSerializer;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Daniyar Zakiev
 */
@Configuration

@ComponentScan(basePackages = {"com.jlefebure.spring.boot.minio"})
public class MinioConfig {

    @Value("${spring.minio.url}")
    private String url;

    @Value("${spring.minio.access-key}")
    private String accessKey;

    @Value("${spring.minio.secret-key}")
    private String secretKey;

    @Value("${spring.minio.bucket}")
    private String bucketName;

//    @SneakyThrows
//    @Bean
//    public MinioClient minioClient() {
//        MinioClient minioClient =
//                MinioClient.builder()
//                        .endpoint(url, 9000, false)
//                        .credentials(accessKey, secretKey)
//                        .build();
//
//
//        boolean found =
//                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//        if (!found) {
//            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
//        }
//        return minioClient;
//    }

//    @Bean
//    public MinioConfigurationProperties minioConfigurationProperties() {
//        MinioConfigurationProperties configurationProperties = new MinioConfigurationProperties();
//        configurationProperties.setBucket(bucketName);
//        configurationProperties.setSecure(false);
//        configurationProperties.setAccessKey(accessKey);
//        configurationProperties.setSecretKey(secretKey);
//        configurationProperties.setUrl(url);
//        return minioConfigurationProperties();
//    }


}
