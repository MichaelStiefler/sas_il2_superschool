
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.*;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import java.io.PrintStream;


public class CockpitE13A1_Bombardier extends CockpitBombardier
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(ac != null && ac.bChangedPit)
            {
                reflectPlaneToModel();
                ac.bChangedPit = false;
            }
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                if(bEntered)
                {
                    float f = ac.fSightCurForwardAngle + ac.tSAim;
                    float f1 = -ac.fSightCurSideslip;
                    mesh.chunkSetAngles("BlackBox", f1, -ac.kSAim, -f);
                    HookPilot hookpilot = HookPilot.current;
                    hookpilot.setInstantOrient(-f1, tAim + f, 0.0F);
                }
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                setNew.altimeter = fm.getAltitude();
                setNew.flaps = 0.9F * setOld.flaps + 0.1F * fm.CT.FlapsControl;
                setNew.gear = 0.7F * setOld.gear + 0.3F * fm.CT.GearControl;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        AnglesFork azimuth;
        float gear;
        float flaps;
        float altimeter;

        private Variables()
        {
            azimuth = new AnglesFork();
        }

    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.18000000715255737D, 0.059999998658895493D, -0.31999999284744263D);
            hookpilot.setTubeSight(point3d);
            aircraft().hierMesh().chunkVisible("BlisterInterior1_D0", false);
            aircraft().hierMesh().chunkVisible("BlisterInterior2_D0", false);
            aircraft().hierMesh().chunkVisible("BlisterInterior3_D0", false);
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1A_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
            aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
            aircraft().hierMesh().chunkVisible("HMask1_D0", false);
            aircraft().hierMesh().chunkVisible("HMask3_D0", false);
            aircraft().hierMesh().chunkVisible("Head1_D0", false);
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
            aircraft().hierMesh().chunkVisible("BlisterInterior1_D0", true);
            aircraft().hierMesh().chunkVisible("BlisterInterior2_D0", true);
            aircraft().hierMesh().chunkVisible("BlisterInterior3_D0", true);
            aircraft().hierMesh().chunkVisible("Interior_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            aircraft().hierMesh().chunkVisible("Pilot1_D1", !aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            aircraft().hierMesh().chunkVisible("Pilot2_D0", aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
            aircraft().hierMesh().chunkVisible("Pilot2_D1", !aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
            aircraft().hierMesh().chunkVisible("Pilot3_D0", aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
            aircraft().hierMesh().chunkVisible("Pilot3_D1", !aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
            aircraft().hierMesh().chunkVisible("Head1_D0", aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
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
        hookpilot.setSimpleAimOrient(-27F, -50F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov " + saveBSFov);
        Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(aAim, tAim + ac.tSAim, ac.kSAim);
        hookpilot.setInstantOrient(aAim, tAim + ac.tSAim, ac.kSAim);
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
            hookpilot.setInstantOrient(-27F, -50F, 0.0F);
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
            hookpilot1.setInstantOrient(-27F, -50F, 0.0F);
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
            prepareToEnter();
        else
            leave();
    }

    public CockpitE13A1_Bombardier()
    {
        super("3DO/Cockpit/B5N2-Bombardier/hier.him", "he111");
        bNeedSetUp = true;
        ac = null;
        bTorp = false;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        curAlt = 300F;
        curSpd = 50F;
        enteringAim = false;
        spareAmmoGunner = 5;
        saveBSFov = 24F;
        bEntered = false;
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
            hooknamed.computePos(this, new Loc(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
            kAim = loc.getOrient().getKren();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        cockpitNightMats = (new String[] {
            "dbombergauges", "bombergauges", "dgauges1", "dgauges4", "gauges1", "gauges4", "turnbankneedles", "Rotatinginvertedcompass", "fixinvertedcompass"
        });
        ac = (E13A1)aircraft();
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(acoustics != null)
            acoustics.globFX = new ReverbFXRoom(0.45F);
        limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.15F, 0.13F, -0.13F, 0.06F, -0.1F
        });
        if(aircraft().thisWeaponsName.startsWith("1x91"))
            bTorp = true;
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
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
        mesh.chunkVisible("BlackBox", bEntered);
        mesh.chunkVisible("zReticle", bEntered);
        mesh.chunkVisible("zDriftMark", bEntered);
        mesh.chunkVisible("zBulb", bEntered);
        mesh.chunkVisible("zRefraction", bEntered);
        if(bEntered)
        {
            mesh.chunkSetAngles("zDriftMark", ac.fSightCurSideslip * 0.9F, 0.0F, 0.0F);
            resetYPRmodifier();
            float f1 = fm.Or.getKren() + ac.kSAim;
            Cockpit.xyz[0] = cvt(f1, -13F, 13F, 0.23F, -0.23F);
            float f3 = fm.Or.getTangage() + ac.tSAim;
            Cockpit.xyz[1] = cvt(f3, -13F, 13F, -0.23F, 0.23F);
            Cockpit.ypr[0] = cvt(fm.Or.getKren(), -45F, 45F, -180F, 180F);
            mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[0] = cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
            Cockpit.xyz[1] = cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
            mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
        }
        resetYPRmodifier();
        xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.655F);
        mesh.chunkSetLocate("Z_Canopy", xyz, ypr);
        mesh.chunkSetAngles("Z_Need_Navairspeed", 0.0F, floatindex(cvt(0.539957F * Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 30F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("Z_CompassReflected", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_CompassInverted", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_Need_NavclockH", 0.0F, 180F + cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Z_Need_NavclockM", 0.0F, 180F + cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        float f2 = Atmosphere.temperature((float)fm.Loc.z) - 273.15F;
        if(f2 > 0.0F)
            mesh.chunkSetAngles("Z_Need_FreeairTemp", 0.0F, cvt(f2, 0.0F, 40F, 0.0F, 32F), 0.0F);
        else
            mesh.chunkSetAngles("Z_Need_FreeairTemp", 0.0F, cvt(f2, -60F, 0.0F, -48F, 0.0F), 0.0F);
        float f4 = fm.turret[0].tu[0];
        float f5 = fm.turret[0].tu[1];
        mesh.chunkSetAngles("Z_Gun1", 0.0F, f4, 0.0F);
        mesh.chunkSetAngles("Z_T92Mg", 0.0F, f5, 0.0F);
        mesh.chunkSetAngles("Z_LandingGear", 0.0F, 70F * setNew.gear, 0.0F);
        mesh.chunkSetAngles("Z_Flaps", 0.0F, 70F * setNew.flaps, 0.0F);
        mesh.chunkSetAngles("Z_CowlFlaps", 0.0F, -75F + fm.EI.engines[0].getControlRadiator() * 75F, 0.0F);
        mesh.chunkSetAngles("Z_RudderTrim", 0.0F, 40F * fm.CT.getTrimRudderControl(), 0.0F);
        mesh.chunkSetAngles("Z_AileronTrim", 0.0F, 40F * fm.CT.getTrimAileronControl(), 0.0F);
        mesh.chunkSetAngles("Z_Need_Alt1a", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 21600F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Alt1b", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 2160F), 0.0F);
        if(bTorp)
        {
            mesh.chunkSetAngles("Z_TorpedoSight1", 0.0F, ac.fSightCurTorpslip, 0.0F);
            mesh.chunkSetAngles("Z_TorpedoSight2", 0.0F, ac.fSightCurTorpslip, 0.0F);
            mesh.chunkSetAngles("Z_TorpedoSight3", 0.0F, -cvt(ac.fSightCurTorpslip, -40F, 40F, -48F, 48F), 0.0F);
            mesh.chunkSetAngles("Torpedosight", cvt(ac.fSightCurTorpslip, -40F, 40F, -48F, 48F), 0.0F, 0.0F);
        }
        if(aircraft().FM.CT.Weapons[10] != null && aircraft().FM.CT.Weapons[10][0] != null)
        {
            int i = aircraft().FM.CT.Weapons[10][0].countBullets() / 97 + 1;
            for(int k = 1; k < 6; k++)
                if(k < i)
                    mesh.chunkVisible("T92Spare" + k, true);
                else
                    mesh.chunkVisible("T92Spare" + k, false);

        } else
        {
            for(int j = 1; j < 6; j++)
                mesh.chunkVisible("T92Spare" + j, false);

        }
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("holesMiddle", true);
        if((fm.AS.astateCockpitState & 0x40) == 0);
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("holesGunner", true);
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("holesCanopyNav", true);
            mesh.chunkVisible("DNavGauge2", true);
            mesh.chunkVisible("NavGauge2", false);
            mesh.chunkVisible("Z_Need_FreeairTemp", false);
            mesh.chunkVisible("Z_Need_NavclockH", false);
            mesh.chunkVisible("Z_Need_NavclockM", false);
        }
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("ZOil", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("holesCanopyNav", true);
            mesh.chunkVisible("DNavGauge1", true);
            mesh.chunkVisible("NavGauge1", false);
            mesh.chunkVisible("Z_Need_Navairspeed", false);
            mesh.chunkVisible("Z_Need_Alt1a", false);
            mesh.chunkVisible("Z_Need_Alt1b", false);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("holesCanopy", true);
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("holesFront", true);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    protected void reflectPlaneToModel()
    {
        aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
        aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
        aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
        aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
        aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
        aircraft().hierMesh().chunkVisible("HMask1_D0", false);
        aircraft().hierMesh().chunkVisible("HMask3_D0", false);
        aircraft().hierMesh().chunkVisible("Head1_D0", false);
        mesh.chunkVisible("pilot1_d0", aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
        mesh.chunkVisible("pilot1_d1", !aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
        mesh.chunkVisible("pilotgunner_d0", aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
        mesh.chunkVisible("pilotgunner_d1", !aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
        mesh.materialReplace("Pilot2", mat);
    }

    private boolean bNeedSetUp;
    private E13A1 ac;
    private boolean bTorp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float curAlt;
    private float curSpd;
    private boolean enteringAim;
    private int spareAmmoGunner;
    private static final float speedometerScale[] = {
        0.0F, 7F, 14F, 21F, 28F, 43.5F, 62F, 81F, 104.5F, 130F, 
        157F, 184.5F, 214F, 244.5F, 275.5F, 305F, 333F, 363F, 388F, 420F, 
        445F, 472F, 497F, 522F, 549F, 573F, 595F, 616F, 635F, 656F, 
        675F
    };
    private float saveFov;
    private float saveBSFov;
    private float aAim;
    private float tAim;
    private float kAim;
    private boolean bEntered;

    static 
    {
        Property.set(CLASS.THIS(), "astatePilotIndx", 0);
        Property.set(CLASS.THIS(), "normZNs", new float[] {
            0.7F, 0.45F, 0.7F, 0.45F
        });
        Property.set(CLASS.THIS(), "gsZN", 0.5F);
        Property.set(CLASS.THIS(), "aiTuretNum", -2);
        Property.set(CLASS.THIS(), "weaponControlNum", 3);
        Property.set(CLASS.THIS(), "astatePilotIndx", 1);
    }

}
