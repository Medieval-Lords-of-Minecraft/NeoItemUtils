package me.neoblade298.neoitemutils;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;


public class CmdItems implements CommandExecutor{
	
	ItemUtils main;
	
	public CmdItems(ItemUtils main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender instanceof Player && sender.hasPermission("mycommand.staff")) {
			Player p = (Player) sender;

			ItemStack item = p.getInventory().getItemInMainHand();
			ItemMeta meta = item.getItemMeta();
			if (item == null || item.getType().equals(Material.AIR)) {
				p.sendMessage("§cYou must be holding an item in your hand!");
				return true;
			}
			
			if (args.length == 0) {
				p.sendMessage("§c/nitem lore set/add/rem/get");
				p.sendMessage("§c/nitem name set/get");
				p.sendMessage("§c/nitem model set/get");
				p.sendMessage("§c/nitem nbt set/get [int/double/string] [key] [value]");
				p.sendMessage("§c/nitem nbt getkeys");
			}
			else if (args[0].equalsIgnoreCase("nbt")) {
				NBTItem nbti = new NBTItem(item);
				if (args.length == 1) {
					p.sendMessage("§c/nitem nbt set [int/double/string] [key] [value]");
					p.sendMessage("§c/nitem nbt get [int/double/string] [key]");
					p.sendMessage("§c/nitem nbt getkeys");
				}
				else {
					if (args[1].equalsIgnoreCase("set")) {
						if (args[2].equalsIgnoreCase("int")) {
							nbti.setInteger(args[3], Integer.parseInt(args[4]));
							nbti.applyNBT(item);
							p.sendMessage("§7Successfully set NBT");
						}
						else if (args[2].equalsIgnoreCase("double")) {
							nbti.setDouble(args[3], Double.parseDouble(args[4]));
							nbti.applyNBT(item);
							p.sendMessage("§7Successfully set NBT");
						}
						else if (args[2].equalsIgnoreCase("string")) {
							nbti.setString(args[3], args[4]);
							nbti.applyNBT(item);
							p.sendMessage("§7Successfully set NBT");
						}
					}
					else if (args[1].equalsIgnoreCase("get")) {
						if (args[2].equalsIgnoreCase("int")) {
							p.sendMessage("" + nbti.getInteger(args[3]));
						}
						else if (args[2].equalsIgnoreCase("double")) {
							p.sendMessage("" + nbti.getDouble(args[3]));
						}
						else if (args[2].equalsIgnoreCase("string")) {
							p.sendMessage(nbti.getString(args[3]));
						}
					}
					else if (args[1].equalsIgnoreCase("getkeys")) {
						for (String key : nbti.getKeys()) {
							p.sendMessage(key);
						}
					}
				}
			}
			else if (args[0].equalsIgnoreCase("lore")) {
				if (args.length == 1) {
					p.sendMessage("§c/nitem lore set # [lore]");
					p.sendMessage("§c/nitem lore get #");
					p.sendMessage("§c/nitem lore add [lore]");
					p.sendMessage("§c/nitem lore insert # [lore]");
					p.sendMessage("§c/nitem lore rem #");
				}
				else {
					ArrayList<String> lore = new ArrayList<String>();
					if (meta.hasLore()) {
						lore = (ArrayList<String>) meta.getLore();
					}
					
					
					if (args[1].equalsIgnoreCase("set")) {
						int num = Integer.parseInt(args[2]);
						if (lore.size() <= num) {
							p.sendMessage("§cLore is smaller than #!");
							return true;
						}
						
						// Piece together the lore
						String line = args[3];
						for (int i = 4; i < args.length; i++) {
							line += " " + args[i];
						}
						
						lore.set(num, main.translateHexCodes(line));
						meta.setLore(lore);
						p.sendMessage("§7Successfully set lore");
						item.setItemMeta(meta);
					}
					else if (args[1].equalsIgnoreCase("get")) {
						int num = Integer.parseInt(args[2]);
						if (lore.size() <= num) {
							p.sendMessage("§cLore is smaller than #!");
							return true;
						}
						
						p.sendMessage(lore.get(num).replaceAll("§", "&"));
					}
					else if (args[1].equalsIgnoreCase("add")) {
						// Piece together the lore
						String line = args[2];
						for (int i = 3; i < args.length; i++) {
							line += " " + args[i];
						}
						
						lore.add(main.translateHexCodes(line));
						meta.setLore(lore);
						item.setItemMeta(meta);
						p.sendMessage("§7Successfully added lore");
					}
					// nitem lore insert # [line]
					else if (args[1].equalsIgnoreCase("insert")) {
						// Piece together the lore
						String line = args[3];
						for (int i = 4; i < args.length; i++) {
							line += " " + args[i];
						}
						
						lore.add(Integer.parseInt(args[2]), main.translateHexCodes(line));
						meta.setLore(lore);
						item.setItemMeta(meta);
						p.sendMessage("§7Successfully added lore");
					}
					else if (args[1].equalsIgnoreCase("rem")) {
						int num = Integer.parseInt(args[2]);
						if (lore.size() <= num) {
							p.sendMessage("§cLore is smaller than #!");
							return true;
						}
						
						lore.remove(num);
						meta.setLore(lore);
						item.setItemMeta(meta);
						p.sendMessage("§7Successfully removed lore");
					}
				}
			}
			else if (args[0].equalsIgnoreCase("name")) {
				if (args.length == 1) {
					p.sendMessage("§c/nitem name set [name]");
					p.sendMessage("§c/nitem name get [name]");
				}
				else {
					if (args[1].equalsIgnoreCase("set")) {
						// Piece together the name
						String line = args[2];
						for (int i = 3; i < args.length; i++) {
							line += " " + args[i];
						}
						meta.setDisplayName(main.translateHexCodes(line));
						item.setItemMeta(meta);
						p.sendMessage("§7Successfully set name");
					}
					else if (args[1].equalsIgnoreCase("get")) {
						if (!meta.hasDisplayName()) {
							p.sendMessage("§cItem does not have a display name!");
						}
						else {
							p.sendMessage(meta.getDisplayName().replaceAll("§", "&"));
						}
					}
				}
			}
			else if (args[0].equalsIgnoreCase("model")) {
				if (args.length == 1) {
					p.sendMessage("§c/nitem model set [model]");
					p.sendMessage("§c/nitem model get [model]");
				}
				else {
					if (args[1].equalsIgnoreCase("set")) {
						int num = Integer.parseInt(args[2]);
						
						meta.setCustomModelData(num);
						item.setItemMeta(meta);
						p.sendMessage("§cSuccessfully set custom model data!");
					}
					else if (args[1].equalsIgnoreCase("get")) {
						if (!meta.hasCustomModelData()) {
							p.sendMessage("§cItem does not have custom model data!");
						}
						else {
							p.sendMessage("" + meta.getCustomModelData());
						}
					}
				}
			}
		}
		return true;
	}
}