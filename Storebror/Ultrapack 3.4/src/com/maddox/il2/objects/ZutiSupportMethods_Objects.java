package com.maddox.il2.objects;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import com.maddox.rts.SectFile;

public class ZutiSupportMethods_Objects {
    static class FilesComparator implements Comparator {
        public int compare(Object arg0, Object arg1) {
            if (arg0 instanceof File && arg1 instanceof File) // TODO: Fixed by SAS~Storebror: Don't ignore return type!
//                File file1 = (File) arg0;
//                File file2 = (File) arg1;
//                file1.getName().toUpperCase().compareTo(file2.getName().toUpperCase());
                return ((File) arg0).getName().toUpperCase().compareTo(((File) arg1).getName().toUpperCase());
            return 0;
        }
    }

    private static SectFile chiefs1     = null;
    private static SectFile air1        = null;
    private static SectFile stationary1 = null;
    private static SectFile rockets1    = null;
    private static SectFile vehicle1    = null;
    private static SectFile ships1      = null;
    private static SectFile technics1   = null;
    private static SectFile buildings1  = null;
    private static SectFile ships2      = null;
    private static SectFile technics2   = null;
    private static SectFile buildings2  = null;
    private static SectFile chiefs      = null;
    private static SectFile air         = null;
    private static SectFile stationary  = null;
    private static SectFile rockets     = null;
    private static SectFile vehicle     = null;

    /**
     * Method calculates number for specified location using mix of sizes, hashes and positions.
     *
     * @param dir
     * @param files
     * @param dirs
     * @return
     */
    public static long getLocationGeneratedNumber(File dir, long files, long dirs) {
        long size = 0;
        if (dir.isFile()) {
            size = dir.length();
            files++;
        } else {
            File[] subFiles = dir.listFiles();
            // TODO: Added by SAS~Storebror: Fix possible null pointer dereference
            if (subFiles != null) {

                Arrays.sort(subFiles, new FilesComparator());

                for (int i = 0; i < subFiles.length; i++) {
                    File file = subFiles[i];

                    if (file.getName().equals(".rcs") || file.getName().equals(".rc")) continue;

                    if (file.isFile()) {
                        size += file.length();
                        size += file.getName().hashCode();
                        files++;
                    } else if (!file.getName().equalsIgnoreCase("i18n")) // System.out.println(" Folder - " + file.getName());
                        size += getLocationGeneratedNumber(file, files, dirs);
                }
                // TODO: Added by SAS~Storebror: Fix possible null pointer dereference
            }

            dirs++;
        }
        return size + files + dirs;
    }

    public synchronized static SectFile getShips1File() {
        if (!new File("mods/std/com/maddox/il2/objects/ships.ini").exists()) return null;

        try {
            if (ships1 == null) {
                ships1 = new SectFile("mods/std/com/maddox/il2/objects/ships.ini");
                ships1.createIndexes();
            }
            return ships1;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getTechnics1File() {
        if (!new File("mods/std/com/maddox/il2/objects/technics.ini").exists()) return null;

        try {
            if (technics1 == null) {
                technics1 = new SectFile("mods/std/com/maddox/il2/objects/technics.ini");
                technics1.createIndexes();
            }
            return technics1;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getBuildings1File() {
        if (!new File("mods/std/com/maddox/il2/objects/static.ini").exists()) return null;

        try {
            if (buildings1 == null) {
                buildings1 = new SectFile("mods/std/com/maddox/il2/objects/static.ini");
                buildings1.createIndexes();
            }
            return buildings1;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getChiefs1File() {
        if (!new File("mods/std/com/maddox/il2/objects/chief.ini").exists()) return null;

        try {
            if (chiefs1 == null) {
                chiefs1 = new SectFile("mods/std/com/maddox/il2/objects/chief.ini");
                chiefs1.createIndexes();
            }
            return chiefs1;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getAir1File() {
        if (!new File("mods/std/com/maddox/il2/objects/air.ini").exists()) return null;

        try {
            if (air1 == null) {
                air1 = new SectFile("mods/std/com/maddox/il2/objects/air.ini");
                air1.createIndexes();
            }
            return air1;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getStationary1File() {
        if (!new File("mods/std/com/maddox/il2/objects/stationary.ini").exists()) return null;

        try {
            if (stationary1 == null) {
                stationary1 = new SectFile("mods/std/com/maddox/il2/objects/stationary.ini");
                stationary1.createIndexes();
            }
            return stationary1;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getRockets1File() {
        if (!new File("mods/std/com/maddox/il2/objects/rockets.ini").exists()) return null;

        try {
            if (rockets1 == null) {
                rockets1 = new SectFile("mods/std/com/maddox/il2/objects/rockets.ini");
                rockets1.createIndexes();
            }
            return rockets1;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getVehicles1File() {
        if (!new File("mods/std/com/maddox/il2/objects/vehicle.ini").exists()) return null;

        try {
            if (vehicle1 == null) {
                vehicle1 = new SectFile("mods/std/com/maddox/il2/objects/vehicle.ini");
                vehicle1.createIndexes();
            }
            return vehicle1;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getShips2File() {
        if (!new File("files/com/maddox/il2/objects/ships.ini").exists()) return null;

        try {
            if (ships2 == null) {
                ships2 = new SectFile("files/com/maddox/il2/objects/ships.ini");
                ships2.createIndexes();
            }
            return ships2;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getTechnics2File() {
        if (!new File("files/com/maddox/il2/objects/technics.ini").exists()) return null;

        try {
            if (technics2 == null) {
                technics2 = new SectFile("files/com/maddox/il2/objects/technics.ini");
                technics2.createIndexes();
            }
            return technics2;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getBuildings2File() {
        if (!new File("files/com/maddox/il2/objects/static.ini").exists()) return null;

        try {
            if (buildings2 == null) {
                buildings2 = new SectFile("files/com/maddox/il2/objects/static.ini");
                buildings2.createIndexes();
            }
            return buildings2;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getChiefsFile() {
        if (!new File("files/com/maddox/il2/objects/chief.ini").exists()) return null;

        try {
            if (chiefs == null) {
                chiefs = new SectFile("files/com/maddox/il2/objects/chief.ini");
                chiefs.createIndexes();
            }
            return chiefs;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getAirFile() {
        if (!new File("files/com/maddox/il2/objects/air.ini").exists()) return null;

        try {
            if (air == null) {
                air = new SectFile("files/com/maddox/il2/objects/air.ini");
                air.createIndexes();
            }
            return air;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getStationaryFile() {
        if (!new File("files/com/maddox/il2/objects/stationary.ini").exists()) return null;

        try {
            if (stationary == null) {
                stationary = new SectFile("files/com/maddox/il2/objects/stationary.ini");
                stationary.createIndexes();
            }
            return stationary;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getRocketsFile() {
        if (!new File("files/com/maddox/il2/objects/rockets.ini").exists()) return null;

        try {
            if (rockets == null) {
                rockets = new SectFile("files/com/maddox/il2/objects/rockets.ini");
                rockets.createIndexes();
            }
            return rockets;
        } catch (Exception ex) {
            return null;
        }
    }

    public synchronized static SectFile getVehiclesFile() {
        if (!new File("files/com/maddox/il2/objects/vehicle.ini").exists()) return null;

        try {
            if (vehicle == null) {
                vehicle = new SectFile("files/com/maddox/il2/objects/vehicle.ini");
                vehicle.createIndexes();
            }
            return vehicle;
        } catch (Exception ex) {
            return null;
        }
    }
}
