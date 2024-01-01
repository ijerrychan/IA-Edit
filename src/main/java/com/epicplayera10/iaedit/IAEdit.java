package com.epicplayera10.iaedit;

import com.epicplayera10.iaedit.hook.CustomBlocksFactory;
import com.epicplayera10.iaedit.hook.listener.FaweListener;
import com.epicplayera10.iaedit.listeners.ItemsAdderListener;
import com.sk89q.worldedit.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class IAEdit extends JavaPlugin implements CommandExecutor, Listener {
    private static IAEdit instance;

    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new ItemsAdderListener(), this);

        WorldEdit.getInstance().getEventBus().register(new FaweListener());
        WorldEdit.getInstance().getBlockFactory().register(new CustomBlocksFactory());
    }

    public static IAEdit instance() {
        return instance;
    }
}
