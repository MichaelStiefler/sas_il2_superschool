// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 30.03.2018 23:16:44
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombTorpAT2M.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            TorpedoAT2, Bomb

public class BombTorpAT2M extends TorpedoAT2
{

    public BombTorpAT2M()
    {
        chute = null;
        bOnChute = false;
    }

    public void start()
    {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
        ttexpTM = World.Rnd().nextFloat(11.2F, 17.75F);
        openHeight = 10000F;
    }

    public void destroy()
    {
        if(chute != null)
            chute.destroy();
        super.destroy();
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if((actor instanceof ActorLand) && chute != null)
            bOnChute = false;
        ttcurTM = 100000F;
        if(chute != null)
            chute.landing();
        super.msgCollision(actor, s, s1);
    }

    public void interpolateTick()
    {
        super.curTm += Time.tickLenFs();
        super.interpolateTick();
        if(bOnChute)
        {
            getSpeed(v3d);
            v3d.scale(0.98999999999999999D);
            if(((Tuple3d) (v3d)).z < -10D)
                v3d.z += 1.1F * Time.tickConstLenFs();
            setSpeed(v3d);
            super.pos.getAbs(TorpedoAT2.P, TorpedoAT2.Or);
        } else
        if(super.curTm > ttcurTM && ((Tuple3d) (TorpedoAT2.P)).z < (double)openHeight)
        {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
            chute.mesh().setScale(2.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(1.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        }
    }

    protected void mydebug(String s1)
    {
    }

    private Chute chute;
    private boolean bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float ttcurTM;
    private float ttexpTM;
    private float openHeight;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombTorpAT2M.class;
        Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");
        Property.set(class1, "radius", 90.8F);
        Property.set(class1, "power", 1025F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.533F);
        Property.set(class1, "massa", 1000F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 74F);
        Property.set(class1, "traveltime", 5000F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 0.0F);
        Property.set(class1, "impactAngleMax", 90.5F);
        Property.set(class1, "impactSpeed", 200.0F);
        Property.set(class1, "armingTime", 3.5F);
    }
}