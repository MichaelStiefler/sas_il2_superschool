package utility;

import mainController.MainController;

public class SocketSendData {
    public static void sendData(SocketConnection conn, String data) {
        // Send command to server
        conn.getOut().println(data);
        data = StringUtilities.stringReplace(data, "%", "%%");
        MainController.writeDebugLogFile(2, "SocketSendData.sendData - Command (" + data + ") sent to server!");
        conn.getOut().flush();
    }
}
