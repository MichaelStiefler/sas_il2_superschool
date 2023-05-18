package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class KI_46_RECCE extends KI_46
    implements TypeFighter
{

    public KI_46_RECCE()
    {
        bChangedPit = true;
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public boolean bChangedPit;

    static 
    {
        Class class1 = KI_46_RECCE.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-46");
        Property.set(class1, "meshName", "3DO/Plane/Ki-46(Recce)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-46-IIIRecce.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_46_RECCE.class });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_Clip04"
        });
    }
}
