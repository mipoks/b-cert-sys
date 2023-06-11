package design.kfu.sunrise.domain.event;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
public class AbstractEvent {
    protected String requestId;
    protected String domain;
}
