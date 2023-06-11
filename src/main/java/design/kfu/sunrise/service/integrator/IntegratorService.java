package design.kfu.sunrise.service.integrator;

import design.kfu.sunrise.domain.dto.club.ClubCDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.util.model.Filter;

import java.util.List;
import java.util.Set;

public interface IntegratorService {
    Integer getUserCountFromChat(Long chatId);
}
