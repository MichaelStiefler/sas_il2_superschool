// Source File Name:   BigshipGeneric.java

package com.maddox.il2.objects.ships;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Line2d;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Aimer;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MissileAimer;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.StrengthProperties;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.ShipAim;
import com.maddox.il2.ai.ground.ShipHunterInterface;
import com.maddox.il2.ai.ground.TgtFactory;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorDraw;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorMeshDraw;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateAdapter;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.VisibilityLong;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiStayPoint;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.ObjectsLogLevel;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.weapons.CannonMidrangeGeneric;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.MissileInterceptable;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.ships:
//            WeakBody, ShipGeneric, TankerType, TransparentTestRunway

public class BigshipGeneric extends ActorHMesh
    implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Predator, ActorAlign, ShipHunterInterface, VisibilityLong
{
    private static class TowStringMeshDraw extends ActorMeshDraw
    {

        public void render(Actor actor)
        {
            super.render(actor);
            BigshipGeneric bigshipgeneric = (BigshipGeneric)actor;
            if(bigshipgeneric.prop.propAirport == null)
                return;
            Point3d apoint3d[] = bigshipgeneric.prop.propAirport.towPRel;
            if(apoint3d == null)
                return;
            actor.pos.getRender(lRender);
            int i = apoint3d.length / 2;
            for(int j = 0; j < i; j++)
            {
                if(j != bigshipgeneric.towPortNum)
                {
                    lRender.transform(apoint3d[j * 2], p0);
                    lRender.transform(apoint3d[j * 2 + 1], p1);
                    renderTow(bigshipgeneric.prop.propAirport.towString);
                    continue;
                }
                if(Actor.isValid(bigshipgeneric.towAircraft))
                {
                    lRender.transform(apoint3d[j * 2], p0);
                    l.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    bigshipgeneric.towHook.computePos(bigshipgeneric.towAircraft, bigshipgeneric.towAircraft.pos.getRender(), l);
                    p1.set(l.getPoint());
                    renderTow(bigshipgeneric.prop.propAirport.towString);
                    lRender.transform(apoint3d[j * 2 + 1], p0);
                    l.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    bigshipgeneric.towHook.computePos(bigshipgeneric.towAircraft, bigshipgeneric.towAircraft.pos.getRender(), l);
                    p1.set(l.getPoint());
                    renderTow(bigshipgeneric.prop.propAirport.towString);
                }
            }

        }

        private void renderTow(Mesh mesh)
        {
            tmpVector.sub(p1, p0);
            mesh.setScaleXYZ((float)tmpVector.length(), 1.0F, 1.0F);
            tmpVector.normalize();
            Orient orient = l.getOrient();
            orient.setAT0(tmpVector);
            l.set(p0);
            mesh.setPos(l);
            mesh.render();
        }

        private Loc lRender = new Loc();
        private Loc l = new Loc();
        private Vector3d tmpVector = new Vector3d();
        private Point3d p0 = new Point3d();
        private Point3d p1 = new Point3d();


        public TowStringMeshDraw(ActorDraw actordraw)
        {
            super(actordraw);
        }
    }

    public static class AirportProperties
    {

        public void firstInit(BigshipGeneric bigshipgeneric)
        {
            if(bInited)
                return;
            bInited = true;
            HierMesh hiermesh = bigshipgeneric.hierMesh();
            findHook(hiermesh, "_RWY_TO", rwy[0]);
            findHook(hiermesh, "_RWY_LDG", rwy[1]);
            towString = new Mesh("3DO/Arms/ArrestorCable/mono.sim");
            ArrayList arraylist = new ArrayList();
            int i = 0;
            do
            {
                String s = i <= 9 ? "0" + i : "" + i;
                if(!findHook(hiermesh, "_TOW" + s + "A", loc))
                    break;
                arraylist.add(new Point3d(loc.getPoint()));
                findHook(hiermesh, "_TOW" + s + "B", loc);
                arraylist.add(new Point3d(loc.getPoint()));
                i++;
            } while(true);
            if(i > 0)
            {
                i *= 2;
                towPRel = new Point3d[i];
                for(int j = 0; j < i; j++)
                    towPRel[j] = (Point3d)arraylist.get(j);

            }
            fillParks(bigshipgeneric, hiermesh, "_Park", arraylist);
            if(arraylist.size() > 0)
                cellTO = new CellAirField(new com.maddox.il2.ai.air.CellObject[1][1], arraylist, 1.0D);
            fillParks(bigshipgeneric, hiermesh, "_LPark", arraylist);
            if(arraylist.size() > 0)
                cellLDG = new CellAirField(new com.maddox.il2.ai.air.CellObject[1][1], arraylist, 1.0D);
        }

        private void fillParks(BigshipGeneric bigshipgeneric, HierMesh hiermesh, String s, ArrayList arraylist)
        {
            arraylist.clear();
            int i = 0;
            do
            {
                String s1 = s + (i <= 9 ? "0" + i : "" + i);
                if(findHook(hiermesh, s1, loc))
                {
                    arraylist.add(new Point3d(-p.y, p.x, p.z));
                    i++;
                } else
                {
                    return;
                }
            } while(true);
        }

        private boolean findHook(HierMesh hiermesh, String s, Loc loc1)
        {
            int i = hiermesh.hookFind(s);
            if(i == -1)
            {
                return false;
            } else
            {
                hiermesh.hookMatrix(i, m1);
                m1.getEulers(tmp);
                o.setYPR(Geom.RAD2DEG((float)tmp[0]), 360F - Geom.RAD2DEG((float)tmp[1]), 360F - Geom.RAD2DEG((float)tmp[2]));
                p.set(m1.m03, m1.m13, m1.m23);
                loc1.set(p, o);
                return true;
            }
        }

        public Loc rwy[] = {
            new Loc(), new Loc()
        };
        public Mesh towString;
        public Point3d towPRel[];
        public CellAirField cellTO;
        public CellAirField cellLDG;
        private boolean bInited;
        private Loc loc = new Loc();
        private Point3d p = new Point3d();
        private Orient o = new Orient();
        private Matrix4d m1 = new Matrix4d();
        private double tmp[] = new double[3];


        public AirportProperties(Class class1)
        {
            bInited = false;
            Property.set(class1, "IsAirport", "true");
        }
    }

    public static class SPAWN
        implements ActorSpawn
    {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1)
        {
            float f2 = sectfile.get(s, s1, -9865.345F);
            if(f2 == -9865.345F || f2 < f || f2 > f1)
            {
                if(f2 == -9865.345F)
                    System.out.println("Ship: Value of [" + s + "]:<" + s1 + "> " + "not found");
                else
                    System.out.println("Ship: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
                throw new RuntimeException("Can't set property");
            } else
            {
                return f2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1)
        {
            String s2 = sectfile.get(s, s1);
            if(s2 == null || s2.length() <= 0)
            {
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                    System.out.print("Ship: Value of [" + s + "]:<" + s1 + "> not found");
                    throw new RuntimeException("Can't set property");
                } else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
                    System.out.println("Bigship \"" + s + "\" is not (correctly) declared in ships.ini file!");
                }
                return null;
                // ---
            } else
            {
                return new String(s2);
            }
        }

//        private static String getS(SectFile sectfile, String s, String s1, String s2)
//        {
//            String s3 = sectfile.get(s, s1);
//            if(s3 == null || s3.length() <= 0)
//                return s2;
//            else
//                return new String(s3);
//        }

        private static void tryToReadRadarProperties(SectFile sectfile, String s, ShipPartProperties shippartproperties)
        {
            if(sectfile.exist(s, "NowRadar"))
            {
                if(sectfile.exist(s, "HeadMaxYawSpeed"))
                    shippartproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed", -999F, 999F);
            }
            else
            {
                System.out.println("BigShip: Can't find NowRadar definition '" + s + "'");
                throw new RuntimeException("Can't register Ship object");
            }
        }

        private static void tryToReadGunProperties(SectFile sectfile, String s, ShipPartProperties shippartproperties)
        {
            if(sectfile.exist(s, "Gun"))
            {
                String s1 = "com.maddox.il2.objects.weapons." + getS(sectfile, s, "Gun");

                try
                {
                    shippartproperties.gunClass = Class.forName(s1);
                }
                catch(Exception exception)
                {
                    System.out.println("BigShip: Can't find gun class '" + s1 + "'");
                    throw new RuntimeException("Can't register Ship object");
                }
            }
            if(sectfile.exist(s, "AttackMaxDistance"))
                shippartproperties.ATTACK_MAX_DISTANCE = getF(sectfile, s, "AttackMaxDistance", 6F, 50000F);
            if(sectfile.exist(s, "AttackMinDistance"))
                shippartproperties.ATTACK_MIN_DISTANCE = getF(sectfile, s, "AttackMinDistance", 5F, 50000F);
            if(sectfile.exist(s, "AttackMaxRadius"))
                shippartproperties.ATTACK_MAX_RADIUS = getF(sectfile, s, "AttackMaxRadius", 6F, 50000F);
            if(sectfile.exist(s, "AttackMaxHeight"))
                shippartproperties.ATTACK_MAX_HEIGHT = getF(sectfile, s, "AttackMaxHeight", 6F, 15000F);
            if(sectfile.exist(s, "AttackMinHeight"))
                shippartproperties.ATTACK_MIN_HEIGHT = getF(sectfile, s, "AttackMinHeight", 0F, 15000F);
            if(sectfile.exist(s, "TrackingOnly"))
                shippartproperties.TRACKING_ONLY = true;
            if(sectfile.exist(s, "FireFastTargets"))
            {
                float f = getF(sectfile, s, "FireFastTargets", 0.0F, 2.0F);
                shippartproperties.ATTACK_FAST_TARGETS = (int)(f + 0.5F);
                if(shippartproperties.ATTACK_FAST_TARGETS > 2)
                    shippartproperties.ATTACK_FAST_TARGETS = 2;
            }
            if(sectfile.exist(s, "FastTargetsAngleError"))
            {
                float f1 = getF(sectfile, s, "FastTargetsAngleError", 0.0F, 45F);
                shippartproperties.FAST_TARGETS_ANGLE_ERROR = f1;
            }
            if(sectfile.exist(s, "InterceptMissiles"))
            {
                float f = getF(sectfile, s, "InterceptMissiles", 0.0F, 1.0F);
                shippartproperties.ATTACK_MISSILES = (((int)(f + 0.5F)) > 0);
            }
            if(sectfile.exist(s, "PreferSlowTarget"))
                shippartproperties.PREFER_SLOW_TARGET = true;
            if(sectfile.exist(s, "HeadMinYaw"))
                shippartproperties._HEAD_MIN_YAW = getF(sectfile, s, "HeadMinYaw", -360F, 360F);
            if(sectfile.exist(s, "HeadMaxYaw"))
                shippartproperties._HEAD_MAX_YAW = getF(sectfile, s, "HeadMaxYaw", -360F, 360F);
            if(sectfile.exist(s, "NoFireMinYaw"))
              {
                shippartproperties._NOFIRE_MIN_YAW = getF(sectfile, s, "NoFireMinYaw", -360F, 360F);
                shippartproperties.NOFIRE_FLAG = true;
              }
            if(sectfile.exist(s, "NoFireMaxYaw"))
                shippartproperties._NOFIRE_MAX_YAW = getF(sectfile, s, "NoFireMaxYaw", -360F, 360F);
            if(sectfile.exist(s, "GunMinPitch"))
                shippartproperties.GUN_MIN_PITCH = getF(sectfile, s, "GunMinPitch", -15F, 85F);
            if(sectfile.exist(s, "GunMaxPitch"))
                shippartproperties.GUN_MAX_PITCH = getF(sectfile, s, "GunMaxPitch", 0.0F, 89.9F);
            if(sectfile.exist(s, "HeadMaxYawSpeed"))
                shippartproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed", 0.1F, 999F);
            if(sectfile.exist(s, "GunMaxPitchSpeed"))
                shippartproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, s, "GunMaxPitchSpeed", 0.1F, 999F);
            if(sectfile.exist(s, "DelayAfterShoot"))
                shippartproperties.DELAY_AFTER_SHOOT = getF(sectfile, s, "DelayAfterShoot", 0.0F, 999F);
            if(sectfile.exist(s, "DelayBeforeFirstShoot"))
                shippartproperties.DELAY_BEFORE_FIRST_SHOOT = getF(sectfile, s, "DelayBeforeFirstShoot", 0.0F, 999F);
            if(sectfile.exist(s, "ChainfireTime"))
                shippartproperties.CHAINFIRE_TIME = getF(sectfile, s, "ChainfireTime", 0.0F, 600F);
            if(sectfile.exist(s, "GunHeadChunk"))
                shippartproperties.headChunkName = getS(sectfile, s, "GunHeadChunk");
            if(sectfile.exist(s, "GunBarrelChunk"))
                shippartproperties.gunChunkName = getS(sectfile, s, "GunBarrelChunk");
            if(sectfile.exist(s, "GunShellStartHook"))
                shippartproperties.gunShellStartHookName = getS(sectfile, s, "GunShellStartHook");
        }

        private static void tryToReadMissileProperties(SectFile sectfile, String s, ShipPartProperties shippartproperties)
        {
            if( bLogDetail )
                System.out.println("BigShip: coming in tryToReadMissileProperties '" + s + "'.");
            if(sectfile.exist(s, "Missile"))
            {
                String s1 = "com.maddox.il2.objects.weapons." + getS(sectfile, s, "Missile");

                try
                {
                    Class.forName(s1);
                }
                catch(Exception exception)
                {
                    System.out.println("BigShip: Can't find missile class '" + s1 + "'");
                    throw new RuntimeException("Can't register Ship object");
                }
                shippartproperties.missileName = s1;
            }
            if(sectfile.exist(s, "AttackMaxDistance"))
                shippartproperties.ATTACK_MAX_DISTANCE = getF(sectfile, s, "AttackMaxDistance", 6F, 50000F);
            if(sectfile.exist(s, "AttackMinDistance"))
                shippartproperties.ATTACK_MIN_DISTANCE = getF(sectfile, s, "AttackMinDistance", 5F, 50000F);
            if(sectfile.exist(s, "AttackMaxRadius"))
                shippartproperties.ATTACK_MAX_RADIUS = getF(sectfile, s, "AttackMaxRadius", 6F, 50000F);
            if(sectfile.exist(s, "AttackMaxHeight"))
                shippartproperties.ATTACK_MAX_HEIGHT = getF(sectfile, s, "AttackMaxHeight", 6F, 15000F);
            if(sectfile.exist(s, "AttackMinHeight"))
                shippartproperties.ATTACK_MIN_HEIGHT = getF(sectfile, s, "AttackMinHeight", 0F, 15000F);
            if(sectfile.exist(s, "FireFastTargets"))
            {
                float f = getF(sectfile, s, "FireFastTargets", 0.0F, 2.0F);
                shippartproperties.ATTACK_FAST_TARGETS = (int)(f + 0.5F);
                if(shippartproperties.ATTACK_FAST_TARGETS > 2)
                    shippartproperties.ATTACK_FAST_TARGETS = 2;
            }
            if(sectfile.exist(s, "InterceptMissiles"))
            {
                float f = getF(sectfile, s, "InterceptMissiles", 0.0F, 1.0F);
                shippartproperties.ATTACK_MISSILES = (((int)(f + 0.5F)) > 0);
            }
            if(sectfile.exist(s, "TypeOfTarget"))
            {
                String s2 = getS(sectfile, s, "TypeOfTarget");
                if( bLogDetail )
                    System.out.println("BigShip: reading TypeOfTarget as '" + s2 + "'.");
                if(s2.equals("SAM")) shippartproperties.TypeOfTarget = 1;
                else if(s2.equals("SSM")) shippartproperties.TypeOfTarget = 2;
                else if(s2.equals("SUM")) shippartproperties.TypeOfTarget = 3;
            }
            if(sectfile.exist(s, "LauncherType"))
            {
                float f = getF(sectfile, s, "LauncherType", 0.0F, 1.0F);
                shippartproperties.LauncherType = (int)(f + 1.5F);
                if(shippartproperties.LauncherType > 2)
                    shippartproperties.LauncherType = 2;
            }
            else shippartproperties.LauncherType = 1;
            if(sectfile.exist(s, "HeadMinYaw"))
                shippartproperties._HEAD_MIN_YAW = getF(sectfile, s, "HeadMinYaw", -360F, 360F);
            if(sectfile.exist(s, "HeadMaxYaw"))
                shippartproperties._HEAD_MAX_YAW = getF(sectfile, s, "HeadMaxYaw", -360F, 360F);
            if(sectfile.exist(s, "NoFireMinYaw"))
              {
                shippartproperties._NOFIRE_MIN_YAW = getF(sectfile, s, "NoFireMinYaw", -360F, 360F);
                shippartproperties.NOFIRE_FLAG = true;
              }
            if(sectfile.exist(s, "NoFireMaxYaw"))
                shippartproperties._NOFIRE_MAX_YAW = getF(sectfile, s, "NoFireMaxYaw", -360F, 360F);
            if(sectfile.exist(s, "SelfYaw"))
                shippartproperties._SELF_YAW = getF(sectfile, s, "SelfYaw", 0F, 180F);
            if(sectfile.exist(s, "GunMinPitch"))
                shippartproperties.GUN_MIN_PITCH = getF(sectfile, s, "GunMinPitch", -15F, 85F);
            if(sectfile.exist(s, "GunMaxPitch"))
                shippartproperties.GUN_MAX_PITCH = getF(sectfile, s, "GunMaxPitch", 0.0F, 89.9F);
            if(sectfile.exist(s, "HeadMaxYawSpeed"))
                shippartproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed", 0.1F, 999F);
            if(sectfile.exist(s, "GunMaxPitchSpeed"))
                shippartproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, s, "GunMaxPitchSpeed", 0.1F, 999F);
            if(sectfile.exist(s, "ChainfireTime"))
                shippartproperties.CHAINFIRE_TIME = getF(sectfile, s, "ChainfireTime", 0.0F, 600F);
            if(sectfile.exist(s, "DelayAfterShoot"))
                shippartproperties.DELAY_AFTER_SHOOT = getF(sectfile, s, "DelayAfterShoot", 0.0F, 999F);
            if(sectfile.exist(s, "DelayBeforeFirstShoot"))
                shippartproperties.DELAY_BEFORE_FIRST_SHOOT = getF(sectfile, s, "DelayBeforeFirstShoot", 0.0F, 999F);
            if(sectfile.exist(s, "GunHeadChunk"))
                shippartproperties.headChunkName = getS(sectfile, s, "GunHeadChunk");
            if(sectfile.exist(s, "GunBarrelChunk"))
                shippartproperties.gunChunkName = getS(sectfile, s, "GunBarrelChunk");
            if(sectfile.exist(s, "GunShellStartHook"))
                shippartproperties.gunShellStartHookName = getS(sectfile, s, "GunShellStartHook");
        }

        private static ShipProperties LoadShipProperties(SectFile sectfile, String s, Class class1)
        {
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
            String checkMesh = getS(sectfile, s, "Mesh");
            if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && (checkMesh == null || checkMesh.length() == 0)) return null;
            // TODO: ---
            ShipProperties shipproperties = new ShipProperties();
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
//            shipproperties.meshName = getS(sectfile, s, "Mesh");
            shipproperties.meshName = checkMesh;
            // TODO: ---
            shipproperties.soundName = getS(sectfile, s, "SoundMove");
            if(shipproperties.soundName.equalsIgnoreCase("none"))
                shipproperties.soundName = null;
            shipproperties.SLIDER_DIST = getF(sectfile, s, "SliderDistance", 5F, 1000F);
            shipproperties.SPEED = BigshipGeneric.KmHourToMSec(getF(sectfile, s, "Speed", 0.5F, 200F));
            shipproperties.DELAY_RESPAWN_MIN = 15F;
            shipproperties.DELAY_RESPAWN_MAX = 30F;
            if(sectfile.exist(s, "StabilizeFactor"))
                shipproperties.STABILIZE_FACTOR = getF(sectfile, s, "StabilizeFactor", 0.5F, 5F);
            else
                shipproperties.STABILIZE_FACTOR = 1.0F;
            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", shipproperties.meshName);
            Property.set(class1, "speed", shipproperties.SPEED);
            int i;
            for(i = 0; sectfile.sectionIndex(s + ":Part" + i) >= 0; i++);
            if(i <= 0)
            {
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                    throw new RuntimeException("Can't register BigShip object");
                return null;
                // ---
            }
            if(i >= MAX_PARTS)
            {
                System.out.println("BigShip: Too many parts in " + s + ".");
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                  throw new RuntimeException("Can't register BigShip object");
                return null;
                // ---
            }
