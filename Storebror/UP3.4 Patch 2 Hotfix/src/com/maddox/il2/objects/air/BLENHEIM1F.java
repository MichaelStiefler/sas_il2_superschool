package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BLENHEIM1F extends BLENHEIM
    implements TypeStormovik, TypeFighter
{
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasBayDoors = false;
        this.FM.CT.bHasBayDoorControl = false;
    }
    
    static 
    {
        Class class1 = BLENHEIM1F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BlenheimIF");
        Property.set(class1, "meshName", "3DO/Plane/BlenheimMkIF/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Blenheim_MkI.fmd");
        Property.set(class1, "LOSElevation", 0.73425F);
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitBLENHEIM1F.class, CockpitBLENHEIM1_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 0, 0, 0, 0
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06"
        });
    }
}
