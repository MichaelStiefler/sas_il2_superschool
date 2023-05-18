package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class F2G extends F4U implements TypeX4Carrier {

    public F2G() {
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.002F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.002F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.002F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    protected void mydebug(String s) {
    }

    protected static void weaponsRegister(Class class1, String s, String as[]) {
        try {
            int ai[] = Aircraft.getWeaponTriggersRegistered(class1);
            int i = ai.length;
            int j = as.length;
            ArrayList arraylist = (ArrayList) Property.value(class1, "weaponsList");
            if (arraylist == null) {
                arraylist = new ArrayList();
                Property.set(class1, "weaponsList", arraylist);
            }
            HashMapInt hashmapint = (HashMapInt) Property.value(class1, "weaponsMap");
            if (hashmapint == null) {
                hashmapint = new HashMapInt();
                Property.set(class1, "weaponsMap", hashmapint);
            }
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
// System.out.println("F2G Wepon Loading List:" + s);
            for (int k = 0; k < j; k++) {
                String s1 = as[k];
                int i1 = 1;
                if (s1 != null) {
                    for (int j1 = s1.length() - 1; j1 > 0; j1--) {
                        if (s1.charAt(j1) != ' ') {
                            continue;
                        }
                        try {
                            i1 = Integer.parseInt(s1.substring(j1 + 1));
                            s1 = s1.substring(0, j1);
                        } catch (Exception exception1) {
// System.out.println(as[k] + ":" + s1.substring(j1 + 1) + "(" + j1 + ")");
                        }
                        break;
                    }

// System.out.println(" No." + (k + 1) + ":" + s1 + "(" + i1 + ")");
                    a_lweaponslot[k] = new Aircraft._WeaponSlot(ai[k], s1, i1);
                } else {
                    a_lweaponslot[k] = null;
                }
            }

            for (int l = j; l < i; l++) {
                a_lweaponslot[l] = null;
            }

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean        bToFire;
    private float         deltaAzimuth;
    private float         deltaTangage;
    public static boolean bChangedPit = false;
    public float          fSightCurDistance;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;

    static {
        Class class1 = com.maddox.il2.objects.air.F2G.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F2G");
        Property.set(class1, "meshName", "3DO/Plane/F2G(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/F2G(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 2100.5F);
        byte byte0 = 6;
        try {
            if (FlightModelMain.sectFile("FlightModels/F2G.fmd:URA") != null) {
                byte0 = 10;
            } else if (FlightModelMain.sectFile("FlightModels/F8F-2.fmd") != null) {
                byte0 = 8;
            } else if (FlightModelMain.sectFile("FlightModels/SeaFuryMkI.fmd") != null) {
                byte0 = 0;
            } else {
                byte0 = 6;
            }
        } catch (Exception exception) {
        }
        switch (byte0) {
            case 10: // '\n'
                Property.set(class1, "FlightModel", "FlightModels/F2G.fmd:URA");
// System.out.println("F2G Loading FMD: FlightModels/F2G.fmd:URA");
                break;

            case 8: // '\b'
                Property.set(class1, "FlightModel", "FlightModels/F8F-2.fmd");
// System.out.println("F2G Loading FMD: FlightModels/F8F-2.fmd");
                break;

            case 0: // '\0'
                Property.set(class1, "FlightModel", "FlightModels/SeaFuryMkI.fmd");
// System.out.println("F2G Loading FMD: FlightModels/SeaFuryMkI.fmd");
                break;

            default:
                Property.set(class1, "FlightModel", "FlightModels/F4U-1C.fmd");
// System.out.println("F2G Loading FMD: FlightModels/F4U-1C.fmd");
                break;
        }
        Property.set(class1, "cockpitClass", new Class[] { com.maddox.il2.objects.air.CockpitF2G.class });
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
        String as[] = new String[21];
        as[0] = "MGunBrowning50kWF 400";
        as[1] = "MGunBrowning50kWF 400";
        as[2] = "MGunBrowning50kWF 400";
        as[3] = "MGunBrowning50kWF 400";
        weaponsRegister(class1, "default", as);
        String as1[] = new String[21];
        as1[0] = "MGunBrowning50kWF 400";
        as1[1] = "MGunBrowning50kWF 400";
        as1[2] = "MGunBrowning50kWF 400";
        as1[3] = "MGunBrowning50kWF 400";
        as1[13] = "RocketGunHVAR5 1";
        as1[14] = "RocketGunHVAR5 1";
        as1[15] = "RocketGunHVAR5 1";
        as1[16] = "RocketGunHVAR5 1";
        as1[17] = "RocketGunHVAR5 1";
        as1[18] = "RocketGunHVAR5 1";
        as1[19] = "RocketGunHVAR5 1";
        as1[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "8xhvargp", as1);
        String as2[] = new String[21];
        as2[0] = "MGunBrowning50kWF 400";
        as2[1] = "MGunBrowning50kWF 400";
        as2[2] = "MGunBrowning50kWF 400";
        as2[3] = "MGunBrowning50kWF 400";
        as2[4] = "PylonF4UPLN3";
        as2[5] = "PylonF4UPLN3";
        as2[9] = "FuelTankGun_Tank154gal";
        as2[10] = "FuelTankGun_Tank154gal";
        as2[13] = "RocketGunHVAR5 1";
        as2[14] = "RocketGunHVAR5 1";
        as2[15] = "RocketGunHVAR5 1";
        as2[16] = "RocketGunHVAR5 1";
        as2[17] = "RocketGunHVAR5 1";
        as2[18] = "RocketGunHVAR5 1";
        as2[19] = "RocketGunHVAR5 1";
        as2[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "8xhvargp2x154dt", as2);
        String as3[] = new String[21];
        as3[0] = "MGunBrowning50kWF 400";
        as3[1] = "MGunBrowning50kWF 400";
        as3[2] = "MGunBrowning50kWF 400";
        as3[3] = "MGunBrowning50kWF 400";
        as3[13] = "RocketGunHVAR5AP 1";
        as3[14] = "RocketGunHVAR5AP 1";
        as3[15] = "RocketGunHVAR5AP 1";
        as3[16] = "RocketGunHVAR5AP 1";
        as3[17] = "RocketGunHVAR5AP 1";
        as3[18] = "RocketGunHVAR5AP 1";
        as3[19] = "RocketGunHVAR5AP 1";
        as3[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "8xhvarap", as3);
        String as4[] = new String[21];
        as4[0] = "MGunBrowning50kWF 400";
        as4[1] = "MGunBrowning50kWF 400";
        as4[2] = "MGunBrowning50kWF 400";
        as4[3] = "MGunBrowning50kWF 400";
        as4[4] = "PylonF4UPLN3";
        as4[5] = "PylonF4UPLN3";
        as4[9] = "FuelTankGun_Tank154gal";
        as4[10] = "FuelTankGun_Tank154gal";
        as4[13] = "RocketGunHVAR5AP 1";
        as4[14] = "RocketGunHVAR5AP 1";
        as4[15] = "RocketGunHVAR5AP 1";
        as4[16] = "RocketGunHVAR5AP 1";
        as4[17] = "RocketGunHVAR5AP 1";
        as4[18] = "RocketGunHVAR5AP 1";
        as4[19] = "RocketGunHVAR5AP 1";
        as4[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "8xhvarap2x154dt", as4);
        String as5[] = new String[21];
        as5[0] = "MGunBrowning50kWF 400";
        as5[1] = "MGunBrowning50kWF 400";
        as5[2] = "MGunBrowning50kWF 400";
        as5[3] = "MGunBrowning50kWF 400";
        as5[4] = "PylonF4UPLN3";
        as5[5] = "PylonF4UPLN3";
        as5[9] = "FuelTankGun_Tank154gal";
        as5[10] = "FuelTankGun_Tank154gal";
        weaponsRegister(class1, "2x154dt", as5);
        String as6[] = new String[21];
        as6[0] = "MGunBrowning50kWF 400";
        as6[1] = "MGunBrowning50kWF 400";
        as6[2] = "MGunBrowning50kWF 400";
        as6[3] = "MGunBrowning50kWF 400";
        as6[4] = "PylonF4UPLN3";
        as6[5] = "PylonF4UPLN3";
        as6[11] = "RocketGunTinyTim 1";
        as6[12] = "RocketGunTinyTim 1";
        as6[13] = "RocketGunHVAR5 1";
        as6[14] = "RocketGunHVAR5 1";
        as6[15] = "RocketGunHVAR5 1";
        as6[16] = "RocketGunHVAR5 1";
        as6[17] = "RocketGunHVAR5 1";
        as6[18] = "RocketGunHVAR5 1";
        as6[19] = "RocketGunHVAR5 1";
        as6[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "2xtt8xhvargp", as6);
        String as7[] = new String[21];
        as7[0] = "MGunBrowning50kWF 400";
        as7[1] = "MGunBrowning50kWF 400";
        as7[2] = "MGunBrowning50kWF 400";
        as7[3] = "MGunBrowning50kWF 400";
        as7[4] = "PylonF4UPLN3";
        as7[5] = "PylonF4UPLN3";
        as7[11] = "RocketGunTinyTim 1";
        as7[12] = "RocketGunTinyTim 1";
        as7[13] = "RocketGunHVAR5AP 1";
        as7[14] = "RocketGunHVAR5AP 1";
        as7[15] = "RocketGunHVAR5AP 1";
        as7[16] = "RocketGunHVAR5AP 1";
        as7[17] = "RocketGunHVAR5AP 1";
        as7[18] = "RocketGunHVAR5AP 1";
        as7[19] = "RocketGunHVAR5AP 1";
        as7[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "2xtt8xhvarap", as7);
        String as8[] = new String[21];
        as8[0] = "MGunBrowning50kWF 400";
        as8[1] = "MGunBrowning50kWF 400";
        as8[2] = "MGunBrowning50kWF 400";
        as8[3] = "MGunBrowning50kWF 400";
        as8[4] = "PylonF4UPLN3";
        as8[5] = "PylonF4UPLN3";
        as8[8] = "BombGun500lbs 1";
        as8[9] = "FuelTankGun_Tank154gal";
        as8[10] = "FuelTankGun_Tank154gal";
        weaponsRegister(class1, "1x5002x154dt", as8);
        String as9[] = new String[21];
        as9[0] = "MGunBrowning50kWF 400";
        as9[1] = "MGunBrowning50kWF 400";
        as9[2] = "MGunBrowning50kWF 400";
        as9[3] = "MGunBrowning50kWF 400";
        as9[4] = "PylonF4UPLN3";
        as9[5] = "PylonF4UPLN3";
        as9[8] = "BombGun500lbs 1";
        as9[9] = "FuelTankGun_Tank154gal";
        as9[10] = "FuelTankGun_Tank154gal";
        as9[13] = "RocketGunHVAR5 1";
        as9[14] = "RocketGunHVAR5 1";
        as9[15] = "RocketGunHVAR5 1";
        as9[16] = "RocketGunHVAR5 1";
        as9[17] = "RocketGunHVAR5 1";
        as9[18] = "RocketGunHVAR5 1";
        as9[19] = "RocketGunHVAR5 1";
        as9[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "1x5008xhvargp2x154dt", as9);
        String as10[] = new String[21];
        as10[0] = "MGunBrowning50kWF 400";
        as10[1] = "MGunBrowning50kWF 400";
        as10[2] = "MGunBrowning50kWF 400";
        as10[3] = "MGunBrowning50kWF 400";
        as10[4] = "PylonF4UPLN3";
        as10[5] = "PylonF4UPLN3";
        as10[8] = "BombGun500lbs 1";
        as10[9] = "FuelTankGun_Tank154gal";
        as10[10] = "FuelTankGun_Tank154gal";
        as10[13] = "RocketGunHVAR5AP 1";
        as10[14] = "RocketGunHVAR5AP 1";
        as10[15] = "RocketGunHVAR5AP 1";
        as10[16] = "RocketGunHVAR5AP 1";
        as10[17] = "RocketGunHVAR5AP 1";
        as10[18] = "RocketGunHVAR5AP 1";
        as10[19] = "RocketGunHVAR5AP 1";
        as10[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "1x5008xhvarap2x154dt", as10);
        String as11[] = new String[21];
        as11[0] = "MGunBrowning50kWF 400";
        as11[1] = "MGunBrowning50kWF 400";
        as11[2] = "MGunBrowning50kWF 400";
        as11[3] = "MGunBrowning50kWF 400";
        as11[4] = "PylonF4UPLN3";
        as11[5] = "PylonF4UPLN3";
        as11[6] = "BombGun500lbs 1";
        as11[7] = "BombGun500lbs 1";
        weaponsRegister(class1, "2x500", as11);
        String as12[] = new String[21];
        as12[0] = "MGunBrowning50kWF 400";
        as12[1] = "MGunBrowning50kWF 400";
        as12[2] = "MGunBrowning50kWF 400";
        as12[3] = "MGunBrowning50kWF 400";
        as12[4] = "PylonF4UPLN3";
        as12[5] = "PylonF4UPLN3";
        as12[6] = "BombGun500lbs 1";
        as12[7] = "BombGun500lbs 1";
        as12[13] = "RocketGunHVAR5 1";
        as12[14] = "RocketGunHVAR5 1";
        as12[15] = "RocketGunHVAR5 1";
        as12[16] = "RocketGunHVAR5 1";
        as12[17] = "RocketGunHVAR5 1";
        as12[18] = "RocketGunHVAR5 1";
        as12[19] = "RocketGunHVAR5 1";
        as12[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "2x5008xhvargp", as12);
        String as13[] = new String[21];
        as13[0] = "MGunBrowning50kWF 400";
        as13[1] = "MGunBrowning50kWF 400";
        as13[2] = "MGunBrowning50kWF 400";
        as13[3] = "MGunBrowning50kWF 400";
        as13[4] = "PylonF4UPLN3";
        as13[5] = "PylonF4UPLN3";
        as13[6] = "BombGun500lbs 1";
        as13[7] = "BombGun500lbs 1";
        as13[13] = "RocketGunHVAR5AP 1";
        as13[14] = "RocketGunHVAR5AP 1";
        as13[15] = "RocketGunHVAR5AP 1";
        as13[16] = "RocketGunHVAR5AP 1";
        as13[17] = "RocketGunHVAR5AP 1";
        as13[18] = "RocketGunHVAR5AP 1";
        as13[19] = "RocketGunHVAR5AP 1";
        as13[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "2x5008xhvarap", as13);
        String as14[] = new String[21];
        as14[0] = "MGunBrowning50kWF 400";
        as14[1] = "MGunBrowning50kWF 400";
        as14[2] = "MGunBrowning50kWF 400";
        as14[3] = "MGunBrowning50kWF 400";
        as14[4] = "PylonF4UPLN3";
        as14[5] = "PylonF4UPLN3";
        as14[6] = "BombGun500lbs 1";
        as14[7] = "BombGun500lbs 1";
        as14[8] = "BombGun500lbs 1";
        weaponsRegister(class1, "3x500", as14);
        String as15[] = new String[21];
        as15[0] = "MGunBrowning50kWF 400";
        as15[1] = "MGunBrowning50kWF 400";
        as15[2] = "MGunBrowning50kWF 400";
        as15[3] = "MGunBrowning50kWF 400";
        as15[4] = "PylonF4UPLN3";
        as15[5] = "PylonF4UPLN3";
        as15[6] = "BombGun500lbs 1";
        as15[7] = "BombGun500lbs 1";
        as15[8] = "BombGun500lbs 1";
        as15[13] = "RocketGunHVAR5 1";
        as15[14] = "RocketGunHVAR5 1";
        as15[15] = "RocketGunHVAR5 1";
        as15[16] = "RocketGunHVAR5 1";
        as15[17] = "RocketGunHVAR5 1";
        as15[18] = "RocketGunHVAR5 1";
        as15[19] = "RocketGunHVAR5 1";
        as15[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "3x5008xhvargp", as15);
        String as16[] = new String[21];
        as16[0] = "MGunBrowning50kWF 400";
        as16[1] = "MGunBrowning50kWF 400";
        as16[2] = "MGunBrowning50kWF 400";
        as16[3] = "MGunBrowning50kWF 400";
        as16[4] = "PylonF4UPLN3";
        as16[5] = "PylonF4UPLN3";
        as16[6] = "BombGun500lbs 1";
        as16[7] = "BombGun500lbs 1";
        as16[8] = "BombGun500lbs 1";
        as16[13] = "RocketGunHVAR5AP 1";
        as16[14] = "RocketGunHVAR5AP 1";
        as16[15] = "RocketGunHVAR5AP 1";
        as16[16] = "RocketGunHVAR5AP 1";
        as16[17] = "RocketGunHVAR5AP 1";
        as16[18] = "RocketGunHVAR5AP 1";
        as16[19] = "RocketGunHVAR5AP 1";
        as16[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "3x5008xhvarap", as16);
        String as17[] = new String[21];
        as17[0] = "MGunBrowning50kWF 400";
        as17[1] = "MGunBrowning50kWF 400";
        as17[2] = "MGunBrowning50kWF 400";
        as17[3] = "MGunBrowning50kWF 400";
        as17[4] = "PylonF4UPLN3";
        as17[5] = "PylonF4UPLN3";
        as17[8] = "BombGun1000lbs 1";
        as17[9] = "FuelTankGun_Tank154gal";
        as17[10] = "FuelTankGun_Tank154gal";
        weaponsRegister(class1, "1x10002x154dt", as17);
        String as18[] = new String[21];
        as18[0] = "MGunBrowning50kWF 400";
        as18[1] = "MGunBrowning50kWF 400";
        as18[2] = "MGunBrowning50kWF 400";
        as18[3] = "MGunBrowning50kWF 400";
        as18[4] = "PylonF4UPLN3";
        as18[5] = "PylonF4UPLN3";
        as18[8] = "BombGun1000lbs 1";
        as18[11] = "RocketGunTinyTim";
        as18[12] = "RocketGunTinyTim";
        weaponsRegister(class1, "1x10002xtt", as18);
        String as19[] = new String[21];
        as19[0] = "MGunBrowning50kWF 400";
        as19[1] = "MGunBrowning50kWF 400";
        as19[2] = "MGunBrowning50kWF 400";
        as19[3] = "MGunBrowning50kWF 400";
        as19[8] = "BombGun1000lbs 1";
        as19[13] = "RocketGunHVAR5 1";
        as19[14] = "RocketGunHVAR5 1";
        as19[15] = "RocketGunHVAR5 1";
        as19[16] = "RocketGunHVAR5 1";
        as19[17] = "RocketGunHVAR5 1";
        as19[18] = "RocketGunHVAR5 1";
        as19[19] = "RocketGunHVAR5 1";
        as19[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "1x10008xhvargp", as19);
        String as20[] = new String[21];
        as20[0] = "MGunBrowning50kWF 400";
        as20[1] = "MGunBrowning50kWF 400";
        as20[2] = "MGunBrowning50kWF 400";
        as20[3] = "MGunBrowning50kWF 400";
        as20[4] = "PylonF4UPLN3";
        as20[5] = "PylonF4UPLN3";
        as20[8] = "BombGun1000lbs 1";
        as20[9] = "FuelTankGun_Tank154gal";
        as20[10] = "FuelTankGun_Tank154gal";
        as20[13] = "RocketGunHVAR5 1";
        as20[14] = "RocketGunHVAR5 1";
        as20[15] = "RocketGunHVAR5 1";
        as20[16] = "RocketGunHVAR5 1";
        as20[17] = "RocketGunHVAR5 1";
        as20[18] = "RocketGunHVAR5 1";
        as20[19] = "RocketGunHVAR5 1";
        as20[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "1x10008xhvargp2x154dt", as20);
        String as21[] = new String[21];
        as21[0] = "MGunBrowning50kWF 400";
        as21[1] = "MGunBrowning50kWF 400";
        as21[2] = "MGunBrowning50kWF 400";
        as21[3] = "MGunBrowning50kWF 400";
        as21[8] = "BombGun1000lbs 1";
        as21[13] = "RocketGunHVAR5AP 1";
        as21[14] = "RocketGunHVAR5AP 1";
        as21[15] = "RocketGunHVAR5AP 1";
        as21[16] = "RocketGunHVAR5AP 1";
        as21[17] = "RocketGunHVAR5AP 1";
        as21[18] = "RocketGunHVAR5AP 1";
        as21[19] = "RocketGunHVAR5AP 1";
        as21[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "1x10008xhvarap", as21);
        String as22[] = new String[21];
        as22[0] = "MGunBrowning50kWF 400";
        as22[1] = "MGunBrowning50kWF 400";
        as22[2] = "MGunBrowning50kWF 400";
        as22[3] = "MGunBrowning50kWF 400";
        as22[4] = "PylonF4UPLN3";
        as22[5] = "PylonF4UPLN3";
        as22[8] = "BombGun1000lbs 1";
        as22[9] = "FuelTankGun_Tank154gal";
        as22[10] = "FuelTankGun_Tank154gal";
        as22[13] = "RocketGunHVAR5AP 1";
        as22[14] = "RocketGunHVAR5AP 1";
        as22[15] = "RocketGunHVAR5AP 1";
        as22[16] = "RocketGunHVAR5AP 1";
        as22[17] = "RocketGunHVAR5AP 1";
        as22[18] = "RocketGunHVAR5AP 1";
        as22[19] = "RocketGunHVAR5AP 1";
        as22[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "1x10002x154dt8xhvarap", as22);
        String as23[] = new String[21];
        as23[0] = "MGunBrowning50kWF 400";
        as23[1] = "MGunBrowning50kWF 400";
        as23[2] = "MGunBrowning50kWF 400";
        as23[3] = "MGunBrowning50kWF 400";
        as23[4] = "PylonF4UPLN3";
        as23[5] = "PylonF4UPLN3";
        as23[6] = "BombGun500lbs 1";
        as23[7] = "BombGun500lbs 1";
        as23[8] = "BombGun1000lbs 1";
        weaponsRegister(class1, "1x10002x500", as23);
        String as24[] = new String[21];
        as24[0] = "MGunBrowning50kWF 400";
        as24[1] = "MGunBrowning50kWF 400";
        as24[2] = "MGunBrowning50kWF 400";
        as24[3] = "MGunBrowning50kWF 400";
        as24[4] = "PylonF4UPLN3";
        as24[5] = "PylonF4UPLN3";
        as24[6] = "BombGun500lbs 1";
        as24[7] = "BombGun500lbs 1";
        as24[8] = "BombGun1000lbs 1";
        as24[13] = "RocketGunHVAR5 1";
        as24[14] = "RocketGunHVAR5 1";
        as24[15] = "RocketGunHVAR5 1";
        as24[16] = "RocketGunHVAR5 1";
        as24[17] = "RocketGunHVAR5 1";
        as24[18] = "RocketGunHVAR5 1";
        as24[19] = "RocketGunHVAR5 1";
        as24[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "1x10002x5008xhvargp", as24);
        String as25[] = new String[21];
        as25[0] = "MGunBrowning50kWF 400";
        as25[1] = "MGunBrowning50kWF 400";
        as25[2] = "MGunBrowning50kWF 400";
        as25[3] = "MGunBrowning50kWF 400";
        as25[4] = "PylonF4UPLN3";
        as25[5] = "PylonF4UPLN3";
        as25[6] = "BombGun500lbs 1";
        as25[7] = "BombGun500lbs 1";
        as25[8] = "BombGun1000lbs 1";
        as25[13] = "RocketGunHVAR5AP 1";
        as25[14] = "RocketGunHVAR5AP 1";
        as25[15] = "RocketGunHVAR5AP 1";
        as25[16] = "RocketGunHVAR5AP 1";
        as25[17] = "RocketGunHVAR5AP 1";
        as25[18] = "RocketGunHVAR5AP 1";
        as25[19] = "RocketGunHVAR5AP 1";
        as25[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "1x10002x5008xhvarap", as25);
        String as26[] = new String[21];
        as26[0] = "MGunBrowning50kWF 400";
        as26[1] = "MGunBrowning50kWF 400";
        as26[2] = "MGunBrowning50kWF 400";
        as26[3] = "MGunBrowning50kWF 400";
        as26[4] = "PylonF4UPLN3";
        as26[5] = "PylonF4UPLN3";
        as26[6] = "BombGun1000lbs 1";
        as26[7] = "BombGun1000lbs 1";
        weaponsRegister(class1, "2x1000", as26);
        String as27[] = new String[21];
        as27[0] = "MGunBrowning50kWF 400";
        as27[1] = "MGunBrowning50kWF 400";
        as27[2] = "MGunBrowning50kWF 400";
        as27[3] = "MGunBrowning50kWF 400";
        as27[4] = "PylonF4UPLN3";
        as27[5] = "PylonF4UPLN3";
        as27[6] = "BombGun1000lbs 1";
        as27[7] = "BombGun1000lbs 1";
        as27[13] = "RocketGunHVAR5 1";
        as27[14] = "RocketGunHVAR5 1";
        as27[15] = "RocketGunHVAR5 1";
        as27[16] = "RocketGunHVAR5 1";
        as27[17] = "RocketGunHVAR5 1";
        as27[18] = "RocketGunHVAR5 1";
        as27[19] = "RocketGunHVAR5 1";
        as27[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "2x10008xhvargp", as27);
        String as28[] = new String[21];
        as28[0] = "MGunBrowning50kWF 400";
        as28[1] = "MGunBrowning50kWF 400";
        as28[2] = "MGunBrowning50kWF 400";
        as28[3] = "MGunBrowning50kWF 400";
        as28[4] = "PylonF4UPLN3";
        as28[5] = "PylonF4UPLN3";
        as28[6] = "BombGun1000lbs 1";
        as28[7] = "BombGun1000lbs 1";
        as28[13] = "RocketGunHVAR5AP 1";
        as28[14] = "RocketGunHVAR5AP 1";
        as28[15] = "RocketGunHVAR5AP 1";
        as28[16] = "RocketGunHVAR5AP 1";
        as28[17] = "RocketGunHVAR5AP 1";
        as28[18] = "RocketGunHVAR5AP 1";
        as28[19] = "RocketGunHVAR5AP 1";
        as28[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "2x10008xhvarap", as28);
        String as29[] = new String[21];
        as29[0] = "MGunBrowning50kWF 400";
        as29[1] = "MGunBrowning50kWF 400";
        as29[2] = "MGunBrowning50kWF 400";
        as29[3] = "MGunBrowning50kWF 400";
        as29[4] = "PylonF4UPLN3";
        as29[5] = "PylonF4UPLN3";
        as29[6] = "BombGun1000lbs 1";
        as29[7] = "BombGun1000lbs 1";
        as29[8] = "BombGun1000lbs 1";
        weaponsRegister(class1, "3x1000", as29);
        String as30[] = new String[21];
        as30[0] = "MGunBrowning50kWF 400";
        as30[1] = "MGunBrowning50kWF 400";
        as30[2] = "MGunBrowning50kWF 400";
        as30[3] = "MGunBrowning50kWF 400";
        as30[4] = "PylonF4UPLN3";
        as30[5] = "PylonF4UPLN3";
        as30[6] = "BombGun1000lbs 1";
        as30[7] = "BombGun1000lbs 1";
        as30[8] = "BombGun1000lbs 1";
        as30[13] = "RocketGunHVAR5 1";
        as30[14] = "RocketGunHVAR5 1";
        as30[15] = "RocketGunHVAR5 1";
        as30[16] = "RocketGunHVAR5 1";
        as30[17] = "RocketGunHVAR5 1";
        as30[18] = "RocketGunHVAR5 1";
        as30[19] = "RocketGunHVAR5 1";
        as30[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "3x10008xhvargp", as30);
        String as31[] = new String[21];
        as31[0] = "MGunBrowning50kWF 400";
        as31[1] = "MGunBrowning50kWF 400";
        as31[2] = "MGunBrowning50kWF 400";
        as31[3] = "MGunBrowning50kWF 400";
        as31[4] = "PylonF4UPLN3";
        as31[5] = "PylonF4UPLN3";
        as31[6] = "BombGun1000lbs 1";
        as31[7] = "BombGun1000lbs 1";
        as31[8] = "BombGun1000lbs 1";
        as31[13] = "RocketGunHVAR5AP 1";
        as31[14] = "RocketGunHVAR5AP 1";
        as31[15] = "RocketGunHVAR5AP 1";
        as31[16] = "RocketGunHVAR5AP 1";
        as31[17] = "RocketGunHVAR5AP 1";
        as31[18] = "RocketGunHVAR5AP 1";
        as31[19] = "RocketGunHVAR5AP 1";
        as31[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "3x10008xhvarap", as31);
        String as32[] = new String[21];
        as32[0] = "MGunBrowning50kWF 400";
        as32[1] = "MGunBrowning50kWF 400";
        as32[2] = "MGunBrowning50kWF 400";
        as32[3] = "MGunBrowning50kWF 400";
        as32[8] = "BombGun2000lbs 1";
        weaponsRegister(class1, "1x2000", as32);
        String as33[] = new String[21];
        as33[0] = "MGunBrowning50kWF 400";
        as33[1] = "MGunBrowning50kWF 400";
        as33[2] = "MGunBrowning50kWF 400";
        as33[3] = "MGunBrowning50kWF 400";
        as33[8] = "BombGun2000lbs 1";
        as33[13] = "RocketGunHVAR5 1";
        as33[14] = "RocketGunHVAR5 1";
        as33[15] = "RocketGunHVAR5 1";
        as33[16] = "RocketGunHVAR5 1";
        as33[17] = "RocketGunHVAR5 1";
        as33[18] = "RocketGunHVAR5 1";
        as33[19] = "RocketGunHVAR5 1";
        as33[20] = "RocketGunHVAR5 1";
        weaponsRegister(class1, "1x20008xhvargp", as33);
        String as34[] = new String[21];
        as34[0] = "MGunBrowning50kWF 400";
        as34[1] = "MGunBrowning50kWF 400";
        as34[2] = "MGunBrowning50kWF 400";
        as34[3] = "MGunBrowning50kWF 400";
        as34[8] = "BombGun2000lbs 1";
        as34[13] = "RocketGunHVAR5AP 1";
        as34[14] = "RocketGunHVAR5AP 1";
        as34[15] = "RocketGunHVAR5AP 1";
        as34[16] = "RocketGunHVAR5AP 1";
        as34[17] = "RocketGunHVAR5AP 1";
        as34[18] = "RocketGunHVAR5AP 1";
        as34[19] = "RocketGunHVAR5AP 1";
        as34[20] = "RocketGunHVAR5AP 1";
        weaponsRegister(class1, "1x20008xhvarap", as34);
        String as35[] = new String[21];
        as35[0] = "MGunBrowning50kWF 400";
        as35[1] = "MGunBrowning50kWF 400";
        as35[2] = "MGunBrowning50kWF 400";
        as35[3] = "MGunBrowning50kWF 400";
        as35[4] = "PylonF4UPLN3";
        as35[5] = "PylonF4UPLN3";
        as35[6] = "BombGun1000lbs 1";
        as35[7] = "BombGun1000lbs 1";
        as35[8] = "BombGun2000lbs 1";
        weaponsRegister(class1, "1x20002x1000", as35);
        String as36[] = new String[21];
        as36[0] = "MGunBrowning50kWF 400";
        as36[1] = "MGunBrowning50kWF 400";
        as36[2] = "MGunBrowning50kWF 400";
        as36[3] = "MGunBrowning50kWF 400";
        as36[8] = "RocketGunBat 1";
        weaponsRegister(class1, "1xBat", as36);
        weaponsRegister(class1, "none", new String[21]);
    }
}
