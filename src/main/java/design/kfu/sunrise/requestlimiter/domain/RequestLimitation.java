package design.kfu.sunrise.requestlimiter.domain;

import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "Limitation", timeToLive = 60 * 24 * 30 * 2 /* 1 сутки */)
public class RequestLimitation {

    //Hash берется от ip + url
    @Id
    private String hash;

    //IP адрес пользователя
    private String ip;
    //URL, куда идет запрос
    private String url;
    //Время, когда началось окно подсчета
    private Instant started;
    //Число сделанных запросов на эндпоинт
    private Integer requestCount;
    //Число разрешенных запросов на эндпоинт
    private Integer requestLimit;
    //Тип ограничения
    private LimitType limitType;



    public void incr() {
        Instant now = Instant.now();
        long seconds = now.getEpochSecond() - started.getEpochSecond();
        long minutes = seconds / 60;
        long millis = seconds * 1000 + (now.getNano() - started.getNano()) / 1000000;
        switch (limitType) {
            case PER_DAY -> {
            }
            case PER_HOUR -> {
                if (minutes > 60) {
                    started = started.plus(minutes / 60, ChronoUnit.HOURS);
                    requestCount = 1;
                } else {
                    requestCount++;
                }
            }
            case PER_MINUTE -> {
                if (seconds > 60) {
                    started = started.plus(seconds / 60, ChronoUnit.MINUTES);
                    requestCount = 1;
                } else {
                    requestCount++;
                }
            }
            case PER_SECOND -> {
                if (millis > 1000) {
                    started = started.plus(millis / 1000, ChronoUnit.SECONDS);
                    requestCount = 1;
                } else {
                    requestCount++;
                }
            }
        }
    }

    public boolean isOverLimit() {
        return requestCount > requestLimit;
    }
}
