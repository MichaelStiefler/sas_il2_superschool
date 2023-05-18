package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class F8F2 extends F8F {

    public F8F2() {
        this.flapps = 0.0F;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (!super.FM.isPlayers()) {
            float airSpeedPerSec = Pitot.Indicator((float) ((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z, super.FM.getSpeed());
            if ((super.FM.getAltitude() > 500F) || (airSpeedPerSec > 51.44F)) {
                ((FlightModelMain) (super.FM)).CT.cockpitDoorControl = 0.0F;
            } else {
                ((FlightModelMain) (super.FM)).CT.cockpitDoorControl = 1.0F;
            }
        }
    }

    public void update(float f) {
        super.update(f);
        float f_0_ = ((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f_0_) > 0.01F) {
            this.flapps = f_0_;
            for (int i = 1; i < 5; i++) {
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f_0_, 0.0F);
            }

        }
    }

    protected static void weaponsRegister(Class var_class, String string, String strings[]) {
        try {
            int triggers[] = Aircraft.getWeaponTriggersRegistered(var_class);
            int length = triggers.length;
            int count = strings.length;
            ArrayList arraylist = (ArrayList) Property.value(var_class, "weaponsList");
            if (arraylist == null) {
                arraylist = new ArrayList();
                Property.set(var_class, "weaponsList", arraylist);
            }
            HashMapInt hashmapint = (HashMapInt) Property.value(var_class, "weaponsMap");
            if (hashmapint == null) {
                hashmapint = new HashMapInt();
                Property.set(var_class, "weaponsMap", hashmapint);
            }
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[length];
// System.out.println("F8F2 Wepon Loading List:" + string);
            for (int i = 0; i < count; i++) {
                String weponName = strings[i];
                int weponCount = 1;
                if (weponName != null) {
                    for (int j = weponName.length() - 1; j > 0; j--) {
                        if (weponName.charAt(j) != ' ') {
                            continue;
                        }
                        try {
                            weponCount = Integer.parseInt(weponName.substring(j + 1));
                            weponName = weponName.substring(0, j);
                        } catch (Exception e) {
                            e.printStackTrace();
// System.out.println(strings[i] + ":" + weponName.substring(j + 1) + "(" + j + ")");
                        }
                        break;
                    }

// System.out.println(" No." + (i + 1) + ":" + weponName + "(" + weponCount + ")");
                    a_lweaponslot[i] = new Aircraft._WeaponSlot(triggers[i], weponName, weponCount);
                } else {
                    a_lweaponslot[i] = null;
                }
            }

            for (int i = count; i < length; i++) {
                a_lweaponslot[i] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), a_lweaponslot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float flapps;

    static {
        Class class1 = F8F2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F8F");
        Property.set(class1, "meshName", "3DO/Plane/F8F-2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/F8F-2/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1959.5F);
        Property.set(class1, "noseart", 1);
        Property.set(class1, "FlightModel", "FlightModels/F8F-2.fmd:URA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF8F2.class });
        Property.set(class1, "LOSElevation", 1.16055F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
        String as[];
        try {
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            weaponsRegister(class1, "default", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[13] = "RocketGunHVAR2 1";
            as[14] = "RocketGunHVAR2 1";
            as[15] = "RocketGunHVAR2 1";
            as[16] = "RocketGunHVAR2 1";
            weaponsRegister(class1, "4xhvar2", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "4xhvargp", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "4xhvarap", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            weaponsRegister(class1, "1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[13] = "RocketGunHVAR2 1";
            as[14] = "RocketGunHVAR2 1";
            as[15] = "RocketGunHVAR2 1";
            as[16] = "RocketGunHVAR2 1";
            weaponsRegister(class1, "1x150dt4xhvar2", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "1x150dt4xhvargp", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "1x150dt4xhvarap", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGunFAB50 1";
            as[8] = "BombGunFAB50 1";
            weaponsRegister(class1, "2x100", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGunFAB50 1";
            as[8] = "BombGunFAB50 1";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "2x100_4xhvargp", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGunFAB50 1";
            as[8] = "BombGunFAB50 1";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "2x100_4xhvargp1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGunFAB50 1";
            as[8] = "BombGunFAB50 1";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "2x100_4xhvarap", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGunFAB50 1";
            as[8] = "BombGunFAB50 1";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "2x100_4xhvarap1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGunFAB50 1";
            as[8] = "BombGunFAB50 1";
            weaponsRegister(class1, "2x100_1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGun250lbs 1";
            as[8] = "BombGun250lbs 1";
            weaponsRegister(class1, "2x250", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGun250lbs 1";
            as[8] = "BombGun250lbs 1";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "2x250_4xhvargp", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun250lbs 1";
            as[8] = "BombGun250lbs 1";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "2x250_4xhvargp1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGun250lbs 1";
            as[8] = "BombGun250lbs 1";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "2x250_4xhvarap", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun250lbs 1";
            as[8] = "BombGun250lbs 1";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "2x250_4xhvarap1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun250lbs 1";
            as[8] = "BombGun250lbs 1";
            weaponsRegister(class1, "2x250_1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[6] = "BombGun500lbs 1";
            weaponsRegister(class1, "1x500", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[6] = "BombGun500lbs 1";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "1x500_4xhvargp", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[6] = "BombGun500lbs 1";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "1x500_4xhvarap", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGun500lbs 1";
            as[8] = "BombGun500lbs 1";
            weaponsRegister(class1, "2x500", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGun500lbs 1";
            as[8] = "BombGun500lbs 1";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "2x500_4xhvargp", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGun500lbs 1";
            as[8] = "BombGun500lbs 1";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "2x500_4xhvarap", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun500lbs 1";
            as[8] = "BombGun500lbs 1";
            weaponsRegister(class1, "2x500_1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[6] = "BombGun1000lbs 1";
            weaponsRegister(class1, "1x1000", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[6] = "BombGun1000lbs 1";
            as[13] = "RocketGunHVAR5 1";
            as[14] = "RocketGunHVAR5 1";
            as[15] = "RocketGunHVAR5 1";
            as[16] = "RocketGunHVAR5 1";
            weaponsRegister(class1, "1x1000_4xhvargp", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[6] = "BombGun1000lbs 1";
            as[13] = "RocketGunHVAR5AP 1";
            as[14] = "RocketGunHVAR5AP 1";
            as[15] = "RocketGunHVAR5AP 1";
            as[16] = "RocketGunHVAR5AP 1";
            weaponsRegister(class1, "1x1000_4xhvarap", as);
            weaponsRegister(class1, "none", new String[19]);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun100lbs 1";
            as[8] = "BombGun100lbs 1";
            weaponsRegister(class1, "2x100_1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun110GalNapalm 1";
            as[8] = "BombGun110GalNapalm 1";
            weaponsRegister(class1, "2x110GalNapalm_1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun75GalNapalm 1";
            as[8] = "BombGun75GalNapalm 1";
            weaponsRegister(class1, "2x75GalNapalm_1x150dt", as);
            as = new String[19];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun75Napalm 1";
            as[8] = "BombGun75Napalm 1";
            weaponsRegister(class1, "2x75Napalm_1x150dt", as);
            weaponsRegister(class1, "2xTyniyTim_1x150dt", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", "FuelTankGun_Tank150galF8F 1", null, null, null, null, null, null, null, null, null, null, null, "RocketGunTinyTim 1", "RocketGunTinyTim 1" });
        } catch (Exception exception1) {
            exception1.printStackTrace();
        }
    }
}
