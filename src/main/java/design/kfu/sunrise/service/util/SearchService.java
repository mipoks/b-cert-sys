package design.kfu.sunrise.service.util;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.dto.club.ClubVDTO;

import java.util.Set;

/**
 * @author Daniyar Zakiev
 */
public interface SearchService {
    Set<ClubVDTO> getClubsByName(String like);
    Set<ClubVDTO> getClubsByNameAndDescription(String like);

    Set<CategoryDTO> getCategoriesByName(String like);
    Set<CategoryDTO> getCategoriesByNameAndDescription(String like);

    void saveCategory(CategoryDTO category);
    void saveClub(ClubVDTO club);
    void deleteCategory(CategoryDTO category);
    void deleteClub(ClubVDTO club);
}
