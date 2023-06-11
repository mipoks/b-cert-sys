package design.kfu.sunrise.feign;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {

    @NotNull
    private Long telegramChatId;
    private String userName;
    private Long messageId;
    private Long clubId;
    @NotNull
    private String text;
    private List<String> urls = new ArrayList<>();
}
