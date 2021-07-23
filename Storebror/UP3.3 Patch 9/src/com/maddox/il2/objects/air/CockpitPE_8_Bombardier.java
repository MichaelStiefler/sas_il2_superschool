package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitPE_8_Bombardier extends CockpitPilot //CockpitBombardier
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
                if(bEntered)
                {
                    PE_8xyz pe_8xyz = (PE_8xyz)aircraft();
                    float f = pe_8xyz.fSightCurForwardAngle;
                    float f1 = -pe_8xyz.fSightCurSideslip;
                    mesh.chunkSetAngles("BlackBox", f1, 0.0F, -f);
                    HookPilot hookpilot = HookPilot.current;
                    hookpilot.setInstantOrient(-f1, tAim + f, 0.0F);
                }
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                mesh.chunkVisible("Light", externalBombs[7].haveBullets() || externalBombs[9].haveBullets() || externalBombs[6].haveBullets() || externalBombs[8].haveBullets() || externalBombs[21].haveBullets());
                mesh.chunkVisible("Light01", externalBombs[1].haveBullets() || externalBombs[20].haveBullets() || externalBombs[14].haveBullets() || externalBombs[15].haveBullets() || externalBombs[16].haveBullets() || externalBombs[17].haveBullets());
                mesh.chunkVisible("Light09", externalBombs[2].haveBullets() || externalBombs[4].haveBullets() || externalBombs[3].haveBullets() || externalBombs[5].haveBullets() || externalBombs[18].haveBullets());
                mesh.chunkVisible("Light08", externalBombs[0].haveBullets() || externalBombs[19].haveBullets() || externalBombs[10].haveBullets() || externalBombs[11].haveBullets() || externalBombs[12].haveBullets() || externalBombs[13].haveBullets());
                mesh.chunkVisible("Light02", internalBombs[2].haveBullets() || internalBombs[7].haveBullets() || internalBombs[8].haveBullets() || internalBombs[9].haveBullets() || internalBombs[10].haveBullets() || internalBombs[29].haveBullets() || internalBombs[30].haveBullets());
                mesh.chunkVisible("Light03", internalBombs[0].haveBullets() || internalBombs[11].haveBullets() || internalBombs[12].haveBullets() || internalBombs[13].haveBullets() || internalBombs[14].haveBullets());
                mesh.chunkVisible("Light04", internalBombs[23].haveBullets() || internalBombs[24].haveBullets() || internalBombs[25].haveBullets() || internalBombs[26].haveBullets() || internalBombs[33].haveBullets() || internalBombs[34].haveBullets());
                mesh.chunkVisible("Light07", internalBombs[1].haveBullets() || internalBombs[3].haveBullets() || internalBombs[4].haveBullets() || internalBombs[5].haveBullets() || internalBombs[6].haveBullets() || internalBombs[27].haveBullets() || internalBombs[28].haveBullets());
                mesh.chunkVisible("Light05", internalBombs[0].haveBullets() || internalBombs[15].haveBullets() || internalBombs[16].haveBullets() || internalBombs[17].haveBullets() || internalBombs[18].haveBullets());
                mesh.chunkVisible("Light06", internalBombs[19].haveBullets() || internalBombs[20].haveBullets() || internalBombs[21].haveBullets() || internalBombs[22].haveBullets() || internalBombs[31].haveBullets() || internalBombs[32].haveBullets());
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(0.0F);
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), getBeaconDirection());
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

