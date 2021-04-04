package com.gavingogaming.skyblockbanker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gavingogaming.skyblockbanker.banks.Bank;
import com.gavingogaming.skyblockbanker.managers.EasyManager;
import com.gavingogaming.skyblockbanker.managers.MCLogs;
import com.gavingogaming.skyblockbanker.managers.MoneyManager;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class SBBanker extends JavaPlugin implements Listener {
	
	MCLogs logs = new MCLogs();
	public Economy eco;
	public Inventory gui;
	public MoneyManager mm = new MoneyManager(this);
	public Map<String, Long> cooldowns = new HashMap<String, Long>();
	public Map<Player, Bank> banks = new HashMap<Player, Bank>();
	
	public boolean serverHasEco() {
		RegisteredServiceProvider<Economy> eco2 = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if(eco2 != null) {
			eco = eco2.getProvider();
		}
		return (eco != null);
	}
	
	@EventHandler()
	public void onClickGUI(InventoryClickEvent e) {
		if(e.getClickedInventory() == gui) {
			if(e.getCurrentItem() == null) return;
			if(e.getCurrentItem().getItemMeta() == null) return;
			if(!e.getCurrentItem().getItemMeta().hasLore()) return;
			if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Your Coins")) {
			
				e.setCancelled(true);
				Player player = (Player) e.getWhoClicked();
				player.performCommand("bal");
				player.performCommand("bank");
				return;
			}
			if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Collect Coins")) {
				Player player = (Player) e.getWhoClicked();
				e.setCancelled(true);
				String abilityName = "claimcoins";
				Long time = (long) 72000;
			    if(cooldowns.containsKey(player.getName() + "-" + abilityName)) {
			    	if(cooldowns.get(player.getName() + "-" + abilityName) > System.currentTimeMillis()) {
			    		long timeleft = (cooldowns.get(player.getName() + "-" + abilityName) - System.currentTimeMillis()) / 1000;
						player.sendMessage(ChatColor.RED +"You can claim this gift in " + timeleft +" more secconds.");
						return;
					}
				}
																				
				cooldowns.put(player.getName() + "-" + abilityName, System.currentTimeMillis() + (time * 1000));
				mm.addAmount(player, mm.newAmount(1000), getConfig().getString("colors.main") + "Collected coins!");
			}
		}
	}
	
	@Override
	public void onEnable() {
		
		if(!serverHasEco()) {
			logs.log(Level.SEVERE, "&cERROR: You do not have VAULT or ESSENTIALS installed. Please install this and reload the server. Shutting down SBBank now.", getLogger());
			getServer().getPluginManager().disablePlugin(this);
		}
		getServer().getPluginManager().registerEvents(this, this);
		createGUI();
		logs.log(Level.INFO, "&eThank you for using SBBanker by GavinGoGaming. This plugin was made for the server Hylandia from a request that the owner, ItsGunner_ wanted.", getLogger());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = (Player) e.getPlayer();
		if(!player.hasPlayedBefore()) {
			banks.put(player, new Bank(player, 0));
			player.sendMessage(getConfig().getString("colors.main") + "Hey! Thanks for choosing Hylandia Skyblock! Do /is to get started, and do /spawn to get back to spawn.");
			return;
		}
		player.sendMessage(getConfig().getString("colors.main") + "Welcome to Hylandia Skyblock!");
		return;
	}
	
	public void createGUI() {
		gui = Bukkit.createInventory(null, 27, "Hylandia Skyblock (Banker)");
		
		// ur coins
				ItemStack getMoney = new ItemStack(Material.PAPER);
				ItemMeta meta = (ItemMeta) getMoney.getItemMeta();
				meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Coins");
				List<String> getMoneylore = new ArrayList<String>();
				getMoneylore.add(ChatColor.translateAlternateColorCodes('&', getConfig().getString("colors.main") +"Click to see your balence!"));
				meta.setLore(getMoneylore);
				getMoney.setItemMeta(meta);
				gui.setItem(14, getMoney);
		// Collect Daily Coins
				ItemStack collect = new ItemStack(Material.PAPER);
				ItemMeta cmeta = (ItemMeta) getMoney.getItemMeta();
				cmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Get Coins" );
				List<String> collectlore = new ArrayList<String>();
				collectlore.add(ChatColor.translateAlternateColorCodes('&', getConfig().getString("colors.main") +"Adds a random amount of money (1-1000) to your bal! Can be used once per day."));
				cmeta.setLore(collectlore);
				collect.setItemMeta(cmeta);
				gui.setItem(12, collect);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//		if(label.equalsIgnoreCase("sbbank")) {
//			if(!(sender instanceof Player)) {
//				sender.sendMessage("You can only use this command in-game!");
//				return true;
//			}
//			
//			Player p =(Player) sender;
//			// p.openInventory(gui);
//			p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("colors.main") + "Opening the bank!"));
//			return true;
//		}
		if(label.equalsIgnoreCase("bank")) {
			if(!(sender instanceof Player)) return true;
			Player player = (Player) sender;
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("colors.main") + "Your banks bal: " + banks.get(player).value));
		}
		if(label.equalsIgnoreCase("deposit")) {
			if(!(sender instanceof Player)) return true;
			if(args.length == 0) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.RED + "Usage: /deposit <amount>");
				return true;
			}
			if(!EasyManager.isInt(args[0])) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.RED + "Usage: /deposit <amount>");
				return true;
			}
			Player p = (Player) sender;
			banks.get(p).value += Integer.parseInt(args[0]);
			mm.removeAmount(p, Integer.parseInt(args[0]), "");
			((Player) sender).sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("colors.main") + "Deposited coins!"));
		}
		if(label.equalsIgnoreCase("balance")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
					getConfig().getString("colors.main") + Double.toString(eco.getBalance(((Player) sender)))));
			return true;
		}
		if(label.equalsIgnoreCase("start")) {
			if(!(sender instanceof Player)) return true;
			banks.put(((Player) sender), new Bank(((Player) sender), 0));
			((Player) sender).sendMessage("Started!");
		}
		if(label.equalsIgnoreCase("withdraw")) {
			if(!(sender instanceof Player)) return true;
			if(args.length == 0) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.RED + "Usage: /withdraw <amount>");
				return true;
			}
			Player player = (Player) sender;
			if(!EasyManager.isInt(args[0])) {
				
				player.sendMessage(ChatColor.RED + "Usage: /withdraw <amount>");
				return true;
			}
			banks.get(player).value -= Integer.parseInt(args[0]);
			mm.addAmount(player, Integer.parseInt(args[0]), "");
			((Player) sender).sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("colors.main") + "Withdrew coins!"));
		}
		
		return false;
	}
}
