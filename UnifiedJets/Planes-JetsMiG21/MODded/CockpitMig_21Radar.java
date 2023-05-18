// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/10/2015 07:10:40 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitMig_21Radar.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapExt;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Aircraft, AircraftLH

public class CockpitMig_21Radar extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                long l = Time.current();
                long l1 = (long)((float)l / BDiv);
                if(l1 != tBOld)
                {
                    tBOld = l1;
                    resetYPRmodifier();
                    float f = (l % (long)BRefresh) / (long)BRefresh;
                    Cockpit.xyz[0] = (2.0F * f - 1.0F) * BRange;
                    mesh.chunkSetLocate("R-RadarBeam", Cockpit.xyz, Cockpit.ypr);
                }
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            Aircraft aircraft = aircraft();
            Main3D main3d = Main3D.cur3D();
            viewDX = main3d.renderHUD.getViewPortWidth();
            viewDY = main3d.renderHUD.getViewPortHeight();
            if(hookpilot.isPadlock())
                hookpilot.stopPadlock();
            hookpilot.reset();
            enter();
            go_top();
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
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
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
            return;
        }
    }

    private void go_top()
    {
        bBAiming = false;
        CmdEnv.top().exec("fov 43.2");
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
    }

    private void go_bottom()
    {
        bBAiming = true;
        CmdEnv.top().exec("fov 23.913");
        HookPilot hookpilot = HookPilot.current;
        hookpilot.reset();
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.setSimpleUse(false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    public void destroy()
    {
        super.destroy();
        leave();
    }

    public void doToggleAim(boolean flag)
    {
        if(!isFocused())
            return;
        if(isToggleAim() == flag)
            return;
        else
            return;
    }

    public CockpitMig_21Radar()
    {
        super("3DO/Cockpit/R-Izumrud/Izumrud.him", "He111");
        FOV = 1.0D;
        ScX = 0.0099999997764825821D;
        ScY = 0.0099999997764825821D;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 12000F;
        RClose = 5F;
        ElevationMaxPositive = 26F;
        ElevationMinNegative = 16F;
        BRange = 0.1F;
        BRefresh = 1300;
        BSteps = 20;
        BDiv = BRefresh / BSteps;
        tBOld = 0L;
        radarPlane = new ArrayList();
        bEntered = false;
        bBAiming = false;
    }

    public void reflectWorldToInstruments(float f)
    {
        draw();
    }

    public void draw()
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            if(!Mission.isNet() && Actor.isValid(aircraft) && Actor.isAlive(aircraft))
            {
                long l = (Time.current() % 1000L) / 500L;
                if(l != to)
                {
                    to = l;
                    Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
                    Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
                    double d = 10D;
                    radarPlane.clear();
                    List list = Engine.targets();
                    int j1 = list.size();
                    for(int k1 = 0; k1 < j1; k1++)
                    {
                        Actor actor = (Actor)list.get(k1);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft())
                        {
                            Point3d point3d1 = new Point3d();
                            point3d1.set(actor.pos.getAbsPoint());
                            point3d1.sub(point3d);
                            orient.transformInv(point3d1);
                            if(((Tuple3d) (point3d1)).x > (double)RClose && ((Tuple3d) (point3d1)).x < (double)RRange)
                                radarPlane.add(point3d1);
                        }
                    }

                }
                int i = radarPlane.size();
                int j = 0;
                for(int k = 0; k < i; k++)
                {
                    double d1 = ((Tuple3d) ((Point3d)radarPlane.get(k))).x;
                    if(d1 > (double)RClose && j <= nTgts)
                    {
                        double d2 = ((Tuple3d) ((Point3d)radarPlane.get(k))).z * FOV;
                        if(d2 < (double)ElevationMaxPositive && d2 > (double)ElevationMinNegative)
                        {
                            FOV = 60D / d1;
                            double d3 = -((Tuple3d) ((Point3d)radarPlane.get(k))).y * FOV;
                            double d4 = ((Tuple3d) ((Point3d)radarPlane.get(k))).x * FOV;
                            float f = FOrigX + (float)(d3 * ScX);
                            float f1 = FOrigY + (float)(d4 * ScY);
                            j++;
                            String s1 = "R-Signal" + j;
                            super.mesh.setCurChunk(s1);
                            super.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                            resetYPRmodifier();
                            Cockpit.xyz[0] = -f;
                            Cockpit.xyz[2] = f1;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            if(!super.mesh.isChunkVisible(s1))
                                super.mesh.chunkVisible(s1, true);
                        }
                    }
                }

                for(int i1 = j + 1; i1 <= nTgts; i1++)
                {
                    String s = "R-Signal" + i1;
                    if(super.mesh.isChunkVisible(s))
                        super.mesh.chunkVisible(s, false);
                }

            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void initDashBoard()
    {
        spriteLeft = Mat.New("gui/game/hud/hudleft.mat");
        spriteRight = Mat.New("gui/game/hud/hudright.mat");
        meshNeedle1 = new Mesh("gui/game/hud/needle1/mono.sim");
        meshNeedle2 = new Mesh("gui/game/hud/needle2/mono.sim");
        meshNeedle3 = new Mesh("gui/game/hud/needle3/mono.sim");
        meshNeedle4 = new Mesh("gui/game/hud/needle4/mono.sim");
        meshNeedle5 = new Mesh("gui/game/hud/needle5/mono.sim");
        meshNeedle6 = new Mesh("gui/game/hud/needle6/mono.sim");
        meshNeedleMask = new Mesh("gui/game/hud/needlemask/mono.sim");
        fntLcd = TTFont.get("lcdnova");
        setScales();
    }

    private void setScales()
    {
        float f = viewDX;
        float f1 = f / 1024F;
        meshNeedle1.setScale(140F * f1);
        meshNeedle2.setScale(140F * f1);
        meshNeedle3.setScale(75F * f1);
        meshNeedle4.setScale(100F * f1);
        meshNeedle5.setScale(90F * f1);
        meshNeedle6.setScale(150F * f1);
        meshNeedleMask.setScale(150F * f1);
    }

    public void CockpitBF_110G_Gunner()
    {
        bNeedSetUp = true;
        bAiming = true;
        ObserverHook = null;
        hook1 = null;
        hook2 = null;
        super.cockpitNightMats = (new String[] {
            "cadran1", "radio", "bague2"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        HookNamed hooknamed = new HookNamed(super.mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(126F, 232F, 245F);
        light1.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        ObserverHook = new HookNamed(super.mesh, "CAMERAAIM");
        AircraftLH.printCompassHeading = true;
        bBeaconKeysEnabled = ((AircraftLH)aircraft()).bWantBeaconKeys;
        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
    }

//    static Class _mthclass$(String s)
//    {
//        return Class.forName(s);
//        ClassNotFoundException classnotfoundexception;
//        classnotfoundexception;
//        throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//    }

    private int viewDX;
    private int viewDY;
    private Mat spriteLeft;
    private Mat spriteRight;
    private Mesh meshNeedle1;
    private Mesh meshNeedle2;
    private Mesh meshNeedle3;
    private Mesh meshNeedle4;
    private Mesh meshNeedle5;
    private Mesh meshNeedle6;
    private Mesh meshNeedleMask;
    private TTFont fntLcd;
    private Hook hook1;
    private Hook hook2;
    private boolean bNeedSetUp;
    private LightPointActor light1;
    private boolean bAiming;
    private Hook ObserverHook;
    private boolean bBeaconKeysEnabled;
    private float saveFov;
    private float aAim;
    private float tAim;
    private float aDiv;
    private float tDiv;
    private boolean bEntered;
    private boolean bBAiming;
    private long to;
    double FOV;
    double ScX;
    double ScY;
    double ScZ;
    float FOrigX;
    float FOrigY;
    int nTgts;
    float RRange;
    float RClose;
    float ElevationMaxPositive;
    float ElevationMinNegative;
    float BRange;
    int BRefresh;
    int BSteps;
    float BDiv;
    long tBOld;
    private ArrayList radarPlane;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitMig_21Radar.class, "astatePilotIndx", 0);
    }
}
