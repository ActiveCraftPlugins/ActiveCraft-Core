package de.silencio.activecraftcore.exceptions;

import lombok.Getter;

@Getter
public class InvalidArgumentException extends ActiveCraftException {

    private ErrorType errorType;

    public InvalidArgumentException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public InvalidArgumentException(ErrorType errorType) {
        this("Invalid arguments.", errorType);
    }

    public InvalidArgumentException() {
        this("Invalid arguments.", ErrorType.DEFAULT);
    }

    public enum ErrorType {
        DEFAULT,
        INCLUDE_MESSAGE
    }

}
