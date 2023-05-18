package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;

public class TransonicEffects {

    private float            lal = 0F;
    private float            tal = 9000F;
    private float            bef = 0.8F;
    private float            tef = 1.0F;
    private float            bhef = 0.01F;
    private float            thef = 1.0F;
    private float            phef = 0.2F;
    private float            mef = 1.0F;
    private float            wef = 0.45F;
    private float            lef = 0.58F;
    private float            ftl = 0F;
    private float            aileronControlSensitivity;
    private float            rudderControlSensitivity;
    private float            elevatorControlSensitivity;
    private float            mn;
    private float            lowerTranssonicEffectBoundary = 0.9F;
    private float            maxTranssonicEffectBoundary   = 1.0F;
    private float            upperTranssonicEffectBoundary = 1.25F;
    private float            lowerTranssonicVaporBoundary = 0.975F;
    private float            upperTranssonicVaporBoundary = 1.065F;
    private Aircraft         aircraft;
    private boolean                 sonicBoom;
    private Eff3DActor              shockwave;
    private boolean                 sonicVapor;
    private Hook             vaporHook;

    private static final int PART_ARONEL                   = 0;
    private static final int PART_ARONER                   = 0;
    private static final int PART_VATORL                   = 31;
    private static final int PART_VATORR                   = 32;
    private static final int PART_RUDDER1                  = 15;
    private static final int PART_RUDDER2                  = 16;

    public TransonicEffects(Aircraft aircraft, float lal, float tal, float bef, float tef, float bhef, float thef, float phef, float mef, float wef, float lef, float ftl, float lteb, float mteb, float uteb) {
        this.lal = lal;
        this.tal = tal;
        this.bef = bef;
        this.tef = tef;
        this.bhef = bhef;
        this.thef = thef;
        this.phef = phef;
        this.mef = mef;
        this.wef = wef;
        this.lef = lef;
        this.ftl = ftl;
        this.aircraft = aircraft;
        this.sonicBoom = false;
        this.sonicVapor = false;
        this.lowerTranssonicEffectBoundary = lteb;
        this.maxTranssonicEffectBoundary = mteb;
        this.upperTranssonicEffectBoundary = uteb;
        this.lowerTranssonicVaporBoundary = 0.75F + this.lowerTranssonicEffectBoundary * 0.25F;
        this.upperTranssonicVaporBoundary = 0.75F + this.upperTranssonicVaporBoundary * 0.25F;
        this.vaporHook = null;
        try {
            this.vaporHook = aircraft.findHook("_Shockwave");
        } catch (ActorException ae1) {
            try {
                this.vaporHook = aircraft.findHook("_ExternalBail01");
            } catch (ActorException ae2) {
                System.out.println("### No suitable effect hook found for TranssonicEffects implementation of " + aircraft.getClass().getName() + " ###");
            }
        }
    }

    public void onAircraftLoaded() {
        this.aileronControlSensitivity = aircraft.FM.SensRoll;
        this.elevatorControlSensitivity = aircraft.FM.SensPitch;
        this.rudderControlSensitivity = aircraft.FM.SensYaw;
    }

    public void reduceSensitivity(int part) {
        if (part == PART_ARONEL || part == PART_ARONER)
            this.aileronControlSensitivity *= 0.68D;
        if (part == PART_VATORL || part == PART_VATORR)
            this.elevatorControlSensitivity *= 0.68D;
        if (part == PART_RUDDER1 || part == PART_RUDDER2)
            this.rudderControlSensitivity *= 0.68D;
    }

    public void update() {
        this.mn = (float) Math.sqrt(aircraft.FM.getVflow().lengthSquared()) / Atmosphere.sonicSpeed((float) aircraft.FM.Loc.z);
        if (this.mn >= lowerTranssonicEffectBoundary) {
            float f1 = Aircraft.cvt(aircraft.FM.getAltitude(), this.lal, this.tal, this.bef, this.tef);
            float f2 = Aircraft.cvt(this.mn, this.mn < this.maxTranssonicEffectBoundary ? this.lowerTranssonicEffectBoundary : this.upperTranssonicEffectBoundary, this.mn < this.maxTranssonicEffectBoundary ? this.upperTranssonicEffectBoundary : this.lowerTranssonicEffectBoundary, this.mn < this.maxTranssonicEffectBoundary ? this.bhef : this.thef, this.mn < this.maxTranssonicEffectBoundary ? this.thef : this.phef);
            float f3 = Aircraft.cvt(this.mn, this.mn < this.maxTranssonicEffectBoundary ? this.lowerTranssonicEffectBoundary : this.upperTranssonicEffectBoundary, this.mn < this.maxTranssonicEffectBoundary ? this.upperTranssonicEffectBoundary : this.lowerTranssonicEffectBoundary, this.mn < this.maxTranssonicEffectBoundary ? this.mef : this.wef / f1, this.mn < this.maxTranssonicEffectBoundary ? this.wef / f1 : this.lef / f1);
            ((RealFlightModel) aircraft.FM).producedShakeLevel += 0.1125F * f2;
            aircraft.FM.SensPitch = this.elevatorControlSensitivity * f3 * f3;
            aircraft.FM.SensRoll = this.aileronControlSensitivity * f3;
            aircraft.FM.SensYaw = this.rudderControlSensitivity * f3;
//                if (f2 > 0.6F) this.ictl = true;
//                else this.ictl = false;
            if (this.ftl > 0.0F) {
                if (World.Rnd().nextFloat() > 0.6F)
                    if (aircraft.FM.CT.RudderControl > 0.0F)
                        aircraft.FM.CT.RudderControl -= this.ftl * f2;
                    else if (aircraft.FM.CT.RudderControl < 0.0F)
                        aircraft.FM.CT.RudderControl += this.ftl * f2;
                    else {
                        Controls controls = aircraft.FM.CT;
                        controls.RudderControl = controls.RudderControl + (World.Rnd().nextFloat() > 0.5F ? this.ftl * f2 : -this.ftl * f2);
                    }
                if (aircraft.FM.CT.RudderControl > 1.0F)
                    aircraft.FM.CT.RudderControl = 1.0F;
                if (aircraft.FM.CT.RudderControl < -1.0F)
                    aircraft.FM.CT.RudderControl = -1.0F;
            }
        } else {
            aircraft.FM.SensPitch = this.elevatorControlSensitivity;
            aircraft.FM.SensRoll = this.aileronControlSensitivity;
            aircraft.FM.SensYaw = this.rudderControlSensitivity;
        }
    }

