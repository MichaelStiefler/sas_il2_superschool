package com.maddox.il2.objects.air;

import java.text.DecimalFormat;

import com.maddox.rts.Property;

public class ME_264_4M_801E extends ME_264
{
    static 
    {
        Class class1 = ME_264_4M_801E.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me-264");
        Property.set(class1, "meshName", "3DO/Plane/Me-264-4M-801E/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1955F);
        Property.set(class1, "FlightModel", "FlightModels/Me-264-4M-801E.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
                CockpitME_264.class, CockpitME_264_Bombardier.class, CockpitME_264_T1Gunner.class, CockpitME_264_T2Gunner.class, CockpitME_264_B1Gunner.class, CockpitME_264_B2Gunner.class
        });
        
        int slotLen = 253;
        
        int[] triggers = new int[slotLen];
        for (int hookIndex=0; hookIndex < 7; hookIndex++) triggers[hookIndex] = 10 + (hookIndex + 1) / 2;        
        for (int hookIndex=7; hookIndex < slotLen; hookIndex++) triggers[hookIndex] = 3;        
        
        DecimalFormat df = new DecimalFormat("##00");
        String[] hooks = new String[slotLen];
        for (int hookIndex=0; hookIndex < 7; hookIndex++) hooks[hookIndex] = "_MGUN0" + (hookIndex + 1);
        for (int hookIndex=7; hookIndex < slotLen; hookIndex++) hooks[hookIndex] = "_BombSpawn" + df.format((hookIndex - 7) / 2);
        
        Aircraft.weaponTriggersRegister(class1, triggers);
        Aircraft.weaponHooksRegister(class1, hooks);
    }
}
