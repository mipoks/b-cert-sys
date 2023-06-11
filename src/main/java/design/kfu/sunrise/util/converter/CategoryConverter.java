package design.kfu.sunrise.util.converter;

import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Daniyar Zakiev
 */
@Component
public class CategoryConverter implements Converter<String, Category> {
    @Autowired
    private CategoryService categoryService;

    @Override
    public Category convert(String nameWithId) {
        int dotIndex = nameWithId.lastIndexOf('.');
        Long id = Long.parseLong(nameWithId.substring(dotIndex + 1));
        return categoryService.findOrThrow(id);
    }
}
