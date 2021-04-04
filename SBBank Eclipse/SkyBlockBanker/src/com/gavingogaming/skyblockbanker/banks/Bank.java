package com.gavingogaming.skyblockbanker.banks;

import org.bukkit.entity.Player;

import com.gavingogaming.skyblockbanker.SBBanker;
import com.gavingogaming.skyblockbanker.managers.MoneyManager;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class Bank {
	public Player owner;
	public int value;
	
	public Bank(Player owner, int value) {
		this.value = value;
		this.owner = owner;
	}
	
	public void deposit(Economy eco, int amount) {
		MoneyManager mm = new MoneyManager(new SBBanker());
		if(eco.getBalance(owner) < amount) {
			owner.sendMessage(ChatColor.RED + "You do not have that much money in your purse!");
			return;
		}
		mm.removeAmount(owner, amount, SBBanker.getPlugin(SBBanker.class).getConfig().getString("colors.main") + "Deposited " + amount + " into your bank.");
		value += amount;
	}
	public void withdraw(Economy eco, int amount) {
		MoneyManager mm = new MoneyManager(new SBBanker());
		if(eco.getBalance(owner) > amount) {
			owner.sendMessage(ChatColor.RED + "You do not have that much money in the bank!");
			return;
		}
		mm.addAmount(owner, amount, SBBanker.getPlugin(SBBanker.class).getConfig().getString("colors.main") + "Withdrew " + amount + " from your bank.");
		value -= amount;
	}
}
