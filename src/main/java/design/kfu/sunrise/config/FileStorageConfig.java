package design.kfu.sunrise.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daniyar Zakiev
 */
@Configuration
@ComponentScan(basePackages = {"design.kfu.sunrise.filerepository"})
public class FileStorageConfig {
}
