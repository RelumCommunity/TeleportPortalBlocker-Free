package com.relumcommunity.tpblockerfree.main;

import com.relumcommunity.tpblockerfree.commands.Commands;
import com.relumcommunity.tpblockerfree.data.BlocksData;
import com.relumcommunity.tpblockerfree.events.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    @Getter
    private static Main plugin;
    @Getter
    private static String pluginVersion;
    @Getter
    private final Logger log = getLogger();
    @Getter
    private static FileConfiguration cfg;
    @Getter
    private static FileConfiguration lang;
    private BlocksData blocksData;
    public void onEnable() {
        plugin = this;
        pluginVersion = plugin.getDescription().getVersion();
        PluginManager bpm = Bukkit.getPluginManager();
        File sqlite = new File(getDataFolder(), "BlocksData.sqlite");
        String[] ver = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        String version = ver[0] + "." + ver[1] + (ver.length > 2 ? "." + ver[2] : "");
        String server = Bukkit.getVersion().contains("Spigot") ? "Spigot" : Bukkit.getName();
        if (server.equals("Spigot") || server.equals("Paper")) {
            log.info("TPBlockerFree is using: " + server + " | Version: " + version);
        } else {
            log.info("TPBlockerFree is using: " + version + " UNTESTED SERVER VERSION");
        }
        saveDefaultConfig();
        cfg = plugin.getConfig();
        setupLang();
        new VersionChecker();
        bpm.registerEvents(new BlocksEvent(), plugin);
        int mcVersion = Integer.parseInt(version.split("\\.")[0]) > 1 ? Integer.parseInt(version.split("\\.")[0]) : Integer.parseInt(version.split("\\.")[1]);
        if (mcVersion >= 12) {
            bpm.registerEvents(new TPEvent(), plugin);
        } else {
            try {
                bpm.registerEvents((Listener) Class.forName("com.relumcommunity.tpblockerfree.events.TPEventLegacy").getDeclaredConstructor().newInstance(), plugin);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                log.severe("An error occurred while enabling legacy version");
                if (plugin.getConfig().getBoolean("Debug")) {
                    log.severe("[DEBUG] Debug trace: " + Arrays.toString(e.getStackTrace()));
                }
                throw new RuntimeException(e);
            }
        }
        getCommand("tpblocker").setExecutor(new Commands());
        try {
            blocksData = new BlocksData(sqlite);
        } catch (ClassNotFoundException | SQLException e) {
            log.severe("An error occurred while connecting to the sqlite file");
            if (plugin.getConfig().getBoolean("Debug")) {
                log.severe("[DEBUG] Debug trace: " + Arrays.toString(e.getStackTrace()));
            }
            throw new RuntimeException(e);
        }
        log.info("TPBlockerFree " + pluginVersion + " has been enabled");
    }
    public void onDisable() {
        if (blocksData != null) {
            try {
                blocksData.closeConnection();
            } catch (SQLException e) {
                log.warning("An error occurred while closing connection with the sqlite file");
                if (plugin.getConfig().getBoolean("Debug")) {
                    log.warning("[DEBUG] Debug trace: " + Arrays.toString(e.getStackTrace()));
                }
            }
        }
        log.info("TPBlockerFree " + pluginVersion + " has been disabled");
    }
    private void setupLang() {
        for (String fileName : new String[] {"en_US.yml", "it_IT.yml", "es_ES.yml"}) {
            try {
                copyFile(fileName);
            } catch (IOException e) {
                log.warning("Error occured while initializing '" + fileName + "'!");
                if (cfg.getBoolean("Debug")) {
                    log.warning("[DEBUG] Debug trace: " + Arrays.toString(e.getStackTrace()));
                }
            }
        }
        lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/lang/", cfg.getString("Language") + ".yml"));
    }
    private void copyFile(String fileName) throws IOException {
        File file = new File(plugin.getDataFolder() + "/lang/", fileName);
        if (!file.exists()) {
            saveResource("lang/" + fileName, false);
            log.info("Creating language file '" + fileName + "'.");
        }
    }
    public void reloadFiles(CommandSender sender) {
        plugin = this;
        reloadConfig();
        cfg = plugin.getConfig();
        setupLang();
        BlocksEvent.reloadBlocks();
        sender.sendMessage(lang.getString("Messages.Reload", "Plugin Reloaded").replaceAll("%prefix%", cfg.getString("Prefix", "§7[TPBlockerFree] ")).replaceAll("&", "§"));
    }
}
