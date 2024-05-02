package com.epicplayera10.iaedit.hook.process;

import com.epicplayera10.iaedit.hook.CustomBlocksFactory;
import com.epicplayera10.iaedit.utils.IACache;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.nbt.CompoundBinaryTag;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomBlocksWorldEditUtils {

    /**
     * The core method that processes the custom block placement or removal.
     */
    public static void processBlock(Location location, BlockStateHolder<?> stateToSet, BlockStateHolder<?> stateBefore) {
        System.out.println("before: "+stateBefore.getNbt());
        if (CustomBlocksFactory.isCustomBlockType(stateBefore.getBlockType())) {
            // Remove existing custom block
            CustomBlock.Advanced.removeFromCustomRegion(location);
        }

        // Match custom block with block state and raw place it
        BlockType stateToSetType = stateToSet.getBlockType();
        if (CustomBlocksFactory.isCustomBlockType(stateToSetType)) {
            CustomBlock customBlock = getCustomBlockByBlockState(stateToSet);
            if (customBlock == null) return;

            // Recover custom block
            CustomBlock.Advanced.placeInCustomRegion(customBlock, location);
        }
    }

    /**
     * Get the custom block by the block state.
     */
    @Nullable
    private static CustomBlock getCustomBlockByBlockState(@NotNull BlockStateHolder<?> blockState) {
        if (blockState.getBlockType() == BlockTypes.SPAWNER && blockState.getNbt() != null) {
            // Tile block (spawner)

            CompoundTag nbt = blockState.getNbtData();
            System.out.println(nbt);
            //System.out.println("spawndata: "+nbt.get("SpawnData"));
            //System.out.println("spawndata2: "+nbt.getCompound("SpawnData"));

            CompoundTag itemNbt = nbt.getC("SpawnData")
                .getCompound("entity")
                .getList("ArmorItems")
                .getCompound(3);

            if (itemNbt.get("id") == null || itemNbt.get("tag") == null) return null;

            String itemId = itemNbt.getString("id");
            CompoundBinaryTag itemTag = itemNbt.getCompound("tag");

            if (itemTag.get("CustomModelData") == null) return null;
            int customModelData = itemTag.getInt("CustomModelData");

            return IACache.getCustomBlockByItemTypeAndCustomModelData(itemId, customModelData);
        } else {
            return IACache.getCustomBlockByBlockData(BukkitAdapter.adapt(blockState));
        }
    }
}
