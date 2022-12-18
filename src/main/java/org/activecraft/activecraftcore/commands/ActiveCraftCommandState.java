package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.bukkit.command.CommandSender;

public class ActiveCraftCommandState {

    private ActiveCraftCommand activeCraftCommand;
    private MessageFormatter msgFormatter;
    private MessageSupplier msgSupplier;

    protected ActiveCraftCommandState(ActiveCraftCommand activeCraftCommand, CommandSender sender) {
        this.activeCraftCommand = activeCraftCommand;
        //msgFormatter = new MessageFormatter(activeCraftCommand.getActiveCraftMessage());
    }



}
