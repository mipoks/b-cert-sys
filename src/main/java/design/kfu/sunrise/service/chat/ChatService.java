package design.kfu.sunrise.service.chat;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Daniyar Zakiev
 */
public interface ChatService {
    Page<ChatMessage> getMessages(Pageable pageable);
    ChatMessage delete(ChatMessage chatMessage);
    ChatMessage processMessage(ChatMessage chatMessage, Account account);
    Boolean setEnabled(Boolean onOff);
}
