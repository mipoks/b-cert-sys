package design.kfu.sunrise.repository;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
//    @EntityGraph(value = "Club.comments")
    Optional<Club> findById(Long id);
    Set<Club> findAllByAuthor(Account account);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT c FROM Club c WHERE c.id = :id")
    Optional<Club> findByIdWithLock(Long id);
}
