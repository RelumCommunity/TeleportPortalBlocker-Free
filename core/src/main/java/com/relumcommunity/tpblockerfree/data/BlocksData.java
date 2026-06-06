package com.relumcommunity.tpblockerfree.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.relumcommunity.tpblockerfree.main.Main;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class BlocksData {
    private static Connection con;
    private static Statement stat;
    @Getter
    private static final Map<String, PlayerData> cache = new ConcurrentHashMap<>();
    private static final Logger log = Main.getPlugin().getLog();
    private static final String jsonDef = "{\"eg\":0,\"ep\":0,\"np\":0}";
    private static String path;
    public BlocksData(File sqlite) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        path = sqlite.getAbsolutePath();
        con = DriverManager.getConnection("jdbc:sqlite:" + path);
        stat = con.createStatement();
        stat.execute("CREATE TABLE IF NOT EXISTS BlocksData (username TEXT PRIMARY KEY, break TEXT, place TEXT)");
        ResultSet rs = stat.executeQuery("SELECT * FROM BlocksData");
        while (rs.next()) {
            addPlayerToCache(rs.getString("username"), rs.getString("break"), rs.getString("place"));
        }
        con.close();
        FileConfiguration cfg = Main.getCfg();
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), () -> syncToDatabase(null), 20 * 60 * cfg.getLong("DBSyncTime", 10), 20 * 60 * cfg.getLong("DBSyncTime", 10));
    }
    public void closeConnection() throws SQLException {
        syncToDatabase(null);
    }
    private static synchronized void syncToDatabase(String user) {
        FileConfiguration cfg = Main.getCfg();
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + path);
            stat = con.createStatement();
            try {
                if (user == null) {
                    for (Map.Entry<String, PlayerData> entry : cache.entrySet()) {
                        sync(entry.getValue(), entry.getKey());
                    }
                    if (cfg.getBoolean("Debug")) {
                        log.info("[DEBUG] Database Synced");
                    }
                } else {
                    sync(cache.get(user), user);
                    if (cfg.getBoolean("Debug")) {
                        log.info("[DEBUG] User " + user + " database Synced");
                    }
                }
            } finally {
                con.close();
            }
        } catch (SQLException e) {
            log.warning("Error occured while syncing database!");
            if (cfg.getBoolean("Debug")) {
                log.warning("[DEBUG] Debug trace: " + Arrays.toString(e.getStackTrace()));
            }
        }
    }
    private static void sync(PlayerData data, String user) throws SQLException {
        String breakData = new Gson().toJson(data.getStats().get("break"));
        String placeData = new Gson().toJson(data.getStats().get("place"));
        stat.executeUpdate("INSERT OR REPLACE INTO BlocksData (username, break, place) VALUES ('" + user + "', '" + breakData + "', '" + placeData + "')");
    }
    public static void addPlayerToCache(String username, String breakData, String placeData) {
        PlayerData playerData = new PlayerData();
        playerData.setStat("break", new Gson().fromJson(breakData == null ? jsonDef : breakData, new TypeToken<Map<String, Integer>>(){}.getType()));
        playerData.setStat("place", new Gson().fromJson(placeData == null ? jsonDef : placeData, new TypeToken<Map<String, Integer>>(){}.getType()));
        cache.put(username, playerData);
    }
    public static void syncLeftPlayer(String username) {
        syncToDatabase(username);
    }
    @Data
    public static class PlayerData {
        private Map<String, Map<String, Integer>> stats = new HashMap<>();
        public int getStat(String action, String key) {
            return stats.get(action).get(key);
        }
        private void setStat(String action, Map<String, Integer> values) {
            stats.put(action, values);
        }
        public void modifyStat(String action, String key, int value) {
            stats.get(action).put(key, getStat(action, key) + value);
        }
        public void resetStat(String action, String key) {
            if (action.equals("all")) {
                if (key.equals("all")) {
                    stats.forEach((k, v) -> v.replaceAll((k2, v2) -> 0));
                } else {
                    stats.forEach((k, v) -> v.replace(key, 0));
                }
            } else {
                if (key.equals("all")) {
                    stats.get(action).replaceAll((k, v) -> 0);
                } else {
                    stats.get(action).replace(key, 0);
                }
            }
        }
    }
}