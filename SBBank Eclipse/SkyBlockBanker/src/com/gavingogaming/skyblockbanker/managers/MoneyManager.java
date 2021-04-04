package com.gavingogaming.skyblockbanker.managers;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.gavingogaming.skyblockbanker.SBBanker;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class MoneyManager implements Listener{
	
	private Random r = new Random();
	private SBBanker plugin;
	public MoneyManager(SBBanker plugin) {
		this.plugin = plugin;
	}
	
	public int newAmount(int limit) {
		return r.nextInt(limit);
	}
	
	public void addAmount(Player player, int amount, String message) {
		Economy eco = plugin.eco;
		eco.depositPlayer(player, amount);
		//ADD
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public void removeAmount(Player player, int amount, String message) {
		Economy eco = plugin.eco;
		eco.withdrawPlayer(player, amount);
		//REMOVE 
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
}
