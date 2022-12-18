package org.activecraft.activecraftcore.exceptions;

public class InvalidColorException extends ActiveCraftException{

    public InvalidColorException(String message) {
        super(message);
    }

    public InvalidColorException() {
        this("Invalid color.");
    }
}
