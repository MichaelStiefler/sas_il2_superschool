package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class JU_87G2RUDEL extends JU_87
    implements TypeStormovik, TypeAcePlane
{

    public JU_87G2RUDEL()
    {
        bDynamoLOperational = true;
        bDynamoROperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.Skill = 3;
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
        Class class1 = JU_87G2RUDEL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "meshName", "3do/plane/Ju-87G-2(ofRudel)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "FlightModel", "FlightModels/Ju-87G-2(ofRudel).fmd");
        weaponTriggersRegister(class1, new int[] {
            1, 1, 10, 10
        });
        weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02"
        });
    }
}
