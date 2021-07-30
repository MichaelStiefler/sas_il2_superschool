package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.List;

import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiMDSVariables;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.NetEnv;

public class ZutiSupportMethods_AI {
    public static final int DESPAWN_AC_DELAY            = 4000;// in seconds
    public static final int MAX_SPEED_FOR_DECK_BAILOUT  = 100;
    public static final int MAX_HEIGHT_FOR_DECK_BAILOUT = 50;
    public static int       AC_DESTROY_DELAY            = 5000;

    // TODO: Lutz mod
    public static boolean    bIniTakeOff;
    private static ArrayList AircraftTakeOff;
    private static ArrayList AircraftTrigger;

    /**
     * Remove specified aircraft from specified group.
     *
     * @param ag
     * @param aircraftName
     */
    public static void removeAircraftFromItsAirGroup(AirGroup ag, String aircraftName) {
        if (ag == null || aircraftName == null) return;

        for (int i = 0; i < ag.nOfAirc; i++)
            if (aircraftName.equals(ag.airc[i].name())) {
                // Edited by |ZUTI|
                if (ag.airc[i].FM instanceof Maneuver) ((Maneuver) ag.airc[i].FM).Group = null;

                // System.out.println("Aircraft destroyed: " + airc[i].name());

                for (int i_4_ = i; i_4_ < ag.nOfAirc - 1; i_4_++)
                    ag.airc[i_4_] = ag.airc[i_4_ + 1];
                ag.nOfAirc--;
                break;
            }
        if (ag.grTask == 1 || ag.grTask == 2) ag.setTaskAndManeuver(0);
    }

    /**
     * Method searches for all spawn places inside defined area. If some are found and are not from spawn place place holder object, remove it.
     *
     * @param x
     * @param y
     * @param r
     * @return number of spawn places that were removed
     */
    public static int removeSpawnPlacesInsideArea(double x, double y, float r) {
        double max = r * r;

        // System.out.println("X=" + x + ", Y=" + y + ", r=" + r);

        List allowedSpawnPlaces = new ArrayList();
        Point_Stay[][] points = World.cur().airdrome.stay;
        int originalPointsCount = points.length;
        Point_Stay pointStay = null;
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) continue;

            pointStay = points[i][0];

            if (pointStay.zutiLocation != null) {
                // My spawn place - allow it always
                allowedSpawnPlaces.add(points[i]);
                continue;
            }

