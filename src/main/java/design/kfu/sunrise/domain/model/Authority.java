package design.kfu.sunrise.domain.model;


import design.kfu.sunrise.domain.model.embedded.ClubsInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "club_accounts")
public class Authority {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride( name = "dAccountId", column = @Column(name = "ACCOUNT_ID")),
            @AttributeOverride( name = "clubId", column = @Column(name = "CLUB_ID"))
    })
    private ClubsInfo clubsInfo;

    @ElementCollection(targetClass=AuthorityType.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="authority_type")
    private Set<AuthorityType> authorityTypes = new HashSet<>();


    public Authority addAuthotityType(AuthorityType type) {
        authorityTypes.add(type);
        return this;
    }

    public Authority removeAuthorityType(AuthorityType type) {
        authorityTypes.remove(type);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Authority that = (Authority) o;
        return clubsInfo != null && clubsInfo.equals(that.clubsInfo);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum AuthorityType {
        READ_CLUB_COMMENTS, WRITE_CLUB_COMMENTS
    }




    public ClubsInfo getClubsInfo() {
        return clubsInfo;
    }

//    @Transient
    public void setClubsInfo(ClubsInfo clubsInfo) {
        this.clubsInfo = clubsInfo;
    }

//    @Transient
    public Set<AuthorityType> getAuthorityTypes() {
        return authorityTypes;
    }

//    @Transient
    public void setAuthorityTypes(Set<AuthorityType> authorityTypes) {
        this.authorityTypes = authorityTypes;
    }
}
