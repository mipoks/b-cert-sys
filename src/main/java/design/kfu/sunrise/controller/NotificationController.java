package design.kfu.sunrise.controller;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.util.Notification;
import design.kfu.sunrise.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Daniyar Zakiev
 */
@RestController
@RequestMapping(value = "/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(description = "Get notifications", summary = "Returns list of notifications")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notifications")
    public List<Notification> getNotifications(@AuthenticationPrincipal(expression = "account") Account account) {
        return notificationService.findByAccount(account);
    }

    @Operation(description = "Mark notification as red", summary = "Returns boolean")
    @PreAuthorize("@access.hasAccessToReadNotification(#account, #notification)")
    @PutMapping("/notification/{notification_id}")
    public Boolean markAsRed(@PathVariable("notification_id") Notification notification, @AuthenticationPrincipal(expression = "account") Account account) {
        notificationService.markAsRed(notification);
        return true;
    }
}
