package mainController;

import utility.SocketSendData;

class ServerCommandController {
    public static synchronized void serverCommandSend(String command) {
        SocketSendData.sendData(MainController.SERVERCONN, command);
    }
}