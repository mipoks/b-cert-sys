package design.kfu.sunrise.domain.model.conversation;

import design.kfu.sunrise.domain.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;

/**
 * @author Daniyar Zakiev
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "conversation_message")
//        indexes = {
//                @Index(columnList = "created", name = "created_idx")
//        })
public class ConversationMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String senderRole;
    private Long repliedMessage;
    @ManyToOne
    private Conversation conversation;

    private String content;
    private String mimeType;
}
