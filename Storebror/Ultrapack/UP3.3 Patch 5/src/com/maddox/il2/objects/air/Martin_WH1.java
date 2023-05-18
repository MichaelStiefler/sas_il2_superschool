package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Martin_WH1 extends Martin_B10_xyz
    implements TypeBomber
{

    static 
    {
        Class class1 = Martin_WH1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Martin-Bomber");
        Property.set(class1, "meshName", "3DO/Plane/Martin-WH1(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/B-10B.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitB10B.class, CockpitB10B_Bombardier.class, CockpitB10B_FGunner.class, CockpitB10B_Topgun.class
        });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 
            9, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", 
            "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", 
            "_ExternalDev04", "_ExternalDev05", "_ExternalDev06"
        });
    }
}
