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
public class CostInfo implements Serializable {
    private Integer certificateCost;
    private Integer entryCost;

    private Integer discountCertificateCost;
    private Integer discountEntryCost;
}
