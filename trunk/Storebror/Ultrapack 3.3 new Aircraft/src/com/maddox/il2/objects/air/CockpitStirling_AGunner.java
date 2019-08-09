package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitStirling_AGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getYaw(), -38F, 38F, -15F, 15F), 0.0F);
        this.mesh.chunkSetAngles("TurretE", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretF", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretG", -cvt(orient.getYaw(), -33F, 33F, -33F, 33F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretH", 0.0F, cvt(orient.getTangage(), -10F, 32F, -10F, 32F), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(Math.max(Math.abs(orient.getYaw()), Math.abs(orient.getTangage())), 0.0F, 20F, 0.0F, 0.3F);
        this.mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
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
                if(f < -38F)
                    f = -38F;
                if(f > 38F)
                    f = 38F;
                if(f1 > 38F)
                    f1 = 38F;
                if(f1 < -41F)
                    f1 = -41F;
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
                this.bGunFire = false;
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
            if(this.bGunFire)
            {
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN05");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN05");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN06");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN06");
                if(hook3 == null)
                    hook3 = new HookNamed(aircraft(), "_MGUN07");
                doHitMasterAircraft(aircraft(), hook3, "_MGUN07");
                if(hook4 == null)
                    hook4 = new HookNamed(aircraft(), "_MGUN08");
                doHitMasterAircraft(aircraft(), hook4, "_MGUN08");
            }
        }
    }

    public void doGunFire(boolean flag)
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
                this.bGunFire = false;
            else
                this.bGunFire = flag;
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public CockpitStirling_AGunner()
    {
        super("3DO/Cockpit/Stirling-AGun/hier.him", "bf109");
        hook1 = null;
        hook2 = null;
        hook3 = null;
        hook4 = null;
    }

    private Hook hook1;
    private Hook hook2;
    private Hook hook3;
    private Hook hook4;

    static 
    {
        Property.set(CockpitStirling_AGunner.class, "aiTuretNum", 2);
        Property.set(CockpitStirling_AGunner.class, "weaponControlNum", 12);
        Property.set(CockpitStirling_AGunner.class, "astatePilotIndx", 3);
    }
}
