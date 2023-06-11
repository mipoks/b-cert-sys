package design.kfu.sunrise.controller.payment;

import design.kfu.sunrise.domain.dto.requisite.RequisiteDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.payment.Requisite;
import design.kfu.sunrise.service.payment.RequisiteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class RequisiteController {

    @Autowired
    private RequisiteService requisiteService;

    @Operation(description = "Get available requisites of account", summary = "Returns Page of requisite")
    @PreAuthorize("@access.hasAccessToRequisites(#account, #req)")
    @GetMapping("/requisites/{account_id}")
    public Page<Requisite> getRequisites(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size,
                                         @AuthenticationPrincipal(expression = "account") Account account, @PathVariable("account_id") Account req){
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        return requisiteService.getRequisitesFor(account, pageable);
    }

    @Operation(description = "Get all requisites", summary = "Returns Page of requisites")
    @PreAuthorize("@access.hasAccessToAllRequisites(#account)")
    @GetMapping("/requisites")
    public Page<Requisite> getAllRequisites(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size,
                                         @AuthenticationPrincipal(expression = "account") Account account){
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        return requisiteService.getRequisites(pageable);
    }

    @Operation(description = "Add requisite", summary = "Returns created requisites")
    @PreAuthorize("@access.hasAccessToCreateRequisite(#account)")
    @PostMapping("/requisite")
    public Requisite createRequisite(@AuthenticationPrincipal(expression = "account") Account account, @Valid @RequestBody Requisite requisite){
        return requisiteService.createRequisite(account, requisite);
    }

    @Operation(description = "Update requisite", summary = "Returns updated requisites")
    @PreAuthorize("@access.hasAccessToCreateRequisite(#account)")
    @PutMapping("/requisite/{requisite_id}")
    public Requisite modifyRequisite(@PathVariable("requisite_id") Requisite requisite, @Valid @RequestBody RequisiteDTO requisiteDTO, @AuthenticationPrincipal(expression = "account") Account account){
        requisite.getRequisiteInfo().setCardHolder(requisiteDTO.getRequisiteInfo().getCardHolder());
        requisite.setActive(requisiteDTO.getActive());
        return requisiteService.updateRequisite(requisite);
    }
}
