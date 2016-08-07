package com.absolutecraft.bungee;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import com.absolutecraft.bungee.listeners.PostLogin;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class AbsoluteBungee extends Plugin {
	
	private Configuration config;
	
	@Override
    public void onEnable() {
		File configFile = new File(this.getDataFolder(), "config.yml");
		try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
		} catch (IOException e) {
			this.getLogger().log(Level.SEVERE, "Failed to retrieve config file", e);
			this.onDisable();
		}
		
		this.getProxy().getPluginManager().registerListener(this, new PostLogin(this));
    }
	
	@Override
	public void onDisable() {
	    this.getProxy().getPluginManager().unregisterListener(new PostLogin(this));
	}
	
	protected Configuration getConfig() {
	    return this.config;
	}
	
}
