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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DaylightSensorExpanded extends JavaPlugin {
    private final DseBlockListener blockListener = new DseBlockListener(this);
    private final DseCommandListener commandListener = new DseCommandListener(this);

    private final String ConfigurationSectionName_Basic = "Basic";
    protected int debugLevel = 0;

    private final String configurationSectionName_Profiles = "Profiles";
    private final Map<String, List<Integer>> daylightSensorProfiles = new HashMap<String, List<Integer>>();
    private boolean daylightSensorProfiles_Dirty = false;
    protected final DseCommandListenerProfileCreate clProfileCreate = new DseCommandListenerProfileCreate(this);
    protected final DseCommandListenerProfileAlter  clProfileAlter  = new DseCommandListenerProfileAlter(this);
    protected final DseCommandListenerProfileDelete clProfileDelete = new DseCommandListenerProfileDelete(this);
    protected final DseCommandListenerProfileList   clProfileList   = new DseCommandListenerProfileList(this);
    
    private final String configurationSectionName_Sensors = "Sensors";
    private final HashMap<Player, String> daylightSensorPendingActions = new HashMap<Player, String>();
    private final HashMap<Location, String> daylightSensorAltered = new HashMap<Location, String>();
    protected boolean daylightSensorAltered_Loaded = false;
    private boolean daylightSensorAltered_Dirty = false;
    protected final DseCommandListenerSensorSet         clSensorSet         = new DseCommandListenerSensorSet(this);
    protected final DseCommandListenerSensorSetAll      clSensorSetAll      = new DseCommandListenerSensorSetAll(this);
    protected final DseCommandListenerSensorConvertAll  clSensorConvertAll  = new DseCommandListenerSensorConvertAll(this);
    protected final DseCommandListenerSensorClear       clSensorClear       = new DseCommandListenerSensorClear(this);
    protected final DseCommandListenerSensorList        clSensorList        = new DseCommandListenerSensorList(this);
    protected final DseCommandListenerSensorInfo        clSensorInfo        = new DseCommandListenerSensorInfo(this);
    
    
    /**
     * Called when this plugin is enabled
     */
    @Override
    public void onEnable() {
        // Save a copy of the default config.yml if one is not there
        this.saveDefaultConfig();

        //Get the basic settings
        settings_Basic_Read();
        
        //Get the defined profiles
        settings_Profile_Read();

        //get the list of altered sensors
        //This is read later
        //settings_Sensors_Read();
        
        //Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(blockListener, this);
        
        //Register our commands to which we listen
        getCommand("daylightsensorexpanded").setExecutor(commandListener);
            //Profile Commands
            getCommand("dseprofilecreate").setExecutor(clProfileCreate);
            getCommand("dseprofilealter").setExecutor(clProfileAlter);
            getCommand("dseprofiledelete").setExecutor(clProfileDelete);
            getCommand("dseprofilelist").setExecutor(clProfileList);
            //Sensor Commands
            getCommand("dsesensorset").setExecutor(clSensorSet);
            getCommand("dsesensorsetall").setExecutor(clSensorSetAll);
            getCommand("dsesensorconvertall").setExecutor(clSensorConvertAll);
            getCommand("dsesensorclear").setExecutor(clSensorClear);
            getCommand("dsesensorlist").setExecutor(clSensorList);
            getCommand("dsesensorinfo").setExecutor(clSensorInfo);
            
        //Track the usage of this plugin with MCStats
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    /**
     * Called when this plugin is disabled 
     */
    @Override
    public void onDisable() {
        //code to shut down the plugin safely.
        
        //save the defined profiles
        if (daylightSensorAltered_Dirty || daylightSensorProfiles_Dirty) {
            settings_Basic_Write();
            settings_Profile_Write();
            settings_Sensors_Write();
            this.saveConfig();
        }
    }

    public void settings_Basic_Read() {
        debugLevel = this.getConfig().getConfigurationSection(ConfigurationSectionName_Basic).getInt("DebugLevel");
    }
    public void settings_Basic_Write() {
        this.getConfig().getConfigurationSection(ConfigurationSectionName_Basic).set("DebugLevel", debugLevel);
    }
    public void settings_Profile_Read() {
        int profilesLoaded = 0;
        boolean badRead = false;
        ConfigurationSection configSection = this.getConfig().getConfigurationSection(configurationSectionName_Profiles); 
        if (configSection == null) {
            //there is no profile config section, create one with a profile.
            daylightSensor_Profiles_Set("inverse", convertStringToListInteger("15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0"));
            this.getConfig().createSection(configurationSectionName_Profiles, daylightSensorProfiles);
            this.saveConfig();
        } else {
            Set<String> keys = configSection.getKeys(false);
            for (String key : keys) {
                String returnMsg = daylightSensor_Profiles_Set(key, (String)configSection.get(key));
                if (returnMsg == "") {
                    profilesLoaded++;
                } else {
                    badRead = true;
                    this.getLogger().warning(returnMsg);
                }
            }
            this.getLogger().info(String.valueOf(profilesLoaded) + " Profiles successfully loaded");
        }
        //if we have a bad read, then the config changed. otherwise the config is not changed and is not dirty.
        daylightSensorProfiles_Dirty = badRead;
    }
    public void settings_Profile_Write() {
        //convert hashmap data to strings
        HashMap<String, String> profiles = new HashMap<String, String>();
        for (String key : daylightSensorProfiles.keySet()) {
            profiles.put(key,convertListIntegerToString(daylightSensorProfiles.get(key)));
        }
        this.getConfig().set(configurationSectionName_Profiles, profiles);        
    }
    public void settings_Sensors_Read() {
        int sensorsLoaded = 0;
        boolean badRead = false;
        ConfigurationSection configSection = this.getConfig().getConfigurationSection(configurationSectionName_Sensors); 
        if (configSection == null) {
            this.getConfig().createSection(configurationSectionName_Sensors, daylightSensorAltered);
            this.saveConfig();
        } else {
            Set<String> keys = configSection.getKeys(false);
            for (String key : keys) {
                if (daylightSensor_Profiles_Exist((String)configSection.get(key))) {
                    daylightSensor_Altered_Set(key, (String)configSection.get(key));
                    sensorsLoaded++;
                } else {
                    badRead = true;
                    this.getLogger().info("Sensor not loaded because of missing profile: " + key + ":" + (String)configSection.get(key));
                }
            }
            this.getLogger().info(String.valueOf(sensorsLoaded) + " Sensors successfully loaded");
        }
        daylightSensorAltered_Loaded = true;
        //if we have a bad read, then the config changed. otherwise the config is not changed and is not dirty.
        daylightSensorAltered_Dirty = badRead;
    }
    public void settings_Sensors_Write() {
        //convert hashmap data to strings
        HashMap<String, String> sensors = new HashMap<String, String>();
        for (Location key : daylightSensorAltered.keySet()) {
            sensors.put(convertLocationToString(key),daylightSensorAltered.get(key));
        }
        this.getConfig().set(configurationSectionName_Sensors, sensors);        
    }
    
    public String        daylightSensor_Profiles_Set(final String name, final List<Integer> profile) {
        String returnMsg = "";
        if (profile.size() == 16) { 
            daylightSensorProfiles.put(name, profile);
            daylightSensorProfiles_Dirty = true;
        } else {
            returnMsg = "Invalid Profile (should be 16 parameters): " + name + ": " + convertListIntegerToString(profile);
            this.getLogger().warning(returnMsg);
        }
        return returnMsg;
    }
    public String        daylightSensor_Profiles_Set(final String name, final String profile) {
        List<Integer> temp = convertStringToListInteger(profile);
        return daylightSensor_Profiles_Set(name, temp);
    }
    public boolean       daylightSensor_Profiles_Exist(final String name) {
        return daylightSensorProfiles.containsKey(name);
    }
    public boolean       daylightSensor_Profiles_Remove(final String name) {
        if (daylightSensorProfiles.containsKey(name)) {
            daylightSensorProfiles.remove(name);
            daylightSensorProfiles_Dirty = true;
            return true;
        }
        return false;
    }
    public List<Integer> daylightSensor_Profiles_GetProfileAsList(final String name) {
        return daylightSensorProfiles.get(name);
    }
    public String        daylightSensor_Profiles_GetProfileAsString(final String name) {
        return convertListIntegerToString(daylightSensorProfiles.get(name));
    }
    public Set<String>   daylightSensor_Profiles_GetKeysAsSet() {
        return daylightSensorProfiles.keySet();
    }
    public List<String>  daylightSensor_Profiles_GetInfo(final String name) {
        List<Integer> profile = daylightSensor_Profiles_GetProfileAsList(name);
        List<String> returnList = new ArrayList<String>();

        String[] output = {"    ","    ","    ","    "};
        
        for (int i = 0; i < 16; i++) {
            output[i%4] = output[i%4] + (String.valueOf(i) + " ").substring(0, 2) + " -> " + (String.valueOf(profile.get(i)) + " ").substring(0,2) + "     ";
        }
        returnList.add("Sensor profile: " + name);
        returnList.add("(Standard redstone level -> mapped level)");
        returnList.add(output[0]);
        returnList.add(output[1]);
        returnList.add(output[2]);
        returnList.add(output[3]);
        
        return returnList;
    }    
    
    public void          daylightSensor_Altered_Set(final Location blockLocation, final String profile) {
        daylightSensorAltered.put(blockLocation, profile);
        daylightSensorAltered_Dirty = true;
    }
    public void          daylightSensor_Altered_Set(final String block, final String profile) {
        daylightSensor_Altered_Set(convertStringToLocation(block), profile);
    }
    public boolean       daylightSensor_Altered_Remove(final Location blockLocation) {
        if (daylightSensor_Altered_Exist(blockLocation)) {
            daylightSensorAltered.remove(blockLocation);
            daylightSensorAltered_Dirty = true;
            return true;
        }
        return false;
    }
    public int           daylightSensor_Altered_RemoveAssociated(final String profileName) {
        int removed = 0;
        Set<Location> keys = daylightSensorAltered.keySet();
        for (Location key : keys) {
            if (daylightSensor_Altered_Get(key) == profileName) {
                daylightSensor_Altered_Remove(key);
                removed++;
            }
        }
        return removed;
    }
    public boolean       daylightSensor_Altered_Exist(final Location blockLocation) {
        return daylightSensorAltered.containsKey(blockLocation);
    }
    public String        daylightSensor_Altered_Get(final Location blockLocation) {
        return daylightSensorAltered.get(blockLocation);
    }
    public int           daylightSensor_Altered_GetCurrent(final Location blockLocation, int current) {
        try {
            List<Integer> temp = daylightSensor_Profiles_GetProfileAsList(daylightSensor_Altered_Get(blockLocation));
            if (temp != null) 
                return temp.get(current);
            else
                return -1;
        } catch(Error e) {
            return -1;
        }
    }
    public Set<Location> daylightSensor_Altered_GetKeysAsSet() {
        return daylightSensorAltered.keySet();
    }
    
    public void          daylightSensor_PendingActions_Set(final Player player, final String pCommand) {
        daylightSensor_PendingActions_Remove(player);
        daylightSensorPendingActions.put(player, pCommand);
    }
    /**
     * Removes the action flags for a particular player. 
     * This happens when they click on an object so that the next click will not repeat the action.
     * 
     * @param player The player that does the action
     */
    public void          daylightSensor_PendingActions_Remove(final Player player) {
        if (daylightSensorPendingActions.containsKey(player)) {
            daylightSensorPendingActions.remove(player);
        }
    }
    public String        daylightSensor_PendingActions_Get(final Player player) {
        String returnValue = daylightSensorPendingActions.get(player);
        if (returnValue == null)
            return "";
        else
            return returnValue;
    }

    public void          sendMessageInfo(CommandSender sender, List<String> msg) {
        for (String line : msg) {
            sendMessageInfo(sender, line);
        }
    }
    public void          sendMessageInfo(CommandSender sender, String msg) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(msg);
        } else {
            this.getLogger().info(msg);
        }
    }
    public void          sendMessageWarning(CommandSender sender, List<String> msg) {
        for (String line : msg) {
            sendMessageWarning(sender, line);
        }
    }
    public void          sendMessageWarning(CommandSender sender, String msg) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(msg);
        } else {
            this.getLogger().warning(msg);
        }
    }
    public void          sendMessageSevere(CommandSender sender, List<String> msg) {
        for (String line : msg) {
            sendMessageSevere(sender, line);
        }
    }
    public void          sendMessageSevere(CommandSender sender, String msg) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(msg);
        } else {
            this.getLogger().severe(msg);
        }
    }
    
    public String        convertListIntegerToString(List<Integer> source) {
        String combined = "";
        Iterator<Integer> it = source.iterator();
        while (it.hasNext()) {
            combined = combined + (String)it.next().toString() + ",";
        }
        return combined.substring(0,combined.length()-1);
    }
    public List<Integer> convertStringToListInteger(String combined) {
        List<Integer> profile = new ArrayList<Integer>();
        String[] split = combined.split(","); 
        for (String item : split) {
            try {
                profile.add(Integer.parseInt(item));
            } catch (NumberFormatException e) {
                //do nothing, just suppress the error.
            }
        }
        return profile;
    }
    public String        convertLocationToString(Location location) {
        String combined = "";
        if (location != null) {
            if (location.getWorld() != null) {
                combined = location.getWorld().getName() + ",";
            }
            combined += location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        }
        return combined;
    }
    public Location      convertStringToLocation(String combined) {
        String split[] = combined.split(",");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }
}
