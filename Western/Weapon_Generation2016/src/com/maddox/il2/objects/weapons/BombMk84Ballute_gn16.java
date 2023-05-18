
package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.rts.Property;


public class BombMk84Ballute_gn16 extends Bomb
{

    public BombMk84Ballute_gn16()
    {
        bBalDeployed = false;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void start()
    {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.5F, 1.0F);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(!bBalDeployed && super.curTm > ttcurTM)
        {
            bBalDeployed = true;
            super.S *= 120F;
            setMesh("3DO/Arms/Mk84_gn16/monobal_dep.sim");
        }
    }

    private boolean bBalDeployed;
    private float ttcurTM;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk84Ballute_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk84_gn16/monobal.sim");
        Property.set(class1, "radius", 400.65F);
        Property.set(class1, "power", 428.644F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.457F);
        Property.set(class1, "massa", 893.57F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
 		Property.set(class1, "dragCoefficient", 0.28F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}