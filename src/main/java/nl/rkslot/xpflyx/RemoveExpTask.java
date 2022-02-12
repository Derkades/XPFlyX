package nl.rkslot.xpflyx;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveExpTask extends BukkitRunnable {

	private final Main main;

	RemoveExpTask(final Main main) {
		this.main = main;
	}

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().stream().filter(main::isXpFlyEnabled).forEach(player -> {
			player.setFallDistance(0);

			final float currentProgress = player.getExp();
			
			if (player.getLevel() == 0 && currentProgress < Main.XP_COST){
				player.sendMessage(Main.RAN_OUT_OF_XP);
				main.setXpFlyEnabled(player, false);
//				if (main.getConfig().getBoolean("disable-fall-damage", true)) {
//					new BukkitRunnable() {
//						public void run() {
//							// Keep resetting fall distance while player is in air
//							player.setFallDistance(0);
//							if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
//								this.cancel();
//							}
//						}
//					}.runTaskTimer(main, 1, 1);
//				}
				return;
			}

			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				final float newProgress = currentProgress - Main.XP_COST;

				if (newProgress < 0) {
					player.setLevel(player.getLevel() - 1);
					player.setExp(1.0f + newProgress);
				} else {
					player.setExp(newProgress);
				}
			}
		});
	}

}
