
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.util.HashMapExt;


public class CockpitMi8 extends CockpitPilot
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
                setNew.throttle = 0.9F * setOld.throttle + fm.CT.PowerControl * 0.1F;
                setNew.starter = 0.94F * setOld.starter + 0.06F * (fm.EI.engines[0].getStage() > 0 && fm.EI.engines[0].getStage() < 6 ? 1.0F : 0.0F);
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                    if(fm.AS.listenLorenzBlindLanding && fm.AS.isAAFIAS)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    } else
                    {
                        setNew.ilsLoc = 0.0F;
                        setNew.ilsGS = 0.0F;
                    }
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth() - fm.Or.azimut());
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                }
                Variables variables = setNew;
                float f1 = 0.9F * setOld.radioalt;
                float f2 = 0.1F;
                float f3 = fm.getAltitude();
                World.cur();
                World.land();
                variables.radioalt = f1 + f2 * (f3 - Landscape.HQ_Air((float)fm.Loc.x, (float)fm.Loc.y));
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle;
        float vspeed;
        float starter;
        float altimeter;
        float radioalt;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;
        float ilsLoc;
        float ilsGS;

        private Variables()
        {
            throttle = 0.0F;
            starter = 0.0F;
            altimeter = 0.0F;
            vspeed = 0.0F;
            radioalt = 0.0F;
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("CF_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(Actor.isAlive(aircraft()))
            aircraft().hierMesh().chunkVisible("CF_D0", true);
        super.doFocusLeave();
    }

    public CockpitMi8()
    {
        super("3DO/Cockpit/MI8/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        bNeedSetUp = true;
        bHasRadar = false;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(super.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(300F, 0.0F, 0.0F);
        light1.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(super.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(300F, 0.0F, 0.0F);
        light2.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        hooknamed = new HookNamed(super.mesh, "LAMPHOOK3");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        light3.light.setColor(300F, 0.0F, 0.0F);
        light3.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK3", light3);
        super.cockpitNightMats = (new String[] {
            "Gause1", "Gause2", "Gause3", "Gause4", "Sidepanel", "instrument1"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Z_STICK1", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 10F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F);
        super.mesh.chunkSetAngles("Z_STICK2", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 10F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F);
        super.mesh.chunkSetAngles("Z_RPM1", 0.0F, -cvt(fm.EI.engines[0].getRPM(), 0.0F, 13500F, 0.0F, 345F), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM2", 0.0F, -cvt(fm.EI.engines[1].getRPM(), 0.0F, 13500F, 0.0F, 345F), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM12", 0.0F, -cvt(fm.EI.engines[0].getRPM(), 0.0F, 13500F, 0.0F, 345F), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM22", 0.0F, -cvt(fm.EI.engines[1].getRPM(), 0.0F, 13500F, 0.0F, 345F), 0.0F);
        super.mesh.chunkSetAngles("Z_FUEL", 0.0F, -cvt(fm.M.fuel, 0.0F, 1470F, 0.0F, 330F), 0.0F);
        super.mesh.chunkSetAngles("Z_FUEL2", 0.0F, -cvt(fm.M.fuel, 0.0F, 1470F, 0.0F, 330F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP1", 0.0F, cvt(fm.EI.engines[0].tOilOut, 20F, 175F, 0.0F, 130F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP2", 0.0F, -cvt(fm.EI.engines[1].tOilOut, 20F, 175F, 0.0F, 130F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP3", 0.0F, -cvt(fm.EI.engines[0].tWaterOut, 300F, 900F, 0.0F, 220F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP12", 0.0F, cvt(fm.EI.engines[0].tOilOut, 20F, 175F, 0.0F, 130F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP22", 0.0F, -cvt(fm.EI.engines[1].tOilOut, 20F, 175F, 0.0F, 130F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP32", 0.0F, -cvt(fm.EI.engines[0].tWaterOut, 300F, 900F, 0.0F, 220F), 0.0F);
        super.mesh.chunkSetAngles("Z_PRES", 0.0F, -cvt(fm.M.fuel > 1.0F ? 0.55F : 0.0F, 0.0F, 1.0F, 0.0F, 160F), 0.0F);
        super.mesh.chunkSetAngles("Z_PRES2", 0.0F, -cvt(fm.M.fuel > 1.0F ? 0.55F : 0.0F, 0.0F, 1.0F, 0.0F, 160F), 0.0F);
        super.mesh.chunkSetAngles("Z_AOA", 0.0F, cvt(fm.getOverload(), -4.5F, 10F, -110F, 220F), 0.0F);
        super.mesh.chunkSetAngles("Z_AOA2", 0.0F, cvt(fm.getOverload(), -4.5F, 10F, -110F, 220F), 0.0F);
        super.mesh.chunkSetAngles("Z_HOUR", 0.0F, -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_MIMUTE", 0.0F, -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_SECOND", 0.0F, -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_HOUR2", 0.0F, -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_MIMUTE2", 0.0F, -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_SECOND2", 0.0F, -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        super.mesh.chunkSetAngles("Z_COMPASSA", 0.0F, 90F + setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSB", -90F + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSC", setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSA2", 0.0F, 90F + setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSB2", -90F + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSC2", setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_SPD", 0.0F, -cvt(fm.getSpeedKMH(), 0.0F, 300F, 0.0F, 330F), 0.0F);
        super.mesh.chunkSetAngles("Z_SPD2", 0.0F, -cvt(fm.getSpeedKMH(), 0.0F, 300F, 0.0F, 330F), 0.0F);
        super.mesh.chunkSetAngles("Z_ALTKM", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_ALTM", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F);
        super.mesh.chunkSetAngles("Z_ALTKM2", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_ALTM2", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F);
        super.mesh.chunkSetAngles("Z_VERTSPD", 0.0F, -cvt(setNew.vspeed, -20F, 20F, -65F, 65F), 0.0F);
        super.mesh.chunkSetAngles("Z_VERTSPD2", 0.0F, -cvt(setNew.vspeed, -20F, 20F, -65F, 65F), 0.0F);
        super.mesh.chunkSetAngles("Z_BANK", 0.0F, -cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
        super.mesh.chunkSetAngles("ADIBANK", fm.Or.getKren(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("ADIMOVE", 1.2F * fm.Or.getTangage(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_BANK2", 0.0F, -cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
        super.mesh.chunkSetAngles("ADIBANK2", fm.Or.getKren(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("ADIMOVE2", 1.2F * fm.Or.getTangage(), 0.0F, 0.0F);
    }

    protected void reflectPlaneMats()
    {
    }

    protected void reflectPlaneToModel()
    {
        if((super.aircraft() instanceof MI8T) || (super.aircraft() instanceof MI8MT))
        {
            super.mesh.chunkVisible("Radar", false);
            super.mesh.chunkVisible("Radar2", false);
            bHasRadar = false;
        }
        else
            bHasRadar = true;
    }

    public void doToggleDim()
    {
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private float pictAiler;
    private float pictElev;
    private LightPointActor light1;
    private LightPointActor light2;
    private LightPointActor light3;
    private boolean bNeedSetUp;
    private boolean bHasRadar;
    public Vector3f w;



}