package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {
    
    TranxCraft plugin;
    
    public BlockListener(TranxCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        switch (event.getBlockPlaced().getType()) {
            case FIREWORK: {
                if (!((plugin.moderatorList.isPlayerMod(player)))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Fireworks is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case TNT: {
                if (!((plugin.moderatorList.isPlayerMod(player)))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of TNT is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case LAVA:
            case STATIONARY_LAVA:
            case LAVA_BUCKET: {
                if (!((plugin.moderatorList.isPlayerMod(player)))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Lava is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case WATER:
            case STATIONARY_WATER:
            case WATER_BUCKET: {
                if (!((plugin.moderatorList.isPlayerMod(player)))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Water is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }

            case FIRE: {
                if (!((plugin.moderatorList.isPlayerMod(player)))) {
                    if (!(event.getPlayer().getName().equalsIgnoreCase("WickedGamingUK"))) {
                        player.sendMessage(ChatColor.RED + "The Use of Fire is not permitted on TranxCraft.");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        event.setCancelled(true);
                    }
                }
                break;
            }
        }
    }
}
