package com.epicplayera10.iaedit.hook.listener;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.epicplayera10.iaedit.hook.processor.FaweCustomBlocksProcessor;
import org.bukkit.event.Listener;

public class FaweListener {

    @Subscribe
    public void onEditSession(EditSessionEvent event) {
        if (event.getStage() == EditSession.Stage.BEFORE_CHANGE) {
            event.getExtent().addPostProcessor(
                new FaweCustomBlocksProcessor(BukkitAdapter.adapt(event.getWorld()))
            );
        }
    }
}
