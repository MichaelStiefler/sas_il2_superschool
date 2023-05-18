package utility;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class DirectoryRead {
    // Returns list of all directories
    public static ArrayList<String> getDirectories() {
        ArrayList<String> directories = new ArrayList<String>();

        File dir = new File("./config/");

        // This filter only returns directories
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        File[] dirs = dir.listFiles(fileFilter);

        for (int i = 0; i < dirs.length; i++) {
            directories.add(dirs[i].getName());
        }

        return directories;
    }

    // Returns directory exists boolean
    public static boolean directoryExists(String directory) {
        File checkDir = new File(directory);
        return (checkDir.exists() && checkDir.isDirectory());
    }

    public static void main(String[] args) {
        ArrayList<String> directories = DirectoryRead.getDirectories();

        for (int i = 0; i < directories.size(); i++) {
            System.out.println(directories.get(i));
        }
    }
}
