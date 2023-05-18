package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
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

public class CockpitR_5_SKIS extends CockpitPilot
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
                if(R_5xyz.bChangedPit)
                {
                    reflectPlaneToModel();
                    R_5xyz.bChangedPit = false;
                }
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
                setNew.prop = (10F * setOld.prop + fm.EI.engines[0].getControlProp()) / 11F;
                setNew.altimeter = fm.getAltitude();
                if(Math.abs(fm.Or.getKren()) < 30F)
                    setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-10F, 10F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                mesh.chunkSetAngles("Turret1AP", 0.0F, aircraft().FM.turret[0].tu[0], 0.0F);
                mesh.chunkSetAngles("Turret1BP", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
                boolean flag = false;
                if(cockpitDimControl)
                {
                    if(setNew.dimPos < 1.0F)
                        setNew.dimPos = setOld.dimPos + 0.03F;
                } else
                if(setNew.dimPos > 0.0F)
                    setNew.dimPos = setOld.dimPos - 0.03F;
                if(flag != sfxPlaying)
                {
                    if(flag)
                        sfxStart(16);
                    else
                        sfxStop(16);
                    sfxPlaying = flag;
                }
            }
            return true;
        }

        boolean sfxPlaying;

        Interpolater()
        {
            sfxPlaying = false;
        }
    }

    private class Variables
    {

        float throttle;
        float prop;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;
        float dimPos;

        private Variables()
        {
            dimPos = 1.0F;
        }

    }


    protected float waypointAzimuth()    {
        WayPoint waypoint = this.fm.AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, this.fm.Loc);
            return (float)(57.295779513082323D * Math.atan2(-tmpV.y, tmpV.x));
        }
    }

    protected void setCameraOffset()
    {
        this.cameraCenter.add(0.045D, 0.0D, 0.0D);
    }

    public CockpitR_5_SKIS()
    {
        super("3DO/Cockpit/R-5/hierR_5.him", "i16");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        hasTubeSight = true;
        bEntered = false;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        enteringAim = false;
        this.cockpitNightMats = (new String[] {
            "prib_one", "prib_one_dd", "prib_two", "prib_two_dd", "prib_three", "prib_three_dd", "prib_four", "prib_four_dd", "shkala", "oxigen"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
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
        if(R_5xyz.bChangedPit)
        {
            reflectPlaneToModel();
            R_5xyz.bChangedPit = false;
        }
        this.mesh.chunkSetAngles("Stick", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl) * 15F, (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("Fire1", 0.0F, -20F * (this.fm.CT.WeaponControl[0] ? 1.0F : 0.0F), 0.0F);
        this.mesh.chunkSetAngles("Thtl", 30F - 57F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Thtl_Rod", -30F + 57F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, cvt(this.fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -90F - interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, floatindex(cvt(this.fm.M.fuel, 0.0F, 190F, 0.0F, 14F), fuelQuantityScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), speedometerScale), 0.0F);
        if((this.fm.AS.astateCockpitState & 4) == 0 && (this.fm.AS.astateCockpitState & 0x10) == 0)
        {
            w.set(this.fm.getW());
            this.fm.Or.transform(w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, cvt(getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zTOilIn1a", 0.0F, cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 125F, 0.0F, 275F), 0.0F);
            this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 275F), 0.0F);
            this.mesh.chunkSetAngles("zPressAir1a", 0.0F, cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 10F, 0.0F, 275F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, cvt(setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        if((this.fm.AS.astateCockpitState & 8) == 0 && (this.fm.AS.astateCockpitState & 0x20) == 0)
        {
            if(this.fm.EI.engines[0].getRPM() > 400F)
                this.mesh.chunkSetAngles("zRPS1a", 0.0F, floatindex(cvt(this.fm.EI.engines[0].getRPM(), 400F, 2200F, 0.0F, 18F), engineRPMScale), 0.0F);
            else
                this.mesh.chunkSetAngles("zRPS1a", 0.0F, cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 400F, 0.0F, 18F), 0.0F);
            this.mesh.chunkSetAngles("zManifold1a", 0.0F, cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
            this.mesh.chunkSetAngles("zTCil1a", 0.0F, cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -75F), 0.0F);
        }
        this.mesh.chunkSetAngles("zClock1a", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zPressOil1a", 0.0F, cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 270F), 0.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red2", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() == 1.0F);
        if(hasTubeSight)
        {
            float f1 = cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -90F);
            this.mesh.chunkSetAngles("Z_sight_cap", 0.0F, f1, 0.0F);
            this.mesh.chunkSetAngles("Z_sight_cap_big", 0.0F, f1, 0.0F);
            this.mesh.chunkSetAngles("Z_sight_cap_inside", 0.0F, f1, 0.0F);
            this.mesh.chunkSetAngles("Z_sight_cap_inside", 0.0F, f1, 0.0F);
            this.mesh.chunkSetAngles("SightCapSwitch", f1 * 0.75F, 0.0F, 0.0F);
            if(f1 <= -88F)
                this.mesh.chunkVisible("Z_sight_cap_inside", false);
            else
                this.mesh.chunkVisible("Z_sight_cap_inside", true);
        } else
        {
            float f2 = cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 90F);
            this.mesh.chunkSetAngles("DarkGlass", 0.0F, f2, 0.0F);
        }
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0 || (this.fm.AS.astateCockpitState & 0x10) != 0)
        {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_dd", true);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zPressOil1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
        }
        if((this.fm.AS.astateCockpitState & 8) != 0 || (this.fm.AS.astateCockpitState & 0x20) != 0)
        {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_dd", true);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zGas1a", false);
        }
        if((this.fm.AS.astateCockpitState & 2) != 0)
        {
            if(!hasTubeSight)
                if(World.Rnd().nextInt(0, 99) < 15);
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if((this.fm.AS.astateCockpitState & 1) != 0)
        {
            if(!hasTubeSight)
                if(World.Rnd().nextInt(0, 99) < 10);
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingLMid_CAP", hiermesh.isChunkVisible("WingLMid_CAP"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        this.mesh.chunkVisible("WingRMid_CAP", hiermesh.isChunkVisible("WingRMid_CAP"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
        this.mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        this.mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
        this.mesh.materialReplace("Matt1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D1o"));
        this.mesh.materialReplace("Matt2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        this.mesh.materialReplace("Matt2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
        this.mesh.materialReplace("Pilot1", mat);
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    public void setTubeSight(boolean flag)
    {
        hasTubeSight = flag;
        this.mesh.chunkVisible("Z_sight_cap", flag);
        this.mesh.chunkVisible("tubeSight", flag);
        this.mesh.chunkVisible("tubeSightLens", flag);
        this.mesh.chunkVisible("tube_inside", flag);
        this.mesh.chunkVisible("TubeSightEyePiece", flag);
        this.mesh.chunkVisible("tube_mask", flag);
        this.mesh.chunkVisible("Z_sight_cap_inside", flag);
        this.mesh.chunkVisible("SightCapSwitch", flag);
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setTubeSight(flag);
    }

    public void destroy()
    {
        leave(false);
        super.destroy();
    }

    public void toggleDim()
    {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            if(!hasTubeSight && bEntered)
                enter();
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            if(hasTubeSight)
            {
                Point3d point3d = new Point3d();
                point3d.set(0.18D, 0.0D, 0.0D);
                hookpilot.setTubeSight(point3d);
            } else
            {
                hookpilot.setAim(refSightAimPoint);
            }
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(isFocused())
        {
            leave(false);
            super.doFocusLeave();
        }
    }

    public void doToggleAim(boolean flag)
    {
        if(isFocused() && isToggleAim() != flag)
            if(flag)
                prepareToEnter();
            else
                leave(true);
    }

    private void leave(boolean flag)
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if(bEntered)
        {
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.doAim(false);
            if(flag)
                bEntered = false;
            if(hasTubeSight)
            {
                Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
                CmdEnv.top().exec("fov " + saveFov);
                hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
                hookpilot1.setSimpleUse(false);
                doSetSimpleUse(false);
                boolean flag1 = HotKeyEnv.isEnabled("aircraftView");
                HotKeyEnv.enable("PanView", flag1);
                HotKeyEnv.enable("SnapView", flag1);
                this.mesh.chunkVisible("superretic", false);
                this.mesh.chunkVisible("Z_sight_cap_big", false);
            }
        }
    }

    private void prepareToEnter()
    {
        if(!hasTubeSight)
        {
            enter();
            return;
        }
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        HookPilot hookpilot = HookPilot.current;
        hookpilot.doAim(true);
        bEntered = true;
        if(!hasTubeSight)
        {
            hookpilot.setAim(refSightAimPoint);
        } else
        {
            hookpilot.setSimpleUse(true);
            doSetSimpleUse(true);
            HotKeyEnv.enable("PanView", false);
            HotKeyEnv.enable("SnapView", false);
        }
    }

    public void doSetSimpleUse(boolean flag)
    {
        super.doSetSimpleUse(flag);
        if(flag)
        {
            saveFov = Main3D.FOVX;
            CmdEnv.top().exec("fov 31");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            this.mesh.chunkVisible("superretic", true);
            this.mesh.chunkVisible("Z_sight_cap_big", true);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private boolean hasTubeSight;
    private boolean bEntered;
    private float saveFov;
    private static final float speedometerScale[] = {
        0.0F, 0.0F, 18F, 44F, 74.5F, 106F, 136.3F, 169.5F, 207.5F, 245F, 
        287.5F, 330F
    };
    private static final float fuelQuantityScale[] = {
        0.0F, 38.5F, 74.5F, 98.5F, 122F, 143F, 163F, 182.5F, 203F, 221F, 
        239.5F, 256F, 274F, 295F, 295F, 295F
    };
    private static final float engineRPMScale[] = {
        18F, 38F, 61F, 81F, 102F, 120F, 137F, 152F, 167F, 183F, 
        198F, 213F, 227F, 241F, 254F, 267F, 280F, 292F, 306F
    };
    private Point3d tmpP;
    private Vector3d tmpV;
    private final Point3d refSightAimPoint = new Point3d(-1.1D, -0.001D, 0.873D);
    private boolean enteringAim;

    static 
    {
        Property.set(CockpitR_5_SKIS.class, "normZN", 1.0F);
        Property.set(CockpitR_5_SKIS.class, "gsZN", 0.95F);
    }
}
