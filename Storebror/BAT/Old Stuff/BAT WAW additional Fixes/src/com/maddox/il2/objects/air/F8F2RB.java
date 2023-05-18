// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode
// Source File Name: F8F2RB.java

package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

// Referenced classes of package com.maddox.il2.objects.air:
// F8F, PaintSchemeFMPar05, Aircraft, NetAircraft

public class F8F2RB extends F8F {

    public F8F2RB() {
        this.flapps = 0.0F;
    }

    protected void moveFlap(float f1) {
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
// System.out.println("F8F2RB Wepon Loading List:" + string);
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
        Class class1 = F8F2RB.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F8F");
        Property.set(class1, "meshName", "3DO/Plane/F8F-2_RB/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/F8F-2_RB/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 2100F);
        Property.set(class1, "noseart", 1);
        Property.set(class1, "FlightModel", "FlightModels/RareBear.fmd:URA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF8F2.class });
        Property.set(class1, "LOSElevation", 1.16055F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04" });
        String as[];
        try {
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            weaponsRegister(class1, "default", as);
            weaponsRegister(class1, "4xhvar2", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, null, null, null, null, null, null, "RocketGunHVAR2 1", "RocketGunHVAR2 1", "RocketGunHVAR2 1", "RocketGunHVAR2 1" });
            weaponsRegister(class1, "4xhvargp", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, null, null, null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "4xhvarap", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, null, null, null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            weaponsRegister(class1, "1x150dt", as);
            weaponsRegister(class1, "1x150dt4xhvar2", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", "FuelTankGun_Tank150galF8F 1", null, null, null, null, null, null, null, "RocketGunHVAR2 1", "RocketGunHVAR2 1", "RocketGunHVAR2 1", "RocketGunHVAR2 1" });
            weaponsRegister(class1, "1x150dt4xhvargp", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", "FuelTankGun_Tank150galF8F 1", null, null, null, null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "1x150dt4xhvarap", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", "FuelTankGun_Tank150galF8F 1", null, null, null, null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGunFAB50 1";
            as[8] = "BombGunFAB50 1";
            weaponsRegister(class1, "2x100", as);
            weaponsRegister(class1, "2x100_4xhvargp", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, "BombGunFAB50 1", "BombGunFAB50 1", null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "2x100_4xhvargp1x150dt", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", "FuelTankGun_Tank150galF8F 1", null, "BombGunFAB50 1", "BombGunFAB50 1", null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "2x100_4xhvarap", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, "BombGunFAB50 1", "BombGunFAB50 1", null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            weaponsRegister(class1, "2x100_4xhvarap1x150dt", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", "FuelTankGun_Tank150galF8F 1", null, "BombGunFAB50 1", "BombGunFAB50 1", null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGunFAB50 1";
            as[8] = "BombGunFAB50 1";
            weaponsRegister(class1, "2x100_1x150dt", as);
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGun250lbs 1";
            as[8] = "BombGun250lbs 1";
            weaponsRegister(class1, "2x250", as);
            weaponsRegister(class1, "2x250_4xhvargp", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "2x250_4xhvargp1x150dt", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", "FuelTankGun_Tank150galF8F 1", null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "2x250_4xhvarap", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            weaponsRegister(class1, "2x250_4xhvarap1x150dt", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", "FuelTankGun_Tank150galF8F 1", null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun250lbs 1";
            as[8] = "BombGun250lbs 1";
            weaponsRegister(class1, "2x250_1x150dt", as);
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[6] = "BombGun500lbs 1";
            weaponsRegister(class1, "1x500", as);
            weaponsRegister(class1, "1x500_4xhvargp", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", null, "BombGun500lbs 1", null, null, null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "1x500_4xhvarap", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", null, "BombGun500lbs 1", null, null, null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[7] = "BombGun500lbs 1";
            as[8] = "BombGun500lbs 1";
            weaponsRegister(class1, "2x500", as);
            weaponsRegister(class1, "2x500_4xhvargp", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "2x500_4xhvarap", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", null, null, null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[5] = "FuelTankGun_Tank150galF8F 1";
            as[7] = "BombGun500lbs 1";
            as[8] = "BombGun500lbs 1";
            weaponsRegister(class1, "2x500_1x150dt", as);
            as = new String[17];
            as[0] = "MGunHispanoMkIkWF 205";
            as[1] = "MGunHispanoMkIkWF 205";
            as[2] = "MGunHispanoMkIkWF 205";
            as[3] = "MGunHispanoMkIkWF 205";
            as[4] = "PylonF8F 1";
            as[6] = "BombGun1000lbs 1";
            weaponsRegister(class1, "1x1000", as);
            weaponsRegister(class1, "1x1000_4xhvargp", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", null, "BombGun1000lbs 1", null, null, null, null, null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
            weaponsRegister(class1, "1x1000_4xhvarap", new String[] { "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "MGunHispanoMkIkWF 205", "PylonF8F 1", null, "BombGun1000lbs 1", null, null, null, null, null, null, "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1", "RocketGunHVAR5AP 1" });
            weaponsRegister(class1, "none", new String[17]);
        } catch (Exception exception1) {
            exception1.printStackTrace();
        }
    }
}
