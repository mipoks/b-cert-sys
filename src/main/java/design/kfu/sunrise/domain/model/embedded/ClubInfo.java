package design.kfu.sunrise.domain.model.embedded;

import design.kfu.sunrise.util.model.Country;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ClubInfo implements Serializable {

    private Boolean expired;
    private Boolean visible;
    //ToDo сделать проверку на условие
    @NotNull
    private String expireCondition;

    private Long telegramChatId;
    @Enumerated(EnumType.STRING)
    private Country country;

    @ElementCollection(targetClass = String.class)
    @Enumerated(EnumType.STRING)
    private List<String> images = new ArrayList<>();
    private Boolean isStock;

    //Различные варианты: истечение по количеству участников, истечение по времени
    public boolean isExpired() {
        return expired;
    }

    @PrePersist
    public void expiredFalse() {
        this.visible = true;
        this.expired = false;
    }
}
