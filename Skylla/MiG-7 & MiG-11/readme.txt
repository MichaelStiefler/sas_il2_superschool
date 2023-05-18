 **********************************************************************************
*                                                                                  *
*                                   Installation:                                  *
*                                                                                  *
 **********************************************************************************

Extract this file and copy the "MiG-7_11" folder to your game's modded files folder.
This is a subfolder of your IL-2 1946 game folder, the name differs depending
on the modpack you're using, for instance the folder name could be
"#SAS", "#WAW", "#DBW", "#UP#" or just "MODS".

Add the following lines to your air.ini file.
The file usually can be found in the following location:
<Your IL2 game folder>/<your modded files folder>/STD/com/maddox/il2/objects/air.ini
Add these lines:

MiG-7             air.MIG_7 1                           NOINFO  r01   SUMMER
MiG-11            air.MIG_11 1                          NOINFO  r01   SUMMER



ATTENTION:  You need some kind of "DiffFM" mod installed in your game.
            CUP, Modact 5/6, DBW and UP3 ship with DiffFM included by default,
            others need to make sure to have it installed, otherwise your game will crash at 60%.

            Some links for DiffFM mods (not comprehensive):
            4.09m:                http://www.sas1946.com/main/index.php/topic,3988.0.html
            4.10m (incl. UP3):    http://www.sas1946.com/main/index.php/topic,12169.0.html
            4.11m (incl. HSFX 6): http://www.sas1946.com/main/index.php/topic,24777.0.html
            HSFX 7:               http://www.sas1946.com/main/index.php/topic,38980.0.html

ATTENTION²: You need "SAS Common Utils" mod installed in your game.
            CUP and Modact 6 ship with SAS Common Utils included by default,
            others need to make sure to have it installed, otherwise your game will crash when you fly this plane.
           
            Get the latest SAS Common Utils here, it works for any base game version and causes no conflicts
            with any other mod or mod pack, comes as a simple non-intrusive addon with no side effects:
            http://www.sas1946.com/main/index.php/topic,40490.0.html



- Optional -
Add the following lines to your "plane.properties" or "plane_ru.properties" file:

MiG-7         MiG-7 '44
MiG-11        MiG-11 '44

------------

- Optional -
Add the following lines to your "weapons.properties" or "weapons_ru.properties" file:

weapons.properties:

#####################################################################
# MiG-7
#####################################################################
MiG-7.default         Default (2x 20mm ShVak)
MiG-7.none            None

#####################################################################
# MiG-11
#####################################################################
MiG-11.default         Default (2x 20mm ShVak)
MiG-11.none            None

------------

skin folders:

MiG-7
MiG-11


IL-2 4.09 Rebels need to have the "AircraftLH Stub Class Mod" installed.
If you don't have it yet, get it here:
http://www.mediafire.com/download/646xifmiikikjqc


 **********************************************************************************
*                                                                                  *
*                                       Credits                                    *
*                                                                                  *
 **********************************************************************************

Credits:
3D and everything else not mentioned below: 101tfs
Cockpit is "stand in" based on other mod cockpits
Slot, Weapons, Flight Model, Cockpit Mod: SAS~Skylla & SAS~Storebror
