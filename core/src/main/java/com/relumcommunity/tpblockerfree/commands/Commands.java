package com.relumcommunity.tpblockerfree.commands;

import com.relumcommunity.tpblockerfree.data.BlocksData;
import com.relumcommunity.tpblockerfree.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;

public class Commands implements CommandExecutor {
    private static final Map<String, String> perms = Map.of("help", "tpblocker.help", "reload", "tpblocker.reload", "kinds", "tpblocker.kinds", "types", "tpblocker.types", "aliases", "tpblocker.aliases", "resetdata", "tpblocker.resetdata", "resetname", "tpblocker.resetname");
    private static final Map<String, String> aliases = Map.of("netherportal", "np", "endportal", "ep", "endgateway", "eg");
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender != null) {
            if (cmd.getName().equalsIgnoreCase("tpblocker")) {
                String prefix = Main.getCfg().getString("Prefix", "§7[TPBlockerFree] ");
                if (args.length > 0) {
                    FileConfiguration lang = Main.getLang();
                    String noPerm = lang.getString("Error.NoPerm", prefix + "You don't have permission to do that").replaceAll("%prefix%", prefix).replaceAll("&", "§");
                    switch(args[0]) {
                        case "help" -> {
                            if (sender.hasPermission(perms.get("help"))) {
                                List<String> help = lang.getStringList("Messages.Help");
                                for (String s : help) {
                                    sender.sendMessage(s.replaceAll("%prefix%", prefix).replaceAll("&", "§").replaceAll("%label%", label).replaceAll("%ver%", Main.getPluginVersion()));
                                }
                            } else {
                                sender.sendMessage(noPerm);
                            }
                        }
                        case "reload" -> {
                            if (sender.hasPermission(perms.get("reload"))) {
                                Main.getPlugin().reloadFiles(sender);
                            } else {
                                sender.sendMessage(noPerm);
                            }
                        }
                        case "kinds" -> {
                            if (sender.hasPermission(perms.get("kinds"))) {
                                sender.sendMessage("§aKinds list\nKinds = Break - Place - all");
                            } else {
                                sender.sendMessage(noPerm);
                            }
                        }
                        case "types" -> {
                            if (sender.hasPermission(perms.get("types"))) {
                                sender.sendMessage("§aTypes list\nNetherPortal = np\nEndPortal = ep\nEndGateway = eg");
                            } else {
                                sender.sendMessage(noPerm);
                            }
                        }
                        case "aliases" -> {
                            if (sender.hasPermission(perms.get("aliases"))) {
                                sender.sendMessage("§aAliases list\ntpblocker = tpb - tportalb\nNetherPortal = np\nEndPortal = ep\nEndGateway = eg");
                            } else {
                                sender.sendMessage(noPerm);
                            }
                        }
                        case "resetdata" -> {
                            if (args.length == 3) {
                                if (args[1].equalsIgnoreCase("Break") || args[1].equalsIgnoreCase("Place") || args[1].equalsIgnoreCase("all")) {
                                    String alias = aliases.get(args[2]) != null ? aliases.get(args[2]) : aliases.containsValue(args[2]) || args[2].equalsIgnoreCase("all") ? args[2] : null;
                                    if (alias != null) {
                                        if (sender.hasPermission(perms.get("resetdata"))) {
                                            BlocksData.getCache().forEach((k, v) -> v.resetStat(args[1].toLowerCase(), alias));
                                            sender.sendMessage(lang.getString("Database.ResetData", "Missing: Database.ResetData").replaceAll("%prefix%", prefix).replaceAll("%kind%", args[1]).replaceAll("%type%", alias == null ? "" : alias).replaceAll("&", "§"));
                                        } else {
                                            sender.sendMessage(noPerm);
                                        }
                                    }
                                }
                            }
                        }
                        case "resetname" -> {
                            if (args.length == 4) {
                                if (args[2].equalsIgnoreCase("Break") || args[2].equalsIgnoreCase("Place") || args[2].equalsIgnoreCase("all")) {
                                    String alias = aliases.get(args[3]) != null ? aliases.get(args[3]) : aliases.containsValue(args[3]) || args[3].equalsIgnoreCase("all") ? args[3] : null;
                                    if (alias != null) {
                                        if (sender.hasPermission(perms.get("resetname"))) {
                                            if (BlocksData.getCache().get(args[1]) != null) {
                                                BlocksData.getCache().get(args[1]).resetStat(args[2].toLowerCase(), alias);
                                                sender.sendMessage(lang.getString("Database.ResetName", "Missing: Database.ResetName").replaceAll("%prefix%", prefix).replaceAll("%kind%", args[2]).replaceAll("%type%", alias == null ? "" : alias).replaceAll("%player%", args[1]).replaceAll("&", "§"));
                                            } else {
                                                sender.sendMessage(lang.getString("Database.WrongName", "Missing: Database.WrongName").replaceAll("%prefix%", prefix).replaceAll("%player%", args[1]).replaceAll("&", "§"));
                                            }
                                        } else {
                                            sender.sendMessage(noPerm);
                                        }
                                    }
                                }
                            }
                        }
                        default -> sender.sendMessage(prefix.replaceAll("&", "§") + "§cUsage: /" + label + " help");
                    }
                } else {
                    sender.sendMessage(prefix.replaceAll("&", "§") + "§cUsage: /" + label + " help");
                }
            }
        }
        return true;
    }
}
