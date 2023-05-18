package com.maddox.il2.game;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.ZutiSupportMethods.ZutiNetPlaceSearchResult;
import com.maddox.il2.gui.GUI;
import com.maddox.il2.gui.GUINetAircraft;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Cockpit;
import com.maddox.il2.objects.air.CockpitGunner;
import com.maddox.il2.objects.air.CockpitPilot;
import com.maddox.il2.objects.air.NetGunner;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;

public class ZutiSupportMethods_Multicrew {
    public static boolean CAN_REQUEST_COCKPIT_CHANGE = true;

    // ----------------------------------------------------------
    public static class CrewTableItem_CompareByName implements Comparator {
        public int compare(Object o1, Object o2) {
            GUINetAircraft.Item oo1 = (GUINetAircraft.Item) o1;
            GUINetAircraft.Item oo2 = (GUINetAircraft.Item) o2;

            if (oo1 == null || oo2 == null) return 0;

            return oo1.zutiGetUniqueId().compareTo(oo2.zutiGetUniqueId());
        }
    }
    // ----------------------------------------------------------

    /**
     * Reset class variables
     */
    public static void resetClassVariables() {
        ZutiSupportMethods_Multicrew.CAN_REQUEST_COCKPIT_CHANGE = true;
    }

    /**
     * Eject player from his position on the plane that he is in. Old cockpit autopilot is also turned on.
     */
    public static void ejectPlayerGunner() {
        if (World.isPlayerGunner()) {
            Aircraft playerAc = World.getPlayerAircraft();
            NetGunner gunner = World.getPlayerGunner();
            int cockpitNr = gunner.zutiGetCockpitNum();

            // Set autopilot ON!
            ZutiSupportMethods_Air.setGunnerAutopilotOn();

            Paratrooper paratrooper = ZutiSupportMethods_Multicrew.ejectCrewFromCockpit(playerAc, cockpitNr, false, false);

            if (paratrooper != null) World.doPlayerParatrooper(paratrooper);
        }
    }

