package mainController;

import java.util.ArrayList;
import java.util.HashMap;

import model.IL2StaticObject;
import model.IL2StaticObject.ObjectType;

public class IL2StaticObjectController {
    public static void initialize(String il2StaticObjectsFileName) {
        IL2StaticObjectController.loadStaticObjects(il2StaticObjectsFileName);
    }

    public static void il2StaticObjectAdd(String name, IL2StaticObject.ObjectType objectType, String displayName) {
        HashMap<String, Integer> il2Data = new HashMap<String, Integer>();
        IL2StaticObject newIL2Object = new IL2StaticObject(name, objectType, displayName);
        il2Data = MySQLConnectionController.getIL2StaticData(name);
        newIL2Object.setIl2ObjectID(il2Data.get("objectId"));
        newIL2Object.setPointValue(il2Data.get("pointValue"));
//		newIL2Object.setBaseValue(il2Data.get("baseValue"));
        MainController.IL2STATICOBJECTS.put(name, newIL2Object);
    }

    private static void loadStaticObjects(String il2StaticObjectsFileName) {
        String il2ObjectName;
        ObjectType il2ObjectType;
        String il2DisplayName;
        int objectCount = 0;

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
                il2DisplayName = il2DisplayName.substring(0, displayNameEndIndex);
                if (tempObjectType.equals("\"AAA")) {
                    il2ObjectType = ObjectType.AAA;
                } else if (tempObjectType.equals("\"Artillery")) {
                    il2ObjectType = ObjectType.ARTILLERY;
                } else if (tempObjectType.equals("\"Tank")) {
                    il2ObjectType = ObjectType.TANK;
                } else if (tempObjectType.equals("\"Ship")) {
                    il2ObjectType = ObjectType.SHIP;
                } else if (tempObjectType.equals("\"Car")) {
                    il2ObjectType = ObjectType.CAR;
                } else if (tempObjectType.equals("\"Ground")) {
                    il2ObjectType = ObjectType.WAGON;
                } else if (tempObjectType.equals("\"Plane")) {
                    il2ObjectType = ObjectType.PLANE;
                } else if (tempObjectType.equals("\"Bridge")) {
                    il2ObjectType = ObjectType.BRIDGE;
                } else {
                    il2ObjectType = ObjectType.MISC;
                }

                // Add to static IL2Objects
                IL2StaticObjectController.il2StaticObjectAdd(il2ObjectName, il2ObjectType, il2DisplayName);
                objectCount++;
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "IL2StaticObjectController.loadStaticObjects Error: " + ex);
            }
        }
        MainController.writeDebugLogFile(1, "IL2StaticObjectController.loadStaticObjects Loaded data for (" + objectCount + ") Static Objects");
    } // loadStaticObjects

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
            default:
                return "Misc";
        }
    }

    // Return IL2 Object Type from String passed in
    public static ObjectType il2StaticObjectGetType(String stringType) {
        if (stringType.equals("AAA")) {
            return ObjectType.AAA;
        } else if (stringType.equals("ARTILLERY")) {
            return ObjectType.ARTILLERY;
        } else if (stringType.equals("CAR")) {
            return ObjectType.CAR;
        } else if (stringType.equals("PLANE")) {
            return ObjectType.PLANE;
        } else if (stringType.equals("TANK")) {
            return ObjectType.TANK;
        } else if (stringType.equals("SHIP")) {
            return ObjectType.SHIP;
        } else if (stringType.equals("WAGON")) {
            return ObjectType.WAGON;
        } else if (stringType.equals("SPLANE")) {
            return ObjectType.SPLANE;
        } else if (stringType.equals("PILOT")) {
            return ObjectType.PILOT;
        } else if (stringType.equals("BRIDGE")) {
            return ObjectType.BRIDGE;
        } else {
            return ObjectType.MISC;
        }
    }

}
