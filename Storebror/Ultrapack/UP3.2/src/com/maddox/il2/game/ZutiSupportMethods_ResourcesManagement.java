package com.maddox.il2.game;

import java.util.Map;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Front;
import com.maddox.il2.builder.ZutiSupportMethods_Builder;
import com.maddox.il2.builder.Zuti_WResourcesManagement.RRRItem;
import com.maddox.il2.engine.Config;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.buildings.House;

public class ZutiSupportMethods_ResourcesManagement {
    private static BornPlace LAST_RESOURCES_BORN_PLACE = null;

    /**
     * Reset class variables
     */
    public static void resetClassVariables() {
        LAST_RESOURCES_BORN_PLACE = null;
    }

    /**
     * Method searches for born place near given coordinates and checks if it has
     * enough fuel to serve to the player. Its fuel supply counter is appropriately
     * adjusted.
     * 
     * @param requestedFuel
     * @param x
     * @param y
     * @return
     */
    public static long getFuelForPlayer(float requestedFuel, double x, double y) {
        if (!Mission.MDS_VARIABLES().enabledResourcesManagement_BySide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases)
            return (long) requestedFuel;

        BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
        int armyForLocation = Front.army(x, y);

        if (bp != null) {
            if (bp.zutiEnableResourcesManagement) {
                if (requestedFuel <= bp.zutiFuelSupply) {
                    bp.zutiFuelSupply -= requestedFuel;
                } else {
                    requestedFuel = bp.zutiFuelSupply;
                    bp.zutiFuelSupply = 0;
                }
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                // Manage side resources
                if (requestedFuel <= Mission.MDS_VARIABLES().zutiFuelSupply_Red) {
                    Mission.MDS_VARIABLES().zutiFuelSupply_Red -= requestedFuel;
                } else {
                    requestedFuel = (int) Mission.MDS_VARIABLES().zutiFuelSupply_Red;
                    Mission.MDS_VARIABLES().zutiFuelSupply_Red = 0;
                }
            } else if (armyForLocation == 2) {
                // Manage side resources
                if (requestedFuel <= Mission.MDS_VARIABLES().zutiFuelSupply_Blue) {
                    Mission.MDS_VARIABLES().zutiFuelSupply_Blue -= requestedFuel;
                } else {
                    requestedFuel = (int) Mission.MDS_VARIABLES().zutiFuelSupply_Blue;
                    Mission.MDS_VARIABLES().zutiFuelSupply_Blue = 0;
                }
            }
        }

        // -------------------PRINT OUT--------------------------------------------
        if (bp != null) {
            if (bp.zutiEnableResourcesManagement) {
                printDebugMessage("After deduction of >" + requestedFuel + "< fuel - Home Base(" + (int) bp.place.x + ", " + (int) bp.place.y + ")");
                printDebugMessage("  Fuel supply=" + bp.zutiFuelSupply);
                printDebugMessage("======================================================");
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                printDebugMessage("After deduction of >" + requestedFuel + "< fuel - Red side");
                printDebugMessage("  Fuel supply=" + Mission.MDS_VARIABLES().zutiFuelSupply_Red);
                printDebugMessage("======================================================");
            } else if (armyForLocation == 2) {
                printDebugMessage("After deduction of >" + requestedFuel + "< fuel - Blue side");
                printDebugMessage("  Fuel supply=" + Mission.MDS_VARIABLES().zutiFuelSupply_Blue);
                printDebugMessage("======================================================");
            }
        }
        // ------------------------------------------------------------------------

        return (long) requestedFuel;
    }

    /**
     * Method searches for born place near given coordinates and checks if it has
     * enough bullets to serve to the player. Its bullets supply counter is appropriately
     * adjusted.
     * 
     * @param requestedBullets
     * @param x
     * @param y
     * @return
     */
    public static long getBulletsForPlayer(long requestedBullets, double x, double y) {
        if (!Mission.MDS_VARIABLES().enabledResourcesManagement_BySide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases)
            return requestedBullets;

        BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
        int armyForLocation = Front.army(x, y);

        if (bp != null) {
            if (bp.zutiEnableResourcesManagement) {
                if (requestedBullets <= bp.zutiBulletsSupply) {
                    bp.zutiBulletsSupply -= requestedBullets;
                } else {
                    requestedBullets = bp.zutiBulletsSupply;
                    bp.zutiBulletsSupply = 0;
                }
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                // Manage side resources
                if (requestedBullets <= Mission.MDS_VARIABLES().zutiBulletsSupply_Red) {
                    Mission.MDS_VARIABLES().zutiBulletsSupply_Red -= requestedBullets;
                } else {
                    requestedBullets = (int) Mission.MDS_VARIABLES().zutiBulletsSupply_Red;
                    Mission.MDS_VARIABLES().zutiBulletsSupply_Red = 0;
                }
            } else if (armyForLocation == 2) {
                // Manage side resources
                if (requestedBullets <= Mission.MDS_VARIABLES().zutiBulletsSupply_Blue) {
                    Mission.MDS_VARIABLES().zutiBulletsSupply_Blue -= requestedBullets;
                } else {
                    requestedBullets = (int) Mission.MDS_VARIABLES().zutiBulletsSupply_Blue;
                    Mission.MDS_VARIABLES().zutiBulletsSupply_Blue = 0;
                }
            }
        }

        // -------------------PRINT OUT--------------------------------------------
        if (bp != null) {
            if (bp.zutiEnableResourcesManagement) {
                printDebugMessage("After deduction of >" + requestedBullets + "< bullets - Home Base(" + (int) bp.place.x + ", " + (int) bp.place.y + ")");
                printDebugMessage("  Bullets supply=" + bp.zutiBulletsSupply);
                printDebugMessage("======================================================");
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                printDebugMessage("After deduction of >" + requestedBullets + "< bullets - Red side");
                printDebugMessage("  Bullets supply=" + Mission.MDS_VARIABLES().zutiBulletsSupply_Red);
                printDebugMessage("======================================================");
            } else if (armyForLocation == 2) {
                printDebugMessage("After deduction of >" + requestedBullets + "< bullets - Blue side");
                printDebugMessage("  Bullets supply=" + Mission.MDS_VARIABLES().zutiBulletsSupply_Blue);
                printDebugMessage("======================================================");
            }
        }
        // ------------------------------------------------------------------------

        return requestedBullets;
    }

