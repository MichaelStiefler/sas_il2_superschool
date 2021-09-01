package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.*;
import com.maddox.sound.ReverbFXRoom;

public class CockpitFury_S extends CockpitPilot
{
    private class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.92F * setOld.throttle + 0.08F * fm.CT.PowerControl;
                setNew.prop = 0.92F * setOld.prop + 0.08F * fm.EI.engines[0].getControlProp();
                setNew.mix = 0.92F * setOld.mix + 0.08F * fm.EI.engines[0].getControlMix();
                setNew.altimeter = fm.getAltitude();
                if(Math.abs(fm.Or.getKren()) < 30F)
                    setNew.azimuth = 0.97F * setOld.azimuth + 0.03F * -fm.Or.getYaw();
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = 0.91F * setOld.waypointAzimuth + 0.09F * (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-10F, 10F);
                setNew.vspeed = 0.99F * setOld.vspeed + 0.01F * fm.getVertSpeed();
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
        float prop;
        float mix;
        float altimeter;
        float azimuth;
        float waypointAzimuth;
        float vspeed;

        private Variables()
        {
        }

    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
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
            super.doFocusLeave();
            return;
        }
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 31");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
        this.mesh.chunkVisible("tube_inside", true);
    }

    private void leave()
    {
        if(!bEntered)
        {
            return;
        } else
        {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            this.mesh.chunkVisible("tube_inside", false);
            return;
        }
    }

    public void destroy()
    {
        leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag)
    {
        if(!isFocused())
            return;
        if(isToggleAim() == flag)
            return;
        if(flag)
            enter();
        else
            leave();
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
            return (float)(Math.toDegrees(Math.atan2(-tmpV.y, tmpV.x)));
        }
    }

    public CockpitFury_S()
    {
        super("3DO/Cockpit/Fury/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictBrake = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictManf = 1.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] {
            "COMPASS", "BORT2", "prib_five", "prib_five_damage", "prib_one", "prib_one_damage", "prib_three", "prib_three_damage", "prib_two", "prib_two_damage", 
            "text13", "text15"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(this.acoustics != null)
            this.acoustics.globFX = new ReverbFXRoom(0.2F);
    }

    public void reflectWorldToInstruments(float f)
    {
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        this.mesh.chunkSetLocate("Blister_D0", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("XGlassDamage2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("XGlassDamage3", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("XLampFuel", fm.M.fuel < 0.25F * fm.M.maxFuel);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 8F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Column", -30F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang01a", -5F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang01b", -9F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang01c", -12F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang02a", -5F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang02b", -7.5F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang02c", -15F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang03a", -5F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang03b", -8.5F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang03c", -18.5F * pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick_Shtok01", 0.0F, 0.0F, 8F * pictElev);
        this.mesh.chunkSetAngles("Z_ColumnSwitch", -18F * (pictBrake = 0.89F * pictBrake + 0.11F * fm.CT.BrakeControl), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_Button_Gun", !fm.CT.WeaponControl[0]);
        this.mesh.chunkSetAngles("Z_Throtle1", -64.5454F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BasePedal", 20F * fm.CT.getRudder(), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = 0.0578F * fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -0.0578F * fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Gear1", -160F + 160F * (pictGear = 0.89F * pictGear + 0.11F * fm.CT.GearControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 160F * (pictFlap = 0.89F * pictFlap + 0.11F * fm.CT.FlapsControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim1", 1000F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 1000F * fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", -90F * setNew.prop, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", -60F * setNew.mix, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("COMPASS_M", -interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SHKALA_DIRECTOR", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_LONG", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        this.mesh.chunkSetAngles("STREL_ALT_SHORT", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        this.mesh.chunkSetAngles("STREL_ALT_SHRT1", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -108F));
        this.mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -cvt(pictManf = 0.91F * pictManf + 0.09F * fm.EI.engines[0].getManifoldPressure(), 0.5173668F, 2.72369F, -70F, 250F));
        this.mesh.chunkSetAngles("STRELKA_FUEL", 0.0F, 0.0F, cvt(fm.M.fuel, 0.0F, 378.54F, 0.0F, 68F));
        this.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -floatindex(cvt(fm.EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), rpmScale));
        this.mesh.chunkSetAngles("STRELK_TEMP_OIL", 0.0F, 0.0F, -cvt(fm.EI.engines[0].tOilOut, 50F, 100F, 0.0F, 271F));
        this.mesh.chunkSetAngles("STRELK_TEMP_RAD", 0.0F, 0.0F, -floatindex(cvt(fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), radScale));
        this.mesh.chunkSetAngles("STR_OIL_LB", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, -37F), 0.0F);
        w.set(this.fm.getW());
        fm.Or.transform(w);
        this.mesh.chunkSetAngles("STREL_TURN_DOWN", 0.0F, 0.0F, -cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, -48F, 48F));
        this.mesh.chunkSetAngles("STRELK_TURN_UP", 0.0F, 0.0F, -cvt(getBall(8D), -8F, 8F, 35F, -35F));
        this.mesh.chunkVisible("STRELK_V_SHORT", false);
        this.mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, this.fm.getSpeed()), 0.0F, 223.52F, 0.0F, 25F), speedometerScale));
        this.mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale));
        this.mesh.chunkSetAngles("STRELKA_GOR", 0.0F, 0.0F, fm.Or.getKren());
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.032F, -0.032F);
        this.mesh.chunkSetLocate("STRELKA_GOS", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("STRELKA_HOUR", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("STRELKA_MINUTE", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("STRELKA_SECUND", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("STR_CLIMB", 0.0F, 0.0F, cvt(fm.CT.trimElevator, -0.5F, 0.5F, -35.23F, 35.23F));
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 0x80) != 0)
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictBrake;
    private float pictFlap;
    private float pictGear;
    private float pictManf;
    private float saveFov;
    private boolean bEntered;
    private static final float speedometerScale[] = {
        0.0F, 7.5F, 17.5F, 37F, 63F, 88.5F, 114.5F, 143F, 171.5F, 202.5F, 
        228.5F, 255.5F, 282F, 309F, 336.5F, 366.5F, 394F, 421F, 448.5F, 474.5F, 
        500.5F, 530F, 557.5F, 584F, 609F, 629F
    };
    private static final float radScale[] = {
        0.0F, 3F, 7F, 13.5F, 21.5F, 27F, 34.5F, 50.5F, 71F, 94F, 
        125F, 161F, 202.5F, 253F, 315.5F
    };
    private static final float rpmScale[] = {
        0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F, 
        311.5F
    };
    private static final float variometerScale[] = {
        -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F
    };
    private Point3d tmpP;
    private Vector3d tmpV;
}