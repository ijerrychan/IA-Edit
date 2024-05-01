package com.epicplayera10.iaedit.hook.process;

import com.epicplayera10.iaedit.hook.CustomBlocksFactory;
import com.epicplayera10.iaedit.utils.IACache;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Location;

public class CustomBlocksWorldEditUtils {
    public static void processBlock(Location location, BlockStateHolder<?> stateToSet, BlockStateHolder<?> stateBefore) {
        if (CustomBlocksFactory.isCustomBlockType(stateBefore.getBlockType())) {
            // Remove existing custom block
            CustomBlock.Advanced.removeFromCustomRegion(location);
        }

        // Match custom block with block state and raw place it
        if (CustomBlocksFactory.isCustomBlockType(stateToSet.getBlockType())) {
            CustomBlock customBlock = IACache.getCustomBlockByBlockData(BukkitAdapter.adapt(stateToSet));
            if (customBlock == null) return;

            // Recover custom block
            CustomBlock.Advanced.placeInCustomRegion(customBlock, location);
        }
    }
}
