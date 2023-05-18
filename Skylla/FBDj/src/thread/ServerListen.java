package thread;

import java.io.IOException;

import mainController.ConnectController;
import mainController.MainController;
import model.QueueObj;
import utility.SocketConnection;
import viewController.MainWindowController;

public class ServerListen implements Runnable {
    // Keep going
    private boolean  stop;
    // Socket
    SocketConnection conn;

    public ServerListen(SocketConnection conn) {
        this.conn = conn;
        this.stop = false;
    }

    // Exit the Listen thread
    public void stop() {
        stop = true;
        MainController.writeDebugLogFile(1, "ServerListen.stop: Stop called.");
    }

    public void reConnect() {
        ConnectController.reConnect();
    }

    // Start the Listen thread
    public void run() {
        String line = null;

        while (!stop) {
            try {
                line = conn.getIn().readLine();

                // We didn't really connect.
                // Probably a mismatched config with confs.ini
                if (line == null) {
                    MainWindowController.setStatusMessage("Server Not Accepting Connection...Stopping Listener");
                    MainController.writeDebugLogFile(1, "ServerListen.run: Server Not Acception Connection, Stopping Listener!");
                    MainController.CONNECTED = false;
                    stop();
                }
                // Good connection
                else {
                    MainController.doQueue(new QueueObj(QueueObj.Source.SOCKET, line, 0));
                }
            } catch (IOException e) {
                // Something went wrong with IO
                if (!stop) {
                    MainController.writeDebugLogFile(1, "SocketListen.run: IOException! at Listen thread run\n" + e.toString());

                    e.printStackTrace();
                    stop();
                    reConnect();
                    return;
                }
                // Gracefully exit with no errors
                else {
                    MainController.writeDebugLogFile(1, "SocketListen.run: Listen thread is stopped");
                }
            } catch (NullPointerException e) {
                // We had a null pointer exception, usually caused by a bad connection
                if (!stop) {
                    MainController.writeDebugLogFile(1, "SocketListen.run: NullPointerException! at Listen thread run\n" + e.toString());
                    e.printStackTrace();
                    stop();
                    reConnect();
                    return;
                }
                // Gracefully exit with no errors
                else {
                    MainController.writeDebugLogFile(1, "SocketListen.run: Listen thread is stopped");
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "SocketListen.run - Error Unhandled exception from line (" + line + "): " + ex);
            }
        }
    }
}
