package de.cplaiz.activecraftcore.utils.config;

public enum Feature {

    // TODO: 05.08.2022 was geht zu features machen

    PREFIX("prefix");

    private final String identifier;

    Feature(String identifier) {
        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

    public static Feature fromIdentifier(String identifier) {
        for (Feature feature : values()) {
            if (feature.identifier.equals(identifier)) {
                return feature;
            }
        }
        return null;
    }
}
