package me.poma123.anvilrestrict;

import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private final AnvilRestrict plugin;

    public InventoryListener(AnvilRestrict p) {
        this.plugin = p;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            if (event.getSlot() != 64537) {
                Player p = (Player) event.getWhoClicked();
                boolean done = Boolean.FALSE;
                ItemStack item = event.getCurrentItem();
                if (event.getInventory().getType() == InventoryType.ANVIL) {
                    if (AnvilRestrict.noEntryAll) {
                        event.setCancelled(true);
                        p.sendMessage(plugin.withFormatting(plugin.getConfig().getString("messages.all_entry_disabled")));
                    } else if (AnvilRestrict.noRenameAll) {
                        if (item != null) {
                            String tempname = "";
                            if (Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
                                tempname = item.getItemMeta().getDisplayName();
                            }
                            if (!tempname.equals(Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(0)).getItemMeta()).getDisplayName())) {
                                event.setCancelled(true);
                                p.sendMessage(plugin.withFormatting(plugin.getConfig().getString("messages.all_rename_disabled")));
                            }
                        }
                    } else {
                        for (ArrayList<String> noEntryData : AnvilRestrict.noEntry) {
                            if (!done) {
                                if (!noEntryData.isEmpty()) {
                                    if (noEntryData.get(0).equalsIgnoreCase("id")) {
                                        if ((item != null)
                                                && (item.getType() == Material.getMaterial(noEntryData.get(1)))) {
                                            done = Boolean.TRUE;
                                            event.setCancelled(true);
                                            p.sendMessage(plugin.withFormatting(plugin.getConfig().getString("messages.noentry")));
                                        }
                                    } else {
                                        if ((noEntryData.get(0).equalsIgnoreCase("name"))) {
                                            assert item != null;
                                            if ((item.getItemMeta() != null) && (item.getItemMeta().hasDisplayName())) {
                                                if (item.getItemMeta().getDisplayName().toLowerCase().contains(noEntryData.get(1).toLowerCase())) {
                                                    done = Boolean.TRUE;
                                                    event.setCancelled(true);
                                                    p.sendMessage(plugin.withFormatting(plugin.getConfig().getString("messages.noentry")));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (event.getSlot() == 2 && Objects.requireNonNull(event.getClickedInventory()).getType().equals(InventoryType.ANVIL)) {
                            for (ArrayList<String> noRenameData : AnvilRestrict.noRename) {
                                if (!done) {
                                    if (!noRenameData.isEmpty()) {
                                        if (noRenameData.get(0).equalsIgnoreCase("id")) {
                                            if ((item != null) && (item.getType() == Material
                                                    .getMaterial(noRenameData.get(1)))) {
                                                String tempname = "";
                                                if (Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
                                                    tempname = item.getItemMeta().getDisplayName();
                                                }
                                                if (!tempname.equals(
                                                        Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(0)).getItemMeta()).getDisplayName())) {
                                                    event.setCancelled(true);
                                                    p.sendMessage(plugin.withFormatting(plugin.getConfig().getString("messages.norename")));
                                                }
                                            }
                                        } else if ((noRenameData.get(0).equalsIgnoreCase("name"))
                                                && (Objects.requireNonNull(item).getItemMeta() != null)
                                                && (Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(0)).getItemMeta()).hasDisplayName())) {
                                            if (Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(0)).getItemMeta()).getDisplayName().toLowerCase().contains(noRenameData.get(1).toLowerCase())) {
                                                event.setCancelled(true);
                                                p.sendMessage(plugin.withFormatting(plugin.getConfig().getString("messages.norename")));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {

        }
    }
}
