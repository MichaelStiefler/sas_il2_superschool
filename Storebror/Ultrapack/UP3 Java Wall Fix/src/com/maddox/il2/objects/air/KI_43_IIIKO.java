package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_43_IIIKO extends KI_43
{

    public KI_43_IIIKO()
    {
        flapps = 0.0F;
    }

    public void update(float f)
    {
        super.update(f);
        float f1 = FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f1) > 0.01F)
        {
            flapps = f1;
            for(int i = 1; i < 15; i++)
                hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 26F * f1, 0.0F, 0.0F);

        }
    }

    private float flapps;

    static 
    {
        Class class1 = KI_43_IIIKO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-43");
        Property.set(class1, "meshName", "3DO/Plane/Ki-43-III(Ko)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-43-III(Ko)(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-43-III.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitKI_43_IIKAI.class
        });
        Property.set(class1, "LOSElevation", 0.4252F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 3, 3, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}
