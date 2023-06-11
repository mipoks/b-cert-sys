package design.kfu.sunrise.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
public class CategoryEvent extends AbstractEvent {
    private String event;
    private Object object;

    public CategoryEvent(String domain, String event, Object object, String requestId) {
        this.event = event;
        this.domain = domain;
        this.requestId = requestId;
        this.object = object;
    }

    public CategoryEvent(String domain, String event, Object object) {
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
        DECLINE("decline");
        private final String name;
    }
}
