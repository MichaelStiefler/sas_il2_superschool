package utility;

import mainController.MainController;

public class Coordinates {
    private static final String[] mapGridsBig   = { "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC", "BD", "BE", "BF", "BG",
            "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ", "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU",
            "CV", "CW", "CX", "CY", "CZ", "DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ" };

    private static final String[] mapGridsSmall = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    public static String getCoordinates(String mapName, boolean bigMap, double xIn, double yIn) {
        String cor;
        String[][] keypad = new String[3][3];
        keypad[0][0] = "1";
        keypad[0][1] = "4";
        keypad[0][2] = "7";
        keypad[1][0] = "2";
        keypad[1][1] = "5";
        keypad[1][2] = "8";
        keypad[2][0] = "3";
        keypad[2][1] = "6";
        keypad[2][2] = "9";
        int keypadXValue = 0;
        int keypadYValue = 0;

        long x = Math.round(xIn);
        long y = Math.round(yIn);
        double xReal = (xIn + MainController.IL2MAPS.get(mapName).getXOffset()) / 10000.0;
        double yReal = (yIn + MainController.IL2MAPS.get(mapName).getYOffset()) / 10000.0;

        // Set coordinates
        try {
            // X coordinate
            String ltr;

            x = x + MainController.IL2MAPS.get(mapName).getXOffset();
            y = y + MainController.IL2MAPS.get(mapName).getYOffset();

            if (!bigMap)
                ltr = mapGridsSmall[Math.round(x / 10000)];
            else
                ltr = mapGridsBig[Math.round(x / 10000)];

            // Y coordinate
            int nbr = Math.round(y / 10000) + 1;

            double xRemainder = 10 * (xReal - (Math.round(x / 10000)));
            double yRemainder = 10 * (yReal - (Math.round(y / 10000)));
            if (xRemainder < 3.3333) {
                keypadXValue = 0;
            } else if (xRemainder >= 3.3333 && xRemainder < 6.6666) {
                keypadXValue = 1;
            } else {
                keypadXValue = 2;
            }
            if (yRemainder < 3.3333) {
                keypadYValue = 0;
            } else if (yRemainder >= 3.3333 && yRemainder < 6.6666) {
                keypadYValue = 1;
            } else {
                keypadYValue = 2;
            }

            // Build coordinate
            cor = ltr + nbr + "." + keypad[keypadXValue][keypadYValue];

        } catch (IndexOutOfBoundsException ioobe) {
            MainController.writeDebugLogFile(1, "Coordinates.getCoordinates - Error IndexOutOfBounds Exception Map(" + mapName + ") X(" + xIn + ") Y(" + yIn + ") : " + ioobe);
            return "error";
        }
        // This probably can't happen since the division is always by 10000, but
        // just in case
        catch (ArithmeticException ae) {
            MainController.writeDebugLogFile(1, "Coordinates.getCoordinates - Error ArithmeticException Map(" + mapName + ") X(" + xIn + ") Y(" + yIn + ") : " + ae);
            return "error";
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "Coordinates.getCoordinates - Error ArithmeticException Map(" + mapName + ") X(" + xIn + ") Y(" + yIn + ") : " + ex);
            return "error";
        }

        return cor;
    }

    public static void main(String[] args) {
        double x = 24191;
        double y = 5709;

        String result = Coordinates.getCoordinates("Iwo/load", true, x, y);
        System.out.println(result);
    }
}
