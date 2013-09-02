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
                    plugin.sendMessageInfo(sender, "All future Daylight Sensors placed will be set to profile: " + args[0]);
                } else {
                    plugin.sendMessageInfo(sender, "Invalid specified profile: " + args[0]);
                }
                return true;
            }
            return false;
        } else {
            plugin.sendMessageInfo(sender, "'setall' command not available at console");
            return true;
        }
        
    }
}
