package com.epicplayera10.iaedit.hook.processor;

import com.epicplayera10.iaedit.hook.CustomBlocksFactory;
import com.fastasyncworldedit.core.extent.processor.ProcessorScope;
import com.fastasyncworldedit.core.queue.IBatchProcessor;
import com.fastasyncworldedit.core.queue.IChunk;
import com.fastasyncworldedit.core.queue.IChunkGet;
import com.fastasyncworldedit.core.queue.IChunkSet;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypesCache;
import com.epicplayera10.iaedit.utils.IACache;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FaweCustomBlocksProcessor implements IBatchProcessor {
    private final World world;

    public FaweCustomBlocksProcessor(World world) {
        this.world = world;
    }

    @Override
    public IChunkSet processSet(IChunk chunk, IChunkGet get, IChunkSet set) {
        int bx = chunk.getX() << 4;
        int bz = chunk.getZ() << 4;

        // Remove existing custom blocks
        for (int layer = get.getMinSectionPosition(); layer <= get.getMaxSectionPosition(); layer++) {
            if (!set.hasSection(layer)) {
                continue;
            }

            // loadIfPresent shouldn't be null if set.hasSection(layer) is true
            char[] blocksSet = set.loadIfPresent(layer);

            // Account for negative layers
            int by = layer << 4;
            for (int y = 0, index = 0; y < 16; y++) {
                int yy = y + by;
                for (int z = 0; z < 16; z++) {
                    int zz = z + bz;
                    for (int x = 0; x < 16; x++, index++) {
                        final int rawState = blocksSet[index];
                        if (rawState != BlockTypesCache.ReservedIDs.__RESERVED__) {
                            int xx = bx + x;

                            Location location = new Location(this.world, xx, yy, zz);
                            if (CustomBlock.Advanced.getInCustomRegion(location) != null) {
                                // Remove existing custom block
                                CustomBlock.Advanced.removeFromCustomRegion(location);
                            }

                            // Match custom block with block state and raw place it
                            BlockState state = BlockTypesCache.states[rawState];
                            if (CustomBlocksFactory.isCustomBlockType(state.getBlockType())) {
                                CustomBlock customBlock = IACache.getCustomBlockByBlockData(BukkitAdapter.adapt(state));
                                if (customBlock == null) continue;

                                // Recover custom block
                                CustomBlock.Advanced.placeInCustomRegion(customBlock, location);
                            }
                        }
                    }
                }
            }
        }

        return set;
    }

    @Nullable
    @Override
    public Extent construct(Extent child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProcessorScope getScope() {
        return ProcessorScope.READING_SET_BLOCKS;
    }
}
