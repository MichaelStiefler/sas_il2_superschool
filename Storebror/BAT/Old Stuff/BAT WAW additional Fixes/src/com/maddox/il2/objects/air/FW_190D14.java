package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class FW_190D14 extends FW_190DB603NEW {

    public FW_190D14() {
        this.kangle = 0.0F;
    }

    protected void moveGear(float f) {
        FW_190DB603NEW.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (((FlightModelMain) (super.FM)).CT.getGear() >= 0.98F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
        ((FlightModelMain) (super.FM)).Sq.dragParasiteCx = 0.0F;
    }

    public void update(float f) {
        for (int i = 1; i < 15; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * ((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator());
        super.update(f);
    }

    private float kangle;

    static {
        Class class1 = FW_190D14.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190D-14(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1946.6F);
        Property.set(class1, "yearExpired", 1948F);
        byte byte0 = 9;
        try {
            if (FlightModelMain.sectFile("FlightModels/Fw-190D-14.fmd") != null) {
                byte0 = 15;
            } else if (FlightModelMain.sectFile("FlightModels/Fw-190D-15.fmd") != null) {
                byte0 = 13;
            }
        } catch (Exception exception) {
        }
        switch (byte0) {
            case 9: // '\t'
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-9.fmd");
// System.out.println("Fw190-D15 Loading FMD: FlightModels/Fw-190D-9.fmd");
                break;

            case 13: // '\r'
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-14.fmd");
// System.out.println("Fw190-D15 Loading FMD: FlightModels/Fw-190D-14.fmd");
                break;

            case 15: // '\017'
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-15.fmd");
// System.out.println("Fw190-D15 Loading FMD: FlightModels/Fw-190D-15.fmd");
                break;

            case 10: // '\n'
            case 11: // '\013'
            case 12: // '\f'
            case 14: // '\016'
            default:
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-9.fmd");
// System.out.println("Fw190-D15 Loading FMD: FlightModels/Fw-190D-9.fmd");
                break;
        }
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D14.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 9, 9, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 1, 1, 9, 9, 2, 2, 9, 9, 1, 1, 9, 9, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalDev02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev05", "_ExternalDev06", "_CANNON09", "_CANNON10", "_ExternalDev07", "_ExternalDev08", "_CANNON11", "_CANNON12", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_CANNON13", "_CANNON14", "_CANNON15" });
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte1 = 32;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte1];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            for (int i = 2; i < 31; i++) {
                a_lweaponslot[i] = null;
            }

            a_lweaponslot[31] = new Aircraft._WeaponSlot(1, "MGunMG15120MGki", 220);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "DT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte1];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            for (int j = 2; j < 25; j++) {
                a_lweaponslot[j] = null;
            }

            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "PylonETC504FW190", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            for (int k = 27; k < 31; k++) {
                a_lweaponslot[k] = null;
            }

            a_lweaponslot[31] = new Aircraft._WeaponSlot(1, "MGunMG15120MGki", 220);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "SC500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte1];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunSC500", 1);
            for (int l = 3; l < 25; l++) {
                a_lweaponslot[l] = null;
            }

            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "PylonETC504FW190", 1);
            for (int i1 = 26; i1 < 31; i1++) {
                a_lweaponslot[i1] = null;
            }

            a_lweaponslot[31] = new Aircraft._WeaponSlot(1, "MGunMG15120MGki", 220);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "MK108";
            a_lweaponslot = new Aircraft._WeaponSlot[byte1];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            for (int j1 = 2; j1 < 31; j1++) {
                a_lweaponslot[j1] = null;
            }

            a_lweaponslot[31] = new Aircraft._WeaponSlot(1, "MGunMK108k", 85);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "MK108+DT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte1];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            for (int k1 = 2; k1 < 25; k1++) {
                a_lweaponslot[k1] = null;
            }

            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "PylonETC504FW190", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            for (int l1 = 27; l1 < 31; l1++) {
                a_lweaponslot[l1] = null;
            }

            a_lweaponslot[31] = new Aircraft._WeaponSlot(1, "MGunMK108k", 85);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "MK108+SC500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte1];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunMG15120MGs", 250);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunSC500", 1);
            for (int i2 = 3; i2 < 25; i2++) {
                a_lweaponslot[i2] = null;
            }

            a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "PylonETC504FW190", 1);
            for (int j2 = 26; j2 < 31; j2++) {
                a_lweaponslot[j2] = null;
            }

            a_lweaponslot[31] = new Aircraft._WeaponSlot(1, "MGunMK108k", 85);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte1];
            for (int k2 = 0; k2 < byte1; k2++) {
                a_lweaponslot[k2] = null;
            }

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception1) {
        }
    }
}
