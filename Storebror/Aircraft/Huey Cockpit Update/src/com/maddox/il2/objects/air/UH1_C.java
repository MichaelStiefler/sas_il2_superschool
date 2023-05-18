package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class UH1_C extends HueyX implements TypeScout, TypeTransport, TypeStormovik {

    public UH1_C() {
        this.hoverThrustFactor1 = 1.1F;
        this.hoverThrustFactor2 = 1.03F;
        this.numTurrets = 3;
    }

    public void computeMass() {
        this.FM.M.massEmpty = Aircraft.cvt(this.FM.getSpeedKMH(), 100F, 180F, 2200F, 3000F);
    }

    static {
        final Class class1 = UH1_C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Huey_UH-1C");
        Property.set(class1, "meshName", "3DO/Plane/UH1_C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1986.5F);
        Property.set(class1, "FlightModel", "FlightModels/UH-1C.fmd:Huey_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHuey1B.class, CockpitUH1_CGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 9, 9, 9, 12, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_MGUN03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46",
                "_ExternalRock47", "_ExternalRock48" });
        try {
            final ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            final HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            final byte byte0 = 54;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunM60", 0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(12, "MGun40mmsGrenade", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xTurret40mmM5+48xRockets";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunM60", 0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunM60", 0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "ROCKPOD_Mount", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "ROCKPOD_Left", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "ROCKPOD_Right", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(12, "MGun40mmsGrenade", 300);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (final Exception exception) {
        }
    }
}
