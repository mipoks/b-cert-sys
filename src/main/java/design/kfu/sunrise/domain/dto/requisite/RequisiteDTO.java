package design.kfu.sunrise.domain.dto.requisite;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.embedded.PaymentInfo;
import design.kfu.sunrise.domain.model.embedded.RequisiteInfo;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.domain.model.payment.Requisite;
import design.kfu.sunrise.service.StaticService;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequisiteDTO {

    private Long id;
    private RequisiteInfo requisiteInfo;
    private Long authorId;
    private Boolean active;
    public static Requisite toRequisite(RequisiteDTO requisiteDTO, Account account) {
        return Requisite.builder()
                .requisiteInfo(requisiteDTO.getRequisiteInfo())
                .active(requisiteDTO.getActive())
                .authorId(account.getId())
                .build();
    }

    public static RequisiteDTO from(Requisite requisite) {
        return RequisiteDTO.builder()
                .id(requisite.getId())
                .requisiteInfo(requisite.getRequisiteInfo())
                .authorId(requisite.getAuthorId())
                .active(requisite.getActive())
                .build();
    }
}
