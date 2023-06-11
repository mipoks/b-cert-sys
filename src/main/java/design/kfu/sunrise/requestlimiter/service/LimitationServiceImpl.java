package design.kfu.sunrise.requestlimiter.service;

import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.domain.RequestLimitation;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitCount;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import design.kfu.sunrise.requestlimiter.repository.RequestLimitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniyar Zakiev
 */
@Service
public class LimitationServiceImpl implements LimitationService {

    @Autowired
    private RequestLimitationRepository requestLimitationRepository;


    private int getRequestCount(Limit limit) {
        if (limit.requestCount() == LimitCount.OPTIMAL) {
            switch (limit.limitType()) {
                case PER_SECOND -> {
                    return LimitCount.FOR_SECOND;
                }
                case PER_MINUTE -> {
                    return LimitCount.FOR_MINUTE;
                }
                case PER_HOUR -> {
                    return LimitCount.FOR_HOUR;
                }
                case PER_DAY -> {
                    return LimitCount.FOR_DAY;
                }
            }
        }
        return limit.requestCount();
    }

    @Override
    public boolean checkRequestLimit(Limit limit, HttpServletRequest request) {
        String url = limit.url();
        String ip = request.getRemoteAddr();
        int requestLimit = getRequestCount(limit);

        LimitType limitType = limit.limitType();

        String id = UUID.nameUUIDFromBytes((ip + url).getBytes()).toString();
        Optional<RequestLimitation> optionalRequestLimitation = requestLimitationRepository.findById(id);

        if (optionalRequestLimitation.isPresent()) {
            return checkRequestLimit(optionalRequestLimitation.get(), limitType);
        } else {
            return createRequestLimit(id, ip, url, requestLimit, limitType);
        }
    }

    private boolean createRequestLimit(String id, String ip, String url, int limit, LimitType limitType) {
        RequestLimitation limitation = RequestLimitation.builder()
                .requestCount(1)
                .started(Instant.now())
                .hash(id)
                .limitType(limitType)
                .ip(ip)
                .requestLimit(limit)
                .url(url)
                .build();
        requestLimitationRepository.save(limitation);
        return true;
    }

    private boolean checkRequestLimit(RequestLimitation requestLimitation, LimitType limitType) {
        requestLimitation.incr();
        boolean result = requestLimitation.isOverLimit();
        if (!result) {
            requestLimitationRepository.save(requestLimitation);
        }
        return !result;
    }
}
