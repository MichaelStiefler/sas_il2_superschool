package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Eff3DActor;
import com.maddox.rts.Property;

public class BF2C extends Hawk_3xyz {
    public BF2C() {
        this.arrestor = 0.0F;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -40F * f, 0.0F);
        this.arrestor = f;
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -26F, 11F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-42F * this.FM.Gears.arrestorVSink) / 37F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
    }

    protected float arrestor;

    static {
        Class class1 = BF2C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BF2C");
        Property.set(class1, "meshName", "3DO/Plane/BF2C/BF2C_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/Hawk3_nav.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHawk_3.class });
        Property.set(class1, "LOSElevation", 0.84305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 0, 3, 3, 9, 9, 3});
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb05" });
//        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 3 });
//        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb05" });
//        try {
//            ArrayList arraylist = new ArrayList();
//            Property.set(class1, "weaponsList", arraylist);
//            HashMapInt hashmapint = new HashMapInt();
//            Property.set(class1, "weaponsMap", hashmapint);
//            byte byte0 = 25;
//            String s = "default";
//            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 315);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning303k", 775);
//            a_lweaponslot[2] = null;
//            a_lweaponslot[3] = null;
//            a_lweaponslot[4] = null;
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = null;
//            a_lweaponslot[14] = null;
//            a_lweaponslot[15] = null;
//            a_lweaponslot[16] = null;
//            a_lweaponslot[17] = null;
//            a_lweaponslot[18] = null;
//            a_lweaponslot[19] = null;
//            a_lweaponslot[20] = null;
//            a_lweaponslot[21] = null;
//            a_lweaponslot[22] = null;
//            a_lweaponslot[23] = null;
//            a_lweaponslot[24] = null;
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "2x100lbs";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 315);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning303k", 775);
//            a_lweaponslot[2] = null;
//            a_lweaponslot[3] = null;
//            a_lweaponslot[4] = null;
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 1);
//            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 1);
//            a_lweaponslot[14] = null;
//            a_lweaponslot[15] = null;
//            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonRO_82_1", 1);
//            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonRO_82_1", 1);
//            a_lweaponslot[18] = null;
//            a_lweaponslot[19] = null;
//            a_lweaponslot[20] = null;
//            a_lweaponslot[21] = null;
//            a_lweaponslot[22] = null;
//            a_lweaponslot[23] = null;
//            a_lweaponslot[24] = null;
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "1x500lbs";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 315);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning303k", 775);
//            a_lweaponslot[2] = null;
//            a_lweaponslot[3] = null;
//            a_lweaponslot[4] = null;
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = null;
//            a_lweaponslot[14] = null;
//            a_lweaponslot[15] = null;
//            a_lweaponslot[16] = null;
//            a_lweaponslot[17] = null;
//            a_lweaponslot[18] = null;
//            a_lweaponslot[19] = null;
//            a_lweaponslot[20] = null;
//            a_lweaponslot[21] = null;
//            a_lweaponslot[22] = null;
//            a_lweaponslot[23] = null;
//            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "1xdt";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 315);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning303k", 775);
//            a_lweaponslot[2] = null;
//            a_lweaponslot[3] = null;
//            a_lweaponslot[4] = null;
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = null;
//            a_lweaponslot[14] = null;
//            a_lweaponslot[15] = null;
//            a_lweaponslot[16] = null;
//            a_lweaponslot[17] = null;
//            a_lweaponslot[18] = null;
//            a_lweaponslot[19] = null;
//            a_lweaponslot[20] = null;
//            a_lweaponslot[21] = null;
//            a_lweaponslot[22] = null;
//            a_lweaponslot[23] = null;
//            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank75gal2", 1);
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "none";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = null;
//            for (int i = 25; i < byte0; i++) {
//                a_lweaponslot[i] = null;
//            }
//
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//        } catch (Exception exception) {
//        }
    }
}
