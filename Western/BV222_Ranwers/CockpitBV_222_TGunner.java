
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner, Aircraft

public class CockpitBV_222_TGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret7B_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Turret7B_D0", aircraft().hierMesh().isChunkVisible("Turret7XA_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        super.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        super.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        super.mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getTangage(), -10F, 58F, -10F, 58F), 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
        } else
        {
            if(bNeedSetUp)
            {
                prevTime = Time.current() - 1L;
                bNeedSetUp = false;
                reflectPlaneMats();
            }
            if(f < -120F && prevA0 > 120F)
                f += 360F;
            else
            if(f > 120F && prevA0 < -120F)
                prevA0 += 360F;
            float f2 = f - prevA0;
            float f3 = 0.001F * (float)(Time.current() - prevTime);
            float f4 = Math.abs(f2 / f3);
            if(f4 > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f3;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f3;
            prevTime = Time.current();
            if(f1 > 73F)
                f1 = 73F;
            if(f1 < 0.0F)
                f1 = 0.0F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
            prevA0 = f;
        }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(super.emitter == null || !super.emitter.haveBullets() || !aiTurret().bIsOperable)
                super.bGunFire = false;
            (super.fm).CT.WeaponControl[weaponControlNum()] = super.bGunFire;
            if(super.bGunFire)
            {
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN07");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN07");
            }
        }
    }

    public void doGunFire(boolean flag)
    {
        if(isRealMode())
        {
            if(super.emitter == null || !super.emitter.haveBullets() || !aiTurret().bIsOperable)
                super.bGunFire = false;
            else
                super.bGunFire = flag;
            (super.fm).CT.WeaponControl[weaponControlNum()] = super.bGunFire;
        }
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            bNeedSetUp = false;
            reflectPlaneMats();
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState()
    {
        if(((super.fm).AS.astateCockpitState & 4) != 0)
            super.mesh.chunkVisible("XGlassDamage1", true);
        if(((super.fm).AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
    }

    public CockpitBV_222_TGunner()
    {
        super("3do/Cockpit/B-25J-TGun/TGunnerBV222.him", "bf109");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
    }

    private boolean bNeedSetUp;
    private Hook hook1;
    private long prevTime;
    private float prevA0;

    static 
    {
        Property.set(CLASS.THIS(), "aiTuretNum", 6);
        Property.set(CLASS.THIS(), "weaponControlNum", 16);
        Property.set(CLASS.THIS(), "astatePilotIndx", 2);
    }
}