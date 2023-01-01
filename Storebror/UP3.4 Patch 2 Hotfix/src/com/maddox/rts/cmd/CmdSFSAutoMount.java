/*
 * New conf.ini parameters for [Mods] Section:
 *   sfsloglevel = <n>
 *
 *                n is an integer value from a binary array:
 *
 *                Index 0: Log Errors
 *                Index 1: Log Success
 *
 *                Example (binary index)
 *                1 = Log Errors enabled
 *                0 = Log Success disabled
 *
 *                Hexadecimal representation of the values above = 0x01
 *                Decimal: 1
 *
 *                conf-ini entry:
 *                sfsloglevel=1
 *
 *                Default value is "sfsloglevel=1", this applies when no value is set in conf.ini
 */

package com.maddox.rts.cmd;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Map;

import com.maddox.il2.engine.Config;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.SFS;

public class CmdSFSAutoMount extends Cmd {

    public Object exec(CmdEnv cmdenv, Map map) {
        String theSFSFolderName = null;
        boolean flag = false;
        if (nargs(map, "MOUNT") > 0) {
            theSFSFolderName = arg(map, "MOUNT", 0);
            try {
                this.AutoMountSFSFiles(new File(theSFSFolderName));
            } catch (Exception exception1) {
                bMountError = true;
                this.ERR_HARD("SFS files from folder (" + theSFSFolderName + ") NOT Mounted: " + exception1.getMessage());
                exception1.printStackTrace();
            }
            flag = true;
        }
        if (nargs(map, "UNMOUNT") > 0) {
            theSFSFolderName = arg(map, "UNMOUNT", 0);
            try {
                this.AutoUnMountSFSFiles(new File(theSFSFolderName));
            } catch (Exception exception) {
                this.ERR_HARD("SFS files from folder  (" + theSFSFolderName + ") NOT UnMounted: " + exception.getMessage());
                exception.printStackTrace();
            }
            flag = true;
        }
        if (flag) return CmdEnv.RETURN_OK;
        else {
            this.ERR_HARD("Bad command format");
            return null;
        }
    }

    private File[] CreateFileList(File theSFSFolder) {
        File[] retVal = theSFSFolder.listFiles(new FilenameFilter() {
            public boolean accept(File theFileFolder, String theFileName) {
                return theFileName.toLowerCase().endsWith(".sfs");
            }
        });
        Arrays.sort(retVal); // Make sure the list of files is sorted lexicographically.
        return retVal;
//        return theSFSFolder.listFiles(new FilenameFilter() {
//            public boolean accept(File theFileFolder, String theFileName) {
//                return theFileName.toLowerCase().endsWith(".sfs");
//            }
//        });
    }

    private File[] CreateSubFolderList(File theSFSFolder) {
        File[] retVal = theSFSFolder.listFiles(new FileFilter() {
            public boolean accept(File theFile) {
                return theFile.isDirectory();
            }
        });
        Arrays.sort(retVal); // Make sure the list of folders is sorted lexicographically.
        return retVal;
//        return theSFSFolder.listFiles(new FileFilter() {
//            public boolean accept(File theFile) {
//                return theFile.isDirectory();
//            }
//        });
    }

