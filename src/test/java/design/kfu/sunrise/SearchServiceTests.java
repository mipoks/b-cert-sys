package design.kfu.sunrise;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.dto.club.ClubVDTO;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.repository.CategoryRepository;
import design.kfu.sunrise.repository.ClubRepository;
import design.kfu.sunrise.service.util.SearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class SearchServiceTests {

    /*
    Set<ClubVDTO> getClubsByName(String like);
    Set<ClubVDTO> getClubsByNameAndDescription(String like);

    Set<CategoryDTO> getCategoriesByName(String like);
    Set<CategoryDTO> getCategoriesByNameAndDescription(String like);

    void saveCategory(CategoryDTO category);
    void saveClub(ClubVDTO club);
    void deleteCategory(CategoryDTO category);
    void deleteClub(ClubVDTO club);
     */

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SearchService searchService;

    Club getClubFromRepository() {
        return clubRepository.findAll().stream().findFirst().orElseThrow();
    }

    Category getCategoryFromRepository() {
        return categoryRepository.findAll().stream().findFirst().orElseThrow();
    }

    @Test
    void getClubsByNameTest() {
        Club club = getClubFromRepository();
        String name = club.getName();
        String substring = name.substring(4);
        Set<ClubVDTO> clubsByName = searchService.getClubsByName(substring);
        Assertions.assertTrue(clubsByName.size() > 0);
    }

    @Test
    void getClubsByNameAndDescriptionTest() {
        Club club = getClubFromRepository();
        String description = club.getDescription();
        Set<ClubVDTO> clubsByNameAndDescription = searchService.getClubsByNameAndDescription(description);
        Assertions.assertTrue(clubsByNameAndDescription.size() > 0);
    }

    @Test
    void getCategoriesByNameTest() {
        Category category = getCategoryFromRepository();
        String name = category.getName();
        Set<CategoryDTO> categoriesByName = searchService.getCategoriesByName(name);
        Assertions.assertTrue(categoriesByName.size() > 0);
    }

    @Test
    void getCategoriesByNameAndDescriptionTest() {
        Category category = getCategoryFromRepository();
        String description = category.getDescription();
        Set<CategoryDTO> categoriesByNameAndDescription = searchService.getCategoriesByNameAndDescription(description);
        Assertions.assertTrue(categoriesByNameAndDescription.size() > 0);
    }

}