//            bLogDetail = (Config.cur.ini.get("Console", "LOGDetailShip", 0) == 1);
            if( bLogDetail )
                System.out.println("BigShip: read ships.ini '" + s + "'.");
            shipproperties.propparts = new ShipPartProperties[i];
            shipproperties.nGuns = 0;
            shipproperties.nRadars = 0;
            for(int j = 0; j < i; j++)
            {
                String s1 = s + ":Part" + j;
                ShipPartProperties shippartproperties = new ShipPartProperties();
                shipproperties.propparts[j] = shippartproperties;
                shippartproperties.baseChunkName = getS(sectfile, s1, "BaseChunk");
                int l;
                for(l = 0; sectfile.exist(s1, "AdditionalCollisionChunk" + l); l++);
                if(l > MAX_USER_ADDITIONAL_COLLISION_CHUNKS)
                {
                    System.out.println("BigShip: Too many addcollischunks in '" + s1 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register BigShip object");
                    return null;
                    // ---
                }
                shippartproperties.additCollisChunkName = new String[l];
                for(int i1 = 0; i1 < l; i1++)
                    shippartproperties.additCollisChunkName[i1] = getS(sectfile, s1, "AdditionalCollisionChunk" + i1);

                String s2 = null;
                if(sectfile.exist(s1, "strengthBasedOnThisSection"))
                    s2 = getS(sectfile, s1, "strengthBasedOnThisSection");
                if(!shippartproperties.stre.read("Bigship", sectfile, s2, s1))
                {
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        System.out.println("BigShip: Error in part properties for '" + s + "'");
                        throw new RuntimeException("Can't register Bigship object");
                    }
                    return null;
                    // ---
                }
                if(sectfile.exist(s1, "Vital"))
                {
                    shippartproperties.dmgDepth = getF(sectfile, s1, "damageDepth", 0.0F, 99F);
                    shippartproperties.dmgPitch = getF(sectfile, s1, "damagePitch", -89F, 89F);
                    shippartproperties.dmgRoll = getF(sectfile, s1, "damageRoll", 0.0F, 89F);
                    shippartproperties.dmgTime = getF(sectfile, s1, "damageTime", 1.0F, 1200F);
                    shippartproperties.BLACK_DAMAGE = 0.6666667F;
                } else
                {
                    shippartproperties.dmgDepth = -1F;
                    shippartproperties.BLACK_DAMAGE = 1.0F;
                }
                if(!sectfile.exist(s1, "Gun") && !sectfile.exist(s1, "gunBasedOnThisSection")
                   && !sectfile.exist(s1, "Missile") && !sectfile.exist(s1, "missileBasedOnThisSection")
                   && !sectfile.exist(s1, "NowRadar") && !sectfile.exist(s1, "radarBasedOnThisSection"))
                {
                    shippartproperties.gun_idx = -1;
                    shippartproperties.radar_idx = -1;
                    continue;
                }
                if(shippartproperties.isItLifeKeeper())
                {
                    System.out.println("*** ERROR: bigship: vital with gun or radar");
                    shippartproperties.gun_idx = -1;
                    shippartproperties.radar_idx = -1;
                    continue;
                }
                if(sectfile.exist(s1, "NowRadar") || sectfile.exist(s1, "radarBasedOnThisSection"))
                {
                    shippartproperties.gun_idx = -1;
                    shippartproperties.radar_idx = shipproperties.nRadars++;
                    if(shipproperties.nRadars > MAX_GUNS)
                    {
                        System.out.println("BigShip: Too many radars in " + s + ".");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                          throw new RuntimeException("Can't register BigShip object");
                        return null;
                        // ---
                    }
                    shippartproperties.NOW_RADAR = true;
                    shippartproperties.HEAD_MAX_YAW_SPEED = -1000F;
                    if(sectfile.exist(s1, "radarBasedOnThisSection"))
                    {
                        String s13 = getS(sectfile, s1, "radarBasedOnThisSection");
                        tryToReadRadarProperties(sectfile, s13, shippartproperties);
                    }
                    else
                        tryToReadRadarProperties(sectfile, s1, shippartproperties);
                    shippartproperties.headChunkName = shippartproperties.baseChunkName;
                    if(shippartproperties.HEAD_MAX_YAW_SPEED <= -1000F || shippartproperties.headChunkName == null)
                    {
                        System.out.println("BigShip: Not enough 'radar' data  in '" + s1 + "'");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                          throw new RuntimeException("Can't register BigShip object");
                        return null;
                        // ---
                    }
                    if( bLogDetail )
                        System.out.println("BigShip: read NowRadar in Part:" + j + ", as Radar No." + shippartproperties.radar_idx + ".");
                    continue;
                }
                else if(sectfile.exist(s1, "Missile") || sectfile.exist(s1, "missileBasedOnThisSection"))
                {
                    shippartproperties.gun_idx = shipproperties.nGuns++;
                    shippartproperties.radar_idx = -1;
                    if(shipproperties.nGuns > MAX_GUNS)
                    {
                        System.out.println("BigShip: Too many guns in " + s + ".");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                          throw new RuntimeException("Can't register BigShip object");
                        return null;
                        // ---
                    }

                    shippartproperties.gunClass = null;
                    shippartproperties.missileName = null;
                    shippartproperties.ATTACK_MAX_DISTANCE = -1000F;
                    shippartproperties.ATTACK_MIN_DISTANCE = 5F;
                    shippartproperties.ATTACK_MAX_RADIUS = -1000F;
                    shippartproperties.ATTACK_MAX_HEIGHT = -1000F;
                    shippartproperties.ATTACK_MIN_HEIGHT = 0F;
                    shippartproperties.TRACKING_ONLY = false;
                    shippartproperties.NOW_RADAR = false;
                    shippartproperties.TypeOfTarget = 0;
                    shippartproperties.LauncherType = 0;
                    shippartproperties.ATTACK_FAST_TARGETS = 1;
                    shippartproperties.FAST_TARGETS_ANGLE_ERROR = 0.0F;
                    shippartproperties.ATTACK_MISSILES = false;
                    shippartproperties.PREFER_SLOW_TARGET = false;
                    shippartproperties._HEAD_MIN_YAW = -1000F;
                    shippartproperties._HEAD_MAX_YAW = -1000F;
                    shippartproperties._NOFIRE_MIN_YAW = -1000F;
                    shippartproperties._NOFIRE_MAX_YAW = -1000F;
                    shippartproperties.NOFIRE_FLAG = false;
                    shippartproperties.NOHEADING_FLAG = false;
                    shippartproperties._SELF_YAW = 0F;
                    shippartproperties.GUN_MIN_PITCH = -1000F;
                    shippartproperties.GUN_STD_PITCH = -1000F;
                    shippartproperties.GUN_MAX_PITCH = -1000F;
                    shippartproperties.HEAD_MAX_YAW_SPEED = -1000F;
                    shippartproperties.GUN_MAX_PITCH_SPEED = -1000F;
                    shippartproperties.DELAY_AFTER_SHOOT = -1000F;
                    shippartproperties.DELAY_BEFORE_FIRST_SHOOT = 0F;
                    shippartproperties.CHAINFIRE_TIME = -1000F;
                    shippartproperties.headChunkName = null;
                    shippartproperties.gunChunkName = null;
                    shippartproperties.gunShellStartHookName = null;
                    if(sectfile.exist(s1, "missileBasedOnThisSection"))
                    {
                        String s3 = getS(sectfile, s1, "missileBasedOnThisSection");
                        tryToReadMissileProperties(sectfile, s3, shippartproperties);
                    }
                    tryToReadMissileProperties(sectfile, s1, shippartproperties);
                    if(shippartproperties.TypeOfTarget == 0 || shippartproperties.missileName == null || shippartproperties.ATTACK_MAX_DISTANCE <= -1000F || shippartproperties.ATTACK_MAX_RADIUS <= -1000F || shippartproperties.ATTACK_MAX_HEIGHT <= -1000F || shippartproperties._HEAD_MIN_YAW <= -1000F || shippartproperties._HEAD_MAX_YAW <= -1000F || shippartproperties.GUN_MIN_PITCH <= -1000F || shippartproperties.GUN_MAX_PITCH <= -1000F || shippartproperties.HEAD_MAX_YAW_SPEED <= -1000F || shippartproperties.GUN_MAX_PITCH_SPEED <= -1000F || shippartproperties.DELAY_AFTER_SHOOT <= -1000F || shippartproperties.CHAINFIRE_TIME <= -1000F || shippartproperties.headChunkName == null || shippartproperties.gunChunkName == null || shippartproperties.gunShellStartHookName == null)
                    {
                        if( bLogDetail )
                        {
                            System.out.println("BigShip: TypeOfTarget:" + shippartproperties.TypeOfTarget);
                            System.out.println("BigShip: missileName:" + shippartproperties.missileName);
                            System.out.println("BigShip: ATTACK_MAX_DISTANCE:" + shippartproperties.ATTACK_MAX_DISTANCE);
                            System.out.println("BigShip: ATTACK_MIN_DISTANCE:" + shippartproperties.ATTACK_MIN_DISTANCE);
                            System.out.println("BigShip: ATTACK_MAX_RADIUS:" + shippartproperties.ATTACK_MAX_RADIUS);
                            System.out.println("BigShip: ATTACK_MAX_HEIGHT:" + shippartproperties.ATTACK_MAX_HEIGHT);
                            System.out.println("BigShip: _HEAD_MIN_YAW:" + shippartproperties._HEAD_MIN_YAW);
                            System.out.println("BigShip: _HEAD_MAX_YAW:" + shippartproperties._HEAD_MAX_YAW);
                            System.out.println("BigShip: GUN_MIN_PITCH:" + shippartproperties.GUN_MIN_PITCH);
                            System.out.println("BigShip: GUN_MAX_PITCH:" + shippartproperties.GUN_MAX_PITCH);
                            System.out.println("BigShip: HEAD_MAX_YAW_SPEED:" + shippartproperties.HEAD_MAX_YAW_SPEED);
                            System.out.println("BigShip: GUN_MAX_PITCH_SPEED:" + shippartproperties.GUN_MAX_PITCH_SPEED);
                            System.out.println("BigShip: DELAY_AFTER_SHOOT:" + shippartproperties.DELAY_AFTER_SHOOT);
                            System.out.println("BigShip: CHAINFIRE_TIME:" + shippartproperties.CHAINFIRE_TIME);
                            System.out.println("BigShip: headChunkName:" + shippartproperties.headChunkName);
                            System.out.println("BigShip: gunChunkName:" + shippartproperties.gunChunkName);
                            System.out.println("BigShip: gunShellStartHookName:" + shippartproperties.gunShellStartHookName);
                        }
                        System.out.println("BigShip: Not enough 'missile' data  in '" + s1 + "'");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                          throw new RuntimeException("Can't register BigShip object");
                        return null;
                        // ---
                    }
                    shippartproperties.WEAPONS_MASK = 4;
                    if(shippartproperties.WEAPONS_MASK == 0)
                    {
                        System.out.println("BigShip: Undefined weapon type in gun class '" + shippartproperties.gunClass.getName() + "'");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                          throw new RuntimeException("Can't register BigShip object");
                        return null;
                        // ---
                    }
                    if(shippartproperties._HEAD_MIN_YAW > shippartproperties._HEAD_MAX_YAW)
                    {
                        System.out.println("BigShip: Wrong yaw angles in missile " + s1 + ".");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                          throw new RuntimeException("Can't register BigShip object");
                        return null;
                        // ---
                    }
                    if(shippartproperties._NOFIRE_MIN_YAW > shippartproperties._NOFIRE_MAX_YAW)
                    {
                        System.out.println("BigShip: Wrong NoFire yaw angles in missile " + s1 + ".");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                          throw new RuntimeException("Can't register BigShip object");
                        return null;
                        // ---
                    }
                    shippartproperties.HEAD_STD_YAW = 0.0F;
                    shippartproperties.HEAD_YAW_RANGE.set(shippartproperties._HEAD_MIN_YAW, shippartproperties._HEAD_MAX_YAW);
                    float self_head_min_yaw = shippartproperties._HEAD_MIN_YAW - shippartproperties._SELF_YAW;
                    float self_head_max_yaw = shippartproperties._HEAD_MAX_YAW + shippartproperties._SELF_YAW;
                    if( self_head_max_yaw - self_head_min_yaw > 360 )
                    {
                        self_head_max_yaw = 360;
                        self_head_min_yaw = 0;
                    }
                    if( self_head_max_yaw > 360 ) self_head_max_yaw = 360;
                    if( self_head_min_yaw < -360 ) self_head_min_yaw = -360;
                    shippartproperties.SELF_YAW_RANGE.set(self_head_min_yaw, self_head_max_yaw);
                    if( shippartproperties.NOFIRE_FLAG )
                    {
                        shippartproperties.NOFIRE_YAW_RANGE.set(shippartproperties._NOFIRE_MIN_YAW, shippartproperties._NOFIRE_MAX_YAW);
                        float noheading_max_yaw = shippartproperties._NOFIRE_MAX_YAW - shippartproperties._SELF_YAW;
                        float noheading_min_yaw = shippartproperties._NOFIRE_MIN_YAW + shippartproperties._SELF_YAW;
                        if( noheading_min_yaw >= noheading_max_yaw ) shippartproperties.NOHEADING_FLAG = false;
                        else
                        {
                            shippartproperties.NOSELF_YAW_RANGE.set(noheading_min_yaw, noheading_max_yaw);
                            shippartproperties.NOHEADING_FLAG = true;
                        }
                    }
                    if( bLogDetail )
                        System.out.println("BigShip: read Missile in Part:" + j + ", as Gun No." + shippartproperties.gun_idx + ".");
                }
                else{
                    shippartproperties.gun_idx = shipproperties.nGuns++;
                    shippartproperties.radar_idx = -1;
                    if(shipproperties.nGuns > MAX_GUNS)
                    {
                        System.out.println("BigShip: Too many guns in " + s + ".");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register BigShip object");
                    return null;
                    // ---
                    }

                    shippartproperties.gunClass = null;
                    shippartproperties.missileName = null;
                    shippartproperties.ATTACK_MAX_DISTANCE = -1000F;
                    shippartproperties.ATTACK_MIN_DISTANCE = 5F;
                    shippartproperties.ATTACK_MAX_RADIUS = -1000F;
                    shippartproperties.ATTACK_MAX_HEIGHT = -1000F;
                    shippartproperties.ATTACK_MIN_HEIGHT = 0F;
                    shippartproperties.TRACKING_ONLY = false;
                    shippartproperties.NOW_RADAR = false;
                    shippartproperties.TypeOfTarget = 0;
                    shippartproperties.LauncherType = 0;
                    shippartproperties.ATTACK_FAST_TARGETS = 1;
                    shippartproperties.FAST_TARGETS_ANGLE_ERROR = 0.0F;
                    shippartproperties.ATTACK_MISSILES = false;
                    shippartproperties.PREFER_SLOW_TARGET = false;
                    shippartproperties._HEAD_MIN_YAW = -1000F;
                    shippartproperties._HEAD_MAX_YAW = -1000F;
                    shippartproperties._NOFIRE_MIN_YAW = -1000F;
                    shippartproperties._NOFIRE_MAX_YAW = -1000F;
                    shippartproperties.NOFIRE_FLAG = false;
                    shippartproperties.NOHEADING_FLAG = false;
                    shippartproperties.GUN_MIN_PITCH = -1000F;
                    shippartproperties.GUN_STD_PITCH = -1000F;
                    shippartproperties.GUN_MAX_PITCH = -1000F;
                    shippartproperties.HEAD_MAX_YAW_SPEED = -1000F;
                    shippartproperties.GUN_MAX_PITCH_SPEED = -1000F;
                    shippartproperties.DELAY_AFTER_SHOOT = -1000F;
                    shippartproperties.DELAY_BEFORE_FIRST_SHOOT = 0F;
                    shippartproperties.CHAINFIRE_TIME = -1000F;
                    shippartproperties.headChunkName = null;
                    shippartproperties.gunChunkName = null;
                    shippartproperties.gunShellStartHookName = null;
                    if(sectfile.exist(s1, "gunBasedOnThisSection"))
                    {
                        String s3 = getS(sectfile, s1, "gunBasedOnThisSection");
                        tryToReadGunProperties(sectfile, s3, shippartproperties);
                    }
                    tryToReadGunProperties(sectfile, s1, shippartproperties);
                    if(shippartproperties.gunClass == null || shippartproperties.ATTACK_MAX_DISTANCE <= -1000F || shippartproperties.ATTACK_MAX_RADIUS <= -1000F || shippartproperties.ATTACK_MAX_HEIGHT <= -1000F || shippartproperties._HEAD_MIN_YAW <= -1000F || shippartproperties._HEAD_MAX_YAW <= -1000F || shippartproperties.GUN_MIN_PITCH <= -1000F || shippartproperties.GUN_MAX_PITCH <= -1000F || shippartproperties.HEAD_MAX_YAW_SPEED <= -1000F || shippartproperties.GUN_MAX_PITCH_SPEED <= -1000F || shippartproperties.DELAY_AFTER_SHOOT <= -1000F || shippartproperties.CHAINFIRE_TIME <= -1000F || shippartproperties.headChunkName == null || shippartproperties.gunChunkName == null || shippartproperties.gunShellStartHookName == null)
                    {
                        System.out.println("BigShip: Not enough 'gun' data  in '" + s1 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register BigShip object");
                    return null;
                    // ---
                    }
                    shippartproperties.WEAPONS_MASK = Gun.getProperties(shippartproperties.gunClass).weaponType;
                    if(shippartproperties.WEAPONS_MASK == 0)
                    {
                        System.out.println("BigShip: Undefined weapon type in gun class '" + shippartproperties.gunClass.getName() + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register BigShip object");
                    return null;
                    // ---
                    }
                    if(shippartproperties._HEAD_MIN_YAW > shippartproperties._HEAD_MAX_YAW)
                    {
                        System.out.println("BigShip: Wrong yaw angles in gun " + s1 + ".");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register BigShip object");
                    return null;
                    // ---
                    }
                    if(shippartproperties._NOFIRE_MIN_YAW > shippartproperties._NOFIRE_MAX_YAW)
                    {
                        System.out.println("BigShip: Wrong NoFire yaw angles in gun " + s1 + ".");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register BigShip object");
                    return null;
                    // ---
                    }
                    shippartproperties.HEAD_STD_YAW = 0.0F;
                    shippartproperties.HEAD_YAW_RANGE.set(shippartproperties._HEAD_MIN_YAW, shippartproperties._HEAD_MAX_YAW);
                    if( shippartproperties.NOFIRE_FLAG ) shippartproperties.NOFIRE_YAW_RANGE.set(shippartproperties._NOFIRE_MIN_YAW, shippartproperties._NOFIRE_MAX_YAW);
                    if( bLogDetail )
                        System.out.println("BigShip: read Gun in Part:" + j + ", as Gun No." + shippartproperties.gun_idx + ".");
                }
            }

            shipproperties.WEAPONS_MASK = 0;
            shipproperties.ATTACK_MAX_DISTANCE = 1.0F;
            for(int k = 0; k < shipproperties.propparts.length; k++)
            {
                if(!shipproperties.propparts[k].haveGun())
                    continue;
                shipproperties.WEAPONS_MASK |= shipproperties.propparts[k].WEAPONS_MASK;
                if(shipproperties.ATTACK_MAX_DISTANCE < shipproperties.propparts[k].ATTACK_MAX_DISTANCE)
                    shipproperties.ATTACK_MAX_DISTANCE = shipproperties.propparts[k].ATTACK_MAX_DISTANCE;
            }

            if(sectfile.get(s, "IsAirport", false))
            {
                shipproperties.propAirport = new AirportProperties(class1);
                shipproperties.typicalPlaneClass = sectfile.get(s, "TypicalPlaneClass", "");
            }
            return shipproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            BigshipGeneric bigshipgeneric = null;
            try
            {
                BigshipGeneric.constr_arg1 = proper;
                BigshipGeneric.constr_arg2 = actorspawnarg;
                bigshipgeneric = (BigshipGeneric)cls.newInstance();
                BigshipGeneric.constr_arg1 = null;
                BigshipGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                BigshipGeneric.constr_arg1 = null;
                BigshipGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Ship object [class:" + cls.getName() + "]");
                return null;
            }
            return bigshipgeneric;
        }

        public Class cls;
        public ShipProperties proper;

        public SPAWN(Class class1)
        {
            try
            {
                String s = class1.getName();
                int i = s.lastIndexOf('.');
                int j = s.lastIndexOf('$');
                if(i < j)
                    i = j;
                String s1 = s.substring(i + 1);
                proper = LoadShipProperties(Statics.getShipsFile(), s1, class1);

                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL && proper == null) return;
                // TODO: ---

            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in spawn: " + class1.getName());
            }
            cls = class1;
            Spawn.add(cls, this);
        }
    }

    class Mirror extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
            {
                int j1 = netmsginput.readUnsignedByte();
                switch(j1)
                {
                case REQUEST_LOC:
                    double d = netmsginput.readDouble();
                    double d1 = netmsginput.readDouble();
                    double d2 = netmsginput.readDouble();
                    float f1 = netmsginput.readFloat();
                    float f2 = netmsginput.readFloat();
                    float f3 = netmsginput.readFloat();
                    Loc loc = new Loc(d, d1, d2, f1, f2, f3);
                    if(airport != null)
                        airport.setClientLoc(loc);
                    return true;

                case 73:
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted);
                    }
                    timeOfDeath = netmsginput.readLong();
                    if(timeOfDeath < 0L)
                    {
                        if(dying == 0)
                        {
                            makeLive();
                            setDefaultLivePose();
                            forgetAllAiming();
                            appearSailorsMats();
                        }
                    } else
                    if(dying == 0)
                        Die(null, timeOfDeath, false, true);
                    return true;

                case 82:
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted1);
                    }
                    makeLive();
                    setDefaultLivePose();
                    forgetAllAiming();
                    appearSailorsMats();
                    setDiedFlag(false);
                    tmInterpoStart = tmInterpoEnd = 0L;
                    bodyDepth = bodyPitch = bodyRoll = 0.0F;
                    bodyDepth0 = bodyPitch0 = bodyRoll0 = 0.0F;
                    bodyDepth1 = bodyPitch1 = bodyRoll1 = 0.0F;
                    setPosition();
                    pos.reset();
                    return true;

                case 83:
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted2);
                    }
                    int i2 = netmsginput.available();
                    if(i2 > 0 && !Mission.isDogfight())
                    {
                        int l2 = netmsginput.readUnsignedByte();
                        float f = l2;
                        if(path != null && l2 != 127 && f < CURRSPEED)
                        {
                            CURRSPEED = f;
                            computeNewPath();
                        }
                        i2--;
                    }
                    int i3 = (parts.length + 3) / 4;
                    if(i2 != i3)
                    {
                        System.out.println("*** net bigship S");
                        return true;
                    }
                    if(i3 <= 0)
                    {
                        System.out.println("*** net bigship S0");
                        return true;
                    }
                    int l3 = 0;
                    for(int j4 = 0; j4 < i2; j4++)
                    {
                        int i5 = netmsginput.readUnsignedByte();
                        for(int k5 = 0; k5 < 4 && l3 < parts.length; k5++)
                        {
                            int i6 = i5 >>> k5 * 2 & 3;
                            if(i6 <= parts[l3].state)
                            {
                                l3++;
                                continue;
                            }
                            if(i6 == 2)
                            {
                                parts[l3].damage = 0.0F;
                                parts[l3].mirror_initiator = null;
                            }
                            parts[l3].state = i6;
                            visualsInjurePart(l3, true);
                            l3++;
                        }

                    }

                    return true;

                case 100:
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted3 = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted3);
                    }
                    int j2 = netmsginput.available();
                    if(j2 != 8)
                    {
                        System.out.println("*** net bigship d");
                        return true;
                    }
                    if(dying != 0)
                    {
                        return true;
                    } else
                    {
                        computeInterpolatedDPR(NetServerParams.getServerTime());
                        bodyDepth0 = bodyDepth;
                        bodyPitch0 = bodyPitch;
                        bodyRoll0 = bodyRoll;
                        bodyDepth1 = (float)(1000D * ((double)(netmsginput.readUnsignedShort() & 0x7fff) / 32767D));
                        bodyPitch1 = (float)(90D * ((double)netmsginput.readShort() / 32767D));
                        bodyRoll1 = (float)(90D * ((double)netmsginput.readShort() / 32767D));
                        tmInterpoStart = tmInterpoEnd = NetServerParams.getServerTime();
                        tmInterpoEnd += (long)(1000D * (1200D * ((double)(netmsginput.readUnsignedShort() & 0x7fff) / 32767D)));
                        computeInterpolatedDPR(NetServerParams.getServerTime());
                        return true;
                    }

                case 68:
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted4 = new NetMsgGuaranted(netmsginput, 1);
                        post(netmsgguaranted4);
                    }
                    int j3 = netmsginput.available();
                    boolean flag;
                    if(j3 == 8 + NetMsgInput.netObjReferenceLen() + 8 + 8)
                        flag = false;
                    else
                    if(j3 == 8 + NetMsgInput.netObjReferenceLen() + 8 + 8 + 8)
                    {
                        flag = true;
                    } else
                    {
                        System.out.println("*** net bigship D");
                        return true;
                    }
                    if(dying != 0)
                        return true;
                    timeOfDeath = netmsginput.readLong();
                    if(Mission.isDeathmatch())
                        timeOfDeath = NetServerParams.getServerTime();
                    if(timeOfDeath < 0L)
                    {
                        System.out.println("*** net bigship D tm");
                        return true;
                    }
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                    computeInterpolatedDPR(NetServerParams.getServerTime());
                    bodyDepth0 = bodyDepth;
                    bodyPitch0 = bodyPitch;
                    bodyRoll0 = bodyRoll;
                    bodyDepth1 = (float)(1000D * ((double)(netmsginput.readUnsignedShort() & 0x7fff) / 32767D));
                    bodyPitch1 = (float)(90D * ((double)netmsginput.readShort() / 32767D));
                    bodyRoll1 = (float)(90D * ((double)netmsginput.readShort() / 32767D));
                    tmInterpoStart = tmInterpoEnd = NetServerParams.getServerTime();
                    tmInterpoEnd  += (long)(1000D * (1200D * ((double)(netmsginput.readUnsignedShort() & 0x7fff) / 32767D)));
                    computeInterpolatedDPR(NetServerParams.getServerTime());
                    sink2Depth = (float)(1000D * ((double)(netmsginput.readUnsignedShort() & 0x7fff) / 32767D));
                    sink2Pitch = (float)(90D * ((double)netmsginput.readShort() / 32767D));
                    sink2Roll = (float)(90D * ((double)netmsginput.readShort() / 32767D));
                    sink2timeWhenStop = tmInterpoEnd;
                    sink2timeWhenStop += (long)(1000D * (1200D * ((double)(netmsginput.readUnsignedShort() & 0x7fff) / 32767D)));
                    if(flag)
                    {
                        long l5 = netmsginput.readLong();
                        if(l5 > 0L)
                        {
                            tmInterpoStart -= l5;
                            tmInterpoEnd -= l5;
                            sink2timeWhenStop -= l5;
                            computeInterpolatedDPR(NetServerParams.getServerTime());
                        }
                    }
                    Die(actor, timeOfDeath, true, false);
                    return true;
                }
                System.out.println("**net bigship unknown cmd " + j1);
                return false;
            }
            int i = netmsginput.readUnsignedByte();
            if((i & 0xe0) == 224)
            {
                int j = 1 + NetMsgInput.netObjReferenceLen() + 1;
                int l = 2 + NetMsgInput.netObjReferenceLen() + 1;
                int k1 = netmsginput.available();
                int k2 = i & 0x1f;
                int k3 = k1 - k2 * j;
                int i4 = k3 / l;
                if(i4 < 0 || i4 > NETSEND_ABSLIMIT_NUMITEMS_FIRE || k2 > NETSEND_ABSLIMIT_NUMITEMS_FIRE || k3 % l != 0)
                {
                    System.out.println("*** net big0 code:" + i + " szT:" + j + " szF:" + l + " len:" + k1 + " nT:" + k2 + " lenF:" + k3 + " nF:" + i4);
                    return true;
                }
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, k2 + i4);
                    out.setIncludeTime(true);
                    postReal(Message.currentRealTime(), out);
                }
                while(--k2 >= 0)
                {
                    int k4 = netmsginput.readUnsignedByte();
                    com.maddox.rts.NetObj netobj1 = netmsginput.readNetObj();
                    Actor actor1 = netobj1 != null ? ((ActorNet)netobj1).actor() : null;
                    int j6 = netmsginput.readUnsignedByte();
                    Track_Mirror(k4, actor1, j6);
                }
                while(--i4 >= 0)
                {
                    int l4 = netmsginput.readUnsignedByte();
                    int j5 = netmsginput.readUnsignedByte();
                    com.maddox.rts.NetObj netobj2 = netmsginput.readNetObj();
                    Actor actor2 = netobj2 != null ? ((ActorNet)netobj2).actor() : null;
                    double d3 = -2D + (((double)l4 / 255D) * 7000D) / 1000D;
                    double d4 = 0.001D * (double)(Message.currentGameTime() - NetServerParams.getServerTime()) + d3;
                    int k6 = netmsginput.readUnsignedByte();
                    Fire_Mirror(j5, actor2, k6, (float)d4);
                }
                return true;
            }
            if(i == 80)
            {
                int k = 2 + NetMsgInput.netObjReferenceLen();
                int i1 = netmsginput.available();
                int l1 = i1 / k;
                if(l1 <= 0 || l1 > NETSEND_ABSLIMIT_NUMITEMS_DMG || i1 % k != 0)
                {
                    System.out.println("*** net bigship2 n:" + l1);
                    return true;
                } else
                {
                    out.unLockAndSet(netmsginput, l1);
                    out.setIncludeTime(false);
                    postRealTo(Message.currentRealTime(), masterChannel(), out);
                    return true;
                }
            } else
            {
                System.out.println("**net bigship unknown ng cmd " + i);
                return true;
            }
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
            {
                int i = netmsginput.readUnsignedByte();
                if(i == REQUEST_LOC)
                {
                    NetUser netuser = (NetUser)netmsginput.readNetObj();
                    String s = netmsginput.readUTF();
                    handleLocationRequest(netuser, s);
                    return true;
                }
                if(i != 86)
                    return false;
                i = netmsginput.readUnsignedByte();
                float f = i;
                if(path != null && i != 127 && f < CURRSPEED)
                {
                    CURRSPEED = f;
                    if(Mission.isCoop())
                    {
                        computeNewPath();
                        netsendPartsState_needtosend = true;
                    }
                }
                return true;
            }
            if(netmsginput.readUnsignedByte() != 80)
                return false;
            if(dying != 0)
                return true;
            int j = 2 + NetMsgInput.netObjReferenceLen();
            int k = netmsginput.available();
            int l = k / j;
            if(l <= 0 || l > NETSEND_ABSLIMIT_NUMITEMS_DMG || k % j != 0)
            {
                System.out.println("*** net bigship1 len:" + k);
                return true;
            }
            do
            {
                if(--l < 0)
                    break;
                int i1 = netmsginput.readUnsignedByte();
                if(i1 < 0 || i1 >= parts.length)
                    return true;
                int j1 = netmsginput.readUnsignedByte();
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                if(parts[i1].state != 2)
                {
                    parts[i1].damage += (float)((j1 & 0x7f) + 1) / 128F;
                    parts[i1].damageIsFromRight = (j1 & 0x80) != 0;
                    InjurePart(i1, actor, true);
                }
            } while(true);
            return true;
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    class Move extends Interpolate
    {

        public boolean tick()
        {
            validateTowAircraft();
            if(dying == 0)
            {
                long l = Time.tickNext();
                if(Mission.isCoop() || Mission.isDogfight())
                    l = NetServerParams.getServerTime() + (long)Time.tickLen();
                if(path != null)
                {
                    computeInterpolatedDPR(l);
                    setMovablePosition(l);
                } else
                if(computeInterpolatedDPR(l))
                    setPosition();
                boolean flag = false;

                for(int j = 0; j < prop.nRadars; j++)
                {
                    if(parts[radars[j].part_idx].state == STPART_LIVE)
                    {
                        float f = parts[radars[j].part_idx].pro.HEAD_MAX_YAW_SPEED * Time.tickLenFs();
                        rotateRadar(radars[j], f);
                    }
                }
                radarTmr++;
                if(radarTmr > SecsToTicks(1.0F)) radarTmr = 0;

        if(bHasBlastDeflectorControl)
                {
        if( bLogDetailBD )
            System.out.println("BigShip: 1173 - bHasBlastDeflectorControl=true");
                    for(int iii=0; iii<4; iii++)
                    {
            blastDeflector[iii] = filter(Time.tickLenFs(), BlastDeflectorControl[iii], blastDeflector[iii], 999.9F, dvBlastDeflector);
                       if(Math.abs(blastDeflector_[iii] - blastDeflector[iii]) > 0.0005F)
                       {
                           moveBlastDeflector(iii, blastDeflector_[iii] = blastDeflector[iii]);
        if( bLogDetailBD )
            System.out.println("BigShip: 1180 - moveBlastDeflector(iii=" + Integer.toString(iii) + ", blastDeflector[iii]" + Float.toString(blastDeflector[iii]) + ")");
                       }
                    }
                }

                if(bHasMirrorLA)
                {
                    if(!bInitDoneMirrorLA)
                        InitMirrorLandingAid();

                    MlaUpdate();
                }

                if(bHasUSIflols)
                {
                    if(!bInitDoneUSIflols)
                        InitUSIflols();

                    USIflolsUpdate();
                }

                if(bHasFRFlols)
                {
                    if(!bInitDoneFRFlols)
                        InitFRFlols();

                    FRFlolsUpdate();
                }

                if(wakeupTmr == 0L)
                {
                    for(int j = 0; j < prop.nGuns; j++)
                        if(parts[arms[j].part_idx].state == STPART_LIVE)
                        {
                            arms[j].aime.tick_();
                            flag = true;
                        }
                } else
                {
                    int k = 0;
                    do
                    {
                        if(k >= prop.nGuns)
                            break;
                        if(parts[arms[k].part_idx].state == STPART_LIVE)
                        {
                            flag = true;
                            break;
                        }
                        k++;
                    } while(true);
                    if(wakeupTmr > 0L)
                        wakeupTmr--;
                    else
                    if(++wakeupTmr == 0L)
                        if(isAnyEnemyNear())
                            wakeupTmr = BigshipGeneric.SecsToTicks(BigshipGeneric.Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
                        else
                            wakeupTmr = -BigshipGeneric.SecsToTicks(BigshipGeneric.Rnd(4F, 7F));
                }
                if(flag)
                    send_bufferized_FireCommand();
                if(isNetMirror())
                {
                    mirror_send_bufferized_Damage();
                    if(Mission.isCoop() && mustSendSpeedToNet)
                    {
                        mirror_send_speed();
                        mustSendSpeedToNet = false;
                    }
                } else
                if(netsendPartsState_needtosend)
                    send_bufferized_PartsState();
                zutiRefreshBornPlace();
                return true;
            }
            if(dying == 3)
            {
                zutiRefreshBornPlace();
                if(path != null || !Mission.isDeathmatch())
                {
                    eraseGuns();
                    return false;
                }
                if(respawnDelay-- > 0L)
                    return true;
                if(!isNetMaster())
                {
                    respawnDelay = 10000L;
                    return true;
                } else
                {
                    wakeupTmr = 0L;
                    radarTmr = 0L;
                    makeLive();
                    forgetAllAiming();
                    appearSailorsMats();
                    setDefaultLivePose();
                    setDiedFlag(false);
                    tmInterpoStart = tmInterpoEnd = 0L;
                    bodyDepth = bodyPitch = bodyRoll = 0.0F;
                    bodyDepth0 = bodyPitch0 = bodyRoll0 = 0.0F;
                    bodyDepth1 = bodyPitch1 = bodyRoll1 = 0.0F;
                    setPosition();
                    pos.reset();
                    send_RespawnCommand();
                    return true;
                }
            }
            if(netsendPartsState_needtosend)
                send_bufferized_PartsState();
            long l1 = NetServerParams.getServerTime();
            if(dying == 1)
            {
                timeOfSailorsDisappear--;
                if( bSailorsDisappear == false && timeOfSailorsDisappear < 0 )
                    disappearSailorsMats();

                if(bHasMirrorLA)
                    MlaSetVisible(false);
                if(bHasUSIflols)
                    USIflolsSetVisible(false);
                if(bHasFRFlols)
                    FRFlolsSetVisible(false);

                if(l1 >= tmInterpoEnd)
                {
                    bodyDepth0 = bodyDepth1;
                    bodyPitch0 = bodyPitch1;
                    bodyRoll0 = bodyRoll1;
                    bodyDepth1 = sink2Depth;
                    bodyPitch1 = sink2Pitch;
                    bodyRoll1 = sink2Roll;
                    tmInterpoStart = tmInterpoEnd;
                    tmInterpoEnd = sink2timeWhenStop;
                    dying = 2;
                }
            } else
            if(l1 >= tmInterpoEnd)
            {
                bodyDepth0 = bodyDepth1 = sink2Depth;
                bodyPitch0 = bodyPitch1 = sink2Pitch;
                bodyRoll0 = bodyRoll1 = sink2Roll;
                tmInterpoStart = tmInterpoEnd = 0L;
                dying = 3;
            }
            if((Time.tickCounter() & 0x63) == 0 && dsmoks != null)
            {
                for(int i = 0; i < dsmoks.length; i++)
                    if(dsmoks[i] != null && dsmoks[i].pipe != null && dsmoks[i].pipe.pos.getAbsPoint().z < -4.891D)
                    {
                        Eff3DActor.finish(dsmoks[i].pipe);
                        dsmoks[i].pipe = null;
                    }

            }
            computeInterpolatedDPR(l1);
            if(path != null)
                setMovablePosition(timeOfDeath);
            else
                setPosition();
            zutiRefreshBornPlace();
            return true;
        }

        Move()
        {
        }
    }

    static class HookNamedZ0 extends HookNamed
    {

        public void computePos(Actor actor, Loc loc, Loc loc1)
        {
            super.computePos(actor, loc, loc1);
            loc1.getPoint().z = 0.25D;
        }

        public HookNamedZ0(ActorMesh actormesh, String s)
        {
            super(actormesh, s);
        }

        public HookNamedZ0(Mesh mesh, String s)
        {
            super(mesh, s);
        }
    }

    public static class Pipe
    {

        private Eff3DActor pipe;
        private int part_idx;





        public Pipe()
        {
            pipe = null;
            part_idx = -1;
        }
    }

    private static class Segment
    {

        public Point3d posIn;
        public Point3d posOut;
        public float length;
        public long timeIn;
        public long timeOut;
        public float speedIn;
        public float speedOut;
        public boolean slidersOn;

        private Segment()
        {
        }

    }

    public static class Part
    {

        private float damage;
        private Actor mirror_initiator;
        private Point3d shotpointOffs;
        private boolean damageIsFromRight;
        private int state;
        ShipPartProperties pro;


        public Part()
        {
            shotpointOffs = new Point3d();
            damageIsFromRight = false;
        }
    }

    public static class FiringDevice
    {

        private int gun_idx;
        private int part_idx;
        private Gun gun;
        private String missileName;
        private int typeOfMissile;
        int missileLauncherType;
        private ShipAim aime;
        private float headYaw;
        private float gunPitch;
        private Actor enemy;
        private double timeWhenFireS;
        private int shotpointIdx;


        public FiringDevice()
        {
        }
    }

    public static class RadarDevice
    {

        int radar_idx;
        private int part_idx;
        private float headYaw;


        public RadarDevice()
        {
        }
    }

    public static class TmpTrackOrFireInfo
    {

        private int gun_idx;
        private Actor enemy;
        private double timeWhenFireS;
        private int shotpointIdx;


        public TmpTrackOrFireInfo()
        {
        }
    }

    public static class ShipProperties
    {

        public String meshName;
        public String soundName;
        public int WEAPONS_MASK;
        public int HITBY_MASK;
        public float ATTACK_MAX_DISTANCE;
        public float SLIDER_DIST;
        public float SPEED;
        public float DELAY_RESPAWN_MIN;
        public float DELAY_RESPAWN_MAX;
        public float STABILIZE_FACTOR;
        public ShipPartProperties propparts[];
        public int nGuns;
        public int nRadars;
        public AirportProperties propAirport;
        public String typicalPlaneClass;

        public ShipProperties()
        {
            meshName = null;
            soundName = null;
            WEAPONS_MASK = 4;
            HITBY_MASK = -2;
            ATTACK_MAX_DISTANCE = 1.0F;
            SLIDER_DIST = 1.0F;
            SPEED = 1.0F;
            DELAY_RESPAWN_MIN = 15F;
            DELAY_RESPAWN_MAX = 30F;
            STABILIZE_FACTOR = 1.0F;
            propparts = null;
            propAirport = null;
            typicalPlaneClass = null;
        }
    }

    public static class ShipPartProperties
    {

        public boolean isItLifeKeeper()
        {
            return dmgDepth >= 0.0F;
        }

        public boolean haveGun()
        {
            return gun_idx >= 0;
        }

        public boolean haveRadar()
        {
            return radar_idx >= 0;
        }

        public String baseChunkName;
        public int baseChunkIdx;
        public Point3f partOffs;
        public float partR;
        public String additCollisChunkName[];
        public int additCollisChunkIdx[];
        public StrengthProperties stre;
        public float dmgDepth;
        public float dmgPitch;
        public float dmgRoll;
        public float dmgTime;
        public float BLACK_DAMAGE;
        public int gun_idx;
        public int radar_idx;
        public Class gunClass;
        public String missileName;
        public int WEAPONS_MASK;
        public boolean TRACKING_ONLY;
        public boolean NOW_RADAR;
        public int TypeOfTarget;
        public int LauncherType;
        public float ATTACK_MAX_DISTANCE;
        public float ATTACK_MIN_DISTANCE;
        public float ATTACK_MAX_RADIUS;
        public float ATTACK_MAX_HEIGHT;
        public float ATTACK_MIN_HEIGHT;
        public int ATTACK_FAST_TARGETS;
        public float FAST_TARGETS_ANGLE_ERROR;
        public boolean ATTACK_MISSILES;
        public boolean PREFER_SLOW_TARGET;
        public AnglesRange HEAD_YAW_RANGE;
        public AnglesRange NOFIRE_YAW_RANGE;
        public AnglesRange SELF_YAW_RANGE;
        public AnglesRange NOSELF_YAW_RANGE;
        public float HEAD_STD_YAW;
        public float _HEAD_MIN_YAW;
        public float _HEAD_MAX_YAW;
        public float _NOFIRE_MIN_YAW;
        public float _NOFIRE_MAX_YAW;
        public boolean NOFIRE_FLAG;
        public boolean NOHEADING_FLAG;
        public float _SELF_YAW;
        public float GUN_MIN_PITCH;
        public float GUN_STD_PITCH;
        public float GUN_MAX_PITCH;
        public float HEAD_MAX_YAW_SPEED;
        public float GUN_MAX_PITCH_SPEED;
        public float DELAY_AFTER_SHOOT;
        public float DELAY_BEFORE_FIRST_SHOOT;
        public float CHAINFIRE_TIME;
        public String headChunkName;
        public String gunChunkName;
        public int headChunkIdx;
        public int gunChunkIdx;
        public Point3d fireOffset;
        public Orient fireOrient;
        public String gunShellStartHookName;

        public ShipPartProperties()
        {
            baseChunkName = null;
            baseChunkIdx = -1;
            partOffs = null;
            partR = 1.0F;
            additCollisChunkName = null;
            additCollisChunkIdx = null;
            stre = new StrengthProperties();
            dmgDepth = -1F;
            dmgPitch = 0.0F;
            dmgRoll = 0.0F;
            dmgTime = 1.0F;
            BLACK_DAMAGE = 0.0F;
            gunClass = null;
            missileName = null;
            WEAPONS_MASK = 4;
            TRACKING_ONLY = false;
            NOW_RADAR = false;
            TypeOfTarget = 0;
            LauncherType = 0;
            ATTACK_MAX_DISTANCE = 1.0F;
            ATTACK_MIN_DISTANCE = 1.0F;
            ATTACK_MAX_RADIUS = 1.0F;
            ATTACK_MAX_HEIGHT = 1.0F;
            ATTACK_MIN_HEIGHT = 0.0F;
            ATTACK_FAST_TARGETS = 1;
            FAST_TARGETS_ANGLE_ERROR = 0.0F;
            ATTACK_MISSILES = false;
            PREFER_SLOW_TARGET = false;
            HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            NOFIRE_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            SELF_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            NOSELF_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            HEAD_STD_YAW = 0.0F;
            _HEAD_MIN_YAW = -1F;
            _HEAD_MAX_YAW = -1F;
            _NOFIRE_MIN_YAW = -1F;
            _NOFIRE_MAX_YAW = -1F;
            NOFIRE_FLAG = false;
            NOHEADING_FLAG = false;
            GUN_MIN_PITCH = -20F;
            GUN_STD_PITCH = -18F;
            GUN_MAX_PITCH = -15F;
            HEAD_MAX_YAW_SPEED = 720F;
            GUN_MAX_PITCH_SPEED = 60F;
            DELAY_AFTER_SHOOT = 1.0F;
            DELAY_BEFORE_FIRST_SHOOT = 0F;
            CHAINFIRE_TIME = 0.0F;
            headChunkName = null;
            gunChunkName = null;
            headChunkIdx = -1;
            gunChunkIdx = -1;
            gunShellStartHookName = null;
        }
    }


    public ShipProperties getShipProp()
    {
        return prop;
    }

    public static final double Rnd(double d, double d1)
    {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1)
    {
        return World.Rnd().nextFloat(f, f1);
    }

    public static final int Rnd(int i, int j)
    {
        return World.Rnd().nextInt(i, j);
    }

//    private boolean RndB(float f)
//    {
//        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
//    }

    public static final float KmHourToMSec(float f)
    {
        return f / 3.6F;
    }

    private static final long SecsToTicks(float f)
    {
        long l = (long)(0.5D + (double)(f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    protected final boolean Head360(FiringDevice firingdevice)
    {
        return parts[firingdevice.part_idx].pro.HEAD_YAW_RANGE.fullcircle();
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        if(actor instanceof BridgeSegment)
        {
            if(dying != 0)
                aflag[0] = false;
            return;
        }
        if(path == null && (actor instanceof ActorMesh) && ((ActorMesh)actor).isStaticPos())
        {
            aflag[0] = false;
            return;
        } else
        {
            return;
        }
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(dying != 0)
            return;
        if(isNetMirror())
            return;
        if(actor instanceof WeakBody)
            return;
        if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof BridgeSegment))
            Die(null, -1L, true, true);
    }

    private int findNotDeadPartByShotChunk(String s)
    {
        if(s == null || s.equals(""))
            return -2;
        int i = hierMesh().chunkFindCheck(s);
        if(i < 0)
            return -2;
label0:
        for(int j = 0; j < parts.length; j++)
        {
            if(parts[j].state == STPART_DEAD)
                continue;
            if(i == parts[j].pro.baseChunkIdx)
                return j;
            int k = 0;
            do
            {
                if(k >= parts[j].pro.additCollisChunkIdx.length)
                    continue label0;
                if(i == parts[j].pro.additCollisChunkIdx[k])
                    return j;
                k++;
            } while(true);
        }

        return -1;
    }

    private void computeNewPath()
    {
        if(path == null || dying != 0 || Mission.isDogfight())
            return;
        Segment segment = (Segment)path.get(cachedSeg);
        long l = 0L;
        long l1 = Time.tickNext();
        if(Mission.isCoop() || Mission.isDogfight())
            l1 = NetServerParams.getServerTime();
        if((segment.timeIn > l1 || !isTurning) && (segment.speedIn > CURRSPEED || segment.speedOut > CURRSPEED))
        {
            if(Mission.isCoop())
                mustSendSpeedToNet = true;
            float f = 0.0F;
            if(l1 >= segment.timeIn)
            {
                long l2 = segment.timeOut - segment.timeIn;
                long l4 = l1 - segment.timeIn;
                float f1 = segment.speedOut - segment.speedIn;
                f = segment.speedIn + f1 * (float)((double)l4 / (double)l2);
            }
            if(f > CURRSPEED)
                segment.speedIn = CURRSPEED;
            else
                segment.speedIn = f;
            if(segment.speedOut > CURRSPEED)
                segment.speedOut = CURRSPEED;
            Point3d point3d = new Point3d();
            point3d.x = initLoc.getX();
            point3d.y = initLoc.getY();
            point3d.z = initLoc.getZ();
            segment.posIn.set(point3d);
            if(segment.timeIn < l1)
                segment.timeIn = l1;
            double d = segment.posIn.distance(segment.posOut);
            l = segment.timeOut;
            segment.timeOut = segment.timeIn + (long)(1000D * ((2D * d) / (double)Math.abs(segment.speedOut + segment.speedIn)));
            segment.length = (float)d;
            segment.slidersOn = false;
        } else
        {
            l = segment.timeOut;
        }
        if(isTurningBackward && (segment.speedIn > CURRSPEED || segment.speedOut > CURRSPEED))
            mustRecomputePath = true;
        int i = cachedSeg;
        for(i++; i <= path.size() - 1; i++)
        {
            Segment segment1 = (Segment)path.get(i);
            long l3 = segment1.timeIn - l;
            segment1.timeIn = segment.timeOut + l3;
            segment1.posIn = segment.posOut;
            if(segment1.speedIn > CURRSPEED)
            {
                if(Mission.isCoop())
                    mustSendSpeedToNet = true;
                segment1.speedIn = CURRSPEED;
            }
            if(segment1.speedOut > CURRSPEED)
            {
                if(Mission.isCoop())
                    mustSendSpeedToNet = true;
                segment1.speedOut = CURRSPEED;
            }
            l = segment1.timeOut;
            segment1.timeOut = segment1.timeIn + (long)(1000D * ((2D * (double)segment1.length) / (double)Math.abs(segment1.speedOut + segment1.speedIn)));
            segment = segment1;
        }

    }

    public void msgShot(Shot shot)
    {
        shot.bodyMaterial = 2;
        if(dying != 0)
            return;
        if(shot.power <= 0.0F)
            return;
        if(isNetMirror() && shot.isMirage())
            return;
        if(wakeupTmr < 0L)
            wakeupTmr = SecsToTicks(Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
        int i = findNotDeadPartByShotChunk(shot.chunkName);
        if(i < 0)
            return;
        float f;
        float f1;
        if(shot.powerType == 1)
        {
            f = parts[i].pro.stre.EXPLHIT_MIN_TNT;
            f1 = parts[i].pro.stre.EXPLHIT_MAX_TNT;
        } else
        {
            f = parts[i].pro.stre.SHOT_MIN_ENERGY;
            f1 = parts[i].pro.stre.SHOT_MAX_ENERGY;
        }
        float f2 = shot.power * Rnd(1.0F, 1.1F);
        if(f2 < f)
            return;
        tmpvd.set(shot.v);
        pos.getAbs().transformInv(tmpvd);
        parts[i].damageIsFromRight = tmpvd.y > 0.0D;
        float f3 = f2 / f1;
        parts[i].damage += f3;
        if(isNetMirror() && shot.initiator != null)
            parts[i].mirror_initiator = shot.initiator;
        InjurePart(i, shot.initiator, true);
        if(!Mission.isDogfight() && path != null && parts[i].pro.isItLifeKeeper() && parts[i].damage > 0.2F)
        {
            computeSpeedReduction(parts[i].damage);
            computeNewPath();
        }
    }

    private void computeSpeedReduction(float f)
    {
        int i = (int)(f * 128F);
        if(--i < 0)
            i = 0;
        else
        if(i > 127)
            i = 127;
        f = (float)i / 128F;
        float f1 = 0.4F * prop.SPEED + (1.0F - f) * 2.0F * prop.SPEED;
        int j = Math.round(f1);
        f1 = j;
        if(f1 < CURRSPEED)
            CURRSPEED = f1;
    }

    public void msgExplosion(Explosion explosion)
    {
        if(dying != 0)
            return;
        if(isNetMirror() && explosion.isMirage())
            return;
        if(wakeupTmr < 0L)
            wakeupTmr = SecsToTicks(Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
        float f = explosion.power;
        if(explosion.powerType == 2 && explosion.chunkName != null)
            f *= 0.45F;
        int i = -1;
        if(explosion.chunkName != null)
        {
            int j = findNotDeadPartByShotChunk(explosion.chunkName);
            if(j >= 0)
            {
                float f1 = f;
                f1 *= Rnd(0.5F, 1.6F) * Mission.BigShipHpDiv();
                if(f1 >= parts[j].pro.stre.EXPLHIT_MIN_TNT)
                {
                    i = j;
                    p1.set(explosion.p);
                    pos.getAbs().transformInv(p1);
                    parts[j].damageIsFromRight = p1.y < 0.0D;
                    float f2 = (f1 - parts[j].pro.stre.EXPLHIT_MIN_TNT) / parts[j].pro.stre.EXPLHIT_MAX_TNT;
                    parts[j].damage += f2;
                    if(isNetMirror() && explosion.initiator != null)
                        parts[j].mirror_initiator = explosion.initiator;
                    InjurePart(j, explosion.initiator, true);
                    if(!Mission.isDogfight() && path != null && parts[j].pro.isItLifeKeeper() && parts[j].damage > 0.2F)
                    {
                        computeSpeedReduction(parts[j].damage);
                        computeNewPath();
                    }
                }
            }
        }
        Loc loc = pos.getAbs();
        p1.set(explosion.p);
        pos.getAbs().transformInv(p1);
        boolean flag = p1.y < 0.0D;
        for(int k = 0; k < parts.length; k++)
        {
            if(k == i || parts[k].state == STPART_DEAD)
                continue;
            p1.set(parts[k].pro.partOffs);
            loc.transform(p1);
            float f3 = parts[k].pro.partR;
//            float f4 = (float)(p1.distance(explosion.p)) - f3;
            float f5 = 0.0F;
            if(explosion.p.z < 0.0D && parts[k].pro.baseChunkName.startsWith("Hull") && explosion.powerType == 0 && explosion.power > 2.0F)
                f5 = explosion.receivedTNT_1meterWater(p1, f3, (float)explosion.p.z);
            else
                f5 = explosion.receivedTNT_1meter(p1, f3);
            f5 *= Rnd(1.0F, 1.1F) * Mission.BigShipHpDiv();
            if(f5 < parts[k].pro.stre.EXPLNEAR_MIN_TNT)
                continue;
            parts[k].damageIsFromRight = flag;
            float f6 = f5 / parts[k].pro.stre.EXPLNEAR_MAX_TNT;
            parts[k].damage += f6;
            if(isNetMirror() && explosion.initiator != null)
                parts[k].mirror_initiator = explosion.initiator;
            InjurePart(k, explosion.initiator, true);
            if(!Mission.isDogfight() && path != null && parts[k].pro.isItLifeKeeper() && parts[k].damage > 0.2F)
            {
                computeSpeedReduction(parts[k].damage);
                computeNewPath();
            }
        }

    }

    private void recomputeShotpoints()
    {
        if(shotpoints == null || shotpoints.length < 1 + parts.length)
            shotpoints = new int[1 + parts.length];
        numshotpoints = 0;
        if(dying != 0)
            return;
        numshotpoints = 1;
        shotpoints[0] = 0;
        for(int i = 0; i < parts.length; i++)
        {
            if(parts[i].state == STPART_DEAD)
                continue;
            int j;
            if(parts[i].pro.isItLifeKeeper())
            {
                j = parts[i].pro.baseChunkIdx;
            } else
            {
                if(!parts[i].pro.haveGun())
                    continue;
                j = parts[i].pro.gunChunkIdx;
            }
            shotpoints[numshotpoints] = i + 1;
            hierMesh().setCurChunk(j);
            hierMesh().getChunkLocObj(tmpL);
            parts[i].shotpointOffs.set(tmpL.getPoint());
            numshotpoints++;
        }

    }

    private boolean visualsInjurePart(int i, boolean flag)
    {
        if(!flag)
        {
            if(parts[i].state == STPART_DEAD)
            {
                parts[i].damage = 1.0F;
                return false;
            }
            if(parts[i].damage < parts[i].pro.BLACK_DAMAGE)
                return false;
            netsendDrown_nparts = 0;
            netsendDrown_depth = 0.0F;
            netsendDrown_pitch = 0.0F;
            netsendDrown_roll = 0.0F;
            netsendDrown_timeS = 0.0F;
            if(parts[i].damage < 1.0F)
            {
                if(parts[i].state == STPART_BLACK)
                    return false;
                parts[i].state = 1;
            } else
            {
                parts[i].damage = 1.0F;
                parts[i].state = 2;
            }
            if(parts[i].pro.isItLifeKeeper())
            {
                netsendDrown_nparts++;
                netsendDrown_depth += Rnd(0.8F, 1.0F) * parts[i].pro.dmgDepth;
                netsendDrown_pitch += Rnd(0.8F, 1.0F) * parts[i].pro.dmgPitch;
                netsendDrown_roll = (float)((double)netsendDrown_roll + (double)(Rnd(0.8F, 1.0F) * parts[i].pro.dmgRoll) * (parts[i].damageIsFromRight ? -1D : 1.0D));
                netsendDrown_timeS += Rnd(0.7F, 1.3F) * parts[i].pro.dmgTime;
            }
        }
        if(parts[i].pro.haveGun())
        {
            arms[parts[i].pro.gun_idx].aime.forgetAiming();
            arms[parts[i].pro.gun_idx].enemy = null;
            if(parts[i].pro.TRACKING_ONLY)
                hasTracking--;
        }
        int ai[] = hierMesh().getSubTreesSpec(parts[i].pro.baseChunkName);
        for(int j = 0; j < ai.length; j++)
        {
            hierMesh().setCurChunk(ai[j]);
            if(!hierMesh().isChunkVisible())
                continue;
            for(int l = 0; l < parts.length; l++)
            {
                if(l == i || parts[l].state == STPART_DEAD || ai[j] != parts[l].pro.baseChunkIdx)
                    continue;
                if(!flag && parts[l].state == STPART_LIVE && parts[l].pro.isItLifeKeeper())
                {
                    netsendDrown_nparts++;
                    netsendDrown_depth += Rnd(0.8F, 1.0F) * parts[l].pro.dmgDepth;
                    netsendDrown_pitch += Rnd(0.8F, 1.0F) * parts[l].pro.dmgPitch;
                    netsendDrown_roll = (float)((double)netsendDrown_roll + (double)(Rnd(0.8F, 1.0F) * parts[l].pro.dmgRoll) * (parts[l].damageIsFromRight ? -1D : 1.0D));
                    netsendDrown_timeS += Rnd(0.7F, 1.3F) * parts[l].pro.dmgTime;
                }
                parts[l].damage = flag ? 0.0F : 1.0F;
                parts[l].mirror_initiator = null;
                parts[l].state = 2;
                if(parts[l].pro.haveGun())
                {
                    arms[parts[l].pro.gun_idx].aime.forgetAiming();
                    arms[parts[l].pro.gun_idx].enemy = null;
                }
            }

            if(hierMesh().chunkName().endsWith("_x") || hierMesh().chunkName().endsWith("_X"))
            {
                hierMesh().chunkVisible(false);
                continue;
            }
            String s = hierMesh().chunkName() + "_dmg";
            int l1 = hierMesh().chunkFindCheck(s);
            if(l1 >= 0)
            {
                hierMesh().chunkVisible(false);
                hierMesh().chunkVisible(s, true);
            }
        }

        if(pipes != null)
        {
            boolean flag1 = false;
            for(int i1 = 0; i1 < pipes.length; i1++)
            {
                if(pipes[i1] == null)
                    continue;
                if(pipes[i1].pipe == null)
                {
                    pipes[i1] = null;
                    continue;
                }
                int i2 = pipes[i1].part_idx;
                if(parts[i2].state == STPART_LIVE)
                {
                    flag1 = true;
                } else
                {
                    pipes[i1].pipe._finish();
                    pipes[i1].pipe = null;
                    pipes[i1] = null;
                }
            }

            if(!flag1)
            {
                for(int j1 = 0; j1 < pipes.length; j1++)
                    if(pipes[j1] != null)
                        pipes[j1] = null;

                pipes = null;
            }
        }
        if(dsmoks != null)
        {
            for(int k = 0; k < dsmoks.length; k++)
            {
                if(dsmoks[k] == null || dsmoks[k].pipe != null)
                    continue;
                int k1 = dsmoks[k].part_idx;
                if(parts[k1].state == STPART_LIVE)
                    continue;
                String s1 = parts[k1].pro.baseChunkName;
                Loc loc = new Loc();
                hierMesh().setCurChunk(s1);
                hierMesh().getChunkLocObj(loc);
                float f = parts[k1].pro.stre.EXPLNEAR_MIN_TNT;
                String s2 = "Effects/Smokes/Smoke";
//                boolean flag2 = true;
                if(parts[k1].pro.haveGun())
                {
                    s2 = s2 + "Gun";
                    int j2;
                    if(f < 6F)
                    {
                        s2 = s2 + "Tiny";
                        j2 = 6;
                    } else
                    if(f < 10F)
                    {
                        s2 = s2 + "Small";
                        j2 = 8;
                    } else
                    if(f < 14F)
                    {
                        s2 = s2 + "Medium";
                        j2 = 12;
                    } else
                    if(f < 18F)
                    {
                        s2 = s2 + "Large";
                        j2 = 16;
                    } else
                    {
                        s2 = s2 + "Huge";
                        j2 = 24;
                    }
                    dsmoks[k].pipe = Eff3DActor.New(this, null, loc, 1.0F, s2 + ".eff", j2);
                    Eff3DActor.New(this, null, loc, 1.0F, s2 + "Fire.eff", (float)j2 * 0.5F);
                    continue;
                }
                s2 = s2 + "Ship";
                if(f < 6F)
                    s2 = s2 + "Tiny";
                else
                if(f < 10F)
                    s2 = s2 + "Small";
                else
                if(f < 22F)
                    s2 = s2 + "Medium";
                else
                if(f < 30F)
                    s2 = s2 + "Large";
                else
                if(f < 50F)
                    s2 = s2 + "Huge";
                else
                if(f < 100F)
                    s2 = s2 + "Enormous";
                else
                    s2 = s2 + "Invulnerable";
                if(this instanceof TankerType)
                {
                    dsmoks[k].pipe = Eff3DActor.New(this, null, loc, 1.1F, "Effects/Smokes/SmokeShipTanker.eff", -1F);
                    Eff3DActor.New(this, null, loc, 1.0F, "Effects/Smokes/SmokeShipTankerFire.eff", parts[k1].pro.dmgTime);
                } else
                {
                    dsmoks[k].pipe = Eff3DActor.New(this, null, loc, 1.1F, s2 + ".eff", -1F);
                    Eff3DActor.New(this, null, loc, 1.0F, s2 + "Fire.eff", parts[k1].pro.dmgTime);
                }
            }

        }
        recomputeShotpoints();
        return true;
    }

    void master_sendDrown(float f, float f1, float f2, float f3)
    {
        if(!net.isMirrored())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(100);
            float f4 = f / 1000F;
            if(f4 <= 0.0F)
                f4 = 0.0F;
            if(f4 >= 1.0F)
                f4 = 1.0F;
            int i = (int)(f4 * 32767F);
            if(i > 32767)
                i = 32767;
            if(i < 0)
                i = 0;
            netmsgguaranted.writeShort(i);
            f4 = f1 / 90F;
            if(f4 <= -1F)
                f4 = -1F;
            if(f4 >= 1.0F)
                f4 = 1.0F;
            i = (int)(f4 * 32767F);
            if(i > 32767)
                i = 32767;
            if(i < -32767)
                i = -32767;
            netmsgguaranted.writeShort(i);
            f4 = f2 / 90F;
            if(f4 <= -1F)
                f4 = -1F;
            if(f4 >= 1.0F)
                f4 = 1.0F;
            i = (int)(f4 * 32767F);
            if(i > 32767)
                i = 32767;
            if(i < -32767)
                i = -32767;
            netmsgguaranted.writeShort(i);
            f4 = f3 / 1200F;
            if(f4 <= 0.0F)
                f4 = 0.0F;
            if(f4 >= 1.0F)
                f4 = 1.0F;
            i = (int)(f4 * 32767F);
            if(i > 32767)
                i = 32767;
            if(i < 0)
                i = 0;
            netmsgguaranted.writeShort(i);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void InjurePart(int i, Actor actor, boolean flag)
    {
        if(isNetMirror())
            return;
        netsendPartsState_needtosend = true;
        if(!visualsInjurePart(i, false))
            return;
        if(dying != 0)
            return;
        boolean flag1 = false;
        int j = 0;
        do
        {
            if(j >= parts.length)
                break;
            if(parts[j].pro.isItLifeKeeper() && parts[j].state == STPART_DEAD)
            {
                flag1 = true;
                break;
            }
            j++;
        } while(true);
        if(netsendDrown_nparts > 0)
        {
            netsendDrown_depth += bodyDepth1;
            netsendDrown_pitch += bodyPitch1;
            netsendDrown_roll += bodyRoll1;
            netsendDrown_timeS /= netsendDrown_nparts;
            if(netsendDrown_timeS >= 1200F)
                netsendDrown_timeS = 1200F;
            tmInterpoStart = NetServerParams.getServerTime();
            tmInterpoEnd = tmInterpoStart + (long)(netsendDrown_timeS * 1000F);
            bodyDepth0 = bodyDepth;
            bodyPitch0 = bodyPitch;
            bodyRoll0 = bodyRoll;
            bodyDepth1 = netsendDrown_depth;
            bodyPitch1 = netsendDrown_pitch;
            bodyRoll1 = netsendDrown_roll;
            master_sendDrown(netsendDrown_depth, netsendDrown_pitch, netsendDrown_roll, netsendDrown_timeS);
        }
        if(!flag1)
        {
            return;
        } else
        {
            Die(actor, -1L, flag, true);
            return;
        }
    }

    private float computeSeaDepth(Point3d point3d)
    {
        for(float f = 5F; f <= 355F; f += 10F)
        {
            for(float f1 = 0.0F; f1 < 360F; f1 += 30F)
            {
                float f2 = f * Geom.cosDeg(f1);
                float f3 = f * Geom.sinDeg(f1);
                f2 += (float)point3d.x;
                f3 += (float)point3d.y;
                if(!World.land().isWater(f2, f3))
                    return 150F * (f / 355F);
            }

        }

        return 1000F;
    }

    private void computeSinkingParams(long l)
    {
        if(path != null)
            setMovablePosition(l);
        else
            setPosition();
        pos.reset();
        float f = computeSeaDepth(pos.getAbsPoint()) * Rnd(1.0F, 1.25F);
        if(f >= 400F)
            f = 400F;
        float f1 = Rnd(0.2F, 0.25F);
        float f2;
        float f3;
        float f4;
        float f5;
        if(f >= 200F)
        {
            f2 = Rnd(90F, 110F);
            f3 = f2 * f1;
            f4 = 50F - Rnd(0.0F, 20F);
            f5 = Rnd(15F, 32F);
            f1 *= 1.6F;
        } else
        {
            f2 = Rnd(30F, 40F);
            f3 = f2 * f1;
            f4 = 4.5F - Rnd(0.0F, 2.5F);
            f5 = Rnd(6F, 13F);
        }
        float f6 = (f - f3) / f1;
        if(f6 < 1.0F)
            f6 = 1.0F;
        float f7 = f6 * f1;
        computeInterpolatedDPR(l);
        bodyDepth0 = bodyDepth;
        bodyPitch0 = bodyPitch;
        bodyRoll0 = bodyRoll;
        bodyDepth1 += f3;
        bodyPitch1 += ((double)bodyPitch1 <= 0.0D ? -1F : 1.0F) * f4;
        bodyRoll1 += ((double)bodyRoll1 <= 0.0D ? -1F : 1.0F) * f5;
        if(bodyPitch1 > 80F)
            bodyPitch1 = 80F;
        if(bodyPitch1 < -80F)
            bodyPitch1 = -80F;
        if(bodyRoll1 > 80F)
            bodyRoll1 = 80F;
        if(bodyRoll1 < -80F)
            bodyRoll1 = -80F;
        float sinkTimeMultipiler = Config.cur.ini.get("Mods", "SinkTimeMultipiler", 1.0F);
        if(sinkTimeMultipiler < 0.3F)  sinkTimeMultipiler = 0.3F;
        if(sinkTimeMultipiler > 10.0F)  sinkTimeMultipiler = 10.0F;
        sinkTimeMultipiler *= Rnd(0.8F, 1.4F);
        tmInterpoStart = l;
        tmInterpoEnd = tmInterpoStart + (long)((f2 * 1000F) * 10L * sinkTimeMultipiler);
        sink2Depth = bodyDepth1 + f7;
        sink2Pitch = bodyPitch1;
        sink2Roll = bodyRoll1;
        sink2timeWhenStop = tmInterpoEnd + (long)(f6 * 1000F);
    }

    private void showExplode()
    {
        Explosions.Antiaircraft_Explode(pos.getAbsPoint());
    }

    private void Die(Actor actor, long l, boolean flag, boolean flag1)
    {
        if(dying != 0)
            return;
        if(l < 0L)
        {
            if(isNetMirror())
                return;
            l = NetServerParams.getServerTime();
        }
        dying = 1;
        World.onActorDied(this, actor);
        recomputeShotpoints();
        forgetAllAiming();
        SetEffectsIntens(-1F);
        if(flag1)
            computeSinkingParams(l);
        computeInterpolatedDPR(l);
        if(path != null)
            setMovablePosition(l);
        else
            setPosition();
        pos.reset();
        timeOfDeath = l;
        if(flag)
            showExplode();
        if(flag && isNetMaster())
            send_DeathCommand(actor, null);
        if(airport != null && zutiBornPlace != null)
            zutiBornPlace.army = -2;
        timeOfSailorsDisappear = SecsToTicks( 60.0f );
    }

    public void destroy()
    {
        if(isDestroyed())
            return;
        eraseGuns();
        if(parts != null)
        {
            for(int i = 0; i < parts.length; i++)
            {
                parts[i].mirror_initiator = null;
                parts[i] = null;
            }

            parts = null;
        }
        super.destroy();
    }

    private boolean isAnyEnemyNear()
    {
        NearestEnemies.set(WeaponsMask());
        Actor actor = NearestEnemies.getAFoundEnemy(pos.getAbsPoint(), 2000D, getArmy());
        return actor != null;
    }

    private final FiringDevice GetFiringDevice(ShipAim aim)
    {
        for(int i = 0; i < prop.nGuns; i++)
            if(arms[i] != null && arms[i].aime == aim)
                return arms[i];

        System.out.println("Internal error 1: Can't find ship gun.");
        return null;
    }

    private final ShipPartProperties GetGunProperties(ShipAim aim)
    {
        for(int i = 0; i < prop.nGuns; i++)
            if(arms[i].aime == aim)
                return parts[arms[i].part_idx].pro;

        System.out.println("Internal error 2: Can't find ship gun.");
        return null;
    }

    private void setGunAngles(FiringDevice firingdevice, float f, float f1)
    {
        firingdevice.headYaw = f;
        firingdevice.gunPitch = f1;
        ShipPartProperties shippartproperties = parts[firingdevice.part_idx].pro;
        tmpYPR[1] = 0.0F;
        tmpYPR[2] = 0.0F;
        hierMesh().setCurChunk(shippartproperties.headChunkIdx);
        tmpYPR[0] = firingdevice.headYaw;
        hierMesh().chunkSetAngles(tmpYPR);
        hierMesh().setCurChunk(shippartproperties.gunChunkIdx);
        tmpYPR[0] = -(firingdevice.gunPitch - shippartproperties.GUN_STD_PITCH);
        hierMesh().chunkSetAngles(tmpYPR);
    }

    private void setRadarAngles(RadarDevice radardevice, float f)
    {
        radardevice.headYaw = f;
        ShipPartProperties shippartproperties = parts[radardevice.part_idx].pro;
        tmpYPR[1] = 0.0F;
        tmpYPR[2] = 0.0F;
        hierMesh().setCurChunk(shippartproperties.headChunkIdx);
        tmpYPR[0] = radardevice.headYaw;
        hierMesh().chunkSetAngles(tmpYPR);
    }

    private void rotateRadar(RadarDevice radardevice, float f)
    {
        radardevice.headYaw += f;
        if( radardevice.headYaw < -180  ) radardevice.headYaw += 360;
        if( radardevice.headYaw > 180  ) radardevice.headYaw -= 360;
        setRadarAngles( radardevice, radardevice.headYaw );
    }

    private void eraseGuns()
    {
        if(arms != null)
        {
            for(int i = 0; i < prop.nGuns; i++)
            {
                if(arms[i] == null)
                    continue;
                if(arms[i].aime != null)
                {
                    arms[i].aime.forgetAll();
                    arms[i].aime = null;
                }
                if(arms[i].gun != null)
                {
                    destroy(arms[i].gun);
                    arms[i].gun = null;
                }
                arms[i].enemy = null;
                arms[i] = null;
            }

            arms = null;
        }
    }

//    private void eraseRadars()
//    {
//        if(radars != null)
//        {
//            for(int i = 0; i < prop.nRadars; i++)
//            {
//                if(radars[i] == null)
//                    continue;
//                radars[i] = null;
//            }
//
//            radars = null;
//        }
//    }

    private void forgetAllAiming()
    {
        if(arms != null)
        {
            for(int i = 0; i < prop.nGuns; i++)
                if(arms[i] != null && arms[i].aime != null)
                {
                    arms[i].aime.forgetAiming();
                    arms[i].enemy = null;
                }

        }
    }

    private void CreateGuns()
    {
        arms = new FiringDevice[prop.nGuns];
        for(int i = 0; i < parts.length; i++)
        {
            if(!parts[i].pro.haveGun())
                continue;
            ShipPartProperties shippartproperties = parts[i].pro;
            int j = shippartproperties.gun_idx;
            arms[j] = new FiringDevice();
            arms[j].gun_idx = j;
            arms[j].part_idx = i;
            arms[j].typeOfMissile = shippartproperties.TypeOfTarget;
            arms[j].missileLauncherType = shippartproperties.LauncherType;
            arms[j].gun = null;
            arms[j].missileName = null;
            if(arms[j].typeOfMissile == 0)
            {
                try
                {
                    arms[j].gun = (Gun)shippartproperties.gunClass.newInstance();
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    System.out.println("BigShip: Can't create gun '" + shippartproperties.gunClass.getName() + "'");
                }
                arms[j].gun.set(this, shippartproperties.gunShellStartHookName);
                arms[j].gun.loadBullets(-1);
            }
            else
            {
                try
                {
//                    Class ctemp = Class.forName(shippartproperties.missileName);
//                    Rocket temp = (Rocket)ctemp.newInstance();
                    Class.forName(shippartproperties.missileName).newInstance();
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    System.out.println("BigShip: Can't create missile '" + shippartproperties.missileName + "'");
                }
                arms[j].missileName = shippartproperties.missileName;
            }
            if(shippartproperties.TRACKING_ONLY)
            {
                arms[j].aime = new ShipAim(this, isNetMirror(), SLOWFIRE_K * ((shippartproperties.DELAY_BEFORE_FIRST_SHOOT > 0F)? shippartproperties.DELAY_BEFORE_FIRST_SHOOT : 1F ), true);
                hasTracking++;
            } else
            {
                arms[j].aime = new ShipAim(this, isNetMirror(), SLOWFIRE_K * ((shippartproperties.DELAY_BEFORE_FIRST_SHOOT > 0F)? shippartproperties.DELAY_BEFORE_FIRST_SHOOT : 1F ));
            }
            arms[j].enemy = null;
        }
    }

    private void CreateRadars()
    {
        radars = new RadarDevice[prop.nRadars];
        for(int i = 0; i < parts.length; i++)
        {
            if(!parts[i].pro.haveRadar())
                continue;
            ShipPartProperties shippartproperties = parts[i].pro;
            int j = shippartproperties.radar_idx;
            radars[j] = new RadarDevice();
            radars[j].radar_idx = j;
            radars[j].part_idx = i;
        }
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    private void initMeshBasedProperties()
    {
        for(int i = 0; i < prop.propparts.length; i++)
        {
            ShipPartProperties shippartproperties = prop.propparts[i];
            if(shippartproperties.baseChunkIdx >= 0)
                continue;
            shippartproperties.baseChunkIdx = hierMesh().chunkFind(shippartproperties.baseChunkName);
            hierMesh().setCurChunk(shippartproperties.baseChunkIdx);
            hierMesh().getChunkLocObj(tmpL);
            tmpL.get(p1);
            shippartproperties.partOffs = new Point3f();
            shippartproperties.partOffs.set(p1);
            shippartproperties.partR = hierMesh().getChunkVisibilityR();
            int j = shippartproperties.additCollisChunkName.length;
            for(int k = 0; k < shippartproperties.additCollisChunkName.length; k++)
                if(hierMesh().chunkFindCheck(shippartproperties.additCollisChunkName[k] + "_dmg") >= 0)
                    j++;

            if(hierMesh().chunkFindCheck(shippartproperties.baseChunkName + "_dmg") >= 0)
                j++;
            shippartproperties.additCollisChunkIdx = new int[j];
            j = 0;
            for(int l = 0; l < shippartproperties.additCollisChunkName.length; l++)
            {
                shippartproperties.additCollisChunkIdx[j++] = hierMesh().chunkFind(shippartproperties.additCollisChunkName[l]);
                int j1 = hierMesh().chunkFindCheck(shippartproperties.additCollisChunkName[l] + "_dmg");
                if(j1 >= 0)
                    shippartproperties.additCollisChunkIdx[j++] = j1;
            }

            int i1 = hierMesh().chunkFindCheck(shippartproperties.baseChunkName + "_dmg");
            if(i1 >= 0)
                shippartproperties.additCollisChunkIdx[j++] = i1;
            if(j != shippartproperties.additCollisChunkIdx.length)
                System.out.println("*** bigship: collis internal error");
            if(shippartproperties.haveGun())
            {
                shippartproperties.headChunkIdx = hierMesh().chunkFind(shippartproperties.headChunkName);
                shippartproperties.gunChunkIdx = hierMesh().chunkFind(shippartproperties.gunChunkName);
                hierMesh().setCurChunk(shippartproperties.headChunkIdx);
                hierMesh().getChunkLocObj(tmpL);
                shippartproperties.fireOffset = new Point3d();
                tmpL.get(shippartproperties.fireOffset);
                shippartproperties.fireOrient = new Orient();
                tmpL.get(shippartproperties.fireOrient);
                Vector3d vector3d = new Vector3d();
                Vector3d vector3d1 = new Vector3d();
                vector3d.set(1.0D, 0.0D, 0.0D);
                vector3d1.set(1.0D, 0.0D, 0.0D);
                tmpL.transform(vector3d);
                hierMesh().setCurChunk(shippartproperties.gunChunkIdx);
                hierMesh().getChunkLocObj(tmpL);
                tmpL.transform(vector3d1);
                shippartproperties.GUN_STD_PITCH = Geom.RAD2DEG((float)vector3d.angle(vector3d1));
            }
            else if(shippartproperties.haveRadar())
            {
                shippartproperties.headChunkIdx = hierMesh().chunkFind(shippartproperties.headChunkName);
                shippartproperties.gunChunkIdx = -1;
                hierMesh().setCurChunk(shippartproperties.headChunkIdx);
                hierMesh().getChunkLocObj(tmpL);
                shippartproperties.fireOffset = new Point3d();
                tmpL.get(shippartproperties.fireOffset);
                shippartproperties.fireOrient = new Orient();
                tmpL.get(shippartproperties.fireOrient);
                Vector3d vector3d = new Vector3d();
                Vector3d vector3d1 = new Vector3d();
                vector3d.set(1.0D, 0.0D, 0.0D);
                vector3d1.set(1.0D, 0.0D, 0.0D);
                tmpL.transform(vector3d);
            }
        }

        initMeshMats();
    }

    private void initMeshMats()
    {
        if(Config.cur.b3dgunners)
        {
            return;
        } else
        {
            hierMesh().materialReplaceToNull("Sailor");
            hierMesh().materialReplaceToNull("Sailor1o");
            hierMesh().materialReplaceToNull("Sailor2p");
            hierMesh().materialReplaceToNull("marina");
            hierMesh().materialReplaceToNull("mariM");
            hierMesh().materialReplaceToNull("Crew");
            return;
        }
    }

    private void disappearSailorsMats()
    {
        if(Config.cur.b3dgunners)
        {
            try{
                hierMesh().materialReplace("Sailor", "Sailor_dis");
                hierMesh().materialReplace("Sailor1o", "Sailor1o_dis");
                hierMesh().materialReplace("Sailor2p", "Sailor2p_dis");
                hierMesh().materialReplace("marina", "marina_dis");
                hierMesh().materialReplace("mariM", "mariM_dis");
                hierMesh().materialReplace("Crew", "Crew_dis");
                hierMesh().materialReplace("SailorUSo", "SailorUSo_dis");
            }
                catch(Exception exception)
            {
            }
        }

        bSailorsDisappear = true;
    }

    private void appearSailorsMats()
    {
        if(Config.cur.b3dgunners)
        {
            hierMesh().materialReplace("Sailor", "Sailor");
            hierMesh().materialReplace("Sailor1o", "Sailor1o");
            hierMesh().materialReplace("Sailor2p", "Sailor2p");
            hierMesh().materialReplace("marina", "marina");
            hierMesh().materialReplace("mariM", "mariM");
            hierMesh().materialReplace("Crew", "Crew");
            hierMesh().materialReplace("SailorUSo", "SailorUSo");
        }

        bSailorsDisappear = false;
    }

    private void makeLive()
    {
        dying = 0;
        for(int i = 0; i < parts.length; i++)
        {
            parts[i].damage = 0.0F;
            parts[i].state = 0;
            parts[i].pro = prop.propparts[i];
        }

        for(int j = 0; j < hierMesh().chunks(); j++)
        {
            hierMesh().setCurChunk(j);
            if(hierMesh().chunkName().equals("Red") || (this instanceof TransparentTestRunway) && hierMesh().chunkName().equals("Hull1"))
                continue;
            boolean flag = !hierMesh().chunkName().endsWith("_dmg");
            if(hierMesh().chunkName().startsWith("ShdwRcv"))
                flag = false;
            hierMesh().chunkVisible(flag);
        }

        recomputeShotpoints();
    }

    private void setDefaultLivePose()
    {
        int i = hierMesh().hookFind("Ground_Level");
        if(i != -1)
        {
            Matrix4d matrix4d = new Matrix4d();
            hierMesh().hookMatrix(i, matrix4d);
        }
        for(int j = 0; j < arms.length; j++)
        {
            int k = arms[j].part_idx;
            setGunAngles(arms[j], parts[k].pro.HEAD_STD_YAW, parts[k].pro.GUN_STD_PITCH);
        }
        for(int j = 0; j < radars.length; j++)
        {
            int k = radars[j].part_idx;
            setRadarAngles(radars[j], parts[k].pro.HEAD_STD_YAW);
        }

        bodyDepth = 0.0F;
        align();
    }

    protected BigshipGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    private BigshipGeneric(ShipProperties shipproperties, ActorSpawnArg actorspawnarg)
    {
        super(shipproperties.meshName);
        CURRSPEED = 1.0F;
        isTurning = false;
        isTurningBackward = false;
        mustRecomputePath = false;
        mustSendSpeedToNet = false;
        hasTracking = 0;
        prop = null;
        netsendPartsState_lasttimeMS = 0L;
        netsendPartsState_needtosend = false;
        netsendFire_lasttimeMS = 0L;
        netsendFire_armindex = 0;
        arms = null;
        radars = null;
        parts = null;
        shotpoints = null;
        netsendDmg_lasttimeMS = 0L;
        netsendDmg_partindex = 0;
        cachedSeg = 0;
        bSailorsDisappear = false;
        timeOfSailorsDisappear = 0L;
        timeOfDeath = 0L;
        dying = 0;
        respawnDelay = 0L;
        wakeupTmr = 0L;
        radarTmr = 0L;
        DELAY_WAKEUP = 0.0F;
        SKILL_IDX = 2;
        SLOWFIRE_K = 1.0F;
        pipes = null;
        dsmoks = null;
        noseW = null;
        nose = null;
        tail = null;
        o = new Orient();
        rollAmp = 0.7F * (float)Mission.curCloudsType() / shipproperties.STABILIZE_FACTOR;
        rollPeriod = 12345;
        rollWAmp = ((double)rollAmp * 19739.208802178713D) / (double)(180 * rollPeriod);
        pitchAmp = 0.1F * (float)Mission.curCloudsType() / shipproperties.STABILIZE_FACTOR;
        pitchPeriod = 23456;
        pitchWAmp = ((double)pitchAmp * 19739.208802178713D) / (double)(180 * pitchPeriod);
        W = new Vector3d(0.0D, 0.0D, 0.0D);
        N = new Vector3d(0.0D, 0.0D, 1.0D);
        tmpV = new Vector3d();
        initOr = new Orient();
        initLoc = new Loc();
        airport = null;
        towPortNum = -1;
        zutiBornPlace = null;
        zutiIsClassBussy = false;

        for(int iii=0; iii<4; iii++)
        {
            BlastDeflectorControl[iii] = 0.0F;
            blastDeflector[iii] = 0.0F;
            blastDeflector_[iii] = 0.0F;
        }
        dvBlastDeflector = 0.3F;

        prop = shipproperties;
        if((this instanceof TransparentTestRunway) || (this instanceof TgtFactory))
            hideTransparentRunwayRed();
        CURRSPEED = prop.SPEED;
        initMeshBasedProperties();
        actorspawnarg.setStationary(this);
        path = null;
        collide(true);
        drawing(true);
        tmInterpoStart = tmInterpoEnd = 0L;
        bodyDepth = bodyPitch = bodyRoll = 0.0F;
        bodyDepth0 = bodyPitch0 = bodyRoll0 = 0.0F;
        bodyDepth1 = bodyPitch1 = bodyRoll1 = 0.0F;
        shipYaw = actorspawnarg.orient.getYaw();
        setPosition();
        pos.reset();
        parts = new Part[prop.propparts.length];
        for(int i = 0; i < parts.length; i++)
            parts[i] = new Part();

        makeLive();
        appearSailorsMats();
        createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        SKILL_IDX = Chief.new_SKILL_IDX;
        SLOWFIRE_K = Chief.new_SLOWFIRE_K;
        DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
        wakeupTmr = 0L;
        radarTmr = 0L;
        CreateGuns();
        CreateRadars();
        int j = 0;
        for(int k = 0; k < parts.length; k++)
            if(parts[k].pro.isItLifeKeeper() || parts[k].pro.haveGun())
                j++;

        if(j <= 0)
        {
            dsmoks = null;
        } else
        {
            dsmoks = new Pipe[j];
            j = 0;
            for(int l = 0; l < parts.length; l++)
                if(parts[l].pro.isItLifeKeeper() || parts[l].pro.haveGun())
                {
                    dsmoks[j] = new Pipe();
                    dsmoks[j].part_idx = l;
                    dsmoks[j].pipe = null;
                    j++;
                }

        }
        setDefaultLivePose();
        if(!isNetMirror() && prop.nGuns > 0 && DELAY_WAKEUP > 0.0F)
            wakeupTmr = -SecsToTicks(Rnd(2.0F, 7F));
        if(((this instanceof TransparentTestRunway) || (this instanceof TgtFactory)) && Engine.land().isWater(pos.getAbs().getX(), pos.getAbs().getY()))
            hierMesh().chunkVisible("Hull1", false);
        createAirport();
        if(!interpEnd("move"))
        {
            interpPut(new Move(), "move", Time.current(), null);
            InterpolateAdapter.forceListener(this);
        }
    }

    public BigshipGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
    {
        CURRSPEED = 1.0F;
        isTurning = false;
        isTurningBackward = false;
        mustRecomputePath = false;
        mustSendSpeedToNet = false;
        hasTracking = 0;
        prop = null;
        netsendPartsState_lasttimeMS = 0L;
        netsendPartsState_needtosend = false;
        netsendFire_lasttimeMS = 0L;
        netsendFire_armindex = 0;
        arms = null;
        radars = null;
        parts = null;
        shotpoints = null;
        netsendDmg_lasttimeMS = 0L;
        netsendDmg_partindex = 0;
        cachedSeg = 0;
        bSailorsDisappear = false;
        timeOfSailorsDisappear = 0L;
        timeOfDeath = 0L;
        dying = 0;
        respawnDelay = 0L;
        wakeupTmr = 0L;
        radarTmr = 0L;
        DELAY_WAKEUP = 0.0F;
        SKILL_IDX = 2;
        SLOWFIRE_K = 1.0F;
        pipes = null;
        dsmoks = null;
        noseW = null;
        nose = null;
        tail = null;
        o = new Orient();
        W = new Vector3d(0.0D, 0.0D, 0.0D);
        N = new Vector3d(0.0D, 0.0D, 1.0D);
        tmpV = new Vector3d();
        initOr = new Orient();
        initLoc = new Loc();
        airport = null;
        towPortNum = -1;
        zutiBornPlace = null;
        zutiIsClassBussy = false;
        if(this instanceof TransparentTestRunway)
            hideTransparentRunwayRed();

        for(int iii=0; iii<4; iii++)
        {
            BlastDeflectorControl[iii] = 0.0F;
            blastDeflector[iii] = 0.0F;
            blastDeflector_[iii] = 0.0F;
        }
        dvBlastDeflector = 0.3F;

        try
        {
            int j = sectfile.sectionIndex(s1);
            String s3 = sectfile.var(j, 0);
            Object obj = Spawn.get(s3);
            if(obj == null)
                throw new ActorException("Ship: Unknown class of ship (" + s3 + ")");
            prop = ((SPAWN)obj).proper;
            try
            {
                setMesh(prop.meshName);
            }
            catch(RuntimeException runtimeexception)
            {
                super.destroy();
                throw runtimeexception;
            }
            initMeshBasedProperties();
            if(prop.soundName != null)
                newSound(prop.soundName, true);
            setName(s);
            setArmy(i);
            LoadPath(sectfile1, s2);
            cachedSeg = 0;
            tmInterpoStart = tmInterpoEnd = 0L;
            rollAmp = 0.7F * (float)Mission.curCloudsType() / prop.STABILIZE_FACTOR;
            rollPeriod = 12345;
            rollWAmp = ((double)rollAmp * 19739.208802178713D) / (double)(180 * rollPeriod);
            pitchAmp = 0.1F * (float)Mission.curCloudsType() / prop.STABILIZE_FACTOR;
            pitchPeriod = 23456;
            pitchWAmp = ((double)pitchAmp * 19739.208802178713D) / (double)(180 * pitchPeriod);
            bodyDepth = bodyPitch = bodyRoll = 0.0F;
            bodyDepth0 = bodyPitch0 = bodyRoll0 = 0.0F;
            bodyDepth1 = bodyPitch1 = bodyRoll1 = 0.0F;
            CURRSPEED = 2.0F * prop.SPEED;
            setMovablePosition(NetServerParams.getServerTime());
            pos.reset();
            collide(true);
            drawing(true);
            parts = new Part[prop.propparts.length];
            for(int k = 0; k < parts.length; k++)
                parts[k] = new Part();

            makeLive();
            appearSailorsMats();
            int l = 0;
            for(int i1 = 0; i1 <= 10; i1++)
            {
                String s4 = "Vapor";
                if(i1 > 0)
                    s4 = s4 + (i1 - 1);
                if(mesh().hookFind(s4) >= 0)
                    l++;
                s4 = "VaporCoal";
                if(i1 > 0)
                    s4 = s4 + (i1 - 1);
                if(mesh().hookFind(s4) >= 0)
                    l++;
                s4 = "Diesel";
                if(i1 > 0)
                    s4 = s4 + (i1 - 1);
                if(mesh().hookFind(s4) >= 0)
                    l++;
            }

            if(l <= 0)
            {
                pipes = null;
            } else
            {
                pipes = new Pipe[l];
                l = 0;
                for(int j1 = 0; j1 <= 10; j1++)
                {
                    String s5 = "Vapor";
                    if(j1 > 0)
                        s5 = s5 + (j1 - 1);
                    if(mesh().hookFind(s5) < 0)
                        continue;
                    pipes[l] = new Pipe();
                    int j2 = hierMesh().hookParentChunk(s5);
                    if(j2 < 0)
                    {
                        System.out.println(" *** Bigship: unexpected error in Vapor hook " + s5);
                        pipes = null;
                        break;
                    }
                    int k2;
                    for(k2 = 0; k2 < parts.length && parts[k2].pro.baseChunkIdx != j2; k2++);
                    if(k2 >= parts.length)
                    {
                        System.out.println(" *** Bigship: Vapor hook '" + s5 + "' MUST be linked to baseChunk");
                        pipes = null;
                        break;
                    }
                    pipes[l].part_idx = k2;
                    HookNamed hooknamed = new HookNamed(this, s5);
                    pipes[l].pipe = Eff3DActor.New(this, hooknamed, null, 1.0F, "Effects/Smokes/SmokePipeShip.eff", -1F);
                    l++;
                }

                for(int j1 = 0; j1 <= 10; j1++)
                {
                    String s5 = "VaporCoal";
                    if(j1 > 0)
                        s5 = s5 + (j1 - 1);
                    if(mesh().hookFind(s5) < 0)
                        continue;
                    pipes[l] = new Pipe();
                    int j2 = hierMesh().hookParentChunk(s5);
                    if(j2 < 0)
                    {
                        System.out.println(" *** Bigship: unexpected error in VaporCoal hook " + s5);
                        pipes = null;
                        break;
                    }
                    int k2;
                    for(k2 = 0; k2 < parts.length && parts[k2].pro.baseChunkIdx != j2; k2++);
                    if(k2 >= parts.length)
                    {
                        System.out.println(" *** Bigship: VaporCoal hook '" + s5 + "' MUST be linked to baseChunk");
                        pipes = null;
                        break;
                    }
                    pipes[l].part_idx = k2;
                    HookNamed hooknamed = new HookNamed(this, s5);
                    pipes[l].pipe = Eff3DActor.New(this, hooknamed, null, 1.0F, "Effects/Smokes/SmokePipeShipCoal.eff", -1F);
                    l++;
                }

                for(int j1 = 0; j1 <= 10; j1++)
                {
                    String s5 = "Diesel";
                    if(j1 > 0)
                        s5 = s5 + (j1 - 1);
                    if(mesh().hookFind(s5) < 0)
                        continue;
                    pipes[l] = new Pipe();
                    int j2 = hierMesh().hookParentChunk(s5);
                    if(j2 < 0)
                    {
                        System.out.println(" *** Bigship: unexpected error in Diesel hook " + s5);
                        pipes = null;
                        break;
                    }
                    int k2;
                    for(k2 = 0; k2 < parts.length && parts[k2].pro.baseChunkIdx != j2; k2++);
                    if(k2 >= parts.length)
                    {
                        System.out.println(" *** Bigship: Diesel hook '" + s5 + "' MUST be linked to baseChunk");
                        pipes = null;
                        break;
                    }
                    pipes[l].part_idx = k2;
                    HookNamed hooknamed = new HookNamed(this, s5);
                    pipes[l].pipe = Eff3DActor.New(this, hooknamed, null, 1.0F, "Effects/Smokes/SmokePipeShipDiesel.eff", -1F);
                    l++;
                }

            }
            wake[2] = wake[1] = wake[0] = null;
            tail = null;
            noseW = null;
            nose = null;
            boolean flag = prop.SLIDER_DIST / 2.5F < 90F;
            if(mesh().hookFind("_Prop") >= 0)
            {
                HookNamedZ0 hooknamedz0 = new HookNamedZ0(this, "_Prop");
                tail = Eff3DActor.New(this, hooknamedz0, null, 1.0F, flag ? "3DO/Effects/Tracers/ShipTrail/PropWakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/PropWake.eff", -1F);
            }
            if(mesh().hookFind("_Centre") >= 0)
            {
                Loc loc = new Loc();
                Loc loc1 = new Loc();
                HookNamed hooknamed1 = new HookNamed(this, "_Left");
                hooknamed1.computePos(this, new Loc(), loc);
                HookNamed hooknamed2 = new HookNamed(this, "_Right");
                hooknamed2.computePos(this, new Loc(), loc1);
                float f = (float)loc.getPoint().distance(loc1.getPoint());
                HookNamedZ0 hooknamedz0_1 = new HookNamedZ0(this, "_Centre");
                if(mesh().hookFind("_Prop") >= 0)
                {
                    HookNamedZ0 hooknamedz0_3 = new HookNamedZ0(this, "_Prop");
                    Loc loc2 = new Loc();
                    hooknamedz0_1.computePos(this, new Loc(), loc2);
                    Loc loc3 = new Loc();
                    hooknamedz0_3.computePos(this, new Loc(), loc3);
                    float f1 = (float)loc2.getPoint().distance(loc3.getPoint());
                    wake[0] = Eff3DActor.New(this, hooknamedz0_3, new Loc((double)(-f1) * 0.33000000000000002D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/Wake.eff", -1F);
                    wake[1] = Eff3DActor.New(this, hooknamedz0_1, new Loc((double)f1 * 0.14999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff" : "3DO/Effects/Tracers/ShipTrail/WakeS.eff", -1F);
                    wake[2] = Eff3DActor.New(this, hooknamedz0_1, new Loc((double)(-f1) * 0.14999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff" : "3DO/Effects/Tracers/ShipTrail/WakeS.eff", -1F);
                } else
                {
                    wake[0] = Eff3DActor.New(this, hooknamedz0_1, new Loc((double)(-f) * 0.29999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, (double)prop.SLIDER_DIST / 2.5D >= 50D ? "3DO/Effects/Tracers/ShipTrail/Wake.eff" : "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff", -1F);
                }
            }
            if(mesh().hookFind("_Nose") >= 0)
            {
                HookNamedZ0 hooknamedz0_2 = new HookNamedZ0(this, "_Nose");
                noseW = Eff3DActor.New(this, hooknamedz0_2, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), 1.0F, "3DO/Effects/Tracers/ShipTrail/SideWave.eff", -1F);
                nose = Eff3DActor.New(this, hooknamedz0_2, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), 1.0F, flag ? "3DO/Effects/Tracers/ShipTrail/FrontPuffBoat.eff" : "3DO/Effects/Tracers/ShipTrail/FrontPuff.eff", -1F);
            }
            SetEffectsIntens(0.0F);
            int k1 = Mission.cur().getUnitNetIdRemote(this);
            NetChannel netchannel = Mission.cur().getNetMasterChannel();
            if(netchannel == null)
                net = new Master(this);
            else
            if(k1 != 0)
                net = new Mirror(this, netchannel, k1);
            SKILL_IDX = Chief.new_SKILL_IDX;
            SLOWFIRE_K = Chief.new_SLOWFIRE_K;
            DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
            wakeupTmr = 0L;
            radarTmr = 0L;
            CreateGuns();
            CreateRadars();
            k1 = 0;
            for(int l1 = 0; l1 < parts.length; l1++)
                if(parts[l1].pro.isItLifeKeeper() || parts[l1].pro.haveGun())
                    k1++;

            if(k1 <= 0)
            {
                dsmoks = null;
            } else
            {
                dsmoks = new Pipe[k1];
                k1 = 0;
                for(int i2 = 0; i2 < parts.length; i2++)
                    if(parts[i2].pro.isItLifeKeeper() || parts[i2].pro.haveGun())
                    {
                        dsmoks[k1] = new Pipe();
                        dsmoks[k1].part_idx = i2;
                        dsmoks[k1].pipe = null;
                        k1++;
                    }

            }
            setDefaultLivePose();
            if(!isNetMirror() && prop.nGuns > 0 && DELAY_WAKEUP > 0.0F)
                wakeupTmr = -SecsToTicks(Rnd(2.0F, 7F));
            createAirport();
            if(((this instanceof TransparentTestRunway) || (this instanceof TgtFactory)) && Engine.land().isWater(pos.getAbs().getX(), pos.getAbs().getY()))
                hierMesh().chunkVisible("Hull1", false);
            if(!World.cur().triggersGuard.listTriggerChiefSol.contains(s) && !interpEnd("move"))
            {
                interpPut(new Move(), "move", Time.current(), null);
                InterpolateAdapter.forceListener(this);
            }
        }
        catch(Exception exception)
        {
            System.out.println("Ship creation failure:");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            throw new ActorException();
        }
    }

    public void startMove()
    {
        long l = NetServerParams.getServerTime();
        for(int k1 = 0; k1 < path.size(); k1++)
        {
            Segment segment = (Segment)path.get(k1);
            segment.timeIn += l;
            segment.timeOut += l;
        }

        if(!interpEnd("move"))
        {
            interpPut(new Move(), "move", Time.current(), null);
            InterpolateAdapter.forceListener(this);
        }
    }

    private void SetEffectsIntens(float f)
    {
        if(dying != 0)
            f = -1F;
        if(pipes != null)
        {
            boolean flag = false;
            for(int j = 0; j < pipes.length; j++)
            {
                if(pipes[j] == null)
                    continue;
                if(pipes[j].pipe == null)
                {
                    pipes[j] = null;
                    continue;
                }
                if(f >= 0.0F)
                {
                    pipes[j].pipe._setIntesity(f);
                    flag = true;
                } else
                {
                    pipes[j].pipe._finish();
                    pipes[j].pipe = null;
                    pipes[j] = null;
                }
            }

            if(!flag)
            {
                for(int k = 0; k < pipes.length; k++)
                    if(pipes[k] != null)
                        pipes[k] = null;

                pipes = null;
            }
        }
        for(int i = 0; i < 3; i++)
        {
            if(wake[i] == null)
                continue;
            if(f >= 0.0F)
            {
                wake[i]._setIntesity(f);
            } else
            {
                wake[i]._finish();
                wake[i] = null;
            }
        }

        if(noseW != null)
            if(f >= 0.0F)
            {
                noseW._setIntesity(f);
            } else
            {
                noseW._finish();
                noseW = null;
            }
        if(nose != null)
            if(f >= 0.0F)
            {
                nose._setIntesity(f);
            } else
            {
                nose._finish();
                nose = null;
            }
        if(tail != null)
            if(f >= 0.0F)
            {
                tail._setIntesity(f);
            } else
            {
                tail._finish();
                tail = null;
            }
    }

    private void LoadPath(SectFile sectfile, String s)
    {
        int i = sectfile.sectionIndex(s);
        if(i < 0)
            throw new ActorException("Ship path: Section [" + s + "] not found");
        int j = sectfile.vars(i);
        if(j < 1)
            throw new ActorException("Ship path must contain at least 2 nodes");
        path = new ArrayList();
        for(int k = 0; k < j; k++)
        {
            StringTokenizer stringtokenizer = new StringTokenizer(sectfile.line(i, k));
            float f1 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
            float f2 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
//            float f4 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
            stringtokenizer.nextToken();
            double d = 0.0D;
            float f8 = 0.0F;
            if(stringtokenizer.hasMoreTokens())
            {
                d = Double.valueOf(stringtokenizer.nextToken()).doubleValue();
                if(stringtokenizer.hasMoreTokens())
                {
                    Double.valueOf(stringtokenizer.nextToken()).doubleValue();
                    if(stringtokenizer.hasMoreTokens())
                    {
                        f8 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
                        if(f8 <= 0.0F)
                            f8 = prop.SPEED;
                    }
                }
            }
            if(f8 <= 0.0F && (k == 0 || k == j - 1))
                f8 = prop.SPEED;
            if(k >= j - 1)
                d = -1D;
            Segment segment12 = new Segment();
            segment12.posIn = new Point3d(f1, f2, 0.0D);
            if(Math.abs(d) < 0.10000000000000001D)
            {
                segment12.timeIn = 0L;
            } else
            {
                segment12.timeIn = (long)(d * 60D * 1000D + (d <= 0.0D ? -0.5D : 0.5D));
                if(k == 0 && segment12.timeIn < 0L)
                    segment12.timeIn = -segment12.timeIn;
            }
            segment12.speedIn = f8;
            segment12.slidersOn = true;
            path.add(segment12);
        }

        for(int l = 0; l < path.size() - 1; l++)
        {
            Segment segment2 = (Segment)path.get(l);
            Segment segment5 = (Segment)path.get(l + 1);
            segment2.length = (float)segment2.posIn.distance(segment5.posIn);
        }

        int i1 = 0;
        float f = ((Segment)path.get(i1)).length;
        int l1;
        for(; i1 < path.size() - 1; i1 = l1)
        {
            l1 = i1 + 1;
            do
            {
                Segment segment8 = (Segment)path.get(l1);
                if(segment8.speedIn > 0.0F)
                    break;
                f += segment8.length;
                l1++;
            } while(true);
            if(l1 - i1 <= 1)
                continue;
            float f3 = ((Segment)path.get(i1)).length;
            float f5 = ((Segment)path.get(i1)).speedIn;
            float f6 = ((Segment)path.get(l1)).speedIn;
            for(int j2 = i1 + 1; j2 < l1; j2++)
            {
                Segment segment11 = (Segment)path.get(j2);
                float f10 = f3 / f;
                segment11.speedIn = f5 * (1.0F - f10) + f6 * f10;
                f += segment11.length;
            }

        }

        for(int j1 = 0; j1 < path.size() - 1; j1++)
        {
            Segment segment3 = (Segment)path.get(j1);
            Segment segment6 = (Segment)path.get(j1 + 1);
            if(segment3.timeIn > 0L && segment6.timeIn > 0L)
            {
                Segment segment9 = new Segment();
                segment9.posIn = new Point3d(segment3.posIn);
                segment9.posIn.add(segment6.posIn);
                segment9.posIn.scale(0.5D);
                segment9.timeIn = 0L;
                segment9.speedIn = (segment3.speedIn + segment6.speedIn) * 0.5F;
                path.add(j1 + 1, segment9);
            }
        }

        for(int k1 = 0; k1 < path.size() - 1; k1++)
        {
            Segment segment4 = (Segment)path.get(k1);
            Segment segment7 = (Segment)path.get(k1 + 1);
            segment4.length = (float)segment4.posIn.distance(segment7.posIn);
        }

        Segment segment = (Segment)path.get(0);
        boolean flag = segment.timeIn != 0L;
        long l2 = segment.timeIn;
        for(int i2 = 0; i2 < path.size() - 1; i2++)
        {
            Segment segment1 = (Segment)path.get(i2);
            Segment segment10 = (Segment)path.get(i2 + 1);
            segment1.posOut = new Point3d(segment10.posIn);
            segment10.posIn = segment1.posOut;
            float f7 = segment1.speedIn;
            float f9 = segment10.speedIn;
            float f11 = (f7 + f9) * 0.5F;
            if(flag)
            {
                segment1.speedIn = 0.0F;
                segment1.speedOut = f9;
                float f12 = ((2.0F * segment1.length) / f9) * 1000F + 0.5F;
                segment1.timeIn = l2;
                segment1.timeOut = segment1.timeIn + (long)(int)f12;
                l2 = segment1.timeOut;
                flag = false;
                continue;
            }
            if(segment10.timeIn == 0L)
            {
                segment1.speedIn = f7;
                segment1.speedOut = f9;
                float f13 = (segment1.length / f11) * 1000F + 0.5F;
                segment1.timeIn = l2;
                segment1.timeOut = segment1.timeIn + (long)(int)f13;
                l2 = segment1.timeOut;
                flag = false;
                continue;
            }
            if(segment10.timeIn > 0L)
            {
                float f14 = (segment1.length / f11) * 1000F + 0.5F;
                long l3 = l2 + (long)(int)f14;
                if(l3 >= segment10.timeIn)
                {
                    segment10.timeIn = 0L;
                } else
                {
                    segment1.speedIn = f7;
                    segment1.speedOut = 0.0F;
                    float f15 = ((2.0F * segment1.length) / f7) * 1000F + 0.5F;
                    segment1.timeIn = l2;
                    segment1.timeOut = segment1.timeIn + (long)(int)f15;
                    l2 = segment10.timeIn;
                    flag = true;
                    continue;
                }
            }
            if(segment10.timeIn == 0L)
            {
                segment1.speedIn = f7;
                segment1.speedOut = f9;
                float f16 = (segment1.length / f11) * 1000F + 0.5F;
                segment1.timeIn = l2;
                segment1.timeOut = segment1.timeIn + (long)(int)f16;
                l2 = segment1.timeOut;
                flag = false;
            } else
            {
                segment1.speedIn = f7;
                segment1.speedOut = 0.0F;
                float f17 = ((2.0F * segment1.length) / f7) * 1000F + 0.5F;
                segment1.timeIn = l2;
                segment1.timeOut = segment1.timeIn + (long)(int)f17;
                l2 = segment1.timeOut + -segment10.timeIn;
                flag = true;
            }
        }

        path.remove(path.size() - 1);
    }

    public void align()
    {
        pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) - (double)bodyDepth;
        pos.setAbs(p);
    }

    private boolean computeInterpolatedDPR(long l)
    {
        if(tmInterpoStart >= tmInterpoEnd || l >= tmInterpoEnd)
        {
            bodyDepth = bodyDepth1;
            bodyPitch = bodyPitch1;
            bodyRoll = bodyRoll1;
            return false;
        }
        if(l <= tmInterpoStart)
        {
            bodyDepth = bodyDepth0;
            bodyPitch = bodyPitch0;
            bodyRoll = bodyRoll0;
            return true;
        } else
        {
            float f = (float)(l - tmInterpoStart) / (float)(tmInterpoEnd - tmInterpoStart);
            bodyDepth = bodyDepth0 + (bodyDepth1 - bodyDepth0) * f;
            bodyPitch = bodyPitch0 + (bodyPitch1 - bodyPitch0) * f;
            bodyRoll = bodyRoll0 + (bodyRoll1 - bodyRoll0) * f;
            return true;
        }
    }

    private void setMovablePosition(long l)
    {
        if(cachedSeg < 0)
            cachedSeg = 0;
        else
        if(cachedSeg >= path.size())
            cachedSeg = path.size() - 1;
        Segment segment = (Segment)path.get(cachedSeg);
        if(segment.timeIn <= l && l <= segment.timeOut)
        {
            SetEffectsIntens(1.0F);
            setMovablePosition((float)(l - segment.timeIn) / (float)(segment.timeOut - segment.timeIn));
            return;
        }
        if(l > segment.timeOut)
        {
            while(cachedSeg + 1 < path.size())
            {
                Segment segment1 = (Segment)path.get(++cachedSeg);
                if(l <= segment1.timeIn)
                {
                    SetEffectsIntens(0.0F);
                    setMovablePosition(0.0F);
                    return;
                }
                if(l <= segment1.timeOut)
                {
                    SetEffectsIntens(1.0F);
                    setMovablePosition((float)(l - segment1.timeIn) / (float)(segment1.timeOut - segment1.timeIn));
                    return;
                }
            }
            SetEffectsIntens(-1F);
            setMovablePosition(1.0F);
            return;
        }
        while(cachedSeg > 0)
        {
            Segment segment2 = (Segment)path.get(--cachedSeg);
            if(l >= segment2.timeOut)
            {
                SetEffectsIntens(0.0F);
                setMovablePosition(1.0F);
                return;
            }
            if(l >= segment2.timeIn)
            {
                SetEffectsIntens(1.0F);
                setMovablePosition((float)(l - segment2.timeIn) / (float)(segment2.timeOut - segment2.timeIn));
                return;
            }
        }
        SetEffectsIntens(0.0F);
        setMovablePosition(0.0F);
    }

    private void setMovablePosition(float f)
    {
        Segment segment = (Segment)path.get(cachedSeg);
        float f1 = (float)(segment.timeOut - segment.timeIn) * 0.001F;
        float f2 = segment.speedIn;
        float f3 = segment.speedOut;
        float f4 = (f3 - f2) / f1;
        f *= f1;
        float f5 = f2 * f + f4 * f * f * 0.5F;
        isTurning = false;
        isTurningBackward = false;
        int i = cachedSeg;
        float f6 = prop.SLIDER_DIST - (segment.length - f5);
        if(f6 <= 0.0F)
        {
            p1.interpolate(segment.posIn, segment.posOut, (f5 + prop.SLIDER_DIST) / segment.length);
        } else
        {
            isTurning = true;
            do
            {
                if(i + 1 >= path.size())
                {
                    p1.interpolate(segment.posIn, segment.posOut, 1.0F + f6 / segment.length);
                    break;
                }
                segment = (Segment)path.get(++i);
                if(f6 <= segment.length)
                {
                    p1.interpolate(segment.posIn, segment.posOut, f6 / segment.length);
                    break;
                }
                f6 -= segment.length;
            } while(true);
        }
        i = cachedSeg;
        segment = (Segment)path.get(i);
        f6 = prop.SLIDER_DIST - f5;
        if(f6 <= 0.0F || !segment.slidersOn)
        {
            p2.interpolate(segment.posIn, segment.posOut, (f5 - prop.SLIDER_DIST) / segment.length);
        } else
        {
            isTurning = true;
            isTurningBackward = true;
            do
            {
                if(i <= 0)
                {
                    p2.interpolate(segment.posIn, segment.posOut, 0.0F - f6 / segment.length);
                    break;
                }
                segment = (Segment)path.get(--i);
                if(f6 <= segment.length)
                {
                    p2.interpolate(segment.posIn, segment.posOut, 1.0F - f6 / segment.length);
                    break;
                }
                f6 -= segment.length;
            } while(true);
        }
        if(!Mission.isDogfight() && !isTurning && mustRecomputePath && (double)f6 < -1.5D * (double)prop.SLIDER_DIST)
        {
            computeNewPath();
            mustRecomputePath = false;
        }
        p.interpolate(p1, p2, 0.5F);
        tmpvd.sub(p1, p2);
        if(tmpvd.lengthSquared() < 0.0010000000474974513D)
        {
            Segment segment1 = (Segment)path.get(cachedSeg);
            tmpvd.sub(segment1.posOut, segment1.posIn);
        }
        float f7 = (float)(Math.atan2(tmpvd.y, tmpvd.x) * 57.295779513082323D);
        setPosition(p, f7);
    }

    public void addRockingSpeed(Vector3d vector3d, Vector3d vector3d1, Point3d point3d)
    {
        tmpV.sub(point3d, pos.getAbsPoint());
        o.transformInv(tmpV);
        tmpV.cross(W, tmpV);
        o.transform(tmpV);
        vector3d.add(tmpV);
        vector3d1.set(N);
    }

    private void setPosition(Point3d point3d, float f)
    {
        shipYaw = f;
        float f1 = (float)(NetServerParams.getServerTime() % (long)rollPeriod) / (float)rollPeriod;
        float f2 = 0.05F * (20F - Math.abs(bodyPitch));
        if(f2 < 0.0F)
            f2 = 0.0F;
        float f3 = rollAmp * f2 * (float)Math.sin((double)(f1 * 2.0F) * 3.1415926535897931D);
        W.x = -rollWAmp * (double)f2 * Math.cos((double)(f1 * 2.0F) * 3.1415926535897931D);
        f1 = (float)(NetServerParams.getServerTime() % (long)pitchPeriod) / (float)pitchPeriod;
        float f4 = pitchAmp * f2 * (float)Math.sin((double)(f1 * 2.0F) * 3.1415926535897931D);
        W.y = -pitchWAmp * (double)f2 * Math.cos((double)(f1 * 2.0F) * 3.1415926535897931D);
        o.setYPR(shipYaw, bodyPitch + f4, bodyRoll + f3);
        N.set(0.0D, 0.0D, 1.0D);
        o.transform(N);
        initOr.setYPR(shipYaw, bodyPitch, bodyRoll);
        point3d.z = -bodyDepth;
        pos.setAbs(point3d, o);
        initLoc.set(point3d, initOr);
    }

    private void setPosition()
    {
        o.setYPR(shipYaw, bodyPitch, bodyRoll);
        N.set(0.0D, 0.0D, 1.0D);
        o.transform(N);
        pos.setAbs(o);
        align();
        initLoc.set(pos.getAbs());
    }

    public int WeaponsMask()
    {
        return prop.WEAPONS_MASK;
    }

    public int HitbyMask()
    {
        return prop.HITBY_MASK;
    }

    public int chooseBulletType(BulletProperties abulletproperties[])
    {
        if(dying != 0)
            return -1;
        if(abulletproperties.length == 1)
            return 0;
        if(abulletproperties.length <= 0)
            return -1;
        if(abulletproperties[0].power <= 0.0F)
            return 0;
        if(abulletproperties[1].power <= 0.0F)
            return 1;
        if(abulletproperties[0].cumulativePower > 0.0F)
            return 0;
        if(abulletproperties[1].cumulativePower > 0.0F)
            return 1;
        if(abulletproperties[0].powerType == 0)
            return 0;
        if(abulletproperties[1].powerType == 0)
            return 1;
        return abulletproperties[0].powerType != 1 ? 0 : 1;
    }

    public int chooseShotpoint(BulletProperties bulletproperties)
    {
        if(dying != 0)
            return -1;
        if(numshotpoints <= 0)
            return -1;
        else
            return shotpoints[Rnd(0, numshotpoints - 1)];
    }

    public boolean getShotpointOffset(int i, Point3d point3d)
    {
        if(dying != 0)
            return false;
        if(numshotpoints <= 0)
            return false;
        if(i == 0)
        {
            if(point3d != null)
                point3d.set(0.0D, 0.0D, 0.0D);
            return true;
        }
        int j = i - 1;
        if(j >= parts.length || j < 0)
            return false;
        if(parts[j].state == STPART_DEAD)
            return false;
        if(!parts[j].pro.isItLifeKeeper() && !parts[j].pro.haveGun())
            return false;
        if(point3d != null)
            point3d.set(parts[j].shotpointOffs);
        return true;
    }

    public float AttackMaxDistance()
    {
        return prop.ATTACK_MAX_DISTANCE;
    }

    public float AttackMaxDistance(ShipAim aim)
    {
        return GetGunProperties(aim).ATTACK_MAX_DISTANCE;
    }

    private void send_DeathCommand(Actor actor, NetChannel netchannel)
    {
        if(!isNetMaster())
            return;
        if(netchannel == null)
            if(Mission.isDeathmatch())
            {
                float f = Mission.respawnTime("Bigship");
                respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
            } else
            {
                respawnDelay = 0L;
            }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeLong(timeOfDeath);
            netmsgguaranted.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            long l = Time.tickNext();
            long l1 = 0L;
            boolean flag = dying == 1;
            double d = (double)(flag ? bodyDepth1 : bodyDepth0) / 1000D;
            if(d <= 0.0D)
                d = 0.0D;
            if(d >= 1.0D)
                d = 1.0D;
            int i = (int)(d * 32767D);
            if(i > 32767)
                i = 32767;
            if(i < 0)
                i = 0;
            netmsgguaranted.writeShort(i);
            d = (double)(flag ? bodyPitch1 : bodyPitch0) / 90D;
            if(d <= -1D)
                d = -1D;
            if(d >= 1.0D)
                d = 1.0D;
            i = (int)(d * 32767D);
            if(i > 32767)
                i = 32767;
            if(i < -32767)
                i = -32767;
            netmsgguaranted.writeShort(i);
            d = (double)(flag ? bodyRoll1 : bodyRoll0) / 90D;
            if(d <= -1D)
                d = -1D;
            if(d >= 1.0D)
                d = 1.0D;
            i = (int)(d * 32767D);
            if(i > 32767)
                i = 32767;
            if(i < -32767)
                i = -32767;
            netmsgguaranted.writeShort(i);
            d = (double)(tmInterpoEnd - tmInterpoStart) / 1000D / 1200D;
            if(flag)
                l1 = l - tmInterpoStart;
            else
                d = 0.0D;
            if(d <= 0.0D)
                d = 0.0D;
            if(d >= 1.0D)
                d = 1.0D;
            i = (int)(d * 32767D);
            if(i > 32767)
                i = 32767;
            if(i < 0)
                i = 0;
            netmsgguaranted.writeShort(i);
            d = (double)sink2Depth / 1000D;
            if(d <= 0.0D)
                d = 0.0D;
            if(d >= 1.0D)
                d = 1.0D;
            i = (int)(d * 32767D);
            if(i > 32767)
                i = 32767;
            if(i < 0)
                i = 0;
            netmsgguaranted.writeShort(i);
            d = (double)sink2Pitch / 90D;
            if(d <= -1D)
                d = -1D;
            if(d >= 1.0D)
                d = 1.0D;
            i = (int)(d * 32767D);
            if(i > 32767)
                i = 32767;
            if(i < -32767)
                i = -32767;
            netmsgguaranted.writeShort(i);
            d = (double)sink2Roll / 90D;
            if(d <= -1D)
                d = -1D;
            if(d >= 1.0D)
                d = 1.0D;
            i = (int)(d * 32767D);
            if(i > 32767)
                i = 32767;
            if(i < -32767)
                i = -32767;
            netmsgguaranted.writeShort(i);
            d = (double)(sink2timeWhenStop - tmInterpoEnd) / 1000D / 1200D;
            if(!flag)
            {
                d = (double)(tmInterpoEnd - tmInterpoStart) / 1000D / 1200D;
                l1 = l - tmInterpoStart;
            }
            if(d <= 0.0D)
                d = 0.0D;
            if(d >= 1.0D)
                d = 1.0D;
            i = (int)(d * 32767D);
            if(i > 32767)
                i = 32767;
            if(i < 0)
                i = 0;
            netmsgguaranted.writeShort(i);
            if(netchannel != null)
                netmsgguaranted.writeLong(l1);
            if(netchannel == null)
                net.post(netmsgguaranted);
            else
                net.postTo(netchannel, netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_RespawnCommand()
    {
        if(!isNetMaster() || !Mission.isDeathmatch())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(82);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        netsendPartsState_needtosend = false;
    }

    private void send_bufferized_FireCommand()
    {
        long l;
        int j;
        int k;
        if(!isNetMaster())
            return;
        l = NetServerParams.getServerTime();
        long l1 = Rnd(NETSEND_MIN_DELAY_MS_FIRE, NETSEND_MAX_DELAY_MS_FIRE);
        if(Math.abs(l - netsendFire_lasttimeMS) < l1)
            return;
        netsendFire_lasttimeMS = l;
        if(!net.isMirrored())
        {
            for(int i = 0; i < arms.length; i++)
                arms[i].enemy = null;

            netsendFire_armindex = 0;
            return;
        }
        j = 0;
        k = 0;
        int i1;
        for(i1 = 0; i1 < arms.length; i1++)
        {
            int i2 = netsendFire_armindex + i1;
            if(i2 >= arms.length)
                i2 -= arms.length;
            if(arms[i2].enemy == null)
                continue;
            if(parts[arms[i2].part_idx].state != 0)
            {
                System.out.println("*** BigShip internal error #0");
                arms[i2].enemy = null;
                continue;
            }
            if(!Actor.isValid(arms[i2].enemy) || !arms[i2].enemy.isNet())
            {
                arms[i2].enemy = null;
                continue;
            }
            if(j >= NETSEND_MAX_NUMITEMS_FIRE)
                break;
            netsendFire_tmpbuff[j].gun_idx = i2;
            netsendFire_tmpbuff[j].enemy = arms[i2].enemy;
            netsendFire_tmpbuff[j].timeWhenFireS = arms[i2].timeWhenFireS;
            netsendFire_tmpbuff[j].shotpointIdx = arms[i2].shotpointIdx;
            arms[i2].enemy = null;
            if(arms[i2].timeWhenFireS < 0.0D)
                k++;
            j++;
        }

        for(netsendFire_armindex += i1; netsendFire_armindex >= arms.length; netsendFire_armindex -= arms.length);
        if(j <= 0)
            return;
        NetMsgFiltered netmsgfiltered;
        netmsgfiltered = new NetMsgFiltered();
        try{
            netmsgfiltered.writeByte(224 + k);
        }
        catch(IOException e){
            System.out.println("BigShip: netmsgfiltered.writeByte(224 + k)");
            // TODO: +++ Resource Leak fixed by SAS~Storebror
            closeNetMsgFiltered(netmsgfiltered);
            // ---
            throw new RuntimeException("Can't register BigShip object");
        }
        for(int j1 = 0; j1 < j; j1++)
        {
            double d = netsendFire_tmpbuff[j1].timeWhenFireS;
            if(d < 0.0D)
            {
                try{
                    netmsgfiltered.writeByte(netsendFire_tmpbuff[j1].gun_idx);
                }
                catch(IOException e){
                    System.out.println("BigShip: netmsgfiltered.writeByte(netsendFire_tmpbuff[j1].gun_idx)");
                    // TODO: +++ Resource Leak fixed by SAS~Storebror
                    closeNetMsgFiltered(netmsgfiltered);
                    // ---
                    throw new RuntimeException("Can't register BigShip object");
                }
                try{
                    netmsgfiltered.writeNetObj(netsendFire_tmpbuff[j1].enemy.net);
                }
                catch(IOException e){
                    System.out.println("BigShip: netmsgfiltered.writeNetObj(netsendFire_tmpbuff[j1].enemy.net)");
                    // TODO: +++ Resource Leak fixed by SAS~Storebror
                    closeNetMsgFiltered(netmsgfiltered);
                    // ---
                    throw new RuntimeException("Can't register BigShip object");
                }
                try{
                    netmsgfiltered.writeByte(netsendFire_tmpbuff[j1].shotpointIdx);
                }
                catch(IOException e){
                    System.out.println("BigShip: netmsgfiltered.writeByte(netsendFire_tmpbuff[j1].shotpointIdx)");
                    // TODO: +++ Resource Leak fixed by SAS~Storebror
                    closeNetMsgFiltered(netmsgfiltered);
                    // ---
                    throw new RuntimeException("Can't register BigShip object");
                }
                k--;
            }
        }

        if(k != 0)
        {
            System.out.println("*** BigShip internal error #5");
            // TODO: +++ Resource Leak fixed by SAS~Storebror
            closeNetMsgFiltered(netmsgfiltered);
            // ---
            return;
        }
        try
        {
            for(int k1 = 0; k1 < j; k1++)
            {
                double d1 = netsendFire_tmpbuff[k1].timeWhenFireS;
                if(d1 < 0.0D)
                    continue;
                double d2 = (double)l * 0.001D;
                double d3 = (d1 - d2) * 1000D;
                if(d3 <= -2000D)
                    d3 = -2000D;
                if(d3 >= 5000D)
                    d3 = 5000D;
                d3 = (d3 - -2000D) / 7000D;
                int j2 = (int)(d3 * 255D);
                if(j2 < 0)
                    j2 = 0;
                if(j2 > 255)
                    j2 = 255;
                netmsgfiltered.writeByte(j2);
                netmsgfiltered.writeByte(netsendFire_tmpbuff[k1].gun_idx);
                netmsgfiltered.writeNetObj(netsendFire_tmpbuff[k1].enemy.net);
                netmsgfiltered.writeByte(netsendFire_tmpbuff[k1].shotpointIdx);
                netsendFire_tmpbuff[k1].enemy = null;
            }

            netmsgfiltered.setIncludeTime(true);
            net.post(l, netmsgfiltered);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        // TODO: +++ Resource Leak fixed by SAS~Storebror
        finally
        {
            closeNetMsgFiltered(netmsgfiltered);
        }
        // ---
    }

    // TODO: +++ Resource Leak fixed by SAS~Storebror
    private static void closeNetMsgFiltered(NetMsgFiltered netmsgfiltered) {
        try {
            netmsgfiltered.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }
    // ---


    private void send_bufferized_PartsState()
    {
        if(!isNetMaster())
            return;
        if(!netsendPartsState_needtosend)
            return;
        long l = NetServerParams.getServerTime();
        long l1 = Rnd(NETSEND_MIN_DELAY_MS_PARTSSTATE, NETSEND_MAX_DELAY_MS_PARTSSTATE);
        if(Math.abs(l - netsendPartsState_lasttimeMS) < l1)
            return;
        netsendPartsState_lasttimeMS = l;
        netsendPartsState_needtosend = false;
        if(!net.isMirrored())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(83);
            if(!Mission.isDogfight())
            {
                int i = 127;
                if(path != null && CURRSPEED < 2.0F * prop.SPEED)
                {
                    i = Math.round(CURRSPEED);
                    if(i < 0)
                        i = 0;
                    if(i > 126)
                        i = 126;
                }
                netmsgguaranted.writeByte(i);
            }
            int j = (parts.length + 3) / 4;
            int k = 0;
            for(int i1 = 0; i1 < j; i1++)
            {
                int j1 = 0;
                for(int k1 = 0; k1 < 4; k1++)
                {
                    if(k < parts.length)
                    {
                        int i2 = parts[k].state;
                        j1 |= i2 << k1 * 2;
                    }
                    k++;
                }

                netmsgguaranted.writeByte(j1);
            }

            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void bufferize_FireCommand(int i, Actor actor, int j, float f)
    {
        if(!isNetMaster())
            return;
        if(!net.isMirrored())
            return;
        if(!Actor.isValid(actor) || !actor.isNet())
            return;
        if(arms[i].enemy != null && arms[i].timeWhenFireS >= 0.0D)
            return;
        j &= 0xff;
        arms[i].enemy = actor;
        arms[i].shotpointIdx = j;
        if(f < 0.0F)
            arms[i].timeWhenFireS = -1D;
        else
            arms[i].timeWhenFireS = (double)f + (double)NetServerParams.getServerTime() * 0.001D;
    }

    private void mirror_send_speed()
    {
        if(!isNetMirror())
            return;
        if(net.masterChannel() instanceof NetChannelInStream)
            return;
        if(!Mission.isCoop())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(86);
            int i = 127;
            if(path != null && CURRSPEED < 2.0F * prop.SPEED)
            {
                i = Math.round(CURRSPEED);
                if(i < 0)
                    i = 0;
                if(i > 126)
                    i = 126;
            }
            netmsgguaranted.writeByte(i);
            net.postTo(net.masterChannel(), netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void mirror_send_bufferized_Damage()
    {
        if(!isNetMirror())
            return;
        if(net.masterChannel() instanceof NetChannelInStream)
            return;
        long l = NetServerParams.getServerTime();
        long l1 = Rnd(NETSEND_MIN_DELAY_MS_DMG, NETSEND_MAX_DELAY_MS_DMG);
        if(Math.abs(l - netsendDmg_lasttimeMS) < l1)
            return;
        netsendDmg_lasttimeMS = l;
        try
        {
            int i = 0;
            NetMsgFiltered netmsgfiltered = null;
            int j;
            for(j = 0; j < parts.length; j++)
            {
                int k = netsendDmg_partindex + j;
                if(k >= parts.length)
                    k -= parts.length;
                if(parts[k].state == STPART_DEAD || (double)parts[k].damage < 0.0078125D)
                    continue;
                int i1 = (int)(parts[k].damage * 128F);
                if(--i1 < 0)
                    i1 = 0;
                else
                if(i1 > 127)
                    i1 = 127;
                if(parts[k].damageIsFromRight)
                    i1 |= 0x80;
                if(i <= 0)
                {
                    netmsgfiltered = new NetMsgFiltered();
                    netmsgfiltered.writeByte(80);
                }
                Actor actor = parts[k].mirror_initiator;
                if(!Actor.isValid(actor) || !actor.isNet())
                    actor = null;
                parts[k].mirror_initiator = null;
                parts[k].damage = 0.0F;
                netmsgfiltered.writeByte(k);
                netmsgfiltered.writeByte(i1);
                netmsgfiltered.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
                if(++i >= NETSEND_MAX_NUMITEMS_DMG)
                    break;
            }

            for(netsendDmg_partindex += j; netsendDmg_partindex >= parts.length; netsendDmg_partindex -= parts.length);
            if(i > 0)
            {
                netmsgfiltered.setIncludeTime(false);
                net.postTo(l, net.masterChannel(), netmsgfiltered);
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void createNetObject(NetChannel netchannel, int i)
    {
        if(netchannel == null)
            net = new Master(this);
        else
            net = new Mirror(this, netchannel, i);
    }

    public void requestLocationOnCarrierDeck(NetUser netuser, String s)
    {
        if(!isNetMirror())
            return;
        try
        {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(REQUEST_LOC);
            netmsgguaranted.writeNetObj(netuser);
            netmsgguaranted.writeUTF(s);
            net.postTo(net.masterChannel(), netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void handleLocationRequest(NetUser netuser, String s)
    {
        try
        {
            Class class1 = ObjIO.classForName(s);
            Object obj = class1.newInstance();
            Aircraft aircraft = (Aircraft)obj;
            String s1 = Property.stringValue(aircraft.getClass(), "FlightModel", null);
            aircraft.FM = new FlightModel(s1);
            aircraft.FM.Gears.set(aircraft.hierMesh());
            Aircraft.forceGear(aircraft.getClass(), aircraft.hierMesh(), 1.0F);
            aircraft.FM.Gears.computePlaneLandPose(aircraft.FM);
            Aircraft.forceGear(aircraft.getClass(), aircraft.hierMesh(), 0.0F);
            if(airport != null)
            {
                Loc loc = airport.requestCell(aircraft);
                postLocationToMirror(netuser, loc);
            }
            aircraft.FM = null;
            aircraft.destroy();
            aircraft = null;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void postLocationToMirror(NetUser netuser, Loc loc)
    {
        NetChannel netchannel;
        netchannel = null;
        List list = NetEnv.channels();
        int i = 0;
        do
        {
            if(i >= list.size())
                break;
            netchannel = (NetChannel)list.get(i);
            com.maddox.rts.NetObj netobj = netchannel.getMirror(netuser.idRemote());
            if(netuser == netobj)
                break;
            netchannel = null;
            i++;
        } while(true);
        if(netchannel == null)
            return;
        try
        {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(REQUEST_LOC);
            netmsgguaranted.writeDouble(loc.getX());
            netmsgguaranted.writeDouble(loc.getY());
            netmsgguaranted.writeDouble(loc.getZ());
            netmsgguaranted.writeFloat(loc.getAzimut());
            netmsgguaranted.writeFloat(loc.getTangage());
            netmsgguaranted.writeFloat(loc.getKren());
            net.postTo(netchannel, netmsgguaranted);
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        return;
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        netmsgguaranted.writeLong(-1L);
        net.postTo(netchannel, netmsgguaranted);
        if(dying == 0)
            master_sendDrown(bodyDepth1, bodyPitch1, bodyRoll1, (float)(tmInterpoEnd - NetServerParams.getServerTime()) * 1000F);
        else
            send_DeathCommand(null, netchannel);
        netsendPartsState_needtosend = true;
    }

    public float getReloadingTime(ShipAim aim)
    {
        return SLOWFIRE_K * GetGunProperties(aim).DELAY_AFTER_SHOOT;
    }

    public float chainFireTime(ShipAim aim)
    {
        float f = GetGunProperties(aim).CHAINFIRE_TIME;
        return f > 0.0F ? f * Rnd(0.75F, 1.25F) : 0.0F;
    }

    public float probabKeepSameEnemy(Actor actor)
    {
        return 0.75F;
    }

    public float minTimeRelaxAfterFight()
    {
        return 0.1F;
    }

    public void gunStartParking(ShipAim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        ShipPartProperties shippartproperties = parts[firingdevice.part_idx].pro;
        aim.setRotationForParking(firingdevice.headYaw, firingdevice.gunPitch, shippartproperties.HEAD_STD_YAW, shippartproperties.GUN_STD_PITCH, shippartproperties.HEAD_YAW_RANGE, shippartproperties.HEAD_MAX_YAW_SPEED, shippartproperties.GUN_MAX_PITCH_SPEED);
    }

    public void gunInMove(boolean flag, ShipAim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        float f = aim.t();
        float f1 = aim.anglesYaw.getDeg(f);
        float f2 = aim.anglesPitch.getDeg(f);
        setGunAngles(firingdevice, f1, f2);
        pos.inValidate(false);
    }

    public Actor findEnemy(ShipAim aim)
    {
        if(isNetMirror())
            return null;
        ShipPartProperties shippartproperties = GetGunProperties(aim);
        Actor actor = null;
        o.add(shippartproperties.fireOrient, pos.getAbs().getOrient());

        if( shippartproperties.PREFER_SLOW_TARGET ){
            NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, KmHourToMSec(100F), shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT);
            actor = NearestEnemies.getAFoundEnemyInShoot(pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, getArmy(), shippartproperties.partOffs.z,
                    o, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG);
            if( actor == null ){
                NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, 9999.9F, shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT, shippartproperties.ATTACK_MISSILES, false, (String)null);
                actor = NearestEnemies.getAFoundEnemyInShoot(pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, getArmy(), shippartproperties.partOffs.z,
                        o, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG);
            }
        }
        else{
            switch(shippartproperties.ATTACK_FAST_TARGETS)
            {
            case 0:
                NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, KmHourToMSec(100F), shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT);
                break;

            case 1:
                NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, 9999.9F, shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT, shippartproperties.ATTACK_MISSILES, false, (String)null);
                break;

            default:
                NearestEnemies.set(shippartproperties.WEAPONS_MASK, KmHourToMSec(100F), 9999.9F, shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT, shippartproperties.ATTACK_MISSILES, false, (String)null);
                break;
            }
            if(shippartproperties.TypeOfTarget == 0)
                actor = NearestEnemies.getAFoundEnemyInShoot(pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, getArmy(), shippartproperties.partOffs.z,
                        o, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG);
            else if(shippartproperties.TypeOfTarget == 1)
            {
                actor = NearestEnemies.getAFoundEnemyInHeading(pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, getArmy(), shippartproperties.partOffs.z,
                        o, shippartproperties.SELF_YAW_RANGE, shippartproperties.NOSELF_YAW_RANGE, shippartproperties.NOHEADING_FLAG,
                        shippartproperties.ATTACK_MIN_DISTANCE);
                if( bLogDetail )
                {
                    System.out.println("BigShip: in findEnemy(aim), calling NearestEnemies.getAFoundEnemyInHeading");
                    if( actor == null )
                        System.out.println("        getting actor == null");
                }
            }
            else
            {
                actor = NearestEnemies.getAFoundEnemyShipInHeading(pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, getArmy(),
                        o, shippartproperties.SELF_YAW_RANGE, shippartproperties.NOSELF_YAW_RANGE, shippartproperties.NOHEADING_FLAG,
                        shippartproperties.ATTACK_MIN_DISTANCE);
            }
        }
        if(actor == null)
            return null;
        if( bLogDetail )
        {
            if(shippartproperties.TypeOfTarget == 0)
                System.out.println("BigShip: in findEnemy(aim), Enemy is found by Gun!");
            if(shippartproperties.TypeOfTarget == 1)
                System.out.println("BigShip: in findEnemy(aim), Enemy is found by SAM!");
            if(shippartproperties.TypeOfTarget == 2)
                System.out.println("BigShip: in findEnemy(aim), Enemy is found by SSM!");
            if(shippartproperties.TypeOfTarget == 3)
                System.out.println("BigShip: in findEnemy(aim), Enemy is found by SUM!");
        }
        if(!(actor instanceof Prey) && !(actor instanceof com.maddox.il2.objects.weapons.RocketBomb))
        {
            System.out.println("bigship: nearest enemies: non-Prey");
            return null;
        }
        FiringDevice firingdevice = GetFiringDevice(aim);
        BulletProperties bulletproperties = null;
        if(shippartproperties.TypeOfTarget == 0)
        {
            if(firingdevice.gun.prop != null)
            {
                int i = ((Prey)actor).chooseBulletType(firingdevice.gun.prop.bullet);
                if(i < 0)
                    return null;
                bulletproperties = firingdevice.gun.prop.bullet[i];
            }
            int j = ((Prey)actor).chooseShotpoint(bulletproperties);
            if(j < 0)
                return null;
            aim.shotpoint_idx = j;
            double d = distance(actor);
            d /= AttackMaxDistance(aim);
            aim.setAimingError(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), 0.0F);
            aim.scaleAimingError(23.47F * (float)(4 - SKILL_IDX) * (float)(4 - SKILL_IDX));
            if((actor instanceof Aircraft) || (actor instanceof MissileInterceptable))
                d *= 0.25D;
            aim.scaleAimingError((float)d);
            if(targetControl() > 0)
                aim.scaleAimingError(0.5F);
            return actor;
        }
        else{
            aim.shotpoint_idx = 0;
            return actor;
        }
    }

    public boolean enterToFireMode(int i, Actor actor, float f, ShipAim aim)
    {
        if(!isNetMirror())
        {
            FiringDevice firingdevice = GetFiringDevice(aim);
            bufferize_FireCommand(firingdevice.gun_idx, actor, aim.shotpoint_idx, i != 0 ? f : -1F);
        }
        return true;
    }

    private void Track_Mirror(int i, Actor actor, int j)
    {
        if(actor == null)
            return;
        if(arms == null || i < 0 || i >= arms.length || arms[i].aime == null)
            return;
        if(parts[arms[i].part_idx].state != 0)
        {
            return;
        } else
        {
            arms[i].aime.passive_StartFiring(0, actor, j, 0.0F);
            return;
        }
    }

    private void Fire_Mirror(int i, Actor actor, int j, float f)
    {
        if(actor == null)
            return;
        if(arms == null || i < 0 || i >= arms.length || arms[i].aime == null)
            return;
        if(parts[arms[i].part_idx].state != 0)
            return;
        if(f <= 0.15F)
            f = 0.15F;
        if(f >= 7F)
            f = 7F;
        arms[i].aime.passive_StartFiring(1, actor, j, f);
    }

    public boolean isMissile(ShipAim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        return(parts[firingdevice.part_idx].pro.TypeOfTarget > 0);
    }

    public int targetGun(ShipAim aim, Actor actor, float f, boolean flag)
    {
        if(!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0)
            return 0;
        FiringDevice firingdevice = GetFiringDevice(aim);
        if(firingdevice.gun instanceof CannonMidrangeGeneric)
        {
            int i = ((Prey)actor).chooseBulletType(firingdevice.gun.prop.bullet);
            if(i < 0)
                return 0;
            ((CannonMidrangeGeneric)firingdevice.gun).setBulletType(i);
        }
        boolean flag1 = ((Prey)actor).getShotpointOffset(aim.shotpoint_idx, p1);
        if(!flag1)
            return 0;
        ShipPartProperties shippartproperties = parts[firingdevice.part_idx].pro;
        float f1 = f * Rnd(0.8F, 1.2F);
        if(!Aimer.aim((BulletAimer)firingdevice.gun, actor, this, f1, p1, shippartproperties.fireOffset))
            return 0;
        Point3d point3d = new Point3d();
        Aimer.getPredictedTargetPosition(point3d);
        point3d.add(aim.getAimingError());
        Point3d point3d1 = Aimer.getHunterFirePoint();
        float f2 = 0.05F;
        double d = point3d.distance(point3d1);
        double d1 = point3d.z;
        if(f1 > 0.001F)
        {
            Point3d point3d2 = new Point3d();
            actor.pos.getAbs(point3d2);
            point3d2.add(aim.getAimingError());
            tmpvd.sub(point3d, point3d2);
            double d2 = tmpvd.length();
            if(d2 > 0.001D)
            {
                float f7 = (float)d2 / f1;
                if(f7 > 200F)
                    f7 = 200F;
                float f8 = f7 * 0.01F;
                point3d2.sub(point3d1);
                double d3 = point3d2.x * point3d2.x + point3d2.y * point3d2.y + point3d2.z * point3d2.z;
                if(d3 > 0.01D)
                {
                    float f9 = (float)tmpvd.dot(point3d2);
                    f9 /= (float)(d2 * Math.sqrt(d3));
                    f9 = (float)Math.sqrt(1.0F - f9 * f9);
                    f8 *= 0.4F + 0.6F * f9;
                }
                f8 *= 1.3F;
                f8 *= ShipAim.AngleErrorKoefForSkill[SKILL_IDX];
                int k = Mission.curCloudsType();
                if(k > 2)
                {
                    float f10 = k <= 4 ? 800F : 400F;
                    float f11 = (float)(d / (double)f10);
                    if(f11 > 1.0F)
                    {
                        if(f11 > 10F)
                            return 0;
                        f11 = (f11 - 1.0F) / 9F;
                        f8 *= f11 + 1.0F;
                    }
                }
                if(k >= 3 && d1 > (double)Mission.curCloudsHeight())
                    f8 *= 1.25F;
                if(targetControl() > 0)
                    if((actor instanceof Aircraft) || (actor instanceof MissileInterceptable))
                        f8 = (float)((double)f8 * 0.75D);
                    else
                        f8 = (float)((double)f8 * 0.075D);
                f2 += f8;
            }
        }
        if((actor instanceof Aircraft) || (actor instanceof MissileInterceptable))
        {
            aim.scaleAimingError(0.73F);
            f2 = f2 * (0.5F + Aim.AngleErrorKoefForSkill[SKILL_IDX] * 0.75F);
        } else
        if(aim.getAimingError().length() > 1.1100000143051147D)
            aim.scaleAimingError(0.973F);
        if(World.Sun().ToSun.z < -0.15F)
        {
            float f4 = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
            if(f4 >= 1.0F)
                f4 = 1.0F;
            if((actor instanceof Aircraft) && NetServerParams.getServerTime() - ((Aircraft)actor).tmSearchlighted < 1000L)
                f4 = 0.0F;
            f2 += 10F * f4;
        }
        float f5 = (float)actor.getSpeed(null) - 10F;
        if(f5 > 0.0F)
        {
            float f6 = 83.33334F;
            f5 = f5 < f6 ? f5 / f6 : 1.0F;
            f2 += f5 * shippartproperties.FAST_TARGETS_ANGLE_ERROR;
        }
        Vector3d vector3d = new Vector3d();
        if(!((BulletAimer)firingdevice.gun).FireDirection(point3d1, point3d, vector3d))
            return 0;
        float f3;
        if(flag)
        {
            f3 = 99999F;
            d1 = 99999F;
        } else
        {
            f3 = shippartproperties.HEAD_MAX_YAW_SPEED;
            d1 = shippartproperties.GUN_MAX_PITCH_SPEED;
        }
        o.add(shippartproperties.fireOrient, pos.getAbs().getOrient());
        int j = aim.setRotationForTargeting(this, o, point3d1, firingdevice.headYaw, firingdevice.gunPitch, vector3d, f2, f1, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG, shippartproperties.GUN_MIN_PITCH, shippartproperties.GUN_MAX_PITCH, f3, (float)d1, 0.0F);
        return j;
    }

    public int targetMissile(ShipAim aim, Actor actor, float f, boolean flag)
    {
        if( bLogDetail )
            System.out.println("BigShip: Entering 'targetMissile', float f=" + f + ".");
        if(!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0)
            return 0;
        FiringDevice firingdevice = GetFiringDevice(aim);
        boolean flag1 = ((Prey)actor).getShotpointOffset(aim.shotpoint_idx, p1);
        if(!flag1)
            return 0;
        ShipPartProperties shippartproperties = parts[firingdevice.part_idx].pro;
        float f1 = f * Rnd(0.8F, 1.2F);
        if(!MissileAimer.aim((BulletAimer)firingdevice.gun, actor, this, f1, p1, shippartproperties.fireOffset))
            return 0;
        Point3d point3d = new Point3d();
        MissileAimer.getPredictedTargetPosition(point3d);
        Point3d point3d1 = MissileAimer.getHunterFirePoint();
        float f2 = 0.05F;
//        double d = point3d.distance(point3d1);
        double d1 = point3d.z;
        point3d.sub(point3d1);
        point3d.scale(Rnd(0.995D, 1.005D));
        point3d.add(point3d1);
        if(f1 > 0.001F)
        {
            Point3d point3d2 = new Point3d();
            actor.pos.getAbs(point3d2);
            tmpvd.sub(point3d, point3d2);
            double d2 = tmpvd.length();
            if(d2 > 0.001D)
            {
                float f7 = (float)d2 / f1;
                if(f7 > 200F)
                    f7 = 200F;
//                float f8 = f7 * 0.01F;
                point3d2.sub(point3d1);
                double d3 = point3d2.x * point3d2.x + point3d2.y * point3d2.y + point3d2.z * point3d2.z;
                if(d3 > 0.01D)
                {
                    float f9 = (float)tmpvd.dot(point3d2);
                    f9 /= (float)(d2 * Math.sqrt(d3));
                    f9 = (float)Math.sqrt(1.0F - f9 * f9);
//                    f8 *= 0.4F + 0.6F * f9;
                }
//                f8 *= 1.3F;
//                f8 *= ShipAim.AngleErrorKoefForSkill[SKILL_IDX];
            }
        }
        Vector3d vector3d = new Vector3d();
        FireDirection(point3d1, point3d, vector3d);
        if( shippartproperties.LauncherType == 2 ) return 2;
        float f3;
        if(flag)
        {
            f3 = 99999F;
            d1 = 99999F;
        } else
        {
            f3 = shippartproperties.HEAD_MAX_YAW_SPEED;
            d1 = shippartproperties.GUN_MAX_PITCH_SPEED;
        }
        o.add(shippartproperties.fireOrient, pos.getAbs().getOrient());
        if( bLogDetail )
            System.out.println("BigShip: Going 'aim.setRotationForTargetingMissile'");
        int j = aim.setRotationForTargetingMissile(this, o, point3d1, firingdevice.headYaw, firingdevice.gunPitch, vector3d, f2, f1,
                                                   shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG, shippartproperties.GUN_MIN_PITCH, shippartproperties.GUN_MAX_PITCH, f3, (float)d1, 0.0F,
                                                   shippartproperties._HEAD_MIN_YAW, shippartproperties._HEAD_MAX_YAW, shippartproperties._NOFIRE_MAX_YAW, shippartproperties._NOFIRE_MAX_YAW);
        return j;
    }

    public void singleShot(ShipAim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        if(!parts[firingdevice.part_idx].pro.TRACKING_ONLY)
        {
            if(firingdevice.typeOfMissile == 0)
                firingdevice.gun.shots(1);
            else
            {
               if( bLogDetail )
                    System.out.println("BigShip: singleShot() - Fire a missile!");

                String startHookName = parts[firingdevice.part_idx].pro.gunShellStartHookName;
                HierMesh hiermesh = hierMesh();
                Loc loc = new Loc();
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                findHook(hiermesh, startHookName, loc);
                if( bLogDetail )
                {
                    System.out.println("singleShot: startHookName==" + startHookName);
                    System.out.println("singleShot: loc==" + loc);
                }
                point3d = loc.getPoint();
                Orient shipo = pos.getAbs().getOrient();
                double tempx = Math.cos((double)DEG2RAD(shipo.getYaw()))*(double)point3d.x - Math.sin((double)DEG2RAD(shipo.getYaw()))*(double)point3d.y;
                double tempy = Math.sin((double)DEG2RAD(shipo.getYaw()))*(double)point3d.x + Math.cos((double)DEG2RAD(shipo.getYaw()))*(double)point3d.y;
                point3d.set(tempx, tempy, point3d.z);
                if( bLogDetail )
                {
                    System.out.println("singleShot: rotated point3d==" + point3d);
                }
                point3d.add(pos.getAbsPoint());
                orient = loc.getOrient();
                orient.add(pos.getAbs().getOrient());
//                NetChannel netchannel;
//                netchannel = null;

                if( bLogDetail )
                {
                    System.out.println("singleShot: point3d==" + point3d);
                    System.out.println("singleShot: orient==" + orient);
                    System.out.println("singleShot: pos.getAbsPoint()==" + pos.getAbsPoint());
                    System.out.println("singleShot: pos.getAbs().getOrient()==" + pos.getAbs().getOrient());
                }

//                Class classObject2 = null;
//                Rocket missile = null;
//                Constructor classConstructor2 = null;

                Class[] constructorParameterClasses2 = new Class[] {Actor.class, NetChannel.class, Integer.TYPE, Point3d.class, Orient.class, Float.TYPE };
//                constructorParameterClasses2[0]=Actor.class;
//                constructorParameterClasses2[1]=NetChannel.class;
//                constructorParameterClasses2[2]=Integer.TYPE;
//                constructorParameterClasses2[3]=Point3d.class;
//                constructorParameterClasses2[4]=Orient.class;
//                constructorParameterClasses2[5]=Float.TYPE;

                Object[] constructorParameter2 = new Object[] { this, null, new Integer(1), point3d, orient, new Float(100F) };
//                constructorParameter2[0] = this;
//                constructorParameter2[1] = null;
//                constructorParameter2[2] = new Integer(1);
//                constructorParameter2[3] = point3d;
//                constructorParameter2[4] = orient;
//                constructorParameter2[5] = new Float(100F);

                try{
//                    classObject2 = Class.forName(firingdevice.missileName);
//                    classConstructor2 = classObject2.getConstructor(constructorParameterClasses2);
//                    missile = (Rocket)classConstructor2.newInstance(constructorParameter2);
                    Class.forName(firingdevice.missileName).getConstructor(constructorParameterClasses2).newInstance(constructorParameter2);
                }catch(Exception exception){
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    System.out.println("SPAWN: Can't create missile object [class:" + firingdevice.missileName + "]");
                    return;
                }
            }
        }
    }

    public void startFire(ShipAim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        if(!parts[firingdevice.part_idx].pro.TRACKING_ONLY)
        {
            if(firingdevice.typeOfMissile == 0)
                firingdevice.gun.shots(-1);
            else
            {
               if( bLogDetail )
                    System.out.println("BigShip: startFire() - Fire a missile!");

                String startHookName = parts[firingdevice.part_idx].pro.gunShellStartHookName;
                HierMesh hiermesh = hierMesh();
                Loc loc = new Loc();
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                findHook(hiermesh, startHookName, loc);
                if( bLogDetail )
                {
                    System.out.println("startFire: startHookName==" + startHookName);
                    System.out.println("startFire: loc==" + loc);
                }
                point3d = loc.getPoint();
                Orient shipo = pos.getAbs().getOrient();
                double tempx = Math.cos((double)DEG2RAD(shipo.getYaw()))*(double)point3d.x - Math.sin((double)DEG2RAD(shipo.getYaw()))*(double)point3d.y;
                double tempy = Math.sin((double)DEG2RAD(shipo.getYaw()))*(double)point3d.x + Math.cos((double)DEG2RAD(shipo.getYaw()))*(double)point3d.y;
                point3d.set(tempx, tempy, point3d.z);
                if( bLogDetail )
                {
                    System.out.println("startFire: rotated point3d==" + point3d);
                }
                point3d.add(pos.getAbsPoint());
                orient = loc.getOrient();
                orient.add(pos.getAbs().getOrient());
//                NetChannel netchannel;
//                netchannel = null;

                if( bLogDetail )
                {
                    System.out.println("startFire: point3d==" + point3d);
                    System.out.println("startFire: orient==" + orient);
                    System.out.println("startFire: pos.getAbsPoint()==" + pos.getAbsPoint());
                    System.out.println("startFire: pos.getAbs().getOrient()==" + pos.getAbs().getOrient());
                }

//                Class classObject2 = null;
//                Rocket missile = null;
//                Constructor classConstructor2 = null;

                Class[] constructorParameterClasses2 = new Class[] {Actor.class, NetChannel.class, Integer.TYPE, Point3d.class, Orient.class, Float.TYPE };
//                constructorParameterClasses2[0]=Actor.class;
//                constructorParameterClasses2[1]=NetChannel.class;
//                constructorParameterClasses2[2]=Integer.TYPE;
//                constructorParameterClasses2[3]=Point3d.class;
//                constructorParameterClasses2[4]=Orient.class;
//                constructorParameterClasses2[5]=Float.TYPE;

                Object[] constructorParameter2 = new Object[] { this, null, new Integer(1), point3d, orient, new Float(100F) };
//                constructorParameter2[0] = this;
//                constructorParameter2[1] = null;
//                constructorParameter2[2] = new Integer(1);
//                constructorParameter2[3] = point3d;
//                constructorParameter2[4] = orient;
//                constructorParameter2[5] = new Float(100F);

                try{
//                    classObject2 = Class.forName(firingdevice.missileName);
//                    classConstructor2 = classObject2.getConstructor(constructorParameterClasses2);
//                    missile = (Rocket)classConstructor2.newInstance(constructorParameter2);
                    Class.forName(firingdevice.missileName).getConstructor(constructorParameterClasses2).newInstance(constructorParameter2);
                }catch(Exception exception){
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    System.out.println("SPAWN: Can't create missile object [class:" + firingdevice.missileName + "]");
                    return;
                }
            }
        }
    }

    public void continueFire(ShipAim aim)
    {
    }

    public void stopFire(ShipAim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        if(!parts[firingdevice.part_idx].pro.TRACKING_ONLY)
            if(firingdevice.typeOfMissile == 0)
                firingdevice.gun.shots(0);
    }

    protected static float DEG2RAD(float f)
    {
        return f * 0.01745329F;
    }

    private boolean findHook(HierMesh hiermesh, String s, Loc loc1)
    {
        int i = hiermesh.hookFind(s);
        if(i == -1)
        {
            return false;
        } else
        {
            double tmp[] = new double[3];
            Point3d p = new Point3d();
            Orient o = new Orient();
            Matrix4d m1 = new Matrix4d();
            hiermesh.hookMatrix(i, m1);
            m1.getEulers(tmp);
            o.setYPR(Geom.RAD2DEG((float)tmp[0]), 360F - Geom.RAD2DEG((float)tmp[1]), 360F - Geom.RAD2DEG((float)tmp[2]));
            p.set(m1.m03, m1.m13, m1.m23);
            loc1.set(p, o);
            return true;
        }
     }

    public int targetControl()
    {
        return hasTracking;
    }

    public boolean isVisibilityLong()
    {
        return true;
    }

    private void createAirport()
    {
        if(prop.propAirport != null)
        {
            prop.propAirport.firstInit(this);
            draw = new TowStringMeshDraw(draw);
            if(prop.propAirport.cellTO != null)
                cellTO = (CellAirField)prop.propAirport.cellTO.getClone();
            if(prop.propAirport.cellLDG != null)
                cellLDG = (CellAirField)prop.propAirport.cellLDG.getClone();
            airport = new AirportCarrier(this, prop.propAirport.rwy);
        }
    }

    public AirportCarrier getAirport()
    {
        return airport;
    }

    public CellAirField getCellTO()
    {
        return cellTO;
    }

    public CellAirField getCellLDR()
    {
        return cellLDG;
    }

    private void validateTowAircraft()
    {
        if(towPortNum < 0)
            return;
        if(!Actor.isValid(towAircraft))
        {
            requestDetowAircraft(towAircraft);
            return;
        }
        if(pos.getAbsPoint().distance(towAircraft.pos.getAbsPoint()) > (double)hierMesh().visibilityR())
        {
            requestDetowAircraft(towAircraft);
            return;
        }
        if(!towAircraft.FM.CT.bHasArrestorControl)
        {
            requestDetowAircraft(towAircraft);
            return;
        } else
        {
            return;
        }
    }

    public void forceTowAircraft(Aircraft aircraft, int i)
    {
        if(towPortNum >= 0)
        {
            return;
        } else
        {
            towPortNum = i;
            towAircraft = aircraft;
            towHook = new HookNamed(aircraft, "_ClipAGear");
            return;
        }
    }

    public void requestTowAircraft(Aircraft aircraft)
    {
        if(towPortNum >= 0 || prop.propAirport.towPRel == null)
            return;
        HookNamed hooknamed = new HookNamed(aircraft, "_ClipAGear");
        Point3d apoint3d[] = prop.propAirport.towPRel;
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Point3d point3d2 = new Point3d();
        Point3d point3d3 = new Point3d();
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        for(int i = 0; i < apoint3d.length / 2; i++)
        {
            pos.getCurrent(loc);
            point3d2.set(apoint3d[i + i]);
            loc.transform(point3d2);
            point3d3.set(apoint3d[i + i + 1]);
            loc.transform(point3d3);
            aircraft.pos.getCurrent(loc1);
            loc.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(aircraft, loc1, loc);
            point3d.set(loc.getPoint());
            aircraft.pos.getPrev(loc1);
            loc.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(aircraft, loc1, loc);
            point3d1.set(loc.getPoint());
            if(point3d1.z >= point3d2.z + 0.5D * (point3d3.z - point3d2.z) + 0.2D)
                continue;
            Line2d line2d = new Line2d(new Point2d(point3d2.x, point3d2.y), new Point2d(point3d3.x, point3d3.y));
            Line2d line2d1 = new Line2d(new Point2d(point3d.x, point3d.y), new Point2d(point3d1.x, point3d1.y));
            try
            {
                Point2d point2d = line2d.crossPRE(line2d1);
                double d = Math.min(point3d2.x, point3d3.x);
                double d1 = Math.max(point3d2.x, point3d3.x);
                double d2 = Math.min(point3d2.y, point3d3.y);
                double d3 = Math.max(point3d2.y, point3d3.y);
                if(point2d.x <= d || point2d.x >= d1 || point2d.y <= d2 || point2d.y >= d3)
                    continue;
                d = Math.min(point3d.x, point3d1.x);
                d1 = Math.max(point3d.x, point3d1.x);
                d2 = Math.min(point3d.y, point3d1.y);
                d3 = Math.max(point3d.y, point3d1.y);
                if(point2d.x > d && point2d.x < d1 && point2d.y > d2 && point2d.y < d3)
                {
                    towPortNum = i;
                    towAircraft = aircraft;
                    towHook = new HookNamed(aircraft, "_ClipAGear");
                    return;
                }
            }
            catch(Exception exception) { }
        }

    }

    public void requestDetowAircraft(Aircraft aircraft)
    {
        if(aircraft == towAircraft)
        {
            towAircraft = null;
            towPortNum = -1;
        }
    }

    public boolean isTowAircraft(Aircraft aircraft)
    {
        return towAircraft == aircraft;
    }

    public double getSpeed(Vector3d vector3d)
    {
        if(path == null)
            return super.getSpeed(vector3d);
        long l = NetServerParams.getServerTime();
        if(l > (long)(Time.tickLen() * 4))
            return super.getSpeed(vector3d);
        Segment segment = (Segment)path.get(0);
        tmpDir.sub(segment.posOut, segment.posIn);
        tmpDir.normalize();
        tmpDir.scale(segment.speedIn);
        if(vector3d != null)
            vector3d.set(tmpDir);
        return tmpDir.length();
    }

    private boolean FireDirection(Point3d point3d, Point3d point3d1, Vector3d vector3d) {
        float f = (float)point3d.distance(point3d1);
        vector3d.set(point3d1);
        vector3d.sub(point3d);
        vector3d.scale(1.0F / f);
        return true;
    }

    private void zutiRefreshBornPlace()
    {
        if(zutiBornPlace == null || zutiIsClassBussy)
            return;
        zutiIsClassBussy = true;
        if(dying == 0)
        {
            Point3d point3d = pos.getAbsPoint();
            zutiBornPlace.place.set(point3d.x, point3d.y);
            if(zutiBornPlace.zutiBpStayPoints != null)
            {
                for(int i = 0; i < zutiBornPlace.zutiBpStayPoints.size(); i++)
                {
                    ZutiStayPoint zutistaypoint = (ZutiStayPoint)zutiBornPlace.zutiBpStayPoints.get(i);
                    zutistaypoint.PsVsShipRefresh(point3d.x, point3d.y, initOr.getYaw());
                }

            }
        } else
        if(dying > 0)
        {
            ZutiSupportMethods.removeBornPlace(zutiBornPlace);
            zutiBornPlace = null;
        }
        zutiIsClassBussy = false;
    }

    private void zutiAssignStayPointsToBp()
    {
        double d;
        double d1;
        ArrayList arraylist;
        int j;
//        String s;
//        int l;
        if(zutiBornPlace == null)
            return;
        d = pos.getAbsPoint().x;
        d1 = pos.getAbsPoint().y;
        zutiBornPlace.zutiBpStayPoints = new ArrayList();
        double d2 = 22500D;
        Point_Stay apoint_stay[][] = World.cur().airdrome.stay;
        arraylist = new ArrayList();
        for(int i = 0; i < apoint_stay.length; i++)
        {
            if(apoint_stay[i] == null)
                continue;
            Point_Stay point_stay = apoint_stay[i][apoint_stay[i].length - 1];
            double d3 = ((double)point_stay.x - d) * ((double)point_stay.x - d) + ((double)point_stay.y - d1) * ((double)point_stay.y - d1);
            if(d3 <= d2)
                arraylist.add(point_stay);
        }

        j = arraylist.size();
        if(j < 0)
            return;
        for(int k = 0; k < j; k++)
        {
            ZutiStayPoint zutistaypoint;
            zutistaypoint = new ZutiStayPoint();
            zutistaypoint.pointStay = (Point_Stay)arraylist.get(k);
            if(zutiBornPlace == null)
                return;
            try
            {
                zutiBornPlace.zutiBpStayPoints.add(zutistaypoint);
                continue; /* Loop/switch isn't completed */
            }
            catch(Exception exception)
            {
                System.out.println("BigshipGeneric zutiAssignStayPointsToBp error: " + exception.toString());
                exception.printStackTrace();
                // TODO: Fixed by SAS~Storebror: Ridiculous loop error fix +++ 
                //k++;
                // TODO: Fixed by SAS~Storebror: Ridiculous loop error fix ---
            }
        }
    }

    public void zutiAssignBornPlace(boolean flag)
    {
        double d = pos.getAbsPoint().x;
        double d1 = pos.getAbsPoint().y;
        double d2 = 1000000D;
        BornPlace bornplace = null;
        ArrayList arraylist = World.cur().bornPlaces;
        if(arraylist == null)
            return;
        for(int i = 0; i < arraylist.size(); i++)
        {
            BornPlace bornplace1 = (BornPlace)arraylist.get(i);
            if(bornplace1.zutiAlreadyAssigned)
                continue;
            double d3 = Math.sqrt(Math.pow(bornplace1.place.x - d, 2D) + Math.pow(bornplace1.place.y - d1, 2D));
            if(d3 < d2 && bornplace1.army == getArmy())
            {
                d2 = d3;
                bornplace = bornplace1;
            }
        }

        if(d2 < 1000D)
        {
            zutiBornPlace = bornplace;
            bornplace.zutiAlreadyAssigned = true;
            airport.setCustomStayPoints();
            if(!flag)
                zutiAssignStayPointsToBp();
        }
    }

    public int zutiGetDying()
    {
        return dying;
    }

    public boolean zutiIsStatic()
    {
        return path == null || path.size() <= 0;
    }

    public void showTransparentRunwayRed()
    {
        hierMesh().chunkVisible("Red", true);
    }

    public void hideTransparentRunwayRed()
    {
        hierMesh().chunkVisible("Red", false);
    }

    private float filter(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if(f6 < f1)
        {
            f6 += f4 * f;
        if(f6 > f1)
            f6 = f1;
        } else
        if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
    return f6;
    }

    // By western, Jet Blast Deflector
    public void setBlastDeflector(int num, int i)
    {
        if(!bHasBlastDeflectorControl)
            return;
        if(BlastDeflectorControl[num] == (float)i)
            return;

        doSetBlastDeflector(num, i);
 if( bLogDetailBD )
   System.out.println("BigShip: 5223 - setBlastDeflector(int num=" + Integer.toString(num) + ", int i ="  + Integer.toString(i) + ")");
        return;
    }

    private void doSetBlastDeflector(int num, int i)
    {
        BlastDeflectorControl[num] = i;
    }

    public float getBlastDeflector(int num)
    {
        return blastDeflector[num];
    }

    public void forceBlastDeflector(int num, float f)
    {
        blastDeflector[num] = f;
    }

    public void moveBlastDeflector(int num, float f)
    {
    }
    // End of - Jet Blast Deflector

    // by western, Visual Landing Aid
    public float getVlaGlidePath(Aircraft aircraft1)
    {
        Loc ldghookloc = new Loc();
        Loc tmpLoc = new Loc();
		HierMesh carrierhiermesh = this.hierMesh();
        findHook(carrierhiermesh, "_RWY_LDG", ldghookloc);
        tmpLoc.set(ldghookloc);
        tmpLoc.add(initLoc);
        double d = tmpLoc.getZ() + 3D;
        double d1 = Math.abs(aircraft1.pos.getAbsPoint().x - tmpLoc.getX());
        double d2 = Math.abs(aircraft1.pos.getAbsPoint().y - tmpLoc.getY());
        float f1 = 10F;
        distanceVla = (float)Math.sqrt(d1 * d1 + d2 * d2);
        double d3 = (double)(distanceVla - f1);
        double d4 = aircraft1.pos.getAbsPoint().z - d;
        if(d3 < d4)
            return 90F;
        double d5 = Math.asin(d4 / d3);
        double d6 = glideScopeInRads - d5;
        float f2 = (float)Math.toDegrees(d6);
        return f2;
    }

    public float getVlaAzimuthBP(Aircraft aircraft1)
    {
        Loc ldghookloc = new Loc();
        Loc tmpLoc = new Loc();
		HierMesh carrierhiermesh = this.hierMesh();
        findHook(carrierhiermesh, "_RWY_LDG", ldghookloc);
        tmpLoc.set(ldghookloc);
        tmpLoc.add(initLoc);
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();
        aircraft1.pos.getAbs(p2);
        p1 = tmpLoc.getPoint();
        p1.sub(p2);
        double d = Math.sqrt(p1.x * p1.x + p1.y * p1.y);
//        double d1 = (double) aircraft1.FM.getAltitude() - tmpLoc.getZ() - 2D;
//        float f = getConeOfSilence(d, d1);
        if (d > 35000D) {
            return 0F;
        }
//        float f1 = f;
//        f1 *= Aircraft.cvt((float) d, 0.0F, 35000F, 1.0F, 0.0F);
        float f2 = 57.32484F * (float) Math.atan2(p1.x, p1.y);
		f2 -= tmpLoc.getAzimut();
        for (f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F)
            ;
        for (; f2 >= 360F; f2 -= 360F)
            ;
        float f3 = f2 - 90F;
//        boolean flag = true;
//        boolean flag1 = false;
        if (f3 > 90F) {
            f3 = -(f3 - 180F);
//            flag = false;
        }
//        float f4 = 0.0F;
//        if (!flag) f4 = f3 * -18F;
//        else f4 = f3 * 18F;

        return f3;
    }

    public float getVlaAzimuthPB(Aircraft aircraft1)
    {
        Loc ldghookloc = new Loc();
        Loc tmpLoc = new Loc();
		HierMesh carrierhiermesh = this.hierMesh();
        findHook(carrierhiermesh, "_RWY_LDG", ldghookloc);
        tmpLoc.set(ldghookloc);
        tmpLoc.add(initLoc);
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();
        aircraft1.pos.getAbs(p2);
        p1 = tmpLoc.getPoint();
        p1.sub(p2);
        double d = Math.sqrt(p1.x * p1.x + p1.y * p1.y);
        double d1 = (double) aircraft1.FM.getAltitude() - tmpLoc.getZ() - 2D;
        float f = getConeOfSilence(d, d1);
        if (d > 35000D) {
            return 0F;
        }
//        float f1 = f;
//        f1 *= Aircraft.cvt((float) d, 0.0F, 35000F, 1.0F, 0.0F);
        float f2 = 57.32484F * (float) Math.atan2(p1.x, p1.y);
		f2 -= tmpLoc.getAzimut();
        for (f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F)
            ;
        for (; f2 >= 360F; f2 -= 360F)
            ;
        float f3 = f2 - 90F;
        boolean flag = true;
//        boolean flag1 = false;
        if (f3 > 90F) {
            f3 = -(f3 - 180F);
            flag = false;
        }
        float f4 = 0.0F;
        if (!flag) f4 = f3 * -18F;
        else f4 = f3 * 18F;

        return f4 * f;
    }

    private float getConeOfSilence(double d, double d1) {
        float f = 57.32484F * (float) Math.atan2(d, d1);
        return Aircraft.cvt(f, 20F, 40F, 0.0F, 1.0F);
    }

    public Aircraft getNearestAircraftFront()
    {
        List list = Engine.targets();
        int i = list.size();
        double oldnearestdist = 12001D;
        int oldnearestindex = -1;
		HierMesh carrierhiermesh = this.hierMesh();
        Loc ldghookloc = new Loc();
        Loc tmpLoc = new Loc();
        findHook(carrierhiermesh, "_RWY_LDG", ldghookloc);
        tmpLoc.set(ldghookloc);
        tmpLoc.add(initLoc);

        for (int j = 0; j < i; j++)
        {
            Actor target = (Actor) list.get(j);
            if (!(target instanceof Aircraft)) continue;
            Aircraft aircraft1 = (Aircraft) target;
            Point3d point3d1 = aircraft1.pos.getAbsPoint();
            Point3d point3d2 = tmpLoc.getPoint();
            double d1 = Math.sqrt((point3d2.x - point3d1.x) * (point3d2.x - point3d1.x) + (point3d2.y - point3d1.y) * (point3d2.y - point3d1.y));
            if (d1 >= 12000D) continue;
            if (d1 >= oldnearestdist) continue;
            if (aircraft1.FM.Gears.onGround() || aircraft1.FM.isCrashedOnGround()) continue;
            if (getVlaGlidePath(aircraft1) > 89.8F || Math.abs(getVlaAzimuthBP(aircraft1)) > 40F
                || Math.abs(getVlaAzimuthPB(aircraft1)) > 90F)
                insideOfGlidepath = false;
            else
                insideOfGlidepath = true;

            oldnearestdist = d1;
            oldnearestindex = j;
        }

        if (oldnearestindex == -1)
        {
            distanceVla = 1000F;
            return null;
        }
        distanceVla = (float)oldnearestdist;
        return (Aircraft) list.get(oldnearestindex);
    }


    private void InitMirrorLandingAid()
    {
        bInitDoneMirrorLA = true;
        Meatballxyz[0] = Meatballxyz[1] = Meatballxyz[2] = 0.0F;
        Meatballypr[0] = Meatballypr[1] = Meatballypr[2] = 0.0F;
        if(!Config.isUSE_RENDER())
            return;
        MlaDatumLightOn();
        MlaMeatballOn();
        MlaMeatballCenter();
    }

    private void InitUSIflols()
    {
        bInitDoneUSIflols = true;
        Meatballxyz[0] = Meatballxyz[1] = Meatballxyz[2] = 0.0F;
        Meatballypr[0] = Meatballypr[1] = Meatballypr[2] = 0.0F;
        if(!Config.isUSE_RENDER())
            return;
        MlaDatumLightOn();
        USIflolsMeatballOn();
        USIflolsMeatballCenter();
    }

    private void InitFRFlols()
    {
        bInitDoneFRFlols = true;
        Meatballxyz[0] = Meatballxyz[1] = Meatballxyz[2] = 0.0F;
        Meatballypr[0] = Meatballypr[1] = Meatballypr[2] = 0.0F;
        if(!Config.isUSE_RENDER())
            return;
        FRFlolsDatumLightOn();
        USIflolsMeatballOn();
        USIflolsMeatballCenter();
    }

//    private void MlaDatumLightOff()
//    {
//        for(int i=0; i<12; i++)
//            Eff3DActor.finish(effdatum[i]);
//    }

    private void MlaDatumLightOn()
    {
        effdatum[0] = Eff3DActor.New(this, findHook("_datum01"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[1] = Eff3DActor.New(this, findHook("_datum02"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[2] = Eff3DActor.New(this, findHook("_datum03"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[3] = Eff3DActor.New(this, findHook("_datum04"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[4] = Eff3DActor.New(this, findHook("_datum05"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[5] = Eff3DActor.New(this, findHook("_datum06"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[6] = Eff3DActor.New(this, findHook("_datum07"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[7] = Eff3DActor.New(this, findHook("_datum08"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[8] = Eff3DActor.New(this, findHook("_datum09"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[9] = Eff3DActor.New(this, findHook("_datum10"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[10] = Eff3DActor.New(this, findHook("_datum11"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[11] = Eff3DActor.New(this, findHook("_datum12"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
    }

    private void MlaCutLightOff()
    {
        if(cutlighton)
            for(int i=0; i<4; i++)
                Eff3DActor.finish(effcut[i]);
    }

    private void MlaCutLightOn()
    {
        effcut[0] = Eff3DActor.New(this, findHook("_cut01"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effcut[1] = Eff3DActor.New(this, findHook("_cut02"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effcut[2] = Eff3DActor.New(this, findHook("_cut03"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effcut[3] = Eff3DActor.New(this, findHook("_cut04"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
    }

    private void MlaWaveoffOff()
    {
        if(waveofflighton)
            for(int i=0; i<4; i++)
                Eff3DActor.finish(effwaveoff[i]);
    }

    private void MlaWaveoffOn()
    {
        effwaveoff[0] = Eff3DActor.New(this, findHook("_waveoffL01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[1] = Eff3DActor.New(this, findHook("_waveoffL02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[2] = Eff3DActor.New(this, findHook("_waveoffR01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[3] = Eff3DActor.New(this, findHook("_waveoffR02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

//    private void MlaEmergencyWaveoffOff()
//    {
//        for(int i=0; i<2; i++)
//            Eff3DActor.finish(effemgwaveoff[i]);
//    }
//
//    private void MlaEmergencyWaveoffOn()
//    {
//        effemgwaveoff[0] = Eff3DActor.New(this, findHook("_emgwaveoffL"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
//        effemgwaveoff[1] = Eff3DActor.New(this, findHook("_emgwaveoffR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
//    }
//
//    private void MlaReserve1Off()
//    {
//        for(int i=0; i<2; i++)
//            Eff3DActor.finish(effreserve1[i]);
//    }
//
//    private void MlaReserve1On()
//    {
//        effreserve1[0] = Eff3DActor.New(this, findHook("_reserveL01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
//        effreserve1[1] = Eff3DActor.New(this, findHook("_reserveR01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
//    }
//
//    private void MlaReserve2Off()
//    {
//        for(int i=0; i<2; i++)
//            Eff3DActor.finish(effreserve2[i]);
//    }
//
//    private void MlaReserve2On()
//    {
//        effreserve2[0] = Eff3DActor.New(this, findHook("_reserveL02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
//        effreserve2[1] = Eff3DActor.New(this, findHook("_reserveR02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
//    }
//
//    private void FRFlolsDatumLightOff()
//    {
//        for(int i=0; i<14; i++)
//            Eff3DActor.finish(effdatum[i]);
//    }

    private void FRFlolsDatumLightOn()
    {
        effdatum[0] = Eff3DActor.New(this, findHook("_datum01"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[1] = Eff3DActor.New(this, findHook("_datum02"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[2] = Eff3DActor.New(this, findHook("_datum03"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[3] = Eff3DActor.New(this, findHook("_datum04"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[4] = Eff3DActor.New(this, findHook("_datum05"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[5] = Eff3DActor.New(this, findHook("_datum06"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[6] = Eff3DActor.New(this, findHook("_datum07"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[7] = Eff3DActor.New(this, findHook("_datum08"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[8] = Eff3DActor.New(this, findHook("_datum09"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[9] = Eff3DActor.New(this, findHook("_datum10"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[10] = Eff3DActor.New(this, findHook("_datum11"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[11] = Eff3DActor.New(this, findHook("_datum12"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[12] = Eff3DActor.New(this, findHook("_datum13"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[13] = Eff3DActor.New(this, findHook("_datum14"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
    }

    private void FRFlolsCutLightOff()
    {
        if(cutlighton)
            for(int i=0; i<2; i++)
                Eff3DActor.finish(effcut[i]);
    }

    private void FRFlolsCutLightOn()
    {
        effcut[0] = Eff3DActor.New(this, findHook("_cut01"), null, 0.7F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effcut[1] = Eff3DActor.New(this, findHook("_cut02"), null, 0.7F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    private void FRFlolsWaveoffOff()
    {
        if(waveofflighton)
            for(int i=0; i<4; i++)
                Eff3DActor.finish(effwaveoff[i]);
    }

    private void FRFlolsWaveoffOn()
    {
        effwaveoff[0] = Eff3DActor.New(this, findHook("_waveoffL01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[1] = Eff3DActor.New(this, findHook("_waveoffL02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[2] = Eff3DActor.New(this, findHook("_waveoffR01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[3] = Eff3DActor.New(this, findHook("_waveoffR02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

//    private void FRFlolsEmergencyWaveoffOff()
//    {
//        if(waveofflighton)
//            for(int i=0; i<2; i++)
//                Eff3DActor.finish(effemgwaveoff[i]);
//    }
//
//    private void FRFlolsEmergencyWaveoffOn()
//    {
//        effemgwaveoff[0] = Eff3DActor.New(this, findHook("_emgwaveoffL"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
//        effemgwaveoff[1] = Eff3DActor.New(this, findHook("_emgwaveoffR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
//    }

    private void MlaMeatballOff()
    {
        hierMesh().chunkVisible("Meatball", false);
        Eff3DActor.finish(effmeatball[0]);
    }

    private void MlaMeatballOn()
    {
        hierMesh().chunkVisible("Meatball", true);
        effmeatball[0] = Eff3DActor.New(this, findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
    }

    private void MlaMeatballCenter()
    {
        Meatballxyz[0] = Meatballxyz[1] = Meatballxyz[2] = 0.0F;
        hierMesh().chunkSetLocate("Meatball", Meatballxyz, Meatballypr);
        Eff3DActor.finish(effmeatball[0]);
        effmeatball[0] = Eff3DActor.New(this, findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
    }

    private void MlaMeatballSet(float horiz, float vert)
    {
        Meatballxyz[0] = 0.0F;
        Meatballxyz[1] = horiz * -0.43F;
        Meatballxyz[2] = vert * -0.43F;
        hierMesh().chunkSetLocate("Meatball", Meatballxyz, Meatballypr);
        if(Time.current() % 100F == 0F)
        {
            Eff3DActor.finish(effmeatball[0]);
            effmeatball[0] = Eff3DActor.New(this, findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
        }
    }

    private void IflolsMeatballOff()
    {
        hierMesh().chunkVisible("meatball_red", false);
        hierMesh().chunkVisible("meatball_yellow", false);
        Eff3DActor.finish(effmeatball[0]);
        Eff3DActor.finish(effmeatball[1]);
    }

    private void USIflolsMeatballOn()
    {
        switch(meatballpos)
        {
              case 0:
              case 1:
                  hierMesh().chunkVisible("meatball_red", true);
                  effmeatball[0] = Eff3DActor.New(this, findHook("_meatball_r"), null, 0.8F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                  break;

              case 2:
              case 3:
              case 4:
              case 5:
              case 6:
              case 7:
              case 8:
              case 9:
              case 10:
              case 11:
                  hierMesh().chunkVisible("meatball_yellow", true);
                  effmeatball[1] = Eff3DActor.New(this, findHook("_meatball_y"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                  break;
              default:
                  break;
        }
    }

    private void FRFlolsMeatballOn()
    {
        switch(meatballpos)
        {
              case 0:
              case 1:
                  hierMesh().chunkVisible("meatball_red", true);
                  effmeatball[0] = Eff3DActor.New(this, findHook("_meatball_r"), null, 0.8F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                  break;

              case 2:
              case 3:
              case 4:
              case 5:
                  hierMesh().chunkVisible("meatball_yellow", true);
                  effmeatball[1] = Eff3DActor.New(this, findHook("_meatball_y"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                  break;
              default:
                  break;
        }
    }

    private void USIflolsMeatballCenter()
    {
        USIflolsMeatballSet(0.0F);
    }

    private void FRFlolsMeatballCenter()
    {
        FRFlolsMeatballSet(0.0F);
    }

    private void USIflolsMeatballSet(float vert)
    {
        int newmeatballpos = (int)Math.floor(Aircraft.cvt(-vert, -1.0F, 1.0F, 0F, 11F));
        if(newmeatballpos == meatballpos)  return;
        Meatballxyz[0] = 0.0F;
        Meatballxyz[1] = 0.0F;
        Meatballxyz[2] = newmeatballpos * 0.146624F;
        hierMesh().chunkSetLocate("meatball_red", Meatballxyz, Meatballypr);
        hierMesh().chunkSetLocate("meatball_yellow", Meatballxyz, Meatballypr);
        switch(newmeatballpos)
        {
              case 0:
              case 1:
                  if(meatballpos > 1)
                  {
                      Eff3DActor.finish(effmeatball[1]);
                      hierMesh().chunkVisible("meatball_yellow", false);
                      hierMesh().chunkVisible("meatball_red", true);
                  }
                  else
                  {
                      Eff3DActor.finish(effmeatball[0]);
                  }
                  effmeatball[0] = Eff3DActor.New(this, findHook("_meatball_r"), null, 0.8F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                  break;

              case 2:
              case 3:
              case 4:
              case 5:
              case 6:
              case 7:
              case 8:
              case 9:
              case 10:
              case 11:
                  if(meatballpos > 1)
                  {
                      Eff3DActor.finish(effmeatball[1]);
                  }
                  else
                  {
                      Eff3DActor.finish(effmeatball[0]);
                      hierMesh().chunkVisible("meatball_yellow", true);
                      hierMesh().chunkVisible("meatball_red", false);
                  }
                  effmeatball[1] = Eff3DActor.New(this, findHook("_meatball_y"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                  break;
              default:
                  break;
        }
        meatballpos = newmeatballpos;
    }

    private void FRFlolsMeatballSet(float vert)
    {
        int newmeatballpos = (int)Math.floor(Aircraft.cvt(-vert, -1.0F, 1.0F, 0F, 5F));
        if(newmeatballpos == meatballpos)  return;
        Meatballxyz[0] = 0.0F;
        Meatballxyz[1] = 0.0F;
        Meatballxyz[2] = newmeatballpos * 0.40F;
        hierMesh().chunkSetLocate("meatball_red", Meatballxyz, Meatballypr);
        hierMesh().chunkSetLocate("meatball_yellow", Meatballxyz, Meatballypr);
        switch(newmeatballpos)
        {
              case 0:
              case 1:
                  if(meatballpos > 1)
                  {
                      Eff3DActor.finish(effmeatball[1]);
                      hierMesh().chunkVisible("meatball_yellow", false);
                      hierMesh().chunkVisible("meatball_red", true);
                  }
                  else
                  {
                      Eff3DActor.finish(effmeatball[0]);
                  }
                  effmeatball[0] = Eff3DActor.New(this, findHook("_meatball_r"), null, 0.8F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                  break;

              case 2:
              case 3:
              case 4:
              case 5:
                  if(meatballpos > 1)
                  {
                      Eff3DActor.finish(effmeatball[1]);
                  }
                  else
                  {
                      Eff3DActor.finish(effmeatball[0]);
                      hierMesh().chunkVisible("meatball_yellow", true);
                      hierMesh().chunkVisible("meatball_red", false);
                  }
                  effmeatball[1] = Eff3DActor.New(this, findHook("_meatball_y"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                  break;
              default:
                  break;
        }
        meatballpos = newmeatballpos;
    }

    public void MlaSetVisible(boolean flag)
    {
        if(flag)
        {
            MlaMeatballOn();
            for(int i=0; i<12; i++)
                effdatum[i]._setIntesity(0.4F);
            for(int i=0; i<4; i++)
                effcut[i]._setIntesity(1.0F);
            for(int i=0; i<4; i++)
                effwaveoff[i]._setIntesity(1.0F);
            for(int i=0; i<2; i++)
                effemgwaveoff[i]._setIntesity(1.0F);
//            for(int i=0; i<2; i++)
//                effreserve1[i]._setIntesity(1.0F);
//            for(int i=0; i<2; i++)
//                effreserve2[i]._setIntesity(1.0F);
        }
        else
        {
            MlaMeatballOff();
            for(int i=0; i<12; i++)
                effdatum[i]._setIntesity(0.0F);
            if(cutlighton)
                for(int i=0; i<4; i++)
                    effcut[i]._setIntesity(0.0F);
            cutlighton = false;
            if(waveofflighton)
                for(int i=0; i<4; i++)
                    effwaveoff[i]._setIntesity(0.0F);
            waveofflighton = false;
            if(emgwaveofflighton)
                for(int i=0; i<2; i++)
                    effemgwaveoff[i]._setIntesity(0.0F);
            emgwaveofflighton = false;
//            for(int i=0; i<2; i++)
//                effreserve1[i]._setIntesity(0.0F);
//            for(int i=0; i<2; i++)
//                effreserve2[i]._setIntesity(0.0F);
        }
    }

    public void USIflolsSetVisible(boolean flag)
    {
        if(flag)
        {
            USIflolsMeatballOn();
            for(int i=0; i<12; i++)
                effdatum[i]._setIntesity(0.4F);
            for(int i=0; i<4; i++)
                effcut[i]._setIntesity(1.0F);
            for(int i=0; i<4; i++)
                effwaveoff[i]._setIntesity(1.0F);
            for(int i=0; i<2; i++)
                effemgwaveoff[i]._setIntesity(1.0F);
        }
        else
        {
            IflolsMeatballOff();
            for(int i=0; i<12; i++)
                effdatum[i]._setIntesity(0.0F);
            if(cutlighton)
                for(int i=0; i<4; i++)
                    effcut[i]._setIntesity(0.0F);
            cutlighton = false;
            if(waveofflighton)
                for(int i=0; i<4; i++)
                    effwaveoff[i]._setIntesity(0.0F);
            waveofflighton = false;
            if(emgwaveofflighton)
                for(int i=0; i<2; i++)
                    effemgwaveoff[i]._setIntesity(0.0F);
            emgwaveofflighton = false;
        }
    }

    public void FRFlolsSetVisible(boolean flag)
    {
        if(flag)
        {
            FRFlolsMeatballOn();
            for(int i=0; i<NETSEND_MAX_NUMITEMS_DMG; i++)
                effdatum[i]._setIntesity(0.4F);
            for(int i=0; i<2; i++)
                effcut[i]._setIntesity(1.0F);
            for(int i=0; i<4; i++)
                effwaveoff[i]._setIntesity(1.0F);
            for(int i=0; i<2; i++)
                effemgwaveoff[i]._setIntesity(1.0F);
        }
        else
        {
            IflolsMeatballOff();
            for(int i=0; i<NETSEND_MAX_NUMITEMS_DMG; i++)
                effdatum[i]._setIntesity(0.0F);
            if(cutlighton)
                for(int i=0; i<2; i++)
                    effcut[i]._setIntesity(0.0F);
            cutlighton = false;
            if(waveofflighton)
                for(int i=0; i<4; i++)
                    effwaveoff[i]._setIntesity(0.0F);
            waveofflighton = false;
            if(emgwaveofflighton)
                for(int i=0; i<2; i++)
                    effemgwaveoff[i]._setIntesity(0.0F);
            emgwaveofflighton = false;
        }
    }


    private boolean MlaUpdate()
    {
        Aircraft aircraft1 = getNearestAircraftFront();
        if(aircraft1 == null || (!insideOfGlidepath && distanceVla > 100F))
        {
            if(meatballmoving)
            {
                MlaMeatballCenter();
                MlaCutLightOff();
                cutlighton = false;
                MlaWaveoffOff();
                waveofflighton = false;
            }
            meatballmoving = false;
            return true;
        }
        float glidepath = getVlaGlidePath(aircraft1);
        float vert = Aircraft.cvt(glidepath, -0.8F, 0.8F, -1.0F, 1.0F);
        float beacon = getVlaAzimuthPB(aircraft1);
        float horiz = Aircraft.cvt(beacon, -92F, 92F, -1.0F, 1.0F);
        MlaMeatballSet(horiz, vert);

        if(glidepath > 0.7F)
        {
            if(!cutlighton)
            {
                MlaCutLightOn();
                cutlighton = true;
            }
        }
        else
        {
            if(cutlighton)
            {
                MlaCutLightOff();
                cutlighton = false;
            }
        }
        if(distanceVla < 100F && (glidepath > 0.7F || glidepath < 0.7F || beacon < -80F || beacon > 80F))
        {
            if(!waveofflighton)
            {
                MlaWaveoffOn();
                waveofflighton = true;
            }
        }
        else
        {
            if(waveofflighton)
            {
                MlaWaveoffOff();
                waveofflighton = false;
            }
        }
        meatballmoving = true;
        return true;
    }


    private boolean USIflolsUpdate()
    {
        Aircraft aircraft1 = getNearestAircraftFront();
        if(aircraft1 == null || (!insideOfGlidepath && distanceVla > 100F))
        {
            if(meatballmoving)
            {
                USIflolsMeatballCenter();
                MlaCutLightOff();
                cutlighton = false;
                MlaWaveoffOff();
                waveofflighton = false;
            }
            meatballmoving = false;
            return true;
        }
        float glidepath = getVlaGlidePath(aircraft1);
        float vert = Aircraft.cvt(glidepath, -0.8F, 0.8F, -1.0F, 1.0F);
        float beacon = getVlaAzimuthPB(aircraft1);
        USIflolsMeatballSet(vert);

        if(glidepath > 0.7F)
        {
            if(!cutlighton)
            {
                MlaCutLightOn();
                cutlighton = true;
            }
        }
        else
        {
            if(cutlighton)
            {
                MlaCutLightOff();
                cutlighton = false;
            }
        }
        if(distanceVla < 100F && (glidepath > 0.7F || glidepath < 0.7F || beacon < -80F || beacon > 80F))
        {
            if(!waveofflighton)
            {
                MlaWaveoffOn();
                waveofflighton = true;
            }
        }
        else
        {
            if(waveofflighton)
            {
                MlaWaveoffOff();
                waveofflighton = false;
            }
        }
        meatballmoving = true;
        return true;
    }


    private boolean FRFlolsUpdate()
    {
        Aircraft aircraft1 = getNearestAircraftFront();
        if(aircraft1 == null || (!insideOfGlidepath && distanceVla > 100F))
        {
            if(meatballmoving)
            {
                FRFlolsMeatballCenter();
                FRFlolsCutLightOff();
                cutlighton = false;
                FRFlolsWaveoffOff();
                waveofflighton = false;
            }
            meatballmoving = false;
            return true;
        }
        float glidepath = getVlaGlidePath(aircraft1);
        float vert = Aircraft.cvt(glidepath, -0.8F, 0.8F, -1.0F, 1.0F);
        float beacon = getVlaAzimuthPB(aircraft1);
        FRFlolsMeatballSet(vert);

        if(glidepath > 0.7F)
        {
            if(!cutlighton)
            {
                FRFlolsCutLightOn();
                cutlighton = true;
            }
        }
        else
        {
            if(cutlighton)
            {
                FRFlolsCutLightOff();
                cutlighton = false;
            }
        }
        if(distanceVla < 100F && (glidepath > 0.7F || glidepath < 0.7F || beacon < -80F || beacon > 80F))
        {
            if(!waveofflighton)
            {
                FRFlolsWaveoffOn();
                waveofflighton = true;
            }
        }
        else
        {
            if(waveofflighton)
            {
                FRFlolsWaveoffOff();
                waveofflighton = false;
            }
        }
        meatballmoving = true;
        return true;
    }

    // End of - Visual Landing Aid

    private static final int MAX_PARTS = 255;
    private static final int MAX_GUNS = 255;
    private static final int MAX_USER_ADDITIONAL_COLLISION_CHUNKS = 4;
    public float CURRSPEED;
    public boolean isTurning;
    public boolean isTurningBackward;
    public boolean mustRecomputePath;
    public boolean mustSendSpeedToNet;
    private final int REQUEST_LOC = 93;
    int hasTracking;
    private ShipProperties prop;
    private static final int NETSEND_MIN_DELAY_MS_PARTSSTATE = 650;
    private static final int NETSEND_MAX_DELAY_MS_PARTSSTATE = 1100;
    private long netsendPartsState_lasttimeMS;
    private boolean netsendPartsState_needtosend;
    private float netsendDrown_pitch = 0.0F;
    private float netsendDrown_roll = 0.0F;
    private float netsendDrown_depth = 0.0F;
    private float netsendDrown_timeS = 0.0F;
    private int netsendDrown_nparts = 0;
    private static final int NETSEND_MIN_DELAY_MS_FIRE = 40;
    private static final int NETSEND_MAX_DELAY_MS_FIRE = 85;
//    private static final long NETSEND_MIN_BYTECODEDDELTATIME_MS_FIRE = -2000L;
//    private static final long NETSEND_MAX_BYTECODEDDELTATIME_MS_FIRE = 5000L;
    private static final int NETSEND_ABSLIMIT_NUMITEMS_FIRE = 31;
    private static final int NETSEND_MAX_NUMITEMS_FIRE = 15;
    private long netsendFire_lasttimeMS;
    private int netsendFire_armindex;
    private static TmpTrackOrFireInfo netsendFire_tmpbuff[];
    private FiringDevice arms[];
    private RadarDevice radars[];
    private static final int STPART_LIVE = 0;
    private static final int STPART_BLACK = 1;
    private static final int STPART_DEAD = 2;
    private Part parts[];
    private int shotpoints[];
    int numshotpoints;
    private static final int NETSEND_MIN_DELAY_MS_DMG = 65;
    private static final int NETSEND_MAX_DELAY_MS_DMG = 115;
    private static final int NETSEND_ABSLIMIT_NUMITEMS_DMG = 256;
    private static final int NETSEND_MAX_NUMITEMS_DMG = 14;
    private long netsendDmg_lasttimeMS;
    private int netsendDmg_partindex;
    private ArrayList path;
    private int cachedSeg;
    private float bodyDepth;
    private float bodyPitch;
    private float bodyRoll;
    private float shipYaw;
    private long tmInterpoStart;
    private long tmInterpoEnd;
    private float bodyDepth0;
    private float bodyPitch0;
    private float bodyRoll0;
    private float bodyDepth1;
    private float bodyPitch1;
    private float bodyRoll1;
    private boolean bSailorsDisappear;
    private long timeOfSailorsDisappear;
    private long timeOfDeath;
    private long sink2timeWhenStop;
    private float sink2Depth;
    private float sink2Pitch;
    private float sink2Roll;
    public int dying;
    static final int DYING_NONE = 0;
    static final int DYING_SINK1 = 1;
    static final int DYING_SINK2 = 2;
    static final int DYING_DEAD = 3;
    private long respawnDelay;
    private long wakeupTmr;
    private long radarTmr;
    public float DELAY_WAKEUP;
    public int SKILL_IDX;
    public float SLOWFIRE_K;
    private Pipe pipes[];
    private Pipe dsmoks[];
    private Eff3DActor wake[] = {
        null, null, null
    };
    private Eff3DActor noseW;
    private Eff3DActor nose;
    private Eff3DActor tail;
    private static ShipProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private Point3d p = new Point3d();
    private Point3d p1 = new Point3d();
    private Point3d p2 = new Point3d();
    private Orient o;
//    private Vector3f tmpvf = new Vector3f();
    private Vector3d tmpvd = new Vector3d();
    private float tmpYPR[] = new float[3];
//    private float tmpf6[] = new float[6];
    private Loc tmpL = new Loc();
//    private byte tmpBitsState[] = new byte[32];
    private float rollAmp;
    private int rollPeriod;
    private double rollWAmp;
    private float pitchAmp;
    private int pitchPeriod;
    private double pitchWAmp;
    private Vector3d W;
    private Vector3d N;
    private Vector3d tmpV;
    public Orient initOr;
    public Loc initLoc;
    private AirportCarrier airport;
    private CellAirField cellTO;
    private CellAirField cellLDG;
    public Aircraft towAircraft;
    public int towPortNum;
    public HookNamed towHook;
    private Vector3d tmpDir = new Vector3d();
    public static String ZUTI_RADAR_SHIPS[] = {
        "CV", "Marat", "Kirov", "BB", "Niobe", "Illmarinen", "Vainamoinen", "Tirpitz", "Aurora", "Carrier0",
        "Carrier1"
    };
    public static String ZUTI_RADAR_SHIPS_SMALL[] = {
        "Destroyer", "DD", "USSMcKean", "Italia0", "Italia1"
    };
    public BornPlace zutiBornPlace;
    private boolean zutiIsClassBussy;

    private static boolean bLogDetail = false;
    private static boolean bLogDetailBD = false;

    private float BlastDeflectorControl[] = new float[4];
    public boolean bHasBlastDeflectorControl = false;
    private float blastDeflector[] = new float[4];
    private float blastDeflector_[] = new float[4];
    private float dvBlastDeflector;

    private static final double glideScopeInRads = Math.toRadians(3D);
    private Eff3DActor effmeatball[] = new Eff3DActor[2];
    private Eff3DActor effdatum[] = new Eff3DActor[NETSEND_MAX_NUMITEMS_DMG];
    private Eff3DActor effcut[] = new Eff3DActor[4];
    private Eff3DActor effwaveoff[] = new Eff3DActor[4];
    private Eff3DActor effemgwaveoff[] = new Eff3DActor[2];
//    private Eff3DActor effreserve1[] = new Eff3DActor[2];
//    private Eff3DActor effreserve2[] = new Eff3DActor[2];
//    private Actor actorme = this;
    private float[] Meatballxyz = new float[3];
    private float[] Meatballypr = new float[3];
    private boolean meatballmoving = false;
    private float distanceVla = 1000F;
    private int meatballpos = 0;
    private boolean insideOfGlidepath = false;
    private boolean cutlighton = false;
    private boolean waveofflighton = false;
    private boolean emgwaveofflighton = false;
//    private boolean bMlaVisible = true;

    public boolean bHasMirrorLA = false;
    public boolean bInitDoneMirrorLA = false;
    public boolean bHasUSIflols = false;
    public boolean bInitDoneUSIflols = false;
    public boolean bHasFRFlols = false;
    public boolean bInitDoneFRFlols = false;

    static
    {
        netsendFire_tmpbuff = new TmpTrackOrFireInfo[NETSEND_ABSLIMIT_NUMITEMS_FIRE];
        for(int i = 0; i < netsendFire_tmpbuff.length; i++)
            netsendFire_tmpbuff[i] = new TmpTrackOrFireInfo();

    }
}
