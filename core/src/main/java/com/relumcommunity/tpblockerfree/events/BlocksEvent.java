package com.relumcommunity.tpblockerfree.events;

import com.relumcommunity.tpblockerfree.main.Main;
import com.relumcommunity.tpblockerfree.data.BlocksData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class BlocksEvent implements Listener {
    private static final Map<String, Map<String, Map<String, Boolean>>> blocks = new HashMap<>(Map.of("Break", new HashMap<>(), "Place", new HashMap<>()));
    private static final Map<String, String> aliases = Map.of("eg", "EndGateway", "ep", "EndPortal", "np", "NetherPortal");
    public BlocksEvent() {
        reloadBlocks();
    }
    public static void reloadBlocks() {
        FileConfiguration cfg = Main.getCfg();
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            String key = entry.getValue();
            String alias = entry.getKey();
            blocks.get("Break").computeIfAbsent(cfg.getString(key + ".BlockBreak.Block"), k -> new HashMap<>()).put(alias, cfg.getBoolean(key + ".BlockBreak.Active"));
            blocks.get("Place").computeIfAbsent(cfg.getString(key + ".BlockPlace.Block"), k -> new HashMap<>()).put(alias, cfg.getBoolean(key + ".BlockPlace.Active"));
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        BlocksHandler(e.getPlayer(), "Break", e.getBlock().getType().toString());
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        BlocksHandler(e.getPlayer(), "Place", e.getBlock().getType().toString());
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (BlocksData.getCache().get(e.getPlayer().getName()) == null) {
            BlocksData.addPlayerToCache(e.getPlayer().getName(), null, null);
        }
    }
    @EventHandler
    public void onLeft(PlayerQuitEvent e) {
        if (BlocksData.getCache().get(e.getPlayer().getName()) != null) {
            BlocksData.syncLeftPlayer(e.getPlayer().getName());
        }
    }
    private void BlocksHandler(Player p, String Type, String block) {
        if (p != null) {
            if (blocks.get(Type).get(block) != null) {
                for (Map.Entry<String, Boolean> entry : blocks.get(Type).get(block).entrySet()) {
                    if (entry.getValue()) {
                        int blockData = BlocksData.getCache().get(p.getName()).getStat(Type.toLowerCase(), entry.getKey());
                        if (blockData < Main.getCfg().getInt(aliases.get(entry.getKey()) + ".Block" + Type + ".Qnt")) {
                            BlocksData.getCache().get(p.getName()).modifyStat(Type.toLowerCase(), entry.getKey(), 1);
                        }
                    }
                }
            }
        }
    }
}
