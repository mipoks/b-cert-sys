package design.kfu.sunrise.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorType {
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Неожиданная ошибка"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Ошибка валидации"),
    ENTITY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Уже существует"),
    INCORRECT_PARAMETERS(HttpStatus.BAD_REQUEST, "Некорректные параметры"),
    HAS_DEPENDENT(HttpStatus.BAD_REQUEST, "Связанная сущность"),
    MEDIA_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "Ошибка загрузки"),
    MEDIA_DOWNLOAD_ERROR(HttpStatus.BAD_REQUEST, "Ошибка выгрузки"),
    ENTITY_ACCESS_VIOLATION(HttpStatus.BAD_REQUEST, "Обработка запроса запрещена"),

    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Не найдено"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Доступ запрещен");

    private final HttpStatus status;
    private final String title;
}
