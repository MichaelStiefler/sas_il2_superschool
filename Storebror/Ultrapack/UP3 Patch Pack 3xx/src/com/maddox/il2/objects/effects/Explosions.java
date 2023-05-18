/*
 *  Explosions Hotfix by SAS~Storebror
 *  2016-05-30
 *  Fixing giant craters created by any explosion after big bombs
 */

/* 4.10.1 class with UP */
package com.maddox.il2.objects.effects;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3D;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorCrater;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.ActorSnapToLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.SfxExplosion;
import com.maddox.il2.objects.weapons.BallisticProjectile;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Time;

public class Explosions {
    private static Orient      o                     = new Orient();
    private static Loc         l                     = new Loc();
    private static Loc         rel                   = new Loc();
    private static Loc         tmpLoc                = new Loc();
    private static RangeRandom rnd                   = new RangeRandom();
    private static final int   LAND                  = 0;
    private static final int   WATER                 = 1;
    private static final int   OBJECT                = 2;
    private static final int   BOMB250               = 0;
    private static final int   BOMB1000              = 2;
    private static final int   RS82                  = 1;
    public static final int    HOUSEEXPL_WOOD_SMALL  = 0;
    public static final int    HOUSEEXPL_WOOD_MIDDLE = 1;
    public static final int    HOUSEEXPL_ROCK_MIDDLE = 2;
    public static final int    HOUSEEXPL_ROCK_BIG    = 3;
    public static final int    HOUSEEXPL_ROCK_HUGE   = 4;
    public static final int    HOUSEEXPL_FUEL_SMALL  = 5;
    public static final int    HOUSEEXPL_FUEL_BIG    = 6;
    private static boolean     bEnableActorCrater    = true;

    // TODO: Added by |ZUTI|: my variable
    // -------------------------------------------------
    
    // Removed by SAS~Storebror: Absolutely unnecessary!
//    private static float       ZUTI_CRATER_SIZE      = 0.0F;
//    private static long        ZUTI_TIME_TO_LIVE     = 80;
//    private static boolean     ZUTI_VARIABLES_SET    = false;
    // -------------------------------------------------

    static class MydataForSmoke {
        ActorHMesh a;
        float      tim;

        MydataForSmoke(ActorHMesh actorhmesh, float f) {
            a = actorhmesh;
            tim = f;
        }
    }

    public static void HydrogenBalloonExplosion(Loc loc, Actor actor) {
        if (Config.isUSE_RENDER()) {
            Loc loc1 = new Loc();
            Vector3d vector3d = new Vector3d();
            for (int i = 0; i < 2; i++) {
                loc1.set(loc);
                loc1.getPoint().x += World.Rnd().nextDouble(-12.0, 12.0);
                loc1.getPoint().y += World.Rnd().nextDouble(-12.0, 12.0);
                loc1.getPoint().z += World.Rnd().nextDouble(-3.0, 3.0);
                Eff3DActor.New(loc1, 1.0F, "3DO/Effects/Fireworks/Tank_Burn.eff", -1.0F);
            }
            int k = World.Rnd().nextInt(2, 6);
            for (int j = 0; j < k; j++) {
                vector3d.set((double) World.Rnd().nextFloat(-1.0F, 1.0F), (double) World.Rnd().nextFloat(-1.0F, 1.0F), (double) World.Rnd().nextFloat(-0.5F, 1.5F));
                vector3d.normalize();
                vector3d.scale((double) World.Rnd().nextFloat(4.0F, 15.0F));
                float f = World.Rnd().nextFloat(4.0F, 7.0F);
                BallisticProjectile ballisticprojectile = new BallisticProjectile(loc.getPoint(), vector3d, f);
                Eff3DActor.New(ballisticprojectile, null, null, 1.0F, "3DO/Effects/Aircraft/BlackHeavySPD.eff", f);
                Eff3DActor.New(ballisticprojectile, null, null, 1.0F, "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", f);
                Eff3DActor.New(ballisticprojectile, null, null, 1.0F, "3DO/Effects/Aircraft/FireSPD.eff", f);
            }
            SfxExplosion.crashAir(loc.getPoint(), 1);
        }
    }

    public static void runByName(String s, ActorHMesh actorhmesh, String s1, String s2, float f) {
        runByName(s, actorhmesh, s1, s2, f, null);
    }

