package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class MustangMkI extends P_51
{

    public MustangMkI()
    {
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        if(f <= 0.5D)
        {
            hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f * 2.0F, 0.0F);
        } else
        {
            float f1 = (f - 0.5F) * 2.0F;
            hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -185F * f1, 0.0F);
        }
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    static 
    {
        Class class1 = MustangMkI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-51");
        Property.set(class1, "meshName", "3DO/Plane/MustangMkI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_gb", "3DO/Plane/MustangMkI(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1943.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-51A.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitP_51B.class
        });
        Property.set(class1, "LOSElevation", 1.03F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 0, 0
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08"
        });
    }
}
