package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombCargoB extends Bomb
{

    public BombCargoB()
    {
        chute = null;
        bOnChute = false;
        DropName = null;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void start()
    {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
        Actor actor = getOwner();
        if(Actor.isValid(actor))
        {
            DropName = EventLog.name(actor);
            EventLog.DropCargo(actor);
        }
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        getSpeed(v3d);
        or.setAT0(v3d);
        pos.setAbs(or);
        if(bOnChute)
        {
            v3d.scale(0.99D);
            if(v3d.z < -5D)
                v3d.z += 1.1F * Time.tickConstLenFs();
            setSpeed(v3d);
        } else
        if(curTm > ttcurTM)
        {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
            chute.mesh().setScale(1.5F);
            chute.pos.setRel(new Point3d(2D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        }
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(actor instanceof ActorLand)
        {
            if(chute != null)
                chute.landing();
            Loc loc = new Loc();
            pos.getAbs(loc);
            loc.getPoint().z = Engine.land().HQ(loc.getPoint().x, loc.getPoint().y);
            if(!Engine.land().isWater(loc.getPoint().x, loc.getPoint().y))
            {
                loc.getOrient().set(loc.getOrient().getAzimut(), 0.0F, 0.0F);
                ActorSimpleMesh actorsimplemesh = new ActorSimpleMesh("3DO/Arms/Cargo-TypeB/mono.sim", loc);
                actorsimplemesh.collide(false);
                actorsimplemesh.postDestroy(0x249f0L);
                EventLog.LandCargo(DropName, (float)loc.getPoint().x, (float)loc.getPoint().y, (float)loc.getPoint().z);
            } else
            {
                EventLog.WaterCargo(DropName, (float)loc.getPoint().x, (float)loc.getPoint().y, (float)loc.getPoint().z);
            }
        } else
        if(chute != null)
            chute.destroy();
        DropName = null;
        destroy();
    }

    private Chute chute;
    private boolean bOnChute;
    private static Orient or = new Orient();
    private static Vector3d v3d = new Vector3d();
    private float ttcurTM;
    String DropName;

    static 
    {
        Class class1 = BombCargoB.class;
        Property.set(class1, "mesh", "3DO/Arms/Cargo-TypeB/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", (String)null);
    }
}
