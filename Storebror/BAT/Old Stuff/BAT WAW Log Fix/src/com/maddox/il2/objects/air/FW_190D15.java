package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Property;

public class FW_190D15 extends FW_190DB603NEW {

    public FW_190D15() {
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
        Class class1 = FW_190D15.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190D-15(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        int DoraFMType = 9;
        try {
            if (FlightModelMain.sectFile("FlightModels/Fw-190D-15.fmd") != null) {
                DoraFMType = 15;
            } else if (FlightModelMain.sectFile("FlightModels/Fw-190D-13.fmd") != null) {
                DoraFMType = 13;
            }
        } catch (Exception exception) {
        }
        switch (DoraFMType) {
            case 9: // '\t'
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-9.fmd");
// System.out.println("Fw190-D15 Loading FMD: FlightModels/Fw-190D-9.fmd");
                break;

            case 13: // '\r'
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-13.fmd");
// System.out.println("Fw190-D15 Loading FMD: FlightModels/Fw-190D-13.fmd");
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
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D15.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 9, 9, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 1, 1, 9, 9, 2, 2, 9, 9, 9, 9, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalDev02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev05", "_ExternalDev06", "_CANNON09", "_CANNON10", "_ExternalDev07", "_ExternalDev08", "_CANNON11", "_CANNON12", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_CANNON13", "_CANNON14" });
        Aircraft.weaponsRegister(class1, "default", new String[] { "MGunMG15120MGs 250", "MGunMG15120MGs 250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMK108Internal 1", "PylonMK108Internal 1", "MGunMK108kh 55", "MGunMK108kh 55" });
        Aircraft.weaponsRegister(class1, "R2-2xMK108+DT", new String[] { "MGunMG15120MGs 250", "MGunMG15120MGs 250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonETC504FW190 1", "FuelTankGun_Type_D 1", "PylonMK108Internal 1", "PylonMK108Internal 1", "MGunMK108kh 55", "MGunMK108kh 55" });
        String as[] = new String[31];
        as[0] = "MGunMG15120MGs 250";
        as[1] = "MGunMG15120MGs 250";
        as[3] = "PylonMG15120Internal 1";
        as[4] = "PylonMG15120Internal 1";
        as[5] = "MGunMG15120MGkh 125";
        as[6] = "MGunMG15120MGkh 125";
        Aircraft.weaponsRegister(class1, "R1-2xMG151-20", as);
        String as1[] = new String[31];
        as1[0] = "MGunMG15120MGs 250";
        as1[1] = "MGunMG15120MGs 250";
        as1[3] = "PylonMG15120Internal 1";
        as1[4] = "PylonMG15120Internal 1";
        as1[5] = "MGunMG15120MGkh 125";
        as1[6] = "MGunMG15120MGkh 125";
        as1[25] = "PylonETC504FW190 1";
        as1[26] = "FuelTankGun_Type_D 1";
        Aircraft.weaponsRegister(class1, "R1-2xMG151-20+DT", as1);
        String as2[] = new String[31];
        as2[0] = "MGunMG15120MGs 250";
        as2[1] = "MGunMG15120MGs 250";
        as2[7] = "PylonMG15120x2 1";
        as2[8] = "PylonMG15120x2 1";
        as2[9] = "MGunMG15120MGkh 125";
        as2[10] = "MGunMG15120MGkh 125";
        as2[11] = "MGunMG15120MGkh 125";
        as2[12] = "MGunMG15120MGkh 125";
        Aircraft.weaponsRegister(class1, "R1-4xMG151-20", as2);
        String as3[] = new String[31];
        as3[0] = "MGunMG15120MGs 250";
        as3[1] = "MGunMG15120MGs 250";
        as3[7] = "PylonMG15120x2 1";
        as3[8] = "PylonMG15120x2 1";
        as3[9] = "MGunMG15120MGkh 125";
        as3[10] = "MGunMG15120MGkh 125";
        as3[11] = "MGunMG15120MGkh 125";
        as3[12] = "MGunMG15120MGkh 125";
        as3[25] = "PylonETC504FW190 1";
        as3[26] = "FuelTankGun_Type_D 1";
        Aircraft.weaponsRegister(class1, "R1-4xMG151-20+DT", as3);
        String as4[] = new String[31];
        as4[0] = "MGunMG15120MGs 250";
        as4[1] = "MGunMG15120MGs 250";
        as4[17] = "PylonMk103 1";
        as4[18] = "PylonMk103 1";
        as4[19] = "MGunMK103kh 35";
        as4[20] = "MGunMK103kh 35";
        Aircraft.weaponsRegister(class1, "R3_2xMK103", as4);
        String as5[] = new String[31];
        as5[0] = "MGunMG15120MGs 250";
        as5[1] = "MGunMG15120MGs 250";
        as5[17] = "PylonMk103 1";
        as5[18] = "PylonMk103 1";
        as5[19] = "MGunMK103kh 35";
        as5[20] = "MGunMK103kh 35";
        as5[25] = "PylonETC504FW190 1";
        as5[26] = "FuelTankGun_Type_D 1";
        Aircraft.weaponsRegister(class1, "R3_2xMK103+DT", as5);
        String as6[] = new String[31];
        as6[0] = "MGunMG15120MGs 250";
        as6[1] = "MGunMG15120MGs 250";
        as6[21] = "PylonRO_WfrGr21 1";
        as6[22] = "PylonRO_WfrGr21 1";
        as6[23] = "RocketGunWfrGr21 1";
        as6[24] = "RocketGunWfrGr21 1";
        Aircraft.weaponsRegister(class1, "R6_2xWfrGr21", as6);
        String as7[] = new String[31];
        as7[0] = "MGunMG15120MGs 250";
        as7[1] = "MGunMG15120MGs 250";
        as7[21] = "PylonRO_WfrGr21 1";
        as7[22] = "PylonRO_WfrGr21 1";
        as7[23] = "RocketGunWfrGr21 1";
        as7[24] = "RocketGunWfrGr21 1";
        as7[25] = "PylonETC504FW190 1";
        as7[26] = "FuelTankGun_Type_D 1";
        Aircraft.weaponsRegister(class1, "R6_2xWfrGr21+DT", as7);
        String as8[] = new String[31];
        as8[0] = "MGunMG15120MGs 250";
        as8[1] = "MGunMG15120MGs 250";
        as8[2] = "BombGunSC500 1";
        as8[3] = "PylonMG15120Internal 1";
        as8[4] = "PylonMG15120Internal 1";
        as8[5] = "MGunMG15120MGkh 125";
        as8[6] = "MGunMG15120MGkh 125";
        as8[25] = "PylonETC504FW190 1";
        Aircraft.weaponsRegister(class1, "R1-2xMG151-20+1xSC500", as8);
        Aircraft.weaponsRegister(class1, "R2-2xMK108+1xSC500", new String[] { "MGunMG15120MGs 250", "MGunMG15120MGs 250", "BombGunSC500 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonETC504FW190 1", null, "PylonMK108Internal 1", "PylonMK108Internal 1", "MGunMK108kh 55", "MGunMK108kh 55" });
        String as9[] = new String[31];
        as9[0] = "MGunMG15120MGs 250";
        as9[1] = "MGunMG15120MGs 250";
        as9[2] = "BombGunSC500 1";
        as9[17] = "PylonMk103 1";
        as9[18] = "PylonMk103 1";
        as9[19] = "MGunMK103kh 35";
        as9[20] = "MGunMK103kh 35";
        as9[25] = "PylonETC504FW190 1";
        Aircraft.weaponsRegister(class1, "R3-2xMK103+1xSC500", as9);
        String as10[] = new String[31];
        as10[0] = "MGunMG15120MGs 250";
        as10[1] = "MGunMG15120MGs 250";
        Aircraft.weaponsRegister(class1, "LightWeight", as10);
        Aircraft.weaponsRegister(class1, "none", new String[31]);
    }
}
