package design.kfu.sunrise.domain.model;

import design.kfu.sunrise.domain.model.embedded.ClubInfo;
import design.kfu.sunrise.domain.model.embedded.CostInfo;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.util.DigestUtils;

import jakarta.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
//@NamedEntityGraph(name = "Club.comments",
//        attributeNodes = @NamedAttributeNode("comments")
//)
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(length = 3000)
    private String description;

    @Embedded
    private CostInfo costInfo;

    //ToDo дописать
    @ManyToOne
    private Category category;

    @Embedded
    private ClubInfo clubInfo;

    //ToDo дописать
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account author;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "club_accounts",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> accounts = new HashSet<>();

    public Club addAccount(Account account) {
        accounts.add(account);
        return this;
    }

    public Club removeAccount(Account account) {
        accounts.remove(account);
        return this;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Comment> comments = new LinkedHashSet<>();

    public Club addComment(Comment comment) {
        comments.add(comment);
        return this;
    }

    public Club removeComment(Comment comment) {
        comments.remove(comment);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Club club = (Club) o;
        return id != null && Objects.equals(id, club.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String generateHash() {
        return DigestUtils.md5DigestAsHex((id + ":" + name.hashCode() + ":" + description.hashCode() + ":" + category.getId()).getBytes(StandardCharsets.UTF_8));
    }
}
