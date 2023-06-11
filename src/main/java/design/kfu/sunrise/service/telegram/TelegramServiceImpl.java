package design.kfu.sunrise.service.telegram;

import design.kfu.sunrise.repository.TelegramChatInfoRepository;
import design.kfu.sunrise.domain.model.TelegramChatInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMemberCount;
import org.telegram.telegrambots.meta.api.objects.Chat;

@Service
@Slf4j
@ConditionalOnProperty(value = "bot.enabled", havingValue = "true")
public class TelegramServiceImpl implements TelegramService {
    @Autowired
    private TelegramChatInfoRepository chatInfoRepository;
    @Autowired
    private TelegramBot certWebhookBot;

    @Override
    public Integer getUsersFromChat(String chatId) {
        log.info("chatId {}", chatId);
        GetChatMemberCount getChatMemberCount = new GetChatMemberCount();
        getChatMemberCount.setChatId(chatId);
        GetChat getChat = new GetChat();
        getChat.setChatId(chatId);
        Chat chat = certWebhookBot.getChat().executeCommand(getChat);
        log.info("chat is {}", chat);
        return certWebhookBot.getCountOfUsers().executeCommand(getChatMemberCount);
    }

    public void saveTelegramChatInfoByChatId(String chatId) {
        GetChat getChat = new GetChat();
        Chat chat = certWebhookBot.getChat().executeCommand(getChat);
        chatInfoRepository.save(
                TelegramChatInfo
                        .builder()
                        .chatId(chatId)
                        .title(chat.getTitle())
                        .type(chat.getType())
                        .build());
    }
}
