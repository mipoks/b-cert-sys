package design.kfu.sunrise.domain.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

//    @Version
//    private Long version;

    @Getter
    private Instant created;
    @Getter
    private Instant updated;

    @PrePersist
    public void setCreated() {
        this.created = Instant.now();
    }

    @PreUpdate
    public void setUpdated() {
        this.updated = Instant.now();
    }
}
