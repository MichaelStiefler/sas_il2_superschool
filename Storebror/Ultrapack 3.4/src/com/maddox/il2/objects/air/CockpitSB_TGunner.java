package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitSB_TGunner extends CockpitGunner
{

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("Z_HR_HOUR", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_HR_MINUTE", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_HR_SECOND", -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ALT_METRS", -cvt(fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ALT_KM", -cvt(fm.getAltitude(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        if(bGunFire && patronMat != null)
        {
            patron += 0.5F * f;
            patronMat.setLayer(0);
            patronMat.set((byte)11, patron);
        }
        if(emitter != null && World.cur().diffCur.Limited_Ammo)
        {
            if(emitter.countBullets() < 30)
                mesh.chunkVisible("Bullets3", false);
            if(emitter.countBullets() < 20)
                mesh.chunkVisible("Bullets2", false);
            if(emitter.countBullets() < 10)
                mesh.chunkVisible("Bullets1", false);
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
        float f1 = -orient.getTangage();
        mesh.chunkSetAngles("TUR_cradle", f, 0.0F, 0.0F);
        mesh.chunkSetAngles("ZGun", f1, 0.0F, 0.0F);
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
        float f2 = ((SB)aircraft()).turretUp;
        if(f2 < 40F)
        {
            float f3 = cvt(f2, 30F, 40F, 0.0F, 105F);
            float f5 = cvt(f2, 30F, 40F, 15F, 50F);
            if(f > f3)
                f = f3;
            if(f < -f3)
                f = -f3;
            if(f1 > f5)
                f1 = f5;
        }
        if(f < -120F && prevA0 > 120F)
            f += 360F;
        else
        if(f > 120F && prevA0 < -120F)
            prevA0 += 360F;
        float f4 = f - prevA0;
        float f6 = 0.001F * (float)(Time.current() - prevTime);
        float f7 = Math.abs(f4 / f6);
        if(f7 > 120F)
            if(f > prevA0)
                f = prevA0 + 120F * f6;
            else
            if(f < prevA0)
                f = prevA0 - 120F * f6;
        prevTime = Time.current();
        if(f < -105F)
            f = -105F;
        else
        if(f > 105F)
            f = 105F;
        float f8 = Math.abs(f);
        if(f1 < 17F && f8 < 1.0F)
            f = 0.0F;
        if(f8 < 30F)
        {
            float f9 = 0.0F;
            float f10 = 0.0F;
            float f11 = 0.0F;
            float f12 = 0.0F;
            if(f8 < 1.0F)
            {
                f11 = -10F;
                f12 = -10F;
            } else
            if(f8 < 5F)
            {
                f9 = 0.0F;
                f10 = 5F;
                f11 = 17F;
                f12 = 16.3F;
            } else
            if(f8 < 10F)
            {
                f9 = 5F;
                f10 = 10F;
                f11 = 16.3F;
                f12 = 14.5F;
            } else
            if(f8 < 15F)
            {
                f9 = 10F;
                f10 = 15F;
                f11 = 14.5F;
                f12 = 11.5F;
            } else
            if(f8 < 20F)
            {
                f9 = 15F;
                f10 = 20F;
                f11 = 11.5F;
                f12 = 5.8F;
            } else
            if(f8 < 25F)
            {
                f9 = 20F;
                f10 = 25F;
                f11 = 5.8F;
                f12 = -2.2F;
            } else
            if(f8 < 30F)
            {
                f9 = 25F;
                f10 = 30F;
                f11 = -2.2F;
                f12 = -10F;
            }
            float f13 = cvt(f8, f9, f10, f11, f12);
            if(f1 < f13)
                f1 = f13;
        }
        if(f1 < -10F)
            f1 = -10F;
        if(f1 > 50F)
            f1 = 50F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        prevA0 = f;
    }

    protected void interpTick()
    {
        float f = ((SB)aircraft()).turretUp;
        if(!aiTurret().bIsNetMirror && !aiTurret().bIsAIControlled && f < 40F && f > 0.0F)
            hookGunner().mouseMove(0, 0, 0);
        resetYPRmodifier();
        Cockpit.xyz[2] = -cvt(f, 0.0F, 20F, 0.0F, 0.9F);
        mesh.chunkSetLocate("Z_CANNOPY", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("TUR_UP", f, 0.0F, 0.0F);
        mesh.chunkSetAngles("ZGunUpPivot", f, 0.0F, 0.0F);
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
        if (aircraft().FM.turret[1].bIsAIControlled != onAuto) {
            onAuto = aircraft().FM.turret[1].bIsAIControlled;
            ((SB)aircraft()).bMultiFunction = !onAuto;
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

    public CockpitSB_TGunner()
    {
        super("3DO/Cockpit/SB-TGunner/hier.him", "he111_gunner");
        bNeedSetUp = true;
        patron = 1.0F;
        patronMat = null;
        onAuto = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        cockpitNightMats = (new String[] {
            "arrow", "Dprib_six", "Prib_six"
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
            mesh.chunkVisible("Bullets3", false);
            mesh.chunkVisible("Bullets2", false);
            mesh.chunkVisible("Bullets1", false);
        }
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        setNightMats(false);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("turret2b_D0", false);
            aircraft().hierMesh().chunkVisible("Blister3_D0", false);
            aircraft().hierMesh().chunkVisible("Turret2hood_D0", false);
            aircraft().FM.turret[2].bIsAIControlled = true;
            aircraft().FM.turret[1].bIsAIControlled = onAuto;
            ((SB)aircraft()).bMultiFunction = !onAuto;
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
            aircraft().hierMesh().chunkVisible("turret2b_D0", true);
            aircraft().hierMesh().chunkVisible("Blister3_D0", true);
            aircraft().hierMesh().chunkVisible("Turret2hood_D0", true);
            onAuto = aircraft().FM.turret[1].bIsAIControlled;
            ((SB)aircraft()).bMultiFunction = !onAuto;
            super.doFocusLeave();
            return;
        }
    }

    public boolean useMultiFunction()
    {
        return true;
    }

    public void doMultiFunction(boolean flag)
    {
        if(flag)
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TurretUp");
        else
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TurretDown");
    }

    private boolean bNeedSetUp;
    private float patron;
    private Mat patronMat;
    private boolean onAuto;
    private long prevTime;
    private float prevA0;
    private Hook hook1;

    static 
    {
        Property.set(CockpitSB_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitSB_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitSB_TGunner.class, "astatePilotIndx", 2);
        Property.set(CockpitSB_TGunner.class, "normZN", 0.8F);
        Property.set(CockpitSB_TGunner.class, "gsZN", 0.8F);
    }
}
