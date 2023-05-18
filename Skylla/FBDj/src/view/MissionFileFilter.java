package view;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

public class MissionFileFilter extends FileFilter {

    private static ArrayList<String> extensionList;

    public MissionFileFilter(ArrayList<String> extensionList) {
        MissionFileFilter.extensionList = extensionList;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            for (String extensionItem : extensionList) {
                if (extension.equals(extensionItem)) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    // The description of this filter
    public String getDescription() {
        return "Mission Files Only";
    }

    public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
