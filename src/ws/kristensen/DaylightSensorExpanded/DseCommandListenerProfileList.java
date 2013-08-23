package ws.kristensen.DaylightSensorExpanded;

import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerProfileList implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerProfileList(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.hasPermission("daylightsensorexpanded.profile.list")) {
                return true;
            }
        }
        plugin.sendMessageInfo(sender, "Available Profiles");
        
        Set<String> keys = plugin.daylightSensor_Profiles_GetKeysAsSet();
        for (String key : keys) {
            List<Integer> profile = plugin.daylightSensor_Profiles_GetProfileAsList(key);

            String[] output = {"    ","    ","    ","    "};
            
            for (int i = 0; i < 16; i++) {
                output[i%4] = output[i%4] + (String.valueOf(i) + " ").substring(0, 2) + " -> " + (String.valueOf(profile.get(i)) + " ").substring(0,2) + "     ";
            }
            plugin.sendMessageInfo(sender, key + ": " + plugin.daylightSensor_Profiles_GetProfileAsString(key));
            plugin.sendMessageInfo(sender, output[0]);
            plugin.sendMessageInfo(sender, output[1]);
            plugin.sendMessageInfo(sender, output[2]);
            plugin.sendMessageInfo(sender, output[3]);
            //plugin.sendMessageInfo(sender, key + ": " + plugin.daylightSensor_Profiles_GetProfileAsString(key));
        }
        return true;
    }
}
