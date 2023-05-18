UP3 RC4 Patch Pack v3.03

Contents:
- All fixes listed below
- All feature enhancements listed below
- UP3 RC4 without obfuscation
- SAS Common Utils v1.10
- SFS AutoMount v2
- IL-2 Selector 3.5.0

Included feature enhancements:
- Replication of aircraft control surfaces movement over the net and through NTRK recordings
- Replication of pilot head movement over the net and through NTRK recordings
- Animated AI pilot heads, according to pilot's task (watch wing leader, watch enemy etc.)
- Screenshots taken in native code, much faster than before
- "Stabs4All" included, can be activated through conf.ini (*)
- New DiffFM included, capable of reading different FM file versions, less intrusive
- I-16 Planes can hook to TB-3 SPB bombers in flight.
  Hook Parameters on Ground: Distance to hook point max. 5m
  Hook Parameters in Air: Distance to hook point max. 15m, speed difference max. 30km/h, flight vector difference max. 25 degrees
- G4M-2 and Ohka flying bomb can be hooked to each other on ground now (max. distance to hook point: 50m)
- Ju-88 Mistel and 109/190 fighter aircraft can be hooked to each other on ground now (max. distance to hook point: 50m) (needs further testing)
- Torpedo drop parameters shown in command menu (default: Tab key)
- Torpedo water impact values shown in HUD log on impact
- Implemented "smooth" limits for torpedo drop parameters.
  When the drop limits (entry speed, impact angle) are exceeded, the torpedo doesn't break immediately,
  but with a probability according to the proportion of the exceedance vs. the original limits
- WYSIWYG:
  New "smooth" Torpedo Engagement Limits,
  Random Bomb, Rocket, Ordnance drop spread,
  bouncing bombs on water (skip-bombing),
  Detonation Delay (Bomb fuze) settings and
  Random Explosion Radii get replicated over the net, so that everyone sees what you see
  (and your NTRK playbacks always look the same like original as well)
- AI Gunners are slightly better again when being on Veteran or Ace level
- Enhanced Cheater protection
- Flak Burst Flash Light Effects by SAS~Skylla
- PA_Jeronimo improved engine cowling for ME-262V3
- New AI code from TD's Il-2 Patch 4.13.2 implemented
- New TrackIR code from TD's Il-2 Patch 4.11+ available optionally
- Network File Transfer Boost setting available, makes skin transfers about 4 times faster
- New "Mods" GUI for various conf.ini settings, no need to manually edit conf.ini for those settings anymore
- New optional setting to add default "None" country to missions where it's missing by default
- New Fiat CR.32 3D model
- New Moon Textures by TT
- Automatic Track Recording on demand (can be activated in new Mods GUI)
- Cycling through externals shows player planes first, AI and static cameras follow behind
- Additional nightly darkness disabled by default, can be re-enabled (and adjusted) by user in Mod GUI if this is desired
- Nightly moonlight effect enhanced by default, can be disabled (and adjusted) by user in Mod GUI if this is desired
- Auto Admin (for FBDj Admins) and Auto User (for reserved names in Online Gameplay) added to Mod GUI
- User can select specific Username for Online Gameplay, e.g. to override the default Hyperlobby Username
- Skin Download Notifications can be enabled/disabled in Mod Settings
- Bomb Fuze (arming time) can be set in arming screen
- New AC-47 Minigun Mod by SAS~Skylla
- Enhanced WrfGr.21 explosion damage Mod by SAS~Skylla
- IL-2 1941 Fieldmod Gunner Cockpit by Loku
- Enhanced damage code, explosion effects and subammo release code for German "AB" bomb containers
- Enhanced bomb trajectory for german SD2 / SD4 / B2.2EZ bomblets
- 10 seconds minimum delay implemented between subsequent Net Mission Spawns to avoid multi-spawn-crashes
- "Semi Self Illumination" backported from 4.13.4 and enhanced for UP3
- Enhanced "Ordnance View" mode implemented - this is the "Ordnance View" from 4.13 as it should always have been!
- "Camera Mod" implemented
- Optional upgraded 3D and Textures for default Ordnance (use JGSME to activate, courtesy of VPMedia)
- MXY-7 Ohka causes realistic damage on impact
- SPB (TB-3, I-16 Type 5 and 24) improved, including automatic docking mechanism
- Enhanced Weapon Release Control mod by SAS~Skylla integrated
- SB-2 M-100 and M-103 playable nose gunner station added
- Animated FOV mod included as optional JSGME Module
- Enhanced FPS Display Mod included as optional JSGME Module
- Nvidia Screenmode Mod included as optional JSGME Module
- Enhanced Time Compression Mod included as optional JSGME Module
- Widescreen Mod ("Ecran Wide") included as optional JSMGE Module

