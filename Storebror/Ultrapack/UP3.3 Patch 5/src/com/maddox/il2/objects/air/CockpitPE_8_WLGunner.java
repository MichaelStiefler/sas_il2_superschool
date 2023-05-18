package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitPE_8_WLGunner extends CockpitGunner
{

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        mesh.materialReplace("Matt2D0o", mat);
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
        if(f1 > 50F)
            f1 = 50F;
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
        if(f1 < -60F)
            f1 = -60F;
        if(f1 > 8F)
            f1 = 8F;
        if(f1 > -15F && f < -90F)
            f1 = -15F;
        if(f <= -13.5F && f > -19F && f1 >= -50F)
            f = -13.5F;
        else
        if(f >= -91.5F && f < -90.5F && f1 >= -50F)
            f = -91.5F;
        else
        if(f >= -83F && f < -19F)
        {
            float f2 = cvt(f, -90F, -19F, -60F, -50F);
            if(f1 > f2)
                f1 = f2;
        }
        if(f < -98.5F)
            f = -98.5F;
        if(f > 102F)
            f = 102F;
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
                hook1 = new HookNamed(aircraft(), "_MGUN03");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
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
            aircraft().hierMesh().chunkVisible("turret4b_D0", false);
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
            aircraft().hierMesh().chunkVisible("turret4b_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    public CockpitPE_8_WLGunner()
    {
        super("3DO/Cockpit/Pe-8_WLGUN/hier.him", "he111_gunner");
        bNeedSetUp = true;
        hook1 = null;
    }

    private boolean bNeedSetUp;
    private Hook hook1;

    static 
    {
        Property.set(CockpitPE_8_WLGunner.class, "aiTuretNum", 3);
        Property.set(CockpitPE_8_WLGunner.class, "weaponControlNum", 13);
        Property.set(CockpitPE_8_WLGunner.class, "astatePilotIndx", 5);
        Property.set(CockpitPE_8_WLGunner.class, "normZN", 0.7F);
        Property.set(CockpitPE_8_WLGunner.class, "gsZN", 0.7F);
        Property.set(CockpitPE_8_WLGunner.class, "aiTuretNum", 3);
        Property.set(CockpitPE_8_WLGunner.class, "weaponControlNum", 13);
        Property.set(CockpitPE_8_WLGunner.class, "astatePilotIndx", 5);
    }
}
