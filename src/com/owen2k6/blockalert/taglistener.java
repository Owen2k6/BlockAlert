package com.owen2k6.blockalert;

import com.johnymuffin.discordcore.DiscordCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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
		log.info("BlockBreakEvent triggered");
		log.info(event.getBlock().getType().toString());
		log.info(baConfig.getTaggedBlocks().toString());
		if (baConfig.getTaggedBlocks().contains(event.getBlock().getType().toString())) {
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
			plugin.log.info("BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getLocation().toString());
		}
	}


}



