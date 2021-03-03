package me.hadzakee.blackmarket.menu;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class MenuManager {
    private static HashMap<Player, AbstractPlayerMenuUtility> playerMenuUtilityMap = new HashMap();
    private static Class<? extends AbstractPlayerMenuUtility> pmuClass;
    private static boolean isSetup = false;

    public MenuManager() {
    }

    private static void registerMenuListener(Server server, Plugin plugin) {
        boolean isAlreadyRegistered = false;
        RegisteredListener[] var3 = InventoryClickEvent.getHandlerList().getRegisteredListeners();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            RegisteredListener rl = var3[var5];
//            System.out.println(rl.getListener().getClass().getSimpleName());
            if (rl.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }

//        System.out.println("erwiwjriwer: " + isAlreadyRegistered);
        if (!isAlreadyRegistered) {
            server.getPluginManager().registerEvents(new MenuListener(), plugin);
        }

    }

    private static void registerPlayerMenuUtility(Class<? extends AbstractPlayerMenuUtility> playerMenuUtilityClass) {
        pmuClass = playerMenuUtilityClass;
    }

    public static void setup(Server server, Plugin plugin, Class<? extends AbstractPlayerMenuUtility> playerMenuUtilityClass) {
//        System.out.println("MENU MANAGER HAS BEEN SETUP");
        registerMenuListener(server, plugin);
        registerPlayerMenuUtility(playerMenuUtilityClass);
        isSetup = true;
    }

    public static void openMenu(Class<? extends Menu> menuClass, Player player) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            ((Menu)menuClass.getConstructor(AbstractPlayerMenuUtility.class).newInstance(getPlayerMenuUtility(player))).open();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var3) {
            throw new MenuManagerException();
        }
    }

    public static void openMenu(Class<? extends Menu> menuClass,AbstractPlayerMenuUtility abstractPlayerMenuUtility) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            ((Menu)menuClass.getConstructor(AbstractPlayerMenuUtility.class).newInstance(abstractPlayerMenuUtility)).open();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var3) {
            throw new MenuManagerException();
        }
    }

    public static AbstractPlayerMenuUtility getPlayerMenuUtility(Player p) throws MenuManagerException, MenuManagerNotSetupException {
        if (!isSetup) {
            throw new MenuManagerNotSetupException();
        } else if (!playerMenuUtilityMap.containsKey(p)) {
            Constructor constructor = null;

            try {
                constructor = pmuClass.getConstructor(Player.class);
                AbstractPlayerMenuUtility playerMenuUtility = (AbstractPlayerMenuUtility)constructor.newInstance(p);
                playerMenuUtilityMap.put(p, playerMenuUtility);
                return playerMenuUtility;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var4) {
                throw new MenuManagerException();
            }
        } else {
            return (AbstractPlayerMenuUtility)playerMenuUtilityMap.get(p);
        }
    }

    public static <T> T getPlayerMenuUtility(Player p, Class<T> t) throws MenuManagerException {
        if (!playerMenuUtilityMap.containsKey(p)) {
            AbstractPlayerMenuUtility playerMenuUtility;
            try {
                Constructor<? extends AbstractPlayerMenuUtility> constructor = pmuClass.getConstructor(Player.class);
                playerMenuUtility = (AbstractPlayerMenuUtility)constructor.newInstance(p);
                playerMenuUtilityMap.put(p, playerMenuUtility);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException var4) {
                throw new MenuManagerException();
            }

            return t.cast(playerMenuUtility);
        } else {
            return t.cast(playerMenuUtilityMap.get(p));
        }
    }
}