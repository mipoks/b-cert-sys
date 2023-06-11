package design.kfu.sunrise.domain.dto.conversation;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.conversation.ConversationMessage;
import design.kfu.sunrise.service.StaticService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Daniyar Zakiev
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationMessageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long recipientId;
    private Long repliedId;
    @NotNull
    private Long conversationId;
    @NotNull
    @Size(min = 2, max = 4096)
    private String content;
    private String mimeType;

    public static ConversationMessage toConversationMessage(ConversationMessageDTO messageDTO, Account account) {
        return ConversationMessage.builder()
                .senderRole(account.getRole().name())
                .conversation(StaticService.getConversationService().findOrCreate(messageDTO.getConversationId(), messageDTO.getRecipientId(), account))
                .repliedMessage(StaticService.getConversationService().findMessageOrNull(messageDTO.getRepliedId()).getId())
                .mimeType(messageDTO.getMimeType())
                .content(messageDTO.getContent())
                .build();
    }

    public static ConversationMessageDTO from(ConversationMessage message) {
        return ConversationMessageDTO.builder()
                .content(message.getContent())
                .conversationId(message.getConversation().getId())
                .id(message.getId())
                .repliedId(message.getRepliedMessage() == null ? null : message.getRepliedMessage())
                .mimeType(message.getMimeType())
                .build();
    }
}
