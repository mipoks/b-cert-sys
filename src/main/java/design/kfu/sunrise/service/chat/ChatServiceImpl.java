package design.kfu.sunrise.service.chat;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.chat.ChatMessage;
import design.kfu.sunrise.repository.chat.ChatMessageRepository;
import design.kfu.sunrise.service.util.AnonymousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
@Service
public class ChatServiceImpl implements ChatService {

    private static boolean enabled = true;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AnonymousService anonymousService;

    @Autowired
    private ChatMessageRepository messageRepository;

    @Override
    public Page<ChatMessage> getMessages(Pageable pageable) {
        return messageRepository.findAllOrderByCreated(pageable);
    }

    public ChatMessage save(ChatMessage chatMessage) {
        return messageRepository.save(chatMessage);
    }

    @Override
    public ChatMessage delete(ChatMessage chatMessage) {
        messageRepository.delete(chatMessage);
        return chatMessage;
    }


    @Override
    public ChatMessage processMessage(ChatMessage chatMessage, Account account) {
        if (!enabled) {
            throw new IllegalStateException("Чат временно отключен");
        }
        chatMessage.setSenderId(account.getId());
        chatMessage.setSenderRole(account.getRole().name());
        String senderName = anonymousService.hideEmail(account.getEmail());
        chatMessage.setSenderName(senderName);
        chatMessage.setCreated(Instant.now());
        chatMessage = save(chatMessage);
        messagingTemplate.convertAndSend("/messages", chatMessage);
        return chatMessage;
    }

    @Override
    public Boolean setEnabled(Boolean onOff) {
        enabled = onOff;
        return enabled;
    }
}
