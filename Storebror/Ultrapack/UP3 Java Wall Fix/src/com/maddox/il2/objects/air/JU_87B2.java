package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.weapons.BombAB1000;
import com.maddox.il2.objects.weapons.BombSC1000;
import com.maddox.rts.Property;

public class JU_87B2 extends JU_87
{

    public JU_87B2()
    {
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        Object aobj[] = pos.getBaseAttached();
        if(aobj != null)
        {
            int i = 0;
            do
            {
                if(i >= aobj.length)
                    break;
                if((aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombAB1000))
                {
                    hierMesh().chunkVisible("Pilot2_D0", false);
                    hierMesh().chunkVisible("HMask2_D0", false);
                    hierMesh().chunkVisible("Turret1A_D0", false);
                    hierMesh().chunkVisible("Turret1B_D0", false);
                    FM.turret[0].bIsOperable = false;
                    FM.AS.setPilotState(FM.actor, 1, 99);
                    hierMesh().chunkVisible("Pilot2_D1", false);
                    FM.M.massEmpty -= 100F;
                    break;
                }
                i++;
            } while(true);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        if(i == 36 || i == 37 || i == 10)
        {
            hierMesh().chunkVisible("GearR3_D0", false);
            hierMesh().chunkVisible("GearR3Rot_D0", false);
            bDynamoOperational = false;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveFan(float f)
    {
        if(bDynamoOperational)
        {
            pk = Math.abs((int)(FM.Vwld.length() / 14D));
            if(pk >= 1)
                pk = 1;
        }
        if(bDynamoRotary != (pk == 1))
        {
            bDynamoRotary = pk == 1;
            hierMesh().chunkVisible("GearR3_D0", !bDynamoRotary);
            hierMesh().chunkVisible("GearR3Rot_D0", bDynamoRotary);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 17.987F) % 360F : (float)(dynamoOrient - FM.Vwld.length() * 1.5444015264511108D) % 360F;
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 80F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 80F * f, 0.0F);
    }

    public void update(float f)
    {
        for(int i = 1; i < 9; i++)
            hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -15F * FM.EI.engines[0].getControlRadiator(), 0.0F);

        super.update(f);
    }

    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;

    static 
    {
        Class class1 = JU_87B2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87B-2m.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-87B-2/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitJU_87B2.class, CockpitJU_87B2_Gunner.class
        });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1939.9F);
        Property.set(class1, "yearExpired", 1945.5F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 10, 3, 3, 3, 3, 3, 3, 3, 
            3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb03", 
            "_ExternalBomb04", "_ExternalBomb05"
        });
    }
}
