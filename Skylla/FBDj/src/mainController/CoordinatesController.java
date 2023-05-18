package mainController;

import utility.Coordinates;

public class CoordinatesController {
    public static String getCoordinates(boolean bigMap, double xIn, double yIn) {
        return Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), bigMap, xIn, yIn);
    }

    public static String getCoordinates(String mapName, boolean bigMap, double xIn, double yIn) {
        return Coordinates.getCoordinates(mapName, bigMap, xIn, yIn);
    }
}
