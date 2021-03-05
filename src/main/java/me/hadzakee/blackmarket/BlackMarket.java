package me.hadzakee.blackmarket;

import me.hadzakee.blackmarket.commands.BlackMarketCommand;
import me.hadzakee.blackmarket.events.MarketClose;
import me.hadzakee.blackmarket.events.PlayerLeave;
import me.hadzakee.blackmarket.tasks.MenuUpdateTask;
import me.hadzakee.blackmarket.utils.MarketItem;
import me.hadzakee.blackmarket.utils.MarketUtility;
import me.hadzakee.blackmarket.utils.MenuUtility;
import me.hadzakee.blackmarket.menu.MenuManager;
import me.hadzakee.blackmarket.utils.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class BlackMarket extends JavaPlugin {

    private static File file;
    private static FileConfiguration data;
    private static Economy economy;
    private static BlackMarket plugin;

    @Override
    public void onEnable() {

        if (!loadDataFile()) {
            System.out.println("[" + getDescription().getName() + "] Unable to load 'data.yml'. Disabling the plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy() ) {
            System.out.println("[" + getDescription().getName() + "] Vault not found. Disabling the plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MenuManager.setup(getServer(), this, MenuUtility.class);

        initialise();

        System.out.println("[" + getDescription().getName() + "] Plugin loaded successfully.");

        MarketUtility.createItemsForCurrentSession();

        new MenuUpdateTask().runTaskTimer(this, 20, 20);
    }

    @Override
    public void onDisable() {

    }

    public void initialise() {

        plugin = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        MessageUtils.setPrefix(getConfig().getString("Prefix"));
        MessageUtils.setMessagecolor(getConfig().getString("Message-Color"));

        registerCommands();
        registerEvents();

        MarketUtility.updateItems();

    }

    public void registerCommands() {
        getCommand("blackmarket").setExecutor(new BlackMarketCommand(this));

    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new MarketClose(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
    }


    public static Economy getEconomy() {
        return economy;
    }

    public static FileConfiguration getData() {
        return data;
    }

    public static BlackMarket getPlugin() {
        return plugin;
    }



    public boolean loadDataFile() {
        try {
            file = new File(getDataFolder(), "data.yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            (data = new YamlConfiguration()).load(file);
            return true;
        } catch (IOException | InvalidConfigurationException e) {
            return false;
        }
    }

    public static void saveDataFile() {
        try {
            data.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
}
