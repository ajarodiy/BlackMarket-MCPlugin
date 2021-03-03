package me.hadzakee.blackmarket.tasks;

import me.hadzakee.blackmarket.BlackMarket;
import me.hadzakee.blackmarket.market.MainPage;
import me.hadzakee.blackmarket.menu.MenuManager;
import me.hadzakee.blackmarket.menu.MenuManagerException;
import me.hadzakee.blackmarket.menu.MenuManagerNotSetupException;
import me.hadzakee.blackmarket.utils.MarketUtility;
import me.hadzakee.blackmarket.utils.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
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

    }
}
