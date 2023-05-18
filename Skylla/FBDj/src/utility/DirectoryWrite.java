package utility;

import java.io.File;

import mainController.MainController;

public class DirectoryWrite {
    public static boolean createDirectory(String directory) {
        // Create a directory; all ancestor directories must exist
        boolean success = (new File("./" + directory)).mkdir();
        if (!success) {
            MainController.writeDebugLogFile(1, "DirectoryWrite.createDirectory: Failed to create directory! " + directory);
        }

        return success;
    }

    public static void main(String[] args) {
        DirectoryWrite.createDirectory("test");
    }
}
