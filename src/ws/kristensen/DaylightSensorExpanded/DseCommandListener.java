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
            } else if (arg == "info") {
                return plugin.clSensorInfo.onCommand(sender, command, label, args);
            }
        }
        return false;
    }
}
