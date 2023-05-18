package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class BombMk81SnakeEye extends Bomb
{

    public BombMk81SnakeEye()
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
        if(!bFinsDeployed && this.curTm > ttcurTM)
        {
            bFinsDeployed = true;
            this.S *= 200F;
            setMesh("3DO/Arms/Mk81SnakeEye/mono_open.sim");
        }
    }

    private boolean bFinsDeployed;
    private float ttcurTM;

    static 
    {
        Class class1 = BombMk81SnakeEye.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk81SnakeEye/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 64F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 113F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
