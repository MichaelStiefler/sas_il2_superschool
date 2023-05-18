
package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.rts.Property;


public class BombMk81SnakeEye_gn16 extends Bomb
{

    public BombMk81SnakeEye_gn16()
    {
        bFinsDeployed = false;
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
        if(!bFinsDeployed && super.curTm > ttcurTM)
        {
            bFinsDeployed = true;
            super.S *= 200F;
            setMesh("3DO/Arms/Mk81SnakeEye_gn16/mono_open.sim");
        }
    }

    private boolean bFinsDeployed;
    private float ttcurTM;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk81SnakeEye_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk81SnakeEye_gn16/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 64F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.23F);
        Property.set(class1, "massa", 113F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
 		Property.set(class1, "dragCoefficient", 0.28F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}