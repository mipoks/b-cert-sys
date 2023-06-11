package design.kfu.sunrise.domain.model.embedded;

import lombok.*;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CommentInfo implements Serializable {
    private boolean visible;
}
