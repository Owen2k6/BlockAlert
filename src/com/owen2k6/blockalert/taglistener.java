package com.owen2k6.blockalert;

import com.johnymuffin.discordcore.DiscordCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class taglistener implements Listener {

	public BlockAlert plugin;

	public BAConfig baConfig;
	public Logger log;
	public DiscordCore discordCore;

	public taglistener(BlockAlert plugin) {
		this.plugin = plugin;
		this.log = this.plugin.log;
		if (this.plugin.baConfig == null) throw new RuntimeException("BlockAlert config is null?!?!?!?");
		this.baConfig = this.plugin.baConfig;
		this.discordCore = new DiscordCore();
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (baConfig == null) throw new RuntimeException("baConfig == null for no fuckin reason");
		List<Integer> tagblock = baConfig.getTaggedBlocks();
		log.info("Block destroyed: " + event.getBlock().getType().getId());
		try {
			log.info(tagblock.toString());
		}catch(Exception e){
			log.severe("tagblock is null");
			log.severe("initialising default setting (diamond, iron, gold)");
			tagblock.clear();
			tagblock.add(57);
			tagblock.add(42);
			tagblock.add(41);
		}
		//attempt again
		try {
			log.info(tagblock.toString());
		}catch(Exception e){
			log.severe("tagblock is null again");
			log.severe("There was an issue with BlockAlert. Please contact the developer.");
			return;
		}
		if (tagblock.contains(event.getBlock().getTypeId())) {
			if (baConfig.getConfigBoolean("enable-discord-features")) {
				try {
					discordCore.getDiscordBot().discordSendToChannel(baConfig.getConfigString("discord-channel-id"), "BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getLocation().toString());
				} catch (RuntimeException exception) {
					log.info("An exception occurred when sending a message to Discord.");
					exception.printStackTrace();
				}
			}
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission("blockalert.alert")) {
					player.sendMessage(ChatColor.RED +"BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getX()+" "+event.getBlock().getY()+" "+event.getBlock().getZ() + " in world " + event.getBlock().getWorld().getName());
				}
			}
			plugin.log.info("BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getX()+" "+event.getBlock().getY()+" "+event.getBlock().getZ() + " in world " + event.getBlock().getWorld().getName());
		}
	}


}



