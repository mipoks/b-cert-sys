package design.kfu.sunrise.config;

import design.kfu.sunrise.repository.TelegramChatInfoRepository;
import design.kfu.sunrise.service.telegram.CertWebhookBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "bot.enabled", havingValue = "true")
public class TelegramBotConfig {
    @Value("${bot.token}")
    @NotNull
    @NotEmpty
    private String TOKEN;

    @Value("${bot.name}")
    @NotNull
    @NotEmpty
    private String BOT_USERNAME;

    @Autowired
    private TelegramChatInfoRepository repository;


    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url("https://f20d-188-234-15-100.ngrok.io").build();
    } // public address, now it is ngrok, in the future it will (i think) be the server address

    // Create it as
    @Bean
    public CertWebhookBot telegramBot(SetWebhook setWebhookInstance) throws TelegramApiException {

        CertWebhookBot certWebhookBot = new CertWebhookBot(TOKEN, BOT_USERNAME, "");
        certWebhookBot.setBOT_USERNAME(BOT_USERNAME);
        certWebhookBot.setInfoRepository(repository);
        certWebhookBot.setTOKEN(TOKEN);
        certWebhookBot.setBOT_PATH("");
        log.info("bot token is {}", TOKEN);
        DefaultWebhook defaultWebhook = new DefaultWebhook();
        defaultWebhook.setInternalUrl(
                "http://localhost:8086/callback"); // the port to start the server, on the localhost computer, on the server it
        // be the server address
        defaultWebhook.registerWebhook(certWebhookBot);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
        log.info("SetWebHook from bot {}", setWebhookInstance);
        telegramBotsApi.registerBot(certWebhookBot, setWebhookInstance);
        return certWebhookBot;

    }
}
