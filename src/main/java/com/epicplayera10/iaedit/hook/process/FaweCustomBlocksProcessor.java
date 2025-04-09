package com.epicplayera10.iaedit.hook.process;

import com.fastasyncworldedit.core.queue.IBatchProcessor;
import com.fastasyncworldedit.core.queue.IChunk;
import com.fastasyncworldedit.core.queue.IChunkGet;
import com.fastasyncworldedit.core.queue.IChunkSet;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypesCache;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

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

            // Account for negative layers
            int by = layer << 4;
            for (int y = 0; y < 16; y++) {
                int yy = y + by;
                for (int z = 0; z < 16; z++) {
                    int zz = z + bz;
                    for (int x = 0; x < 16; x++) {

                        BlockState stateSet = set.getBlock(x, yy, z);
                        if (stateSet.getInternalId() == BlockTypesCache.ReservedIDs.__RESERVED__) continue;
                        BlockState stateGet = get.getBlock(x, yy, z);

                        int xx = bx + x;

                        // If they are the same, skip
                        if (stateSet == stateGet) continue;

                        Location location = new Location(this.world, xx, yy, zz);

                        CustomBlocksWorldEditUtils.processBlock(location, stateSet, stateGet, stateSet.getNbtData(), stateGet.getNbtData());
                    }
                }
            }
        }
        return set;
    }

    @Nullable
    @Override
    public Extent construct(Extent child) {
        throw new UnsupportedOperationException("Processing only");
    }
}
