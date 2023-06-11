package design.kfu.sunrise.domain.model.util;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "Review", timeToLive = 60 * 60 * 24 * 30 * 2 /* 60 суток */)
public class Review {
    @Indexed
    private boolean viewed;

    @Indexed
    private String objectName;
    @Id
    private Long objectId;
    private String objectHash;
    @Indexed
    private Instant instant;

    @NotNull
    private boolean accept;
    private String reason;
}
