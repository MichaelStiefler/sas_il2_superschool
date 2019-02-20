package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SEAFIRE15 extends SeafireLate
{

    public SEAFIRE15()
    {
    }

    public void moveArrestorHook(float paramFloat)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -57F * paramFloat, 0.0F);
        hierMesh().chunkSetAngles("Hook2_D0", 0.0F, -12F * paramFloat, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * paramFloat;
        hierMesh().chunkSetLocate("Hook3_D0", Aircraft.xyz, Aircraft.ypr);
        this.arrestor = paramFloat;
    }

    static 
    {
        Class class1 = SEAFIRE15.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SeafireMkXV_SAS(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1942.11F);
        Property.set(class1, "yearExpired", 1944.12F);
        Property.set(class1, "FlightModel", "FlightModels/SeafireXV.fmd:SeafireLate_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSeafireLate.class
        });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 1, 1, 9, 3, 9, 9, 
            3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 
            9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", 
            "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", 
            "_ExternalDev08", "_ExternalDev09"
        });
    }
}
