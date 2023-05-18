// US BLU-97 Bomblet class for CBU-87 Cluster bomb

// TODO: Edited by SAS~Storebror 0n 2019-12-26: Increase explosive power to balance reduction of spawned bomblets to reasonable figures, code cleanup.

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.TrueRandom;


public class BombletBLU97_4x extends Bomb
{

    public void start()
    {
        super.start();
        ttcurTM = TrueRandom.nextFloat(2.5F, 6.0F);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(!bChuteDeployed && super.curTm > ttcurTM)
        {
            bChuteDeployed = true;
            super.S *= 24F;
            setMesh("3do/arms/BLU97Bomlet/monoC_4x.sim");
        }
    }

    protected boolean haveSound()
    {
        return index % 32 == 0;
    }

    private boolean bChuteDeployed = false;
    private float ttcurTM;

    static 
    {
        Class class1 = BombletBLU97_4x.class;
        Property.set(class1, "mesh", "3do/arms/BLU97Bomlet/mono_4x.sim");
        Property.set(class1, "radius", 2.6F);
        Property.set(class1, "power", 9.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.064F);
        Property.set(class1, "massa", 1.54F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_IT_Standard3.class
        })));
    }
}
