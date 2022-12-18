package org.activecraft.activecraftcore.exceptions;

import lombok.Getter;

@Getter
public class ModuleException extends ActiveCraftException {

    private final String moduleName;
    private final ErrorType errorType;

    public ModuleException(String message, String moduleName, ErrorType errorType) {
        super(message);
        this.moduleName = moduleName;
        this.errorType = errorType;
    }

    public ModuleException(String moduleName, ErrorType errorType) {
        this(errorType.message, moduleName, errorType);
    }

    public enum ErrorType {
        DOES_NOT_EXIST("Module does not exist"),
        NOT_INSTALLED("Module not installed"),
        ALREADY_INSTALLED("Module already installed"),
        NOT_LOADED("Module not loaded"),
        ALREADY_LOADED("Module already loaded"),
        NOT_ENABLED("Module not enabled"),
        ALREADY_ENABLED("Module already enabled");

        private final String message;

        ErrorType(String message) {
            this.message = message;
        }
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
