package me.hadzakee.blackmarket.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {
    public MenuListener() {
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu)holder;

            try {
                menu.handleMenu(e);
            } catch (MenuManagerNotSetupException var5) {
                System.out.println("The menu manager has not been configured. Call MenuManager.setup()");
            } catch (MenuManagerException var6) {
                var6.printStackTrace();
            }
        }

    }
}