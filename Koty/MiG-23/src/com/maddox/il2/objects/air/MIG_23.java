// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.08.2019 20:17:37
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MIG_23.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;

import java.io.IOException;
import java.sql.Array;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, MIG_27K, TypeFighterAceMaker, TypeRadarGunsight, 
//            Chute, MIG_23BN, MIG_23ML, MIG_23MLA, 
//            MIG_23MLD, MIG_23P, MIG_23M, MIG_23MF, 
//            TypeSupersonic, TypeFastJet, TypeFighter, TypeBNZFighter, 
//            TypeGSuit, TypeX4Carrier, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            TypeThreatDetector, Aircraft, Cockpit, PaintScheme, 
//            EjectionSeat

public class MIG_23 extends Scheme1
    implements TypeSupersonic, TypeFastJet, TypeMyotka, TypeFighter, TypeSemiRadar, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, TypeX4Carrier, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public double directionalDistanceToGround(double d)
    {
        Point3d point3d = new Point3d(super.pos.getAbsPoint());
        Point3d point3d1 = new Point3d(d, 0.0D, 0.0D);
        super.pos.getAbsOrient().transform(point3d1);
        point3d1.add(point3d);
        Point3d point3d2 = new Point3d();
        if(Landscape.rayHitHQ(point3d, point3d1, point3d2))
            return point3d2.distance(point3d);
        else
            return (1.0D / 0.0D);
    }

    public MIG_23()
    {
        {


        	
        }
        SonicBoom = 0.0F;
        fxSirena = newSound("aircraft.Sirena2", false);
        smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        sirenaSoundPlaying = false;
        bHasSK1Seat = true;
        gearLightsEffects = new Eff3DActor[3];
        gearLightsLights = new LightPointActor[3];
        bGearLightsOn = false;
        bGearLightState = false;
        k14Mode = 0;
        RP_23_range = 2;
        TP_23_model = 30f;
        RP_23_elevation = 0f;
        RP_23_model[0] = 55; //high altitude range
        RP_23_model[1] = 25; //low altitude range
        RP_23_model[2] = 1500; //low altitude break
        RP_23_model[3] = 4500; //high altitude break
        
        RP_23_doppler[0] = 70;
        RP_23_doppler[1] = 110;
        
        ECCM_power = 70000;
        
        BSV_SC = false;
        BSV_mode = 0;
        radar_aspect = 0;
        
        air_ground = false;
        
        rangeGate_range = -0.5f;
        rangeGate_azimuth = 0.0f;
        k14WingspanType = 0;
        k14Distance = 200F;
        overrideBailout = false;
        ejectComplete = false;
        lTimeNextEject = 0L;
        oldthrl = -1F;
        curthrl = -1F;
        pylonOccupied = false;
        booster = new Bomb[2];
        bHasBoosters = true;
        boosterFireOutTime = -1L;
        smplSirena.setInfinite(true);
        radarmode = 0;
        targetnum = 0;
        lockrange = 0.018F;
        APmode1 = false;
        APmode2 = false;
        bSlatsOff = false;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
        fSightCurReadyness = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        guidedMissileUtils = new GuidedMissileUtils(this);
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
        tX4Prev = 0L;
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
        error = 0;
    }

    public void typeRadarGainMinus()
    {
        BSV_SC = false;

        HUD.log(AircraftHotKeys.hudLogWeaponId, "BSMV");
    }

    public void typeRadarGainPlus()
    {
    	BSV_SC = true;

        HUD.log(AircraftHotKeys.hudLogWeaponId, "BSV SC");
    }

    public void typeRadarRangeMinus()
    {
    	RP_23_range--;
    	if(radarmode == 1)
    	{
    		if(RP_23_range<1)RP_23_range=1;
    	}
    	else
    	{
    		if(RP_23_range<0)RP_23_range=0;
    	}
    }

    public void typeRadarRangePlus()
    {
    	RP_23_range++;
    	if(RP_23_range>3)RP_23_range=3;
    }

    public void typeRadarReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public void typeRadarReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public boolean typeRadarToggleMode()
    {/*
        long l = Time.current();
        if(radarmode == 1)
            radarmode++;
        if(radarmode == 0 && l > twait + 5000L)
        {
            radarmode++;
            twait = l;
        }
        if(radarmode > 1)
            radarmode = 0;*/
        return false;
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

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(2.5F, 1.5F, 2.0F, 5.5F, 4F, 2.5F);
    }

    public void rareAction(float f, boolean flag)
    {
        if(raretimer != Time.current() && this == World.getPlayerAircraft())
        {
            counter++;
            if(counter % 12 == 9)
                InertialNavigation();
        }
        super.rareAction(f, flag);
        raretimer = Time.current();
        if((((FlightModelMain) (super.FM)).Gears.nearGround() || ((FlightModelMain) (super.FM)).Gears.onGround()) && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    private boolean InertialNavigation()
    {
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(aircraft.getSpeed(vector3d) > 20D && ((Actor) (aircraft)).pos.getAbsPoint().z >= 150D && (aircraft instanceof MIG_27K))
        {
            pos.getAbs(point3d);
            if(Mission.cur() != null)
            {
                error++;
                if(error > 99)
                    error = 1;
            }
            int i = error;
            int j = i;
            Random random = new Random();
            int k = random.nextInt(100);
            if(k > 50)
                i -= i * 2;
            k = random.nextInt(100);
            if(k > 50)
                j -= j * 2;
            double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x) / 1000D / 10D;
            double d2 = (Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y) / 1000D / 10D;
            char c = (char)(int)(65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
            char c1 = (char)(int)(65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
            String s = "";
            if(d > 260D)
                s = "" + c + c1;
            else
                s = "" + c1;
            int l = (int)Math.ceil(d2);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "INS: " + s + "-" + l);
        }
        return true;
    }

    public void setTimer(int i)
    {
        Random random = new Random();
        Timer1 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
        Timer2 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
    }

    public void resetTimer(float f)
    {
        Timer1 = f;
        Timer2 = f;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
    		if(radarmode==2||radarmode==12)
        	{
        		TP_lock=true;
        	}
    		if(radarmode==1||radarmode==11)
        	{
    			if(RP_lock)RP_lock=false;
    			else
        		RP_lock=true;
        		return false;
        	}

    		if(radarmode==100)
    		{
    			
    			victim_RP = null;
    			victim_TP = null;
    			radarmode = 11;
    			

        		
        		rangeGate_range = 0;
        		rangeGate_azimuth = 0;
    		}
    		if(radarmode==101)
    		{

    			victim_TP = victim_RP;
    			victim_RP = null;
    			radarmode = 201;
    			

        		
        		rangeGate_range = 0;
        		rangeGate_azimuth = 0;
    		}
    		
    		
    		if(radarmode==200)
    		{
    			victim_TP = null;
    			victim_RP = null;
    			radarmode = 12;

        		
        		rangeGate_range = 0;
        		rangeGate_azimuth = 0;
    		}
    		if(radarmode==201)
    		{
    			victim_TP = null;
    			victim_RP = null;
    			radarmode = 12;

        		
        		rangeGate_range = 0;
        		rangeGate_azimuth = 0;
    			/*
    			victim_RP = victim_TP;
    			radarmode = 101;
    			victim_TP = null;
    			*/
    		}
    		
    	
    	
    	return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    	
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
    	if(radarmode == 1 || radarmode == 2 || radarmode == 11 || radarmode == 12)
    	{
    		rangeGate_range += 0.025;
    		if (rangeGate_range > 1) rangeGate_range = 1;
    	}
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
    	if(radarmode == 1 || radarmode == 2||radarmode == 11 || radarmode == 12)
    	{
    		rangeGate_range -= 0.025;
    		if (rangeGate_range < -1) rangeGate_range = -1;
    	}
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    	
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
    	rangeGate_azimuth += 0.025f;
    	if (rangeGate_azimuth > 1) rangeGate_azimuth = 1;

    	/*
    	else
    	{
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    	}*/
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
    		rangeGate_azimuth -= 0.05f;
    		if (rangeGate_azimuth < -1) rangeGate_azimuth = -1;
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "Bracket azimuth: " + rangeGate_azimuth);
    	/*
    	else
    	{
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    	}*/
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 0)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (super.FM)).actor, 2000F, 9);
        if(hunted != null)
        {
            k14Distance = (float)((Interpolate) (super.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 1700F)
                k14Distance = 1700F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Glass_Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 1500F, -70F);
        float f2 = Math.max(-f * 1500F, -90F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -f2);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, f2);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.3F, 0.9F, 0.0F, -160F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.3F, 0.9F, 0.0F, -160F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(f, 0.001F, 0.002F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(f, 0.001F, 0.002F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("Spine_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.1F, 0.8F, 0.0F, -90F));
    }

    protected void moveGear(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        hierMesh().chunkSetLocate("GearL33_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        hierMesh().chunkSetLocate("GearR33_D0", Aircraft.xyz, Aircraft.ypr);
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        float f = ((FlightModelMain) (super.FM)).Gears.gWheelSinking[2];
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 20F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[1] = ((FlightModelMain) (super.FM)).Gears.gWheelSinking[0];
        hierMesh().chunkSetLocate("GearL212_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = ((FlightModelMain) (super.FM)).Gears.gWheelSinking[1];
        hierMesh().chunkSetLocate("GearR212_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
        hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -16.5F * ((FlightModelMain) (super.FM)).CT.getElevator(), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -16.5F * ((FlightModelMain) (super.FM)).CT.getElevator(), 0.0F);
        } else
        if(f < 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -7F * ((FlightModelMain) (super.FM)).CT.getElevator(), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -7F * ((FlightModelMain) (super.FM)).CT.getElevator(), 0.0F);
        }
    }

    protected void moveAileron(float f)
    {
        if(FM.CT.getAileron() < 0.0F)
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 15F * FM.CT.getAileron(), 0.0F);
        else
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -15F * FM.CT.getAileron(), 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = 44.5F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("AirbrakeL", -35F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("AirbrakeL_1", -35F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("AirbrakeR", 35F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("AirbrakeR_1", 35F * f, 0.0F, 0.0F);
    }

    protected void moveSlats(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.15F);
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 0.1F);
        Aircraft.xyz[2] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.065F);
        hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.1F);
        hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff()
    {
        if(!bSlatsOff)
        {
            return;
        } else
        {
            resetYPRmodifier();
            Aircraft.xyz[0] = -0.15F;
            Aircraft.xyz[1] = 0.1F;
            Aircraft.xyz[2] = -0.065F;
            hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 8.5F, 0.0F);
            hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[1] = -0.1F;
            hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 8.5F, 0.0F);
            hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
            bSlatsOff = true;
            return;
        }
    }

    protected void moveFan(float f)
    {
    }

    public void moveSteering(float f)
    {
        if(((FlightModelMain) (super.FM)).CT.GearControl > 0.9F)
        {
            if(f < -30F)
                hierMesh().chunkSetAngles("GearC_D0", -30F, 0.0F, 0.0F);
            else
            if(f > 30F)
                hierMesh().chunkSetAngles("GearC_D0", 30F, 0.0F, 0.0F);
            else
                hierMesh().chunkSetAngles("GearC_D0", f, 0.0F, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("GearC_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    public void moveShockCone()
    {
        float f = calculateMach();
        Aircraft.xyz[0] = Aircraft.cvt(f / 1.9F, 0.0F, 1.0F, 0.0F, 1.2F);
        hierMesh().chunkSetLocate("Cone", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 50F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.77F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1: // '\001'
                case 2: // '\002'
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 20F)
                {
                    ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if(s.endsWith("exht"));
                return;
            }
            if(s.startsWith("xxmgun0"))
            {
                Aircraft.debugprintln(this, "Armament: Gunpod Disabled..");
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[j] == 0)
                    {
                        debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j, 2);
                        debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxhyd"))
            {
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
                return;
            }
            if(s.startsWith("xxpnm"))
            {
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcockpit"))
        {
            ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
            getEnergyPastArmor(0.05F, shot);
        }
        if(s.startsWith("xcf"))
            hitChunk("CF", shot);
        else
        if(s.startsWith("xnose"))
            hitChunk("Nose", shot);
        else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl"))
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr"))
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xflap"))
        {
            if(s.startsWith("xflap1"))
                hitChunk("Flap01", shot);
            if(s.startsWith("xflap2"))
                hitChunk("Flap02", shot);
            if(World.Rnd().nextFloat() < 0.4F)
                ((FlightModelMain) (super.FM)).CT.bHasFlapsControlRed = true;
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int k;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else
            {
                k = s.charAt(5) - 49;
            }
            hitFlesh(k, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(actor);
            // fall through

        case 33: // '!'
        case 34: // '"'
        case 35: // '#'
        case 36: // '$'
        case 37: // '%'
        case 38: // '&'
            doCutBoosters();
            ((FlightModelMain) (super.FM)).AS.setGliderBoostOff();
            bHasBoosters = false;
            // fall through

        case 20: // '\024'
        case 21: // '\025'
        case 22: // '\026'
        case 23: // '\027'
        case 24: // '\030'
        case 25: // '\031'
        case 26: // '\032'
        case 27: // '\033'
        case 28: // '\034'
        case 29: // '\035'
        case 30: // '\036'
        case 31: // '\037'
        case 32: // ' '
        default:
            return super.cutFM(i, j, actor);
        }
    }

    public void destroy()
    {
        doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters()
    {
        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 15F);
        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster1"), null, 0.25F, "3DO/Effects/Aircraft/TurboHWK109D.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 15F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 0.25F, "3DO/Effects/Aircraft/TurboHWK109D.eff", 6F);
    }

    public void doCutBoosters()
    {
        for(int i = 0; i < 2; i++)
            if(booster[i] != null)
            {
                booster[i].start();
                booster[i] = null;
            }

    }

    public float getAirPressure(float f)
    {
        float f1 = 1.0F - (0.0065F * f) / 288.15F;
        float f2 = 5.255781F;
        return 101325F * (float)Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f)
    {
        return getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f)
    {
        return (getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * f));
    }

    public float getAirDensityFactor(float f)
    {
        return getAirDensity(f) / 1.225F;
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float getMpsFromKmh(float f)
    {
        return f / 3.6F;
    }

    public float calculateMach()
    {
        return super.FM.getSpeedKMH() / getMachForAlt(super.FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(super.FM.getAltitude()) - super.FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = super.FM.getSpeedKMH() - getMachForAlt(super.FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if((double)calculateMach() <= 1.0D)
        {
            super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if((double)calculateMach() >= 1.0D)
        {
            super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(((FlightModelMain) (super.FM)).VmaxAllowed > 1500F)
            super.FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log("Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(super.FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if((double)calculateMach() > 1.01D || (double)calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster())
            if(curthrl == -1F)
            {
                curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
            } else
            {
                curthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
                if(curthrl < 1.05F)
                {
                    if((curthrl - oldthrl) / f > 20F && ((FlightModelMain) (super.FM)).EI.engines[0].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                            HUD.log("Compressor Stall!");
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.01D * (double)(((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
                        ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            ((FlightModelMain) (super.FM)).AS.hitEngine(this, 0, 100);
                        if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(this);
                    }
                    if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && ((FlightModelMain) (super.FM)).EI.engines[0].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6)
                    {
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.001D * (double)(((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
                        ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                        {
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log("Engine Flameout!");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineStops(this);
                        } else
                        if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                            HUD.log("Compressor Stall!");
                    }
                }
                oldthrl = curthrl;
            }
    }

    private boolean sirenaWarning(float f)
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(!Aircraft.isValid(aircraft))
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
        Aircraft aircraft1 = War.getNearestEnemy(this, f);
        if(!Aircraft.isValid(aircraft1))
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        if(aircraft1.getArmy() != World.getPlayerArmy() && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && aircraft1 != World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
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
            float f1 = 57.32484F * (float)Math.atan2(d8, -d7);
            int k = (int)(Math.floor((int)f1) - 90D);
            if(k < 0)
                k += 360;
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
            float f2 = 57.32484F * (float)Math.atan2(i1, d11);
            int j1 = (int)(Math.floor((int)f2) - 90D);
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
        if(this != World.getPlayerAircraft())
            return;
        if(flag && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else
        if(!flag && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        //TODO
        

    	((FlightModelMain) (super.FM)).CT.bHasVarWingControl = true;
    	((FlightModelMain) (super.FM)).CT.bHasVarWingControlSwitch = true;
    	((FlightModelMain) (super.FM)).CT.nVarWingStages = 3;
    	((FlightModelMain) (super.FM)).CT.VarWingStage = new float[((FlightModelMain) (super.FM)).CT.nVarWingStages];
    	((FlightModelMain) (super.FM)).CT.VarWingStage[0] = 1.0f;
    	((FlightModelMain) (super.FM)).CT.VarWingStage[1] = 0.45f;
    	((FlightModelMain) (super.FM)).CT.VarWingStage[2] = 0.08f;
    	((FlightModelMain) (super.FM)).CT.VarWingStageText = new String[((FlightModelMain) (super.FM)).CT.nVarWingStages];
    	((FlightModelMain) (super.FM)).CT.VarWingStageText[0] = "16°";
    	((FlightModelMain) (super.FM)).CT.VarWingStageText[1] = "45°";
    	((FlightModelMain) (super.FM)).CT.VarWingStageText[2] = "72°";


        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
        FM.CT.bHasBombSelect = true;
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
        if(thisWeaponsName.endsWith("3xDroptank"))
        {
            hierMesh().chunkVisible("PylonWL", true);
            hierMesh().chunkVisible("PylonWR", true);
        }
        if(thisWeaponsName.endsWith("2xDroptank"))
        {
            hierMesh().chunkVisible("PylonWL", true);
            hierMesh().chunkVisible("PylonWR", true);
        }
        if(thisWeaponsName.startsWith("Fighter_2:"))
        {
            hierMesh().chunkVisible("PylonAPUL", true);
            hierMesh().chunkVisible("PylonAPUR", true);
        }
        if(thisWeaponsName.startsWith("Def"))
        {
            hierMesh().chunkVisible("PylonC", false);
            hierMesh().chunkVisible("PylonC2", false);
            hierMesh().chunkVisible("PylonC3", false);
            hierMesh().chunkVisible("PylonR", false);
            hierMesh().chunkVisible("PylonL", false);
        }
        if(thisWeaponsName.startsWith("non"))
        {
            hierMesh().chunkVisible("PylonC", false);
            hierMesh().chunkVisible("PylonC2", false);
            hierMesh().chunkVisible("PylonC3", false);
            hierMesh().chunkVisible("PylonR", false);
            hierMesh().chunkVisible("PylonL", false);
        }
        if(super.thisWeaponsName.endsWith("RATO"))
        {
            for(int i = 0; i < 2; i++)
                try
                {
                    booster[i] = new BombSPRD99();
                    ((Actor) (booster[i])).pos.setBase(this, findHook("_BoosterH" + (i + 1)), false);
                    ((Actor) (booster[i])).pos.resetAsBase();
                    booster[i].drawing(true);
                }
                catch(Exception exception)
                {
                    debugprintln("Structure corrupt - can't hang SPRD-99..");
                }

        }
    }

    private float flapsMovement(float f, float f1, float f2, float f3, float f4)
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

    public void update(float f)
    {
        super.update(f);
        guidedMissileUtils.update();
        if(super.FM.getSpeed() > 5F)
        {
            moveSlats(f);
            bSlatsOff = false;
        } else
        {
            slatsOff();
        }
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteMiG21bis/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-10.5D, 0.0D, 0.59999999999999998D), new Orient(0.0F, 90F, 0.0F));
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
        if(super.FM.getSpeedKMH() > 700F && ((FlightModelMain) (super.FM)).CT.bHasFlapsControl)
        {
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
        } else
        {
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
        }
        if(((FlightModelMain) (super.FM)).CT.FlapsControl < 0.0F)
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
        if(((FlightModelMain) (super.FM)).CT.FlapsControl > 1.0F)
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 1.0F;
        if(((FlightModelMain) (super.FM)).CT.getFlap() < ((FlightModelMain) (super.FM)).CT.FlapsControl)
            ((FlightModelMain) (super.FM)).CT.forceFlaps(flapsMovement(f, ((FlightModelMain) (super.FM)).CT.FlapsControl, ((FlightModelMain) (super.FM)).CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        else
        if(((FlightModelMain) (super.FM)).CT.getFlap() > ((FlightModelMain) (super.FM)).CT.FlapsControl)
            ((FlightModelMain) (super.FM)).CT.forceFlaps(flapsMovement(f, ((FlightModelMain) (super.FM)).CT.FlapsControl, ((FlightModelMain) (super.FM)).CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        for(int i = 1; i < 19; i++)
            hierMesh().chunkSetAngles("EngineExhaustFlap" + i, 0.0F, -38F * ((FlightModelMain) (super.FM)).CT.getPowerControl(), 0.0F);

        resetYPRmodifier();
        float f1 = ((FlightModelMain) (super.FM)).CT.getPowerControl() * 1.5F;
        Aircraft.xyz[0] = Aircraft.cvt(f1, 0.0F, 1.5F, 0.0F, 1.5F);
        hierMesh().chunkSetLocate("EffectBox", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(calculateMach() / 1.9F, 0.0F, 1.0F, 0.0F, 0.15F);
        hierMesh().chunkSetLocate("Cone", Aircraft.xyz, Aircraft.ypr);
        float f2 = super.FM.getSpeedKMH() - 1000F;
        if(f2 < 0.0F)
            f2 = 0.0F;
        ((FlightModelMain) (super.FM)).CT.dvGear = 0.2F - f2 / 1000F;
        if(((FlightModelMain) (super.FM)).CT.dvGear < 0.0F)
            ((FlightModelMain) (super.FM)).CT.dvGear = 0.0F;
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        soundbarier();
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
            if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 0.5F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6)
            {
                if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 0.5F)
                    if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F)
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 5);
                    else
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 3);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
            }
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
        {
            if(((FlightModelMain) (super.FM)).AP.way.isLanding() && ((FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() > 40F)
            {
                ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
                if(((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
                    ((FlightModelMain) (super.FM)).CT.DragChuteControl = 1.0F;
            }
            if(((FlightModelMain) (super.FM)).AP.way.isLanding() && ((FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() < 40F)
            {
                ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
                if(super.FM.getSpeed() < 20F)
                    ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
            }
        }
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        if(super.thisWeaponsName != null && (super.FM instanceof Pilot) && bHasBoosters && super.thisWeaponsName.endsWith("RATO"))
        {
            if(super.FM.getAltitude() > 300F && boosterFireOutTime == -1L && ((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z != 0.0D && World.Rnd().nextFloat() < 0.05F)
            {
                doCutBoosters();
                ((FlightModelMain) (super.FM)).AS.setGliderBoostOff();
                bHasBoosters = false;
            }
            if(bHasBoosters && boosterFireOutTime == -1L && ((FlightModelMain) (super.FM)).Gears.onGround() && ((FlightModelMain) (super.FM)).EI.getPowerOutput() > 0.8F && super.FM.getSpeedKMH() > 20F)
            {
                boosterFireOutTime = Time.current() + 6000L;
                doFireBoosters();
                ((FlightModelMain) (super.FM)).AS.setGliderBoostOn();
            }
            if(bHasBoosters && boosterFireOutTime > 0L)
            {
                if(Time.current() < boosterFireOutTime)
                    ((FlightModelMain) (super.FM)).producedAF.x += 20000D;
                if(Time.current() > boosterFireOutTime + 20000L)
                {
                    doCutBoosters();
                    ((FlightModelMain) (super.FM)).AS.setGliderBoostOff();
                    bHasBoosters = false;
                }
            }
        }
        if(((FlightModelMain) (super.FM)).AS.bNavLightsOn && ((FlightModelMain) (super.FM)).CT.getGear() >= 1.0F)
            bGearLightsOn = true;
        else
            bGearLightsOn = false;
        if(bGearLightState != bGearLightsOn)
            doSetGearLightsState(bGearLightsOn);
        
        
        
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
        {
        	supersonicDrag();
        	if(FM.Gears.onGround())
        steering();
        }
        else
        {
            computeLift();
            computeSubsonicLimiter();
            computeSubsonicLimiter2();
            
            computeLoadoutsDrag();
            computeSupersonicLimiter();
        }
        computeVarWing();
        
    }
    
    
    
    //TODO:
    
    
    
    private void supersonicDrag()
    {
    	float Mach = calculateMach();
    	Polares polares = (Polares)Reflection.getValue(FM, "Wing");
    	
    	float Ro = getAirDensity(super.FM.getAltitude());
    	float V = super.FM.getSpeed();
    	
    	
    	//Drag at 16°:
    	float drag_16 = 0;
    	if(Mach < 0.8f)
    	drag_16 = (float)cvt(Mach, 0.65f, 0.8f, 0.0f, 0.003f);
    	else
    		drag_16 = (float)cvt(Mach, 0.8f, 1f, 0.003f, 0.0267f);
    	//Drag at 45°:
    	float drag_45 = 0;
    	if(Mach < 0.9f)
    	drag_45 = (float)cvt(Mach, 0.7f, 0.9f, 0.0f, 0.0034f);
    	if(Mach >= 0.9f && Mach < 1.05f)
        	drag_45 = (float)cvt(Mach, 0.9f, 1.05f, 0.0034f, 0.0184f);
    	if(Mach >= 1.05f && Mach < 1.2f)
            drag_45 = (float)cvt(Mach, 1.05f, 1.2f, 0.0184f, 0.0224f);
    	if(Mach >= 1.2f && Mach < 1.5f)
        	drag_45 = (float)cvt(Mach, 1.2f, 1.5f, 0.0224f, 0.02238f);
    	if(Mach >= 1.5f)
        	drag_45 = (float)cvt(Mach, 1.2f, 1.5f, 0.02238f, 0.0192f);
    	//Drag at 72°:
    	float drag_72 = 0;
    	if(Mach < 0.9f)
    		drag_72 = (float)cvt(Mach, 0.7f, 0.9f, 0.0f, 0.0028f);
    	if(Mach >= 0.9f && Mach < 1.0f)
    		drag_72 = (float)cvt(Mach, 0.9f, 1.0f, 0.0028f, 0.0095f);
    	if(Mach >= 1.0f && Mach < 1.1f)
    		drag_72 = (float)cvt(Mach, 1.0f, 1.1f, 0.095f, 0.012f);
    	if(Mach >= 1.1f && Mach < 1.2f)
    		drag_72 = (float)cvt(Mach, 1.1f, 1.2f, 0.012f, 0.015f);
    	if(Mach >= 1.5f)
    		drag_72 = (float)cvt(Mach, 1.2f, 2.5f, 0.015f, 0.0045f);
    	float drag = 0;
    	if (polares.CxMin_0 < 0.0216f)
    	{
    		drag = (float) cvt(polares.CxMin_0, 0.0233f, 0.0216f, drag_16, drag_45);
    	}
    	else
    	{
    		drag = (float) cvt(polares.CxMin_0, 0.0216f, 0.0205f, drag_45, drag_72);
    	}
    	
    	drag = 0.5f * Ro * V*V * drag * ((FlightModelMain) (super.FM)).Sq.squareWing;
    	((FlightModelMain) (super.FM)).producedAF.x -= drag; 
    	
    	float lift = super.FM.getAOA();
    	lift /=19.75f;
    	lift = cvt(polares.CxMin_0, 0.0216f, 0.0205f, 0, lift);
    	lift*=Math.abs(lift);
    	lift*=0.2;
    	lift = 0.5f * Ro * V*V * lift * ((FlightModelMain) (super.FM)).Sq.squareWing;
    	((FlightModelMain) (super.FM)).producedAF.z += lift; 
    	
    	
    }
    
    private void steering()
    {
    	double rudder = - ((FlightModelMain) (super.FM)).CT.getRudder();
    	float speed = super.FM.getSpeed();
    	
    	if(speed < 10)
    		rudder*=35f;
    	else
    		rudder *=5f;
    	
    	float mass = ((FlightModelMain) (super.FM)).M.getFullMass();
    	float Fn = mass * ((speed*speed)/5.9f) * (float)Math.tan(Math.toRadians(rudder));
    	if(Fn>27000)
    		Fn=27000;
    	if(Fn<-27000)
    		Fn=-27000;
    	
    	((FlightModelMain) (super.FM)).producedAF.y += Fn;
    	
    	float roll = Fn*0.75f;
    	((FlightModelMain) (super.FM)).producedAM.x += roll;

    }

    private void doSetGearLightsState(boolean flag)
    {
        for(int i = 0; i < gearLightsEffects.length; i++)
        {
            if(gearLightsEffects[i] != null)
            {
                Eff3DActor.finish(gearLightsEffects[i]);
                gearLightsLights[i].light.setEmit(0.0F, 0.0F);
            }
            gearLightsEffects[i] = null;
        }

        if(flag)
        {
            gearLightsEffects[0] = Eff3DActor.New(this, findHook("_GearLightC"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            gearLightsEffects[1] = Eff3DActor.New(this, findHook("_GearLightL"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            gearLightsEffects[2] = Eff3DActor.New(this, findHook("_GearLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            for(int j = 0; j < gearLightsEffects.length; j++)
            {
                Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                Loc loc1 = new Loc();
                loc1.set(1.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                if(j == 0)
                    findHook("_GearLightC").computePos(this, loc, loc1);
                if(j == 1)
                    findHook("_GearLightL").computePos(this, loc, loc1);
                if(j == 2)
                    findHook("_GearLightR").computePos(this, loc, loc1);
                Point3d point3d = loc1.getPoint();
                gearLightsLights[j] = new LightPointActor(new LightPoint(), point3d);
                gearLightsLights[j].light.setColor(0.5F, 0.6F, 0.7F);
                gearLightsLights[j].light.setEmit(1.0F, 8F);
                super.draw.lightMap().put("_GearLightC", gearLightsLights[0]);
                super.draw.lightMap().put("_GearLightL", gearLightsLights[1]);
                super.draw.lightMap().put("_GearLightR", gearLightsLights[2]);
            }

            bGearLightState = true;
        } else
        {
            bGearLightState = flag;
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
        case 2: // '\002'
        case 3: // '\003'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("HolyGrail02"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5: // '\005'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("HolyGrail02"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("HolyGrail01"), null, 2.5F, "3DO/Effects/Aircraft/afterburner.eff", -1F);
            // fall through

        case 4: // '\004'
        default:
            return;
        }
    }

    public void doEjectCatapult(final int i)
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 75D - (double)i * 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat0" + i);
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        };
        hierMesh().chunkVisible("Seat" + i + "_D0", false);
        FM.setTakenMortalDamage(true, null);
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
    }

    private void bailout()
    {
        if(overrideBailout)
            if(FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2)
            {
                if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F && FM.getSpeedKMH() < 15F)
                    FM.AS.astateBailoutStep = 11;
                else
                    FM.AS.astateBailoutStep = 2;
            } else
            if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
            {
                switch(FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(FM.CT.cockpitDoorControl < 0.5F || FM.getSpeedKMH() > 15F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    doRemoveBlister2();
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate = FM.AS;
                aircraftstate.astateBailoutStep = (byte)(aircraftstate.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            } else
            if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                byte byte0 = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate1 = FM.AS;
                aircraftstate1.astateBailoutStep = (byte)(aircraftstate1.astateBailoutStep + 1);
                if(byte0 == 11)
                {
                    FM.setTakenMortalDamage(true, null);
                    if((FM instanceof Maneuver) && ((Maneuver)FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)FM).set_maneuver(44);
                    }
                }
                if(FM.AS.astatePilotStates[byte0 - 11] < 99)
                {
                    doRemoveBodyFromPlane(byte0 - 10);
                    if(byte0 == 11)
                    {
                        doEjectCatapult(byte0 - 10);
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        lTimeNextEject = Time.current() + 1000L;
                        if(!bTwoSeat)
                        {
                            FM.AS.astateBailoutStep = -1;
                            overrideBailout = false;
                            FM.AS.bIsAboutToBailout = true;
                            ejectComplete = true;
                            if(byte0 > 10 && byte0 <= 19)
                                EventLog.onBailedOut(this, byte0 - 11);
                        }
                    } else
                    if(bTwoSeat && byte0 == 12)
                    {
                        doEjectCatapult(byte0 - 10);
                        FM.AS.astateBailoutStep = 51;
                        super.FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    FM.AS.astatePilotStates[byte0 - 11] = 99;
                } else
                {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            if(!bHasSK1Seat)
            {
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(((FlightModelMain) (super.FM)).Vwld);
                wreckage.setSpeed(vector3d);
            }
        }
    }

    private final void doRemoveBlister2()
    {
        if(hierMesh().chunkFindCheck("Blister2_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister2_D0");
            if(!bHasSK1Seat)
            {
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister2_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(((FlightModelMain) (super.FM)).Vwld);
                wreckage.setSpeed(vector3d);
            }
        }
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        hierMesh().chunkVisible("Glass_Head1_D0", false);
    }

    public void computeSubsonicLimiter()
    {
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() == 6 && (double)calculateMach() >= 0.94999999999999996D)
            FM.Sq.dragParasiteCx += 0.0004F;
    }

    public void computeSubsonicLimiter2()
    {
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && (double)calculateMach() >= 0.69999999999999996D)
            if((double)f > 12D)
                f1 = 4F;
            else
            if((double)f > 12D && (this instanceof MIG_23BN) || (this instanceof MIG_27K))
                f1 = 3F;
            else
            if((this instanceof MIG_23ML) || (this instanceof MIG_23MLA) || (this instanceof MIG_23MLD) || (this instanceof MIG_23P))
                f1 = 24F - (5F * f) / 3F;
            else
            if((this instanceof MIG_23M) || (this instanceof MIG_23MF))
                f1 = 23F - (19F * f) / 12F;
            else
            if((this instanceof MIG_23BN) || (this instanceof MIG_27K))
                f1 = 20F - (17F * f) / 12F;
        FM.producedAF.x -= f1 * 1000F;
    }

    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float f = calculateMach();
        if(f < 0.9F)
            polares.lineCyCoeff = 0.075F;
        else
        if(f < 2.4F)
        {
            float f1 = f * f;
            float f2 = f1 * f;
            float f3 = f2 * f;
            polares.lineCyCoeff = (((0.01921F * f3 - 0.178517F * f2) + 0.601783F * f1) - 0.881919F * f) + 0.498818F;
        } else
        {
            polares.lineCyCoeff = 0.012F;
        }
    }

    public void computeSupersonicLimiter()
    {
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6 && (double)calculateMach() >= 1.05D)
            if(f > 4F)
                f1 = 0.0F;
            else
                f1 = 0.00027F - 6.75E-005F * f;
        ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += f1;
    }

    public void computeLoadoutsDrag()
    {
        if(thisWeaponsName.startsWith("Fighter:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.0001F;
        if(thisWeaponsName.startsWith("Fighter_2:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.00017F;
        if(thisWeaponsName.startsWith("GAttack:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.0001F;
        if(thisWeaponsName.startsWith("GAttack_1:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.00025F;
        if(thisWeaponsName.startsWith("GAttack_2:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.0004F;
        if(thisWeaponsName.startsWith("GAttack_3"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.0008F;
        if(thisWeaponsName.startsWith("GAttack_4"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.0006F;
        if(thisWeaponsName.startsWith("GAttack_5:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.001F;
        if(thisWeaponsName.startsWith("GAttack_6:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.00025F;
        if(thisWeaponsName.startsWith("GAttack_7:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.00035F;
        if(thisWeaponsName.startsWith("GAttack_8:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.00045F;
        if(thisWeaponsName.startsWith("GAttack_9:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.00015F;
        if(thisWeaponsName.startsWith("GAttack_10:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.00035F;
        if(thisWeaponsName.startsWith("GAttack_11:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.0002F;
        if(thisWeaponsName.startsWith("GAttack_12:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.0005F;
        if(thisWeaponsName.startsWith("GAttack_13:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.0003F;
        if(thisWeaponsName.startsWith("GAttack_14:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.00055F;
        if(thisWeaponsName.startsWith("Ferry:"))
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 7E-005F;
    }
//TODO: tick
    /*
    public void tick()
    {
    	
    	if(((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot)))
    	{
    	int varWing_switch = ((FlightModelMain) (super.FM)).CT.VarWingControlSwitch;
    	float varWing_control = ((FlightModelMain) (super.FM)).CT.VarWingStage[varWing_switch];
    	
    	varWing = varWing * 0.9997f + varWing_control * 0.0003f;
    	moveVarWing(varWing);
    	}
    	
    }*/
    protected void moveVarWing(float f)
    {
        float f1 = -55F * f;
        hierMesh().chunkSetAngles("WingPivotL", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("WingPivotR", 0.0F, f1, 0.0F);
        if(((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot)))
        {
        	
        	Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        	if(f>=0.45)
        	{
        		float param = cvt(f, 1f, 0.45f, -0.151f, -0.1659f);
        		polares.Cy0_0 = param;
        		param = cvt(f, 1f, 0.45f, 0.084f, 0.07125f);
        		polares.lineCyCoeff = param;
        		param = cvt(f, 1f, 0.45f, 18.25f, 20.75f);
        		polares.AOACritH_0 = param;
        		param = cvt(f, 1f, 0.45f, 1.03f, 1.05f);
        		polares.CyCritH_0 = param;
        		param = cvt(f, 1f, 0.45f, 0.0233f, 0.0216f);
        		polares.CxMin_0 = param;
        		param = cvt(f, 1f, 0.45f, 0.0009f, 0.00103f);
        		polares.parabCxCoeff_0 = param;
        		param = cvt(f, 1f, 0.45f, 37.27f, 35.3f);
        		((FlightModelMain) (super.FM)).Sq.squareWing = param;
        	}
        	else
        	{
        		float param = cvt(f, 0.45f, 0.08f, -0.1659f, 0f);
        		polares.Cy0_0 = param;
        		param = cvt(f, 0.45f, 0.08f, 0.07125f, 0.0405f);
        		polares.lineCyCoeff = param;
        		param = cvt(f, 0.45f, 0.08f, 20.75f, 19.75f);
        		polares.AOACritH_0 = param;
        		param = cvt(f, 0.45f, 0.08f, 1.05f, 0.75f);
        		polares.CyCritH_0 = param;
        		param = cvt(f, 0.45f, 0.08f, 0.02166f, 0.0205f);
        		polares.CxMin_0 = param;
        		param = cvt(f, 0.45f, 0.08f, 0.00103f, 0.00113f);
        		polares.parabCxCoeff_0 = param;
        		param = cvt(f, 0.45f, 0.08f, 35.3f, 34.16f);
        		((FlightModelMain) (super.FM)).Sq.squareWing = param;
        	}
        }
    }

    public void computeVarWing()
    {
    	if(!((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot)))
    	{
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if((double)calculateMach() > 0.90000000000000002D && FM.EI.engines[0].getThrustOutput() < 1.001F)
        {
            ((FlightModelMain) (super.FM)).CT.VarWingControl = 0.08F;
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
            Reflection.setFloat(FM, "GCenter", Reflection.getFloat(FM, "GCenter") - 0.7F);
            polares.CxMin_0 = 0.021F;
            polares.CyCritH_0 = 0.9F;
            polares.Cy0_0 = 0.007F;
            polares.parabCxCoeff_0 = 0.00113F;
        }
        if((double)calculateMach() > 0.5D && FM.EI.engines[0].getThrustOutput() > 1.001F)
        {
            ((FlightModelMain) (super.FM)).CT.VarWingControl = 0.08F;
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
            Reflection.setFloat(FM, "GCenter", Reflection.getFloat(FM, "GCenter") - 0.7F);
            polares.CxMin_0 = 0.021F;
            polares.CyCritH_0 = 0.9F;
            polares.Cy0_0 = 0.007F;
            polares.parabCxCoeff_0 = 0.00113F;
        }
        if((double)calculateMach() > 0.45000000000000001D && (double)calculateMach() < 0.90000000000000002D && FM.EI.engines[0].getThrustOutput() < 0.9F)
        {
            ((FlightModelMain) (super.FM)).CT.VarWingControl = 0.45F;
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
            polares.CxMin_0 = 0.023F;
            polares.CyCritH_0 = 1.0F;
            polares.Cy0_0 = 0.012F;
            polares.parabCxCoeff_0 = 0.00103F;
        }
        if((double)calculateMach() > 0.45000000000000001D && (double)calculateMach() < 0.90000000000000002D && (double)FM.getAOA() >= 10D)
        {
            ((FlightModelMain) (super.FM)).CT.VarWingControl = 0.45F;
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
            polares.CxMin_0 = 0.023F;
            polares.CyCritH_0 = 1.0F;
            polares.Cy0_0 = 0.012F;
            polares.parabCxCoeff_0 = 0.00103F;
        }
        if((double)calculateMach() > 0.45000000000000001D && (double)calculateMach() < 0.80000000000000004D && FM.EI.engines[0].getThrustOutput() < 0.8F)
        {
            ((FlightModelMain) (super.FM)).CT.VarWingControl = 1.0F;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
        if((double)calculateMach() < 0.65000000000000002D && (double)super.FM.getAOA() >= 10D)
        {
            ((FlightModelMain) (super.FM)).CT.VarWingControl = 1.0F;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
        if((double)calculateMach() < 0.45000000000000001D && FM.EI.engines[0].getStage() >= 6)
        {
            ((FlightModelMain) (super.FM)).CT.VarWingControl = 1.0F;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
        if(thisWeaponsName.endsWith("3xDroptank") || thisWeaponsName.endsWith("2xDroptank"))
        {
            ((FlightModelMain) (super.FM)).CT.VarWingControl = 1.0F;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
            polares.CxMin_0 = 0.026F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
    	}
    	else
    	{
    		int varWing_switch = ((FlightModelMain) (super.FM)).CT.VarWingControlSwitch;
    		((FlightModelMain) (super.FM)).CT.VarWingControl = ((FlightModelMain) (super.FM)).CT.VarWingStage[varWing_switch];
    		if(varWing_switch !=0)
        	{
                ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
                ((FlightModelMain) (super.FM)).CT.FlapsControl = 0f;
        	}
        	else
        	{

                ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
        	}
    		if(thisWeaponsName.endsWith("3xDroptank") || thisWeaponsName.endsWith("2xDroptank"))
            {
                ((FlightModelMain) (super.FM)).CT.VarWingControl = 1.0F;
                ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
                if(((FlightModelMain) (super.FM)).CT.FlapsControl>0.5f)
                	((FlightModelMain) (super.FM)).CT.FlapsControl = 0.5f;
            }
    	}
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(true);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(false);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = true;
            }
        if(i == 22)
        {
        	radar_aspect --;
        	switch (radar_aspect)
        	{
        	case 0:
                HUD.log("Aspect avt.");
                break;
        	case -1:

                HUD.log("ZPS");
        	}
        	if(radar_aspect < -1)radar_aspect = -1;
        	
        	
        }
        if(i == 23)
        {
        	radar_aspect ++;
        	switch (radar_aspect)
        	{
        	case 0:
                HUD.log("Aspect avt.");
                break;
        	case 1:

                HUD.log("PPS");
        	}
        	if(radar_aspect > 1) radar_aspect = 1;
        }
        if(i == 24)
        {
        	if(air_ground){
        		air_ground = false;
        		radarmode = 0;        	}
        	else {
        		air_ground = true;
        		radarmode = 0;
        	}
        }
        
    }

    public boolean typeDiveBomberToggleAutomation()
    {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset()
    {
    }

    public void typeDiveBomberAdjAltitudePlus()
    {
    }

    public void typeDiveBomberAdjAltitudeMinus()
    {
    }

    public void typeDiveBomberAdjVelocityReset()
    {
    }

    public void typeDiveBomberAdjVelocityPlus()
    {
    }

    public void typeDiveBomberAdjVelocityMinus()
    {
    }

    public void typeDiveBomberAdjDiveAngleReset()
    {
    }

    public void typeDiveBomberAdjDiveAnglePlus()
    {
    }

    public void typeDiveBomberAdjDiveAngleMinus()
    {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public boolean typeBomberToggleAutomation()
    {
    	/*
        bSightAutomation = !bSightAutomation;
        bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (bSightAutomation ? "ON" : "OFF"));
        return bSightAutomation;
        */
    	if(radarmode==2)
    	{
    		TP_lock=true;
    	}
    	return false;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
    	
    	
    	{
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    	}
    }

    public void typeBomberAdjDistanceMinus()
    {
    	
    	
    	{
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    	}
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }


    public void typeBomberAdjSideslipPlus()
    {
    	rangeGate_azimuth += 0.025f;
    	if (rangeGate_azimuth > 1) rangeGate_azimuth = 1;
    	
    }

    public void typeBomberAdjSideslipMinus()
    {
    	rangeGate_azimuth -= 0.025f;
		if (rangeGate_azimuth < -1) rangeGate_azimuth = -1;
		
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        
    }

    public void typeBomberAdjAltitudeMinus()
    {
       
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 250F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        
    }

    public void typeBomberAdjSpeedMinus()
    {
       
    }

    public void typeBomberUpdate(float f)
    {
        if((double)Math.abs(FM.Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * f;
            if(fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if(fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * f;
        else
        if(bSightAutomation)
        {
            fSightCurDistance -= (fSightCurSpeed / 3.6F) * f;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / fSightCurAltitude));
            if((double)fSightCurDistance < (double)(fSightCurSpeed / 3.6F) * Math.sqrt(fSightCurAltitude * 0.2038736F))
                bSightBombDump = true;
            if(bSightBombDump)
                if(FM.isTick(3, 0))
                {
                    if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    FM.CT.WeaponControl[3] = false;
                }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(fSightCurDistance);
        netmsgguaranted.writeByte((int)fSightCurForwardAngle);
        netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeFloat(fSightCurSpeed);
        netmsgguaranted.writeByte((int)(fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readUnsignedByte();
        fSightCurSideslip = -3F + (float)netmsginput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = netmsginput.readFloat();
        fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.1F;
    	rangeGate_azimuth += 0.025f;
    	if (rangeGate_azimuth > 1) rangeGate_azimuth = 1;

    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.1F;    	
        rangeGate_azimuth -= 0.025f;
    	if (rangeGate_azimuth < -1) rangeGate_azimuth = -1;

    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.1F;
        
        if(radarmode==1||radarmode==11)
        {
        	if(RP_23_elevation == 6f)return;
        if(RP_23_elevation == -4f) {RP_23_elevation = -3f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == -3f) {RP_23_elevation = -2f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == -2f) {RP_23_elevation = -1f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == -1f) {RP_23_elevation = 0f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 0) {RP_23_elevation = 0.5f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 0.5f) {RP_23_elevation = 1f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 1f) {RP_23_elevation = 2f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 2f) {RP_23_elevation = 3f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 3f) {RP_23_elevation = 4; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 4f) {RP_23_elevation = 6; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        }
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.1F;
        if(radarmode==1||radarmode==11)
        {
        	if(RP_23_elevation == -4f)return;
        if(RP_23_elevation == -3f) {RP_23_elevation = -4f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == -2f) {RP_23_elevation = -3f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == -1f) {RP_23_elevation = -2f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 0) {RP_23_elevation = -1f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 0.5f) {RP_23_elevation = 0f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 1f) {RP_23_elevation = 0.5f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 2f) {RP_23_elevation = 1f; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 3f) {RP_23_elevation = 2; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 4f) {RP_23_elevation = 3; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        if(RP_23_elevation == 6f) {RP_23_elevation = 4; HUD.log(AircraftHotKeys.hudLogWeaponId, "dH " + RP_23_elevation + "°"); return;}
        }
        //TODO
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(super.FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(super.FM.isPlayers())
            bChangedPit = true;
    }

	public Actor getSemiActiveRadarLockedActor() {
		if(victim_RP != null){if(isValid(victim_RP)&&isAlive(victim_RP)&&victim_RP instanceof TypeRadarWarningReceiver)((TypeRadarWarningReceiver) victim_RP).myRadarLockYou(this, "aircraft.APR25AAA");}
		return victim_RP;
	}

	public boolean getSemiActiveRadarOn() {
		// TODO Auto-generated method stub
		boolean track = (radarmode==100||radarmode==101);
		return track;
	}

	public Actor setSemiActiveRadarLockedActor(Actor arg0) {
		return null;
	}

	public boolean setSemiActiveRadarOn(boolean arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public float getMyotkaAnglesVertical()
	{
		return Myotka_x;
	}
	
	public float getMyotkaAnglesHorizontal()
	{
		return Myotka_y;
	}
	
	public Actor getMyotkaActor()
	{
		if(victim_RP != null){
			return victim_RP;
		}
		if(victim_TP != null){
			return victim_TP;
		}
		return null;
	}
	
	public int getMyotkaMode()
	{
		switch(radarmode)
		{
		case 100:
		{
			return 2;
		}
		case 101:
		{
			return 2;
		}
		case 200:
		{
			return 2;
		}
		case 201:
		{
			return 2;
		}
		case 13:
		{
			return 1;
		}
			default: return 0;
		}
	}
    

    private float airBrake_State;
    private boolean pylonOccupied_DT;
    private boolean pylonOccupied_Py;
    private float oldthrl;
    private float curthrl;
    private float engineSurgeDamage;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private boolean pylonOccupied;
    private Bomb booster[];
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private boolean bRadarWarning;
    protected boolean bHasSK1Seat;
    private static Actor hunted = null;
    private Eff3DActor gearLightsEffects[];
    private LightPointActor gearLightsLights[];
    private boolean bGearLightsOn;
    private boolean bGearLightState;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private static final float NEG_G_TOLERANCE_FACTOR = 2.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 2F;
    private static final float POS_G_TOLERANCE_FACTOR = 5.5F;
    private static final float POS_G_TIME_FACTOR = 4F;
    private static final float POS_G_RECOVERY_FACTOR = 2.5F;
    public boolean APmode1;
    public boolean APmode2;
    public int radarmode;
    public int targetnum;
    public float lockrange;
    public int RP_23_range;
    private long twait;
    private long lTimeNextEject;
    private boolean bTwoSeat;
    protected boolean bSlatsOff;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    private float deltaAzimuth;
    private float deltaTangage;
    public static boolean bChangedPit = false;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    public float rangeGate_range;
    public float rangeGate_azimuth;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    public boolean bToFire;
    private long tX4Prev;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    public float Timer1;
    public float Timer2;
    private int freq;
    private int counter;
    private int error;
    private long raretimer;
    public float TP_23_model;
    public float[] RP_23_model = new float[10];
    public float[] RP_23_doppler = new float[3];
    public boolean TP_lock;
    public boolean RP_lock;
    public Actor victim_TP;
    public Actor victim_RP;
    public float RP_23_elevation;
    public float Myotka_x;
    public float Myotka_y;
    public boolean BSV_SC;
    public int BSV_mode;
    public int radar_aspect;
    public boolean air_ground;
    public float varWing;
    public float ECCM_power;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG_23.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}