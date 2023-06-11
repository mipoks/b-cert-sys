package design.kfu.sunrise.domain.model.embedded;

import lombok.Builder;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Daniyar Zakiev
 */
@Embeddable
@Builder
public class ClubsInfo implements Serializable {

    @Column(name="ACCOUNT_ID")
    private Long dAccountId;

    @Column(name="CLUB_ID")
    private Long clubId;

    public ClubsInfo() {

    }

    public ClubsInfo(Long accountId, Long clubId) {
        this.dAccountId = accountId;
        this.clubId = clubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClubsInfo clubsInfo = (ClubsInfo) o;
        return Objects.equals(clubId, clubsInfo.clubId) && Objects.equals(dAccountId, clubsInfo.dAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clubId, dAccountId);
    }

    @Column(name="ACCOUNT_ID")
    public Long getAccountId() {
        return dAccountId;
    }

    public void setAccountId(Long accountId) {
        this.dAccountId = accountId;
    }


    @Column(name="CLUB_ID")
    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }
}
