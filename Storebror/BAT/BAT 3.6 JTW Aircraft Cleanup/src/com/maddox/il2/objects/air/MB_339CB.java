package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.Regiment;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import com.maddox.util.NumberTokenizer;

public class MB_339CB extends MB_339Modern {

    public MB_339CB() {
    }

    protected static void XweaponsRegister(Class class1, String s, String as[]) {
        if (as.length != Aircraft.getWeaponHooksRegistered(class1).length) {
            throw new RuntimeException("Sizeof 'weaponSlots' != sizeof 'weaponHooks'");
        }
        int ai[] = Aircraft.getWeaponTriggersRegistered(class1);
        ArrayList arraylist = MB_339CB.XweaponsListProperty(class1);
        HashMapInt hashmapint = MB_339CB.XweaponsMapProperty(class1);
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[as.length];
        try {
            for (int i = 0; i < as.length; i++) {
                if (as[i] != null) {
                    NumberTokenizer numbertokenizer = new NumberTokenizer(as[i]);
                    a_lweaponslot[i] = new Aircraft._WeaponSlot(ai[i], numbertokenizer.next(null), numbertokenizer.next(-12345));
                }
            }

        } catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
        int j = arraylist.indexOf(s);
        if (j >= 0) {
            arraylist.remove(j);
        }
        arraylist.add(s);
        hashmapint.put(Finger.Int(s), a_lweaponslot);
    }

    private static ArrayList XweaponsListProperty(Class class1) {
        Object obj = Property.value(class1, "weaponsList", null);
        if (obj != null) {
            return (ArrayList) obj;
        } else {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            return arraylist;
        }
    }

    private static HashMapInt XweaponsMapProperty(Class class1) {
        Object obj = Property.value(class1, "weaponsMap", null);
        if (obj != null) {
            return (HashMapInt) obj;
        } else {
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            return hashmapint;
        }
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "CB_";
    }

    static {
        Class class1 = MB_339CB.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MB-339");
        Property.set(class1, "meshName", "3DO/Plane/MB-339/hierC.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1979.9F);
        Property.set(class1, "yearExpired", 2020.3F);
        Property.set(class1, "FlightModel", "FlightModels/MB-339CB.fmd:MB339FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMB339C.class, CockpitT_33i.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 0, 0, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_MGUN03", "_MGUN04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Rock05", "_Rock06", "_ExternalDev09", "_ExternalDev10", "_Rock07", "_Rock08" });
    }
}
