package ws.kristensen.DaylightSensorExpanded;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerSensorSet implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerSensorSet(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("daylightsensorexpanded.sensor.set")) {
                return true;
            }

            //make sure they specify the profile name
            if (args.length == 1) {
                //make sure profile exists
                if (plugin.daylightSensor_Profiles_Exist(args[0])) {
                    plugin.daylightSensor_PendingActions_Set(player, "set:" + args[0]);
                    player.sendMessage("Click on sensor to set to profile: " + args[0]);
                } else {
                    player.sendMessage("Invalid specified profile: " + args[0]);
                }
                return true;
            }
            return false;
        } else {
            plugin.getLogger().info("'set' command not available at console");
            return true;
        }
        
    }
}
