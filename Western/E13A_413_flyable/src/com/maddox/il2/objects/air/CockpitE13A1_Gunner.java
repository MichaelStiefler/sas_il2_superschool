
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;


public class CockpitE13A1_Gunner extends CockpitGunner
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
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                setNew.altimeter = fm.getAltitude();
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
        float altimeter;

        private Variables()
        {
            azimuth = new AnglesFork();
        }

    }


    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Turret1A", 0.0F, -f, 0.0F);
        mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
        if(f > 50F)
            f = 50F;
        else
        if(f < -50F)
            f = -50F;
        if(f1 < -12F)
        {
            float f2 = Math.abs(f1 + 12F);
            f1 = -12F;
            resetYPRmodifier();
            if(f < 0.0F)
            {
                xyz[1] = cvt(f2, 0.0F, 23F, 0.0F, 0.2F);
                xyz[2] = cvt(f2, 0.0F, 23F, 0.0F, -0.2F);
            } else
            {
                xyz[1] = cvt(f2, 0.0F, 23F, 0.0F, -0.2F);
                xyz[2] = cvt(f2, 0.0F, 23F, 0.0F, -0.2F);
            }
            mesh.chunkSetLocate("Z_CameraSideLean", xyz, ypr);
        } else
        {
            resetYPRmodifier();
            mesh.chunkSetLocate("Z_CameraSideLean", xyz, ypr);
        }
        mesh.chunkSetAngles("Z_CameraA", 0.0F, -f, 0.0F);
        mesh.chunkSetAngles("Z_CameraB", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(!isRealMode())
            return;
        if(!aiTurret().bIsOperable)
        {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = -35F;
        if(f < 0.0F)
        {
            if(f < -20F)
                f2 = cvt(f, -41F, -20F, -35F, -15F);
            else
                f2 = cvt(f, -20F, -10F, -15F, -8F);
        } else
        if(f > 20F)
            f2 = cvt(f, 20F, 41F, -15F, -35F);
        else
            f2 = cvt(f, 10F, 20F, -8F, -15F);
        if(f < -54F)
            f = -54F;
        if(f > 54F)
            f = 54F;
        if(f1 > 55F)
            f1 = 55F;
        if(f1 < f2)
            f1 = f2;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        if(bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN01");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
        }
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public CockpitE13A1_Gunner()
    {
        super("3DO/Cockpit/B5N2-Gunner/hier.him", "he111_gunner");
        setOld = new Variables();
        setNew = new Variables();
        ac = null;
        hook1 = null;
        bNeedSetUp = true;
        cockpitNightMats = (new String[] {
            "dbombergauges", "bombergauges", "dgauges1", "dgauges4", "gauges1", "gauges4", "turnbankneedles", "Rotatinginvertedcompass", "fixinvertedcompass"
        });
        interpPut(new Interpolater(), null, Time.current(), null);
        setNightMats(false);
        ac = (E13A)aircraft();
        spareMagName = "T92Spare";
        magRotate = 0.0F;
    }

    public void reflectWorldToInstruments(float f)
    {
        super.reflectWorldToInstruments(f);
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.5F);
        mesh.chunkSetLocate("Z_Canopy", xyz, ypr);
        mesh.chunkSetAngles("Z_Need_Navairspeed", 0.0F, floatindex(cvt(0.539957F * Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 30F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("Z_CompassReflected", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_CompassInverted", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_Need_NavclockH", 0.0F, 180F + cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Z_Need_NavclockM", 0.0F, 180F + cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        float f1 = Atmosphere.temperature((float)fm.Loc.z) - 273.15F;
        if(f1 > 0.0F)
            mesh.chunkSetAngles("Z_Need_FreeairTemp", 0.0F, cvt(f1, 0.0F, 40F, 0.0F, 32F), 0.0F);
        else
            mesh.chunkSetAngles("Z_Need_FreeairTemp", 0.0F, cvt(f1, -60F, 0.0F, -48F, 0.0F), 0.0F);
        mesh.chunkSetAngles("Z_Need_NavAltim", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 21600F), 0.0F);
        mesh.chunkSetAngles("Torpedosight", ((E13A1)aircraft()).fSightCurSideslip, 0.0F, 0.0F);
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

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("BlisterInterior1_D0", false);
            aircraft().hierMesh().chunkVisible("BlisterInterior2_D0", false);
            aircraft().hierMesh().chunkVisible("BlisterInterior3_D0", false);
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
            aircraft().hierMesh().chunkVisible("HMask2_D0", false);
            aircraft().hierMesh().chunkVisible("HMask1_D0", false);
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
            aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            aircraft().hierMesh().chunkVisible("Pilot1_D1", !aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            aircraft().hierMesh().chunkVisible("Pilot2_D0", aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
            aircraft().hierMesh().chunkVisible("Pilot2_D1", !aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
            aircraft().hierMesh().chunkVisible("Head1_D0", aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            super.doFocusLeave();
            return;
        }
    }

    protected void reflectPlaneToModel()
    {
        aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
        aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
        aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
        aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
        aircraft().hierMesh().chunkVisible("HMask2_D0", false);
        aircraft().hierMesh().chunkVisible("HMask1_D0", false);
        aircraft().hierMesh().chunkVisible("Head1_D0", false);
        mesh.chunkVisible("pilotNavigator_d0", aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
        mesh.chunkVisible("pilotNavigator_d1", !aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
        mesh.chunkVisible("pilot1_d0", aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
        mesh.chunkVisible("pilot1_d1", !aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        com.maddox.il2.engine.Mat mat1 = hiermesh.material(hiermesh.materialFind("Pilot2"));
        mesh.materialReplace("Pilot2", mat1);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private static final float speedometerScale[] = {
        0.0F, 7F, 14F, 21F, 28F, 43.5F, 62F, 81F, 104.5F, 130F, 
        157F, 184.5F, 214F, 244.5F, 275.5F, 305F, 333F, 363F, 388F, 420F, 
        445F, 472F, 497F, 522F, 549F, 573F, 595F, 616F, 635F, 656F, 
        675F
    };
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private E13A ac;
    private Hook hook1;
    private boolean bNeedSetUp;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitE13A1_Gunner.class, "aiTuretNum", 0);
        Property.set(com.maddox.il2.objects.air.CockpitE13A1_Gunner.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitE13A1_Gunner.class, "astatePilotIndx", 2);
        Property.set(CLASS.THIS(), "normZN", 0.8F);
    }



}
