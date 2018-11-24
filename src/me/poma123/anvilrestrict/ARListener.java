package me.poma123.anvilrestrict;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ARListener implements Listener {
	@SuppressWarnings("unused")
	private AnvilRestrict plugin;
	public HashMap<String, String> fixDisplayName = new HashMap<String, String>();

	public ARListener(AnvilRestrict p) {
		this.plugin = p;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
	//debug	event.getWhoClicked().sendMessage(String.valueOf(event.getSlot()));
		if (event.getSlot() != 64537) {
			Player p = (Player) event.getWhoClicked();
			Boolean done = Boolean.valueOf(false);
			ItemStack item = event.getCurrentItem();
			if (event.getInventory().getType() == InventoryType.ANVIL) {
				if (AnvilRestrict.noEntryAll.booleanValue()) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "The Anvil is disabled.");
				} else if (AnvilRestrict.noRenameAll.booleanValue()) {
					if (item != null) {
						String tempname = "";
						if (item.getItemMeta().hasDisplayName()) {
							tempname = item.getItemMeta().getDisplayName();
						}
						if (!tempname.equals(event.getInventory().getItem(0).getItemMeta().getDisplayName())) {
							event.setCancelled(true);
							p.sendMessage(ChatColor.RED + "Renaming items is disabled.");
						}
					}
				} else {
					for (ArrayList<String> noEntryData : AnvilRestrict.noEntry) {
						if (!done.booleanValue()) {
							if ((noEntryData.size() == 2) || (!noEntryData.isEmpty())) {
								if (((String) noEntryData.get(0)).equalsIgnoreCase("id")) {
									if ((item != null)
											&& (item.getType() == Material.getMaterial((String) noEntryData.get(1)))) {
										done = Boolean.valueOf(true);
										event.setCancelled(true);
										p.sendMessage(ChatColor.RED + "Entering that item in the Anvil is disabled.");
									}
								} else if ((((String) noEntryData.get(0)).equalsIgnoreCase("name"))
										&& (item.getItemMeta() != null) && (item.getItemMeta().hasDisplayName())) {
									if (item.getItemMeta().getDisplayName().toLowerCase()
											.indexOf(((String) noEntryData.get(1)).toLowerCase()) >= 0) {
										done = Boolean.valueOf(true);
										event.setCancelled(true);
										p.sendMessage(ChatColor.RED + "Entering that item in the Anvil is disabled.");
									}
								}
							}
						}
					}
					if (event.getSlot() == 2 && event.getClickedInventory().getType().equals(InventoryType.ANVIL)) {
						for (ArrayList<String> noRenameData : AnvilRestrict.noRename) {
							if (!done.booleanValue()) {
								if ((noRenameData.size() == 2) || (!noRenameData.isEmpty())) {
									if (((String) noRenameData.get(0)).equalsIgnoreCase("id")) {
										if ((item != null) && (item.getType() == Material
												.getMaterial((String) noRenameData.get(1)))) {
											String tempname = "";
											if (item.getItemMeta().hasDisplayName()) {
												tempname = item.getItemMeta().getDisplayName();
											}
											if (!tempname.equals(
													event.getInventory().getItem(0).getItemMeta().getDisplayName())) {
												event.setCancelled(true);
												p.sendMessage(ChatColor.RED + "Renaming that item is disabled.");
											}
										}
									} else if ((((String) noRenameData.get(0)).equalsIgnoreCase("name"))
											&& (item.getItemMeta() != null)
											&& (event.getInventory().getItem(0).getItemMeta().hasDisplayName())) {
										if (event.getInventory().getItem(0).getItemMeta().getDisplayName().toLowerCase()
												.indexOf(((String) noRenameData.get(1)).toLowerCase()) >= 0) {
											event.setCancelled(true);
											p.sendMessage(ChatColor.RED + "Renaming that item is disabled.");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
