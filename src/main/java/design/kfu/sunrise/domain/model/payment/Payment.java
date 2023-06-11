package design.kfu.sunrise.domain.model.payment;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.BaseEntity;
import design.kfu.sunrise.domain.model.embedded.PaymentInfo;
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
@Table(name = "payment")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Requisite requisite;
    private Status status;

    @Embedded
    private PaymentInfo paymentInfo;
    @ManyToOne
    private Account account;


    @AllArgsConstructor
    @Getter
    public enum Status {
        NEW("new"),
        SEND("send"),
        CONFIRMED("confirmed"),
        REJECTED("rejected"),
        REFUND("refund");
        private final String name;
    }
}
