package design.kfu.sunrise.domain.model;

import design.kfu.sunrise.domain.model.embedded.AccountInfo;
import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ToString
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account")
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Todo index
    @Column(unique = true)
    private String email;
    private String hashPassword;

    @Embedded
    private AccountInfo accountInfo;

    //ToDo дописать
    @ManyToOne
    private Company partnerInfo;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "accounts", cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Club> clubs = new HashSet<>();

    public Account addClub(Club club) {
        club.addAccount(this);
        return this;
    }

    public Account removeClub(Club club) {
        club.removeAccount(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum Role {
        USER, ADMIN, PARTNER
    }
}
