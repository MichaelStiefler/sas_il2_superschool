package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class MIG_9_I_300 extends MIG_9
{

    public MIG_9_I_300()
    {
        nCN37 = -1;
    }

    public void update(float f)
    {
        super.update(f);
        if(FM.AS.isMaster() && FM.CT.Weapons[1] != null && FM.CT.Weapons[1][0] != null)
        {
            if(FM.CT.Weapons[1][0].countBullets() < nCN37)
            {
                if(World.Rnd().nextFloat() < cvt(FM.getAltitude(), 3000F, 7000F, 0.0F, 0.1F))
                    FM.EI.engines[0].setEngineStops(this);
                if(World.Rnd().nextFloat() < cvt(FM.getAltitude(), 3000F, 7000F, 0.0F, 0.1F))
                    FM.EI.engines[1].setEngineStops(this);
            }
            nCN37 = FM.CT.Weapons[1][0].countBullets();
        }
    }

    private int nCN37;

    static 
    {
        Class class1 = MIG_9_I_300.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-9");
        Property.set(class1, "meshName", "3DO/Plane/MiG-9(F-2)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_ru", "3DO/Plane/MiG-9(F-2)(ru)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-9.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_9.class });
        Property.set(class1, "LOSElevation", 0.75635F);
        weaponTriggersRegister(class1, new int[] {
            0, 0, 1
        });
        weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03"
        });
    }
}
