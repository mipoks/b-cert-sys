package design.kfu.sunrise.controller.telegram;

import design.kfu.sunrise.service.telegram.CertWebhookBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Controller
@ConditionalOnProperty(value = "bot.enabled", havingValue = "true")
public class BotMessageRecieveController {

    @Autowired
    private CertWebhookBot certWebhookBot;

    @PostMapping("/callback")
    @ResponseStatus(HttpStatus.OK)
    public void getUpdateWithDifferentUrl(@RequestBody Update update) {
        log.info("some update recieved from callback/adam path {}", update.toString());
        certWebhookBot.onWebhookUpdateReceived(update);
    }
}