package design.kfu.sunrise;

import design.kfu.sunrise.domain.dto.account.AccountCDTO;
import design.kfu.sunrise.domain.dto.category.CategoryDTO;
import design.kfu.sunrise.domain.dto.club.ClubCDTO;
import design.kfu.sunrise.domain.dto.comment.CommentDTO;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.embedded.ClubInfo;
import design.kfu.sunrise.domain.model.embedded.CostInfo;

import java.util.UUID;

public class ModelGenerator {
    public static Category generateCategory() {
        CategoryDTO dto = CategoryDTO.builder()
                .description(generateString())
                .name(generateString())
                .build();
        Category category = CategoryDTO.toCategory(dto);
        return category;
    }

    public static CategoryDTO generateCategoryDTO() {
        CategoryDTO dto = CategoryDTO.builder()
                .description(generateString())
                .name(generateString())
                .build();
        return dto;
    }


    public static Club generateClub() {
        ClubCDTO dto = ClubCDTO.builder()
                .description(generateString())
                .name(generateString())
                .authorId(1L)
                .categoryId(6L)
                .costInfo(
                        CostInfo.builder()
                                .entryCost(3000)
                                .certificateCost(35000)
                                .build()
                )
                .clubInfo(ClubInfo.builder()
                        .expireCondition(generateString())
                        .expired(false)
                        .visible(true)
                        .build())
                .build();
        Club club = ClubCDTO.toClub(dto);
        return club;
    }

    public static ClubCDTO generateClubCDTO() {
        ClubCDTO dto = ClubCDTO.builder()
                .description(generateString())
                .name(generateString())
                .authorId(1L)
                .categoryId(20L)
                .costInfo(
                        CostInfo.builder()
                                .entryCost(3000)
                                .certificateCost(35000)
                                .build()
                )
                .clubInfo(ClubInfo.builder()
                        .expireCondition(generateString())
                        .expired(false)
                        .visible(true)
                        .build())
                .build();
        return dto;
    }

    public static CommentDTO generateCommentDTO() {
        CommentDTO dto = CommentDTO.builder()
//                .clubId()
                .value(generateString())
//                .answered()
                .build();
        return dto;
    }

    public static AccountCDTO generateAccountCDTO() {
        AccountCDTO dto = AccountCDTO.builder()
                .email(generateString() + "@mail.ru")
                .phone("+7999999999")
                .password(generateString())
                .useTerms(true)
                .build();
        return dto;
    }

    public static String generateString() {
        return UUID.randomUUID().toString();
    }
}
