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
            //get profile output strings
            List<String> profileInfo = plugin.daylightSensor_Profiles_GetInfo(key);
            //return the lines to requester
            plugin.sendMessageInfo(sender, profileInfo);
        }
        return true;
    }
}
