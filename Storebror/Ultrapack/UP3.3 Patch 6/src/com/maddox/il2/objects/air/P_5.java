package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class P_5 extends R_5xyz
{

    public P_5()
    {
        strafeWithGuns = false;
//        flapps = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        Class class1 = P_5.class;
        if(Mission.isCoop())
        {
            Property.set(class1, "cockpitClass", new Class[] {
                CockpitP_5.class, CockpitP_5_Copilot.class
            });
        } else
        {
            Property.set(class1, "cockpitClass", new Class[] {
                CockpitP_5.class, CockpitP_5_Bombardier.class
            });
            if(this == World.getPlayerAircraft())
                this.createCockpits();
        }
    }

    public void update(float f)
    {
        updateRadiator();
        super.update(f);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
        } else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
            hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        else
            return "p5_";
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        default:
            break;

        case 1:
            if(f <= 0.0F)
                gunnerDead = true;
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
            hierMesh().chunkVisible("HMask1_D0", false);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D1", hierMesh().isChunkVisible("Pilot2_D0"));
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            gunnerDead = true;
            break;
        }
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        if(i == 2)
            gunnerEjected = true;
    }

//    private float flapps;

    static 
    {
        Class class1 = P_5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-5");
        Property.set(class1, "meshName", "3do/plane/R-5/hier-P5.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar08());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/P-5.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitP_5.class, CockpitP_5_Bombardier.class
        });
//        Aircraft.weaponTriggersRegister(class1, new int[] {
//            3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 
//            9
//        });
//        Aircraft.weaponHooksRegister(class1, new String[] {
//            "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev03", "_ExternalDev04", 
//            "_ExternalDev05"
//        });
        Aircraft.weaponTriggersRegister(class1, new int[] {});
        Aircraft.weaponHooksRegister(class1, new String[] {});
    }
}
