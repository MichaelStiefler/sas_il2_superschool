
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class DC_10_30F extends DC_10family
{
    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "DC30F_";
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasSideDoor = true;
    }

    public void msgShot(Shot shot)
    {
        super.msgShot(shot);
        if(FM.AS.astateEngineStates[0] > 2 && FM.AS.astateEngineStates[1] > 2 && FM.AS.astateEngineStates[2] > 2)
            FM.setCapableOfBMP(false, shot.initiator);
    }


    static
    {
        Class class1 = DC_10_30F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/DC10-30fake.fmd:KC10fakeFM");
        Property.set(class1, "meshName", "3do/plane/KC-10(Multi1)/hierDC1030F.him");
        Property.set(class1, "iconFar_shortClassName", "DC-10");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1984F);
        Property.set(class1, "yearExpired", 2025F);
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitKC_10.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_BombSpawn01"
        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = 1;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
        }
    }
}
