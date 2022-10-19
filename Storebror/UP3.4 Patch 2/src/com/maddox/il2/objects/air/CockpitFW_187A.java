package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitFW_187A extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            if(cockpitDimControl)
            {
                if(setOld.dimPosition > 0.0F)
                    setOld.dimPosition = setTmp.dimPosition - 0.05F;
            } else
            if(setOld.dimPosition < 1.0F)
                setOld.dimPosition = setTmp.dimPosition + 0.05F;
            setOld.throttle1 = 0.91F * setTmp.throttle1 + 0.09F * fm.EI.engines[0].getControlThrottle();
            setOld.throttle2 = 0.91F * setTmp.throttle2 + 0.09F * fm.EI.engines[1].getControlThrottle();
            setOld.mix1 = 0.88F * setTmp.mix1 + 0.12F * fm.EI.engines[0].getControlMix();
            setOld.mix2 = 0.88F * setTmp.mix2 + 0.12F * fm.EI.engines[1].getControlMix();
            setOld.azimuth = fm.Or.getYaw();
            if(setTmp.azimuth > 270F && setOld.azimuth < 90F)
                setTmp.azimuth -= 360F;
            if(setTmp.azimuth < 90F && setOld.azimuth > 270F)
                setTmp.azimuth += 360F;
            setOld.waypointAzimuth = (10F * setTmp.waypointAzimuth + (waypointAzimuth() - setTmp.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
            buzzerFX(fm.CT.getGear() < 0.999999F && fm.CT.getFlap() > 0.0F);
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float altimeter;
        float throttle1;
        float throttle2;
        float dimPosition;
        float azimuth;
        float waypointAzimuth;
        float mix1;
        float mix2;

        private Variables()
        {
        }

    }

    protected float waypointAzimuth()
    {
        WayPoint waypoint = fm.AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, fm.Loc);
            return (float)(Math.toDegrees(Math.atan2(tmpV.y, tmpV.x)));
        }
    }

    public CockpitFW_187A()
    {
        super("3DO/Cockpit/CockpitFW_187A/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        pictManifold1 = 0.0F;
        pictManifold2 = 0.0F;
        setOld = new Variables();
        setNew = new Variables();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        setNew.dimPosition = 1.0F;
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(126F, 232F, 245F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(126F, 232F, 245F);
        light2.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        cockpitNightMats = (new String[] {
            "ZClocks1", "ZClocks1DMG", "ZClocks2", "ZClocks3", "FW190A4Compass"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        loadBuzzerFX();
    }

    public void reflectWorldToInstruments(float f)
    {
        if(gun[0] == null)
        {
            gun[0] = ((Aircraft)fm.actor).getGunByHookName("_CANNON01");
            gun[1] = ((Aircraft)fm.actor).getGunByHookName("_CANNON05");
            gun[2] = ((Aircraft)fm.actor).getGunByHookName("_CANNON02");
        }
        mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ReviTint", cvt(interp(setNew.dimPosition, setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 45F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ATA1", cvt(pictManifold1 = 0.75F * pictManifold1 + 0.25F * fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 329F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ATA2", cvt(pictManifold2 = 0.75F * pictManifold2 + 0.25F * fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 329F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM1", floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM2", floatindex(cvt(fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_FuelQuantity1", -44.5F + floatindex(cvt(fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 8F), fuelScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_EngTemp1", cvt(fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_EngTemp2", cvt(fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_FuelPress1", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_FuelPress2", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_OilPress1", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_OilPress2", cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass1", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        mesh.chunkSetAngles("Z_Azimuth1", -interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_PropPitch1", 270F - (float)Math.toDegrees(fm.EI.engines[0].getPropPhi() - fm.EI.engines[0].getPropPhiMin()) * 60F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_PropPitch2", 105F - (float)Math.toDegrees(fm.EI.engines[0].getPropPhi() - fm.EI.engines[0].getPropPhiMin()) * 5F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_PropPitch3", 270F - (float)Math.toDegrees(fm.EI.engines[1].getPropPhi() - fm.EI.engines[1].getPropPhiMin()) * 60F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_PropPitch4", 105F - (float)Math.toDegrees(fm.EI.engines[1].getPropPhi() - fm.EI.engines[1].getPropPhiMin()) * 5F, 0.0F, 0.0F);
        float f1;
        if(aircraft().isFMTrackMirror())
        {
            f1 = aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else
        {
            f1 = cvt((setNew.azimuth - setOld.azimuth) / Time.tickLenFs(), -3F, 3F, 30F, -30F);
            if(aircraft().fmTrack() != null)
                aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
        }
        mesh.chunkSetAngles("Z_TurnBank1", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TurnBank2", cvt(getBall(6D), -6F, 6F, -4.5F, 4.5F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Horizon1", 0.0F, 0.0F, fm.Or.getKren());
        mesh.chunkSetAngles("Z_Horizon2", cvt(fm.Or.getTangage(), -45F, 45F, -13F, 13F), 0.0F, 0.0F);
        mesh.chunkVisible("Z_FuelWarning1", fm.M.fuel < 36F);
        mesh.chunkVisible("Z_GearLRed1", fm.CT.getGear() == 0.0F);
        mesh.chunkVisible("Z_GearRRed1", fm.CT.getGear() == 0.0F);
        mesh.chunkVisible("Z_GearLGreen1", fm.CT.getGear() == 1.0F);
        mesh.chunkVisible("Z_GearRGreen1", fm.CT.getGear() == 1.0F);
        if(gun[0] != null)
            mesh.chunkSetAngles("Z_AmmoCounter1", cvt(gun[0].countBullets(), 0.0F, 250F, 15F, 0.0F), 0.0F, 0.0F);
        if(gun[1] != null)
            mesh.chunkSetAngles("Z_AmmoCounter2", cvt(gun[1].countBullets(), 0.0F, 250F, 15F, 0.0F), 0.0F, 0.0F);
        if(gun[2] != null)
            mesh.chunkSetAngles("Z_AmmoCounter3", cvt(gun[2].countBullets(), 0.0F, 250F, 15F, 0.0F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Column", (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 15F, 0.0F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F);
        mesh.chunkSetAngles("Z_PedalStrut", fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_LeftPedal", -fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RightPedal", -fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Column", fm.CT.getAileron() * 15F, 0.0F, fm.CT.getElevator() * 10F);
        if((fm.AS.astateCockpitState & 8) == 0)
            mesh.chunkSetAngles("Z_Throttle", interp(setNew.throttle1, setOld.throttle1, f) * 68.18182F, 0.0F, 0.0F);
        if((fm.AS.astateCockpitState & 8) == 0)
            mesh.chunkSetAngles("Z_Throttle2", interp(setNew.throttle2, setOld.throttle2, f) * 68.18182F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Mix", interp(setNew.mix1, setOld.mix2, f) * 62.5F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Mix2", interp(setNew.mix1, setOld.mix2, f) * 62.5F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_MagnetoSwitch", -45F + 28.333F * (float)fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_MagnetoSwit1", -45F + 28.333F * (float)fm.EI.engines[1].getControlMagnetos(), 0.0F, 0.0F);
        mesh.chunkVisible("BodyErla", false);
        if(fm.AS.bIsAboutToBailout)
        {
            mesh.chunkVisible("Top", false);
            mesh.chunkVisible("Z_Holes2_D1", false);
            mesh.chunkVisible("Z_Holes1_D1", false);
            mesh.chunkVisible("BodyErla", true);
        }
    }

    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            light1.light.setEmit(0.005F, 0.5F);
            light2.light.setEmit(0.005F, 0.5F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("Z_Holes3_D1", true);
            mesh.chunkVisible("Revi_D0", false);
            mesh.chunkVisible("Z_ReviTint", false);
            mesh.chunkVisible("Z_Z_RETICLE", false);
            mesh.chunkVisible("Z_Z_MASK", false);
            mesh.chunkVisible("Revi_D1", true);
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("Z_Holes2_D1", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("PoppedPanel_D0", false);
            mesh.chunkVisible("Z_Repeater1", false);
            mesh.chunkVisible("Z_Azimuth1", false);
            mesh.chunkVisible("Z_Compass1", false);
            mesh.chunkVisible("Z_Speedometer1", false);
            mesh.chunkVisible("PoppedPanel_D1", true);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("Z_Holes1_D1", true);
        if((fm.AS.astateCockpitState & 8) != 0);
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("Z_OilSplats_D1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("Z_Holes1_D1", true);
            mesh.chunkVisible("Radio_D0", false);
            mesh.chunkVisible("Radio_D1", true);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0);
    }

    private Gun gun[] = {
        null, null, null
    };
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private LightPointActor light1;
    private LightPointActor light2;
    private float pictAiler;
    private float pictElev;
    private float pictManifold1;
    private float pictManifold2;
    private static final float speedometerScale[] = {
        0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 
        212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F
    };
    private static final float rpmScale[] = {
        0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F
    };
    private static final float fuelScale[] = {
        0.0F, 9F, 21F, 29.5F, 37F, 48F, 61.5F, 75.5F, 92F, 92F
    };
    private Point3d tmpP;
    private Vector3d tmpV;

    static 
    {
        Property.set(CockpitFW_187A.class, "normZN", 0.4F);
        Property.set(CockpitFW_187A.class, "gsZN", 0.4F);
    }

}
