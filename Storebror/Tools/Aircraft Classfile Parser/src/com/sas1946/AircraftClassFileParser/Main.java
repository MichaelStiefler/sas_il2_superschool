package com.sas1946.AircraftClassFileParser;

import java.io.File;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

public class Main {
    private static String currentPath;

    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File theFolder = chooser.getSelectedFile();
            ClassFileParser classFileParser = new ClassFileParser();
            for (int i=0; i<5; i++)
                classFileParser.parseFiles(theFolder.getAbsolutePath());
        } else {
            ClassFileParser classFileParser = new ClassFileParser();
            currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            classFileParser.parseFiles(currentPath + File.separator + "aircraft_source");
        }
    }
}
