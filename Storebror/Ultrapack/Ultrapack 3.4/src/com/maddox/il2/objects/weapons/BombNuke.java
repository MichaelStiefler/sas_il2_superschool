package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorCrater;
import com.maddox.il2.objects.ActorSnapToLand;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombNuke extends Bomb
{

    public BombNuke()
    {
        fuzeAlt = -1F;
        fuzeTime = -1F;
    }

    public int getFuzeType()
    {
        return Property.intValue(getClass(), "type", -1);
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void start()
    {
        super.start();
        setStartDelayedExplosion(true);
        ttcurTM = World.Rnd().nextFloat(2F, 5F);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(this.fuze != null && this.fuze.getFuzeType() == 8)
        {
            fuzeAlt = this.fuze.getDetonationDelay();
            this.pos.getTime(Time.current(), Bomb.P);
            if(this.pos.getRelPoint().z < fuzeAlt)
            {
                doFireContaineds();
                doExplosion(this, "NukeChute");
            }
        } else
        if(this.fuze != null && this.fuze.getFuzeType() == 10)
        {
            fuzeAlt = this.fuze.getDetonationDelay();
            this.pos.getTime(Time.current(), Bomb.P);
            double d7 = this.pos.getRelPoint().x;
            double d9 = this.pos.getRelPoint().y;
            double d11 = World.land().HQ(d7, d9);
            if(this.pos.getRelPoint().z < fuzeAlt + d11)
            {
                doFireContaineds();
                doExplosion(this, "NukeChute");
            }
        } else
        if(this.fuze != null && this.fuze.getFuzeType() == 7)
        {
            this.pos.getTime(Time.current(), Bomb.P);
            fuzeTime = this.fuze.getDetonationDelay();
            double d2 = fuzeTime;
            double d5 = d2 + 15D;
            if(this.curTm > ttcurTM + d5)
            {
                doFireContaineds();
                doExplosion(this, "NukeChute");
            }
        }
    }

    private void doFireContaineds()
    {
        if(Engine.land().isWater(Bomb.P.x, Bomb.P.y))
            bombFatMan_water(Bomb.P, -1F, 1.0F);
        else
            bombFatMan_land(Bomb.P, -1F, 1.0F);
    }

    public static void bombFatMan_land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            SurfaceLight(0, 910000F, 0.5F);
            SurfaceCrater(0, 312.1F, 900F);
            ExplodeSurfaceWave(0, 4000F, 4.6F);
            point3d.z += 0.1D;
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(shock).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(buff).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(circleL).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(column).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(flare).eff", 0.1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(ring).eff", -1F);
            return;
        }
    }

    public static void bombFatMan_water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            SurfaceLight(0, 910000F, 0.5F);
            ExplodeSurfaceWave(1, 7000F, 6.6F);
            point3d.z += 0.1D;
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(shock).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(buff).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(circle).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(column).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(flare).eff", 0.1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(ring).eff", -1F);
            return;
        }
    }

    private static void ExplodeSurfaceWave(int i, float f, float f1)
    {
        if(i == 0)
            new ActorSnapToLand("3do/Effects/Explosion/DustRing.sim", true, l, 1.0F, f, 1.0F, 0.0F, f1);
        else
        if(i == 1)
            new ActorSnapToLand("3do/Effects/Explosion/WaterRing.sim", true, l, 2.0F, f, 1.0F, 0.0F, f1);
    }

    private static void SurfaceLight(int i, float f, float f1)
    {
        new ActorSnapToLand("3do/Effects/Explosion/LandLight.sim", true, l, 1.0F, f, i != 0 ? 0.5F : 1.0F, 0.0F, f1);
    }

    private static void SurfaceCrater(int i, float f, float f1)
    {
        if(i == 0)
        {
            new ActorSnapToLand("3do/Effects/Explosion/Crater.sim", true, l, 0.2F, f, f + 2.0F, 1.0F, 0.0F, f1);
            if(bEnableActorCrater)
            {
                int j;
                for(j = 64; j >= 2 && f < (float)j; j /= 2);
                if(j >= 2)
                    new ActorCrater("3do/Effects/Explosion/Crater" + j + "/Live.sim", l, f1);
            }
        }
    }

    float ttcurTM;
    private static boolean bEnableActorCrater = true;
    private static Orient o = new Orient();
    private static Loc l = new Loc();
    protected float fuzeAlt;
    protected float fuzeTime;
}
