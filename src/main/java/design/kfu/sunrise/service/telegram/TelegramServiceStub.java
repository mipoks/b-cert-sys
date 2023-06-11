package design.kfu.sunrise.service.telegram;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "bot.enabled", havingValue = "false")
public class TelegramServiceStub implements TelegramService{
    @Override
    public Integer getUsersFromChat(String chatId) {
        return 668;
    }
}
