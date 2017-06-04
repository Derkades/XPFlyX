package xyz.derkades.xpflyx;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0){
			return false;
		}
		
		if (!(sender instanceof Player)){
			sender.sendMessage(Main.NOT_A_PLAYER);
			return true;
		}
		
		Player player = (Player) sender;
		
		if (Main.XP_FLY_PLAYERS.contains(player.getUniqueId())){
			//The player is currently flying
			
			player.setAllowFlight(false);
			
			Main.XP_FLY_PLAYERS.remove(player.getUniqueId());
			
			player.sendMessage(Main.COMMAND_FLIGHT_DISABLED);
			return true;
		} else {
			//The player is not flying
			
			if (player.getLevel() == 0 && player.getExp() < Main.XP_COST){
				player.sendMessage(Main.COMMAND_NOT_ENOUGH_XP);
				return true;
			} else {
				Main.XP_FLY_PLAYERS.add(player.getUniqueId());
				player.setAllowFlight(true);
				player.setFlying(true);
				player.sendMessage(Main.COMMAND_FLIGHT_ENABLED);
				return true;
			}
		}
		
	}

}
