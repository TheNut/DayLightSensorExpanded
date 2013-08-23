package ws.kristensen.DaylightSensorExpanded;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DseCommandListener implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListener(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            String arg = args[0];
            args = java.util.Arrays.copyOfRange(args, 1, args.length);
            if (arg == "profilecreate") {
                return plugin.clProfileCreate.onCommand(sender, command, label, args);
            } else if (arg == "profilealter") {
                return plugin.clProfileAlter.onCommand(sender, command, label, args);
            } else if (arg == "profiledelete") {
                return plugin.clProfileDelete.onCommand(sender, command, label, args);
            } else if (arg == "profilelist") {
                return plugin.clProfileList.onCommand(sender, command, label, args);
            } else if (arg == "set") {
                return plugin.clSensorSet.onCommand(sender, command, label, args);
            } else if (arg == "setall") {
                return plugin.clSensorSetAll.onCommand(sender, command, label, args);
            } else if (arg == "clear") {
                return plugin.clSensorClear.onCommand(sender, command, label, args);
            } else if (arg == "list") {
                return plugin.clSensorList.onCommand(sender, command, label, args);
            } else if (arg == "convertall") {
                return plugin.clSensorConvertAll.onCommand(sender, command, label, args);
            }
        }
        return false;
    }
}
