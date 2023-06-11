package design.kfu.sunrise.service.integrator;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.chat.ChatMessage;
import design.kfu.sunrise.feign.FeignIntegrateClient;
import design.kfu.sunrise.repository.chat.ChatMessageRepository;
import design.kfu.sunrise.service.chat.ChatService;
import design.kfu.sunrise.service.util.AnonymousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
@Service
@ConditionalOnProperty(value = "use.integrator", havingValue = "true")
public class IntegratorServiceImpl implements IntegratorService {

    @Autowired
    private FeignIntegrateClient feignIntegrateClient;
    @Override
    public Integer getUserCountFromChat(Long chatId) {
        return feignIntegrateClient.getUsersCountOfChat(chatId.toString());
    }
}
