package design.kfu.sunrise.repository.conversation;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.conversation.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Daniyar Zakiev
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Page<Conversation> findConversationsByWithAccountsIn(List<Account> withAccounts, Pageable pageable);
//    Page<Conversation> findConversationsByWithAccountId1OrWithAccountId2Is(Account withAccountId1, Account withAccountId2, Pageable pageable);
}
