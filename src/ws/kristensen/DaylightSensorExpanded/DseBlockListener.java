package ws.kristensen.DaylightSensorExpanded;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class DseBlockListener implements Listener {
    private final DaylightSensorExpanded plugin;
    
    public DseBlockListener(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        //get any pending action for the player
        String pendingAction = plugin.daylightSensor_PendingActions_Get(event.getPlayer());
        //make sure the user has set the setall pending action
        if (pendingAction.startsWith("setall:")) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            if (block.getType() == Material.DAYLIGHT_DETECTOR) {
                //record the block information as an extended sensor.
                plugin.daylightSensor_Altered_Set(block.getLocation(), pendingAction.substring(7));
                //notify user of success or failure of recording.
                player.sendMessage("Daylight Sensor set to profile: " + pendingAction.substring(7));
            }
        }
    }
    
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        //get any pending action for the player
        String pendingAction = plugin.daylightSensor_PendingActions_Get(event.getPlayer());
        //make sure the user has set a known pending action that we handle just prior to this.
        if (pendingAction.startsWith("set:") || pendingAction.startsWith("clear:")) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            String msg = "";
            if (block.getType() == Material.DAYLIGHT_DETECTOR) {
                //Set the default failure message
                msg = "No pending action is set.  No action taken.";
                if (pendingAction.startsWith("set:")) {
                    //record the block information as an extended sensor.
                    plugin.daylightSensor_Altered_Set(block.getLocation(), pendingAction.substring(4));
                    msg = "Daylight Sensor set to profile: " + pendingAction.substring(4);
                } else if (pendingAction.startsWith("clear:")) {
                    //remove the extended properties from the sensor that was clicked
                    plugin.daylightSensor_Altered_Remove(block.getLocation());
                    msg = "Profile cleared from Daylight Sensor";
                }
                
                //notify user of success or failure of recording.
                player.sendMessage(msg);
                event.setCancelled(true);
            } else {
                //let the user know it was a bad object.
                player.sendMessage("DaylightSensorExtended pending action canceled.");
                
                //do not cancel this event since they wanted to damage something else
            }
            //remove the pending event from being there for the player
            plugin.daylightSensor_PendingActions_Remove(player);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (!plugin.daylightSensorAltered_Loaded) {
            plugin.settings_Sensors_Read();
        }
        //do this test first to keep the footprint of this handler to a minimum
        if (event.getBlock().getType() == Material.DAYLIGHT_DETECTOR) {
            Block block = event.getBlock();
            if (event.getOldCurrent() != event.getNewCurrent()) {
                //Make sure the daylight detector is in our list of watched detectors.
                int test = plugin.daylightSensor_Altered_GetCurrent(block.getLocation(), event.getNewCurrent());
                //If it is then change the current to the mapped current.
                if (test > -1 && event.getNewCurrent() != test) {
                    if (plugin.debugLevel > 0)
                        plugin.getLogger().info("Altered sensor " + plugin.convertLocationToString(block.getLocation()) + " new redstone power from :" + String.valueOf(event.getNewCurrent()) + " to:" + String.valueOf(test));
                    event.setNewCurrent(test);
                }
            }
        }
    }
}
