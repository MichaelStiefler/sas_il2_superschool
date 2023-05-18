//By PAL (benitomuso)
//To be added to AI Mod
//Edited from SAS Engine and AI Mod 2.6RC
//Introduced setRealGunnerMode, isRealGunnerMode methods and public property RealGunnerMode
//This was required to allow shaking and other functions in Gunner cockpits
//Original Code only worked for RealMode (non-AI player pilot cockpit), so Gunnner positions
//had very restricted functions. The RealGunnerMode must be set from CockpitGunner.class

// Source File Name:   RealFlightModel.java

package com.maddox.il2.fm;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Time;
import com.maddox.sound.AudioStream;
import com.maddox.sound.SoundFX;
import java.io.PrintStream;
import java.util.Random;

// Referenced classes of package com.maddox.il2.fm:
//            Autopilot, FlightModelMain, AIFlightModel, Autopilotage, 
//            Gear, FMMath, FlightModel, Controls, 
//            Squares, Mass, EnginesInterface, Motor, 
//            AircraftState, Polares, Atmosphere, Wind, 
//            Supersonic, Arm

public class RealFlightModel extends Pilot
{

    public RealFlightModel(String s)
    {
        super(s);
        RealMode = true;
        //By PAL
        	RealGunnerMode = false;
        indSpeed = 0.0F;
        Cwl = new Vector3d();
        Cwr = new Vector3d();
        Chl = new Vector3d();
        Chr = new Vector3d();
        Cv = new Vector3d();
        Fwl = new Vector3d();
        Fwr = new Vector3d();
        Fhl = new Vector3d();
        Fhr = new Vector3d();
        Fv = new Vector3d();
        superFuel = 10F;
        shakeLevel = 0.0F;
        producedShakeLevel = 0.0F;
        lastAcc = 1.0F;
        ailerInfluence = 1.0F;
        rudderInfluence = 1.0F;
        indiffDnTime = 4F;
        knockDnTime = 0.0F;
        indiffUpTime = 4F;
        knockUpTime = 0.0F;
        saveDeep = 0.0F;
        su26add = 0.0D;
        spinCoeff = 0.0D;
        bSound = true;
        Current_G_Limit = 8F;
        cycleCounter = 0;
        timeCounter = 0.0F;
        gearCutCounter = 0;
        bGearCut = false;
        max_G_Cycle = 1.0F;
        maxSpeed = 0.0F;
        airborneState = 0;
        airborneStartPoint = new Point3d();
        TmpP = new Point3d();
        Vn = new Vector3d();
        TmpV = new Vector3d();
        TmpVd = new Vector3d();
        plAccel = new Vector3d();
        super.AP = new Autopilot(this);
        Realism = World.cur().diffCur;
        maxSpeed = super.VmaxAllowed;
    }

    public Vector3d getW()
    {
        return RealMode ? super.W : super.Wtrue;
    }

    private void flutter()
    {
        if(Realism.Flutter_Effect)
            ((Aircraft)super.actor).msgCollision(super.actor, "CF_D0", "CF_D0");
    }

    private void flutterDamage()
    {
        if(Realism.Flutter_Effect)
        {
            String s;
            switch(World.Rnd().nextInt(0, 29))
            {
            case 0: // '\0'
            case 1: // '\001'
            case 2: // '\002'
            case 3: // '\003'
            case 20: // '\024'
                s = "AroneL";
                break;

            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
            case 7: // '\007'
            case 21: // '\025'
                s = "AroneR";
                break;

            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 22: // '\026'
                s = "VatorL";
                break;

            case 11: // '\013'
            case 12: // '\f'
            case 13: // '\r'
            case 23: // '\027'
                s = "VatorR";
                break;

            case 24: // '\030'
            case 25: // '\031'
            case 26: // '\032'
                s = "Rudder1";
                break;

            case 27: // '\033'
            case 28: // '\034'
            case 29: // '\035'
                s = "Rudder2";
                break;

            case 14: // '\016'
                s = "WingLOut";
                break;

            case 15: // '\017'
                s = "WingROut";
                break;

            case 16: // '\020'
                s = "WingLMid";
                break;

            case 17: // '\021'
                s = "WingRMid";
                break;

            case 18: // '\022'
                s = "WingLIn";
                break;

            case 19: // '\023'
                s = "WingRIn";
                break;

            default:
                s = "CF";
                break;
            }
            s = s + "_D0";
            ((Aircraft)super.actor).msgCollision(super.actor, s, s);
        }
    }

    private void cutWing()
    {
        if(Realism.Flutter_Effect)
        {
            String s;
            switch(World.Rnd().nextInt(0, 8))
            {
            case 0: // '\0'
                s = "Tail1";
                break;

            case 1: // '\001'
            case 2: // '\002'
                s = "WingRMid";
                break;

            case 3: // '\003'
            case 4: // '\004'
                s = "WingLMid";
                break;

            case 5: // '\005'
            case 6: // '\006'
                s = "WingLIn";
                break;

            default:
                s = "WingRIn";
                break;
            }
            s = s + "_D0";
            ((Aircraft)super.actor).msgCollision(super.actor, s, s);
        }
    }

    private void cutPart(int i)
    {
        if(Realism.Flutter_Effect)
        {
            String s;
            switch(i)
            {
            case 0: // '\0'
                s = "WingLOut";
                break;

            case 1: // '\001'
                s = "WingLMid";
                break;

            case 2: // '\002'
                s = "WingLIn";
                break;

            case 3: // '\003'
                s = "WingRIn";
                break;

            case 4: // '\004'
                s = "WingRMid";
                break;

            case 5: // '\005'
                s = "WingROut";
                break;

            case 6: // '\006'
                s = "Tail1";
                break;

            default:
                s = "Tail1";
                break;
            }
            s = s + "_D0";
            ((Aircraft)super.actor).msgCollision(super.actor, s, s);
        }
    }

    private void dangerEM()
    {
        if((long)Time.tickCounter() < lastDangerTick + 1L)
            return;
        lastDangerTick = Time.tickCounter();
        Actor actor = War.GetNearestEnemy(super.actor, -1, 1000F);
        if(!(actor instanceof Aircraft))
            return;
        Aircraft aircraft = (Aircraft)actor;
        TmpVd.set(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Loc);
        TmpVd.sub(super.Loc);
        super.Or.transformInv(TmpVd);
        TmpVd.normalize();
        if(((Tuple3d) (TmpVd)).x < 0.88D)
            return;
        if(!(((SndAircraft) (aircraft)).FM instanceof Pilot))
        {
            return;
        } else
        {
            Pilot pilot = (Pilot)((SndAircraft) (aircraft)).FM;
            pilot.setAsDanger(super.actor);
            return;
        }
    }

    private void dangerEMAces()
    {
        Actor actor = War.GetNearestEnemy(super.actor, -1, World.Rnd().nextFloat(200F, 800F));
        if(!(actor instanceof Aircraft))
            return;
        Aircraft aircraft = (Aircraft)actor;
        TmpVd.set(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Loc);
        TmpVd.sub(super.Loc);
        super.Or.transformInv(TmpVd);
        TmpVd.normalize();
        if(((Tuple3d) (TmpV)).x < 0.84999999999999998D)
            return;
        if(!(((SndAircraft) (aircraft)).FM instanceof Pilot))
        {
            return;
        } else
        {
            Pilot pilot = (Pilot)((SndAircraft) (aircraft)).FM;
            pilot.setAsDanger(super.actor);
            return;
        }
    }

    public boolean isRealMode()
    {
        return RealMode;
    }

    public void setRealMode(boolean flag)
    {
        if(RealMode == flag)
            return;
        RealMode = flag;
        if(RealMode)
            super.AP.setStabAll(false);
    }

