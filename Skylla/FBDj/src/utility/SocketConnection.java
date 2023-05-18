package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import viewController.MainWindowController;

public class SocketConnection {
    // Connection attributes
    private boolean        isConnected;
    String                 ipAddress;
    int                    port;

    // Socket
    private Socket         socket;
    private PrintWriter    out;
    private BufferedReader in;

    public SocketConnection(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public boolean connect() {
        try {
            MainWindowController.setStatusMessage("Connecting to Server...");

            socket = new Socket(ipAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            isConnected = true;

            return true;

        } catch (UnknownHostException e) {
            System.err.println("ServerConnection.connect: UnknownHostException! at connection");
            System.err.println(e.toString());
            isConnected = false;
            return false;
        } catch (IOException e) {
            System.err.println("ServerConnection.connect: IOException! at connection");
            System.err.println(e.toString());
            isConnected = false;
            return false;

        }

    }

    public void disconnect() {
        try {
            // Close connections
            out.close();
            in.close();
            socket.close();
            isConnected = false;
        } catch (IOException e) {
            System.err.println("ServerConnection.disconnect: IOException! at close connections");
            System.err.println(e.toString());
            return;
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public int getPort() {
        return port;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }
}
