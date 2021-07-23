package com.maddox.rts;

import java.io.File;

public class IniFileDated extends IniFile {

    public IniFileDated() {
        super("");
    }

    public IniFileDated(String s) {
        super(s, 1);
    }

    public IniFileDated(String s, int i) {
        super(s, i);
        this.filePath = HomePath.toFileSystemName(this.fileName, 0);
//        System.out.println("new Instance IniFileDated(" + s + ", " + i + ")");
    }

    public long getModified() {
        File file = new File(this.filePath);
//        System.out.println(this.fileName + " getModified()=" + file.lastModified());
        return file.lastModified();
    }

    public boolean isFileChanged() {
        if (this.lastModified == this.getModified()) // System.out.println("isFileChanged() = false");
            return false;
//        System.out.println("isFileChanged() = true (lastModified=" + this.lastModified + ", getModified()=" + this.getModified() + ")");
        return true;
//        return this.lastModified != this.getModified();
    }

    public boolean loadFile() {
        if (this.filePath == null) this.filePath = HomePath.toFileSystemName(this.fileName, 0);
//        System.out.println("IniFileDated " + this.filePath + " loadFile() object=" + this.hashCode());
        this.lastModified = this.getModified();
//        System.out.println(this.fileName + " loadFile() lastModified=" + this.lastModified);
        return super.loadFile();
    }

    public void parseLines() {
        super.parseLines();
    }

    public void saveFile() {
        super.saveFile();
        this.lastModified = this.getModified();
    }

    public void saveFile(String s) {
        super.saveFile(s);
        this.lastModified = this.getModified();
    }

    private String filePath = "";
    private long   lastModified;
}
