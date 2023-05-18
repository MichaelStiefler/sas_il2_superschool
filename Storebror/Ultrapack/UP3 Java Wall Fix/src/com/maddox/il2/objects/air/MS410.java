package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MS410 extends MS400X
{

    public MS410()
    {
    }

    public void update(float f)
    {
        hierMesh().chunkSetAngles("OilRad_D0", 0.0F, -20F * FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    static 
    {
        Class class1 = MS410.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Morane");
        Property.set(class1, "meshNameDemo", "3DO/Plane/MS410(fi)/hier.him");
        Property.set(class1, "meshName_fi", "3DO/Plane/MS410(fi)/hier.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName", "3DO/Plane/MS410(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1951.8F);
        Property.set(class1, "FlightModel", "FlightModels/MS410.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMorane.class
        });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01"
        });
    }
}
