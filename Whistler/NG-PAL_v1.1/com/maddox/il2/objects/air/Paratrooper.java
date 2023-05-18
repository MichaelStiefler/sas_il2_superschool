package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.ground.UnitInterface;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.NetMissionTrack;
//import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.CannonRocketSimpleRS82;
import com.maddox.sound.*;
import com.maddox.rts.*;
import java.io.IOException;
//import java.io.PrintStream;

public class Paratrooper extends ActorMesh
    implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener
{

    public static class SPAWN
        implements NetSpawn
    {
        public void netSpawn(int i, NetMsgInput netmsginput)
        {
            try
            {
                Loc loc = new Loc(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Vector3d vector3d = new Vector3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Actor actor = null;
                NetObj netobj = netmsginput.readNetObj();
                if(netobj != null)
                    actor = (Actor)netobj.superObj();
                /*Paratrooper paratrooper = */new Paratrooper(actor, netmsginput.readUnsignedByte(), netmsginput.readUnsignedByte(), loc, vector3d, netmsginput, i);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        public SPAWN()
        {
        }
    }

    class Mirror extends ParaNet
    {
        public Mirror(Actor actor, NetMsgInput netmsginput, int i)
        {
            super(actor, netmsginput, i);
            try
            {
                turn_para_on_height = netmsginput.readFloat();
                nRunCycles = netmsginput.readByte();
                driver = (NetUser)netmsginput.readNetObj();
                testDriver();
            }
            catch(Exception exception) { }
        }
    }

    class Master extends ParaNet
    {
        public Master(Actor actor)
        {
            super(actor);
            actor.pos.getAbs(Paratrooper.p);
            float f = (float)Paratrooper.p.z - Landscape.HQ((float)Paratrooper.p.x, (float)Paratrooper.p.y);
            if(f <= 500F)
                turn_para_on_height = 500F;
            else
            if(f >= 4000F)
                turn_para_on_height = 2000F;
            else
                turn_para_on_height = 500F + 1500F * ((f - 500F) / 3500F);
            turn_para_on_height *= World.Rnd().nextFloat(1.0F, 1.2F);
            nRunCycles = World.Rnd().nextInt(6, 12);
            Class class1 = actor.getOwner().getClass();
            Object obj = Property.value(class1, "cockpitClass");
            if(obj != null)
            {
                Class aclass[] = null;
                if(obj instanceof Class)
                {
                    aclass = new Class[1];
                    aclass[0] = (Class)obj;
                } else
                {
                    aclass = (Class[])obj;
                }
                for(int i = 0; i < aclass.length; i++)
                {
                    int j = Property.intValue(aclass[i], "astatePilotIndx", 0);
                    if(j != idxOfPilotPlace)
                        continue;
                    Actor actor1 = ((Aircraft)actor.getOwner()).netCockpitGetDriver(i);
                    if(actor1 == null)
                        continue;
                    if(Mission.isSingle())
                        driver = (NetUser)NetEnv.host();
                    else
                    if(actor1 instanceof NetGunner)
                        driver = ((NetGunner)actor1).getUser();
                    else
                        driver = ((NetAircraft)actor1).netUser();
                    break;
                }

            }
            testDriver();
        }
    }

    class ParaNet extends ActorNet
    {
        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(!netmsginput.isGuaranted())
                return false;
            byte byte0 = netmsginput.readByte();
            byte byte1 = -1;
            switch(byte0)
            {
            case 68:
                byte1 = 1;
                NetObj netobj = netmsginput.readNetObj();
                Actor actor = null;
                if(netobj != null)
                    actor = (Actor)netobj.superObj();
                Die(actor, false);
                break;

            case 83:
                byte1 = 1;
                NetObj netobj1 = netmsginput.readNetObj();
                Actor actor1 = null;
                if(netobj1 != null)
                    actor1 = (Actor)netobj1.superObj();
                Object aobj[] = getOwnerAttached();
                for(int i = 0; i < aobj.length; i++)
                {
                    Chute chute = (Chute)aobj[i];
                    if(Actor.isValid(chute))
                        chute.tangleChute(actor1);
                }

                break;
            }
            if(byte1 >= 0)
            {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, byte1);
                postExclude(netmsginput.channel(), netmsgguaranted);
                return true;
            } else
            {
                return false;
            }
        }

        public ParaNet(Actor actor)
        {
            super(actor);
        }

        public ParaNet(Actor actor, NetMsgInput netmsginput, int i)
        {
            super(actor, netmsginput.channel(), i);
        }
    }

    class Move extends Interpolate
    {
        public boolean tick()
        {
            if(st == ST_DISAPPEAR)
            {
                if(dying == 0)
                    checkCaptured();
                postDestroy();
                return false;
            }
            if((st == ST_LIE || st == ST_LIEDEAD || st == ST_SWIM) && Time.current() >= disappearTime)
            {
                postDestroy();
                return false;
            }
            if(dying != 0)
            {
                switch(st)
                {
                case ST_RUN:
                    st = ST_FALL;
                    if(isWalkerPilot())
                    {
                        fxFreeFall.stop();
                        fxSprint.stop();
                        fxFatality.start();
                        bControlled = false;
                        flashLight.light.setEmit(0.0F, 0.0F);
                        bFlashLight = false;
                        if(!World.isPlayerDead())
                            World.setPlayerDead();
//                    setName(playerParaName);
                    }
                    animStartTime = Time.current();
                    break;

//                case ST_LIE:
//                    st = ST_LIEDEAD;
//                    idxOfDeadPose = World.Rnd().nextInt(0, 3);
//                    break;
                }
            }
            long l = Time.tickNext() - animStartTime;
            switch(st)
            {
            default:
                break;

            case ST_FREEFLY:
            case ST_PARAUP1:
            case ST_PARAUP2:
            case ST_PARATANGLED:
                pos.getAbs(Paratrooper.p);
                Engine.land();
                float f = Landscape.HQ((float)Paratrooper.p.x, (float)Paratrooper.p.y);

//                if(isWalkerPilot() && carrierDeck != null && World.land().isWater(Paratrooper.p.x, Paratrooper.p.y) && World.getPlayerAircraft().FM.isStationedOnGround())
                if(isWalkerPilot() && carrierDeck != null && World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
                {
                    Point3d corn = new Point3d();
                    Point3d corn1 = new Point3d();
                    Point3d Pship = new Point3d();
                    corn.set(Paratrooper.p);
                    corn1.set(Paratrooper.p);
                    corn1.z -= 20D;
                    Actor actor = Engine.collideEnv().getLine(corn, corn1, false, Paratrooper.clipFilter, Pship);
                    if(Pship.z > 1.0D)
                    {
                        f = (float)(Pship.z + 0.5D);
                    } else
                    {
                        carrierDeck = null;
/*
                        st = ST_LIEDEAD;
                        animStartTime = Time.current();
                        disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                        idxOfDeadPose = World.Rnd().nextInt(0, 3);
                        new MsgAction(0.0D, actor) {

                            public void doAction(Object obj)
                            {
                                Paratrooper paratrooper = (Paratrooper)obj;
                                paratrooper.Die(Engine.actorLand());
                            }
                        }
;
*/
                    }
                }

                if(st == ST_FREEFLY)
                {

// Free fall movement

                    if(l >= FREEFLY_ROT_TIME)
                    {
// Deploy chute (all Paratroopers - detect WalkerPilot)
                        pos.setAbs(faceOrient);
                        if(dying == 0 && (float)Paratrooper.p.z - f <= (isWalkerPilot() ? 100F : turn_para_on_height) && speed.z < -5D)
                        {
                            if(isWalkerPilot())
                            {
                                joyX = joyY = 0.0F;
                                joyZ = 1.0F;
                                setMeshPitch(0.0F);
                            }
                            st = ST_PARAUP1;
                            animStartTime = Time.current();
                            l = Time.tickNext() - animStartTime;
                            new Chute(actor);

                        } else if(isWalkerPilot() && dying == 0)
                        {
                            fxFreeFall.start();
                            controlParatrooper();

//                            Paratrooper.o.interpolate(faceOrient, f1);
//                            pos.setAbs(Paratrooper.o);

                            double speedTemp = speed.z;
                            if(joyX != 0.0F)
                            {
                                Vector3d vector3d = new Vector3d();
                                vector3d.set(((!RTSConf.cur.keyboard.isPressed(KEY_U) && RTSConf.cur.keyboard.isPressed(KEY_D) && joyZ <= 1.0F) ? -1.0D : 1.0D), 0.0D, 0.0D);
                                faceOrient.transform(vector3d);
                                speed.set(vector3d);
                                speed.z = 0.0D;
                                speed.normalize();
                                speed.scale((double)joyX * 10D);
                                speed.interpolate(speedIn, 0.05F);
                                speedIn.set(speed);
//                              tRunCycle = (int)((double)RUN_CYCLE_TIME * (6D - 5D * (double)joyX));
//                              bStanding = false;
                            } else
                            {
                                speed.scale(0.0D);
//                                tRunCycle = 0xb2f48;
//                                bStanding = true;
                            }
                            speed.z = speedTemp;
                        }

                    } else
                    {
                        pos.getAbs(Paratrooper.o);
                        float f1 = (float)l / (float)FREEFLY_ROT_TIME;
                        if(f1 <= 0.0F)
                            f1 = 0.0F;
                        if(f1 >= 1.0F)
                            f1 = 1.0F;
                        Paratrooper.o.interpolate(startOrient, faceOrient, f1);
                        pos.setAbs(Paratrooper.o);
                    }
                }
                if(st == ST_PARAUP1 && l >= PARAUP1_CYCLE_TIME)
                {
                    st = ST_PARAUP2;
                    animStartTime = Time.current();
                    l = Time.tickNext() - animStartTime;
                }
                Paratrooper.p.scaleAdd(Time.tickLenFs(), speed, Paratrooper.p);
                speed.z -= Time.tickLenFs() * World.g();
                if(st == ST_PARAUP2)
                {
                    if(speed.x != 0.0D)
                        speed.x -= (Math.abs(speed.x) / speed.x) * 0.01D * (speed.x * speed.x) * (double)Time.tickLenFs();
                    if(speed.y != 0.0D)
                        speed.y -= (Math.abs(speed.y) / speed.y) * 0.01D * (speed.y * speed.y) * (double)Time.tickLenFs();
                } else
                {
                    if(speed.x != 0.0D)
                        speed.x -= (Math.abs(speed.x) / speed.x) * 0.001D * (speed.x * speed.x) * (double)Time.tickLenFs();
                    if(speed.y != 0.0D)
                        speed.y -= (Math.abs(speed.y) / speed.y) * 0.001D * (speed.y * speed.y) * (double)Time.tickLenFs();
                }
                double d = st == ST_PARAUP2 ? (double)PARA_SPEED : (double)FREE_SPEED;
                if(-speed.z > d)
                {
                    double d1 = -speed.z - d;
                    if(d1 > (double)(Time.tickLenFs() * (float)FREEFLY_N_FRAMES))
                        d1 = Time.tickLenFs() * (float)FREEFLY_N_FRAMES;
                    speed.z += d1;
                }
                if(Paratrooper.p.z <= (double)f)
                {
                    fxFreeFall.stop();
// why? - dunno
                    boolean flag = isWalkerPilot() ? false : speed.length() > 10.5D;
                    Vector3d vector3d = new Vector3d();
                    vector3d.set(1.0D, 0.0D, 0.0D);
                    faceOrient.transform(vector3d);
                    speed.set(vector3d);
                    speed.z = 0.0D;
                    speed.normalize();
                    speed.scale((double)RUN_SPEED);
                    Paratrooper.p.z = f;

// If chute didn't deploy (flag = falling too fast?) or actor is already dead
// Then don't do anything else, just lay dead

                    if(flag || dying != 0)
                    {
                        if(World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
                        {
                            st = ST_LIE;
                            Paratrooper.p.z -= 0.5D;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                        } else
                        {
                            st = ST_LIEDEAD;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                            idxOfDeadPose = World.Rnd().nextInt(0, 3);
                        }
                        new MsgAction(0.0D, actor) {

                            public void doAction(Object obj)
                            {
                                Paratrooper paratrooper = (Paratrooper)obj;
                                paratrooper.Die(Engine.actorLand());
                            }
                        }
;

// Otherwise let chute fall into ground/water

                    } else
                    {
                        st = LIEDEAD_N_FRAMES;
                        animStartTime = Time.current();
                        if(name().equals(playerParaName) && Mission.isNet() && World.getPlayerFM() != null && Actor.isValid(World.getPlayerAircraft()) && World.getPlayerAircraft().isNetPlayer())
                        {
                            FlightModel flightmodel = World.getPlayerFM();
                            if(flightmodel.isWasAirborne() && flightmodel.isStationedOnGround() && !flightmodel.isNearAirdrome())
                                Chat.sendLogRnd(2, "gore_walkaway", World.getPlayerAircraft(), null);
                        }
                    }
                    pos.setAbs(faceOrient);
                    Object aobj[] = getOwnerAttached();
                    for(int i = 0; i < aobj.length; i++)
                    {
                        Chute chute = (Chute)aobj[i];
                        if(Actor.isValid(chute))
                            chute.landing();
                    }

                }
                pos.setAbs(Paratrooper.p);
                break;

            case ST_RUN:
                if(isWalkerPilot())
                {
// RUN
                    pos.getAbs(Paratrooper.p);
                    Paratrooper.p.scaleAdd(Time.tickLenFs(), speed, Paratrooper.p);

//                    if(carrierDeck != null)
//                    {
//                        carrierDeck.getSpeed(Vship);
//                        Paratrooper.p.scaleAdd(Time.tickLenFs(), Vship, Paratrooper.p);
//                    }

                    double h = Engine.land().HQ(Paratrooper.p.x, Paratrooper.p.y);
                    if(carrierDeck != null)
                    {
                        Point3d corn = new Point3d();
                        Point3d corn1 = new Point3d();
                        Point3d Pship = new Point3d();
                        corn.set(Paratrooper.p);
                        corn1.set(Paratrooper.p);
                        corn1.z -= 20D;
                        Actor actor1 = Engine.collideEnv().getLine(corn, corn1, false, Paratrooper.clipFilter, Pship);

                        if(Pship.z > 1.0D + h)
                        {
                            Paratrooper.p.z = Pship.z + 0.5D;
                            carrierDeck.getSpeed(Vship);
                            Paratrooper.p.scaleAdd(Time.tickLenFs(), Vship, Paratrooper.p);
                        } else
                        {
                            Paratrooper.p.z = Pship.z + 0.05D;
//                            Paratrooper.p.z = h;
//                            pos.setAbs(Paratrooper.p);
                            carrierDeck = null;
//
//                            st = 7;
//                            animStartTime = Time.current();
//                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
//                            idxOfDeadPose = World.Rnd().nextInt(0, 3);
//                            new MsgAction(0.0D, actor) {
//
//                                public void doAction(Object obj)
//                                {
//                                    Paratrooper paratrooper = (Paratrooper)obj;
//                                    paratrooper.Die(Engine.actorLand());
//                                }
//                            }
//;
//                            break;
                        }
                    } else
                    {
                        Paratrooper.p.z = h;
                    }
                    pos.setAbs(Paratrooper.p);
/*
                    if(!bControlled && carrierDeck == null && World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
                    {
                        if(swimMeshCode < 0)
                            swimMeshCode = World.getPlayerAircraft().getArmy() == 1 ? 1 : 0;

                        if(swimMeshCode < 0)
                        {
                            st = 5;
                            animStartTime = Time.current();
                        } else
                        {
                            setMesh(Paratrooper.GetMeshName_Water(swimMeshCode));
                            pos.getAbs(Paratrooper.p);
                            Paratrooper.p.z = Engine.land().HQ(Paratrooper.p.x, Paratrooper.p.y);
                            pos.setAbs(Paratrooper.p);
                            st = 8;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                            checkCaptured();
                        }
                        break;
                    }
*/
                    if(bControlled)
                    {

// Own aircraft explodes
/*
                        if(World.isPlayerDead())
                        {
                            setName(playerParaName);
                            bControlled = false;
                            setMeshPitch(0.0F);
                            SoundFX soundfx = newSound("pal.death", true);
                            st = 7;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                            idxOfDeadPose = World.Rnd().nextInt(0, 3);
                            new MsgAction(0.0D, actor) {

                                public void doAction(Object obj)
                                {
                                    Paratrooper paratrooper = (Paratrooper)obj;
                                    paratrooper.Die(Engine.actorLand());
                                }
                            }
;
                            break;
                        }
*/
                        if(carrierDeck == null && World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
                        {
                            if(!bWater)
                                setMesh(Paratrooper.GetMeshName_Water(2));
                            bWater = true;
                        } else
                        {
                            if(bWater)
                            {
                                setMesh(Paratrooper.GetMeshName(World.getPlayerAircraft().getArmy() == 1 ? 1 : 2));
                                if(skinMat != null)
                                     mesh().materialReplace("pilot", skinMat);
                            }
                            bWater = false;
                        }

                        controlParatrooper();

                        pos.getAbs(Paratrooper.o);
                        float f1 = (float)l / (float)FREEFLY_ROT_TIME;
                        if(f1 <= 0.0F)
                            f1 = 0.0F;
                        if(f1 >= 1.0F)
                            f1 = 1.0F;
                        Paratrooper.o.interpolate(faceOrient, f1);
                        pos.setAbs(Paratrooper.o);
                        if(joyX != 0.0F)
                        {
                            Vector3d vector3d = new Vector3d();
                            vector3d.set(((!RTSConf.cur.keyboard.isPressed(KEY_U) && RTSConf.cur.keyboard.isPressed(KEY_D) && joyZ <= 1.0F) ? -1.0D : 1.0D), 0.0D, 0.0D);
                            faceOrient.transform(vector3d);
                            speed.set(vector3d);
                            speed.z = 0.0D;
                            speed.normalize();
                            speed.scale((double)joyX * 10D * (bWater ? 0.5D : 1D));
                            speed.interpolate(speedIn, 0.05F);
                            speedIn.set(speed);
//                            tRunCycle = (int)((double)RUN_CYCLE_TIME * (6D - 5D * (double)joyX));
                            bStanding = false;
                            Eff3DActor.New(new Loc(Paratrooper.p, new Orient(0.0F, 90F, 0.0F)), (joyZ < 4.0F ? 0.0F : (joyZ < 10.0F ? joyZ * 0.1F : 1.0F)), "effects/Explodes/Bullet/" + (bWater ? "Water" : "Land") + "/PalSpray.eff", 0.5F);
                        } else
                        {
                            speed.scale(0.0D);
//                            tRunCycle = 0xb2f48;
                            bStanding = true;
                        }

                        if(World.Sun().ToSun.z < -0.1F)
                        {
                            if(!bFlashLight)
                            {
                                flashLight.light.setEmit(0.5F, 30F);
                                bFlashLight = true;
                            }
                        } else if(bFlashLight)
                        {
                            flashLight.light.setEmit(0.0F, 0.0F);
                            bFlashLight = false;
                        }

                        setAnimFrame(Time.tickNext());
                        return true;

                    } else
                    {
                        if(((l * (long)LIEDEAD_N_FRAMES) / (long)RUN_CYCLE_TIME >= (long)nRunCycles) || (carrierDeck == null && World.land().isWater(Paratrooper.p.x, Paratrooper.p.y)))
                        {
                            fxExplore.play();
                            bControlled = true;
                            joyZ = 1.0F;
//                            try
//                            {
//                                HotKeyEnv.enable("aircraftView", false);
//                                CmdEnv.top().exec("fov " + Main3D.FOVX);
//                                CmdEnv.top().exec("fov " + Main3D.cur3D().camera3D.FOV());
//                            }
//                            catch(Exception e)
//                            {
//                                System.out.println("Couldn't find a CAMERA for the Paratrooper Mesh. No First Person Pilot available!");
//                            }
                        }
                    }

                } else
                {
                    pos.getAbs(Paratrooper.p);
                    Paratrooper.p.scaleAdd(Time.tickLenFs(), speed, Paratrooper.p);
                    Paratrooper.p.z = Engine.land().HQ(Paratrooper.p.x, Paratrooper.p.y);
                    pos.setAbs(Paratrooper.p);
                    if(World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
                    {
                        if(swimMeshCode < 0)
                        {
                            st = ST_FALL;
                            animStartTime = Time.current();
                        } else
                        {
                            setMesh(Paratrooper.GetMeshName_Water(swimMeshCode));
                            pos.getAbs(Paratrooper.p);
                            Paratrooper.p.z = Engine.land().HQ(Paratrooper.p.x, Paratrooper.p.y);
                            pos.setAbs(Paratrooper.p);
                            st = ST_SWIM;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                            checkCaptured();
                        }
                        break;
                    }
                }
                if(l / (long)RUN_CYCLE_TIME >= (long)nRunCycles)
                {
                    st = ST_FALL;
                    animStartTime = Time.current();
                }
                break;

            case ST_FALL:

// Hit the dirt animation

                pos.getAbs(Paratrooper.p);
                Paratrooper.p.scaleAdd(Time.tickLenFs(), speed, Paratrooper.p);
                Paratrooper.p.z = Engine.land().HQ(Paratrooper.p.x, Paratrooper.p.y);
                if(isWalkerPilot())
                {
                    if(World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
                    {
                        Paratrooper.p.z -= 0.5D;
                        setMesh(Paratrooper.GetMeshName(World.getPlayerAircraft().getArmy() == 1 ? 1 : 2));
                        if(skinMat != null)
                            mesh().materialReplace("pilot", skinMat);
                    }
                } else
                {
                    if(World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
                    {
                        if(swimMeshCode < 0)
                        {
                            Paratrooper.p.z -= 0.5D;
                            pos.setAbs(Paratrooper.p);
                            st = ST_LIE;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                            new MsgAction(0.0D, actor) {

                                public void doAction(Object obj)
                                {
                                    Paratrooper paratrooper = (Paratrooper)obj;
                                    paratrooper.Die(Engine.actorLand());
                                }
                            }
;
                        } else
                        {
                            setMesh(Paratrooper.GetMeshName_Water(swimMeshCode));
                            pos.getAbs(Paratrooper.p);
                            Paratrooper.p.z = Engine.land().HQ(Paratrooper.p.x, Paratrooper.p.y);
                            pos.setAbs(Paratrooper.p);
                            st = ST_SWIM;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                            checkCaptured();
                        }
                        break;
                    }
                }
                pos.setAbs(Paratrooper.p);

// Hit the dirt animation completes

                if(l >= FALL_CYCLE_TIME)
                {
                    if(isWalkerPilot())
                    {
                        if(World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
                        {
                            st = ST_LIE;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                        } else
                        {
                            st = ST_LIEDEAD;
                            animStartTime = Time.current();
                            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                            idxOfDeadPose = World.Rnd().nextInt(0, 3);
                        }
                    } else
                    {
                        st = ST_LIE;
                        animStartTime = Time.current();
                        disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                        checkCaptured();
                    }
                }
                break;

            case ST_LIE:
                break;
            case ST_LIEDEAD:
                pos.getAbs(Paratrooper.p);
                Paratrooper.p.z = Engine.land().HQ(Paratrooper.p.x, Paratrooper.p.y);
//                if(World.land().isWater(Paratrooper.p.x, Paratrooper.p.y))
//                    Paratrooper.p.z -= 5D;
                pos.setAbs(Paratrooper.p);
                break;
            }
            setAnimFrame(Time.tickNext());
            return true;
        }

        public void setMeshPitch(float f)
        {
            faceOrient.set(-faceOrient.getYaw(), f, 0.0F);
        }

        public void controlParatrooper()
        {
            boolean bMeshPitch = false;

            joyY = joyX = 0.0F;

            if((Main3D.cur3D().viewActor() instanceof Paratrooper) && isWalkerPilot())
            {
                if(RTSConf.cur.keyboard.isPressed(KEY_U))
                {
// Free fall
                    if(st == 0)
                    {
                        bMeshPitch = true;
                        setMeshPitch(-5.0F);
                    }

                    if(RTSConf.cur.keyboard.isPressed(KEY_D))
                    {
                        bMeshPitch = true;
                        setMeshPitch((bWater ? -5.0F : -10.0F));
                        joyZ += bControlled? 0.05F : 0.25F;

                        if(!bSpeedUp && st != 0)
                        {
//                            fxSpeedUp.start();
                            fxSpeedUp.play();
                            bSpeedUp = true;
                        }
                    }

                    joyX = joyZ;

                } else
                {
                    if(joyZ > 1.0F)
                    {
//                        bMeshPitch = true;
//                        setMeshPitch((bWater ? 5.0F : 10.0F));
                        joyZ -= joyZ > 15F ? 0.50F : 0.25F;
                        joyX = joyZ;

                    } else
                    if(RTSConf.cur.keyboard.isPressed(KEY_D))
                    {
                        joyX = 0.5F;
// Free fall
                        if(st == 0)
                        {
                            bMeshPitch = true;
                            setMeshPitch(5.0F);
                        }
                    }
                }

                if(RTSConf.cur.keyboard.isPressed(KEY_T))
                {
                    if(bAllowTurn && bControlled)
                    {
                        joyY = 180F;
                        if(joyZ > 4.0F)
                            fxReverse.play();
                    }
                    bAllowTurn = false;

                } else if(!bAllowTurn)
                {
                    bAllowTurn = true;
                }

                if(bAllowTurn)
                {
                    if(RTSConf.cur.keyboard.isPressed(KEY_R) && !RTSConf.cur.keyboard.isPressed(KEY_L))
                        joyY = 2.0F;

                    if(RTSConf.cur.keyboard.isPressed(KEY_L) && !RTSConf.cur.keyboard.isPressed(KEY_R))
                        joyY = -2.0F;
                }

                if(joyY != 0.0F)
                    faceOrient.increment(joyY, 0.0F, 0.0F);

// bControlled = to prevent firing from free falling
                if(RTSConf.cur.keyboard.isPressed(KEY_F))
                {
                    if(bAllowFire && bControlled && joyZ < (bWater ? 30F : 15F))
                    {
                        CannonRocketSimpleRS82 rocket = new CannonRocketSimpleRS82();
                        rocket.launch(new Point3d(Paratrooper.p.x, Paratrooper.p.y, Paratrooper.p.z + 3D), new Orient(Main3D.cur3D().camera3D.pos.getAbsOrient().azimut(), Main3D.cur3D().camera3D.pos.getAbsOrient().tangage() + 10F, 0.0F), 150F, Main3D.cur3D().viewActor());
                        rocket.destroy();
                    }
                    bAllowFire = false;

                } else if(!bAllowFire)
                {
                    bAllowFire = true;
                }

                if(bSpeedUp && !RTSConf.cur.keyboard.isPressed(KEY_D))
                    bSpeedUp = false;

                if(joyZ > 15.0F && bControlled)
                    fxSprint.start();
                else
                    fxSprint.stop();
            }

            if(!bMeshPitch)
                setMeshPitch(0.0F);
        }

        Move()
        {
        }
    }

    static class ClipFilter
        implements ActorFilter
    {

        public boolean isUse(Actor actor, double d1)
        {
            return actor instanceof BigshipGeneric;
        }

        ClipFilter()
        {
        }
    }

    private class SoldDraw extends ActorMeshDraw
    {

        public int preRender(Actor actor)
        {
            setAnimFrame(Time.current());
            return super.preRender(actor);
        }

        private SoldDraw()
        {
        }
    }

    public boolean isChuteSafelyOpened()
    {
        return st == 2 || st == 6 || st == 8 || st == 9;
    }

    public static void resetGame()
    {
        _counter = 0;
//        preload1 = preload2 = preload3 = null;
        ngPALkeyboard = (int)Config.cur.ini.get("Mods", "ngPALkeyboard", 0, 0, 1);
        Config.cur.ini.set("Mods", "ngPALkeyboard", ngPALkeyboard);
    }

    public static void PRELOAD()
    {
//        preload1 = new Mesh(GetMeshName(1));
//        preload2 = new Mesh(GetMeshName(2));
//        preload3 = new Mesh(Chute.GetMeshName());
//        preload4 = new Mesh(GetMeshName_Water(0));
//        preload5 = new Mesh(GetMeshName_Water(1));
//        preload6 = new Mesh(GetMeshName_Water(2));
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        if((actor instanceof Aircraft) && actor.isNet() && actor.isNetMirror())
            aflag[0] = false;
        if((actor == getOwner() || getOwner() == null) && Time.current() - animStartTime < 2800L)
            aflag[0] = false;
        if(dying != 0 && (actor == null || !(actor instanceof ShipGeneric) && !(actor instanceof BigshipGeneric)))
            aflag[0] = false;
        if(carrierDeck == null && Mission.isSingle() && dying == 0 && (actor instanceof BigshipGeneric))
        {
            carrierDeck = actor;
            aflag[0] = false;
            nRunCycles = 2;
        }
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(st == 9)
            return;
        Point3d point3d = p;
        super.pos.getAbs(p);
        Point3d point3d1 = actor.pos.getAbsPoint();
        Vector3d vector3d = new Vector3d();
        vector3d.set(point3d.x - point3d1.x, point3d.y - point3d1.y, 0.0D);
        if(vector3d.length() < 0.001D)
        {
            float f = World.Rnd().nextFloat(0.0F, 359.99F);
            vector3d.set(Geom.sinDeg(f), Geom.cosDeg(f), 0.0D);
        }
        vector3d.normalize();
        float f1 = 0.2F;
        vector3d.add(World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1));
        vector3d.normalize();
        float f2 = 13.09091F * Time.tickLenFs();
        vector3d.scale(f2);
        speed.z *= 0.5D;
        point3d.add(vector3d);
        super.pos.setAbs(point3d);

// Take aircraft
        if(bControlled && st == LIEDEAD_N_FRAMES && actor != null && (actor instanceof Aircraft) && ((Aircraft)actor == World.getPlayerAircraft()) && (Time.current() >= paraBoardTime) && Actor.isValid(actor) && Actor.isAlive(actor) && ((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).isCapableOfTaxiing())
        {
            try
            {
// Destroy Walking Pilot
                postDestroy();
                HotKeyEnv.enable("aircraftView", true);
// Restore player vulnerability
				RealFlightModel realflightmodel = (RealFlightModel) World.getPlayerFM();
                realflightmodel.AS.astatePilotStates[realflightmodel.AS.astatePlayerIndex] = 0;
                World.cur().diffCur.RealisticPilotVulnerability = bRealPilotVul;
// Enter aircraft
                Aircraft aircraft = World.getPlayerAircraft();
                aircraft.netCockpitEnter(aircraft, 0);
                Main3D.cur3D().cockpitCur.focusEnter();
// Close the canopy (not required)
//                if(((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).CT.bHasCockpitDoorControl && ((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).CT.cockpitDoorControl < 0.5F && ((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).CT.getCockpitDoor() < 0.01F)
//                    ((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).AS.setCockpitDoor((Aircraft)actor, 1);
                World.cur().resetUser();
// Restore pilot model in cockpit
                doRemoveBodyFromPlane(((Aircraft)actor).hierMesh(), 1, true);

// REFUEL
/*
                String playerActor = actor.name();
                String playerFlight = playerActor.substring(0, playerActor.length() - 1);
                SectFile sectfile = Mission.cur().sectFile();
                float playerFuel = sectfile.get(playerFlight, "Fuel", 75F, 0.0F, 100F) * 0.01F * ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).M.maxFuel;
                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).M.fuel = playerFuel;
                HUD.log("Aircraft refueled");
*/
            }
            catch(Exception exception) { }

            return;

// OLD CODE
/*
            try
            {
                HotKeyEnv.enable("aircraftView", true);
                Class class1 = getOwner().getClass();
                Object obj = Property.value(class1, "cockpitClass");
                if(obj != null)
                {
                    Actor oldactor = getOwner();
//                    System.out.println("* (Paratrooper-msgCollision) Leaving old Owner '" + oldactor.name() + "' Plane...");
                    postDestroy();
//                    System.out.println("* (Paratrooper-msgCollision) Trying to switch Control to '" + actor.name() + "' Plane!");
                    ActorAux.doTakePlane(actor);
                    if(((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).CT.bHasCockpitDoorControl && ((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).CT.cockpitDoorControl < 0.5F && ((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).CT.getCockpitDoor() < 0.01F)
                        ((FlightModelMain) (((SndAircraft) ((Aircraft)actor)).FM)).AS.setCockpitDoor((Aircraft)actor, 1);
                    World.cur().resetUser();
                    if(oldactor != null && (oldactor instanceof Aircraft))
                        ActorAux.doRemoveBodyFromPlane(((Aircraft)oldactor).hierMesh(), 1, true);
                    ActorAux.doRemoveBodyFromPlane(((Aircraft)actor).hierMesh(), 1, true);
                }
            }
            catch(Exception e) { }
            return;
*/
        }

        if(st == 6 && dying == 0 && (actor instanceof UnitInterface) && actor.getSpeed(null) > 0.5D)
            Die(actor);
    }

    public void msgShot(Shot shot)
    {
        if(st == 9)
            return;
        shot.bodyMaterial = 3;
        if(dying != 0)
            return;
        if(shot.power <= 0.0F)
            return;
        if(shot.powerType == 1)
        {
            Die(shot.initiator);
            return;
        }
        if(shot.v.length() < 20D)
        {
            return;
        } else
        {
            Die(shot.initiator);
            return;
        }
    }

    public void msgExplosion(Explosion explosion)
    {
        if(st == 9)
            return;
        if(dying != 0)
            return;
        float f = 0.005F;
        float f1 = 0.1F;
        if(Explosion.killable(this, explosion.receivedTNT_1meter(this), f, f1, 0.0F))
            Die(explosion.initiator);
    }

    public void checkCaptured()
    {
        bCheksCaptured = true;
        if(logAircraftName != null && (driver == null && isNetMaster() || driver != null && driver.isMaster()))
            EventLog.onParaLanded(this, logAircraftName, idxOfPilotPlace);
        if(Front.isCaptured(this))
        {
            if(name().equals(playerParaName) || isWalkerPilot())
            {
                World.setPlayerCaptured();
                if(Config.isUSE_RENDER())
                    HUD.log("PlayerCAPT");
                if(Mission.isNet())
                    Chat.sendLog(1, "gore_captured", (NetUser)NetEnv.host(), null);
            }
            if(logAircraftName != null && (driver == null && isNetMaster() || driver != null && driver.isMaster()))
                EventLog.onCaptured(this, logAircraftName, idxOfPilotPlace);
        }
    }

    public boolean isChecksCaptured()
    {
        if(dying != 0)
            return true;
        else
            return bCheksCaptured;
    }

    private void Die(Actor actor)
    {
        Die(actor, true);
    }

    private void Die(Actor actor, boolean flag)
    {
        if(dying != 0)
            return;
        World.onActorDied(this, actor);
        if(actor != this)
        {
            if(name().equals(playerParaName) || isWalkerPilot())
            {
                World.setPlayerDead();
                if(Config.isUSE_RENDER())
                    HUD.log("Player_Killed");
                if(Mission.isNet())
                {
                    if((actor instanceof Aircraft) && ((Aircraft)actor).isNetPlayer() && Actor.isAlive(World.getPlayerAircraft()))
                        Chat.sendLogRnd(1, "gore_pkonchute", (Aircraft)actor, World.getPlayerAircraft());
                    Chat.sendLog(0, "gore_killed", (NetUser)NetEnv.host(), (NetUser)NetEnv.host());
                }
            }
            if(logAircraftName != null && (driver == null && isNetMaster() || driver != null && driver.isMaster()))
                if(Actor.isValid(actor) && actor != Engine.actorLand())
                    EventLog.onParaKilled(this, logAircraftName, idxOfPilotPlace, actor);
                else
                    EventLog.onPilotKilled(this, logAircraftName, idxOfPilotPlace);
        }
        dying = 1;
        if(isNet() && flag)
            try
            {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(68);
                if(actor != null)
                    netmsgguaranted.writeNetObj(actor.net);
                else
                    netmsgguaranted.writeNetObj(null);
                net.postExclude(null, netmsgguaranted);
            }
            catch(Exception exception) { }
    }

    public void destroy()
    {
        if(!HotKeyEnv.isEnabled("aircraftView"))
            HotKeyEnv.enable("aircraftView", true);
        Object aobj[] = getOwnerAttached();
        for(int i = 0; i < aobj.length; i++)
        {
            Chute chute = (Chute)aobj[i];
            if(Actor.isValid(chute))
                chute.destroy();
        }

        if(Mission.isPlaying() && World.cur() != null && driver != null && (driver.isMaster() || driver.isTrackWriter()))
            World.cur().checkViewOnPlayerDied(this);
        super.destroy();
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    void chuteTangled(Actor actor, boolean flag)
    {
        if(st == 1 || st == 2)
        {
            st = 3;
            animStartTime = Time.current();
            super.pos.setAbs(faceOrient);
            if(logAircraftName != null && (driver == null && isNetMaster() || driver != null && driver.isMaster()))
                EventLog.onChuteKilled(this, logAircraftName, idxOfPilotPlace, actor);
            if(isNet() && flag)
                try
                {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(83);
                    if(actor != null)
                        netmsgguaranted.writeNetObj(actor.net);
                    else
                        netmsgguaranted.writeNetObj(null);
                    super.net.postExclude(null, netmsgguaranted);
                }
                catch(Exception exception) { }
        }
    }

// st == 0 Eject
// st == 1 Open chute
// st == 2 ???
// st == 3 Touch down
// st == 4 Run
// st == 5 Hit the dirt
// st == 6 Take cover on ground
// st == 7 Dead pose
// st == 8 ???
// st == 9 ???

    void setAnimFrame(double d)
    {
        int startFrame;
        int lastFrame;
        float framePos;
        int cycleTime;
        double animStartTimeDouble;
        switch(st)
        {
// Eject
        case 0:
            startFrame = FREEFLY_START_FRAME;
            lastFrame = FREEFLY_LAST_FRAME;
            cycleTime = FREEFLY_CYCLE_TIME;
            animStartTimeDouble = d - (double)animStartTime;
            if(animStartTimeDouble <= 0.0D)
                framePos = 0.0F;
            else
            if(animStartTimeDouble >= (double)cycleTime)
                framePos = 1.0F;
            else
                framePos = (float)(animStartTimeDouble / (double)cycleTime);
            if(framePos >= 1.0F && dying != 0)
            {
                startFrame = lastFrame = FREEFLYDEAD_FRAME;
                framePos = 0.0F;
            }
            break;

// Open chute
        case 1:
            startFrame = PARAUP1_START_FRAME;
            lastFrame = PARAUP1_LAST_FRAME;
            cycleTime = PARAUP1_CYCLE_TIME;
            animStartTimeDouble = d - (double)animStartTime;
            if(animStartTimeDouble <= 0.0D)
            {
                framePos = 0.0F;
                break;
            }
            if(animStartTimeDouble >= (double)cycleTime)
                framePos = 1.0F;
            else
                framePos = (float)(animStartTimeDouble / (double)cycleTime);
            break;

        case 2:

// Touch down
        case 3:
            startFrame = PARAUP2_START_FRAME;
            lastFrame = PARAUP2_LAST_FRAME;
            cycleTime = PARAUP2_CYCLE_TIME;
            animStartTimeDouble = d - (double)animStartTime;
            if(animStartTimeDouble <= 0.0D)
                framePos = 0.0F;
            else
            if(animStartTimeDouble >= (double)cycleTime)
                framePos = 1.0F;
            else
                framePos = (float)(animStartTimeDouble / (double)cycleTime);
            if(framePos >= 1.0F && dying != 0)
            {
                startFrame = lastFrame = PARADEAD_FRAME;
                framePos = 0.0F;
            }
            break;

// Run
        case 4:
            startFrame = RUN_START_FRAME;
            lastFrame = RUN_LAST_FRAME;
            cycleTime = RUN_CYCLE_TIME;
            animStartTimeDouble = (d - (double)animStartTime) % cycleTime;
            if(animStartTimeDouble < 0.0D)
                animStartTimeDouble += cycleTime;
            framePos = (float)(animStartTimeDouble / (double)cycleTime);
// Stop animation
//            if(bControlled && joyX == 0.0F)
//                f = 0.5F;
            if(bControlled && bStanding)
                framePos = 0.0F;

            break;

// Hit the dirt
        case 5:
            startFrame = FALL_START_FRAME;
            lastFrame = FALL_LAST_FRAME;
            cycleTime = FALL_CYCLE_TIME;
            animStartTimeDouble = d - (double)animStartTime;
            if(animStartTimeDouble <= 0.0D)
            {
                framePos = 0.0F;
                break;
            }
            if(animStartTimeDouble >= (double)cycleTime)
                framePos = 1.0F;
            else
                framePos = (float)(animStartTimeDouble / (double)cycleTime);
            break;

// Take cover
        case 6:
            startFrame = LIE_START_FRAME;
            lastFrame = LIE_LAST_FRAME;
            cycleTime = LIE_CYCLE_TIME;
            animStartTimeDouble = d - (double)animStartTime;
            if(animStartTimeDouble <= 0.0D)
            {
                framePos = 0.0F;
                break;
            }
            if(animStartTimeDouble >= (double)cycleTime)
                framePos = 1.0F;
            else
                framePos = (float)(animStartTimeDouble / (double)cycleTime);
            break;

        case 8:
            return;

        case 9:
            return;

// Dead pose
        case 7:
        default:
            startFrame = lastFrame = LIEDEAD_START_FRAME + idxOfDeadPose;
            framePos = 0.0F;
            break;
        }
        mesh().setFrameFromRange(startFrame, lastFrame, framePos);
    }

    public int HitbyMask()
    {
        return -25;
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
            return 1;
        if(abulletproperties[0].powerType == 1)
            return 0;
        if(abulletproperties[1].powerType == 1)
            return 1;
        if(abulletproperties[0].cumulativePower > 0.0F)
            return 1;
        else
            return abulletproperties[0].powerType == 2 ? 1 : 0;
    }

    public int chooseShotpoint(BulletProperties bulletproperties)
    {
        return dying != 0 ? -1 : 0;
    }

    public boolean getShotpointOffset(int i, Point3d point3d)
    {
        if(dying != 0)
            return false;
        if(i != 0)
            return false;
        if(point3d != null)
            point3d.set(0.0D, 0.0D, 0.0D);
        return true;
    }

    private static String GetMeshName(int i)
    {
        return "3do/humans/Paratroopers/" + (i == 2 ? "Germany" : "Russia") + "/mono.sim";
    }

    private static String GetMeshName_Water(int i)
    {
        return "3do/humans/Paratroopers/Water/" + (i == 0 ? "JN_Jacket" : i == 1 ? "US_Jacket" : "US_Dinghy") + "/live.sim";
    }

    public void prepareSkin(String s, String s1, Mat amat[])
    {
        if(!Config.isUSE_RENDER())
            return;
        String s2 = "Pilot";
        int i = mesh().materialFind(s2);
        if(i < 0)
            return;
        Mat mat;
        if(FObj.Exist(s))
        {
            mat = (Mat)FObj.Get(s);
        } else
        {
            Mat mat1 = mesh().material(i);
            mat = (Mat)mat1.Clone();
            mat.Rename(s);
            mat.setLayer(0);
            mat.set('\0', s1);
        }
        if(amat != null)
            amat[0] = mat;
        mesh().materialReplace(s2, mat);
    }

    public Paratrooper(Actor actor, int i, int j, Loc loc, Vector3d vector3d, NetMsgInput netmsginput, int k)
    {
        super(GetMeshName(i));
        bControlled = false;
        bStanding = false;
        bWater = false;
//        tRunCycle = RUN_CYCLE_TIME;
        speedIn = new Vector3d();
        carrierDeck = null;
        Vship = new Vector3d();
        logAircraftName = null;
        swimMeshCode = -1;
        st = 0;
        dying = 0;
        bCheksCaptured = false;
        skinMat = null;
        if(Config.isUSE_RENDER() && (actor instanceof Aircraft))
        {
            Aircraft aircraft = (Aircraft)actor;
            String s = "Pilot1";
            if(j > 0)
                s = "Pilot2";
            int l = aircraft.hierMesh().materialFind(s);
            if(l >= 0)
            {
                Mat mat = aircraft.hierMesh().material(l);
                mesh().materialReplace("pilot", mat);
                skinMat = mat;
            } else
            if(j > 0)
            {
                int i1 = aircraft.hierMesh().materialFind("Pilot1");
                if(i1 >= 0)
                {
                    Mat mat1 = aircraft.hierMesh().material(i1);
                    mesh().materialReplace("pilot", mat1);
                    skinMat = mat1;
                }
            }
        }
        startOrient = new Orient();
        loc.get(startOrient);
        faceOrient = new Orient();
        faceOrient.set(startOrient);
        faceOrient.setYPR(faceOrient.getYaw(), 0.0F, 0.0F);
        Vector3d vector3d1 = new Vector3d();
        vector3d1.set(1.0D, 0.0D, 0.0D);
        faceOrient.transform(vector3d1);
        speed = new Vector3d();
        speed.set(vector3d);
        setOwner(actor);
        idxOfPilotPlace = j;
        setArmy(i);
        swimMeshCode = -1;
        if(Actor.isValid(actor) && (actor instanceof Aircraft))
        {
            String s1 = ((Aircraft)actor).getRegiment().country();
            if("us".equals(s1) || "gb".equals(s1))
                swimMeshCode = j == 0 ? 2 : 1;
            else
            if("ja".equals(s1))
                swimMeshCode = 0;
        }
        o.setAT0(speed);
        o.set(o.azimut(), 0.0F, 0.0F);
        super.pos.setAbs(loc);
        super.pos.reset();
        st = 0;
        animStartTime = Time.tick();
        paraBoardTime = Time.tickNext() + 5000L;
        dying = 0;
        setName("_para_" + _counter++);
        collide(true);
        super.draw = new SoldDraw();
        dreamFire(true);
        drawing(true);
        if(!interpEnd("move"))
            interpPut(new Move(), "move", Time.current(), null);
        if(Actor.isValid(actor))
            logAircraftName = EventLog.name(actor);
        if(netmsginput == null)
            super.net = new Master(this);
        else
            super.net = new Mirror(this, netmsginput, k);
        carrierDeck = null;
        fxFreeFall = newSound("pal.freefall", false);
        fxExplore = newSound("pal.explore", false);
        fxSpeedUp = newSound("pal.speedup", false);
        fxReverse = newSound("pal.reverse", false);
        fxSprint = newSound("pal.sprint", false);
//      Preload
        fxCockpit = newSound("pal.cockpit", false);
        fxFatality = newSound("pal.fatality", false);
        flashLight = new LightPointActor(new LightPointWorld(), new Point3d(12D, 0D, 3D));
        flashLight.light.setColor(1.0F, 1.0F, 1.0F);
        flashLight.light.setEmit(0.0F, 0.0F);
        draw.lightMap().put("Ground_Level", flashLight);
        bFlashLight = false;
//      0 = QWERTY/QWERTZ, 1 = AZERTY
        KEY_U = ngPALkeyboard == 0 ? 87 : 90; // Forwards (W/Z)
        KEY_D = ngPALkeyboard == 0 ? 83 : 83; // Backwards (S/S)
        KEY_L = ngPALkeyboard == 0 ? 65 : 81; // Left (A/Q)
        KEY_R = ngPALkeyboard == 0 ? 68 : 68; // Right (D/D)
        KEY_T = ngPALkeyboard == 0 ? 81 : 65; // Turn Around (Q/A)
        KEY_F = ngPALkeyboard == 0 ? 69 : 69; // Fire (E/E)
    }

    public boolean isWalkerPilot()
    {
        return name().equals(playerPalName);
    }

    private void testDriver()
    {
//        if(driver != null && (driver.isMaster() || driver.isTrackWriter()) && Actor.isValid(getOwner()) && getOwner() == World.getPlayerAircraft())
        if(driver != null && (driver.isMaster() || driver.isTrackWriter()) && Actor.isValid(getOwner()))
        {
            if(World.isPlayerGunner())
                World.doGunnerParatrooper(this);
            else
                World.doPlayerParatrooper(this);
            setName((Mission.isNet() || NetMissionTrack.isRecording() || NetMissionTrack.isPlaying() || Main3D.cur3D().isDemoPlaying()) ? playerParaName : playerPalName);
//            if(Main3D.cur3D().viewActor() != World.getPlayerAircraft() || !Mission.isSingle())
//                Main3D.cur3D().setViewFlow10(this, false);
            if(Mission.isNet())
                Chat.sendLog(1, "gore_bailedout", (NetUser)NetEnv.host(), (NetUser)null);
        }
        if(driver != null)
            driver.tryPreparePilot(this);
    }

    public static void doRemoveBodyFromPlane(HierMesh hiermesh, int i, boolean flag)
    {
        doRemoveBodyChunkFromPlane(hiermesh, "Pilot" + i, flag);
        doRemoveBodyChunkFromPlane(hiermesh, "Head" + i, flag);
        doRemoveBodyChunkFromPlane(hiermesh, "HMask" + i, flag);
        doRemoveBodyChunkFromPlane(hiermesh, "Pilot" + i + "a", flag);
        doRemoveBodyChunkFromPlane(hiermesh, "Head" + i + "a", flag);
        doRemoveBodyChunkFromPlane(hiermesh, "Pilot" + i + "FAK", flag);
        doRemoveBodyChunkFromPlane(hiermesh, "Head" + i + "FAK", flag);
        doRemoveBodyChunkFromPlane(hiermesh, "Pilot" + i + "FAL", flag);
        doRemoveBodyChunkFromPlane(hiermesh, "Head" + i + "FAL", flag);
    }

    protected static void doRemoveBodyChunkFromPlane(HierMesh hiermesh, String s, boolean flag)
    {
        if(hiermesh.chunkFindCheck(s + "_D0") != -1)
            hiermesh.chunkVisible(s + "_D0", flag);
        if(hiermesh.chunkFindCheck(s + "_D1") != -1)
            hiermesh.chunkVisible(s + "_D1", false);
    }

    public static void realPilotVulnerability(boolean flag)
    {
        bRealPilotVul = flag;
    }

    public Paratrooper(Actor actor, int i, int j, Loc loc, Vector3d vector3d)
    {
        this(actor, i, j, loc, vector3d, null, 0);
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        NetMsgSpawn netmsgspawn = new NetMsgSpawn(super.net);
        Point3d point3d = super.pos.getAbsPoint();
        netmsgspawn.writeFloat((float)point3d.x);
        netmsgspawn.writeFloat((float)point3d.y);
        netmsgspawn.writeFloat((float)point3d.z);
        Orient orient = super.pos.getAbsOrient();
        netmsgspawn.writeFloat(orient.getAzimut());
        netmsgspawn.writeFloat(orient.getTangage());
        netmsgspawn.writeFloat(orient.getKren());
        netmsgspawn.writeFloat((float)speed.x);
        netmsgspawn.writeFloat((float)speed.y);
        netmsgspawn.writeFloat((float)speed.z);
        netmsgspawn.writeByte(getArmy());
        if(getOwner() != null && netchannel != null && netchannel.isMirrored(getOwner().net))
            netmsgspawn.writeNetObj(getOwner().net);
        else
            netmsgspawn.writeNetObj(null);
        netmsgspawn.writeByte(idxOfPilotPlace);
        netmsgspawn.writeFloat(turn_para_on_height);
        netmsgspawn.writeByte(nRunCycles);
        netmsgspawn.writeNetObj(driver);
        return netmsgspawn;
    }

    static ClipFilter clipFilter = new ClipFilter();
//    private static final int FPS = 30;
    private static final int FREEFLY_START_FRAME = 0;
    private static final int FREEFLY_LAST_FRAME = 19;
    private static final int FREEFLY_N_FRAMES = 20;
    private static final int FREEFLY_CYCLE_TIME = 633;
    private static final int FREEFLY_ROT_TIME = 2500;
    private static final int PARAUP1_START_FRAME = 19;
    private static final int PARAUP1_LAST_FRAME = 34;
//    private static final int PARAUP1_N_FRAMES = 16;
    private static final int PARAUP1_CYCLE_TIME = 500;
    private static final int PARAUP2_START_FRAME = 34;
    private static final int PARAUP2_LAST_FRAME = 54;
//    private static final int PARAUP2_N_FRAMES = 21;
    private static final int PARAUP2_CYCLE_TIME = 666;
    private static final int RUN_START_FRAME = 55;
    private static final int RUN_LAST_FRAME = 77;
//    private static final int RUN_N_FRAMES = 23;
    private static final int RUN_CYCLE_TIME = 733;
    private static final int FALL_START_FRAME = 77;
    private static final int FALL_LAST_FRAME = 109;
//    private static final int FALL_N_FRAMES = 33;
    private static final int FALL_CYCLE_TIME = 1066;
    private static final int LIE_START_FRAME = 109;
    private static final int LIE_LAST_FRAME = 128;
//    private static final int LIE_N_FRAMES = 20;
    private static final int LIE_CYCLE_TIME = 633;
    private static final int LIEDEAD_START_FRAME = 129;
    private static final int LIEDEAD_N_FRAMES = 4;
    private static final int PARADEAD_FRAME = 133;
    private static final int FREEFLYDEAD_FRAME = 134;
    private static final float FREE_SPEED = 50F;
    private static final float PARA_SPEED = 5F;
    private static final float RUN_SPEED = 6.545455F;
    public static final String playerParaName = "_paraplayer_";
    public static final String playerPalName = "WalkerPilot";
    private String logAircraftName;
    private int idxOfPilotPlace;
    private NetUser driver;
    private int swimMeshCode;
    private Vector3d speed;
    private Orient startOrient;
    private Orient faceOrient;
    private static final int ST_FREEFLY = 0;
    private static final int ST_PARAUP1 = 1;
    private static final int ST_PARAUP2 = 2;
    private static final int ST_PARATANGLED = 3;
    private static final int ST_RUN = 4;
    private static final int ST_FALL = 5;
    private static final int ST_LIE = 6;
    private static final int ST_LIEDEAD = 7;
    private static final int ST_SWIM = 8;
    private static final int ST_DISAPPEAR = 9;
    private int KEY_U = 87; // Forwards (W)
    private int KEY_D = 83; // Backwards (S)
    private int KEY_L = 65; // Left (A)
    private int KEY_R = 68; // Right (D)
    private int KEY_T = 81; // Turn around (Q)
    private int KEY_F = 69; // Fire (E)
    private int st;
    private int dying;
    static final int DYING_NONE = 0;
    static final int DYING_DEAD = 1;
    private int idxOfDeadPose;
    private long animStartTime;
    private long disappearTime;
    private long paraBoardTime;
    private int nRunCycles;
    private float turn_para_on_height;
    private static int _counter = 0;
//    private static Mesh preload1 = null;
//    private static Mesh preload2 = null;
//    private static Mesh preload3 = null;
//    private static Mesh preload4 = null;
//    private static Mesh preload5 = null;
//    private static Mesh preload6 = null;
    private Mat skinMat;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
//    private static Vector3f n = new Vector3f();
    private static boolean bRealPilotVul = false;
    private boolean bCheksCaptured;
    private boolean bControlled;
    private boolean bStanding;
    private boolean bWater;
    private float joyX;
    private float joyY;
    private float joyZ;
    private boolean bAllowFire;
    private boolean bAllowTurn;
    private SoundFX fxFreeFall;
    private SoundFX fxExplore;
    private SoundFX fxSpeedUp;
    private boolean bSpeedUp;
    private SoundFX fxReverse;
    private SoundFX fxSprint;
    private SoundFX fxCockpit;
    private SoundFX fxFatality;
    private LightPointActor flashLight;
    private boolean bFlashLight;
//    private int tRunCycle;
    private Vector3d speedIn;
    private Actor carrierDeck;
    private Vector3d Vship;
    private static int ngPALkeyboard = 0;
    private static String ngPALversion = "NG-PAL v1.1";

    static
    {
        Spawn.add(Paratrooper.class, new SPAWN());
    }
}
