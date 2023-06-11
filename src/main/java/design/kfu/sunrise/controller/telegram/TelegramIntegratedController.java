package design.kfu.sunrise.controller.telegram;

import design.kfu.sunrise.service.club.ClubService;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import design.kfu.sunrise.domain.model.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daniyar Zakiev
 */
@RestController
@Slf4j
@RequestMapping(value = "/v1")
public class TelegramIntegratedController {

    @Autowired
    private ClubService clubService;

    @PermitAll
    @GetMapping("/club/{club_id}/count")
    public Integer getUsersCountInChat(@PathVariable("club_id") Club club) {
        log.info("dto {}", club.getClubInfo().getTelegramChatId());
        return clubService.getClubUserCount(club);
    }
}