    public static void runByName(String s, ActorHMesh actorhmesh, String s1, String s2, float f, Actor actor) {
        HookNamed hooknamed = null;
        if (s1 != null && !s1.equals("")) {
            int i = actorhmesh.mesh().hookFind(s1);
            if (i >= 0)
                hooknamed = new HookNamed(actorhmesh, s1);
            else if (s2 != null && !s2.equals("")) {
                int j = actorhmesh.mesh().hookFind(s2);
                if (j >= 0)
                    hooknamed = new HookNamed(actorhmesh, s2);
            }
        }
        if (s.equalsIgnoreCase("Tank"))
            Tank_Explode(actorhmesh.pos.getAbsPoint());
        else if (s.equalsIgnoreCase("_TankSmoke_")) {
            if (f > 0.0F) {
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingSmoke.eff", f);
            }
        } else if (s.equalsIgnoreCase("Car")) {
            Car_Explode(actorhmesh.pos.getAbsPoint());
            if (f > 0.0F) {
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/CarDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/CarDyingSmoke.eff", f);
            }
        } else if (s.equalsIgnoreCase("CarFuel")) {
            new MsgAction(0.0, actorhmesh) {
                public void doAction(Object obj) {
                    Point3d point3d = new Point3d();
                    ((Actor) obj).pos.getAbs(point3d);
                    ExplodeVagonFuel(point3d, point3d, 1.5F);
                }
            };
            if (f > 0.0F) {
                new MyMsgAction(0.43, actorhmesh, actor) {
                    public void doAction(Object obj) {
                        Point3d point3d = new Point3d();
                        ((Actor) obj).pos.getAbs(point3d);
                        float f1 = 25.0F;
                        int k = 0;
                        float f2 = 50.0F;
                        MsgExplosion.send((Actor) obj, "Body", point3d, (Actor) obj2, 0.0F, f1, k, f2);
                    }
                };
                new MsgAction(1.2, new MydataForSmoke(actorhmesh, f)) {
                    public void doAction(Object obj) {
                        Eff3DActor.New(((MydataForSmoke) obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke) obj).tim);
                    }
                };
            }
        } else if (s.equalsIgnoreCase("Artillery")) {
            Antiaircraft_Explode(actorhmesh.pos.getAbsPoint());
            if (f > 0.0F) {
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingSmoke.eff", f);
            }
        } else if (s.equalsIgnoreCase("Stationary")) {
            Antiaircraft_Explode(actorhmesh.pos.getAbsPoint());
            if (f > 0.0F) {
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingFire.eff", f * 0.7F);
                Eff3DActor.New(actorhmesh, hooknamed, null, 1.0F, "Effects/Smokes/TankDyingSmoke.eff", f);
            }
        } else if (s.equalsIgnoreCase("Aircraft"))
            Antiaircraft_Explode(actorhmesh.pos.getAbsPoint());
        else if (s.equalsIgnoreCase("Aircraft"))
            System.out.println("*** Unknown named explode: '" + s + "'");
        else if (s.equalsIgnoreCase("WagonWoodExplosives")) {
            new MsgAction(0.0, actorhmesh) {
                public void doAction(Object obj) {
                    Point3d point3d = new Point3d();
                    ((Actor) obj).pos.getAbs(point3d);
                    ExplodeVagonArmor(point3d, point3d, 2.0F);
                }
            };
            if (f > 0.0F) {
                new MyMsgAction(0.43, actorhmesh, actor) {
                    public void doAction(Object obj) {
                        Point3d point3d = new Point3d();
                        ((Actor) obj).pos.getAbs(point3d);
                        float f1 = 180.0F;
                        int k = 0;
                        float f2 = 140.0F;
                        MsgExplosion.send((Actor) obj, "Body", point3d, (Actor) obj2, 0.0F, f1, k, f2);
                    }
                };
                new MsgAction(1.2, new MydataForSmoke(actorhmesh, f)) {
                    public void doAction(Object obj) {
                        Eff3DActor.New(((MydataForSmoke) obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke) obj).tim);
                    }
                };
            }
        } else if (s.equalsIgnoreCase("WagonWood")) {
            new MsgAction(0.0, actorhmesh) {
                public void doAction(Object obj) {
                    Point3d point3d = new Point3d();
                    ((Actor) obj).pos.getAbs(point3d);
                    ExplodeVagonArmor(point3d, point3d, 2.0F);
                }
            };
            if (f > 0.0F)
                new MsgAction(1.2, new MydataForSmoke(actorhmesh, f)) {
                    public void doAction(Object obj) {
                        Eff3DActor.New(((MydataForSmoke) obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke) obj).tim);
                    }
                };
        } else if (s.equalsIgnoreCase("WagonFuel")) {
            new MsgAction(0.0, actorhmesh) {
                public void doAction(Object obj) {
                    Point3d point3d = new Point3d();
                    ((Actor) obj).pos.getAbs(point3d);
                    ExplodeVagonFuel(point3d, point3d, 2.0F);
                }
            };
            if (f > 0.0F) {
                new MyMsgAction(0.43, actorhmesh, actor) {
                    public void doAction(Object obj) {
                        Point3d point3d = new Point3d();
                        ((Actor) obj).pos.getAbs(point3d);
                        float f1 = 180.0F;
                        int k = 0;
                        float f2 = 120.0F;
                        MsgExplosion.send((Actor) obj, "Body", point3d, (Actor) obj2, 0.0F, f1, k, f2);
                    }
                };
                new MsgAction(1.2, new MydataForSmoke(actorhmesh, f)) {
                    public void doAction(Object obj) {
                        Eff3DActor.New(((MydataForSmoke) obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke) obj).tim);
                    }
                };
            }
        } else if (s.equalsIgnoreCase("WagonMetal")) {
            new MsgAction(0.0, actorhmesh) {
                public void doAction(Object obj) {
                    Point3d point3d = new Point3d();
                    ((Actor) obj).pos.getAbs(point3d);
                    ExplodeVagonArmor(point3d, point3d, 2.0F);
                }
            };
            if (f > 0.0F)
                new MsgAction(1.2, new MydataForSmoke(actorhmesh, f)) {
                    public void doAction(Object obj) {
                        Eff3DActor.New(((MydataForSmoke) obj).a, null, null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", ((MydataForSmoke) obj).tim);
                    }
                };
        }
    }

    public static void shot(Point3d point3d) {
        if (Config.isUSE_RENDER()) {
            l.set(point3d, o);
            Eff3DActor eff3dactor = Eff3DActor.New(l, 2.0F, "effects/sprites/spritesun.eff", -1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
        }
    }

    public static void HouseExplode(int i, Loc loc, float[] af) {
        if (Config.isUSE_RENDER()) {
            String s = "";
            byte byte0 = 0;
            switch (i) {
                case 0:
                case 1:
                    s = "Wood";
                    byte0 = (byte) 4;
                    break;
                case 2:
                case 3:
                case 4:
                    s = "Rock";
                    byte0 = (byte) 3;
                    break;
                case 5:
                case 6: {
                    s = "Fuel";
                    byte0 = (byte) 5;
                    break;
                }
                default:
                    System.out.println("WARNING: HouseExplode(): unknown type");
                    return;
            }
            String s1 = "effects/Explodes/Objects/House/" + s + "/Boiling.eff";
            String s2 = "effects/Explodes/Objects/House/" + s + "/Boiling2.eff";
            Eff3D.initSetBoundBox(af[0], af[1], af[2], af[3], af[4], af[5]);
            Eff3DActor.New(loc, 1.0F, s1, 3.0F);
            Eff3D.initSetBoundBox(af[0] + (af[3] - af[0]) * 0.25F, af[1] + (af[4] - af[1]) * 0.25F, af[2], af[3] - (af[3] - af[0]) * 0.25F, af[4] - (af[4] - af[1]) * 0.25F, af[2] + (af[5] - af[2]) * 0.5F);
            Eff3DActor.New(loc, 1.0F, s2, 3.0F);
            SfxExplosion.building(loc.getPoint(), byte0, af);
        }
    }

    private static void ExplodeSurfaceWave(int i, float f, float f1) {
        if (i == 0)
            new ActorSnapToLand("3do/Effects/Explosion/DustRing.sim", true, l, 1.0F, f, 1.0F, 0.0F, f1);
        else if (i == 1)
            new ActorSnapToLand("3do/Effects/Explosion/WaterRing.sim", true, l, 0.2F, f, 1.0F, 0.0F, f1);
    }

    private static void SurfaceLight(int i, float f, float f1) {
        new ActorSnapToLand("3do/Effects/Explosion/LandLight.sim", true, l, 1.0F, f, i != 0 ? 0.5F : 1.0F, 0.0F, f1);
    }

    private static void SurfaceCrater(int surfaceType, float size, float ttl) {
        try {
            if (surfaceType == LAND) {
                // TODO: Added by |ZUTI|: my own size and tile to live
                // ----------------------------------------------------------------
                // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
                // size = Explosions.ZUTI_CRATER_SIZE;
                // ttl = Explosions.ZUTI_TIME_TO_LIVE;
                // ----------------------------------------------------------------

                new ActorSnapToLand("3do/Effects/Explosion/Crater.sim", true, l, 0.2F, size, size + 2.0F, 1.0F, 0.0F, ttl);
                if (bEnableActorCrater) {
                    int i;
                    for (i = 64; i >= 2; i /= 2) {
                        if (size >= (float) i)
                            break;
                    }
                    if (i >= 2) {
                        new ActorCrater(("3do/Effects/Explosion/Crater" + i + "/Live.sim"), l, ttl);
                    }
                }
                // TODO: Added by |ZUTI|: reset flag variable here
                // ----------------------------------------------------------------
                // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
                //                Explosions.ZUTI_VARIABLES_SET = false;
                // ----------------------------------------------------------------
            }
        } catch (Exception ex) {}
        ;
    }

    private static void fontain(Point3d point3d, float size, float ttl, int surfaceType, int weaponType, boolean flag) {
        int k = 4 + (int) (Math.random() * 2.0);
        float f2 = 30.0F;
        o.set(0.0F, 90.0F, 0.0F);
        l.set(point3d, o);
        switch (surfaceType) {
            default:
                break;
            case 0: {
                String s;
                float f3;
                float f5;
                float craterSize;
                float f6;
                if (weaponType == 2) {
                    s = "Bomb1000";
                    f3 = 500.0F;
                    f5 = 600.0F;
                    craterSize = 36.0F;
                    k = 3 + (int) (Math.random() * 3.0);
                    f6 = 1.6F;
                } else if (weaponType == 0) {
                    s = "Bomb250";
                    f3 = 250.0F;
                    f5 = 300.0F;
                    craterSize = 18.0F;
                    f6 = 0.8F;
                } else {
                    s = "RS82";
                    f3 = 125.0F;
                    f5 = 150.0F;
                    craterSize = 4.5F;
                    k = 2 + (int) (Math.random() * 2.0);
                    f6 = 0.6F;
                }
                String s1 = "effects/Explodes/" + s + "/Land/Fontain.eff";
                String s3 = "effects/Explodes/" + s + "/Land/Burn.eff";
                for (int i1 = 0; i1 < k; i1++) {
                    float f8 = (float) (360.0 * Math.random());
                    float f9 = 90.0F + (2.0F * (float) Math.random() - 1.0F) * f2;
                    o.set(f8, f9, 0.0F);
                    l.set(point3d, o);
                    Eff3DActor.New(l, 1.0F, s1, size);
                }
                o.set(0.0F, 0.0F, 0.0F);
                l.set(point3d, o);
                ExplodeSurfaceWave(surfaceType, f3, f6);
                SurfaceLight(surfaceType, f5, 2.0F);

                // TODO: Modified by |ZUTI|: deleted old one and replaced with this one
                // -----------------------------------------------------------------
                SurfaceCrater(surfaceType, craterSize, ttl);
                // -----------------------------------------------------------------

                o.set(0.0F, 90.0F, 0.0F);
                l.set(point3d, o);
                Eff3DActor eff3dactor1 = Eff3DActor.New(l, 1.0F, s3, 1.0F);
                eff3dactor1.postDestroy(Time.current() + 1500L);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                lightpointactor.light.setEmit(1.0F, f5 * 2.0F);
                eff3dactor1.draw.lightMap().put("light", lightpointactor);
                break;
            }
            case WATER:
                if (weaponType == 2)
                    Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb1000/Water/Fontain.eff", size);
                else if (weaponType == 0)
                    Eff3DActor.New(l, 1.0F, "effects/Explodes/Bomb250/Water/Fontain.eff", size);
                else
                    Eff3DActor.New(l, 1.0F, "effects/Explodes/RS82/Water/Fontain.eff", size);
                o.set(0.0F, 0.0F, 0.0F);
                l.set(point3d, o);
                if (weaponType == 2)
                    ExplodeSurfaceWave(surfaceType, 750.0F, 15.0F);
                else if (weaponType == 0)
                    ExplodeSurfaceWave(surfaceType, 325.0F, 10.0F);
                else
                    ExplodeSurfaceWave(surfaceType, 50.0F, 7.0F);
                break;
            case OBJECT:
                Tank_Explode(point3d);
        }
    }

    public static void Tank_Explode(Point3d point3d) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            Tank_ExplodeCollapse(point3d);
            float f = 31.25F;
            float f2 = 150.0F;
            int i = 0;
            boolean flag = true;
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            ExplodeSurfaceWave(i, f, flag ? 0.6F : 0.8F);
            SurfaceLight(i, f2, 0.3F);
        }
    }

    public static void Antiaircraft_Explode(Point3d point3d) {
        if (Config.isUSE_RENDER())
            Tank_Explode(point3d);
    }

    public static void Car_Explode(Point3d point3d) {
        if (Config.isUSE_RENDER())
            Tank_Explode(point3d);
    }

    public static void Tank_ExplodeCollapse(Point3d point3d) {
        if (Config.isUSE_RENDER()) {
            SfxExplosion.crashTank(point3d, 0);
            explodeCollapse(point3d);
        }
    }

    private static void explodeCollapse(Point3d point3d) {
        o.set(0.0F, 90.0F, 0.0F);
        l.set(point3d, o);
        String s = "Objects/Tank_Collapse";
        float f3 = 150.0F;
        String s4 = "effects/Explodes/" + s + "/Burn.eff";
        Eff3DActor eff3dactor2 = Eff3DActor.New(l, 1.0F, s4, 0.3F);
        eff3dactor2.postDestroy(Time.current() + 1500L);
        LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d(5.0, 0.0, 0.0));
        lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
        lightpointactor.light.setEmit(1.0F, f3 * 2.0F);
        eff3dactor2.draw.lightMap().put("light", lightpointactor);
    }

    public static void Car_ExplodeCollapse(Point3d point3d) {
        if (Config.isUSE_RENDER())
            explodeCollapse(point3d);
    }

    public static void AirDrop_Land(Point3d point3d) {
        if (Config.isUSE_RENDER()) {
            float f = 4.0F;
            float f1 = 1.0F;
            if (Mission.isDeathmatch())
                bEnableActorCrater = false;
            fontain(point3d, f, f1, 0, 0, false);
            bEnableActorCrater = true;
            SfxExplosion.crashAir(point3d, 0);
        }
    }

    public static void AirDrop_Water(Point3d point3d) {
        if (Config.isUSE_RENDER()) {
            float f = 4.0F;
            float f1 = 1.0F;
            fontain(point3d, f, f1, 1, 0, false);
            SfxExplosion.crashAir(point3d, 2);
        }
    }

    public static void AirDrop_Air(Point3d point3d) {
        if (Config.isUSE_RENDER()) {
            explodeCollapse(point3d);
            SfxExplosion.crashAir(point3d, 1);
        }
    }

    public static void WreckageDrop_Water(Point3d point3d) {
        if (Config.isUSE_RENDER()) {
            float f = 3.0F;
            float f1 = 1.0F;
            fontain(point3d, f, f1, 1, 1, false);
            SfxExplosion.crashParts(point3d, 2);
        }
    }

    public static void SomethingDrop_Water(Point3d point3d, float f) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            new ActorSnapToLand("3do/Effects/Explosion/WaterRing.sim", true, l, f * 0.2F, f, 1.0F, 0.0F, 2.5F);
            SfxExplosion.crashParts(point3d, 2);
        }
    }

    public static void AirFlak(Point3d point3d, int i) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            String s = "effects/Explodes/Air/Zenitka/";
            switch (i) {
                case BOMB250:
                    s += "USSR_85mm/";
                    break;
                case RS82:
                    s += "Germ_88mm/";
                    break;
                case BOMB1000:
                    s += "USSR_25mm/";
                    break;
                default:
                    s += "Germ_20mm/";
            }
            SfxExplosion.zenitka(point3d, i);
            float f = -1.0F;
            Eff3DActor.New(l, 1.0F, s + "SmokeBoiling.eff", f);
