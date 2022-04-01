package me.poma123.anvilrestrict;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
                    for (String row : getConfig().getStringList("messages.help"))
                        sender.sendMessage(withFormatting(row));
                } else {
                    if (args[0].equalsIgnoreCase("reload")) {
                        saveDefaultConfigIfNotExists();
                        updateConfig();
                        reloadConfig();
                        setup();
                        sender.sendMessage(withFormatting(getConfig().getString("messages.config_reloaded")));
                    } else {
                        for (String row : getConfig().getStringList("messages.help"))
                            sender.sendMessage(withFormatting(row));
                    }
                }
            } else {
                sender.sendMessage(withFormatting(getConfig().getString("messages.nopermission")));
            }
        }
        return true;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        saveDefaultConfigIfNotExists();
        updateConfig();
        setup();
    }

    public String withFormatting(String input) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.prefix") + " &r" + input);
    }

    private void updateConfig() {
        ConfigurationSection messagesSection = getConfig().getConfigurationSection("messages");

        if(messagesSection == null)
            messagesSection = getConfig().createSection("messages");

        if (messagesSection.get("prefix") == null)
            messagesSection.set("prefix", "&8[&7AnvilRestrict&8]");
        if (messagesSection.get("noentry") == null)
            messagesSection.set("noentry", "&cEntering that item in the Anvil is disabled.");
        if (messagesSection.get("norename") == null)
            messagesSection.set("norename", "&cRenaming that item is disabled.");
        if (messagesSection.get("all_entry_disabled") == null)
            messagesSection.set("all_entry_disabled", "&cThe Anvil is disabled.");
        if (messagesSection.get("all_rename_disabled") == null)
            messagesSection.set("all_rename_disabled", "&cRenaming items is disabled.");
        if (messagesSection.get("config_reloaded") == null)
            messagesSection.set("config_reloaded", "&fConfig reloaded!");
        if (messagesSection.get("nopermission") == null)
            messagesSection.set("nopermission", "&cYou don't have permission to perform this command!");
        if (messagesSection.get("help") == null)
            messagesSection.set("help", new ArrayList<>(
                    Arrays.asList(
                            "&cAnvilRestrict HELP:",
                            "",
                            "&6/anvilrestrict reload &f- Reload config",
                            "&cAliases: &7[ar, anvilr]"
                    ))
            );
        saveConfig();
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
