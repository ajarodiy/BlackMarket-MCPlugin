package me.hadzakee.blackmarket.menu;

import org.bukkit.entity.Player;

public abstract class AbstractPlayerMenuUtility {
    private Player owner;

    public AbstractPlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return this.owner;
    }
}