package me.hadzakee.blackmarket.events;

import me.hadzakee.blackmarket.utils.MarketUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        MarketUtility.playersWithMenuOpen.remove(e.getPlayer().getUniqueId().toString());
    }
}
