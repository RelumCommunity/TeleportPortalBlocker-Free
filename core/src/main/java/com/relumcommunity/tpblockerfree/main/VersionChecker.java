package com.relumcommunity.tpblockerfree.main;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class VersionChecker implements Listener {
    private static final String version = Main.getPluginVersion();
    private static String latestVer;
    private static final String tpblockerfree = "https://relumcommunity.com/progetti/plugins/tpblockerfree/redirect.html";
    private static final String tpblockerpremium = "https://relumcommunity.com/progetti/plugins/tpblockerpremium/redirect.html";
    public VersionChecker() {
        if (Main.getCfg().getBoolean("ConsoleCheckUpdate")) {
            Logger log = Main.getPlugin().getLog();
            log.info(" ");
            log.info("         -= TPBlockerFree =-");
            try {
                HttpsURLConnection connection = (HttpsURLConnection)(new URL("https://relumcommunity.com/progetti/plugins/tpblockerfree/version.json")).openConnection();
                latestVer = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();
                if (!version.equals(latestVer)) {
                    log.warning("    You do not have the latest version!");
                    log.warning(" ");
                    log.warning("Current: " + version);
                    log.warning("Latest: " + latestVer);
                    log.warning("Download: " + tpblockerfree);
                } else {
                    log.info("    You are running the latest version!");
                }
            } catch (IOException e) {
                log.severe("Could not make connection to RelumCommunity.com to verify the latest version!");
            }
            log.info(" ");
            log.info("TP Blocker Premium is OUT!");
            log.info("Download: " + tpblockerpremium);
            log.info(" ");
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("tpblocker.update")) {
            String prefix = Main.getCfg().getString("Prefix", "§7[TPBlockerFree] ").replaceAll("&", "§");
            TextComponent component = new TextComponent(prefix + "§eTP Blocker Premium is OUT! Click here to give a look!");
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§bClick to open the plugin page.")));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, tpblockerpremium));
            if (Main.getCfg().getBoolean("CheckUpdate")) {
                if (!latestVer.equals(version)) {
                    TextComponent MSG = new TextComponent(prefix + "§cYou are not in the latest version, please update the plugin from our plugin page");
                    MSG.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§bClick to open the plugin page.")));
                    MSG.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL , tpblockerfree));
                    p.spigot().sendMessage(MSG);
                }
            }
        }
    }
}
