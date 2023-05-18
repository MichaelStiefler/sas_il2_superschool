package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class SB_2M103A extends SB
{

    public int[] getBombTrainDelayArray()
    {
        return (new int[] {
            130, 200, 500, 1000, 1500, 2000
        });
    }

    public int getBombTrainMaxAmount()
    {
        return 20;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(thisWeaponsName.startsWith("KMB") || thisWeaponsName.startsWith("6xsab15"))
            hierMesh().chunkVisible("KMB_D0", true);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            for(int i = 0; i < 4; i++)
                if(FM.AS.astateTankStates[i] > 0 && FM.AS.astateTankStates[i] < 5 && World.Rnd().nextFloat() < 0.05F)
                    FM.AS.repairTank(i);

        }
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        if(regiment.country().equals(PaintScheme.countryRussia))
        {
            int i = Mission.getMissionDate(true);
            if(i > 0)
            {
                if(i > 19420101)
                    return "1942_";
                if(i > 19410101)
                    return "1941_";
            }
            return "";
        }
        if(regiment.country().equals(PaintScheme.countryFrance) || regiment.country().equals(PaintScheme.countryBritain) || regiment.country().equals(PaintScheme.countryNetherlands) || regiment.country().equals(PaintScheme.countryPoland) || regiment.country().equals(PaintScheme.countryNewZealand) || regiment.country().equals(PaintScheme.countryUSA) || regiment.country().equals(PaintScheme.countryNoName))
            return "";
        else
            return "fi_";
    }

    static 
    {
        Class class1 = SB_2M103A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SB");
        Property.set(class1, "meshNameDemo", "3DO/Plane/SB-2M-103(Russian)/hier_103A.him");
        Property.set(class1, "meshName", "3DO/Plane/SB-2M-103(Multi1)/hier_103A.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ru", "3DO/Plane/SB-2M-103(Russian)/hier_103A.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/SB-2M-103A.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSB103A.class, CockpitSB103A_Bombardier.class, CockpitSB103A_NGunner.class, CockpitSB103_TGunner.class, CockpitSB103_BGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 10, 11, 12, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", 
            "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}
