package design.kfu.sunrise.repository;

import design.kfu.sunrise.domain.model.TelegramChatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChatInfoRepository extends JpaRepository<TelegramChatInfo,String> {
}
