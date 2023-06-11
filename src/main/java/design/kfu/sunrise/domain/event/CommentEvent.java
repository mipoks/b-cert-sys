package design.kfu.sunrise.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
public class CommentEvent extends AbstractEvent {
    private String event;
    private Object object;

    public CommentEvent(String domain, String event, Object object, String requestId) {
        this.event = event;
        this.domain = domain;
        this.requestId = requestId;
        this.object = object;
    }

    public CommentEvent(String domain, String event, Object object) {
        this.event = event;
        this.domain = domain;
        this.object = object;
    }

    @Getter
    @AllArgsConstructor
    public enum Event {
        SAVE("save"),
        UPDATE("update"),
        DELETE("delete"),
        PUBLISH("publish"),
        DECLINE("decline"),
        IMAGE_ADD("image_add"),
        IMAGE_REMOVE("image_remove");
        private final String name;
    }
}
