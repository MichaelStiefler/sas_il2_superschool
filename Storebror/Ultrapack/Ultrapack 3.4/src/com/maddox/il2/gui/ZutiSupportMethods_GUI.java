package com.maddox.il2.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.gwindow.GPoint;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Target;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiTargetsSupportMethods;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiPadObject;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.game.ZutiSupportMethods_Multicrew;
import com.maddox.il2.gui.GUIBriefing.TargetPoint;
import com.maddox.il2.gui.GUINetAircraft.Item;
import com.maddox.il2.gui.GUINetServerMisSelect.FileMission;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.CockpitPilot;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.trains.Train;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.NetEnv;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.sound.AudioDevice;
import com.maddox.util.NumberTokenizer;

public class ZutiSupportMethods_GUI {
    public static long      ZUTI_LAST_CREW_RESYNC   = 0;
    public static boolean   ZUTI_CRATERS_SYNCED     = false;
    private static boolean  ZUTI_TARGETS_LOADED     = false;
    private static String[] ZUTI_TIP                = new String[3];
    private static List     DROP_DOWN_SERVERS_LIST  = null;
    public static GameState GAME_STATE              = new GameState(0);
    public static List      STD_HOME_BASE_AIRFIELDS = null;

    // ----------------------------------------------------------
    static class Missions_CompareByFileName implements Comparator {
        public int compare(Object o1, Object o2) {
            FileMission oo1 = (FileMission) o1;
            FileMission oo2 = (FileMission) o2;

            if (oo1 == null || oo2 == null) return 0;

            return oo1.fileName.toLowerCase().compareTo(oo2.fileName.toLowerCase());
        }
    }
    // ----------------------------------------------------------

    /**
     * Reset class variables
     */
    public static void resetClassVariables() {
        ZutiSupportMethods_GUI.ZUTI_LAST_CREW_RESYNC = 0;
        ZutiSupportMethods_GUI.ZUTI_CRATERS_SYNCED = false;
        ZutiSupportMethods_GUI.ZUTI_TARGETS_LOADED = false;
        ZutiSupportMethods_GUI.ZUTI_TIP = new String[3];
        ZutiSupportMethods_GUI.STD_HOME_BASE_AIRFIELDS = new ArrayList();
    }

    /**
     * Targets should only be loaded once per mission. If you want to load them more than once, call this method with value = false and then call zutiFillTargets method again.
     *
     * @param value
     */
    public static void setTargetsLoaded(boolean value) {
        ZutiSupportMethods_GUI.ZUTI_TARGETS_LOADED = value;
    }