Included fixes:
- Bf 109 Series Cockpit Code fix
- Bf 109 G14 Hotfix
- Early 109 Hotfix
- Invincible 20mm Flak Hotfix
- Jet Booster Hotfix
- Ju-87C Hotfix
- Net Max Lag Hotfix
- Objects Hotfix Pack 01
- SM.79 and Cant Z.1007 Gunner Hotfix
- Sniper Gunner Hotfix
- Swordfish Spawn Hotfix
- Typhoon Update Fix (new 3D, fully skinnable)
- Fixed very old online bug that caused wrong visual animation of engine states
  (with 2 or more engines)
- Fixed "Stabs4All" bug preventing that functionality to work on most systems
- Fixed Cockpit bug which showed wrong meshes after repairing the cockpit from
  damage using RRR function, applies to Bf 109 B/C/D/E/F, Fi-156, Ju-87D/G
- Fixed Head Movement bug where, when the head is turned to either side, pitching
  the head up/down got misinterpreted to tilting it left/right when viewed from
  externals
- Fixed a Mission Builder Bug where erratically airspawns were created when the
  mission contained "Standalone" Home Bases, but no single "Standalone" Spawn Point
- Fixed pairwise shooting X-4 missiles on Me 410 D
- Logging relieved from surplus content
- TBM-1 Torpedo loadout fixed
- Me-163 Flight Model from 4.13 implemented to get rid of the lame duck FM which existed up to 4.10
- Bomb rotation axis fixed, was set to a Null-Vector in UP3 (??)
- Replaced the partly predictable and reproduceable "pseudo" random number generator static initialization, now "Random" _IS_ random.
- Initial static random "0" seed for bombs fixed
- Pe-2 wounded gunner bug fixed
- Further fix for gunner position switch bug, reducing log output and defaults to player previous army setting now
- Missing Dead Meshes fixed for following pacific buildings: Flag_ArmeeRouge, Flag_LW, Flag_RAF, Flag_US, Flag_VVS
- Missing Dead Meshes fixed for Train "Platform1_Dmg"
- Disappearing Bombs/Torpedos/Rockets bug fixed
  (this was an old IL-2 Core game bug where Bombs/Torpedos/Rockets would simply disappear instead of being dropped/shot online or during NTRK playback)
- VAP bomb (IL-2 incendiary cluster bomb) rearming fixed, based on hotfix by SAS~Skylla from 22 Sep 2015
- FM2 rocket trigger fixed, now all rockets can be fired with rocket trigger key (previously the last pair had to be fired using bomb trigger key)
- Invincible Opel Maultier Flak fixed
- Do-217 crew position locked up in belly gunner position fixed
- Pairwise Hs-293 and Fritz-X drop on Do-217K-2 fixed
- SBD gunner envelope limits fixed
- B-29 gunner positions fixed
- Fokker DXXI compass fixed
- H8K outer frame parts appearing in cockpit view when plane is damaged: Fixed
- I-16 SPB "Drone" picks up fuel from mothership until it reaches 100%: Fixed, will refuel up to initial mission start limit only
- Enemy planes become available on player homebase not only after (belly/emergency) landing, but even if they crashed there: Fixed
- On maps with more than 256 bridges, when a human player destroys a bridge with index 256 or bigger, another bridge with that index modulo 256 will be destroyed instead: Fixed
- MDS/RRR: other players will only see one rocket being rearmed although you have (and see) all: Fixed
- MDS/RRR: other players will not see any bomb/droptank/torpedo rearming at all: Fixed
- MDS/RRR: other players will see an empty loadout when loadout options are changed: Fixed
- Missing Catapults and arrestor wires on german carriers "Graf Zeppelin" and "Peter Strasser" fixed
- Non-functional arrestor wires on german carriers in online mode fixed
- Bf-109T-1 flight model fixed (inherited from Bf-109E-7/N like in real life now, with larger wing span and increased weight)
- Bf-109T-1 nose over after unhooking from arrestor wires fixed
- Invincible H8K "Emily" fixed
- I-16 Type 24 guns separated on trigger 1 (cowling guns) and trigger 2 (wing guns) (by SAS~Skylla)
- Me-262V3 engine damage on ground start fixed
- Me-262V3 flight model fixed (*)
- Me-262V3 muzzles, cannon bulges, nose gear door seams, gun camera, shell and cartridge exhaust bays removed
- Mistel Hotfix
- FW 190 Schusszaehler Fix
- Bailout Speed Fix (players can bail out at any time now, but risk to get hurt when speed is too high)
- Explosion Fix (gets rid of the big explosion meshes on ground after large bomb detonations even if smaller bombs or actors crash later on)
- IJN Ryujo fixed
- A nasty IL-2 bug has been fixed that prevented IL-2 to actually take the month and day from a mission date into account
- Right Alt (Alt Gr) key works in Chat etc. now
- Wheels turn when plane starts to roll, not just when it rolls with more than 5 km/h
- AI doesn't proceed to further Ground Attack waypoints anymore when there's nothing left to attack with
- AI avoids collisions also when flying heavy planes, as long as they're not much heavier than the plane they're about to collide with.
  In the latter case it's the smaller plane's responsibility to avoid the collision.
