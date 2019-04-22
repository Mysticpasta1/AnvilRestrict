package me.poma123.anvilrestrict;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AnvilRestrict extends JavaPlugin {
	public static Boolean noEntryAll = Boolean.valueOf(false);
	public static Boolean noRenameAll = Boolean.valueOf(false);
	public static ArrayList<ArrayList<String>> noEntry = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> noRename = new ArrayList<ArrayList<String>>();

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
						if (!new File(getDataFolder(), "config.yml").exists()) {
							saveDefaultConfig();
						}
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
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
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
		noEntryAll = Boolean.valueOf(getConfig().getBoolean("NoEntryAll"));
		noRenameAll = Boolean.valueOf(getConfig().getBoolean("NoRenameAll"));
		String[] NoEntryTemp = getConfig().getString("NoEntry").split("\\,");
		String[] arrayOfString1;
		int j = (arrayOfString1 = NoEntryTemp).length;
		String[] ettemp;
		for (int i = 0; i < j; i++) {
			String EntryTemp = arrayOfString1[i];
			ettemp = EntryTemp.split("\\|");
			ArrayList<String> noEntryt = new ArrayList<String>();
			if (ettemp.length >= 1) {
				noEntryt.add(ettemp[0]);
				noEntryt.add(ettemp[1]);
			}
			noEntry.add(noEntryt);
		}
		String[] NoRenameTemp = getConfig().getString("NoRename").split("\\,");
		int k = (ettemp = NoRenameTemp).length;
		for (j = 0; j < k; j++) {
			String NoRename = ettemp[j];
			ettemp = NoRename.split("\\|");
			ArrayList<String> noRenamet = new ArrayList<String>();
			if (ettemp.length >= 1) {
				noRenamet.add(ettemp[0]);
				noRenamet.add(ettemp[1]);
			}
			noRename.add(noRenamet);
		}
	}
}
