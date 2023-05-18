package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.*;

public class B_29B extends B_29X
{

    public B_29B()
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
        Class class1 = com.maddox.il2.objects.air.B_29B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-29");
        Property.set(class1, "meshName", "3DO/Plane/B-29B(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-29SP.fmd:B29_50");
        Property.set(class1, "cockpitClass", new Class[] {
                com.maddox.il2.objects.air.CockpitB29.class, com.maddox.il2.objects.air.CockpitB29_Bombardier.class, com.maddox.il2.objects.air.CockpitB29_AGunner.class
            });
        weaponTriggersRegister(class1, new int[] {
            14, 14, 14, 3, 3, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN11", "_MGUN12", "_MGUN13", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04"
        });
        weaponsRegister(class1, "default", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null
        });
        weaponsRegister(class1, "1x1600", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, "BombGun1600lbs 1"
        });
        weaponsRegister(class1, "6x300", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun300lbs 3", "BombGun300lbs 3"
        });
        weaponsRegister(class1, "20x100", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun50kg 3", "BombGun50kg 3", "BombGun50kg 7", "BombGun50kg 7"
        });
        weaponsRegister(class1, "4x500", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun500lbs 2", "BombGun500lbs 2"
        });
        weaponsRegister(class1, "2x1000", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun1000lbs 1", "BombGun1000lbs 1"
        });
        weaponsRegister(class1, "1x2000", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, "BombGun2000lbs 1"
        });
        weaponsRegister(class1, "4x1000", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun1000lbs 2", "BombGun1000lbs 2"
        });
        weaponsRegister(class1, "2x2000", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", null, null, "BombGun2000lbs 1", "BombGun2000lbs 1"
        });
        weaponsRegister(class1, "16x300", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun300lbs 8", "BombGun300lbs 8", null, null
        });
        weaponsRegister(class1, "20x20FragClusters", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGunM26A2 8", "BombGunM26A2 8", "BombGunM26A2 2", "BombGunM26A2 2"
        });
        weaponsRegister(class1, "10x500", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun500lbs 5", "BombGun500lbs 5", null, null
        });
        weaponsRegister(class1, "20x250", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun250lbs 8", "BombGun250lbs 8", "BombGun250lbs 2", "BombGun250lbs 2"
        });
        weaponsRegister(class1, "6x1600", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1600lbs 1", "BombGun1600lbs 1", "BombGun1600lbs 2", "BombGun1600lbs 2"
        });
        weaponsRegister(class1, "20x500", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun500lbs 8", "BombGun500lbs 8", "BombGun500lbs 2", "BombGun500lbs 2"
        });
        weaponsRegister(class1, "12x1000", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1000lbs 1", "BombGun1000lbs 1", "BombGun1000lbs 2", "BombGun1000lbs 2"
        });
        weaponsRegister(class1, "6x2000", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun2000lbs 1", "BombGun2000lbs 1", "BombGun2000lbs 2", "BombGun2000lbs 2"
        });
        weaponsRegister(class1, "12x1600", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1600lbs 1", "BombGun1600lbs 1", "BombGun1600lbs 2", "BombGun1600lbs 2"
        });
        weaponsRegister(class1, "20x1000", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1000lbs 8", "BombGun1000lbs 8", "BombGun1000lbs 2", "BombGun1000lbs 2"
        });
        weaponsRegister(class1, "10x2000", new String[] {
            "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 500", 
            "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun2000lbs 3", "BombGun2000lbs 3", "BombGun2000lbs 2", "BombGun2000lbs 2"
        });
        weaponsRegister(class1, "none", new String[] {
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null
        });
    }
}