//	        TODO: +++ Add Flash Effect to Flak Bursts +++
//			Eff3DActor.New(l, 1.0F, s + "Burn.eff", f);
//	        TODO: --- Add Flash Effect to Flak Bursts ---
            Eff3DActor.New(l, 1.0F, s + "Sparks.eff", f);
            Eff3DActor.New(l, 1.0F, s + "SparksP.eff", f);
//	        TODO: +++ Add Flash Effect to Flak Bursts +++
            if (World.Sun().ToSun.z > 0.1F) { // Daylight, don't do any changes here
                Eff3DActor.New(l, 1.0F, s + "Burn.eff", f);
            } else { // Night time, create dynamic flash effect
                float burnSize = cvt(World.Sun().ToSun.z, -0.3F, 0.1F, 10F, 1F);
                float lightPointIntensity = 0.5F + World.Rnd().nextFloat();
                float lightPointRange = cvt(World.Sun().ToSun.z, -0.3F, 0.1F, 1000F, 100F);
                Eff3DActor eff3dactor = Eff3DActor.New(l, burnSize, s + "Burn_night.eff", f);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                lightpointactor.light.setColor(1.0F, 0.9F, 0.6F);
                lightpointactor.light.setEmit(lightPointIntensity, lightPointRange);
                eff3dactor.draw.lightMap().put("light", lightpointactor);
            }
