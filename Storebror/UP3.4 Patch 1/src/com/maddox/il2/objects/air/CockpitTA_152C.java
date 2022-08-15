package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.*;

public class CockpitTA_152C extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.altimeter = fm.getAltitude();
                if(cockpitDimControl)
                {
                    if(setNew.dimPosition > 0.0F)
                        setNew.dimPosition = setOld.dimPosition - 0.05F;
                } else
                if(setNew.dimPosition < 1.0F)
                    setNew.dimPosition = setOld.dimPosition + 0.05F;
                setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
                setNew.vspeed = (499F * setOld.vspeed + fm.getVertSpeed()) / 500F;
                float f = waypointAzimuth();
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
                }
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
                setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float altimeter;
        float throttle;
        float dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;
        float turn;
        float vspeed;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(30F);
    }

    public CockpitTA_152C()
    {
        super("3DO/Cockpit/Ta-152C-1/hier.him", "bf109");
        gun = new Gun[5];
        bomb = new BulletEmitter[4];
        AircraftLH.printCompassHeading = true;
        bBeaconKeysEnabled = ((AircraftLH)aircraft()).bWantBeaconKeys;
        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        w = new Vector3f();
        pictGear = 0.0F;
        setNew.dimPosition = 1.0F;
        this.cockpitNightMats = (new String[] {
            "D9GP1", "D9GP2", "Ta152GP3", "A5GP3Km", "Ta152GP4_MW50", "Ta152GP5", "A4GP6", "Ta152Trans2", "D9GP2", "Ta152Panel3", 
            "D9GP7", "Ta152EQpt5"
        });
        setNightMats(false);
        this.cockpitDimControl = !this.cockpitDimControl;
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(gun[0] == null)
        {
            gun[0] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON01");
            gun[1] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON03");
            gun[2] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON04");
            gun[3] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON05");
            gun[4] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON06");
        }
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        if(bomb[0] == null)
        {
            for(int i = 0; i < bomb.length; i++)
                bomb[i] = GunEmpty.get();

            if(((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05") != GunEmpty.get())
            {
                bomb[1] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
                bomb[2] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
            } else
            if(((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev02") != GunEmpty.get())
            {
                bomb[1] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev02");
                bomb[2] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev02");
            }
            if(((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalRock01") != GunEmpty.get())
            {
                bomb[0] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalRock01");
                bomb[3] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalRock02");
            } else
            if(((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03") != GunEmpty.get())
            {
                bomb[0] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
                bomb[3] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb04");
            } else
            if(((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev05") != GunEmpty.get())
            {
                bomb[0] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev05");
                bomb[3] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalDev06");
            }
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.5F);
        Cockpit.xyz[2] = cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.01F);
        Cockpit.ypr[2] = cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, -2F);
        this.mesh.chunkSetLocate("CanopyTop", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("EjectatorHandle", 0.0F, 0.0F, 2520F * this.fm.CT.getCockpitDoor());
        this.mesh.chunkSetAngles("CanopyFrameL", -cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 2.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CanopyFrameR", cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 2.0F), 0.0F, 0.0F);
        resetYPRmodifier();
        float f1;
        if(this.fm.CT.GearControl == 0.0F)
            f1 = 0.0205F;
        else
            f1 = 0.0F;
        pictGear = 0.93F * pictGear + 0.07F * f1;
        Cockpit.xyz[2] = pictGear;
        this.mesh.chunkSetLocate("FahrHandle", Cockpit.xyz, Cockpit.ypr);
        if((this.fm.AS.astateCockpitState & 0x20) == 0 && (this.fm.AS.astateCockpitState & 0x80) == 0)
        {
            this.mesh.chunkSetAngles("NeedleALT", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleALTKm", 0.0F, 0.0F, cvt(setNew.altimeter, 0.0F, 10000F, 0.0F, -180F));
        }
        this.mesh.chunkSetAngles("NeedleManPress", -cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 15F, 345F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleKMH", -floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 900F, 0.0F, 9F), speedometerScale), 0.0F, 0.0F);
        if((this.fm.AS.astateCockpitState & 0x40) == 0 && (this.fm.AS.astateCockpitState & 8) == 0 && (this.fm.AS.astateCockpitState & 0x10) == 0)
            this.mesh.chunkSetAngles("NeedleRPM", -floatindex(cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleFuel", floatindex(cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 4F), fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleWaterTemp", cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleOilTemp", cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 60F), 0.0F, 0.0F);
        if((this.fm.AS.astateCockpitState & 2) == 0 && (this.fm.AS.astateCockpitState & 1) == 0 && (this.fm.AS.astateCockpitState & 4) == 0)
        {
            this.mesh.chunkSetAngles("NeedleFuelPress", cvt(this.fm.EI.engines[0].getRPM() / 1500F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
            if(this.fm.getOverload() < 0.0F)
                this.mesh.chunkSetAngles("NeedleOilPress", -cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut + this.fm.EI.engines[0].getRPM() / 1300F + this.fm.getOverload() * 2.0F, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
            else
                this.mesh.chunkSetAngles("NeedleOilPress", -cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut + this.fm.EI.engines[0].getRPM() / 1300F + this.fm.getOverload() / 2.0F, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
            if(this.fm.EI.engines[0].getRPM() < 200F)
                this.mesh.chunkSetAngles("NeedleOilPress", -cvt(0.0F, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        }
        if((this.fm.AS.astateCockpitState & 0x40) == 0 && (this.fm.AS.astateCockpitState & 8) == 0 && (this.fm.AS.astateCockpitState & 0x10) == 0)
        {
            this.mesh.chunkSetAngles("NeedleAHTurn", 0.0F, cvt(setNew.turn, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
            this.mesh.chunkSetAngles("NeedleAHTurn", f1, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleAHBank", 0.0F, -cvt(getBall(6D), -6F, 6F, 11F, -11F), 0.0F);
            this.mesh.chunkSetAngles("NeedleAHCyl", 0.0F, 0.0F, this.fm.Or.getKren());
            this.mesh.chunkSetAngles("NeedleAHBar", 0.0F, 0.0F, cvt(this.fm.Or.getTangage(), -45F, 45F, 12F, -12F));
        }
        if((this.fm.AS.astateCockpitState & 0x20) == 0 && (this.fm.AS.astateCockpitState & 0x80) == 0)
            this.mesh.chunkSetAngles("NeedleCD", setNew.vspeed >= 0.0F ? -floatindex(cvt(setNew.vspeed, 0.0F, 30F, 0.0F, 6F), vsiNeedleScale) : floatindex(cvt(-setNew.vspeed, 0.0F, 30F, 0.0F, 6F), vsiNeedleScale), 0.0F, 0.0F);
        if((this.fm.AS.astateCockpitState & 0x40) == 0 && (this.fm.AS.astateCockpitState & 8) == 0 && (this.fm.AS.astateCockpitState & 0x10) == 0)
            if(useRealisticNavigationInstruments())
            {
                this.mesh.chunkSetAngles("RepeaterPlane", -setNew.azimuth.getDeg(f) + setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("RepeaterOuter", setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            } else
            {
                this.mesh.chunkSetAngles("RepeaterOuter", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("RepeaterPlane", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            }
        this.mesh.chunkSetAngles("NeedleHBSmall", -105F + (float)Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleHBLarge", -270F + (float)Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F, 0.0F, 0.0F);
        if((this.fm.AS.astateCockpitState & 0x40) == 0 && (this.fm.AS.astateCockpitState & 8) == 0 && (this.fm.AS.astateCockpitState & 0x10) == 0)
            this.mesh.chunkSetAngles("NeedleTrimmung", this.fm.CT.getTrimElevatorControl() * 25F * 2.0F, 0.0F, 0.0F);
        if((this.fm.AS.astateCockpitState & 0x20) == 0 && (this.fm.AS.astateCockpitState & 0x80) == 0)
        {
            this.mesh.chunkSetAngles("NeedleHClock", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleMClock", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleSClock", -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        }
        resetYPRmodifier();
        if(gun[0] != null)
        {
            Cockpit.xyz[0] = cvt(gun[0].countBullets(), 0.0F, 55F, -0.018F, 0.0F);
            this.mesh.chunkVisible("XLampMK108", gun[0].haveBullets());
        }
        if(gun[3] != null)
        {
            Cockpit.xyz[0] = cvt(gun[3].countBullets(), 0.0F, 500F, -0.044F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG17_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("XLampMG17_1", gun[3].haveBullets());
        }
        if(gun[4] != null)
        {
            Cockpit.xyz[0] = cvt(gun[4].countBullets(), 0.0F, 500F, -0.044F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG17_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("XLampMG17_2", gun[4].haveBullets());
        }
        if(gun[1] != null)
        {
            Cockpit.xyz[0] = cvt(gun[1].countBullets(), 0.0F, 200F, -0.017F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("XLampMG151_1", gun[1].haveBullets());
        }
        if(gun[2] != null)
        {
            Cockpit.xyz[0] = cvt(gun[2].countBullets(), 0.0F, 200F, -0.017F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("XLampMG151_2", gun[2].haveBullets());
        }
        this.mesh.chunkSetAngles("IgnitionSwitch", 24F * (float)this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Revi16Tinter", cvt(interp(setNew.dimPosition, setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl) * 20F, (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl) * 20F);
        resetYPRmodifier();
        if(this.fm.CT.saveWeaponControl[1])
            Cockpit.xyz[2] = -0.004F;
        this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
        float f2 = 0.0F;
        int i = 0;
        if(this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1] || this.fm.CT.saveWeaponControl[3])
        {
            if(this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1])
                f2 = 20F;
            tClap = Time.tickCounter() + World.Rnd().nextInt(500, 1000);
            if(this.fm.CT.saveWeaponControl[0] && !this.fm.CT.saveWeaponControl[1])
                selectorAngle = 24F;
            else
            if(!this.fm.CT.saveWeaponControl[0] && this.fm.CT.saveWeaponControl[1])
                selectorAngle = 43F;
            else
                selectorAngle = 10F;
        }
        if(Time.tickCounter() < tClap)
            i = 1;
        this.mesh.chunkSetAngles("SecTrigger2", -(240F + f2) * (pictClap = 0.85F * pictClap + 0.15F * (float)i), 0.0F, 0.0F);
        this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
        Cockpit.ypr[0] = interp(setNew.throttle, setOld.throttle, f) * 34F * 0.91F;
        Cockpit.xyz[2] = Cockpit.ypr[0] > 7F ? -0.006F : 0.0F;
        this.mesh.chunkSetLocate("ThrottleQuad", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("RPedalBase", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("RPedalStrut", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("RPedal", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F - this.fm.CT.getBrake() * 15F);
        this.mesh.chunkSetAngles("LPedalBase", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("LPedalStrut", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("LPedal", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F - this.fm.CT.getBrake() * 15F);
        this.mesh.chunkVisible("XLampTankSwitch", (float)this.fm.EI.engines[0].getStage() > 0.0F);
        this.mesh.chunkVisible("XLampFuelLow", this.fm.M.fuel < 81.6F);
        this.mesh.chunkVisible("XLampGearL_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearL_2", this.fm.CT.getGear() > 0.95F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearR_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearR_2", this.fm.CT.getGear() > 0.95F && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearC_1", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("XLampGearC_3", this.fm.CT.getFlap() > 0.1F && this.fm.CT.getFlap() < 0.5F);
        this.mesh.chunkVisible("XLampGearC_2", this.fm.CT.getFlap() > 0.9F);
        resetYPRmodifier();
        Cockpit.xyz[2] = this.fm.CT.GearControl < 0.5F ? 0.02F : 0.0F;
        this.mesh.chunkSetAngles("NeedleNahe1", cvt(setNew.beaconDirection, -45F, 45F, 20F, -20F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleNahe2", cvt(setNew.beaconRange, 0.0F, 1.0F, -20F, 20F), 0.0F, 0.0F);
        this.mesh.chunkVisible("AFN2_RED", isOnBlindLandingMarker());
        if(HotKeyCmd.getByRecordedId(142).isActive() && Main3D.cur3D().aircraftHotKeys.isPropAuto())
            this.mesh.chunkSetAngles("SwitchProp", -23F, 0.0F, 0.0F);
        if(HotKeyCmd.getByRecordedId(142).isActive() && !Main3D.cur3D().aircraftHotKeys.isPropAuto())
            this.mesh.chunkSetAngles("SwitchProp", 23F, 0.0F, 0.0F);
        if(this.fm.EI.engines[0].getControlAfterburner())
        {
            this.mesh.chunkSetAngles("SwitchMW50", -23F, 0.0F, 0.0F);
            this.mesh.chunkVisible("XLampMW50", true);
        } else
        {
            this.mesh.chunkSetAngles("SwitchMW50", 23F, 0.0F, 0.0F);
            this.mesh.chunkVisible("XLampMW50", false);
        }
        if(this.fm.getAltitude() > 4000F)
            this.mesh.chunkVisible("XLampAlt", true);
        else
            this.mesh.chunkVisible("XLampAlt", false);
        float f3 = Atmosphere.temperature((float)this.fm.Loc.z) - 273.15F;
        if(f3 < -16F)
            this.mesh.chunkVisible("XLampPitot", true);
        else
        if(f3 > -12F)
            this.mesh.chunkVisible("XLampPitot", false);
    }

    public void toggleDim()
    {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(this.cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 2) != 0 || (this.fm.AS.astateCockpitState & 1) != 0 || (this.fm.AS.astateCockpitState & 4) != 0)
        {
            this.mesh.chunkVisible("Revi16", false);
            this.mesh.chunkVisible("Revi16Tinter", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("DRevi16", true);
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.materialReplace("D9GP1", "DD9GP1");
            this.mesh.materialReplace("D9GP1_night", "DD9GP1_night");
            this.mesh.chunkVisible("NeedleManPress", false);
            this.mesh.chunkVisible("NeedleRPM", false);
            this.mesh.chunkVisible("RepeaterOuter", false);
            this.mesh.chunkVisible("RepeaterPlane", false);
        }
        if((this.fm.AS.astateCockpitState & 0x40) != 0 || (this.fm.AS.astateCockpitState & 8) != 0 || (this.fm.AS.astateCockpitState & 0x10) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.materialReplace("D9GP2", "DD9GP2");
            this.mesh.materialReplace("D9GP2_night", "DD9GP2_night");
            this.mesh.chunkVisible("NeedleAHCyl", false);
            this.mesh.chunkVisible("NeedleAHBank", false);
            this.mesh.chunkVisible("NeedleAHBar", false);
            this.mesh.chunkVisible("NeedleAHTurn", false);
            this.mesh.chunkVisible("NeedleFuelPress", false);
            this.mesh.chunkVisible("NeedleOilPress", false);
            this.mesh.materialReplace("Ta152GP4_MW50", "DTa152GP4");
            this.mesh.materialReplace("Ta152GP4_MW50_night", "DTa152GP4_night");
            this.mesh.chunkVisible("NeedleFuel", false);
            resetYPRmodifier();
            Cockpit.xyz[0] = -0.001F;
            Cockpit.xyz[1] = 0.008F;
            Cockpit.xyz[2] = 0.025F;
            Cockpit.ypr[0] = 3F;
            Cockpit.ypr[1] = -6F;
            Cockpit.ypr[2] = 1.5F;
            this.mesh.chunkSetLocate("IPCentral", Cockpit.xyz, Cockpit.ypr);
        }
        if((this.fm.AS.astateCockpitState & 0x20) != 0 || (this.fm.AS.astateCockpitState & 0x80) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.materialReplace("Ta152GP3", "DTa152GP3");
            this.mesh.materialReplace("Ta152GP3_night", "DTa152GP3_night");
            this.mesh.chunkVisible("NeedleKMH", false);
            this.mesh.chunkVisible("NeedleCD", false);
            this.mesh.chunkVisible("NeedleAlt", false);
            this.mesh.chunkVisible("NeedleAltKM", false);
            this.mesh.materialReplace("Ta152Trans2", "DTa152Trans2");
            this.mesh.materialReplace("Ta152Trans2_night", "DTa152Trans2_night");
            this.mesh.chunkVisible("NeedleWaterTemp", false);
            this.mesh.chunkVisible("NeedleOilTemp", false);
        }
        retoggleLight();
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private boolean bBeaconKeysEnabled;
    private float tmp;
    private Gun gun[];
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private BulletEmitter bomb[];
    private float pictAiler;
    private float pictElev;
    public Vector3f w;
    private float pictGear;
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        0.0F, 18.5F, 67F, 117F, 164F, 215F, 267F, 320F, 379F, 427F, 
        428F
    };
    private static final float rpmScale[] = {
        0.0F, 11.25F, 53F, 108F, 170F, 229F, 282F, 334F, 342.5F, 342.5F
    };
    private static final float fuelScale[] = {
        0.0F, 16F, 35F, 52.5F, 72F, 72F
    };
    private static final float manPrsScale[] = {
        0.0F, 0.0F, 0.0F, 15.5F, 71F, 125F, 180F, 235F, 290F, 245F, 
        247F, 247F
    };
    private static final float oilfuelNeedleScale[] = {
        0.0F, 38F, 84F, 135.5F, 135F
    };
    private static final float vsiNeedleScale[] = {
        0.0F, 48F, 82F, 96.5F, 111F, 120.5F, 130F, 130F
    };
    private int tClap;
    private float pictClap;
    private float selectorAngle;

    static 
    {
        Property.set(CockpitTA_152C.class, "normZN", 0.72F);
        Property.set(CockpitTA_152C.class, "gsZN", 0.66F);
    }

}
