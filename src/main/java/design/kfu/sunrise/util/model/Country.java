package design.kfu.sunrise.util.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Daniyar Zakiev
 */
@Getter
@AllArgsConstructor
public enum Country {
    RUSSIA("RU"),
    CHINA("CN"),
    KIRGHISTAN("KG"),
    KAZAKHSTAN("KZ");
    private final String name;
}
