package design.kfu.sunrise.controller;

import design.kfu.sunrise.domain.dto.club.ClubCDTO;
import design.kfu.sunrise.domain.dto.club.ClubVDTO;
import design.kfu.sunrise.domain.dto.payment.PaymentDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.service.access.AccountAccessService;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.util.model.Filter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @Autowired
    private AccountAccessService accountAccessService;


    @Operation(description = "Get club's info", summary = "Returns club's info")
    @PermitAll
    @GetMapping("/club/{club_id}")
    public ClubVDTO getClub(@PathVariable("club_id") Club club) {
        return ClubVDTO.from(club);
    }


    @Operation(description = "Check is account in club", summary = "Returns boolean")
    @PermitAll
    @GetMapping("/account/club/{club_id}")
    public boolean isAccountInClub(@PathVariable("club_id") Club club, @AuthenticationPrincipal(expression = "account") Account account) {
        return club.getAccounts().contains(account);
    }

    @Operation(description = "Create club", summary = "Returns created club in DTO")
    @PreAuthorize("@access.hasAccessToCreateClub(#account)")
    @PostMapping("/club")
    public ClubVDTO addClub(@Valid @RequestBody ClubCDTO clubDTO, @AuthenticationPrincipal(expression = "account") Account account) {
        clubDTO.setAuthorId(account.getId());
        return ClubVDTO.from(clubService.addClub(clubDTO));
    }

//    @Transactional
//    @PreAuthorize("@access.hasAccessToEnterClub(#account, #club)")
//    @PostMapping("/club/{club_id}")
//    public ClubVDTO enterClub(@PathVariable("club_id") Club club, @AuthenticationPrincipal(expression = "account") Account account) {
//        clubService.addAccountToClub(club, account);
//        return ClubVDTO.from(club);
//    }

    @Operation(description = "Update club", summary = "Returns updated club in DTO")
    @PreAuthorize("@access.hasAccessToEditClub(#account, #club)")
    @PutMapping("/club/{clubId}")
    public ClubVDTO updateClub(@PathVariable("clubId") Club club, @Valid @RequestBody ClubCDTO clubDTO, @AuthenticationPrincipal(expression = "account") Account account) {
        club.setDescription(clubDTO.getDescription());
        club.setName(clubDTO.getName());
        club.setClubInfo(clubDTO.getClubInfo());
        club.setCostInfo(clubDTO.getCostInfo());
        return ClubVDTO.from(clubService.updateClub(club));
    }

    @Operation(description = "Get clubs in categories", summary = "This should return Page but it does not")
    @GetMapping("/clubs")
    public List<ClubVDTO> getClubs(@RequestParam("category_id") Long categoryId,
                                  @RequestParam(value = "expired", required = false, defaultValue = "false") Boolean expired,
                                  @RequestParam(value = "sort", required = false, defaultValue = "date") String sort) {
        Filter filter = Filter.builder()
                .categoryId(categoryId)
                .expired(expired)
                .sort(sort)
                .build();
        return clubService.findClubs(filter).stream().map(ClubVDTO::from).collect(Collectors.toList());
    }
}
