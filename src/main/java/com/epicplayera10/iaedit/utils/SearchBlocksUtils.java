package com.epicplayera10.iaedit.utils;

import dev.lone.itemsadder.api.CustomBlock;

import java.util.List;

public class SearchBlocksUtils {
    public static List<String> searchNamespacedBlocks(String searchStr) {
        return CustomBlock.getNamespacedIdsInRegistry().stream()
            .filter(namespacedID -> namespacedID.contains(searchStr))
            .toList();
    }
}
