package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class JU_87G1 extends JU_87
    implements TypeStormovik
{

    public JU_87G1()
    {
        bDynamoLOperational = true;
        bDynamoROperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
    }

    public void update(float f)
    {
        for(int i = 1; i < 5; i++)
            hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, 15F - 30F * FM.EI.engines[0].getControlRadiator(), 0.0F);

        super.update(f);
    }

    private boolean bDynamoLOperational;
    private boolean bDynamoROperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;

    static 
    {
        Class class1 = JU_87G1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87G-1j.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-87G-1/hier_G1.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitJU_87D3.class, CockpitJU_87G1_Gunner.class
        });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 10, 10, 0, 0, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02"
        });
    }
}
