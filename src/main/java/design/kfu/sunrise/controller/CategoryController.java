package design.kfu.sunrise.controller;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.service.access.AccountAccessService;
import design.kfu.sunrise.service.category.CategoryService;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.util.model.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private AccountAccessService accountAccessService;

    @Operation(description = "Get category's info", summary = "Returns category's info")
    @PermitAll
    @GetMapping("/category/{category_id}")
    public CategoryDTO getCategory(@PathVariable("category_id") Category category){
        Filter filter = Filter.builder()
                .expired(false)
                .categoryId(category.getId())
                .build();
        List<Club> clubs = clubService.findClubs(filter);
        return CategoryDTO.from(category, clubs);
    }

    @Operation(description = "Create category", summary = "Returns registered category in DTO")
    @PreAuthorize("@access.hasAccessToCreateCategory(#account)")
    @PostMapping("/category")
    @Transactional
    public CategoryDTO addCategory(@Valid @RequestBody CategoryDTO categoryDTO, @AuthenticationPrincipal(expression = "account") Account account){
        Category category = categoryService.addCategory(categoryDTO);
        Filter filter = Filter.builder()
                .expired(false)
                .categoryId(category.getId())
                .build();
        List<Club> clubs = clubService.findClubs(filter);
        return CategoryDTO.from(category, clubs);
    }

    @Operation(description = "Delete category", summary = "Returns boolean")
    @PreAuthorize("@access.hasAccessToDeleteCategory(#account, #category)")
    @DeleteMapping("/category/{category_id}")
    public Boolean deleteCategory(@PathVariable("category_id") Category category, @AuthenticationPrincipal(expression = "account") Account account){
        categoryService.deleteCategory(category);
        return true;
    }

    @Operation(description = "Update category", summary = "Returns updated category in DTO")
    @PreAuthorize("@access.hasAccessToCreateCategory(#account)")
    @PutMapping("/category/{category_id}")
    public CategoryDTO updateCategory(@PathVariable("category_id") Category category, @Valid @RequestBody CategoryDTO categoryDTO, @AuthenticationPrincipal(expression = "account") Account account){
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        categoryService.update(category);
        Filter filter = Filter.builder()
                .expired(false)
                .categoryId(category.getId())
                .build();
        List<Club> clubs = clubService.findClubs(filter);
        return CategoryDTO.from(category, clubs);
    }

    @Operation(description = "Get categories (by parent id)", summary = "Returns categories or subcategories")
    @GetMapping("/categories")
    @PermitAll
    @PageableAsQueryParam
    public Page<CategoryDTO> getCategories(@Nullable @RequestParam(value = "parent_id", required = false) Long parentId,
                                           @Parameter(hidden = true) Pageable pageable) {

        return categoryService.findByParentId(parentId, pageable).map(c -> {
            Filter filter = Filter.builder()
                    .categoryId(c.getId())
                    .expired(false)
                    .build();
            List<Club> clubs = clubService.findClubs(filter);
            return CategoryDTO.from(c, clubs);
        });
    }
}
