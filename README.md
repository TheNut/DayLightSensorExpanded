DayLightSensorExpanded
======================

Bukkit plugin to expand functionality of generic DaylightSensor.

This allows you to change the values of the redstone signal strength of a Daylight Sensor.
  See www.minecraftwiki.net/wiki/Daylight_Sensor
  Normally the values are from 0 to 15, you can re-map these 16 values to any other values you want.
  Example 15,14,13,12,10,9,8,7,6,5,4,3,2,1,0 would inverse the sensor.
  Example 1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0 would be good for a street lamp to turn on at sunset and off at sunrise.
  
Commands
--------
/dseprofilecreate <profileName> <parameters>
    description: Create a profile that can be applied to different Daylight Sensors.
    example: dseprofilecreate standard 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
    aliases: dsepcreate, dsepc
/dseprofilealter <profileName> <parameters>
    description: Alter a profile by assigning new settings to it. This will affect ALL sensors assigned the profile name.
    example: dseprofilealter standard 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
    aliases: dsepalter, dsepa
/dseprofiledelete <profileName>
    description: Remove a profile that has been created.
    aliases: dsepdelete, dsepd
/dseprofilelist
    description: Show a list of profiles that are defined.
    aliases: dseplist, dsepl
/dsesensorset <profileName>
    description: Indicate that the next object clicked on will be a sensor and set to the given profile.
    aliases: dseset, dses
/dsesensorsetall <profileName>
    description: Indicate that all future Daylight Sensors placed will be set to the given profile.
    aliases: dsesetall, dsesall, dsesa
/dsesensorclear
    description: Indicate that the next object clicked on will be a sensor and will have the extended profile removed.
    aliases: dseclear, dsesclear, dsesc
/dsesensorlist
    description: List all sensors that are considered extended sensors.
    aliases: dselist, dseslist, dsesl
/dsesensorconvertall <profileName> [Optional radius]
    description: Convert all sensors within a block radius to a given profile. Default of 50 radius from player location.
    aliases: dseconvertall, dsecall, dsecall
