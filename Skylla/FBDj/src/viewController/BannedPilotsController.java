package viewController;

import mainController.MainController;
import mainController.PilotBanController;
import model.PilotBan;

public class BannedPilotsController {

    // Update the Ban model with new data.
    public static void updateBannedPilot(String ipAddress, String name, String reason, long banDuration) {
        String banKey = name + "(IP)" + ipAddress;
        PilotBan banRecord = null;
        banRecord = MainController.getBannedPilots().get(banKey);
        if (banRecord == null) {
            PilotBanController.pilotBanAdd(name, ipAddress, reason, banDuration);
        } else {
            banRecord.setName(name);
            banRecord.setDuration(banDuration);
            banRecord.setBanner(reason);
        }
    }

    public static void removePilotBan(String name, String IPAddress) {
        PilotBanController.pilotBanRemove(name, IPAddress, null);
    }

    public static void saveBannedPilots() {
        PilotBanController.pilotBansWrite();
    }

    public static void cancelBannedPilotChanges() {
        MainController.setBannedPilots(PilotBanController.pilotBansLoad());
    }

}
