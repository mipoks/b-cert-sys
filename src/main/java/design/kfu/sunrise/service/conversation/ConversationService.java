package design.kfu.sunrise.service.conversation;

import design.kfu.sunrise.domain.dto.conversation.ConversationMessageDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.conversation.Conversation;
import design.kfu.sunrise.domain.model.conversation.ConversationMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Daniyar Zakiev
 */
public interface ConversationService {
    Page<Conversation> findConversations(Account account, Pageable pageable);
    Page<ConversationMessage> findConversationMessages(Conversation conversation, Pageable pageable);
    ConversationMessage save(ConversationMessageDTO conversationMessage, Account account);

    Conversation findOrCreate(Long conversationId, Long recipientId, Account account);
    Conversation findOrNull(Long conversationId);
    ConversationMessage findMessageOrNull(Long messageId);
}
