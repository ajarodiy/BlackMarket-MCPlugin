package me.hadzakee.blackmarket.market;

import me.hadzakee.blackmarket.BlackMarket;
import me.hadzakee.blackmarket.menu.*;
import me.hadzakee.blackmarket.utils.ColorTranslator;
import me.hadzakee.blackmarket.utils.MarketItem;
import me.hadzakee.blackmarket.utils.MarketUtility;
import me.hadzakee.blackmarket.utils.MenuUtility;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.manager.ItemManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MainPage extends Menu {

    MenuUtility mu;
    Player player;
    FileConfiguration data;
    FileConfiguration config;


    public MainPage(AbstractPlayerMenuUtility pmu) {
        super(pmu);
        mu = (MenuUtility) pmu;
        player = mu.getOwner();
        data = BlackMarket.getData();
        config = BlackMarket.getPlugin().getConfig();
    }

    @Override
    public String getMenuName() {
        return ColorTranslator.translateColorCodes("&8&lBlack Market");
    }

    @Override
    public int getSlots() {
        return 18 + config.getInt("ShopSize") * 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        e.setCancelled(true);
        if (!e.getView().getTopInventory().equals(e.getClickedInventory())) return;

        if (e.getCurrentItem() == null || e.getCurrentItem().equals(FILLER_GLASS) ||
                e.getCurrentItem().getType() == Material.BARRIER || e.getCurrentItem().getType() == Material.CLOCK) return;

        mu.setItem(MarketUtility.matchItem(e.getCurrentItem()));
        player.closeInventory();
        new ConfirmationPage(MenuManager.getPlayerMenuUtility(mu.getOwner())).open();
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        ItemStack clock = makeItem(Material.CLOCK, ColorTranslator.translateColorCodes("&6&lBlack Market"), "",
                ColorTranslator.translateColorCodes("&7Every reset will restock the shop  "),
                ColorTranslator.translateColorCodes("&7with different items each time."), "",
                ColorTranslator.translateColorCodes("&6Time until next reset: &a" + MarketUtility.getRemainingTime()));

        int size = config.getInt("ShopSize");
        for (int i=1; i<=size; ++i) {
            for (int j=9*i+1; j < 9*i+8; ++j) {
                inventory.setItem(j, new ItemStack(Material.AIR));
            }
        }


        List<MarketItem> items = MarketUtility.getItemsForCurrentSession();
        int i=10;
        for (MarketItem item : items) {
            inventory.setItem(i, MarketUtility.getRealItem(item));
            i++;
            if (i%9 == 8) i+=2;
        }
        inventory.setItem(4, clock);
    }
}
