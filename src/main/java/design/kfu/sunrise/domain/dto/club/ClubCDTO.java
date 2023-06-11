package design.kfu.sunrise.domain.dto.club;

import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.embedded.ClubInfo;
import design.kfu.sunrise.domain.model.embedded.CostInfo;
import design.kfu.sunrise.service.StaticService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubCDTO {

    private Long id;
    private Long authorId;
    private Long categoryId;
    private ClubInfo clubInfo;

    @NotNull
    private String name;
    @NotNull
    private String description;

    private CostInfo costInfo;

    public static Club toClub(ClubCDTO clubDTO) {
        return Club.builder()
                .name(clubDTO.getName())
                .category(StaticService.getCategoryService().findOrThrow(clubDTO.getCategoryId()))
                .costInfo(clubDTO.costInfo)
                .author(StaticService.getAccountService().findOrThrow(clubDTO.getAuthorId()))
                .clubInfo(clubDTO.getClubInfo())
                .description(clubDTO.getDescription())
                .build();
    }

    public static ClubCDTO fromClub(Club club) {
        return ClubCDTO.builder()
                .clubInfo(club.getClubInfo())
                .id(club.getId())
                .costInfo(club.getCostInfo())
                .authorId(club.getAuthor().getId())
                .categoryId(club.getCategory().getId())
                .name(club.getName())
                .description(club.getDescription())
                .build();
    }
}
