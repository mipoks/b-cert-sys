package design.kfu.sunrise.service.integrator;

import design.kfu.sunrise.feign.FeignIntegrateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author Daniyar Zakiev
 */
@Service
@ConditionalOnProperty(value = "use.integrator", havingValue = "false")
public class IntegratorServiceStub implements IntegratorService {
    @Override
    public Integer getUserCountFromChat(Long chatId) {
        return 0;
    }
}
