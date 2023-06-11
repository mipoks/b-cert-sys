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
public class RequisiteInfo implements Serializable {
    private String cardNumber;
    private String cardHolder;
}
