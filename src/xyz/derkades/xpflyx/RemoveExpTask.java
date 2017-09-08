package xyz.derkades.xpflyx;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveExpTask extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()){
			if (Main.isFlying(player)){
				//This player is currently flying
				
				float currentProgress = player.getExp();
				
				if (player.getLevel() == 0 && currentProgress < Main.XP_COST){
					player.sendMessage(Main.RAN_OUT_OF_XP);
					Main.setFlightEnabled(player, false);
					continue;
				}
				
				float newProgress = currentProgress - Main.XP_COST;
				
				if (newProgress < 0){
					player.setLevel(player.getLevel() - 1);
					player.setExp(1.0f + newProgress);
				} else {
					player.setExp(newProgress);
				}
			}
		}
	}

}
