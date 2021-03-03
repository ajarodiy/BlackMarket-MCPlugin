package me.hadzakee.blackmarket.utils;

import me.hadzakee.blackmarket.menu.AbstractPlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuUtility extends AbstractPlayerMenuUtility {

    public MarketItem item;

    public MarketItem getItem() {
        return item;
    }

    public void setItem(MarketItem item) {
        this.item = item;
    }

    public MenuUtility(Player p) {
        super(p);
    }
}
