/*Modified Explosions class for the SAS Engine Mod*/
//TODO: Allows for custom explosions e.g. Nukes

package com.maddox.il2.objects.effects;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.SfxExplosion;
import com.maddox.il2.objects.weapons.BallisticProjectile;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Time;

public class Explosions
{
    static class MydataForSmoke
    {

        ActorHMesh a;
        float tim;

        MydataForSmoke(ActorHMesh actorhmesh, float f)
        {
            a = actorhmesh;
            tim = f;
        }
    }


    public Explosions()
    {
    }

    public static void HydrogenBalloonExplosion(Loc loc, Actor actor)
    {
        if(!Config.isUSE_RENDER())
            return;
        Loc loc1 = new Loc();
        Vector3d vector3d = new Vector3d();
        for(int i = 0; i < 2; i++)
        {
            loc1.set(loc);
            loc1.getPoint().x += World.rnd().nextDouble(-12D, 12D);
            loc1.getPoint().y += World.rnd().nextDouble(-12D, 12D);
            loc1.getPoint().z += World.rnd().nextDouble(-3D, 3D);
            Eff3DActor.New(loc1, 1.0F, "3DO/Effects/Fireworks/Tank_Burn.eff", -1F);
        }

        int k = World.rnd().nextInt(2, 6);
        for(int j = 0; j < k; j++)
        {
            vector3d.set(World.rnd().nextFloat(-1F, 1.0F), World.rnd().nextFloat(-1F, 1.0F), World.rnd().nextFloat(-0.5F, 1.5F));
            vector3d.normalize();
            vector3d.scale(World.rnd().nextFloat(4F, 15F));
            float f = World.rnd().nextFloat(4F, 7F);
            BallisticProjectile ballisticprojectile = new BallisticProjectile(loc.getPoint(), vector3d, f);
            Eff3DActor.New(ballisticprojectile, null, null, 1.0F, "3DO/Effects/Aircraft/BlackHeavySPD.eff", f);
            Eff3DActor.New(ballisticprojectile, null, null, 1.0F, "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", f);
            Eff3DActor.New(ballisticprojectile, null, null, 1.0F, "3DO/Effects/Aircraft/FireSPD.eff", f);
        }

        SfxExplosion.crashAir(loc.getPoint(), 1);
    }

    public static void runByName(String s, ActorHMesh actorhmesh, String s1, String s2, float f)
    {
        runByName(s, actorhmesh, s1, s2, f, null);
    }

    private static HookNamed getRandomHook(ActorHMesh actorhmesh)
    {
        RangeRandom rangerandom = World.rndPos(actorhmesh.pos.getAbs().getX(), actorhmesh.pos.getAbs().getY());
        int i = 0;
        String s = "";
        do
        {
            int j = rangerandom.nextInt(0, 7);
            if(j == 0)
                s = "_Engine1Smoke";
            else
            if(j == 1)
                s = "_Engine2Smoke";
            else
            if(j == 2)
                s = "_Engine3Smoke";
            else
            if(j == 3)
                s = "_Engine4Smoke";
            else
            if(j == 4)
                s = "_Tank1Burn";
            else
            if(j == 5)
                s = "_Tank2Burn";
            else
            if(j == 6)
                s = "_Tank3Burn";
            else
            if(j == 7)
                s = "_Tank4Burn";
            int k = actorhmesh.mesh().hookFind(s);
            if(k >= 0)
                return new HookNamed(actorhmesh, s);
        } while(++i < 6);
        return null;
    }

    public static void runByName(String s, ActorHMesh actorhmesh, String s1, String s2, float f, Actor actor)
    {
        HookNamed hooknamed = null;
        if(f > 0.0F)
            f *= 4F;
        if(s1 != null && !s1.equals(""))
        {
            int i = actorhmesh.mesh().hookFind(s1);
            if(i >= 0)
                hooknamed = new HookNamed(actorhmesh, s1);
            else
            if(s2 != null && !s2.equals(""))
            {
                int j = actorhmesh.mesh().hookFind(s2);
                if(j >= 0)
                    hooknamed = new HookNamed(actorhmesh, s2);
            }
        }
        if(s.equalsIgnoreCase("Tank"))
            Tank_Explode(actorhmesh.pos.getAbsPoint());
        else
        if(s.equalsIgnoreCase("_TankSmoke_"))
        {
            if(f > 0.0F)
            {
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingSmoke.eff", f);
            }
        } else
        if(s.equalsIgnoreCase("Car"))
        {
            Car_Explode(actorhmesh.pos.getAbsPoint());
            if(f > 0.0F)
            {
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/CarDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/CarDyingSmoke.eff", f);
            }
        } else
        if(s.equalsIgnoreCase("CarFuel"))
        {
            new MsgAction(0.0D, actorhmesh) {

                public void doAction(Object obj)
                {
                    Point3d point3d = new Point3d();
                    ((Actor)obj).pos.getAbs(point3d);
                    Explosions.ExplodeVagonFuel(point3d, point3d, 1.5F);
                }

            }
;
            if(f > 0.0F)
            {
                new MyMsgAction(0.42999999999999999D, actorhmesh, actor) {

                    public void doAction(Object obj)
                    {
                        Point3d point3d = new Point3d();
                        ((Actor)obj).pos.getAbs(point3d);
                        float f1 = 25F;
                        int k = 0;
                        float f2 = 50F;
                        MsgExplosion.send((Actor)obj, "Body", point3d, (Actor)obj2, 0.0F, f1, k, f2);
                    }

                }
;
                new MsgAction(1.2D, new MydataForSmoke(actorhmesh, f)) {

                    public void doAction(Object obj)
                    {
                        Eff3DActor.New(((MydataForSmoke)obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke)obj).tim);
                    }

                }
;
            }
        } else
        if(s.equalsIgnoreCase("Artillery"))
        {
            Antiaircraft_Explode(actorhmesh.pos.getAbsPoint());
            if(f > 0.0F)
            {
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/CarDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", f);
            }
        } else
        if(s.equalsIgnoreCase("Stationary"))
        {
            Antiaircraft_Explode(actorhmesh.pos.getAbsPoint());
            if(f > 0.0F)
            {
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingSmoke.eff", f);
            }
        } else
        if(s.equalsIgnoreCase("Aircraft"))
        {
            Antiaircraft_Explode(actorhmesh.pos.getAbsPoint());
            if(f > 0.0F)
            {
                HookNamed hooknamed1 = getRandomHook(actorhmesh);
                Eff3DActor.New(actorhmesh, hooknamed1, null, 1.0F, "Effects/Smokes/TankDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed1, null, 1.0F, "Effects/Smokes/TankDyingSmoke.eff", f);
            }
        } else
        if(s.equalsIgnoreCase("Aircraft"))
            System.out.println("*** Unknown named explode: '" + s + "'");
        else
        if(s.equalsIgnoreCase("WagonWoodExplosives"))
        {
            new MsgAction(0.0D, actorhmesh) {

                public void doAction(Object obj)
                {
                    Point3d point3d = new Point3d();
                    ((Actor)obj).pos.getAbs(point3d);
                    Explosions.ExplodeVagonArmor(point3d, point3d, 2.0F);
                }

            }
;
            if(f > 0.0F)
            {
                new MyMsgAction(0.42999999999999999D, actorhmesh, actor) {

                    public void doAction(Object obj)
                    {
                        Point3d point3d = new Point3d();
                        ((Actor)obj).pos.getAbs(point3d);
                        float f1 = 180F;
                        int k = 0;
                        float f2 = 140F;
                        MsgExplosion.send((Actor)obj, "Body", point3d, (Actor)obj2, 0.0F, f1, k, f2);
                    }

                }
;
                new MsgAction(1.2D, new MydataForSmoke(actorhmesh, f)) {

                    public void doAction(Object obj)
                    {
                        Eff3DActor.New(((MydataForSmoke)obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke)obj).tim);
                    }

                }
;
            }
        } else
        if(s.equalsIgnoreCase("WagonWood"))
        {
            new MsgAction(0.0D, actorhmesh) {

                public void doAction(Object obj)
                {
                    Point3d point3d = new Point3d();
                    ((Actor)obj).pos.getAbs(point3d);
                    Explosions.ExplodeVagonArmor(point3d, point3d, 2.0F);
                }

            }
;
            if(f > 0.0F)
                new MsgAction(1.2D, new MydataForSmoke(actorhmesh, f)) {

                    public void doAction(Object obj)
                    {
                        Eff3DActor.New(((MydataForSmoke)obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke)obj).tim);
                    }

                }
;
        } else
        if(s.equalsIgnoreCase("WagonFuel"))
        {
            new MsgAction(0.0D, actorhmesh) {

                public void doAction(Object obj)
                {
                    Point3d point3d = new Point3d();
                    ((Actor)obj).pos.getAbs(point3d);
                    Explosions.ExplodeVagonFuel(point3d, point3d, 2.0F);
                }

            }
;
            if(f > 0.0F)
            {
                new MyMsgAction(0.42999999999999999D, actorhmesh, actor) {

                    public void doAction(Object obj)
                    {
                        Point3d point3d = new Point3d();
                        ((Actor)obj).pos.getAbs(point3d);
                        float f1 = 180F;
                        int k = 0;
                        float f2 = 120F;
                        MsgExplosion.send((Actor)obj, "Body", point3d, (Actor)obj2, 0.0F, f1, k, f2);
                    }

                }
;
                new MsgAction(1.2D, new MydataForSmoke(actorhmesh, f)) {

                    public void doAction(Object obj)
                    {
                        Eff3DActor.New(((MydataForSmoke)obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke)obj).tim);
                    }

                }
;
            }
        } else
        if(s.equalsIgnoreCase("WagonMetal"))
        {
            new MsgAction(0.0D, actorhmesh) {

                public void doAction(Object obj)
                {
                    Point3d point3d = new Point3d();
                    ((Actor)obj).pos.getAbs(point3d);
                    Explosions.ExplodeVagonArmor(point3d, point3d, 2.0F);
                }

            }
;
            if(f > 0.0F)
                new MsgAction(1.2D, new MydataForSmoke(actorhmesh, f)) {

                    public void doAction(Object obj)
                    {
                        Eff3DActor.New(((MydataForSmoke)obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke)obj).tim);
                    }

                }
