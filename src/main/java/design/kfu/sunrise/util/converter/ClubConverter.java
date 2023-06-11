package design.kfu.sunrise.util.converter;

import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.service.club.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Daniyar Zakiev
 */
@Component
public class ClubConverter implements Converter<String, Club> {
    @Autowired
    private ClubService clubService;

    @Override
    public Club convert(String nameWithId) {
        int dotIndex = nameWithId.lastIndexOf('.');
        Long id = Long.parseLong(nameWithId.substring(dotIndex + 1));
        return clubService.findOrThrow(id);
    }
}
