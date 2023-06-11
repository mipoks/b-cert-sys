package design.kfu.sunrise.domain.model.util;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "Notification", timeToLive = 60 * 60 * 24 * 30 * 2 /* 60 суток */)
public class Notification {
    @Id
    private Long id;
    @Indexed
    private Long accountId;
    private Instant instant;
    private String name;
    private String description;
    private String url;
    @Indexed
    private boolean read;
}
