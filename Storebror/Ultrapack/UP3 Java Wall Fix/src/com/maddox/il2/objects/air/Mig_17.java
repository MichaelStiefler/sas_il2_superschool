package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.*;
import java.io.IOException;

public class Mig_17 extends Scheme1
    implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker
{
    private class _cls0
    {

        public void rs(int ii)
        {
            if(ii == 0 || ii == 1)
                actl*= 0.68D;
            if(ii == 31 || ii == 32)
                ectl*= 0.68D;
            if(ii == 15 || ii == 16)
                rctl*= 0.68D;
        }

        private void $1()
        {
            if(ts)
            {
                float f1 = Aircraft.cvt(FM.getAltitude(), lal, tal, bef, tef);
                float f2 = Aircraft.cvt(mn, mn >= Mig_17.mteb ? Mig_17.uteb : Mig_17.lteb, mn >= Mig_17.mteb ? Mig_17.lteb : Mig_17.uteb, mn >= Mig_17.mteb ? thef : bhef, mn >= Mig_17.mteb ? phef : thef);
                float f3 = Aircraft.cvt(mn, mn >= Mig_17.mteb ? Mig_17.uteb : Mig_17.lteb, mn >= Mig_17.mteb ? Mig_17.lteb : Mig_17.uteb, mn >= Mig_17.mteb ? wef / f1 : mef, mn >= Mig_17.mteb ? lef / f1 : wef / f1);
                ((RealFlightModel)FM).producedShakeLevel += 0.1125F * f2;
                FM.SensPitch = ectl * f3 * f3;
                FM.SensRoll = actl * f3;
                FM.SensYaw = rctl * f3;
                if(f2 > 0.6F)
                    ictl = true;
                else
                    ictl = false;
                if(ftl > 0.0F)
                {
                    if(World.Rnd().nextFloat() > 0.6F)
                        if(FM.CT.RudderControl > 0.0F)
                            FM.CT.RudderControl -= ftl * f2;
                        else
                        if(FM.CT.RudderControl < 0.0F)
                        {
                            FM.CT.RudderControl += ftl * f2;
                        } else
                        {
                            Controls controls = FM.CT;
                            controls.RudderControl = controls.RudderControl + (World.Rnd().nextFloat() <= 0.5F ? -ftl * f2 : ftl * f2);
                        }
                    if(FM.CT.RudderControl > 1.0F)
                        FM.CT.RudderControl = 1.0F;
                    if(FM.CT.RudderControl < -1F)
                        FM.CT.RudderControl = -1F;
                }
            } else
            {
                FM.SensPitch = ectl;
                FM.SensRoll = actl;
                FM.SensYaw = rctl;
            }
        }

        private float lal;
        private float tal;
        private float bef;
        private float tef;
        private float bhef;
        private float thef;
        private float phef;
        private float mef;
        private float wef;
        private float lef;
        private float ftl;


        private _cls0(float f1, float f2, float f3, float f4, float f5, float f6,
                float f7, float f8, float f9, float f10, float f11)
        {
            lal = f1;
            tal = f2;
            bef = f3;
            tef = f4;
            bhef = f5;
            thef = f6;
            phef = f7;
            mef = f8;
            wef = f9;
            lef = f10;
            ftl = f11;
        }

    }

    public float getDragForce(float arg0, float arg1, float arg2, float arg3)
    {
        throw new UnsupportedOperationException("getDragForce not supported anymore.");
    }

    public float getDragInGravity(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5)
    {
        throw new UnsupportedOperationException("getDragInGravity supported anymore.");
    }

    public float getForceInGravity(float arg0, float arg1, float arg2)
    {
        throw new UnsupportedOperationException("getForceInGravity supported anymore.");
    }

    public float getDegPerSec(float arg0, float arg1)
    {
        throw new UnsupportedOperationException("getDegPerSec supported anymore.");
    }

    public float getGForce(float arg0, float arg1)
    {
        throw new UnsupportedOperationException("getGForce supported anymore.");
    }

