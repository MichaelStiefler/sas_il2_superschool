package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class YAK_9B extends YAK
    implements TypeBNZFighter, TypeStormovik
{

    public YAK_9B()
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 1500F, -80F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 80F * f, 0.0F);
        f1 = Math.max(-f * 1500F, -60F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 82.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 82.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
    }

    public void update(float f)
    {
        hierMesh().chunkSetAngles("Water_luk", 0.0F, 12F * FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
        int i = 0;
        if(FM.CT.Weapons[3] != null)
        {
            for(int j = 0; j < FM.CT.Weapons[3].length; j++)
                if(FM.CT.Weapons[3][j] != null && FM.CT.Weapons[3][j].haveBullets())
                    i++;

        }
        float f1 = 0.14F;
        switch(i)
        {
        case 0:
        default:
            FM.setGCenter(0.1F);
            FM.setGC_Gear_Shift(0.0F);
            break;

        case 1:
            FM.setGCenter(0.1F - f1);
            FM.setGC_Gear_Shift(0.0F + f1);
            // fall through

        case 2:
            FM.setGCenter(0.1F - 2.0F * f1);
            FM.setGC_Gear_Shift(0.0F + 2.0F * f1);
            break;

        case 3:
            FM.setGCenter(0.1F - 3F * f1);
            FM.setGC_Gear_Shift(0.0F + 3F * f1);
            break;

        case 4:
            FM.setGCenter(0.1F - 4F * f1);
            FM.setGC_Gear_Shift(0.0F + 4F * f1);
            break;
        }
    }

    static 
    {
        Class class1 = YAK_9B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-9B(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1952.8F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-9B.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_9D.class });
        Property.set(class1, "LOSElevation", 0.6432F);
        weaponTriggersRegister(class1, new int[] {
            0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 
            9, 9, 9, 9
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_CANNON01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", 
            "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04"
        });
    }
}
