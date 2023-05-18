// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/3/2012 10:11:10 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Yak_36S.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGunMk82LGB;
import com.maddox.il2.objects.weapons.FuelTankGun_AV8B;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunAGM65Ds;
import com.maddox.il2.objects.weapons.RocketGunAGM65L;
import com.maddox.il2.objects.weapons.RocketGunAIM9L;
import com.maddox.il2.objects.weapons.RocketGunAIM120A;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;
import com.maddox.il2.objects.weapons.Missile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Yak_36, Aircraft, TypeFighterAceMaker, TypeRadarGunsight, 
//            Chute, PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, 
//            NetAircraft

public class AV_8A extends AV_8
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeDockable
{

    public AV_8A()
    {
        fxRWR = newSound("aircraft.RWR2", false);
        smplRWR = new Sample("RWR2.wav", 256, 65535);
        RWRSoundPlaying = false;
        fxMissileWarning = newSound("aircraft.MissileMissile", false);
        smplMissileWarning = new Sample("MissileMissile.wav", 256, 65535);
        MissileSoundPlaying = false;
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
        smplRWR.setInfinite(true);
        smplMissileWarning.setInfinite(true);
        trgtlongAI = null;
        missilesList = new ArrayList();
        hasIR = false;
        hasPRHM = false;
        a2a = false;
		IR = false;
		tX4Prev = 0L;
		backfireList = new ArrayList();
		backfire = false;	
		misslebrg = 0F;
	    aircraftbrg = 0F;
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

    private boolean RWRWarning()//TODO RWR
    {   	
    	boolean SPIKE = false;
		Point3d point3d = new Point3d();
		super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft spike = War.getNearestEnemy(this, 6000F);
        if (spike != null)
        {
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (spike)).pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (spike)).pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (spike)).pos.getAbsPoint().z;
        double d3 = d2 - (double)Landscape.Hmin((float)((Actor) (spike)).pos.getAbsPoint().x, (float)((Actor) (spike)).pos.getAbsPoint().y);
        if(d3 < 0.0D)
            d3 = 0.0D;
        int i = (int)(-((double)((Actor) (spike)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i = 360 + i;
        int j = (int)(-((double)((Actor) (spike)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j = 360 + j;
        Actor actor = War.getNearestEnemy(spike, 6000F);
        if((actor instanceof Aircraft) && spike.getArmy() != World.getPlayerArmy() && (spike instanceof TypeFighterAceMaker)&& ((spike instanceof TypeSupersonic) || (spike instanceof TypeFastJet)) && actor == World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
        	{                
        		pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
                int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                boolean flag2 = false;
                Engine.land();
                int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                if(i1 >= 28 && i1 < 32 && f < 7.5F)
                    flag2 = true;
                new String();
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                int j1 = (int)(Math.floor((int)f1) - 90D);
                if(j1 < 0)
                    j1 = 360 + j1;
                int k1 = j1 - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f2 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int l1 = random.nextInt(6) - 3;
                float f3 = 19000F;
                float f4 = f3;
                if(d3 < 1200D)
                    f4 = (float)(d3 * 0.80000001192092896D * 3D);
                int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                if((float)i2 > f3)
                    i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                int j2 = (int)(Math.floor((int)f5) - 90D);
                int k2 = (j2 - (90 - j)) + l1;
                int l2 = (int)f3;
                if((float)i2 < f3)
                    if(i2 > 1150)
                        l2 = (int)(Math.ceil((double)i2 / 900D) * 900D);
                    else
                        l2 = (int)(Math.ceil((double)i2 / 500D) * 500D);
                int i3 = k1 + l1;
                int j3 = i3;
                if(j3 < 0)
                    j3 += 360;
                float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * ((double)f4 * 0.25D));
                int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                if((double)i2 <= (double)k3 && (double)i2 <= 14000D && (double)i2 >= 200D && k2 >= -30 && k2 <= 30 && Math.sqrt(i3 * i3) <= 60D)
                {
                    SPIKE = true;
                } else {
                	SPIKE = false;
                }
                
        	}
		Aircraft aircraft = World.getPlayerAircraft();
		double dd = Main3D.cur3D().land2D.worldOfsX()
				+ ((Actor) (actor)).pos.getAbsPoint().x;
		double dd1 = Main3D.cur3D().land2D.worldOfsY()
				+ ((Actor) (actor)).pos.getAbsPoint().y;
		double dd2 = Main3D.cur3D().land2D.worldOfsY()
				+ ((Actor) (actor)).pos.getAbsPoint().z;
		int ii = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient()
				.getYaw() - 90D));
		if (ii < 0)
			ii = 360 + ii;
		if(SPIKE && actor == World.getPlayerAircraft() && actor instanceof F_18)
    	{
			pos.getAbs(point3d);
			double d31 = Main3D.cur3D().land2D.worldOfsX()
					+ spike.pos.getAbsPoint().x;
			double d41 = Main3D.cur3D().land2D.worldOfsY()
					+ spike.pos.getAbsPoint().y;
			double d51 = Main3D.cur3D().land2D.worldOfsY()
					+ spike.pos.getAbsPoint().z;
			double d81 = (int) (Math.ceil((dd2 - d51) / 10D) * 10D);
			String s = "";
			if (dd2 - d51 - 500D >= 0.0D)
				s = " low";
			if ((dd2 - d51) + 500D < 0.0D)
				s = " high";
			new String();
			double d91 = d31 - dd;
			double d101 = d41 - dd1;
			float f11 = 57.32484F * (float) Math.atan2(d101, -d91);
			int j11 = (int) (Math.floor((int) f11) - 90D);
			if (j11 < 0)
				j11 = 360 + j11;
			int k11 = j11 - ii;
			if (k11 < 0)
				k11 = 360 + k11;
			int l11 = (int) (Math.ceil((double) (k11 + 15) / 30D) - 1.0D);
			if (l11 < 1)
				l11 = 12;
			double d111 = dd - d31;
			double d12 = dd1 - d41;
			double d13 = Math
					.ceil(Math.sqrt(d12 * d12 + d111 * d111) / 10D) * 10D;
			if(bMissileWarning == true)
			{
				bRadarWarning = false;
				playRWRWarning();
			}
			else
				{bRadarWarning = d13 <= 8000D && d13 >= 500D
						&& Math.sqrt(d81 * d81) <= 6000D;				
				aircraftbrg = cvt(l11, 0, 12, 0F,360F);
				HUD.log(AircraftHotKeys.hudLogWeaponId, "Enemy at " + l11 + " o'clock" + s + "!");
				}
				playRWRWarning();
    			} else 
    			{
    				bRadarWarning = false;
    				playRWRWarning();
    				aircraftbrg = 0F;
    			}
        } else
        {
			bMissileWarning = false;
			playRWRWarning(); 
			misslebrg = 0F;
		}
	return true;
    }
    
    public float misslebrg;
    public float aircraftbrg;
    
    private boolean RWRLaunchWarning()
    {   	    	
    	Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Actor actor;
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
        actor = World.getPlayerAircraft(); else
        actor = this;	
        super.pos.getAbs(point3d);
		Aircraft aircraft;
		if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
		aircraft = World.getPlayerAircraft(); else
		aircraft = this;
		double dd = Main3D.cur3D().land2D.worldOfsX()
				+ ((Actor) (actor)).pos.getAbsPoint().x;
		double dd1 = Main3D.cur3D().land2D.worldOfsY()
				+ ((Actor) (actor)).pos.getAbsPoint().y;
		double dd2 = Main3D.cur3D().land2D.worldOfsY()
				+ ((Actor) (actor)).pos.getAbsPoint().z;
		int ii = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient()
				.getYaw() - 90D));
		if (ii < 0)
			ii = 360 + ii;
		List list = Engine.missiles();
		int m = list.size();		
		for (int t = 0; t < m; t++) {
			Actor missile = (Actor) list.get(t);		
		if((missile instanceof com.maddox.il2.objects.weapons.Missile || missile instanceof com.maddox.il2.objects.weapons.MissileSAM) && missile.getSpeed(vector3d) > 20D && ((Missile) missile).getMissileTarget() == this)
    	{
				pos.getAbs(point3d);
				double d31 = Main3D.cur3D().land2D.worldOfsX()
						+ missile.pos.getAbsPoint().x;
				double d41 = Main3D.cur3D().land2D.worldOfsY()
						+ missile.pos.getAbsPoint().y;
				double d51 = Main3D.cur3D().land2D.worldOfsY()
						+ missile.pos.getAbsPoint().z;
				double d81 = (int) (Math.ceil((dd2 - d51) / 10D) * 10D);
				String s = "";
				if (dd2 - d51 - 500D >= 0.0D)
					s = " LOW";
				if ((dd2 - d51) + 500D < 0.0D)
					s = " HIGH";
				new String();
				double d91 = d31 - dd;
				double d101 = d41 - dd1;
				float f11 = 57.32484F * (float) Math.atan2(d101, -d91);
				int j11 = (int) (Math.floor((int) f11) - 90D);
				if (j11 < 0)
					j11 = 360 + j11;
				int k11 = j11 - ii;
				if (k11 < 0)
					k11 = 360 + k11;
				int l11 = (int) (Math.ceil((double) (k11 + 15) / 30D) - 1.0D);
				if (l11 < 1)
					l11 = 12;
				double d111 = dd - d31;
				double d12 = dd1 - d41;
				double d13 = Math
						.ceil(Math.sqrt(d12 * d12 + d111 * d111) / 10D) * 10D;
				//bMissileWarning = d13 <= 8000D && d13 >= 500D && Math.sqrt(d81 * d81) <= 6000D;
				bMissileWarning = true;
				if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
		        {
				HUD.log(AircraftHotKeys.hudLogWeaponId, "MISSILE AT " + l11 + " O'CLOCK" + s + "!!!" + misslebrg);
				playRWRWarning();
				misslebrg = cvt(l11, 0, 12, 0F,360F);
		        }
				if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
				{
					backFire();					
//					if (FM.CT.Weapons[7] != null) {
//						for (int i = 0; i < FM.CT.Weapons[7].length; ++i) {
//							if ((FM.CT.Weapons[7][i] != null) && (FM.CT.Weapons[7][i].countBullets() != 0)) {
//								FM.CT.Weapons[7][i].shots(3);
//							}
//						}
//					}
				}
    			} else 
    			{
    				bMissileWarning = false;
    				playRWRWarning(); 
    				misslebrg = 0F;
    			}
		}
	return true;
    }

    public void playRWRWarning()
    {
        if(bRadarWarning && !fxRWR.isPlaying())
        {
        	fxRWR.start();
        	//fxRWR.play();           
        } else
        if(!bRadarWarning && fxRWR.isPlaying())
        {
        	fxRWR.stop();
        	//fxRWR.cancel();
        }
        if(bMissileWarning && !fxMissileWarning.isPlaying())
        {
        	fxMissileWarning.start();
        	fxRWR.stop();
        	//fxMissileWarning.play();
        } else
        if(!bMissileWarning && fxMissileWarning.isPlaying())
        {
        	fxMissileWarning.stop();
        	//fxMissileWarning.cancel();
        }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(3.5F, 1.5F, 2.5F, 8.2F, 3.0F, 3.5F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        droptank();
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;  
        FM.turret[0].bIsAIControlled = false;
        checkmesh();
    }
    
    
    
    private void checkmesh()// TODO: Checkmesh
    {
    	boolean flag1 = false;
    	boolean flag2 = false;
    	boolean flag3 = false;
    	TankOut = false;
    	TankIn = false;
    	if(hasIR)
        	flag1 = true;       
    	if(thisWeaponsName.startsWith("6xMk82") || thisWeaponsName.startsWith("8xMk82") || thisWeaponsName.startsWith("4xMk82"))
        {
            hierMesh().chunkVisible("pylonBTerL", true);
            hierMesh().chunkVisible("pylonBTerR", true);
        }
        if(HasAGM || IR)
        {
            hierMesh().chunkVisible("FLIRPOD", true);
            flag2 = true;
        }
        if(thisWeaponsName.startsWith("4xAIM-9+ 2xAIM-120") || thisWeaponsName.startsWith("2xAIM-9+ 4xAIM-120") || thisWeaponsName.startsWith("6xAIM-120"))
        {
        	flag1 = true;
        	flag2 = true;
        	flag3 = true;
        }      
        if(thisWeaponsName.startsWith("2xAIM-9 + 2xDT") || thisWeaponsName.startsWith("2xAIM-120 + 2xDT"))
        {
        	flag1 = true;
        }       
        if(thisWeaponsName.startsWith("4xAIM-9+ 2xDT") || thisWeaponsName.startsWith("4xAIM-120 + 2xDT") || thisWeaponsName.startsWith("2xAIM-120 + 2xAIM-9+ 2xDT"))
        {
        	flag1 = true;
        	flag2 = true;
        }
        if(thisWeaponsName.startsWith("2xRocketpod + 2xAGM65L + 2xAIM-9") || thisWeaponsName.startsWith("2xRocketpod + 2xAGM65D + 2xAIM-9") || thisWeaponsName.startsWith("2xAGM65D + 2xAIM-9 + 2xDTO") || thisWeaponsName.startsWith("2xAGM65L + 2xAIM-9 + 2xDTO"))
        {
        	flag1 = true;
        	flag3 = true;
        	flag2 = false;
        }
        if(thisWeaponsName.endsWith("2xDTO"))
        {
        	TankOut = true;        	
        } else
        if(thisWeaponsName.endsWith("2xDT"))
        {
        	TankIn = true; 
        }
        hierMesh().chunkVisible("TankLO", TankOut);
        hierMesh().chunkVisible("TankRO", TankOut);
        hierMesh().chunkVisible("TankL", TankIn);
        hierMesh().chunkVisible("TankR", TankIn);
        hierMesh().chunkVisible("PylonLOut", flag1);
        hierMesh().chunkVisible("PylonROut", flag1);
        hierMesh().chunkVisible("PylonLMid", flag2);
        hierMesh().chunkVisible("PylonRMid", flag2);
        hierMesh().chunkVisible("PylonLIn", flag3);
        hierMesh().chunkVisible("PylonRIn", flag3);      
    }//TODO checkmesh
    
    
    
    private final void doRemoveTankL()
    {
        if(hierMesh().chunkFindCheck("TankL") != -1)
        {
            hierMesh().hideSubTrees("TankL");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("TankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }
    
    private final void doRemoveTankR()
    {
        if(hierMesh().chunkFindCheck("TankR") != -1)
        {
            hierMesh().hideSubTrees("TankR");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("TankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }
    
    private final void doRemoveTankLO()
    {
        if(hierMesh().chunkFindCheck("TankLO") != -1)
        {
            hierMesh().hideSubTrees("TankLO");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("TankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }
    
    private final void doRemoveTankRO()
    {
        if(hierMesh().chunkFindCheck("TankRO") != -1)
        {
            hierMesh().hideSubTrees("TankRO");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("TankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }
    
    private void checkdroptank()
    {
    	if(TankIn == true)
        {	
        if(!FM.CT.Weapons[9][1].haveBullets())	       	
        {
        	TankIn = false;
        	doRemoveTankL();
        	doRemoveTankR();
        }
        } else
        if(TankOut == true)
        {	
        if(!FM.CT.Weapons[9][1].haveBullets())	       	
        {
            TankOut = false;
            doRemoveTankLO();
            doRemoveTankRO();
        }
        }	
    }
    
    private boolean HasAGM;
    
    private void droptank()
    {
    	//missilesList.clear();       	
    	for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
        if(FM.CT.Weapons[i] == null)
              continue;
          for(int j = 0; j < FM.CT.Weapons[i].length; j++)
        {
        if(!FM.CT.Weapons[i][j].haveBullets())
               continue;        
        if(FM.CT.Weapons[i][j] instanceof RocketGunAIM9L)
        {	
        	hasIR = true;
        	//missilesList.add(FM.CT.Weapons[i][j]);
        }
        if(FM.CT.Weapons[i][j] instanceof RocketGunAIM120A)
        {	
        	hasPRHM = true;
        	//missilesList.add(FM.CT.Weapons[i][j]);
        }
        if (FM.CT.Weapons[i][j] instanceof RocketGunAGM65L || FM.CT.Weapons[i][j] instanceof BombGunMk82LGB) {
        	HasAGM = true;
			//missilesList.add(FM.CT.Weapons[i][j]);
        }
        if (FM.CT.Weapons[i][j] instanceof RocketGunAGM65Ds) {
        	IR = true;
			missilesList.add(FM.CT.Weapons[i][j]);
        }if (FM.CT.Weapons[i][j] instanceof RocketGunFlare) {        	
        	backfire = true;
        	backfireList.add(FM.CT.Weapons[i][j]);
        }
    } 
    }
    }
    //------------------------------------------------------
    
    public void launchMsl()
    {
        if(missilesList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGunAGM65Ds)missilesList.remove(0)).shots(1);
            return;
        }
    }
    
    private long tfire;
    public BulletEmitter Weapons[][];
    private boolean hasIR;
    private boolean hasPRHM; 
    
    public void rareAction(float f, boolean flag) {
		int counter = 0;
		if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
        {
    	if (counter++ % 5 == 0) {
			RWRWarning();
    	}	
	}
	super.rareAction(f, flag);
}

    public void update(float f)
    {        
        if(bNeedSetup)
            checkAsDrone();
    	guidedMissileUtils.update();
    	//AIswitchmissile();
    	int i = aircIndex();
    	if(super.FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(((FlightModelMain) (super.FM)).CT.getRefuel() < 0.90F)
                    typeDockableAttemptDetach();
                else
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
                }
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
        RWRLaunchWarning();       
        //RWRWarning();        
        super.update(f);
        checkdroptank();
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
                //((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                //((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        if((!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (FM instanceof Maneuver) && ((Maneuver)FM).get_task() == 7 && !((FlightModelMain) (super.FM)).AP.way.isLanding()) //TODO straft
        {           
        	if(missilesList.isEmpty() && !((Maneuver)super.FM).hasBombs())
        	{        	
        	Pilot pilot = (Pilot)FM;           
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            float f1 = (float)((double)FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
            if(f1 < 55F && vector3d.z < 0.0D){
                vector3d.z = 0.0D;} else               	
            if(pilot != null && isAlive(pilot.target_ground))
            {
                Point3d point3d2 = new Point3d();
                pilot.target_ground.pos.getAbs(point3d2);
                pilot.set_maneuver(43);
                if(pos.getAbsPoint().distance(point3d2) < 2000D)
                {
                    point3d2.sub(FM.Loc);
                    FM.Or.transformInv(point3d2);
                    ((FlightModelMain) (super.FM)).CT.PowerControl = 0.55F;                                                                             	
                }
            }
            setSpeed(vector3d);
        } else
        	if(!missilesList.isEmpty() && Time.current() > tX4Prev + 500L + (IR ? 10000L : 0L))
            {       		
        		Pilot pilot = (Pilot)FM;
                if(pilot.get_maneuver() == 43 && pilot.target_ground != null)
                {
                    Point3d point3d = new Point3d();
                    pilot.target_ground.pos.getAbs(point3d);
                    point3d.sub(FM.Loc);
                    FM.Or.transformInv(point3d);
                    if(point3d.x > 1000D && point3d.x < (IR ? 2250D : 1250D) + 250D * (double)FM.Skill)
                    {
                        if(!IR)
                            point3d.x /= 2 - FM.Skill / 3;
                        if(point3d.y < point3d.x && point3d.y > -point3d.x && point3d.z * 1.5D < point3d.x && point3d.z * 1.5D > -point3d.x)
                        {
                            launchMsl();
                            tX4Prev = Time.current();
                            Voice.speakAttackByRockets(this);
                        }
                    }
                }
            }
        }	       
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
            if(aircraft instanceof TypeTankerDrogue && ((FlightModelMain) (super.FM)).CT.getRefuel() > 0.95F)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
      /*          ((FlightModelMain) (super.FM)).CT.RefuelControl = 1F;
            } else  
            {
                ((FlightModelMain) (super.FM)).CT.RefuelControl = 0F;
            }	*/
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

    private GuidedMissileUtils guidedMissileUtils;
    private SoundFX fxRWR;
    private Sample smplRWR;
    private boolean RWRSoundPlaying;
    private SoundFX fxMissileWarning;
    private Sample smplMissileWarning;
    private boolean MissileSoundPlaying;
    boolean bRadarWarning;
    boolean bMissileWarning;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive = 0L;         // Last Time when a common threat was reported
	private long intervalCommonThreat = 1000L;        // Interval (milliseconds) at which common threats should be dealt with (i.e. duration of warning sound / light)
	private long lastRadarLockThreatActive = 0L;      // Last Time when a radar lock threat was reported
	private long intervalRadarLockThreat = 1000L;     // Interval (milliseconds) at which radar lock threats should be dealt with (i.e. duration of warning sound / light)
	private long lastMissileLaunchThreatActive = 0L;  // Last Time when a missile launch threat was reported
	private long intervalMissileLaunchThreat = 1000L; // Interval (milliseconds) at which missile launch threats should be dealt with (i.e. duration of warning sound / light)
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
    private boolean TankIn;
    private boolean TankOut;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private ArrayList missilesList;
    private ArrayList backfireList;
    private Actor trgtlongAI;
    private long tX4Prev;
	private boolean a2a;
    private boolean IR;
    private boolean backfire;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Harrier");
        Property.set(class1, "meshName", "3DO/Plane/AV-8B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1997F);
        Property.set(class1, "yearExpired", 2014F);
        Property.set(class1, "FlightModel", "FlightModels/AV-8B.fmd:AV8B");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitAV_8B.class, com.maddox.il2.objects.air.CockpitAV8FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 9, 9, 2, 2, 2, 2, 9, 
            9, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 
            3, 9, 9, 9, 9, 3, 3, 9, 9, 9, 
            9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 0, 0, 0, 0, 0,
            0, 3, 3, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", 
            "_ExternalDev04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", 
            "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev05", "_ExternalDev06", "_ExternalRock21", 
            "_ExternalRock22", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", 
            "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", 
            "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08",
            "_CANNON09", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_FlareL", "_FlareR"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 69;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-120 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM-9+ 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM-120 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-120 + 2xAIM-9+ 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM-9+ 2xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9+ 4xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83 + 2xMk84 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83 + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84 + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);            
            s = "4 x Mk82SnakeEye + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6 x Mk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82SnakeEye + 2xAIM-9 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82SnakeEye + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xCBU87 + 2xAIM-9 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xCBU87 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);            
            s = "2xMk82LGB + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82LGB + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);           
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);           
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27); 
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);          
            s = "2xRocketpod + 2xMk82SnakeEye + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27); 
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRocketpod + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);            
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRocketpod + 2xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);            
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xMk82SnakeEye + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xMk82LGB + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xCBU87 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 4xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);           
            s = "2xRocketpod + 2xAGM65L + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);            
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xAGM65D + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);            
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65L + 2xAIM-9 + 2xDTO";//TODO
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65D + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65L + 2xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65D + 2xMk84 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65L + 2xCBU87 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65D + 2xCBU87 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65L + 2xMk82LGB + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65D + 2xMk82LGB + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xAGM-84 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM-84 + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM-84 + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunJDAM84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunJDAM84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xAGM65L + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);         
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xAGM65D + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);         
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xRocketpod + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);         
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82SnakeEye + 2xAIM-9 + 2xDTO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_AV8B", 1);           
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82SnakeEye + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82SnakeEye + 2xAGM65L + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82SnakeEye + 2xAGM65D + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82SnakeEye + 2xRocketpod + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);         
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
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