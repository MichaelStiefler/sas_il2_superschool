// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11/9/2012 7:27:33 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   F_18S.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.*;
import com.maddox.util.HashMapExt;












import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme2, TypeSupersonic, TypeFighter, TypeBNZFighter, 
//            TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeNoOpenCanopyFly, 
//            TypeX4Carrier, TypeGuidedBombCarrier, Aircraft, Cockpit, 
//            PaintScheme, EjectionSeat

public class F_18S extends Scheme2
    implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeX4Carrier, TypeGuidedBombCarrier, TypeBomber, TypeAcePlane, TypeLaserSpotter
{

    public float getDragForce(float f, float f1, float f2, float f3)
    {
        throw new UnsupportedOperationException("getDragForce not supported anymore.");
    }

    public float getDragInGravity(float f, float f1, float f2, float f3, float f4, float f5)
    {
        throw new UnsupportedOperationException("getDragInGravity supported anymore.");
    }

    public float getForceInGravity(float f, float f1, float f2)
    {
        throw new UnsupportedOperationException("getForceInGravity supported anymore.");
    }

    public float getDegPerSec(float f, float f1)
    {
        throw new UnsupportedOperationException("getDegPerSec supported anymore.");
    }

    public float getGForce(float f, float f1)
    {
        throw new UnsupportedOperationException("getGForce supported anymore.");
    }

