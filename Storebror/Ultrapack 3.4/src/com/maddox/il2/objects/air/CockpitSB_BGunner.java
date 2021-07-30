package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitSB_BGunner extends CockpitGunner
{

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        float f1 = ((SB)aircraft()).turretUp;
        resetYPRmodifier();
        Cockpit.xyz[2] = -cvt(f1, 0.0F, 40F, 0.0F, 0.9F);
        mesh.chunkSetLocate("Z_CANNOPY", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_LUK_DOWN", hatch * -90F, 0.0F, 0.0F);
        if(bGunFire && patronMat != null)
        {
            patron += 0.5F * f;
            patronMat.setLayer(0);
            patronMat.set((byte)11, patron);
        }
        if(emitter != null && World.cur().diffCur.Limited_Ammo)
        {
            if(emitter.countBullets() < 64)
                mesh.chunkVisible("Bullets_1", false);
            if(emitter.countBullets() < 58)
                mesh.chunkVisible("Bullets_2", false);
            int i = 3;
            for(int j = 51; j >= 0; j -= 3)
            {
                if(emitter.countBullets() < j)
                    mesh.chunkVisible("Bullets_" + i, false);
                i++;
            }

        }
    }

    protected void reflectPlaneMats()
    {
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = -orient.getTangage();
        mesh.chunkSetAngles("Z_gunY", f, 0.0F, 0.0F);
        mesh.chunkSetAngles("ZGun", -f1, 0.0F, 0.0F);
        float f2 = f * 0.055F;
        float f3 = f1 * 0.25F;
        mesh.chunkSetAngles("Belt_1", 0.0F, 0.0F, f * 0.07F - f1 * 0.12F);
        mesh.chunkSetAngles("Belt_2", -f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("Belt_3", -f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("Belt_4", -f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("Belt_5", -f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("Belt_6", -f2, 0.0F, 0.0F);
        if(f < 0.0F)
        {
            mesh.chunkSetAngles("Belt_7", 6.5F * f2, 0.0F, 0.0F);
            mesh.chunkSetAngles("Belt_8", 6.5F * f2, 0.0F, 0.0F);
            mesh.chunkSetAngles("Belt_9", 6.5F * f2, 0.0F, 0.0F);
            mesh.chunkSetAngles("Belt_10", 6.5F * -f2, 0.0F, 0.0F);
            mesh.chunkSetAngles("Belt_11", 6.5F * -f2, 0.0F, 0.0F);
            mesh.chunkSetAngles("Belt_12", 6.5F * -f2, 0.0F, 0.0F);
        }
        mesh.chunkSetAngles("Belt_14", -f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Belt_15", -f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Belt_16", -f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Belt_17", -f3, 0.0F, 0.0F);
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
        if(f1 < -70F)
            f1 = -70F;
        else
        if(f1 > 0.0F)
            f1 = 0.0F;
        float f2 = cvt(f1, -70F, -11F, 25F, 18F);
        if(f < -f2)
            f = -f2;
        if(f > f2)
            f = f2;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        if(!entered && firstEnter)
        {
            if(fm.turret[2].tu[0] == 0.0F && fm.turret[2].tu[1] == 0.0F)
                hatchMovDir = 1.0F;
            else
                hatch = 1.0F;
            entered = true;
        }
        if(hatch < 1.0F && hatchMovDir == 1.0F)
            hatch = hatch + 0.015F;
        else
        if(hatch > 0.0F && hatchMovDir == -1F)
            hatch = hatch - 0.015F;
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        if(bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN04");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN04");
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

    public CockpitSB_BGunner()
    {
        super("3DO/Cockpit/SB-BGunner/hier.him", "he111_gunner");
        bNeedSetUp = true;
        entered = false;
        firstEnter = false;
        hatch = 0.0F;
        hatchMovDir = 0.0F;
        patronMat = null;
        patron = 1.0F;
        onAuto = true;
        hook1 = null;
        int i = -1;
        i = mesh.materialFind("patron");
        if(i != -1)
        {
            patronMat = mesh.material(i);
            patronMat.setLayer(0);
        }
        if(emitter == null)
        {
            for(int j = 1; j <= 19; j++)
                mesh.chunkVisible("Bullets_" + j, false);

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
            aircraft().hierMesh().chunkVisible("Blister3_D0", false);
            firstEnter = true;
            aircraft().FM.turret[1].bIsAIControlled = true;
            aircraft().FM.turret[2].bIsAIControlled = onAuto;
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
            onAuto = aircraft().FM.turret[2].bIsAIControlled;
            aircraft().hierMesh().chunkVisible("Blister3_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private boolean bNeedSetUp;
    private boolean entered;
    private boolean firstEnter;
    private float hatch;
    private float hatchMovDir;
    private Mat patronMat;
    private float patron;
    private boolean onAuto;
    private Hook hook1;

    static 
    {
        Property.set(CockpitSB_BGunner.class, "aiTuretNum", 2);
        Property.set(CockpitSB_BGunner.class, "weaponControlNum", 12);
        Property.set(CockpitSB_BGunner.class, "astatePilotIndx", 2);
        Property.set(CockpitSB_BGunner.class, "normZN", 1.48F);
        Property.set(CockpitSB_BGunner.class, "gsZN", 1.48F);
    }
}
