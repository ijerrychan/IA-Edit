package com.epicplayera10.iaedit.hook;

import com.sk89q.jnbt.*;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.internal.registry.InputParser;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
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
        return CustomBlock.getNamespacedIdsInRegistry().stream()
                .filter(namespacedID -> namespacedID.contains(input));
    }


    @Override
    public BaseBlock parseFromInput(String input, ParserContext context) {
        if (!input.contains(":")) return null;

        CustomBlock customBlock = CustomBlock.getInstance(input);
        if (customBlock == null) return null;

        return createBaseBlockFromCustomBlock(customBlock);
    }

    /**
     * Creates a worldedit {@link BaseBlock} from a custom block.
     */
    private BaseBlock createBaseBlockFromCustomBlock(CustomBlock customBlock) {
        if (customBlock.getBaseBlockData() == null) {
            // Tile block (Spawner)
            BlockState blockState = BlockTypes.SPAWNER.getDefaultState();
            CompoundTag nbt = createCustomBlockSpawnerNbt(customBlock);

            return blockState.toBaseBlock(nbt);
        }

        BlockState blockState = BukkitAdapter.adapt(customBlock.getBaseBlockData());

        return blockState.toBaseBlock();
    }

    /**
     * Creates the NBT data for a custom block spawner. Recreated from placed tile block.
     */
    private CompoundTag createCustomBlockSpawnerNbt(CustomBlock customBlock) {
        CompoundTag entityNbt = createArmorStandEntityNbt(customBlock);

        return new CompoundTag(new HashMap<>()).createBuilder()
            .put("SpawnData", new CompoundTag(new HashMap<>()).createBuilder()
                .put("entity", entityNbt)
                .build()
            )
            .put("SpawnPotentials", new ListTag(CompoundTag.class, List.of(
                new CompoundTag(new HashMap<>()).createBuilder()
                    .putInt("weight", 1)
                    .put("data", new CompoundTag(new HashMap<>()).createBuilder()
                        .put("entity", entityNbt)
                        .build()
                    )
                    .build()
            )))
            .putString("id", "minecraft:mob_spawner")
            .putShort("MaxNearbyEntities", (short) 0)
            .putShort("MinSpawnDelay", (short) 1337)
            .putShort("SpawnRange", (short) 4)
            .putShort("MaxSpawnDelay", (short) 0)
            .putShort("RequiredPlayerRange", (short) 0)
            .putShort("SpawnCount", (short) 0)
            .putShort("Delay", (short) 0)
            .build();
    }

    private CompoundTag createArmorStandEntityNbt(CustomBlock customBlock) {
        ItemStack customBlockItem = customBlock.getItemStack();
        Material material = customBlockItem.getType();
        int customModelData = customBlockItem.getItemMeta().getCustomModelData();

        return new CompoundTag(new HashMap<>()).createBuilder()
            .put("ArmorItems", new ListTag(CompoundTag.class, List.of(
                new CompoundTag(new HashMap<>()),
                new CompoundTag(new HashMap<>()),
                new CompoundTag(new HashMap<>()),
                // Head
                new CompoundTag(new HashMap<>()).createBuilder()
                    .putString("id", material.getKey().toString())
                    .putByte("Count", (byte) 1)
                    .put("tag", new CompoundTag(new HashMap<>()).createBuilder()
                        .putInt("CustomModelData", customModelData)
                        .build()
                    )
                    .build()
            )))
            .putByte("Marker", (byte) 1)
            .putString("id", "minecraft:armor_stand")
            .putByte("NoBasePlate", (byte) 1)
            .putByte("Invisible", (byte) 1)
            .build();
    }
}
