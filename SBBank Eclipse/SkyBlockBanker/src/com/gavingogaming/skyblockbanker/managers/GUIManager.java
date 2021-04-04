package com.gavingogaming.skyblockbanker.managers;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIManager {
	public void addItem(Inventory gui, ItemStack item, int slot) {
		gui.setItem(slot, item);
	}
}
