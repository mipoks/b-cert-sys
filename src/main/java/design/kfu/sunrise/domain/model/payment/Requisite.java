package design.kfu.sunrise.domain.model.payment;

import design.kfu.sunrise.domain.model.BaseEntity;
import design.kfu.sunrise.domain.model.embedded.RequisiteInfo;
import lombok.*;

import jakarta.persistence.*;

/**
 * @author Daniyar Zakiev
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "requisite")
public class Requisite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private RequisiteInfo requisiteInfo;
    private Long authorId;
    private Boolean active;
}
