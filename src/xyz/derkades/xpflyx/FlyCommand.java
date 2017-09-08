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
		
		if (Main.isFlying(player)){
			Main.setFlightEnabled(player, false);
			player.sendMessage(Main.COMMAND_FLIGHT_DISABLED);
		} else {
			if (player.getLevel() == 0 && player.getExp() < Main.XP_COST){
				player.sendMessage(Main.COMMAND_NOT_ENOUGH_XP);
			} else {
				Main.setFlightEnabled(player, true);
				player.sendMessage(Main.COMMAND_FLIGHT_ENABLED);
			}
		}
		
		return true;
		
	}

}
