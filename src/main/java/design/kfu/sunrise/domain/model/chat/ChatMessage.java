package design.kfu.sunrise.domain.model.chat;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
@Builder
@Data
@RedisHash(value = "ChatMessage", timeToLive = 60 * 60 * 24 * 3 /* 3 суток */)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Size(min = 2, max = 4096)
    private long id;

    private Long senderId;
    private String senderRole;
    private String senderName;
    private Long repliedId;

    private String content;
    private String mimeType;

    @Indexed
    private Instant created;
}
