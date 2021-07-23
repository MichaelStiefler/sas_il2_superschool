package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitPE_8 extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                if(bNeedSetUp)
                {
                    reflectPlaneMats();
                    bNeedSetUp = false;
                }
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                for(int i = 0; i < 4; i++)
                    setNew.throttle[i] = (10F * setOld.throttle[i] + fm.EI.engines[i].getControlThrottle()) / 11F;

                pictSupc1 = 0.8F * pictSupc1 + 0.2F * (float)fm.EI.engines[0].getControlCompressor();
                pictSupc2 = 0.8F * pictSupc2 + 0.2F * (float)fm.EI.engines[1].getControlCompressor();
                pictSupc3 = 0.8F * pictSupc3 + 0.2F * (float)fm.EI.engines[2].getControlCompressor();
                pictSupc4 = 0.8F * pictSupc4 + 0.2F * (float)fm.EI.engines[3].getControlCompressor();
                float f = 20F;
                if(gearsLever != 0.0F && gears == fm.CT.getGear())
                {
                    gearsLever = gearsLever * 0.8F;
                    if(Math.abs(gearsLever) < 0.1F)
                        gearsLever = 0.0F;
                } else
                if(gears < fm.CT.getGear())
                {
                    gears = fm.CT.getGear();
                    gearsLever = gearsLever + 2.0F;
                    if(gearsLever > f)
                        gearsLever = f;
                } else
                if(gears > fm.CT.getGear())
                {
                    gears = fm.CT.getGear();
                    gearsLever = gearsLever - 2.0F;
                    if(gearsLever < -f)
                        gearsLever = -f;
                }
                float f1 = fm.CT.FlapsControl;
                float f2 = 0.0F;
                if(f1 < 0.2F)
                    f2 = 1.5F;
                else
                if(f1 < 0.3333333F)
                    f2 = 2.0F;
                else
                    f2 = 1.0F;
                setNew.flaps = 0.91F * setOld.flaps + 0.09F * f1 * f2;
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                setNew.prop1 = (10F * setOld.prop1 + fm.EI.engines[0].getControlProp()) / 11F;
                setNew.prop2 = (10F * setOld.prop2 + fm.EI.engines[1].getControlProp()) / 11F;
                setNew.prop3 = (10F * setOld.prop3 + fm.EI.engines[2].getControlProp()) / 11F;
                setNew.prop4 = (10F * setOld.prop4 + fm.EI.engines[3].getControlProp()) / 11F;
                setNew.mix1 = (10F * setOld.mix1 + fm.EI.engines[0].getControlMix()) / 11F;
                setNew.mix2 = (10F * setOld.mix2 + fm.EI.engines[1].getControlMix()) / 11F;
                setNew.mix3 = (10F * setOld.mix3 + fm.EI.engines[2].getControlMix()) / 11F;
                setNew.mix4 = (10F * setOld.mix4 + fm.EI.engines[3].getControlMix()) / 11F;
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    if(fm.AS.listenLorenzBlindLanding && fm.AS.isAAFIAS)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                        setNew.waypointAzimuth.setDeg(0.0F);
                    } else
                    {
                        setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), getBeaconDirection());
                        setNew.ilsLoc = 0.0F;
                        setNew.ilsGS = 0.0F;
                    }
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth() - fm.Or.azimut());
                }
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

        float throttle[] = {
            0.0F, 0.0F, 0.0F, 0.0F
        };
        float altimeter;
        AnglesFork azimuth;
        float vspeed;
        float prop1;
        float prop2;
        float prop3;
        float prop4;
        float mix1;
        float mix2;
        float flaps;
        float mix3;
        float mix4;
        float turn;
        AnglesFork waypointAzimuth;
        float ilsLoc;
        float ilsGS;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

    }


    protected float waypointAzimuth()
    {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitPE_8()
    {
        super("3DO/Cockpit/Pe-8/hier.him", "he111");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictManifold1 = 0.0F;
        pictManifold2 = 0.0F;
        pictManifold3 = 0.0F;
        pictManifold4 = 0.0F;
        rpmGeneratedPressure1 = 0.0F;
        rpmGeneratedPressure2 = 0.0F;
        rpmGeneratedPressure3 = 0.0F;
        rpmGeneratedPressure4 = 0.0F;
        oilPressure1 = 0.0F;
        oilPressure2 = 0.0F;
        oilPressure3 = 0.0F;
        oilPressure4 = 0.0F;
        pictSupc1 = 0.0F;
        pictSupc2 = 0.0F;
        pictSupc3 = 0.0F;
        pictSupc4 = 0.0F;
        gearsLever = 0.0F;
        gears = 0.0F;
        enteringAim = false;
        bEntered = false;
        cockpitNightMats = (new String[] {
            "AP_Nid_DM", "AP_Nid", "APDials_DM", "APDials", "ArtHoriz", "ArtHoriz_DM", "GP_II", "GP_II_DM", "GP_III", "GP_III_DM", 
            "GP_IV", "GP_IV_DM", "GP_V", "GP_V_DM", "GP_VI", "GP_VII", "GP_VII_DM", "TrimBase"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.04F
        });
    }

    public void reflectWorldToInstruments(float f)
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            if(hookpilot.isAimReached())
            {
                enteringAim = false;
                enter();
            } else
            if(!hookpilot.isAim())
                enteringAim = false;
        }
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("Z_StickBase", 0.0F, 0.0F, 12.5F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) - 12.5F);
        mesh.chunkSetAngles("Z_Steer", -80F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 0.0F);
        float f1 = 15F * fm.CT.getRudder();
        float f2 = 10F * fm.CT.getBrakeR();
        float f3 = 10F * fm.CT.getBrakeL();
        mesh.chunkSetAngles("PedLever", 0.0F, 0.0F, -f1);
        mesh.chunkSetAngles("PedLever01", 0.0F, 0.0F, f1);
        mesh.chunkSetAngles("PedRod", 0.0F, 0.0F, f1);
        mesh.chunkSetAngles("PedRod01", 0.0F, 0.0F, -f1);
        mesh.chunkSetAngles("PedBase", 0.0F, 0.0F, f1 * 1.5F);
        mesh.chunkSetAngles("PedBase01", 0.0F, 0.0F, -f1 * 1.5F);
        mesh.chunkSetAngles("PedBrake", 0.0F, 0.0F, -f3);
        mesh.chunkSetAngles("PedBrake01", 0.0F, 0.0F, -f2);
        mesh.chunkSetAngles("PedBrakeRod", 0.0F, 0.0F, -f3 / 2.05F);
        mesh.chunkSetAngles("PedBrakeRod01", 0.0F, 0.0F, -f2 / 2.05F);
        mesh.chunkSetAngles("PedBrakeLever", 0.0F, 0.0F, f3);
        mesh.chunkSetAngles("PedBrakeLever01", 0.0F, 0.0F, f2);
        for(int i = 0; i < 4; i++)
        {
            mesh.chunkSetAngles("Z_throttle" + (i + 1), 0.0F, 0.0F, -70F * interp(setNew.throttle[i], setOld.throttle[i], f));
            mesh.chunkSetAngles("Z_aux_throttle" + (i + 1), 0.0F, 0.0F, -35F * interp(setNew.throttle[i], setOld.throttle[i], f));
        }

        float f4 = 45F * interp(setNew.prop1, setOld.prop1, f);
        mesh.chunkSetAngles("Z_pitch_1", f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_pitchrod_1", -f4, 0.0F, 0.0F);
        f4 = 45F * interp(setNew.prop2, setOld.prop2, f);
        mesh.chunkSetAngles("Z_pitch_2", f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_pitchrod_2", -f4, 0.0F, 0.0F);
        f4 = 45F * interp(setNew.prop3, setOld.prop3, f);
        mesh.chunkSetAngles("Z_pitch_3", f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_pitchrod_3", -f4, 0.0F, 0.0F);
        f4 = 45F * interp(setNew.prop4, setOld.prop4, f);
        mesh.chunkSetAngles("Z_pitch_4", f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_pitchrod_4", -f4, 0.0F, 0.0F);
        f4 = 60F * interp(setNew.mix1, setOld.mix1, f);
        mesh.chunkSetAngles("Z_mixture_1", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Z_mixknob1", 0.0F, 0.0F, f4);
        f4 = 60F * interp(setNew.mix2, setOld.mix2, f);
        mesh.chunkSetAngles("Z_mixture_2", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Z_mixknob2", 0.0F, 0.0F, f4);
        f4 = 60F * interp(setNew.mix3, setOld.mix3, f);
        mesh.chunkSetAngles("Z_mixture_3", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Z_mixknob3", 0.0F, 0.0F, f4);
        f4 = 60F * interp(setNew.mix4, setOld.mix4, f);
        mesh.chunkSetAngles("Z_mixture_4", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Z_mixknob4", 0.0F, 0.0F, f4);
        mesh.chunkSetAngles("Z_superc_1", pictSupc1 * 39F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_superc_2", pictSupc2 * 39F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_superc_3", pictSupc3 * 39F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_superc_4", pictSupc4 * 39F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Compass", setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_AutoPilot01", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Alt_Short", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Alt_Big", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Inclinometer", cvt(setNew.turn, -0.2F, 0.2F, -25F, 25F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_ball", cvt(getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_ball2", cvt(getBall(8D), -8F, 8F, 16F, -16F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Airspeed", -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeed2KMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RPMlong_L", -cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RPMShort_L", -cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RPMlong_L01", -cvt(fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RPMShort_L01", -cvt(fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RPMlong_L02", -cvt(fm.EI.engines[2].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RPMShort_L02", -cvt(fm.EI.engines[2].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RPMlong_L03", -cvt(fm.EI.engines[3].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RPMShort_L03", -cvt(fm.EI.engines[3].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        pictManifold1 = 0.85F * pictManifold1 + 0.15F * fm.EI.engines[0].getManifoldPressure() * 76F;
        pictManifold2 = 0.85F * pictManifold2 + 0.15F * fm.EI.engines[1].getManifoldPressure() * 76F;
        pictManifold3 = 0.85F * pictManifold3 + 0.15F * fm.EI.engines[2].getManifoldPressure() * 76F;
        pictManifold4 = 0.85F * pictManifold4 + 0.15F * fm.EI.engines[3].getManifoldPressure() * 76F;
        mesh.chunkSetAngles("Z_Need_NadduvL", -cvt(pictManifold1, 30F, 120F, 0.0F, 300F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_NadduvL06", -cvt(pictManifold2, 30F, 120F, 0.0F, 300F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_NadduvL07", -cvt(pictManifold3, 30F, 120F, 0.0F, 300F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_NadduvL08", -cvt(pictManifold4, 30F, 120F, 0.0F, 300F), 0.0F, 0.0F);
        if(fm.Or.getKren() < -110F || fm.Or.getKren() > 110F)
        {
            rpmGeneratedPressure1 = rpmGeneratedPressure1 - 2.0F;
            rpmGeneratedPressure2 = rpmGeneratedPressure2 - 2.0F;
            rpmGeneratedPressure3 = rpmGeneratedPressure3 - 2.0F;
            rpmGeneratedPressure4 = rpmGeneratedPressure4 - 2.0F;
        } else
        {
            if(fm.EI.engines[0].getRPM() < rpmGeneratedPressure1)
                rpmGeneratedPressure1 = rpmGeneratedPressure1 - (rpmGeneratedPressure1 - fm.EI.engines[0].getRPM()) * 0.01F;
            else
                rpmGeneratedPressure1 = rpmGeneratedPressure1 + (fm.EI.engines[0].getRPM() - rpmGeneratedPressure1) * 0.001F;
            if(fm.EI.engines[1].getRPM() < rpmGeneratedPressure2)
                rpmGeneratedPressure2 = rpmGeneratedPressure2 - (rpmGeneratedPressure2 - fm.EI.engines[1].getRPM()) * 0.01F;
            else
                rpmGeneratedPressure2 = rpmGeneratedPressure2 + (fm.EI.engines[1].getRPM() - rpmGeneratedPressure2) * 0.001F;
            if(fm.EI.engines[2].getRPM() < rpmGeneratedPressure3)
                rpmGeneratedPressure3 = rpmGeneratedPressure3 - (rpmGeneratedPressure3 - fm.EI.engines[2].getRPM()) * 0.01F;
            else
                rpmGeneratedPressure3 = rpmGeneratedPressure3 + (fm.EI.engines[2].getRPM() - rpmGeneratedPressure3) * 0.001F;
            if(fm.EI.engines[3].getRPM() < rpmGeneratedPressure4)
                rpmGeneratedPressure4 = rpmGeneratedPressure4 - (rpmGeneratedPressure4 - fm.EI.engines[3].getRPM()) * 0.01F;
            else
                rpmGeneratedPressure4 = rpmGeneratedPressure4 + (fm.EI.engines[3].getRPM() - rpmGeneratedPressure4) * 0.001F;
        }
        if(rpmGeneratedPressure1 < 800F)
            oilPressure1 = cvt(rpmGeneratedPressure1, 0.0F, 800F, 0.0F, 4F);
        else
        if(rpmGeneratedPressure1 < 1800F)
            oilPressure1 = cvt(rpmGeneratedPressure1, 800F, 1800F, 4F, 8F);
        else
            oilPressure1 = cvt(rpmGeneratedPressure1, 1800F, 2750F, 8F, 10F);
        if(rpmGeneratedPressure2 < 800F)
            oilPressure2 = cvt(rpmGeneratedPressure2, 0.0F, 800F, 0.0F, 4F);
        else
        if(rpmGeneratedPressure2 < 1800F)
            oilPressure2 = cvt(rpmGeneratedPressure2, 800F, 1800F, 4F, 8F);
        else
            oilPressure2 = cvt(rpmGeneratedPressure2, 1800F, 2750F, 8F, 10F);
        if(rpmGeneratedPressure3 < 800F)
            oilPressure3 = cvt(rpmGeneratedPressure3, 0.0F, 800F, 0.0F, 4F);
        else
        if(rpmGeneratedPressure3 < 1800F)
            oilPressure3 = cvt(rpmGeneratedPressure3, 800F, 1800F, 4F, 8F);
        else
            oilPressure3 = cvt(rpmGeneratedPressure3, 1800F, 2750F, 8F, 10F);
        if(rpmGeneratedPressure4 < 800F)
            oilPressure4 = cvt(rpmGeneratedPressure4, 0.0F, 800F, 0.0F, 4F);
        else
        if(rpmGeneratedPressure4 < 1800F)
            oilPressure4 = cvt(rpmGeneratedPressure4, 800F, 1800F, 4F, 8F);
        else
            oilPressure4 = cvt(rpmGeneratedPressure4, 1800F, 2750F, 8F, 10F);
        float f6 = 0.0F;
        if(fm.EI.engines[0].tOilOut > 90F)
            f6 = cvt(fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
        else
        if(fm.EI.engines[0].tOilOut < 50F)
            f6 = cvt(fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        else
            f6 = cvt(fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        float f7 = f6 * fm.EI.engines[0].getReadyness() * oilPressure1;
        mesh.chunkSetAngles("Z_Need_OilPressureL", -cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_OilPressureL15", -cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_GasPressureR", -cvt(rpmGeneratedPressure1, 0.0F, 2000F, 0.0F, 200F), 0.0F, 0.0F);
        if(fm.EI.engines[1].tOilOut > 90F)
            f6 = cvt(fm.EI.engines[1].tOilOut, 90F, 110F, 1.1F, 1.5F);
        else
        if(fm.EI.engines[1].tOilOut < 50F)
            f6 = cvt(fm.EI.engines[1].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        else
            f6 = cvt(fm.EI.engines[1].tOilOut, 50F, 90F, 0.9F, 1.1F);
        f7 = f6 * fm.EI.engines[1].getReadyness() * oilPressure2;
        mesh.chunkSetAngles("Z_Need_OilPressureL09", -cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_OilPressureL14", -cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_GasPressureR01", -cvt(rpmGeneratedPressure2, 0.0F, 2000F, 0.0F, 200F), 0.0F, 0.0F);
        if(fm.EI.engines[2].tOilOut > 90F)
            f6 = cvt(fm.EI.engines[2].tOilOut, 90F, 110F, 1.1F, 1.5F);
        else
        if(fm.EI.engines[2].tOilOut < 50F)
            f6 = cvt(fm.EI.engines[2].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        else
            f6 = cvt(fm.EI.engines[2].tOilOut, 50F, 90F, 0.9F, 1.1F);
        f7 = f6 * fm.EI.engines[2].getReadyness() * oilPressure3;
        mesh.chunkSetAngles("Z_Need_OilPressureL10", -cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_OilPressureL13", -cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_GasPressureR02", -cvt(rpmGeneratedPressure3, 0.0F, 2000F, 0.0F, 200F), 0.0F, 0.0F);
        if(fm.EI.engines[3].tOilOut > 90F)
            f6 = cvt(fm.EI.engines[3].tOilOut, 90F, 110F, 1.1F, 1.5F);
        else
        if(fm.EI.engines[3].tOilOut < 50F)
            f6 = cvt(fm.EI.engines[3].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        else
            f6 = cvt(fm.EI.engines[3].tOilOut, 50F, 90F, 0.9F, 1.1F);
        f7 = f6 * fm.EI.engines[3].getReadyness() * oilPressure4;
        mesh.chunkSetAngles("Z_Need_OilPressureL11", -cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_OilPressureL12", -cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_GasPressureR03", -cvt(rpmGeneratedPressure4, 0.0F, 2000F, 0.0F, 200F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Lever01", 0.0F, -gearsLever, 0.0F);
        mesh.chunkSetAngles("Z_Need_Fuel", -cvt(fm.M.fuel, 0.0F, 1500F, 0.0F, 196F), 0.0F, 0.0F);
        mesh.chunkSetAngles("G_Hor_Sph02", 0.0F, -fm.Or.getKren(), 0.0F);
        mesh.chunkSetAngles("G_Hor_Sph01", 0.0F, -fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -35F, 35F, 0.028F, -0.028F);
        mesh.chunkSetLocate("Z_Hor1", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetLocate("Z_Hor2", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Need_ClimbRate", cvt(setNew.vspeed, -30F, 30F, 180F, -180F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_OilTemp_L01", -cvt(fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_OilTemp_L02", -cvt(fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_OilTemp_L04", -cvt(fm.EI.engines[2].tOilOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_OilTemp_L03", -cvt(fm.EI.engines[3].tOilOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_WatT_L01", -cvt(fm.EI.engines[0].tWaterOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_WatT_L02", -cvt(fm.EI.engines[1].tWaterOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_WatT_L04", -cvt(fm.EI.engines[2].tWaterOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_WatT_L03", -cvt(fm.EI.engines[3].tWaterOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_RDF", cvt(setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, 30F, -30F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_clock_minute", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_clock_hour", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_clock_second", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Trim2", 0.0F, 360F * fm.CT.getTrimAileronControl(), 0.0F);
        mesh.chunkSetAngles("TrimElevator", 0.0F, 0.0F, 360F * fm.CT.getTrimElevatorControl());
        mesh.chunkSetAngles("Trimmer3", 0.0F, 0.0F, -360F * fm.CT.getTrimRudderControl());
        mesh.chunkVisible("L_gearUP_L", fm.CT.getGearL() == 0.0F && fm.Gears.lgear);
        mesh.chunkVisible("L_gearUP_R", fm.CT.getGearR() == 0.0F && fm.Gears.rgear);
        mesh.chunkVisible("L_gearDOWN_L", fm.CT.getGearL() == 1.0F && fm.Gears.lgear);
        mesh.chunkVisible("L_gearDOWN_R", fm.CT.getGearR() == 1.0F && fm.Gears.rgear);
        mesh.chunkSetAngles("Lever02", interp(setNew.flaps, setOld.flaps, f) * 20F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_need_blind_V", cvt(setNew.ilsLoc, -63F, 63F, -45F, 45F), 0.0F, 0.0F);
        if(setNew.ilsGS >= 0.0F)
            mesh.chunkSetAngles("Z_need_blind_H", cvt(setNew.ilsGS, 0.0F, 0.5F, 0.0F, 40F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_need_blind_H", cvt(setNew.ilsGS, -0.3F, 0.0F, -40F, 0.0F), 0.0F, 0.0F);
        if(fm.CT.bHasBrakeControl)
        {
            float f5 = fm.CT.getBrake();
            mesh.chunkSetAngles("Z_Need_Air", -cvt(fm.CT.getBrakeR(), 0.0F, 1.5F, 0.0F, 140F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Need_Air2", -cvt(fm.CT.getBrakeL(), 0.0F, 1.5F, 0.0F, 140F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Need_Air_Pressure", -155F + f5 * 40F, 0.0F, 0.0F);
        }
        mesh.chunkSetAngles("Z_Need_Oil_Pressure", 180F, 0.0F, 0.0F);
        mesh.chunkVisible("L_RED", fm.CT.pdiLights == 1);
        mesh.chunkVisible("L_WHITE", fm.CT.pdiLights == 2);
        mesh.chunkVisible("L_GREEN", fm.CT.pdiLights == 3);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("Glass_DM_05", true);
        if((fm.AS.astateCockpitState & 8) != 0)
            mesh.chunkVisible("Glass_DM_06", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("Glass_DM_02", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("Glass_DM_01", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Glass_DM_03", true);
            mesh.chunkVisible("BodyDamage1", true);
            mesh.chunkVisible("gauges1_D0", false);
            mesh.chunkVisible("gauges1_D1", true);
            mesh.chunkVisible("Z_Hor2", false);
            mesh.chunkVisible("Z_Need_ball", false);
            mesh.chunkVisible("Z_Need_Inclinometer", false);
            mesh.chunkVisible("Z_clock_minute", false);
            mesh.chunkVisible("Z_clock_hour", false);
            mesh.chunkVisible("Z_clock_second", false);
            mesh.chunkVisible("Z_Need_Air", false);
            mesh.chunkVisible("Z_Need_Air2", false);
            mesh.chunkVisible("Z_Need_Airspeed01", false);
            mesh.chunkVisible("Z_Need_RDF", false);
            mesh.chunkVisible("G_Hor_Sph01", false);
        }
        if((fm.AS.astateCockpitState & 0x80) != 0)
        {
            mesh.chunkVisible("BodyDamage2", true);
            mesh.chunkVisible("Glass_DM_04", true);
            mesh.chunkVisible("gauges2_D0", false);
            mesh.chunkVisible("gauges2_D1", true);
            mesh.chunkVisible("Z_Need_Alt_Big", false);
            mesh.chunkVisible("Z_Need_Alt_Short", false);
            mesh.chunkVisible("Z_Need_Vacuum", false);
            mesh.chunkVisible("Z_need_blind_V", false);
            mesh.chunkVisible("Z_need_blind_H", false);
            mesh.chunkVisible("Z_Need_Oil_Pressure", false);
            mesh.chunkVisible("Z_Need_NadduvL", false);
            mesh.chunkVisible("Z_Hor1", false);
            mesh.chunkVisible("G_Hor_Sph02", false);
        }
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("Glass_DM_07", true);
            mesh.chunkVisible("BodyDamage1", true);
            mesh.chunkVisible("gauges3_D0", false);
            mesh.chunkVisible("gauges3_D1", true);
            mesh.chunkVisible("Z_Need_ClimbRate", false);
            mesh.chunkVisible("Z_Need_Airspeed", false);
            mesh.chunkVisible("Z_Need_Air_Pressure", false);
            mesh.chunkVisible("Z_Need_RPMShort_L01", false);
            mesh.chunkVisible("Z_Need_RPMlong_L01", false);
            mesh.chunkVisible("Z_Need_Ball2", false);
            mesh.chunkVisible("Z_AutoPilot", false);
            mesh.chunkVisible("Z_AutoPilot01", false);
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("Glass_DM_08", true);
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        mesh.materialReplace("Matt1D0o", mat);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void prepareToEnter()
    {
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(32.4F, -40.4F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 50");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(32.4F, -40.4F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if(!bEntered)
        {
            return;
        } else
        {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(32.4F, -40.4F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            return;
        }
    }

    public void destroy()
    {
        leave();
        super.destroy();
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Antenna_D0", false);
            aircraft().hierMesh().chunkVisible("Interior1_D0", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.0D, -0.14D, -0.02D);
            hookpilot.setTubeSight(point3d);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            leave();
            aircraft().hierMesh().chunkVisible("Antenna_D0", true);
            aircraft().hierMesh().chunkVisible("Interior1_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    public void doToggleAim(boolean flag)
    {
        if(!isFocused())
            return;
        if(isToggleAim() == flag)
            return;
        if(flag)
            prepareToEnter();
        else
            leave();
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private float pictManifold1;
    private float pictManifold2;
    private float pictManifold3;
    private float pictManifold4;
    private float rpmGeneratedPressure1;
    private float rpmGeneratedPressure2;
    private float rpmGeneratedPressure3;
    private float rpmGeneratedPressure4;
    private float oilPressure1;
    private float oilPressure2;
    private float oilPressure3;
    private float oilPressure4;
    private float pictSupc1;
    private float pictSupc2;
    private float pictSupc3;
    private float pictSupc4;
    private float gearsLever;
    private float gears;
    private boolean enteringAim;
    private static final float speedometerScale[] = {
        0.0F, 7F, 11.5F, 42F, 85F, 125.5F, 164.5F, 181F, 198F, 213.5F, 
        230F, 248F, 266F, 289F, 310F, 327F, 346F
    };
    private float saveFov;
    private boolean bEntered;

    static 
    {
        Property.set(CockpitPE_8.class, "normZNs", new float[] {
            1.82F, 0.6F, 1.15F, 1.6F
        });
    }
}