- FM2 weapon hooks fixed, bombs and rockets separated and bomb loadout options corrected
- Hs-123 patched by Loku
- TBF/TBM Gunsight fixed by Loku
- F2A series flight model update by Loku
- A6M2-N "Rufe" and Blenheim Nav light fixes by Loku
- A6M Engine Texture update by Loku
- Fw-190 Schusszähler Fix by SAS~Skylla
- Gyro Gunsight Patch for F-51D30NA, Fw-190D-9 late, P-47D late, P-51D20/25/30 NA/NT, P-80, Spitfire Mk.16 & LF.Mk.14, Ta-152C-1, Ta-183 by SAS~Skylla
- Fw-189 gunner patch by SAS~Skylla
- Random Radio Broadcast patch by SAS~Skylla
- CR.32 Patch by Gubi
- Texture file loading bug (core game bug since day one) fixed
- TBF/TBM Gunsight Fix by Loku
- P-40E/P-40M Cockpit Fuel Gauge Fix by Loku
- "Request Runway Lights" command restored (some menu structures had to be changed to achieve this)
- Ballistics bug fixed where the radicand of a square root calculation could become zero, causing JVM 1.3.1 to create page faults in memory
- HookView bug fixed where no minimum object radius was taken into consideration (or at least: Not correctly) when switching to Ordnance View
- BombGunNull/BombNull bug fixed
- Old/New TrackIR code compatibility bug fixed
- P-40M Flight Model fixed, vertical stabilizer and rudder arm values adjusted according to the lengthened fuselage
- MBR-2 top gunner fixed
- SB-2 M-100 and M-103 Top and Ventral gunner fixed
- Cant Z-1007 bis Top gunner fixed
- All bomb/cargo/flare loads with parachutes fixed
- YAK-7 Center of Gravity issues fixed
- YAK-7 Cockpit location fixed
- When a house's dead mesh is not available, the game should proceed normally in case the house gets "killed" (without visual representation of the kill though)

(*) Me-262V3 flight model:
    Like in real life, you have to tap the brakes in order to get the 262V3 off the ground.
    Let the plane accelerate to 180km/h, tap the brakes until the tail comes up and you will soon get airborne automatically.
    Remember to move the throttle gently below 6000rpm.
    VERY GENTLY!
    This plane is equipped with very early stages of the Jumo 004 jet engines which are still under development.
    The specified spool up time from idle to full throttle is ==>> 40 Seconds <<==
    This means that e.g. when using keyboard for throttle, you're allowed to increase throttle by one step (=5%) every 2 seconds only !!
    You might be lucky and manage to spool up within half of that time (20 seconds) but we cannot guarantee that the engines will survive it.
    If you spool up any faster, your engines will go up in flames, mark these words!
    The same applies when you throttle back: GENTLY!
    You can do it quicker than throttling up, actually about twice as fast, but you still have to do it GENTLY once you fall below 6000rpm.
    That much for the bad news about the engines, the good news is that above 6000rpm you can move the throttle as quick as you like.
    Watch your RPMs!
    
    Note: * Trim setting for ground start is "full up".
            It will be applied automatically on ground start, however if you have a joystick axis attached for elevator trim,
            remember to trim up all the way when you start on ground, otherwise you risk to nose over when you tap the brakes.
          * Don't let the plane accelerate beyond 200 km/h without lifting the tail.
            Otherwise eventually the wings might lift the plane into the air (especially with low fuel load),
            but you have no elevator authority yet since they're still wing alee: -> Crash.
          * Don't tap the brakes too soon when you try to lift the tail.
            If you do so below ~160km/h, the tail will come up, but since you don't have sufficient elevator authority yet, your plane will nose over.
    
    ATTENTION: If you have installed PA_Jeronimo's Engine Cowling Mod, please deactivate the Me-262V-3 folder inside of that mod, otherwise
               Your engine will get damage once started on ground, see deactivation details below.

Additionally implemented Debugging Switches:
You can adjust log.lst debugging by setting the following values in the conf.ini [Mods] section:

