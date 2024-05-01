package com.epicplayera10.iaedit.hook.listener;

import com.epicplayera10.iaedit.IAEdit;
import com.epicplayera10.iaedit.hook.process.FaweCompatibility;
import com.epicplayera10.iaedit.hook.process.WorldEditCustomBlocksExtent;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.Subscribe;

public class WorldEditListener {

    @Subscribe
    public void onEditSession(EditSessionEvent event) {
        if (event.getStage() == EditSession.Stage.BEFORE_CHANGE) {
            if (IAEdit.instance().isFaweEnabled()) {
                // Better performance and compatibility with FAWE
                FaweCompatibility.addProcessor(event);
            } else {
                event.setExtent(
                    new WorldEditCustomBlocksExtent(event.getExtent(), BukkitAdapter.adapt(event.getWorld()))
                );
            }
        }
    }
}
