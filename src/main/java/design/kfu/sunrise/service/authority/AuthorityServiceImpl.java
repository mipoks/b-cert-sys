package design.kfu.sunrise.service.authority;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Authority;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.embedded.ClubsInfo;
import design.kfu.sunrise.exception.ErrorType;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public Authority findOrThrow(Account account, Club club) {
        Optional<Authority> optional = authorityRepository.findById(ClubsInfo.builder()
                .dAccountId(account.getId())
                .clubId(club.getId())
                .build());
        return optional.orElseThrow(Exc.sup(ErrorType.ENTITY_NOT_FOUND,"Сущность прав не найдена"));
    }

    @Override
    public Authority remove(Authority authority) {
        authorityRepository.delete(authority);
        return authority;
    }


}
