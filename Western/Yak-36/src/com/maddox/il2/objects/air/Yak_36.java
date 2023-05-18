
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;


public class Yak_36 extends Scheme2
    implements TypeSupersonic, TypeFighter, TypeFighterAceMaker, TypeFastJet, TypeStormovikArmored, TypeBomber
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

    public Yak_36()
    {
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        bSlatsOff = false;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 0;
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
        hasHydraulicPressure = true;
        vectorthrustx = 0.0F;
        vectorthrustz = 0.0F;
        vtolSlipX = 0;
        vtolSlipY = 0;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
        vtolSlipY = 0;
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(nozzlemode == 1)
        {
            vtolSlipY += 10;
            if(vtolSlipY > 100)
                vtolSlipY = 100;
            if(vtolSlipY == 0)
                HUD.log("Side Slip Thrust: Neutral");
            else if(vtolSlipY > 0)
                HUD.log("Side Slip Thrust: Right" + Math.abs(vtolSlipY));
            else
                HUD.log("Side Slip Thrust: Left" + Math.abs(vtolSlipY));
        }
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(nozzlemode == 1)
        {
            vtolSlipY -= 10;
            if(vtolSlipY < -100)
                vtolSlipY = -100;
            if(vtolSlipY == 0)
                HUD.log("Side Slip Thrust: Neutral");
            else if(vtolSlipY > 0)
                HUD.log("Side Slip Thrust: Right" + Math.abs(vtolSlipY));
            else
                HUD.log("Side Slip Thrust: Left" + Math.abs(vtolSlipY));
        }
    }

    public void typeBomberAdjAltitudeReset()
    {
        vtolSlipX = 0;
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(nozzlemode == 1)
        {
            vtolSlipX += 10;
            if(vtolSlipX > 100)
                vtolSlipX = 100;
            if(vtolSlipX == 0)
                HUD.log("Forward Slip Thrust: Neutral");
            else if(vtolSlipX > 0)
                HUD.log("Forward Slip Thrust: " + Math.abs(vtolSlipX));
            else
                HUD.log("Backward Slip Thrust: " + Math.abs(vtolSlipX));
        }
    }

    public void typeBomberAdjAltitudeMinus()
    {
        if(nozzlemode == 1)
        {
            vtolSlipX -= 10;
            if(vtolSlipX < -100)
                vtolSlipX = -100;
            if(vtolSlipX == 0)
                HUD.log("Forward Slip Thrust: Neutral");
            else if(vtolSlipX > 0)
                HUD.log("Forward Slip Thrust: " + Math.abs(vtolSlipX));
            else
                HUD.log("Backward Slip Thrust: " + Math.abs(vtolSlipX));
        }
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f1)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public boolean typeBomberToggleAutomation()
    {
        return true;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        actl = FM.SensRoll;
        ectl = FM.SensPitch;
        rctl = FM.SensYaw;
        FM.CT.DiffBrakesType = 0;
        System.out.println("*** Diff Brakes Set to Type: " + FM.CT.DiffBrakesType);
        FM.CT.bHasCockpitDoorControl = true;
        FM.CT.dvCockpitDoor = 1.0F;
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.EI.engines[1].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            gearTargetAngle = 90F;
            hasHydraulicPressure = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.AirBrakeControl = 1.0F;
        } else
        if(!hasHydraulicPressure)
        {
            gearTargetAngle = 0.0F;
            hasHydraulicPressure = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void moveHydraulics(float f)
    {
        if(gearTargetAngle >= 0.0F)
            if(gearCurrentAngle < gearTargetAngle)
            {
                gearCurrentAngle += 90F * f * 0.8F;
                if(gearCurrentAngle >= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            } else
            {
                gearCurrentAngle -= 90F * f * 0.8F;
                if(gearCurrentAngle <= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            }
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
                if(FM.AS.astateLandingLightEffects[j] != null)
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
        super.rareAction(f, flag);
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = super.FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            Yak_36 Yak_36 = this;
            float f1 = mn;
            World.cur().getClass();
            Yak_36.mn = f1 / Atmosphere.sonicSpeed((float)((Tuple3d) (FM.Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if((FM.Gears.nearGround() || FM.Gears.onGround()) && FM.CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
            if(FM.AP.way.isLanding() && super.FM.getSpeed() > FM.VmaxFLAPS && super.FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
            {
                if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 25 && FM.AP.way.isLanding() && super.FM.getSpeed() < FM.VmaxFLAPS * 1.2F)
            {
                if(super.FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.nearGround())
                {
                    if(FM.Gears.onGround())
                    {
                        if(FM.CT.AirBrakeControl != 1.0F)
                            FM.CT.AirBrakeControl = 1.0F;
                        FM.CT.DragChuteControl = 1.0F;
                    } else
                    if(FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                } else
                if(FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 66)
            {
                if(FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 7)
            {
                if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            } else
            if(hasHydraulicPressure && FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        if(FM.AP.way.curr().Action == 3)
            FM.CT.dropFuelTanks();
        if(FM.AP.way.curr().Action == 3 && !((Maneuver)super.FM).hasBombs())
        {
            FM.AP.way.next();
            ((Maneuver)super.FM).target_ground = null;
            ((Maneuver)super.FM).Group.setGroupTask(1);
        }
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

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
        } else
        if(k14Mode == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
        } else
        if(k14Mode == 2 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Off");
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
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
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
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
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

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Airbrake_D0", 0.0F, 0.0F, -45F * f);
        hierMesh().chunkSetAngles("Airbrake2_D0", 0.0F, 0.0F, 22F * f);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.14F);
        hierMesh().chunkSetLocate("Airbrake21_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float fy = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, 90F, 0.0F) : Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F);
        float fx = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, 6F, 0.0F) : Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 6F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -95F));
        hiermesh.chunkSetAngles("GearC7_D0", fx, -fy, 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", -fx, fy, 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC11_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -90F));
        hiermesh.chunkSetAngles("GearB2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, 85F));
        hiermesh.chunkSetAngles("GearB5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, 85F));
        hiermesh.chunkSetAngles("GearB6_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -40F));
        hiermesh.chunkSetAngles("GearB7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearB8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -100F));
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -100F));
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 100F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -100F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, 0.42F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.6F, 0.8F, 0.0F, 0.02F);
        hierMesh().chunkSetLocate("GearL5_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("GearR5_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = FM.Gears.gWheelSinking[2];
        Aircraft.xyz[2] = -f;
        hierMesh().chunkSetLocate("GearC211_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -20F * f, 0.0F);
        resetYPRmodifier();
        f = FM.Gears.gWheelSinking[0] + FM.Gears.gWheelSinking[1];
        Aircraft.xyz[1] = -f / 2.0F;
        hierMesh().chunkSetLocate("GearB21_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearB3_D0", 0.0F, 20F * f, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder_D0", 30F * f, 0.0F, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl > 0.5F && FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC21_D0", -1.2F * f, 0.0F, 0.0F);
        if(FM.CT.GearControl < 0.5F)
            hierMesh().chunkSetAngles("GearC21_D0", 0.0F, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -20.5F * FM.CT.getElevator());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -20.5F * FM.CT.getElevator());
        } else
        if(f < 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -10F * FM.CT.getElevator());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -10F * FM.CT.getElevator());
        }
    }

    protected void moveAileron(float f)
    {
        if(FM.getSpeedKMH() > 570F)
        {
            float f1 = 1.5F * f;
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f1);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f1);
        } else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f);
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = 38F * f;
        hierMesh().chunkSetAngles("Flap1_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetAngles("Flap2_D0", 0.0F, 0.0F, f1);
    }

    protected void moveFan(float f1)
    {
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
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
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
                    if(World.Rnd().nextFloat() < -10.5F && getEnergyPastArmor(1.1F, shot) > 200F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200F && World.Rnd().nextFloat() < -10.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200F && World.Rnd().nextFloat() < -10.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxengine1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 44000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            } else
            if(s.startsWith("xxengine2"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[1].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[1].getCylindersOperable() + "/" + FM.EI.engines[1].getCylinders() + " Right..");
                    if(World.Rnd().nextFloat() < shot.power / 44000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 1, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.hitEngine(shot.initiator, 1, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                {
                    FM.AS.hitEngine(shot.initiator, 1, 3);
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
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            } else
            if(s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
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
            if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
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
            if(s.startsWith("xgear"))
            {
                if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    FM.AS.setInternalDamage(shot.initiator, 3);
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
label0:
        switch(i)
        {
        default:
            break;

        case 13: // '\r'
            FM.Gears.cgear = false;
            float f = World.Rnd().nextFloat();
            for(int k = 0; k < 2; k++)
            {
                if(f < 0.1F)
                {
                    FM.AS.hitEngine(this, k, 100);
                    if((double)World.Rnd().nextFloat() < 0.48999999999999999D)
                        FM.EI.engines[k].setEngineDies(actor);
                    break label0;
                }
                if((double)f > 0.55000000000000004D)
                    FM.EI.engines[k].setEngineDies(actor);
            }

            break;

        case 34: // '"'
            FM.Gears.lgear = false;
            break;

        case 37: // '%'
            FM.Gears.rgear = false;
            break;

        case 19: // '\023'
            for(int l = 0; l < 2; l++)
            {
                FM.CT.bHasAirBrakeControl = false;
                FM.EI.engines[l].setEngineDies(actor);
            }

            break;

        case 11: // '\013'
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasRudderTrim = false;
            FM.CT.bHasElevatorTrim = false;
            break;
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
            k14Distance = 200F;
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
        for(i = 0; i < TypeSupersonic.fMachAltX.length - 1; i++)
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
        return FM.getSpeedKMH() / getMachForAlt(super.FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(super.FM.getAltitude()) - FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = FM.getSpeedKMH() - getMachForAlt(super.FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if((double)calculateMach() <= 1.0D)
        {
            super.FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if((double)calculateMach() >= 1.0D)
        {
            super.FM.VmaxAllowed = FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(FM.VmaxAllowed > 1500F)
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
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < 2; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = FM.EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = FM.EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                FM.AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                FM.EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            {
                                if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                FM.EI.engines[i].setEngineStops(this);
                            } else
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                    }
                    oldthrl = curthrl;
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
        if(FM.CT.getFlap() < FM.CT.FlapsControl)
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        else
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            bailout();
        }
        float f2 = FM.getSpeedKMH() - 1000F;
        if(f2 < 0.0F)
            f2 = 0.0F;
        FM.CT.dvGear = 0.2F - f2 / 1000F;
        if(FM.CT.dvGear < 0.0F)
            FM.CT.dvGear = 0.0F;
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
        {
            if(FM.AP.way.isLanding() && FM.Gears.onGround() && super.FM.getSpeed() > 40F)
            {
                FM.CT.AirBrakeControl = 1.0F;
                if(FM.CT.bHasDragChuteControl)
                    FM.CT.DragChuteControl = 1.0F;
            }
            if(FM.AP.way.isLanding() && FM.Gears.onGround() && super.FM.getSpeed() < 40F)
            {
                FM.CT.AirBrakeControl = 0.0F;
                if(super.FM.getSpeed() < 20F)
                    FM.CT.DragChuteControl = 0.0F;
            }
        }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            for(int i = 0; i < 2; i++)
                if(FM.EI.engines[i].getPowerOutput() > 0.25F && FM.EI.engines[i].getStage() == 6)
                {
                    if(FM.EI.engines[i].getPowerOutput() > 0.85F)
                    {
                        if(nozzlemode == 1)
                            FM.AS.setSootState(this, i, 5);
                        else
                            FM.AS.setSootState(this, i, 6);
                    } else
                    if(FM.EI.engines[i].getPowerOutput() > 0.55F && FM.EI.engines[i].getPowerOutput() < 0.85F)
                    {
                        if(nozzlemode == 1)
                            FM.AS.setSootState(this, i, 3);
                        else
                            FM.AS.setSootState(this, i, 4);
                    } else
                    {
                        FM.AS.setSootState(this, i, 2);
                    }
                } else
                {
                    FM.AS.setSootState(this, i, 0);
                }

            if(super.FM instanceof RealFlightModel)
                umn();
        }
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        moveHydraulics(f);
        soundbarier();
        super.update(f);
        if((!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_task() == 7 && !FM.AP.way.isLanding())
        {
            Pilot pilot = (Pilot)super.FM;
            if(pilot != null && Actor.isAlive(((Maneuver) (pilot)).target_ground))
            {
                Point3d point3d2 = new Point3d();
                ((Maneuver) (pilot)).target_ground.pos.getAbs(point3d2);
                if(super.pos.getAbsPoint().distance(point3d2) < 1000D)
                {
                    point3d2.sub(FM.Loc);
                    FM.Or.transformInv(point3d2);
                    FM.CT.PowerControl = 0.8F;
                }
            }
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            Point3d point3d = new Point3d();
            super.pos.getAbs(point3d);
            float f1 = (float)((double)super.FM.getAltitude() - World.land().HQ(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y));
            if(f1 < 15F && ((Tuple3d) (vector3d)).z < 0.0D)
                vector3d.z = 0.0D;
            setSpeed(vector3d);
        }
        VTOL();
    }

    private void VTOL()
    {
        if((double)FM.CT.getBrake() > 0.5D && FM.Gears.onGround())
        {
            if(FM.getSpeedKMH() > 15F)
                FM.producedAF.x -= FM.getSpeedKMH() * 2.0F;
            if(FM.getSpeedKMH() < -15F)
                FM.producedAF.x += FM.getSpeedKMH() * 2.0F;
        }
        if(nozzlemode == 1)
        {
            if(super.FM.getAltitude() > 0.0F && (double)FM.getSpeedKMH() >= 80D && FM.EI.engines[0].getStage() > 5)
                FM.producedAF.x -= 3200D;
            if(super.FM.getAltitude() > 0.0F && (double)FM.getSpeedKMH() >= 80D && FM.EI.engines[1].getStage() > 5)
                FM.producedAF.x -= 3200D;
            if(super.FM.getAltitude() > 0.0F && (double)FM.getSpeedKMH() >= 200D && FM.EI.engines[0].getStage() > 5)
                FM.producedAF.x -= 5300D;
            if(super.FM.getAltitude() > 0.0F && (double)FM.getSpeedKMH() >= 200D && FM.EI.engines[1].getStage() > 5)
                FM.producedAF.x -= 5300D;
            if(super.FM.getAltitude() > 0.0F && (double)FM.getSpeedKMH() >= 300D && FM.EI.engines[0].getStage() > 5)
                FM.producedAF.x -= 10000D;
            if(super.FM.getAltitude() > 0.0F && (double)FM.getSpeedKMH() >= 300D && FM.EI.engines[1].getStage() > 5)
                FM.producedAF.x -= 10000D;
            if(super.FM.getAltitude() > 0.0F && FM.EI.engines[1].getStage() > 5)
                FM.producedAF.z += (700D - (double)super.FM.getAltitude()) * 3D * (double)FM.EI.engines[1].getPowerOutput();
            if(super.FM.getAltitude() > 0.0F && FM.EI.engines[0].getStage() > 5)
                FM.producedAF.z += (700D - (double)super.FM.getAltitude()) * 3D * (double)FM.EI.engines[0].getPowerOutput();
            if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
            {
                float avW = (FM.EI.engines[0].getw() + FM.EI.engines[1].getw()) / 2.0F;
                if(avW > 60F)
                {
                    Vector3f eVect = new Vector3f();
                    eVect.x = -(FM.CT.getElevator() + FM.CT.getTrimElevatorControl()) * 0.3F;
                    eVect.y = -(FM.CT.getAileron() + FM.CT.getTrimRudderControl()) * 0.3F;
                    eVect.z = 1.5F;
                    eVect.normalize();
                    FM.EI.engines[0].setVector(eVect);
                    FM.EI.engines[1].setVector(eVect);
                    FM.Or.increment((FM.CT.getRudder() + FM.CT.getTrimRudderControl()) * 0.3F, (FM.CT.getElevator() + FM.CT.getTrimElevatorControl()) * 0.3F, (FM.CT.getAileron() + FM.CT.getTrimAileronControl()) * 0.3F);
                    if(hierMesh().isChunkVisible("WingROut_CAP") || hierMesh().isChunkVisible("WingROut_CAP") || hierMesh().isChunkVisible("WingROut_CAP"))
                        FM.Or.increment(0.0F, 0.0F, -1F);
                    if(hierMesh().isChunkVisible("WingLOut_CAP") || hierMesh().isChunkVisible("WingLOut_CAP") || hierMesh().isChunkVisible("WingLOut_CAP"))
                        FM.Or.increment(0.0F, 0.0F, 1.0F);
                    if(hierMesh().isChunkVisible("Tail1_CAP"))
                        FM.Or.increment(0.0F, 3F, 0.0F);
                    if(hierMesh().isChunkVisible("Nose_CAP"))
                        FM.Or.increment(0.0F, -3F, 0.0F);
                    super.FM.getW().scale(0.89999999999999991D);
                    if(FM.Gears.nOfGearsOnGr < 1)
                    {
                        FM.producedAF.x += (double)vtolSlipX * 200D;
                        FM.producedAF.y -= (double)vtolSlipY * 200D;
                    }
                } else
                {
                    if(FM.Gears.nOfGearsOnGr < 2)
                        FM.producedAF.z -= (100D - (double)avW) * 300D + 15000D;
                    Vector3d vector3d = new Vector3d();
                    getSpeed(vector3d);
                    Point3d point3d = new Point3d();
                    super.pos.getAbs(point3d);
                    float f1 = (float)((double)super.FM.getAltitude() - World.land().HQ(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y));
                    if(f1 < 10F && FM.getSpeedKMH() < 60F && ((Tuple3d) (vector3d)).z < -1D)
                    {
                        vector3d.z *= 0.70000000000000007D;
                        setSpeed(vector3d);
                    }
                }
            }
        }
        if(nozzlemode == 0)
        {
            float t = (tnozzle - Time.current()) / 10000L;
            if(t < 0.0F)
                t = 0.0F;
            Vector3f eVect = new Vector3f();
            eVect.x = 1.0F - vectorthrustx;
            eVect.y = 0.0F;
            eVect.z = vectorthrustz + t * 2.0F;
            eVect.normalize();
            FM.EI.engines[0].setVector(eVect);
            FM.EI.engines[1].setVector(eVect);
            if(nozzleswitch && FM.EI.engines[1].getStage() > 5 && FM.EI.engines[1].getPowerOutput() > 0.7F && !FM.Gears.onGround())
                FM.producedAF.x += (double)vectorthrustx * 850000D;
            if(nozzleswitch && FM.EI.engines[0].getStage() > 5 && FM.EI.engines[0].getPowerOutput() > 0.7F && !FM.Gears.onGround())
                FM.producedAF.x += (double)vectorthrustx * 850000D;
            if(super.FM.getAltitude() > 0.0F && (double)FM.getSpeedKMH() >= 100D && FM.EI.engines[0].getStage() > 5)
                FM.producedAF.x -= (double)FM.getSpeedKMH() * 30D;
            if(super.FM.getAltitude() > 0.0F && (double)FM.getSpeedKMH() >= 100D && FM.EI.engines[1].getStage() > 5)
                FM.producedAF.x -= (double)FM.getSpeedKMH() * 30D;
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Tailhook_D0", 0.0F, 0.0F, 65F * f);
    }

    public void moveVarWing(float f)
    {
        hierMesh().chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -45F * f);
        hierMesh().chunkSetAngles("GearC6_D0", 0.0F, 0.0F, -45F * f);
        hierMesh().chunkSetAngles("GearCA_D0", 0.0F, 0.0F, 25F * f);
        hierMesh().chunkSetAngles("GearCB_D0", 0.0F, 0.0F, 25F * f);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.14F);
        hierMesh().chunkSetLocate("GearCB1_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.14F);
        hierMesh().chunkSetLocate("GearCA1_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("nozzole1", -115F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("nozzole2", -115F * f, 0.0F, 0.0F);
        if(f > 0.7F)
        {
            nozzlemode = 1;
            nozzleswitch = true;
        } else
        {
            nozzlemode = 0;
        }
        if(f < 0.1F)
        {
            nozzleswitch = false;
            vtolSlipX = 0;
            vtolSlipY = 0;
            tnozzle = Time.current() + 4000L;
        }
        float f1 = f * 4F;
        if(f1 > 0.5F)
            f1 = 0.5F;
        vectorthrustz = f1;
        vectorthrustx = 0.5F * f;
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(FM.AS.astateSootEffects[i][k]);
            FM.AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 0.5F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 0.3F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 0.7F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.7F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 6: // '\006'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 0.7F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 0.5F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;
        }
    }

    private void bailout()
    {
        if(overrideBailout)
            if(FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2)
            {
                if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F)
                {
                    FM.AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    FM.AS.astateBailoutStep = 2;
                }
            } else
            if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
            {
                switch(FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(FM.CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    doRemoveBlisters();
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp178_177 = FM.AS;
                tmp178_177.astateBailoutStep = (byte)(tmp178_177.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            } else
            if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                int i = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp383_382 = FM.AS;
                tmp383_382.astateBailoutStep = (byte)(tmp383_382.astateBailoutStep + 1);
                if(i == 11)
                {
                    super.FM.setTakenMortalDamage(true, null);
                    if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)super.FM).set_maneuver(44);
                    }
                }
                if(FM.AS.astatePilotStates[i - 11] < 99)
                {
                    doRemoveBodyFromPlane(i - 10);
                    if(i == 11)
                    {
                        doEjectCatapult();
                        super.FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                        if(i > 10 && i <= 19)
                            EventLog.onBailedOut(this, i - 11);
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && FM.AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    protected final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && FM.AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    private final void umn()
    {
        Vector3d vector3d = super.FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        Yak_36 Yak_36 = this;
        float f = mn;
        World.cur().getClass();
        Yak_36.mn = f / Atmosphere.sonicSpeed((float)((Tuple3d) (FM.Loc)).z);
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

    public int nozzlemode;
    public long tvect;
    private long tnozzle;
    private boolean nozzleswitch;
    private float vectorthrustz;
    private float vectorthrustx;
    private long twait;
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
    public boolean hasHydraulicPressure;
    private int vtolSlipX;
    private int vtolSlipY;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.Yak_36.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}