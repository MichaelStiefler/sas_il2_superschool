package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Gamma2A5B extends Northropxyz {

    public void update(float f) {
        super.update(f);
        super.onAircraftLoaded();
        if (super.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("Blister2_D0", true);
            }
        }
        if (super.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D1", false);
            }
            this.hierMesh().chunkVisible("Blister2_D1", false);
            this.hierMesh().chunkVisible("Blister1_D2", false);
            this.hierMesh().chunkVisible("Blister2_D3", false);
        }
    }

    static {
        Class class1 = Gamma2A5B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Gamma");
        Property.set(class1, "meshName", "3DO/Plane/Gamma/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Gamma5B.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGamma.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
//        try
//        {
//            ArrayList arraylist = new ArrayList();
//            Property.set(class1, "weaponsList", arraylist);
//            HashMapInt hashmapint = new HashMapInt();
//            Property.set(class1, "weaponsMap", hashmapint);
//            byte byte0 = 15;
//            String s = "default";
//            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[4] = new Aircraft._WeaponSlot(10, "MGunShKASt", 1500);
//            for(int i = 5; i < byte0; i++)
//                a_lweaponslot[i] = null;
//
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "1xFAB250+4xFAB100";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[4] = new Aircraft._WeaponSlot(10, "MGunShKASt", 1500);
//            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250", 1);
//            a_lweaponslot[14] = null;
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "3xFAB250";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[4] = new Aircraft._WeaponSlot(10, "MGunShKASt", 1500);
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB250", 1);
//            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250", 1);
//            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250", 1);
//            a_lweaponslot[14] = null;
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "6xFAB100";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[4] = new Aircraft._WeaponSlot(10, "MGunShKASt", 1500);
//            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = null;
//            a_lweaponslot[14] = null;
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "1xFAB500";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunPV1i", 500);
//            a_lweaponslot[4] = new Aircraft._WeaponSlot(10, "MGunShKASt", 1500);
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = null;
//            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "none";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            for(int j = 0; j < byte0; j++)
//                a_lweaponslot[j] = null;
//
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//        }
//        catch(Exception exception) { }
    }
}
