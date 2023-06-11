package design.kfu.sunrise.domain.dto.category;

import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.service.StaticService;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "categoryind")
public class CategoryDTO {

    private Long id;

    @NotNull
    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Transient
    private String url;

    @Transient
    private Long parentId;

    @Transient
    private int clubCountInCategory;
    @Transient
    private int categoryCountInCategory;

    @Transient
    private Set<CategoryChildDTO> childs = new LinkedHashSet<>();

    public static CategoryDTO from(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .categoryCountInCategory(category.getChilds().size())
                .childs(category.getChilds().stream().map(CategoryDTO::makeChildfrom).collect(Collectors.toCollection(LinkedHashSet::new)))
                .url(StaticService.getUrlService().generateUrl(category))
                .build();
    }

    public static CategoryDTO from(Category category, List<Club> clubs) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .categoryCountInCategory(category.getChilds().size())
                .clubCountInCategory(clubs.size())
                .childs(category.getChilds().stream().map(CategoryDTO::makeChildfrom).collect(Collectors.toCollection(LinkedHashSet::new)))
                .url(StaticService.getUrlService().generateUrl(category))
                .build();
    }

    public static CategoryChildDTO makeChildfrom(Category category) {
        return CategoryChildDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static CategoryDTO fromExcludeChilds(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .url(StaticService.getUrlService().generateUrl(category))
                .build();
    }

    public static Category toCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .childs(new HashSet<>())
                .parent(categoryDTO.getParentId() == null ? null : StaticService.getCategoryService().findOrNull(categoryDTO.getParentId()))
                .build();
    }
}

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CategoryChildDTO {
    private Long id;
    private String name;
}
