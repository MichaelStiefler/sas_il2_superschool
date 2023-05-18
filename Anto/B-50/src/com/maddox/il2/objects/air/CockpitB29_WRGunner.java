package com.maddox.il2.objects.air;

import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB29_WRGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        mesh.chunkSetAngles("Body", 180F, 0.0F, 180F);
        mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
        mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage(), 180F);
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
            }
            if(f < -120F && prevA0 > 120F)
                f += 360F;
            else
            if(f > 120F && prevA0 < -120F)
                prevA0 += 360F;
            float f3 = f - prevA0;
            float f4 = 0.001F * (float)(Time.current() - prevTime);
            float f5 = Math.abs(f3 / f4);
            if(f5 > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f4;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f4;
            prevTime = Time.current();
            if(bUpper)
            {
                if(f1 < 5F)
                {
                    if(prevElev - f1 >= 4F)
                    {
                        selectLowerT();
                        f1 = 5F;
                    } else
                    {
                        f1 = 5F;
                    }
                } else
                if(f1 > 85F)
                    f1 = 85F;
                if(f > -10F)
                    f = -10F;
                else
                if(f < -170F)
                    f = -170F;
            } else
            if(bLower)
            {
                if(f1 > 5F)
                {
                    if(f1 - prevElev >= 4F)
                    {
                        selectUpperT();
                        f1 = 5F;
                    } else
                    {
                        f1 = 5F;
                    }
                } else
                if(f1 < -85F)
                    f1 = -85F;
                if(f > -10F)
                    f = -10F;
                else
                if(f < -170F)
                    f = -170F;
            }
            prevElev = f1;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
            prevA0 = f;
        }
    }

    protected void selectUpperT()
    {
        HUD.log("Upper turret Selected!");
        bUpper = true;
        bLower = false;
        fm.turret[2].bIsAIControlled = fm.turret[3].bIsAIControlled;
    }

    protected void selectLowerT()
    {
        HUD.log("Lower turret Selected!");
        bUpper = false;
        bLower = true;
        fm.turret[3].bIsAIControlled = fm.turret[2].bIsAIControlled;
    }

    public Turret aiTurret()
    {
        return fm.turret[bUpper ? 2 : 3];
    }

    public int weaponControlNum()
    {
        return bUpper ? 12 : 13;
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
                    hook1 = new HookNamed(aircraft(), "_MGUN07");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN07");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN08");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN08");
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

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("Z_Holes1_D1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitB29_WRGunner()
    {
        super("3DO/Cockpit/B-29/WRGunnerB29.him", "he111_gunner");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        hook2 = null;
    }

    protected boolean doFocusEnter()
    {
        return super.doFocusEnter();
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
            return;
        else
            return;
    }

    private float prevElev;
    private static boolean bUpper = false;
    private static boolean bLower = true;
    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitB29_WRGunner.class, "astatePilotIndx", 6);
    }
}