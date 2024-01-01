package com.epicplayera10.iaedit.listeners;

import com.epicplayera10.iaedit.utils.IACache;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderListener implements Listener {
    @EventHandler
    public void onLoad(ItemsAdderLoadDataEvent event) {
        IACache.init();
    }
}
