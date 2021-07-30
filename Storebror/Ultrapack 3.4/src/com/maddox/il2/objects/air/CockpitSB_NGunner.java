package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;

public class CockpitSB_NGunner extends CockpitGunner
{
    private class Variables
    {

        AnglesFork azimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
        }

    }


    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("Z_arrow_hour", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_min", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_sec_1", -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_temp", cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -70F, 70F, 52F, -52F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_speed", floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeed2KMH()), 0.0F, 600F, 0.0F, 15F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_alt2", -cvt(fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_arrow_alt1", -cvt(fm.getAltitude(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_bomb_door", (aircraft().getBayDoor() - 0.5F) * -55F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_AN4b", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        if(bGunFire && patronMat != null)
        {
            patron -= 0.5F * f;
            patronMat.setLayer(0);
            patronMat.set((byte)11, patron);
        }
        if(emitter != null && World.cur().diffCur.Limited_Ammo)
        {
            if(emitter.countBullets() < 44)
            {
                mesh.chunkVisible("ammoL1", false);
                mesh.chunkVisible("ammoR1", false);
            }
            if(emitter.countBullets() < 40)
            {
                mesh.chunkVisible("ammoL2", false);
                mesh.chunkVisible("ammoR2", false);
            }
            if(emitter.countBullets() < 34)
            {
                mesh.chunkVisible("ammoL3", false);
                mesh.chunkVisible("ammoR3", false);
            }
            if(emitter.countBullets() < 30)
            {
                mesh.chunkVisible("ammoL4", false);
                mesh.chunkVisible("ammoR4", false);
            }
            if(emitter.countBullets() < 25)
            {
                mesh.chunkVisible("ammoL5", false);
                mesh.chunkVisible("ammoR5", false);
            }
            if(emitter.countBullets() < 21)
            {
                mesh.chunkVisible("ammoL6", false);
                mesh.chunkVisible("ammoR6", false);
            }
            if(emitter.countBullets() < 16)
            {
                mesh.chunkVisible("ammoL7", false);
                mesh.chunkVisible("ammoR7", false);
            }
            if(emitter.countBullets() < 12)
            {
                mesh.chunkVisible("ammoL8", false);
                mesh.chunkVisible("ammoR8", false);
            }
            if(emitter.countBullets() < 8)
            {
                mesh.chunkVisible("ammoL9", false);
                mesh.chunkVisible("ammoR9", false);
            }
            if(emitter.countBullets() < 1)
            {
                mesh.chunkVisible("ammoL10", false);
                mesh.chunkVisible("ammoR10", false);
            }
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Z_TUR_1", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("CameraTan", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_SHKAS_R", f, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_SHKAS_L", f, 0.0F, 0.0F);
        mesh.chunkSetAngles("CameraYaw", f, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TUR_3", -f, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TUR_center", -f, 0.0F, 0.0F);
        mesh.chunkSetAngles("CameraTan", 0.0F, 0.0F, f1);
        resetYPRmodifier();
        Cockpit.xyz[0] = f * 0.0003F;
        mesh.chunkSetLocate("Z_TUR_bag", Cockpit.xyz, Cockpit.ypr);
        if(f1 > 0.0F)
        {
            mesh.chunkSetAngles("Extract4", 0.0F, f * 0.4F, f1 * 0.3F);
            resetYPRmodifier();
            Cockpit.xyz[2] = f1 * 0.00102F;
            mesh.chunkSetLocate("Extract3", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkSetLocate("Extract2", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkSetLocate("Extract1", Cockpit.xyz, Cockpit.ypr);
        } else
        {
            mesh.chunkSetAngles("Extract4", 0.0F, f * 0.4F, f1 * -0.02F);
            resetYPRmodifier();
            Cockpit.xyz[2] = f1 * 0.0012F;
            mesh.chunkSetLocate("Extract3", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkSetLocate("Extract2", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkSetLocate("Extract1", Cockpit.xyz, Cockpit.ypr);
        }
        float f2 = f1 * 0.12F;
        mesh.chunkSetAngles("BeltL1", -f2, 0.0F, -f * 0.36F);
        mesh.chunkSetAngles("BeltL2", -f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltL3", -f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltL4", -f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltL5", -f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR1", f2, 0.0F, f * 0.36F);
        mesh.chunkSetAngles("BeltR2", f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR3", f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR4", f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR5", f2, 0.0F, 0.0F);
        float f3 = f * 0.32F;
        float f4 = 5F + f1 * 0.038F;
        mesh.chunkSetAngles("BeltL6", f3 * 0.5F + f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltL7", f3 + f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltL8", f3 + f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltL9", -f3 - f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltL10", -f3 * 1.5F - f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR6", f3 * 0.5F - f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR7", f3 - f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR8", f3 - f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR9", -f3 + f4, 0.0F, 0.0F);
        mesh.chunkSetAngles("BeltR10", -(f3 * 1.5F) + f4, 0.0F, 0.0F);
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
        if(f < -20F)
            f = -20F;
        if(f > 20F)
            f = 20F;
        if(f1 < -50F)
            f1 = -50F;
        if(f1 > 45F)
            f1 = 45F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected float waypointAzimuth()
    {
        return this.waypointAzimuthInvertMinus(10F);
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        setTmp = setOld;
        setOld = setNew;
        setNew = setTmp;
        setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
        if(bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN01");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
            if(hook2 == null)
                hook2 = new HookNamed(aircraft(), "_MGUN02");
            doHitMasterAircraft(aircraft(), hook2, "_MGUN02");
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

    public CockpitSB_NGunner()
    {
        super("3DO/Cockpit/SB-NGunner/hier.him", "he111_gunner");
        patron = 1.0F;
        patronMat = null;
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        hook1 = null;
        hook2 = null;
        cockpitNightMats = (new String[] {
            "ACHO_arrow", "Dprib_one", "Dprib_six", "equip_AN4_sh", "prib_one", "Prib_six"
        });
        setNightMats(false);
        int i = -1;
        i = mesh.materialFind("patron");
        if(i != -1)
        {
            patronMat = mesh.material(i);
            patronMat.setLayer(0);
        }
        if(emitter == null)
        {
            for(int j = 1; j <= 10; j++)
            {
                mesh.chunkVisible("ammoL" + j, false);
                mesh.chunkVisible("ammoR" + j, false);
            }

        }
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            mesh.materialReplace("lamp_n1", "lamp_n1_light");
            mesh.materialReplace("equip_an4_sh", "equip_an4_sh_light");
            mesh.materialReplace("equip_an4", "equip_an4_light");
            mesh.materialReplace("equip_an4_sha", "equip_an4_sha_light");
            mesh.materialReplace("equip_an4a", "equip_an4a_light");
        } else
        {
            mesh.materialReplace("lamp_n1", "lamp_n1");
            mesh.materialReplace("equip_an4_sh", "equip_an4_sh");
            mesh.materialReplace("equip_an4", "equip_an4");
            mesh.materialReplace("equip_an4_sha", "equip_an4_sha");
            mesh.materialReplace("equip_an4a", "equip_an4a");
        }
        setNightMats(false);
    }

    private float patron;
    private Mat patronMat;
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        0.0F, -10F, -19.5F, -32F, -46F, -66.5F, -89F, -114F, -141F, -170.5F, 
        -200.5F, -232.5F, -264F, -295.5F, -328F, -360F
    };
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(CockpitSB_NGunner.class, "aiTuretNum", 0);
        Property.set(CockpitSB_NGunner.class, "weaponControlNum", 10);
        Property.set(CockpitSB_NGunner.class, "astatePilotIndx", 1);
        Property.set(CockpitSB_NGunner.class, "normZN", 1.1F);
        Property.set(CockpitSB_NGunner.class, "gsZN", 1.1F);
    }
}
