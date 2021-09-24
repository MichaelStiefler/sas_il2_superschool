 **********************************************************************************
*                                                                                  *
*                             SAS Loadout Lister 4.09+                             *
*                                                                                  *
 **********************************************************************************

 **********************************************************************************
*                                                                                  *
*                                   Installation:                                  *
*                                                                                  *
 **********************************************************************************

Extract this file and copy the "Loadout Lister 4.09+" folder to your game's
modded files folder. This is a subfolder of your IL-2 1946 game folder, the name
differs depending on the modpack you're using, for instance the folder name could be
"#SAS", "#WAW3", "#DBW", "#UP#" or just "MODS".

 **********************************************************************************
*                                                                                  *
*                                       Usage:                                     *
*                                                                                  *
 **********************************************************************************

1.) Run the game and start any mission (e.g. some random QMB mission)
2.) Open up the console (Shift+Tab)
3.) Enter the following commands (case sensitive):

preload LL [mode]

[mode] is an optional parameter, consisting of a binary combination of the
following flags:
* PADDED = 0x1      if set, adds padding between weapons slot and readable name
* CLASSNAME = 0x2   if set, outputs readable name even if it equals slot name
* SORTED = 0x4      if set, sorts the list by aircraft key name, e.g. "Yak-9U"

When the console prompt returns, you can continue playing the game or exit.
The list of current plane weapon slot declarations will be present in a file...

weapons.properties

...right in your IL-2 game folder.

Credits:
- 1C/Maddox for IL-2 1946 base game
- SAS Team for this mod

Sourcefile included with this mod.
Released under the "DWTFYWWI" license.
Don't be a dick: If you use this mod for your own projects or further work on
this mod, give credits accordingly and share your source like we did!