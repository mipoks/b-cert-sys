package design.kfu.sunrise.esrepository.elastic;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Daniyar Zakiev
 */
@Repository
public interface ESCategoryRepository extends ElasticsearchRepository<CategoryDTO, Long> {
    Set<CategoryDTO> findAllByNameContaining(String like);
    Set<CategoryDTO> findAllByDescriptionContaining(String like);
}
