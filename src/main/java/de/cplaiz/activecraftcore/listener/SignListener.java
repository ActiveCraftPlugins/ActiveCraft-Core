package de.cplaiz.activecraftcore.listener;

import de.cplaiz.activecraftcore.utils.ColorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < 4; i++)
            event.setLine(i, ColorUtils.replaceColorAndFormat(event.getLine(i)));
    }

}
