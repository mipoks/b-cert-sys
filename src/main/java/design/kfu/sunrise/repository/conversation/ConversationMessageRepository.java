package design.kfu.sunrise.repository.conversation;

import design.kfu.sunrise.domain.model.conversation.Conversation;
import design.kfu.sunrise.domain.model.conversation.ConversationMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Daniyar Zakiev
 */
@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Long> {
    Page<ConversationMessage> findAllByConversation(Conversation conversation, Pageable pageable);
}