    public Mig_17()
    {
        mn = 0.0F;
        SonicBoom = 0.0F;
        ts = false;
        curst = false;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        ictl = false;
        overrideBailout = false;
        ejectComplete = false;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        engineSurgeDamage = 0.0F;
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 0)
            return;
        if(!Config.isUSE_RENDER())
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (this.FM)).actor, 2000F, 9);
        if(hunted != null)
        {
            k14Distance = (float)((Interpolate) (this.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 800F)
                k14Distance = 800F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
        }
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
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
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        actl = this.FM.SensRoll;
        ectl = this.FM.SensPitch;
        rctl = this.FM.SensYaw;
    }

    public void rareAction(float paramFloat, boolean paramBoolean)
    {
        super.rareAction(paramFloat, paramBoolean);
        if((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && this.FM.CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver))
            if(this.FM.AP.way.isLanding() && this.FM.getSpeed() > this.FM.VmaxFLAPS && this.FM.getSpeed() > this.FM.AP.way.curr().getV() * 1.4F)
            {
                if(this.FM.CT.AirBrakeControl != 1.0F)
                    this.FM.CT.AirBrakeControl = 1.0F;
            } else
            if(((Maneuver)this.FM).get_maneuver() == 25 && this.FM.AP.way.isLanding() && this.FM.getSpeed() < this.FM.VmaxFLAPS * 1.16F)
            {
                if(this.FM.getSpeed() > this.FM.VminFLAPS * 0.5F && (this.FM.Gears.nearGround() || this.FM.Gears.onGround()))
                {
                    if(this.FM.CT.AirBrakeControl != 1.0F)
                        this.FM.CT.AirBrakeControl = 1.0F;
                } else
                if(this.FM.CT.AirBrakeControl != 0.0F)
                    this.FM.CT.AirBrakeControl = 0.0F;
            } else
            if(((Maneuver)this.FM).get_maneuver() == 66)
            {
                if(this.FM.CT.AirBrakeControl != 0.0F)
                    this.FM.CT.AirBrakeControl = 0.0F;
            } else
            if(((Maneuver)this.FM).get_maneuver() == 7)
            {
                if(this.FM.CT.AirBrakeControl != 1.0F)
                    this.FM.CT.AirBrakeControl = 1.0F;
            } else
            if(this.FM.CT.AirBrakeControl != 0.0F)
                this.FM.CT.AirBrakeControl = 0.0F;
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(!Actor.isValid(aircraft))
                {
                    return;
                } else
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(2, loc, vector3d, aircraft);
                    return;
                }
            }

        }
