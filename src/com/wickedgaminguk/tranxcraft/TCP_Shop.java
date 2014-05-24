package com.wickedgaminguk.tranxcraft;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TCP_Shop {

    private final TCP_Util TCP_Util;

    public TCP_Shop(TranxCraft plugin) {
        this.TCP_Util = new TCP_Util(plugin);
    }

    private final Map<Material, Double> itemPrice = new HashMap();

    public void init() {
        itemPrice.put(Material.LOG, 8.0);
        itemPrice.put(Material.WOOD, 2.0);
        itemPrice.put(Material.STICK, 1.0);
        itemPrice.put(Material.WOOD_STEP, 1.0);
        itemPrice.put(Material.WOOD_STAIRS, 4.0);
        itemPrice.put(Material.WORKBENCH, 8.0);
        itemPrice.put(Material.CHEST, 19.0);
        itemPrice.put(Material.BOWL, 3.0);
        itemPrice.put(Material.SIGN, 16.0);
        itemPrice.put(Material.WOOD_DOOR, 15.0);
        itemPrice.put(Material.TRAP_DOOR, 7.0);
        itemPrice.put(Material.FENCE, 5.0);
        itemPrice.put(Material.FENCE_GATE, 11.0);
        itemPrice.put(Material.LADDER, 3.0);
        itemPrice.put(Material.BOAT, 10.0);
        itemPrice.put(Material.WOOD_PLATE, 4.0);
        itemPrice.put(Material.PAINTING, 18.0);
        itemPrice.put(Material.BED, 28.0);

        itemPrice.put(Material.COBBLESTONE, 2.0);
        itemPrice.put(idToMaterial("44:3"), 1.0); //Cobblestone Slab
        itemPrice.put(Material.COBBLESTONE_STAIRS, 5.0);
        itemPrice.put(Material.MOSSY_COBBLESTONE, 10.0);
        itemPrice.put(Material.FURNACE, 28.0);
        itemPrice.put(Material.LEVER, 3.0);
        itemPrice.put(Material.STONE, 6.0);
        itemPrice.put(idToMaterial(44), 3.0); //Stone Slab
        itemPrice.put(Material.STONE_BUTTON, 12.0);
        itemPrice.put(Material.STONE_PLATE, 12.0);
        itemPrice.put(idToMaterial(98), 6.0); //Stone Brick
        itemPrice.put(idToMaterial("44:5"), 3.0); //Stone Brick Slab
        itemPrice.put(idToMaterial(109), 9.0); //Stone Brick Stairs
        itemPrice.put(idToMaterial("98:1"), 10.0); //Mossy Stone Brick
        itemPrice.put(idToMaterial("98:2"), 10.0); //Cracked Stone Brick
        itemPrice.put(idToMaterial("98:3"), 10.0); //Chiseled Stone Brick

        itemPrice.put(Material.IRON_ORE, 37.0);
        itemPrice.put(Material.IRON_INGOT, 41.0);
        itemPrice.put(Material.IRON_BLOCK, 369.0);
        itemPrice.put(Material.IRON_DOOR, 264.0);
        itemPrice.put(Material.BUCKET, 135.0);
        itemPrice.put(idToMaterial(326), 136.0); //Water Bucket
        itemPrice.put(idToMaterial(327), 137.0); //Lava Bucket
        itemPrice.put(idToMaterial(355), 138.0); //Milk Bucket
        itemPrice.put(Material.COMPASS, 186.0);
        itemPrice.put(Material.MINECART, 221.0);
        itemPrice.put(idToMaterial(342), 240.0); //Minecart with Chest
        itemPrice.put(Material.RAILS, 17.0);
        itemPrice.put(Material.DETECTOR_RAIL, 48.0);
        itemPrice.put(idToMaterial(101), 16.0);
        itemPrice.put(Material.CAULDRON, 307.0);

        itemPrice.put(Material.REDSTONE, 8.0);
        itemPrice.put(Material.REDSTONE_TORCH_OFF, 9.0);
        itemPrice.put(idToMaterial(93), 54.0); //Redstone Repeater
        itemPrice.put(Material.REDSTONE_LAMP_OFF, 81.0);

        itemPrice.put(Material.GOLD_ORE, 104.0);
        itemPrice.put(Material.GOLD_INGOT, 108.0);
        itemPrice.put(Material.GOLD_BLOCK, 945.0);
        itemPrice.put(Material.GOLD_NUGGET, 12.0);
        itemPrice.put(idToMaterial(347), 463.0); //Clock
        itemPrice.put(Material.POWERED_RAIL, 114.0);

        itemPrice.put(Material.COAL, 12.0);
        itemPrice.put(Material.TORCH, 3.0);
        itemPrice.put(Material.JACK_O_LANTERN, 7.0);
        itemPrice.put(idToMaterial(385), 16.0); //Fire Charge
        itemPrice.put(idToMaterial("351:4"), 28.0); //Lapis Lazuli
        itemPrice.put(Material.LAPIS_BLOCK, 252.0);
        itemPrice.put(Material.DIAMOND, 167.0);
        itemPrice.put(Material.DIAMOND_BLOCK, 1503.0);
        itemPrice.put(Material.SADDLE, 15.0);
    }

    public void buy(Player player, Material material, int quantity) {
        TCP_Util.withdrawPlayer(player, getPrice(material) * quantity);

        if (getPrice(material) != 0) {
            TCP_Util.sendItem(player, material, quantity, ChatColor.GREEN + "You have just bought " + ChatColor.GOLD + material.toString() + ChatColor.GREEN + " for $" + ChatColor.GOLD + getPrice(material));
        }
    }

    public double getPrice(Material material) {
        if (itemPrice.get(material) != null) {
            return itemPrice.get(material);
        }
        else {
            return 0.0;
        }
    }

    public Material idToMaterial(int id) {
        return Material.getMaterial(id);
    }

    public Material idToMaterial(String id) {
        return Material.getMaterial(id);
    }
}
