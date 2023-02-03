package com.owen2k6.blockalert;

import com.johnymuffin.discordcore.DiscordCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.logging.Logger;

public class taglistener implements Listener {

	public BlockAlert plugin = BlockAlert.getInstance();

	public BAConfig baConfig = plugin.baConfig;
	public Logger log = Bukkit.getServer().getLogger();
	public DiscordCore discordCore = (DiscordCore) Bukkit.getPluginManager().getPlugin("DiscordCore");
	public boolean isDiscordEnabled = baConfig.getConfigBoolean("is-discord-enabled");
	public String discordChannelID = baConfig.getConfigString("discord-channel-id");

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		List<String> tagblock = baConfig.getTaggedBlocks();
		List<Integer> taggedBlockIDs = baConfig.getTaggedIDs();
		if (tagblock == null) return;
		if (event.getPlayer().hasPermission("blockalert.exempt")) return;
		Block block = event.getBlock();
		Location blockLoc = block.getLocation();
		String blockLocSerial = String.format("%s:%s:%s", blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ());
		if (tagblock.contains(blockLocSerial) || taggedBlockIDs.contains(block.getTypeId())) {
			if (isDiscordEnabled) {
				try {
					sendDiscordMsg("BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " @ " + getFriendlyLocation(event.getBlock().getLocation()));
				} catch (RuntimeException exception) {
					log.info("An exception occurred when sending a message to Discord.");
					exception.printStackTrace();
				}
			}
			for (Player player : Bukkit.getOnlinePlayers())
				if (player.hasPermission("blockalert.alert"))
					player.sendMessage(ChatColor.RED + "BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getX() + " " + event.getBlock().getY() + " " + event.getBlock().getZ() + " in world " + event.getBlock().getWorld().getName());
			plugin.log.info("BlockAlert: " + event.getPlayer().getName() + " has broken a " + event.getBlock().getType().toString() + " at " + event.getBlock().getX() + " " + event.getBlock().getY() + " " + event.getBlock().getZ() + " in world " + event.getBlock().getWorld().getName());
		}
	}

	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent event) {
		List<String> tags = baConfig.getTaggedBlocks();
		boolean pushingTaggedBlocks = false;

		for (Block block : event.getBlocks()) {
			if (pushingTaggedBlocks) break;
			if (tags.contains(getTagLocation(block.getLocation()))) {
				pushingTaggedBlocks = true;
				event.setCancelled(true);
				break;
			}
		}

		if (pushingTaggedBlocks) {
			if (isDiscordEnabled)
				sendDiscordMsg("BlockAlert: Piston @ " + getFriendlyLocation(event.getBlock().getLocation()) + " has been prevented from pushing tagged blocks.");
			for (Player player : Bukkit.getOnlinePlayers())
				if (player.hasPermission("blockalert.alert"))
					player.sendMessage(ChatColor.RED + "BlockAlert: Piston @ " + getFriendlyLocation(event.getBlock().getLocation()) + " has been prevented from pushing tagged blocks.");
			plugin.log.info("BlockAlert: Piston @ " + getFriendlyLocation(event.getBlock().getLocation()) + " has been prevented from pushing tagged blocks.");
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getType() == Material.ARROW && event.getPlayer().hasPermission("blockalert.tag")) {
			Block block = event.getClickedBlock();
			if (block == null) return;
			Location blockLoc = block.getLocation();
			String blockLocSerial = getTagLocation(blockLoc);
			List<String> tagblock = baConfig.getTaggedBlocks();
			if (tagblock == null) return;
			if (tagblock.contains(blockLocSerial)) tagblock.remove(blockLocSerial);
			else tagblock.add(blockLocSerial);

			baConfig.setProperty("tagged-blocks", tagblock);
			baConfig.save();
			//This is unessasary since this is not a required thing for staff to know. Moving the log to the console seems more practical.
			//if (isDiscordEnabled) sendDiscordMsg("BlockAlert: " + event.getPlayer().getName() + " has " + (tagblock.contains(blockLocSerial) ? "tagged" : "untagged") + " a " + block.getType().toString() + " @ " + getFriendlyLocation(blockLoc));
			event.getPlayer().sendMessage(ChatColor.GREEN + "BlockAlert: " + (tagblock.contains(blockLocSerial) ? "Tagged" : "Untagged") + " block at " + getFriendlyLocation(blockLoc));
			plugin.log.info("BlockAlert: " + event.getPlayer().getName() + " has " + (tagblock.contains(blockLocSerial) ? "tagged" : "untagged") + " a " + block.getType().toString() + " @ " + getFriendlyLocation(blockLoc));
		}
	}

	private void sendDiscordMsg(String msg) {
		discordCore.getDiscordBot().discordSendToChannel(discordChannelID, msg);
	}

	private String getFriendlyLocation(Location loc) {
		return String.format("%s %s %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	private String getTagLocation(Location loc) {
		return String.format("%s:%s:%s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
}



