package de.cplaiz.activecraftcore.exceptions;

import lombok.Getter;

@Getter
public class StartupException extends ActiveCraftException {

    private final boolean shutdown;

    public StartupException(String message) {
        this(message, true);
    }

    public StartupException(String message, boolean shutdown) {
        super(message);
        this.shutdown = shutdown;
    }
}
