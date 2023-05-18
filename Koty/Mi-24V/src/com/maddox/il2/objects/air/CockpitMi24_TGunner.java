// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 19.03.2017 15:26:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitMi24_TGunner.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner, Cockpit

public class CockpitMi24_TGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        super.mesh.chunkSetAngles("Body", 0.0F, 0.0F, -1F);
        super.mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
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
                if(f < -60F)
                    f = -60F;
                if(f > 60F)
                    f = 60F;
                if(f1 > 20F)
                    f1 = 20F;
                if(f1 < -80F)
                    f1 = -80F;
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
            ((FlightModelMain) (super.fm)).CT.WeaponControl[weaponControlNum()] = super.bGunFire;
            if(super.bGunFire)
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
            if(super.emitter == null || !super.emitter.haveBullets() || !aiTurret().bIsOperable)
                super.bGunFire = false;
            else
                super.bGunFire = flag;
            ((FlightModelMain) (super.fm)).CT.WeaponControl[weaponControlNum()] = super.bGunFire;
        }
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
            super.mesh.chunkVisible("Z_Holes1_D1", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitMi24_TGunner()
    {
        super("3DO/Cockpit/A-20G-TGun/TGunnerMi24.him", "he111_gunner");
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
        Property.set(CLASS.THIS(), "aiTuretNum", 0);
        Property.set(CLASS.THIS(), "weaponControlNum", 10);
        Property.set(CLASS.THIS(), "astatePilotIndx", 1);
    }
}