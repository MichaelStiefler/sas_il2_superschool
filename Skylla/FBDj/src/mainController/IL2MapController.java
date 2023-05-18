package mainController;

import java.util.ArrayList;

import model.IL2Map;
import utility.FileRead;

public class IL2MapController {

    public static void initialize(String il2MapFileName) {
        loadIL2Maps(il2MapFileName);
    }

    public static void il22MapAdd(String name, boolean bigMap, String displayName, long xOffset, long yOffset) {
        IL2Map newIL2Map = new IL2Map(name, bigMap, displayName);
        newIL2Map.setXOffset(xOffset);
        newIL2Map.setYOffset(yOffset);
        MainController.IL2MAPS.put(name, newIL2Map);
    }

    private static void loadIL2Maps(String il2MapFileName) {
        String mapName;
        String mapSize;
        long xOffset;
        long yOffset;
        String displayName;
        boolean bigMap = false;
        int displayNameEndIndex = 0;
        int mapCount = 0;

        ArrayList<String> il2MapFile = new ArrayList<String>(80);

        il2MapFile = FileRead.getFile(il2MapFileName);

        // Loop through all lines of the file
        for (String line : il2MapFile) {
            try {
                bigMap = false;
                mapName = line.split(",")[0].trim();
                xOffset = Long.valueOf(line.split(",")[1].trim());
                yOffset = Long.valueOf(line.split(",")[2].trim());
                mapSize = line.split(",")[3].trim();
                displayName = line.split(",")[4].trim();
                displayNameEndIndex = displayName.lastIndexOf("\"");
                displayName = displayName.substring(1, displayNameEndIndex);
                if (mapSize.equalsIgnoreCase("Small") || mapSize.equalsIgnoreCase("Large")) {
                    if (mapSize.equalsIgnoreCase("Large"))
                        bigMap = true;
                    IL2MapController.il22MapAdd(mapName, bigMap, displayName, xOffset, yOffset);
                    mapCount++;
                } else {
                    MainController.writeDebugLogFile(1, "IL2MapController.loadIL2Maps - Error Illegal value for mapSize( " + mapSize + " ) in IL2Maps file for map( " + mapName + " )");
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "IL2MapController.loadIL2Maps - Error loading IL2Maps File Record (" + mapCount + ") Error: " + ex);
            }
        }
        MainController.writeDebugLogFile(1, "IL2MapController.loadIL2Maps Loaded data for (" + mapCount + ") Maps");
    } // loadIL2Maps

}
