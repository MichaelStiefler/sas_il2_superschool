package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitR_5 extends CockpitPilot
{
    private class Variables
    {

        float throttle1;
        float mix1;
        float altimeter;
        float turn;
        float compasTangage;
        float compasKren;
        float azimuth;
        float pictElev;
        float pictAiler;
        float pictRudd;
        float dimPos;
        float Airstartr;

        private Variables()
        {
        }

    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.pictElev = 0.8F * setOld.pictElev + 0.2F * fm.CT.ElevatorControl;
                setNew.pictAiler = 0.8F * setOld.pictAiler + 2.0F * fm.CT.AileronControl;
                setNew.pictRudd = 0.8F * setOld.pictRudd + 4F * fm.CT.getRudder();
                setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
                setNew.mix1 = 0.8F * setOld.mix1 + 0.2F * fm.EI.engines[0].getControlMix();
                setNew.altimeter = fm.getAltitude();
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                if(Math.abs(fm.Or.getKren()) < 20F && Math.abs(fm.Or.getTangage()) < 20F)
                    setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.compasTangage = 0.95F * setOld.compasTangage + 0.05F * cvt(fm.Or.getTangage(), -20F, 20F, -20F, 20F);
                setNew.compasKren = 0.95F * setOld.compasKren + 0.05F * cvt(fm.Or.getKren(), -20F, 20F, -20F, 20F);
                if(cockpitDimControl)
                {
                    if(setNew.dimPos < 1.0F)
                        setNew.dimPos = setOld.dimPos + 0.05F;
                } else
                if(setNew.dimPos > 0.0F)
                    setNew.dimPos = setOld.dimPos - 0.05F;
            }
            if(fm.EI.engines[0].getStage() == 1 || fm.EI.engines[0].getStage() == 2)
            {
                if(setNew.Airstartr < 1.0F)
                    setNew.Airstartr = setOld.Airstartr + 0.1F;
                if(setNew.Airstartr > 1.0F)
                    setNew.Airstartr = 1.0F;
            } else
            {
                if(setNew.Airstartr > 0.0F)
                    setNew.Airstartr = setOld.Airstartr - 0.1F;
                if(setNew.Airstartr < 0.0F)
                    setNew.Airstartr = 0.0F;
            }
            R_5xyz r_5xyz = (R_5xyz)aircraft();
            if((double)r_5xyz.getGunnerAnimation() < 1.0D)
            {
                moveGunner();
            } else
            {
                mesh.chunkSetAngles("zTurret1A", aircraft().FM.turret[0].tu[0], 0.0F, 0.0F);
                mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -aircraft().FM.turret[0].tu[1]);
                mesh.chunkSetAngles("zTurret1B_Flug", aircraft().FM.turret[0].tu[0], 0.0F, 0.0F);
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    private void moveGunner()
    {
        R_5xyz r_5xyz = (R_5xyz)aircraft();
        if(r_5xyz.gunnerDead || r_5xyz.gunnerEjected)
            return;
        if((double)r_5xyz.getGunnerAnimation() > 0.5D)
        {
            mesh.chunkVisible("Pilot2_D0", true);
            mesh.chunkVisible("Pilot3_D0", false);
            float f = 120F * (r_5xyz.getGunnerAnimation() - 0.5F) - 60F;
            mesh.chunkSetAngles("Pilot2_D0", 0.0F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1A", f, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", -f, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B_Flug", 0.0F, 0.0F, 0.0F);
        } else
        if((double)r_5xyz.getGunnerAnimation() > 0.25D)
        {
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = (r_5xyz.getGunnerAnimation() - 0.5F) * 0.5F;
            Cockpit.ypr[0] = -120F + 480F * (r_5xyz.getGunnerAnimation() - 0.25F);
            Cockpit.ypr[1] = 0.0F;
            Cockpit.ypr[2] = 0.0F;
            mesh.chunkSetLocate("Pilot2_D0", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkVisible("Pilot2_D0", true);
            mesh.chunkVisible("Pilot3_D0", false);
            mesh.chunkSetAngles("zTurret1A", -60F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 60F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B_Flug", 0.0F, 0.0F, 0.0F);
        } else
        {
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = r_5xyz.getGunnerAnimation() * 0.5F;
            Cockpit.ypr[0] = 0.0F;
            Cockpit.ypr[1] = 0.0F;
            Cockpit.ypr[2] = 0.0F;
            mesh.chunkSetLocate("Pilot3_D0", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkVisible("Pilot2_D0", false);
            mesh.chunkVisible("Pilot3_D0", true);
            mesh.chunkSetAngles("zTurret1A", -60F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 60F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B_Flug", 0.0F, 0.0F, 0.0F);
        }
    }

    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(30F);
    }

    protected void setCameraOffset()
    {
        cameraCenter.add(0.0D, 0.0D, 0.0D);
    }

    public CockpitR_5()
    {
        super("3DO/Cockpit/R-5/hier.him", "u2");
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        cockpitNightMats = new String[0];
        for(int i = 0; i < 2; i++)
        {
            HookNamed hooknamed = new HookNamed(mesh, "LAMP0" + (i + 1));
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
            lights[i] = new LightPointActor(new LightPoint(), loc.getPoint());
            lights[i].light.setColor(0.8980392F, 0.8117647F, 0.6235294F);
            lights[i].light.setEmit(0.0F, 0.0F);
            pos.base().draw.lightMap().put("LAMP0" + (i + 1), lights[i]);
        }

        setNightMats(false);
        interpPut(new Interpolater(), (Object)null, Time.current(), (Message)null);
        tros1 = 0.5F;
        tros1Mat = null;
        int j = -1;
        j = mesh.materialFind("chain");
        if(j != -1)
        {
            tros1Mat = mesh.material(j);
            tros1Mat.setLayer(0);
        }
        limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.03F
        });
//        hidePilot = true;
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
            bNeedSetUp = true;
            moveGunner();
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Interior_D0", true);
        HierMesh hiermesh = aircraft().hierMesh();
        R_5xyz r_5xyz = (R_5xyz)aircraft();
        if((double)r_5xyz.getGunnerAnimation() < 1.0D)
        {
            hiermesh.chunkVisible("Turret1A_FAKE", true);
            hiermesh.chunkVisible("Turret1B_FAKE", true);
            hiermesh.chunkVisible("Turret1A_D0", false);
            hiermesh.chunkVisible("Turret1C_D0", false);
        } else
        {
            hiermesh.chunkVisible("Turret1A_FAKE", false);
            hiermesh.chunkVisible("Turret1B_FAKE", false);
            hiermesh.chunkVisible("Turret1A_D0", true);
            hiermesh.chunkVisible("Turret1C_D0", true);
        }
        super.doFocusLeave();
    }

    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
        mesh.materialReplace("Matt1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        mesh.materialReplace("Matt2D0o", mat);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2"));
        mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        mesh.chunkVisible("WingRMid_CAP", hiermesh.isChunkVisible("WingRMid_CAP"));
        mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        mesh.chunkVisible("WingLMid_CAP", hiermesh.isChunkVisible("WingLMid_CAP"));
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
//        R_5xyz r_5xyz = (R_5xyz)aircraft();
        if(R_5xyz.bChangedPit)
        {
            reflectPlaneToModel();
//            R_5xyz r_5xyz1 = (R_5xyz)aircraft();
            R_5xyz.bChangedPit = false;
        }
        float f1 = setNew.pictRudd;
        mesh.chunkSetAngles("Ped_Base", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("PedalL", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("PedalR", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Ped_trossL", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Ped_trossR", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("StckAiler", setNew.pictAiler, 0.0F, 0.0F);
        float f2 = 0.0F;
        if(setNew.pictElev > 0.0F)
            f2 = 21F * setNew.pictElev;
        else
            f2 = 13F * setNew.pictElev;
        mesh.chunkSetAngles("Stick", f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("StckConnctr", 0.0F, 0.0F, -f2);
        mesh.chunkSetAngles("Fire1", fm.CT.saveWeaponControl[0] ? -15F : 0.0F, 0.0F, 0.0F);
        float f3 = -50F * setNew.throttle1;
        mesh.chunkSetAngles("zThrotle", 0.0F, 0.0F, f3);
        mesh.chunkSetAngles("Cable_throtle01", 0.0F, 0.0F, -f3);
        mesh.chunkSetAngles("Cable_throtle02", 0.0F, 0.0F, -f3);
        float f4 = -37.5F * setNew.mix1;
        mesh.chunkSetAngles("zMixture", 0.0F, 0.0F, f4);
        mesh.chunkSetAngles("Cable_mixture01", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Cable_mixture02", 0.0F, 0.0F, -f4);
        float f5 = cvt(fm.EI.engines[0].getRPM(), 900F, 1200F, 0.0F, -50F);
        mesh.chunkSetAngles("zIgnition", 0.0F, 0.0F, f5);
        mesh.chunkSetAngles("Cable_Ignition", 0.0F, 0.0F, -f5);
        mesh.chunkSetAngles("zSW_Magneto", 0.0F, cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -90F), 0.0F);
        mesh.chunkSetAngles("zArr_KI6a", 0.0F, -setNew.compasKren, -setNew.compasTangage);
        mesh.chunkSetAngles("zArr_KI6b", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_AL2a", 0.0F, -setNew.compasKren, -setNew.compasTangage);
        mesh.chunkSetAngles("Z_AL2b", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        R_5xyz r_5xyz2 = (R_5xyz)aircraft();
        mesh.chunkSetAngles("Z_AL2c", r_5xyz2.CompassDelta, 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Speed", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), IAS_Scale), 0.0F);
        float f6 = getBall(8D);
        mesh.chunkSetAngles("zArr_PioneerBall", 0.0F, cvt(f6, -4F, 4F, -11F, 11F), 0.0F);
        f6 = cvt(setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        mesh.chunkSetAngles("zArr_Pioneer", 0.0F, f6, 0.0F);
        mesh.chunkSetAngles("zArr_ClockS", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zArr_ClockM", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zArr_ClockH", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zArr_RPM", 0.0F, floatindex(cvt(fm.EI.engines[0].getRPM(), 400F, 2200F, 0.0F, 9F), RPM_Scale), 0.0F);
        mesh.chunkSetAngles("zArr_Alt", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 12000F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zArr_Fuel", 0.0F, cvt(fm.M.fuel, 0.0F, 200F, 0.0F, 303.3F), 0.0F);
        float f7 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 800F, 0.0F, 5F) - cvt(fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 0.5F);
        mesh.chunkSetAngles("zArr_OilPress", 0.0F, cvt(f7 * fm.EI.engines[0].getReadyness(), 0.0F, 10F, 0.0F, 265F), 0.0F);
        float f8 = 0.0F;
        if(fm.M.fuel > 382.5F)
            f8 = 0.9F;
        else
        if(fm.M.fuel > 1.0F)
            f8 = 0.25F;
        mesh.chunkSetAngles("zArr_FuelPress", 0.0F, cvt(f8, 0.0F, 1.0F, 0.0F, 266.7F), 0.0F);
        mesh.chunkSetAngles("zArr_WaterTemp", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 125F, 0.0F, 285.5F), 0.0F);
        mesh.chunkSetAngles("zArr_BrakePress", 0.0F, 122F * fm.CT.getBrake(), 0.0F);
        float f9 = 2.0F * fm.CT.getTrimElevatorControl();
        if(f9 > 0.0F)
        {
            mesh.chunkSetAngles("Z_Trim", 1324F * f9, 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_Stab", 0.0F, 110F * f9, 0.0F);
        } else
        {
            mesh.chunkSetAngles("Z_Trim", 1080F * f9, 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_Stab", 0.0F, 90F * f9, 0.0F);
        }
        mesh.chunkSetAngles("zArr_OilTemp", 0.0F, floatindex(cvt(fm.EI.engines[0].tOilOut, 40F, 125F, 0.0F, 17F), Oil_Scale), 0.0F);
        float f10 = cvt(fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, 700F);
        mesh.chunkSetAngles("Z_Radiator", f10, 0.0F, 0.0F);
        if(tros1Mat != null)
        {
            tros1 = 6F * fm.EI.engines[0].getControlRadiator();
            tros1Mat.setLayer(0);
            tros1Mat.set((byte)12, tros1);
        }
        mesh.chunkSetAngles("GS_Cap", 0.0F, 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F));
        mesh.chunkSetAngles("GS_Tinter", 0.0F, 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 0.69F, 0.0F, -90F));
        mesh.chunkSetAngles("GS_Spring", cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_AirPress250", 0.0F, 0.0F, 153F);
        mesh.chunkSetAngles("zAirstartr", 0.0F, 0.0F, -90F * setNew.Airstartr);
        if(fm.EI.engines[0].getStage() == 1 || fm.EI.engines[0].getStage() == 2)
            mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, -245F * setNew.Airstartr + 5F * World.Rnd().nextFloat());
        else
            mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, 0.0F);
        mesh.chunkSetAngles("zFireCrane", 0.0F, fm.AS.astateEngineStates[0] <= 2 ? 90F : 0.0F, 0.0F);
        mesh.chunkSetAngles("zSW_ANO", 0.0F, 0.0F, fm.AS.bNavLightsOn ? -40F : 0.0F);
        mesh.chunkSetAngles("zSW_Fara", 0.0F, 0.0F, fm.AS.bLandingLightOn ? -40F : 0.0F);
        mesh.chunkSetAngles("zSW_Fuel1", 0.0F, fm.M.fuel <= 382.5F ? 0.0F : -90F, 0.0F);
        mesh.chunkSetAngles("zSW_Fuel2", 0.0F, fm.M.fuel > 382.5F ? 0.0F : -90F, 0.0F);
        HierMesh hiermesh = aircraft().hierMesh();
        boolean flag = hiermesh.isChunkVisible("Pilot2_D0");
        if(!flag)
            flag = hiermesh.isChunkVisible("Pilot2_FAKE");
        mesh.chunkVisible("Pilot2_D0", flag);
        mesh.chunkVisible("Pilot3_D0", hiermesh.isChunkVisible("Pilot3_D0"));
        mesh.chunkVisible("Pilot3_D1", hiermesh.isChunkVisible("Pilot3_D1"));
        boolean flag1 = hiermesh.isChunkVisible("HMask2_D0");
        if(!flag1)
            flag1 = hiermesh.isChunkVisible("HMask2_FAKE");
        mesh.chunkVisible("HMask2_D0", flag1);
        mesh.chunkVisible("HMask3_D0", hiermesh.isChunkVisible("HMask3_D0"));
        hiermesh.chunkVisible("Turret1A_D0", false);
        hiermesh.chunkVisible("Turret1C_D0", false);
        hiermesh.chunkVisible("Turret1A_FAKE", false);
        hiermesh.chunkVisible("Turret1B_FAKE", false);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("xHullDm1", true);
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Alt", false);
                mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
            }
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Pioneer", false);
                mesh.chunkVisible("zArr_PioneerBall", false);
                mesh.materialReplace("Prib_Peoneer", "Prib_Peoneer_dmg");
            }
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_WaterTemp", false);
                mesh.materialReplace("Prib_Water125", "Prib_Water125_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_RPM", false);
                mesh.materialReplace("Prib_Prib22", "Prib_Prib22_dmg");
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_Stab", false);
                mesh.materialReplace("Prib_Stab6", "Prib_Stab6_dmg");
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_FuelPress", false);
                mesh.materialReplace("Prib_Fuel1", "Prib_Fuel1_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("xGlassDm3", true);
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_ClockH", false);
                mesh.chunkVisible("zArr_ClockM", false);
                mesh.chunkVisible("zArr_ClockS", false);
                mesh.materialReplace("Prib_Clock1", "Prib_Clock1_dmg");
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_Fuel", false);
                mesh.materialReplace("Prib_G20", "Prib_G20_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("xGlassDm2", true);
            mesh.materialReplace("Prib_Kpa3Alt", "Prib_Kpa3Alt_dmg");
            if(World.Rnd().nextFloat() < 0.75F)
                mesh.materialReplace("Prib_Kpa3Pressure", "Prib_Kpa3Pressure_dmg");
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_OilPress", false);
                mesh.materialReplace("Prib_M10", "Prib_M10_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("xGlassDm1", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("xHullDm2", true);
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Speed", false);
                mesh.materialReplace("Prib_Prib40", "Prib_Prib40_dmg");
            }
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_OilTemp", false);
                mesh.materialReplace("Prib_OilTemp125", "Prib_OilTemp125_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("xHullDm3", true);
            mesh.chunkVisible("zArr_AirPress25", false);
            mesh.materialReplace("Prib_Oxy25", "Prib_Oxy25_dmg");
            mesh.chunkVisible("zArr_AirPress250", false);
            mesh.materialReplace("Prib_Oxy250", "Prib_Oxy250_dmg");
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_BrakePress", false);
                mesh.materialReplace("Prib_M25", "Prib_M25_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("xOilSplats_D1", true);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            for(int i = 0; i < 2; i++)
                lights[i].light.setEmit(0.5F, 0.7F);

            mesh.materialReplace("AL2_add", "AL2_add_light");
            mesh.materialReplace("AL2_na", "AL2_na_light");
            mesh.chunkSetAngles("zSW_CockLight", 0.0F, 70F, 0.0F);
        } else
        {
            for(int j = 0; j < 2; j++)
                lights[j].light.setEmit(0.0F, 0.0F);

            mesh.materialReplace("AL2_add", "AL2_add");
            mesh.materialReplace("AL2_na", "AL2_na");
            mesh.chunkSetAngles("zSW_CockLight", 0.0F, 0.0F, 0.0F);
        }
        setNightMats(false);
    }

    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private LightPointActor lights[] = {
        null, null
    };
    private static final float IAS_Scale[] = {
        0.0F, 13.7F, 29.5F, 49.5F, 72.2F, 98.8F, 127.09F, 161.5F, 192.5F, 229F, 
        262.6F, 299F, 335.29F, 370.29F, 405.29F, 439.59F, 471.29F, 504.69F
    };
    private static final float RPM_Scale[] = {
        0.0F, 30F, 68.8F, 114.8F, 160.9F, 200.4F, 236F, 263.9F, 288.1F, 307F
    };
    private static final float Oil_Scale[] = {
        0.0F, 5.4F, 11.3F, 20.9F, 31.1F, 42.1F, 53.3F, 66.7F, 81.7F, 96.7F, 
        115.3F, 130.6F, 147.89F, 173.69F, 197.4F, 223.6F, 253.1F, 286F
    };
    private float tros1;
    private Mat tros1Mat;

    static 
    {
        Property.set(CockpitR_5.class, "normZNs", new float[] {
            1.9F, 0.7F, 0.7F, 0.7F
        });
    }







}
