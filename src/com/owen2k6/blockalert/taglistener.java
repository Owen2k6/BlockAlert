package com.owen2k6.blockalert;

import com.johnymuffin.discordcore.DiscordCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
		if (tagblock.contains(event.getBlock().getTypeId()) && !event.getPlayer().hasPermission("blockalert.exempt")) {
			if (baConfig.getConfigBoolean("enable-discord-features")) {
				try {
					discordCore.getDiscordBot().discordSendToChannel(baConfig.getConfigString("discord-channel-id"), "BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getX()+" "+event.getBlock().getY()+" "+event.getBlock().getZ() + " in world " + event.getBlock().getWorld().getName());
				} catch (RuntimeException exception) {
					log.info("An exception occurred when sending a message to Discord.");
					exception.printStackTrace();
				}
			}
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission("blockalert.alert") && !plugin.ignored.contains(player.getName())) {
					player.sendMessage(ChatColor.RED +"BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getX()+" "+event.getBlock().getY()+" "+event.getBlock().getZ() + " in world " + event.getBlock().getWorld().getName());
				}
			}
		}
	}


}



