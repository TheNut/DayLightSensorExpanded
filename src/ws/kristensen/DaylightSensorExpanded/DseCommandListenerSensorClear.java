package ws.kristensen.DaylightSensorExpanded;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerSensorClear implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerSensorClear(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("daylightsensorexpanded.sensor.clear")) {
                return true;
            }
            
            //make sure they specify the profile name
            plugin.daylightSensor_PendingActions_Set(player, "clear:");
            player.sendMessage("Click on sensor to reset to standard Daylight Sensor.");
        } else {
            plugin.getLogger().info("'delete' command not available at console");
        }
        return true;
    }
}
