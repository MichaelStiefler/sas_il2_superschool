package mainController;

import java.util.ArrayList;

import model.QueueObj;

public class MainParseController {
    public static void processQueue(ArrayList<QueueObj> queueObjects) {
        for (int i = 0; i < queueObjects.size(); i++) {
            if (queueObjects.get(i).getSource().equals(QueueObj.Source.EVENTLOG)) {
                EventLogParser.parseEvent(queueObjects.get(i).getEventTime(), queueObjects.get(i).getData());
                continue;
            }

            if (queueObjects.get(i).getSource().equals(QueueObj.Source.SOCKET)) {
                MainController.COMMANDPARSE.parseCommand(queueObjects.get(i).getData());
                continue;
            }

            if (queueObjects.get(i).getSource().equals(QueueObj.Source.GUI)) {
                GUICommandController.processGUICommand(queueObjects.get(i).getGuiCommand());
                continue;
            }
        }
    }
}
