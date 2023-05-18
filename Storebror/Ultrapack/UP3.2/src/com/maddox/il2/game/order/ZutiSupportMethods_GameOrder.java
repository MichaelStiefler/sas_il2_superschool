package com.maddox.il2.game.order;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.builder.ZutiSupportMethods_Builder;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.game.ZutiRearm_Cannons;
import com.maddox.il2.game.ZutiWeaponsManagement;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.Property;

public class ZutiSupportMethods_GameOrder {
    static Orders ZutiDogfightOptions = new Orders(new Order[] { (new Order("MainMenu")), new Order("Wingman", "deWingman", OrdersTree.ToWingman) {
        public void run() {
            this.cset(this.Wingman());
            super.run();
        }
    }, new Order("Squadron", "deSquadron", OrdersTree.ToGroupSquad) {
        public void run() {
            this.cset(this.PlayerSquad());
            super.run();
        }
    }, new OrderAnyone_Help_Me(), new Order("Frequency", OrdersTree.Frequency), (new Order("Ground_Control", OrdersTree.ToGroundControl)), null, new OrderBack() });

    /**
     * Set up orders tree so that it fits doghfight game mode.
     * 
     * @param ordersTree
     */
    public static void setAsDogfight(OrdersTree ordersTree) {
        ordersTree.home = OrdersTree.GroupLeader;
        if (!ordersTree.isRemote()) {
            for (int i = 0; i < ordersTree.home.order.length; i++) {
                if (ordersTree.home.order[i] != null)
                    ordersTree.home.order[i].attrib &= ~0x1;
            }
        }
    }

    /**
     * Check if issued order is about RRR processes.
     * 
     * @param order
     * @return
     */
    public static boolean isRRROrder(Order order) {
        // System.out.println("Checking order: " + order.name);
        if (order instanceof ZutiOrder_EngineRepair) {
            // System.out.println("Order of type ZutiOrder_EngineRepair");
            return true;
        }
        if (order instanceof ZutiOrder_Loadout) {
            // System.out.println("Order of type ZutiOrder_Loadout");
            return true;
        }
        if (order instanceof ZutiOrder_RearmAll) {
            // System.out.println("Order of type ZutiOrder_RearmAircraft");
            return true;
        }
        if (order instanceof ZutiOrder_RearmGuns) {
            // System.out.println("Order of type ZutiOrder_RearmAircraft");
            return true;
        }
        if (order instanceof ZutiOrder_RearmRockets) {
            // System.out.println("Order of type ZutiOrder_RearmAircraft");
            return true;
        }
        if (order instanceof ZutiOrder_RearmBombs) {
            // System.out.println("Order of type ZutiOrder_RearmAircraft");
            return true;
        }
        if (order instanceof ZutiOrder_RefuelAircraft) {
            // System.out.println("Order of type ZutiOrder_RefuelAircraft");
            return true;
        }
        if (order instanceof ZutiOrder_RepairAircraft) {
            // System.out.println("Order of type ZutiOrder_RepairAircraft");
            return true;
        }
        if (order instanceof ZutiOrder_StartAll) {
            // System.out.println("Order of type ZutiOrder_StartAll");
            return true;
        }
        if (order instanceof ZutiOrder_UnjamChocks) {
            // System.out.println("Order of type ZutiOrder_UnjamChocks");
            return true;
        }
        if (order instanceof ZutiOrder_VectorToNearestHB) {
            // System.out.println("Order of type ZutiOrder_VectorToNearestHB");
            return true;
        }
        if (order instanceof OrderVector_To_Home_Base) {
            // System.out.println("Order of type OrderVector_To_Home_Base");
            return true;
        }
        if (order instanceof OrderVector_To_Target) {
            // System.out.println("Order of type OrderVector_To_Target");
            return true;
        }
        if (order instanceof ZutiOrder_EjectGunner) {
            // System.out.println("Order of type ZutiOrder_KickGunner");
            return true;
        }

        return false;
    }

    /**
     * This method will return amount of fuel in kg that can be poured into aircraft.
     * 
     * @param aircraft
     * @param full
     * @return
     */
    public static float calculateFuelToRefuel(Aircraft aircraft, boolean full) {
        printDebugMessage("ZSP_GameOrder: maxFuel=" + aircraft.FM.M.maxFuel + ", currentFuel=" + aircraft.FM.M.fuel + ", requestingFullTank=" + full);
        Point3d pos = aircraft.pos.getAbsPoint();

        // This is the fuel amount that plane had when it spawned
        float fuelToFill = aircraft.zutiAircraftFuel;

        BornPlace fuelLimitBornPlace = ZutiSupportMethods_Net.getNearestBornPlace(pos.x, pos.y, aircraft.getArmy());
        float bpSpecifiedMaxFuel = 0.0F;
        if (fuelLimitBornPlace != null) {
            // Player landed born place that has fuel limitations
            bpSpecifiedMaxFuel = ZutiSupportMethods_Net.getAircraftFuelLimitBasedOnBornPlaceAircraftFuelSelectionLimitation(fuelLimitBornPlace, aircraft);
        }
        if (full) {
            if (bpSpecifiedMaxFuel > 0.0F)
                fuelToFill = bpSpecifiedMaxFuel;
            else
                fuelToFill = aircraft.FM.M.maxFuel;
        } else if (fuelToFill > bpSpecifiedMaxFuel && bpSpecifiedMaxFuel > 0.0F)
            fuelToFill = bpSpecifiedMaxFuel;

        fuelToFill = fuelToFill - aircraft.FM.M.fuel;
        if (fuelToFill < 0)
            fuelToFill = 0;

        return fuelToFill;
    }

