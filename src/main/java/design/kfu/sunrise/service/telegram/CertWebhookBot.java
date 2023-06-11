package design.kfu.sunrise.service.telegram;

import design.kfu.sunrise.exception.ErrorType;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.repository.TelegramChatInfoRepository;
import design.kfu.sunrise.domain.model.TelegramChatInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.telegram.abilitybots.api.bot.AbilityWebhookBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Slf4j
@Setter
@ConditionalOnProperty(value = "bot.enabled", havingValue = "true")
public class CertWebhookBot extends AbilityWebhookBot implements TelegramBot {


    @Value("${bot.token}")
    private String TOKEN;

    @Value("${bot.name}")
    private String BOT_USERNAME;

    @Value("${bot.path}")
    private String BOT_PATH;

    private TelegramChatInfoRepository infoRepository;
    @Getter
    private ExecuteApiMethod<Chat> chat = method -> {

        try {
            return sendApiMethod(method);
        } catch (TelegramApiException e) {
            throw Exc.gen(ErrorType.UNEXPECTED_ERROR, e.getMessage());
        }
    };
    @Getter
    private ExecuteApiMethod<Integer> countOfUsers = method -> {
        try {
            return sendApiMethod(method);
        } catch (TelegramApiException e) {
            throw Exc.gen(ErrorType.UNEXPECTED_ERROR, e.getMessage());
        }
    };

    public CertWebhookBot(String token, String username, String path) {
        super(token, username, path);
    }

    @Override
    public boolean checkGlobalFlags(Update update) {
        log.info("result {}", Objects.nonNull(update.getMessage()) && (Objects.nonNull(update.getMessage().getLeftChatMember())
                || Objects.nonNull(update.getMessage().getNewChatMembers())
                || !update.getMessage().getNewChatMembers().isEmpty()));


        return Objects.nonNull(update.getMessage()) && (Objects.nonNull(update.getMessage().getLeftChatMember())
                || Objects.nonNull(update.getMessage().getNewChatMembers())
                || !update.getMessage().getNewChatMembers().isEmpty());
    }


    @Override
    public String getBotUsername() {
        log.info("BOT_USERNAME {}", BOT_USERNAME);
        return BOT_USERNAME;
    }

    @Override
    public long creatorId() {
        return 1234567891;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    public Ability sayNiceToPhoto() {
        return Ability.builder()
                .name("default") // DEFAULT ability is executed if user did not specify a command -> Bot needs to have access to messages (check FatherBot)
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> {
                    log.info("action started chatId {}", ctx.chatId());
                    infoRepository.save(
                            TelegramChatInfo
                                    .builder()
                                    .chatId(ctx.chatId().toString())
                                    .type(ctx.update().getMessage().getChat().getType())
                                    .title(ctx.update().getMessage().getChat().getTitle()).build());
                })
                .build();
    }



/*    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.info("update in inessageSenderImpl {}", update);
        try {
            execute(SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text(update.getMessage().getText()).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @Override
    public String getBotPath() {
        log.info(" bot paath {}", BOT_PATH);
        return BOT_PATH;
    }


}
