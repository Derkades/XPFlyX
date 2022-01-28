package xyz.derkades.xpflyx;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
	
	static float XP_COST;
	static boolean AUTO_DISABLE;
	static boolean ENABLE_PERMISSION;
	static String RAN_OUT_OF_XP;
	static String NOT_A_PLAYER;
	static String AUTO_DISABLED;
	static String COMMAND_FLIGHT_DISABLED;
	static String COMMAND_FLIGHT_ENABLED;
	static String COMMAND_NOT_ENOUGH_XP;
	static String COMMAND_NO_PERMISSION;

	static JavaPlugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		super.saveDefaultConfig();
		
		XP_COST = (float) getConfig().getDouble("xp-cost");
		AUTO_DISABLE = getConfig().getBoolean("auto-disable", false);
		ENABLE_PERMISSION = getConfig().getBoolean("auto-disable", false);
		
		RAN_OUT_OF_XP = message("messages.ran-out-of-xp", "&bYou can no longer fly because you are out of xp.");
		NOT_A_PLAYER = message("messages.not-a-player", "&cYou must be a player in order to execute this command.");
		AUTO_DISABLED = message("messages.auto-disabled", "&cFlying has been disabled because you were on the ground.");
		COMMAND_FLIGHT_DISABLED = message("messages.command-flight-disabled", "&bXP flight has been disabled.");
		COMMAND_FLIGHT_ENABLED = message("messages.command-flight-enabled", "&bXP flight has been enabled.");
		COMMAND_NOT_ENOUGH_XP = message("messages.command-not-enough-xp", "&cYou do not have enough XP to start flying.");
		COMMAND_NO_PERMISSION = message("messages.command-no-permission", "&cYou do not have permission to use this command.");
		
		new RemoveExpTask(this).runTaskTimer(this, 1, 1);
		
		getCommand("xpfly").setExecutor(new FlyCommand(this));
	}
	
	@Override
	public void onDisable() {
		for (final UUID uuid : XP_FLY_PLAYERS){
			final OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
			if (offline.isOnline()){
				final Player player = (Player) offline;
				player.setAllowFlight(false);
			}
		}
		XP_FLY_PLAYERS.clear();
	}
	
	private String message(final String path, final String def) {
		return colors(getConfig().getString(path, def));
	}
	
	private static String colors(final String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	private final Set<UUID> XP_FLY_PLAYERS = new HashSet<>();
	
	public void setFlight(final Player player, final boolean flight) {
		if (flight) {
			XP_FLY_PLAYERS.add(player.getUniqueId());
			player.setAllowFlight(true);
			player.setFlying(true);
			
			if (AUTO_DISABLE) {
				new BukkitRunnable() {
					@Override
					public void run() {
						if (!player.isFlying()) {
							// Player is no longer flying, disable flight.
							this.cancel();
							setFlight(player, false);
							player.sendMessage(AUTO_DISABLED);
						}
					}
				}.runTaskTimer(plugin, 5*20, 10);
			}
		} else {
			player.setAllowFlight(false);
			XP_FLY_PLAYERS.remove(player.getUniqueId());
		}
	}
	
	public boolean isFlying(final Player player) {
		return XP_FLY_PLAYERS.contains(player.getUniqueId());
	}
	
}
