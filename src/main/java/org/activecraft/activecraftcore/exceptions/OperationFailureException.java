package org.activecraft.activecraftcore.exceptions;

public class OperationFailureException extends ActiveCraftException {

    public OperationFailureException(String message) {
        super(message);
    }

    public OperationFailureException() {
        this(null);
    }
}
