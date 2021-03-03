package me.hadzakee.blackmarket.utils;

import me.hadzakee.blackmarket.BlackMarket;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.manager.TypeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class MarketUtility {
    public static List<MarketItem> items = new ArrayList<>();
    public static List<MarketItem> itemsForCurrentSession = new ArrayList<>();
    public static HashMap<MarketItem, ItemStack> map = new HashMap<>();

    public static List<MarketItem> getItemsForCurrentSession() {
        return itemsForCurrentSession;
    }

    public static void setItemsForCurrentSession(List<MarketItem> itemsForCurrentSession) {
        MarketUtility.itemsForCurrentSession = itemsForCurrentSession;
    }

    public static HashMap<MarketItem, ItemStack> getMap() {
        return map;
    }

    public static void setMap(HashMap<MarketItem, ItemStack> map) {
        MarketUtility.map = map;
    }

    public static void updateItems() {
        items.clear();

        FileConfiguration config = BlackMarket.getPlugin().getConfig();

        TypeManager types = MMOItems.plugin.getTypes();
        Set<String> keys = Objects.requireNonNull(config.getConfigurationSection("Items")).getKeys(false);
        keys.forEach(key -> {
            String type = config.getString("Items." + key + ".Type");
            String id = config.getString("Items." + key + ".Id");
            if (!types.has(type)) {
                System.out.println(MessageUtils.message(type + " type not found. Please fix this is the 'config.yml'"));
                return;
            }
            ItemStack item = MMOItems.plugin.getMMOItem(types.get(type), id).newBuilder().build();
            int minPrice = config.getInt("Items." + key + ".MinimumPrice");
            int maxPrice = config.getInt("Items." + key + ".MaximumPrice");
            items.add(new MarketItem(item, minPrice, maxPrice));
        });
    }

    public static void createItemsForCurrentSession() {
        itemsForCurrentSession.clear();
        map.clear();

        FileConfiguration data = BlackMarket.getData();
        FileConfiguration config = BlackMarket.getPlugin().getConfig();
        data.set("LastUpdate", Instant.now().toString());

        Collections.shuffle(items);
        for (int i=0; i < Math.min(items.size(), config.getInt("ShopSize") * 7); ++i) {
            MarketItem item = items.get(i);
            int price = new Random().nextInt((item.getMaxPrice() - item.getMinPrice())/100) * 100 + item.getMinPrice();
            item.setCurrentPrice(price);
            item.setIndex(i);

            ItemStack itemDisplay = item.getItem().clone();

            ItemMeta meta = itemDisplay.getItemMeta();
            List<String> lore = meta.getLore();
            assert lore != null;
            lore.clear();
            lore.add("");
            lore.add(ColorTranslator.translateColorCodes("&6Today's Offer: &e" + parsePrice(item.getCurrentPrice())));
            lore.add("");
            lore.add(ColorTranslator.translateColorCodes("&6Minimum Price: &e" + parsePrice(item.getMinPrice())));
            lore.add(ColorTranslator.translateColorCodes("&6Maximum Price: &e" + parsePrice(item.getMaxPrice())));
            lore.add("");
            lore.add(ColorTranslator.translateColorCodes("&8&lLEFT-CLICK TO VIEW"));
            meta.setLore(lore);
            itemDisplay.setItemMeta(meta);

            itemsForCurrentSession.add(item);
            map.put(item, itemDisplay);
        }

        for (int i=0; i < config.getInt("ShopSize") * 7; ++i) {
            String key = "Item" + i;
            data.set(key, "Not Bought");
        }

        BlackMarket.saveDataFile();

    }

    public static void updateItemsForCurrentSession() {
        for (int i=0; i < itemsForCurrentSession.size(); ++i) {
            String who = whoBought(i);
            if (!who.equalsIgnoreCase("Not Bought")) {
                ItemStack sold = new ItemStack(Material.BARRIER);
                ItemMeta meta = sold.getItemMeta();
                List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
                lore.add("");
                lore.add(ColorTranslator.translateColorCodes("&6Item: &e" + ChatColor.stripColor(itemsForCurrentSession.get(i).getItem().getItemMeta().getDisplayName())));
                lore.add(ColorTranslator.translateColorCodes("&6Bought By: &e" + who));
                lore.add(ColorTranslator.translateColorCodes("&6Price: &e" + parsePrice(itemsForCurrentSession.get(i).getCurrentPrice())));
                lore.add("");
                lore.add(ColorTranslator.translateColorCodes("&7Until next time!"));
                meta.setLore(lore);
                Objects.requireNonNull(meta).setDisplayName(ColorTranslator.translateColorCodes("&cThis item is sold out!"));
                sold.setItemMeta(meta);

                map.put(itemsForCurrentSession.get(i), sold);
            }
        }
    }

    public static String whoBought(int index) {
        FileConfiguration data = BlackMarket.getData();
        String key = "Item" + index;
        if (!data.isString(key)) {
            data.set(key, "Not Bought");
        }

        BlackMarket.saveDataFile();

        return data.getString(key);
    }

    public static MarketItem matchItem(ItemStack item) {
        for (MarketItem item1 : getItemsForCurrentSession()) {
            if (map.get(item1) !=null && map.get(item1).equals(item)) return item1;
        }
        return null;
    }

    public static void changeBoughtStatus(int index, String name) {
        FileConfiguration data = BlackMarket.getData();
        String key = "Item" + index;
        data.set(key, name);
        BlackMarket.saveDataFile();
    }

    public static String parsePrice(int n) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(true);
        return formatter.format(n);
    }

    public static ItemStack getRealItem(MarketItem item) {
        return map.get(item);
    }

    public static String getRemainingTime() {
        FileConfiguration data = BlackMarket.getData();
        FileConfiguration config = BlackMarket.getPlugin().getConfig();

        if (!data.isString("LastUpdate")) {
            MarketUtility.createItemsForCurrentSession();
            Instant time =  Instant.now();
            data.set("LastUpdate", time.toString());
            BlackMarket.saveDataFile();
        }

        Instant lastUpdate = Instant.parse(Objects.requireNonNull(data.getString("LastUpdate")));
        Instant current = Instant.now();
        long seconds = Duration.between(lastUpdate, current).toMillis() / 1000;
        seconds = config.getInt("ResetTimer") * 3600L - seconds;
        long hours = seconds/3600;
        seconds %= 3600;
        long minutes = seconds/60;
        seconds %= 60;
        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}
