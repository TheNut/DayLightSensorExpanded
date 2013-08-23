package ws.kristensen.DaylightSensorExpanded;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerSensorConvertAll implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerSensorConvertAll(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.hasPermission("daylightsensorexpanded.sensor.convert.all")) {
                return true;
            }
            
            if (args.length >= 1) {
                if (!plugin.daylightSensor_Profiles_Exist(args[0])) {
                    plugin.sendMessageInfo(sender, "Profile does not exist: " + args[0]);
                    return false;
                }
                
                int radius = 50;
                int converted = 0;
                if (args.length >= 2)
                    radius = Integer.parseInt(args[1]);
                
                plugin.sendMessageInfo(sender, "Converting all Daylight Sensors within " + String.valueOf(radius) + " blocks radius to profile: " + args[0]);
        
                //find the player location
                Location playerLocation = player.getLocation();

                //get the world the player is in
                final World world = player.getWorld();

                //find all blocs with in the radius of the player.
                for (int searchX = playerLocation.getBlockX() - radius; searchX <= playerLocation.getBlockX() + radius; searchX++) {
                    for (int searchY = playerLocation.getBlockY() - radius; searchY <= playerLocation.getBlockY() + radius; searchY++) {
                        for (int searchZ = playerLocation.getBlockZ() - radius; searchZ <= playerLocation.getBlockZ() + radius; searchZ++) {

                            final Block sensorBlock = world.getBlockAt(searchX, searchY, searchZ);

                            if (sensorBlock.getType() == Material.DAYLIGHT_DETECTOR) {
                                //record the profile with the block location
                                plugin.daylightSensor_Altered_Set(sensorBlock.getLocation(), args[0]);
                                //increment converted counter
                                converted++;
                            }
                        }
                    }
                }
                
                //report how many sensors set to the profile
                plugin.sendMessageInfo(sender, String.valueOf(converted) + " found and converted.");
                
                return true;
            }
            return false;
        } else {
            plugin.getLogger().info("'convertall' command not available at console");
        }
        return true;
    }
}