;
        hierMesh().chunkVisible("Seat_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    public void doMurderPilot(int paramInt)
    {
        switch(paramInt)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    protected void nextDMGLevel(String paramString, int paramInt, Actor paramActor)
    {
        super.nextDMGLevel(paramString, paramInt, paramActor);
        if(this.FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String paramString, int paramInt, Actor paramActor)
    {
        super.nextCUTLevel(paramString, paramInt, paramActor);
        if(this.FM.isPlayers())
            bChangedPit = true;
    }

    public void moveCockpitDoor(float paramFloat)
    {
        resetYPRmodifier();
        if(paramFloat < 0.1F)
            Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.08F, 0.0F, 0.1F);
        else
            Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.17F, 0.99F, 0.1F, 0.4F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(paramFloat);
            setDoorSnd(paramFloat);
        }
    }

    public static void moveGear(HierMesh paramHierMesh, float paramFloat)
    {
        float f = Math.max(-paramFloat * 1500F, -90F);
        paramHierMesh.chunkSetAngles("GearC2_D0", 0.0F, -127F * paramFloat, 0.0F);
        paramHierMesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(paramFloat, 0.0F, 0.15F, 0.0F, -90F), 0.0F);
        paramHierMesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(paramFloat, 0.0F, 0.15F, 0.0F, -95F), 0.0F);
        paramHierMesh.chunkSetAngles("GearL2_D0", -90F * paramFloat, -38F * paramFloat, 90F * paramFloat);
        paramHierMesh.chunkSetAngles("GearR2_D0", 90F * paramFloat, -38F * paramFloat, -90F * paramFloat);
        paramHierMesh.chunkSetAngles("GearL4_D0", 0.0F, -115F * paramFloat, 0.0F);
        paramHierMesh.chunkSetAngles("GearR4_D0", 0.0F, -115F * paramFloat, 0.0F);
        paramHierMesh.chunkSetAngles("GearL6_D0", 0.0F, f, 0.0F);
        paramHierMesh.chunkSetAngles("GearR6_D0", 0.0F, f, 0.0F);
    }

    protected void moveGear(float paramFloat)
    {
        moveGear(hierMesh(), paramFloat);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.2F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = -0.2F * f;
        hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearC8_D0", 0.0F, -15F * f, 0.0F);
    }

    protected void moveRudder(float paramFloat)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * paramFloat, 0.0F);
        if(this.FM.CT.GearControl > 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 40F * paramFloat, 0.0F);
    }

    protected void moveFlap(float paramFloat)
    {
        float f = -45F * paramFloat;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
    }

    protected void moveFan(float f)
    {
    }

    protected void moveAirBrake(float paramFloat)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Brake01_D0", 10F * paramFloat, -55F * paramFloat, 0.0F);
        hierMesh().chunkSetAngles("Brake02_D0", 10F * paramFloat, -55F * paramFloat, 0.0F);
    }

    protected void hitBone(String paramString, Shot paramShot, Point3d paramPoint3d)
    {
        int ii = part(paramString);
        $1.rs(ii);
        if(paramString.startsWith("xx"))
        {
            if(paramString.startsWith("xxammo"))
            {
                if(World.Rnd().nextFloat(0.0F, 20000F) < paramShot.power && World.Rnd().nextFloat() < 0.25F)
                    this.FM.AS.setJamBullets(0, World.Rnd().nextInt(0, 2));
                getEnergyPastArmor(11.4F, paramShot);
            } else
            if(paramString.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(paramString.endsWith("p1"))
                {
                    getEnergyPastArmor(13.35D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), paramShot);
                    if(paramShot.power <= 0.0F)
                        doRicochetBack(paramShot);
                } else
                if(paramString.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, paramShot);
                else
                if(paramString.endsWith("P3"))
                    getEnergyPastArmor(8.770001F, paramShot);
                else
                if(paramString.endsWith("g1"))
                {
                    getEnergyPastArmor(World.Rnd().nextFloat(40F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), paramShot);
                    this.FM.AS.setCockpitState(paramShot.initiator, this.FM.AS.astateCockpitState | 2);
                    if(paramShot.power <= 0.0F)
                        doRicochetBack(paramShot);
                }
            } else
            if(paramString.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = paramString.charAt(10) - 48;
                switch(i)
                {
                case 1:
                case 2:
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, paramShot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        this.FM.AS.setControlsDamage(paramShot.initiator, 0);
                    }
                    break;

                case 3:
                case 4:
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), paramShot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(paramShot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), paramShot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(paramShot.initiator, 2);
                    }
                    break;
                }
            } else
            if(paramString.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(paramString.endsWith("bloc"))
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), paramShot);
                if(paramString.endsWith("cams") && getEnergyPastArmor(0.45F, paramShot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    this.FM.EI.engines[0].setCyliderKnockOut(paramShot.initiator, World.Rnd().nextInt(1, (int)(paramShot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < paramShot.power / 24000F)
                    {
                        this.FM.AS.hitEngine(paramShot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(paramShot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        this.FM.AS.hitEngine(paramShot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(paramString.endsWith("eqpt") && World.Rnd().nextFloat() < paramShot.power / 24000F)
                {
                    this.FM.AS.hitEngine(paramShot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if(paramString.endsWith("exht"));
            } else
            if(paramString.startsWith("xxcannon0"))
            {
                int i = paramString.charAt(9) - 49;
                if(getEnergyPastArmor(1.5F, paramShot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon (" + i + ") Disabled..");
                    this.FM.AS.setJamBullets(0, i);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), paramShot);
                }
            } else
            if(paramString.startsWith("xxtank"))
            {
                int i = paramString.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, paramShot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(this.FM.AS.astateTankStates[i] == 0)
                    {
                        debuggunnery("Fuel Tank (" + i + "): Pierced..");
                        this.FM.AS.hitTank(paramShot.initiator, i, 1);
                        this.FM.AS.doSetTankState(paramShot.initiator, i, 1);
                    }
                    if(paramShot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        this.FM.AS.hitTank(paramShot.initiator, i, 2);
                        debuggunnery("Fuel Tank (" + i + "): Hit..");
                    }
                }
            } else
            if(paramString.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(paramString.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", paramShot.initiator);
                }
                if(paramString.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", paramShot.initiator);
                }
                if(paramString.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", paramShot.initiator);
                }
                if(paramString.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", paramShot.initiator);
                }
            } else
            if(paramString.startsWith("xxhyd"))
                this.FM.AS.setInternalDamage(paramShot.initiator, 3);
            else
            if(paramString.startsWith("xxpnm"))
                this.FM.AS.setInternalDamage(paramShot.initiator, 1);
        } else
        {
            if(paramString.startsWith("xblister"))
            {
                this.FM.AS.setCockpitState(paramShot.initiator, this.FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, paramShot);
            }
            if(paramString.startsWith("xcf"))
                hitChunk("CF", paramShot);
            else
            if(paramString.startsWith("xnose"))
                hitChunk("Nose", paramShot);
            else
            if(paramString.startsWith("xTail"))
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", paramShot);
            } else
            if(paramString.startsWith("xkeel"))
            {
                if(chunkDamageVisible("Keel1") < 2)
                    hitChunk("Keel1", paramShot);
            } else
            if(paramString.startsWith("xrudder"))
                hitChunk("Rudder1", paramShot);
            else
            if(paramString.startsWith("xstab"))
            {
                if(paramString.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", paramShot);
                if(paramString.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                    hitChunk("StabR", paramShot);
            } else
            if(paramString.startsWith("xvator"))
            {
                if(paramString.startsWith("xvatorl"))
                    hitChunk("VatorL", paramShot);
                if(paramString.startsWith("xvatorr"))
                    hitChunk("VatorR", paramShot);
            } else
            if(paramString.startsWith("xwing"))
            {
                if(paramString.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                    hitChunk("WingLIn", paramShot);
                if(paramString.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                    hitChunk("WingRIn", paramShot);
                if(paramString.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", paramShot);
                if(paramString.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                    hitChunk("WingRMid", paramShot);
                if(paramString.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                    hitChunk("WingLOut", paramShot);
                if(paramString.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                    hitChunk("WingROut", paramShot);
            } else
            if(paramString.startsWith("xarone"))
            {
                if(paramString.startsWith("xaronel"))
                    hitChunk("AroneL", paramShot);
                if(paramString.startsWith("xaroner"))
                    hitChunk("AroneR", paramShot);
            } else
            if(paramString.startsWith("xgear"))
            {
                if(paramString.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(paramShot.initiator, 0);
                }
                if(paramString.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), paramShot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(paramShot.initiator, 3);
                }
            } else
            if(paramString.startsWith("xpilot") || paramString.startsWith("xhead"))
            {
                int i = 0;
                int j;
                if(paramString.endsWith("a"))
                {
                    i = 1;
                    j = paramString.charAt(6) - 49;
                } else
                if(paramString.endsWith("b"))
                {
                    i = 2;
                    j = paramString.charAt(6) - 49;
                } else
                {
                    j = paramString.charAt(5) - 49;
                }
                hitFlesh(j, paramShot, i);
            }
        }
    }

    protected boolean cutFM(int paramInt1, int paramInt2, Actor paramActor)
    {
        switch(paramInt1)
        {
        default:
            break;

        case 13:
            this.FM.Gears.cgear = false;
            float t = World.Rnd().nextFloat();
            if(t < 0.1F)
            {
                this.FM.AS.hitEngine(this, 0, 100);
                if(World.Rnd().nextFloat() < 0.49D)
                    this.FM.EI.engines[0].setEngineDies(paramActor);
                break;
            }
            if(t > 0.55D)
                this.FM.EI.engines[0].setEngineDies(paramActor);
            break;

        case 34:
            this.FM.Gears.lgear = false;
            break;

        case 37:
            this.FM.Gears.rgear = false;
            break;

        case 19:
            this.FM.CT.bHasAirBrakeControl = false;
            this.FM.EI.engines[0].setEngineDies(paramActor);
            break;

        case 11:
            this.FM.CT.bHasElevatorControl = false;
            this.FM.CT.bHasRudderControl = false;
            this.FM.CT.bHasRudderTrim = false;
            this.FM.CT.bHasElevatorTrim = false;
            break;
        }
        return super.cutFM(paramInt1, paramInt2, paramActor);
    }

    public float getAirPressure(float theAltitude)
    {
        float fBase = 1.0F - (0.0065F * theAltitude) / 288.15F;
        float fExponent = 5.255781F;
        return 101325F * (float)Math.pow(fBase, fExponent);
    }

    public float getAirPressureFactor(float theAltitude)
    {
        return getAirPressure(theAltitude) / 101325F;
    }

    public float getAirDensity(float theAltitude)
    {
        return (getAirPressure(theAltitude) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * theAltitude));
    }

    public float getAirDensityFactor(float theAltitude)
    {
        return getAirDensity(theAltitude) / 1.225F;
    }

    public float getMachForAlt(float theAltValue)
    {
        theAltValue /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length && TypeSupersonic.fMachAltX[i] <= theAltValue; i++);
        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float baseMach = TypeSupersonic.fMachAltY[i - 1];
            float spanMach = TypeSupersonic.fMachAltY[i] - baseMach;
            float baseAlt = TypeSupersonic.fMachAltX[i - 1];
            float spanAlt = TypeSupersonic.fMachAltX[i] - baseAlt;
            float spanMult = (theAltValue - baseAlt) / spanAlt;
            return baseMach + spanMach * spanMult;
        }
    }

    public float calculateMach()
    {
        return this.FM.getSpeedKMH() / getMachForAlt(this.FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(this.FM.getAltitude()) - this.FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f_0_ = this.FM.getSpeedKMH() - getMachForAlt(this.FM.getAltitude());
        if(f_0_ < 0.5F)
            f_0_ = 0.5F;
        if(calculateMach() <= 1.0D)
        {
            this.FM.VmaxAllowed = this.FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if(calculateMach() >= 1.0D)
        {
            this.FM.VmaxAllowed = this.FM.getSpeedKMH() + f_0_;
            isSonic = true;
        }
        if(this.FM.VmaxAllowed > 1500F)
            this.FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            this.playSound("aircraft.SonicBoom", true);
            this.playSound("aircraft.SonicBoomInternal", true);
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(this.FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if(calculateMach() > 1.01D || calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(this.FM.AS.isMaster())
            if(curthrl == -1F)
            {
                curthrl = oldthrl = this.FM.EI.engines[0].getControlThrottle();
            } else
            {
                curthrl = this.FM.EI.engines[0].getControlThrottle();
                if(curthrl < 1.05F)
                {
                    if((curthrl - oldthrl) / f > 20F && this.FM.EI.engines[0].getRPM() < 3200F && this.FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        this.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.01D * (this.FM.EI.engines[0].getRPM() / 1000F);
                        this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.05F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                            this.FM.AS.hitEngine(this, 0, 100);
                        if(World.Rnd().nextFloat() < 0.05F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                            this.FM.EI.engines[0].setEngineDies(this);
                    }
                    if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && this.FM.EI.engines[0].getRPM() < 3200F && this.FM.EI.engines[0].getStage() == 6)
                    {
                        this.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.001D * (this.FM.EI.engines[0].getRPM() / 1000F);
                        this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.4F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                        {
                            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                            this.FM.EI.engines[0].setEngineStops(this);
                        } else
                        if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                    }
                }
                oldthrl = curthrl;
            }
    }

    public void update(float paramFloat)
    {
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            setExhaustFlame(Math.round(Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            if(this.FM instanceof RealFlightModel)
            {
                umnr();
                this.$1.$1();
            }
            if(curctl == -1F)
            {
                curctl = oldctl = this.FM.CT.getAirBrake();
            } else
            {
                curctl = this.FM.CT.getAirBrake();
                if(curctl > 0.05F)
                    if(curctl - oldctl > 0.0F)
                        curst = true;
                    else
                    if(curctl - oldctl == 0.0F && oldctl == 1.0F)
                        curst = true;
                    else
                        curst = false;
            }
            oldctl = curctl;
        }
        if((this.FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && this.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            bailout();
        }
        soundbarier();
        engineSurge(paramFloat);
        super.update(paramFloat);
    }

    public void setExhaustFlame(int stage, int i)
    {
        if(i == 0)
            switch(stage)
            {
            case 0:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 1:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 2:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 3:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                // fall through

            case 4:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 5:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 6:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 7:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 8:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 9:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 10:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 11:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 12:
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

    private void bailout()
    {
        if(overrideBailout)
            if(this.FM.AS.astateBailoutStep >= 0 && this.FM.AS.astateBailoutStep < 2)
            {
                if(this.FM.CT.cockpitDoorControl > 0.5F && this.FM.CT.getCockpitDoor() > 0.5F)
                {
                    this.FM.AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else
            if(this.FM.AS.astateBailoutStep >= 2 && this.FM.AS.astateBailoutStep <= 3)
            {
                switch(this.FM.AS.astateBailoutStep)
                {
                case 2:
                    if(this.FM.CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3:
                    doRemoveBlisters();
                    break;
                }
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp178_177 = this.FM.AS;
                tmp178_177.astateBailoutStep = (byte)(tmp178_177.astateBailoutStep + 1);
                if(this.FM.AS.astateBailoutStep == 4)
                    this.FM.AS.astateBailoutStep = 11;
            } else
            if(this.FM.AS.astateBailoutStep >= 11 && this.FM.AS.astateBailoutStep <= 19)
            {
                int i = this.FM.AS.astateBailoutStep;
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp383_382 = this.FM.AS;
                tmp383_382.astateBailoutStep = (byte)(tmp383_382.astateBailoutStep + 1);
                if(i == 11)
                {
                    this.FM.setTakenMortalDamage(true, null);
                    if((this.FM instanceof Maneuver) && ((Maneuver)this.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(this.FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)this.FM).set_maneuver(44);
                    }
                }
                if(this.FM.AS.astatePilotStates[i - 11] < 99)
                {
                    doRemoveBodyFromPlane(i - 10);
                    if(i == 11)
                    {
                        doEjectCatapult();
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && this.FM.AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            localWreckage.collide(false);
            Vector3d localVector3d = new Vector3d();
            localVector3d.set(this.FM.Vwld);
            localWreckage.setSpeed(localVector3d);
        }
    }

    private final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && this.FM.AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                localWreckage.collide(false);
                Vector3d localVector3d = new Vector3d();
                localVector3d.set(this.FM.Vwld);
                localWreckage.setSpeed(localVector3d);
            }

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

    private final void umnr()
    {
        Vector3d vf1 = this.FM.getVflow();
        mn = (float)vf1.lengthSquared();
        mn = (float)Math.sqrt(mn);
        Mig_17 mig_17 = this;
        float f = mn;
        if(World.cur().Atm == null);
        mig_17.mn = f / Atmosphere.sonicSpeed((float)this.FM.Loc.z);
        if(mn >= lteb)
            ts = true;
        else
            ts = false;
    }

    public void doSetSootState(int paramInt1, int paramInt2)
    {
        for(int i = 0; i < 2; i++)
        {
            if(this.FM.AS.astateSootEffects[paramInt1][i] != null)
                Eff3DActor.finish(this.FM.AS.astateSootEffects[paramInt1][i]);
            this.FM.AS.astateSootEffects[paramInt1][i] = null;
        }

        switch(paramInt2)
        {
        case 3:
            this.FM.AS.astateSootEffects[paramInt1][0] = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 0.75F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5:
            this.FM.AS.astateSootEffects[paramInt1][0] = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 0.75F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            this.FM.AS.astateSootEffects[paramInt1][1] = Eff3DActor.New(this, findHook("_Engine1EF_02"), null, 2.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            break;
        }
    }

    protected boolean curst;
    private boolean ts;
    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
    private boolean overrideBailout;
    private boolean ejectComplete;
    public static boolean bChangedPit = false;
    private float mn;
    private static float uteb = 1.25F;
    private static float lteb = 0.9F;
    private static float mteb = 1.0F;
    private boolean ictl;
    private float actl;
    private float rctl;
    private float ectl;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    private final _cls0 $1 = new _cls0(0.0F, 14000F, 0.65F, 1.0F, 0.05F, 1.0F, 0.4F, 1.0F, 0.46F, 0.55F, 0.65F);
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private static Actor hunted = null;
    private float engineSurgeDamage;

    static
    {
        Class localClass = Mig_17.class;
        Property.set(localClass, "originCountry", PaintScheme.countryRussia);
    }












}
