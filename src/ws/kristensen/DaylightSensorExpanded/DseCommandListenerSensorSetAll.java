package ws.kristensen.DaylightSensorExpanded;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerSensorSetAll implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerSensorSetAll(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("daylightsensorexpanded.sensor.setall")) {
                return true;
            }

            //make sure they specify the profile name
            if (args.length == 1) {
                //make sure profile exists
                if (plugin.daylightSensor_Profiles_Exist(args[0])) {
                    plugin.daylightSensor_PendingActions_Set(player, "setall:" + args[0]);
                    player.sendMessage("All future Daylight Sensors placed will be set to profile: " + args[0]);
                } else {
                    player.sendMessage("Invalid specified profile: " + args[0]);
                }
                return true;
            }
            return false;
        } else {
            plugin.getLogger().info("'setall' command not available at console");
            return true;
        }
        
    }
}
