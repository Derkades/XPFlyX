package nl.rkslot.xpflyx;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

	private final Main main;

	FlyCommand(final Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (args.length > 0){
			return false;
		}
		
		if (!(sender instanceof Player)){
			sender.sendMessage(Main.NOT_A_PLAYER);
			return true;
		}
		
		if (Main.ENABLE_PERMISSION && !sender.hasPermission("xpflyx.fly")) {
			sender.sendMessage(Main.COMMAND_NO_PERMISSION);
			return true;
		}
		
		final Player player = (Player) sender;
		
		if (main.isXpFlyEnabled(player)){
			main.setXpFlyEnabled(player, false);
			player.sendMessage(Main.COMMAND_FLIGHT_DISABLED);
		} else {
			if (player.getLevel() == 0 && player.getExp() < Main.XP_COST){
				player.sendMessage(Main.COMMAND_NOT_ENOUGH_XP);
			} else {
				main.setXpFlyEnabled(player, true);
				player.sendMessage(Main.COMMAND_FLIGHT_ENABLED);
			}
		}
		
		return true;
		
	}

}
