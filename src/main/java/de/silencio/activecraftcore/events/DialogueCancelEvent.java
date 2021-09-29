package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.dialogue.DialogueManager;
import de.silencio.activecraftcore.utils.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DialogueCancelEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private DialogueManager dialogueManager;
    private boolean cancelled;

    public DialogueCancelEvent(DialogueManager dialogueManager) {
        this.dialogueManager = dialogueManager;
    }

    public void setDialogueManager(DialogueManager dialogueManager) {
        this.dialogueManager = dialogueManager;
    }

    public DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = true;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}