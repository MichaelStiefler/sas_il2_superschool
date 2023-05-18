package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class B_29SP extends B_29X
    implements TypeBomber
{

    public B_29SP()
    {
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            killPilot(this, 4);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 2: // '\002'
            FM.turret[0].setHealth(f);
            break;

        case 3: // '\003'
            FM.turret[1].setHealth(f);
            break;

        case 4: // '\004'
            FM.turret[2].setHealth(f);
            break;

        case 5: // '\005'
            FM.turret[3].setHealth(f);
            FM.turret[4].setHealth(f);
            break;
        }
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.air.B_29SP.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-29");
        Property.set(class1, "meshName", "3DO/Plane/B-29-SP(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-29SP.fmd:B29_50");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitB29.class, com.maddox.il2.objects.air.CockpitB29SP_Bombardier.class, com.maddox.il2.objects.air.CockpitB29_AGunner.class
        });
        weaponTriggersRegister(class1, new int[] {
            14, 14, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02"
        });
        weaponsRegister(class1, "default", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null
        });
        weaponsRegister(class1, "LittleBoy", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGunLittleBoy 1", null
        });
        weaponsRegister(class1, "FatMan", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, "BombGunFatMan 1"
        });
        weaponsRegister(class1, "none", new String[] {
            null, null, null, null
        });
    }
}