    private void checkAirborneState()
    {
        if(World.getPlayerFM() != this)
            return;
        if(!Actor.isAlive(super.actor))
            return;
        Mission.initRadioSounds();
        switch(airborneState)
        {
        default:
            break;

        case 0: // '\0'
            if((double)getAltitude() - Engine.land().HQ_Air(((Tuple3d) (super.Loc)).x, ((Tuple3d) (super.Loc)).y) > 40D)
            {
                airborneState = 2;
                setWasAirborne(true);
                setStationedOnGround(false);
                EventLog.onAirInflight((Aircraft)super.actor);
                if(!Mission.hasRadioStations)
                    CmdEnv.top().exec("music RAND music/inflight");
            } else
            {
                airborneState = 1;
                setStationedOnGround(true);
                if(!Mission.hasRadioStations)
                    CmdEnv.top().exec("music RAND music/takeoff");
            }
            setCrossCountry(false);
            break;

        case 1: // '\001'
            if(super.Vrel.length() > (double)super.Vmin)
                setStationedOnGround(false);
            if((double)getAltitude() - Engine.land().HQ_Air(((Tuple3d) (super.Loc)).x, ((Tuple3d) (super.Loc)).y) <= 40D || super.Vrel.length() <= (double)(super.Vmin * 1.15F))
                break;
            airborneState = 2;
            setStationedOnGround(false);
            setNearAirdrome(false);
            setWasAirborne(true);
            airborneStartPoint.set(super.Loc);
            World.cur().scoreCounter.playerTakeoff();
            EventLog.onAirInflight((Aircraft)super.actor);
            if(!Mission.hasRadioStations)
                CmdEnv.top().exec("music RAND music/inflight");
            break;

        case 2: // '\002'
            if(!isCrossCountry() && super.Loc.distance(airborneStartPoint) > 50000D)
            {
                setCrossCountry(true);
                World.cur().scoreCounter.playerDoCrossCountry();
            }
            if(!super.Gears.onGround || super.Vrel.length() >= 1.0D)
                break;
            airborneState = 1;
            setStationedOnGround(true);
            if(!Mission.hasRadioStations)
                CmdEnv.top().exec("music RAND music/takeoff");
            if(Airport.distToNearestAirport(super.Loc) > 3000D)
            {
                World.cur().scoreCounter.playerLanding(true);
                setNearAirdrome(false);
            } else
            {
                World.cur().scoreCounter.playerLanding(false);
                setNearAirdrome(true);
            }
            break;
        }
    }

    private void initSound(Actor actor)
    {
        structuralFX = ((Aircraft)actor).newSound("models.structuralFX", false);
        setSound(false);
    }

    private void setSound(boolean flag)
    {
        bSound = flag;
    }

    private boolean getSound()
    {
        return bSound;
    }

