package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitSB_Bombardier extends CockpitPilot //CockpitBombardier
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
                    SB sb = (SB)aircraft();
                    float f = sb.fSightCurForwardAngle + sb.tSAim;
                    float f1 = -sb.fSightCurSideslip;
                    mesh.chunkSetAngles("BlackBox", f1, -sb.kSAim, -f);
                    HookPilot hookpilot = HookPilot.current;
                    hookpilot.setInstantOrient(-f1, tAim + f, 0.0F);
                }
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
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

        private Variables()
        {
            azimuth = new AnglesFork();
        }

    }


    protected float waypointAzimuth()
    {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitSB_Bombardier()
    {
        super("3DO/Cockpit/SB-Bombardier/hier.him", "i16");
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
            HookNamed hooknamed1 = new HookNamed(mesh, "CAMERAAIM");
            hooknamed1.computePos(this, new Loc(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        cockpitNightMats = (new String[] {
            "ACHO_arrow", "Dprib_one", "Dprib_six", "equip_AN4_sh", "prib_one", "Prib_six"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK03");
        Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        light3 = new LightPointActor(new LightPoint(), loc1.getPoint());
        light3.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light3.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK03", light3);
        hooknamed = new HookNamed(mesh, "LAMPHOOK04");
        loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        light4 = new LightPointActor(new LightPoint(), loc1.getPoint());
        light4.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light4.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK04", light4);
    }

    public float[] getBombSightFovs()
    {
        return defaultBSightFoVsOPB;
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
        mesh.chunkVisible("zMark1", bEntered);
        mesh.chunkVisible("zMark2", bEntered);
        mesh.chunkVisible("zBulb", bEntered);
        mesh.chunkVisible("zRefraction", bEntered);
        if(bEntered)
        {
            SB sb = (SB)aircraft();
            mesh.chunkSetAngles("zMark1", sb.fSightCurForwardAngle * 3.675F, 0.0F, 0.0F);
            float f1 = cvt(sb.fSightSetForwardAngle, -15F, 75F, -15F, 75F);
            mesh.chunkSetAngles("zMark2", f1 * 3.675F, 0.0F, 0.0F);
            resetYPRmodifier();
            Cockpit.xyz[0] = cvt(fm.Or.getKren() + sb.kSAim, -13F, 13F, 0.23F, -0.23F);
            Cockpit.xyz[1] = cvt(fm.Or.getTangage() + sb.tSAim, -13F, 13F, -0.23F, 0.23F);
            Cockpit.ypr[0] = cvt(fm.Or.getKren(), -45F, 45F, -180F, 180F);
            mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[0] = cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
            Cockpit.xyz[1] = cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
            mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
        }
        mesh.chunkSetAngles("Z_Opb1a", -((SB)aircraft()).fSightCurSideslip, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Opb1", 0.0F, ((SB)aircraft()).kSAim, ((SB)aircraft()).tSAim);
        mesh.chunkSetAngles("Z_arrow_hour", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_min", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_sec_1", -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_temp", cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -70F, 70F, 52F, -52F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_speed", floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeed2KMH()), 0.0F, 600F, 0.0F, 15F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_alt2", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_alt1", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_bomb_door", (aircraft().getBayDoor() - 0.5F) * -55F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_AN4b", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
    }

    public void reflectCockpitState()
    {
    }

    protected boolean doFocusEnter()
    {
        BulletEmitter abulletemitter[] = aircraft().FM.CT.Weapons[10];
        boolean flag = false;
        if(abulletemitter != null)
        {
            BulletEmitter bulletemitter = abulletemitter[0];
            if(bulletemitter.countBullets() == 0)
                flag = true;
        } else
        {
            flag = true;
        }
        if(flag && World.cur().diffCur.Limited_Ammo)
        {
            for(int i = 1; i <= 10; i++)
            {
                mesh.chunkVisible("ammoL" + i, false);
                mesh.chunkVisible("ammoR" + i, false);
            }

        }
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
        SB sb = (SB)aircraft();
        hookpilot.setSimpleAimOrient(aAim, tAim + sb.tSAim, sb.kSAim);
        hookpilot.setInstantOrient(aAim, tAim + sb.tSAim, sb.kSAim);
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
        {
            light3.light.setEmit(0.3F, 1.4F);
            light4.light.setEmit(0.3F, 1.4F);
            mesh.materialReplace("lamp_n1", "lamp_n1_light");
            mesh.materialReplace("equip_an4_sh", "equip_an4_sh_light");
            mesh.materialReplace("equip_an4", "equip_an4_light");
            mesh.materialReplace("equip_an4_sha", "equip_an4_sha_light");
            mesh.materialReplace("equip_an4a", "equip_an4a_light");
        } else
        {
            light3.light.setEmit(0.0F, 0.0F);
            light4.light.setEmit(0.0F, 0.0F);
            mesh.materialReplace("lamp_n1", "lamp_n1");
            mesh.materialReplace("equip_an4_sh", "equip_an4_sh");
            mesh.materialReplace("equip_an4", "equip_an4");
            mesh.materialReplace("equip_an4_sha", "equip_an4_sha");
            mesh.materialReplace("equip_an4a", "equip_an4a");
        }
        setNightMats(false);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private boolean bNeedSetUp;
    private boolean enteringAim;
    private LightPointActor light3;
    private LightPointActor light4;
    private static final float speedometerScale[] = {
        0.0F, -10F, -19.5F, -32F, -46F, -66.5F, -89F, -114F, -141F, -170.5F, 
        -200.5F, -232.5F, -264F, -295.5F, -328F, -360F
    };
    private float saveFov;
    private float saveBSFov;
    private final float defaultBSightFoVsOPB[] = {
        15F, 50F, 65F
    };
    private float aAim;
    private float tAim;
    private boolean bEntered;

    static 
    {
        Property.set(CockpitSB_Bombardier.class, "normZN", 2.0F);
        Property.set(CockpitSB_Bombardier.class, "astatePilotIndx", 1);
        Property.set(CockpitSB_Bombardier.class, "aiTuretNum", -2);
        Property.set(CockpitSB_Bombardier.class, "weaponControlNum", 3);
    }
}
