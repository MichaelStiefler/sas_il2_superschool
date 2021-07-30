package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitG10N1_AGunner extends CockpitGunner
{

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 8) != 0)
            mesh.chunkVisible("XGlassDamage1", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("XGlassDamage2", true);
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getYaw(), -38F, 38F, -15F, 15F), 0.0F);
        mesh.chunkSetAngles("TurretD", 0.0F, cvt(orient.getTangage(), -43F, 43F, -10F, 10F), 0.0F);
        mesh.chunkSetAngles("TurretE", -orient.getYaw(), 0.0F, 0.0F);
        mesh.chunkSetAngles("TurretF", 0.0F, orient.getTangage(), 0.0F);
        mesh.chunkSetAngles("TurretG", -cvt(orient.getYaw(), -33F, 33F, -33F, 33F), 0.0F, 0.0F);
        mesh.chunkSetAngles("TurretH", 0.0F, cvt(orient.getTangage(), -10F, 32F, -10F, 32F), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(Math.max(Math.abs(orient.getYaw()), Math.abs(orient.getTangage())), 0.0F, 20F, 0.0F, 0.3F);
        mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(isRealMode())
            if(!aiTurret().bIsOperable)
            {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else
            {
                float yaw = orient.getYaw();
                float pitch = orient.getTangage();
                if(yaw < -38F)
                    yaw = -38F;
                if(yaw > 38F)
                    yaw = 38F;
                if(pitch > 43F)
                    pitch = 43F;
                if(pitch < -41F)
                    pitch = -41F;
                orient.setYPR(yaw, pitch, 0.0F);
                orient.wrap();
            }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN11");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN11");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN12");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN12");
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

    public CockpitG10N1_AGunner()
    {
        super("3DO/Cockpit/G10N1-AGun/AGunnerG10N1.him", "bf109");
        this.hook1 = null;
        this.hook2 = null;
//        bNeedSetUp = true;
    }

//    private boolean bNeedSetUp;
    private Hook    hook1;
    private Hook    hook2;

    static
    {
        Class class1 = CockpitG10N1_AGunner.class;
        Property.set(class1, "aiTuretNum", 5);
        Property.set(class1, "weaponControlNum", 15);
        Property.set(class1, "astatePilotIndx", 3);
        Property.set(class1, "normZN", 0.2F);
    }
}
