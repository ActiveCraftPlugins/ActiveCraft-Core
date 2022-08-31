package de.cplaiz.activecraftcore.commands;

public enum CommandTargetType {

    SELF("self"),
    OTHERS("others");

    private final String code;

    CommandTargetType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
