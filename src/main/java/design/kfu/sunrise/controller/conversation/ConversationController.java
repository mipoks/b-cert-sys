package design.kfu.sunrise.controller.conversation;

import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import design.kfu.sunrise.domain.dto.conversation.ConversationMessageDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.conversation.Conversation;
import design.kfu.sunrise.domain.model.conversation.ConversationMessage;
import design.kfu.sunrise.service.conversation.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/conversations")
    public Page<Conversation> getConversations(@RequestParam(value = "size", defaultValue = "60") Integer size,
                                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @AuthenticationPrincipal(expression = "account") Account account) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        return conversationService.findConversations(account, pageable);
    }

    @PreAuthorize("@access.hasAccessToSendMessage(#account, #messageDTO)")
    @Limit(url = "/club/comment", limitType = LimitType.PER_MINUTE)
    @PostMapping("/conversation")
    public ConversationMessage createMessage(@AuthenticationPrincipal(expression = "account") Account account, ConversationMessageDTO messageDTO) {

        return conversationService.save(messageDTO, account);
    }

    @PreAuthorize("@access.hasAccessToConversation(#account, #conversation)")
    @GetMapping("/conversation/{conversation_id}")
    public Page<ConversationMessage> getConversationMessages(@RequestParam(value = "size", defaultValue = "60") Integer size,
                                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                             @AuthenticationPrincipal(expression = "account") Account account,
                                                             @PathVariable("conversation_id") Conversation conversation) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        return conversationService.findConversationMessages(conversation, pageable);
    }

    @Transactional
    @PreAuthorize("@access.hasAccessToConversation(#account, #conversation)")
    @GetMapping("/conversation/{conversation_id}/{message_id}")
    public ConversationMessage getConversationMessage(@AuthenticationPrincipal(expression = "account") Account account,
                                                      @PathVariable("conversation_id") Conversation conversation,
                                                      @PathVariable("message_id") ConversationMessage message) {
        if (conversation.getMessages().contains(message)) {
            return message;
        } else {
            return null;
        }
    }
}
