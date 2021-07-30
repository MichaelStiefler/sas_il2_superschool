package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitPE_8_TGunner extends CockpitGunner
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
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
            return;
        }
        if(f < -120F && prevA0 > 120F)
            f += 360F;
        else
        if(f > 120F && prevA0 < -120F)
            prevA0 += 360F;
        float f13 = f - prevA0;
        float f14 = 0.001F * (float)(Time.current() - prevTime);
        float f15 = Math.abs(f13 / f14);
        if(f15 > 120F)
            if(f > prevA0)
                f = prevA0 + 120F * f14;
            else
            if(f < prevA0)
                f = prevA0 - 120F * f14;
        prevTime = Time.current();
        if(f < -122F)
        {
            float f2 = cvt(f, -180F, -122F, 35F, 5F);
            if(f1 < f2)
                f1 = f2;
        } else
        if(f < -90F)
        {
            float f3 = cvt(f, -122F, -90F, 5F, 1.5F);
            if(f1 < f3)
                f1 = f3;
        } else
        if(f < -75F)
        {
            float f4 = cvt(f, -90F, -75F, 1.5F, -9F);
            if(f1 < f4)
                f1 = f4;
        } else
        if(f < -38F)
        {
            float f5 = cvt(f, -75F, -38F, -9F, -25F);
            if(f1 < f5)
                f1 = f5;
        } else
        if(f < 0.0F)
        {
            float f6 = cvt(f, -38F, 0.0F, -25F, 0.0F);
            if(f1 < f6)
                f1 = f6;
        } else
        if(f < 56F)
        {
            float f7 = cvt(f, 0.0F, 56F, 0.0F, -25F);
            if(f1 < f7)
                f1 = f7;
        } else
        if(f < 75F)
        {
            float f8 = cvt(f, 56F, 75F, -25F, -9F);
            if(f1 < f8)
                f1 = f8;
        } else
        if(f < 90F)
        {
            float f9 = cvt(f, 75F, 90F, -9F, 1.5F);
            if(f1 < f9)
                f1 = f9;
        } else
        if(f < 122F)
        {
            float f10 = cvt(f, 90F, 122F, 1.5F, 5F);
            if(f1 < f10)
                f1 = f10;
        } else
        {
            float f11 = cvt(f, 122F, 180F, 5F, 35F);
            if(f1 < f11)
                f1 = f11;
        }
        if(f1 > 89F)
            f1 = 89F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        prevA0 = f;
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
                hook1 = new HookNamed(aircraft(), "_CANNON01");
            doHitMasterAircraft(aircraft(), hook1, "_CANNON01");
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

    public CockpitPE_8_TGunner()
    {
        super("3DO/Cockpit/Pe-8_TGUN/hier.him", "he111_gunner");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
    }

    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    private Hook hook1;

    static 
    {
        Property.set(CockpitPE_8_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitPE_8_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitPE_8_TGunner.class, "astatePilotIndx", 4);
        Property.set(CockpitPE_8_TGunner.class, "normZN", 1.5F);
        Property.set(CockpitPE_8_TGunner.class, "gsZN", 1.5F);
        Property.set(CockpitPE_8_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitPE_8_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitPE_8_TGunner.class, "astatePilotIndx", 4);
    }
}
