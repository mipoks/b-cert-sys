package design.kfu.sunrise.util.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Daniyar Zakiev
 */

@Getter
@Setter
@AllArgsConstructor
public class Permission {
    private String name;
    private boolean value;
}