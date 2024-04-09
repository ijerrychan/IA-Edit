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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class FaweCustomBlocksProcessor implements IBatchProcessor {
    private final World world;

    public FaweCustomBlocksProcessor(World world) {
        this.world = world;
    }

    @Override
    public IChunkSet processSet(IChunk chunk, IChunkGet get, IChunkSet set) {
        return null;
    }

    @Override
    public void postProcess(IChunk chunk, IChunkGet get, IChunkSet set) {
        int bx = chunk.getX() << 4;
        int bz = chunk.getZ() << 4;

        // Remove existing custom blocks
        for (int layer = get.getMinSectionPosition(); layer <= get.getMaxSectionPosition(); layer++) {
            if (!set.hasSection(layer)) {
                continue;
            }

            // loadIfPresent shouldn't be null if set.hasSection(layer) is true
            char[] blocksSet = set.loadIfPresent(layer);
            char[] blocksGet = get.loadIfPresent(layer);

            // Account for negative layers
            int by = layer << 4;
            for (int y = 0, index = 0; y < 16; y++) {
                int yy = y + by;
                for (int z = 0; z < 16; z++) {
                    int zz = z + bz;
                    for (int x = 0; x < 16; x++, index++) {
                        final int rawStateSet = blocksSet[index];
                        if (rawStateSet == BlockTypesCache.ReservedIDs.__RESERVED__) continue;

                        final int rawStateGet = blocksGet[index];

                        int xx = bx + x;

                        BlockState stateSet = BlockTypesCache.states[rawStateSet];
                        BlockState stateGet = BlockTypesCache.states[rawStateGet];

                        Location location = new Location(this.world, xx, yy, zz);
                        if (CustomBlocksFactory.isCustomBlockType(stateGet.getBlockType())) {
                            // Remove existing custom block
                            CustomBlock.Advanced.removeFromCustomRegion(location);
                        }

                        // Match custom block with block state and raw place it
                        if (CustomBlocksFactory.isCustomBlockType(stateSet.getBlockType())) {
                            CustomBlock customBlock = IACache.getCustomBlockByBlockData(BukkitAdapter.adapt(stateSet));
                            if (customBlock == null) continue;

                            // Recover custom block
                            CustomBlock.Advanced.placeInCustomRegion(customBlock, location);
                        }
                    }
                }
            }
        }
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
