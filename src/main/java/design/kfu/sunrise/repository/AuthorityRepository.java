package design.kfu.sunrise.repository;

import design.kfu.sunrise.domain.model.Authority;
import design.kfu.sunrise.domain.model.embedded.ClubsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, ClubsInfo> {
}
