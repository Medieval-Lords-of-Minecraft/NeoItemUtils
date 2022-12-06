package me.neoblade298.neoitemutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class ItemUtils extends JavaPlugin implements org.bukkit.event.Listener {
    private static Economy econ = null;
	public static final Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoItemUtils Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
	    setupEconomy();
	    this.getCommand("nitem").setExecutor(new CmdItems(this));
	    this.getCommand("rename").setExecutor(new CmdRename(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoItemUtils Disabled");
	    super.onDisable();
	}

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public Economy getEconomy() {
    	return econ;
    }
    


	public String translateHexCodes(String textToTranslate) {

		Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(buffer, ChatColor.of(matcher.group(1)).toString());
		}

		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());

	}
}
