package utility;

import java.util.ArrayList;

public class FileLoggerFormat {
    public static ArrayList<String> getLogString(String data) {
        String ENDOFLINE = "\r";
        ArrayList<String> logData = new ArrayList<String>();
        logData.add("[" + Time.getTime(Time.getTime()) + "] " + data + ENDOFLINE);

        return logData;
    }
}
