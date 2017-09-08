package xyz.derkades.xpflyx;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
	
	public static float XP_COST;
	
	public static boolean AUTO_DISABLE;
	
	public static String RAN_OUT_OF_XP;
	public static String NOT_A_PLAYER;
	public static String AUTO_DISABLED;
	public static String COMMAND_FLIGHT_DISABLED;
	public static String COMMAND_FLIGHT_ENABLED;
	public static String COMMAND_NOT_ENOUGH_XP;

	private static JavaPlugin plugin;
	
	@Override
	public void onEnable(){
		plugin = this;
		
		super.saveDefaultConfig();
		super.saveConfig();
		
		XP_COST = (float) getConfig().getDouble("xp-cost");
		
		AUTO_DISABLE = getConfig().getBoolean("auto-disable", false);
		
		RAN_OUT_OF_XP = colors(getConfig().getString("messages.ran-out-of-xp", "&bYou can no longer fly because you are out of xp."));
		NOT_A_PLAYER = colors(getConfig().getString("messages.not-a-player", "&cYou must be a player in order to execute this command."));
		AUTO_DISABLED = colors(getConfig().getString("messages.auto-disabled", "&cFlying has been disabled because you were on the ground."));
		COMMAND_FLIGHT_DISABLED = colors(getConfig().getString("messages.command-flight-disabled", "&bXP flight has been disabled."));
		COMMAND_FLIGHT_ENABLED = colors(getConfig().getString("messages.command-flight-enabled", "&bXP flight has been enabled."));
		COMMAND_NOT_ENOUGH_XP = colors(getConfig().getString("messages.command-not-enough-xp", "&cYou do not have enough XP to start flying."));
		
		new RemoveExpTask().runTaskTimer(this, 1, 1);
		
		getCommand("xpfly").setExecutor(new FlyCommand());
	}
	
	@Override
	public void onDisable(){
		for (UUID uuid: XP_FLY_PLAYERS){
			OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
			if (offline.isOnline()){
				Player player = (Player) offline;
				player.setAllowFlight(false);
			}
		}
		XP_FLY_PLAYERS.clear();
	}
	
	private static String colors(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	private static final ArrayList<UUID> XP_FLY_PLAYERS = new ArrayList<>();
	
	public static void setFlightEnabled(Player player, boolean flight) {
		if (flight) {
			if (!XP_FLY_PLAYERS.contains(player.getUniqueId())) XP_FLY_PLAYERS.add(player.getUniqueId());
			player.setAllowFlight(true);
			player.setFlying(true);
			
			if (AUTO_DISABLE) {
				new BukkitRunnable() {
					public void run() {
						if (!player.isFlying()) {
							// Player is no longer flying, disable flight.
							this.cancel();
							setFlightEnabled(player, false);
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
	
	public static boolean isFlying(Player player) {
		return XP_FLY_PLAYERS.contains(player.getUniqueId());
	}
	
}
