package design.kfu.sunrise.domain.model;

import lombok.*;

import jakarta.persistence.*;

/**
 * @author Daniyar Zakiev
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Document extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account owner;

    private String url;

    private String mimeType;

    private String description;

    private String name;
}
