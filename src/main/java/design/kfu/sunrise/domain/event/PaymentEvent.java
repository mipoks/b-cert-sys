package design.kfu.sunrise.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
public class PaymentEvent extends AbstractEvent {
    private String event;
    private Object object;

    public PaymentEvent(String domain, String event, Object object, String requestId) {
        this.event = event;
        this.domain = domain;
        this.requestId = requestId;
        this.object = object;
    }

    public PaymentEvent(String domain, String event, Object object) {
        this.event = event;
        this.domain = domain;
        this.object = object;
    }

    @Getter
    @AllArgsConstructor
    public enum Event {
        NEW("new"),
        SEND("send"),
        CONFIRM("confirm"),
        REJECT("reject"),
        REFUND("refund");
        private final String name;
    }
}
