package ws.kristensen.DaylightSensorExpanded;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerSensorList implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerSensorList(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.hasPermission("daylightsensorexpanded.sensor.list")) {
                return true;
            }
        }
        plugin.sendMessageInfo(sender, "Available Sensors");

        Set<Location> keys = plugin.daylightSensor_Altered_GetKeysAsSet();
        for (Location key : keys) {
            String profile = plugin.daylightSensor_Altered_Get(key);
            
            plugin.sendMessageInfo(sender, plugin.convertLocationToString(key) + ": " + profile + "(" + plugin.daylightSensor_Profiles_GetProfileAsString(profile) + ")");
        }
        return true;
    }
}
