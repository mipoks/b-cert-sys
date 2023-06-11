package design.kfu.sunrise.domain.dto.club;

import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.embedded.ClubInfo;
import design.kfu.sunrise.domain.model.embedded.CostInfo;
import design.kfu.sunrise.service.StaticService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author Daniyar Zakiev
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "clubind")
public class ClubVDTO {

    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    private ClubInfo activeInfo;

    @Transient
    private CostInfo costInfo;

    @Transient
    private String created;

    @Transient
    private String updated;

    @Transient
    private Long authorId;

    @Transient
    private String categoryName;

    @Transient
    private String url;

    public static ClubVDTO from(Club club) {
        return ClubVDTO.builder()
                .id(club.getId())
                .name(club.getName())
                .description(club.getDescription())
                .activeInfo(club.getClubInfo())
                .costInfo(club.getCostInfo())
                .created(StaticService.getInstantConverter().convert(club.getCreated()))
                .updated(StaticService.getInstantConverter().convert(club.getUpdated()))
                .authorId(club.getAuthor()==null? null :club.getAuthor().getId())
                .categoryName(club.getCategory()==null? null : club.getCategory().getName())
                .url(StaticService.getUrlService().generateUrl(club))
                .build();
    }
}
