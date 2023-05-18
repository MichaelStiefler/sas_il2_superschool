package com.maddox.il2.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.NetAircraft.AircraftNet;
import com.maddox.rts.NetEnv;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class ZutiAircraftCrewManagement {
    private static Map AIRCRAFT_CREW = new HashMap();

    /**
     * Clears AIRCRAFT_CREW map.
     */
    public static void resetMainMap() {
        AIRCRAFT_CREW.clear();
    }

    /**
     * Method returns aircraft crew object for given aircraft. If such object does not
     * exist, new one is created.
     * 
     * @param acName
     * @return
     */
    public static ZutiAircraftCrew getAircraftCrew(String acName) {
        ZutiAircraftCrew result = null;
        if (AIRCRAFT_CREW != null) {
            result = (ZutiAircraftCrew) AIRCRAFT_CREW.get(acName);
        }

        if (result == null) {
            // crew for given AC does not yet exist, create it
            result = new ZutiAircraftCrew(acName);
            AIRCRAFT_CREW.put(acName, result);
        }

        return result;
    }

    /**
     * This method removes aircraft and it's crew information from AIRCRAFT_CREW map.
     * 
     * @param acName
     */
    public static void removeAircraft(String acName) {
//      System.out.println("----------------------ZutiAircraftCrewManagement - Removing AC: " + acName);

        // TODO: Storebror: Track Users hitting refly where this shouldn't be possible
        NetUser netuser = null;
        NetAircraft netaircraft = null;
        boolean isSuspicious = false;
        if (NetEnv.isServer()) {
            netaircraft = (NetAircraft) Actor.getByName(acName);
            do {
                if (netaircraft == null)
                    break;
                netuser = ((AircraftNet) (netaircraft.net)).netUser;
                if (netuser == null)
                    break;
//                netuser.addSuspiciousPreReflyAddonInfo("----------------------ZutiAircraftCrewManagement - Removing AC: " + acName);
                System.out.println("----------------------ZutiAircraftCrewManagement - Removing AC: " + acName);
                netuser.clearSuspiciousPreReflyAddonInfo();
                netuser.addSuspiciousPreReflyAddonInfo("netaircraft = " + netaircraft.name());
                netuser.addSuspiciousPreReflyAddonInfo("user = " + netuser.uniqueName());
                boolean isAircraftValid = Actor.isValid(netaircraft);
                netuser.addSuspiciousPreReflyAddonInfo("Is valid = " + isAircraftValid);
                if (!isAircraftValid)
                    break;
                boolean isAircraftAlive = netaircraft.isAlive();
                netuser.addSuspiciousPreReflyAddonInfo("Is alive = " + isAircraftAlive);
                if (!isAircraftAlive)
                    break;
                netuser.addSuspiciousPreReflyAddonInfo("Takeoff_N_Landing = " + World.cur().diffCur.Takeoff_N_Landing);
                if (!World.cur().diffCur.Takeoff_N_Landing)
                    break;
                boolean isPilotDead = netaircraft.FM.AS.isPilotDead(0);
                netuser.addSuspiciousPreReflyAddonInfo("Is Pilot dead = " + isPilotDead);
                byte astateBailoutStep = netaircraft.FM.AS.astateBailoutStep;
                netuser.addSuspiciousPreReflyAddonInfo("astateBailoutStep = " + astateBailoutStep);
                boolean isPilotParatrooper = netaircraft.FM.AS.isPilotParatrooper(0);
                netuser.addSuspiciousPreReflyAddonInfo("Is Pilot Paratrooper = " + isPilotParatrooper);
                boolean isAircraftOnGround = netaircraft.FM.Gears.onGround();
                netuser.addSuspiciousPreReflyAddonInfo("Is on ground = " + isAircraftOnGround);
                boolean isAircraftStandingStill = netaircraft.FM.getSpeedKMH() < 10F;
                netuser.addSuspiciousPreReflyAddonInfo("Speed (kmh)= " + netaircraft.FM.getSpeedKMH());
                boolean isAircraftOnChocks = netaircraft.FM.brakeShoe;
                netuser.addSuspiciousPreReflyAddonInfo("Chocks engaged = " + isAircraftOnChocks);
                boolean isAircraftGearDamaged = netaircraft.FM.Gears.isAnyDamaged();
                netuser.addSuspiciousPreReflyAddonInfo("Gear damaged = " + isAircraftGearDamaged);
                boolean bDamagedGround = Reflection.getBoolean(netaircraft.FM, "bDamagedGround");
                netuser.addSuspiciousPreReflyAddonInfo("bDamagedGround = " + bDamagedGround);
                boolean isReflyOverride = NetAircraft.ZUTI_REFLY_OWERRIDE;
                netuser.addSuspiciousPreReflyAddonInfo("Refly override = " + isReflyOverride);
                boolean isLandedOnWater = false;
                Point3d pos = netaircraft.pos.getAbsPoint();
                if (pos != null && netaircraft.FM.getSpeedKMH() < 100F && netaircraft.FM.getAltitude() < 50F && World.land().isWater(pos.x, pos.y))
                    isLandedOnWater = true;
                netuser.addSuspiciousPreReflyAddonInfo("Landed on water = " + isLandedOnWater);
                
                if (isReflyOverride)
                    break;
                if (isAircraftOnGround && isAircraftStandingStill)
                    break;
                if (isPilotDead)
                    break;
                if (isPilotParatrooper)
                    break;
                if (astateBailoutStep > 10)
                    break;
                if (isAircraftOnChocks)
                    break;
                if (isAircraftGearDamaged && isAircraftOnGround)
                    break;
                if (isLandedOnWater)
                    break;
                if (bDamagedGround)
                    break;
                netuser.addSuspiciousPreReflyAddonInfo("This could become a suspicious refly attempt, current time is " + Time.current());
                netuser.setLastSuspiciousPreRefly(Time.current());
                isSuspicious = true;
                
                netuser.addSuspiciousPreReflyAddonInfo("*********************************************************************************");
                netuser.addSuspiciousPreReflyAddonInfo("*                 additional Aircraft/Player State Information                  *");
                netuser.addSuspiciousPreReflyAddonInfo("*********************************************************************************");
                
                netuser.addSuspiciousPreReflyAddonInfo("isCapableOfACM = " + netaircraft.FM.isCapableOfACM());
                netuser.addSuspiciousPreReflyAddonInfo("isCapableOfBMP = " + netaircraft.FM.isCapableOfBMP());
                netuser.addSuspiciousPreReflyAddonInfo("isCapableOfTaxiing = " + netaircraft.FM.isCapableOfTaxiing());
                netuser.addSuspiciousPreReflyAddonInfo("isReadyToDie = " + netaircraft.FM.isReadyToDie());
                netuser.addSuspiciousPreReflyAddonInfo("isReadyToReturn = " + netaircraft.FM.isReadyToReturn());
                netuser.addSuspiciousPreReflyAddonInfo("isTakenMortalDamage = " + netaircraft.FM.isTakenMortalDamage());
                netuser.addSuspiciousPreReflyAddonInfo("isStationedOnGround = " + netaircraft.FM.isStationedOnGround());
                netuser.addSuspiciousPreReflyAddonInfo("isCrashedOnGround = " + netaircraft.FM.isCrashedOnGround());
                netuser.addSuspiciousPreReflyAddonInfo("isNearAirdrome = " + netaircraft.FM.isNearAirdrome());
                netuser.addSuspiciousPreReflyAddonInfo("isCrossCountry = " + netaircraft.FM.isCrossCountry());
                netuser.addSuspiciousPreReflyAddonInfo("isWasAirborne = " + netaircraft.FM.isWasAirborne());
                netuser.addSuspiciousPreReflyAddonInfo("isSentWingNote = " + netaircraft.FM.isSentWingNote());
                netuser.addSuspiciousPreReflyAddonInfo("isSentBuryNote = " + netaircraft.FM.isSentBuryNote());
                netuser.addSuspiciousPreReflyAddonInfo("isSentControlsOutNote = " + netaircraft.FM.isSentControlsOutNote());
                netuser.addSuspiciousPreReflyAddonInfo("bDamaged = " + Reflection.getBoolean(netaircraft.FM, "bDamaged"));
                netuser.addSuspiciousPreReflyAddonInfo("flags0 = " + Reflection.getLong(netaircraft.FM, "flags0"));
                
                dumpArray(netuser, netaircraft.FM.AS.astateBleedingNext, "astateBleedingNext contents:");
                dumpArray(netuser, netaircraft.FM.AS.astateBleedingTimes, "astateBleedingTimes contents:");
                dumpArray(netuser, netaircraft.FM.AS.astateEngineStates, "astateEngineStates contents:");
                dumpArray(netuser, netaircraft.FM.AS.astateOilStates, "astateOilStates contents:");
                dumpArray(netuser, netaircraft.FM.AS.astatePilotStates, "astatePilotStates contents:");
                dumpArray(netuser, netaircraft.FM.AS.astateSootStates, "astateSootStates contents:");
                dumpArray(netuser, netaircraft.FM.AS.astateTankStates, "astateTankStates contents:");
                
                netuser.addSuspiciousPreReflyAddonInfo("astateCockpitState = " + netaircraft.FM.AS.astateCockpitState);
                netuser.addSuspiciousPreReflyAddonInfo("astatePlayerIndex = " + netaircraft.FM.AS.astatePlayerIndex);
                netuser.addSuspiciousPreReflyAddonInfo("bIsAboutToBailout = " + netaircraft.FM.AS.bIsAboutToBailout);
                netuser.addSuspiciousPreReflyAddonInfo("bIsEnableToBailout = " + netaircraft.FM.AS.bIsEnableToBailout);
                netuser.addSuspiciousPreReflyAddonInfo("bShowSmokesOn = " + netaircraft.FM.AS.bShowSmokesOn);
                netuser.addSuspiciousPreReflyAddonInfo("bNavLightsOn = " + netaircraft.FM.AS.bNavLightsOn);
                netuser.addSuspiciousPreReflyAddonInfo("bLandingLightOn = " + netaircraft.FM.AS.bLandingLightOn);
                netuser.addSuspiciousPreReflyAddonInfo("bWingTipLExists = " + netaircraft.FM.AS.bWingTipLExists);
                netuser.addSuspiciousPreReflyAddonInfo("bWingTipRExists = " + netaircraft.FM.AS.bWingTipRExists);
                netuser.addSuspiciousPreReflyAddonInfo("bIsAboveCriticalSpeed = " + Reflection.getBoolean(netaircraft.FM, "bIsAboveCriticalSpeed"));
                netuser.addSuspiciousPreReflyAddonInfo("bIsAboveCondensateAlt = " + Reflection.getBoolean(netaircraft.FM, "bIsAboveCondensateAlt"));
                netuser.addSuspiciousPreReflyAddonInfo("bIsOnInadequateAOA = " + Reflection.getBoolean(netaircraft.FM, "bIsOnInadequateAOA"));
                netuser.addSuspiciousPreReflyAddonInfo("legsWounded = " + Reflection.getBoolean(netaircraft.FM, "legsWounded"));
                netuser.addSuspiciousPreReflyAddonInfo("armsWounded = " + Reflection.getBoolean(netaircraft.FM, "armsWounded"));
            } while (false);
            if (!isSuspicious && netuser != null)
                netuser.clearSuspiciousPreReflyAddonInfo();
        }
        // ---

        if (AIRCRAFT_CREW != null && AIRCRAFT_CREW.containsKey(acName)) {
            AIRCRAFT_CREW.remove(acName);
            if (netaircraft != null && netaircraft.isNetPlayer()) // Patch Pack 107, reduce logging, don't show removed AI planes
                System.out.println("ZutiAircraftCrewManagement - removeAircraft: Aircraft removed >" + acName);
        }
    }
    
    // TODO: Storebror: Track Users hitting refly where this shouldn't be possible
    private static final long REFLY_TRACKING_TIME   = 300000L; // 5 Minutes
    
    public static void dumpArray(long[] theArray, String theMessage) {
        System.out.println(theMessage);
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<theArray.length; i++) {
            if (i>0) sb.append(", ");
            sb.append(theArray[i]);
        }
        System.out.println(sb.toString());
    }

    public static void dumpArray(NetUser netuser, long[] theArray, String theMessage) {
        netuser.addSuspiciousPreReflyAddonInfo(theMessage);
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<theArray.length; i++) {
            if (i>0) sb.append(", ");
            sb.append(theArray[i]);
        }
        netuser.addSuspiciousPreReflyAddonInfo(sb.toString());
    }

    public static void dumpArray(byte[] theArray, String theMessage) {
        System.out.println(theMessage);
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<theArray.length; i++) {
            if (i>0) sb.append(", ");
            sb.append(theArray[i]);
        }
        System.out.println(sb.toString());
    }

    public static void dumpArray(NetUser netuser, byte[] theArray, String theMessage) {
        netuser.addSuspiciousPreReflyAddonInfo(theMessage);
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<theArray.length; i++) {
            if (i>0) sb.append(", ");
            sb.append(theArray[i]);
        }
        netuser.addSuspiciousPreReflyAddonInfo(sb.toString());
    }

    public static boolean isSuspiciousRefly(byte byte0, NetUser netuser) {
        if (!NetEnv.isServer()) return false;
        if (byte0 != EventLog.REFLY) return false;
        if (netuser.getLastSuspiciousPreRefly() == 0L) return false;
        if (Time.current() < netuser.getLastSuspiciousPreRefly()) return false;
        if (Time.current() - netuser.getLastSuspiciousPreRefly() > REFLY_TRACKING_TIME) return false;
        ArrayList suspiciousPreReflyAddonInfo = netuser.getSuspiciousPreReflyAddonInfo();
        if (RTSConf.cur.console.getEnv().existAtom("reflykick", true)) {
            if (RTSConf.cur.console.getEnv().atom("reflykick").toString().trim().equalsIgnoreCase("on")) {
                
                System.out.println(" ****************************************************************************************************************************************** ");
                System.out.println("********************************************************************************************************************************************");
                System.out.println("**                                                                                                                                        **");
                System.out.println("**                                                   +++ !!! REFLY KICK !!! +++                                                           **");
                System.out.println("**                                                                                                                                        **");
                System.out.println("********************************************************************************************************************************************");
                System.out.println(" ****************************************************************************************************************************************** ");
                System.out.println();
                System.out.println("Time: " + Time.current());
                System.out.println("ZutiAircraftCrewManagement isSuspiciousRefly(" + byte0 + ", " + netuser.uniqueName() + ") = true and reflykick is on, player will be kicked!");
                System.out.println();
                for (int i=0; i<suspiciousPreReflyAddonInfo.size(); i++) {
                    System.out.println(suspiciousPreReflyAddonInfo.get(i));
                }
                System.out.println();
                System.out.println(" ****************************************************************************************************************************************** ");
                System.out.println("********************************************************************************************************************************************");
                System.out.println("**                                                                                                                                        **");
                System.out.println("**                                                   --- !!! REFLY KICK !!! ---                                                           **");
                System.out.println("**                                                                                                                                        **");
                System.out.println("********************************************************************************************************************************************");
                System.out.println(" ****************************************************************************************************************************************** ");
                return true;
            }
        }
        System.out.println("************************************");
        System.out.println("*     +++ SUSPICIOUS REFLY +++     *");
        System.out.println("************************************");
        System.out.println();
        System.out.println("ZutiAircraftCrewManagement isSuspiciousRefly(" + byte0 + ", " + netuser.uniqueName() + ") = true but reflykick isn't on, player will stay!");
        System.out.println();
        for (int i=0; i<suspiciousPreReflyAddonInfo.size(); i++) {
            System.out.println(suspiciousPreReflyAddonInfo.get(i));
        }
        System.out.println();
        System.out.println("************************************");
        System.out.println("*     --- SUSPICIOUS REFLY ---     *");
        System.out.println("************************************");
        return false;
    }
    // ---

}