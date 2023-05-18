// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/10/2015 10:06:29 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombJATO.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombJATO extends Bomb
{

    public BombJATO()
    {
        chute = null;
        bOnChute = false;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void start()
    {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.5F, 3.5F);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(bOnChute)
        {
            pos.getAbs(or);
            or.interpolate(or_, 0.4F);
            pos.setAbs(or);
            getSpeed(v3d);
            v3d.scale(0.997D);
            if(v3d.z < -5D)
                v3d.z += 1.1F * Time.tickConstLenFs();
            setSpeed(v3d);
        } else
        if(curTm > ttcurTM)
        {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
            setMesh("3DO/Arms/JatoF84Chuted/mono.sim");
        }
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(actor instanceof ActorLand)
        {
            if(chute != null)
                chute.landing();
            postDestroy();
        } else
        {
            super.msgCollision(actor, s, s1);
        }
    }

    private Chute chute;
    private boolean bOnChute;
    private static Orient or = new Orient();
    private static Orient or_ = new Orient(0.0F, 0.0F, 0.0F);
    private static Vector3d v3d = new Vector3d();
    private float ttcurTM;

    static 
    {
        Class class1 = CLASS.THIS();
        Property.set(class1, "mesh", "3DO/Arms/JatoF84/mono.sim");
        Property.set(class1, "radius", 0.1F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.7F);
        Property.set(class1, "massa", 0.9F);
        Property.set(class1, "sound", "weapon.bomb_phball");
    }
}