//        float throttle[] = {
//            0.0F, 0.0F, 0.0F, 0.0F
//        };
        float altimeter;
        AnglesFork azimuth;
        float vspeed;
        float turn;
        AnglesFork waypointAzimuth;

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

    private BulletEmitter getBomb(String s)
    {
        BulletEmitter bulletemitter = aircraft().getBulletEmitterByHookName(s);
        return bulletemitter;
    }

    public CockpitPE_8_Bombardier()
    {
        super("3DO/Cockpit/Pe-8_NAV/hier.him", "i16");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        enteringAim = false;
        saveBSFov = 50F;
        bEntered = false;
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
            hooknamed.computePos(this, new Loc(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
//            kAim = loc.getOrient().getKren();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        cockpitNightMats = (new String[] {
            "GP_III", "GP_IV", "GP_RDF", "GP_V", "GP_VI", "TrimBase"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        for(int i = 0; i < externalBombs.length; i++)
        {
            String s = "_ExternalBomb";
            if(i < 10)
                s = s + "0" + (i + 1);
            else
                s = s + (i + 1);
            externalBombs[i] = getBomb(s);
        }

        for(int j = 0; j < internalBombs.length; j++)
        {
            String s1 = "_BombSpawn";
            if(j < 10)
                s1 = s1 + "0" + (j + 1);
            else
                s1 = s1 + (j + 1);
            internalBombs[j] = getBomb(s1);
        }

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
        mesh.chunkVisible("BlackBox", bEntered);
        mesh.chunkVisible("zReticle", bEntered);
        mesh.chunkVisible("zBulb", bEntered);
        mesh.chunkVisible("zRefraction", bEntered);
        if(bEntered)
        {
            resetYPRmodifier();
            float f1 = fm.Or.getKren();
            Cockpit.xyz[0] = cvt(f1, -13F, 13F, 0.23F, -0.23F);
            float f2 = fm.Or.getTangage();
            Cockpit.xyz[1] = cvt(f2, -13F, 13F, -0.23F, 0.23F);
            Cockpit.ypr[0] = cvt(fm.Or.getKren(), -45F, 45F, -180F, 180F);
            mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[0] = cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
            Cockpit.xyz[1] = cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
            mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
        }
        mesh.chunkSetAngles("Z_Need_Alt_Short", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Alt_Big01", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Compass", setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Inclinometer", cvt(setNew.turn, -0.2F, 0.2F, -25F, 25F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_ball", cvt(getBall(8D), -8F, 8F, 13F, -13F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Airspeed", -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeed2KMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_ClimbRate", cvt(setNew.vspeed, -30F, 30F, 180F, -180F), 0.0F, 0.0F);
        mesh.chunkSetAngles("RDF_needle", cvt(setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, 30F, -30F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_clock_minute", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_clock_hour", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_clock_second", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_Thermo", cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -70F, 70F, 55F, -55F), 0.0F, 0.0F);
    }

    public void reflectCockpitState()
    {
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.25D, 0.0D, -0.09D);
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
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter()
    {
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, -90F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov " + saveBSFov);
        Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        hookpilot.setInstantOrient(aAim, tAim, 0.0F);
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    public float[] getBombSightFovs()
    {
        return defaultBSightFoVsOPB;
    }

    private void leave()
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if(!bEntered)
        {
            return;
        } else
        {
            saveBSFov = Main3D.FOVX;
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(0.0F, -90F, 0.0F);
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

    public void destroy()
    {
        super.destroy();
        leave();
    }

    protected void reflectPlaneMats()
    {
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private BulletEmitter internalBombs[] = {
        null, null, null, null, null, null, null, null, null, null, 
        null, null, null, null, null, null, null, null, null, null, 
        null, null, null, null, null, null, null, null, null, null, 
        null, null, null, null, null
    };
    private BulletEmitter externalBombs[] = {
        null, null, null, null, null, null, null, null, null, null, 
        null, null, null, null, null, null, null, null, null, null, 
        null, null
    };
    public Vector3f w;
    private boolean bNeedSetUp;
    private boolean enteringAim;
    private static final float speedometerScale[] = {
        0.0F, 7F, 11.5F, 42F, 85F, 125.5F, 164.5F, 181F, 198F, 213.5F, 
        230F, 248F, 266F, 289F, 310F, 327F, 346F
    };
    private float saveFov;
    private float saveBSFov;
    private final float defaultBSightFoVsOPB[] = {
        15F, 50F, 65F
    };
    private float aAim;
    private float tAim;
//    private float kAim;
    private boolean bEntered;

    static 
    {
        Property.set(CockpitPE_8_Bombardier.class, "normZN", 2.0F);
        Property.set(CockpitPE_8_Bombardier.class, "astatePilotIndx", 3);
        Property.set(CockpitPE_8_Bombardier.class, "aiTuretNum", -2);
        Property.set(CockpitPE_8_Bombardier.class, "weaponControlNum", 3);
    }
}
