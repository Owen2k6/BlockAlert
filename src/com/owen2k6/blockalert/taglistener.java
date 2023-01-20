package com.owen2k6.blockalert;

import com.johnymuffin.discordcore.DiscordCore;
import jdk.nashorn.internal.ir.Block;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import com.owen2k6.blockalert.BAConfig;

import java.io.File;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.logging.Logger;

public class taglistener implements Listener {

	public BlockAlert plugin;

	public BAConfig baConfig;
	public Logger log;
	public DiscordCore discordCore;

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		this.log = Bukkit.getServer().getLogger();
		this.plugin = new BlockAlert();
		baConfig = new BAConfig(new File(plugin.getDataFolder(), "config.yml"));
		List<String> tagblock = baConfig.getTaggedBlocks();
		log.info("BlockBreakEvent triggered");
		log.info(String.valueOf(event.getBlock().getType().getId()));
		try {
			log.info(tagblock.toString());
		}catch(Exception e){
			log.severe("tagblock is null");
			log.severe("initialising default setting (diamond, iron, gold)");
			tagblock.clear();
			tagblock.add("57");
			tagblock.add("42");
			tagblock.add("41");
		}
		//attempt again
		try {
			log.info(tagblock.toString());
		}catch(Exception e){
			log.severe("tagblock is null again");
			log.severe("There was an issue with BlockAlert. Please contact the developer.");
			return;
		}
		if (tagblock.contains(String.valueOf(event.getBlock().getTypeId()))) {
			if (baConfig.getConfigBoolean("is-discord-enabled")) {
				try {
					discordCore.getDiscordBot().discordSendToChannel(baConfig.getConfigString("discord-channel-id"), "BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getLocation().toString());
				} catch (RuntimeException exception) {
					log.info("An exception occurred when sending a message to Discord.");
					exception.printStackTrace();
				}
			}
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission("blockalert.alert")) {
					player.sendMessage("BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getLocation().toString());
				}
			}
			plugin.log.info("BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getX()+" "+event.getBlock().getY()+" "+event.getBlock().getZ() + " in world " + event.getBlock().getWorld().getName());
		}
	}


}