    public float getAirPressure(float theAltitude) {
        float fBase = 1.0F - 0.0065F * theAltitude / 288.15F;
        float fExponent = 5.255781F;
        return 101325F * (float) Math.pow(fBase, fExponent);
    }

    public float getAirPressureFactor(float theAltitude) {
        return this.getAirPressure(theAltitude) / 101325F;
    }

    public float getAirDensity(float theAltitude) {
        return this.getAirPressure(theAltitude) * 0.0289644F / (8.31447F * (288.15F - 0.0065F * theAltitude));
    }

    public float getAirDensityFactor(float theAltitude) {
        return this.getAirDensity(theAltitude) / 1.225F;
    }

    public float getMachForAlt(float theAltValue) {
        theAltValue /= 1000F;
        int i = 0;
        for (i = 0; i < TypeSupersonic.fMachAltX.length && TypeSupersonic.fMachAltX[i] <= theAltValue; i++)
            ;
        if (i == 0) return TypeSupersonic.fMachAltY[0];
        else {
            float baseMach = TypeSupersonic.fMachAltY[i - 1];
            float spanMach = TypeSupersonic.fMachAltY[i] - baseMach;
            float baseAlt = TypeSupersonic.fMachAltX[i - 1];
            float spanAlt = TypeSupersonic.fMachAltX[i] - baseAlt;
            float spanMult = (theAltValue - baseAlt) / spanAlt;
            return baseMach + spanMach * spanMult;
        }
    }

    public float calculateMach() {
        return aircraft.FM.getSpeedKMH() / this.getMachForAlt(aircraft.FM.getAltitude());
    }

    public void soundbarrier() {
        float machOne = this.getMachForAlt(aircraft.FM.getAltitude());
        float machFactor = aircraft.FM.getSpeedKMH() / machOne;
        float belowMachOne = machOne - aircraft.FM.getSpeedKMH();
        if (belowMachOne < 0.5F) belowMachOne = 0.5F;
        float aboveMachOne = aircraft.FM.getSpeedKMH() - machOne;
        if (aboveMachOne < 0.5F) aboveMachOne = 0.5F;
        if (machFactor <= 1.0D) {
            aircraft.FM.VmaxAllowed = aircraft.FM.getSpeedKMH() + belowMachOne;
            this.sonicBoom = false;
        } else {
            aircraft.FM.VmaxAllowed = aircraft.FM.getSpeedKMH() + aboveMachOne;
            if (!this.sonicBoom) {
                boolean isInternal = false;
                if (aircraft.FM.actor == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
                    if (Config.isUSE_RENDER()) {
                        if (!Main3D.cur3D().isViewOutside()) {
                            aircraft.playSound("aircraft.SonicBoomInternal", true);
                            isInternal = true;
                        }
                    }
                }
                if (!isInternal) aircraft.playSound("aircraft.SonicBoom", true);
                this.sonicBoom = true;
            }
        }
        
        if (machFactor < this.lowerTranssonicVaporBoundary || machFactor > this.upperTranssonicVaporBoundary) {
            if (this.sonicVapor) {
                if (this.shockwave != null) Eff3DActor.finish(this.shockwave);
                this.sonicVapor = false;
            }
        } else if (!this.sonicVapor) {
            if (Config.isUSE_RENDER() && World.Rnd().nextFloat() < this.getAirDensityFactor(aircraft.FM.getAltitude()) && this.vaporHook != null)  {
                this.shockwave = Eff3DActor.New(aircraft, this.vaporHook, null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
                this.sonicVapor = true;
            }
        }
    }

}
