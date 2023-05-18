////////////////////////////////////////////////////////////////////////////////////////
// By PAL, from Vega Su25
// Corrected MoveSteering to match with normal settings on Gear.class using Passive Steering (DiffBrakes=3)
// Fails Safe Updated sirenaWarning, now is Range dependant sirenaWarning(float range)
// Fails Safe Updated sirenaLaunchWarning() Missile detection code
// Method playSirenaWarning was not doing anything. Corrected.   
////////////////////////////////////////////////////////////////////////////////////////

// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 09/10/2015 07:28:00 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Su_25.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;
import java.util.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            Su_25X, Aircraft, TypeFighterAceMaker, TypeSupersonic, 
//            TypeFastJet, Chute, PaintSchemeFMPar05, TypeGuidedMissileCarrier, 
//            TypeCountermeasure, TypeThreatDetector, TypeStormovikArmored, TypeLaserSpotter, 
//            Cockpit, NetAircraft, EjectionSeat

public class Su_25 extends Su_25X
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeStormovikArmored, TypeLaserSpotter
{

    public Su_25()
    {
        headPos = new float[3];
        headOr = new float[3];
        pilotHeadT = 0.0F;
        pilotHeadY = 0.0F;
        LaserHook = new Hook[4];
        counter = 0;
        guidedMissileUtils = null;
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
        dynamoOrient = 0.0F;
        guidedMissileUtils = new GuidedMissileUtils(this);
        intervalMissileLaunchThreat = 1000L;
        guidedMissileUtils = new GuidedMissileUtils(this);
        fxSPO10 = newSound("aircraft.Sirena2", false);
        smplSPO10 = new Sample("sample.Sirena2.wav", 256, 65535);
        SPO10SoundPlaying = false;
        smplSPO10.setInfinite(true);
        counter = 0;
        radarmode = 0;
        APmode1 = false;
        APmode2 = false;
        APmode3 = false;
        backfireList = new ArrayList();
        backfire = false;
        tX4Prev = 0L;
        headPos = new float[3];
        headOr = new float[3];
        pilotHeadT = 0.0F;
        pilotHeadY = 0.0F;
        laserOn = false;
        laserLock = false;
    }

    public void typeBomberAdjDistancePlus()
    {
        super.fSightCurForwardAngle += 0.2F;
        if(super.fSightCurForwardAngle > 6F)
            super.fSightCurForwardAngle = 6F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        super.fSightCurForwardAngle -= 0.2F;
        if(super.fSightCurForwardAngle < -30F)
            super.fSightCurForwardAngle = -30F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        super.fSightCurSideslip += 0.2F;
        if(super.fSightCurSideslip > 12F)
            super.fSightCurSideslip = 12F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        super.fSightCurSideslip -= 0.2F;
        if(super.fSightCurSideslip < -12F)
            super.fSightCurSideslip = -12F;
    }

    public void laserUpdate()
    {
        if(!laserLock)
        {
            hierMesh().chunkSetAngles("LaserMsh_D0", -super.fSightCurForwardAngle, -super.fSightCurSideslip, 0.0F);
            super.pos.setUpdateEnable(true);
            super.pos.getRender(Actor._tmpLoc);
            LaserHook[1] = new HookNamed(this, "_Laser1");
            LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            LaserHook[1].computePos(this, Actor._tmpLoc, LaserLoc1);
            LaserLoc1.get(LaserP1);
            LaserLoc1.set(30000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            LaserHook[1].computePos(this, Actor._tmpLoc, LaserLoc1);
            LaserLoc1.get(LaserP2);
            Engine.land();
            if(Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL))
            {
                LaserPL.z -= 0.94999999999999996D;
                LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
                TypeLaserSpotter.spot.set(LaserP2);
                Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(((Tuple3d) (LaserP2)).x, ((Tuple3d) (LaserP2)).y, ((Tuple3d) (LaserP2)).z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            }
        } else
        if(laserLock)
        {
            LaserP3.x = ((Tuple3d) (LaserP2)).x + (double)(-(super.fSightCurForwardAngle * 6F));
            LaserP3.y = ((Tuple3d) (LaserP2)).y + (double)(super.fSightCurSideslip * 6F);
            LaserP3.z = ((Tuple3d) (LaserP2)).z;
            TypeLaserSpotter.spot.set(LaserP3);
            Eff3DActor eff3dactor1 = Eff3DActor.New(null, null, new Loc(((Tuple3d) (LaserP3)).x, ((Tuple3d) (LaserP3)).y, ((Tuple3d) (LaserP3)).z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        }
    }

    private void checkAmmo()
    {
        for(int i = 0; i < ((FlightModelMain) (super.FM)).CT.Weapons.length; i++)
            if(((FlightModelMain) (super.FM)).CT.Weapons[i] != null)
            {
                for(int j = 0; j < ((FlightModelMain) (super.FM)).CT.Weapons[i].length; j++)
                    if(((FlightModelMain) (super.FM)).CT.Weapons[i][j].haveBullets() && (((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof RocketGunFlare))
                    {
                        backfire = true;
                        backfireList.add(((FlightModelMain) (super.FM)).CT.Weapons[i][j]);
                    }

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

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(true);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(false);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = true;
            }
        if(i == 22)
            if(!APmode3)
            {
                APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route ON");
                ((FlightModelMain) (super.FM)).AP.setWayPoint(true);
            } else
            if(APmode3)
            {
                APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route OFF");
                ((FlightModelMain) (super.FM)).AP.setWayPoint(false);
                ((FlightModelMain) (super.FM)).CT.AileronControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.ElevatorControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.RudderControl = 0.0F;
            }
        if(i == 23)
        {
            ((FlightModelMain) (super.FM)).CT.AileronControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.ElevatorControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.RudderControl = 0.0F;
            ((FlightModelMain) (super.FM)).AP.setWayPoint(false);
            ((FlightModelMain) (super.FM)).AP.setStabDirection(false);
            ((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
            APmode1 = false;
            APmode2 = false;
            APmode3 = false;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: All Off");
        }
        if(i == 24)
            if(!laserOn)
            {
                laserOn = true;
                laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: On");
                super.fSightCurSideslip = 0.0F;
                super.fSightCurForwardAngle = 0.0F;
            } else
            if(laserOn)
            {
                laserOn = false;
                laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Off");
                super.fSightCurSideslip = 0.0F;
                super.fSightCurForwardAngle = 0.0F;
            }
        if(i == 25)
            if(!laserLock)
            {
                laserLock = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Locked");
                super.fSightCurSideslip = 0.0F;
                super.fSightCurForwardAngle = 0.0F;
            } else
            if(laserLock)
            {
                laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Unlocked");
                super.fSightCurSideslip = 0.0F;
                super.fSightCurForwardAngle = 0.0F;
            }
    }

    public boolean typeRadarToggleMode()
    {
        return true;
    }

    public void rareAction(float f, boolean flag)
    {
        if(counter++ % 5 == 0)
            sirenaWarning(6000F);
        super.rareAction(f, flag);
    }

	//By PAL, made range dependant
	//By PAL, Why do I have two different Sirena Codes?	
    private boolean sirenaWarning(float range)
    {
        boolean flag = false;
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = War.getNearestEnemy(this, range);
        if(Aircraft.isValid(aircraft))
        {
            double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
            double d3 = d2 - (double)Landscape.Hmin((float)((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x, (float)((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i += 360;
            int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j += 360;
            Aircraft aircraft1 = War.getNearestEnemy(aircraft, range);
		//By PAL, added verification for Fail-Safe	        
	        if(Aircraft.isValid(aircraft1))
	        {
	            if((aircraft1 instanceof Aircraft) && aircraft.getArmy() != World.getPlayerArmy() && (aircraft instanceof TypeFighterAceMaker) && ((aircraft instanceof TypeSupersonic) || (aircraft instanceof TypeFastJet)) && aircraft1 == World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
	            {
	                super.pos.getAbs(point3d);
	                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
	                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
	                double d8 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
	                new String();
	                new String();
	                int k = (int)(Math.floor(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z * 0.10000000000000001D) * 10D);
	                int l = (int)(Math.floor((aircraft1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
	                double d10 = (int)(Math.ceil((d2 - d8) / 10D) * 10D);
	                boolean flag1 = false;
	                Engine.land();
	                int j1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x), Engine.land().WORLD2PIXY(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y));
	                float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
	                if(j1 >= 28 && j1 < 32 && f < 7.5F)
	                    flag1 = true;
	                new String();
	                double d14 = d4 - d;
	                double d16 = d6 - d1;
	                float f1 = 57.32484F * (float)Math.atan2(d16, -d14);
	                int k1 = (int)(Math.floor((int)f1) - 90D);
	                if(k1 < 0)
	                    k1 += 360;
	                int l1 = k1 - i;
	                double d19 = d - d4;
	                double d20 = d1 - d6;
	                Random random = new Random();
	                float f3 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
	                int l2 = random.nextInt(6) - 3;
	                float f4 = 19000F;
	                float f5 = f4;
	                if(d3 < 1200D)
	                    f5 = (float)(d3 * 0.80000001192092896D * 3D);
	                int i3 = (int)(Math.ceil(Math.sqrt((d20 * d20 + d19 * d19) * (double)f3) / 10D) * 10D);
	                if((float)i3 > f4)
	                    i3 = (int)(Math.ceil(Math.sqrt(d20 * d20 + d19 * d19) / 10D) * 10D);
	                float f6 = 57.32484F * (float)Math.atan2(i3, d10);
	                int j3 = (int)(Math.floor((int)f6) - 90D);
	                int k3 = (j3 - (90 - j)) + l2;
	                int l3 = (int)f4;
	                if((float)i3 < f4)
	                    if(i3 > 1150)
	                        l3 = (int)(Math.ceil((double)i3 / 900D) * 900D);
	                    else
	                        l3 = (int)(Math.ceil((double)i3 / 500D) * 500D);
	                int i4 = l1 + l2;
	                int j4 = i4;
	                if(j4 < 0)
	                    j4 += 360;
	                float f7 = (float)((double)f5 + Math.sin(Math.toRadians(Math.sqrt(l1 * l1) * 3D)) * ((double)f5 * 0.25D));
	                int k4 = (int)((double)f7 * Math.cos(Math.toRadians(k3)));
	                if((double)i3 <= (double)k4 && (double)i3 <= 14000D && (double)i3 >= 200D && k3 >= -30 && k3 <= 30 && Math.sqrt(i4 * i4) <= 60D)
	                    flag = true;
	                else
	                    flag = false;
	            }
	        }
            Aircraft aircraft2 = World.getPlayerAircraft();
            double d5 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
            double d7 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
            double d9 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
            int i1 = (int)(-((double)((Actor) (aircraft2)).pos.getAbsOrient().getYaw() - 90D));
            if(i1 < 0)
                i1 += 360;
            if(flag && aircraft1 == World.getPlayerAircraft())
            {
                super.pos.getAbs(point3d);
                double d11 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
                double d12 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
                double d13 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
                double d15 = (int)(Math.ceil((d9 - d13) / 10D) * 10D);
                String s = "";
                if(d9 - d13 - 500D >= 0.0D)
                    s = " low";
                if((d9 - d13) + 500D < 0.0D)
                    s = " high";
                new String();
                double d17 = d11 - d5;
                double d18 = d12 - d7;
                float f2 = 57.32484F * (float)Math.atan2(d18, -d17);
                int i2 = (int)(Math.floor((int)f2) - 90D);
                if(i2 < 0)
                    i2 += 360;
                int j2 = i2 - i1;
                if(j2 < 0)
                    j2 += 360;
                int k2 = (int)(Math.ceil((double)(j2 + 15) / 30D) - 1.0D);
                if(k2 < 1)
                    k2 = 12;
                double d21 = d5 - d11;
                double d22 = d7 - d12;
                double d23 = Math.ceil(Math.sqrt(d22 * d22 + d21 * d21) / 10D) * 10D;
                bRadarWarning = d23 <= 8000D && d23 >= 500D && Math.sqrt(d15 * d15) <= 6000D;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-10: Spike at " + k2 + " o'clock" + s + "!");
                playSirenaWarning(bRadarWarning);
            } else
            {
                bRadarWarning = false;
                playSirenaWarning(bRadarWarning);
            }
        }
        return true;
    }

	//By PAL, this SirenaCode is only for Missiles? How would react to multiple signals? Verify!!!
    private boolean sirenaLaunchWarning(float range)
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(!Aircraft.isValid(aircraft))
        	return false;
        super.pos.getAbs(point3d);
        Aircraft aircraft1 = World.getPlayerAircraft();
        if(!Aircraft.isValid(aircraft1))
        	return false;        
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft1)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        //By PAL
        boolean bSignalFound = false;            
        List list = Engine.missiles();
        int j = list.size();
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            //By PAL, compare just in case
            if(actor != null)
            {
	            if(((actor instanceof Missile) || (actor instanceof MissileSAM)) && actor.getArmy() != World.getPlayerArmy() && actor.getSpeed(vector3d) > 20D)
	            {
	                super.pos.getAbs(point3d);
	                double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
	                double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
	                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
	                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
	                String s = "";
	                if(d2 - d5 - 500D >= 0.0D)
	                    s = " LOW";
	                if((d2 - d5) + 500D < 0.0D)
	                    s = " HIGH";
	                new String();
	                double d7 = d3 - d;
	                double d8 = d4 - d1;
	                float f = 57.32484F * (float)Math.atan2(d8, -d7);
	                int l = (int)(Math.floor((int)f) - 90D);
	                if(l < 0)
	                    l += 360;
	                int i1 = l - i;
	                if(i1 < 0)
	                    i1 += 360;
	                int j1 = (int)(Math.ceil((double)(i1 + 15) / 30D) - 1.0D);
	                if(j1 < 1)
	                    j1 = 12;
	                double d9 = d - d3;
	                double d10 = d1 - d4;
	                double d11 = Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D;
	                bSignalFound |= (d11 <= 8000D) && (d11 >= 500D) && (Math.sqrt(d6 * d6) <= (double)range);
	                HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-10: MISSILE AT " + j1 + " O'CLOCK" + s + "!!!");
	                playSirenaWarning(bRadarWarning);	                
//	                bRadarWarning = d11 <= 8000D && d11 >= 500D && Math.sqrt(d6 * d6) <= 6000D;
//	                HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-10: MISSILE AT " + j1 + " O'CLOCK" + s + "!!!");
//	                playSirenaWarning(bRadarWarning);
	                if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
	                    backFire();
	            }  	
            }
        }
        //By PAL, Play Sirena if there was any signal.
        if(bSignalFound)
        {
       		bRadarWarning |= bSignalFound;
            playSirenaWarning(bRadarWarning);            	
        }
		else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }        

        return true;
    }

//    public void playSirenaWarning(boolean flag)
//    {
//        if(!flag || sirenaSoundPlaying)
//            if(!flag);
//    }

	//By PAL, correct, working playSirenaWarning method    
    public void playSirenaWarning(boolean flag)
    {
    	//By PAL, why doing this comparison here?
        if(this != World.getPlayerAircraft())
            return;
        if(flag && !sirenaSoundPlaying)
        {
            fxSPO10.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else
        if(!flag && sirenaSoundPlaying)
        {
            fxSPO10.cancel();
            sirenaSoundPlaying = false;
        }
    }     

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
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

    public void onAircraftLoaded()
    {
        checkAmmo();
        super.onAircraftLoaded();
        super.bHasSK1Seat = false;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
    }

	//By PAL, new vars
	private float chuteElev, chuteIElev;
	private float chuteAz, chuteIAz;
	private float chuteSizeX, chuteISizeX;
	private float chuteSizeY, chuteISizeY;
	private float fISize;


    //By PAL, Parachute Deployment and Movement
    public void updateChute(float f)
    {	
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute1 = new Chute(this);
            chute2 = new Chute(this);
            chute1.setMesh("3do/plane/ChuteSu_25/mono.sim");
            chute2.setMesh("3do/plane/ChuteSu_25/mono.sim");
            chute1.mesh().setScale(0.5F);
            chute2.mesh().setScale(0.5F);
            ((Actor) (chute1)).pos.setRel(new Point3d(-8D, 0.0D, 0.6D), new Orient(20F, 90F, 0.0F));
            ((Actor) (chute2)).pos.setRel(new Point3d(-8D, 0.0D, 0.6D), new Orient(-20F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() > 600F || ((FlightModelMain) (super.FM)).CT.DragChuteControl < 1.0F)
            {
                if(chute1 != null && chute2 != null)
                {
                    chute1.tangleChute(this);
                    chute2.tangleChute(this);
                    ((Actor) (chute1)).pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(20F, 90F, 0.0F));
                    ((Actor) (chute2)).pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(-20F, 90F, 0.0F));
                }
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F)
            {
                if(chute1 != null && chute2 != null)
                {
                    chute1.tangleChute(this);
                    chute2.tangleChute(this);
                }
                ((Actor) (chute1)).pos.setRel(new Orient(10F, 100F, 0.0F));
                ((Actor) (chute2)).pos.setRel(new Orient(-10F, 100F, 0.0F));
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
        {
            chute1.destroy();
            chute2.destroy();
        }
	}
	
    public void update(float f)
    {
        if(laserOn)
            laserUpdate();
        sirenaLaunchWarning(6000F);
        guidedMissileUtils.update();
        super.update(f);
	//By PAL
		updateChute(f);
        guidedMissileUtils.update();
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 60F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
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
        hierMesh().chunkVisible("Seat1_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
    }

//    static Class _mthclass$(String s)
//    {
//        try
//        {
//            return Class.forName(s);
//        }
//        catch(ClassNotFoundException classnotfoundexception)
//        {
//            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//        }
//    }

    private static Point3d LaserP3 = new Point3d();
    public boolean laserOn;
    public boolean laserLock;
    private float headPos[];
    private float headOr[];
    private float pilotHeadT;
    private float pilotHeadY;
    public static Orient tmpOr = new Orient();
    private static Vector3d Ve = new Vector3d();
    private Hook LaserHook[];
    private LightPointWorld Laser[];
    private static Loc LaserLoc1 = new Loc();
    private static Point3d LaserP1 = new Point3d();
    private static Point3d LaserP2 = new Point3d();
    private static Point3d LaserPL = new Point3d();
    private static Loc LaserLoc1i = new Loc();
    private static Point3d LaserP1i = new Point3d();
    private static Point3d LaserP2i = new Point3d();
    private static Point3d LaserPLi = new Point3d();
    private long tX4Prev;
    private boolean backfire;
    private ArrayList backfireList;
    public boolean APmode1;
    public boolean APmode2;
    public boolean APmode3;
    public int radarmode;
    private int counter;
    private GuidedMissileUtils guidedMissileUtils;
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
    private boolean bHasDeployedDragChute;
    private Chute chute1;
    private Chute chute2;
    private long removeChuteTimer;
    private BulletEmitter g1;
    private int oldbullets;
    public static boolean bChangedPit = false;
    private float dynamoOrient;
    public boolean bToFire;
    private SoundFX fxSPO10;
    private Sample smplSPO10;
    private boolean SPO10SoundPlaying;
    private SoundFX fxSirenaLaunch;
    private Sample smplSirenaLaunch;
    private boolean sirenaLaunchSoundPlaying;
    private boolean bRadarWarningLaunch;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private boolean bRadarWarning;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.Su_25.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-25");
        Property.set(class1, "meshName", "3DO/Plane/Su-25/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/SU-25.fmd:SU25FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSu_25.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            9, 9, 0, 0, 0, 0, 2, 2, 2, 2, 
            7, 7, 3, 3, 3, 3, 3, 3, 3, 3, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 
            9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", 
            "_ExternalBomb09", "_ExternalBomb10", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", 
            "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalRock04", "_ExternalRock03", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", 
            "_ExternalDev11", "_ExternalDev12", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalRock09", "_ExternalRock10", "_ExternalRock12", "_ExternalRock11", 
            "_Flare01", "_Flare02", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", 
            "_ExternalDev13", "_ExternalDev14", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", 
            "_ExternalDev15", "_ExternalDev16", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", 
            "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalDev17", "_ExternalDev18", 
            "_ExternalDev19", "_ExternalDev20"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 82;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-250M46+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB-250M46+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xRBK-250+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK-250+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xB-13+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK-250+2xB-8+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK-250+2xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-250M46+2xB-8+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250M46+4xB-8+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB-250M46+2xSPPU-22+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB-8+2xSPPU-22+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+2xPTB-800L+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+4xB-13+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+2xPTB-800L+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+2xPTB-800L+4xB-13+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+8xFAB-100+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+8xFAB-100+4xB-13+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+6xS-24+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+6xS-25+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS25", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS25", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS25", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS25", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS25", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS25", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonO25", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonO25", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(9, "PylonO25", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(9, "PylonO25", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(9, "PylonO25", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(9, "PylonO25", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-29L+2xPTB-800L+4xS-24+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh-25ML+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunKh25ML", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunKh25ML", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunKh25ML", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunKh25ML", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-25ML+2xPTB-800L+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunKh25ML", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunKh25ML", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh-25ML+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-25ML+2xPTB-800L+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-25ML+8xFAB-100+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh-25MR+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunKh25MR", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunKh25MR", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunKh25MR", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunKh25MR", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-25MR+2xPTB-800L+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunKh25MR", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunKh25MR", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh-25MR+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-25MR+2xPTB-800L+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-25MR+8xFAB-100+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonAPU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-100+6xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU-2(PTAB-2.5)+6xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU-2(AO-2.5RT)+6xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "None";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
