package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.rts.Property;

public class AR_196A3 extends AR_196
{

    public AR_196A3()
    {
    }

    public void update(float f)
    {
        super.update(f);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 2; j++)
                if(this.FM.Gears.clpGearEff[i][j] != null)
                {
                    tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }

        }

    }

    private static Point3d tmpp = new Point3d();

    static 
    {
        Class class1 = AR_196A3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ar-196A-3.fmd");
        Property.set(class1, "meshName", "3DO/Plane/Ar-196A-3/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Ar-196");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1938.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitAR_196A3.class, CockpitAR_196_Gunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 1, 1, 10, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_CANNON01", "_CANNON02", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}
