package mainController;

import java.util.ArrayList;

import utility.FileRead;
import utility.FileWrite;

class FileController {
    public static ArrayList<String> fileRead(String filename) {
        return FileRead.getFile(filename);
    }

    public static Object fileReadSerialized(String filename) {
        return FileRead.getFileSerialized(filename);
    }

    public static void fileWrite(String directory, String filename, ArrayList<String> data, boolean append) {
        FileWrite.writeFile(directory, filename, data, append);
    }

    public static void fileWriteSerialized(String directory, String filename, Object o) {
        FileWrite.writeFileSerialized(directory, filename, o);
    }

    public static void checkDirectory(String directory) {
        FileWrite.checkDirectory(directory);
    }
}
