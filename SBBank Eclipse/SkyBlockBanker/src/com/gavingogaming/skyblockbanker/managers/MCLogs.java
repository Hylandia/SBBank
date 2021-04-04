package com.gavingogaming.skyblockbanker.managers;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;

public class MCLogs {
	public void log(Level l, String text, Logger logger) {
		logger.log(l, ChatColor.translateAlternateColorCodes('&', text));
	}
}