//	        TODO: --- Add Flash Effect to Flak Bursts ---
        }
    }

//  TODO: +++ Add Flash Effect to Flak Bursts +++
    public static float cvt(float f, float f1, float f2, float f3, float f4)
    {
        f = Math.min(Math.max(f, f1), f2);
        return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
    }
//  TODO: --- Add Flash Effect to Flak Bursts ---

    public static void ExplodeFuel(Point3d point3d) {
        if (Config.isUSE_RENDER()) {
            Loc loc = new Loc(point3d);
            Eff3DActor.New(loc, 1.0F, "3DO/Effects/Fireworks/Tank_Burn.eff", -1.0F);
            Eff3DActor.New(loc, 1.0F, "3DO/Effects/Fireworks/Tank_SmokeBoiling.eff", -1.0F);
            Eff3DActor.New(loc, 1.0F, "3DO/Effects/Fireworks/Tank_Sparks.eff", -1.0F);
            Eff3DActor.New(loc, 1.0F, "3DO/Effects/Fireworks/Tank_SparksP.eff", -1.0F);
            double d = point3d.z;
            World.cur();
            if (d - World.land().HQ(point3d.x, point3d.y) > 3.0)
                SfxExplosion.crashAir(point3d, 1);
            else {
                World.cur();
                if (World.land().isWater(point3d.x, point3d.y))
                    SfxExplosion.crashAir(point3d, 2);
                else
                    SfxExplosion.crashAir(point3d, 0);
            }
        }
    }

    private static void LinearExplode(Point3d point3d, Point3d point3d1, float f, float f1, String s, String s1) {
        double d = point3d.distance(point3d1);
        int i = (int) (2.0 * d / (double) f * (double) f1);
        if (i < 2)
            i = 2;
        Point3d point3d2 = new Point3d();
        for (int j = 0; j < i; j++) {
            point3d2.interpolate(point3d, point3d1, Math.random());
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d2, o);
            Eff3DActor.New(l, 1.0F, Math.random() >= 0.5 ? s1 : s, 3.0F);
        }
    }

    public static void ExplodeBridge(Point3d point3d, Point3d point3d1, float f) {
        if (Config.isUSE_RENDER()) {
            LinearExplode(point3d, point3d1, f, 1.0F, "effects/Explodes/Objects/Bridges/SmokeBoiling.eff", "effects/Explodes/Objects/Bridges/SmokeBoiling2.eff");
            SfxExplosion.bridge(point3d, point3d1, f);
        }
    }

    public static void ExplodeVagonArmor(Point3d point3d, Point3d point3d1, float f) {
        if (Config.isUSE_RENDER()) {
            Point3d point3d2 = new Point3d();
            for (int i = 0; i < 3; i++) {
                point3d2.interpolate(point3d, point3d1, Math.random());
                AirFlak(point3d2, 0);
            }
            LinearExplode(point3d, point3d1, f, 0.5F, "effects/Explodes/Objects/VagonArmor/SmokeBoiling.eff", "effects/Explodes/Objects/VagonArmor/SmokeBoiling2.eff");
            SfxExplosion.wagon(point3d, point3d1, f, 6);
        }
    }

    public static void ExplodeVagonFuel(Point3d point3d, Point3d point3d1, float f) {
        if (Config.isUSE_RENDER()) {
            LinearExplode(point3d, point3d1, f, 0.75F, "effects/Explodes/Objects/VagonFuel/SmokeBoilingFire.eff", "effects/Explodes/Objects/VagonFuel/SmokeBoilingFire2.eff");
            SfxExplosion.wagon(point3d, point3d1, f, 5);
        }
    }

    public static void bomb50_land(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, f1, "3DO/Effects/Fireworks/Tank_Burn.eff", -1.0F);
            Eff3DActor.New(l, f1, "3DO/Effects/Fireworks/Tank_SmokeBoiling.eff", -1.0F);
            Eff3DActor.New(l, f1, "3DO/Effects/Fireworks/Tank_Sparks.eff", -1.0F);
            Eff3DActor.New(l, f1, "3DO/Effects/Fireworks/Tank_SparksP.eff", -1.0F);
        }
    }

    public static void BOMB250_Land(Point3d point3d, float f, float f1, boolean flag) {
        if (Config.isUSE_RENDER()) {
            // TODO: Added by |ZUTI|
            // -----------------------------------------
            // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
//            if (!Explosions.ZUTI_VARIABLES_SET) {
//                Explosions.ZUTI_CRATER_SIZE = ZutiSupportMethods_Effects.getCraterSize(250.0F);
//                Explosions.ZUTI_TIME_TO_LIVE = ZutiSupportMethods_Effects.getCraterTimeToLive(250.0F);
//
//                Explosions.ZUTI_VARIABLES_SET = true;
//            }
            // -----------------------------------------

            // TODO: Modified by SAS~Storebror:
            // Just dynamically calculate the right values and use them for this single explosion
            // instead of carrying some useless ZUTI_CRATER_SIZE, ZUTI_TIME_TO_LIVE and ZUTI_VARIABLES_SET
            // properties all across the class!
            fontain(point3d, ZutiSupportMethods_Effects.getCraterSize(250.0F), ZutiSupportMethods_Effects.getCraterTimeToLive(250.0F), 0, 0, flag);
//            fontain(point3d, f, f1, 0, 0, flag);
        }
    }

    public static void BOMB250_Water(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER())
            fontain(point3d, f, f1, 1, 0, false);
    }

    public static void BOMB250_Object(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER())
            fontain(point3d, f, f1, 0, 0, false);
    }

    public static void BOMB1000a_Land(Point3d point3d, float f, float f1, boolean flag) {
        if (Config.isUSE_RENDER()) {
            // TODO: Added by |ZUTI|
            // -----------------------------------------
            // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
//            if (!Explosions.ZUTI_VARIABLES_SET) {
//                Explosions.ZUTI_CRATER_SIZE = ZutiSupportMethods_Effects.getCraterSize(1000.0F);
//                Explosions.ZUTI_TIME_TO_LIVE = ZutiSupportMethods_Effects.getCraterTimeToLive(1000.0F);
//
//                Explosions.ZUTI_VARIABLES_SET = true;
//            }
            // -----------------------------------------

            // TODO: Modified by SAS~Storebror:
            // Just dynamically calculate the right values and use them for this single explosion
            // instead of carrying some useless ZUTI_CRATER_SIZE, ZUTI_TIME_TO_LIVE and ZUTI_VARIABLES_SET
            // properties all across the class!
            fontain(point3d, ZutiSupportMethods_Effects.getCraterSize(250.0F), ZutiSupportMethods_Effects.getCraterTimeToLive(250.0F), 0, 2, flag);
//            fontain(point3d, f, f1, 0, 2, flag);
        }
    }

    public static void BOMB1000a_Water(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER())
            fontain(point3d, f, f1, 1, 2, false);
    }

    public static void BOMB1000a_Object(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER())
            fontain(point3d, f, f1, 0, 2, false);
    }

    public static void bomb1000_land(Point3d point3d, float f, float f1, boolean flag) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            SurfaceLight(0, 10000.0F, 1.0F);

