package com.owen2k6.blockalert;

import com.johnymuffin.discordcore.DiscordCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class BlockAlert extends JavaPlugin {
	Logger log;
	BlockAlert plugin;

	BAConfig baConfig;

	public String tagblock;

	boolean discordCoreEnabled;
	DiscordCore discordCore;
	@Override
	public void onEnable() {
		this.log = this.getServer().getLogger();
		this.log.info("BlockAlert has been enabled!");
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("DiscordCore")) {
			discordCore = (DiscordCore) Bukkit.getServer().getPluginManager().getPlugin("DiscordCore");
			discordCoreEnabled = true;
			this.log.info("DiscordCore Support Enabled");
		}
		getServer().getPluginManager().registerEvents(new taglistener(), this);
		baConfig = new BAConfig(new File(this.getDataFolder(), "config.yml"));


	}

	@Override
	public void onDisable() {
		this.log.info("BlockAlert has been disabled!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("blockalert")) {
			if (args.length == 0) {
				sender.sendMessage("BlockAlert Commands:");
				sender.sendMessage("/blockalert reload - Reloads the config");
				sender.sendMessage("/blockalert help - Shows this message");
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				baConfig.reload();
				sender.sendMessage("BlockAlert config reloaded!");
				return true;
			}
			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage("BlockAlert Commands:");
				sender.sendMessage("/blockalert reload - Reloads the config");
				sender.sendMessage("/blockalert help - Shows this message");
				return true;
			}
		}
		return false;
	}
}
