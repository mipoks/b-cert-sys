package design.kfu.sunrise.service.util;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.dto.club.ClubVDTO;
import design.kfu.sunrise.esrepository.elastic.ESCategoryRepository;
import design.kfu.sunrise.esrepository.elastic.ESClubRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ESCategoryRepository esCategoryRepository;

    @Autowired
    private ESClubRepository esClubRepository;

    @Override
    public Set<ClubVDTO> getClubsByName(String like) {
        return esClubRepository.findAllByNameContaining(like);
    }

    @Override
    public Set<ClubVDTO> getClubsByNameAndDescription(String like) {
        Set<ClubVDTO> allByDescriptionContaining = esClubRepository.findAllByDescriptionContaining(like);
        allByDescriptionContaining
                .addAll(esClubRepository.findAllByNameContaining(like));
        return allByDescriptionContaining;
    }

    @Override
    public Set<CategoryDTO> getCategoriesByName(String like) {
        Set<CategoryDTO> allByName = esCategoryRepository.findAllByNameContaining(like);
        return allByName;
    }

    @Override
    public Set<CategoryDTO> getCategoriesByNameAndDescription(String like) {
        Set<CategoryDTO> allByDescriptionContaining = esCategoryRepository.findAllByDescriptionContaining(like);
        allByDescriptionContaining
                .addAll(esCategoryRepository.findAllByNameContaining(like));
        return allByDescriptionContaining;
    }

    @Override
    public void saveCategory(CategoryDTO category) {
        esCategoryRepository.save(category);
    }

    @Override
    public void saveClub(ClubVDTO club) {
        esClubRepository.save(club);
    }

    @Override
    public void deleteCategory(CategoryDTO category) {
        esCategoryRepository.delete(category);
    }

    @Override
    public void deleteClub(ClubVDTO club) {
        esClubRepository.delete(club);
    }


}
