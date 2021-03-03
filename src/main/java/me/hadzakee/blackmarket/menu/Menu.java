package me.hadzakee.blackmarket.menu;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Menu implements InventoryHolder {
    protected AbstractPlayerMenuUtility pmu;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS;

    public Menu(AbstractPlayerMenuUtility pmu) {
        this.FILLER_GLASS = this.makeItem(Material.BLACK_STAINED_GLASS_PANE, " ");
        this.pmu = pmu;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void setMenuItems();

    public void open() {
        this.inventory = Bukkit.createInventory(this, this.getSlots(), this.getMenuName());
        this.setMenuItems();
        this.pmu.getOwner().openInventory(this.inventory);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setFillerGlass() {
        for(int i = 0; i < this.getSlots(); ++i) {
            if (this.inventory.getItem(i) == null) {
                this.inventory.setItem(i, this.FILLER_GLASS);
            }
        }

    }

    public ItemStack makeItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack makeItem(Material material, int amount, String displayName, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

    public <T> T PMUCaster(AbstractPlayerMenuUtility abstractPlayerMenuUtility, Class<T> t) {
        return t.cast(abstractPlayerMenuUtility);
    }
}