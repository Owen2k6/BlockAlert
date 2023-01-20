package com.owen2k6.blockalert;

import com.johnymuffin.discordcore.DiscordCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class BlockAlert extends JavaPlugin {
	public Logger log;

	public BAConfig baConfig;

	@Override
	public void onEnable() {
		this.log = this.getServer().getLogger();
		this.log.info("[BlockAlert] BlockAlert has been enabled!");
		baConfig = new BAConfig(new File(this.getDataFolder(), "config.yml"));
		getServer().getPluginManager().registerEvents(new taglistener(this), this);

		log.info("Discord features enabled: " + baConfig.getConfigBoolean("enable-discord-features"));
		log.info("Discord Channel ID: " + baConfig.getConfigString("discord-channel-id"));
	}

	@Override
	public void onDisable() {
		this.log.info("BlockAlert has been disabled!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("blockalert")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "BlockAlert " + ChatColor.GOLD + "by Owen2k6");
				sender.sendMessage(ChatColor.BLUE + "To get a list of available commands, run " + ChatColor.YELLOW + "/blockalert help");
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				baConfig.reload();
				sender.sendMessage("BlockAlert config reloaded!");
				return true;
			}
			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.AQUA + "BlockAlert Commands:");
				sender.sendMessage(ChatColor.RED + "/blockalert reload - Reloads the config");
				sender.sendMessage(ChatColor.RED + "/blockalert help - Shows this message");
				return true;
			}
		}
		return false;
	}
}
