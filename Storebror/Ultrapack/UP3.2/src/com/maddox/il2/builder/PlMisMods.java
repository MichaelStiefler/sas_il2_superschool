/**
 * Lutz class for his mods. Removed MODS lines handling and weather lines handling.
 */
package com.maddox.il2.builder;

public class PlMisMods extends com.maddox.il2.builder.Plugin {

    public PlMisMods() {
        iAOCModsLines = 0;
        iAOCGunner = 0;
        iAOCTrigger = 0;
    }

    public boolean save(com.maddox.rts.SectFile sectfile) {
        int i = sectfile.sectionAdd("AOC_Gunner");
        for (int l = 0; l < iAOCGunner; l++)
            sectfile.lineAdd(i, sAOCGunner[l]);

        i = sectfile.sectionAdd("AOC_Trigger");
        for (int i1 = 0; i1 < iAOCTrigger; i1++)
            sectfile.lineAdd(i, sAOCTrigger[i1]);

        return true;
    }

    public void load(com.maddox.rts.SectFile sectfile) {
        int i = sectfile.sectionIndex("AOC_Gunner");
        if (i >= 0) {
            int l = sectfile.vars(i);
            sAOCGunner = new java.lang.String[l];
            iAOCGunner = l;
            for (int l1 = 0; l1 < l; l1++)
                sAOCGunner[l1] = sectfile.line(i, l1);

        }
        i = sectfile.sectionIndex("AOC_Trigger");
        if (i >= 0) {
            int i1 = sectfile.vars(i);
            sAOCTrigger = new java.lang.String[i1];
            iAOCTrigger = i1;
            for (int i2 = 0; i2 < i1; i2++)
                sAOCTrigger[i2] = sectfile.line(i, i2);

        }
    }

    public void deleteAll() {
        sAOCGunner = null;
        iAOCGunner = 0;
        sAOCTrigger = null;
        iAOCTrigger = 0;
    }

    java.lang.String sAOCMods[];
    int              iAOCModsLines;
    java.lang.String sAOCGunner[];
    int              iAOCGunner;
    java.lang.String sAOCTrigger[];
    int              iAOCTrigger;

    static {
        com.maddox.rts.Property.set(com.maddox.il2.builder.PlMisMods.class, "name", "MisMods");
    }
}
