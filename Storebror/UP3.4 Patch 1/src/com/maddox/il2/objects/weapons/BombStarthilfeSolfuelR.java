package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.*;

public class BombStarthilfeSolfuelR extends BombStarthilfeSolfuel
{

    public void interpolateTick()
    {
        super.interpolateTick();
        if(!bOnChute && this.curTm > ttcurTM)
        {
            setMesh("3DO/Arms/StarthilfeSolfuelR/mono.sim");
        }
    }

    static 
    {
        Class var_class = BombStarthilfeSolfuelR.class;
        Property.set(var_class, "mesh", "3DO/Arms/StarthilfeSolfuelR/mono.sim");
        Property.set(var_class, "radius", 0.1F);
        Property.set(var_class, "power", 0.0F);
        Property.set(var_class, "powerType", 0);
        Property.set(var_class, "kalibr", 0.7F);
        Property.set(var_class, "massa", 0.9F);
        Property.set(var_class, "sound", "weapon.bomb_phball");
    }
}
