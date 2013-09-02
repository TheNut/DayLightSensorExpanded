/*
 * Copyright 2013 Alan Kristensen. All rights reserved.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  See LICENSE in the jar file. If not, 
 *   see {http://www.gnu.org/licenses/}.
 */
package ws.kristensen.DaylightSensorExpanded;

import java.util.List;

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
                plugin.sendMessageInfo(player, "Daylight Sensor set to profile: " + pendingAction.substring(7));
            }
        }
    }
    
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        //get any pending action for the player
        String pendingAction = plugin.daylightSensor_PendingActions_Get(event.getPlayer());
        //make sure the user has set a known pending action that we handle just prior to this.
        if (pendingAction.startsWith("set:") || pendingAction.startsWith("clear:") || pendingAction.startsWith("info:")) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            if (block.getType() == Material.DAYLIGHT_DETECTOR) {
                if (pendingAction.startsWith("set:")) {
                    //record the block information as an extended sensor.
                    plugin.daylightSensor_Altered_Set(block.getLocation(), pendingAction.substring(4));
                    plugin.sendMessageInfo(player, "Daylight Sensor set to profile: " + pendingAction.substring(4));
                } else if (pendingAction.startsWith("clear:")) {
                    //remove the extended properties from the sensor that was clicked
                    plugin.daylightSensor_Altered_Remove(block.getLocation());
                    plugin.sendMessageInfo(player, "Profile cleared from Daylight Sensor");
                } else if (pendingAction.startsWith("info:")) {
                    //display the profile information for the sensor

                    //see if a profile exists
                    if (!plugin.daylightSensor_Altered_Exist(block.getLocation())) {
                        plugin.sendMessageInfo(player, "This is a standard Daylight Sensor.");
                    } else {
                        //get profile parameters
                        String profileName = plugin.daylightSensor_Altered_Get(block.getLocation());
                        List<String> profileInfo = plugin.daylightSensor_Profiles_GetInfo(profileName);
                        plugin.sendMessageInfo(player, profileInfo);
                    }
                } else {
                    //Send the default failure message
                    plugin.sendMessageInfo(player, "No pending action is set.  No action taken.");
                }
                
                event.setCancelled(true);
            } else {
                //let the user know it was a bad object.
                plugin.sendMessageInfo(player, "DaylightSensorExtended pending action canceled.");
                
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
                        plugin.sendMessageInfo(null, "Altered sensor " + plugin.convertLocationToString(block.getLocation()) + " new redstone power from :" + String.valueOf(event.getNewCurrent()) + " to:" + String.valueOf(test));
                    event.setNewCurrent(test);
                }
            }
        }
    }
}
