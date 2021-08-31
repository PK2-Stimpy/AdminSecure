package me.pk2.adminsecure.user;

import me.pk2.adminsecure.AdminSecure;
import me.pk2.adminsecure.config.ConfigDefault;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;

public class UserListener implements Listener {
    public boolean checkCommand(String label) {
        for(String cmd : ConfigDefault.security_code.allowed_commands)
            if(cmd.toLowerCase().startsWith("/" + label.toLowerCase()) || cmd.toLowerCase().startsWith("/" + label.toLowerCase() + " "))
                return true;
        return false;
    }
    public boolean playerConfigPermissionsCheck(Player player) {
        if(!ConfigDefault.security_code.join_config.triggers.permissions.enabled)
            return false;
        for(String permission : ConfigDefault.security_code.join_config.triggers.permissions.perms)
            if(player.hasPermission(permission))
                return true;
        return false;
    }
    public void fullJoinSecurityCheck(Player player) {
        if(!AdminSecure.INSTANCE.userManager.eliminationProcessPlayers.contains(player.getName()) && (AdminSecure.INSTANCE.userManager.verifiedPlayers.contains(player.getName()) || AdminSecure.INSTANCE.userManager.frozenPlayers.contains(player.getName().toLowerCase())))
            return;
        if(AdminSecure.INSTANCE.userManager.eliminationProcessPlayers.contains(player.getName())) {
            Bukkit.getScheduler().runTask(AdminSecure.INSTANCE, () -> player.kickPlayer("Player is in elimination process, please wait 1s."));
            return;
        }
        if(((ConfigDefault.security_code.join_config.triggers.op && player.isOp()) || player.hasPermission("adminsecure.code"))) {
            AdminSecure.INSTANCE.userManager.handleUserAdd(player);
            return;
        }

        if(((ConfigDefault.security_code.join_config.triggers.gamemode && player.getGameMode() == GameMode.CREATIVE) || playerConfigPermissionsCheck(player)))
            AdminSecure.INSTANCE.userManager.handleUserAdd(player, true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        if(AdminSecure.INSTANCE.userManager == null)
            return;

        AdminSecure.INSTANCE.userManager.handleUserAdd(event.getPlayer());
        fullJoinSecurityCheck(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        if(AdminSecure.INSTANCE.userManager == null)
            return;
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()) || AdminSecure.INSTANCE.userManager.containsUser(event.getPlayer().getName()))
            if(event.getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS))
                event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);

        AdminSecure.INSTANCE.userManager.eliminationProcessPlayers.add(event.getPlayer().getName());
        Bukkit.getScheduler().runTaskLater(AdminSecure.INSTANCE, () -> {
            AdminSecure.INSTANCE.userManager.handleUserRemove(event.getPlayer());
            AdminSecure.INSTANCE.userManager.verifiedPlayers.remove(event.getPlayer().getName());
            AdminSecure.INSTANCE.userManager.eliminationProcessPlayers.remove(event.getPlayer().getName());
        }, 1L);
    }

    /* FROZEN PLAYER EVENTS */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase())) {
            event.setCancelled(true);
            event.getPlayer().teleport(event.getFrom());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()) && !(event.getMessage().toLowerCase().startsWith("/adminsc ") || event.getMessage().toLowerCase().startsWith("/adminsc")) && !checkCommand(event.getMessage()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerHitPlayerEvent(EntityDamageByEntityEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getDamager().getName().toLowerCase()) || AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getEntity().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerShear(PlayerShearEntityEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerFish(PlayerFishEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onSignChange(SignChangeEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerHeldItem(PlayerItemHeldEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getWhoClicked().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityAirChange(EntityAirChangeEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getEntity().getName().toLowerCase()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if(AdminSecure.INSTANCE.userManager.frozenPlayers.contains(event.getPlayer().getName().toLowerCase()))
            event.setCancelled(true);
    }

    /* TRIGGERS */

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCommandOP(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().contains(" ")?event.getMessage().split(" "):new String[]{event.getMessage()};
        if(args[0].equalsIgnoreCase("/op") && args.length > 1 && (event.getPlayer().isOp() || event.getPlayer().hasPermission("minecraft.command.op"))) {
            if(!AdminSecure.INSTANCE.userManager.verifiedPlayers.contains(event.getPlayer().getName())) {
                AdminSecure.INSTANCE.userManager.handleUserAdd(event.getPlayer(), true);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if(ConfigDefault.security_code.join_config.triggers.gamemode && event.getNewGameMode() == GameMode.CREATIVE && !AdminSecure.INSTANCE.userManager.verifiedPlayers.contains(event.getPlayer().getName())) {
            AdminSecure.INSTANCE.userManager.handleUserAdd(event.getPlayer(), true);
            event.setCancelled(true);
        }
    }
}