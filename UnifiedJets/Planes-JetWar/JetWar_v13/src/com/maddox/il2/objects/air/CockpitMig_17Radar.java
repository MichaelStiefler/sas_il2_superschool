package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import java.util.ArrayList;
import java.util.List;

public class CockpitMig_17Radar extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                long t = Time.current();
                long tB = (long)((float)t / BDiv);
                if(tB != tBOld)
                {
                    tBOld = tB;
                    resetYPRmodifier();
                    float x = (t % (long)BRefresh) / (long)BRefresh;
                    Cockpit.xyz[0] = (2.0F * x - 1.0F) * BRange;
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

    public CockpitMig_17Radar()
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
        BRange = 0.1F;
        BRefresh = 1300;
        BSteps = 12;
        BDiv = BRefresh / BSteps;
        tBOld = 0L;
        radarPlane = new ArrayList();
        bEntered = false;
        bBAiming = false;
    }

    public void reflectWorldToInstruments(float f0)
    {
        draw();
    }

    public void draw()
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            if(!Mission.isNet() && Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                long ti = (Time.current() % 1000L) / 500L;
                if(ti != to)
                {
                    to = ti;
                    Point3d pointAC = ownaircraft.pos.getAbsPoint();
                    Orient orientAC = ownaircraft.pos.getAbsOrient();
                    radarPlane.clear();
                    List list = Engine.targets();
                    int i = list.size();
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if(!(actor instanceof Aircraft) || actor == World.getPlayerAircraft())
                            continue;
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(actor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange)
                            radarPlane.add(pointOrtho);
                    }

                }
                int i = radarPlane.size();
                int nt = 0;
                for(int j = 0; j < i; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;
                    if(x <= (double)RClose || nt > nTgts)
                        continue;
                    FOV = 26D / x;
                    double NewX = -((Tuple3d) ((Point3d)radarPlane.get(j))).y * FOV;
                    double NewY = ((Tuple3d) ((Point3d)radarPlane.get(j))).z * FOV;
                    float f = FOrigX + (float)(NewX * ScX);
                    float f1 = FOrigY + (float)(NewY * ScY);
                    nt++;
                    String m = "R-Signal" + nt;
                    mesh.setCurChunk(m);
                    mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                    resetYPRmodifier();
                    xyz[0] = -f;
                    xyz[2] = f1;
                    mesh.chunkSetLocate(xyz, ypr);
                    mesh.render();
                    if(!mesh.isChunkVisible(m))
                        mesh.chunkVisible(m, true);
                }

                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "R-Signal" + j;
                    if(mesh.isChunkVisible(m))
                        mesh.chunkVisible(m, false);
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
        cockpitNightMats = (new String[] {
            "cadran1", "radio", "bague2"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(126F, 232F, 245F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        ObserverHook = new HookNamed(mesh, "CAMERAAIM");
        AircraftLH.printCompassHeading = true;
        bBeaconKeysEnabled = ((AircraftLH)aircraft()).bWantBeaconKeys;
        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
    }

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
    float BRange;
    int BRefresh;
    int BSteps;
    float BDiv;
    long tBOld;
    private ArrayList radarPlane;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitMig_17Radar.class, "astatePilotIndx", 0);
    }
}