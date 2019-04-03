package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class SPITFIRE11 extends SPITFIRE9
{

    public SPITFIRE11()
    {
        flapps = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.6F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, -0.247F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void update(float f)
    {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f1) > 0.01F)
        {
            flapps = f1;
            hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * f1, 0.0F);
            hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * f1, 0.0F);
        }
    }

    private float flapps;

    static 
    {
        Class class1 = SPITFIRE11.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SpitfireMkXI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1942.11F);
        Property.set(class1, "yearExpired", 1944.12F);
        Property.set(class1, "FlightModel", "FlightModels/SpitfirePRXI.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSpitPR.class
        });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_ExternalDev08"
        });
    }
}