            double distance = Math.pow(x - pointStay.x, 2) + Math.pow(y - pointStay.y, 2);
            // System.out.println("Point x=" + pointStay.x + ", y=" + pointStay.y + ", distance=" + Math.round(distance) + " vs " + Math.round(max));
            if (distance > max) // this spawn place can exist
                allowedSpawnPlaces.add(points[i]);
        }

        // Now reset original list
        Point_Stay[][] newStayPointsList = new Point_Stay[allowedSpawnPlaces.size()][];
        for (int i = 0; i < allowedSpawnPlaces.size(); i++)
            newStayPointsList[i] = (Point_Stay[]) allowedSpawnPlaces.get(i);

        World.cur().airdrome.stay = newStayPointsList;

        return originalPointsCount - newStayPointsList.length;
        // System.out.println(" StayHold = " + World.cur().airdrome.stayHold.length + ", result of: " + originalPointsCount + " - " + newStayPointsList.length);
    }

    /**
     * Collect points that you gathered during your flight.
     */
    public static void collectPoints() {
        ((NetUser) NetEnv.host()).sendStatInc();
    }

    // TODO: Lutz Methods
    // ----------------------------------------------------------------------------------------------
    public static void checkGroupTakeOff(AirGroupList[] Groups) {
        if (!bIniTakeOff) {
            IniTakeOff(Groups);
            bIniTakeOff = true;
        }

        int j = java.lang.Integer.parseInt(AircraftTakeOff.get(0).toString());
        int i = AircraftTakeOff.indexOf("*");
        int k = i;
        for (int i1 = 0; i1 < j; i1++) {
            k++;
            int l = java.lang.Integer.parseInt(AircraftTakeOff.get(k).toString());
            for (int j1 = 0; j1 < l; j1++) {
                k++;
                com.maddox.il2.objects.air.Aircraft aircraft = (com.maddox.il2.objects.air.Aircraft) AircraftTakeOff.get(k);
                if (aircraft.isAlive()) ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.bstartmotor = checkTakeOff(aircraft);
            }

        }

    }

    private static boolean checkTakeOff(com.maddox.il2.objects.air.Aircraft aircraft) {
        if (((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.numberOfTrigger == 0) return true;
        for (int i1 = 0; i1 < ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.numberOfTrigger; i1++) {
            if (((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.typeOfTrigger[i1] == -1) {
                float f = ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.timeToTakeOff - com.maddox.il2.ai.World.getTimeofDay();
                if (f > 23.75F) f -= 24F;
                if (f < 0.0F) {
                    ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.numberOfTrigger = 0;
                    return true;
                }
                if (((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.numberOfTrigger == 1) return false;
            }
            if (AircraftTrigger.isEmpty()) return true;
            if (((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.typeOfTrigger[i1] == -2) {
                java.lang.String s = ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.valueOfTrigger[i1];
                int i = AircraftTrigger.indexOf(s);
                i++;
                int k = java.lang.Integer.parseInt(AircraftTrigger.get(i).toString());
                for (int j1 = 0; j1 < k; j1++) {
                    i++;
                    com.maddox.il2.objects.air.Aircraft aircraft1 = (com.maddox.il2.objects.air.Aircraft) AircraftTrigger.get(i);
                    if (((com.maddox.il2.objects.sounds.SndAircraft) aircraft1).FM.bstartmotor) return true;
                }

                if (((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.numberOfTrigger == 1) return false;
            }
            if (((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.typeOfTrigger[i1] > -1) {
                int j = AircraftTrigger.indexOf(((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.valueOfTrigger[i1]);
                j++;
                int l = java.lang.Integer.parseInt(AircraftTrigger.get(j).toString());
                for (int k1 = 0; k1 < l; k1++) {
                    j++;
                    com.maddox.il2.objects.air.Aircraft aircraft2 = (com.maddox.il2.objects.air.Aircraft) AircraftTrigger.get(j);
                    if (checkPosition(aircraft2, ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.typeOfTrigger[i1])) {
                        ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM.numberOfTrigger = 0;
                        return true;
                    }
                }

            }
        }

        return false;
    }

    private static boolean checkPosition(com.maddox.il2.objects.air.Aircraft aircraft, int i) {
        ZutiMDSVariables variables = Mission.MDS_VARIABLES();

        if (((com.maddox.JGP.Tuple3d) ((com.maddox.il2.fm.FlightModelMain) ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM).Loc).z < variables.getZmin(i)) return false;

        if (((com.maddox.JGP.Tuple3d) ((com.maddox.il2.fm.FlightModelMain) ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM).Loc).z > variables.getZmax(i)) return false;

        if (java.lang.Math.abs(((com.maddox.JGP.Tuple3d) ((com.maddox.il2.fm.FlightModelMain) ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM).Loc).x - variables.getXcenter(i)) > variables.getLenghtZone(i)) return false;

        return java.lang.Math.abs(((com.maddox.JGP.Tuple3d) ((com.maddox.il2.fm.FlightModelMain) ((com.maddox.il2.objects.sounds.SndAircraft) aircraft).FM).Loc).y - variables.getYcenter(i)) <= variables.getWidthZone(i);
    }

    private static void IniTakeOff(AirGroupList[] Groups) {
        AircraftTakeOff = new ArrayList();
        AircraftTrigger = new ArrayList();
        AircraftTakeOff.add(new Integer(0));
        for (int k = 0; k < 2; k++) {
            int l = com.maddox.il2.ai.air.AirGroupList.length(Groups[k]);
            for (int j1 = 0; j1 < l; j1++) {
                com.maddox.il2.ai.air.AirGroup airgroup = com.maddox.il2.ai.air.AirGroupList.getGroup(Groups[k], j1);
                if (((com.maddox.il2.objects.sounds.SndAircraft) airgroup.airc[0]).FM.numberOfTrigger > 0) {
                    int i = java.lang.Integer.parseInt(AircraftTakeOff.get(0).toString()) + 1;
                    AircraftTakeOff.set(0, new Integer(i));
                    java.lang.String s = ((com.maddox.il2.engine.Interpolate) ((com.maddox.il2.objects.sounds.SndAircraft) airgroup.airc[0]).FM).actor.name();
                    s = s.trim();
                    s = s.substring(0, s.length() - 1);
                    AircraftTakeOff.add(new String(s));
                }
            }

        }

        int j = java.lang.Integer.parseInt(AircraftTakeOff.get(0).toString());
        if (j == 0) return;
        AircraftTakeOff.add(new String("*"));
        for (int i1 = 0; i1 < 2; i1++) {
            int k1 = com.maddox.il2.ai.air.AirGroupList.length(Groups[i1]);
            for (int i2 = 0; i2 < k1; i2++) {
                com.maddox.il2.ai.air.AirGroup airgroup1 = com.maddox.il2.ai.air.AirGroupList.getGroup(Groups[i1], i2);
                for (int l2 = 0; l2 < airgroup1.nOfAirc; l2++) {
                    if (((com.maddox.il2.objects.sounds.SndAircraft) airgroup1.airc[0]).FM.numberOfTrigger > 0 && l2 == 0) AircraftTakeOff.add(new Integer(airgroup1.nOfAirc));
                    if (((com.maddox.il2.objects.sounds.SndAircraft) airgroup1.airc[l2]).FM.numberOfTrigger > 0) AircraftTakeOff.add(airgroup1.airc[l2]);
                }
            }
        }

        for (int l1 = 0; l1 < 2; l1++) {
            int j2 = com.maddox.il2.ai.air.AirGroupList.length(Groups[l1]);
            for (int k2 = 0; k2 < j2; k2++) {
                com.maddox.il2.ai.air.AirGroup airgroup2 = com.maddox.il2.ai.air.AirGroupList.getGroup(Groups[l1], k2);
                for (int i3 = 0; i3 < ((com.maddox.il2.objects.sounds.SndAircraft) airgroup2.airc[0]).FM.numberOfTrigger; i3++)
                    if (!((com.maddox.il2.objects.sounds.SndAircraft) airgroup2.airc[0]).FM.valueOfTrigger[i3].equals("delay") && !AircraftTrigger.contains(((com.maddox.il2.objects.sounds.SndAircraft) airgroup2.airc[0]).FM.valueOfTrigger[i3]))
                        addRefTrigger(((com.maddox.il2.objects.sounds.SndAircraft) airgroup2.airc[0]).FM.valueOfTrigger[i3], Groups);

            }
        }
    }

    private static void addRefTrigger(java.lang.String s, AirGroupList[] Groups) {
        for (int k = 0; k < 2; k++) {
            int l = com.maddox.il2.ai.air.AirGroupList.length(Groups[k]);
            for (int i1 = 0; i1 < l; i1++) {
                com.maddox.il2.ai.air.AirGroup airgroup = com.maddox.il2.ai.air.AirGroupList.getGroup(Groups[k], i1);
                for (int j1 = 0; j1 < airgroup.nOfAirc; j1++) {
                    java.lang.String s1 = ((com.maddox.il2.engine.Interpolate) ((com.maddox.il2.objects.sounds.SndAircraft) airgroup.airc[j1]).FM).actor.name();
                    java.lang.String s2 = s1.substring(0, s1.length() - 1);
                    if (s.equalsIgnoreCase(s2)) if (AircraftTrigger.contains(s)) {
                        int i = AircraftTrigger.indexOf(s);
                        i++;
                        int j = java.lang.Integer.parseInt(AircraftTrigger.get(i).toString()) + 1;
                        AircraftTrigger.set(i, new Integer(j));
                        AircraftTrigger.add(airgroup.airc[j1]);
                    } else {
                        AircraftTrigger.add(s);
                        AircraftTrigger.add(new Integer(1));
                        AircraftTrigger.add(airgroup.airc[j1]);
                    }
                }
            }
        }
    }
    // ----------------------------------------------------------------------------------------------

}