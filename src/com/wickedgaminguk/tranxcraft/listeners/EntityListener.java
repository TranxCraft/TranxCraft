package com.wickedgaminguk.tranxcraft.listeners;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import java.util.ArrayList;
import net.pravian.bukkitlib.util.LocationUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class EntityListener implements Listener {

    private final TranxCraft plugin;

    public EntityListener(TranxCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!(event.getEntityType().equals(EntityType.PRIMED_TNT))) {
            LoggerUtils.info("A " + WordUtils.capitalizeFully(event.getEntityType().toString().toLowerCase()) + " exploded at: " + LocationUtils.format(event.getLocation()));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        Player p = (Player) event.getEntity();

        ArrayList<Player> noFall = plugin.noFall;

        if (noFall.contains(p)) {
            event.setCancelled(true);
            noFall.remove(p);
        }
    }

    @EventHandler
    public void onGrappleThrow(ProjectileLaunchEvent event) {
        if (!event.getEntityType().equals(EntityType.FISHING_HOOK)) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        final Player player = (Player) event.getEntity().getShooter();

        if (!(player.hasPermission("tranxcraft.grapple"))) {
        }
        else {
            if (plugin.util.hasGrapple(player)) {
                if (plugin.cooldown.contains(player)) {
                    event.setCancelled(true);
                    return;
                }

                Location target = null;

                for (Block block : player.getLineOfSight(null, 100)) {
                    if (!block.getType().equals(Material.AIR)) {
                        target = block.getLocation();
                        break;
                    }
                }

                if (target == null) {
                    event.setCancelled(true);
                    return;
                }

                player.teleport(player.getLocation().add(0, 0.5, 0));

                final Vector v = plugin.util.getVectorForPoints(player.getLocation(), target);

                event.getEntity().setVelocity(v);

                if (!plugin.noFall.contains(player)) {
                    plugin.noFall.add(player);
                }

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.setVelocity(v);
                }, 5);

                plugin.cooldown.add(player);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.cooldown.remove(player);
                }, 15);
            }
        }
    }
}
