package me.hadzakee.blackmarket.commands;

import me.hadzakee.blackmarket.market.MainPage;
import me.hadzakee.blackmarket.menu.MenuManager;
import me.hadzakee.blackmarket.menu.MenuManagerException;
import me.hadzakee.blackmarket.menu.MenuManagerNotSetupException;
import me.hadzakee.blackmarket.utils.MarketUtility;
import me.hadzakee.blackmarket.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BlackMarketCommand implements CommandExecutor {

    me.hadzakee.blackmarket.BlackMarket plugin;

    public BlackMarketCommand(me.hadzakee.blackmarket.BlackMarket plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageUtils.message("Only players can open the Black Market."));
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("BlackMarketCommand")))) {
                player.sendMessage(MessageUtils.message("Permission missing."));
                return true;
            }


            // Open market
            try {
                new MainPage(MenuManager.getPlayerMenuUtility(player)).open();
            } catch (MenuManagerException | MenuManagerNotSetupException e) {
                e.printStackTrace();
            }

            return true;

        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("BlackMarketReloadConfig")))) {
                plugin.reloadConfig();
                sender.sendMessage(MessageUtils.message("Config reloaded successfully."));
                MarketUtility.updateItems();
            }else{
                sender.sendMessage(MessageUtils.message("Missing permission."));
            }
        }else if (args[0].equalsIgnoreCase("session")) {
            if (sender.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("BlackMarketGenerateSession")))) {
                sender.sendMessage(MessageUtils.message("New session generated successfully."));
                MarketUtility.createItemsForCurrentSession();
            }else{
                sender.sendMessage(MessageUtils.message("Missing permission."));
            }
        }else{
            sender.sendMessage(MessageUtils.message("Invalid command."));
        }

        return true;
    }
}
