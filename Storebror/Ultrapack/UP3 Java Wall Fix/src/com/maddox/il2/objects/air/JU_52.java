package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class JU_52 extends Scheme6
    implements TypeTransport
{

    public JU_52()
    {
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        default:
            break;

        case 2:
            if(FM.turret.length > 0)
                FM.turret[0].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;
        }
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
            hierMesh().chunkVisible("Cart_D0", !bDynamoRotary);
            hierMesh().chunkVisible("CartRot_D0", bDynamoRotary);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 17.987F) % 360F : (float)(dynamoOrient - FM.Vwld.length() * 1.5444015264511108D) % 360F;
        hierMesh().chunkSetAngles("Cart_D0", 0.0F, dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)
            FM.AS.hitEngine(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)
            FM.AS.hitEngine(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("Engine3") && World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)
            FM.AS.hitEngine(shot.initiator, 2, 1);
        if(shot.chunkName.startsWith("Turret"))
            FM.turret[0].bIsOperable = false;
        if(shot.chunkName.startsWith("Tail1") && Pd.z > 0.5D && Pd.x > -6D && Pd.x < -4.9499998092651367D && World.Rnd().nextFloat() < 0.5F)
            FM.AS.hitPilot(shot.initiator, 2, (int)(shot.mass * 1000F * 0.5F));
        if(shot.chunkName.startsWith("CF") && v1.x < -0.2D && Pd.x > 2.5999999046325684D && Pd.z > 0.735D && World.Rnd().nextFloat() < 0.178F)
            FM.AS.hitPilot(shot.initiator, Pd.y <= 0.0D ? 1 : 0, (int)(shot.mass * 900F));
        if(shot.chunkName.startsWith("WingLIn") && Math.abs(Pd.y) < 2.0999999046325684D)
            FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(0, (int)(shot.mass * 30F)));
        if(shot.chunkName.startsWith("WingRIn") && Math.abs(Pd.y) < 2.0999999046325684D)
            FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int)(shot.mass * 30F)));
        super.msgShot(shot);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;

    static 
    {
        Class class1 = JU_52.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
