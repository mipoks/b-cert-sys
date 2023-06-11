package design.kfu.sunrise.domain.model;

import design.kfu.sunrise.domain.model.embedded.CommentInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Comment extends BaseEntity {

    @Version
    private Long version;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;

    //ToDo дописать
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Account account;

    //ToDo дописать
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Comment answered;

    //ToDo дописать
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Club club;

    @Embedded
    private CommentInfo commentInfo;

    private Long telegramAccountId;

    @ElementCollection(targetClass = String.class)
    @Enumerated(EnumType.STRING)
    private List<String> images = new ArrayList<>();

    public String generateHash() {
        StringBuilder stringBuilder = new StringBuilder(id.toString());
        stringBuilder.append(value);
        if (answered != null) {
            stringBuilder.append(answered.getId());
        }
        stringBuilder.append(club.getId());

        return DigestUtils.md5DigestAsHex(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    public String generateUrl() {
        StringBuilder url = new StringBuilder("/club/");
        url.append(club.getId())
                .append("/comment/")
                .append(getId());
        return url.toString();
    }
}
