package design.kfu.sunrise.controller;

import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.dto.club.ClubVDTO;
import design.kfu.sunrise.service.util.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * @author Daniyar Zakiev
 */
@RestController
@RequestMapping(value = "/v1")
public class SearchController {

    @Autowired
    private SearchService searchService;


    @Operation(description = "Search clubs", summary = "Send description is true for searching in body of club too")
    @PermitAll
    @GetMapping("/search/clubs")
    public Set<ClubVDTO> getClubsByName(@Size(min = 3) @RequestParam("name") String clubLike, @RequestParam(value = "description", required = false) Boolean withDescription) {
        if (withDescription != null) {
            return searchService.getClubsByNameAndDescription(clubLike);
        } else {
            return searchService.getClubsByName(clubLike);
        }
    }

    @Operation(description = "Search categories", summary = "Send description is true for searching in body of categories too")
    @PermitAll
    @GetMapping("/search/categories")
    public Set<CategoryDTO> getCategoriesByName(@Size(min = 3) @RequestParam("name") String categoryLike, @RequestParam(value = "description", required = false) Boolean withDescription) {
        if (withDescription != null) {
            return searchService.getCategoriesByNameAndDescription(categoryLike);
        } else {
            return searchService.getCategoriesByName(categoryLike);
        }
    }

}
