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
import java.util.Map;
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

        return new CompoundTag(Map.ofEntries(
            Map.entry("SpawnData", new CompoundTag(Map.ofEntries(
                Map.entry("entity", entityNbt)
            ))),
            Map.entry("SpawnPotentials", new ListTag(CompoundTag.class, List.of(
                new CompoundTag(Map.ofEntries(
                    Map.entry("weight", new IntTag(1)),
                    Map.entry("data", new CompoundTag(Map.ofEntries(
                        Map.entry("entity", entityNbt)
                    )))
                ))
            ))),
            Map.entry("id", new StringTag("minecraft:mob_spawner")),
            Map.entry("MaxNearbyEntities", new ShortTag((short) 0)),
            Map.entry("MinSpawnDelay", new ShortTag((short) 1337)),
            Map.entry("SpawnRange", new ShortTag((short) 4)),
            Map.entry("MaxSpawnDelay", new ShortTag((short) 0)),
            Map.entry("RequiredPlayerRange", new ShortTag((short) 0)),
            Map.entry("SpawnCount", new ShortTag((short) 0)),
            Map.entry("Delay", new ShortTag((short) 0))
        ));

        /*return CompoundBinaryTag.builder()
            // Set entity data
            .put("SpawnData", CompoundBinaryTag.builder()
                .put("entity", entityNbt)
                .build()
            )
            .put("SpawnPotentials", ListBinaryTag.builder()
                .add(CompoundBinaryTag.builder()
                    .putInt("weight", 1)
                    .put("data", CompoundBinaryTag.builder()
                        .put("entity", entityNbt)
                        .build()
                    )
                    .build()
                )
                .build()
            )
            // Other properties
            .putString("id", "minecraft:mob_spawner")
            .putInt("x", 0)
            .putInt("y", 0)
            .putInt("z", 0)
            .putShort("MaxNearbyEntities", (short) 0)
            .putShort("MinSpawnDelay", (short) 1337)
            .putShort("SpawnRange", (short) 4)
            .putShort("MaxSpawnDelay", (short) 0)
            .putShort("RequiredPlayerRange", (short) 0)
            .putShort("SpawnCount", (short) 0)
            .putShort("Delay", (short) 0)
            .build();*/
    }

    private CompoundTag createArmorStandEntityNbt(CustomBlock customBlock) {
        ItemStack customBlockItem = customBlock.getItemStack();
        Material material = customBlockItem.getType();
        int customModelData = customBlockItem.getItemMeta().getCustomModelData();

        return new CompoundTag(Map.ofEntries(
            Map.entry("ArmorItems", new ListTag(CompoundTag.class, List.of(
                new CompoundTag(new HashMap<>()),
                new CompoundTag(new HashMap<>()),
                new CompoundTag(new HashMap<>()),
                // Head
                new CompoundTag(Map.of(
                    "id", new StringTag(material.getKey().toString()),
                    "Count", new ByteTag((byte) 1),
                    "tag", new CompoundTag(Map.of(
                        "CustomModelData", new IntTag(customModelData)
                    ))
                ))
            ))),
            Map.entry("Marker", new ByteTag((byte) 1)),
            Map.entry("id", new StringTag("minecraft:armor_stand")),
            Map.entry("NoBasePlate", new ByteTag((byte) 1)),
            Map.entry("Invisible", new ByteTag((byte) 1))
        ));
    }
}
