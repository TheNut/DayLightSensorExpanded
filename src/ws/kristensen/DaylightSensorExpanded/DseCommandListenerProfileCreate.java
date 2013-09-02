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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DseCommandListenerProfileCreate implements CommandExecutor {
    private final DaylightSensorExpanded plugin;

    public DseCommandListenerProfileCreate(DaylightSensorExpanded plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.hasPermission("daylightsensorexpanded.profile.create")) {
                return true;
            }
        }
        if (args.length > 1) {
            if (!plugin.daylightSensor_Profiles_Exist(args[0])) {
                //convert the string to a list
                List<Integer> profileArgs = plugin.convertStringToListInteger(args[1]);

                String returnMsg = plugin.daylightSensor_Profiles_Set(args[0], profileArgs);
                if (returnMsg != "") {
                    plugin.sendMessageInfo(sender, returnMsg);
                    return true;
                } else {
                    plugin.sendMessageInfo(sender, "Profile created '" + args[0] + "': " + profileArgs);
                }
            } else {
                plugin.sendMessageInfo(sender, "Profile '" + args[0] + "' already exists. Use Alter to change it.");
            }
            return true;
        }
        return false;
    }
}