;
        }
    }

    public static void shot(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            l.set(point3d, o);
            Eff3DActor eff3dactor = Eff3DActor.New(l, 2.0F, "effects/sprites/spritesun.eff", -1F);
            eff3dactor.postDestroy(Time.current() + 500L);
            return;
        }
    }

    public static void HouseExplode(int i, Loc loc, float af[])
    {
        if(!Config.isUSE_RENDER())
            return;
        String s = "";
        byte byte0 = 0;
        float f = 1.0F;
        switch(i)
        {
        case 0: // '\0'
            f = 0.75F;
            // fall through

        case 1: // '\001'
            s = "Wood";
            byte0 = 4;
            break;

        case 2: // '\002'
            f = 0.75F;
            // fall through

        case 3: // '\003'
        case 4: // '\004'
            s = "Rock";
            byte0 = 3;
            break;

        case 5: // '\005'
            f = 0.75F;
            // fall through

        case 6: // '\006'
            s = "Fuel";
            byte0 = 5;
            break;

        default:
            System.out.println("WARNING: HouseExplode(): unknown type");
            return;
        }
        String s1 = "effects/Explodes/Objects/House/" + s + "/Boiling.eff";
        String s2 = "effects/Explodes/Objects/House/" + s + "/Boiling2.eff";
        String s3 = "effects/Explodes/Objects/House/" + s + "/Pieces.eff";
        Eff3D.initSetBoundBox(af[0], af[1], af[2], af[3], af[4], af[5]);
        Eff3DActor.New(loc, 1.0F, s1, 3F);
        Eff3D.initSetBoundBox(af[0] + (af[3] - af[0]) * 0.25F, af[1] + (af[4] - af[1]) * 0.25F, af[2], af[3] - (af[3] - af[0]) * 0.25F, af[4] - (af[4] - af[1]) * 0.25F, af[2] + (af[5] - af[2]) * 0.5F);
        Eff3DActor.New(loc, 1.0F, s3, 3F);
        if(i != 0)
        {
            String s4 = "effects/Explodes/Objects/House/" + s + "/Fire_Smoke.eff";
            RangeRandom rangerandom = World.rndPos(loc.getX(), loc.getY());
            float f2 = rangerandom.nextFloat();
            if((double)f2 > 0.5D)
            {
                float f3 = rangerandom.nextFloat(300F, 600F);
                Eff3DActor.New(loc, f, s4, f3);
                Eff3DActor.New(loc, f, s2, f3);
            } else
            {
                Eff3DActor.New(loc, f, s4, 5F);
                Eff3DActor.New(loc, f, s2, 5F);
            }
        } else
        {
            Eff3DActor.New(loc, f, s2, 3F);
        }
        SfxExplosion.building(loc.getPoint(), byte0, af);
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

    private static void fontain(Point3d point3d, float f, float f1, int i, int j, boolean flag, boolean flag1)
    {
        f = -1F;
        int k = 4 + World.rnd().nextInt(2);
        float f2 = 30F;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            String s;
            float f3;
            float f4;
            float f5;
            float f6;
            if(j == 2)
            {
                s = "Bomb1000";
                f3 = 150F;
                f5 = 190F;
                f4 = 36F;
                k = 3 + World.rnd().nextInt(3);
                f6 = 0.44F;
            } else
            if(j == 0)
            {
                s = "Bomb250";
                f3 = 100F;
                f5 = 165F;
                f4 = 18F;
                f6 = 0.42F;
            } else
            {
                s = "RS82";
                f3 = 75F;
                f5 = 100F;
                f4 = 4.5F;
                k = 2 + World.rnd().nextInt(2);
                f6 = 0.38F;
            }
            String s1 = "Land";
            if(flag1)
                s1 = "Land_dig_in";
            if(World.rnd().nextFloat() < 0.5F)
                s1 = s1 + "_2";
            String s2 = "effects/Explodes/" + s + "/" + s1 + "/Fontain.eff";
            String s3 = "effects/Explodes/" + s + "/" + s1 + "/Peaces.eff";
            String s4 = "effects/Explodes/" + s + "/" + s1 + "/Burn.eff";
            String s5 = "effects/Explodes/" + s + "/" + s1 + "/Burn_Fog.eff";
            String s6 = "effects/Explodes/" + s + "/" + s1 + "/Shock.eff";
            String s7 = "effects/Explodes/" + s + "/" + s1 + "/Base.eff";
            Eff3DActor.New(l, 1.0F, s3, 3.5F);
            for(int i1 = 0; i1 < k; i1++)
            {
                float f8 = (float)(360D * Math.random());
                float f9 = 90F + (2.0F * (float)Math.random() - 1.0F) * f2;
                o.set(f8, f9, 0.0F);
                l.set(point3d, o);
                Eff3DActor.New(l, 1.0F, s2, 10F);
                Eff3DActor.New(l, 1.0F, s5, f);
                Eff3DActor.New(l, 1.0F, s6, f);
                Eff3DActor.New(l, 1.0F, s7, f);
            }

            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            ExplodeSurfaceWave(i, f3, f6);
            SurfaceLight(i, f5, 0.0F);
            float f7 = 80F;
            if(flag && !Mission.isCoop() && !Mission.isDogfight())
                if(j == 0)
                    f7 *= Main.cur().mission.zutiMisc_BombsCat2_CratersVisibilityMultiplier;
                else
                if(j == 2)
                    f7 *= Main.cur().mission.zutiMisc_BombsCat3_CratersVisibilityMultiplier;
                else
                    f7 *= Main.cur().mission.zutiMisc_BombsCat1_CratersVisibilityMultiplier;
            SurfaceCrater(i, f4, f7);
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor eff3dactor = Eff3DActor.New(l, 1.0F, s4, 1.0F);
            eff3dactor.postDestroy(Time.current() + 200L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
            lightpointactor.light.setEmit(1.0F, f5);
            ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
            break;

        case 1: // '\001'
            if(j == 2)
            {
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb1000/Water/Fontain.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb1000/Water/Fontain2.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb1000/Water/Fontain3.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb1000/Water/Fontain4.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb1000/Water/Fontain5.eff", f);
            } else
            if(j == 0)
            {
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain2.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain3.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain4.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain5.eff", f);
            } else
            if(j == 3)
            {
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Water/Fontain.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Water/Fontain2.eff", f);
            } else
            {
                Eff3DActor.New(l, 1.0F, "effects/Explodes/RS82/Water/Fontain.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/RS82/Water/Fontain2.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/RS82/Water/Fontain3.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/RS82/Water/Fontain4.eff", f);
                Eff3DActor.New(l, 1.0F, "effects/Explodes/RS82/Water/Fontain5.eff", f);
            }
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            if(j == 2)
            {
                ExplodeSurfaceWave(i, 175F, 9.5F);
                break;
            }
            if(j == 0)
            {
                ExplodeSurfaceWave(i, 95F, 7.5F);
                break;
            }
            if(j == 3)
                ExplodeSurfaceWave(i, 15F, 2.75F);
            else
                ExplodeSurfaceWave(i, 45F, 5F);
            break;

        case 2: // '\002'
            Tank_Explode(point3d);
            break;
        }
    }

    public static void Tank_Explode(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Tank_ExplodeCollapse(point3d);
            float f = 31.25F;
            float f2 = 150F;
            int i = 0;
            boolean flag = true;
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            ExplodeSurfaceWave(i, f, flag ? 0.6F : 0.8F);
            SurfaceLight(i, f2, 0.3F);
            return;
        }
    }

    public static void Antiaircraft_Explode(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            Tank_Explode(point3d);
            return;
        }
    }

    public static void Car_Explode(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            Tank_Explode(point3d);
            return;
        }
    }

    protected static void Building_Explode(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
            return;
        float f = 20F;
        byte byte0 = 3;
        Point3d point3d1 = new Point3d();
        String s = "effects/Explodes/Objects/Building20m/SmokeBoiling.eff";
        String s1 = "effects/Explodes/Objects/Building20m/SmokeBoiling2.eff";
        for(int i = 0; i < byte0 * byte0; i++)
        {
            double d = (Math.random() - 0.5D) * (double)f;
            double d1 = (Math.random() - 0.5D) * (double)f;
            point3d1.set(point3d);
            point3d1.x += d;
            point3d1.y += d1;
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d1, o);
            Eff3DActor.New(l, 1.0F, Math.random() >= 0.5D ? s1 : s, 3F);
        }

        o.set(0.0F, 0.0F, 0.0F);
        l.set(point3d, o);
        float f3 = 62.5F;
        int j = 0;
        boolean flag = false;
        ExplodeSurfaceWave(j, f3, flag ? 0.6F : 0.8F);
    }

    public static void Tank_ExplodeCollapse(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            SfxExplosion.crashTank(point3d, 0);
            explodeCollapse(point3d);
            return;
        }
    }

    private static void explodeCollapse(Point3d point3d)
    {
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        String s = "Objects/Tank_Collapse";
        float f3 = 150F;
        String s1 = "effects/Explodes/" + s + "/Peaces1.eff";
        String s2 = "effects/Explodes/" + s + "/Peaces2.eff";
        String s3 = "effects/Explodes/" + s + "/Sparks.eff";
        String s4 = "effects/Explodes/" + s + "/Burn.eff";
        String s5 = "effects/Explodes/" + s + "/SmokeBoiling.eff";
        Eff3DActor.New(l, 1.0F, s1, 3.5F);
        Eff3DActor.New(l, 1.0F, s2, 3.5F);
        Eff3DActor.New(l, 1.0F, s3, 0.5F);
        Eff3DActor.New(l, 1.0F, s5, 2.5F);
        Eff3DActor eff3dactor2 = Eff3DActor.New(l, 1.0F, s4, 0.3F);
        eff3dactor2.postDestroy(Time.current() + 1500L);
        LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d(5D, 0.0D, 0.0D));
        lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
        lightpointactor.light.setEmit(1.0F, f3 * 2.0F);
        ((Actor) (eff3dactor2)).draw.lightMap().put("light", lightpointactor);
    }

    public static void Car_ExplodeCollapse(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            explodeCollapse(point3d);
            return;
        }
    }

    public static void AirDrop_Land(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
            return;
        float f = 4F;
        float f1 = 18F;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Aircraft/Land/Base.eff", f);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Aircraft/Land/Burn.eff", f);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Aircraft/Land/Dirt.eff", f);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Aircraft/Land/Smoke.eff", f);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Aircraft/Land/Smoke2.eff", f);
        o.set(0.0F, 0.0F, 0.0F);
        l.set(point3d, o);
        ExplodeSurfaceWave(0, 300F, 0.8F);
        SurfaceLight(0, 150F, 0.5F);
        if(Mission.isDeathmatch())
            bEnableActorCrater = false;
        new ActorSnapToLand("3do/Effects/Explosion/AircraftCrater.sim", true, l, 0.2F, f1, f1 + 2.0F, 1.0F, 0.0F, 1500F);
        if(bEnableActorCrater)
        {
            int i;
            for(i = 64; i >= 2 && f1 < (float)i; i /= 2);
            if(i >= 2)
                new ActorCrater("3do/Effects/Explosion/Crater" + i + "/Live.sim", l, 1500F);
        }
        bEnableActorCrater = true;
        SfxExplosion.crashAir(point3d, 0);
        l = new Loc(point3d);
        World.cur();
		l.getPoint().z = World.land().HQ(point3d.x, point3d.y);
        Eff3DActor.New(l, 1.0F, "EFFECTS/Smokes/SmokeBoiling.eff", 1200F);
        Eff3DActor.New(l, 1.0F, "3DO/Effects/Aircraft/BlackHeavyGND.eff", 1200F);
        Eff3DActor.New(l, 1.75F, "3DO/Effects/Aircraft/FireGND.eff", 1200F);
        int j = World.rnd().nextInt(1, 4);
        for(int k = 0; k < j; k++)
        {
            Point3d point3d1 = new Point3d(point3d);
            double d = World.rnd().nextDouble(0.0D, 10D) - 5D;
            if(d < 0.5D && d > -0.5D)
                d = 0.5D;
            point3d1.x += d;
            d = World.rnd().nextDouble(0.0D, 16D) - 8D;
            if(d < 0.5D && d > -0.5D)
                d = 0.5D;
            point3d1.y += d;
            Loc loc = new Loc(point3d1);
            World.cur();
			loc.getPoint().z = World.land().HQ(point3d1.x, point3d1.y);
            Eff3DActor.New(loc, 1.0F, "3DO/Effects/Aircraft/FireGND.eff", 1200F - World.rnd().nextFloat(10F, 200F));
        }

    }

    public static void AirDrop_Water(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            float f = 4F;
            float f1 = 1.0F;
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, f1, "effects/Explodes/Aircraft/Water/Base.eff", f);
            Eff3DActor.New(l, f1, "effects/Explodes/Aircraft/Water/Fontain.eff", f);
            Eff3DActor.New(l, f1, "effects/Explodes/Aircraft/Water/Spray.eff", f);
            Eff3DActor.New(l, f1, "effects/Explodes/Aircraft/Water/Soot.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain2.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain3.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain4.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain5.eff", f);
            SfxExplosion.crashAir(point3d, 2);
            ExplodeSurfaceWave(1, 110F, 6F);
            return;
        }
    }

    public static void AirDrop_Air(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            explodeCollapse(point3d);
            SfxExplosion.crashAir(point3d, 1);
            return;
        }
    }

    public static void WreckageDrop_Water(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            float f = 3F;
            float f1 = 1.0F;
            fontain(point3d, f, f1, 1, 3, false, false);
            SfxExplosion.crashParts(point3d, 2);
            return;
        }
    }

    public static void SomethingDrop_Water(Point3d point3d, float f)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            new ActorSnapToLand("3do/Effects/Explosion/WaterRing.sim", true, l, f * 0.2F, f, 1.0F, 0.0F, 2.5F);
            SfxExplosion.crashParts(point3d, 2);
            return;
        }
    }

    public static void airbustABcontainer(Point3d point3d, int i)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        if(i == 0)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain.eff", -1F);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain2.eff", -1F);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain3.eff", -1F);
        } else
        if(i == 1)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain.eff", -1F);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain2.eff", -1F);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain3.eff", -1F);
        } else
        if(i == 2)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain.eff", -1F);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain2.eff", -1F);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain3.eff", -1F);
        } else
        if(i == 3)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain.eff", -1F);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain2.eff", -1F);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/AB/Fontain3.eff", -1F);
        }
    }

    public static void AirFlak(Point3d point3d, int i)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        String s = "effects/Explodes/Air/Zenitka/";
        switch(i)
        {
        case 0: // '\0'
            s = s + "USSR_85mm/";
            break;

        case 1: // '\001'
            s = s + "Germ_88mm/";
            break;

        case 2: // '\002'
            s = s + "USSR_25mm/";
            break;

        default:
            s = s + "Germ_20mm/";
            break;
        }
        SfxExplosion.zenitka(point3d, i);
        float f = -1F;
        Eff3DActor.New(l, 1.0F, s + "SmokeBoiling.eff", f);
        Eff3DActor.New(l, 1.0F, s + "Burn.eff", f);
        Eff3DActor.New(l, 1.0F, s + "Sparks.eff", f);
        Eff3DActor.New(l, 1.0F, s + "SparksP.eff", f);
    }

    public static void ExplodeFuel(Point3d point3d)
    {
        if(!Config.isUSE_RENDER())
            return;
        Loc loc = new Loc(point3d);
        Eff3DActor.New(loc, 1.0F, "3DO/Effects/Fireworks/Tank_Burn.eff", -1F);
        Eff3DActor.New(loc, 1.0F, "3DO/Effects/Fireworks/Tank_SmokeBoiling.eff", -1F);
        Eff3DActor.New(loc, 1.0F, "3DO/Effects/Fireworks/Tank_Sparks.eff", -1F);
        Eff3DActor.New(loc, 1.0F, "3DO/Effects/Fireworks/Tank_SparksP.eff", -1F);
        World.cur();
		if(point3d.z - World.land().HQ(point3d.x, point3d.y) > 3D)
        {
            SfxExplosion.crashAir(point3d, 1);
        } else
        {
            World.cur();
            if(World.land().isWater(point3d.x, point3d.y))
                SfxExplosion.crashAir(point3d, 2);
            else
                SfxExplosion.crashAir(point3d, 0);
        }
    }

    private static void LinearExplode(Point3d point3d, Point3d point3d1, float f, float f1, String s, String s1)
    {
        double d = point3d.distance(point3d1);
        int i = (int)(((2D * d) / (double)f) * (double)f1);
        if(i < 2)
            i = 2;
        Point3d point3d2 = new Point3d();
        for(int j = 0; j < i; j++)
        {
            point3d2.interpolate(point3d, point3d1, Math.random());
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d2, o);
            Eff3DActor.New(l, 1.0F, Math.random() >= 0.5D ? s1 : s, 3F);
        }

    }

    public static void ExplodeBridge(Point3d point3d, Point3d point3d1, float f)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            LinearExplode(point3d, point3d1, f, 1.0F, "effects/Explodes/Objects/Bridges/SmokeBoiling.eff", "effects/Explodes/Objects/Bridges/SmokeBoiling2.eff");
            SfxExplosion.bridge(point3d, point3d1, f);
            return;
        }
    }

    public static void ExplodeVagonArmor(Point3d point3d, Point3d point3d1, float f)
    {
        if(!Config.isUSE_RENDER())
            return;
        Point3d point3d2 = new Point3d();
        for(int i = 0; i < 3; i++)
        {
            point3d2.interpolate(point3d, point3d1, Math.random());
            AirFlak(point3d2, 0);
        }

        LinearExplode(point3d, point3d1, f, 0.5F, "effects/Explodes/Objects/VagonArmor/SmokeBoiling.eff", "effects/Explodes/Objects/VagonArmor/SmokeBoiling2.eff");
        SfxExplosion.wagon(point3d, point3d1, f, 6);
    }

    public static void ExplodeVagonFuel(Point3d point3d, Point3d point3d1, float f)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            LinearExplode(point3d, point3d1, f, 0.75F, "effects/Explodes/Objects/VagonFuel/SmokeBoilingFire.eff", "effects/Explodes/Objects/VagonFuel/SmokeBoilingFire2.eff");
            SfxExplosion.wagon(point3d, point3d1, f, 5);
            return;
        }
    }

    public static void bomb50_land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, f1, "3DO/Effects/Fireworks/Tank_Burn.eff", -1F);
            Eff3DActor.New(l, f1, "3DO/Effects/Fireworks/Tank_SmokeBoiling.eff", -1F);
            Eff3DActor.New(l, f1, "3DO/Effects/Fireworks/Tank_Sparks.eff", -1F);
            Eff3DActor.New(l, f1, "3DO/Effects/Fireworks/Tank_SparksP.eff", -1F);
            return;
        }
    }

    public static void BOMB250_Land(Point3d point3d, float f, float f1, boolean flag, boolean flag1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            fontain(point3d, f, f1, 0, 0, flag, flag1);
            return;
        }
    }

    public static void BOMB250_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            fontain(point3d, f, f1, 1, 0, false, false);
            return;
        }
    }

    public static void BOMB250_Object(Point3d point3d, Vector3d vector3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.setAT0(vector3d);
            o.set(o.azimut(), o.tangage() + 180F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Object/Sparks.eff", f);
            return;
        }
    }

    public static void BOMB1000a_Land(Point3d point3d, float f, float f1, boolean flag, boolean flag1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            fontain(point3d, f, f1, 0, 2, flag, flag1);
            return;
        }
    }

    public static void BOMB1000a_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            fontain(point3d, f, f1, 1, 2, false, false);
            return;
        }
    }

    public static void BOMB1000a_Object(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            fontain(point3d, f, f1, 0, 2, false, false);
            return;
        }
    }

    public static void bomb1000_land(Point3d point3d, float f, float f1, boolean flag, boolean flag1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            SurfaceLight(0, 10000F, 1.0F);
            SurfaceCrater(0, 112.1F, 600F);
            ExplodeSurfaceWave(0, 2000F, 4.6F);
            point3d.z += 5D;
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(buff).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(circle).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(column).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(flare).eff", 0.1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(ring).eff", -1F);
            return;
        }
    }

    public static void bomb1000_water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            SurfaceLight(0, 10000F, 1.0F);
            ExplodeSurfaceWave(1, 3000F, 6.6F);
            point3d.z += 5D;
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(buff).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(circle).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(column).eff", -1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(flare).eff", 0.1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(ring).eff", -1F);
            return;
        }
    }

    public static void bomb1000_object(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        else
            return;
    }

    public static void bomb5000_land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        else
            return;
    }

    public static void bomb5000_water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        else
            return;
    }

    public static void bomb5000_object(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        else
            return;
    }

    public static void bomb999999_object(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        else
            return;
    }

    //TODO: Nukes
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
            point3d.z += 5D;
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
            point3d.z += 5D;
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

    public static void bombFatMan_object(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        else
            return;
    }
    
    public static void RS82_Land(Point3d point3d, float f, float f1, boolean flag, boolean flag1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            fontain(point3d, f, f1, 0, 1, flag, flag1);
            return;
        }
    }

    public static void RS82_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            fontain(point3d, f, f1, 1, 1, false, false);
            return;
        }
    }

    public static void RS82_Object(Point3d point3d, Vector3d vector3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.setAT0(vector3d);
            o.set(o.azimut(), o.tangage() + 180F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Object/Sparks.eff", f);
            return;
        }
    }

    public static void torpedoEnter_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Torpedo/Fontain.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Torpedo/Fontain2.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Torpedo/Fontain3.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Torpedo/Fontain4.eff", f);
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            ExplodeSurfaceWave(1, 20F, 3F);
            return;
        }
    }

    public static void Explode10Kg_Object(Point3d point3d, Vector3d vector3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.setAT0(vector3d);
            o.set(o.azimut(), o.tangage() + 180F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Object/Sparks.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Object/Flash.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Object/Spray.eff", f);
            return;
        }
    }

    public static void Explode10Kg_Land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Land/BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Land/Flame.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Land/Flash.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Land/Dirt.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Land/Spray.eff", f);
            return;
        }
    }

    public static void dudBomb_Land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Dud/BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Dud/Spray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Dud/Dirt.eff", f);
            return;
        }
    }

    public static void Explode10Kg_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Water/BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Water/Fontain.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Water/FontainSoot.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Water/Spray.eff", f);
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            ExplodeSurfaceWave(1, 9.5F, 2.75F);
            return;
        }
    }

    public static void dudBomb_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Dud/BaseSprayWater.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Dud/FontainWater.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Dud/SprayWater.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Debris/Dud/Spray2Water.eff", f);
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            return;
        }
    }

    public static void Bullet_Object(Point3d point3d, Vector3d vector3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.setAT0(vector3d);
            o.set(o.azimut(), o.tangage() + 180F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Object/Spray.eff", f);
            return;
        }
    }

    public static void Bullet_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Water/BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Water/Fontain.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Water/Spray.eff", f);
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            new ActorSnapToLand("3do/Effects/Explosion/WaterRingCannon.sim", true, l, 2.0F, 4F, 1.0F, 0.0F, 2.0F);
            return;
        }
    }

    public static void Bullet_Land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Land/BaseSpray.eff", f);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Land/Fontain.eff", f);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Land/Spray.eff", f);
        if(World.rnd().nextFloat() < 0.2F)
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Land/Flash.eff", f);
    }

    public static void Bullet_Explode_Land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        if(World.rnd().nextFloat() < 0.3F)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Land/Exp_BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Land/Exp_Flash.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Land/Exp_Spray.eff", f);
        }
    }

    public static void Bullet_Explode_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        if(World.rnd().nextFloat() < 0.3F)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Water/Exp_BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Water/Exp_Spray.eff", f);
        }
    }

    public static void Cannon_Object(Point3d point3d, Vector3d vector3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.setAT0(vector3d);
            o.set(o.azimut(), o.tangage() + 180F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Object/Sparks.eff", f);
            return;
        }
    }

    public static void Cannon_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/Fontain.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/Spray.eff", f);
            return;
        }
    }

    public static void Cannon_Explode_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        if(World.rnd().nextFloat() < 0.4F)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/20mm_Exp_BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/20mm_Exp_Fontain.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/20mm_Exp_Spray.eff", f);
        }
        o.set(0.0F, 0.0F, 0.0F);
        l.set(point3d, o);
        new ActorSnapToLand("3do/Effects/Explosion/WaterRingCannon.sim", true, l, 2.0F, 8F, 1.0F, 0.0F, 4F);
    }

    public static void Cannon_Explode_Water_2(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        if(World.rnd().nextFloat() < 0.3F)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/12mm_Exp_BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/12mm_Exp_Spray.eff", f);
        }
        o.set(0.0F, 0.0F, 0.0F);
        l.set(point3d, o);
        new ActorSnapToLand("3do/Effects/Explosion/WaterRingCannon.sim", true, l, 2.0F, 6F, 1.0F, 0.0F, 3F);
    }

    public static void CannonBigExp_Water(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Water/BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Water/Fontain.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Water/FontainSoot.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Water/Spray.eff", f);
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            new ActorSnapToLand("3do/Effects/Explosion/WaterRingCannon.sim", true, l, 3F, 9F, 1.0F, 0.0F, 4F);
            return;
        }
    }

    public static void Cannon_Land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/BaseSpray.eff", f);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/Fontain.eff", f);
        Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/Spray.eff", f);
        if(World.rnd().nextFloat() < 0.3F)
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/Flash.eff", f);
    }

    public static void Cannon_Explode_Land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        if(World.rnd().nextFloat() < 0.4F)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/20mm_Exp_BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/20mm_Exp_Flame.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/20mm_Exp_Flash.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/20mm_Exp_Fontain.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/20mm_Exp_Spray.eff", f);
        }
    }

    public static void Cannon_Explode_Land_2(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
            return;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        if(World.rnd().nextFloat() < 0.3F)
        {
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/12mm_Exp_BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/12mm_Exp_Flash.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/12mm_Exp_Spray.eff", f);
        }
    }

    public static void CannonBigExp_Land(Point3d point3d, float f, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return;
        } else
        {
            o.set(0.0F, 90F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Land/BaseSpray.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Land/Flame.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Land/Flash.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Land/Dirt.eff", f);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/CannonBig/Land/Spray.eff", f);
            return;
        }
    }

    public static void generateSound(Actor actor, Point3d point3d, float f, int i, float f1)
    {
        if(Config.isUSE_RENDER())
            if(actor == null)
                SfxExplosion.shell(point3d, 1, f, i, f1);
            else
            if(Engine.land().isWater(point3d.x, point3d.y))
                SfxExplosion.shell(point3d, 2, f, i, f1);
            else
                SfxExplosion.shell(point3d, 0, f, i, f1);
    }

    //TODO: Edited to allow nuclear rockets
    public static void generateRocket(Actor actor, Point3d point3d, float f, int i, float f1)
    {
        generateRocket(actor, point3d, f <= 15F ? 15F : f, i, f1, 0);
    }
    
    public static void generateRocket(Actor actor, Point3d point3d, float f, int i, float f1, int j)
    {
        generate(actor, point3d, f <= 15F ? 15F : f, i, f1, false, false, j);
    }

    //TODO: Generate methods edited to allow for nukes
    public static void generate(Actor actor, Point3d point3d, float f, int i, float f1, boolean flag){
    	generate(actor, point3d, f, i, f1, flag, false, 0);
    }

    public static void generate(Actor actor, Point3d point3d, float f, int i, float f1, boolean flag, boolean flag1, int k)
    {
        if(!Config.isUSE_RENDER())
            return;
        if(actor != null)
        {
            generateSound(actor, point3d, f, i, f1);
            rel.set(point3d);
            actor.pos.getAbs(tmpLoc);
            actor.pos.getCurrent(l);
            l.interpolate(tmpLoc, 0.5D);
            rel.sub(l);
            if(i == 2)
            {
                if(f1 < 3F)
                {
                    switch(World.rnd().nextInt(1, 2))
                    {
                    case 1: // '\001'
                        Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Termit1W.eff", 10F);
                        break;

                    case 2: // '\002'
                        Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Termit1SM.eff", -1F);
                        break;
                    }
                } else
                {
                    Vector3d vector3d = new Vector3d();
                    for(int j = 0; j < 36; j++)
                    {
                        vector3d.set(World.rnd().nextDouble(-20D, 20D), World.rnd().nextDouble(-20D, 20D), World.rnd().nextDouble(3D, 20D));
                        float f2 = World.rnd().nextFloat(3F, 15F);
                        BallisticProjectile ballisticprojectile = new BallisticProjectile(point3d, vector3d, f2);
                        Eff3DActor.New(ballisticprojectile, null, null, 1.0F, "3DO/Effects/Fireworks/PhosfourousFire.eff", f2);
                    }

                }
                return;
            }
            if(actor instanceof ActorLand)
            {
            	if(k > 0)
                {
                    if(k == 1)
                        if(Engine.land().isWater(point3d.x, point3d.y))
                            bombFatMan_water(point3d, -1F, 1.0F);
                        else
                            bombFatMan_land(point3d, -1F, 1.0F);
                } else

                if(f < 15F)
                {
                    if(Engine.land().isWater(point3d.x, point3d.y))
                        Explode10Kg_Water(point3d, 4F, 1.0F);
                    else
                        Explode10Kg_Land(point3d, 4F, 1.0F);
                } else
                if(f < 50F)
                {
                    if(Engine.land().isWater(point3d.x, point3d.y))
                        RS82_Water(point3d, 4F, 1.0F);
                    else
                        RS82_Land(point3d, 4F, 1.0F, flag, flag1);
                } else
                if(f < 450F)
                {
                    if(Engine.land().isWater(point3d.x, point3d.y))
                        BOMB250_Water(point3d, 4F, 1.0F);
                    else
                        BOMB250_Land(point3d, 4F, 1.0F, flag, flag1);
                } else
                if(f < 3000F)
                {
                    if(Engine.land().isWater(point3d.x, point3d.y))
                        BOMB1000a_Water(point3d, 4F, 1.0F);
                    else
                        BOMB1000a_Land(point3d, 4F, 1.0F, flag, flag1);
                } else
                if(Engine.land().isWater(point3d.x, point3d.y))
                    bomb1000_water(point3d, -1F, 1.0F);
                else
                    bomb1000_land(point3d, -1F, 1.0F, flag, flag1);
            } else
            if(f < 50F)
            {
                if(point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 5D)
                    if(Engine.land().isWater(point3d.x, point3d.y))
                        RS82_Water(point3d, 4F, 1.0F);
                    else
                        RS82_Land(point3d, 4F, 1.0F, flag, flag1);
                bomb50_land(point3d, -1F, 1.0F);
            } else
            if(f < 450F)
            {
                if(point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 10D)
                    if(Engine.land().isWater(point3d.x, point3d.y))
                        BOMB250_Water(point3d, 4F, 1.0F);
                    else
                        BOMB250_Land(point3d, 4F, 1.0F, flag, flag1);
                bomb50_land(point3d, -1F, 2.0F);
            } else
            if(f < 3000F)
            {
                if(point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 20D)
                    if(Engine.land().isWater(point3d.x, point3d.y))
                        BOMB1000a_Water(point3d, 4F, 1.0F);
                    else
                        BOMB1000a_Land(point3d, 4F, 1.0F, flag, flag1);
                bomb50_land(point3d, -1F, 2.0F);
            } else
            {
                if(point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 50D)
                    if(Engine.land().isWater(point3d.x, point3d.y))
                        bomb1000_water(point3d, -1F, 1.0F);
                    else
                        bomb1000_land(point3d, -1F, 1.0F, flag, flag1);
                bomb50_land(point3d, -1F, 10F);
            }
        }
    }

    //TODO: Modified to allow for mid-air nuke explosions
    public static void generateMidAirBombExp(Point3d point3d, float f, int i, float f1, boolean flag)
    {
    	generateMidAirBombExp(point3d, f, i, f1, flag, 0);
    }
    
    public static void generateMidAirBombExp(Point3d point3d, float f, int i, float f1, boolean flag, int x)
    {
        if(!Config.isUSE_RENDER())
            return;
        Actor actor = Engine.actorLand();
        generateSound(actor, point3d, f, i, f1);
        int j = 0;
        if(Engine.land().isWater(point3d.x, point3d.y))
            j = 1;
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        float f2;
        float f3;
        String s;
        float f5;
        if(x > 0)
        {
            s = "BombNuke";
            f5 = 3000F;
            f2 = 3000F;
            f3 = 10F;
        } else
        if(f > 450F)
        {
            s = "Bomb1000";
            f5 = 600F;
            f2 = 500F;
            f3 = 1.6F;
        } else
        if(f > 50F)
        {
            s = "Bomb250";
            f5 = 300F;
            f2 = 250F;
            f3 = 0.8F;
        } else
        {
            s = "RS82";
            f5 = 150F;
            f2 = 125F;
            f3 = 0.6F;
        }
        String s1 = "effects/Explodes/" + s + "/Land/Peaces.eff";
        String s2 = "effects/Explodes/" + s + "/Land/Burn.eff";
        Eff3DActor.New(l, 1.0F, s1, 3.5F);
        s = "effects/Explodes/Air/Zenitka/Germ_88mm/";
        float f6 = -1F;
        Eff3DActor.New(l, 1.0F, s + "SmokeBoiling.eff", f6);
        Eff3DActor.New(l, 1.0F, s + "Burn.eff", f6);
        Eff3DActor.New(l, 1.0F, s + "Sparks.eff", f6);
        Eff3DActor.New(l, 1.0F, s + "SparksP.eff", f6);
        o.set(0.0F, 0.0F, 0.0F);
        l.set(point3d, o);
        if(Engine.land().HQ_Air(point3d.x, point3d.y) >= point3d.z - (double)f1)
            ExplodeSurfaceWave(j, f2, f3);
        o.set(0.0F, 90F, 0.0F);
        l.set(point3d, o);
        Eff3DActor eff3dactor = Eff3DActor.New(l, 1.0F, s2, 1.0F);
        eff3dactor.postDestroy(Time.current() + 1500L);
        LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
        lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
        lightpointactor.light.setEmit(1.0F, f5);
        ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
    }

    private static void playShotSound(Shot shot1)
    {
        shot1.p.distanceSquared(Engine.soundListener().absPos);
    }

    public static void generateShot(Actor actor, Shot shot1)
    {
        if(!Config.isUSE_RENDER())
            return;
        float f = shot1.mass;
        playShotSound(shot1);
        rel.set(shot1.p);
        actor.pos.getAbs(tmpLoc);
        actor.pos.getCurrent(l);
        l.interpolate(tmpLoc, shot1.tickOffset);
        rel.sub(l);
        if(World.cur().isArcade() && !(actor instanceof Aircraft))
            Eff3DActor.New(actor, null, rel, 0.75F, "3DO/Effects/Fireworks/Sprite.eff", 30F);
        if(!(actor instanceof ActorLand))
        {
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Debris1A.eff", -1F);
            if(World.rnd().nextFloat() < 0.05F)
                switch(World.rnd().nextInt(1, 3))
                {
                case 1: // '\001'
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Debris1B.eff", -1F);
                    break;

                case 2: // '\002'
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Debris1C.eff", -1F);
                    break;

                case 3: // '\003'
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Debris1D.eff", -1F);
                    break;
                }
        }
        if(actor instanceof Aircraft)
            return;
        switch(shot1.bodyMaterial)
        {
        default:
            break;

        case 0: // '\0'
            if(f < 0.05F)
            {
                Bullet_Land(shot1.p, 0.5F, 1.0F);
                break;
            }
            if(f < 0.701F)
            {
                Cannon_Land(shot1.p, 4F, 1.0F);
                break;
            }
            if(f < 5F)
            {
                Explode10Kg_Land(shot1.p, 4F, 1.0F);
                break;
            }
            if(f < 50F)
                RS82_Land(shot1.p, 4F, 1.0F, false, true);
            else
                BOMB250_Land(shot1.p, 4F, 1.0F, false, true);
            break;

        case 1: // '\001'
            if(f < 0.05F)
            {
                Bullet_Water(shot1.p, 0.5F, 1.0F);
                break;
            }
            if(f < 0.701F)
            {
                Cannon_Water(shot1.p, 4F, 1.0F);
                break;
            }
            if(f < 8.55F)
            {
                Explode10Kg_Water(shot1.p, 4F, 1.0F);
                break;
            }
            if(f < 24.2F)
                RS82_Water(shot1.p, 4F, 1.0F);
            else
                BOMB250_Water(shot1.p, 4F, 1.0F);
            break;

        case 2: // '\002'
            if(f < 0.05F)
            {
                Bullet_Object(shot1.p, shot1.v, 0.5F, 1.0F);
                break;
            }
            if(f < 0.701F)
            {
                Cannon_Object(shot1.p, shot1.v, 0.5F, 1.0F);
                break;
            }
            if(f < 8.55F)
            {
                Explode10Kg_Object(shot1.p, shot1.v, 0.5F, 1.0F);
                break;
            }
            if(f < 24.2F)
                RS82_Object(shot1.p, shot1.v, 0.5F, 1.0F);
            else
                BOMB250_Object(shot1.p, shot1.v, 0.5F, 1.0F);
            break;

        case 3: // '\003'
            if(f < 0.05F)
            {
                Bullet_Object(shot1.p, shot1.v, 1.0F, 1.0F);
                break;
            }
            if(f < 0.701F)
            {
                Cannon_Object(shot1.p, shot1.v, 1.0F, 1.0F);
                break;
            }
            if(f < 8.55F)
            {
                Explode10Kg_Object(shot1.p, shot1.v, 1.0F, 1.0F);
                break;
            }
            if(f < 24.2F)
                RS82_Object(shot1.p, shot1.v, 1.0F, 1.0F);
            else
                BOMB250_Object(shot1.p, shot1.v, 1.0F, 1.0F);
            break;
        }
    }

    public static void generateExplosion(Actor actor, Point3d point3d, float f, int i, float f1, double d)
    {
        if(!Config.isUSE_RENDER())
            return;
        generateSound(actor, point3d, f, i, f1);
        rel.set(point3d);
        if(actor != null)
        {
            actor.pos.getAbs(tmpLoc);
            actor.pos.getCurrent(l);
            l.interpolate(tmpLoc, d);
            rel.sub(l);
            if(f > 0.0022F)
            {
                float f2 = Math.min(f * 300F, 5F);
                float f3 = Math.min(f1 * 20F, 10F);
                Eff3DActor eff3dactor = Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Object/light.eff", 0.1F);
                eff3dactor.postDestroy(Time.current() + 100L);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), rel.getPoint());
                lightpointactor.light.setColor(0.6F, 0.4F, 0.2F);
                lightpointactor.light.setEmit(f2, f3);
                ((Actor) (eff3dactor)).draw.lightMap().put("cannonHit", lightpointactor);
            }
        }
        if(actor == null)
        {
            Eff3DActor.New(rel, 0.75F, "3DO/Effects/Fireworks/12_Burn.eff", -1F);
            Eff3DActor.New(rel, 0.4F, "3DO/Effects/Fireworks/20_Burn.eff", -1F);
            Eff3DActor.New(rel, 0.25F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", 0.1F);
            Eff3DActor.New(rel, 1.0F, "3DO/Effects/Fireworks/20_Sparks.eff", -1F);
            Eff3DActor.New(rel, 0.75F, "3DO/Effects/Fireworks/20_SparksP.eff", -1F);
        } else
        if(actor instanceof ActorLand)
        {
            boolean flag = Engine.land().isWater(point3d.x, point3d.y);
            if(f < 0.0019F)
            {
                if(flag)
                    Bullet_Explode_Water(point3d, 4F, 1.0F);
                else
                    Bullet_Explode_Land(point3d, 4F, 1.0F);
            } else
            if(f < 0.005F)
            {
                if(flag)
                    Cannon_Explode_Water_2(point3d, 4F, 1.0F);
                else
                    Cannon_Explode_Land_2(point3d, 4F, 1.0F);
            } else
            if(f < 0.05F)
            {
                if(flag)
                    Cannon_Explode_Water(point3d, 4F, 1.0F);
                else
                    Cannon_Explode_Land(point3d, 4F, 1.0F);
            } else
            if(f < 1.0F)
            {
                if(flag)
                    CannonBigExp_Water(point3d, 4F, 1.0F);
                else
                    CannonBigExp_Land(point3d, 4F, 1.0F);
            } else
            if(f < 15F)
            {
                if(flag)
                    Explode10Kg_Water(point3d, 4F, 1.0F);
                else
                    Explode10Kg_Land(point3d, 4F, 1.0F);
            } else
            if(f < 50F)
            {
                if(flag)
                    RS82_Water(point3d, 4F, 1.0F);
                else
                    RS82_Land(point3d, 4F, 1.0F, false, true);
            } else
            if(flag)
                BOMB250_Water(point3d, 4F, 1.0F);
            else
                BOMB250_Land(point3d, 4F, 1.0F, false, true);
        } else
        if(f < 0.002F)
        {
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12_Burn.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12_Smoke.eff", -1F);
            if(World.rnd().nextFloat() < 0.3F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12_BigFlame.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12_BigFlash.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12_BigSmoke.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12_BigSparks.eff", -1F);
            }
        } else
        if(f < 0.003F)
        {
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mmPluff.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_Burn.eff", -1F);
            if(World.rnd().nextFloat() < 0.05F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_Debris.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_BigFlame2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_BigFlash2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_BigSmoke2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_BigSparks2.eff", -1F);
            }
            if(World.rnd().nextFloat() < 0.25F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_BigFlame.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_BigFlash.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_BigSmoke.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mm_BigSparks.eff", -1F);
            }
        } else
        if(f < 0.005F)
        {
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_Burn.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_Sparks.eff", -1F);
            if(World.rnd().nextFloat() < 0.35F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksP.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_BurnBig.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoilingBig.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksBig.eff", -1F);
            }
            if(World.rnd().nextFloat() < 0.1F)
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_DebrisBigA.eff", -1F);
            if(World.rnd().nextFloat() < 0.15F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksP2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_BurnBig2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoilingBig2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_DebrisBig2.eff", -1F);
            }
        } else
        if(f < 0.01F)
        {
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_Burn.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_Sparks.eff", -1F);
            if(World.rnd().nextFloat() < 0.35F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksP.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_BurnBig.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoilingBig.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksBig.eff", -1F);
            }
            if(World.rnd().nextFloat() < 0.1F)
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_DebrisBigA.eff", -1F);
            if(World.rnd().nextFloat() < 0.15F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksP2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_BurnBig2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoilingBig2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_DebrisBig2.eff", -1F);
            }
        } else
        if(f < 0.02F)
        {
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_Burn.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_Sparks.eff", -1F);
            if(World.rnd().nextFloat() < 0.35F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksP.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_BurnBig.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoilingBig.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksBig.eff", -1F);
            }
            if(World.rnd().nextFloat() < 0.1F)
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_DebrisBigA.eff", -1F);
            if(World.rnd().nextFloat() < 0.15F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksP2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_BurnBig2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoilingBig2.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_DebrisBig2.eff", -1F);
            }
        } else
        if(f < 1.0F)
        {
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_Burn.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_SmokeBoiling.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_SmokeBoilingA.eff", -1F);
            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_Sparks.eff", -1F);
            if(World.rnd().nextFloat() < 0.3F)
            {
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_BurnBig.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_SmokeBoilingBig.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_SparksP.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_Debris1B.eff", -1F);
                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_Debris1C.eff", -1F);
            }
        } else
        if(f < 9999F)
        {
            Eff3DActor.New(actor, null, rel, 3F, "3DO/Effects/Fireworks/37_Burn.eff", -1F);
            Eff3DActor.New(actor, null, rel, 3F, "3DO/Effects/Fireworks/37_SmokeBoiling.eff", -1F);
            Eff3DActor.New(actor, null, rel, 3F, "3DO/Effects/Fireworks/37_Sparks.eff", -1F);
            Eff3DActor.New(actor, null, rel, 3F, "3DO/Effects/Fireworks/37_SparksP.eff", -1F);
        }
    }

    public static void generateComicBulb(Actor actor, String s, float f)
    {
        if(!Config.isUSE_RENDER())
            return;
        if(!World.cur().isArcade())
        {
            return;
        } else
        {
            Eff3DActor.New(actor, null, null, 1.0F, "3DO/Effects/Debug/msg" + s + ".eff", f);
            return;
        }
    }

    private static Orient o = new Orient();
    private static Loc l = new Loc();
    private static Loc rel = new Loc();
    private static Loc tmpLoc = new Loc();
    public static final int AB23 = 0;
    public static final int AB250 = 1;
    public static final int AB500 = 2;
    public static final int AB1000 = 3;
    public static final int HOUSEEXPL_WOOD_SMALL = 0;
    public static final int HOUSEEXPL_WOOD_MIDDLE = 1;
    public static final int HOUSEEXPL_ROCK_MIDDLE = 2;
    public static final int HOUSEEXPL_ROCK_BIG = 3;
    public static final int HOUSEEXPL_ROCK_HUGE = 4;
    public static final int HOUSEEXPL_FUEL_SMALL = 5;
    public static final int HOUSEEXPL_FUEL_BIG = 6;
    private static boolean bEnableActorCrater = true;

}
