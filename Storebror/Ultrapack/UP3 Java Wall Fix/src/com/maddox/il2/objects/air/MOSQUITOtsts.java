package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.rts.Property;

public class MOSQUITOtsts extends MOSQUITO
    implements TypeFighter, TypeStormovik
{

    public MOSQUITOtsts()
    {
        phase = 0;
        disp = 0.0F;
        oldbullets = 0;
        g1 = null;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(thisWeaponsName.startsWith("2x") || thisWeaponsName.startsWith("L2x"))
        {
            hierMesh().chunkVisible("Rack1_D0", true);
            hierMesh().chunkVisible("Rack2_D0", true);
        } else
        {
            hierMesh().chunkVisible("Rack1_D0", false);
            hierMesh().chunkVisible("Rack2_D0", false);
        }
        if(thisWeaponsName.startsWith("Less") || thisWeaponsName.startsWith("L2x"))
        {
            hierMesh().chunkVisible("MGBar1", false);
            hierMesh().chunkVisible("MGBar2", false);
        } else
        {
            hierMesh().chunkVisible("MGBar1", true);
            hierMesh().chunkVisible("MGBar2", true);
        }
        if(FM.CT.Weapons[1] != null)
            g1 = FM.CT.Weapons[1][0];
    }

    public void update(float f)
    {
        if(g1 != null && oldbullets != g1.countBullets())
            switch(phase)
            {
            default:
                break;

            case 0:
                if(g1.isShots())
                {
                    oldbullets = g1.countBullets();
                    phase = 1;
                    disp = 0.0F;
                }
                break;

            case 1:
                disp += 12.6F * f;
                resetYPRmodifier();
                Aircraft.xyz[1] = disp;
                hierMesh().chunkSetLocate("Cannon_D0", Aircraft.xyz, Aircraft.ypr);
                if(disp >= 0.7F)
                    phase = 2;
                break;

            case 2:
                disp -= 1.2F * f;
                resetYPRmodifier();
                Aircraft.xyz[1] = disp;
                hierMesh().chunkSetLocate("Cannon_D0", Aircraft.xyz, Aircraft.ypr);
                if(disp <= 0.0F)
                    phase = 3;
                break;

            case 3:
                phase = 0;
                break;
            }
        super.update(f);
    }

    private int phase;
    private float disp;
    private int oldbullets;
    private BulletEmitter g1;

    static 
    {
        Class class1 = MOSQUITOtsts.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mosquito");
        Property.set(class1, "meshName", "3DO/Plane/Mosquito_FB_MkVI_Tse(Multi1)/TseTse_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_gb", "3DO/Plane/Mosquito_FB_MkVI_Tse(GB)/TseTse_hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mosquito-tsts.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMosquitoTseTse.class
        });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 1, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}
