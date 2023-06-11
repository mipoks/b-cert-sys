package design.kfu.sunrise.feign;

import design.kfu.sunrise.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "tgMessage", url = "http://localhost:8089/", configuration = FeignConfig.class)
public interface FeignIntegrateClient {

    @RequestMapping(method = RequestMethod.POST, value = "/integrate/message")
    void sendComment(MessageDTO commentDTO);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/chat/{id}/users/count")
    Integer getUsersCountOfChat(@PathVariable("id") String id);

}