    /**
     * This method releases current occupied gunner cockpit, requsts new net place (-1) and destroys current cockpit.
     */
    public static void releaseGunnerPosition_unused() {
        try {
            if (!(com.maddox.il2.game.Main3D.cur3D().cockpitCur instanceof CockpitGunner)) return;

            // Set autopilot ON!
            ZutiSupportMethods_Air.setGunnerAutopilotOn();

            // Release gunner position
            if (Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight() && World.isPlayerGunner()) ((NetUser) NetEnv.host()).requestPlace(-1);

            // Destroy current cockpit
            ((CockpitGunner) com.maddox.il2.game.Main3D.cur3D().cockpitCur).destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Call this method when player was either kicked as gunner, when gunner enters briefing screen or his pilot left the game. Player must not be in the game, floating around.
     *
     * @param showBriefingScreen: set this to true if you want briefing screen to be visible
     */
    public static void releaseGunnersPositionAndShowBriefing_unused(boolean showBriefingScreen) {
        if (!(com.maddox.il2.game.Main3D.cur3D().cockpitCur instanceof CockpitGunner)) return;

        // Set autopilot ON!
        ZutiSupportMethods_Air.setGunnerAutopilotOn();

        // Release gunner position
        if (Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight() && World.isPlayerGunner()) ((NetUser) NetEnv.host()).requestPlace(-1);

        // Save statistics
        ((com.maddox.il2.net.NetUser) com.maddox.rts.NetEnv.host()).sendStatInc();
        com.maddox.sound.AudioDevice.soundsOff();

        // World.getPlayerAircraft().destroy();
        com.maddox.il2.game.Main3D.cur3D().cockpitCur.focusLeave();

        if (showBriefingScreen) {
            // Change GUI to briefing screen - activate mouse and keyboard
            GUI.activate(true, false);
            com.maddox.il2.game.Main.stateStack().change(40);
        }
    }

    /**
     * Call this method when new multi crew plane spawns or is destroyed. In such event net places are changed because aircraft order is changed.
     *
     * @param netUser
     * @param acName
     * @param currentCockpitNr
     */
    public static void changeNetPlace_unused(String acName) {
        try {
            if (Main3D.cur3D() != null) {
                int currentCockpitNr = Main3D.cur3D().cockpitCurIndx();
                if (currentCockpitNr > -1) {
                    NetUser currentUser = (NetUser) NetEnv.host();
                    if (currentUser != null) {
                        int newNetPlace = ZutiSupportMethods_Multicrew.getNetPlaceFromAircraftCockpit(acName, Main3D.cur3D().cockpitCurIndx());
                        System.out.println("ZutiSupportMethods - changeNetPlace. User: " + currentUser.uniqueName() + ", acName:" + acName + ", old netPlace: " + currentUser.getPlace() + ", new netPlace: " + newNetPlace);
                        currentUser.requestPlace(newNetPlace);
                        System.out.println("----------------------------");
                    }
                }
            }
        } catch (ClassCastException ex) {
            // happens on DServer
        }
    }

    /**
     * Call this method to get new net place for a player that changed his cockpit. that need to be EXACTLY the same method call order!) as in GUINetAircraft! If it would not be the net order would be different!
     *
     * @param acName
     * @param cocpitId
     * @return
     */
    public static int getNetPlaceFromAircraftCockpit(String acName, int cocpitId) {
        // System.out.println("Getting new net place based on: AC=" + acName + ", cockpitNr=" + cocpitId);

        int netPlace = 0;

        ZutiNetPlaceSearchResult result = browseNetPlaces(acName, cocpitId, 1, netPlace);
        if (!result.wasNetPlaceFound) result = browseNetPlaces(acName, cocpitId, 2, result.foundNetPlace);

        if (result.wasNetPlaceFound) // System.out.println(" RETURNING NET PLACE " + result.foundNetPlace);
            return result.foundNetPlace;
        else // System.out.println(" RETURNING NET PLACE -1");
            return -1;
    }

    private static int NET_PLACE_INDEX = 0;

    public static void updateNetUsersCrewPlaces() {
        // System.out.println("--------------------------------------------------------------------------------");
        ZutiSupportMethods_Multicrew.NET_PLACE_INDEX = 0;
        updateNetUsersCrewPlaces(1);
        updateNetUsersCrewPlaces(2);
        // System.out.println("--------------------------------------------------------------------------------");
        // TODO: Limit Log output // by Storebror
//		System.out.println("Updated users net places!");
    }

    /**
     * Method goes through ac and their crew and removes those users that are "destroyed" but might stayed identified none the less.
     */
    private static ZutiAircraftCrew cleanCrew(ZutiAircraftCrew crew) {
        Map existingCrew = crew.getCrewMap();
        Map newCrew = new HashMap();

        String userName = null;
        NetUser netUser = null;
        Integer cockpitId = null;
        Iterator iterator = existingCrew.keySet().iterator();
        // Check current crew map for dead crew
        while (iterator.hasNext()) {
            cockpitId = (Integer) iterator.next();
            if (cockpitId != null && cockpitId.intValue() > 0) {
                userName = (String) existingCrew.get(cockpitId);
                netUser = ZutiSupportMethods.getNetUser(userName);
                try {
                    if (netUser != null) if (!netUser.isDestroyed()) newCrew.put(cockpitId, userName);
                    else netUser.requestPlace(-1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        // Clear old crew map
        crew.clearCrewMap();
        // Insert new crew into ac position
        iterator = newCrew.keySet().iterator();
        while (iterator.hasNext()) {
            cockpitId = (Integer) iterator.next();
            userName = (String) newCrew.get(cockpitId);
            if (userName != null) crew.setUserPosition(userName, cockpitId.intValue());
        }

        return crew;
    }

    private static void updateNetUsersCrewPlaces(int army) {
        // System.out.println("======Multicrew AC for Army: " + army + " AC====================");
        int aircraftArmy = -1;
        Actor actor = null;
        Aircraft aircraft = null;

        for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            aircraftArmy = -1;
            actor = (Actor) entry.getValue();

            if (!(actor instanceof Aircraft)) continue;

            if (!actor.isAlive()) continue;

            aircraft = (Aircraft) actor;

            aircraftArmy = aircraft.getArmy();

            if (aircraft.name().endsWith("_0")) {
                // System.out.println("Live player AC found: " + aircraft.name() + ", army: " + aircraftArmy);
                // System.out.println("It's multicrew settings: " + aircraft.zutiIsPlaneMultiCrew() + ", and " + aircraft.zutiIsPlaneMultiCrewAnytime() );
                // Joining of live planes is allowed only if pilot of that plane selected multicrew
                // option for his plane and he has his engines shut off. On the ground. But if player also
                // selected multicrew at any time, then allow joining, well, at all times.
                if (!aircraft.FM.AS.zutiIsPlaneMultiCrew()) continue;
                else if (aircraft.FM.AS.zutiIsPlaneMultiCrew() && (!ZutiSupportMethods.isPlaneStationary(aircraft.FM) || !ZutiSupportMethods_Multicrew.areEnginesOff(aircraft.FM.EI)) && !aircraft.FM.AS.zutiIsPlaneMultiCrewAnytime()) continue;
            }
            // AI Planes
            else if (!Mission.MDS_VARIABLES().zutiMisc_EnableAIAirborneMulticrew && (!ZutiSupportMethods.isPlaneStationary(aircraft.FM) || !ZutiSupportMethods_Multicrew.areEnginesOff(aircraft.FM.EI))) // AI plane. Joining AI plane can be done if plane is
                                                                                                                                                                                                         // on the ground and stationary OR,
                // if mission allows it, if plane is in the air.
                continue;

            // System.out.println("ZutiSupportMethods - ZutiNetPlaceSearchResult: processing plane: " + aircraft.name());
            // System.out.println("----------acArmy: " + aircraftArmy + " vs gunner army: " + army );
            // System.out.println("----------requesting cockpit: " + inCockpitId);

            if (aircraftArmy == army) {
                // System.out.println("Checking crew for aircraft: " + aircraft.name() + ":" + aircraft.toString());
                ZutiAircraftCrew crew = ZutiAircraftCrewManagement.getAircraftCrew(aircraft.name());
                if (crew != null) {
                    crew = ZutiSupportMethods_Multicrew.cleanCrew(crew);

                    int cockpits = crew.getNrOfCockpits();// -1 because crew can not join pilot cockpit
                    // System.out.println("Cockpits for this AC: " + cockpits);
                    cockpits = cockpits - 1; // -1 because pilot cockpit is not selectable
                    // System.out.println("Net places for this ac are from: " + ZutiSupportMethods_Multicrew.NET_PLACE_INDEX + " to " + (ZutiSupportMethods_Multicrew.NET_PLACE_INDEX+cockpits));
                    Map cockpitsAndCrew = crew.getCrewMap();
                    Iterator iterator = cockpitsAndCrew.keySet().iterator();
                    while (iterator.hasNext()) {
                        Integer cockpitId = (Integer) iterator.next();
                        if (cockpitId != null && cockpitId.intValue() > 0) {
                            String userName = (String) cockpitsAndCrew.get(cockpitId);
                            int netIndex = ZutiSupportMethods_Multicrew.NET_PLACE_INDEX + cockpitId.intValue() - 1;
                            // System.out.println(" Cockpit: " + cockpitId + ", crew: " + userName + ", netPosition: " + netIndex);
                            NetUser netUser = ZutiSupportMethods.getNetUser(userName);
                            try {
                                if (netUser != null) // netUser.requestPlace(-1);
                                    netUser.requestPlace(netIndex);
                                // System.out.println("Requesed net place >" + netIndex + "< for user >" + userName + "<");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    ZutiSupportMethods_Multicrew.NET_PLACE_INDEX += cockpits;
                }
                // System.out.println("=====================================================");
            }
        }
    }

    /**
     * Go through all AC that are alive and allow multicrew and calculate new net place for player that requested cockpit change. This needs to be synced across the net.
     *
     * @param sectfile
     * @param acName
     * @param inCockpitId
     * @param army
     * @param counter
     * @return
     */
    private static ZutiNetPlaceSearchResult browseNetPlaces(String acName, int inCockpitId, int army, int counter) {
        int aircraftArmy = -1;
        Actor actor = null;
        String wingName = null;
        Aircraft aircraft = null;
        Class var_class = null;
        Object cockpitObject = null;

        for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            aircraftArmy = -1;
            actor = (Actor) entry.getValue();

            if (!(actor instanceof Aircraft)) continue;

            if (!actor.isAlive()) continue;

            wingName = entry.getKey().toString();
            // This wing name has also appended aircraft position in wing, ie from 0 to 3. Remove that.
            wingName = wingName.substring(0, wingName.length() - 1);

            aircraft = (Aircraft) actor;
            if (!aircraft.FM.AS.zutiIsPlaneMultiCrew()) continue;

            aircraftArmy = aircraft.getArmy();

            if (aircraft.name().endsWith("_0")) {
                // System.out.println("Live player AC found: " + aircraft.name() + ", army: " + aircraftArmy);
                // System.out.println("It's multicrew settings: " + aircraft.zutiIsPlaneMultiCrew() + ", and " + aircraft.zutiIsPlaneMultiCrewAnytime() );
                // Joining of live planes is allowed only if pilot of that plane selected multicrew
                // option for his plane and he has his engines shut off. On the ground. But if player also
                // selected multicrew at any time, then allow joining, well, at all times.
                if (!aircraft.FM.AS.zutiIsPlaneMultiCrew()) continue;
                else if (aircraft.FM.AS.zutiIsPlaneMultiCrew() && (!ZutiSupportMethods.isPlaneStationary(aircraft.FM) || !ZutiSupportMethods_Multicrew.areEnginesOff(aircraft.FM.EI)) && !aircraft.FM.AS.zutiIsPlaneMultiCrewAnytime()) continue;
            }
            // AI Planes
            else if (!Mission.MDS_VARIABLES().zutiMisc_EnableAIAirborneMulticrew && (!ZutiSupportMethods.isPlaneStationary(aircraft.FM) || !ZutiSupportMethods_Multicrew.areEnginesOff(aircraft.FM.EI))) // AI plane. Joining AI plane can be done if plane is
                                                                                                                                                                                                         // on the ground and stationary OR,
                // if mission allows it, if plane is in the air.
                continue;

            // System.out.println("ZutiSupportMethods - ZutiNetPlaceSearchResult: processing plane: " + aircraft.name());
            // System.out.println("----------acArmy: " + aircraftArmy + " vs gunner army: " + army );
            // System.out.println("----------requesting cockpit: " + inCockpitId);

            if (aircraftArmy == army) {
                var_class = aircraft.getClass();
                cockpitObject = Property.value(var_class, "cockpitClass");
                if (cockpitObject != null) {
                    Class[] var_classes = null;
                    int cockpits;
                    if (cockpitObject instanceof Class) cockpits = 1;
                    else {
                        var_classes = (Class[]) cockpitObject;
                        cockpits = var_classes.length;
                    }
                    for (int cockpitId = 1; cockpitId < cockpits; cockpitId++) {
                        // System.out.println("------------------cockpitId: " + cockpitId + " vs inCockpitId: " + inCockpitId);
                        // System.out.println("------------------acName: " + aircraft.name() + " vs inAcName: " + acName);

                        if (cockpitId == inCockpitId && aircraft.name().equals(acName)) // System.out.println("FOUND NET PLACE: " + counter);
                            return new ZutiSupportMethods().new ZutiNetPlaceSearchResult(true, counter);
                        // Increase counter at the end because in GUINetAircraft class, table starts with row 0!
                        counter++;
                    }
                }
            }
        }
        return new ZutiSupportMethods().new ZutiNetPlaceSearchResult(false, counter);
    }

    /**
     * Since enabling bombardier position to gunners requires of creating a "CockpitPilot" object this also means that they have from then on full joystick control. They must not have it so this method disables it if player ac is not of type real fm!
     */
    public static void setJojstickState() {
        if (RTSConf.cur.joy == null) return;

        Aircraft playerAc = World.getPlayerAircraft();
        // System.out.println("ZutiSupportMethods - canControlAcCockpit: trying to access cockpit: " + cockpitNr + " of ac: " + acName);
        if (playerAc != null && playerAc.FM instanceof RealFlightModel) {
            RTSConf.cur.joy.setEnable(true);
            System.out.println("  JOYSTICK ENABLED!");
        } else {
            RTSConf.cur.joy.setEnable(false);
            System.out.println("  JOYSTICK DISABLED!");
        }
    }

    /**
     * For dogfight mode call this method before final switch is made when changing cockpits. This will ensure that cockpit that is being assigned is free!
     *
     * @param acName
     * @param cockpitNr
     */
    public static void requestNextFreeCockpit(String acName, int cockpitNr) {
        try {
            // Original (more or less) IL2 cockpit check
            // -----------------------------------------------------------
            if (!Actor.isValid(World.getPlayerAircraft())) return;
            if (!Mission.isPlaying()) return;
            if (World.isPlayerParatrooper()) return;
            if (Main3D.cur3D().cockpits == null) return;
            if (cockpitNr >= Main3D.cur3D().cockpits.length) return;
            if (World.getPlayerAircraft().isUnderWater()) return;
            Cockpit cockpit = Main3D.cur3D().cockpits[cockpitNr];
            if (!cockpit.isEnableFocusing()) return;
            int i_141_ = cockpit.astatePilotIndx();
            if (World.getPlayerFM().AS.isPilotParatrooper(i_141_)) return;
            if (World.getPlayerFM().AS.isPilotDead(i_141_)) return;
            if (Mission.isNet()) {
                if (Time.current() == 0L) return;
                if (Main3D.cur3D().isDemoPlaying()) return;
            }

            // System.out.println("User >" + ((NetUser)NetEnv.host()).uniqueName() + "< requesting position " + cockpitNr);

            boolean isPilot = World.getPlayerFM() instanceof RealFlightModel;
            ZutiSupportMethods_NetSend.requestNewCockpitPosition(World.getPlayerAircraft().name(), cockpitNr, isPilot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return;
    }

    /**
     * Checks for given Engine Interface if engines are on or off.
     *
     * @param EI
     * @return
     */
    public static boolean areEnginesOff(EnginesInterface EI) {
        int size = EI.engines.length;
        for (int i = 0; i < size; i++)
            if (EI.engines[i].getRPM() > 0) return false;

        return true;
    }

    /**
     * This method will execute eject sequence for all gunners in specified aircraft. This should be called when plane is destroyed or it's pilot bailed out.
     *
     * @param acName
     */
    public static void ejectGunnersForAircraft(String acName) {
        List crew = ZutiAircraftCrewManagement.getAircraftCrew(acName).getCrewList();
        NetUser netuser = null;
        String username = null;
        for (int i = 0; i < crew.size(); i++) {
            username = (String) crew.get(i);
            netuser = ZutiSupportMethods.getNetUser(username);
            if (netuser != null) ZutiSupportMethods_NetSend.ejectPlayer(netuser);
        }

        // Finally, remove AC from aircraft crew management map
        ZutiAircraftCrewManagement.removeAircraft(acName);
    }

    /**
     * Determines if cockpit owner can execute given bombardier or pilot action in specified cockpit.
     *
     * @param aircraft
     * @param controlId
     * @param weaponControl
     * @return
     */
    public static boolean canExecutePilotOrBombardierAction(Aircraft aircraft, int controlId, boolean weaponControl) {
        if (aircraft == null) return false;
        if (aircraft.FM == null) return false;

        if (aircraft.FM instanceof RealFlightModel) return true;
        else if (ZutiSupportMethods.IS_ACTING_INSTRUCTOR || Main3D.cur3D().cockpitCur instanceof CockpitPilot) // We are in Pilot/Bombardier cockpit or are acting as an instructor.
            switch (controlId) {
            case 19:// 19 = weapon3 release (bombs)
            return true;
            case 131:// 131 = bomb bay doors control
            return true;
            // 117 - 125 = bombardier gunsight control
            case 117:// distance+
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), 1, 0, 0, 0, 0);
            return true;
            case 118:// distance-
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), -1, 0, 0, 0, 0);
            return true;
            case 119:// sideslip+
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), 0, 1, 0, 0, 0);
            return true;
            case 120:// sideslip-
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), 0, -1, 0, 0, 0);
            return true;
            case 121:// altitude+
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), 0, 0, 1, 0, 0);
            return true;
            case 122:// altitude-
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), 0, 0, -1, 0, 0);
            return true;
            case 123:// speed+
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), 0, 0, 0, 1, 0);
            return true;
            case 124:// speed-
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), 0, 0, 0, -1, 0);
            return true;
            case 125:// gunsight automation
            if (aircraft.FM instanceof TypeBomber) ZutiSupportMethods_NetSend.bombardierInstrumentsChanged_ToServer(aircraft.name(), 0, 0, 0, 0, 1);
            return true;
            default: {
                // For remaining actions, executor must be pilot or at least acting pilot/instructor
                // System.out.println("ZutiSupportMethods - can execute pilot action?");
                return canExecutePilotAction(aircraft, controlId, weaponControl);
            }
            }
        else return false;
    }

    /**
     * Call this method to determine if caller can execute pilot specific action. This is needed in case we are dealing with instructors in the plane. If instructor calls this method, method will return true AND it will also forward net command to
     * rightful owner of aircraft in question.
     *
     * @param aircraft
     * @param controlId
     * @param weaponControl
     * @return
     */
    private static boolean canExecutePilotAction(Aircraft aircraft, int controlId, boolean weaponControl) {
        if (aircraft == null) return false;

        if (aircraft.FM == null) return false;

        if (aircraft.FM instanceof RealFlightModel) return true;

        if (ZutiSupportMethods.IS_ACTING_INSTRUCTOR && aircraft.name().endsWith("_0")) {
            // System.out.println("AircraftHotKeys - sending pilot action to server...");
            // OK, this is the instructor, return false so no further actions are performed BUT forward
            // information about executed command over the network to ac rightful owner.
            ZutiSupportMethods_NetSend.aircraftControlsChanged_ToServer(aircraft.name(), controlId, weaponControl);
            return true;
        }

        return false;
    }

    /**
     * This method removes all bombs from specified aircraft.
     *
     * @param acName
     * @param weaponId
     * @param weaponBay
     * @param nrOfBullets
     */
    public static void clearAircraftOrdinance(String acName, int weaponId, int weaponBay, int nrOfBullets) {
        // System.out.println("ZutiSupportMethods - clearAcBombLoad for ac >" + acName + "< 1");
        Aircraft aircraft = (Aircraft) Actor.getByName(acName);
        if (aircraft == null) return;
        // System.out.println("ZutiSupportMethods - clearAcBombLoad for ac >" + acName + "< 2");
        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        BombGun bomb = null;
        RocketGun rocket = null;

        try {
            // Covers rockets
            if (weapons[weaponId][weaponBay] instanceof RocketGun) {
                rocket = (RocketGun) weapons[weaponId][weaponBay];

                int result = rocket.countBullets() - nrOfBullets;
                if (result < 0) result = 0;

                rocket.loadBullets(result);
                // System.out.println("ZutiSupportMethods - clearAircraftOrdinance for ac >" + acName + "<. Status of " + weaponId + "-" + weaponBay + " = " + rocket.countBullets());
            }
            // Covers bombs, fuel tanks, torpedoes
            else if (weapons[weaponId][weaponBay] instanceof BombGun) {
                bomb = (BombGun) weapons[weaponId][weaponBay];

                int result = bomb.countBullets() - nrOfBullets;
                if (result < 0) result = 0;

                bomb.loadBullets(result);
                // System.out.println("ZutiSupportMethods - clearAircraftOrdinance for ac >" + acName + "<. Status of " + weaponId + "-" + weaponBay + " = " + bomb.countBullets());
            }
        } catch (Exception ex) {}
    }

    private static void removeCockpitBlister(Aircraft aircraft, Actor pilot, int cockpitNr) {
        String blisterString = "Blister" + cockpitNr + "_D0";
        if (aircraft.hierMesh().chunkFindCheck(blisterString) != -1)// && getPilotHealth(0) > 0.0F)
        {
            aircraft.hierMesh().hideSubTrees(blisterString);
            Wreckage wreckage = new Wreckage((ActorHMesh) pilot, aircraft.hierMesh().chunkFind(blisterString));
            wreckage.collide(false);
            com.maddox.JGP.Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) ((SndAircraft) aircraft).FM).Vwld);
            wreckage.setSpeed(vector3d);

            // System.out.println("ZutiSupportMethods - removeCockpitBlister. Ejected blister nr: " + cockpitNr);
        }
    }

    /**
     * Method determines if certain operation must be synced over the net or not. Syncing must be done when: - player did something on AI plane - player did something on Live plane but he is not that plane owner
     *
     * @param aircraft
     * @return
     */
    public static boolean mustSyncACOperation(Aircraft aircraft) {
        if (aircraft == null) // System.out.println("ZutiSupportMethods - mustSyncAcOperation can not execute because supplied AC is NULL! Returning false.");
            return false;

        if (aircraft.FM instanceof RealFlightModel) // Player released bombs on his own plane, everything is fine, no syncing needed.
            // System.out.println("ZutiSupportMethods - mustSyncAcOperation - Player is in charge of the real thing!");
            return false;

        /*
         * if( Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && aircraft.name().endsWith("_0") ) { //This is another exception to the rule. NO SYNCING IF this is called on SERVER for a plane that //is being controlled by a
         * LIVE player and that player is NOT a host/server! return false; }
         */

        // All other cases require net syncing
        return true;
    }

    /**
     * Method ejects gunner at specified cockpit num for specified aircraft.
     *
     * @param aircraft
     * @param cockpitNr
     * @param removeCockpitBlister
     * @param removeBodyFromPlane
     */
    public static Paratrooper ejectCrewFromCockpit(Aircraft aircraft, int cockpitNr, boolean removeCockpitBlister, boolean removeBodyFromPlane) {
        if (cockpitNr < 0) return null;
        // System.out.println("ejectPlayerFromHisCockpit 1: cockpit nr: " + cockpitNr);

        if (aircraft == null) return null;

        Actor pilot = aircraft.FM.AS.actor;

        if (pilot == null) return null;

        // TODO: disable this because of inconsistencies online. Even though player might eject gunner
        // position AI will take it over and will still shoot.
        if (removeCockpitBlister) ZutiSupportMethods_Multicrew.removeCockpitBlister(aircraft, pilot, cockpitNr);

        if (removeBodyFromPlane) aircraft.doRemoveBodyFromPlane(cockpitNr);

        try {
            // System.out.println("zutiExecuteLocalTasksForPlayerGunner 2: searching for external para hook...");
            Hook hook = pilot.findHook("_ExternalBail0" + cockpitNr);
            if (hook != null) {
                // System.out.println("zutiExecuteLocalTasksForPlayerGunner 3: hook for paratrooper found! Creating paratrooper...");
                Loc loc = new Loc(0.0D, 0.0D, 0.0D, World.Rnd().nextFloat(-45F, 45F), 0.0F, 0.0F);
                hook.computePos(pilot, pilot.pos.getAbs(), loc);
                Paratrooper paratrooper = new Paratrooper(pilot, pilot.getArmy(), cockpitNr, loc, ((FlightModelMain) ((SndAircraft) aircraft).FM).Vwld);
                // System.out.println("zutiExecuteLocalTasksForPlayerGunner 4: paratrooper created! Setting player as paratrooper...");
                EventLog.onBailedOut(aircraft, cockpitNr);

                return paratrooper;
                // System.out.println("zutiExecuteLocalTasksForPlayerGunner 5: done!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Method returns max cockpits for given aircraft name.
     *
     * @param acName
     * @return
     */
    public static int getMaxCockpitsForAircraft(String acName) {
        Aircraft aircraft = (Aircraft) Actor.getByName(acName);

        if (aircraft == null) return 0;

        Class var_class = aircraft.getClass();
        Object cockpitObject = Property.value(var_class, "cockpitClass");
        int cockpits = 1;
        if (cockpitObject != null) {
            Class[] var_classes = null;
            if (cockpitObject instanceof Class) cockpits = 1;
            else {
                var_classes = (Class[]) cockpitObject;
                cockpits = var_classes.length;
            }
        }

        // System.out.println("ZutiSupportMethods - getMaxCockpitsForAircraft: Aircraft >" + acName + "< has >" + cockpits + "< cockpits");
        return cockpits;
    }

    /**
     * Returns true if speceifed acName relates to AI plane. Else false is returned.
     *
     * @param acName
     * @return
     */
    public static boolean isAiAircraft(String acName) {
        if (acName != null && acName.endsWith("_0")) return true;

        return false;
    }

    /**
     * Returns true if AC is bomber and has bombardier position.
     *
     * @param aircraft
     * @return
     */
    public static boolean hasBombardierCockpit(Aircraft aircraft) {
        // Faster determination
        if (aircraft instanceof TypeBomber) return true;

        return false;

        // More detailed search
        /*
         * Class var_class = aircraft.getClass(); Object cockpitObject = Property.value(var_class, "cockpitClass"); if (cockpitObject != null) { Class[] var_classes = null; int cockpits; if (cockpitObject instanceof Class) cockpits = 1; else { var_classes
         * = (Class[])cockpitObject; cockpits = var_classes.length; }
         *
         * for (int cockpitId = 1; cockpitId < cockpits; cockpitId++ ) { Class cockpit = var_classes[cockpitId]; if( cockpit.toString().indexOf("Bombardier") > -1 ) return true; } }
         *
         * return false;
         */
    }

}