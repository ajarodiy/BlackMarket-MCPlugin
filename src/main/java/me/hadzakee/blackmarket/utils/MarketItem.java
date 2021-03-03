package me.hadzakee.blackmarket.utils;

import org.bukkit.inventory.ItemStack;

public class MarketItem {
    public ItemStack item;
    public int minPrice;
    public int maxPrice;
    public int currentPrice;
    public int index;

    public MarketItem(ItemStack item, int minPrice, int maxPrice) {
        this.item = item;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.currentPrice = 0;
        this.index = -1;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
