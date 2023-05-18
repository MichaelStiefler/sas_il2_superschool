// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/28/2012 7:49:15 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   F_18C.java

package com.maddox.il2.objects.air;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunAIM9L;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;

import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_18S, Aircraft, TypeFighterAceMaker, TypeRadarGunsight, 
//            Chute, PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, 
//            NetAircraft

public class KF_18C extends F_18S
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeTankerDrogue, TypeDockable
{

    public KF_18C()
    {
        guidedMissileUtils = null;
        fxSirena = newSound("aircraft.F4warning", false);
        smplSirena = new Sample("sample.F4warning.wav", 256, 65535);
        sirenaSoundPlaying = false;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        guidedMissileUtils = new GuidedMissileUtils(this);
        removeChuteTimer = -1L;
        smplSirena.setInfinite(true);
        bulletEmitters = null;
        windFoldValue = 0.0F;
        bomb = false;
        AGM = false;
        GuidedBomb = false;
        IR = false;
        backfire = false;
        missilesList = new ArrayList();
        backfireList = new ArrayList();
        tX4Prev = 0L;
        tX4PrevP = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        drones = new Actor[2];
    }
    
    public boolean typeDockableIsDocked()
    {
        return true;
    }

    public void typeDockableAttemptAttach()
    {
    }

    public void typeDockableAttemptDetach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                    typeDockableRequestDetach(drones[i], i, true);

        }
    }

    public void typeDockableRequestAttach(Actor actor)
    {
        if(actor instanceof Aircraft)
        {
            Aircraft aircraft = (Aircraft)actor;
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && ((SndAircraft) (aircraft)).FM.getSpeedKMH() > 10F && super.FM.getSpeedKMH() > 10F)
            {
                for(int i = 0; i < drones.length; i++)
                {
                    if(Actor.isValid(drones[i]))
                        continue;
                    HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    super.pos.getAbs(loc1);
                    hooknamed.computePos(this, loc1, loc);
                    actor.pos.getAbs(loc1);
                    if(loc.getPoint().distance(loc1.getPoint()) >= 7.5D)
                        continue;
                    if(((FlightModelMain) (super.FM)).AS.isMaster())
                        typeDockableRequestAttach(actor, i, true);
                    else
                        ((FlightModelMain) (super.FM)).AS.netToMaster(32, i, 0, actor);
                    break;
                }

            }
        }
    }

    public void typeDockableRequestDetach(Actor actor)
    {
        for(int i = 0; i < drones.length; i++)
            if(actor == drones[i])
            {
                Aircraft aircraft = (Aircraft)actor;
                if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster())
                    if(((FlightModelMain) (super.FM)).AS.isMaster())
                        typeDockableRequestDetach(actor, i, true);
                    else
                        ((FlightModelMain) (super.FM)).AS.netToMaster(33, i, 1, actor);
            }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
        if(i >= 0 && i <= 1)
            if(flag)
            {
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                {
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.netToMaster(34, i, 1, actor);
                }
            } else
            if(((FlightModelMain) (super.FM)).AS.isMaster())
            {
                if(!Actor.isValid(drones[i]))
                {
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                }
            } else
            {
                ((FlightModelMain) (super.FM)).AS.netToMaster(34, i, 0, actor);
            }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
        if(flag)
            if(((FlightModelMain) (super.FM)).AS.isMaster())
            {
                ((FlightModelMain) (super.FM)).AS.netToMirrors(35, i, 1, actor);
                typeDockableDoDetachFromDrone(i);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.netToMaster(35, i, 1, actor);
            }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
        if(!Actor.isValid(drones[i]))
        {
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            super.pos.getAbs(loc1);
            hooknamed.computePos(this, loc1, loc);
            HookNamed probe = new HookNamed((ActorMesh)actor, "_Probe");
            Loc loc2 = new Loc();
            probe.computePos(this, loc, loc2);
            actor.pos.setAbs(loc2);
            actor.pos.setBase(this, null, true);
            actor.pos.resetAsBase();
            drones[i] = actor;
            ((TypeDockable)drones[i]).typeDockableDoAttachToQueen(this, i);
        }
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
        if(Actor.isValid(drones[i]))
        {
            drones[i].pos.setBase(null, null, true);
            ((TypeDockable)drones[i]).typeDockableDoDetachFromQueen(i);
            drones[i] = null;
        }
    }

    public void typeDockableDoAttachToQueen(Actor actor1, int j)
    {
    }

    public void typeDockableDoDetachFromQueen(int j)
    {
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        for(int i = 0; i < drones.length; i++)
            if(Actor.isValid(drones[i]))
            {
                netmsgguaranted.writeByte(1);
                com.maddox.il2.engine.ActorNet actornet = drones[i].net;
                if(actornet.countNoMirrors() == 0)
                    netmsgguaranted.writeNetObj(actornet);
                else
                    netmsgguaranted.writeNetObj(null);
            } else
            {
                netmsgguaranted.writeByte(0);
            }

    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        for(int i = 0; i < drones.length; i++)
            if(netmsginput.readByte() == 1)
            {
                NetObj netobj = netmsginput.readNetObj();
                if(netobj != null)
                    typeDockableDoAttachToDrone((Actor)netobj.superObj(), i);
            }

    }

    
    private void checkAmmo()
    {
        missilesList.clear();
        for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
            if(FM.CT.Weapons[i] == null)
                continue;
            for(int j = 0; j < FM.CT.Weapons[i].length; j++)
            {
                if(!FM.CT.Weapons[i][j].haveBullets())
                    continue;
                if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65)
                {
                    AGM = true;
                    IR = true;
                    missilesList.add(FM.CT.Weapons[i][j]);
                }
                if(FM.CT.Weapons[i][j] instanceof RocketGunFlare)
                {
                    backfire = true;
                    backfireList.add(FM.CT.Weapons[i][j]);
                } else
                {
                    missilesList.add(FM.CT.Weapons[i][j]);
                }
                if(FM.CT.Weapons[i][j] instanceof RocketBombGun)
                {
                	GuidedBomb = true;
                    IR = true;
                    missilesList.add(FM.CT.Weapons[i][j]);
                }
                if(FM.CT.Weapons[i][j] instanceof BombGun)
                    bomb = true;
            }        
        }

    }
    
    public void launchMsl()
    {
        if(missilesList.isEmpty())
        {
            return;
        } else
        {
        ((RocketGunAGM65)missilesList.remove(0)).shots(1);
          return;
        }   
    }
    
    public void launchbmb()
    {
        if(missilesList.isEmpty())
        {
            return;
        } else
        {
        ((RocketBombGun)missilesList.remove(0)).shots(1);
          return;
        }   
    }
    
    public void backFire()
    {
        if(backfireList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGunFlare)backfireList.remove(0)).shots(3);
            return;
        }
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Tailhook_D0", 0.0F, 0.0F, 70F * f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 92F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -92F), 0.0F);
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    private boolean sirenaWarning()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(World.getPlayerAircraft() == null)
            return false;
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if((aircraft1 instanceof Aircraft) && aircraft1.getArmy() != World.getPlayerArmy() && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && aircraft1 != World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
        {
            super.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
            new String();
            new String();
            double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float)Math.atan2(d8, -d7);
            int k = (int)(Math.floor((int)f) - 90D);
            if(k < 0)
                k += 360;
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
            float f1 = 57.32484F * (float)Math.atan2(i1, d11);
            int j1 = (int)(Math.floor((int)f1) - 90D);
            if(j1 < 0)
                j1 += 360;
            int k1 = j1 - j;
            int l1 = (int)(Math.ceil(((double)i1 * 3.2808399000000001D) / 100D) * 100D);
            if(l1 >= 5280)
                l1 = (int)Math.floor(l1 / 5280);
            bRadarWarning = (double)i1 <= 3000D && (double)i1 >= 50D && k1 >= 195 && k1 <= 345 && Math.sqrt(l * l) >= 120D;
            playSirenaWarning(bRadarWarning);
        } else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag)
    {
        if(flag && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APR-36: Enemy at Six!");
        } else
        if(!flag && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.0F, 1.0F, 1.0F, 1.8F, 1.5F, 1.0F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        checkAmmo();
        guidedMissileUtils.onAircraftLoaded();
        FM.Skill = 3;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        bulletEmitters = new BulletEmitter[weponHookArray.length];
        for(int i = 0; i < weponHookArray.length; i++)
            bulletEmitters[i] = getBulletEmitterByHookName(weponHookArray[i]);

    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        sirenaWarning();
        if(FM.CT.getArrestor() > 0.2F)
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                arrestor = 0.8F * arrestor + 0.2F * f1;
                moveArrestorHook(arrestor);
            } else
            {
                float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
                if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                    f2 = 0.0F;
                if(f2 > 0.2F)
                    f2 = 0.2F;
                if(f2 > 0.0F)
                    arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
                else
                    arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
                if(arrestor < 0.0F)
                    arrestor = 0.0F;
                else
                if(arrestor > 1.0F)
                    arrestor = 1.0F;
                moveArrestorHook(arrestor);
            }
        super.update(f);
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.8F);
            ((Actor) (chute)).pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() > 600F || ((FlightModelMain) (super.FM)).CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 32000D;
        if(((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 32000D;
        if(super.FM.getAltitude() > 10000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3500D;
        if(super.FM.getAltitude() > 10000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3500D;
        if(super.FM.getAltitude() > 10000F && (double)calculateMach() >= 1.8100000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 10000F && (double)calculateMach() >= 1.8100000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 11000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 1000D;
        if(super.FM.getAltitude() > 11000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 1000D;
        if(super.FM.getAltitude() > 12000F && (double)calculateMach() >= 1.6699999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 12000F && (double)calculateMach() >= 1.6699999999999999D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 12500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
        if(super.FM.getAltitude() > 12500F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
        if(super.FM.getAltitude() > 12500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 1000D;
        if(super.FM.getAltitude() > 12500F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 1000D;
        if(super.FM.getAltitude() > 12500F && (double)calculateMach() >= 1.6399999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 12500F && (double)calculateMach() >= 1.6399999999999999D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 13000F && (double)calculateMach() >= 1.6000000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 13000F && (double)calculateMach() >= 1.6000000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 13500F && (double)calculateMach() >= 1.55D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 13500F && (double)calculateMach() >= 1.55D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 14000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3500D;
        if(super.FM.getAltitude() > 14000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3500D;
        if(super.FM.getAltitude() > 14000F && (double)calculateMach() >= 1.47D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 14000F && (double)calculateMach() >= 1.47D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 14500F && (double)calculateMach() >= 1.4299999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 14500F && (double)calculateMach() >= 1.4299999999999999D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 15000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3500D;
        if(super.FM.getAltitude() > 15000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3500D;
        if(super.FM.getAltitude() > 15000F && (double)calculateMach() >= 1.3300000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 15000F && (double)calculateMach() >= 1.3300000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 15500F && (double)calculateMach() >= 1.23D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 15500F && (double)calculateMach() >= 1.23D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 15800F && (double)calculateMach() >= 1.1899999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 15800F && (double)calculateMach() >= 1.1899999999999999D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 16000F && (double)calculateMach() >= 1.1399999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 16000F && (double)calculateMach() >= 1.1399999999999999D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 16300F && (double)calculateMach() >= 1.1000000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 16300F && (double)calculateMach() >= 1.1000000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 16000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5200D;
        if(super.FM.getAltitude() > 16000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5200D;
        if(super.FM.getAltitude() > 17000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5500D;
        if(super.FM.getAltitude() > 17000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5500D;
        if(super.FM.getAltitude() > 18000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3900D;
        if(super.FM.getAltitude() > 18000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3900D;
        if(super.FM.getAltitude() > 18800F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5500D;
        if(super.FM.getAltitude() > 18800F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5500D;
        if(super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 7000D;
        if(super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 7000D;
        if(super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 17000D;
        if(super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 17000D;
        if(super.FM.getAltitude() < 1000F || (double)((FlightModelMain) (super.FM)).CT.getGear() > 0.0D || (double)((FlightModelMain) (super.FM)).CT.getArrestor() > 0.0D)
        {
            hierMesh().chunkVisible("FuelLine1_D0", false);
            hierMesh().chunkVisible("Drogue1_D0", false);
        } else
        {
            hierMesh().chunkVisible("FuelLine1_D0", true);
            hierMesh().chunkVisible("Drogue1_D0", true);
        }
    }
    
    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !flag || !(FM instanceof Pilot))
            return;
        if((!missilesList.isEmpty() || !backfireList.isEmpty()) && Time.current() > tX4Prev + 100L + (IR ? 5000L : 0L))
        {
            Pilot pilot = (Pilot)FM;
            if(pilot.get_maneuver() == 43 && pilot.target_ground != null)
            {
                Point3d point3d = new Point3d();
                pilot.target_ground.pos.getAbs(point3d);
                point3d.sub(FM.Loc);
                FM.Or.transformInv(point3d);
                if(point3d.x > 1800D && point3d.x < (IR ? 2250D : 1250D) + 1500D * (double)FM.Skill)
                {
                    if(!IR)
                        point3d.x /= 2 - FM.Skill / 3;
                    if(point3d.y < point3d.x && point3d.y > -point3d.x && point3d.z * 1.5D < point3d.x && point3d.z * 1.5D > -point3d.x)
                    {
                        launchMsl();
                        launchbmb();
                        tX4Prev = Time.current();
                        Voice.speakAttackByRockets(this);
                    }
                }
            } else
            	if(pilot.target != null || pilot.danger != null)
                {
                    Point3d point3d1 = new Point3d();
                    Orientation orientation = new Orientation();
                    Object obj;
                    if(pilot.target != null && pilot.target.actor != null)
                        obj = pilot.target.actor;
                    else
                        obj = (Aircraft)pilot.danger.actor;
                    if(isValid(((Actor) (obj))) && (obj instanceof Aircraft))
                    {
                        ((Actor) (obj)).pos.getAbs(point3d1, orientation);
                        point3d1.sub(FM.Loc);
                        FM.Or.transformInv(point3d1);
                        if(!backfireList.isEmpty() && FM.Loc.z > World.land().HQ_Air(FM.Loc.x, FM.Loc.y) + 10D)
                        {
                            Pilot pilot1 = (Pilot)((Aircraft)obj).FM;
                            if(pilot1.isCapableOfACM() && point3d1.x < -50D && Math.abs(point3d1.y) < -point3d1.x / 3D && Math.abs(point3d1.z) < -point3d1.x / 3D)
                            {
                                Orientation orientation2 = new Orientation();
                                FM.getOrient(orientation2);
                                float f3 = Math.abs(orientation2.getAzimut() - orientation.getAzimut()) % 360F;
                                f3 = f3 <= 180F ? f3 : 360F - f3;
                                f3 = f3 <= 90F ? f3 : 180F - f3;
                                float f4 = Math.abs(orientation2.getTangage() - orientation.getTangage()) % 360F;
                                f4 = f4 <= 180F ? f4 : 360F - f4;
                                f4 = f4 <= 90F ? f4 : 180F - f4;
                                double d1 = (-point3d1.x * (4.5D - (double)FM.Skill)) / (double)(((Aircraft)obj).FM.getSpeed() + 1.0F);
                                if((double)f3 < d1 && (double)f4 < d1)
                                {
                                    backFire();
                                    tX4Prev = Time.current();
                                }
                            }
                        }
                    }
                }
            }
        }        

    public void updateHook()
    {
        for(int i = 0; i < weponHookArray.length; i++)
            try
            {
                if(bulletEmitters[i] instanceof RocketGunAIM9L)
                    ((RocketGunAIM9L)bulletEmitters[i]).updateHook(weponHookArray[i]);
            }
            catch(Exception exception) { }

    }

    public void moveWingFold(float f)
    {
        moveWingFold(hierMesh(), f);
        super.moveWingFold(f);
        if(windFoldValue != f)
        {
            windFoldValue = f;
            super.needUpdateHook = true;
        }
    }

    static String weponHookArray[] = {
        "_CANNON01", "_Extmis05", "_Extmis06", "_Extmis07", "_Extmis08", "_Extmis10", "_Extmis11", "_Extmis12", "_Extmis13", "_ExtDev05", 
        "_ExtTank03", "_ExtDev01", "_ExtDev02", "_ExtDev03", "_ExtDev04", "_ExtTank01", "_ExtTank02", "_Extmis14", "_Extmis15", "_Extmis16", 
        "_Extmis17", "_Extmis01", "_Extmis02", "_Extmis03", "_Extmis04", "_Extmis18", "_Extmis19", "_Extmis20", "_Extmis21", "_ExtDev06", 
        "_ExtDev07", "_ExtDev08", "_ExtDev09", "_ExtBomb01", "_ExtBomb02", "_ExtBomb03", "_ExtBomb04", "_ExtBomb05", "_ExtBomb06", "_ExtBomb07", 
        "_ExtBomb08", "_ExtPlchd1", "_Extmis22", "_Extmis23", "_Extmis24", "_Extmis25", "_Extmis26", "_Extmis27", "_Extmis28", "_Extmis29", 
        "_Extmis30", "_Extmis31", "_Extmis32", "_Extmis33"
    };
    BulletEmitter bulletEmitters[];
    private GuidedMissileUtils guidedMissileUtils;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private boolean bRadarWarning;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    public boolean bToFire;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private float arrestor;
    private int pk;
    float windFoldValue;
    private long tX4Prev;
    private long tX4PrevP;
    private float deltaAzimuth;
    private float deltaTangage;
    private boolean bomb;
    private boolean AGM;
    private boolean GuidedBomb;
    private boolean IR;
    private ArrayList missilesList;
    private ArrayList backfireList;
    private boolean backfire;
    private Actor drones[];

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-18C");
        Property.set(class1, "meshName", "3DO/Plane/F-18C/KC-18.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/F-18C.fmd:F18_FM");       
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 2, 2, 2, 2, 2, 2, 2, 2, 9, 
            9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 
            9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 
            3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_Extmis05", "_Extmis06", "_Extmis07", "_Extmis08", "_Extmis10", "_Extmis11", "_Extmis12", "_Extmis13", "_ExtDev05", 
            "_ExtTank03", "_ExtDev01", "_ExtDev02", "_ExtDev03", "_ExtDev04", "_ExtTank01", "_ExtTank02", "_Extmis14", "_Extmis15", "_Extmis16", 
            "_Extmis17", "_Extmis01", "_Extmis02", "_Extmis03", "_Extmis04", "_Extmis18", "_Extmis19", "_Extmis20", "_Extmis21", "_ExtDev06", 
            "_ExtDev07", "_ExtDev08", "_ExtDev09", "_ExtBomb01", "_ExtBomb02", "_ExtBomb03", "_ExtBomb04", "_ExtBomb05", "_ExtBomb06", "_ExtBomb07", 
            "_ExtBomb08", "_ExtPlchd1", "_Extmis22", "_Extmis23", "_Extmis24", "_Extmis25", "_Extmis26", "_Extmis27", "_Extmis28", "_Extmis29", 
            "_Extmis30", "_Extmis31", "_Extmis32", "_Extmis33", "_Flare01", "_Flare02", "_Extmis34", "_Extmis35", "_Extmis36", "_Extmis37",
            "_Extmis38", "_Extmis39", "_Extmis40", "_Extmis41", "_Extmis42", "_Extmis43", "_Extmis44", "_Extmis45", "_Extmis46", "_Extmis47",
            "_Extmis48", "_Extmis49"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 72;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}