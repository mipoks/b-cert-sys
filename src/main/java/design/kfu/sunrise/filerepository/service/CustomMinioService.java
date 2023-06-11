package design.kfu.sunrise.filerepository.service;

import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniyar Zakiev
 */
@Service
public class CustomMinioService extends MinioService {

    public CustomMinioService(io.minio.MinioClient minioClient, MinioConfigurationProperties configurationProperties) {
        super(minioClient, configurationProperties);
    }

    public String getSignedUrl(Path path) throws com.jlefebure.spring.boot.minio.MinioException {
        try {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(path.toString())
                    .expiry(24, TimeUnit.HOURS)
                    .method(Method.GET)
                    .build();
            return minioClient.getPresignedObjectUrl(args);
        } catch (Exception e) {
            throw new com.jlefebure.spring.boot.minio.MinioException("Error while fetching files in Minio", e);
        }
    }
}
