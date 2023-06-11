package design.kfu.sunrise.controller.chat;

import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.chat.ChatMessage;
import design.kfu.sunrise.security.details.UserDetailsImpl;
import design.kfu.sunrise.service.access.AccountAccessService;
import design.kfu.sunrise.service.access.PermissionService;
import design.kfu.sunrise.service.chat.ChatService;
import design.kfu.sunrise.util.model.Permission;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;
import java.security.Principal;
import java.util.List;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private AccountAccessService accountAccessService;

    @Autowired
    private PermissionService permissionService;

    @Operation(description = "Get chat messages",summary = "Returns Page of messages")
    @PermitAll
    @GetMapping("/chat")
    public Page<ChatMessage> getMessages(@RequestParam(value = "size", defaultValue = "60") Integer size,
                                         @RequestParam(value = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        return chatService.getMessages(pageable);
    }

    @Operation(description = "Delete chat message",summary = "Returns deleted message")
    @PreAuthorize("@access.hasAccessToBan(#account)")
    @DeleteMapping("/chat/{chat_id}")
    public ChatMessage deleteMessage(@AuthenticationPrincipal(expression = "account") Account account, @PathVariable("chat_id") ChatMessage message) {
        return chatService.delete(message);
    }

    @Operation(description = "Set chat is inactive for all users",summary = "Returns boolean")
    @PreAuthorize("@access.hasAccessToBan(#account)")
    @PostMapping("/chat")
    public boolean switchChat(@AuthenticationPrincipal(expression = "account") Account account, @RequestBody Boolean switchTo) {
        return chatService.setEnabled(switchTo);
    }

    @Operation(description = "Get permissions for Chat",summary = "Returns list of permission")
    @PermitAll
    @GetMapping("/chat/permissions")
    public List<Permission> getPermissions(@AuthenticationPrincipal(expression = "account") Account account) {
        return permissionService.generateDefault(List.of("writeMessage"),
                List.of("writeMessage", "deleteMessage"),
                List.of("writeMessage", "deleteMessage"),
                account,
                accountAccessService);
    }

    //ToDo проверить, что все работает
    @Limit(url = "/chat/msg", limitType = LimitType.PER_MINUTE)
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage, Message message) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        Principal userPrincipal = headerAccessor.getUser();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) userPrincipal;
        UserDetailsImpl temp = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        Account account = temp.getAccount();
        if (account != null) {
            chatService.processMessage(chatMessage, account);
        }
    }

}
