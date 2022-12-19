package org.activecraft.activecraftcore.commands;

import java.util.HashSet;
import java.util.Set;

public abstract class ActiveCraftCommandCollection extends HashSet<ActiveCraftCommandv2> {
    public ActiveCraftCommandCollection(ActiveCraftCommandv2... activeCraftCommands) {
        addAll(Set.of(activeCraftCommands));
    }
}
