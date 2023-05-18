
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;


public class CockpitBV_222_FLGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        super.mesh.chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw(), 0.0F);
        super.mesh.chunkSetAngles("TurretLB", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(isRealMode())
            if(!aiTurret().bIsOperable)
            {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else
            {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if(f < -10F)
                    f = -10F;
                if(f > 60F)
                    f = 60F;
                if(f1 > 30F)
                    f1 = 30F;
                if(f1 < -30F)
                    f1 = -30F;
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
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
                    hook1 = new HookNamed(aircraft(), "_MGUN01");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
                if(iCocking > 0)
                    iCocking = 0;
                else
                    iCocking = 1;
                iNewVisDrums = (int)((float)super.emitter.countBullets() / 250F);
                if(iNewVisDrums < iOldVisDrums)
                {
                    iOldVisDrums = iNewVisDrums;
                    super.mesh.chunkVisible("DrumL1", iNewVisDrums > 1);
                    super.mesh.chunkVisible("DrumL2", iNewVisDrums > 0);
                    sfxClick(13);
                }
            } else
            {
                iCocking = 0;
            }
            resetYPRmodifier();
            Cockpit.xyz[0] = -0.07F * (float)iCocking;
            super.mesh.chunkSetLocate("LeverL", Cockpit.xyz, Cockpit.ypr);
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

    public CockpitBV_222_FLGunner()
    {
        super("3DO/Cockpit/He-111H-2-LGun/FLGunnerBV222.him", "he111_gunner");
        hook1 = null;
        iCocking = 0;
        iOldVisDrums = 2;
        iNewVisDrums = 2;
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
        {
            super.mesh.chunkVisible("Flare", true);
            setNightMats(true);
        } else
        {
            super.mesh.chunkVisible("Flare", false);
            setNightMats(false);
        }
    }

    public void reflectCockpitState()
    {
        if((super.fm).AS.astateCockpitState != 0)
            super.mesh.chunkVisible("Holes_D1", true);
    }

    private Hook hook1;
    private int iCocking;
    private int iOldVisDrums;
    private int iNewVisDrums;

    static 
    {
        Property.set(CLASS.THIS(), "aiTuretNum", 0);
        Property.set(CLASS.THIS(), "weaponControlNum", 10);
        Property.set(CLASS.THIS(), "astatePilotIndx", 3);
    }
}