package design.kfu.sunrise.domain.model.conversation;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Daniyar Zakiev
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "conversation",
indexes = {
        @Index(columnList = "created")
})
public class Conversation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Account> withAccounts = new ArrayList<>();

    @OneToMany
    private Set<ConversationMessage> messages;

    private boolean isRead;

    public boolean isAccountInConversation(Account account) {
        return withAccounts.contains(account);
    }
}
