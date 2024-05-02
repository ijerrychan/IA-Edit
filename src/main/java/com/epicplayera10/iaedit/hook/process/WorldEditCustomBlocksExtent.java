package com.epicplayera10.iaedit.hook.process;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldEditCustomBlocksExtent extends AbstractDelegateExtent {
    private final World world;

    public WorldEditCustomBlocksExtent(Extent extent, World world) {
        super(extent);
        this.world = world;
    }

    @Override
    public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 position, T stateToSet) throws WorldEditException {
        Location location = BukkitAdapter.adapt(this.world, position);
        BlockState stateBefore = BukkitAdapter.adapt(location.getBlock().getBlockData());

        CustomBlocksWorldEditUtils.processBlock(location, stateToSet, stateBefore, stateToSet.toBaseBlock().getNbtData(), stateBefore.toBaseBlock().getNbtData());

        return super.setBlock(position, stateToSet);
    }
}