    private void AutoMountSFSFiles(File theSFSFolder) {
        if (logLevel == SFS_LOGLEVEL_NOT_INITIALIZED) logLevel = Config.cur.ini.get("Mods", "sfsloglevel", SFS_LOGLEVEL_ERROR);
        File[] theSFSFileList = this.CreateFileList(theSFSFolder);
        if (theSFSFileList.length < 1) return;
        if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.println("AutoMounting SFS files from folder " + theSFSFolder.getPath() + " now...");
        for (int i = 0; i < theSFSFileList.length; i++)
            try {
                if (theSFSFileList[i].getName().startsWith("-")) {
                    if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.println("Skipping AutoMount of file " + theSFSFileList[i].getParent() + File.separator + theSFSFileList[i].getName().substring(1) + " (disabled)");
                    continue;
                }
                if (theSFSFileList[i].isDirectory()) continue;
                if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.print("Trying to AutoMount " + theSFSFileList[i].getPath() + "... ");
                SFS.mount(theSFSFileList[i].getPath(), 0);
                if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.println("mounted successfully!");
            } catch (Exception e) {
                if ((logLevel & SFS_LOGLEVEL_ERROR) != 0) System.out.println("mount failed, see error below:");
                if ((logLevel & SFS_LOGLEVEL_ERROR) != 0) System.out.println(e.getMessage());
            }
        if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.println("AutoMounting SFS files from folder " + theSFSFolder.getPath() + " finished.");
        File[] theSFSSubfolderList = this.CreateSubFolderList(theSFSFolder);
        for (int i = 0; i < theSFSSubfolderList.length; i++)
            try {
                if (theSFSSubfolderList[i].getName().startsWith("-")) {
                    if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.println("Skipping AutoMount of subfolder " + theSFSSubfolderList[i].getParent() + File.separator + theSFSSubfolderList[i].getName().substring(1) + " (disabled)");
                    continue;
                }
                this.AutoMountSFSFiles(theSFSSubfolderList[i]);
            } catch (Exception e) {
                if ((logLevel & SFS_LOGLEVEL_ERROR) != 0) System.out.println("Automount invocation for subfolder " + theSFSSubfolderList[i].getPath() + " failed, see error below:");
                if ((logLevel & SFS_LOGLEVEL_ERROR) != 0) System.out.println(e.getMessage());
            }
    }

    private void AutoUnMountSFSFiles(File theSFSFolder) {
        if (logLevel == SFS_LOGLEVEL_NOT_INITIALIZED) logLevel = Config.cur.ini.get("Mods", "sfsloglevel", SFS_LOGLEVEL_ERROR);
        File[] theSFSFileList = this.CreateFileList(theSFSFolder);
        if (theSFSFileList.length < 1) return;
        if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.println("AutoUnMounting SFS files from folder " + theSFSFolder.getPath() + " now...");
        for (int i = 0; i < theSFSFileList.length; i++)
            try {
                if (theSFSFileList[i].getName().startsWith("-")) continue;
                if (theSFSFileList[i].isDirectory()) continue;
                if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.print("Trying to AutoUnMount " + theSFSFileList[i].getPath() + "... ");
                SFS.unMount(theSFSFileList[i].getPath());
                if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.println("unmounted successfully!");
            } catch (Exception e) {
                if ((logLevel & SFS_LOGLEVEL_ERROR) != 0) System.out.println("unmount failed, see error below:");
                if ((logLevel & SFS_LOGLEVEL_ERROR) != 0) System.out.println(e.getMessage());
            }
        if ((logLevel & SFS_LOGLEVEL_SUCCESS) != 0) System.out.println("AutoUnMounting SFS files from folder " + theSFSFolder.getPath() + " finished.");
        File[] theSFSSubfolderList = this.CreateSubFolderList(theSFSFolder);
        for (int i = 0; i < theSFSSubfolderList.length; i++)
            try {
                if (theSFSSubfolderList[i].getName().startsWith("-")) continue;
                this.AutoUnMountSFSFiles(theSFSSubfolderList[i]);
            } catch (Exception e) {
                if ((logLevel & SFS_LOGLEVEL_ERROR) != 0) System.out.println("Autounmount invocation for subfolder " + theSFSSubfolderList[i].getPath() + " failed, see error below:");
                if ((logLevel & SFS_LOGLEVEL_ERROR) != 0) System.out.println(e.getMessage());
            }
    }

    public CmdSFSAutoMount() {
        this.param.put("MOUNT", null);
        this.param.put("UNMOUNT", null);
        this._properties.put("NAME", "sfsautomount");
        this._levelAccess = 0;
    }

    public static final String MOUNT                        = "MOUNT";
    public static final String UNMOUNT                      = "UNMOUNT";
    public static boolean      bMountError                  = false;

    private static final int   SFS_LOGLEVEL_ERROR           = 0x1;
    private static final int   SFS_LOGLEVEL_SUCCESS         = 0x2;
    private static final int   SFS_LOGLEVEL_NOT_INITIALIZED = -1;
    private static int         logLevel                     = SFS_LOGLEVEL_NOT_INITIALIZED;
}
