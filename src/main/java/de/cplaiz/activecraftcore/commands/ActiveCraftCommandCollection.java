package de.cplaiz.activecraftcore.commands;

import java.util.HashSet;
import java.util.Set;

public abstract class ActiveCraftCommandCollection extends HashSet<ActiveCraftCommand> {
    public ActiveCraftCommandCollection(ActiveCraftCommand... activeCraftCommands) {
        addAll(Set.of(activeCraftCommands));
    }
}