//            // TODO: Modified by |ZUTI|: this is also called from mistel class??? Oh dear.
//            // -----------------------------------------------------------------
//            // Increase because of the mistel...
//            if (!Explosions.ZUTI_VARIABLES_SET) {
//                Explosions.ZUTI_CRATER_SIZE = ZutiSupportMethods_Effects.getCraterSize(5000.0F);
//                Explosions.ZUTI_TIME_TO_LIVE = ZutiSupportMethods_Effects.getCraterTimeToLive(5000.0F);
//
//                Explosions.ZUTI_VARIABLES_SET = true;
//            }
//            SurfaceCrater(0, 112.1F, 0.0F);
//            // -----------------------------------------------------------------

            // TODO: Modified by SAS~Storebror: this is _NOT_ called by Mistel class anymore.
            // -----------------------------------------------------------------
            // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
//            if (!Explosions.ZUTI_VARIABLES_SET) {
//                Explosions.ZUTI_CRATER_SIZE = ZutiSupportMethods_Effects.getCraterSize(1000.0F);
//                Explosions.ZUTI_TIME_TO_LIVE = ZutiSupportMethods_Effects.getCraterTimeToLive(1000.0F);
//
//                Explosions.ZUTI_VARIABLES_SET = true;
//            }
            
            // TODO: Modified by SAS~Storebror:
            // Just dynamically calculate the right values and use them for this single explosion
            // instead of carrying some useless ZUTI_CRATER_SIZE, ZUTI_TIME_TO_LIVE and ZUTI_VARIABLES_SET
            // properties all across the class!
            SurfaceCrater(0, ZutiSupportMethods_Effects.getCraterSize(1000.0F), ZutiSupportMethods_Effects.getCraterTimeToLive(1000.0F));
