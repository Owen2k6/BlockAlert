package com.owen2k6.blockalert;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ListIterator;
import java.util.Objects;

public class taglistener implements Listener {

	private BlockAlert plugin;


	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (plugin.baConfig.getTaggedBlocks().contains(event.getBlock().getType().toString())) {
			if (plugin.baConfig.getConfigBoolean("is-discord-enabled")) {
				try {
					plugin.discordCore.getDiscordBot().discordSendToChannel(plugin.baConfig.getConfigString("discord-channel-id"), "BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getLocation().toString());
				} catch (RuntimeException exception) {
					plugin.log.info("An exception occurred when sending a message to Discord.");
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