    public void update(float f)
    {
        if(super.actor.isNetMirror())
        {
            ((com.maddox.il2.objects.air.NetAircraft.Mirror)super.actor.net).fmUpdate(f);
            return;
        }
        if(getSound())
            initSound(super.actor);
        super.V2 = (float)super.Vflow.lengthSquared();
        super.V = (float)Math.sqrt(super.V2);
        if(super.V * f > 5F)
        {
            update(f * 0.5F);
            update(f * 0.5F);
            return;
        }
        if(!(RealMode || RealGunnerMode)) //By PAL, not controlling anything
        {
            shakeLevel = 0.0F;
            super.update(f);
            if(isTick(44, 0))
                checkAirborneState();
            if(World.cur().diffCur.Blackouts_N_Redouts)
                calcOverLoad(f, false);
            super.producedAM.set(0.0D, 0.0D, 0.0D);
            super.producedAF.set(0.0D, 0.0D, 0.0D);
            return;
        }
        moveCarrier();
        decDangerAggressiveness();
        if(((Tuple3d) (super.Loc)).z < -20D)
            ((Aircraft)super.actor).postEndAction(0.0D, super.actor, 4, null);
        if(!isOk() && super.Group != null)
            super.Group.delAircraft((Aircraft)super.actor);
        if(Config.isUSE_RENDER() && Maneuver.showFM && super.actor == Main3D.cur3D().viewActor())
        {
            float f6 = (((float)((Tuple3d) (super.W)).x / (super.CT.getAileron() * 111.111F * super.SensRoll)) * super.Sq.squareWing) / 0.8F;
            if(Math.abs(f6) > 50F)
                f6 = 0.0F;
            float f8 = (((float)((Tuple3d) (super.W)).y / (-super.CT.getElevator() * 111.111F * super.SensPitch)) * super.Sq.squareWing) / 0.27F;
            if(Math.abs(f8) > 50F)
                f8 = 0.0F;
            float f10 = (((float)((Tuple3d) (super.W)).z / ((super.AOS - super.CT.getRudder() * 12F) * 111.111F * super.SensYaw)) * super.Sq.squareWing) / 0.15F;
            if(Math.abs(f10) > 50F)
                f10 = 0.0F;
            TextScr.output(5, 60, "~S RUDDR = " + (float)(int)(f10 * 100F) / 100F);
            TextScr.output(5, 80, "~S VATOR = " + (float)(int)(f8 * 100F) / 100F);
            TextScr.output(5, 100, "~S AERON = " + (float)(int)(f6 * 100F) / 100F);
            String s = "";
            for(int i = 0; (float)i < shakeLevel * 10.5F; i++)
                s = s + ">";

            TextScr.output(5, 120, "SHAKE LVL -" + shakeLevel);
            TextScr.output(5, 670, "Pylon = " + super.M.pylonCoeff);
            TextScr.output(5, 640, "WIND = " + (float)(int)(super.Vwind.length() * 10D) / 10F + " " + (float)(int)(((Tuple3d) (super.Vwind)).z * 10D) / 10F + " m/s");
            TextScr.output(5, 140, "BRAKE = " + super.CT.getBrake());
            int j = 0;
            TextScr.output(225, 140, "---ENGINES (" + super.EI.getNum() + ")---" + super.EI.engines[j].getStage());
            TextScr.output(245, 120, "THTL " + (int)(100F * super.EI.engines[j].getControlThrottle()) + "%" + (super.EI.engines[j].getControlAfterburner() ? " (NITROS)" : ""));
            TextScr.output(245, 100, "PROP " + (int)(100F * super.EI.engines[j].getControlProp()) + "%" + (super.CT.getStepControlAuto() ? " (AUTO)" : ""));
            TextScr.output(245, 80, "MIX " + (int)(100F * super.EI.engines[j].getControlMix()) + "%");
            TextScr.output(245, 60, "RAD " + (int)(100F * super.EI.engines[j].getControlRadiator()) + "%" + (super.CT.getRadiatorControlAuto() ? " (AUTO)" : ""));
            TextScr.output(245, 40, "SUPC " + super.EI.engines[j].getControlCompressor() + "x");
            TextScr.output(245, 20, "PropAoA :" + (int)Math.toDegrees(super.EI.engines[j].getPropAoA()));
            TextScr.output(245, 0, "PropPhi :" + (int)Math.toDegrees(super.EI.engines[j].getPropPhi()));
            TextScr.output(455, 120, "Cyls/Cams " + super.EI.engines[j].getCylindersOperable() + "/" + super.EI.engines[j].getCylinders());
            TextScr.output(455, 100, "Readyness " + (int)(100F * super.EI.engines[j].getReadyness()) + "%");
            TextScr.output(455, 80, "PRM " + (int)((float)(int)(super.EI.engines[j].getRPM() * 0.02F) * 50F) + " rpm");
            TextScr.output(455, 60, "Thrust " + (int)((Tuple3f) (super.EI.engines[j].getEngineForce())).x + " N");
            TextScr.output(455, 40, "Fuel " + (int)((100F * super.M.fuel) / super.M.maxFuel) + "% Nitro " + (int)((100F * super.M.nitro) / super.M.maxNitro) + "%");
            TextScr.output(455, 20, "MPrs " + (int)(1000F * super.EI.engines[j].getManifoldPressure()) + " mBar");
            TextScr.output(640, 140, "---Controls---");
            TextScr.output(640, 120, "A/C: " + (super.CT.bHasAileronControl ? "" : "AIL ") + (super.CT.bHasElevatorControl ? "" : "ELEV ") + (super.CT.bHasRudderControl ? "" : "RUD ") + (super.Gears.bIsHydroOperable ? "" : "GEAR "));
            TextScr.output(640, 100, "ENG: " + (super.EI.engines[j].isHasControlThrottle() ? "" : "THTL ") + (super.EI.engines[j].isHasControlProp() ? "" : "PROP ") + (super.EI.engines[j].isHasControlMix() ? "" : "MIX ") + (super.EI.engines[j].isHasControlCompressor() ? "" : "SUPC ") + (super.EI.engines[j].isPropAngleDeviceOperational() ? "" : "GVRNR "));
            TextScr.output(640, 80, "PIL: (" + (int)(super.AS.getPilotHealth(0) * 100F) + "%)");
            TextScr.output(640, 60, "Sens: " + super.CT.Sensitivity);
            TextScr.output(400, 500, "+");
            TextScr.output(400, 400, "|");
            TextScr.output((int)(400F + 200F * super.CT.AileronControl), (int)(500F - 200F * super.CT.ElevatorControl), "+");
            TextScr.output((int)(400F + 200F * super.CT.RudderControl), 400, "|");
            TextScr.output(5, 200, "AOA = " + super.AOA);
            TextScr.output(5, 220, "Mass = " + super.M.getFullMass());
            TextScr.output(5, 320, "AERON TR = " + super.CT.trimAileron);
            TextScr.output(5, 300, "VATOR TR = " + super.CT.trimElevator);
            TextScr.output(5, 280, "RUDDR TR = " + super.CT.trimRudder);
            TextScr.output(245, 160, " pF = " + super.EI.engines[0].zatizeni * 100D + "%/hr");
            hpOld = hpOld * 0.95F + (0.05F * super.EI.engines[0].w * super.EI.engines[0].engineMoment) / 746F;
            TextScr.output(245, 180, " hp = " + hpOld);
            TextScr.output(245, 200, " eMoment = " + super.EI.engines[0].engineMoment);
            TextScr.output(245, 220, " pMoment = " + super.EI.engines[0].propMoment);
        }
        if(!Realism.Limited_Fuel)
            superFuel = super.M.fuel = Math.max(superFuel, super.M.fuel);
        super.AP.update(f);
        ((Aircraft)super.actor).netUpdateWayPoint();
        super.CT.update(f, (float)((Tuple3d) (super.Vflow)).x, super.EI, true);
        float f7 = (float)(((Tuple3d) (super.Vflow)).x * ((Tuple3d) (super.Vflow)).x) / 11000F;
        if(f7 > 1.0F)
            f7 = 1.0F;
        ForceFeedback.fxSetSpringGain(f7);
        if(super.CT.saveWeaponControl[0] || super.CT.saveWeaponControl[1] || super.CT.saveWeaponControl[2])
            dangerEM();
        super.Wing.setFlaps(super.CT.getFlap());
        FMupdate(f);
        super.EI.update(f);
        super.Gravity = super.M.getFullMass() * Atmosphere.g();
        super.M.computeFullJ(super.J, super.J0);
        if(Realism.G_Limits)
        {
            if(super.G_ClassCoeff < 0.0F || !((Aircraft)super.actor instanceof TypeBomber))
                Current_G_Limit = super.ReferenceForce / super.M.getFullMass() - super.M.pylonCoeff;
            else
                Current_G_Limit = super.ReferenceForce / super.M.getFullMass();
            setLimitLoad(Current_G_Limit);
        }
        if(isTick(44, 0))
        {
            super.AS.update(f * 44F);
            ((Aircraft)super.actor).rareAction(f * 44F, true);
            super.M.computeParasiteMass(super.CT.Weapons);
            super.Sq.computeParasiteDrag(super.CT, super.CT.Weapons);
            checkAirborneState();
            putScareShpere();
            dangerEMAces();
            if(super.turnOffCollisions && !super.Gears.onGround && (double)getAltitude() - Engine.land().HQ_Air(((Tuple3d) (super.Loc)).x, ((Tuple3d) (super.Loc)).y) > 30D)
                super.turnOffCollisions = false;
        }
        super.Or.wrap();
        if(Realism.Wind_N_Turbulence)
            World.wind().getVector(super.Loc, super.Vwind);
        else
            super.Vwind.set(0.0D, 0.0D, 0.0D);
        super.Vair.sub(super.Vwld, super.Vwind);
        super.Or.transformInv(super.Vair, super.Vflow);
        super.Density = Atmosphere.density((float)((Tuple3d) (super.Loc)).z);
        super.AOA = FMMath.RAD2DEG(-(float)Math.atan2(((Tuple3d) (super.Vflow)).z, ((Tuple3d) (super.Vflow)).x));
        super.AOS = FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (super.Vflow)).y, ((Tuple3d) (super.Vflow)).x));
        indSpeed = getSpeed() * (float)Math.sqrt(super.Density / 1.225F);
        super.Mach = super.V / Atmosphere.sonicSpeed((float)((Tuple3d) (super.Loc)).z);
        float fDragFactor = 1.0F;
        float fDragParasiteFactor = 1.0F;
        if(super.Ss.allParamsSet)
        {
            float fMachDrag = super.Ss.getDragFactorForMach(super.Mach);
            fDragFactor = (float)Math.sqrt(fMachDrag);
            fDragParasiteFactor = (float)Math.pow(fMachDrag, 5D);
        } else
        {
            super.Ss.getDragFactorForMach(super.Mach);
        }
        if(super.Mach > 0.8F)
            super.Mach = 0.8F;
        super.Kq = 1.0F / (float)Math.sqrt(1.0F - super.Mach * super.Mach);
        super.q_ = super.Density * super.V2 * 0.5F;
        double d1 = ((Tuple3d) (super.Loc)).z - super.Gears.screenHQ;
        if(d1 < 0.0D)
            d1 = 0.0D;
        float f1 = super.CT.getAileron() * 14F;
        f1 = super.Arms.WING_V * (float)Math.sin(FMMath.DEG2RAD(super.AOS)) + super.SensRoll * ailerInfluence * (1.0F - 0.1F * super.CT.getFlap()) * f1;
        double d2 = 0.0D;
        double d4 = 0.0D;
        if(super.EI.engines[0].getType() < 2)
        {
            d2 = super.EI.engines[0].addVflow;
            if(Realism.Torque_N_Gyro_Effects)
                d4 = 0.5D * super.EI.engines[0].addVside;
        }
        Vn.set(-super.Arms.GCENTER, 0.84999999999999998D * (double)super.Arms.WING_END, -0.5D);
        Vn.cross(super.W, Vn);
        Vn.add(super.Vflow);
        float f12 = f1 - FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).z, ((Tuple3d) (Vn)).x));
        Vn.x += 0.070000000000000007D * d2;
        double d = Vn.lengthSquared();
        d *= 0.5F * super.Density;
        f7 = f1 - FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).z + 0.070000000000000007D * d4 * (double)super.EI.getPropDirSign(), ((Tuple3d) (Vn)).x));
        float f14 = 0.015F * f1;
        if(f14 < 0.0F)
            f14 *= 0.18F;
        Cwl.x = -d * (double)(super.Wing.new_Cx(f7) + f14 + super.GearCX * super.CT.getGear() + super.radiatorCX * (super.EI.getRadiatorPos() + super.CT.getCockpitDoor()) + super.Sq.dragAirbrakeCx * super.CT.getAirBrake() + super.Sq.dragChuteCx * super.CT.getDragChute());
        Cwl.z = d * (double)super.Wing.new_Cy(f7) * (double)super.Kq;
        if(super.fmsfxCurrentType != 0)
        {
            if(super.fmsfxCurrentType == 1)
                Cwl.z *= Aircraft.cvt(super.fmsfxPrevValue, 0.003F, 0.8F, 1.0F, 0.0F);
            if(super.fmsfxCurrentType == 2)
            {
                Cwl.z = 0.0D;
                if(Time.current() >= super.fmsfxTimeDisable)
                    doRequestFMSFX(0, 0);
            }
        }
        Vn.set(-super.Arms.GCENTER, -super.Arms.WING_END, -0.5D);
        Vn.cross(super.W, Vn);
        Vn.add(super.Vflow);
        float f13 = -f1 - FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).z, ((Tuple3d) (Vn)).x));
        Vn.x += 0.070000000000000007D * d2;
        d = Vn.lengthSquared();
        d *= 0.5F * super.Density;
        float f9 = -f1 - FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).z - 0.070000000000000007D * d4 * (double)super.EI.getPropDirSign(), ((Tuple3d) (Vn)).x));
        f14 = -0.015F * f1;
        if(f14 < 0.0F)
            f14 *= 0.18F;
        Cwr.x = -d * (double)(super.Wing.new_Cx(f9) + f14 + super.GearCX * super.CT.getGear() + super.radiatorCX * super.EI.getRadiatorPos() + super.Sq.dragAirbrakeCx * super.CT.getAirBrake() + super.Sq.dragChuteCx * super.CT.getDragChute());
        Cwr.z = d * (double)super.Wing.new_Cy(f9) * (double)super.Kq;
        if(super.fmsfxCurrentType != 0)
        {
            if(super.fmsfxCurrentType == 1)
                Cwr.z *= Aircraft.cvt(super.fmsfxPrevValue, 0.003F, 0.8F, 1.0F, 0.0F);
            if(super.fmsfxCurrentType == 3)
            {
                Cwr.z = 0.0D;
                if(Time.current() >= super.fmsfxTimeDisable)
                    doRequestFMSFX(0, 0);
            }
        }
        Cwl.y = -d * (double)super.Fusel.new_Cy(super.AOS);
        Cwl.x -= d * (double)super.Fusel.new_Cx(super.AOS);
        Cwr.y = -d * (double)super.Fusel.new_Cy(super.AOS);
        Cwr.x -= d * (double)super.Fusel.new_Cx(super.AOS);
        float f15 = super.Wing.get_AOA_CRYT();
        double d7 = 1.0D;
        double d8 = 0.5D + 0.40000000000000002D * (double)super.EI.getPowerOutput();
        double d9 = 1.2D + 0.40000000000000002D * (double)super.EI.getPowerOutput();
        if(spinCoeff < d8)
            spinCoeff = d8;
        if(spinCoeff > d9)
            spinCoeff = d9;
        f7 = f12;
        f9 = f13;
        if(!Realism.Stalls_N_Spins || super.Gears.isUnderDeck())
        {
            if(f7 > f9)
            {
                if(((Tuple3d) (Cwl)).z < ((Tuple3d) (Cwr)).z)
                {
                    double d5 = ((Tuple3d) (Cwl)).z;
                    Cwl.z = ((Tuple3d) (Cwr)).z;
                    Cwr.z = d5;
                }
            } else
            if(((Tuple3d) (Cwl)).z > ((Tuple3d) (Cwr)).z)
            {
                double d6 = ((Tuple3d) (Cwl)).z;
                Cwl.z = ((Tuple3d) (Cwr)).z;
                Cwr.z = d6;
            }
        } else
        if(f7 > f15 || f9 > f15)
        {
            spinCoeff += 0.20000000000000001D * (double)f;
            if((double)super.Sq.squareRudders > 0.0D && (double)Math.abs(super.CT.RudderControl) > 0.5D && (double)super.CT.RudderControl * ((Tuple3d) (super.W)).z > 0.0D)
                spinCoeff -= 0.29999999999999999D * (double)f;
            float f16;
            if(f7 > f9)
                f16 = f7;
            else
                f16 = f9;
            super.turbCoeff = 0.8F * (f16 - f15);
            if(super.turbCoeff < 1.0F)
                super.turbCoeff = 1.0F;
            if(super.turbCoeff > 15F)
                super.turbCoeff = 15F;
            d7 = 1.0D - 0.20000000000000001D * (double)(f16 - f15);
            if(d7 < 0.20000000000000001D)
                d7 = 0.20000000000000001D;
            d7 /= super.turbCoeff;
            double d12 = d * (double)super.turbCoeff * spinCoeff;
            float f17 = getAltitude() - (float)Engine.land().HQ_Air(((Tuple3d) (super.Loc)).x, ((Tuple3d) (super.Loc)).y);
            if(f17 < 10F)
                d12 *= 0.1F * f17;
            if(f7 > f9)
            {
                Cwr.x += 0.019999999552965164D * d12 * (double)super.Sq.spinCxloss;
                Cwl.x -= 0.25D * d12 * (double)super.Sq.spinCxloss;
                Cwr.z += 0.019999999552965164D * d12 * (double)super.Sq.spinCyloss;
                Cwl.z -= 0.10000000149011612D * d12 * (double)super.Sq.spinCyloss;
            } else
            {
                Cwl.x += 0.019999999552965164D * d12 * (double)super.Sq.spinCxloss;
                Cwr.x -= 0.25D * d12 * (double)super.Sq.spinCxloss;
                Cwl.z += 0.019999999552965164D * d12 * (double)super.Sq.spinCyloss;
                Cwr.z -= 0.10000000149011612D * d12 * (double)super.Sq.spinCyloss;
            }
            rudderInfluence = 1.0F + 0.035F * super.turbCoeff;
        } else
        {
            super.turbCoeff = 1.0F;
            d7 = 1.0D;
            spinCoeff -= 0.20000000000000001D * (double)f;
            ailerInfluence = 1.0F;
            rudderInfluence = 1.0F;
        }
        if(isTick(15, 0))
            if(Math.abs(f7 - f9) > 5F)
                ForceFeedback.fxSetSpringZero((f9 - f7) * 0.04F, 0.0F);
            else
                ForceFeedback.fxSetSpringZero(0.0F, 0.0F);
        if(d1 < 0.40000000000000002D * (double)super.Length)
        {
            double d10 = 1.0D - d1 / (0.40000000000000002D * (double)super.Length);
            double d13 = 1.0D + 0.20000000000000001D * d10;
            double d16 = 1.0D + 0.20000000000000001D * d10;
            Cwl.z *= d13;
            Cwl.x *= d16;
            Cwr.z *= d13;
            Cwr.x *= d16;
        }
        f1 = super.CT.getElevator() * (super.CT.getElevator() > 0.0F ? 28F : 20F);
        Vn.set(-super.Arms.VER_STAB, 0.0D, 0.0D);
        Vn.cross(super.W, Vn);
        Vn.add(super.Vflow);
        double d11 = Math.sqrt(((Tuple3d) (Vn)).y * ((Tuple3d) (Vn)).y + ((Tuple3d) (Vn)).z * ((Tuple3d) (Vn)).z);
        d2 = 0.0D;
        d4 = 0.0D;
        if(super.EI.engines[0].getType() < 2)
        {
            double d14 = 1.0D + 0.040000000000000001D * (double)super.Arms.RUDDER;
            d14 = 1.0D / (d14 * d14);
            double d17 = ((Tuple3d) (Vn)).x + d14 * super.EI.engines[0].addVflow;
            if(d17 < 0.20000000000000001D)
                d17 = 0.20000000000000001D;
            double d19 = 1.0D - (1.5D * d11) / d17;
            if(d19 < 0.0D)
                d19 = 0.0D;
            double d3 = d19 * d14 * super.EI.engines[0].addVflow;
            Vn.x += d3;
            double d21 = Math.min(0.0011000000000000001D * ((Tuple3d) (Vn)).x * ((Tuple3d) (Vn)).x, 1.0D);
            if(((Tuple3d) (Vn)).x < 0.0D)
                d21 = 0.0D;
            if(Realism.Torque_N_Gyro_Effects)
                d4 = d19 * d21 * super.EI.engines[0].addVside;
        }
        double d15 = (double)super.Density * Vn.lengthSquared() * 0.5D;
        if(super.EI.getNum() == 1 && super.EI.engines[0].getType() < 2)
        {
            f7 = -FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).z - 0.35999999999999999D * d4 * (double)super.EI.getPropDirSign(), ((Tuple3d) (Vn)).x)) - 2.0F - 0.002F * super.V - super.SensPitch * f1;
            f9 = -FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).z + 0.35999999999999999D * d4 * (double)super.EI.getPropDirSign(), ((Tuple3d) (Vn)).x)) - 2.0F - 0.002F * super.V - super.SensPitch * f1;
        } else
        {
            f7 = f9 = -FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).z, ((Tuple3d) (Vn)).x)) - 2.0F - 0.002F * super.V - super.SensPitch * f1;
        }
        Chl.x = -d15 * (double)super.Tail.new_Cx(f7);
        Chl.z = d15 * (double)super.Tail.new_Cy(f7);
        Chr.x = -d15 * (double)super.Tail.new_Cx(f9);
        Chr.z = d15 * (double)super.Tail.new_Cy(f9);
        Chl.y = Chr.y = 0.0D;
        f1 = super.CT.getRudder() * (super.Sq.squareRudders < 0.05F ? 0.0F : 28F);
        float f11;
        if(super.EI.engines[0].getType() < 2)
            f11 = -FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).y - 0.5D * d4 * (double)super.EI.getPropDirSign(), ((Tuple3d) (Vn)).x)) + super.SensYaw * rudderInfluence * f1;
        else
            f11 = -FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (Vn)).y, ((Tuple3d) (Vn)).x)) + super.SensYaw * rudderInfluence * f1;
        Cv.x = -d15 * (double)super.Tail.new_Cx(f11);
        Cv.y = d15 * (double)super.Tail.new_Cy(f11);
        Cv.z = 0.0D;
        if(!Realism.Stalls_N_Spins)
            Cv.y += ((Tuple3d) (Cv)).y;
        Vn.set(super.Vflow);
        d = (double)super.Density * Vn.lengthSquared() * 0.5D;
        Fwl.scale(super.Sq.liftWingLIn + super.Sq.liftWingLMid + super.Sq.liftWingLOut, Cwl);
        Fwr.scale(super.Sq.liftWingRIn + super.Sq.liftWingRMid + super.Sq.liftWingROut, Cwr);
        Fwl.x -= d * (double)(super.Sq.dragParasiteCx * fDragParasiteFactor + super.Sq.dragProducedCx) * 0.5D;
        Fwr.x -= d * (double)(super.Sq.dragParasiteCx * fDragParasiteFactor + super.Sq.dragProducedCx) * 0.5D;
        Fhl.scale((super.Sq.liftStab + super.Sq.squareElevators) * 0.5F, Chl);
        Fhr.scale((super.Sq.liftStab + super.Sq.squareElevators) * 0.5F, Chr);
        Fv.scale(0.2F + super.Sq.liftKeel * 1.5F + super.Sq.squareRudders, Cv);
        Fwl.x *= fDragFactor;
        Fwr.x *= fDragFactor;
        Fhl.x *= fDragFactor;
        Fhr.x *= fDragFactor;
        Fv.x *= fDragFactor;
        super.AF.set(Fwl);
        super.AF.add(Fwr);
        if(FMMath.isNAN(super.AF))
        {
            super.AF.set(0.0D, 0.0D, 0.0D);
            flutter();
            if(World.cur().isDebugFM())
                System.out.println("AF isNAN");
        } else
        if(super.AF.length() > (double)(super.Gravity * 50F))
        {
            flutter();
            if(World.cur().isDebugFM())
                System.out.println("A > 50.0");
            super.AF.normalize();
            super.AF.scale(super.Gravity * 50F);
        } else
        {
            if(Realism.G_Limits)
            {
                if((getOverload() > getUltimateLoad() + World.Rnd().nextFloat(0.0F, 1.0F) || getOverload() < super.Negative_G_Limit - World.Rnd().nextFloat(0.0F, 0.5F)) && !super.Gears.onGround() && World.Rnd().nextInt(0, 100) > 98)
                    if(super.cutPart < 0)
                        cutWing();
                    else
                        cutPart(super.cutPart);
                if(getOverload() > Current_G_Limit || getOverload() < super.Negative_G_Limit)
                {
                    float f19 = Math.abs(getOverload());
                    if(f19 > max_G_Cycle)
                        max_G_Cycle = f19;
                    timeCounter += f;
                    if(timeCounter > 0.75F)
                    {
                        cycleCounter++;
                        if(cycleCounter > 1)
                        {
                            float f18;
                            if(getOverload() > 1.0F)
                                f18 = (max_G_Cycle - Current_G_Limit) / Current_G_Limit;
                            else
                                f18 = (max_G_Cycle + super.Negative_G_Limit) / super.Negative_G_Limit;
                            f18 *= f18;
                            setSafetyFactor(f18);
                            if(structuralFX != null)
                                structuralFX.play();
                            super.VmaxAllowed = maxSpeed * (getSafetyFactor() * 0.3F + 0.55F);
                            rD = World.Rnd().nextFloat();
                            if(rD < 0.001F)
                            {
                                if(super.CT.bHasGearControl)
                                {
                                    ((Aircraft)super.actor).msgCollision(super.actor, "GearR2_D0", "GearR2_D0");
                                    gearCutCounter++;
                                }
                                super.Wing.CxMin_0 += 6F * rD;
                                setSafetyFactor(250F * rD);
                                super.CT.bHasGearControl = false;
                            } else
                            if(rD < 0.002F)
                            {
                                if(super.CT.bHasGearControl)
                                {
                                    ((Aircraft)super.actor).msgCollision(super.actor, "GearL2_D0", "GearL2_D0");
                                    gearCutCounter += 2;
                                }
                                super.Wing.CxMin_0 += 3F * rD;
                                setSafetyFactor(125F * rD);
                                super.CT.bHasGearControl = false;
                            } else
                            if(rD < 0.0025F)
                            {
                                if(super.CT.bHasGearControl)
                                {
                                    super.CT.GearControl = 1.0F;
                                    ((Aircraft)super.actor).msgCollision(super.actor, "GearL2_D0", "GearL2_D0");
                                    super.CT.forceGear(1.0F);
                                    gearCutCounter += 2;
                                }
                                super.Wing.CxMin_0 += 3F * rD;
                                setSafetyFactor(125F * rD);
                                super.CT.bHasGearControl = false;
                            } else
                            if(rD < 0.003F)
                            {
                                if(super.CT.bHasGearControl)
                                {
                                    super.CT.GearControl = 1.0F;
                                    ((Aircraft)super.actor).msgCollision(super.actor, "GearR2_D0", "GearR2_D0");
                                    super.CT.forceGear(1.0F);
                                    gearCutCounter++;
                                }
                                super.Wing.CxMin_0 += 3F * rD;
                                setSafetyFactor(125F * rD);
                                super.CT.bHasGearControl = false;
                            } else
                            if(rD < 0.0035F)
                            {
                                if(super.CT.bHasGearControl)
                                {
                                    super.CT.dvGear = 1.0F;
                                    super.CT.forceGear(1.0F);
                                    super.CT.GearControl = 1.0F;
                                    ((Aircraft)super.actor).msgCollision(super.actor, "GearR2_D0", "GearR2_D0");
                                    ((Aircraft)super.actor).msgCollision(super.actor, "GearL2_D0", "GearL2_D0");
                                    gearCutCounter += 3;
                                }
                                super.Wing.CxMin_0 += 8F * rD;
                                setSafetyFactor(125F * rD);
                                super.CT.bHasGearControl = false;
                            } else
                            if(rD < 0.04F)
                                super.SensYaw *= 0.68F;
                            else
                            if(rD < 0.05F)
                                super.SensPitch *= 0.68F;
                            else
                            if(rD < 0.06F)
                                super.SensRoll *= 0.68F;
                            else
                            if(rD < 0.061F)
                                super.CT.dropFuelTanks();
                            else
                            if(rD < 0.065F)
                                super.CT.bHasFlapsControl = false;
                            else
                            if(rD >= 0.5F)
                                if(rD < 0.6F)
                                    super.Wing.CxMin_0 += 0.011F * rD;
                                else
                                if((int)super.M.getFullMass() % 2 == 0)
                                {
                                    super.Sq.getClass();
                                    super.Sq.liftWingROut *= 0.95F - 0.2F * rD;
                                    super.Wing.CxMin_0 += 0.011F * rD;
                                } else
                                {
                                    super.Sq.getClass();
                                    super.Sq.liftWingLOut *= 0.95F - 0.2F * rD;
                                    super.Wing.CxMin_0 += 0.011F * rD;
                                }
                        }
                        timeCounter = 0.0F;
                        max_G_Cycle = 1.0F;
                    }
                } else
                {
                    timeCounter = 0.0F;
                    max_G_Cycle = 1.0F;
                }
            } else
            if((super.actor instanceof TypeSupersonic) && getOverload() > 15F && !super.Gears.onGround() && World.Rnd().nextInt(0, 100) > 98)
                cutWing();
            else
            if(getOverload() > 13.5F && !super.Gears.onGround() && World.Rnd().nextInt(0, 100) > 98)
                cutWing();
            if(indSpeed > 112.5F && World.Rnd().nextInt(0, 100) > 98 && super.CT.getGear() > 0.3F && super.CT.GearControl == 1.0F)
            {
                if(super.CT.getGear() >= 0.1F && super.CT.GearControl != 0.0F && !bGearCut)
                    if(!(super.actor instanceof F4U) && !(super.actor instanceof TypeSupersonic))
                    {
                        if(World.Rnd().nextInt(0, 100) > 76 && gearCutCounter != 1)
                        {
                            ((Aircraft)super.actor).msgCollision(super.actor, "GearR2_D0", "GearR2_D0");
                            gearCutCounter++;
                        }
                        if(World.Rnd().nextInt(0, 100) > 76 && gearCutCounter != 2)
                        {
                            ((Aircraft)super.actor).msgCollision(super.actor, "GearL2_D0", "GearL2_D0");
                            gearCutCounter += 2;
                        }
                    } else
                    if(indSpeed > 180F && !(super.actor instanceof TypeSupersonic))
                    {
                        if(World.Rnd().nextInt(0, 100) > 76 && gearCutCounter != 1)
                        {
                            ((Aircraft)super.actor).msgCollision(super.actor, "GearR2_D0", "GearR2_D0");
                            gearCutCounter++;
                        }
                        if(World.Rnd().nextInt(0, 100) > 76 && gearCutCounter != 2)
                        {
                            ((Aircraft)super.actor).msgCollision(super.actor, "GearL2_D0", "GearL2_D0");
                            gearCutCounter += 2;
                        }
                    }
                if((double)indSpeed > (double)super.VmaxFLAPS * 1.5D)
                {
                    if(World.Rnd().nextInt(0, 100) > 76 && gearCutCounter != 1)
                    {
                        ((Aircraft)super.actor).msgCollision(super.actor, "GearR2_D0", "GearR2_D0");
                        gearCutCounter++;
                    }
                    if(World.Rnd().nextInt(0, 100) > 76 && gearCutCounter != 2)
                    {
                        ((Aircraft)super.actor).msgCollision(super.actor, "GearL2_D0", "GearL2_D0");
                        gearCutCounter += 2;
                    }
                }
                if(gearCutCounter > 2)
                {
                    bGearCut = true;
                    super.CT.bHasGearControl = false;
                }
            }
            if(indSpeed > 60.5F && super.CT.getWing() > 0.1F)
            {
                if(World.Rnd().nextInt(0, 100) > 90 && ((Aircraft)super.actor).isChunkAnyDamageVisible("WingLMid"))
                    ((Aircraft)super.actor).msgCollision(super.actor, "WingLMid_D0", "WingLMid_D0");
                if(World.Rnd().nextInt(0, 100) > 90 && ((Aircraft)super.actor).isChunkAnyDamageVisible("WingRMid"))
                    ((Aircraft)super.actor).msgCollision(super.actor, "WingRMid_D0", "WingRMid_D0");
            }
            if(!(super.actor instanceof TypeSupersonic) && indSpeed > 81F && super.CT.bHasFlapsControl && super.CT.FlapsControl > 0.21F && (indSpeed - 81F) * super.CT.getFlap() > 8F)
            {
                if(World.getPlayerAircraft() == super.actor && super.CT.bHasFlapsControl)
                    HUD.log("FailedFlaps");
                super.CT.bHasFlapsControl = false;
                super.CT.FlapsControl = 0.0F;
            }
            if(indSpeed > super.VmaxAllowed && World.Rnd().nextFloat(0.0F, 16F) < indSpeed - super.VmaxAllowed && World.Rnd().nextInt(0, 99) < 2)
                flutterDamage();
            if(!(super.actor instanceof TypeSupersonic))
            {
                if(indSpeed > 610F)
                {
                    if(World.cur().isDebugFM())
                        System.out.println("*** Sonic overspeed....");
                    flutter();
                }
            } else
            if(!(super.actor instanceof TypeSupersonic) && indSpeed > 310F)
            {
                if(World.cur().isDebugFM())
                    System.out.println("*** Sonic overspeed....");
                flutter();
            }
        }
        super.AM.set(0.0D, 0.0D, 0.0D);
        if(Math.abs(super.AOA) < 12F)
        {
            float f2 = super.Or.getKren();
            if(f2 > 30F)
                f2 = 30F;
            else
            if(f2 < -30F)
                f2 = -30F;
            f2 = (float)((double)f2 * (Math.min(((Tuple3d) (super.Vflow)).x - 50D, 50D) * 0.0030000000260770321D));
            super.AM.add(-f2 * 0.01F * super.Gravity, 0.0D, 0.0D);
        }
        if(!getOp(19))
        {
            super.AM.y += (double)(8F * super.Sq.squareWing) * ((Tuple3d) (super.Vflow)).x;
            super.AM.z += 200F * super.Sq.squareWing * super.EI.getPropDirSign();
        }
        double d18 = (double)super.CT.getFlap() * 3D;
        if(d18 > 1.0D)
            d18 = 1.0D;
        double d20 = 0.0111D * (double)Math.abs(super.AOA);
        if(super.Wing.AOACritL < super.AOA && super.AOA < super.Wing.AOACritH)
            d20 = 0.0D;
        else
        if(super.AOA >= super.Wing.AOACritH)
            d20 = Math.min(d20, 0.29999999999999999D * (double)(super.AOA - super.Wing.AOACritH));
        else
        if(super.Wing.AOACritL <= super.AOA)
            d20 = Math.min(d20, 0.29999999999999999D * (double)(super.Wing.AOACritL - super.AOA));
        double d22 = (double)super.Arms.GCENTER + (double)super.Arms.GC_FLAPS_SHIFT * d18 * (1.0D - d20) + (double)super.Arms.GC_AOA_SHIFT * d20;
        TmpV.set(-d22, (double)super.Arms.WING_MIDDLE * (1.3D + 1.0D * Math.sin(FMMath.DEG2RAD(super.AOS))), -super.Arms.GCENTER_Z);
        TmpV.cross(TmpV, Fwl);
        super.AM.add(TmpV);
        TmpV.set(-d22, (double)(-super.Arms.WING_MIDDLE) * (1.3D - 1.0D * Math.sin(FMMath.DEG2RAD(super.AOS))), -super.Arms.GCENTER_Z);
        TmpV.cross(TmpV, Fwr);
        super.AM.add(TmpV);
        super.AM.x += su26add;
        TmpV.set(-super.Arms.HOR_STAB, 1.0D, 0.0D);
        TmpV.cross(TmpV, Fhl);
        super.AM.add(TmpV);
        TmpV.set(-super.Arms.HOR_STAB, -1D, 0.0D);
        TmpV.cross(TmpV, Fhr);
        super.AM.add(TmpV);
        TmpV.set(-super.Arms.VER_STAB, 0.0D, 1.0D);
        TmpV.cross(TmpV, Fv);
        super.AM.add(TmpV);
        double d23 = 1.0D - 1.0000000000000001E-005D * (double)indSpeed;
        if(d23 < 0.80000000000000004D)
            d23 = 0.80000000000000004D;
        super.W.scale(d23);
        if(!Realism.Stalls_N_Spins)
            super.AM.y += ((Tuple3d) (super.AF)).z * 0.5D * Math.sin(FMMath.DEG2RAD(Math.abs(super.AOA)));
        if(super.W.lengthSquared() > 25D)
            super.W.scale(5D / super.W.length());
        if(!Realism.Stalls_N_Spins && ((Tuple3d) (super.Vflow)).x > 20D)
            super.W.z += super.AOS * f;
        super.AF.add(super.producedAF);
        super.AM.add(super.producedAM);
        super.producedAF.set(0.0D, 0.0D, 0.0D);
        super.producedAM.set(0.0D, 0.0D, 0.0D);
        super.AF.add(super.EI.producedF);
        super.AM.add(super.EI.producedM);
        if(World.cur().diffCur.Torque_N_Gyro_Effects)
        {
            super.GM.set(super.EI.getGyro());
            super.GM.scale(d7);
            super.AM.add(super.GM);
        }
        super.GF.set(0.0D, 0.0D, 0.0D);
        super.GM.set(0.0D, 0.0D, 0.0D);
        if(Time.tickCounter() % 2 != 0)
            super.Gears.roughness = super.Gears.plateFriction(this);
        super.Gears.ground(this, true);
        int k = 5;
        if(super.GF.lengthSquared() == 0.0D && super.GM.lengthSquared() == 0.0D)
            k = 1;
        super.SummF.add(super.AF, super.GF);
        super.ACmeter.set(super.SummF);
        super.ACmeter.scale(1.0F / super.Gravity);
        TmpV.set(0.0D, 0.0D, -super.Gravity);
        super.Or.transformInv(TmpV);
        super.GF.add(TmpV);
        super.SummF.add(super.AF, super.GF);
        super.SummM.add(super.AM, super.GM);
        double d24 = 1.0D / (double)super.M.mass;
        super.LocalAccel.scale(d24, super.SummF);
        if(Math.abs(getRollAcceleration()) > 50000.5F)
        {
            ForceFeedback.fxPunch(((Tuple3d) (super.SummM)).x > 0.0D ? 0.9F : -0.9F, 0.0F, 1.0F);
            if(World.cur().isDebugFM())
                System.out.println("Punched (Axial = " + ((Tuple3d) (super.SummM)).x + ")");
        }
        if(Math.abs(getOverload() - lastAcc) > 0.5F)
        {
            ForceFeedback.fxPunch(World.Rnd().nextFloat(-0.5F, 0.5F), -0.9F, getSpeed() * 0.05F);
            if(World.cur().isDebugFM())
                System.out.println("Punched (Lat = " + Math.abs(getOverload() - lastAcc) + ")");
        }
        lastAcc = getOverload();
        if(FMMath.isNAN(super.AM))
        {
            super.AM.set(0.0D, 0.0D, 0.0D);
            flutter();
            if(World.cur().isDebugFM())
                System.out.println("AM isNAN");
        } else
        if(super.AM.length() > (double)(super.Gravity * 150F))
        {
            flutter();
            if(World.cur().isDebugFM())
                System.out.println("SummM > 150g");
            super.AM.normalize();
            super.AM.scale(super.Gravity * 150F);
        }
        super.dryFriction -= 0.01D;
        if(super.Gears.gearsChanged)
            super.dryFriction = 1.0F;
        if(super.Gears.nOfPoiOnGr > 0)
            super.dryFriction += 0.02F;
        if(super.dryFriction < 1.0F)
            super.dryFriction = 1.0F;
        if(super.dryFriction > 32F)
            super.dryFriction = 32F;
        float f20 = 4F * (0.25F - super.EI.getPowerOutput());
        if(f20 < 0.0F)
            f20 = 0.0F;
        f20 *= f20;
        f20 *= super.dryFriction;
        float f21 = f20 * super.M.mass * super.M.mass;
        if(!super.brakeShoe && (super.Gears.nOfPoiOnGr == 0 && super.Gears.nOfGearsOnGr < 3 || f20 == 0.0F || super.SummM.lengthSquared() > (double)(2.0F * f21) || super.SummF.lengthSquared() > (double)(80F * f21) || super.W.lengthSquared() > (double)(0.00014F * f20) || super.Vwld.lengthSquared() > (double)(0.09F * f20)))
        {
            double d25 = 1.0D / (double)k;
            for(int l = 0; l < k; l++)
            {
                super.SummF.add(super.AF, super.GF);
                super.SummM.add(super.AM, super.GM);
                super.AW.x = ((((Tuple3d) (super.J)).y - ((Tuple3d) (super.J)).z) * ((Tuple3d) (super.W)).y * ((Tuple3d) (super.W)).z + ((Tuple3d) (super.SummM)).x) / ((Tuple3d) (super.J)).x;
                super.AW.y = ((((Tuple3d) (super.J)).z - ((Tuple3d) (super.J)).x) * ((Tuple3d) (super.W)).z * ((Tuple3d) (super.W)).x + ((Tuple3d) (super.SummM)).y) / ((Tuple3d) (super.J)).y;
                super.AW.z = ((((Tuple3d) (super.J)).x - ((Tuple3d) (super.J)).y) * ((Tuple3d) (super.W)).x * ((Tuple3d) (super.W)).y + ((Tuple3d) (super.SummM)).z) / ((Tuple3d) (super.J)).z;
                TmpV.scale(d25 * (double)f, super.AW);
                super.W.add(TmpV);
                super.Or.transform(super.W, Vn);
                TmpV.scale(d25 * (double)f, super.W);
                super.Or.increment((float)(-FMMath.RAD2DEG(((Tuple3d) (TmpV)).z)), (float)(-FMMath.RAD2DEG(((Tuple3d) (TmpV)).y)), (float)FMMath.RAD2DEG(((Tuple3d) (TmpV)).x));
                super.Or.transformInv(Vn, super.W);
                TmpV.scale(d24, super.SummF);
                super.Or.transform(TmpV);
                super.Accel.set(TmpV);
                TmpV.scale(d25 * (double)f);
                super.Vwld.add(TmpV);
                TmpV.scale(d25 * (double)f, super.Vwld);
                TmpP.set(TmpV);
                super.Loc.add(TmpP);
                super.GF.set(0.0D, 0.0D, 0.0D);
                super.GM.set(0.0D, 0.0D, 0.0D);
                if(l < k - 1)
                {
                    super.Gears.ground(this, true);
                    TmpV.set(0.0D, 0.0D, -super.Gravity);
                    super.Or.transformInv(TmpV);
                    super.GF.add(TmpV);
                }
            }

            for(int i1 = 0; i1 < 3; i1++)
            {
                super.Gears.gWheelAngles[i1] = (super.Gears.gWheelAngles[i1] + (float)Math.toDegrees(Math.atan((super.Gears.gVelocity[i1] * (double)f) / 0.375D))) % 360F;
                super.Gears.gVelocity[i1] *= 0.94999998807907104D;
            }

            super.HM.chunkSetAngles("GearL1_D0", 0.0F, -super.Gears.gWheelAngles[0], 0.0F);
            super.HM.chunkSetAngles("GearR1_D0", 0.0F, -super.Gears.gWheelAngles[1], 0.0F);
            super.HM.chunkSetAngles("GearC1_D0", 0.0F, -super.Gears.gWheelAngles[2], 0.0F);
        }
        if(super.Leader != null && isTick(128, 97) && Actor.isAlive(((Interpolate) (super.Leader)).actor) && !super.Gears.onGround)
        {
            float f22 = (float)super.Loc.distance(((FlightModelMain) (super.Leader)).Loc);
            if(f22 > 3000F)
                Voice.speakDeviateBig((Aircraft)((Interpolate) (super.Leader)).actor);
            else
            if(f22 > 1700F)
                Voice.speakDeviateSmall((Aircraft)((Interpolate) (super.Leader)).actor);
        }
        shakeLevel = 0.0F;
        if(super.Gears.onGround())
        {
            shakeLevel += (30D * super.Gears.roughness * super.Vrel.length()) / (double)super.M.mass;
        } else
        {
            if(indSpeed > 10F)
            {
                float f23 = (float)Math.sin(Math.toRadians(Math.abs(super.AOA)));
                if(f23 > 0.02F)
                {
                    f23 *= f23;
                    shakeLevel += 0.07F * (f23 - 0.0004F) * (indSpeed - 10F);
                    if(isTick(30, 0) && shakeLevel > 0.6F)
                        HUD.log(stallStringID, "Stall");
                }
            }
            if(indSpeed > super.VmaxFLAPS)
            {
                if(super.CT.bHasGearControl && (super.Gears.lgear || super.Gears.rgear) && super.CT.getGear() > 0.0F)
                    shakeLevel += 0.001F * super.CT.getGear() * (indSpeed - super.VmaxFLAPS);
                if(super.CT.getFlap() > 0.0F)
                    shakeLevel += 0.001F * super.CT.getFlap() * (indSpeed - super.VmaxFLAPS);
            }
        }
        if(indSpeed > super.VmaxAllowed * 0.8F)
            shakeLevel = 0.01F * (indSpeed - super.VmaxAllowed * 0.8F);
        if(World.cur().diffCur.Head_Shake)
        {
            shakeLevel += producedShakeLevel;
            producedShakeLevel *= 0.9F;
        }
        if(shakeLevel > 1.0F)
            shakeLevel = 1.0F;
        ForceFeedback.fxShake(shakeLevel);
        if(World.cur().diffCur.Blackouts_N_Redouts)
            calcOverLoad(f, true);
    }

    private void calcOverLoad(float f, boolean flag)
    {
        if(f > 1.0F)
            f = 1.0F;
        if(super.Gears.onGround() || !flag)
        {
            plAccel.set(0.0D, 0.0D, 0.0D);
        } else
        {
            plAccel.set(getAccel());
            plAccel.scale(0.10199999809265137D);
        }
        plAccel.z += 0.5D;
        super.Or.transformInv(plAccel);
        float f1 = -0.5F + (float)((Tuple3d) (plAccel)).z;
        deep = 0.0F;
        if(f1 < -0.6F)
            deep = f1 + 0.6F;
        if(f1 > 2.2F)
            deep = f1 - 2.2F;
        if(knockDnTime > 0.0F)
            knockDnTime -= f;
        if(knockUpTime > 0.0F)
            knockUpTime -= f;
        if(indiffDnTime < 4F)
            indiffDnTime += f;
        if(indiffUpTime < 4F)
            indiffUpTime += 0.3F * f;
        if(deep > 0.0F)
        {
            if(indiffDnTime > 0.0F)
                indiffDnTime -= 0.8F * deep * f;
            if(deep > 2.3F && knockDnTime < 18.4F)
                knockDnTime += 0.75F * deep * f;
            if(indiffDnTime > 0.1F)
            {
                currDeep = 0.0F;
            } else
            {
                currDeep = deep * 0.08F * 3.5F;
                if(currDeep > 0.8F)
                    currDeep = 0.8F;
            }
        } else
        if(deep < 0.0F)
        {
            deep = -deep;
            if(deep < 0.84F)
                deep = 0.84F;
            if(indiffUpTime > 0.0F)
                indiffUpTime -= 1.2F * deep * f;
            if(deep > 2.3F && knockUpTime < 16.1F)
                knockUpTime += deep * f;
            if(indiffUpTime > 0.1F)
                currDeep = 0.0F;
            else
                currDeep = deep * 0.42F * 0.88F;
            currDeep = -currDeep;
        } else
        {
            currDeep = 0.0F;
        }
        if(knockUpTime > 10.81F)
            currDeep = -0.88F;
        if(knockDnTime > 14.03F)
            currDeep = 3.5F;
        if(currDeep > 3.5F)
            currDeep = 3.5F;
        if(currDeep < -0.88F)
            currDeep = -0.88F;
        if(saveDeep > 0.8F)
        {
            super.CT.Sensitivity = 1.0F - (saveDeep - 0.8F);
            if(super.CT.Sensitivity < 0.0F)
                super.CT.Sensitivity = 0.0F;
        } else
        if(saveDeep < -0.4F)
        {
            super.CT.Sensitivity = 1.0F + (saveDeep + 0.4F);
            if(super.CT.Sensitivity < 0.0F)
                super.CT.Sensitivity = 0.0F;
        } else
        {
            super.CT.Sensitivity = 1.0F;
        }
        super.CT.Sensitivity *= super.AS.getPilotHealth(0);
        if(saveDeep < currDeep)
        {
            saveDeep += 0.3F * f;
            if(saveDeep > currDeep)
                saveDeep = currDeep;
        } else
        {
            saveDeep -= 0.2F * f;
            if(saveDeep < currDeep)
                saveDeep = currDeep;
        }
    }

    public void gunMomentum(Vector3d vector3d, boolean flag)
    {
        super.producedAM.x += ((Tuple3d) (vector3d)).x;
        super.producedAM.y += ((Tuple3d) (vector3d)).y;
        super.producedAM.z += ((Tuple3d) (vector3d)).z;
        float f = (float)vector3d.length() * 3.5E-005F;
        if(flag && f > 0.5F)
            f *= 0.05F;
        if(producedShakeLevel < f)
            producedShakeLevel = f;
    }

    public boolean RealMode;
    public float indSpeed;
    private static int stallStringID = HUD.makeIdLog();
    public DifficultySettings Realism;
    Vector3d Cwl;
    Vector3d Cwr;
    Vector3d Chl;
    Vector3d Chr;
    Vector3d Cv;
    Vector3d Fwl;
    Vector3d Fwr;
    Vector3d Fhl;
    Vector3d Fhr;
    Vector3d Fv;
    private float superFuel;
    private long lastDangerTick;
    public float shakeLevel;
    public float producedShakeLevel;
    private float lastAcc;
    private float ailerInfluence;
    private float rudderInfluence;
    private float deep;
    private float currDeep;
    private float indiffDnTime;
    private float knockDnTime;
    private float indiffUpTime;
    private float knockUpTime;
    public float saveDeep;
    private double su26add;
    private double spinCoeff;
    private SoundFX structuralFX;
    private boolean bSound;
    private float rD;
    public float Current_G_Limit;
    private int cycleCounter;
    private float timeCounter;
    private int gearCutCounter;
    private boolean bGearCut;
    private float max_G_Cycle;
    private float maxSpeed;
    private float hpOld;
    private int airborneState;
    private Point3d airborneStartPoint;
    private Point3d TmpP;
    private Vector3d Vn;
    private Vector3d TmpV;
    private Vector3d TmpVd;
    private Vector3d plAccel;

    public void setRealGunnerMode(boolean flag) //By PAL, needed because of ShakeLevel for Gunners
    {
        if(RealGunnerMode == flag)
            return;
        RealGunnerMode = flag;
    }

    public boolean isRealGunnerMode()
    {
        return RealGunnerMode;
    }
    
    public boolean RealGunnerMode; //By PAL

}
