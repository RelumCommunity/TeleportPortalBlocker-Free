package com.relumcommunity.tpblockerfree.events;

import com.relumcommunity.tpblockerfree.data.BlocksData;
import com.relumcommunity.tpblockerfree.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Map;

public class TPEvent implements Listener {
    private static final Map<String, String> teleports = Map.of("END_GATEWAY", "EndGateway", "END_PORTAL", "EndPortal", "NETHER_PORTAL", "NetherPortal");
    private static final Map<String, String> aliases = Map.of("EndGateway", "eg", "EndPortal", "ep","NetherPortal", "np");
    private static final Map<String, String> legacy = Map.of("EndGateway", "END_GATEWAY", "EndPortal", "ENDER_PORTAL", "NetherPortal", "PORTAL");
    protected boolean checkAchievement(Player p, String achievement) {
        Advancement advancement = Bukkit.getAdvancement(NamespacedKey.minecraft(achievement));
        if (advancement != null) {
            return p.getAdvancementProgress(advancement).isDone();
        }
        if (Main.getCfg().getBoolean("Debug")) {
            Main.getPlugin().getLogger().warning("[DEBUG] The setted achievement: " + achievement + " | doesn't exists, the plugin will consider it as acquired in order to avoid issues, PLS FIX IT!");
        }
        return true;
    }
    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        FileConfiguration cfg = Main.getCfg();
        String cause = teleports.getOrDefault(e.getCause().name(), null);
        if (cfg.getBoolean("Debug")) {
            Main.getPlugin().getLogger().info("[DEBUG] TP cause: " + e.getCause().name() + " | got a pair: " + cause);
        }
        if (cause != null) {
            FileConfiguration lang = Main.getLang();
            String prefix = cfg.getString("Prefix", "§7[TPBlockerFree] ");
            if (!cfg.getBoolean(cause + ".PermBlock")) {
                if (cfg.getBoolean(cause + ".AchievementBlock.Active")) {
                    String achievement = cfg.getString(cause + ".AchievementBlock.Achievement", "story/form_obsidian");
                    if (!checkAchievement(p, achievement)) {
                        e.setCancelled(true);
                        if (cfg.getBoolean(cause + ".ErrorMessage")) {
                            p.sendMessage(lang.getString(cause + ".Achievement", "Missing text: " + cause + ".Achievement").replaceAll("%prefix%", prefix).replaceAll("%achievement%", achievement).replaceAll("&", "§"));
                        }
                    }
                }
                if (cfg.getBoolean(cause + ".BlockBreak.Active")) {
                    int blockscounted = BlocksData.getCache().get(p.getName()).getStat("break", aliases.get(cause));
                    int totalblocks = cfg.getInt(cause + ".BlockBreak.Qnt");
                    if (blockscounted < totalblocks) {
                        e.setCancelled(true);
                        if (cfg.getBoolean(cause + ".ErrorMessage")) {
                            p.sendMessage(lang.getString(cause + ".BreakPlaceNeeded", "Missing text: " + cause + ".BreakPlaceNeeded").replaceAll("%prefix%", prefix).replaceAll("%kinds%", lang.getString("Kinds.Break","break")).replaceAll("%blockscounted%", String.valueOf(blockscounted)).replaceAll("%totalblocks%", String.valueOf(totalblocks)).replaceAll("%blocktype%", Material.matchMaterial(cfg.getString(cause + ".BlockBreak.Block", "STONE")).name()).replaceAll("&", "§"));
                        }
                    }
                }
                if (cfg.getBoolean(cause + ".BlockPlace.Active")) {
                    int blockscounted = BlocksData.getCache().get(p.getName()).getStat("place", aliases.get(cause));
                    int totalblocks = cfg.getInt(cause + ".BlockPlace.Qnt");
                    if (blockscounted < totalblocks) {
                        e.setCancelled(true);
                        if (cfg.getBoolean(cause + ".ErrorMessage")) {
                            p.sendMessage(lang.getString(cause + ".BreakPlaceNeeded", "Missing text: " + cause + ".BreakPlaceNeeded").replaceAll("%prefix%", prefix).replaceAll("%kinds%", lang.getString("Kinds.Place","place")).replaceAll("%blockscounted%", String.valueOf(blockscounted)).replaceAll("%totalblocks%", String.valueOf(totalblocks)).replaceAll("%blocktype%", Material.matchMaterial(cfg.getString(cause + ".BlockPlace.Block", "STONE")).name()).replaceAll("&", "§"));
                        }
                    }
                }
            } else {
                e.setCancelled(true);
                if (cfg.getBoolean(cause + ".PermBlockErrorMessage")) {
                    p.sendMessage(lang.getString(cause + ".PermBlock", "Missing text: " + cause + ".PermBlock").replaceAll("%prefix%", prefix).replaceAll("&", "§"));
                }
            }
            if (e.isCancelled()) {
                if (cause.equals("NetherPortal") && cfg.getBoolean(cause + ".Deactivate")) {
                    Location loc = p.getLocation();
                    if (!cfg.getStringList(cause + ".DoNotIn").contains(loc.getWorld().getName())) {
                        Material material = Material.matchMaterial(e.getCause().name()) != null ? Material.matchMaterial(e.getCause().name()) : Material.matchMaterial(legacy.get(cause));
                        if (loc.getWorld().getBlockAt(loc).getType().equals(material)) {
                            loc.getWorld().getBlockAt(loc).setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void createEvent(PortalCreateEvent e) {
        if (e.getReason().equals(PortalCreateEvent.CreateReason.FIRE) && Main.getCfg().getBoolean("NetherPortal.ActivationBlock")) {
            e.setCancelled(true);
        }
    }
}
