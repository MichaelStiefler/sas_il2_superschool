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

public class F_86D extends Scheme1
    implements TypeSupersonic, TypeBNZFighter, TypeFighterAceMaker, TypeFighter, TypeGSuit
{
    private class _cls0 {

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

        private _cls0(float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
            this.lal = f1;
            this.tal = f2;
            this.bef = f3;
            this.tef = f4;
            this.bhef = f5;
            this.thef = f6;
            this.phef = f7;
            this.mef = f8;
            this.wef = f9;
            this.lef = f10;
            this.ftl = f11;
        }

        public void rs(int ii) {
            if (ii == 0 || ii == 1) {
                access$234(F_86D.this, 0.68);
            }
            if (ii == 31 || ii == 32) {
                access$334(F_86D.this, 0.68);
            }
            if (ii == 15 || ii == 16) {
                access$434(F_86D.this, 0.68);
            }
        }

        private void $1() {
            if (F_86D.this.ts) {
                float f1 = Aircraft.cvt(F_86D.this.FM.getAltitude(), this.lal, this.tal, this.bef, this.tef);
                float f2 = Aircraft.cvt(F_86D.this.mn, F_86D.this.mn < F_86D.mteb ? F_86D.lteb : F_86D.uteb, F_86D.this.mn < F_86D.mteb ? F_86D.uteb : F_86D.lteb, F_86D.this.mn < F_86D.mteb ? this.bhef : this.thef, F_86D.this.mn < F_86D.mteb ? this.thef
                        : this.phef);
                float f3 = Aircraft.cvt(F_86D.this.mn, F_86D.this.mn < F_86D.mteb ? F_86D.lteb : F_86D.uteb, F_86D.this.mn < F_86D.mteb ? F_86D.uteb : F_86D.lteb, F_86D.this.mn < F_86D.mteb ? this.mef : this.wef / f1,
                        F_86D.this.mn < F_86D.mteb ? this.wef / f1 : this.lef / f1);
                ((RealFlightModel) F_86D.this.FM).producedShakeLevel += 0.1125F * f2;
                F_86D.this.FM.SensPitch = F_86D.this.ectl * f3 * f3;
                F_86D.this.FM.SensRoll = F_86D.this.actl * f3;
                F_86D.this.FM.SensYaw = F_86D.this.rctl * f3;
                if (f2 > 0.6F) {
                    F_86D.this.ictl = true;
                } else {
                    F_86D.this.ictl = false;
                }
                if (this.ftl > 0.0F) {
                    if (World.Rnd().nextFloat() > 0.6F) {
                        if (F_86D.this.FM.CT.RudderControl > 0.0F) {
                            F_86D.this.FM.CT.RudderControl -= this.ftl * f2;
                        } else if (F_86D.this.FM.CT.RudderControl < 0.0F) {
                            F_86D.this.FM.CT.RudderControl += this.ftl * f2;
                        } else {
                            Controls controls = F_86D.this.FM.CT;
                            controls.RudderControl = (controls.RudderControl + (World.Rnd().nextFloat() > 0.5F ? this.ftl * f2 : -this.ftl * f2));
                        }
                    }
                    if (F_86D.this.FM.CT.RudderControl > 1.0F) {
                        F_86D.this.FM.CT.RudderControl = 1.0F;
                    }
                    if (F_86D.this.FM.CT.RudderControl < -1.0F) {
                        F_86D.this.FM.CT.RudderControl = -1.0F;
                    }
                }
            } else {
                F_86D.this.FM.SensPitch = F_86D.this.ectl;
                F_86D.this.FM.SensRoll = F_86D.this.actl;
                F_86D.this.FM.SensYaw = F_86D.this.rctl;
            }
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

    public F_86D()
    {
        SonicBoom = 0.0F;
        BayDoor_ = 0.0F;
        bSlatsOff = false;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        AirBrakeControl = 0.0F;
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
    }

    public void getGFactors(TypeGSuit.GFactors theGFactors)
    {
        theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
//    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
//    private static final float NEG_G_TIME_FACTOR = 1.5F;
//    private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
//    private static final float POS_G_TOLERANCE_FACTOR = 2.0F;
//    private static final float POS_G_TIME_FACTOR = 2.0F;
//    private static final float POS_G_RECOVERY_FACTOR = 2.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        actl = this.FM.SensRoll;
        ectl = this.FM.SensPitch;
        rctl = this.FM.SensYaw;
    }

    public void checkHydraulicStatus()
    {
        if(this.FM.EI.engines[0].getStage() < 6 && this.FM.Gears.nOfGearsOnGr > 0)
        {
            gearTargetAngle = 90F;
            hasHydraulicPressure = false;
            this.FM.CT.bHasAileronControl = false;
            this.FM.CT.bHasElevatorControl = false;
            this.FM.CT.AirBrakeControl = 1.0F;
        } else
        if(!hasHydraulicPressure)
        {
            gearTargetAngle = 0.0F;
            hasHydraulicPressure = true;
            this.FM.CT.bHasAileronControl = true;
            this.FM.CT.bHasElevatorControl = true;
            this.FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void moveHydraulics(float f)
    {
        if(gearTargetAngle >= 0.0F)
        {
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
            hierMesh().chunkSetAngles("GearL6_D0", 0.0F, -gearCurrentAngle, 0.0F);
            hierMesh().chunkSetAngles("GearR6_D0", 0.0F, -gearCurrentAngle, 0.0F);
            hierMesh().chunkSetAngles("GearC4_D0", 0.0F, -gearCurrentAngle, 0.0F);
        }
    }

    public void updateLLights()
    {
        this.pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = (new LightPointWorld[] {
                    null, null, null, null
                });
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
            for(int i = 0; i < 4; i++)
            {
                if(this.FM.AS.astateLandingLightEffects[i] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[i].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[i].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    Engine.land();
                    if(Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                    {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        lLight[i].setPos(lLightP2);
                        float f1 = (float)lLightP1.distance(lLightPL);
                        float f2 = f1 * 0.5F + 60F;
                        float f3 = 0.7F - (0.8F * f1 * lightTime) / 2000F;
                        lLight[i].setEmit(f3, f2);
                    } else
                    {
                        lLight[i].setEmit(0.0F, 0.0F);
                    }
                    continue;
                }
                if(lLight[i].getR() != 0.0F)
                    lLight[i].setEmit(0.0F, 0.0F);
            }

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

    public void rareAction(float paramFloat, boolean paramBoolean)
    {
        super.rareAction(paramFloat, paramBoolean);
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vf1 = this.FM.getVflow();
            mn = (float)vf1.lengthSquared();
            mn = (float)Math.sqrt(mn);
            F_86D F_86D = this;
            float f = mn;
            if(World.cur().Atm == null);
            F_86D.mn = f / Atmosphere.sonicSpeed((float)this.FM.Loc.z);
            if(mn >= 0.9F && mn < 1.1D)
                ts = true;
            else
                ts = false;
        }
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
            if(hasHydraulicPressure && this.FM.CT.AirBrakeControl != 0.0F)
                this.FM.CT.AirBrakeControl = 0.0F;
        ft = World.getTimeofDay() % 0.01F;
        if(ft == 0.0F)
            UpdateLightIntensity();
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
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
        } else
        if(k14Mode == 1)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
        } else
        if(k14Mode == 2 && ((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
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
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        if(k14WingspanType == 0)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && ((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        if(k14WingspanType == 0)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && ((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted paramNetMsgGuaranted)
        throws IOException
    {
        paramNetMsgGuaranted.writeByte(k14Mode);
        paramNetMsgGuaranted.writeByte(k14WingspanType);
        paramNetMsgGuaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput paramNetMsgInput)
        throws IOException
    {
        k14Mode = paramNetMsgInput.readByte();
        k14WingspanType = paramNetMsgInput.readByte();
        k14Distance = paramNetMsgInput.readFloat();
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

    public void moveCockpitDoor(float paramFloat)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.95F, 0.0F, 0.9F);
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, 45F * paramFloat);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(paramFloat);
            setDoorSnd(paramFloat);
        }
    }

    public static void moveGear(HierMesh paramHierMesh, float paramFloat)
    {
        if(Math.abs(paramFloat) < 0.27F)
        {
            paramHierMesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(paramFloat, 0.15F, 0.26F, 0.0F, -90F), 0.0F);
            paramHierMesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(paramFloat, 0.09F, 0.22F, 0.0F, -90F), 0.0F);
            paramHierMesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(paramFloat, 0.0F, 0.11F, 0.0F, -90F), 0.0F);
        } else
        {
            paramHierMesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(paramFloat, 0.65F, 0.74F, -90F, 0.0F), 0.0F);
            paramHierMesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(paramFloat, 0.67F, 0.78F, -90F, 0.0F), 0.0F);
            paramHierMesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(paramFloat, 0.89F, 0.99F, -90F, 0.0F), 0.0F);
        }
        paramHierMesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(paramFloat, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        paramHierMesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(paramFloat, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        paramHierMesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(paramFloat, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        paramHierMesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(paramFloat, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        paramHierMesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(paramFloat, 0.69F, 0.74F, 0.0F, -90F), 0.0F);
        paramHierMesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(paramFloat, 0.63F, 0.99F, 0.0F, -105F), 0.0F);
        paramHierMesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(paramFloat, 0.63F, 0.99F, 0.0F, -95F), 0.0F);
        paramHierMesh.chunkSetAngles("Gear5e_D0", 0.0F, Aircraft.cvt(paramFloat, 0.63F, 0.99F, 0.0F, -90F), 0.0F);
    }

    protected void moveGear(float paramFloat)
    {
        moveGear(hierMesh(), paramFloat);
    }

    public void moveWheelSink()
    {
        if(curctl == -1F)
        {
            curctl = oldctl = this.FM.CT.getBrake();
            H1 = 0.17F;
            this.FM.Gears.tailStiffness = 0.4F;
        } else
        {
            curctl = this.FM.CT.getBrake();
        }
        if(!this.FM.brakeShoe && this.FM.Gears.cgear)
        {
            if(curctl - oldctl < -0.02F)
                curctl = oldctl - 0.02F;
            if(curctl < 0.0F)
                curctl = 0.0F;
            float tr = 0.25F * curctl * Math.max(Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.5F, 0.8F, 0.0F, 1.0F), Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 80F, 0.0F, 1.0F));
            this.FM.setGC_Gear_Shift(H1 - tr);
            resetYPRmodifier();
            Aircraft.xyz[0] = -0.4F * tr;
            float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2] - Aircraft.xyz[0], 0.0F, 0.45F, 0.0F, 1.0F);
            hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("GearC8_D0", 0.0F, -37.5F * f, 0.0F);
            hierMesh().chunkSetAngles("GearC9_D0", 0.0F, -75F * f, 0.0F);
        }
        oldctl = curctl;
    }

    protected void moveRudder(float paramFloat)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * paramFloat, 0.0F);
        if(this.FM.CT.GearControl > 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -50F * paramFloat, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -12F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -12F * f, 0.0F);
    }

    protected void moveFlap(float paramFloat)
    {
        float f = -45F * paramFloat;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
    }

    protected void moveSlats(float paramFloat)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.15F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 0.1F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.065F);
        hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.1F);
        hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff()
    {
        if(bSlatsOff)
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

    protected void hitBone(String paramString, Shot paramShot, Point3d paramPoint3d)
    {
        int ii = part(paramString);
        $1.rs(ii);
        if(paramString.startsWith("xx"))
        {
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
            if(paramString.startsWith("xxmgun0"))
            {
                int i = paramString.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, paramShot) > 0.0F)
                {
                    debuggunnery("Armament: mnine Gun (" + i + ") Disabled..");
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
            if(paramString.startsWith("xcockpit"))
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
            if(paramString.startsWith("xtail"))
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
        case 13:
            this.FM.Gears.cgear = false;
            float t = World.Rnd().nextFloat(0.0F, 1.0F);
            if(t < 0.1F)
            {
                this.FM.AS.hitEngine(this, 0, 100);
                if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.49D)
                    this.FM.EI.engines[0].setEngineDies(paramActor);
            } else
            if(t > 0.55D)
                this.FM.EI.engines[0].setEngineDies(paramActor);
            return super.cutFM(paramInt1, paramInt2, paramActor);

        case 19:
            this.FM.EI.engines[0].setEngineDies(paramActor);
            return super.cutFM(paramInt1, paramInt2, paramActor);
        }
        return super.cutFM(paramInt1, paramInt2, paramActor);
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        if(!Config.isUSE_RENDER())
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (this.FM)).actor, 2700F, 9);
        }
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
        if(this.FM.getSpeed() > 5F)
        {
            moveSlats(paramFloat);
            bSlatsOff = false;
        } else
        {
            slatsOff();
        }
        if((this.FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && this.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            bailout();
        }
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            if(this.FM.EI.engines[0].getPowerOutput() > 0.45F && this.FM.EI.engines[0].getStage() == 6)
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 1.001F)
                    this.FM.AS.setSootState(this, 0, 5);
                else
                if(this.FM.EI.engines[0].getPowerOutput() > 0.65F && this.FM.EI.engines[0].getPowerOutput() < 1.001F)
                    this.FM.AS.setSootState(this, 0, 3);
                else
                    this.FM.AS.setSootState(this, 0, 2);
            } else
            {
                this.FM.AS.setSootState(this, 0, 0);
            }
            setExhaustFlame((int)Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F), 0);
            if(this.FM instanceof RealFlightModel)
            {
                umn();
                this.$1.$1();
            }
        }
        engineSurge(paramFloat);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        moveHydraulics(paramFloat);
        soundbarier();
        Controls controls = this.FM.CT;
        float f6 = controls.getBayDoor();
        if(Math.abs(BayDoor_ - f6) > 0.025F)
        {
            BayDoor_ = BayDoor_ + 0.025F * (f6 > BayDoor_ ? 2.0F : -1F);
            if(Math.abs(BayDoor_ - 0.5F) >= 0.48F)
                sfxAirBrake();
        }
        super.update(paramFloat);
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(this.FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(this.FM.AS.astateSootEffects[i][k]);
            this.FM.AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1:
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3:
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            // fall through

        case 2:
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 0.75F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5:
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.5F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            // fall through

        case 4:
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    protected void moveAirBrake(float paramFloat)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -70F * paramFloat, 0.0F);
        hierMesh().chunkSetAngles("BrakeB01_D0", 0.0F, -30F * paramFloat, 0.0F);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -70F * paramFloat, 0.0F);
        hierMesh().chunkSetAngles("BrakeB02_D0", 0.0F, 30F * paramFloat, 0.0F);
        if(paramFloat < 0.2D)
            Aircraft.xyz[2] = Aircraft.cvt(paramFloat, 0.01F, 0.18F, 0.0F, -0.05F);
        else
            Aircraft.xyz[2] = Aircraft.cvt(paramFloat, 0.22F, 0.99F, -0.05F, -0.22F);
        hierMesh().chunkSetLocate("BrakeB01e_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("BrakeB02e_D0", Aircraft.xyz, Aircraft.ypr);
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
                        if(i > 10 && i <= 19)
                            EventLog.onBailedOut(this, i - 11);
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

    private final void umn()
    {
        Vector3d vf1 = this.FM.getVflow();
        mn = (float)vf1.lengthSquared();
        mn = (float)Math.sqrt(mn);
        F_86D F_86D = this;
        float f = mn;
        if(World.cur().Atm == null);
        F_86D.mn = f / Atmosphere.sonicSpeed((float)this.FM.Loc.z);
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

    static float access$234(F_86D x0, double x1) {
        return x0.actl *= x1;
    }

    static float access$334(F_86D x0, double x1) {
        return x0.ectl *= x1;
    }

    static float access$434(F_86D x0, double x1) {
        return x0.rctl *= x1;
    }

    private float BayDoor_;
    protected boolean bSlatsOff;
    private final _cls0 $1 = new _cls0(0.0F, 13000F, 0.65F, 1.0F, 0.01F, 1.0F, 0.2F, 1.0F, 0.5F, 0.6F, 0.0F);
    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public float AirBrakeControl;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[] = {
        null, null, null, null
    };
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
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;

    static
    {
        Class localClass = F_86D.class;
        Property.set(localClass, "originCountry", PaintScheme.countryUSA);
    }












}
