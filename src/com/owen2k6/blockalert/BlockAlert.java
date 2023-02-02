package com.owen2k6.blockalert;

import com.johnymuffin.discordcore.DiscordCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public class BlockAlert extends JavaPlugin
{
	private static BlockAlert instance;
	public static BlockAlert getInstance()
	{
		return instance;
	}
	public Logger log;

	public BAConfig baConfig;
	public ArrayList<Player> doNotAlert = new ArrayList<>();

	@Override
	public void onEnable() {
		instance = this;
		this.log = this.getServer().getLogger();
		this.log.info("[BlockAlert] BlockAlert has been enabled!");
		baConfig = new BAConfig(new File(this.getDataFolder(), "config.yml"));
		getServer().getPluginManager().registerEvents(new taglistener(), this);
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
			if (args[0].equalsIgnoreCase("ignore"))
			{
				if (!baConfig.getBoolean("allow-ignore-alerts", false))
				{
					sender.sendMessage(ChatColor.RED + "You can't ignore alerts!");
					return true;
				}
				if (baConfig.getBoolean("temp-ignore-only", false))
				{
					if (doNotAlert.contains((Player) sender))
					{
						doNotAlert.remove((Player) sender);
						sender.sendMessage(ChatColor.GREEN + "You will now receive alerts!");
					}
					else
					{
						doNotAlert.add((Player) sender);
						sender.sendMessage(ChatColor.RED + "You will no longer receive alerts!");
					}
					return true;
				} else {
					if (isIgnoring((Player) sender)) {
						deleteIgnoreFile((Player) sender);
						sender.sendMessage(ChatColor.GREEN + "You will now receive alerts!");
					} else {
						createIgnoreFile((Player) sender);
						sender.sendMessage(ChatColor.RED + "You will no longer receive alerts!");
					}
				}
			}
		}
		return false;
	}

	public boolean isIgnoring(Player player)
	{
		if (!baConfig.getBoolean("allow-ignore-alerts", false))
			return false;
		if (baConfig.getBoolean("temp-ignore-alerts", false) && doNotAlert.contains(player))
			return true;
		File ignoreFolder = new File(this.getDataFolder(), "ignore");
		if (!ignoreFolder.exists())
			ignoreFolder.mkdir();
		File ignoreFile = new File(ignoreFolder, player.getName() + ".dat");
		if (!ignoreFile.exists())
			return false;
		return true;
	}

	private void createIgnoreFile(Player player)
	{
		try
		{
			if (!baConfig.getBoolean("allow-ignore-alerts", false))
				return;
			File ignoreFolder = new File(this.getDataFolder(), "ignore");
			if (!ignoreFolder.exists())
				ignoreFolder.mkdir();
			File ignoreFile = new File(ignoreFolder, player.getName() + ".dat");
			if (!ignoreFile.exists())
				ignoreFile.createNewFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void deleteIgnoreFile(Player player)
	{
		try
		{
			File ignoreFolder = new File(this.getDataFolder(), "ignore");
			if (!ignoreFolder.exists())
				ignoreFolder.mkdir();
			File ignoreFile = new File(ignoreFolder, player.getName() + ".dat");
			if (ignoreFile.exists())
				ignoreFile.delete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