//            SurfaceCrater(0, 112.1F, 0.0F);
            // -----------------------------------------------------------------

            ExplodeSurfaceWave(0, 2000.0F, 4.6F);
            point3d.z += 5.0;
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(buff).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(circle).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(column).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(flare).eff", 0.1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(ring).eff", -1.0F);
        }
    }

    public static void bomb1000_water(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            SurfaceLight(0, 10000.0F, 1.0F);
            ExplodeSurfaceWave(1, 3000.0F, 6.6F);
            point3d.z += 5.0;
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(buff).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(circle).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(column).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(flare).eff", 0.1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FAB-1000(ring).eff", -1.0F);
        }
    }

    public static void bomb1000_object(Point3d point3d, float f, float f1) {
        if (!Config.isUSE_RENDER()) {
            /* empty */
        }
    }

    public static void bombFatMan_land(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            SurfaceLight(0, 910000.0F, 0.5F);

            // TODO: Modified by |ZUTI|
            // -----------------------------------------------------------------
            // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
//            if (!Explosions.ZUTI_VARIABLES_SET) {
//                Explosions.ZUTI_CRATER_SIZE = ZutiSupportMethods_Effects.getCraterSize(9999.0F);
//                Explosions.ZUTI_TIME_TO_LIVE = ZutiSupportMethods_Effects.getCraterTimeToLive(9999.0F);
//
//                Explosions.ZUTI_VARIABLES_SET = true;
//            }
            
            // TODO: Modified by SAS~Storebror:
            // Just dynamically calculate the right values and use them for this single explosion
            // instead of carrying some useless ZUTI_CRATER_SIZE, ZUTI_TIME_TO_LIVE and ZUTI_VARIABLES_SET
            // properties all across the class!
            SurfaceCrater(0, ZutiSupportMethods_Effects.getCraterSize(9999.0F), ZutiSupportMethods_Effects.getCraterTimeToLive(9999.0F));
//            SurfaceCrater(0, 312.1F, 0.0F);
            // -----------------------------------------------------------------

            ExplodeSurfaceWave(0, 4000.0F, 4.6F);
            point3d.z += 5.0;
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(shock).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(buff).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(circleL).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(column).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(flare).eff", 0.1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(ring).eff", -1.0F);
        }
    }

    public static void bombFatMan_water(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            SurfaceLight(0, 910000.0F, 0.5F);
            ExplodeSurfaceWave(1, 7000.0F, 6.6F);
            point3d.z += 5.0;
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(shock).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(buff).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(circle).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(column).eff", -1.0F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(flare).eff", 0.1F);
            Eff3DActor.New(l, 1.0F, "3DO/Effects/Fireworks/FatMan(ring).eff", -1.0F);
        }
    }

    public static void bombFatMan_object(Point3d point3d, float f, float f1) {
        if (!Config.isUSE_RENDER()) {
            /* empty */
        }
    }

    public static void bomb5000_land(Point3d point3d, float f, float f1) {
        if (!Config.isUSE_RENDER()) {
            /* empty */
        }
    }

    public static void bomb5000_water(Point3d point3d, float f, float f1) {
        if (!Config.isUSE_RENDER()) {
            /* empty */
        }
    }

    public static void bomb5000_object(Point3d point3d, float f, float f1) {
        if (!Config.isUSE_RENDER()) {
            /* empty */
        }
    }

    public static void bomb999999_object(Point3d point3d, float f, float f1) {
        if (!Config.isUSE_RENDER()) {
            /* empty */
        }
    }

    public static void RS82_Land(Point3d point3d, float f, float f1, boolean flag) {
        if (Config.isUSE_RENDER())
            fontain(point3d, f, f1, 0, 1, flag);
    }

    public static void RS82_Water(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER())
            fontain(point3d, f, f1, 1, 1, false);
    }

    public static void RS82_Object(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER())
            fontain(point3d, f, f1, 0, 1, false);
    }

    public static void Explode10Kg_Object(Point3d point3d, Vector3f vector3f, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.setAT0(vector3f);
            o.set(o.azimut(), o.tangage() + 180.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Object/Sparks.eff", f);
        }
    }

    public static void Explode10Kg_Land(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Land/Fontain.eff", f);
        }
    }

    public static void Explode10Kg_Water(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Explode10Kg/Water/Fontain.eff", f);
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            ExplodeSurfaceWave(1, 17.5F, 4.0F);
        }
    }

    public static void Bullet_Object(Point3d point3d, Vector3d vector3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.setAT0(vector3d);
            o.set(o.azimut(), o.tangage() + 180.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Object/Sparks.eff", f);
        }
    }

    public static void Bullet_Water(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Water/Fontain.eff", f);
        }
    }

    public static void Bullet_Land(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Bullet/Land/Fontain.eff", f);
        }
    }

    public static void Cannon_Object(Point3d point3d, Vector3f vector3f, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.setAT0(vector3f);
            o.set(o.azimut(), o.tangage() + 180.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Object/Sparks.eff", f);
        }
    }

    public static void Cannon_Water(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Water/Fontain.eff", f);
            o.set(0.0F, 0.0F, 0.0F);
            l.set(point3d, o);
            ExplodeSurfaceWave(1, 17.5F, 4.0F);
        }
    }

    public static void Cannon_Land(Point3d point3d, float f, float f1) {
        if (Config.isUSE_RENDER()) {
            o.set(0.0F, 90.0F, 0.0F);
            l.set(point3d, o);
            Eff3DActor.New(l, 1.0F, "effects/Explodes/Cannon/Land/Fontain.eff", f);
        }
    }

    public static void generateSound(Actor actor, Point3d point3d, float f, int i, float f1) {
        if (Config.isUSE_RENDER()) {
            if (actor == null)
                SfxExplosion.shell(point3d, 1, f, i, f1);
            else if (Engine.land().isWater(point3d.x, point3d.y))
                SfxExplosion.shell(point3d, 2, f, i, f1);
            else
                SfxExplosion.shell(point3d, 0, f, i, f1);
        }
    }

    public static void generateRocket(Actor actor, Point3d point3d, float f, int i, float f1) {
        generateRocket(actor, point3d, f, i, f1, 0);
    }

    public static void generateRocket(Actor actor, Point3d point3d, float f, int i, float f1, int j) {
        generate(actor, point3d, f <= 15.0F ? 15.0F : f, i, f1, false, j);
    }

    public static void generate(Actor actor, Point3d point3d, float f, int i, float f1, boolean flag) {
        generate(actor, point3d, f, i, f1, flag, 0);
    }

    public static void generate(Actor actor, Point3d point3d, float bombChargeMas, int i, float f1, boolean flag, int j) {
        // TODO: Added by |ZUTI|: bomb was released, store its crater effect if it is enabled
        // --------------------------------------------------

        // TODO: Check if Actor is null to avoid exception here!
//		if (actor == null)
//			System.out.println("Bomb with charge mass of >" + bombChargeMas + "kg<, hit >null<.");
//		else
//			System.out.println("Bomb with charge mass of >" + bombChargeMas + "kg<, hit >" + actor.name() + "<.");

        // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
//        Explosions.ZUTI_CRATER_SIZE = ZutiSupportMethods_Effects.getCraterSize(bombChargeMas);
//        Explosions.ZUTI_TIME_TO_LIVE = ZutiSupportMethods_Effects.getCraterTimeToLive(bombChargeMas);
//
//        Explosions.ZUTI_VARIABLES_SET = true;

        if (Mission.isNet() && Main.cur().netServerParams.isMaster() && ZutiSupportMethods_Effects.isExplosionOnSpecialArea(point3d.x, point3d.y)) {
            // Server needs to store bomb craters data so that it can be synced later on for connecting users
            boolean isLand = actor instanceof ActorLand;
            if (isLand) {
//				if( isLand )
//					System.out.println("  Crater ttl = " + Explosions.ZUTI_TIME_TO_LIVE + "s" + ", size = " + Explosions.ZUTI_CRATER_SIZE + "feet");

                if (ZutiSupportMethods_Effects.isSynced(bombChargeMas)) {
                    ZutiSupportMethods_Effects.SyncingCrater myCrater = new ZutiSupportMethods_Effects.SyncingCrater();
                    
                    // TODO: Modified by SAS~Storebror:
                    // Just dynamically calculate the right values and use them for this single explosion
                    // instead of carrying some useless ZUTI_CRATER_SIZE, ZUTI_TIME_TO_LIVE and ZUTI_VARIABLES_SET
                    // properties all across the class!
                    myCrater.expirationTime = Time.current() + ((long)ZutiSupportMethods_Effects.getCraterTimeToLive(bombChargeMas) * 1000L);
//                    myCrater.expirationTime = Time.current() + (Explosions.ZUTI_TIME_TO_LIVE * 1000);
                    myCrater.x = (float) point3d.x;
                    myCrater.y = (float) point3d.y;
                    myCrater.z = (float) point3d.z;
                    
                    // TODO: Modified by SAS~Storebror:
                    // Just dynamically calculate the right values and use them for this single explosion
                    // instead of carrying some useless ZUTI_CRATER_SIZE, ZUTI_TIME_TO_LIVE and ZUTI_VARIABLES_SET
                    // properties all across the class!
                    myCrater.size = ZutiSupportMethods_Effects.getCraterSize(bombChargeMas);
//                    myCrater.size = Explosions.ZUTI_CRATER_SIZE;

//					System.out.println("  Expiration date for crater = " + myCrater.expirationTime);
                    ZutiSupportMethods_Effects.addCrater(myCrater);
                }
            }
        }
        // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
//        // If we are not rendering, reset flag here.
//        if (!Config.isUSE_RENDER())
//            Explosions.ZUTI_VARIABLES_SET = false;
//
////		System.out.println("=========================================");
//        // --------------------------------------------------

        if (Config.isUSE_RENDER()) {
            if (actor != null) {
                generateSound(actor, point3d, bombChargeMas, i, f1);
                rel.set(point3d);
                actor.pos.getAbs(tmpLoc);
                actor.pos.getCurrent(l);
                l.interpolate(tmpLoc, 0.5);
                rel.sub(l);
                if (i == 2) {
                    if (f1 < 3.0F) {
                        switch (rnd.nextInt(1, 2)) {
                            case 1:
                                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Termit1W.eff", 10.0F);
                                break;
                            case 2:
                                Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Termit1SM.eff", -1.0F);
                        }
                    } else {
                        Vector3d vector3d = new Vector3d();
                        for (int j1 = 0; j1 < 36; j1++) {
                            vector3d.set(World.Rnd().nextDouble(-20.0, 20.0), World.Rnd().nextDouble(-20.0, 20.0), World.Rnd().nextDouble(3.0, 20.0));
                            float f2 = World.Rnd().nextFloat(3.0F, 15.0F);
                            BallisticProjectile ballisticprojectile = new BallisticProjectile(point3d, vector3d, f2);
                            Eff3DActor.New(ballisticprojectile, null, null, 1.0F, "3DO/Effects/Fireworks/PhosfourousFire.eff", f2);
                        }
                    }
                } else if (actor instanceof ActorLand) {
                    if (j > 0) {
                        if (j == 1) {
                            if (Engine.land().isWater(point3d.x, point3d.y))
                                bombFatMan_water(point3d, -1.0F, 1.0F);
                            else
                                bombFatMan_land(point3d, -1.0F, 1.0F);
                        }
                    } else if (bombChargeMas < 15.0F) {
                        if (Engine.land().isWater(point3d.x, point3d.y))
                            Explode10Kg_Water(point3d, 4.0F, 1.0F);
                        else
                            Explode10Kg_Land(point3d, 4.0F, 1.0F);
                    } else if (bombChargeMas < 50.0F) {
                        if (Engine.land().isWater(point3d.x, point3d.y))
                            RS82_Water(point3d, 4.0F, 1.0F);
                        else
                            RS82_Land(point3d, 4.0F, 1.0F, flag);
                    } else if (bombChargeMas < 450.0F) {
                        if (Engine.land().isWater(point3d.x, point3d.y))
                            BOMB250_Water(point3d, 4.0F, 1.0F);
                        else
                            BOMB250_Land(point3d, 4.0F, 1.0F, flag);
                    } else if (bombChargeMas < 3000.0F) {
                        if (Engine.land().isWater(point3d.x, point3d.y))
                            BOMB1000a_Water(point3d, 4.0F, 1.0F);
                        else
                            BOMB1000a_Land(point3d, 4.0F, 1.0F, flag);
                    } else if (Engine.land().isWater(point3d.x, point3d.y))
                        bomb1000_water(point3d, -1.0F, 1.0F);
                    else
                        bomb1000_land(point3d, -1.0F, 1.0F, flag);
                } else if (j > 0) {
                    if (j == 1) {
                        if (point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 50.0) {
                            if (Engine.land().isWater(point3d.x, point3d.y))
                                bombFatMan_water(point3d, -1.0F, 1.0F);
                            else
                                bombFatMan_land(point3d, -1.0F, 1.0F);
                        }
                        bomb50_land(point3d, -1.0F, 10.0F);
                    }
                } else if (bombChargeMas < 50.0F) {
                    if (point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 5.0) {
                        if (Engine.land().isWater(point3d.x, point3d.y))
                            RS82_Water(point3d, 4.0F, 1.0F);
                        else
                            RS82_Land(point3d, 4.0F, 1.0F, flag);
                    }
                    bomb50_land(point3d, -1.0F, 1.0F);
                } else if (bombChargeMas < 450.0F) {
                    if (point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 10.0) {
                        if (Engine.land().isWater(point3d.x, point3d.y))
                            BOMB250_Water(point3d, 4.0F, 1.0F);
                        else
                            BOMB250_Land(point3d, 4.0F, 1.0F, flag);
                    }
                    bomb50_land(point3d, -1.0F, 2.0F);
                } else if (bombChargeMas < 3000.0F) {
                    if (point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 20.0) {
                        if (Engine.land().isWater(point3d.x, point3d.y))
                            BOMB1000a_Water(point3d, 4.0F, 1.0F);
                        else
                            BOMB1000a_Land(point3d, 4.0F, 1.0F, flag);
                    }
                    bomb50_land(point3d, -1.0F, 2.0F);
                } else {
                    if (point3d.z - Engine.land().HQ_Air(point3d.x, point3d.y) < 50.0) {
                        if (Engine.land().isWater(point3d.x, point3d.y))
                            bomb1000_water(point3d, -1.0F, 1.0F);
                        else
                            bomb1000_land(point3d, -1.0F, 1.0F, flag);
                    }
                    bomb50_land(point3d, -1.0F, 10.0F);
                }
            }
        }

        // TODO: Removed by SAS~Storebror: Absolutely unnecessary!
//        // TODO: Added by |ZUTI|
//        // -----------------------------------
//        Explosions.ZUTI_VARIABLES_SET = true;
//        // -----------------------------------
    }

    private static void playShotSound(Shot shot1) {
        shot1.p.distanceSquared(Engine.soundListener().absPos);
    }

    public static void generateShot(Actor actor, Shot shot1) {
        if (Config.isUSE_RENDER()) {
            float f = shot1.mass;
            playShotSound(shot1);
            rel.set(shot1.p);
            actor.pos.getAbs(tmpLoc);
            actor.pos.getCurrent(l);
            l.interpolate(tmpLoc, shot1.tickOffset);
            rel.sub(l);
            if (World.cur().isArcade() && !(actor instanceof Aircraft))
                Eff3DActor.New(actor, null, rel, 0.75F, "3DO/Effects/Fireworks/Sprite.eff", 30.0F);
            if (!(actor instanceof ActorLand)) {
                switch (rnd.nextInt(1, 4)) {
                    case 1:
                        Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Debris1A.eff", -1.0F);
                        break;
                    case 2:
                        Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Debris1B.eff", -1.0F);
                        break;
                    case 3:
                        Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Debris1C.eff", -1.0F);
                        break;
                    case 4:
                        Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/Debris1D.eff", -1.0F);
                        break;
                }
            }
            if (!(actor instanceof Aircraft)) {
                switch (shot1.bodyMaterial) {
                    case 0:
                        if (f < 1.0F)
                            Cannon_Land(shot1.p, 4.0F, 1.0F);
                        else if (f < 5.0F)
                            Explode10Kg_Land(shot1.p, 4.0F, 1.0F);
                        else if (f < 50.0F)
                            RS82_Land(shot1.p, 4.0F, 1.0F, false);
                        else
                            BOMB250_Land(shot1.p, 4.0F, 1.0F, false);
                        break;
                    case 3:
                        break;
                    case 1:
                        if (f < 0.023F)
                            Bullet_Water(shot1.p, 0.5F, 1.0F);
                        else if (f < 0.701F)
                            Cannon_Water(shot1.p, 4.0F, 1.0F);
                        else if (f < 8.55F)
                            Explode10Kg_Water(shot1.p, 4.0F, 1.0F);
                        else if (f < 24.2F)
                            RS82_Water(shot1.p, 4.0F, 1.0F);
                        else
                            BOMB250_Water(shot1.p, 4.0F, 1.0F);
                        break;
                    case 2:
                        Bullet_Object(shot1.p, shot1.v, 0.5F, 1.0F);
                        break;
                    default:
                        Bullet_Object(shot1.p, shot1.v, 1.0F, 1.0F);
                }
            }
        }
    }

    public static void generateExplosion(Actor actor, Point3d point3d, float f, int i, float f1, double d) {
        if (Config.isUSE_RENDER()) {
            generateSound(actor, point3d, f, i, f1);
            rel.set(point3d);
            if (actor != null) {
                actor.pos.getAbs(tmpLoc);
                actor.pos.getCurrent(l);
                l.interpolate(tmpLoc, d);
                rel.sub(l);
            }
            if (actor != null) {
                if (actor instanceof ActorLand) {
                    boolean flag = Engine.land().isWater(point3d.x, point3d.y);
                    if (f < 0.0010F) {
                        if (!flag)
                            Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12_Burn.eff", -1.0F);
                    } else if (f < 0.0050F) {
                        if (!flag)
                            Eff3DActor.New(rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", -1.0F);
                    } else if (f < 0.05F) {
                        if (flag)
                            Explode10Kg_Water(point3d, 4.0F, 1.0F);
                        else
                            Explode10Kg_Land(point3d, 4.0F, 1.0F);
                    } else if (f < 1.0F) {
                        if (flag)
                            RS82_Water(point3d, 4.0F, 1.0F);
                        else
                            RS82_Land(point3d, 4.0F, 1.0F, false);
                    } else if (f < 15.0F) {
                        if (flag)
                            Explode10Kg_Water(point3d, 4.0F, 1.0F);
                        else
                            Explode10Kg_Land(point3d, 4.0F, 1.0F);
                    } else if (f < 50.0F) {
                        if (flag)
                            RS82_Water(point3d, 4.0F, 1.0F);
                        else
                            RS82_Land(point3d, 4.0F, 1.0F, false);
                    } else if (flag)
                        BOMB250_Water(point3d, 4.0F, 1.0F);
                    else
                        BOMB250_Land(point3d, 4.0F, 1.0F, false);
                } else if (f < 0.0010F)
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12_Burn.eff", -1.0F);
                else if (f < 0.0030F)
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/12mmPluff.eff", 0.15F);
                else if (f < 0.0050F) {
                    Eff3DActor.New(actor, null, rel, 0.5F, "3DO/Effects/Fireworks/20_Burn.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 0.5F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 0.5F, "3DO/Effects/Fireworks/20_Sparks.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 0.5F, "3DO/Effects/Fireworks/20_SparksP.eff", -1.0F);
                } else if (f < 0.01F) {
                    Eff3DActor.New(actor, null, rel, 0.75F, "3DO/Effects/Fireworks/20_Burn.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 0.75F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 0.75F, "3DO/Effects/Fireworks/20_Sparks.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 0.75F, "3DO/Effects/Fireworks/20_SparksP.eff", -1.0F);
                } else if (f < 0.02F) {
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_Burn.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_Sparks.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/20_SparksP.eff", -1.0F);
                } else if (f < 1.0F) {
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_Burn.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_SmokeBoiling.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_Sparks.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 1.0F, "3DO/Effects/Fireworks/37_SparksP.eff", -1.0F);
                } else if (f < 9999.0F) {
                    Eff3DActor.New(actor, null, rel, 3.0F, "3DO/Effects/Fireworks/37_Burn.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 3.0F, "3DO/Effects/Fireworks/37_SmokeBoiling.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 3.0F, "3DO/Effects/Fireworks/37_Sparks.eff", -1.0F);
                    Eff3DActor.New(actor, null, rel, 3.0F, "3DO/Effects/Fireworks/37_SparksP.eff", -1.0F);
                }
            }
        }
    }

    public static void generateComicBulb(Actor actor, String s, float f) {
        if (Config.isUSE_RENDER() && World.cur().isArcade())
            Eff3DActor.New(actor, null, null, 1.0F, "3DO/Effects/Debug/msg" + s + ".eff", f);
    }
}