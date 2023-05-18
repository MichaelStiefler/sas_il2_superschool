package mainController;

import java.util.ArrayList;
import java.util.HashMap;

import model.IL2Map;
import model.IL2StaticObject;
import model.IL2StaticObject.ObjectType;
import model.MultiVehicleObject;
import model.Weapon;

public class IL2DataLoadController {

    private enum FileSectionTypes {
        RED, BLUE, AIR, NONE
    }

    public static void loadAllDataFiles() {
        // Load map data
        MainController.IL2MAPS = new HashMap<String, IL2Map>();
        IL2MapController.initialize("IL2Maps.txt");

        // Load IL2 Static Objects
        MainController.IL2STATICOBJECTS = new HashMap<String, IL2StaticObject>(610);
        IL2DataLoadController.initializeStaticObjects("IL2StaticObjects.csv");

        MainController.IL2MULTIOBJECTS = new HashMap<String, MultiVehicleObject>(100);
        IL2DataLoadController.initializeMultiVehicleObjects("IL2MultiObjects.csv");

        // Load IL2 Plane Weapons
        MainController.IL2PLANELOADOUTS = new HashMap<String, ArrayList<Weapon>>();
        IL2PlaneLoadoutController.initialize("IL2PlaneLoadouts.csv");

        // Load IL2 Regiments
        MainController.IL2REGIMENTS = new HashMap<String, Integer>();
        IL2DataLoadController.initializeRegiments("IL2Regiments.txt");

        // Load IL2 Air Classes
        MainController.IL2AIRCLASSES = new HashMap<String, String>();
        IL2DataLoadController.initializeAirClasses("IL2Air.txt");
    }

    public static void initializeStaticObjects(String fileName) {
        IL2DataLoadController.loadStaticObjects(fileName);
    }

    public static void initializeMultiVehicleObjects(String fileName) {
        IL2DataLoadController.loadMultiVehicleObjects(fileName);
    }

    public static void initializeRegiments(String fileName) {
        IL2DataLoadController.loadRegiments(fileName);
    }

    public static void initializeAirClasses(String fileName) {
        IL2DataLoadController.loadAirClasses(fileName);
    }

    private static void il2StaticObjectAdd(String name, IL2StaticObject.ObjectType objectType, String displayName, int extraPoints) {
        HashMap<String, Integer> il2Data = new HashMap<String, Integer>();
        IL2StaticObject newIL2Object = new IL2StaticObject(name, objectType, displayName);
        il2Data = MySQLConnectionController.getIL2StaticData(name);
        newIL2Object.setIl2ObjectID(il2Data.get("objectId"));
//		newIL2Object.setPointValue(il2Data.get("pointValue"));
//		newIL2Object.setBaseValue(il2Data.get("baseValue"));
        newIL2Object.setPointValue(extraPoints);
        if (extraPoints > 0)
            MainController.writeDebugLogFile(2, "IL2DataLoadController.il2StaticObjectAdd - Object (" + name + ") extra Points: " + extraPoints);
        // MainController.writeDebugLogFile(2, "IL2DataLoadController.il2StaticObjectAdd - Object ("+name+") Base Value: "+newIL2Object.getBaseValue());

        MainController.IL2STATICOBJECTS.put(name, newIL2Object);
    }

