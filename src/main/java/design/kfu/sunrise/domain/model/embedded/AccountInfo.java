package design.kfu.sunrise.domain.model.embedded;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import java.io.Serializable;

/**
 * @author Daniyar Zakiev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AccountInfo implements Serializable {
    @Getter
    @Setter
    @Column(unique = true)
    private String phone;

    @Getter
    @Setter
    private String telegramNickname;

    @Getter
    private boolean emailConfirmed;

    @Getter
    @Setter
    private boolean bannedWriteComment;

    @PrePersist
    public void setEmailConfirmed() {
        this.emailConfirmed = false;
    }

    public void setEmailConfirmed(boolean confirmed) {
        this.emailConfirmed = confirmed;
    }
}
