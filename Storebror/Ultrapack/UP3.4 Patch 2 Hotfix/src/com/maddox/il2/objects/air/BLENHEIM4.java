package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BLENHEIM4 extends BLENHEIM4base
{

    static 
    {
        Class class1 = BLENHEIM4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Blenheim");
        Property.set(class1, "meshName", "3DO/Plane/BlenheimMkIV(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Blenheim_MkIV.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitBLENHEIM4.class, CockpitBLENHEIM4_Bombardier.class, CockpitBLENHEIM_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_BombSpawn01", "_BombSpawn02"
        });
    }
}
