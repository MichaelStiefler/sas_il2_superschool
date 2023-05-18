Ultrapack 3.3 Bigfoot + Patch 6

Installation:

Download all parts of the installer (5 in total).
Run the installer ("Ultrapack 3.3 Bigfoot.part1.exe") and let the installer do it's job.
At the end, JSGME will open up. "#UP#_Enable_UP3.3" (that's Ultrapack 3.3 "Bigfoot") will be enabled automatically and you can choose additional JSGME Options as you like.
NOTE: Activating #UP#_Enable_UP3.3 takes LOOOOOOOOOOOOOOOOOOONG time as it contains lots of files.
      JSGME might seem non-resonding but it isn't. Really. 10 Minutes? Easily. Wait. Be patient.
ATTENTION: Do _NOT_ enable "#UP#_Enable_UP3.0" and/or "#UP#_Enable_UP3.2", there's no need to anymore, it's outdated.

Next, Download Ultrapack 3.3 Bigfoot Patch 6, run the installer (Ultrapack 3.3 Bigfoot Patch 6.exe) and let it do it's job.
At the end, JSGME will open up and attempt to enable "#UP#_Enable_UP3.3_Patch_06".
Since this Patch overwrites existing files, you will see a warning message.
Please click "Yes" on that warning message.
NOTE: Activating #UP#_Enable_UP3.3_Patch_06 takes LOOOOOOOOOOOOOOOOOOONG time as it contains lots of files.
      JSGME might seem non-resonding but it isn't. Really. 10 Minutes? Easily. Wait. Be patient.

Credits:
- 1C/Maddox/Ubisoft
- Team Dildos (for nothing, actually)
- Ultrapack dev team
- SAS Team
- FAC (The Flying Ass Clowns)
- CFC (Chuffy's Flying Circus)
- gio963tto, IES Team
- For detailed credits concerning aircraft added to this pack, please refer to BAT:
  https://www.sas1946.com/main/index.php/board,264.0.html
  That's where they came from :-)

Changelog:

Ultrapack 3.3 Patch 8
=====================
* Triggers backported from HSFX 7.0.3
* F8F pilot and prop skins fixed (Thanks to macgiver for reporting this issue)
* SB-2 missing cockpit textures fixed by Loku
* SB-2 M 103 stuck turret fixed
* Quick Mission Builder compatibility patch: Runs old QMB missions with 8 flights only now as well
* QMB Missions Pack added
* Net replication issue fixed where long lines in mission files could exceed netmessage length limits accidentally
* TrgInfantry implemented





Ultrapack 3.3 Patch 7
=====================
* Flight Model return value for Roll Accelleration fixed (online multiplayer plane bug related fix)
* Black Screen Bug fixed for multiplayer planes online
* Surplus debug log code removed from InterpolateAdapter class
* P-47 Bubble Top Tail Sway toned down
* N1K1 prop rotation direction fixed
* Me-264 "America Bomber" added
* Do-24 backflip on runway spawn fixed (thanks to Ghost129er for reporting this issue)
* R-5 missing damage materials fixed (thanks to Ghost129er for reporting this issue)
* He-51 tilted 3D model fixed (thanks to Ghost129er for reporting this issue)
* He-51 gunsight, cockpit and pilot head position fixed (thanks to Ghost129er for reporting this issue)
* U-2 missiong damage materials fixed (thanks to Ghost129er for reporting this issue)
* U-2 surplus pilot head mesh fixed (thanks to Ghost129er for reporting this issue)
* U-2VS default loadout bug fixed (thanks to Ghost129er for reporting this issue)
* Sikorsky H-19 and HRS-3 main rotor sound and 3D model location fixed by Loku

Ultrapack 3.3 Patch 6
=====================
* Nakajima G10N1 Fugaku Flight Model fixed
* Countermeasure applied agains log flooding for various irrelevant reasons (DirectX warnings and the like)
* New Flight Model Parameters for max. speed with extracted gears (rip-off speed) and flaps (jam speed) added
* Flap jam speed defaults to max. flaps speed from existing flight model parameters now if max. flaps speed is higher than 300 km/h
* Formation spacing fine tuned
* AI controls input clipped (ported from EngineMod 2.8.17w)

Ultrapack 3.3 Patch 5
=====================
* Nakajima G10N1 Fugaku incl. static plane added
* Polikarpov P-5 incl. static plane added
* Polikarpov S.S.S. incl. static plane added
* Tupolev SB 2M-103 Series 96 incl. static plane added
* Tupolev SB 2M-103A Series 221 incl. static plane added
* Bloch MB.152 static plane added
* Ki-15 I/II static planes added
* Polikarpov R-5 / R-5 (skis) updated to 4.14.1 standards
* Tupolev SB 2M-100A Series 41 / SB 2M-103 Series 201 updated to 4.14.1 standards
* Polikarpov U-2 series updated to 4.14.1 standards
* Petlyakov Pe-8 / Tupolev TB-7 updated to 4.14.1 standards
* Martin B-10B, B-10B on floats, WH-1, WH-2 and WH-3 added including static planes
* P-47 missing frag bomb textures added
* Net Aircraft are being removed if the last net update is more than 10 seconds past the last flight model update.
  This is supposed to fight the so called "Ghost Plane" issue.
  The feature can be disabled by setting "destroyGhostPlanes=0" in conf.ini [Mods] section.
* Missing Royal Navy "f" type decals added for Swordfish, Fulmar etc.
* New Ingame Menu implemented, needs user key assignment for "Toggle Game Menu" in the "VIEWS" section:
  "Smooth view", "Inertia View" and "actor selection from menu" backported from PAL VisualMOD 9

Ultrapack 3.3 Patch 4
=====================
* Localized plane.properties reworked by Mick, sniperton, Loku, Dimlee, Pivoyvo
* New Nakajima B5N2 "Kate" 4.12.2 model, extracted by SAS~Epervier, brought to UP3 by SAS~Loku
* Bloch MB.152 by Ranwers added
* Ki-15 I/II "Babs" by Giotto added
* Tu-2 Flight Model, 3D and Java fixes by SAS~Loku
* Missing Cockpit Textures on Fw 190 A-1 added
* Missing Gear Texture for Fairey Battle in conjunction with Jeronimo's wheels mod added
* New SAS Common Utils 1.12 implemented
* Log shows PC's local time now
* Several netcode issues fixed
* Zuti User disconnection error fixed
* Symbolic Link awareness implemented (e.g. for sharing Plane Skins among different models)
* Online Skin size limiter (only indexed 8bpp max. "SD" (max. 1024x1024 pixel) skins can be used online) reworked
* Opening Canopies added for P-47 Pack
* Planes spawning on the ground do so with open canopy (if opening canopy is available)
* Planes spawning on an aircraft carrier do so with folded wings (if wingfold is available)
* Rocket boost net replication reworked from scratch

Ultrapack 3.3 Patch 3
=====================
* New Tiger Moth 3D by Ranwers, brought to UP3 by SAS~Loku
* Blenheim prop size corrected and finnish default skins added (SAS~Loku)
* Hawk-75 French/British default skins added, engine and props corrected (SAS~Loku)
* MS.406/410 french and "multi" default skins added (SAS~Loku)
* Finflash added to P-51A RAF default skin (SAS~Loku)
* updated 3D IAR-80/81 (source unknown, UP3 import SAS~Loku)
* desert skin for HS-129B-2 (SAS~Loku)
* New SAS Common Utils 1.12 included
* P-47 log flooding fixed
* P-47DT game lockup when prop breaks fixed
* "Override default Rearm/Refuel/Repair settings" on homebases now also respects homebase specific "... only if airport has ..." settings
* Fairey Barracuda Flight model tuned once more, now taking off from carriers is possible without having to use catapults
* Gladiator 1 and 2 missing canopy added to cockpit view
* Cheater Protection enhancements
* Do-17 engine startup log flooding / missing effects fixed
* air.ini sort order reworked (SAS~Loku)
* Some typos fixed in german aircraft names (Pivoyvo)
* Hungarian translation of aircraft names (sniperton)
* Russian/Polish/Czech/French preliminary translation of aircraft names (Google Translator)
* Russian language files added from Stock game. If you set "locale=ru" in the [rts] section of conf.ini, your game should speak russian to you in most places
* When playing online, player can select standard aircraft skins (512x512 and 1024x1024 pixel, 256 colors indexed) only. True color and/or HD skins will not be listed
* Server will reject any non-standard aircraft skins and send chat message to users trying to select one

Ultrapack 3.3 Patch 2
=====================
* Fixed Super Corsair (F2G) Prop visual glitch when standing still / rotating slowly
* Gladiator J8A moved from japanese plane list to "others" - Finland
* Lysander game crash/lockup fixed
* Cal .50 sounds restored
* Missing maps entries in all.ini added
* P-39 Fuel Pressure warning light fixed, blue light activated when gears are down
* Hs 123 tail wheel steering direction fixed
* Map should not draw dead ground targets anymore
* Plane properties files in languages other than english adjusted according to UP3.3 naming standards
* About 1.500 Java Code issues, most of them in Stock game code, of various severity... all fixed
* Cheater Protection enhancements
* Fairey Barracuda Flight Model reworked
* Henschel Hs-123 Flight Model reworked
* Fiat CR.32 Flight Model reworked
* Fiat CR.42 Flight Model reworked
* New function added to substitute inexistent loadout options from old missions with closest matching existent one
* Stock game code glitch fixed which keeps AI from recognizing certain attacking planes (*wink* TD, you still have that glitch in 4.14.1)
* Ju 188 3D updates applied

Ultrapack 3.3 Patch 1
=====================
* Updated Fairey Battle 3D model (Plane + Cockpit) and Java by SAS~Loku
* Typos fixed in plane.properties (YP-80 and G3M), U.S. codenames for japanese planes added
* Loadout names of F4U-4/4B/5/5N and AU-1 shortened (they exceeded the server's 255 character limit)
* Potez Engine Models fixed (caused game crashes before)
* CR.42 moved from "others" to "italian" in air.ini
* MS.406/410 moved from "others" to "french" in air.ini

Ultrapack 3.3
=============
* PZL 11 & 24 A/B/E/F/G Flight Model Updates (SAS~Loku)
* Fw 190 A/D & Ta 152 Flight Models slightly toned down
* Lockheed Electra Flap lift significantly reduced
* Bf 109 G-6 1.3 ATA from UP3 WIP added back to the set
* Hurricane Mk.Ia Forward Air Controller from UP3 WIP added back to the set
* Spitfire P.R. Mk. XI from UP3 WIP added back to the set
* RATO / Starthilferaketen: Code glitches fixed, effect changed, sound added
* New Aircraft added to Ultrapack:
  Aichi E13A1 "Jake"
  Aichi M6A Seiran
  B-24D 1941
  B-24D-140-CO 1942
  B-25A
  B-25A late
  B-25B
  B-25B Doolittle Raiders
  B-26B
  Beaufort Mk.I early
  Beaufort Mk.I late
  Beaufort Mk.II
  Bell P-39N-1 Airacobra Fieldmod
  Bell P-39Q-30 Airacobra
  Blohm&Voss Bv-138 Fieldmod
  Blohm&Voss Bv-138 Minesweeper
  Blohm&Voss Bv-138B
  Blohm&Voss Bv-222C
  Boulton Paul Defiant Mk.I
  CA-12 Boomerang
  CA-13 Boomerang
  Caproni Ca.309
  Caproni Ca.310
  Caproni Ca.311
  Caproni Ca.311M
  Curtiss P-40F Warhawk
  Curtiss P-40K "Kittybomber"
  Curtiss P-40L Warhawk
  Curtiss P-40N Warhawk
  Curtiss SB2C-1C Helldiver
  Curtiss SB2C-3 Helldiver
  Curtiss SB2C-4 Helldiver
  Curtiss SB2C-5 Helldiver
  Dewoitine D.520 C1
  Dewoitine D.520 S
  Do-17 Z-0
  Do-17 Z-2
  Do-217 E-2
  Do-217 M-1
  Dornier Do-18
  Dornier Do-24K
  Dornier Do-24N
  Dornier Do-24T
  Dornier Do-26
  Douglas A-24 Banshee
  Douglas A-24A Banshee
  Douglas A-24B Banshee
  Douglas Dauntless Mk.I
  Douglas SBD-4 Dauntless
  Douglas SBD-5 Dauntless
  Douglas TBD-1 Devastator
  FZG-76 V-1
  Fairey Albacore
  Fairey Albacore ASV
  Fairey Barracuda Mk.II
  Fairey Firefly FR.I
  Fairey Firefly Mk 5
  Focke-Wulf Fw 190 A-1
  Fokker D.XXIII
  Fokker G.I
  G3M2-11 Nell
  G3M2-22 Nell
  G4M2a Betty
  Goodyear F2G Corsair
  Grumman F7F-3 Tigercat
  Grumman F7F-3N Tigercat
  Grumman F8F-2 Bearcat
  H5Y1 Cherry
  H6K5 Mavis
  Halifax Mk.I
  Halifax Mk.II
  Halifax Mk.III
  Hampden Mk.I
  He 111 A
  He 111 B
  He 111 E
  He 111 H-1
  He 111 H-11
  He 111 H-16
  He 111 H-20
  He 111 H-22
  He 111 H-3
  He 111 H-5
  He 111 H-6 Early
  He 111 H-6 Late
  He 111 H-8
  He 111 P-2 Late
  He 111 P-4
  He 177-A3
  He 177-A5
  Heinkel He 115
  Henschel Hs 126 A-1
  Henschel Hs 126 A-1 with Spats
  Henschel Hs 126 K-6
  Ju-188 A-2
  Ju-388 K
  Ju-86 E
  Ju-88 A-1
  Ju-88 A-13
  Ju-88 A-4 Late
  Ju-88 A-5
  Ju-88 A-5 Late
  Ju-88 A-6
  Ju-88 C-1
  Ju-88 C-2
  Ju-88 C-6b
  Ju-88 G-6
  Ju-88 G1 Stechm�cke
  Ju-88 G1 Zerst�rer
  Ju-88 P-1
  Ju-88 R-2
  Kawasaki Ki-45 Toryu "Nick" Kai Hei
  Kawasaki Ki-45 Toryu "Nick" Kai Ko
  Kawasaki Ki-45 Toryu "Nick" Kai Otsu
  Kawasaki Ki-45 Toryu "Nick" Kai Tei
  Ki-46 II Dinah
  Ki-48-I Lily
  Ki-48-II KAI Kamikaze
  Ki-48-IIa Lily
  Ki-48-IIb Lily
  Ki-49-II Helen
  Lancaster "Upkeep"
  Lancaster III Updated
  Liberator GR.V
  Manchester I
  Manchester I early
  Manchester Ia
  Mitsubishi F1M "Pete"
  Mitsubishi Ki-30A "Ann"
  Mitsubishi Ki-51-I "Sonia"
  Mitsubishi Ki-67 Hiryu "Peggy"
  P-47 Thunderbold Mk.I
  P-47 Thunderbold Mk.II
  P-47 Thunderbold Mk.III
  P-47B "Double Twister"
  P-47B-1
  P-47C-5
  P-47D-30
  P-47D-40
  P-47M
  P-47N-15
  P1Y-1 Ginga
  PB4Y-2 Privateer
  Pe-2 Series 11
  Pe-2 Series 115
  Pe-2 Series 22
  Pe-2 Series 244
  Pe-2 Series 265
  Pe-2 Series 354
  Pe-2 Series 402
  Pe-2 Series 87
  Potez 63.11
  Potez 630
  Potez 631
  Potez 633
  Seversky P-35
  Short Sunderland
  Stirling Mk.I
  Stirling Mk.III
  Vought AU-1 Corsair
  Vought F4U-4 Corsair
  Vought F4U-4B Corsair
  Vought F4U-5 Corsair
  Vought F4U-5N Corsair
  Vought OS2U Kingfisher
  Vought SB2U-Vindicator
  Wellesley MkI
  Wellington Mk.III
  Wellington Mk.VIII
  Westland Lysander Mk.I
  Westland Lysander Mk.II
  Westland Lysander Mk.III
  Westland Lysander Mk.III "Special Duties"
  Westland Lysander Target Tug
  Whirlwind Mk.II
  Whitley Mk.V
  Whitley Mk.VII
  YB-40 Escort Fortress
  Yermolayev Yer-2
  Yokosuka D4Y2 Suisei "Judy"
  Yokosuka D4Y2-S Suisei "Judy"
  Yokosuka D4Y3 Suisei "Judy"
  Yokosuka D4Y3-S Suisei "Judy"
* Several features backported from 4.13 and 4.14
* Cross version functionality improvements
* General Code cleanup  

Ultrapack 3.2 (Patch 5)
=============
* PZL 24 added (SAS~Loku)
* PZL 11, 23 and 42 3D Updates (SAS~Loku)
* Fw 190 + Ta 152 Family Flight Model Rework
* Gloster Gladiator Family Flight Model Rework
* Henschel Hs 123 Flight Model Rework
* Fiat CR.32 / CR.42 Flight Model Rework
* Several other small glitches fixed

Ultrapack 3.2 (Patch 4)
=============
* Added planes: Avro Anson, Lockheed Electra, Lockheed Hudson (I/III), MiG-15 (early Farmer version)
* Added Weapons: Tu-4 nuke and KS-1 missile, MiG-17PF mixed missile loadout (launching IR+Radar missiles in pairs)
* Complete Missile code and parameters reworked
* Opening Canopy net replication changed
* Sabre/MiG Flight adjustments
* Fw-190 gunsight dimmer fixed
* Sabre G-Force indicator fixed
* Lots of other less significant minor tweaks


##################################################################################################################

Old readme from last Patch Pack (303) below:

Contents:
- All fixes listed below
- All feature enhancements listed below
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
- Fw-190 Schussz�hler Fix by SAS~Skylla
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
InstantLog=<0/1/2>         Instant Logging support. "0" uses Stock IL-2 Logging, "1" uses old (slow) Instant Logging, "2" uses new "Piped" Instand Logging (available since PP 303)

********************

Installation:
1.) Download the file
2.) Run the installer
3.) At the end, JSGME will open up. UP3 + Patch Pack will be enabled automatically and you can choose additional JSGME Options as you like.

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
- FW 190 Schussz�hler Fix
- !Flak Burst Flash
- TB3_G4M2_JU88_Parasite_Fix
etc...


IMPORTANT: If you have PA_Jeronimo's Engine Cowling Mod installed in your game, please make sure to Rename the folder
           3do/plane/Me-262V-3
           inside that mod folder (e.g. the path in your game might be like "#UP#\Fixed cowling engine & props\3do\plane\Me-262V-3") to
           3do/plane/-Me-262V-3
           or simply remove the "Me-262V-3" of it.
           If you don't do so, your engines will die on ground start.