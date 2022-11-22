package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class DH116 extends DH116xyz
{
    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        hierMesh.chunkVisible("HydraMagazine", thisWeaponsName.endsWith("10xHydra"));
        hierMesh.chunkVisible("Hydraports", thisWeaponsName.endsWith("10xHydra"));
        hierMesh.chunkVisible("Mk82Clamps", thisWeaponsName.indexOf("2xOC") == -1 && (thisWeaponsName.indexOf("6xMk") != -1 || thisWeaponsName.indexOf("12xMk") != -1 || thisWeaponsName.indexOf("6xCB") != -1 || thisWeaponsName.indexOf("12xCB") != -1));
        hierMesh.chunkVisible("Mk83Clamps", thisWeaponsName.indexOf("2xOC") != -1);
        for (int i=1; i<7; i++)
            hierMesh.chunkVisible("WingPylon" + i, (thisWeaponsName.indexOf("AIM") == -1) && (thisWeaponsName.indexOf("12x") != -1 || thisWeaponsName.indexOf("+6xCB") != -1 || thisWeaponsName.indexOf("+6xMk") != -1 || (thisWeaponsName.indexOf("2xOC") != -1 && (thisWeaponsName.indexOf("6xCB") != -1 || thisWeaponsName.indexOf("6xMk") != -1))));
        
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        DH116.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }
    
    static 
    {
        Class class1 = DH116.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "DH-116");
        Property.set(class1, "meshName", "3DO/Plane/DeHavilland116/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/DH116.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitDH116.class
        });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            9, 9, 9, 9, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04",
            "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock13", "_ExternalRock14", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", 
            "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06"
        });
    }
}
