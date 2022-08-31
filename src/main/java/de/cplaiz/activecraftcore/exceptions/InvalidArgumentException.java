package de.cplaiz.activecraftcore.exceptions;

import lombok.Getter;

@Getter
public class InvalidArgumentException extends ActiveCraftException {

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException() {
        this("Invalid arguments.");
    }
}
