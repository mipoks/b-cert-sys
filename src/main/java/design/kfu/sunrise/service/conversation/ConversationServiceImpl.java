package design.kfu.sunrise.service.conversation;

import design.kfu.sunrise.domain.dto.conversation.ConversationMessageDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.conversation.Conversation;
import design.kfu.sunrise.domain.model.conversation.ConversationMessage;
import design.kfu.sunrise.repository.conversation.ConversationMessageRepository;
import design.kfu.sunrise.repository.conversation.ConversationRepository;
import design.kfu.sunrise.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
@Service
public class ConversationServiceImpl implements ConversationService{

    @Autowired
    private AccountService accountService;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ConversationMessageRepository conversationMessageRepository;

    @Override
    public Page<Conversation> findConversations(Account account, Pageable pageable) {
//        return conversationRepository.findConversationsByWithAccountId1OrWithAccountId2Is(account, account, pageable);
        return conversationRepository.findConversationsByWithAccountsIn(List.of(account), pageable);
    }

    @Override
    public Page<ConversationMessage> findConversationMessages(Conversation conversation, Pageable pageable) {
        return conversationMessageRepository.findAllByConversation(conversation, pageable);
    }

    @Override
    public ConversationMessage save(ConversationMessageDTO conversationMessage, Account account) {
        return conversationMessageRepository.save(ConversationMessageDTO.toConversationMessage(conversationMessage, account));
    }

    @Override
    public Conversation findOrCreate(Long conversationId, Long recipientId, Account account) {
        Optional<Conversation> conversationOptional = conversationRepository.findById(conversationId);
        if (conversationOptional.isPresent()) {
            return conversationOptional.get();
        } else {
            Conversation conversation = Conversation.builder()
                    .isRead(false)
                    .messages(new HashSet<>())
                    .withAccounts(List.of(account, accountService.findOrThrow(recipientId)))
                    .build();
            conversationRepository.save(conversation);
            return conversation;
        }
    }

    @Override
    public Conversation findOrNull(Long conversationId) {
        return conversationRepository.findById(conversationId).orElse(null);
    }

    @Override
    public ConversationMessage findMessageOrNull(Long messageId) {
        return conversationMessageRepository.findById(messageId).orElse(null);
    }
}
