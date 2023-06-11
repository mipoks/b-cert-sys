package design.kfu.sunrise.domain.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
//Todo comments to fields
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    /**
     * просмотрено
     */
    private boolean viewed;
    /**
     * имя
     */
    private String objectName;
    /**
     * id
     */
    private Long objectId;
    /**
     * hash
     */
    private String objectHash;
    /**
     * Дата поступления
     */
    private Instant instant;
    /**
     * Результат оценки
     */
    private boolean accept;
    /**
     * Причина
     */
    private String reason;
}
