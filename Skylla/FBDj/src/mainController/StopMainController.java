package mainController;

public class StopMainController implements Runnable {
    public void run() {
        ConnectController.disconnect();
    }
}
