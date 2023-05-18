// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 15/07/2011 10:38:31 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombMk82.java

package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombMk82SnakeEye extends Bomb
{

    public BombMk82SnakeEye()
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
        ttcurTM = World.Rnd().nextFloat(0.5F, 1.00F);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if (!bFinsDeployed && (curTm > ttcurTM))
        {
            bFinsDeployed = true;
            this.S *= 200F; // Here the front square is changed. Apply some offset factor of your choice, the higher the factor, the more the bomb will decelerate.
            setMesh("3DO/Arms/Mk82SnakeEye/mono_open.sim");
        }
    }

    private boolean bFinsDeployed;
    private float ttcurTM;
    
    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk82SnakeEye.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk82SnakeEye/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 226F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
                com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
            })));
    }
}