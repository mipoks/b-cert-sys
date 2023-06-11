package design.kfu.sunrise.repository.chat;

import design.kfu.sunrise.domain.model.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Daniyar Zakiev
 */
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    Page<ChatMessage> findAllOrderByCreated(Pageable pageable);
}
