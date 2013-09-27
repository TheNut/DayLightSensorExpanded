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
See plugin.yml for list of commands. 

Changelog
---------
    Version 0.4 2013-09-27
        - Built for 1.6.4
        - No other changes or bug fixes
    Version 0.3 2013-08-31
        - Added sensorInfo command to list what profile a sensor is mapped to.
        - Added link to mcstats.org see bukkit page for details.
    Version 0.2 2013-08-26
        - Added commands, functionality, etc
    Version 0.1 2013-08-22
        Created plugin structure and stuff