DEBUG_ZSM_BUILDER=<0/1>    ZutiSupportMethods_Builder enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_ZSM_RESM=<0/1>       ZutiSupportMethods_ResourcesManagement enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_ZSM=<0/1>            ZutiSupportMethods enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_RRR=<0/1>            Repair, Refuel and Rearm enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_ZSM_NET=<0/1>        ZutiSupportMethods_Net enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_ZSM_NETRECEIVE=<0/1> ZutiSupportMethods_NetReceive enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_NETAIRCRAFT=<0/1>    NetAircraft enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_HOUSE=<0/1>          House enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_HIERMESH=<0-3>       HierMesh enhanced debugging, default=0, 0=disable, 1=enable, 2=enable detailed error report, 3=extremely detailed (handle with care!)
DEBUG_USER=<0/1>           User enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_TORPEDO=<0/1>        Torpedo enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_ZSM_GAMEORDER=<0/1>  ZutiSupportMethods_GameOrder enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_ZSM_ENGINE=<0/1>     ZutiSupportMethods_Engine enhanced debugging, default=0, 0=disable, 1=enable
DEBUG_GEAR=<0-2>           Gear enhanced debugging, default=0, 0=disable, 1=enable, 2=detailed, useful for carrier takeoff mod debugging
DEBUG_NETAIRCRAFT=<0/1>    NetAircraft debugging, default=0, 0=disable, 1=enable, useful e.g. for weapon trigger replication
DEBUG_BOMB=<0/1>           Bomb debugging, default=0, 0=disable, 1=enable, useful e.g. for Bomb Fuze / Delay replication (also for Torps and Rockets)

Additionally implemented Logfile Switches:
You can adjust log.lst Date/Time output by setting the following values in the conf.ini [Mods] section:

LogDate=<0/1>              Date output in log.lst, default=0, 0=disable, 1=enable
LogMilliseconds=<0/1>      Milliseconds in time output in log.lst, default=0, 0=disable, 1=enable
LogTicks=<0/1>             "Tick Delta" output in log.lst, default=0, 0=disable, 1=enable, this is the time difference between two log lines in game's internal "Ticks"

********************

Installation:
1.) Run JSGME.exe from your UP3 RC4 game folder and disable any of the following previously installed mods(packs), if applicable:
    * Any old Hotfix Pack (* see below)
    * Any old Patch Pack (e.g. "#UP#_Patch_Pack_302")
    * UP3 RC4 without obfuscation ("#UP#_Unobfuscated_MDS_for_UP3"), because this is integrated in this patch pack already
2.) Check your enabled JSGME Mods, now there should only be:
    * #UP#_Enable_UP.3.0
    * Additional JSGME Mods not related to Fixes, Patches, unobfuscated UP etc.
3.) Extract this Patch Pack to your UP3 RC4 game folder
4.) Run jsgme.exe and activate this mod ("#UP#_Patch_Pack_303") and any other module of your choice

********************

(*) Deactivation / Deinstallation of previously installed Hotfixes and/or Hotfix Packs:
Previously installed Hotfixes and Hotfix Packs have to be deactivated from within JSGME.
The Hotfix Packs appear like this in JSGME, watch out for them and deactivate them if they appear to be activated:
- #UP#_Hotfix_Pack_v1.00
- #UP#_Hotfix_Pack_v1.01

The Hotfixes appear like this in JSGME, watch out for them and deactivate them if they appear to be activated:
- #UP#_Early_109_Hotfix_for_UP3
- #UP#_Jet_Booster_Hotfix_for_UP3
- #UP#_Ju87C_Hotfix_for_UP3
- #UP#_SM.79_and_Cant_Z.1007_Gunner_Hotfix_for_UP3
- #UP#_Sniper_Gunner_Hotfix_for_UP3

The Bf 109 G14 Hotfix installs directly into the #UP# folder, please remove or deactivate that folder:
- UP3 Bf 109G-14 Hotfix

Several other Hotfixes installed directly into the #UP# folder are to be removed or deactivated as well:
- UP3 ##MISTEL FIX##
- Fw-190_Schusszaehler_fix_v1.3
- UP3 ##BAILOUT SPEED FIX##
- UP3 ##EXPLOSIONS FIX##
- 0_F2A_series_update
- 0_up_fix_Hs-123_update
- Hawks_GearDoorNOTMod
- FM-2 Loadout Fix
- FW 190 Schusszähler Fix
- !Flak Burst Flash
- TB3_G4M2_JU88_Parasite_Fix
etc...


IMPORTANT: If you have PA_Jeronimo's Engine Cowling Mod installed in your game, please make sure to Rename the folder
           3do/plane/Me-262V-3
           inside that mod folder (e.g. the path in your game might be like "#UP#\Fixed cowling engine & props\3do\plane\Me-262V-3") to
           3do/plane/-Me-262V-3
           or simply remove the "Me-262V-3" of it.
           If you don't do so, your engines will die on ground start.