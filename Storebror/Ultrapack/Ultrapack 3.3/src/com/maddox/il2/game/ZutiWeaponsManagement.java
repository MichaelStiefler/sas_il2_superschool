package com.maddox.il2.game;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Front;
import com.maddox.il2.ai.ScoreCounter;
import com.maddox.il2.ai.ScoreItem;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiTargetsSupportMethods;
import com.maddox.il2.builder.ZutiSupportMethods_Builder;
import com.maddox.il2.builder.Zuti_WResourcesManagement.RRRItem;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.ZutiSupportMethods_Engine;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Aircraft._WeaponSlot;
import com.maddox.il2.objects.air.CockpitGunner;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.Pylon;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.Finger;
import com.maddox.rts.NetEnv;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class ZutiWeaponsManagement {
    public static boolean ZUTI_PROCESS_CARGO_DROPS            = true;
    public static boolean REACQUIRE_COCKPIT_AMMO_COUNTER_GUNS = false;

    public static void changeLoadout(Aircraft aircraft, String selectedLoadout, boolean showHUD) {
        // Unload current loadout.
        unloadLoadedWeapons(aircraft);
        // Destroy current weapons
        destroyLoadedWeapons(aircraft);
        // Reset current loadout
        aircraft.FM.CT.Weapons = new BulletEmitter[20][];

        System.out.println("Changing loadout to: " + selectedLoadout);
        try {
            ZutiSupportMethods_Air.loadWeaponsForAircraft(aircraft, selectedLoadout);
            // Aircraft.weapons(playerAC.getClass());
            // prepareWeapons((Aircraft)playerAC, result);

            // gunners guns references were lost, reset them
            ZutiWeaponsManagement.resetGunnersEmitters();

            // Notify cockpits that have ammo counters to re-acquire guns
            ZutiWeaponsManagement.REACQUIRE_COCKPIT_AMMO_COUNTER_GUNS = true;
        } catch (Exception ex) {
            System.out.println("ZutiWeaponsManagement error, ID_01: " + ex.toString());
            ex.printStackTrace();
        }

        // Unload new loadout. Rearming needs to be done!
        unloadLoadedWeapons(aircraft);

        System.out.println("Loadout changed!");
        if (showHUD) HUD.log("Loadout changed!");

        String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
        String userLocation = ZutiSupportMethods.getPlayerLocation();
        ZutiSupportMethods_NetSend.logMessage((NetUser) NetEnv.host(), userData + " changed loadout to >" + selectedLoadout + "< at " + userLocation);
    }

    private static void resetGunnersEmitters() {
        // TODO: +++ RRR Bug hunting
        if (!Config.isUSE_RENDER()) return; // Dedicated Servers don't have no "Main3D", their "Main" Class is of type "DServer" hence there's no 3D Render, therefore no cockpits.
        // --- RRR Bug hunting
        for (int i = 0; i < Main3D.cur3D().cockpits.length; i++)
            if (Main3D.cur3D().cockpits[i] instanceof CockpitGunner) ZutiSupportMethods_Air.resetCockpitGunnerWeaponOwner((CockpitGunner) Main3D.cur3D().cockpits[i]);
    }

    public static void unloadLoadedWeapons(Aircraft aircraft) {
        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++) {
            // TODO: +++ RRR Bug hunting
            if (weapons[i] == null) continue;
            // --- RRR Bug hunting
            try {
                for (int j = 0; j < weapons[i].length; j++) {
                    if (weapons[i][j] instanceof GunGeneric) ((GunGeneric) weapons[i][j]).loadBullets(0);
                    if (weapons[i][j] instanceof RocketGun) ((RocketGun) weapons[i][j]).loadBullets(0);
                    if (weapons[i][j] instanceof BombGun) ((BombGun) weapons[i][j]).loadBullets(0);
                }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }
    }

    public static void destroyLoadedWeapons(Aircraft aircraft) {
        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++)
            try {
                // TODO: +++ RRR Bug hunting
                if (weapons[i] == null) continue;
                // --- RRR Bug hunting
                for (int j = 0; j < weapons[i].length; j++) {
                    if (weapons[i][j] instanceof GunGeneric) ((GunGeneric) weapons[i][j]).destroy();
                    if (weapons[i][j] instanceof RocketGun) ((RocketGun) weapons[i][j]).doDestroy();
                    if (weapons[i][j] instanceof BombGun) ((BombGun) weapons[i][j]).doDestroy();
                }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
    }

    public static void removePylons(Aircraft aircraft) {
        // System.out.println("NetAircraft - " + ac.toString());

        Object aobj[] = aircraft.pos.getBaseAttached();
        if (aobj == null) // System.out.println("NetAircraft - no pylons to remove!");
            return;

        // System.out.println("NetAircraft - " + aobj.length);

        for (int i = 0; i < aobj.length; i++) {
            Object object = aobj[i];
            // System.out.println("NetAircraft - " + o);

            if (object instanceof Pylon) try {
                ((Pylon) object).destroy();
                // System.out.println("NetAircraft - removed bomb pylon");
                // ac.hierMesh().chunkVisible("Rack_D0", false);
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }
    }

    public static void preloadLoadOptions(Aircraft ac, int loadoutId, String selectedLoadout) {
        for (int i = 0; i < 10; i++)
            if (i == loadoutId) prepareWeapons(ac, selectedLoadout);
    }

    private static void prepareWeapons(Aircraft aircraft, String loadoutName) {
        Class class1 = aircraft.getClass();
        HashMapInt hashmapint = Aircraft.weaponsMapProperty(class1);
        int weaponId = Finger.Int(loadoutName);

        if (!hashmapint.containsKey(weaponId)) {
            System.out.println("Weapon set '" + loadoutName + "' not registered in " + ObjIO.classGetName(class1));
            return;
        }

        _WeaponSlot a_lweaponslot[] = (_WeaponSlot[]) hashmapint.get(weaponId);
        String as[] = Aircraft.getWeaponHooksRegistered(aircraft.getClass());

        for (int j = 0; j < as.length; j++)
            if (as[j] != null && a_lweaponslot[j] != null) {
                Class class2 = a_lweaponslot[j].clazz;
                if (!BombGun.class.isAssignableFrom(class2) || Property.containsValue(class2, "external")) {
                    String s = Property.stringValue(class2, "mesh", null);
                    if (s == null) {
                        Class class3 = (Class) Property.value(class2, "bulletClass", null);
                        if (class3 != null) s = Property.stringValue(class3, "mesh", null);
                    }
                    if (s != null) try {
                        new ActorSimpleMesh(s);
                    } catch (Exception exception) {
                        System.out.println("ZutiWeaponsManagement error, ID_02: " + exception.getMessage());
                        exception.printStackTrace();
                    }
                }
            }
    }

    /**
     * This method will load specified amount of bombs to specified aircraft.
     *
     * @param aircraft
     * @param bombs
     */
    public static void rearmBombsFTanksTorpedoes(Aircraft aircraft, int[] bombs) {
        if (aircraft == null) {
            // TODO: +++ RRR Bug hunting
            ZutiWeaponsManagement.printDebugMessage(aircraft, "ZutiWeaponsManagement rearmBombsFTanksTorpedoes aircraft = null!");
            // --- RRR Bug hunting
            return;
        }

        // TODO: +++ RRR Bug hunting
        String bombsArray = "";
        if (bombs == null) bombsArray = "null";
        else {
            bombsArray = "{ ";
            for (int i = 0; i < bombs.length; i++) {
                if (i > 0) bombsArray += ", ";
                bombsArray += bombs[i];
            }
            bombsArray += " }";
        }
        ZutiWeaponsManagement.printDebugMessage(aircraft, "ZutiWeaponsManagement rearmBombsFTanksTorpedoes(" + aircraft.getClass().getName() + ", " + bombsArray + ")");
        // --- RRR Bug hunting

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        Property bulletCount = null;

        // TODO: +++ RRR Bug hunting
        ZutiWeaponsManagement.printDebugMessage(aircraft, "ZutiWeaponsManagement weapons.length = " + weapons.length);
        // --- RRR Bug hunting
        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) {
                // TODO: +++ RRR Bug hunting
                ZutiWeaponsManagement.printDebugMessage(aircraft, "ZutiWeaponsManagement weapons[" + i + "] == null!");
                // --- RRR Bug hunting
                continue;
            }

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof BombGun) {
                        String className = Property.get(weapons[i][j].getClass(), "bulletClass").stringValue();
                        className = className.substring(className.indexOf("class ") + 6, className.length());

                        Class weaponClass = Class.forName(className);
                        float weaponPower = Property.floatValue(weaponClass, "power", -1F);

                        bulletCount = Property.get(weapons[i][j], "_count");
                        // TODO: +++ RRR Bug hunting
                        ZutiWeaponsManagement.printDebugMessage(aircraft,
                                "ZutiWeaponsManagement unloading BombGun weapons[" + i + "][" + j + "] is BombGun, className=" + className + ", weaponClass=" + weaponClass.getName() + ", weaponPower=" + weaponPower + ", bulletCount=" + bulletCount);
                        // --- RRR Bug hunting
                        ((BombGun) weapons[i][j]).loadBullets(0);
                        int loadBombs = 0;
                        if (bulletCount != null && bulletCount.intValue() > 0) loadBombs = bulletCount.intValue();
                        else loadBombs = 1;

                        // TODO: +++ RRR Bug hunting
                        ZutiWeaponsManagement.printDebugMessage(aircraft, "ZutiWeaponsManagement weapons[" + i + "][" + j + "] is BombGun, className=" + className + ", weaponClass=" + weaponClass.getName() + ", weaponPower=" + weaponPower
                                + ", bulletCount=" + bulletCount + ", loadBombs=" + loadBombs);
                        bombsArray = "";
                        if (bombs == null) bombsArray = "null";
                        else {
                            bombsArray = "{ ";
                            for (int bai = 0; bai < bombs.length; bai++) {
                                if (bai > 0) bombsArray += ", ";
                                bombsArray += bombs[bai];
                            }
                            bombsArray += " }";
                        }
                        ZutiWeaponsManagement.printDebugMessage(aircraft, "ZutiWeaponsManagement current bombsArray = " + bombsArray + ")");
                        // --- RRR Bug hunting
                        if (weaponPower <= 250F) {
                            if (loadBombs >= bombs[0]) {
                                loadBombs = bombs[0];
                                bombs[0] = 0;
                            } else bombs[0] -= loadBombs;
                        } else if (weaponPower <= 500F) {
                            if (loadBombs >= bombs[1]) {
                                loadBombs = bombs[1];
                                bombs[1] = 0;
                            } else bombs[1] -= loadBombs;
                        } else if (weaponPower <= 1000F) {
                            if (loadBombs >= bombs[2]) {
                                loadBombs = bombs[2];
                                bombs[2] = 0;
                            } else bombs[2] -= loadBombs;
                        } else if (weaponPower <= 2000F) {
                            if (loadBombs >= bombs[3]) {
                                loadBombs = bombs[3];
                                bombs[3] = 0;
                            } else bombs[3] -= loadBombs;
                        } else if (weaponPower <= 5000F) {
                            if (loadBombs >= bombs[4]) {
                                loadBombs = bombs[4];
                                bombs[4] = 0;
                            } else bombs[4] -= loadBombs;
                        } else if (loadBombs >= bombs[5]) {
                            loadBombs = bombs[5];
                            bombs[5] = 0;
                        } else bombs[5] -= loadBombs;

                        // TODO: +++ RRR Bug hunting
                        ZutiWeaponsManagement.printDebugMessage(aircraft, "ZutiWeaponsManagement loading BombGun weapons[" + i + "][" + j + "], className=" + className + ", weaponClass=" + weaponClass.getName() + " with " + loadBombs + " bullets!");
                        // --- RRR Bug hunting
                        ((BombGun) weapons[i][j]).loadBullets(loadBombs);
                        // TODO: +++ RRR Bug hunting
                        ZutiWeaponsManagement.printDebugMessage(aircraft, "ZutiWeaponsManagement BombGun weapons[" + i + "][" + j + "], className=" + className + ", weaponClass=" + weaponClass.getName() + " loaded with " + loadBombs + " bombs!");
                        // --- RRR Bug hunting
                    }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }
        // +++++ TODO skylla: enhanced weapon release control +++++
        aircraft.FM.CT.resetAvailableBombs();
        // ----- todo skylla: enhanced weapon release control -----

        System.out.println("Bombs, FuelTanks and Torpedoes rearmed");
    }

    /**
     * This method will load specified amount of bullets to specified aircraft.
     *
     * @param aircraft
     * @param bullets
     */
    public static void rearmMGs_Cannons(Aircraft aircraft, long bullets) {
        if (aircraft == null) return;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        Aircraft._WeaponSlot[] weaponSlots = Aircraft.getWeaponSlotsRegistered(aircraft.getClass(), ZutiSupportMethods_Air.getCurrentAircraftLoadoutName(aircraft));
        Property bulletCount = null;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof GunGeneric) {
                        bulletCount = Property.get(weapons[i][j], "_count");
                        ((GunGeneric) weapons[i][j]).loadBullets(0);
                        long loadBullets = 0;
                        if (bulletCount != null && bulletCount.intValue() > 0) loadBullets = bulletCount.intValue();
                        else loadBullets = ZutiWeaponsManagement.getBulletsForWeapon(weapons[i][j].getClass(), weaponSlots);

                        if (loadBullets >= bullets) {
                            loadBullets = bullets;
                            bullets = 0;
                        } else bullets -= loadBullets;

                        ((GunGeneric) weapons[i][j]).loadBullets((int) loadBullets);
                        // System.out.println(" Gun [" + i + ", " + j + "]" + weapons[i][j].toString() + " reloaded to bullets: " + ((GunGeneric)weapons[i][j]).countBullets());
                    }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }

        System.out.println("MGs and cannons rearmed");
    }

    /**
     * This method will load specified amount of rockets to specified aircraft.
     *
     * @param aircraft
     * @param rockets
     */
    public static void rearmRockets(Aircraft aircraft, long rockets) {
        // TODO: +++ RRR Bug hunting
        printDebugMessage(aircraft, "rearmRockets (" + rockets + ")");
        // --- RRR Bug hunting
        if (aircraft == null) return;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        Property bulletCount = null;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof RocketGun) {
                        bulletCount = Property.get(weapons[i][j], "_count");
                        ((RocketGun) weapons[i][j]).loadBullets(0);
                        long loadBullets = 1;
                        if (bulletCount != null && bulletCount.intValue() > 0) {
                            loadBullets = bulletCount.intValue();
                            // TODO: +++ RRR Bug hunting
                            printDebugMessage(aircraft, "found _count value on Gun [" + i + ", " + j + "]" + weapons[i][j].toString() + " = " + loadBullets + ", remaining rockets = " + rockets);
                            // --- RRR Bug hunting
                        }

                        if (loadBullets >= rockets) {
                            loadBullets = rockets;
                            rockets = 0;
                        } else rockets -= loadBullets;

                        ((RocketGun) weapons[i][j]).loadBullets((int) loadBullets);
                        // System.out.println(" Gun [" + i + ", " + j + "]" + weapons[i][j].toString() + " reloaded to bullets: " + ((RocketGun)weapons[i][j]).countBullets());

                        // TODO: +++ RRR Bug hunting
                        printDebugMessage(aircraft, "rearmRockets Gun [" + i + ", " + j + "]" + weapons[i][j].toString() + " reloaded to bullets: " + ((RocketGun) weapons[i][j]).countBullets());
                        // --- RRR Bug hunting
                    }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }
        // +++++ TODO skylla: enhanced weapon release control +++++
        aircraft.FM.CT.resetAvailableRockets();
        // ----- todo skylla: enhanced weapon release control -----
        System.out.println("Rockets rearmed");
    }

    /**
     * Method returns number of bullets that specified weapon class can have.
     *
     * @param weaponClass
     * @param weaponSlots
     * @return
     */
    public static int getBulletsForWeapon(Class weaponClass, Aircraft._WeaponSlot[] weaponSlots) {
        if (weaponSlots == null) return 0;

        for (int i = 0; i < weaponSlots.length; i++) {
            Aircraft._WeaponSlot ws = weaponSlots[i];
            // System.out.println(weaponClass + " vs " + ws.clazz);

            if (weaponClass != null && ws != null && weaponClass == ws.clazz) return ws.bullets;
        }

        return 0;
    }

    /**
     * Method checks if cargo/para drop was executed for specified mission parameters.
     *
     * @param weaponsName
     */
    public static void processDropCargoEvent(String weaponsName) {
        // System.out.println("Controls - checking released weapons: " + weaponsName);

        if (!ZUTI_PROCESS_CARGO_DROPS) return;

        float altitude = World.getPlayerFM().getAltitude();

        if ((weaponsName.indexOf("Cargo") > 0 || weaponsName.indexOf("Para") > 0) && isDropOverRequiredArea()) {
            if (altitude < Mission.MDS_VARIABLES().zutiDrop_MinHeight || altitude > Mission.MDS_VARIABLES().zutiDrop_MaxHeight) {
                HUD.log("mds.dropWrongAltitude");
                return;
            }

            try {
                ScoreCounter scorecounter = World.cur().scoreCounter;
                // 8=StaticAir, 500=points
                scorecounter.enemyItems.add(new ScoreItem(8, 500));
                HUD.log("mds.dropPoints");

                ZUTI_PROCESS_CARGO_DROPS = false;
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }
    }

    private static boolean isDropOverRequiredArea() {
        try {
            System.out.println("ZutiWeaponsManagement - processing drops!");

            int playerArmy = World.getPlayerArmy();
            FlightModel flightmodel = World.getPlayerFM();

            if (Mission.MDS_VARIABLES().zutiDrop_OverFriendlyHomeBase && dropOverHomeBase(playerArmy, 1, flightmodel.Loc.x, flightmodel.Loc.y)) return true;
            if (Mission.MDS_VARIABLES().zutiDrop_OverEnemyHomeBase && dropOverHomeBase(playerArmy, 2, flightmodel.Loc.x, flightmodel.Loc.y)) return true;
            if (Mission.MDS_VARIABLES().zutiDrop_OverNeutralHomeBase && dropOverHomeBase(playerArmy, 0, flightmodel.Loc.x, flightmodel.Loc.y)) return true;
            if (Mission.MDS_VARIABLES().zutiDrop_OverFriendlyFrictionArea && dropOverFrictionArea(playerArmy, 1, flightmodel.Loc.x, flightmodel.Loc.y)) return true;
            if (Mission.MDS_VARIABLES().zutiDrop_OverEnemyFrictionArea && dropOverFrictionArea(playerArmy, 2, flightmodel.Loc.x, flightmodel.Loc.y)) return true;
            if (Mission.MDS_VARIABLES().zutiDrop_OverDestroyGroundArea && dropOverTargetArea(flightmodel.Loc.x, flightmodel.Loc.y)) return true;
            if (Mission.MDS_VARIABLES().zutiDrop_OverDefenceGroundArea && dropOverTargetArea(flightmodel.Loc.x, flightmodel.Loc.y)) return true;
        } catch (Exception ex) {
            // TODO: +++ RRR Bug hunting
            ex.printStackTrace();
            // --- RRR Bug hunting
        }

        return false;
    }

    private static boolean dropOverHomeBase(int army, int targetArmy, double x, double y) {
        // targetArmy == 0 -> neutral
        // targetArmy == 1 -> friendly
        // targetArmy == 2 -> enemy
        ArrayList bornplaces = World.cur().bornPlaces;
        int size = bornplaces.size();
        for (int i = 0; i < size; i++) {
            BornPlace bp = (BornPlace) bornplaces.get(i);
            if (bp.army == 0 && targetArmy == 0) {
                double r = bp.r * bp.r;
                double tmpDistance = (x - bp.place.x) * (x - bp.place.x) + (y - bp.place.y) * (y - bp.place.y);
                if (tmpDistance < r) return true;
            } else if (army == bp.army && targetArmy == 1) {
                double r = bp.r * bp.r;
                double tmpDistance = (x - bp.place.x) * (x - bp.place.x) + (y - bp.place.y) * (y - bp.place.y);
                if (tmpDistance < r) return true;
            } else if (army != bp.army && targetArmy == 2) {
                double r = bp.r * bp.r;
                double tmpDistance = (x - bp.place.x) * (x - bp.place.x) + (y - bp.place.y) * (y - bp.place.y);
                if (tmpDistance < r) return true;
            }
        }

        return false;
    }

    private static boolean dropOverFrictionArea(int army, int targetArmy, double x, double y) {
        // targetArmy == 1 -> friendly
        // targetArmy == 2 -> enemy
        List zaps = ZutiSupportMethods_Engine.AIRFIELDS;
        int size = zaps.size();
        boolean overFriendlyTerritory = ZutiSupportMethods.isCoordinateOnCapturedTerritory(army, x, y);

        for (int i = 0; i < size; i++) {
            ZutiAirfieldPoint zap = (ZutiAirfieldPoint) zaps.get(i);
            if (overFriendlyTerritory && targetArmy == 1 && zap.isInZAPArea(x, y) > -1) return true;
            else if (!overFriendlyTerritory && targetArmy == 2 && zap.isInZAPArea(x, y) > -1) return true;
        }

        return false;
    }

    private static boolean dropOverTargetArea(double x, double y) {
        return ZutiTargetsSupportMethods.isOverTarget(x, y);
    }

    /**
     * Method unloads all remaining resources from specified aircraft. Resources are added to either home base that aircraft is on or to aircraft side, in case home base does not support own resources management.
     *
     * @param aircraft
     * @param position
     */
    public static void returnRemainingAircraftResources(Aircraft aircraft, Point3d position) {
        if (aircraft == null || aircraft.FM == null || aircraft.FM.M == null) return;

        if (position == null) position = aircraft.pos.getAbsPoint();
        // Check positioned also here because changes to weapons are done before sending is
        // called. Otherwise check in sending methods would suffice.
        if (!ZutiSupportMethods.isResourcesManagementValid(position)) return;

        float fuel = aircraft.FM.M.fuel;
        long bullets = 0;
        long rockets = 0;
        int[] bombs = new int[] { 0, 0, 0, 0, 0, 0 };
        long engines = 0;
        long repairKits = 0;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof GunGeneric) {
                        bullets += ((GunGeneric) weapons[i][j]).countBullets();
                        ((GunGeneric) weapons[i][j]).loadBullets(0);
                    } else if (weapons[i][j] instanceof RocketGun) {
                        rockets += ((RocketGun) weapons[i][j]).countBullets();
                        ((RocketGun) weapons[i][j]).loadBullets(0);
                    } else if (weapons[i][j] instanceof BombGun) {
                        String className = Property.get(weapons[i][j].getClass(), "bulletClass").stringValue();
                        className = className.substring(className.indexOf("class ") + 6, className.length());

                        if (className.indexOf(ZutiSupportMethods_Builder.BOMB_CARGO_NAME) > -1) {
                            // We have cargo box, resolve resources for it.
                            RRRItem rrrItem = null;
                            switch (aircraft.getArmy()) {
                                case 1: {
                                    rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Red");
                                    break;
                                }
                                case 2: {
                                    rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Blue");
                                    break;
                                }
                            }

                            if (rrrItem != null) {
                                int amount = ((BombGun) weapons[i][j]).countBullets();
                                bullets += rrrItem.bullets * amount;
                                rockets = rrrItem.rockets * amount;
                                bombs[0] += (int) rrrItem.bomb250 * amount;
                                bombs[1] += (int) rrrItem.bomb500 * amount;
                                bombs[2] += (int) rrrItem.bomb1000 * amount;
                                bombs[3] += (int) rrrItem.bomb2000 * amount;
                                bombs[4] += (int) rrrItem.bomb5000 * amount;
                                bombs[5] += (int) rrrItem.bomb9999 * amount;
                                fuel += rrrItem.fuel * amount;
                                engines += rrrItem.engines * amount;
                                repairKits += rrrItem.repairKits * amount;
                            }

                            continue;
                        }

                        Class weaponClass = Class.forName(className);
                        float weaponPower = Property.floatValue(weaponClass, "power", -1F);

                        if (weaponPower <= 250F) bombs[0] += ((BombGun) weapons[i][j]).countBullets();
                        else if (weaponPower <= 500F) bombs[1] += ((BombGun) weapons[i][j]).countBullets();
                        else if (weaponPower <= 1000F) bombs[2] += ((BombGun) weapons[i][j]).countBullets();
                        else if (weaponPower <= 2000F) bombs[3] += ((BombGun) weapons[i][j]).countBullets();
                        else if (weaponPower <= 5000F) bombs[4] += ((BombGun) weapons[i][j]).countBullets();
                        else bombs[5] += ((BombGun) weapons[i][j]).countBullets();

                        ((BombGun) weapons[i][j]).loadBullets(0);
                    }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }

        // Return fuel
        // System.out.println("Returning fuel amount: " + fuel);
        ZutiSupportMethods_NetSend.returnRRRResources_Fuel(fuel, position);

        // Return bullets
        // System.out.println("Returning bullets amount: " + bullets);
        ZutiSupportMethods_NetSend.returnRRRResources_Bullets(bullets, position);

        // Return rockets
        // System.out.println("Returning rockets amount: " + rockets);
        ZutiSupportMethods_NetSend.returnRRRResources_Rockets(rockets, position);

        // Return bombs
        /*
         * System.out.println("Returning 250kg bombs amount: " + bombs[0]); System.out.println("Returning 500kg bombs amount: " + bombs[1]); System.out.println("Returning 1000kg bombs amount: " + bombs[2]);
         * System.out.println("Returning 2000kg bombs amount: " + bombs[3]); System.out.println("Returning 5000kg bombs amount: " + bombs[4]); System.out.println("Returning 9999kg bombs amount: " + bombs[5]);
         */
        ZutiSupportMethods_NetSend.returnRRRResources_Bombs(bombs, position);

        // Return engines
        // System.out.println("Returning engines amount: " + engines);
        ZutiSupportMethods_NetSend.returnRRRResources_Engine(engines, position);

        // Return repair kits
        // System.out.println("Returning repair kits amount: " + repairKits);
        ZutiSupportMethods_NetSend.returnRRRResources_RepairKit(repairKits, position);

        System.out.println("Unused aircraft resources returned!");
    }

    /**
     * Method unloads remaining fuel from specified aircraft. It is added to either home base that aircraft is on or to aircraft side, in case home base does not support own resources management.
     *
     * @param aircraft
     * @param sync
     */
    public static void returnRemainingAircraftFuel(Aircraft aircraft, float fuel) {
        if (aircraft == null) return;

        // Check positioned also here because changes to weapons are done before sending is
        // called. Otherwise check in sending methods would suffice.
        if (!ZutiSupportMethods.isResourcesManagementValid(aircraft.pos.getAbsPoint())) return;

        // Return fuel
        System.out.println("Returning fuel amount: " + fuel);
        ZutiSupportMethods_NetSend.returnRRRResources_Fuel(fuel, aircraft.pos.getAbsPoint());

        if (aircraft.FM.M.fuel - fuel >= 0.0D) aircraft.FM.M.fuel -= fuel;
        else aircraft.FM.M.fuel = 0.0F;
    }

    /**
     * Method returns number of loaded guns for specified aircraft.
     *
     * @param aircraft
     * @param sync
     */
    public static int returnNumberOfLoadedGuns(Aircraft aircraft) {
        if (aircraft == null) return 0;

        int guns = 0;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof GunGeneric) if (((GunGeneric) weapons[i][j]).countBullets() > 0) guns++;
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }

        return guns;
    }

    /**
     * Method unloads remaining bullets from specified aircraft. They are added to either home base that aircraft is on or to aircraft side, in case home base does not support own resources management.
     *
     * @param aircraft
     * @param sync
     */
    public static long returnRemainingAircraftBullets(Aircraft aircraft, boolean sync) {
        if (aircraft == null) return 0;

        // Check positioned also here because changes to weapons are done before sending is
        // called. Otherwise check in sending methods would suffice.
        if (!ZutiSupportMethods.isResourcesManagementValid(aircraft.pos.getAbsPoint())) return 0;

        long bullets = 0;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof GunGeneric) {
                        bullets += ((GunGeneric) weapons[i][j]).countBullets();
                        if (sync) ((GunGeneric) weapons[i][j]).loadBullets(0);
                    }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }

        if (sync) {
            // Return bullets
            System.out.println("Returning bullets amount: " + bullets);
            ZutiSupportMethods_NetSend.returnRRRResources_Bullets(bullets, aircraft.pos.getAbsPoint());
        }

        return bullets;
    }

    /**
     * Method unloads remaining bullets from specified aircraft. They are added to either home base that aircraft is on or to aircraft side, in case home base does not support own resources management.
     *
     * @param aircraft
     * @param sync
     */
    public static long returnRemainingAircraftRockets(Aircraft aircraft, boolean sync) {
        if (aircraft == null) return 0;

        // Check positioned also here because changes to weapons are done before sending is
        // called. Otherwise check in sending methods would suffice.
        if (!ZutiSupportMethods.isResourcesManagementValid(aircraft.pos.getAbsPoint())) return 0;

        long rockets = 0;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof RocketGun) {
                        rockets += ((RocketGun) weapons[i][j]).countBullets();
                        if (sync) ((RocketGun) weapons[i][j]).loadBullets(0);
                    }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }

        if (sync) {
            // Return rockets
            System.out.println("Returning rockets amount: " + rockets);
            ZutiSupportMethods_NetSend.returnRRRResources_Rockets(rockets, aircraft.pos.getAbsPoint());
        }

        return rockets;
    }

    /**
     * Method unloads remaining bombs from specified aircraft. They are added to either home base that aircraft is on or to aircraft side, in case home base does not support own resources management.
     *
     * @param aircraft
     * @param sync
     */
    public static int[] returnRemainingAircraftBombsAndCargoCrates(Aircraft aircraft, boolean sync) {
        int[] bombs = new int[] { 0, 0, 0, 0, 0, 0 };

        if (aircraft == null) return bombs;

        // Check positioned also here because changes to weapons are done before sending is
        // called. Otherwise check in sending methods would suffice.
        if (!ZutiSupportMethods.isResourcesManagementValid(aircraft.pos.getAbsPoint())) return bombs;

        long bullets = 0;
        long rockets = 0;
        long fuel = 0;
        long engines = 0;
        long repairKits = 0;

//		if( aircraft == null )
//			return bombs;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof BombGun) {
                        String className = Property.get(weapons[i][j].getClass(), "bulletClass").stringValue();
                        className = className.substring(className.indexOf("class ") + 6, className.length());

                        if (className.indexOf(ZutiSupportMethods_Builder.BOMB_CARGO_NAME) > -1) {
                            // We have cargo box, resolve resources for it.
                            RRRItem rrrItem = null;
                            switch (aircraft.getArmy()) {
                                case 1: {
                                    rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Red");
                                    break;
                                }
                                case 2: {
                                    rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Blue");
                                    break;
                                }
                            }

                            if (rrrItem != null) {
                                int amount = ((BombGun) weapons[i][j]).countBullets();
                                bullets += rrrItem.bullets * amount;
                                rockets = rrrItem.rockets * amount;
                                bombs[0] += (int) rrrItem.bomb250 * amount;
                                bombs[1] += (int) rrrItem.bomb500 * amount;
                                bombs[2] += (int) rrrItem.bomb1000 * amount;
                                bombs[3] += (int) rrrItem.bomb2000 * amount;
                                bombs[4] += (int) rrrItem.bomb5000 * amount;
                                bombs[5] += (int) rrrItem.bomb9999 * amount;
                                fuel += rrrItem.fuel * amount;
                                engines += rrrItem.engines * amount;
                                repairKits += rrrItem.repairKits * amount;
                            }
                        } else {
                            // Normal bomb
                            Class weaponClass = Class.forName(className);
                            float weaponPower = Property.floatValue(weaponClass, "power", -1F);

                            if (weaponPower <= 250F) bombs[0] += ((BombGun) weapons[i][j]).countBullets();
                            else if (weaponPower <= 500F) bombs[1] += ((BombGun) weapons[i][j]).countBullets();
                            else if (weaponPower <= 1000F) bombs[2] += ((BombGun) weapons[i][j]).countBullets();
                            else if (weaponPower <= 2000F) bombs[3] += ((BombGun) weapons[i][j]).countBullets();
                            else if (weaponPower <= 5000F) bombs[4] += ((BombGun) weapons[i][j]).countBullets();
                            else bombs[5] += ((BombGun) weapons[i][j]).countBullets();
                        }

                        if (sync) ((BombGun) weapons[i][j]).loadBullets(0);
                    }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }

        if (sync) {
            if (bullets > 0) ZutiSupportMethods_NetSend.returnRRRResources_Bullets(bullets, aircraft.pos.getAbsPoint());
            if (rockets > 0) ZutiSupportMethods_NetSend.returnRRRResources_Rockets(rockets, aircraft.pos.getAbsPoint());
            if (bombs != null) {
                // Return bombs
                System.out.println("Returning 250kg bombs amount: " + bombs[0]);
                System.out.println("Returning 500kg bombs amount: " + bombs[1]);
                System.out.println("Returning 1000kg bombs amount: " + bombs[2]);
                System.out.println("Returning 2000kg bombs amount: " + bombs[3]);
                System.out.println("Returning 5000kg bombs amount: " + bombs[4]);
                System.out.println("Returning 9999kg bombs amount: " + bombs[5]);

                ZutiSupportMethods_NetSend.returnRRRResources_Bombs(bombs, aircraft.pos.getAbsPoint());
            }
            if (fuel > 0) ZutiSupportMethods_NetSend.returnRRRResources_Fuel(fuel, aircraft.pos.getAbsPoint());
            if (engines > 0) ZutiSupportMethods_NetSend.returnRRRResources_Engine(engines, aircraft.pos.getAbsPoint());
            if (repairKits > 0) ZutiSupportMethods_NetSend.returnRRRResources_RepairKit(repairKits, aircraft.pos.getAbsPoint());
        }

        return bombs;
    }

    /**
     * Call this method when cargo bomb land. Server side method only!
     *
     * @param x
     * @param y
     */
    public static void addCargoResources(Point3d pos) {
        int[] bombs = new int[] { 0, 0, 0, 0, 0, 0 };

        // Check positioned also here because changes to weapons are done before sending is
        // called. Otherwise check in sending methods would suffice.
        if (!ZutiSupportMethods.isResourcesManagementValid(pos)) return;

        long bullets = 0;
        long rockets = 0;
        long fuel = 0;
        long engines = 0;
        long repairKits = 0;
        int areaArmy = Front.army(pos.x, pos.y);
        RRRItem rrrItem = null;
        switch (areaArmy) {
            case 1: {
                rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Red");
                break;
            }
            case 2: {
                rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Blue");
                break;
            }
        }

        if (rrrItem != null) {
            bullets += rrrItem.bullets;
            rockets = rrrItem.rockets;
            bombs[0] += (int) rrrItem.bomb250;
            bombs[1] += (int) rrrItem.bomb500;
            bombs[2] += (int) rrrItem.bomb1000;
            bombs[3] += (int) rrrItem.bomb2000;
            bombs[4] += (int) rrrItem.bomb5000;
            bombs[5] += (int) rrrItem.bomb9999;
            fuel += rrrItem.fuel;
            engines += rrrItem.engines;
            repairKits += rrrItem.repairKits;
        }
        if (bullets > 0) ZutiSupportMethods_NetSend.returnRRRResources_Bullets(bullets, pos);
        if (rockets > 0) ZutiSupportMethods_NetSend.returnRRRResources_Rockets(rockets, pos);
        if (bombs != null) {
            // Return bombs
            System.out.println("Returning 250kg bombs amount : " + bombs[0]);
            System.out.println("Returning 500kg bombs amount : " + bombs[1]);
            System.out.println("Returning 1000kg bombs amount: " + bombs[2]);
            System.out.println("Returning 2000kg bombs amount: " + bombs[3]);
            System.out.println("Returning 5000kg bombs amount: " + bombs[4]);
            System.out.println("Returning 9999kg bombs amount: " + bombs[5]);

            ZutiSupportMethods_NetSend.returnRRRResources_Bombs(bombs, pos);
        }
        if (fuel > 0) ZutiSupportMethods_NetSend.returnRRRResources_Fuel(fuel, pos);
        if (engines > 0) ZutiSupportMethods_NetSend.returnRRRResources_Engine(engines, pos);
        if (repairKits > 0) ZutiSupportMethods_NetSend.returnRRRResources_RepairKit(repairKits, pos);
    }

    /**
     * Method prints in console type of weapons loaded on specified aircraft.
     *
     * @param aircraft
     */
    public static void weaponPrintout(Aircraft aircraft) {
        if (aircraft == null) return;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof BombGun) {
                        String className = Property.get(weapons[i][j].getClass(), "bulletClass").stringValue();
                        className = className.substring(className.indexOf("class ") + 6, className.length());

                        Class weaponClass = Class.forName(className);
                        float weaponPower = Property.floatValue(weaponClass, "power", -1F);

                        System.out.println("Bomb: " + className + ", power: " + weaponPower);
                    }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        }
    }

    /**
     * Method goes through bombs on specified aircraft weapons and prints them to console.
     *
     * @param weapons
     * @return
     */
    public static int[] getBombPrintout(Aircraft aircraft) {
        int[] categories = new int[] { 0, 0, 0, 0, 0, 0 };

        BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons;

        System.out.println("Bombs printout for >" + aircraft.name() + "<");
        for (int i = 0; i < weapons.length; i++)
            try {
                for (int j = 0; j < weapons[i].length; j++) {
                    String className = Property.get(weapons[i][j].getClass(), "bulletClass").stringValue();
                    className = className.substring(className.indexOf("class ") + 6, className.length());

                    Class weaponClass = Class.forName(className);
                    float weaponPower = Property.floatValue(weaponClass, "power", -1F);
                    // int weaponType = Property.intValue(weaponClass, "powerType", -1);

                    // Covers bombs, fuel tanks, torpedoes
                    if (weapons[i][j] instanceof BombGun) {
                        int amount = Property.get(weapons[i][j], "_count").intValue();
                        if (amount < 0) amount = 1;

                        /*
                         * System.out.println(" Bomb power: " + weaponPower); System.out.println("  Bomb type: " + weaponType); System.out.println("Bomb amount: " + amount);
                         */
                        if (weaponPower <= 250F) categories[0] += amount;
                        else if (weaponPower <= 500F) categories[1] += amount;
                        else if (weaponPower <= 1000F) categories[2] += amount;
                        else if (weaponPower <= 2000F) categories[3] += amount;
                        else if (weaponPower <= 5000F) categories[4] += amount;
                        else categories[5] += amount;
                    }
                }
            } catch (Exception ex) {
                // TODO: +++ RRR Bug hunting
                ex.printStackTrace();
                // --- RRR Bug hunting
            }
        System.out.println("===========================================");
        System.out.println("  Bombs by weight: ");
        System.out.println("   	 250kg: " + categories[0]);
        System.out.println("   	 500kg: " + categories[1]);
        System.out.println("   	1000kg: " + categories[2]);
        System.out.println("   	2000kg: " + categories[3]);
        System.out.println("   	5000kg: " + categories[4]);
        System.out.println("   	  more: " + categories[5]);
        System.out.println("===========================================");

        return categories;
    }

    // TODO: +++ RRR Bug hunting
    private static int       debugLevel    = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT = 0;

    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_RRR", DEBUG_DEFAULT);
        return debugLevel;
    }

    public static void printDebugMessage(Actor actor, String theMessage) {
        if (curDebugLevel() == 0) return;
        if (!(actor instanceof NetAircraft)) return;
        NetAircraft netAircraft = (NetAircraft) actor;
        if (netAircraft.netUser() != null) System.out.println("§§§§§§§§§§ " + netAircraft.netUser().uniqueName() + " " + theMessage + " §§§§§§§§§§");
        else System.out.println("§§§§§§§§§§ " + netAircraft.name() + " " + theMessage + " §§§§§§§§§§");

    }

    public static void printDebug(String message) {
        if (curDebugLevel() == 0) return;
        Exception exception = new Exception(message);
        System.out.println(exception.getMessage());
        exception.printStackTrace();
    }
    // --- RRR Bug hunting

}