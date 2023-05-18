// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/3/2012 10:11:10 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Mig_19S.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.FuelTankGun_Tank19;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunAGM65;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;

import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Mig_19, Aircraft, TypeFighterAceMaker, TypeRadarGunsight, 
//            Chute, PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, 
//            NetAircraft

public class SM_12 extends Mig_19
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump
{

    public SM_12()
    {
        fxSirena = newSound("aircraft.Sirena2", false);
        smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        guidedMissileUtils = null;
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
        flame = null;
        dust = null;
        trail = null;
        sprite = null;
        turboexhaust = null;
        bOxidiserLeak = false;
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
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
        gfactors.setGFactors(1.5F, 1.5F, 1.5F, 2.2F, 2.0F, 1.5F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        droptank();
        guidedMissileUtils.onAircraftLoaded();
        FM.Skill = 3;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        if(thisWeaponsName.endsWith("2xNR-30s"))
        {
            hierMesh().chunkVisible("GunL", true);
            hierMesh().chunkVisible("GunR", true);
        }
        if(Config.isUSE_RENDER())
        {
            flame = Eff3DActor.New(this, findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            dust = Eff3DActor.New(this, findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109D.eff", -1F);
            trail = Eff3DActor.New(this, findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", -1F);
            sprite = Eff3DActor.New(this, findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", -1F);
            turboexhaust = Eff3DActor.New(this, findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
            Eff3DActor.setIntesity(flame, 0.0F);
            Eff3DActor.setIntesity(dust, 0.0F);
            Eff3DActor.setIntesity(trail, 0.0F);
            Eff3DActor.setIntesity(sprite, 0.0F);
            Eff3DActor.setIntesity(turboexhaust, 1.0F);
        }
    }
    
    public void destroy()
    {
        if(Actor.isValid(flame))
            flame.destroy();
        if(Actor.isValid(dust))
            dust.destroy();
        if(Actor.isValid(trail))
            trail.destroy();
        if(Actor.isValid(sprite))
            sprite.destroy();
        if(Actor.isValid(turboexhaust))
            turboexhaust.destroy();
        super.destroy();
    }
    
    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(Config.isUSE_RENDER())
        {
            if(oldVwld < 20F && super.FM.getSpeed() > 20F)
            {
                Eff3DActor.finish(turboexhaust);
                turboexhaust = Eff3DActor.New(this, findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallTSPD.eff", -1F);
            }
            if(oldVwld > 20F && super.FM.getSpeed() < 20F)
            {
                Eff3DActor.finish(turboexhaust);
                turboexhaust = Eff3DActor.New(this, findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
            }
            oldVwld = super.FM.getSpeed();
        }
        if(flag && bOxidiserLeak)
            if(World.Rnd().nextFloat() < 0.2F)
                ((FlightModelMain) (super.FM)).AS.hitEngine(this, 2, 100);
            else
            if(World.Rnd().nextFloat() < 0.2F)
                ((FlightModelMain) (super.FM)).EI.engines[2].setEngineDies(this);
    }
    
    private final void doRemovedroptankL()
    {
        if(hierMesh().chunkFindCheck("DroptankL") != -1)
        {
            hierMesh().hideSubTrees("DroptankL");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("DroptankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }
    
    private final void doRemovedroptankR()
    {
        if(hierMesh().chunkFindCheck("DroptankR") != -1)
        {
            hierMesh().hideSubTrees("DroptankR");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("DroptankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }
    
    private void droptank()
    {
    	for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
        if(FM.CT.Weapons[i] == null)
              continue;
          for(int j = 0; j < FM.CT.Weapons[i].length; j++)
        {
        if(!FM.CT.Weapons[i][j].haveBullets())
               continue;
        if(FM.CT.Weapons[i][j] instanceof FuelTankGun_Tank19)
        {	
        	havedroptank = true;
        	super.hierMesh().chunkVisible("DroptankL", true);
        	super.hierMesh().chunkVisible("DroptankR", true);
        }
    } 
    }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        sirenaWarning();
        super.update(f);
        if(havedroptank == true)
        {	
        if(!FM.CT.Weapons[9][1].haveBullets())	       	
        {
        	havedroptank = false;
        	doRemovedroptankL();
        	doRemovedroptankR();
        }
        }
        if(((FlightModelMain) (super.FM)).AS.isMaster())
        {
            if(Config.isUSE_RENDER())
                if(((FlightModelMain) (super.FM)).EI.engines[2].getw() > 0.0F && ((FlightModelMain) (super.FM)).EI.engines[2].getStage() == 6)
                    doSetSootState(2, 8);
                else
                    doSetSootState(2, 7);
            if(prevThtl < 0.85F)
                ((FlightModelMain) (super.FM)).EI.engines[2].setControlThrottle(0.0F);
            else
            if(prevThtl < 0.95F)
                ((FlightModelMain) (super.FM)).EI.engines[2].setControlThrottle(0.85F);
            else
            if(prevThtl < 1.0F)
                ((FlightModelMain) (super.FM)).EI.engines[2].setControlThrottle(0.95F);
            else
                ((FlightModelMain) (super.FM)).EI.engines[2].setControlThrottle(1.0F);
            if(prevThtl != ((FlightModelMain) (super.FM)).CT.PowerControl)
                prevThtl = ((FlightModelMain) (super.FM)).CT.PowerControl;
            if(prevThtl == 0.0F)
            {
                if(!((FlightModelMain) (super.FM)).Gears.onGround() && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() && ((FlightModelMain) (super.FM)).EI.engines[2].getStage() == 6)
                    ((FlightModelMain) (super.FM)).EI.engines[2].setEngineStops(this);
            } else
            if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() && ((FlightModelMain) (super.FM)).EI.engines[2].getStage() == 0 && ((FlightModelMain) (super.FM)).M.nitro > 0.0F)
                ((FlightModelMain) (super.FM)).EI.engines[2].setStage(this, 6);
        }
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
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
    
    protected void moveAirBrake(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("AirbrakeL1_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("AirbrakeL3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.75F, 10F, -27F));
        hierMesh().chunkSetAngles("AirbrakeR1_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("AirbrakeR3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.75F, 10F, -27F));
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -0.14F);
        hierMesh().chunkSetLocate("AirbrakeL2_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -0.14F);
        hierMesh().chunkSetLocate("AirbrakeR2_D0", Aircraft.xyz, Aircraft.ypr);
    }
    
    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }
    
    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k]);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 2: // '\002'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5: // '\005'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 4: // '\004'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
            
        case 6: // '\006'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/Full_throttle.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/Full_throttle.eff", -1F);
            break;

        case 7: // '\007'
            Eff3DActor.setIntesity(flame, 0.0F);
            Eff3DActor.setIntesity(dust, 0.0F);
            Eff3DActor.setIntesity(trail, 0.0F);
            Eff3DActor.setIntesity(sprite, 0.0F);
            break;

        case 8: // '\b'
            Eff3DActor.setIntesity(flame, 1.0F);
            Eff3DActor.setIntesity(dust, 1.0F);
            Eff3DActor.setIntesity(trail, 1.0F);
            Eff3DActor.setIntesity(sprite, 1.0F);
            break;    
        }
    }

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
    private Eff3DActor flame;
    private Eff3DActor dust;
    private Eff3DActor trail;
    private Eff3DActor sprite;
    private Eff3DActor turboexhaust;
    private float prevThtl;
    private float oldVwld;
    private boolean bOxidiserLeak;
    private boolean havedroptank;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SM-12");
        Property.set(class1, "meshName", "3DO/Plane/SM-12/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/SM-12.fmd:MIG19");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSM_12.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 9, 9, 2, 2, 2, 2, 9, 
            9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 
            3, 9, 9, 9, 9, 3, 3, 9, 9, 9, 
            9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", 
            "_ExternalDev04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", 
            "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev05", "_ExternalDev06", "_ExternalRock21", 
            "_ExternalRock22", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", 
            "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", 
            "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 55;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDT + 2xR-55";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunR55S", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunR55S", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);       
            s = "2xDT + 2xK-5M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunK5MS", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunK5MS", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot); 
            s = "2xR-55";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunR55S", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunR55S", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot); 
            s = "2xK-5M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunK5MS", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunK5MS", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot); 
            s = "2xK-13";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xK-13 + 2xNR-30s";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30kiSM", 120);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunNR30kiSM", 120);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);           
            s = "2xDT + 2xK-13";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);            
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDT + 2xK-13 + 2xNR-30s";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30kiSM", 120);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunNR30kiSM", 120);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);            
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDT + 4xK-13";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonK13A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonK13A", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);  
            s = "2xDT + 4xK-13 + 2xNR-30s";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30kiSM", 120);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunNR30kiSM", 120);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonK13A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonK13A", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDT + 2xK-13 + 2xK-5M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonK13A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonK13A", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(4, "RocketGunK5MS", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(4, "RocketGunK5MS", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDT + 2xK-13 + 2xK-5M + 2xNR-30s";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30kiSM", 120);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunNR30kiSM", 120);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank19", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonK13A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonK13A", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "PylonMiG15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(4, "RocketGunK5MS", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(4, "RocketGunK5MS", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
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