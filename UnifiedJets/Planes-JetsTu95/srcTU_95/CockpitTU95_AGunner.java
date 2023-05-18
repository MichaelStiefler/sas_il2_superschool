// Source File Name:   CockpitTU95_AGunner.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.*;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner, Aircraft, Cockpit

public class CockpitTU95_AGunner extends CockpitGunner
{

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        float f1 = ((FlightModelMain) (super.fm)).CT.getElevator();
        super.mesh.chunkSetAngles("VatorL_D0", 0.0F, -30F * f1, 0.0F);
        super.mesh.chunkSetAngles("VatorR_D0", 0.0F, -30F * f1, 0.0F);
    }

    protected void reflectPlaneMats()
    {
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("XGlassDamage1", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        super.mesh.chunkVisible("VatorL_D0", hiermesh.isChunkVisible("VatorL_D0"));
        super.mesh.chunkVisible("VatorL_D1", hiermesh.isChunkVisible("VatorL_D1"));
        super.mesh.chunkVisible("VatorL_CAP", hiermesh.isChunkVisible("VatorL_CAP"));
        super.mesh.chunkVisible("VatorR_D0", hiermesh.isChunkVisible("VatorR_D0"));
        super.mesh.chunkVisible("VatorR_D1", hiermesh.isChunkVisible("VatorR_D1"));
        super.mesh.chunkVisible("VatorR_CAP", hiermesh.isChunkVisible("VatorR_CAP"));
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        super.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        super.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        super.mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getYaw(), -38F, 38F, -15F, 15F), 0.0F);
        super.mesh.chunkSetAngles("TurretD", 0.0F, cvt(orient.getTangage(), -43F, 43F, -10F, 10F), 0.0F);
        super.mesh.chunkSetAngles("TurretE", -orient.getYaw(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("TurretF", 0.0F, orient.getTangage(), 0.0F);
        super.mesh.chunkSetAngles("TurretG", -cvt(orient.getYaw(), -33F, 33F, -33F, 33F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("TurretH", 0.0F, cvt(orient.getTangage(), -10F, 32F, -10F, 32F), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(Math.max(Math.abs(orient.getYaw()), Math.abs(orient.getTangage())), 0.0F, 20F, 0.0F, 0.3F);
        super.mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
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
                if(f1 > 43F)
                    f1 = 43F;
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
            if(super.emitter == null || !super.emitter.haveBullets() || !aiTurret().bIsOperable)
                super.bGunFire = false;
            ((FlightModelMain) (super.fm)).CT.WeaponControl[weaponControlNum()] = super.bGunFire;
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

    public CockpitTU95_AGunner()
    {
        super("3do/Cockpit/TU95-AGun/AGunnerTU95.him", "bf109");
        bNeedSetUp = true;
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private boolean bNeedSetUp;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitTU95_AGunner.class, "aiTuretNum", 1);
        Property.set(com.maddox.il2.objects.air.CockpitTU95_AGunner.class, "weaponControlNum", 11);
        Property.set(com.maddox.il2.objects.air.CockpitTU95_AGunner.class, "astatePilotIndx", 3);
    }
}