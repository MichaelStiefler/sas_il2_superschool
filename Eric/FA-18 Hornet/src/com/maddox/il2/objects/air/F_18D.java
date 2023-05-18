// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/28/2012 7:49:15 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   F_18D.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.FuelTankGun;
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
import com.maddox.il2.ai.air.Maneuver;
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
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;

import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_18S, Aircraft, TypeFighterAceMaker, TypeRadarGunsight, 
//            Chute, PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, 
//            NetAircraft

public class F_18D extends F_18S
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeStormovikArmored
{

    public F_18D()
    {
    	obsLookTime = 0;
        obsLookAzimuth = 0.0F;
        obsLookElevation = 0.0F;
        obsAzimuth = 0.0F;
        obsElevation = 0.0F;
        obsAzimuthOld = 0.0F;
        obsElevationOld = 0.0F;
        obsMove = 0.0F;
        obsMoveTot = 0.0F;
        bObserverKilled = false;
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
        FuelTank = false;
        missilesList = new ArrayList();
        backfireList = new ArrayList();
        tX4Prev = 0L;
        tX4PrevP = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;  
        fuelmode = 0;
        lTimeNextEject = 0L;
        overrideBailout = false;
        ejectComplete = false;
    }
    
    public float checkfuel(int i)
    {
    	FuelTank[] fuelTanks = FM.CT.getFuelTanks();
    	for(i = 0; i < fuelTanks.length; i++)
    		Fuelamount = fuelTanks[i].Fuel;
    	return Fuelamount;
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
                if(FM.CT.Weapons[i][j] instanceof RocketGunFlareF18)
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
            ((RocketGunFlareF18)backfireList.remove(0)).shots(3);
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
    
    public void moveRefuel(float f)
    {
        hierMesh().chunkSetAngles("fueldoor_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, 35F), 0.0F);
        hierMesh().chunkSetAngles("fueldoor2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 90F));
        hierMesh().chunkSetAngles("fueldoor3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.8F, 1.0F, 0.0F, -90F));
        hierMesh().chunkSetAngles("rod2", 0.0F, Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -85F), 0.0F);
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
        FM.turret[0].bIsAIControlled = false;
        bHasDeployedDragChute = false;
        bulletEmitters = new BulletEmitter[weponHookArray.length];
        for(int i = 0; i < weponHookArray.length; i++)
            bulletEmitters[i] = getBulletEmitterByHookName(weponHookArray[i]);        
    }

    public void update(float f)
    {
    	if((((FlightModelMain) (super.FM)).AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout1();
        }       
    	if(obsMove < obsMoveTot && !bObserverKilled && !((FlightModelMain) (super.FM)).AS.isPilotParatrooper(1))
        {
            if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
                obsMove += 0.29999999999999999D * (double)f;
            else
            if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
                obsMove += 0.15F;
            else
                obsMove += 1.2D * (double)f;
            obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
            obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
            hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
        }
    	if(bNeedSetup)
            checkAsDrone();
    	guidedMissileUtils.update();
    	int i = aircIndex();
        if(super.FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);
                    if(((FlightModelMain) (super.FM)).AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)super.FM).setDumbTime(3000L);
                }
                if(((FlightModelMain) (super.FM)).M.fuel < ((FlightModelMain) (super.FM)).M.maxFuel)
                    ((FlightModelMain) (super.FM)).M.fuel += 20F * f;
            } else
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                if(((FlightModelMain) (super.FM)).CT.GearControl == 0.0F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0)
                    ((FlightModelMain) (super.FM)).EI.setEngineRunning();
                if(dtime > 0L && ((Maneuver)super.FM).Group != null)
                {
                    ((Maneuver)super.FM).Group.leaderGroup = null;
                    ((Maneuver)super.FM).set_maneuver(22);
                    ((Pilot)super.FM).setDumbTime(3000L);
                    if(Time.current() > dtime + 3000L)
                    {
                        dtime = -1L;
                        ((Maneuver)super.FM).clear_stack();
                        ((Maneuver)super.FM).set_maneuver(0);
                        ((Pilot)super.FM).setDumbTime(0L);
                    }
                } else
                if(((FlightModelMain) (super.FM)).AP.way.curr().Action == 0)
                {
                    Maneuver maneuver = (Maneuver)super.FM;
                    if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                        maneuver.Group.setGroupTask(2);
                }
            }
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
    }
    
    public void doEjectCatapultInstructor()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 60D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat02");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
    }

    public void doEjectCatapultStudent()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 60D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat2_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
    }
    
    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if(queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
    }

    public void missionStarting()
    {
        checkAsDrone();
    }

    private void checkAsDrone()
    {
        if(target_ == null)
        {
            if(((FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null)
                ((FlightModelMain) (super.FM)).AP.way.next();
            target_ = ((FlightModelMain) (super.FM)).AP.way.curr().getTarget();
            if(Actor.isValid(target_) && (target_ instanceof Wing))
            {
                Wing wing = (Wing)target_;
                int i = aircIndex();
                if(Actor.isValid(wing.airc[i / 2]))
                    target_ = wing.airc[i / 2];
                else
                    target_ = null;
            }
        }
        if(Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue))
        {
            queen_last = target_;
            queen_time = Time.current();
            if(isNetMaster())
                ((TypeDockable)target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
        }
        bNeedSetup = false;
        target_ = null;
    }

    public int typeDockableGetDockport()
    {
        if(typeDockableIsDocked())
            return dockport_;
        else
            return -1;
    }

    public Actor typeDockableGetQueen()
    {
        return queen_;
    }

    public boolean typeDockableIsDocked()
    {
        return Actor.isValid(queen_);
    }

    public void typeDockableAttemptAttach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
            {
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
                ((FlightModelMain) (super.FM)).CT.RefuelControl = 1F;
            } else  
            {
                ((FlightModelMain) (super.FM)).CT.RefuelControl = 0F;
            }	
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable)queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor)
    {
    }

    public void typeDockableRequestDetach(Actor actor)
    {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        ((FlightModelMain) (super.FM)).EI.setEngineRunning();
        ((FlightModelMain) (super.FM)).CT.setGearAirborne();
        moveGear(0.0F);
        com.maddox.il2.fm.FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
        if(aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            Maneuver maneuver1 = (Maneuver)super.FM;
            if(maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1)
            {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
        if(dockport_ == i)
        {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        if(typeDockableIsDocked())
        {
            netmsgguaranted.writeByte(1);
            com.maddox.il2.engine.ActorNet actornet = null;
            if(Actor.isValid(queen_))
            {
                actornet = queen_.net;
                if(actornet.countNoMirrors() > 0)
                    actornet = null;
            }
            netmsgguaranted.writeByte(dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else
        {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        if(netmsginput.readByte() == 1)
        {
            dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if(netobj != null)
            {
                Actor actor = (Actor)netobj.superObj();
                ((TypeDockable)actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }
    
    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(!bObserverKilled)
        {
            if(obsLookTime == 0)
            {
                obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
                obsMove = 0.0F;
                obsAzimuthOld = obsAzimuth;
                obsElevationOld = obsElevation;
                if((double)World.Rnd().nextFloat() > 0.80000000000000004D)
                {
                    obsAzimuth = 0.0F;
                    obsElevation = 0.0F;
                } else
                {
                    obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                    obsElevation = World.Rnd().nextFloat() * 50F - 20F;
                }
            } else
            {
                obsLookTime--;
            } 
       }
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !flag || !(super.FM instanceof Pilot))
            return;
        if(flag && ((FlightModelMain) (super.FM)).AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).Or.getKren()) < 3F)
            if(super.FM.isPlayers())
            {
                if((super.FM instanceof RealFlightModel) && !((RealFlightModel)super.FM).isRealMode())
                {
                    typeDockableAttemptDetach();
                    ((Maneuver)super.FM).set_maneuver(22);
                    ((Maneuver)super.FM).setCheckStrike(false);
                    ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
                    dtime = Time.current();
                }
            } else
            {
                typeDockableAttemptDetach();
                ((Maneuver)super.FM).set_maneuver(22);
                ((Maneuver)super.FM).setCheckStrike(false);
                ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
                dtime = Time.current();
            }
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
                                    //backFire();
                                    tX4Prev = Time.current();
                                    super.FM.CT.WeaponControl[7] = true;
                                }
                                if(tX4Prev + 1000L > Time.current())
                                	super.FM.CT.WeaponControl[7] = false;
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
    
    public void bailout1()
    {
        if(overrideBailout)
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 0 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep < 2)
            {
                if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.5F)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
                else
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 2;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 2 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 3)
            {
                switch(((FlightModelMain) (super.FM)).AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl < 0.5F)
                        doRemoveBlisters2();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 200L;
                    break;
                }
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                ((FlightModelMain) (super.FM)).AS.astateBailoutStep = (byte)(((FlightModelMain) (super.FM)).AS.astateBailoutStep + 1);
                if(((FlightModelMain) (super.FM)).AS.astateBailoutStep == 4)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 11 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 19)
            {
                byte byte0 = ((FlightModelMain) (super.FM)).AS.astateBailoutStep;
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                ((FlightModelMain) (super.FM)).AS.astateBailoutStep = (byte)(((FlightModelMain) (super.FM)).AS.astateBailoutStep + 1);
                if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(((FlightModelMain) (super.FM)).AS.actor != World.getPlayerAircraft())
                        ((Maneuver)super.FM).set_maneuver(44);
                }
                if(((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11] < 99)
                {
                    if(byte0 == 11)
                    {
                        doRemoveBodyFromPlane(2);
                        doEjectCatapultStudent();
                        lTimeNextEject = Time.current() + 200L;
                    } else
                    if(byte0 == 12)
                    {
                        doRemoveBodyFromPlane(1);
                        doEjectCatapultInstructor();
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 51;
                        super.FM.setTakenMortalDamage(true, null);
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    ((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11] = 99;
                } else
                {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + ((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11]);
                }
            }
    }
    
    protected final void doRemoveBlisters2()
    {
        for(int i = 1; i < 2; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(((FlightModelMain) (super.FM)).Vwld);
                wreckage.setSpeed(vector3d);
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
    public boolean FuelTank;
    private boolean IR;
    private ArrayList missilesList;
    private ArrayList backfireList;
    private boolean backfire;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    public int fuelmode;
    public float Fuelamount;
    public float v1;
    public static int Rocket1;   
    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    boolean bObserverKilled;
    private long lTimeNextEject;
    private boolean ejectComplete;
    private boolean overrideBailout;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-18D");
        Property.set(class1, "meshName", "3DO/Plane/F-18D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/F-18D.fmd:F18_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF18D.class, com.maddox.il2.objects.air.CockpitF18CFLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 2, 2, 2, 2, 2, 2, 2, 2, 9, 
            9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 
            9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 
            3, 9, 3, 3, 7, 7, 3, 3, 3, 3, 
            3, 3, 3, 3, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 8, 8 
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_Extmis05", "_Extmis06", "_Extmis07", "_Extmis08", "_Extmis10", "_Extmis11", "_Extmis12", "_Extmis13", "_ExtDev05", 
            "_ExtTank03", "_ExtDev01", "_ExtDev02", "_ExtDev03", "_ExtDev04", "_ExtTank01", "_ExtTank02", "_Extmis14", "_Extmis15", "_Extmis16", 
            "_Extmis17", "_Extmis01", "_Extmis02", "_Extmis03", "_Extmis04", "_Extmis18", "_Extmis19", "_Extmis20", "_Extmis21", "_ExtDev06", 
            "_ExtDev07", "_ExtDev08", "_ExtDev09", "_ExtBomb01", "_ExtBomb02", "_ExtBomb03", "_ExtBomb04", "_ExtBomb05", "_ExtBomb06", "_ExtBomb07", 
            "_ExtBomb08", "_ExtPlchd1", "_Extmis22", "_Extmis23", "_Extmis24", "_Extmis25", "_Extmis26", "_Extmis27", "_Extmis28", "_Extmis29", 
            "_Extmis30", "_Extmis31", "_Extmis32", "_Extmis33", "_Flare01", "_Flare02", "_Extmis34", "_Extmis35", "_Extmis36", "_Extmis37",
            "_Extmis38", "_Extmis39", "_Extmis40", "_Extmis41", "_Extmis42", "_Extmis43", "_Extmis44", "_Extmis45", "_Extmis46", "_Extmis47",
            "_Extmis48", "_Extmis49", "_ExtBomb09", "_ExtBomb10", "_ExtBomb11", "_ExtBomb12", "_ExtBomb13", "_ExtBomb14", "_ExtBomb15", "_ExtBomb16",
            "_ExtBomb17", "_ExtBomb18", "_ExtBomb19", "_ExtBomb20", "_Chaff01", "_Chaff02"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 86;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 6xAIM-7";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 6xAIM-7 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 4xAIM-7 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 4xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 8xAIM-7";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[6] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[8] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "(BS)8xAIM-7 + 2xAIM-9 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "(BS)10xAIM-7 + 2xAIM-9 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[6] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[8] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "(BS)8xAIM-120 + 2xAIM-9 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[4] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "(BS)8xAIM-120 + 2xAIM-9 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[4] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[6] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[8] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 6xAIM-120 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 10xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 10xAIM-120 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-9 + 6xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-9 + 6xAIM-120 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-9 + 4xAIM-120 + 2xAIM-7";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-9 + 2xAIM-7 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-9 + 2xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "(BS)2xAIM-120 + 10xAIM-9 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[4] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[6] =  new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[8] =  new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "(BS)2xAIM-120 + 8xAIM-9 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
            a_lweaponslot[4] =  new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-7 + 4xMk82LGB + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 4xAIM-7 + 4xJDAM84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65 + 2xAIM-9 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65 + 2xAIM-9 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAGM65B + 2xAIM-9 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAGM65B + 2xAIM-9 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B + 2xAGM65D + 2xAIM-9 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B + 2xAGM65D + 2xAIM-9 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM-84 + 2xAGM65B + 2xAGM65D + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(6, "RocketGunAGM84B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(6, "RocketGunAGM84B", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82LGB + 2xAGM65 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM84 + 2xAGM65 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82LGB + 2xAGM65 + 2xAIM-9 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM-7 + 2xAGM65 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65 + 6xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 2xZuni + 6xMk83";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65 + 6xMk82Snakeyes + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xZuni + 6xMk82Snakeyes + 2xAIM-9 + 2xAIM-7";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xZuni + 6xMk82Snakeyes + 2xAIM-9 + 2xAIM-7";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM-65D + 2xAGM-65B + 6xMk82Snakeyes + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM-65D + 2xAGM-65B + 6xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 6xZuni + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 2xAGM-84 + 6xZuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82LGB + 2xZuni + 2xAIM-9 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xZuni + 4xMk82LGB + 2xAIM-9 + 2xAIM-7 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM84 + 2xZuni + 2xAIM-9 + 2xAIM-7 + 1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 2xZuni + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 4xAIM-7 +2xAGM-84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 6xAIM-7 +2xAGM-84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 +2xAGM-84 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 +2xAGM-84 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 4xAGM-84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-9 + 2xAIM-7 + 2xAGM-84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7  +2xHARM + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunHARM", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunHARM", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 4xAIM-7 +2xHARM";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunHARM", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunHARM", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 +2xHARM + 2xZuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunHARM", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(6, "RocketGunHARM", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 6xAIM-7 +2xAGM-88";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunHARM", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunHARM", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);

            a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 2xJDAM84 + 6xZuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 6xAIM-7 +2xJDAM84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 2xJDAM84 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 2xJDAM84 + 2xZuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 2xJDAM84 + 2xAGM-65";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(4, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-9 + 2xAIM-7 + 2xJDAM84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xAIM-7 + 4xJDAM84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 4xAIM-7 +2xLGB-12";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xLGB-12 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xLGB-12 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xLGB-12 + 2xZuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_ZuniF18", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xLGB-12 + 2xAGM-65";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunAGM65", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(4, "RocketGunAGM65", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-9 + 2xLGB-12";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 4xLGB-12";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM154C + 2xAIM-9 + 2xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunAGM_154", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunAGM_154", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "AGM154C + HARM + 2xAIM-9 + 2xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunAGM_154", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunHARM", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "AGM154A + HARM + 2xAIM-9 + 2xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunAGM_154A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunHARM", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM154A + 2xAIM-9 + 2xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunAGM_154A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunAGM_154A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM154B + 2xAIM-9 + 2xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunAGM_154B", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunAGM_154B", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM154B + 1xAGM154A + 2xAIM-9 + 2xAIM-7 + 3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61B", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunAGM_154B", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunAGM_154A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18W", 1);
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlareF18", 70);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
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