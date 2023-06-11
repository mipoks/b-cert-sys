package design.kfu.sunrise.esrepository.elastic;

import design.kfu.sunrise.domain.dto.club.ClubVDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Daniyar Zakiev
 */
@Repository
public interface ESClubRepository extends ElasticsearchRepository<ClubVDTO, Long> {
    Set<ClubVDTO> findAllByNameContaining(String like);
    Set<ClubVDTO> findAllByDescriptionContaining(String like);
}
