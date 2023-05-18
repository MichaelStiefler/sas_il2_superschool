package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public abstract class U_2 extends Scheme1
    implements TypeScout, TypeBomber, TypeTransport
{

    public U_2()
    {
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        if(af[0] < -25F)
        {
            af[0] = -25F;
            flag = false;
        } else
        if(af[0] > 25F)
        {
            af[0] = 25F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if(f < 10F)
        {
            if(af[1] < -5F)
            {
                af[1] = -5F;
                flag = false;
            }
        } else
        if(af[1] < -15F)
        {
            af[1] = -15F;
            flag = false;
        }
        if(af[1] > 35F)
        {
            af[1] = 35F;
            flag = false;
        }
        if(!flag)
            return false;
        float f1 = af[1];
        if(f < 2.0F && f1 < 17F)
            return false;
        if(f1 > -5F)
            return true;
        if(f1 > -12F)
        {
            f1 += 12F;
            return f > 12F + f1 * 2.571429F;
        } else
        {
            f1 = -f1;
            return f > f1;
        }
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("Pilot1"))
            killPilot(shot.initiator, 0);
        if(shot.chunkName.startsWith("Pilot2"))
            killPilot(shot.initiator, 1);
        if(shot.chunkName.startsWith("Engine") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            FM.AS.hitEngine(shot.initiator, 0, 1);
        super.msgShot(shot);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -29F * f, 0.0F);
        hierMesh().chunkSetAngles("Rudder1RodR_D0", -29F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Rudder1RodL_D0", 0.0F, 29F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorLRodV_D0", 0.0F, 35F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorLRodN_D0", 0.0F, 35F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorLRodR_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorRRodV_D0", 0.0F, 35F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorRRodN_D0", 0.0F, 35F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorRRodR_D0", 0.0F, -35F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, 35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneLrod_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, 35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneRrod_D0", 0.0F, -35F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
    }

    static 
    {
        Class class1 = U_2.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
