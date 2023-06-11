package design.kfu.sunrise.service.telegram;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.WebhookBot;

public interface TelegramBot extends WebhookBot {
    default SendMessage createMessageFrom(String messageText, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageText)
                .build();
    }

    @Override
    String getBotUsername();

    @Override
    String getBotToken();

    @Override
    BotApiMethod<?> onWebhookUpdateReceived(Update update);

    @Override
    String getBotPath();

    void setTOKEN(String TOKEN);

    void setBOT_USERNAME(String BOT_USERNAME);

    void setBOT_PATH(String BOT_PATH);

    void setChat(ExecuteApiMethod<org.telegram.telegrambots.meta.api.objects.Chat> chat);

    void setCountOfUsers(ExecuteApiMethod<Integer> countOfUsers);

    ExecuteApiMethod<org.telegram.telegrambots.meta.api.objects.Chat> getChat();

    ExecuteApiMethod<Integer> getCountOfUsers();
}
