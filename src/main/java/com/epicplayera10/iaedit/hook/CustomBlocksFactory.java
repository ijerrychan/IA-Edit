package com.epicplayera10.iaedit.hook;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.internal.registry.InputParser;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.epicplayera10.iaedit.utils.SearchBlocksUtils;
import dev.lone.itemsadder.api.CustomBlock;

import java.util.stream.Stream;

public class CustomBlocksFactory extends InputParser<BaseBlock> {
    public CustomBlocksFactory() {
        super(WorldEdit.getInstance());
    }

    public static boolean isCustomBlockType(BlockType blockType) {
        return blockType == BlockTypes.NOTE_BLOCK
            || blockType == BlockTypes.MUSHROOM_STEM
            || blockType == BlockTypes.BROWN_MUSHROOM_BLOCK
            || blockType == BlockTypes.RED_MUSHROOM_BLOCK
            || blockType == BlockTypes.CHORUS_PLANT
            || blockType == BlockTypes.TRIPWIRE
            || blockType == BlockTypes.SPAWNER;
    }

    @Override
    public Stream<String> getSuggestions(String input) {
        return input.isEmpty() ? Stream.empty() : SearchBlocksUtils.searchNamespacedBlocks(input).stream();
    }

    @Override
    public BaseBlock parseFromInput(String input, ParserContext context) {
        if (!input.contains(":")) return null;

        CustomBlock customBlock = CustomBlock.getInstance(input);
        if (customBlock == null) return null;

        return createIABlockWithNbt(customBlock);
    }

    private BaseBlock createIABlockWithNbt(CustomBlock customBlock) {
        BlockState blockState;
        if (customBlock.getBaseBlockData() == null) {
            blockState = BlockTypes.SPAWNER.getDefaultState();
        } else {
            blockState = BukkitAdapter.adapt(customBlock.getBaseBlockData());
        }

        // Set nbt for further block place
        return new BaseBlock(blockState);
    }
}