    /**
     * Method searches for born place near given coordinates and checks if it has
     * enough bullets to serve to the player. Its bullets supply counter is appropriately
     * adjusted.
     * 
     * @param requestedBullets
     * @param x
     * @param y
     * @return
     */
    public static int[] getBombsForPlayer(int[] requestedBombs, double x, double y) {
        if (!Mission.MDS_VARIABLES().enabledResourcesManagement_BySide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases)
            return requestedBombs;

        BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
        int armyForLocation = Front.army(x, y);

        if (bp != null) {
            if (bp.zutiEnableResourcesManagement) {
                if (requestedBombs[0] <= bp.zutiBombsSupply[0]) {
                    bp.zutiBombsSupply[0] -= requestedBombs[0];
                } else {
                    requestedBombs[0] = bp.zutiBombsSupply[0];
                    bp.zutiBombsSupply[0] = 0;
                }

                if (requestedBombs[1] <= bp.zutiBombsSupply[1]) {
                    bp.zutiBombsSupply[1] -= requestedBombs[1];
                } else {
                    requestedBombs[1] = bp.zutiBombsSupply[1];
                    bp.zutiBombsSupply[1] = 0;
                }

                if (requestedBombs[2] <= bp.zutiBombsSupply[2]) {
                    bp.zutiBombsSupply[2] -= requestedBombs[2];
                } else {
                    requestedBombs[2] = bp.zutiBombsSupply[2];
                    bp.zutiBombsSupply[2] = 0;
                }

                if (requestedBombs[3] <= bp.zutiBombsSupply[3]) {
                    bp.zutiBombsSupply[3] -= requestedBombs[3];
                } else {
                    requestedBombs[3] = bp.zutiBombsSupply[3];
                    bp.zutiBombsSupply[3] = 0;
                }

                if (requestedBombs[4] <= bp.zutiBombsSupply[4]) {
                    bp.zutiBombsSupply[4] -= requestedBombs[4];
                } else {
                    requestedBombs[4] = bp.zutiBombsSupply[4];
                    bp.zutiBombsSupply[4] = 0;
                }

                if (requestedBombs[5] <= bp.zutiBombsSupply[5]) {
                    bp.zutiBombsSupply[5] -= requestedBombs[5];
                } else {
                    requestedBombs[5] = bp.zutiBombsSupply[5];
                    bp.zutiBombsSupply[5] = 0;
                }
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                // Manage side resources
                if (requestedBombs[0] <= Mission.MDS_VARIABLES().zutiBombsSupply_Red[0]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] -= requestedBombs[0];
                } else {
                    requestedBombs[0] = Mission.MDS_VARIABLES().zutiBombsSupply_Red[0];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] = 0;
                }

                if (requestedBombs[1] <= Mission.MDS_VARIABLES().zutiBombsSupply_Red[1]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] -= requestedBombs[1];
                } else {
                    requestedBombs[1] = Mission.MDS_VARIABLES().zutiBombsSupply_Red[1];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] = 0;
                }

                if (requestedBombs[2] <= Mission.MDS_VARIABLES().zutiBombsSupply_Red[2]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] -= requestedBombs[2];
                } else {
                    requestedBombs[2] = Mission.MDS_VARIABLES().zutiBombsSupply_Red[2];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] = 0;
                }

                if (requestedBombs[3] <= Mission.MDS_VARIABLES().zutiBombsSupply_Red[3]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] -= requestedBombs[3];
                } else {
                    requestedBombs[3] = Mission.MDS_VARIABLES().zutiBombsSupply_Red[3];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] = 0;
                }

                if (requestedBombs[4] <= Mission.MDS_VARIABLES().zutiBombsSupply_Red[4]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] -= requestedBombs[4];
                } else {
                    requestedBombs[4] = Mission.MDS_VARIABLES().zutiBombsSupply_Red[4];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] = 0;
                }

                if (requestedBombs[5] <= Mission.MDS_VARIABLES().zutiBombsSupply_Red[5]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] -= requestedBombs[5];
                } else {
                    requestedBombs[5] = Mission.MDS_VARIABLES().zutiBombsSupply_Red[5];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] = 0;
                }
            } else if (armyForLocation == 2) {
                // Manage side resources
                if (requestedBombs[0] <= Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] -= requestedBombs[0];
                } else {
                    requestedBombs[0] = Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] = 0;
                }

                if (requestedBombs[1] <= Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] -= requestedBombs[1];
                } else {
                    requestedBombs[1] = Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] = 0;
                }

                if (requestedBombs[2] <= Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] -= requestedBombs[2];
                } else {
                    requestedBombs[2] = Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] = 0;
                }

                if (requestedBombs[3] <= Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] -= requestedBombs[3];
                } else {
                    requestedBombs[3] = Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] = 0;
                }

                if (requestedBombs[4] <= Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] -= requestedBombs[4];
                } else {
                    requestedBombs[4] = Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] = 0;
                }

                if (requestedBombs[5] <= Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5]) {
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] -= requestedBombs[5];
                } else {
                    requestedBombs[5] = Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5];
                    Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] = 0;
                }
            }
        }

        if (bp != null) {
            if (bp.zutiEnableResourcesManagement) {
                printDebugMessage("After deductin of bombs - Home Base (" + (int) bp.place.x + ", " + (int) bp.place.y + ")");
                printDebugMessage("  Bombs{250kg}  supply=" + bp.zutiBombsSupply[0]);
                printDebugMessage("  Bombs{500kg}  supply=" + bp.zutiBombsSupply[1]);
                printDebugMessage("  Bombs{1000kg} supply=" + bp.zutiBombsSupply[2]);
                printDebugMessage("  Bombs{2000kg} supply=" + bp.zutiBombsSupply[3]);
                printDebugMessage("  Bombs{5000kg} supply=" + bp.zutiBombsSupply[4]);
                printDebugMessage("  Bombs{9999kg} supply=" + bp.zutiBombsSupply[5]);
                printDebugMessage("======================================================");
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                printDebugMessage("After deductin of bombs - Red side");
                printDebugMessage("  Bombs{250kg}  supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Red[0]);
                printDebugMessage("  Bombs{500kg}  supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Red[1]);
                printDebugMessage("  Bombs{1000kg} supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Red[2]);
                printDebugMessage("  Bombs{2000kg} supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Red[3]);
                printDebugMessage("  Bombs{5000kg} supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Red[4]);
                printDebugMessage("  Bombs{9999kg} supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Red[5]);
                printDebugMessage("======================================================");
            } else if (armyForLocation == 2) {
                printDebugMessage("After deductin of bombs - Blue side");
                printDebugMessage("  Bombs{250kg}  supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0]);
                printDebugMessage("  Bombs{500kg}  supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1]);
                printDebugMessage("  Bombs{1000kg} supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2]);
                printDebugMessage("  Bombs{2000kg} supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3]);
                printDebugMessage("  Bombs{5000kg} supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4]);
                printDebugMessage("  Bombs{9999kg} supply=" + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5]);
                printDebugMessage("======================================================");
            }
        }

        return requestedBombs;
    }

    /**
     * Method searches for born place near given coordinates and checks if it has
     * enough rockets to serve to the player. Its rockets supply counter is appropriately
     * adjusted.
     * 
     * @param requestedRockets
     * @param x
     * @param y
     * @return
     */
    public static long getRocketsForPlayer(long requestedRockets, double x, double y) {
        if (!Mission.MDS_VARIABLES().enabledResourcesManagement_BySide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases)
            return requestedRockets;

        BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
        int armyForLocation = Front.army(x, y);

        if (bp != null) {
            if (bp.zutiEnableResourcesManagement) {
                if (requestedRockets <= bp.zutiRocketsSupply) {
                    bp.zutiRocketsSupply -= requestedRockets;
                } else {
                    requestedRockets = bp.zutiRocketsSupply;
                    bp.zutiRocketsSupply = 0;
                }
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                // Manage side resources
                if (requestedRockets <= Mission.MDS_VARIABLES().zutiRocketsSupply_Red) {
                    Mission.MDS_VARIABLES().zutiRocketsSupply_Red -= requestedRockets;
                } else {
                    requestedRockets = (int) Mission.MDS_VARIABLES().zutiRocketsSupply_Red;
                    Mission.MDS_VARIABLES().zutiRocketsSupply_Red = 0;
                }
            } else if (armyForLocation == 2) {
                // Manage side resources
                if (requestedRockets <= Mission.MDS_VARIABLES().zutiRocketsSupply_Blue) {
                    Mission.MDS_VARIABLES().zutiRocketsSupply_Blue -= requestedRockets;
                } else {
                    requestedRockets = (int) Mission.MDS_VARIABLES().zutiRocketsSupply_Blue;
                    Mission.MDS_VARIABLES().zutiRocketsSupply_Blue = 0;
                }
            }
        }

        // -------------------PRINT OUT--------------------------------------------
        if (bp != null) {
            if (bp.zutiEnableResourcesManagement) {
                printDebugMessage("After deduction of >" + requestedRockets + "< rockets - Home Base(" + (int) bp.place.x + ", " + (int) bp.place.y + ")");
                printDebugMessage("  Rockets supply=" + bp.zutiRocketsSupply);
                printDebugMessage("======================================================");
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                printDebugMessage("After deduction of >" + requestedRockets + "< rockets - Red side");
                printDebugMessage("  Rockets supply=" + Mission.MDS_VARIABLES().zutiRocketsSupply_Red);
                printDebugMessage("======================================================");
            } else if (armyForLocation == 2) {
                printDebugMessage("After deduction of >" + requestedRockets + "< rockets - Blue side");
                printDebugMessage("  Rockets supply=" + Mission.MDS_VARIABLES().zutiRocketsSupply_Blue);
                printDebugMessage("======================================================");
            }
        }
        // ------------------------------------------------------------------------

        return requestedRockets;
    }

    /**
     * Method analyzes home base resources and calculates number or cargo "bombs" that can be generated
     * from them based on player side and side/home base resources.
     * 
     * @param requestedCargo
     * @param x
     * @param y
     * @return
     */
    public static long getCargoForPlayer(long requestedCargo, double x, double y) {
        if (!Mission.MDS_VARIABLES().enabledResourcesManagement_BySide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases)
            return requestedCargo;

        BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
        int armyForLocation = Front.army(x, y);

        int availableCargo = 0;

        for (int i = 0; i < requestedCargo; i++) {
            if (bp != null) {
                if (bp.zutiEnableResourcesManagement) {
                    if (checkHomeBaseForCargoResources(bp))
                        availableCargo++;
                }
            } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
                if (armyForLocation == 1) {
                    if (checkRedSideForCargoResources())
                        availableCargo++;
                } else if (armyForLocation == 2) {
                    if (checkBlueSideForCargoResources())
                        availableCargo++;
                }
            }
        }

        if (bp != null) {
            if (bp.zutiEnableResourcesManagement)
                printOutResourcesForHomeBase(bp);
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            printOutResourcesForSide(armyForLocation);
        }

        return availableCargo;
    }

    /**
     * Method checks cargo resources for specified home base
     * 
     * @param bp
     * @return
     */
    private static boolean checkHomeBaseForCargoResources(BornPlace bp) {
        long bullets = 0;
        long rockets = 0;
        int[] bombs = new int[] { 0, 0, 0, 0, 0, 0 };
        long fuel = 0;
        long engines = 0;
        long repairKits = 0;

        // Manage side resources - BLUE
        String cargoName = ZutiSupportMethods_Builder.BOMB_CARGO_NAME;
        switch (bp.army) {
            case 1:
                cargoName += "_Red";
                break;
            case 2:
                cargoName += "_Blue";
                break;
        }

        RRRItem cargoItem = (RRRItem) bp.objectsMap.get(cargoName);

        if (cargoItem == null)
            return false;

        bullets = cargoItem.bullets;
        rockets = cargoItem.rockets;
        bombs[0] = (int) cargoItem.bomb250;
        bombs[1] = (int) cargoItem.bomb500;
        bombs[2] = (int) cargoItem.bomb1000;
        bombs[3] = (int) cargoItem.bomb2000;
        bombs[4] = (int) cargoItem.bomb5000;
        bombs[5] = (int) cargoItem.bomb9999;
        fuel = cargoItem.fuel;
        engines = cargoItem.engines;
        repairKits = cargoItem.repairKits;

        if (bullets > bp.zutiBulletsSupply) {
            return false;
        }

        if (rockets > bp.zutiRocketsSupply) {
            return false;
        }

        if (bombs[0] > bp.zutiBombsSupply[0]) {
            return false;
        }

        if (bombs[1] > bp.zutiBombsSupply[1]) {
            return false;
        }

        if (bombs[2] > bp.zutiBombsSupply[2]) {
            return false;
        }

        if (bombs[3] > bp.zutiBombsSupply[3]) {
            return false;
        }

        if (bombs[4] > bp.zutiBombsSupply[4]) {
            return false;
        }

        if (bombs[5] > bp.zutiBombsSupply[5]) {
            return false;
        }

        if (fuel > bp.zutiFuelSupply) {
            return false;
        }

        if (engines > bp.zutiEnginesSupply) {
            return false;
        }

        if (repairKits > bp.zutiRepairKitsSupply) {
            return false;
        }

        bp.zutiBulletsSupply -= bullets;
        bp.zutiRocketsSupply -= rockets;
        bp.zutiBombsSupply[0] -= bombs[0];
        bp.zutiBombsSupply[1] -= bombs[1];
        bp.zutiBombsSupply[2] -= bombs[2];
        bp.zutiBombsSupply[3] -= bombs[3];
        bp.zutiBombsSupply[4] -= bombs[4];
        bp.zutiBombsSupply[5] -= bombs[5];
        bp.zutiFuelSupply -= fuel;
        bp.zutiEnginesSupply -= engines;
        bp.zutiRepairKitsSupply -= repairKits;

        return true;
    }

    private static boolean checkRedSideForCargoResources() {
        long bullets = 0;
        long rockets = 0;
        int[] bombs = new int[] { 0, 0, 0, 0, 0, 0 };
        long fuel = 0;
        long engines = 0;
        long repairKits = 0;

        // Manage side resources - BLUE
        String cargoName = ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Red";
        RRRItem cargoItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(cargoName);

        if (cargoItem == null)
            return false;

        bullets = cargoItem.bullets;
        rockets = cargoItem.rockets;
        bombs[0] = (int) cargoItem.bomb250;
        bombs[1] = (int) cargoItem.bomb500;
        bombs[2] = (int) cargoItem.bomb1000;
        bombs[3] = (int) cargoItem.bomb2000;
        bombs[4] = (int) cargoItem.bomb5000;
        bombs[5] = (int) cargoItem.bomb9999;
        fuel = cargoItem.fuel;
        engines = cargoItem.engines;
        repairKits = cargoItem.repairKits;

        if (bullets > Mission.MDS_VARIABLES().zutiBulletsSupply_Red) {
            return false;
        }

        if (rockets > Mission.MDS_VARIABLES().zutiRocketsSupply_Red) {
            return false;
        }

        if (bombs[0] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[0]) {
            return false;
        }

        if (bombs[1] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[1]) {
            return false;
        }

        if (bombs[2] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[2]) {
            return false;
        }

        if (bombs[3] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[3]) {
            return false;
        }

        if (bombs[4] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[4]) {
            return false;
        }

        if (bombs[5] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[5]) {
            return false;
        }

        if (fuel > Mission.MDS_VARIABLES().zutiFuelSupply_Red) {
            return false;
        }

        if (engines > Mission.MDS_VARIABLES().zutiEnginesSupply_Red) {
            return false;
        }

        if (repairKits > Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red) {
            return false;
        }

        Mission.MDS_VARIABLES().zutiBulletsSupply_Red -= bullets;
        Mission.MDS_VARIABLES().zutiRocketsSupply_Red -= rockets;
        Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] -= bombs[0];
        Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] -= bombs[1];
        Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] -= bombs[2];
        Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] -= bombs[3];
        Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] -= bombs[4];
        Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] -= bombs[5];
        Mission.MDS_VARIABLES().zutiFuelSupply_Red -= fuel;
        Mission.MDS_VARIABLES().zutiEnginesSupply_Red -= engines;
        Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red -= repairKits;

        return true;
    }

    private static boolean checkBlueSideForCargoResources() {
        long bullets = 0;
        long rockets = 0;
        int[] bombs = new int[] { 0, 0, 0, 0, 0, 0 };
        long fuel = 0;
        long engines = 0;
        long repairKits = 0;

        // Manage side resources - BLUE
        String cargoName = ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Blue";
        RRRItem cargoItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Blue.get(cargoName);

        if (cargoItem == null)
            return false;

        bullets = cargoItem.bullets;
        rockets = cargoItem.rockets;
        bombs[0] = (int) cargoItem.bomb250;
        bombs[1] = (int) cargoItem.bomb500;
        bombs[2] = (int) cargoItem.bomb1000;
        bombs[3] = (int) cargoItem.bomb2000;
        bombs[4] = (int) cargoItem.bomb5000;
        bombs[5] = (int) cargoItem.bomb9999;
        fuel = cargoItem.fuel;
        engines = cargoItem.engines;
        repairKits = cargoItem.repairKits;

        if (bullets > Mission.MDS_VARIABLES().zutiBulletsSupply_Blue) {
            return false;
        }

        if (rockets > Mission.MDS_VARIABLES().zutiRocketsSupply_Blue) {
            return false;
        }

        if (bombs[0] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0]) {
            return false;
        }

        if (bombs[1] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1]) {
            return false;
        }

        if (bombs[2] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2]) {
            return false;
        }

        if (bombs[3] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3]) {
            return false;
        }

        if (bombs[4] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4]) {
            return false;
        }

        if (bombs[5] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5]) {
            return false;
        }

        if (fuel > Mission.MDS_VARIABLES().zutiFuelSupply_Blue) {
            return false;
        }

        if (engines > Mission.MDS_VARIABLES().zutiEnginesSupply_Blue) {
            return false;
        }

        if (repairKits > Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue) {
            return false;
        }

        Mission.MDS_VARIABLES().zutiBulletsSupply_Blue -= bullets;
        Mission.MDS_VARIABLES().zutiRocketsSupply_Blue -= rockets;
        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] -= bombs[0];
        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] -= bombs[1];
        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] -= bombs[2];
        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] -= bombs[3];
        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] -= bombs[4];
        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] -= bombs[5];
        Mission.MDS_VARIABLES().zutiFuelSupply_Blue -= fuel;
        Mission.MDS_VARIABLES().zutiEnginesSupply_Blue -= engines;
        Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue -= repairKits;

        return true;
    }

    /**
     * Method searches for born place near given coordinates and checks if it has
     * enough engines to serve to the player. Its rockets supply counter is appropriately
     * adjusted.
     * 
     * @param requestedEngines
     * @param x
     * @param y
     * @return
     */
    public static long getEnginesForPlayer(long requestedEngines, double x, double y) {
        if (!Mission.MDS_VARIABLES().enabledResourcesManagement_BySide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases)
            return requestedEngines;

        BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
        int armyForLocation = Front.army(x, y);

        if (bp != null && bp.zutiEnableResourcesManagement) {
            if (requestedEngines <= bp.zutiEnginesSupply) {
                bp.zutiEnginesSupply -= requestedEngines;
            } else {
                requestedEngines = bp.zutiEnginesSupply;
                bp.zutiEnginesSupply = 0;
            }
            return (int) requestedEngines;
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                // Manage side resources
                if (requestedEngines <= Mission.MDS_VARIABLES().zutiEnginesSupply_Red) {
                    Mission.MDS_VARIABLES().zutiEnginesSupply_Red -= requestedEngines;
                } else {
                    requestedEngines = (int) Mission.MDS_VARIABLES().zutiEnginesSupply_Red;
                    Mission.MDS_VARIABLES().zutiEnginesSupply_Red = 0;
                }
                return requestedEngines;
            } else if (armyForLocation == 2) {
                // Manage side resources
                if (requestedEngines <= Mission.MDS_VARIABLES().zutiEnginesSupply_Blue) {
                    Mission.MDS_VARIABLES().zutiEnginesSupply_Blue -= requestedEngines;
                } else {
                    requestedEngines = (int) Mission.MDS_VARIABLES().zutiEnginesSupply_Blue;
                    Mission.MDS_VARIABLES().zutiEnginesSupply_Blue = 0;
                }
            } else
                requestedEngines = 0;
        } else
            requestedEngines = 0;

        // -------------------PRINT OUT--------------------------------------------
        if (bp != null && bp.zutiEnableResourcesManagement) {
            printDebugMessage("After deduction of >" + requestedEngines + "< engines - Home Base(" + (int) bp.place.x + ", " + (int) bp.place.y + ")");
            printDebugMessage("  Engines supply=" + bp.zutiEnginesSupply);
            printDebugMessage("======================================================");
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                printDebugMessage("After deduction of >" + requestedEngines + "< engines - Red side");
                printDebugMessage("  Engines supply=" + Mission.MDS_VARIABLES().zutiEnginesSupply_Red);
                printDebugMessage("======================================================");
            } else if (armyForLocation == 2) {
                printDebugMessage("After deduction of >" + requestedEngines + "< engines - Blue side");
                printDebugMessage("  Engines supply=" + Mission.MDS_VARIABLES().zutiEnginesSupply_Blue);
                printDebugMessage("======================================================");
            }
        }
        // ------------------------------------------------------------------------

        return requestedEngines;
    }

    /**
     * Method searches for born place near given coordinates and checks if it has
     * enough repair kits to serve to the player. Its repair kits supply counter is appropriately
     * adjusted.
     * 
     * @param requestedEngines
     * @param x
     * @param y
     * @return
     */
    public static long getRepairKitsForPlayer(long requestedKits, double x, double y) {
        if (!Mission.MDS_VARIABLES().enabledResourcesManagement_BySide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases)
            return requestedKits;

        BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
        int armyForLocation = Front.army(x, y);

        if (bp != null && bp.zutiEnableResourcesManagement) {
            // Born place has resource management... nice.
            if (requestedKits <= bp.zutiRepairKitsSupply) {
                bp.zutiRepairKitsSupply -= requestedKits;
            } else {
                requestedKits = (int) bp.zutiRepairKitsSupply;
                bp.zutiRepairKitsSupply = 0;
            }
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                // Manage side resources
                if (requestedKits <= Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red) {
                    Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red -= requestedKits;
                } else {
                    requestedKits = (int) Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red;
                    Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red = 0;
                }
            } else if (armyForLocation == 2) {
                // Manage side resources
                if (requestedKits <= Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue) {
                    Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue -= requestedKits;
                } else {
                    requestedKits = (int) Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue;
                    Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue = 0;
                }
            } else
                requestedKits = 0;
        } else
            requestedKits = 0;

        // -------------------PRINT OUT--------------------------------------------
        if (bp != null && bp.zutiEnableResourcesManagement) {
            printDebugMessage("After deduction of >" + requestedKits + "< repair kits - Home Base(" + (int) bp.place.x + ", " + (int) bp.place.y + ")");
            printDebugMessage("  Repair kits supply=" + bp.zutiRepairKitsSupply);
            printDebugMessage("======================================================");
        } else if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (armyForLocation == 1) {
                printDebugMessage("After deduction of >" + requestedKits + "< repair kits - Red side");
                printDebugMessage("  Repair kits supply=" + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red);
                printDebugMessage("======================================================");
            } else if (armyForLocation == 2) {
                printDebugMessage("After deduction of >" + requestedKits + "< repair kits - Blue side");
                printDebugMessage("  Repair kits supply=" + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue);
                printDebugMessage("======================================================");
            }
        }
        // ------------------------------------------------------------------------

        return requestedKits;
    }

    /**
     * Call this method when a object is destroyed.
     * 
     * @param objectName
     */
    public static void reduceResources(House house, boolean printResults) {
        String objectName = house.name();
        // System.out.println("Checking object: " + objectName);
        if (ZutiSupportMethods.isAmmoBoxObject(objectName) || ZutiSupportMethods.isFuelTankObject(objectName) || ZutiSupportMethods.isWorkshopObject(objectName)) {
            objectName = objectName.substring(objectName.indexOf("$") + 1, objectName.length());
            RRRItem rrrItem = null;
            Point3d housePosition = house.pos.getAbsPoint();
            boolean updateSideResources = true;

            // Check if destroyed object was part of any home base. If so, reduce resources at that home base.
            // ----------------------------------------------------------------
            if (LAST_RESOURCES_BORN_PLACE != null) {
                boolean isOnBornPlace = ZutiSupportMethods_Net.isOnBornPlace(housePosition.x, housePosition.y, LAST_RESOURCES_BORN_PLACE);
                if (isOnBornPlace && LAST_RESOURCES_BORN_PLACE.zutiEnableResourcesManagement) {
                    // OK, this home base has resources management... update resources for it.
                    reduceHomeBaseResources(LAST_RESOURCES_BORN_PLACE, objectName, printResults);

                    updateSideResources = false;
                } else {
                    // It is not on current known home base, try searching for new one
                    BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(housePosition.x, housePosition.y);
                    if (bp != null && bp.zutiEnableResourcesManagement) {
                        // Yes, house was part of born place and it has resources management, update it...
                        reduceHomeBaseResources(bp, objectName, printResults);
                        // Set it as new last known home base
                        LAST_RESOURCES_BORN_PLACE = bp;

                        updateSideResources = false;
                    }
                }
            } else {
                // No known home base, search for possible one.
                // It is not on current known home base, try searching for new one
                BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(housePosition.x, housePosition.y);
                if (bp != null && bp.zutiEnableResourcesManagement) {
                    // Yes, house was part of born place and it has resources management, update it...
                    reduceHomeBaseResources(bp, objectName, printResults);
                    // Set it as new last known home base
                    LAST_RESOURCES_BORN_PLACE = bp;

                    updateSideResources = false;
                }
            }
            // ----------------------------------------------------------------
            // System.out.println( updateSideResources + ", " + Mission.MDS_VARIABLES().enabledResourcesManagement_BySide);
            if (updateSideResources && Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
                int army = Front.army(housePosition.x, housePosition.y);
                if (army == 1) {
                    // TODO: Storebror: Null Check added
                    if (Mission.MDS_VARIABLES().objectsMap_Red != null) {
                        rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(objectName);
                        if (rrrItem != null) {
                            // Ok, objects were loaded into has, now it's time that we update counters...
                            Mission.MDS_VARIABLES().zutiBulletsSupply_Red -= rrrItem.bullets;
                            Mission.MDS_VARIABLES().zutiRocketsSupply_Red -= rrrItem.rockets;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] -= rrrItem.bomb250;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] -= rrrItem.bomb500;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] -= rrrItem.bomb1000;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] -= rrrItem.bomb2000;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] -= rrrItem.bomb5000;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] -= rrrItem.bomb9999;
                            Mission.MDS_VARIABLES().zutiFuelSupply_Red -= rrrItem.fuel;
                            Mission.MDS_VARIABLES().zutiEnginesSupply_Red -= rrrItem.engines;
                            Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red -= rrrItem.repairKits;
                        }
                        // TODO: Storebror: Null Check added
                    }
                } else if (army == 2) {
                    // TODO: Storebror: Null Check added
                    if (Mission.MDS_VARIABLES().objectsMap_Blue != null) {
                        rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Blue.get(objectName);
                        if (rrrItem != null) {
                            // Ok, objects were loaded into has, now it's time that we update counters...
                            Mission.MDS_VARIABLES().zutiBulletsSupply_Blue -= rrrItem.bullets;
                            Mission.MDS_VARIABLES().zutiRocketsSupply_Blue -= rrrItem.rockets;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] -= rrrItem.bomb250;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] -= rrrItem.bomb500;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] -= rrrItem.bomb1000;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] -= rrrItem.bomb2000;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] -= rrrItem.bomb5000;
                            Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] -= rrrItem.bomb9999;
                            Mission.MDS_VARIABLES().zutiFuelSupply_Blue -= rrrItem.fuel;
                            Mission.MDS_VARIABLES().zutiEnginesSupply_Blue -= rrrItem.engines;
                            Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue -= rrrItem.repairKits;
                        }
                        // TODO: Storebror: Null Check added
                    }
                }

                if (printResults)
                    printOutResourcesForSide(army);
            }
        }
    }

    /**
     * Call this method when a moving object reaches it's final destination.
     * 
     * @param chiefName
     * @param chiefPosition
     * @param printResults
     */
    public static void addResourcesFromMovingRRRObjects(String chiefName, Point3d chiefPosition, int chiefArmy, float survivability, boolean printResults) {
        // System.out.println("Checking moving object: " + chiefName + ", survivability: " + survivability);
        if (ZutiSupportMethods.isMovingRRRObject(chiefName, null)) {
            RRRItem rrrItem = null;
            boolean updateSideResources = true;

            // Check if destroyed object was part of any home base. If so, reduce resources at that home base.
            // ----------------------------------------------------------------
            if (LAST_RESOURCES_BORN_PLACE != null) {
                boolean isOnBornPlace = ZutiSupportMethods_Net.isOnBornPlace(chiefPosition.x, chiefPosition.y, LAST_RESOURCES_BORN_PLACE);
                if (isOnBornPlace && LAST_RESOURCES_BORN_PLACE.zutiEnableResourcesManagement) {
                    // TODO: OK, this home base has resources management... update resources for it.
                    addResourcesToHomeBase(LAST_RESOURCES_BORN_PLACE, chiefArmy, chiefName, survivability, printResults);

                    updateSideResources = false;
                } else {
                    // It is not on current known home base, try searching for new one
                    BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(chiefPosition.x, chiefPosition.y);
                    if (bp != null && bp.zutiEnableResourcesManagement) {
                        // Yes, house was part of born place and it has resources management, update it...
                        addResourcesToHomeBase(bp, chiefArmy, chiefName, survivability, printResults);
                        // Set it as new last known home base
                        LAST_RESOURCES_BORN_PLACE = bp;

                        updateSideResources = false;
                    }
                }
            } else {
                // No known home base, search for possible one.
                // It is not on current known home base, try searching for new one
                BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(chiefPosition.x, chiefPosition.y);
                if (bp != null && bp.zutiEnableResourcesManagement) {
                    // Yes, house was part of born place and it has resources management, update it...
                    addResourcesToHomeBase(bp, chiefArmy, chiefName, survivability, printResults);
                    // Set it as new last known home base
                    LAST_RESOURCES_BORN_PLACE = bp;

                    updateSideResources = false;
                }
            }
            // ----------------------------------------------------------------
            // System.out.println( updateSideResources + ", " + Mission.MDS_VARIABLES().enabledResourcesManagement_BySide);
            if (updateSideResources && Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
                if (chiefArmy == 1 && Mission.MDS_VARIABLES().objectsMap_Red != null) {
                    rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Red.get(chiefName);
                    if (rrrItem != null) {
                        // Ok, objects were loaded into has, now it's time that we update counters...
                        Mission.MDS_VARIABLES().zutiBulletsSupply_Red += rrrItem.bullets * survivability;
                        Mission.MDS_VARIABLES().zutiRocketsSupply_Red += rrrItem.rockets * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] += rrrItem.bomb250 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] += rrrItem.bomb500 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] += rrrItem.bomb1000 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] += rrrItem.bomb2000 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] += rrrItem.bomb5000 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] += rrrItem.bomb9999 * survivability;
                        Mission.MDS_VARIABLES().zutiFuelSupply_Red += rrrItem.fuel * survivability;
                        Mission.MDS_VARIABLES().zutiEnginesSupply_Red += rrrItem.engines * survivability;
                        Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red += rrrItem.repairKits * survivability;
                    }
                } else if (chiefArmy == 2 && Mission.MDS_VARIABLES().objectsMap_Blue != null) {
                    rrrItem = (RRRItem) Mission.MDS_VARIABLES().objectsMap_Blue.get(chiefName);
                    if (rrrItem != null) {
                        // Ok, objects were loaded into has, now it's time that we update counters...
                        Mission.MDS_VARIABLES().zutiBulletsSupply_Blue += rrrItem.bullets * survivability;
                        Mission.MDS_VARIABLES().zutiRocketsSupply_Blue += rrrItem.rockets * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] += rrrItem.bomb250 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] += rrrItem.bomb500 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] += rrrItem.bomb1000 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] += rrrItem.bomb2000 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] += rrrItem.bomb5000 * survivability;
                        Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] += rrrItem.bomb9999 * survivability;
                        Mission.MDS_VARIABLES().zutiFuelSupply_Blue += rrrItem.fuel * survivability;
                        Mission.MDS_VARIABLES().zutiEnginesSupply_Blue += rrrItem.engines * survivability;
                        Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue += rrrItem.repairKits * survivability;
                    }
                }

                if (printResults)
                    printOutResourcesForSide(chiefArmy);
            }
        }
    }

    /**
     * Reduce resources for given home base.
     * 
     * @param bp
     * @param objectName
     * @param printResults
     */
    private static void reduceHomeBaseResources(BornPlace bp, String objectName, boolean printResults) {
        if (bp == null || bp.objectsMap == null || objectName == null) {
            // System.out.println("BP = NULL!");
            return;
        }

        RRRItem rrrItem = (RRRItem) bp.objectsMap.get(objectName);
        if (rrrItem != null) {
            // Ok, objects were loaded into has, now it's time that we update counters...
            bp.zutiBulletsSupply -= rrrItem.bullets;
            bp.zutiRocketsSupply -= rrrItem.rockets;
            bp.zutiBombsSupply[0] -= rrrItem.bomb250;
            bp.zutiBombsSupply[1] -= rrrItem.bomb500;
            bp.zutiBombsSupply[2] -= rrrItem.bomb1000;
            bp.zutiBombsSupply[3] -= rrrItem.bomb2000;
            bp.zutiBombsSupply[4] -= rrrItem.bomb5000;
            bp.zutiBombsSupply[5] -= rrrItem.bomb9999;
            bp.zutiFuelSupply -= rrrItem.fuel;
            bp.zutiEnginesSupply -= rrrItem.engines;
            bp.zutiRepairKitsSupply -= rrrItem.repairKits;

            if (printResults) {
                printOutResourcesForHomeBase(bp);
            }
        }
    }

    /**
     * Add resources for given home base from moving object.
     * 
     * @param bp
     * @param objectName
     * @param printResults
     */
    private static void addResourcesToHomeBase(BornPlace bp, int objectArmy, String objectName, float survivability, boolean printResults) {
        if (bp == null || objectName == null || !Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            // System.out.println("BP = NULL!");
            return;
        }

        Map objectsMap = null;
        switch (objectArmy) {
            case 1:
                objectsMap = Mission.MDS_VARIABLES().objectsMap_Red;
                break;
            case 2:
                objectsMap = Mission.MDS_VARIABLES().objectsMap_Blue;
                break;
        }

        if (objectsMap == null)
            return;

        RRRItem rrrItem = (RRRItem) objectsMap.get(objectName);
        if (rrrItem != null) {
            // Ok, objects were loaded into has, now it's time that we update counters...
            // System.out.println("Current BP Bullets: " + bp.zutiBulletsSupply + ", convoy bullets: " + rrrItem.bullets + ", convoy survivabilits: " + survivability);

            bp.zutiBulletsSupply += rrrItem.bullets * survivability;
            bp.zutiRocketsSupply += rrrItem.rockets * survivability;
            bp.zutiBombsSupply[0] += rrrItem.bomb250 * survivability;
            bp.zutiBombsSupply[1] += rrrItem.bomb500 * survivability;
            bp.zutiBombsSupply[2] += rrrItem.bomb1000 * survivability;
            bp.zutiBombsSupply[3] += rrrItem.bomb2000 * survivability;
            bp.zutiBombsSupply[4] += rrrItem.bomb5000 * survivability;
            bp.zutiBombsSupply[5] += rrrItem.bomb9999 * survivability;
            bp.zutiFuelSupply += rrrItem.fuel * survivability;
            bp.zutiEnginesSupply += rrrItem.engines * survivability;
            bp.zutiRepairKitsSupply += rrrItem.repairKits * survivability;

            if (printResults) {
                printOutResourcesForHomeBase(bp);
            }
        }
    }

    public static void printOutResourcesForHomeBase(BornPlace bp) {
        if (bp != null && bp.zutiEnableResourcesManagement) {
            printDebugMessage("Resources status - Home Base(" + (int) bp.place.x + ", " + (int) bp.place.y + ")");
            printDebugMessage("  Bullets       supply=" + bp.zutiBulletsSupply);
            printDebugMessage("  Rockets       supply=" + bp.zutiRocketsSupply);
            printDebugMessage("  Bombs{250kg}  supply=" + bp.zutiBombsSupply[0]);
            printDebugMessage("  Bombs{500kg}  supply=" + bp.zutiBombsSupply[1]);
            printDebugMessage("  Bombs{1000kg} supply=" + bp.zutiBombsSupply[2]);
            printDebugMessage("  Bombs{2000kg} supply=" + bp.zutiBombsSupply[3]);
            printDebugMessage("  Bombs{5000kg} supply=" + bp.zutiBombsSupply[4]);
            printDebugMessage("  Bombs{9999kg} supply=" + bp.zutiBombsSupply[5]);
            printDebugMessage("  Fuel          supply=" + bp.zutiFuelSupply);
            printDebugMessage("  Engines       supply=" + bp.zutiEnginesSupply);
            printDebugMessage("  Repair kits   supply=" + bp.zutiRepairKitsSupply);
            printDebugMessage("======================================================");
        }
    }

    public static void printOutResourcesForSide(int army) {
        if (Mission.MDS_VARIABLES().enabledResourcesManagement_BySide) {
            if (army == 1) {
                printDebugMessage("Resources - Red side");
                printDebugMessage("  Bullets  = " + Mission.MDS_VARIABLES().zutiBulletsSupply_Red);
                printDebugMessage("  Rockets  = " + Mission.MDS_VARIABLES().zutiRocketsSupply_Red);
                printDebugMessage("  Bomb250  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[0]);
                printDebugMessage("  Bomb500  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[1]);
                printDebugMessage("  Bomb1000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[2]);
                printDebugMessage("  Bomb2000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[3]);
                printDebugMessage("  Bomb5000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[4]);
                printDebugMessage("  Bomb9999 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[5]);
                printDebugMessage("  Fuel     = " + Mission.MDS_VARIABLES().zutiFuelSupply_Red);
                printDebugMessage("  Engines  = " + Mission.MDS_VARIABLES().zutiEnginesSupply_Red);
                printDebugMessage("  Repairs  = " + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red);
                printDebugMessage("================================");
            } else if (army == 2) {
                printDebugMessage("Resources - Blue side");
                printDebugMessage("  Bullets  = " + Mission.MDS_VARIABLES().zutiBulletsSupply_Blue);
                printDebugMessage("  Rockets  = " + Mission.MDS_VARIABLES().zutiRocketsSupply_Blue);
                printDebugMessage("  Bomb250  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0]);
                printDebugMessage("  Bomb500  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1]);
                printDebugMessage("  Bomb1000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2]);
                printDebugMessage("  Bomb2000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3]);
                printDebugMessage("  Bomb5000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4]);
                printDebugMessage("  Bomb9999 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5]);
                printDebugMessage("  Fuel     = " + Mission.MDS_VARIABLES().zutiFuelSupply_Blue);
                printDebugMessage("  Engines  = " + Mission.MDS_VARIABLES().zutiEnginesSupply_Blue);
                printDebugMessage("  Repairs  = " + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue);
                printDebugMessage("================================");
            }
        }
    }
    private static int debugLevel = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT = 0;
    
    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_ZSM_RESM", DEBUG_DEFAULT);
        return debugLevel;
    }
    
    public static void printDebugMessage(String theMessage) {
        if (curDebugLevel() == 0) return;
        System.out.println(theMessage);
    }
}