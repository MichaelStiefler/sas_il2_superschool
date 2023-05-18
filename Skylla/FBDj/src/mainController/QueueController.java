package mainController;

import java.util.ArrayList;

import model.QueueObj;

class QueueController {
    public static ArrayList<QueueObj> doQueue(QueueObj newObj) {
        // If we have data then add to the queue
        if (newObj != null) {
            MainController.QUEUEOBJECTS.add(newObj);
            return null;
        }

        // Since no new data was passed, then load up the queue contents and pass it back
        ArrayList<QueueObj> returnArray = new ArrayList<QueueObj>();
        for (int i = 0; i < MainController.QUEUEOBJECTS.size(); i++) {
            returnArray.add(MainController.QUEUEOBJECTS.get(i));
        }
        MainController.QUEUEOBJECTS.clear();

        return returnArray;
    }
}
