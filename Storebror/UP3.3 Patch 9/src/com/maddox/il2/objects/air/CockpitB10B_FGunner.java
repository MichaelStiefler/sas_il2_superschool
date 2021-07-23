package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB10B_FGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
//        System.out.println("moveGun(" + orient.getYaw() + ", " + orient.getPitch() + ", " + orient.getRoll() + ")");
        super.moveGun(orient);
        mesh.chunkSetAngles("TurrelA", 0.0F, orient.getYaw(), 0.0F);
        mesh.chunkSetAngles("TurrelB", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
//        System.out.println("clipAnglesGun aiTurret().bIsOperable=" + aiTurret().bIsOperable + ", isRealMode()=" + isRealMode());
        if(isRealMode()) {
            if(!aiTurret().bIsOperable)
            {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else
            {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if(f < -90F)
                    f = -90F;
                if(f > 90F)
                    f = 90F;
                if(f1 > 80F)
                    f1 = 80F;
                if(f1 < -15F)
                    f1 = -15F;
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
        }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
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
    }

    public void doGunFire(boolean flag)
    {
        if(isRealMode())
        {
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            else
                bGunFire = flag;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        }
    }

    public CockpitB10B_FGunner()
    {
        super("3DO/Cockpit/B-10B-FGun/hier.him", "bf109");
        hook1 = null;
    }

    private Hook hook1;

    static 
    {
        Property.set(CockpitB10B_FGunner.class, "aiTuretNum", 0);
        Property.set(CockpitB10B_FGunner.class, "weaponControlNum", 10);
        Property.set(CockpitB10B_FGunner.class, "astatePilotIndx", 1);
    }
}
