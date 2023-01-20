package com.owen2k6.blockalert;

import org.bukkit.Bukkit;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class BAConfig extends Configuration {

	public BlockAlert plugin;
	public static BlockAlert singleton;


	public BAConfig(File settingsFile) {
		super(settingsFile);
		this.reload();
	}

	private void write() {
		//Main
		generateConfigOption("config-version", 1);

		//Setting
		generateConfigOption("Discord-Info", "Enable the discord notification features so you can be alerted from your staff channel!");
		generateConfigOption("Discord-Requirements", "Requires discordcore and the channel must be where the bot can talk!");
		generateConfigOption("is-discord-enabled", false);
		generateConfigOption("discord-channel-id", "enter your channel id here");
		generateConfigOption("-", "-");
		generateConfigOption("Tag-Info", "write the Material ID of the blocks you want to be alerted on destroy.");
		generateConfigOption("Tag-Info", "By default, Diamond, Iron and Gold are tagged.");
		getTaggedBlocks(); //Because for some reason it's a deviant fuck that decided it's not generating normally.

	}

	public List<String> getTaggedBlocks() {
		String key = "tagged-blocks";
		if (this.getStringList(key, null) == null || this.getStringList(key, null).isEmpty()) {
			this.setProperty(key, Arrays.asList("57", "42", "41"));
		}
		return this.getStringList(key, null);

	}


	public void generateConfigOption(String key, Object defaultValue) {
		if (this.getProperty(key) == null) {
			this.setProperty(key, defaultValue);
		}
		final Object value = this.getProperty(key);
		this.removeProperty(key);
		this.setProperty(key, value);
	}


	//Getters Start
	public Object getConfigOption(String key) {
		return this.getProperty(key);
	}

	public String getConfigString(String key) {
		return String.valueOf(getConfigOption(key));
	}

	public Integer getConfigInteger(String key) {
		return Integer.valueOf(getConfigString(key));
	}

	public Long getConfigLong(String key) {
		return Long.valueOf(getConfigString(key));
	}

	public Double getConfigDouble(String key) {
		return Double.valueOf(getConfigString(key));
	}

	public Boolean getConfigBoolean(String key) {
		return Boolean.valueOf(getConfigString(key));
	}


	//Getters End


	public Long getConfigLongOption(String key) {
		if (this.getConfigOption(key) == null) {
			return null;
		}
		return Long.valueOf(String.valueOf(this.getProperty(key)));
	}


	private boolean convertToNewAddress(String newKey, String oldKey) {
		if (this.getString(newKey) != null) {
			return false;
		}
		if (this.getString(oldKey) == null) {
			return false;
		}
		System.out.println("Converting Config: " + oldKey + " to " + newKey);
		Object value = this.getProperty(oldKey);
		this.setProperty(newKey, value);
		this.removeProperty(oldKey);
		return true;

	}


	void reload() {
		this.load();
		this.write();
		this.save();
	}
}
