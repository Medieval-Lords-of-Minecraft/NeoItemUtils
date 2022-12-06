package me.neoblade298.neoitemutils;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class CmdRename implements CommandExecutor {
	private static int RENAME_PRICE = 500;

	ItemUtils main;
	Economy econ;

	public CmdRename(ItemUtils main) {
		this.main = main;
		this.econ = main.getEconomy();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.hasPermission("neoitemutils.rename") && sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage("§4[§c§lMLMC§4] §cYou can't have a blank rename!");
				return true;
			}

			if (econ.getBalance(p) >= RENAME_PRICE) {
				ItemStack item = p.getInventory().getItemInMainHand();
				if (item != null && !item.getType().equals(Material.AIR)) {
					ItemMeta meta = item.getItemMeta();

					// Put together rename string
					String rename = args[0];
					for (int i = 1; i < args.length; i++) {
						rename += " " + args[i];
					}
					

					rename = main.translateHexCodes(rename);
					
					if (ChatColor.stripColor(rename).length() > 30 && !p.hasPermission("mycommand.staff")) {
						p.sendMessage("§4[§c§lMLMC§4] §cName must be less than 30 characters!");
						return true;
					}
					
					meta.setDisplayName(rename);
					item.setItemMeta(meta);
					econ.withdrawPlayer(p, RENAME_PRICE);
					p.sendMessage("§4[§c§lMLMC§4] §7Successfully renamed item!");
				}
				else {
					p.sendMessage("§4[§c§lMLMC§4] §cYou're not holding anything in your mainhand!");
				}
			}
			else {
				p.sendMessage("§4[§c§lMLMC§4] §cYou don't have enough gold for this!");
			}
		}
		return true;
	}
}