    private static void loadStaticObjects(String il2StaticObjectsFileName) {
        String il2ObjectName;
        ObjectType il2ObjectType;
        String il2DisplayName;
        int totalObjectCount = 0;

        ArrayList<String> il2StaticObjectFile = new ArrayList<String>(610);

        // Read IL2 Static Object File
        il2StaticObjectFile = FileController.fileRead(il2StaticObjectsFileName);

        int displayNameEndIndex = 0;
        String tempObjectType;

        // Loop through all lines of the file
        for (String line : il2StaticObjectFile) {
            try {
                tempObjectType = line.split("\",\"")[0];
                il2ObjectName = line.split("\",\"")[1];
                il2DisplayName = line.split("\",\"")[2];
                displayNameEndIndex = il2DisplayName.lastIndexOf("\"");
                int extraPoints = Integer.valueOf(il2DisplayName.substring(displayNameEndIndex + 2));
                il2DisplayName = il2DisplayName.substring(0, displayNameEndIndex);
                il2ObjectType = il2StaticObjectGetType(tempObjectType.substring(1));

                // Add to static IL2Objects
                IL2DataLoadController.il2StaticObjectAdd(il2ObjectName, il2ObjectType, il2DisplayName, extraPoints);
                totalObjectCount++;
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "IL2DataLoadController.loadStaticObjects Error: " + ex);
                MainController.writeDebugLogFile(1, " **** Line(" + line + ")");
            }
        }
        MainController.writeDebugLogFile(1, "IL2DataLoadController.loadStaticObjects Loaded data for (" + totalObjectCount + ") Static Objects");
    } // loadStaticObjects

    private static void loadMultiVehicleObjects(String il2MultiVehicleObjectsFileName) {
        String name;
        String tempVehicleType;
        String vehicleName;
        int newStaticObjectsAdded = 0;
        int multiObjectsAdded = 0;

        ArrayList<String> multiVehicleFile = new ArrayList<String>(610);

        // Read IL2 Static Object File
        multiVehicleFile = FileController.fileRead(il2MultiVehicleObjectsFileName);

        // Loop through all lines of the file
        for (String line : multiVehicleFile) {
            line = line.trim();
            if (line.length() > 0) {
                try {
                    name = line.split("\",\"")[0];
                    vehicleName = line.split("\",\"")[1];
                    tempVehicleType = line.split("\",\"")[2];
                    name = name.substring(1);
                    int vehicleTypeIndex = tempVehicleType.lastIndexOf("\"");
                    tempVehicleType = tempVehicleType.substring(0, vehicleTypeIndex);
                    IL2StaticObject.ObjectType vehicleType = il2StaticObjectGetType(tempVehicleType);

                    MultiVehicleObject multiVehicleObject = MainController.IL2MULTIOBJECTS.get(name);
                    if (multiVehicleObject == null) {
                        multiVehicleObject = new MultiVehicleObject(name);
                        MainController.IL2MULTIOBJECTS.put(name, multiVehicleObject);
                        multiObjectsAdded++;
                    }

                    IL2StaticObject vehicleObject = null;
                    // If the vehicle is not in the Static Objects List Add it.
                    if (!MainController.IL2STATICOBJECTS.containsKey(vehicleName)) {
                        IL2DataLoadController.il2StaticObjectAdd(vehicleName, vehicleType, vehicleName, 0);
                        newStaticObjectsAdded++;
                        MainController.writeDebugLogFile(1, "IL2DataLoadController.loadMultiVehicleObjects - New Static Object: (" + vehicleType + "-" + vehicleName + ")");
                    }
                    vehicleObject = MainController.IL2STATICOBJECTS.get(vehicleName);
                    multiVehicleObject.getVehicleList().add(vehicleObject);

                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "IL2DataLoadController.loadMultiVehicleObjects - Error Unhandled Exception loading data: " + ex);
                }
            }
        }
        MainController.writeDebugLogFile(1, "IL2DataLoadController.loadMultiVehicleObjects Loaded data for (" + multiObjectsAdded + ") Multi Vehicle Objects and (" + newStaticObjectsAdded + ") new Static Objects");
    }

    private static void loadRegiments(String fileName) {
        int redRegimentCount = 0;
        int blueRegimentCount = 0;
        FileSectionTypes fileSection = FileSectionTypes.NONE;
        String regiment = "";

        ArrayList<String> regimentsFile = new ArrayList<String>(1000);

        // Read IL2 Static Object File
        regimentsFile = FileController.fileRead(fileName);

        // Loop through all lines of the file
        for (String line : regimentsFile) {
            try {
                line = line.trim();
                if (line.indexOf("[Red]") > -1) {
                    fileSection = FileSectionTypes.RED;
                } else if (line.indexOf("[Blue]") > -1) {
                    fileSection = FileSectionTypes.BLUE;
                } else if (line.startsWith("[")) {
                    fileSection = FileSectionTypes.NONE;
                }
                switch (fileSection) {
                    case RED:
                        if (!line.startsWith("[")) {
                            if (line.length() > 0) {
                                regiment = line.split(" ")[0];
                                MainController.IL2REGIMENTS.put(regiment, 1);
                                redRegimentCount++;
                            }
                        }
                        break;
                    case BLUE:
                        if (!line.startsWith("[")) {
                            if (line.length() > 0) {
                                regiment = line.split(" ")[0];
                                MainController.IL2REGIMENTS.put(regiment, 2);
                                blueRegimentCount++;
                            }
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "IL2DataLoadController.loadRegiments - Error Unhandled Exception Reading Regiments file: " + ex);
                MainController.writeDebugLogFile(1, " **** Line: " + line);
            }
        }
        MainController.writeDebugLogFile(1, "Loaded (" + redRegimentCount + ") Red Regiments & (" + blueRegimentCount + ") Blue Regiments");
    }

    private static void loadAirClasses(String fileName) {
        int airClassesCount = 0;
        FileSectionTypes fileSection = FileSectionTypes.NONE;

        ArrayList<String> airClassesFile = new ArrayList<String>(1000);

        // Read IL2 Static Object File
        airClassesFile = FileController.fileRead(fileName);

        // Loop through all lines of the file
        for (String line : airClassesFile) {
            try {
                line = line.trim();
                if (line.indexOf("[AIR]") > -1) {
                    fileSection = FileSectionTypes.AIR;
                } else if (line.startsWith("[")) {
                    fileSection = FileSectionTypes.NONE;
                }
                switch (fileSection) {
                    case AIR:
                        if (!line.startsWith("[")) {
                            if (line.length() > 0) {
                                try {
                                    String planeName = line.split(" ")[0];
                                    String tempClass = line.substring(line.indexOf("air."));
                                    String airClass = tempClass.split(" ")[0];
                                    MainController.IL2AIRCLASSES.put(airClass, planeName);
                                    airClassesCount++;
                                } catch (Exception ex) {
                                    MainController.writeDebugLogFile(1, "IL2DataLoadController.loadAirClasses - Error Unhandled exception: " + ex);
                                    MainController.writeDebugLogFile(1, " *** Line: " + line);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "IL2DataLoadController.loadRegiments - Error Unhandled Exception Reading Regiments file: " + ex);
                MainController.writeDebugLogFile(1, " **** Line: " + line);
            }
        }
        MainController.writeDebugLogFile(1, "Loaded (" + airClassesCount + ") Air Classes");
    }

    public static String il2StaticObjectGetTypeString(ObjectType objectType) {
        switch (objectType) {
            case AAA:
                return "AAA";
            case ARTILLERY:
                return "Artillery";
            case TANK:
                return "Tank";
            case SHIP:
                return "Ship";
            case PLANE:
                return "Plane";
            case CAR:
                return "Car";
            case WAGON:
                return "Wagon";
            case SPLANE:
                return "SPlane";
            case PILOT:
                return "Pilot";
            case AIPLANE:
                return "AIPlane";
            case RADIO:
                return "Radio";
            default:
                return "Misc";
        }
    }

    // Return IL2 Object Type from String passed in
    public static ObjectType il2StaticObjectGetType(String stringType) {
        if (stringType.equalsIgnoreCase("AAA")) {
            return ObjectType.AAA;
        } else if (stringType.equalsIgnoreCase("ARTILLERY")) {
            return ObjectType.ARTILLERY;
        } else if (stringType.equalsIgnoreCase("CAR")) {
            return ObjectType.CAR;
        } else if (stringType.equalsIgnoreCase("PLANE")) {
            return ObjectType.PLANE;
        } else if (stringType.equalsIgnoreCase("TANK")) {
            return ObjectType.TANK;
        } else if (stringType.equalsIgnoreCase("SHIP")) {
            return ObjectType.SHIP;
        } else if (stringType.equalsIgnoreCase("WAGON") || stringType.equalsIgnoreCase("GROUND")) {
            return ObjectType.WAGON;
        } else if (stringType.equalsIgnoreCase("SPLANE")) {
            return ObjectType.SPLANE;
        } else if (stringType.equalsIgnoreCase("PILOT")) {
            return ObjectType.PILOT;
        } else if (stringType.equalsIgnoreCase("BRIDGE")) {
            return ObjectType.BRIDGE;
        } else if (stringType.equalsIgnoreCase("AIPLANE")) {
            return ObjectType.AIPLANE;
        } else if (stringType.equalsIgnoreCase("RADIO")) {
            return ObjectType.RADIO;
        } else {
            return ObjectType.MISC;
        }
    }

}
