package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class N1K2JA extends N1K2
{

    public N1K2JA()
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.35F, 0.95F, 0.0F, -82F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.35F, 0.4F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.35F, 0.95F, 0.0F, -48F), 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(f, 0.35F, 0.95F, 0.0F, -58F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.65F, 0.0F, -82F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.1F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.65F, 0.0F, -48F), 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.65F, 0.0F, -58F), 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, -0.075F, 0.0F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.0F, 1.0F, 40F, 0.0F);
        hiermesh.chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        if(FM.CT.getGear() == 1.0F)
            hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.1F, 0.0F, 20F), 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.23F, 0.0F, 0.23F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.23F, 0.0F, -42F), 0.0F);
        hierMesh().chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.23F, 0.0F, -45F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.27625F, 0.0F, 0.27625F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.27625F, 0.0F, -33F), 0.0F);
        hierMesh().chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.27625F, 0.0F, -66F), 0.0F);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void update(float f)
    {
        super.update(f);
        float f1 = FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f1) > 0.01F)
        {
            flapps = f1;
            for(int i = 1; i < 9; i++)
                hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -20F * f1, 0.0F);

        }
    }

    static 
    {
        Class class1 = N1K2JA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "N1K");
        Property.set(class1, "meshName", "3DO/Plane/N1K2-Ja(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/N1K2-Ja(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/N1K2-Ja_mod.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitN1K2JA.class
        });
        Property.set(class1, "LOSElevation", 1.1716F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 0, 0, 1, 3, 3, 3, 3, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02"
        });
    }
}
