package design.kfu.sunrise.service.authority;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Authority;
import design.kfu.sunrise.domain.model.Club;

public interface AuthorityService {
    Authority save(Authority authority);
    Authority findOrThrow(Account account, Club club);

    Authority remove(Authority authority);
}
