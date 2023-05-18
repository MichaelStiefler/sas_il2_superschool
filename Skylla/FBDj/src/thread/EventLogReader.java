package thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import mainController.MainController;
import model.QueueObj;
import utility.Time;

/*
 * Loop through Event Log as it's being written by IL2 and parse, process the lines
 */
public class EventLogReader implements Runnable {
    String          eventLogFile;
    private boolean stop;

    public EventLogReader(String eventLogFile) {
        this.eventLogFile = eventLogFile;
        this.stop = false;
    }

    public void stop() {
        stop = true;
    }

    public void run() {
        Boolean fileOpened = false;
        BufferedReader reader = null;
        long lastEventTime = 0;

        try {
            String eventLogFile = this.eventLogFile;

            // Check to see if the EventLog file exists and is readable. If it is then
            // read until the end of File before we
            // start processing data from it.
            // If the file does not exist then create it.
            File eventLog = new File(eventLogFile);

            if (eventLog.exists()) {
                if (eventLog.canRead()) {
                    reader = new BufferedReader(new FileReader(eventLog));
                    fileOpened = true;
                    try {
                        String eventLine = reader.readLine();
                        while (eventLine != null) {
                            eventLine = reader.readLine();
                        }
                        MainController.writeDebugLogFile(1, "EventLogReader.run - EventLog File (" + eventLogFile + ") Opened");
                    } catch (IOException ex) {
                        MainController.writeDebugLogFile(1, "EventLogReader.run - Error reading from eventlog file beginning section" + ex);
                    }
                } else {
                    MainController.writeDebugLogFile(1, "EventLogReader.run - Error unable to read eventlog file: " + eventLogFile);
                }
            } else {
                try {
                    eventLog.createNewFile();
                    MainController.writeDebugLogFile(1, "EventLogReader.run - Created new EventLog File: " + eventLogFile);
                } catch (IOException ex) {
                    MainController.writeDebugLogFile(1, "EventLogReader.run - Error creating empty eventlog file: " + eventLogFile + "Error: " + ex);
                }
            }
            if (!fileOpened) {
                reader = new BufferedReader(new FileReader(eventLog));
            }

            try {
                // Loop through the eventlog indefinitly
                while (!stop) {
                    // Read a line from the eventlog
                    String eventLine = reader.readLine();

                    if (eventLine != null) {
                        // Get current Time. If the time is not more than the previous time
                        // then use previous time + 1.
                        long eventTime = Time.getTime();
                        if (eventTime <= lastEventTime) {
                            eventTime = lastEventTime + 1;
                        }
                        lastEventTime = eventTime;

                        // Add data to the main thread eventlog queue
                        MainController.doQueue(new QueueObj(QueueObj.Source.EVENTLOG, eventLine, eventTime));
                    } else { // No new lines in EventLog sleep for 2 seconds then wake up and
                             // read again
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {}
                    }
                } // While loop

                // Close connections if we stop the loop
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    MainController.writeDebugLogFile(1, "EventLogReader.run() IOException! Error closing reader: " + e.toString());
                }
            } catch (IOException ioe) {
                MainController.writeDebugLogFile(1, "EventLogReader.run - Error reading through eventlog file: " + eventLogFile + "Error: " + ioe);
            }
        } catch (FileNotFoundException fnf) {
            MainController.writeDebugLogFile(1, "EventLogReader.run Error: " + fnf);
        }
        MainController.writeDebugLogFile(1, "EventLogReader.run() EventLog File closed");
    } // end of run
}
