package design.kfu.sunrise.service.mail.context;

import lombok.*;

import java.util.Map;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbstractEmailContext {
    private String from;
    private String to;
    private String subject;
    private String email;
    private String attachment;
    private String fromDisplayName;
    private String emailLanguage;
    private String displayName;
    private String templateLocation;
    private Map<String, Object> context;
}
