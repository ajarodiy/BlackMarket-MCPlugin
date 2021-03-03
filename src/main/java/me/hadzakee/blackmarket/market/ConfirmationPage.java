package me.hadzakee.blackmarket.market;

import me.hadzakee.blackmarket.BlackMarket;
import me.hadzakee.blackmarket.menu.*;
import me.hadzakee.blackmarket.utils.ColorTranslator;
import me.hadzakee.blackmarket.utils.MarketUtility;
import me.hadzakee.blackmarket.utils.MenuUtility;
import me.hadzakee.blackmarket.utils.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmationPage extends Menu {

    MenuUtility mu;
    Player player;
    FileConfiguration data;
    FileConfiguration config;
    Economy economy;

    public ConfirmationPage(AbstractPlayerMenuUtility pmu) {
        super(pmu);
        mu = (MenuUtility) pmu;
        player = mu.getOwner();
        data = BlackMarket.getData();
        config = BlackMarket.getPlugin().getConfig();
        economy = BlackMarket.getEconomy();
    }

    @Override
    public String getMenuName() {
        return ColorTranslator.translateColorCodes("&8&lConfirm Purchase");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        e.setCancelled(true);
        if (!e.getView().getTopInventory().equals(e.getClickedInventory())) return;

        if (e.getCurrentItem() == null) return;
        switch (e.getCurrentItem().getType()) {
            case LIME_WOOL:
                player.closeInventory();
                confirmPurchase();
                break;
            case RED_WOOL:
                player.closeInventory();
                new MainPage(MenuManager.getPlayerMenuUtility(player)).open();
                break;
            default:
                return;
        }

    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        ItemStack confirm = makeItem(Material.LIME_WOOL, ColorTranslator.translateColorCodes("&#2af21f&lConfirm"));
        ItemStack cancel = makeItem(Material.RED_WOOL, ColorTranslator.translateColorCodes("&#f72525&lCancel"));

        inventory.setItem(10, cancel);
        inventory.setItem(13, mu.getItem().getItem());
        inventory.setItem(16, confirm);
    }

    public void confirmPurchase() {
        int price = mu.getItem().getCurrentPrice();

        if (economy.getBalance(player) >= price) {
            if (!hasSpace()) {
                player.sendMessage(MessageUtils.message("You don't have space in your inventory"));
                return;
            }

            EconomyResponse response = economy.withdrawPlayer(player, price);
            if (response.transactionSuccess()) {
                player.getInventory().addItem(mu.getItem().getItem());
                MarketUtility.changeBoughtStatus(mu.getItem().getIndex(), player.getName());
                MarketUtility.updateItemsForCurrentSession();
                player.sendMessage(MessageUtils.message("You have successfully purchased the item."));
                player.closeInventory();
            }else{
                player.sendMessage(MessageUtils.message("Transaction error. Please try again later"));
            }
        }else{
            player.sendMessage(MessageUtils.message("You don't have enough balance."));
        }
    }

    public boolean hasSpace() {
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item == null || item.getType().isAir()) return true;
        }
        return false;
    }
}
