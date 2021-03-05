package me.hadzakee.blackmarket.events;

import me.hadzakee.blackmarket.utils.MarketUtility;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MarketClose implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (ChatColor.stripColor(e.getView().getTitle()).contains("Black")) {
            MarketUtility.playersWithMenuOpen.remove(e.getPlayer().getUniqueId().toString());
        }
    }
}
