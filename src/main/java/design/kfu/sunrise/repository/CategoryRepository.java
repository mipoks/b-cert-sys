package design.kfu.sunrise.repository;

import design.kfu.sunrise.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findCategoriesByParentId(Long parentId, Pageable pageable);
    Page<Category> findCategoriesByParentIsNull(Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT c FROM Category c WHERE c.id = :id")
    Optional<Category> findByIdWithLock(Long id);
}
