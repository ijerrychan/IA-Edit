package com.epicplayera10.iaedit.hook.process;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.event.extent.EditSessionEvent;

/**
 * This class in needed to "proxy" the add processor so the
 * NoClassDefFoundError is not thrown when FAWE is not present.
 */
public class FaweCompatibility {

    public static void addProcessor(EditSessionEvent event) {
        event.getExtent().addPostProcessor(
            new FaweCustomBlocksProcessor(BukkitAdapter.adapt(event.getWorld()))
        );
    }
}
