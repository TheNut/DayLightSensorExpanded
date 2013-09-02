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
