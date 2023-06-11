package design.kfu.sunrise.mapper;


import design.kfu.sunrise.domain.dto.account.AccountVDTO;
import design.kfu.sunrise.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    AccountVDTO toVDTO(Account account);
}
