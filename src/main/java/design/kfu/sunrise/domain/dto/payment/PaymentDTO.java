package design.kfu.sunrise.domain.dto.payment;

import design.kfu.sunrise.domain.dto.requisite.RequisiteDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.embedded.PaymentInfo;
import design.kfu.sunrise.domain.model.embedded.RequisiteInfo;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.service.StaticService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;
    private Long accountId;
    private PaymentInfo paymentInfo;
    private Payment.Status status;
    private String comment;
    private RequisiteDTO requisiteDTO;

    public static Payment toPayment(Club club, Account account) {
        return Payment.builder()
                .account(account)
                .paymentInfo(
                        PaymentInfo.builder()
                                .clubId(club.getId())
                                .comment(StaticService.getAnonymousService().generateComment(5))
                                .build()
                )
                .status(Payment.Status.NEW)
                .requisite(
                        StaticService.getRequisiteService().getAvailableFor(account)
                )
                .build();
    }

    public static PaymentDTO from(Payment payment) {
        return PaymentDTO.builder()
                .accountId(payment.getAccount().getId())
                .id(payment.getId())
                .paymentInfo(payment.getPaymentInfo())
                .requisiteDTO(RequisiteDTO.from(payment.getRequisite()))
                .status(payment.getStatus())
                .build();
    }
}
