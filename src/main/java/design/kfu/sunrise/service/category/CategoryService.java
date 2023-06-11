package design.kfu.sunrise.service.category;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    Category addCategory(CategoryDTO categoryDTO);
    void deleteCategory(Category category);
    Category findOrNull(Long categoryId);
    Category findOrThrow(Long categoryId);
    Category update(Category category);
    Page<Category> findByParentId(Long parentId, Pageable pageable);

    List<Category> findAllCategories();

    @Transactional
    Set<Category> getCategoryChilds(Category detachedCategory);
}
