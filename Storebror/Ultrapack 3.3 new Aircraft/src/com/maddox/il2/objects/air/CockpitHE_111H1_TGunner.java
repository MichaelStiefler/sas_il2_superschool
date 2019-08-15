package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111H1_TGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((HE_111)((Interpolate) (this.fm)).actor).bPitUnfocused = false;
            aircraft().hierMesh().chunkVisible("Kolpak_D0", false);
            aircraft().hierMesh().chunkVisible("Korzina_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
            aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(isFocused())
        {
            ((HE_111)((Interpolate) (this.fm)).actor).bPitUnfocused = true;
            aircraft().hierMesh().chunkVisible("Kolpak_D0", true);
            aircraft().hierMesh().chunkVisible("Korzina_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot4_FAK", aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
            aircraft().hierMesh().chunkVisible("Pilot4_FAL", aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
            super.doFocusLeave();
        }
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        Cockpit.xyz[1] = this.fm.CT.getCockpitDoor() * -0.75F;
        this.mesh.chunkSetLocate("Hood", Cockpit.xyz, Cockpit.ypr);
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, 0.0F, -orient.getTangage());
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
        float f2 = this.fm.CT.getCockpitDoor();
        if(f2 < 0.99F)
        {
            if(f < -42F)
                f = -42F;
            if(f > 42F)
                f = 42F;
        } else
        {
            if(f < -90F)
                f = -90F;
            if(f > 90F)
                f = 90F;
        }
        if(f1 > 63F)
            f1 = 63F;
        if(f1 < -3F)
            f1 = -3F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        if(this.bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN02");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN02");
            if(iCocking > 0)
                iCocking = 0;
            else
                iCocking = 1;
            iNewVisDrums = (int)((float)this.emitter.countBullets() / 1.0F);
            if(iNewVisDrums < iOldVisDrums)
            {
                iOldVisDrums = iNewVisDrums;
                this.mesh.chunkVisible("Drum1", iNewVisDrums > 700);
                this.mesh.chunkVisible("Drum2", iNewVisDrums > 450);
                this.mesh.chunkVisible("Drum3", iNewVisDrums > 150);
                sfxClick(13);
            }
        } else
        {
            iCocking = 0;
        }
        this.mesh.chunkSetAngles("CockingLever", -0.75F * (float)iCocking, 0.0F, 0.0F);
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        else
            this.bGunFire = flag;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
    }

    public CockpitHE_111H1_TGunner()
    {
        super("3DO/Cockpit/He-111P-4-TGun/hier-H1.him", "he111_gunner");
        bNeedSetUp = true;
        hook1 = null;
        iCocking = 0;
        iOldVisDrums = 1000;
        iNewVisDrums = 1000;
    }

    private boolean bNeedSetUp;
    private Hook hook1;
    private int iCocking;
    private int iOldVisDrums;
    private int iNewVisDrums;

    static 
    {
        Property.set(CockpitHE_111H1_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitHE_111H1_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitHE_111H1_TGunner.class, "astatePilotIndx", 2);
    }
}