    /**
     * This method will return amount of bullets.
     * 
     * @param aircraft
     * @return
     */
    public static long calculateBulletsToReload(Aircraft aircraft) {
        int guns = 0;
        long bullets = 0;

        com.maddox.il2.ai.BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        Aircraft._WeaponSlot[] weaponSlots = Aircraft.getWeaponSlotsRegistered(aircraft.getClass(), ZutiSupportMethods_Air.getCurrentAircraftLoadoutName(aircraft));
        Property bulletCount = null;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null)
                continue;

            try {
                for (int j = 0; j < weapons[i].length; j++) {
                    if (weapons[i][j] instanceof GunGeneric) {
                        guns++;

                        bulletCount = Property.get(weapons[i][j], "_count");
                        if (bulletCount != null && bulletCount.intValue() > 0)
                            bullets += bulletCount.intValue();
                        else
                            bullets += ZutiWeaponsManagement.getBulletsForWeapon(weapons[i][j].getClass(), weaponSlots);
                    }
                }
            } catch (Exception ex) {}
        }

        ZutiRearm_Cannons.NUMBER_OF_GUNS = guns;
        return bullets;
    }

    /**
     * Method returns array of categories with their bomb counter.
     * Categories: 250, 500, 1000, 2000, 5000, more
     * 
     * @param weapons
     * @return
     */
    public static int[] getBombsCount(Aircraft aircraft) {
        int[] categories = new int[] { 0, 0, 0, 0, 0, 0 };

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        // System.out.println("===========================================");
        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null)
                continue;

            try {
                for (int j = 0; j < weapons[i].length; j++) {
                    // Covers bombs, fuel tanks, torpedoes
                    if (weapons[i][j] instanceof BombGun) {
                        String className = Property.get(weapons[i][j].getClass(), "bulletClass").stringValue();
                        className = className.substring(className.indexOf("class ") + 6, className.length());

                        if (className.indexOf(ZutiSupportMethods_Builder.BOMB_CARGO_NAME) > -1)
                            continue;

                        Class weaponClass = Class.forName(className);
                        float weaponPower = Property.floatValue(weaponClass, "power", -1F);
                        // int weaponType = Property.intValue(weaponClass, "powerType", -1);

                        Property bulletCount = Property.get(weapons[i][j], "_count");
                        int amount = 0;
                        if (bulletCount != null && bulletCount.intValue() > 0)
                            amount += bulletCount.intValue();
                        else
                            amount += 1;

                        if (weaponPower <= 250F)
                            categories[0] += amount;
                        else if (weaponPower <= 500F)
                            categories[1] += amount;
                        else if (weaponPower <= 1000F)
                            categories[2] += amount;
                        else if (weaponPower <= 2000F)
                            categories[3] += amount;
                        else if (weaponPower <= 5000F)
                            categories[4] += amount;
                        else
                            categories[5] += amount;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        printDebugMessage("===========================================");
        printDebugMessage("  Requesting Bombs by weight: ");
        printDebugMessage("   	 250kg: " + categories[0]);
        printDebugMessage("   	 500kg: " + categories[1]);
        printDebugMessage("   	1000kg: " + categories[2]);
        printDebugMessage("   	2000kg: " + categories[3]);
        printDebugMessage("   	5000kg: " + categories[4]);
        printDebugMessage("   	  more: " + categories[5]);
        printDebugMessage("===========================================");
        return categories;
    }

    /**
     * This method will return number of rockets.
     * 
     * @param aircraft
     * @return
     */
    public static long calculateRocketsToReload(Aircraft aircraft) {
        long rockets = 0;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        Property bulletCount = null;

        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null)
                continue;

            try {
                for (int j = 0; j < weapons[i].length; j++) {
                    if (weapons[i][j] instanceof RocketGun) {
                        bulletCount = Property.get(weapons[i][j], "_count");
                        if (bulletCount != null && bulletCount.intValue() > 0)
                            rockets += bulletCount.intValue();
                        else
                            rockets += 1;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return rockets;
    }

    /**
     * Method will return number of cargo "bombs" that the plane wants to carry.
     * 
     * @param aircraft
     * @return
     */
    public static long getCargoCount(Aircraft aircraft) {
        long cargo = 0;

        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        // System.out.println("===========================================");
        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null)
                continue;

            try {
                for (int j = 0; j < weapons[i].length; j++) {
                    // Covers bombs, fuel tanks, torpedoes
                    if (weapons[i][j] instanceof BombGun) {
                        String className = Property.get(weapons[i][j].getClass(), "bulletClass").stringValue();
                        className = className.substring(className.indexOf("class ") + 6, className.length());

                        if (className.indexOf(ZutiSupportMethods_Builder.BOMB_CARGO_NAME) > -1)
                            cargo += ((BombGun) weapons[i][j]).countBullets();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        printDebugMessage("===========================================");
        printDebugMessage("  Requesting Cargo: " + cargo);
        printDebugMessage("===========================================");

        return cargo;
    }

    private static int       debugLevel    = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT = 0;

    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE)
            debugLevel = Config.cur.ini.get("Mods", "DEBUG_ZSM_GAMEORDER", DEBUG_DEFAULT);
        return debugLevel;
    }

    public static void printDebugMessage(String theMessage) {
        if (curDebugLevel() == 0)
            return;
        System.out.println(theMessage);
    }
}
