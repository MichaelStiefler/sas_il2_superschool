package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class TA_152H1 extends FW_190
{

    public TA_152H1()
    {
        kangle = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 20F * f, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.getGear() < 0.98F)
        {
            return;
        } else
        {
            hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.44F, 0.0F, 0.44F);
        hierMesh().chunkSetLocate("GearL2a_D0", xyz, ypr);
        xyz[1] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.44F, 0.0F, 0.44F);
        hierMesh().chunkSetLocate("GearR2a_D0", xyz, ypr);
    }

    public void update(float f)
    {
        for(int i = 1; i < 15; i++)
            hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * kangle, 0.0F);

        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        if(FM.Loc.z > 9000D)
        {
            if(!FM.EI.engines[0].getControlAfterburner())
                FM.EI.engines[0].setAfterburnerType(2);
        } else
        if(!FM.EI.engines[0].getControlAfterburner())
            FM.EI.engines[0].setAfterburnerType(1);
        super.update(f);
    }

    private float kangle;

    static 
    {
        Class class1 = TA_152H1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ta.152");
        Property.set(class1, "meshName", "3DO/Plane/Ta-152H-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ta-152H-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTA_152.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        weaponTriggersRegister(class1, new int[] {
            0, 1, 1
        });
        weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON03", "_CANNON04"
        });
    }
}
