package com.owen2k6.blockalert;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

public class BlockAlert extends JavaPlugin {
	public Logger log;

	public BAConfig baConfig;


	ArrayList<String> ignored = new ArrayList<String>();

	@Override
	public void onEnable() {
		this.log = this.getServer().getLogger();
		this.log.info("[BlockAlert] BlockAlert has been enabled!");
		baConfig = new BAConfig(new File(this.getDataFolder(), "config.yml"));
		getServer().getPluginManager().registerEvents(new taglistener(this), this);
	}

	@Override
	public void onDisable() {
		this.log.info("[BlockAlert] BlockAlert has been disabled!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("blockalert")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "BlockAlert, by "+ChatColor.GOLD+ "Owen2k6");
				sender.sendMessage(ChatColor.BLUE + "To get a list of available commands, run " + ChatColor.YELLOW + "/blockalert help");
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if(sender.hasPermission("blockalert.reload")){
					baConfig.reload();
					sender.sendMessage(ChatColor.GREEN + "BlockAlert config reloaded!");
					return true;
				}
				else{
					sender.sendMessage(ChatColor.RED + "You do not have permission to do that!");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.AQUA + "BlockAlert Commands:");
				sender.sendMessage(ChatColor.RED + "/blockalert reload - Reloads the config");
				sender.sendMessage(ChatColor.RED + "/blockalert help - Shows this message");
				sender.sendMessage(ChatColor.RED + "/blockalert info - Gives you info on BlockAlert");
				sender.sendMessage(ChatColor.RED + "/blockalert ignore - ignore BlockAlerts");
				sender.sendMessage(ChatColor.RED + "/blockalert receive - reveive BlockAlerts");
				return true;
			}
			if (args[0].equalsIgnoreCase("info")) {
				sender.sendMessage(ChatColor.AQUA + "BlockAlert");
				sender.sendMessage(ChatColor.YELLOW + "Version 1.0.0");
				sender.sendMessage(ChatColor.YELLOW + "Beta Test Edition");
				return true;
			}
			if (args[0].equalsIgnoreCase("ignore")) {
				if(ignored.contains(sender.getName()))
				{
					sender.sendMessage(ChatColor.RED + "You are already ignoring BlockAlert!");
					return true;
				}
				sender.sendMessage(ChatColor.RED + "You will not receive alerts until the server restarts.");
				sender.sendMessage(ChatColor.RED + "You can re-enable alerts by running /blockalert receive");
				return true;
			}
			if (args[0].equalsIgnoreCase("receive")) {
				if(!ignored.contains(sender.getName()))
				{
					sender.sendMessage(ChatColor.RED + "You are already receiving alerts!");
					return true;
				}
				sender.sendMessage(ChatColor.GREEN + "You will now receive alerts on tagged blocks.");
				ignored.remove(sender.getName());
				return true;
			}
		}
		return false;
	}
}
