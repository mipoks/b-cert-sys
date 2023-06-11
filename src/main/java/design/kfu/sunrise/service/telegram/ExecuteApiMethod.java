package design.kfu.sunrise.service.telegram;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.io.Serializable;

public interface ExecuteApiMethod<T extends Serializable> {
    public T executeCommand(BotApiMethod<T> method);
}
