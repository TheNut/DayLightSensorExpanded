package ws.kristensen.DaylightSensorExpanded;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerProfileDelete implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerProfileDelete(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.hasPermission("daylightsensorexpanded.profile.delete")) {
                return true;
            }
        }
        if (args.length == 1) {
            if (plugin.daylightSensor_Profiles_Remove(args[0])) {
                int removed = plugin.daylightSensor_Altered_RemoveAssociated(args[0]);
                plugin.sendMessageInfo(sender, "Removed Profile: " + args[0] + " which resulted in removing " + String.valueOf(removed) + " associated Sensors alterations.");
            } else {
                plugin.sendMessageInfo(sender, "Profile: " + args[0] + " not found.");
            }
            return true;
        }
        return false;
    }
}
