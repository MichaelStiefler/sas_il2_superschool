// "Turret" version of MGun30mmAGS17
// By western0221 on 23rd/Oct./2019

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunAircraftGeneric

public class MGun30mmAGS17T extends MGun30mmAGS17
{

    public MGun30mmAGS17T()
    {
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = false;
        gunproperties.maxDeltaAngle = 0.30F;
        return gunproperties;
    }
}
