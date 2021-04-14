package me.poma123.anvilrestrict;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AnvilRestrict extends JavaPlugin {
    public static Boolean noEntryAll = Boolean.FALSE;
    public static Boolean noRenameAll = Boolean.FALSE;
    public static ArrayList<ArrayList<String>> noEntry = new ArrayList<>();
    public static ArrayList<ArrayList<String>> noRename = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("anvilrestrict")) {
            if (sender.hasPermission("anvilrestrict.admin")) {
                if (args.length == 0) {
                    sender.sendMessage("§8[§7AnvilRestrict§8] §cAnvilRestrict HELP:");
                    sender.sendMessage("§8[§7AnvilRestrict§8]");
                    sender.sendMessage("§8[§7AnvilRestrict§8] §6/anvilrestrict reload §f- Reload config");
                    sender.sendMessage("§8[§7AnvilRestrict§8] §cAliases: §7[ar, anvilr]");
                } else {
                    if (args[0].equalsIgnoreCase("reload")) {
                        saveDefaultConfigIfNotExists();
                        if (getConfig().getConfigurationSection("messages") == null) {
                            getConfig().set("messages.noentry", "&cEntering that item in the Anvil is disabled.");
                            getConfig().set("messages.norename", "&cRenaming that item is disabled.");
                            getConfig().set("messages.all_entry_disabled", "&cThe Anvil is disabled.");
                            getConfig().set("messages.all_rename_disabled", "&cRenaming items is disabled.");
                            saveConfig();
                        }
                        reloadConfig();
                        setup();
                        sender.sendMessage("§8[§7AnvilRestrict§8] §fConfig reloaded!");
                    } else {
                        sender.sendMessage("§8[§7AnvilRestrict§8] §cAnvilRestrict HELP:");
                        sender.sendMessage("§8[§7AnvilRestrict§8]");
                        sender.sendMessage("§8[§7AnvilRestrict§8] §6/anvilrestrict reload §f- Reload config");
                        sender.sendMessage("§8[§7AnvilRestrict§8] §cAliases: §7[ar, anvilr]");
                    }
                }
            } else {
                sender.sendMessage("§cYou don't have permission to perform this command!");
            }
        }
        return true;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ARListener(this), this);
        saveDefaultConfigIfNotExists();
        if (getConfig().getConfigurationSection("messages") == null) {
            getConfig().set("messages.noentry", "&cEntering that item in the Anvil is disabled.");
            getConfig().set("messages.norename", "&cRenaming that item is disabled.");
            getConfig().set("messages.all_entry_disabled", "&cThe Anvil is disabled.");
            getConfig().set("messages.all_rename_disabled", "&cRenaming items is disabled.");
            saveConfig();
        }
        setup();
    }

    public static String withColors(String input, char ch) {
        return ChatColor.translateAlternateColorCodes(ch, input);
    }

    public void setup() {
        try {
            noEntryAll = getConfig().getBoolean("NoEntryAll");
            noRenameAll = getConfig().getBoolean("NoRenameAll");
            int j;
            try {
                String[] noEntryArray = Objects.requireNonNull(getConfig().getString("NoEntry")).split(",");
                j = noEntryArray.length;
                for (int i = 0; i < j; i++) {
                    String noEntryTmp = noEntryArray[i];
                    String[] noEntrySplit = noEntryTmp.split("\\|");
                    ArrayList<String> noEntryList = new ArrayList<>();
                    if (noEntrySplit.length == 2) {
                        noEntryList.add(noEntrySplit[0]);
                        noEntryList.add(noEntrySplit[1]);
                    }
                    noEntry.add(noEntryList);
                }
            } catch (NullPointerException e) {
                getLogger().warning("NoEntry config field is empty or not set at all...");
            }
            try {
                String[] noRenameArray = Objects.requireNonNull(getConfig().getString("NoRename")).split(",");
                j = noRenameArray.length;
                for (int i = 0; i < j; i++) {
                    String noRenameTmp = noRenameArray[i];
                    String[] noRenameSplit = noRenameTmp.split("\\|");
                    ArrayList<String> noRenameList = new ArrayList<>();
                    if (noRenameSplit.length == 2) {
                        noRenameList.add(noRenameSplit[0]);
                        noRenameList.add(noRenameSplit[1]);
                    }
                    noRename.add(noRenameList);
                }
            } catch (NullPointerException e) {
                getLogger().warning("NoRename config field is empty or not set at all...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void saveDefaultConfigIfNotExists() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
    }
}