    /**
     * This method fills zutiPadObjects with new air objects. Duplicates are ignored.
     *
     * @param guiPad
     */
    public static void fillAirInterval(GUIPad guiPad, boolean recheckObjectScoutStatus) {
        if (!Mission.MDS_VARIABLES().zutiIcons_ShowAircraft) return;

        try {
            Actor actor = null;
            ZutiPadObject zo = null;

            Aircraft playerAC = World.getPlayerAircraft();
            boolean refreshIntervalSet = Mission.MDS_VARIABLES().zutiRadar_RefreshInterval > 0;

            // Fill air targets
            List list = Engine.targets();
            Aircraft aircraft = null;
            Integer hash;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                actor = (Actor) list.get(i);

                if (actor instanceof Aircraft) {
                    aircraft = (Aircraft) list.get(i);
                    // Player AC is processed above, skip it here
                    if (!Mission.MDS_VARIABLES().zutiIcons_ShowPlayerAircraft && aircraft.equals(playerAC)) continue;

                    hash = new Integer(actor.hashCode());
                    if (!actor.getDiedFlag()) if (!guiPad.zutiPadObjects.containsKey(hash)) {
                        zo = new ZutiPadObject(actor, refreshIntervalSet);
                        zo.type = 0;
                        guiPad.zutiPadObjects.put(hash, zo);
                    } else if (recheckObjectScoutStatus) {
                        zo = (ZutiPadObject) guiPad.zutiPadObjects.get(hash);
                        zo.updateScoutStatus();
                    }
                }
            }

            printDebugMessage("GUIPad - pad objects: " + guiPad.zutiPadObjects.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Check ground units and adds them to GUIPad array list if they should be processed
     *
     * @param guiPad
     */
    public static void fillGroundChiefsArray(GUIPad guiPad) {
        if (Main.cur() == null) return;

        Mission mission = Main.cur().mission;
        if (mission == null) return;

        if (Mission.MDS_VARIABLES() == null || !Mission.MDS_VARIABLES().zutiIcons_ShowGroundUnits) return;

        boolean radarIntervalSet = Mission.MDS_VARIABLES().zutiRadar_RefreshInterval > 0;

        ZutiPadObject zo = null;
        // this method will collect those ground units that can be detected by scouts. Since
        // ground units can not spawn at time later than 0 we can only check array only few times, say,
        // every time GUIPad is activated? Should provide sufficient cover for new missions

        if (mission.actors == null) return;

        Actor actor = null;

        // HashMapExt hashmapext = Engine.name2Actor();
        // for (Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))

        ArrayList actors = mission.actors;
        if (actors == null || GUI.pad == null || GUI.pad.zutiPadObjects == null) return;

        for (int i = 0; i < actors.size(); i++) {
            // actor = (Actor) entry.getValue();
            actor = (Actor) actors.get(i);

            // TODO: Storebror: Fix Exception when actor is null
            if (actor == null) continue;

            if (GUI.pad.zutiPadObjects.containsKey(new Integer(actor.hashCode()))) continue;

            if (!actor.getDiedFlag()) if (actor instanceof TankGeneric) {
                zo = new ZutiPadObject(actor, radarIntervalSet);
                zo.setIcon(ZutiPadObject.ARTILLERY_TANK_ICON);
                zo.type = 1;

                guiPad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
            } else if (actor instanceof Train) {
                zo = new ZutiPadObject(actor, radarIntervalSet);
                zo.setIcon(ZutiPadObject.TRAIN_ICON);
                zo.type = 5;

                guiPad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
            } else if (actor instanceof AAA) {
                zo = new ZutiPadObject(actor, radarIntervalSet);
                zo.setIcon(ZutiPadObject.AAA_ICON);
                zo.type = 2;

                guiPad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
            } else if (actor instanceof ArtilleryGeneric) {
                zo = new ZutiPadObject(actor, radarIntervalSet);
                zo.setIcon(ZutiPadObject.ARTILLERY_TANK_ICON);
                zo.type = 2;

                guiPad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
            } else if (actor instanceof CarGeneric) {
                zo = new ZutiPadObject(actor, radarIntervalSet);
                zo.setIcon(ZutiPadObject.CAR_ICON);
                zo.type = 5;

                guiPad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
            } else if (actor instanceof BigshipGeneric || actor instanceof ShipGeneric) {
                zo = new ZutiPadObject(actor, radarIntervalSet);
                zo.setIcon(ZutiPadObject.SHIP_ICON);
                zo.type = 4;

                guiPad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
                printDebugMessage("SHIP ADDED " + actor.toString());

            } else if (actor instanceof PlaneGeneric) {
                zo = new ZutiPadObject(actor, radarIntervalSet);
                zo.setIcon(ZutiPadObject.AIRCRAFT_ICON);
                zo.type = 0;

                guiPad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
            }
        }
    }

    /**
     * Check all home bases and save those that are "neutral", meaning that their army is not red(1) or blue(2).
     */
    public static void fillNeutralHomeBases(ArrayList zutiNeutralHomeBases) {
        if (zutiNeutralHomeBases == null) zutiNeutralHomeBases = new ArrayList();
        else zutiNeutralHomeBases.clear();

        ArrayList arraylist = World.cur().bornPlaces;

        if (arraylist == null) return;

        int size = arraylist.size();
        BornPlace bp = null;

        for (int i = 0; i < size; i++) {
            bp = (BornPlace) arraylist.get(i);

            // neutral = all that is not red and blue
            if (bp.army != 1 && bp.army != 2) zutiNeutralHomeBases.add(bp);
        }
    }

    /**
     * Remove target point from ZUTI_TARGETS array based on targets designation.
     *
     * @param targetDesignation
     */
    public static void removeTargetPoint(String targetDesignation) {
        try {
            TargetPoint deadTarget = null;
            // System.out.println("Dead actor name: " + targetActorName);
            Iterator iterator = ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.iterator();
            TargetPoint targetpoint = null;
            String designation = null;

            while (iterator.hasNext()) {
                targetpoint = (TargetPoint) iterator.next();
                designation = targetpoint.zutiGetDesignation();
                // System.out.println("Target nameOrig: " + targetpoint.nameTargetOrig);
                // System.out.println("In Array 2: " + new Integer(targetpoint.nameTargetOrig.indexOf(targetActorName.trim())).toString());
                if (designation.indexOf(targetDesignation) > -1) {
                    deadTarget = targetpoint;
                    printDebugMessage("removeTargetPoint: Target point that will be removed: " + targetDesignation);
                    break;
                }
            }
            if (deadTarget != null) ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.remove(deadTarget);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Iterates through all load target points and prints their designation and type.
     */
    public static void listTargetPoints() {
        TargetPoint targetpoint = null;

        try {
            Iterator iterator = ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.iterator();
            while (iterator.hasNext()) {
                targetpoint = (TargetPoint) iterator.next();
                printDebugMessage("listTargetPoints: targetDesignation = " + targetpoint.zutiGetDesignation() + ", type = " + targetpoint.type + "x=" + targetpoint.x + ", y=" + targetpoint.y);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Target was probably part of home base and home base was overrun. Change it's designation and icon. Only for Destroy/defence ground targets.
     *
     * @param targetActorName
     */
    public static void changeTargetIconDescription(Target target) {
        try {
            TargetPoint targetpoint = null;
            Iterator iterator = ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.iterator();
            while (iterator.hasNext()) {
                targetpoint = (TargetPoint) iterator.next();
                // System.out.println("Comparing pos: " + new Double(targetpoint.x).toString() + ", " + new Double(targetpoint.y).toString());
                if ((int) (targetpoint.x - target.pos.getAbsPoint().x) == 0 && (int) (targetpoint.y - target.pos.getAbsPoint().y) == 0) {
                    if (targetpoint.type == 1) {
                        targetpoint.type = 6;
                        targetpoint.icon = IconDraw.get("icons/tdefenceground.mat");
                        targetpoint.iconOArmy = IconDraw.get("icons/tdestroyground.mat");
                    } else if (targetpoint.type == 6) {
                        targetpoint.type = 1;
                        targetpoint.icon = IconDraw.get("icons/tdestroyground.mat");
                        targetpoint.iconOArmy = IconDraw.get("icons/tdefenceground.mat");
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method searches sectfile for targets lines and creates appropriate target objects.
     *
     * @param sectfile
     */
    public static void fillTargetPoints(SectFile sectfile) {
        if (ZutiSupportMethods_GUI.ZUTI_TARGETS_LOADED || sectfile == null) return;

        ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.clear();

        int sectionIndex = sectfile.sectionIndex("Target");
        if (sectionIndex >= 0) {
            int size = sectfile.vars(sectionIndex);
            for (int index = 0; index < size; index++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(sectionIndex, index));
                int type = numbertokenizer.next(0, 0, 7);
                int importance = numbertokenizer.next(0, 0, 2);
                if (importance != 2) {
                    TargetPoint targetpoint = new TargetPoint();

                    targetpoint.zutiSetDesignation(Target.ZUTI_TARGET_DESIGNATION_PREFIX + index);

                    targetpoint.type = type;
                    targetpoint.importance = importance;
                    numbertokenizer.next(0);
                    numbertokenizer.next(0, 0, 720);
                    numbertokenizer.next(0);
                    targetpoint.x = numbertokenizer.next(0);
                    targetpoint.y = numbertokenizer.next(0);
                    int radius = numbertokenizer.next(0);
                    if (targetpoint.type == 3 || targetpoint.type == 6 || targetpoint.type == 1) {
                        if (radius < 50) radius = 50;
                        if (radius > 3000) radius = 3000;
                    }
                    targetpoint.r = radius;
                    numbertokenizer.next(0);
                    targetpoint.nameTarget = numbertokenizer.next((String) null);

                    if (targetpoint.nameTarget != null && targetpoint.nameTarget.startsWith("Bridge")) {
                        // Edit by |ZUTI|
                        // System.out.println("targetName: " + targetpoint.nameTarget);
                        targetpoint.nameTargetOrig = targetpoint.nameTarget;
                        targetpoint.nameTarget = null;
                    }
                    int xCoord = numbertokenizer.next(0);
                    int yCoord = numbertokenizer.next(0);
                    if (xCoord != 0 && yCoord != 0) {
                        targetpoint.x = xCoord;
                        targetpoint.y = yCoord;
                    }

                    switch (targetpoint.type) {
                        case 0:
                            targetpoint.icon = IconDraw.get("icons/tdestroyair.mat");
                            targetpoint.iconOArmy = IconDraw.get("icons/tdefence.mat");
                            if (targetpoint.nameTarget != null && sectfile.exist("Chiefs", targetpoint.nameTarget)) {
                                targetpoint.icon = IconDraw.get("icons/tdestroychief.mat");
                                targetpoint.iconOArmy = IconDraw.get("icons/tdefence.mat");
                            }

                            // Add actor to this target point as it can be appended to moving actor
                            assignTargetActor(targetpoint);
                            break;
                        case 1:
                            targetpoint.icon = IconDraw.get("icons/tdestroyground.mat");
                            targetpoint.iconOArmy = IconDraw.get("icons/tdefenceground.mat");
                            break;
                        case 2:
                            targetpoint.icon = IconDraw.get("icons/tdestroybridge.mat");
                            targetpoint.iconOArmy = IconDraw.get("icons/tdefencebridge.mat");
                            targetpoint.nameTarget = null;
                            break;
                        case 3:
                            targetpoint.icon = IconDraw.get("icons/tinspect.mat");
                            targetpoint.iconOArmy = IconDraw.get("icons/tdefence.mat");

                            // Add actor to this target point as it can be appended to moving actor
                            assignTargetActor(targetpoint);
                            break;
                        case 4:
                            targetpoint.icon = IconDraw.get("icons/tescort.mat");
                            targetpoint.iconOArmy = IconDraw.get("icons/tdestroychief.mat");

                            // Add actor to this target point as it can be appended to moving actor
                            assignTargetActor(targetpoint);
                            break;
                        case 5:
                            targetpoint.icon = IconDraw.get("icons/tdefence.mat");
                            targetpoint.iconOArmy = IconDraw.get("icons/tdestroychief.mat");

                            // Add actor to this target point as it can be appended to moving actor
                            assignTargetActor(targetpoint);
                            break;
                        case 6:
                            targetpoint.icon = IconDraw.get("icons/tdefenceground.mat");
                            targetpoint.iconOArmy = IconDraw.get("icons/tdestroyground.mat");
                            break;
                        case 7:
                            targetpoint.icon = IconDraw.get("icons/tdefencebridge.mat");
                            targetpoint.iconOArmy = IconDraw.get("icons/tdestroybridge.mat");
                            targetpoint.nameTarget = null;
                            break;
                    }
                    // Save target name at this point to nameTargetOrig
                    if (targetpoint.nameTarget != null) targetpoint.nameTargetOrig = targetpoint.nameTarget;
                    else {
                        // construct a name, just to get unique hash...
                        targetpoint.nameTarget = new Float(targetpoint.x).toString() + new Float(targetpoint.y).toString();
                        if (targetpoint.nameTargetOrig == null) targetpoint.nameTargetOrig = targetpoint.nameTarget;
                    }

                    if (targetpoint.nameTarget != null) if (sectfile.exist("Chiefs", targetpoint.nameTarget)) try {
                        StringTokenizer stringtokenizer = new StringTokenizer(sectfile.get("Chiefs", targetpoint.nameTarget, (String) null));
                        String string = stringtokenizer.nextToken();
                        int tmpName = string.indexOf(".");
                        targetpoint.nameTarget = I18N.technic(string.substring(0, tmpName)) + " " + I18N.technic(string.substring(tmpName + 1));
                    } catch (Exception exception) {
                        targetpoint.nameTarget = null;
                    }
                    else if (sectfile.sectionIndex(targetpoint.nameTarget) >= 0) try {
                        String string = sectfile.get(targetpoint.nameTarget, "Class", (String) null);
                        Class var_class = ObjIO.classForName(string);
                        targetpoint.nameTarget = Property.stringValue((Object) var_class, "iconFar_shortClassName", null);
                    } catch (Exception exception) {
                        targetpoint.nameTarget = null;
                    }
                    else targetpoint.nameTarget = null;

                    // Finally, add target to targets set
                    ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.add(targetpoint);
                }
            }
        }

        printDebugMessage("GUIBriefing - loaded targets: " + ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.size());

        ZutiSupportMethods_GUI.ZUTI_TARGETS_LOADED = true;
    }

    /**
     * For given target point object this method assigns new valid actor to it. Delicate are those target points that are attached to wings. Since one actor is target icon holder... if that one is destroyed, target icon might be removed but that should
     * not happen as target may not yet be completed.
     *
     * @param targetpoint
     */
    public static void assignTargetActor(TargetPoint targetpoint) {
        if (targetpoint == null) return;

        // System.out.println("targetpoint actor name: " + targetpoint.nameTarget);

        // Do we allow moving target icons?
        if (!Mission.MDS_VARIABLES().zutiIcons_MovingIcons) {
            // If we don't , set targetpoint actor to null. This will tell the program to show static icon for target
            targetpoint.actor = null;
            return;
        }

        try {
            // Add actor to this target point as it can be appended to moving actor
            targetpoint.actor = com.maddox.il2.engine.Actor.getByName(targetpoint.nameTarget);
            // This needs to be done because getByName method returns invalid actor (pos = null) if actor is AI plane
            // So, we need to add number from 0 to 3 at the end. But we have to search them one by one. If resulted actor = null, that plane was probably already killed
            if (targetpoint.actor != null && targetpoint.actor instanceof Wing) {
                Wing wing = (Wing) targetpoint.actor;
                targetpoint.isBaseActorWing = true;
                targetpoint.wing = wing;
                int length = wing.airc.length;
                for (int i = 0; i < length; i++)
                    if (wing.airc[i] != null && !wing.airc[i].getDiedFlag()) {
                        targetpoint.actor = wing.airc[i];
                        break;
                    }
            } else if (targetpoint.isBaseActorWing && targetpoint.wing != null) {
                // This target was already assigned and recognized as a wing, get new live actor in that wing to carry the target icon
                int length = targetpoint.wing.airc.length;
                for (int i = 0; i < length; i++)
                    if (targetpoint.wing.airc[i] != null && !targetpoint.wing.airc[i].getDiedFlag()) {
                        targetpoint.actor = targetpoint.wing.airc[i];
                        break;
                    }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Draw targets on given renderer
     *
     * @param guirenders
     * @param ttfont
     * @param mat
     * @param cameraortho2d
     * @param set
     * @param zutiPlayerArmy
     * @param zutiSameArmy
     */
    private static void drawTargets(GUIRenders guirenders, TTFont ttfont, Mat mat, CameraOrtho2D cameraortho2d, Set set, int zutiPlayerArmy, boolean zutiSameArmy) {
        // If targets are not meant to be drawn, don't draw them
        if (!Mission.MDS_VARIABLES().zutiIcons_ShowTargets) return;

        try {
            if (set.size() != 0) {
                GPoint gpoint = guirenders.getMouseXY();
                float f = gpoint.x;
                float f_19_ = guirenders.win.dy - 1.0F - gpoint.y;
                float f_20_ = IconDraw.scrSizeX() / 2;
                float f_21_ = f;
                float f_22_ = f_19_;
                IconDraw.setColor(-16711681);

                TargetPoint targetpoint = null;
                TargetPoint specialTargetPoint = null;
                Iterator iterator = set.iterator();

                while (iterator.hasNext()) {
                    targetpoint = (TargetPoint) iterator.next();
                    if (targetpoint.icon != null) {
                        float f_24_;
                        float f_25_;

                        if (targetpoint.isBaseActorWing && (targetpoint.actor == null || targetpoint.actor.getDiedFlag())) ZutiSupportMethods_GUI.assignTargetActor(targetpoint);
                        // printDebugMessage("ZutiSupportMethods - drawTargets - new actor assigned for target " + targetpoint.zutiGetDesignation());

                        // Debug info
                        /*
                         * System.out.println("playerArmy: " + new Integer(zutiPlayerArmy).toString()); System.out.println("movingRed: " + movingRed); System.out.println("movingBlue: " + movingBlue); System.out.println("zutiTargets_StaticIcons: " +
                         * Main.cur().mission.zutiTargets_StaticIcons); System.out.println("zutiRadar_StaticIconsIfNoRadar: " + Main.cur().mission.zutiRadar_StaticIconsIfNoRadar);
                         * System.out.println("----------------------------------------------------------------------------------------------");
                         */

                        f_24_ = (float) ((targetpoint.x - cameraortho2d.worldXOffset) * cameraortho2d.worldScale);
                        f_25_ = (float) ((targetpoint.y - cameraortho2d.worldYOffset) * cameraortho2d.worldScale);

                        if (Mission.MDS_VARIABLES().zutiIcons_StaticIconsIfNoRadar || targetpoint.isVisibleForPlayerArmy()) {
                            if (zutiSameArmy) IconDraw.render(targetpoint.icon, f_24_, f_25_);
                            else IconDraw.render(targetpoint.iconOArmy, f_24_, f_25_);

                            if (f_24_ >= f - f_20_ && f_24_ <= f + f_20_ && f_25_ >= f_19_ - f_20_ && f_25_ <= f_19_ + f_20_) {
                                specialTargetPoint = targetpoint;
                                f_21_ = f_24_;
                                f_22_ = f_25_;
                            }
                        }
                    }
                }

                if (specialTargetPoint != null) {
                    for (int i_26_ = 0; i_26_ < 3; i_26_++)
                        ZUTI_TIP[i_26_] = null;

                    if (specialTargetPoint.importance == 0) ZUTI_TIP[0] = I18N.gui("brief.Primary");
                    else ZUTI_TIP[0] = I18N.gui("brief.Secondary");

                    if (zutiSameArmy) switch (specialTargetPoint.type) {
                        case 0:
                            ZUTI_TIP[1] = I18N.gui("brief.Destroy");
                            break;
                        case 1:
                            ZUTI_TIP[1] = I18N.gui("brief.DestroyGround");
                            break;
                        case 2:
                            ZUTI_TIP[1] = I18N.gui("brief.DestroyBridge");
                            break;
                        case 3:
                            ZUTI_TIP[1] = I18N.gui("brief.Inspect");
                            break;
                        case 4:
                            ZUTI_TIP[1] = I18N.gui("brief.Escort");
                            break;
                        case 5:
                            ZUTI_TIP[1] = I18N.gui("brief.Defence");
                            break;
                        case 6:
                            ZUTI_TIP[1] = I18N.gui("brief.DefenceGround");
                            break;
                        case 7:
                            ZUTI_TIP[1] = I18N.gui("brief.DefenceBridge");
                            break;
                    }
                    else switch (specialTargetPoint.type) {
                        case 0:
                            ZUTI_TIP[1] = I18N.gui("brief.Defence");
                            break;
                        case 1:
                            ZUTI_TIP[1] = I18N.gui("brief.DefenceGround");
                            break;
                        case 2:
                            ZUTI_TIP[1] = I18N.gui("brief.DefenceBridge");
                            break;
                        case 3:
                            ZUTI_TIP[1] = I18N.gui("brief.Defence");
                            break;
                        case 4:
                            ZUTI_TIP[1] = I18N.gui("brief.Destroy");
                            break;
                        case 5:
                            ZUTI_TIP[1] = I18N.gui("brief.Destroy");
                            break;
                        case 6:
                            ZUTI_TIP[1] = I18N.gui("brief.DestroyGround");
                            break;
                        case 7:
                            ZUTI_TIP[1] = I18N.gui("brief.DestroyBridge");
                            break;
                    }
                    if (specialTargetPoint.nameTarget != null) ZUTI_TIP[2] = specialTargetPoint.nameTarget;
                    float f_27_ = ttfont.width(ZUTI_TIP[0]);
                    int i_28_ = 1;
                    for (int i_29_ = 1; i_29_ < 3; i_29_++) {
                        if (ZUTI_TIP[i_29_] == null) break;
                        i_28_ = i_29_;
                        float f_30_ = ttfont.width(ZUTI_TIP[i_29_]);
                        if (f_27_ < f_30_) f_27_ = f_30_;
                    }
                    float f_31_ = -ttfont.descender();
                    float f_32_ = ttfont.height() + f_31_;
                    f_27_ += 2.0F * f_31_;
                    float f_33_ = f_32_ * (i_28_ + 1) + 2.0F * f_31_;
                    float f_34_ = f_21_ - f_27_ / 2.0F;
                    float f_35_ = f_22_ + f_20_;
                    if (f_34_ + f_27_ > guirenders.win.dx) f_34_ = guirenders.win.dx - f_27_;
                    if (f_35_ + f_33_ > guirenders.win.dy) f_35_ = guirenders.win.dy - f_33_;
                    if (f_34_ < 0.0F) f_34_ = 0.0F;
                    if (f_35_ < 0.0F) f_35_ = 0.0F;
                    Render.drawTile(f_34_, f_35_, f_27_, f_33_, 0.0F, mat, -813694977, 0.0F, 0.0F, 1.0F, 1.0F);
                    Render.drawEnd();
                    for (int i_36_ = 0; i_36_ <= i_28_; i_36_++)
                        ttfont.output(-16777216, f_34_ + f_31_, f_35_ + f_31_ + (i_28_ - i_36_) * f_32_ + f_31_, 0.0F, ZUTI_TIP[i_36_]);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Draw targets on given renderer
     *
     * @param guirenders
     * @param ttfont
     * @param mat
     * @param cameraortho2d
     */
    public static void drawTargets(GUIRenders guirenders, TTFont ttfont, Mat mat, CameraOrtho2D cameraortho2d) {
        try {
            int zutiPlayerArmy = ZutiSupportMethods.getPlayerArmy();

            // System.out.println("Player Army: " + zutiPlayerArmy + " vs mission army: " + com.maddox.il2.ai.World.getMissionArmy());
            if (zutiPlayerArmy < 1) return;

            if (zutiPlayerArmy == World.getMissionArmy()) drawTargets(guirenders, ttfont, mat, cameraortho2d, ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS, zutiPlayerArmy, true);
            else drawTargets(guirenders, ttfont, mat, cameraortho2d, ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS, zutiPlayerArmy, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method draws air units on your pad screen.
     */
    public static void drawPadObjects() {
        boolean noMinimapPath = World.cur().diffCur.NoMinimapPath;
        boolean zutiRadar_AircraftIconsWhite = Mission.MDS_VARIABLES().zutiIcons_AircraftIconsWhite;

        try {
            Iterator iterator = GUI.pad.zutiPadObjects.keySet().iterator();
            ZutiPadObject zo = null;
            // Point3d point3d = null;

            while (iterator.hasNext()) {
                zo = (ZutiPadObject) GUI.pad.zutiPadObjects.get(iterator.next());

                if (zo == null || zo.getOwner() == null || !zo.isAlive()) continue;

                if (!noMinimapPath && zo.isPlayerPlane()) zo.setVisibleForPlayerArmy(true);

                if (zo.isVisibleForPlayerArmy()) {
                    if (zutiRadar_AircraftIconsWhite) IconDraw.setColor(-1);
                    else IconDraw.setColor(Army.color(zo.getArmy()));

                    if (zo.isPlayerPlane()) {
                        // System.out.println("ZutiSupportMethods_GUI - rendering player AC!");
                        GUI.pad.zutiPlayeAcDrawn = true;
                        IconDraw.setColor(-1);
                    }

                    // point3d = zo.getPosition();
                    // float f = (float) ((point3d.x - GUI.pad.cameraMap2D.worldXOffset) * GUI.pad.cameraMap2D.worldScale);
                    // float f_48_ = (float) ((point3d.y - GUI.pad.cameraMap2D.worldYOffset) * GUI.pad.cameraMap2D.worldScale);

                    switch (zo.type) {
                        case 0:
                        case 3: {
                            // IconDraw.render(GUI.pad._iconAir, f, f_48_, zo.getAzimut());
                            renderPadObjects(GUI.pad.GUIPadMode, zo, GUI.pad._iconAir);
                            break;
                        }
                        case 1:
                        case 2:
                        case 4:
                        case 5: {
                            Mat mat = zo.getIcon();
                            if (mat != null) // IconDraw.render(mat, f, f_48_);
                                renderPadObjects(GUI.pad.GUIPadMode, zo, mat);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void renderPadObjects(int GUIPadMode, ZutiPadObject zo, Mat mat) {
        if (GUIPadMode == 1) {
            Point3d point3d = zo.getPosition();
            float f = (float) ((((Tuple3d) point3d).x - GUI.pad.cameraMap2D.worldXOffset) * GUI.pad.cameraMap2D.worldScale);
            float f1 = (float) ((((Tuple3d) point3d).y - GUI.pad.cameraMap2D.worldYOffset) * GUI.pad.cameraMap2D.worldScale);
            // IconDraw.setColor(-1);
            IconDraw.render(mat, f, f1, zo.getAzimut());
        } else if (GUIPadMode == 2) // By PAL, relative map
        {
            Point3d point3d1 = zo.getPosition();

            if (zo.isPlayerPlane()) // IconDraw.setColor(-1);
                IconDraw.render(mat, GUI.pad.FrameOriginX, GUI.pad.FrameOriginY, -90.0F);
            else {
                float NewY = (float) ((((Tuple3d) point3d1).x - ((Tuple3d) GUI.pad.OwnPos3d).x) * Math.cos(GUI.pad.OwnAzimut)) - (float) ((-((Tuple3d) point3d1).y + ((Tuple3d) GUI.pad.OwnPos3d).y) * Math.sin(GUI.pad.OwnAzimut));
                float NewX = (float) ((-((Tuple3d) point3d1).y + ((Tuple3d) GUI.pad.OwnPos3d).y) * Math.cos(GUI.pad.OwnAzimut)) + (float) ((((Tuple3d) point3d1).x - ((Tuple3d) GUI.pad.OwnPos3d).x) * Math.sin(GUI.pad.OwnAzimut));
                float f = GUI.pad.FrameOriginX + (float) (NewX * GUI.pad.cameraMap2D.worldScale);
                float f1 = GUI.pad.FrameOriginY + (float) (NewY * GUI.pad.cameraMap2D.worldScale);
                // IconDraw.setColor(Army.color(actor.getArmy()));
                IconDraw.render(mat, f, f1);
            }
        } else if (GUIPadMode == 3) {
            if (!zo.isPlayerPlane()) return;

            Point3d point3d = zo.getPosition();
            float f = (float) ((((Tuple3d) point3d).x - GUI.pad.cameraMap2D.worldXOffset) * GUI.pad.cameraMap2D.worldScale);
            float f1 = (float) ((((Tuple3d) point3d).y - GUI.pad.cameraMap2D.worldYOffset) * GUI.pad.cameraMap2D.worldScale);
            // IconDraw.setColor(-1);
            IconDraw.render(mat, f, f1, zo.getAzimut());
        }
    }

    /**
     * Method returns a string indicating remaining targets statistics.
     *
     * @return
     */
    public static String remainingTargetsInfo() {
        StringBuffer sb = new StringBuffer();
        int primary = 0;
        int secondary = 0;
        int secret = 0;

        List targets = World.cur().targetsGuard.zutiGetTargets();
        Target target = null;
        for (int i = 0; i < targets.size(); i++) {
            target = (Target) targets.get(i);
            if (target.getDiedFlag() || target.isTaskComplete()) continue;

            switch (target.importance()) {
                case 0: {
                    primary++;
                    break;
                }
                case 1: {
                    secondary++;
                    break;
                }
                case 2: {
                    secret++;
                    break;
                }
            }
        }

        sb.append("PT: ");
        sb.append(primary);
        sb.append(", ST: ");
        sb.append(secondary);
        sb.append(", SeT: ");
        sb.append(secret);

        return sb.toString();
    }

    /**
     * Method fills GUIAirArming weapons list based on selected born place.
     *
     * @param airArming
     * @param bp
     */
    public static void fillWeaponsListBasedOnBornPlace(GUIAirArming airArming, BornPlace bp) {
        airArming.cWeapon.clear();
        airArming.weaponNames.clear();
        int i = airArming.cAircraft.getSelected();

        List limitWeaponsList = ZutiSupportMethods_Net.getLoadoutsForAircraftAtBornPlace(bp, (String) airArming.airNames.get(i));

        if (limitWeaponsList == null || limitWeaponsList.size() < 1) {
            airArming.fillWeapons();
            return;
        }

        if (i < 0) return;

        Class class1 = (Class) Property.value(airArming.airNames.get(i), "airClass", null);
        String as[] = Aircraft.getWeaponsRegistered(class1);
        if (as != null && as.length > 0) {
            String s = (String) airArming.airNames.get(i);
            for (int j = 0; j < as.length; j++) {
                String s1 = as[j];
                String weaponFullName = I18N.weapons(s, s1);

                if (!airArming.bEnableWeaponChange) {
                    String s2 = Main.cur().currentMissionFile.get(airArming.planeName, "weapons", (String) null);
                    if (!s1.equals(s2)) continue;
                }

                if (limitWeaponsList.contains(weaponFullName)) {
                    airArming.weaponNames.add(s1);
                    airArming.cWeapon.add(weaponFullName);
                }
            }

            if (airArming.weaponNames.size() == 0) {
                airArming.weaponNames.add(as[0]);
                airArming.cWeapon.add(I18N.weapons(s, as[0]));
            }
            airArming.cWeapon.setSelected(0, true, false);
        }
    }

    /**
     * Eliminates duplicates from both lists. Result is one clean list without any duplicates.
     *
     * @param airArming
     * @param inList
     * @return
     */
    public static String[] syncWeaponsLists(GUIAirArming airArming, String[] inList) {
        ArrayList newList = new ArrayList();
        for (int i = 0; i < inList.length; i++)
            if (airArming.weaponNames.contains(inList[i])) newList.add(inList[i]);

        String[] outList = new String[newList.size()];
        for (int i = 0; i < newList.size(); i++)
            outList[i] = (String) newList.get(i);

        return outList;
    }

    /**
     * Displays to console all multi-crew enabled aircraft.
     */
    public static void showAllMultiCrewEnabledAIrcraft() {
        for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (actor instanceof NetAircraft && Actor.isAlive(actor)) {
                printDebugMessage("Entry key: " + entry.getKey() + ",  value: " + entry.getValue());
                printDebugMessage("Actor str: " + actor.toString() + ", name: " + actor.name());
            }
        }
    }

    /**
     * Creates list of available AC that allow multi-crew for specific army.
     *
     * @param sectfile: mission file
     * @param army
     */
    public static List createListItemsForMultiCrewEnabledAircraft(boolean isDSConsole, SectFile sectfile, int army, boolean isNetMaster) {
        List crewPositions = new ArrayList();

        try {
            int aircraftArmy = -1;
            int position = 1;

            Actor actor = null;
            String wingName = null;
            Aircraft aircraft = null;
            Regiment regiment = null;
            Class var_class = null;
            PaintScheme paintscheme = null;
            Object cockpitObject = null;
            String aircraftName = null;
            GTexture gtexture = null;
            String flightModelName = null;
            SectFile flightModelSectFile = null;
            Item item = null;
            boolean isPlaneLivePlane = false;

            for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                aircraftArmy = -1;
                actor = (Actor) entry.getValue();

                if (!(actor instanceof Aircraft)) continue;

                if (!actor.isAlive()) continue;

                wingName = entry.getKey().toString();
                // This wing name has also appended aircraft position in wing, ie from 0 to 3. Remove that.
                wingName = wingName.substring(0, wingName.length() - 1);

                if (sectfile.get(wingName, "OnlyAI", 0, 0, 1) == 1) continue;

                aircraft = (Aircraft) actor;
                isPlaneLivePlane = false;
                if (aircraft.name().endsWith("_0")) {
                    // Host can not see LIVE ac crew positions as it is not working by design.
                    if (isNetMaster) continue;

                    isPlaneLivePlane = true;
                    aircraftArmy = aircraft.getArmy();
                    // System.out.println("Live player AC found: " + aircraft.name() + ", army: " + aircraftArmy);
                    // System.out.println("It's multicrew settings: " + aircraft.zutiIsPlaneMultiCrew() + ", and " + aircraft.zutiIsPlaneMultiCrewAnytime() );
                    // Joining of live planes is allowed only if pilot of that plane selected multicrew
                    // option for his plane and he has his engines shut off. On the ground. But if player also
                    // selected multicrew at any time, then allow joining, well, at all times.
                    if (!aircraft.FM.AS.zutiIsPlaneMultiCrew()) continue;
                    else if (aircraft.FM.AS.zutiIsPlaneMultiCrew() && (!ZutiSupportMethods.isPlaneStationary(aircraft.FM) || !ZutiSupportMethods_Multicrew.areEnginesOff(aircraft.FM.EI)) && !aircraft.FM.AS.zutiIsPlaneMultiCrewAnytime()) continue;
                }
                // AI Planes
                else if (!Mission.MDS_VARIABLES().zutiMisc_EnableAIAirborneMulticrew && (!ZutiSupportMethods.isPlaneStationary(aircraft.FM) || !ZutiSupportMethods_Multicrew.areEnginesOff(aircraft.FM.EI))) // AI plane. Joining AI plane can be done if plane
                                                                                                                                                                                                             // is on the ground and stationary OR,
                    // if mission allows it, if plane is in the air.
                    continue;

                // Set plane multiCrew globally - mostly for AI since live planes have this already set
                aircraft.FM.AS.zutiSetMultiCrew(true);

                regiment = aircraft.getRegiment();
                if (aircraftArmy == -1) aircraftArmy = regiment.getArmy();
                else
                    // For live players, regiment army can be messed up... And it determines
                    // the color in which plane is renders in gunner positions list. So, set it
                    // here for live players, just to make sure it's correct army.
                    regiment.setArmy(aircraftArmy);

                if (aircraftArmy == army) {
                    var_class = aircraft.getClass();
                    paintscheme = Aircraft.getPropertyPaintScheme(var_class, regiment.country());
                    cockpitObject = Property.value(var_class, "cockpitClass");
                    if (cockpitObject != null) {
                        Class[] var_classes = null;
                        int cockpits;
                        if (cockpitObject instanceof Class) cockpits = 1;
                        else {
                            var_classes = (Class[]) cockpitObject;
                            cockpits = var_classes.length;
                        }
                        aircraftName = Property.stringValue(var_class, "keyName", null);

                        gtexture = null;
                        if (!isDSConsole) {
                            Mat mat = PaintScheme.makeMatGUI(regiment.name() + "GUI", regiment.fileNameTga(), 1.0F, 1.0F, 1.0F);
                            if (mat != null) // System.out.println("GUINetAircraft - MAT TEXTURE NAME: " + mat.Name());
                                gtexture = GTexture.New(mat.Name());
                            else if (aircraft.getArmy() == 1) {
                                gtexture = GTexture.New("PaintSchemes/Cache/r01GUI.mat");
                                printDebugMessage("item texture for aircraft >" + aircraft.name() + "< set to default RED texture as selected aircraft regiment >" + regiment.name() + "< texture DOES NOT EXIST!");
                            } else if (aircraft.getArmy() == 2) {
                                gtexture = GTexture.New("PaintSchemes/Cache/g01GUI.mat");
                                printDebugMessage("item texture for aircraft >" + aircraft.name() + "< set to default BLUE texture as selected aircraft regiment >" + regiment.name() + "< texture DOES NOT EXIST!");
                            }
                        }

                        GUINetAircraft.crewFunction[0] = 1;
                        for (int crewFunctionsIndex = 1; crewFunctionsIndex < GUINetAircraft.crewFunction.length; crewFunctionsIndex++)
                            GUINetAircraft.crewFunction[crewFunctionsIndex] = 7;

                        flightModelName = Property.stringValue(var_class, "FlightModel", null);
                        flightModelSectFile = FlightModelMain.sectFile(flightModelName);

                        int nrOfCrew = flightModelSectFile.get("Aircraft", "Crew", 1, 1, 20);
                        for (int crewFunctionsIndex = 0; crewFunctionsIndex < GUINetAircraft.crewFunction.length; crewFunctionsIndex++)
                            GUINetAircraft.crewFunction[crewFunctionsIndex] = flightModelSectFile.get("Aircraft", "CrewFunction" + crewFunctionsIndex, GUINetAircraft.crewFunction[crewFunctionsIndex], 1, AircraftState.astateHUDPilotHits.length);

                        int acIndexInWing = aircraft.getWing().aircIndex(aircraft);

                        for (int cockpitId = 1; cockpitId < cockpits; cockpitId++)
                            // if (cockpitId <= 0 || !CockpitPilot.class.isAssignableFrom(var_classes[cockpitId]))
                            if (var_classes != null && !CockpitPilot.class.isAssignableFrom(var_classes[cockpitId])) // TODO: Storebror Cockpit assignment fix !!!
                            {
                                item = new Item();
                                item.indexInArmy = position++;// aircraft.getArmy();
                                item.iSectWing = 0;
                                item.iAircraft = acIndexInWing;
                                item.iCockpitNum = cockpitId;
                                item.wingName = wingName;
                                item.keyName = aircraftName;
                                item.cocName = AircraftState.astateHUDPilotHits[1];
//                                if (var_classes != null) {
                                int crewId = Property.intValue(var_classes[cockpitId], "astatePilotIndx", 0);
                                if (crewId < nrOfCrew) {
                                    crewId = GUINetAircraft.crewFunction[crewId];
                                    if (crewId < AircraftState.astateHUDPilotHits.length) item.cocName = AircraftState.astateHUDPilotHits[crewId];
                                }
//                                }
                                item.cocName = I18N.hud_log(item.cocName);
                                item.reg = regiment;
                                item.clsAircraft = var_class;
                                item.number = paintscheme.typedName(aircraft);
                                item.texture = gtexture;

                                if (item.cocName.equals("Pilot")) item.cocName = "Bombardier";

                                if (isPlaneLivePlane) item.zutiLiveAcName = aircraft.name();
                                else item.zutiLiveAcName = null;

                                // Edited by |ZUTI|: only gunner positions are available! No pilot seats possible!!!
                                crewPositions.add(item);
                            }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return crewPositions;
    }

    /**
     * Method fills GUIAirArming fuel list based on selected born place.
     *
     * @param airArming
     * @param bp
     */
    private static void fillAircraftMaxFuelSelectionBasedOnBornPlace(GUIAirArming airArming, BornPlace bp) {
        airArming.cFuel.clear();
        int selectedAircraft = airArming.cAircraft.getSelected();

        int maxSelection = ZutiSupportMethods_Net.getBornPlaceAircraftFuelSelectionLimit(bp, (String) airArming.airNames.get(selectedAircraft));
        for (int i = 0; i <= maxSelection; i++)
            airArming.cFuel.add(new Integer((i + 1) * 10).toString());
    }

    /**
     * Method fills GUIAirArming fuel combo box to it's fullest.
     *
     * @param airArming
     * @param bp
     */
    private static void fillFuelComboBox(GUIAirArming airArming) {
        airArming.cFuel.clear();
        for (int i = 0; i < 10; i++)
            airArming.cFuel.add(new Integer((i + 1) * 10).toString());
    }

    /**
     * Set appropriate fuel selection for specified aircraft that is placed on specified born place.
     *
     * @param airArming
     * @param bp
     * @param selection
     */
    public static void setFuelSelectionForAircraft(GUIAirArming airArming, BornPlace bp, int selection) {
        if (bp != null && bp.zutiEnablePlaneLimits) {
            ZutiSupportMethods_GUI.fillAircraftMaxFuelSelectionBasedOnBornPlace(airArming, bp);

            String selectedAircraft = (String) airArming.airNames.get(airArming.cAircraft.getSelected());
            int maxForSelectedAircraft = ZutiSupportMethods_Net.getBornPlaceAircraftFuelSelectionLimit(bp, selectedAircraft);

            if (selection > maxForSelectedAircraft) selection = maxForSelectedAircraft;
        } else ZutiSupportMethods_GUI.fillFuelComboBox(airArming);

        airArming.cFuel.setSelected(selection, true, false);
    }

    /**
     * Method loads all visited servers from game config file.
     */
    private static void loadDropDownServersList() {
        if (Config.isUSE_RENDER()) {
            for (int id = 0; id < 255; id++) {
                String index = "";
                if (id < 10) index = "00" + new Integer(id).toString();
                else if (id > 9 && id < 100) index = "0" + new Integer(id).toString();
                else if (id > 99) index = new Integer(id).toString();

                String remoteHost = Config.cur.ini.get("NET", "remoteHost_" + index, "");
                if (remoteHost.trim().length() > 0) DROP_DOWN_SERVERS_LIST.add(remoteHost);
            }

            DROP_DOWN_SERVERS_LIST = clearDuplicatesForDropDownServerList();
        }
    }

    private static List clearDuplicatesForDropDownServerList() {
        List list = new ArrayList();
        for (int id = 0; id < DROP_DOWN_SERVERS_LIST.size(); id++) {
            String line = (String) DROP_DOWN_SERVERS_LIST.get(id);
            if (!list.contains(line)) list.add(line);
        }

        return list;
    }

    /**
     * Method saves entered servers in "connect" menu.
     */
    private static void saveDropDownServersList() {
        if (Config.isUSE_RENDER()) {
            DROP_DOWN_SERVERS_LIST = clearDuplicatesForDropDownServerList();

            for (int id = 0; id < DROP_DOWN_SERVERS_LIST.size(); id++) {
                String index = "";
                if (id < 10) index = "00" + new Integer(id).toString();
                else if (id > 9 && id < 100) index = "0" + new Integer(id).toString();
                else if (id > 99) index = new Integer(id).toString();

                Config.cur.ini.setValue("NET", "remoteHost_" + index, (String) DROP_DOWN_SERVERS_LIST.get(id));
            }
        }
    }

    /**
     * Get all visited servers.
     *
     * @return
     */
    public static List getDropDownServersList() {
        if (DROP_DOWN_SERVERS_LIST == null) {
            DROP_DOWN_SERVERS_LIST = new ArrayList();
            ZutiSupportMethods_GUI.loadDropDownServersList();
        }

        Collections.sort(DROP_DOWN_SERVERS_LIST);

        return DROP_DOWN_SERVERS_LIST;
    }

    /**
     * Add new server to visited servers list.
     *
     * @param serverName
     */
    public static void addDropDownServer(String serverName) {
        if (!DROP_DOWN_SERVERS_LIST.contains(serverName)) {
            DROP_DOWN_SERVERS_LIST.add(serverName);
            ZutiSupportMethods_GUI.saveDropDownServersList();
        }
    }

    /**
     * Add new server to visited servers list.
     *
     * @param serverName
     */
    public static void removeDropDownServer(String serverName) {
        if (DROP_DOWN_SERVERS_LIST.contains(serverName)) {
            DROP_DOWN_SERVERS_LIST.remove(serverName);
            ZutiSupportMethods_GUI.saveDropDownServersList();
        }
    }

    /**
     * Call this method if host selected valid aircraft.
     */
    public static void spawn_ServerGUI() {
        if (isValidBornPlace_ServerGUI()) if (!isValidArming_ServerGUI()) {
            GUIAirArming.stateId = 2;
            Main.stateStack().push(55);
        } else {
            Main.cur().resetUser();
            NetUser netuser = (NetUser) NetEnv.host();
            int i = netuser.getBornPlace();
            int i_7_ = netuser.getAirdromeStay();

            printDebugMessage("------------------STAY AIRDROME: " + i_7_);

            UserCfg usercfg = World.cur().userCfg;
            netuser.checkReplicateSkin(usercfg.netAirName);
            netuser.checkReplicateNoseart(usercfg.netAirName);
            netuser.checkReplicatePilot();
            String string;
            if (usercfg.netTacticalNumber < 10) string = "0" + usercfg.netTacticalNumber;
            else string = "" + usercfg.netTacticalNumber;

            // ---------------------------------------------------------------------------
            // Edited by |ZUTI|: addint support for multi-crew...
            // Original call
            /*
             * CmdEnv.top().exec( "spawn " + ((Class)Property.value(usercfg.netAirName, "airClass", null)).getName() + " PLAYER NAME " + (((NetUser)NetEnv.host()).netUserRegiment.isEmpty() ? usercfg.netRegiment : "") + usercfg.netSquadron + "0" + string +
             * " WEAPONS " + usercfg.getWeapon(usercfg.netAirName) + " BORNPLACE " + i + " STAYPLACE " + i_7_ + " FUEL " + usercfg.fuel + " OVR");
             */
            // TODO: |ZUTI| call
            CmdEnv.top()
                    .exec("spawn " + ((Class) Property.value(usercfg.netAirName, "airClass", null)).getName() + " PLAYER NAME " + (((NetUser) NetEnv.host()).netUserRegiment.isEmpty() ? usercfg.netRegiment : "") + usercfg.netSquadron + "0" + string
                            + " WEAPONS " + usercfg.getWeapon(usercfg.netAirName) + " BORNPLACE " + i + " STAYPLACE " + i_7_ + " FUEL " + usercfg.fuel + (usercfg.bZutiMultiCrew ? " Z_MULTICREW " : " ")
                            + (usercfg.bZutiMultiCrewAnytime ? "Z_MULTICREW_ANYTIME" : "") + " OVR");
            // ---------------------------------------------------------------------------

            Aircraft aircraft = World.getPlayerAircraft();
            if (Actor.isValid(aircraft)) {
                GUI.unActivate();
                HotKeyCmd.exec("aircraftView", "CockpitView");
                ForceFeedback.startMission();
                AudioDevice.soundsOn();
                Main.stateStack().change(42);

                // TODO: Added by |ZUTI|: sync your position with other crew members
                // ---------------------------------------------------------------------------------------
                // Enable joystick!
                ZutiSupportMethods_Multicrew.setJojstickState();
                // ---------------------------------------------------------------------
            }
        }
    }

    /**
     * Method checks if user selected valid ac, loadout, fuel and country for selected home base. Return codes: 0=basic check failed. 1=fuel selection wrong. 2=weapons wrong. 3=country wrong.
     *
     * @return
     */
    public static int isValidArming() {
        UserCfg usercfg = World.cur().userCfg;
        if (usercfg.netRegiment == null) return 0;
        if (((NetUser) NetEnv.host()).netUserRegiment.isEmpty() && Actor.getByName(usercfg.netRegiment) == null) return 0;
        if (usercfg.netAirName == null) return 0;
        if (Property.value(usercfg.netAirName, "airClass", null) == null) return 0;
        if (usercfg.getWeapon(usercfg.netAirName) == null) return 0;
        NetUser netuser = (NetUser) NetEnv.host();
        if (netuser.getBornPlace() == -1) return 4;
        BornPlace bornplace = (BornPlace) World.cur().bornPlaces.get(netuser.getBornPlace());
        if (bornplace.airNames == null) return 0;

        // If home base does not have aircraft limitations selected, it's all valid :D
        if (!bornplace.zutiEnablePlaneLimits) return -1;

        float maxForSelectedAircraft = ZutiSupportMethods_Net.getBornPlaceAircraftFuelSelectionLimit(bornplace, usercfg.netAirName);
        // System.out.println("SELECTION: " + maxForSelectedAircraft);
        maxForSelectedAircraft = (maxForSelectedAircraft + 1) * 10;

        // System.out.println("MAX = " + maxForSelectedAircraft + " vs selected=" + usercfg.fuel);
        if (usercfg.fuel > maxForSelectedAircraft) {
            printDebugMessage("Change fuel selection!");
            return 1;
        }

        List loadouts = ZutiSupportMethods_Net.getLoadoutsForAircraftAtBornPlace(bornplace, usercfg.netAirName);
        boolean isValid = false;
        if (loadouts.size() == 0) isValid = true;
        String weaponName = ZutiSupportMethods.getWeaponI18NName(usercfg.netAirName, usercfg.getWeapon(usercfg.netAirName));
        for (int i = 0; i < loadouts.size(); i++) {
            String tmpWeapon = (String) loadouts.get(i);
            // System.out.println(" >" + tmpWeapon + "< vs >" + weaponName + "<");
            if (tmpWeapon.equals(weaponName)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            printDebugMessage("Change weapons selection!");
            return 2;
        }

        if (!ZutiSupportMethods.isRegimentValidForSelectedHB(usercfg.netRegiment, bornplace)) {
            printDebugMessage("Change country selection!");
            return 3;
        }

        return -1;
    }

    public static boolean isValidArming_ServerGUI() {
        com.maddox.il2.ai.UserCfg usercfg;
        usercfg = com.maddox.il2.ai.World.cur().userCfg;
        if (usercfg.netRegiment == null) return false;
        if (((com.maddox.il2.net.NetUser) com.maddox.rts.NetEnv.host()).netUserRegiment.isEmpty() && com.maddox.il2.engine.Actor.getByName(usercfg.netRegiment) == null) return false;
        if (usercfg.netAirName == null) return false;
        if (com.maddox.rts.Property.value(usercfg.netAirName, "airClass", null) == null) return false;
        if (usercfg.getWeapon(usercfg.netAirName) == null) return false;
        java.lang.Class class1;
        boolean flag;
        class1 = (java.lang.Class) com.maddox.rts.Property.value(usercfg.netAirName, "airClass", null);
        com.maddox.il2.net.NetUser netuser = (com.maddox.il2.net.NetUser) com.maddox.rts.NetEnv.host();
        int i = netuser.getBornPlace();
        com.maddox.il2.net.BornPlace bornplace = (com.maddox.il2.net.BornPlace) com.maddox.il2.ai.World.cur().bornPlaces.get(i);
        if (bornplace.airNames == null) return false;
        java.util.ArrayList arraylist = bornplace.airNames;
        flag = false;
        for (int j = 0; j < arraylist.size(); j++) {
            java.lang.String s = (java.lang.String) arraylist.get(j);
            java.lang.Class class2 = (java.lang.Class) com.maddox.rts.Property.value(s, "airClass", null);
            if (class2 == null || class1 != class2) continue;
            flag = true;
            break;
        }

        if (!flag) return false;
        return com.maddox.il2.objects.air.Aircraft.weaponsExist(class1, usercfg.getWeapon(usercfg.netAirName));
    }

    private static boolean isValidBornPlace_ServerGUI() {
        NetUser netuser = (NetUser) NetEnv.host();

        int i = netuser.getBornPlace();
        BornPlace bp = (BornPlace) World.cur().bornPlaces.get(i);
        if (!bp.zutiAirspawnOnly && netuser.getAirdromeStay() < 0 && !bp.zutiAirspawnIfQueueFull) {
            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, GAME_STATE.i18n("brief.BornPlace"), GAME_STATE.i18n("mds.info.stayPointUnavaliable"), 3, 0.0F);
            return false;
        } else if (bp.zutiAirspawnIfQueueFull && netuser.getAirdromeStay() < 0) return true;

        if (i < 0 || i >= World.cur().bornPlaces.size()) {
            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, GAME_STATE.i18n("brief.BornPlace"), GAME_STATE.i18n("brief.BornPlaceSelect"), 3, 0.0F);
            return false;
        }
        int i_6_ = netuser.getAirdromeStay();
        if (i_6_ < 0) {
            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, GAME_STATE.i18n("brief.StayPlace"), GAME_STATE.i18n("brief.StayPlaceWait"), 3, 0.0F);
            return false;
        }
        return true;
    }

    public static void spawn_ClientGUI() {
        if (isValidBornPlace_ClientGUI()) if (!isValidArming_ClientGUI()) {
            GUIAirArming.stateId = 2;
            Main.stateStack().push(55);
        } else {
            Main.cur().resetUser();
            NetUser netuser = (NetUser) NetEnv.host();
            int i = netuser.getBornPlace();
            int i_7_ = netuser.getAirdromeStay();
            UserCfg usercfg = World.cur().userCfg;
            netuser.checkReplicateSkin(usercfg.netAirName);
            netuser.checkReplicateNoseart(usercfg.netAirName);
            netuser.checkReplicatePilot();
            String string;
            if (usercfg.netTacticalNumber < 10) string = "0" + usercfg.netTacticalNumber;
            else string = "" + usercfg.netTacticalNumber;

            // ---------------------------------------------------------------------------
            // Edited by |ZUTI|: addint support for multi-crew...
            // Original call
            /*
             * CmdEnv.top().exec( "spawn " + ((Class)Property.value(usercfg.netAirName, "airClass", null)).getName() + " PLAYER NAME " + (((NetUser)NetEnv.host()).netUserRegiment.isEmpty() ? usercfg.netRegiment : "") + usercfg.netSquadron + "0" + string +
             * " WEAPONS " + usercfg.getWeapon(usercfg.netAirName) + " BORNPLACE " + i + " STAYPLACE " + i_7_ + " FUEL " + usercfg.fuel + " OVR");
             */
            // TODO: |ZUTI| call
            CmdEnv.top()
                    .exec("spawn " + ((Class) Property.value(usercfg.netAirName, "airClass", null)).getName() + " PLAYER NAME " + (((NetUser) NetEnv.host()).netUserRegiment.isEmpty() ? usercfg.netRegiment : "") + usercfg.netSquadron + "0" + string
                            + " WEAPONS " + usercfg.getWeapon(usercfg.netAirName) + " BORNPLACE " + i + " STAYPLACE " + i_7_ + " FUEL " + usercfg.fuel + (usercfg.bZutiMultiCrew ? " Z_MULTICREW " : " ")
                            + (usercfg.bZutiMultiCrewAnytime ? "Z_MULTICREW_ANYTIME" : "") + " OVR");
            // ---------------------------------------------------------------------------

            Aircraft aircraft = World.getPlayerAircraft();
            if (Actor.isValid(aircraft)) {
                GUI.unActivate();
                HotKeyCmd.exec("aircraftView", "CockpitView");
                ForceFeedback.startMission();
                AudioDevice.soundsOn();
                Main.stateStack().change(43);

                // TODO: Added by |ZUTI|: sync your position with other crew members
                // ---------------------------------------------------------------------------------------
                // Enable joystick!
                ZutiSupportMethods_Multicrew.setJojstickState();
                // ---------------------------------------------------------------------
            }
        }
    }

    private static boolean isValidArming_ClientGUI() {
        com.maddox.il2.ai.UserCfg usercfg;
        usercfg = com.maddox.il2.ai.World.cur().userCfg;
        if (usercfg.netRegiment == null) return false;
        if (((com.maddox.il2.net.NetUser) com.maddox.rts.NetEnv.host()).netUserRegiment.isEmpty() && com.maddox.il2.engine.Actor.getByName(usercfg.netRegiment) == null) return false;
        if (usercfg.netAirName == null) return false;
        if (com.maddox.rts.Property.value(usercfg.netAirName, "airClass", null) == null) return false;
        if (usercfg.getWeapon(usercfg.netAirName) == null) return false;

        boolean flag;
        Class class1 = (Class) Property.value(usercfg.netAirName, "airClass", null);
        com.maddox.il2.net.NetUser netuser = (com.maddox.il2.net.NetUser) com.maddox.rts.NetEnv.host();
        int i = netuser.getBornPlace();
        com.maddox.il2.net.BornPlace bornplace = (com.maddox.il2.net.BornPlace) com.maddox.il2.ai.World.cur().bornPlaces.get(i);
        if (bornplace.airNames == null) return false;
        java.util.ArrayList arraylist = bornplace.airNames;
        flag = false;
        for (int j = 0; j < arraylist.size(); j++) {
            java.lang.String s = (java.lang.String) arraylist.get(j);
            java.lang.Class class2 = (java.lang.Class) com.maddox.rts.Property.value(s, "airClass", null);
            if (class2 == null || class1 != class2) continue;
            flag = true;
            break;
        }

        if (!flag) return false;

        return Aircraft.weaponsExist(class1, usercfg.getWeapon(usercfg.netAirName));
    }

    private static boolean isValidBornPlace_ClientGUI() {
        NetUser netuser = (NetUser) NetEnv.host();
        int i = netuser.getBornPlace();
        BornPlace bp = (BornPlace) World.cur().bornPlaces.get(i);
        if (!bp.zutiAirspawnOnly && netuser.getAirdromeStay() < 0 && !bp.zutiAirspawnIfQueueFull) {
            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, GAME_STATE.i18n("brief.BornPlace"), GAME_STATE.i18n("mds.info.stayPointUnavaliable"), 3, 0.0F);
            return false;
        } else if (bp.zutiAirspawnIfQueueFull && netuser.getAirdromeStay() < 0) return true;

        if (i < 0 || i >= World.cur().bornPlaces.size()) {
            GUINetClientGuard guinetclientguard = (GUINetClientGuard) Main.cur().netChannelListener;
            guinetclientguard.curMessageBox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, GAME_STATE.i18n("brief.BornPlace"), GAME_STATE.i18n("brief.BornPlaceSelect"), 3, 0.0F);
            return false;
        }
        int i_6_ = netuser.getAirdromeStay();
        if (i_6_ < 0) {
            GUINetClientGuard guinetclientguard = (GUINetClientGuard) Main.cur().netChannelListener;
            guinetclientguard.curMessageBox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, GAME_STATE.i18n("brief.StayPlace"), GAME_STATE.i18n("brief.StayPlaceWait"), 3, 0.0F);
            return false;
        }
        return true;
    }

    private static int       debugLevel    = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT = 0;

    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_ZSM_GUI", DEBUG_DEFAULT);
        return debugLevel;
    }

    public static void printDebugMessage(String theMessage) {
        if (curDebugLevel() == 0) return;
        System.out.println("ZutiSupportMethods_GUI - " + theMessage);
    }
}