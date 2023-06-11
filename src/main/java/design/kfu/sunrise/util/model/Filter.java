package design.kfu.sunrise.util.model;

import lombok.*;

/**
 * @author Daniyar Zakiev
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Filter {
    private boolean visible = true;
    private Long categoryId;
    private Boolean expired;
    private String sort;
}
