package com.maddox.sas1946.il2.util;

import java.io.File;
import java.io.IOException;

/**
 * "FileTools" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to provide helper methods for File Handling.
 * <p>
 * 
 * @version 1.1.4
 * @since 1.1.2
 * @author SAS~Storebror
 */
public class FileTools {

    /**
     * Override default Constructor to avoid instanciation
     * @throws Exception
     * @since 1.1.2
     */
    private FileTools() throws Exception{
        throw new Exception("Class com.maddox.sas1946.il2.util.CommonTools cannot be instanciated!");
    }
    
    /**
     * Checks whether a file / directory represented by the given path is a symbolic link.
     * @param path
     *            The path of the file / directory in question
     * @return Whether or not the file / directory represents a symbolic link.
     * @since 1.1.2
     */
    public final static boolean isSymbolicLink(String path) {
        return doIsSymbolicLink(path);
    }
   
    /**
     * Checks whether a file / directory is a symbolic link.
     * @param file
     *            The file / directory in question
     * @return Whether or not the file / directory represents a symbolic link.
     * @since 1.1.2
     */
    public final static boolean isSymbolicLink(File file) {
        return doIsSymbolicLink(file);
    }

    /**
     * Resolves a file / directory path representing a symbolic link to it's real file / directory path on the given file system.
     * @param path
     *            The path of the file / directory representing a symbolic link
     * @return The real file / directory path on the given file system.
     * @since 1.1.2
     */
    public final static String resolveSymbolicLink(String path) {
        return doResolveSymbolicLink(path);
    }
    
    /**
     * Resolves a file / directory representing a symbolic link to it's real file / directory on the given file system.
     * @param file
     *            The file / directory in question
     * @return The real file / directory on the given file system.
     * @since 1.1.2
     */
    public final static File resolveSymbolicLink(File file) {
        return doResolveSymbolicLink(file);
    }

    /**
     * Gets the sizes (in bytes) of all files in a given directory (excluding sub-directories).
     * @param path
     *            The folder which has to be scanned
     * @param searchPattern
     *            The search pattern for files to be searched for
     * @param scanSubFolders
     *            Boolean parameter specifying whether or not subfolders should be scanned
     * @return Array of {@link FileSizeRecord} elements holding the names and sizes of all files found in the given directory.
     * @since 1.1.4
     */
    public final static FileSizeRecord[] getFileSizes(String path, String searchPattern, boolean scanSubFolders) {
        return doGetFileSizes(path, searchPattern, scanSubFolders);
    }

    /**
     * Gets the sizes (in bytes) of all files in a given directory (excluding sub-directories).
     * @param path
     *            The directory in question
     * @return The real file / directory on the given file system.
     * @since 1.1.4
     */

    
    
    /**
     * Class holding folder names, names and sizes of files found when calling the {@link FileTools#getFileSizes(String, String, boolean) getFileSizes} method.
     * 
     * @author SAS~Storebror
     * @since 1.1.4
     */
    public static class FileSizeRecord implements Comparable {
        
        /**
         * The folder name value of a file found.
         * The folder name is relative to the search path specified in the {@link FileTools#getFileSizes(String, String, boolean) getFileSizes} method.
         */
        public String folder;
        
        /**
         * The name value of a file found.
         */
        public String name;
        
        /**
         * The size value of a file found.
         */
        public long size;
        
        /**
         * Default Constructor for the {@link FileSizeRecord} class.
         */
        public FileSizeRecord() {
            folder = new String();
            name = new String();
            size = 0;
        }
        
        /**
         * Converts a {@link FileSizeRecord} to printable format.
         */
        public String toString() {   
            return "[" + folder + "\\" + name + "=" + size + "]";   
        }
        
        /**
         * Compare two {@link FileSizeRecord} according to {@link Comparable} implementation standards
         * @param obj1 first {@link FileSizeRecord} to compare
         * @param obj2 second {@link FileSizeRecord} to compare
         * @return Returns -1, 0 or 1 depending on the natural name's sort order result according to {@link Comparable} implementation standards
         */
        public int compare(Object obj1, Object obj2) {
            if (!(obj1 instanceof FileSizeRecord) || !(obj2 instanceof FileSizeRecord)) return 0;
            FileSizeRecord fsr1 = (FileSizeRecord)obj1;
            FileSizeRecord fsr2 = (FileSizeRecord)obj2;
            return (fsr1.folder + fsr1.name).compareTo(fsr2.folder + fsr2.name);
        }
        
        /**
         * Compare the current {@link FileSizeRecord} element to another {@link FileSizeRecord} element according to {@link Comparable} implementation standards
         * @param obj the other {@link FileSizeRecord} element to compare with
         * @return Returns -1, 0 or 1 depending on the natural name's sort order result according to {@link Comparable} implementation standards
         */
        public int compareTo(Object obj) {
            if (!(obj instanceof FileSizeRecord)) return 0;
            FileSizeRecord fsr = (FileSizeRecord)obj;
            return (this.folder + this.name).compareTo(fsr.folder + fsr.name);
        }
    }
    
    // *****************************************************************************************************************************************************************************************************
    // Private implementation section.
    // Do whatever you like here but keep it private to this class.
    // *****************************************************************************************************************************************************************************************************

    private final static boolean doIsSymbolicLink(File file) {
        try {
            return doIsSymbolicLink(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private final static boolean doIsSymbolicLink(String path) {
        if (!loadNative()) return false;
        String canonicalPath;
        try {
            canonicalPath = new File(path).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return jniIsSymbolicLink(canonicalPath);
    }
    
    private final static String doResolveSymbolicLink(String path) {
        if (!loadNative()) return path;
        String canonicalPath;
        try {
            canonicalPath = new File(path).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return path;
        }
        return jniResolveSymbolicLink(canonicalPath);
    }
    
    private final static File doResolveSymbolicLink(File file) {
        String resolvedPath;
        try {
            resolvedPath=doResolveSymbolicLink(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            resolvedPath=doResolveSymbolicLink(file.getPath());
        }
        return new File(resolvedPath);
    }

    private final static FileSizeRecord[] doGetFileSizes(String path, String searchPattern, boolean scanSubFolders) {
        if (!loadNative()) return new FileSizeRecord[]{};
        return jniGetFileSizes(path, searchPattern, scanSubFolders);
    }
    
    private long start;
    
    // *****************************************************************************************************************************************************************************************************
    // Native Methods implementation section.
    // Do whatever you like here but keep it private to this class.
    // *****************************************************************************************************************************************************************************************************

    private final static boolean loadNative() {
        if (nativeLoaded) return true;
        if (initAttemptDone) return false;
        initAttemptDone = true;
        try {
            System.loadLibrary("SAS Common Utils");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        nativeLoaded = true;
        return true;
    }

    private static boolean nativeLoaded = false;
    private static boolean initAttemptDone = false;
    private static native boolean jniIsSymbolicLink(String path); 
    private static native String jniResolveSymbolicLink(String path); 
    private static native FileSizeRecord[] jniGetFileSizes(String path, String searchPattern, boolean scanSubFolders); 
}
