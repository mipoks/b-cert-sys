package design.kfu.sunrise;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.exception.RestException;
import design.kfu.sunrise.repository.CategoryRepository;
import design.kfu.sunrise.service.category.CategoryService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryServiceTests {


    /*
    Category addCategory(CategoryDTO categoryDTO);
    void deleteCategory(Category category);
    Category findOrNull(Long categoryId);
    Category findOrThrow(Long categoryId);
    Category update(Category category);
    Page<Category> findByParentId(Long parentId, Pageable pageable);
     */


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    List<Category> categoryGenerator(int count) {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            categories.add(ModelGenerator.generateCategory());
        }
        return categories;
    }

    List<CategoryDTO> categoryDTOGenerator(int count) {
        List<CategoryDTO> categories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            categories.add(ModelGenerator.generateCategoryDTO());
        }
        return categories;
    }

    Category getCategoryFromRepository() {
        return categoryRepository.findAll().stream().findFirst().orElseThrow();
    }

    @Test
    @Order(1)
    void testAddCategoryMethod() {
        List<CategoryDTO> categories = categoryDTOGenerator(10);
        for(CategoryDTO categoryDTO : categories) {
            categoryService.addCategory(categoryDTO);
        }
    }

    @Test
    void testDeleteCategoryMethod() {
        List<Category> all = categoryRepository.findAll();
        int forDelete = all.size() / 2;
        boolean tested = false;
        for(int i = 0; i < forDelete; i++) {
            tested = true;
            Category category = all.get(i);
            Long id = category.getId();
            categoryService.deleteCategory(category);

            Optional<Category> categoryOptional = categoryRepository.findById(id);
            Assert.isTrue(categoryOptional.isEmpty(), "Category was deleted, but it still present");
        }
        assertTrue(tested);
    }

    @Test
    void testFindOrNullMethod() {
        Category category = getCategoryFromRepository();
        Category notNullCategory = categoryService.findOrNull(category.getId());
        Assert.notNull(notNullCategory, "Category is null, but shouldn't");
        Category nullCategory = categoryService.findOrNull(42147242L);
        Assert.isNull(nullCategory, "Category is not present, but it is not null");
    }

    @Test
    void testFindOrThrowMethod() {
        Category category = getCategoryFromRepository();
        Category notNullCategory = categoryService.findOrThrow(category.getId());
        Assert.notNull(notNullCategory, "Category is null, but shouldn't");

        Throwable exception = assertThrows(RestException.class, () -> categoryService.findOrThrow(42147242L));
        assertNotNull(exception, "Exception did not throw");
    }

    @Test
    void testUpdateMethod() {
        Category category = getCategoryFromRepository();
        String description = category.getDescription();
        Long id = category.getId();
        category.setDescription(ModelGenerator.generateString());
        categoryService.update(category);

        Category categoryUpdated = categoryRepository.findById(id).get();
        Assert.isTrue(!categoryUpdated.getDescription().equals(description), "Descriptions are equals");
    }


    @Test
    void testFindByParentIdMethod() {
        Page<Category> categories = categoryService.findByParentId(null, PageRequest.of(0, 5));
        List<Category> categoryList = categories.getContent();
        assertTrue(categoryList.size() > 0);
    }
}
