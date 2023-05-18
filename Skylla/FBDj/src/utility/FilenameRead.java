package utility;

/*
 * Returns String[] of file names in a specified directory
 */

import java.io.File;

public class FilenameRead {
    public static String[] getFileNames(String directory) {
        File myDir = new File(directory);
        String[] fileNames = myDir.list();

        return fileNames;
    }

    public static void main(String[] args) {
        String[] fileNames = FilenameRead.getFileNames("./");

        for (int i = 0; i < fileNames.length; i++) {
            System.out.println(fileNames[i]);
        }
    }
}
