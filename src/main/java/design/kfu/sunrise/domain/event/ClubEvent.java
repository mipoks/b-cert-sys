package design.kfu.sunrise.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
public class ClubEvent extends AbstractEvent {
    private String event;
    private Object object;

    public ClubEvent(String domain, String event, Object object, String requestId) {
        this.event = event;
        this.domain = domain;
        this.requestId = requestId;
        this.object = object;
    }

    public ClubEvent(String domain, String event, Object object) {
        this.event = event;
        this.domain = domain;
        this.object = object;
    }

    @Getter
    @AllArgsConstructor
    public enum Event {
        SAVE("save"),
        UPDATE("update"),
        PUBLISH("publish"),
        DECLINE("decline"),
        CLUB_MOVE("club_move"),
        CLUB_DEACTIVATE("club_deactivate"),
        COMMENT_UPDATE("comment_update"),
        ACCOUNT_EXIT("account_exit"),
        ACCOUNT_ENTER("account_enter"),
        IMAGE_ADD("image_add"),

        IMAGE_REMOVE("image_remove");
        private final String name;
    }
}
