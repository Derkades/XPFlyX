package xyz.derkades.xpflyx;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static final ArrayList<UUID> XP_FLY_PLAYERS = new ArrayList<>();
	
	public static float XP_COST;
	
	public static String RAN_OUT_OF_XP;
	public static String NOT_A_PLAYER;
	public static String COMMAND_FLIGHT_DISABLED;
	public static String COMMAND_FLIGHT_ENABLED;
	public static String COMMAND_NOT_ENOUGH_XP;

	@Override
	public void onEnable(){
		super.saveDefaultConfig();
		super.saveConfig();
		
		XP_COST = (float) getConfig().getDouble("xp-cost");
		
		RAN_OUT_OF_XP = colors(getConfig().getString("messages.ran-out-of-xp", "&bYou can no longer fly because you are out of xp."));
		NOT_A_PLAYER = colors(getConfig().getString("messages.not-a-player", "&cYou must be a player in order to execute this command."));
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
	
}
