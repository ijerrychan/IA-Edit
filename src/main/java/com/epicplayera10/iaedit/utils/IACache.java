package com.epicplayera10.iaedit.utils;

import dev.lone.itemsadder.api.CustomBlock;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class IACache {

    private static final Map<BlockData, CustomBlock> BLOCK_DATA_TO_CUSTOM_BLOCK_CACHE = new HashMap<>();
    private static final Map<ObjectIntPair<String>, CustomBlock> ITEM_TYPE_CUSTOM_MODEL_DATA_TO_CUSTOM_BLOCK_CACHE = new HashMap<>();

    public static void init() {
        BLOCK_DATA_TO_CUSTOM_BLOCK_CACHE.clear();
        ITEM_TYPE_CUSTOM_MODEL_DATA_TO_CUSTOM_BLOCK_CACHE.clear();

        for (String customBlockId : CustomBlock.getNamespacedIdsInRegistry()) {
            CustomBlock customBlock = CustomBlock.getInstance(customBlockId);

            BlockData customBlockBlockData = customBlock.getBaseBlockData();
            if (customBlockBlockData == null) continue;

            BLOCK_DATA_TO_CUSTOM_BLOCK_CACHE.put(customBlockBlockData, customBlock);

            ItemStack customBlockItem = customBlock.getItemStack();
            ITEM_TYPE_CUSTOM_MODEL_DATA_TO_CUSTOM_BLOCK_CACHE.put(
                ObjectIntPair.of(customBlockItem.getType().getKey().toString(), customBlockItem.getItemMeta().getCustomModelData()),
                customBlock
            );
        }
    }

    @Nullable
    public static CustomBlock getCustomBlockByBlockData(BlockData blockData) {
        return BLOCK_DATA_TO_CUSTOM_BLOCK_CACHE.get(blockData);
    }

    @Nullable
    public static CustomBlock getCustomBlockByItemTypeAndCustomModelData(String itemId, int customModelData) {
        return ITEM_TYPE_CUSTOM_MODEL_DATA_TO_CUSTOM_BLOCK_CACHE.get(ObjectIntPair.of(itemId, customModelData));
    }
}
