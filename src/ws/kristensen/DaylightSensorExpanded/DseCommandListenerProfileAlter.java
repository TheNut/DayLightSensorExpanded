package ws.kristensen.DaylightSensorExpanded;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerProfileAlter implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerProfileAlter(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.hasPermission("daylightsensorexpanded.profile.alter")) {
                return true;
            }
        }
        if (args.length > 1) {
            if (plugin.daylightSensor_Profiles_Exist(args[0])) {
                //convert the string to a list
                List<Integer> profileArgs = plugin.convertStringToListInteger(args[1]);

                String returnMsg = plugin.daylightSensor_Profiles_Set(args[0], profileArgs);
                if (returnMsg != "") {
                    plugin.sendMessageInfo(sender, returnMsg);
                    return true;
                } else {
                    plugin.sendMessageInfo(sender, "Profile altered '" + args[0] + "': " + profileArgs);
                }
            } else {
                plugin.sendMessageInfo(sender, "Profile '" + args[0] + "' does not exist. Use Create to define it.");
            }
            return true;
        }
        return false;
    }
}
