
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.*;
import com.maddox.sound.*;
import com.maddox.util.HashMapExt;
import java.io.PrintStream;
import java.util.*;


public class CockpitE_3RADAR extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
//            float f = ((JU_88A4)aircraft()).fSightCurForwardAngle;
//            float f1 = ((JU_88A4)aircraft()).fSightCurSideslip;
//            mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if(bEntered)
            {
                HookPilot hookpilot = HookPilot.current;
//                hookpilot.setSimpleAimOrient(aAim + 10F * f1, tAim + f, 0.0F);
            }
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.altimeter = fm.getAltitude();
                float f2 = waypointAzimuth();
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f2 - 90F);
                    setOld.waypointAzimuth.setDeg(f2 - 90F);
                }
                else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f2 - setOld.azimuth.getDeg(1.0F));
                }
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

        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
//            aircraft().hierMesh().chunkVisible("BlisterTop_D0", false);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        }
        else
        {
            leave();
//            aircraft().hierMesh().chunkVisible("BlisterTop_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
//        CmdEnv.top().exec("fov 23.913");
//        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
//        hookpilot.doAim(true);
//        hookpilot.setSimpleUse(true);
//        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(!bEntered)
        {
            return;
        }
        else
        {
//            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot = HookPilot.current;
//            hookpilot.doAim(false);
//            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
//            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            return;
        }
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
        if(flag)
            enter();
        else
            leave();
    }

    public CockpitE_3RADAR()
    {
        super("3DO/Cockpit/E-3_AWACS_Radar/hier.him", "he111");
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        bEntered = false;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        oldRotoMode = 0;
        oldAwacsTimer = 0L;
        oldDisplayRange = 0;
        oldDisplayVrt = 0;
        oldDisplayHol = 0;
        bNeedReflesh = false;
        bNeedCleanup = false;
        planeList = new ArrayList();

        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed1 = new HookNamed(mesh, "CAMERAAIM");
            hooknamed1.computePos(this, pos.getAbs(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
            kAim = loc.getOrient().getKren();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
//        cockpitNightMats = (new String[] {
//            "Pedal"
//        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
//        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK01");
//        Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
//        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
//        light1 = new LightPointActor(new LightPoint(), loc1.getPoint());
//        light1.light.setColor(126F, 232F, 245F);
//        light1.light.setEmit(0.0F, 0.0F);
//        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
//        AircraftLH.printCompassHeading = true;
    }

    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bEntered)
        {
//            mesh.chunkVisible("BlackBox", true);
        }
        else
        {
//            mesh.chunkVisible("BlackBox", false);
        }
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        if(((E_3A)aircraft()).rotoMode > 0 && oldRotoMode == 0)
        {
            mesh.materialReplace("RadarScr", "RadarScr00");
            mesh.materialReplace("RadarScr_dummy", "RadarScr00");
            if(((E_3A)aircraft()).rotoMode == 1)
                bNeedCleanup = true;
            else
                bNeedReflesh = true;
        }
        if(((E_3A)aircraft()).rotoMode == 0 && oldRotoMode > 0)
        {
            mesh.materialReplace("RadarScr", "RadarScr");
            mesh.materialReplace("RadarScr_dummy", "RadarScr_dummy");
            bNeedCleanup = true;
        }
        if(((E_3A)aircraft()).rotoMode == 1 && oldRotoMode == 2)
        {
            bNeedCleanup = true;
        }
        if(((E_3A)aircraft()).rotoMode > 0 && oldRotoMode > 0)
        {
            if(((E_3A)aircraft()).radarDisplayRange != oldDisplayRange ||
               ((E_3A)aircraft()).radarDisplayVrt != oldDisplayVrt ||
               ((E_3A)aircraft()).radarDisplayHol != oldDisplayHol)
            {
                if(((E_3A)aircraft()).radarDisplayRange == 0)
                    mesh.materialReplace("RadarScr", "RadarScr00");
                else if(((E_3A)aircraft()).radarDisplayRange == 1)
                {
                    int j = 0;
                    if(((E_3A)aircraft()).radarDisplayVrt == 1)
                        j = 2;
                    int i = 0;
                    if(((E_3A)aircraft()).radarDisplayHol == 1)
                        i = 1;
                    mesh.materialReplace("RadarScr", "RadarScr1" + String.valueOf(i + j));
                }
                else if(((E_3A)aircraft()).radarDisplayRange == 2)
                {
                    int j = 4 * ((E_3A)aircraft()).radarDisplayVrt;
                    int i = ((E_3A)aircraft()).radarDisplayHol;
                    mesh.materialReplace("RadarScr", "RadarScr" + String.valueOf(20 + i + j));
                }
                if(((E_3A)aircraft()).rotoMode == 1)
                    bNeedCleanup = true;
                else
                    bNeedReflesh = true;
            }
        }
//        mesh.chunkSetAngles("Z_Trim1", cvt(fm.CT.getTrimElevatorControl(), -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
        if(useRealisticNavigationInstruments())
        {
//            mesh.chunkSetAngles("Z_Compass8", setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
        }
        else
        {
//            mesh.chunkSetAngles("Z_Compass8", setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
        }
//        resetYPRmodifier();
//        xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, 0.045F, -0.045F);
//        mesh.chunkSetLocate("zHORIZ2", xyz, ypr);

        if(bNeedCleanup)
            cleanupIcons(0, true);
        else if(bNeedReflesh || (((E_3A)aircraft()).rotoMode == 2 && ((E_3A)aircraft()).awacsTimer != oldAwacsTimer))
        {
            int totalIconCount = 0;

            planeList = ((E_3A)aircraft()).getEnemyPlaneList();
            for(int i = 0; i < Math.min(13, planeList.size()); i++)
            {
                Actor actor = (Actor)planeList.get(i);
                if(isInsideScreen(actor))
                {
                    drawIcon(actor, totalIconCount, "Iconen");
                    totalIconCount++;
                }
            }

            planeList = ((E_3A)aircraft()).getEnemyCruiseMissileList();
            for(int i = 0; i < Math.min(4, planeList.size()); i++)
            {
                Actor actor = (Actor)planeList.get(i);
                if(isInsideScreen(actor))
                {
                    drawIcon(actor, totalIconCount, "Iconmi");
                    totalIconCount++;
                }
            }

            planeList = ((E_3A)aircraft()).getFriendlyPlaneList();
            for(int i = 0; i < Math.min(13, planeList.size()); i++)
            {
                Actor actor = (Actor)planeList.get(i);
                if(isInsideScreen(actor))
                {
                    drawIcon(actor, totalIconCount, "Iconfr");
                    totalIconCount++;
                }
            }

            cleanupIcons(totalIconCount, false);
        }

        oldRotoMode = ((E_3A)aircraft()).rotoMode;
        oldAwacsTimer = ((E_3A)aircraft()).awacsTimer;
        oldDisplayRange = ((E_3A)aircraft()).radarDisplayRange;
        oldDisplayVrt = ((E_3A)aircraft()).radarDisplayVrt;
        oldDisplayHol = ((E_3A)aircraft()).radarDisplayHol;
        bNeedReflesh = false;
        bNeedCleanup = false;
    }

    private void drawIcon(Actor actor, int iconNum, String iconMatName)
    {
        String snum = null;
        if(iconNum < 9)
            snum = "00" + String.valueOf(iconNum + 1);
        else if(iconNum < 99)
            snum = "0" + String.valueOf(iconNum + 1);
        else
            snum = String.valueOf(iconNum + 1);
        String mshIcon = "ZZ_Icon" + snum;
        String mshAlt = "ZZ_Alt" + snum;
        String mshSpd = "ZZ_Spd" + snum;

        float x = (float)(actor.pos.getAbsPoint().x - aircraft().pos.getAbsPoint().x);
        float y = (float)(actor.pos.getAbsPoint().y - aircraft().pos.getAbsPoint().y);
        float head = - actor.pos.getAbsOrient().getYaw() + 90F;

        int tmpalt = (int)(actor.pos.getAbsPoint().z * 3.28084D / 100D);  // convert meter into 100 feet
        if(tmpalt > 100)   // Materials prepaired for 0 ~ 99 by 1, but by 10 for over 100
            tmpalt = (int)(tmpalt * 0.1D) * 10;
        if(tmpalt > 999)
            tmpalt = 999;
        String altnum = null;
        if(tmpalt < 10)
            altnum = "Alt_n00" + String.valueOf(tmpalt);
        else if(tmpalt < 100)
            altnum = "Alt_n0" + String.valueOf(tmpalt);
        else
            altnum = "Alt_n" + String.valueOf(tmpalt);

        actor.pos.speed(vtemp);
        int tmpspd = (int)(vtemp.length() * 0.1943845D);  // convert m/s into 10 knot
        if(tmpspd > 100)   // Materials prepaired for 0 ~ 99 by 1, but by 10 for over 100
            tmpspd = (int)(tmpspd * 0.1D) * 10;
        if(tmpspd > 999)
            tmpspd = 999;
        String spdnum = null;
        if(tmpspd < 10)
            spdnum = "Spd_n00" + String.valueOf(tmpspd);
        else if(tmpspd < 100)
            spdnum = "Spd_n0" + String.valueOf(tmpspd);
        else
            spdnum = "Spd_n" + String.valueOf(tmpspd);

        if(((E_3A)aircraft()).radarDisplayRange == 0)
        {
            x = x / 1852F / 600F * 0.3F + 0.15F;   // 600NM is 0.30m on screen
            y = y / 1852F / 600F * 0.3F + 0.20F;
        }
        else if(((E_3A)aircraft()).radarDisplayRange == 1)
        {
            x = x / 1852F / 300F * 0.3F;   // 300NM is 0.30m on screen
            y = y / 1852F / 300F * 0.3F;

            if(((E_3A)aircraft()).radarDisplayHol == 0)
            {
                x += 0.299F;
            }
            else
            {
                x += 0.001F;
            }

            if(((E_3A)aircraft()).radarDisplayVrt == 0)
            {
                y += 0.10F;
            }
            else
            {
                y += 0.30F;
            }
        }
        else
        {
            x = x / 1852F / 200F * 0.3F;   // 200NM is 0.30m on screen
            y = y / 1852F / 200F * 0.3F;

            if(((E_3A)aircraft()).radarDisplayHol == 0)
            {
                x += 0.44844F;
            }
            else if(((E_3A)aircraft()).radarDisplayHol == 1)
            {
                x += 0.15F;
            }
            else
            {
                x -= 0.14844F;
            }

            if(((E_3A)aircraft()).radarDisplayVrt == 0)
            {
                y -= 0.0875F;
            }
            else if(((E_3A)aircraft()).radarDisplayVrt == 1)
            {
                y += 0.20F;
            }
            else
            {
                y += 0.4875F;
            }
        }
        resetYPRmodifier();
        xyz[0] = x;
        xyz[2] = y;
        ypr[1] = -head;
        mesh.chunkVisible(mshIcon, true);
        mesh.chunkSetLocate(mshIcon, xyz, ypr);
        mesh.materialReplace("zIcon" + snum, iconMatName);
        if(x > 0.27F)
            xyz[0] = x - 0.036F;
        ypr[1] = 0F;
        mesh.chunkVisible(mshAlt, true);
        mesh.chunkSetLocate(mshAlt, xyz, ypr);
        mesh.materialReplace("zAlt" + snum, altnum);
        mesh.chunkVisible(mshSpd, true);
        mesh.chunkSetLocate(mshSpd, xyz, ypr);
        mesh.materialReplace("zSpd" + snum, spdnum);
    }

    private boolean isInsideScreen(Actor actor)
    {
        if(((E_3A)aircraft()).radarDisplayRange == 0)
            return true;

        double leftlimitX;
        double rightlimitX;
        double uplimitY;
        double downlimitY;
        if(((E_3A)aircraft()).radarDisplayRange == 1)
        {
            if(((E_3A)aircraft()).radarDisplayHol == 0)
            {
                leftlimitX = aircraft().pos.getAbsPoint().x - 555600D;  // West 300 NM ~ Me
                rightlimitX = aircraft().pos.getAbsPoint().x + 100D;
            }
            else
            {
                leftlimitX = aircraft().pos.getAbsPoint().x - 100D;  // Me ~ East 300NM
                rightlimitX = aircraft().pos.getAbsPoint().x + 555600D;
            }

            if(((E_3A)aircraft()).radarDisplayVrt == 0)
            {
                uplimitY = aircraft().pos.getAbsPoint().y + 555600D;  // North 300 NM ~ Me ~ South 100NM
                downlimitY = aircraft().pos.getAbsPoint().y - 185200D;
            }
            else
            {
                uplimitY = aircraft().pos.getAbsPoint().y + 185200D;  // North 100 NM ~ Me ~ South 300NM
                downlimitY = aircraft().pos.getAbsPoint().y - 555600D;
            }
        }
        else
        {
            if(((E_3A)aircraft()).radarDisplayHol == 0)
            {
                leftlimitX = aircraft().pos.getAbsPoint().x - 555600D;  // West 300 NM ~ West 100 NM .... Me
                rightlimitX = aircraft().pos.getAbsPoint().x - 185100D;
            }
            else if(((E_3A)aircraft()).radarDisplayHol == 1)
            {
                leftlimitX = aircraft().pos.getAbsPoint().x - 185300D;  // West 100 NM ~ Me ~ East 100 NM
                rightlimitX = aircraft().pos.getAbsPoint().x + 185300D;
            }
            else
            {
                leftlimitX = aircraft().pos.getAbsPoint().x + 185100D;  // Me .... East 100NM ~ East 300NM
                rightlimitX = aircraft().pos.getAbsPoint().x + 555600D;
            }

            if(((E_3A)aircraft()).radarDisplayVrt == 0)
            {
                uplimitY = aircraft().pos.getAbsPoint().y + 555600D;  // North 300 NM ~ North 70NM .... Me
                downlimitY = aircraft().pos.getAbsPoint().y + 129640D;
            }
            else if(((E_3A)aircraft()).radarDisplayVrt == 1)
            {
                uplimitY = aircraft().pos.getAbsPoint().y + 240760D;  // North 130 NM ~ Me ~ South 130NM
                downlimitY = aircraft().pos.getAbsPoint().y - 240760D;
            }
            else
            {
                uplimitY = aircraft().pos.getAbsPoint().y - 129640D;  // Me .... South 70 NM ~ South 300NM
                downlimitY = aircraft().pos.getAbsPoint().y - 555600D;
            }
        }

        if(leftlimitX < actor.pos.getAbsPoint().x && actor.pos.getAbsPoint().x < rightlimitX
           && downlimitY < actor.pos.getAbsPoint().y && actor.pos.getAbsPoint().y < uplimitY)
            return true;

        return false;
    }

    private void cleanupIcons(int start, boolean bAbort)
    {
        for(int i = start; i < ICON_MAXNUM; i++)
        {
            String snum = null;
            if(i < 9)
                snum = "00" + String.valueOf(i + 1);
            else if(i < 99)
                snum = "0" + String.valueOf(i + 1);
            else
                snum = String.valueOf(i + 1);
            String mshIcon = "ZZ_Icon" + snum;

            if(!mesh.isChunkVisible(mshIcon))
            {
                if(bAbort)
                    return;
                else
                    continue;
            }

            String mshAlt = "ZZ_Alt" + snum;
            String mshSpd = "ZZ_Spd" + snum;

            resetYPRmodifier();
            mesh.chunkVisible(mshIcon, false);
            mesh.chunkSetLocate(mshIcon, xyz, ypr);
            mesh.chunkVisible(mshAlt, false);
            mesh.chunkSetLocate(mshAlt, xyz, ypr);
            mesh.chunkVisible(mshSpd, false);
            mesh.chunkSetLocate(mshSpd, xyz, ypr);
        }
    }

    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(20F);
    }

    public void reflectCockpitState()
    {
//        if((fm.AS.astateCockpitState & 0x20) != 0)
//            mesh.chunkVisible("XGlassDamage6", true);
        retoggleLight();
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            light1.light.setEmit(0.004F, 1.0F);
            setNightMats(true);
        }
        else
        {
            light1.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    private void retoggleLight()
    {
        if(cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        }
        else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    protected void reflectPlaneMats()
    {
    }

    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private LightPointActor light1;
    private float saveFov;
    private float aAim;
    private float tAim;
    private float kAim;
    private boolean bEntered;
    private Point3d tmpP;
    private Vector3d tmpV;

    private int oldRotoMode;
    private long oldAwacsTimer;
    private int oldDisplayRange;
    private int oldDisplayVrt;
    private int oldDisplayHol;
    private boolean bNeedReflesh;
    private boolean bNeedCleanup;
    private ArrayList planeList;

    // static temporary values
    static private int ICON_MAXNUM = 30;

    // static temporary values
    static private Vector3d vtemp = new Vector3d();


    static
    {
        Property.set(CLASS.THIS(), "astatePilotIndx", 0);
    }
}