    public F_18S()
    {
        lLightHook = new Hook[4];
        needUpdateHook = false;
        SonicBoom = 0.0F;
        bSlatsOff = false;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 2;
        k14WingspanType = 0;
        k14Distance = 200F;
        AirBrakeControl = 0.0F;
        DragChuteControl = 0.0F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        gearTargetAngle = -1F;
        gearCurrentAngle = -1F;
        super.bWantBeaconKeys = true;
        bToFire = false;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        isGuidingBomb = false;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        trimauto = false;
        t1 = 0L;
        leftscreen = 0;
        leftscreen = 1000;
        targetnum = 0;
        Bingofuel = 1000;
    }
    
    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
    }
    
    public void typeBomberAdjDistanceReset()
    {
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle++;
        if(fSightCurForwardAngle > 85F)
            fSightCurForwardAngle = 85F;
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)fSightCurForwardAngle)
        });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if(fSightCurForwardAngle < 0.0F)
            fSightCurForwardAngle = 0.0F;
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)fSightCurForwardAngle)
        });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }
   
    
    public void typeBomberAdjSideslipPlus()
    {   	
    	targetnum++;
    	HUD.log(AircraftHotKeys.hudLogWeaponId, "Target number  " + targetnum);
    }

    public void typeBomberAdjSideslipMinus()
    {
    	targetnum--;
    	if(targetnum<0)
    		targetnum = 0;
    	HUD.log(AircraftHotKeys.hudLogWeaponId, "Target number  " + targetnum);
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 3000F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 50F;
        if(fSightCurAltitude > 50000F)
            fSightCurAltitude = 50000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 50F;
        if(fSightCurAltitude < 1000F)
            fSightCurAltitude = 1000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 200F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        leftscreen++;
        if(leftscreen>1)
        	leftscreen = 0;
        if(leftscreen == 0)
        {
        	if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Fuel");
        } else
        if(leftscreen == 1)
        {
        	if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: FPAS");
        } 
    }

    public void typeBomberAdjSpeedMinus()
    {
        Bingofuel+=500;
        if(Bingofuel>6000)
        	Bingofuel = 1000;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Bingofuel  " + Bingofuel);
    }
    
    public void typeBomberUpdate(float f)
    {
        
    }
    
    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
            throws IOException
        {
            netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
            netmsgguaranted.writeFloat(fSightCurDistance);
            netmsgguaranted.writeByte((int)fSightCurForwardAngle);
            netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
            netmsgguaranted.writeFloat(fSightCurAltitude);
            netmsgguaranted.writeByte((int)(fSightCurSpeed / 2.5F));
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
            fSightCurSpeed = (float)netmsginput.readUnsignedByte() * 2.5F;
            fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
        }

    private void laser(Point3d point3d)
    {
        point3d.z = World.land().HQ(point3d.x, point3d.y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(point3d.x, point3d.y, point3d.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void FLIR()
    {
        Vector3d vector3d = new Vector3d();
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(((actor instanceof Aircraft) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && !(actor instanceof StationaryGeneric) && !(actor instanceof TypeLaserSpotter) && actor.pos.getAbsPoint().distance(pos.getAbsPoint()) < 20000D)
            {
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                actor.pos.getAbs(point3d, orient);
                l.set(point3d, orient);
                Eff3DActor eff3dactor = Eff3DActor.New(actor, null, new Loc(), 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Glow.eff", 1.0F);
                eff3dactor.postDestroy(Time.current() + 1500L);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                if(actor instanceof Aircraft)
                    lightpointactor.light.setEmit(8F, 50F);
                else
                if(!(actor instanceof ArtilleryGeneric))
                    lightpointactor.light.setEmit(5F, 30F);
                else
                    lightpointactor.light.setEmit(3F, 10F);
                eff3dactor.draw.lightMap().put("light", lightpointactor);
            }
        }

    }

    public boolean typeGuidedBombCisMasterAlive()
    {
        return isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag)
    {
        isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding()
    {
        return isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag)
    {
        isGuidingBomb = flag;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.002F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.002F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.002F;
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

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(3.5F, 2F, 1.0F, 9.0F, 3.0F, 5.0F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
        actl = ((FlightModelMain) (super.FM)).SensRoll;
        ectl = ((FlightModelMain) (super.FM)).SensPitch;
        rctl = ((FlightModelMain) (super.FM)).SensYaw;
        FM.Skill = 3;
        t1 = Time.current();
        mass = this.FM.M.mass;
        leftscreen = 0;
    }

    public void updateLLights()
    {
        super.pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = new LightPointWorld[4];
                for(int i = 0; i < 4; i++)
                {
                    lLight[i] = new LightPointWorld();
                    lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    lLight[i].setEmit(0.0F, 0.0F);
                    try
                    {
                        lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    }
                    catch(Exception exception) { }
                }

            }
        } else
        {
            for(int j = 0; j < 4; j++)
                if(((FlightModelMain) (super.FM)).AS.astateLandingLightEffects[j] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    Engine.land();
                    if(Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                    {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        lLight[j].setPos(lLightP2);
                        float f = (float)lLightP1.distance(lLightPL);
                        float f1 = f * 0.5F + 60F;
                        float f2 = 0.7F - (0.8F * f * lightTime) / 2000F;
                        lLight[j].setEmit(f2, f1);
                    } else
                    {
                        lLight[j].setEmit(0.0F, 0.0F);
                    }
                } else
                if(lLight[j].getR() != 0.0F)
                    lLight[j].setEmit(0.0F, 0.0F);

        }
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

    public void rareAction(float f, boolean flag)
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = super.FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            F_18S f_18s = this;
            float f1 = mn;
            World.cur().getClass();
            f_18s.mn = f1 / Atmosphere.sonicSpeed((float)((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if((((FlightModelMain) (super.FM)).Gears.nearGround() || ((FlightModelMain) (super.FM)).Gears.onGround()) && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
        {
            if(((FlightModelMain) (super.FM)).AP.way.isLanding() && super.FM.getSpeed() > ((FlightModelMain) (super.FM)).VmaxFLAPS && super.FM.getSpeed() > ((FlightModelMain) (super.FM)).AP.way.curr().getV() * 1.4F)
            {
                if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 1.0F)
                    ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 25 && ((FlightModelMain) (super.FM)).AP.way.isLanding() && super.FM.getSpeed() < ((FlightModelMain) (super.FM)).VmaxFLAPS * 1.2F)
            {
                if(super.FM.getSpeed() > ((FlightModelMain) (super.FM)).VminFLAPS * 0.5F && ((FlightModelMain) (super.FM)).Gears.nearGround())
                {
                    if(((FlightModelMain) (super.FM)).Gears.onGround())
                    {
                        if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 1.0F)
                            ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
                        ((FlightModelMain) (super.FM)).CT.DragChuteControl = 1.0F;
                    } else
                    if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 1.0F)
                        ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
                } else
                if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 0.0F)
                    ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 66)
            {
                if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 0.0F)
                    ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 7 && ((FlightModelMain) (super.FM)).CT.AirBrakeControl != 1.0F)
                ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;  
        if(((com.maddox.il2.ai.air.Maneuver)super.FM).get_task() == 7)
        {
        com.maddox.JGP.Vector3d vector3d = new Vector3d();
        getSpeed(vector3d);
        com.maddox.JGP.Point3d point3d1 = new Point3d();
        pos.getAbs(point3d1);        
        float alt = (float)(FM.getAltitude() - com.maddox.il2.ai.World.land().HQ(point3d1.x, point3d1.y));        
        if(alt < 15 && vector3d.z < 0)
            vector3d.z = 0;
        setSpeed(vector3d);        
        }
        }
        if(FLIR)
            FLIR(); 
        
    }

    private final void UpdateLightIntensity()
    {
        if(World.getTimeofDay() >= 6F && World.getTimeofDay() < 7F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        else
        if(World.getTimeofDay() >= 18F && World.getTimeofDay() < 19F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        else
        if(World.getTimeofDay() >= 7F && World.getTimeofDay() < 18F)
            lightTime = 0.1F;
        else
            lightTime = 1.0F;
    }

    public boolean typeBomberToggleAutomation()
    {
    	k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
        	if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Bomb");
        } else
        if(k14Mode == 1)
        {
        	if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Gun");
        } else
        if(k14Mode == 2 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Off");
    	return true;
    }
    
    public boolean typeFighterAceMakerToggleAutomation()
    {
    	
        return true;
    }
    
    

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        if(k14WingspanType == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        if(k14WingspanType == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
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

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
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
        hierMesh().chunkVisible("Seat1_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 20F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.3F, 0.0F, -95F));
        hiermesh.chunkSetAngles("GearC24_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.3F, 0.0F, -76F));
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 75F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.3F, 0.0F, -33.4F));
        hiermesh.chunkSetAngles("GearR252_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.13F, 0.0F, 20F), 0.0F);
        hiermesh.chunkSetAngles("GearL252_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.13F, 0.0F, 20F), 0.0F);
        hiermesh.chunkSetAngles("GearR25_D0", Aircraft.cvt(f, 0.11F, 0.3F, 0.0F, -93.84F), Aircraft.cvt(f, 0.11F, 0.30F, 0.0F, -19.1F), Aircraft.cvt(f, 0.30F, 0.35F, 0.0F, 35F));
        hiermesh.chunkSetAngles("GearL25_D0", Aircraft.cvt(f, 0.11F, 0.3F, 0.0F, -93.84F), Aircraft.cvt(f, 0.11F, 0.30F, 0.0F, -19.1F), Aircraft.cvt(f, 0.30F, 0.35F, 0.0F, 35F));
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.3F, 0.35F, 0.0F, -20.45F));
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.3F, 0.35F, 0.0F, -20.45F));
        hiermesh.chunkSetAngles("GearR221_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.3F, 0.35F, 0.0F, 39.06F));
        hiermesh.chunkSetAngles("GearL221_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.3F, 0.35F, 0.0F, 39.06F));       
        hiermesh.chunkSetAngles("GearR32_D0", Aircraft.cvt(f, 0.3F, 0.4F, 0.0F, 43.53F), 0.0F, Aircraft.cvt(f, 0.3F, 0.4F, 0.0F, -17.53F));
        hiermesh.chunkSetAngles("GearL32_D0", Aircraft.cvt(f, 0.3F, 0.4F, 0.0F, 43.53F), 0.0F, Aircraft.cvt(f, 0.3F, 0.4F, 0.0F, -18.53F));
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -55F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 55F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -92F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 92F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 100F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -100F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.11F, 0.3F, 0.0F, 0.73F);
        hierMesh().chunkSetLocate("GearC25_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.12F, 0.40F, 0.0F, -0.39F);
        hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = ((FlightModelMain) (super.FM)).Gears.gWheelSinking[2];
        xyz[1] = Aircraft.cvt(2.9F * f, 0.0F, 0.4F, 0.0F, 0.39F);
        hierMesh().chunkSetLocate("GearC32_D0", xyz, ypr); 
        resetYPRmodifier();
        xyz[1] = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 0.39F);
        hierMesh().chunkSetLocate("GearC31_D0", xyz, ypr); 
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, -0.5F);
        xyz[2] = f;
        hierMesh().chunkSetLocate("GearR31_D0", xyz, ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 0.5F);
        xyz[2] = f;
        hierMesh().chunkSetLocate("GearL31_D0", xyz, ypr);
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.4F, 0.0F, 1.0F);
        hierMesh().chunkSetAngles("GearR21_D0", 0.0F, 0.0F, 55.5F * f);
        hierMesh().chunkSetAngles("GearR211_D0", 0.0F, 0.0F, 17.6F * f);
        hierMesh().chunkSetAngles("GearR212_D0", 0.0F, 0.0F, 17.51F * f);
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 14.51F * f);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.4F, 0.0F, -0.18F);
        hierMesh().chunkSetLocate("GearR213_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.4F, 0.0F, -0.17F);
        hierMesh().chunkSetLocate("GearR214_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.4F, 0.0F, 1.0F);
        hierMesh().chunkSetAngles("GearL21_D0", 0.0F, 0.0F, 55.5F * f);
        hierMesh().chunkSetAngles("GearL211_D0", 0.0F, 0.0F, 17.6F * f);
        hierMesh().chunkSetAngles("GearL212_D0", 0.0F, 0.0F, 17.51F * f);
        hierMesh().chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 14.51F * f);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.4F, 0.0F, 0.18F);
        hierMesh().chunkSetLocate("GearL213_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.4F, 0.0F, 0.17F);
        hierMesh().chunkSetLocate("GearL214_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
    	if(!((FlightModelMain) (super.FM)).Gears.onGround()) {
    	updateControlsVisuals2();} else
    	{
        }
    }
    
    public void moveSteering(float f)
    {
        if(FM.CT.getGear() > 0.8F)
        hierMesh().chunkSetAngles("GearC33_D0", 0.0F, 1.2F * f, 0.0F);
    }

    private final void updateControlsVisuals()
    {
        if(super.FM.getSpeedKMH() > 590F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * ((FlightModelMain) (super.FM)).CT.getElevator() + 27F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * ((FlightModelMain) (super.FM)).CT.getElevator() - 27F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -17F * ((FlightModelMain) (super.FM)).CT.getElevator() + 10F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -17F * ((FlightModelMain) (super.FM)).CT.getElevator() - 10F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F);
        }
    }
    
    private final void updateControlsVisuals2()
    {
        if(super.FM.getSpeedKMH() > 590F)
        {
            hierMesh().chunkSetAngles("RudderL_D0", 35F * ((FlightModelMain) (super.FM)).CT.getRudder() - 35F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F, 0.0F);
            hierMesh().chunkSetAngles("RudderR_D0", 35F * ((FlightModelMain) (super.FM)).CT.getRudder() - 35F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("RudderL_D0", 25F * ((FlightModelMain) (super.FM)).CT.getRudder() - 20F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F, 0.0F);
            hierMesh().chunkSetAngles("RudderR_D0", 25F * ((FlightModelMain) (super.FM)).CT.getRudder() - 20F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F, 0.0F);
        }
    }

    protected void moveElevator(float f)
    {
        updateControlsVisuals();
    }

    protected void moveAileron(float f)
    {
            updateControlsVisuals();
            updateControlsVisuals2(); 
            if(super.FM.getSpeedKMH() > 590F)
            {
                hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, 40F * f, 0.0F);
                hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -40F * f, 0.0F);
            } else
            {
                hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, 20F * f, 0.0F);
                hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -20F * f, 0.0F);
            }
    }

    protected void moveFlap(float f)
    {
            hierMesh().chunkSetAngles("Flap1_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 55F), 0.0F);
            hierMesh().chunkSetAngles("Flap2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 55F), 0.0F);
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, Aircraft.cvt(f, 0.25F, 0.29F, 0.0F,17F), 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, Aircraft.cvt(f, 0.25F, 0.29F, 0.0F, 17F), 0.0F);
            hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, Aircraft.cvt(f, 0.29F, 0.99F, 0.0F,38F), 0.0F);
            hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, Aircraft.cvt(f, 0.29F, 0.99F, 0.0F, 38F), 0.0F);   
    }

    

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = part(s);
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
                    getEnergyPastArmor(8.770001F, shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor(8.770001F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch(j)
                {
                case 1: // '\001'
                case 2: // '\002'
                    if(World.Rnd().nextFloat() < -10.5F && getEnergyPastArmor(1.1F, shot) > 200.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;
                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200.0F && World.Rnd().nextFloat() < -10.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200.00F && World.Rnd().nextFloat() <-10.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxengine1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 20F)
                {
                    ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 44000F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                {
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            } else
            	if(s.startsWith("xxengine2"))
                {
                    debuggunnery("Engine Module: Hit..");
                    if(s.endsWith("bloc"))
                        getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[1].getCylindersRatio() * 20F)
                    {
                        ((FlightModelMain) (super.FM)).EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                        debuggunnery("Engine Module: Engine Cams Hit, " + ((FlightModelMain) (super.FM)).EI.engines[1].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[1].getCylinders() + " Right..");
                        if(World.Rnd().nextFloat() < shot.power / 44000F)
                        {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 2);
                            debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                        }
                        if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                        {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 1);
                            debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                        }
                    }
                    if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 3);
                        debuggunnery("Engine Module: Hit - Engine Fires..");
                    }
                    s.endsWith("exht");
                } else	
            if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: mnine Gun (" + k + ") Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 1.5F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, l, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < -1.075F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            } else           
            if(s.startsWith("xxhyd"))
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
            {
            	hitChunk("CF1", shot);
            	hitChunk("CF2", shot);
            	hitChunk("Body1", shot);
            	getEnergyPastArmor(8.770001F, shot);
            } else
            if(s.startsWith("x2cf"))
            {
                hitChunk("CF2", shot);
                hitChunk("CF1", shot);
                hitChunk("Body1", shot);
                getEnergyPastArmor(8.770001F, shot);
            } else
            if(s.startsWith("xnose"))
                hitChunk("Nose1", shot);
            else
            if(s.startsWith("xBody"))
            {
                hitChunk("Body1", shot);
                hitChunk("CF2", shot);
                hitChunk("CF1", shot);
                getEnergyPastArmor(8.770001F, shot);
            }	
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
            if(s.startsWith("xrudderl"))
                hitChunk("RudderL", shot);
            else
            if(s.startsWith("xrudderr"))
                hitChunk("RudderR", shot);
            else	
            if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 2)
                    hitChunk("StabR", shot);
            } else
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
                if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                    hitChunk("WingLOut", shot);
                if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                    hitChunk("WingROut", shot);
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
                        hitChunk("Flap1", shot);
                    if(s.startsWith("xflap2"))
                        hitChunk("Flap2", shot);
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
                int i1;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    i1 = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    i1 = s.charAt(6) - 49;
                } else
                {
                    i1 = s.charAt(5) - 49;
                }
                hitFlesh(i1, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 13: // '\r'
            ((FlightModelMain) (super.FM)).Gears.cgear = false;
            float f = World.Rnd().nextFloat(0.0F, 1.0F);
            if(f < 0.1F)
            {
                ((FlightModelMain) (super.FM)).AS.hitEngine(this, 0, 100);
                ((FlightModelMain) (super.FM)).AS.hitEngine(this, 1, 100);
                if((double)World.Rnd().nextFloat(0.0F, 1.0F) < 0.48999999999999999D)
                {
                ((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(actor);
                ((FlightModelMain) (super.FM)).EI.engines[1].setEngineDies(actor);
                }               
            } else
            if((double)f > 0.55000000000000004D)
            {
            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(actor);
            ((FlightModelMain) (super.FM)).EI.engines[1].setEngineDies(actor);
            }
            return super.cutFM(i, j, actor);

        case 19: // '\023'
        {
            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(actor);
            ((FlightModelMain) (super.FM)).EI.engines[1].setEngineDies(actor);
        }    
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }
    
    

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 500F;
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (super.FM)).actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)((Interpolate) (super.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 800F)
                k14Distance = 800F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
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
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
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
        {
            for(int i = 0; i < 2; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 35F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Fans Surge!!!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
                            ((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                ((FlightModelMain) (super.FM)).AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                ((FlightModelMain) (super.FM)).EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -35F && (curthrl - oldthrl) / f > -100F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
                            ((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            {
                                if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                ((FlightModelMain) (super.FM)).EI.engines[i].setEngineStops(this);
                            } else
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Fans Surge!!!");
                        }
                    }
                    oldthrl = curthrl;
                }

        }
    }

    public void gearlimit()
    {
        float f = super.FM.getSpeedKMH() - 650F;
        if(f < 0.0F)
            f = 0.0F;
        ((FlightModelMain) (super.FM)).CT.dvGear = 0.2F - f / 500F;
        if(((FlightModelMain) (super.FM)).CT.dvGear < 0.0F)
            ((FlightModelMain) (super.FM)).CT.dvGear = 0.0F;
    }

    public float flapsMovement(float f, float f1, float f2, float f3, float f4)
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
        if(((FlightModelMain) (super.FM)).CT.getFlap() < ((FlightModelMain) (super.FM)).CT.FlapsControl)
            ((FlightModelMain) (super.FM)).CT.forceFlaps(flapsMovement(f, ((FlightModelMain) (super.FM)).CT.FlapsControl, ((FlightModelMain) (super.FM)).CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 500F, 0.5F, 0.08F)));
        else
            ((FlightModelMain) (super.FM)).CT.forceFlaps(flapsMovement(f, ((FlightModelMain) (super.FM)).CT.FlapsControl, ((FlightModelMain) (super.FM)).CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 500F, 0.5F, 0.7F)));
        if((((FlightModelMain) (super.FM)).AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = false;
            bailout();
        }
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
        {
        	if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.75F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6)
            {
                if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 1.0001F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 5);
                else
                if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.95F && ((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() < 1.0001F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 3);
                else
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 2);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
            }
            setExhaustFlame(Math.round(Aircraft.cvt(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            if(((FlightModelMain) (super.FM)).EI.engines[1].getPowerOutput() > 0.75F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() == 6)
            {
                if(((FlightModelMain) (super.FM)).EI.engines[1].getPowerOutput() > 1.0001F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 5);
                else
                if(((FlightModelMain) (super.FM)).EI.engines[1].getPowerOutput() > 0.95F && ((FlightModelMain) (super.FM)).EI.engines[1].getPowerOutput() < 1.0001F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 3);
                else
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 2);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 0);
            }
            setExhaustFlame1(Math.round(Aircraft.cvt(((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            if(super.FM instanceof RealFlightModel)
                umn();
        }
        if(FLIR)
            laser(spot);       
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        soundbarier();
        gearlimit();
        if(needUpdateHook)
        {
            updateHook();
            needUpdateHook = false;
        }
        super.update(f);
        if(FM.CT.WeaponControl[10] == true && hold == true && t1 + 200L < Time.current())
        {
        	hold = false;
        	HUD.log("Auto Target");
        	t1 = Time.current();
        }	
        if(FM.CT.WeaponControl[10] == true && hold == false && t1 + 200L < Time.current())
        {	
        	hold = true;
        	HUD.log("Laser Uncaged");
        	t1 = Time.current();
        }
        if(((FlightModelMain) (super.FM)).EI.engines[0].getRPM()>200F)
        {
        for(int i = 1; i < 33; i++)
        	hierMesh().chunkSetAngles("Eflap" + i, -8F * ((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput(), 0.0F, 0.0F);
        }
        if(((FlightModelMain) (super.FM)).EI.engines[1].getRPM()>200F)
        {
        for(int j = 33; j > 32 && j < 65; j++)
            hierMesh().chunkSetAngles("Eflap" + j, -8F * ((FlightModelMain) (super.FM)).EI.engines[1].getPowerOutput(), 0.0F, 0.0F);
        }
        if(super.FM.getSpeed() > 7F && World.Rnd().nextFloat() < getAirDensityFactor(super.FM.getAltitude()))
        {
        	if(super.FM.getOverload()>5.7F)
        {
        	pull01 = Eff3DActor.New(this, findHook("_Pull01"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
            pull02 = Eff3DActor.New(this, findHook("_Pull02"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
            pull03 = null;
            pull04 = null;
        }
        	if(super.FM.getOverload()<=5.7F)
        {
        	Eff3DActor.finish(pull01);
        	Eff3DActor.finish(pull02);
        }	
        }
        if(((FlightModelMain) (super.FM)).Gears.onGround())
        {
            hierMesh().chunkSetAngles("SlatLIn_D0", 0.0F, -14.5F, 0.0F);
            hierMesh().chunkSetAngles("SlatRIn_D0", 0.0F, -14.5F, 0.0F);
            hierMesh().chunkSetAngles("SlatLOut_D0", 0.0F, -14.5F, 0.0F);
            hierMesh().chunkSetAngles("SlatROut_D0", 0.0F, -14.5F, 0.0F);
        } else
        if(!((FlightModelMain) (super.FM)).Gears.onGround())
        {
            hierMesh().chunkSetAngles("SlatLIn_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 2.8F, 15F, 0.0F, -18.5F), 0.0F);
            hierMesh().chunkSetAngles("SlatRIn_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 2.8F, 15F, 0.0F, -18.5F), 0.0F);
            hierMesh().chunkSetAngles("SlatLOut_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 2.8F, 15F, 0.0F, -18.5F), 0.0F);
            hierMesh().chunkSetAngles("SlatROut_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 2.8F, 15F, 0.0F, -18.5F), 0.0F);
        }        
        if(FM.CT.getGear() > 0.2F && ((FlightModelMain) (super.FM)).Gears.onGround() && !((FlightModelMain) (super.FM)).AP.way.isLanding())
        {	
        	((FlightModelMain) (super.FM)).CT.BlownFlapsControl = 1.0F;
        	if(trimauto == false)
        	{
        	((FlightModelMain) (super.FM)).CT.setTrimElevatorControl(0.5F);
        	trimauto = true;
        	}
        }
        if(FM.CT.getGear() > 0.2F && !((FlightModelMain) (super.FM)).Gears.onGround() && trimauto == true)
        {
        	((FlightModelMain) (super.FM)).CT.BlownFlapsControl = 0.0F;
        	((FlightModelMain) (super.FM)).CT.setTrimElevatorControl(0.2F);
        	trimauto = false;
        }	
        if(super.FM.getSpeedKMH() > 500 && !((FlightModelMain) (super.FM)).Gears.onGround() && trimauto == false)
        {
        	((FlightModelMain) (super.FM)).CT.setTrimElevatorControl(0F);
        	trimauto = true;
        }
        if(FM.CT.getGear() < 0.8F || super.FM.getSpeedKMH() > 590)
        {
        	((FlightModelMain) (super.FM)).CT.FlapsControl = Aircraft.cvt(super.FM.getAOA(), 2.0F, 10F, 0.0F, 0.26F);
        }
        if(World.getTimeofDay() <= 6.5F || World.getTimeofDay() > 18F)
        {
        	hierMesh().chunkVisible("SSlightstabr", true);
        	hierMesh().chunkVisible("SSlightstabl", true);
        	hierMesh().chunkVisible("SSlightnose", true);
        	hierMesh().chunkVisible("SSlighttail", true);
        }
        if(super.FM.getAOA()>28 || (super.FM.getSpeedKMH() < 469 && ((FlightModelMain) (super.FM)).CT.FlapsControl > 0.16F) || super.FM.getOverload()>=6F)
        	{
        	((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0F;		
        	}
    }
   

    public void updateHook()
    {
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
        case 3: // '\003'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.2F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 3.8F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            break;

        case 2: // '\002'
        	((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 3.5F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            break;

        case 5: // '\005'
        	if(World.getTimeofDay() <= 6F || World.getTimeofDay() > 18F)
        	{
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18A.eff", -1F);
        	} else
        	{
        	((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/AfterBurner.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 2.0F, "3DO/Effects/Aircraft/AfterBurner.eff", -1F);
        	}      		        	
            break;
        }
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Airbrake_D0", 0.0F, -60F * f, 0.0F);
        hierMesh().chunkSetAngles("Airbrake3_D0", 0.0F, 80F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -5.5F);
        hierMesh().chunkSetLocate("Airbrake2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void setExhaustFlame(int i, int j)
    {
        if(j == 0)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            default:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            }
    }
    
    public void setExhaustFlame1(int i, int j)
    {
        if(j == 0)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("Exhaust1b", true);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", true);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("Exhaust1b", true);
                hierMesh().chunkVisible("Exhaust2b", true);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", true);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("Exhaust1b", true);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", true);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", true);
                hierMesh().chunkVisible("Exhaust3b", true);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("Exhaust1b", true);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", true);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", true);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", true);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", true);
                hierMesh().chunkVisible("Exhaust4b", true);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("Exhaust1b", true);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", true);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", true);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", true);
                break;

            default:
                hierMesh().chunkVisible("Exhaust1b", false);
                hierMesh().chunkVisible("Exhaust2b", false);
                hierMesh().chunkVisible("Exhaust3b", false);
                hierMesh().chunkVisible("Exhaust4b", false);
                hierMesh().chunkVisible("Exhaust5b", false);
                break;
            }
    }

    private void bailout()
    {
        if(overrideBailout)
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 0 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep < 2)
            {
                if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.5F)
                {
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 2;
                }
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 2 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 3)
            {
                switch(((FlightModelMain) (super.FM)).AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    doRemoveBlisters();
                    break;
                }
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                AircraftState tmp178_177 = ((FlightModelMain) (super.FM)).AS;
                tmp178_177.astateBailoutStep = (byte)(tmp178_177.astateBailoutStep + 1);
                if(((FlightModelMain) (super.FM)).AS.astateBailoutStep == 4)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 11 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 19)
            {
                int i = ((FlightModelMain) (super.FM)).AS.astateBailoutStep;
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                AircraftState tmp383_382 = ((FlightModelMain) (super.FM)).AS;
                tmp383_382.astateBailoutStep = (byte)(tmp383_382.astateBailoutStep + 1);
                if(i == 11)
                {
                    super.FM.setTakenMortalDamage(true, null);
                    if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(((FlightModelMain) (super.FM)).AS.actor != World.getPlayerAircraft())
                            ((Maneuver)super.FM).set_maneuver(44);
                    }
                }
                if(((FlightModelMain) (super.FM)).AS.astatePilotStates[i - 11] < 99)
                {
                    doRemoveBodyFromPlane(i - 10);
                    if(i == 11)
                    {
                        doEjectCatapult();
                        super.FM.setTakenMortalDamage(true, null);
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                        if(i > 10 && i <= 19)
                            EventLog.onBailedOut(this, i - 11);
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) (super.FM)).Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    protected final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
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

    private final void umn()
    {
        Vector3d vector3d = super.FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        F_18S f_18s = this;
        float f = mn;
        World.cur().getClass();
        f_18s.mn = f / Atmosphere.sonicSpeed((float)((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z);
        if(mn >= lteb)
            ts = true;
        else
            ts = false;
    }

    public boolean ist()
    {
        return ts;
    }

    public float gmnr()
    {
        return mn;
    }

    public boolean inr()
    {
        return ictl;
    }
    
    

    protected boolean bSlatsOff;
    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public float AirBrakeControl;
    public float DragChuteControl;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[];
    private static Loc lLightLoc1 = new Loc();
    private static Point3d lLightP1 = new Point3d();
    private static Point3d lLightP2 = new Point3d();
    private static Point3d lLightPL = new Point3d();
    private boolean ictl;
    private static float mteb = 1.0F;
    private float mn;
    private static float uteb = 1.25F;
    private static float lteb = 0.92F;
    private float actl;
    private float rctl;
    private float ectl;
    private boolean ts;
    private float H1;
    public static boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    public static int LockState = 0;
    static Actor hunted = null;
    private float engineSurgeDamage;
    private float gearTargetAngle;
    private float gearCurrentAngle;
    private static final float NEG_G_TOLERANCE_FACTOR = 3.5F;
    private static final float NEG_G_TIME_FACTOR = 2.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 7F;
    private static final float POS_G_TIME_FACTOR = 3F;
    private static final float POS_G_RECOVERY_FACTOR = 5F;
    public boolean bToFire;
    private float deltaAzimuth;
    private float deltaTangage;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;
    public boolean needUpdateHook;
    private static Loc l = new Loc();
    public boolean FLIR;    
    public boolean hold;
    private Eff3DActor pull01;
    private Eff3DActor pull02;
    private Eff3DActor pull03;
    private Eff3DActor pull04;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public int leftscreen;
    public boolean trimauto;
    private long t1;
    public int Bingofuel;
    public static float mass;
    public int targetnum;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.F_18S.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}