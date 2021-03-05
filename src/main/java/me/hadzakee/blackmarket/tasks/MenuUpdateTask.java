package me.hadzakee.blackmarket.tasks;

import me.hadzakee.blackmarket.BlackMarket;
import me.hadzakee.blackmarket.market.MainPage;
import me.hadzakee.blackmarket.menu.MenuManager;
import me.hadzakee.blackmarket.menu.MenuManagerException;
import me.hadzakee.blackmarket.menu.MenuManagerNotSetupException;
import me.hadzakee.blackmarket.utils.ColorTranslator;
import me.hadzakee.blackmarket.utils.MarketUtility;
import me.hadzakee.blackmarket.utils.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MenuUpdateTask extends BukkitRunnable {

    FileConfiguration config;
    FileConfiguration data;


    @Override
    public void run() {

        data = BlackMarket.getData();
        config = BlackMarket.getPlugin().getConfig();

        if (!data.isString("LastUpdate")) {
            MarketUtility.createItemsForCurrentSession();
            Instant time =  Instant.now();
            data.set("LastUpdate", time.toString());
            BlackMarket.saveDataFile();
        }

        Instant lastUpdate = Instant.parse(Objects.requireNonNull(data.getString("LastUpdate")));
        Instant current = Instant.now();
        long seconds = Duration.between(lastUpdate, current).toMillis() / 1000;


        if (seconds >= config.getInt("ResetTimer") * 3600L) {
            MarketUtility.createItemsForCurrentSession();
            data.set("LastUpdate", current.toString());
            BlackMarket.saveDataFile();
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.getOpenInventory().getTitle().contains("Black") || player.getOpenInventory().getTitle().contains("Confirm")) {
                    player.closeInventory();
                }
            });
        }

        MarketUtility.playersWithMenuOpen.forEach(uuid -> {
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            if (player == null) {
                System.out.println(uuid + " null player");
            }else {
                player.getOpenInventory().getTopInventory().setItem(4, getClock());
            }
        });

    }

    private ItemStack getClock() {
        ItemStack clock = new ItemStack(Material.CLOCK);
        ItemMeta meta = clock.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ColorTranslator.translateColorCodes("&7Every reset will restock the shop  "));
        lore.add(ColorTranslator.translateColorCodes("&7with different items each time."));
        lore.add("");
        lore.add(ColorTranslator.translateColorCodes("&6Time until next reset: &a" + MarketUtility.getRemainingTime()));
        meta.setLore(lore); meta.setDisplayName(ColorTranslator.translateColorCodes("&6&lBlack Market"));
        clock.setItemMeta(meta);
        return clock;
    }
}
