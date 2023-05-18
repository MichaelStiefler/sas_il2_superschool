package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitPE_8_RGunner extends CockpitGunner
{

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        aircraft().hierMesh().setCurChunk("Rudder1_d0");
        aircraft().hierMesh().getChunkLocObj(tmpL);
        mesh.chunkSetAngles("Rudder1_d0", 0.0F, -tmpL.getOrient().getYaw(), 0.0F);
        reflectPlaneToModel();
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel()
    {
        if(!aircraft().hierMesh().isChunkVisible("Rudder1_d0") && !aircraft().hierMesh().isChunkVisible("Rudder1_d1"))
            mesh.chunkVisible("Rudder1_d0", false);
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = -orient.getTangage();
        mesh.chunkSetAngles("Turret", f, 0.0F, 0.0F);
        mesh.chunkSetAngles("Gun", 0.0F, 0.0F, f1);
        float f2 = 0.01905F * (float)Math.sin(Math.toRadians(f));
        float f3 = 0.01905F * (float)Math.cos(Math.toRadians(f));
        f = (float)Math.toDegrees(Math.atan(f2 / (f3 + 0.3565F)));
        mesh.chunkSetAngles("camerarod", 0.0F, 0.0F, f1);
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
        float f11 = aircraft().FM.CT.getElevator() * 25F;
        if(f1 < -45F)
            f1 = -45F;
        if(f1 > 69F)
            f1 = 69F;
        if(f < -110F)
            f = -110F;
        else
        if(f < -90F)
        {
            if(f1 > -11F)
            {
                float f3 = cvt(f, -110F, -90F, 1.0F, -1.4F) + f11;
                if(f1 < f3)
                    f1 = f3;
            } else
            {
                float f4 = cvt(f, -110F, -90F, -36F, -26F) + f11;
                if(f1 > f4)
                    f1 = f4;
            }
        } else
        if(f < -70F)
            if(f1 > -11F)
            {
                float f5 = cvt(f, -90F, -70F, -1.4F, -11F) + f11;
                if(f1 < f5)
                    f1 = f5;
            } else
            {
                float f6 = cvt(f, -90F, -70F, -26F, -11F) + f11;
                if(f1 > f6)
                    f1 = f6;
            }
        if(f > 110F)
            f = 110F;
        else
        if(f > 90F)
        {
            if(f1 > -11F)
            {
                float f7 = cvt(f, 90F, 110F, -1.4F, 1.0F) + f11;
                if(f1 < f7)
                    f1 = f7;
            } else
            {
                float f8 = cvt(f, 90F, 110F, -16F, -27F) + f11;
                if(f1 > f8)
                    f1 = f8;
            }
        } else
        if(f > 70F)
            if(f1 > -11F)
            {
                float f9 = cvt(f, 70F, 90F, -11F, -1.4F) + f11;
                if(f1 < f9)
                    f1 = f9;
            } else
            {
                float f10 = cvt(f, 70F, 90F, -11F, -16F) + f11;
                if(f1 > f10)
                    f1 = f10;
            }
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
                hook1 = new HookNamed(aircraft(), "_CANNON02");
            doHitMasterAircraft(aircraft(), hook1, "_CANNON02");
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

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("turret3a_D0", false);
            aircraft().hierMesh().chunkVisible("turret3b_D0", false);
            aircraft().hierMesh().chunkVisible("Head8_D0", false);
            aircraft().hierMesh().chunkVisible("HMask8_D0", false);
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
            aircraft().hierMesh().chunkVisible("turret3a_D0", true);
            aircraft().hierMesh().chunkVisible("turret3b_D0", true);
            aircraft().hierMesh().chunkVisible("Head8_D0", aircraft().hierMesh().isChunkVisible("Pilot8_D0"));
            super.doFocusLeave();
            return;
        }
    }

    public CockpitPE_8_RGunner()
    {
        super("3DO/Cockpit/Pe-8_RGUN/hier.him", "he111_gunner");
        bNeedSetUp = true;
        tmpL = new Loc();
        l = new Loc();
        hook1 = null;
    }

    private boolean bNeedSetUp;
    private Loc tmpL;
    Loc l;
    private Hook hook1;

    static 
    {
        Property.set(CockpitPE_8_RGunner.class, "aiTuretNum", 2);
        Property.set(CockpitPE_8_RGunner.class, "weaponControlNum", 12);
        Property.set(CockpitPE_8_RGunner.class, "astatePilotIndx", 7);
        Property.set(CockpitPE_8_RGunner.class, "normZN", 0.3F);
        Property.set(CockpitPE_8_RGunner.class, "gsZN", 0.3F);
        Property.set(CockpitPE_8_RGunner.class, "aiTuretNum", 2);
        Property.set(CockpitPE_8_RGunner.class, "weaponControlNum", 12);
        Property.set(CockpitPE_8_RGunner.class, "astatePilotIndx", 7);
    }
}
