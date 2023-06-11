package design.kfu.sunrise.service;

import design.kfu.sunrise.domain.event.CommentEvent;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.feign.FeignIntegrateClient;
import design.kfu.sunrise.feign.MessageDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author Daniyar Zakiev
 */
@Service
@Slf4j
@ConditionalOnProperty(value = "use.integrator", havingValue = "true")
public class FeignEventListener {

    @Autowired
    private FeignIntegrateClient feignIntegrateClient;

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    //Отправка сообщения через Feign в integrator
    @EventListener
    public void handleCommentEventPublish(CommentEvent event) {
        if (event.getEvent().equals(CommentEvent.Event.PUBLISH.getName())) {
            Comment comment = (Comment) event.getObject();
            if (comment.getTelegramAccountId() == null) {
                Long telegramChatId = comment.getClub().getClubInfo().getTelegramChatId();
                String text = comment.getValue();
                String hidedEmail = StaticService.getAnonymousService().hideEmail(comment.getAccount().getEmail());

                MessageDTO messageDTO = MessageDTO.builder()
                        .telegramChatId(telegramChatId)
                        .text(text)
                        .clubId(comment.getClub().getId())
                        .messageId(comment.getId())
                        .userName(hidedEmail)
                        .build();


                feignIntegrateClient.sendComment(messageDTO);
            }
        }
    }


}
