package utility;

import java.util.Scanner;

import mainController.MainController;

public class UnicodeFormatter {

    /**
     * This Method converts a string with embedded unicode characters to a string
     * with the unicode spelled out as a string
     * 
     * @param string
     *            with embedded unicode characters
     * @return string which contains unicode spelled out in string format
     * 
     */
    public static String convertUnicodeToString(String str) {
        StringBuffer ostr = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if ((ch >= 0x0020) && (ch <= 0x007e))	// Does the char need to be converted to unicode?
            {
                ostr.append(ch);					// No.
            } else 									// Yes.
            {
                ostr.append("\\u");				// standard unicode format.
                String hex = Integer.toHexString(str.charAt(i) & 0xFFFF);	// Get hex value of the char.
                for (int j = 0; j < 4 - hex.length(); j++)
                    // Prepend zeros because unicode requires 4 digits
                    ostr.append("0");
//				ostr.append(hex.toLowerCase());		// standard unicode format.
                ostr.append(hex.toUpperCase());
                // ostr.append(hex.toLowerCase(Locale.ENGLISH));
            }
        }

        return (new String(ostr));		// Return the stringbuffer cast as a string.

    }

    /**
     * This Method converts a string that contains the unicode expressed as a string \\uxxxx
     * to a string that contains the actual embedded unicode characters
     * 
     * @param string
     *            with embededed unicode expressed as a string
     * @return string which contains the embeded unicode
     */
    @SuppressWarnings("resource")
    public static String convertAsciiStringToUnicode(String asciiText) {
        boolean finished = false;
        String newText = "";
        String tempText = asciiText;

        try {
            while (!finished) {
                int unicodeIndex = tempText.indexOf("\\u");
                if (unicodeIndex > tempText.length() || unicodeIndex < 0) {
                    finished = true;
                    if (tempText.length() > 0) {
                        newText = newText + tempText;
                    }
                } else {
                    newText = newText + tempText.substring(0, unicodeIndex);
                    String asciiCode = tempText.substring(unicodeIndex + 2, unicodeIndex + 6);
                    long b = new Scanner(asciiCode).nextLong(16);
                    String unicode = "" + (char) b;
                    newText = newText + unicode;
                    tempText = tempText.substring(unicodeIndex + 6);
                }
            }

            return newText;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.convertUnicodeName - Unhandled Exception processing Unicode in (" + asciiText + "): " + ex);
            return asciiText;
        }
    }

    public static void main(String[] args) {

        String bob = "\\u00E5This \\u20AC a test\\u00E5";
        String convertedBob = convertAsciiStringToUnicode(bob);
        System.out.println("new:" + convertedBob);
    }
} // class
