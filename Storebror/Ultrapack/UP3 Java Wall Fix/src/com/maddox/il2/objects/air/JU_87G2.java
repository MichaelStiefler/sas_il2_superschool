package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class JU_87G2 extends JU_87G1
    implements TypeStormovik
{

    public JU_87G2()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(!(getGunByHookName("_MGUN01") instanceof GunEmpty))
            hierMesh().chunkVisible("20mmL_D0", true);
        if(!(getGunByHookName("_MGUN02") instanceof GunEmpty))
            hierMesh().chunkVisible("20mmR_D0", true);
    }

    static 
    {
        Class class1 = JU_87G2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87G-2.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-87D-5/hier_G2.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitJU_87D3.class, CockpitJU_87G1_Gunner.class
        });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 10, 10, 0, 0, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_MGUN03", "_MGUN04", "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02"
        });
    }
}
