package com.relumcommunity.tpblockerfree.events;

import com.relumcommunity.tpblockerfree.main.Main;
import org.bukkit.entity.Player;

public class TPEventLegacy extends TPEvent {
    @Override
    protected boolean checkAchievement(Player p, String achievement) {
        try {
            return p.hasAchievement(org.bukkit.Achievement.valueOf(achievement.toUpperCase()));
        } catch (IllegalArgumentException e) {
            if (Main.getCfg().getBoolean("Debug")) {
                Main.getPlugin().getLogger().warning("[DEBUG] The setted achievement: " + achievement + " | doesn't exists, the plugin will consider it as acquired in order to avoid issues, PLS FIX IT!");
            }
            return true;
        }
    }
}