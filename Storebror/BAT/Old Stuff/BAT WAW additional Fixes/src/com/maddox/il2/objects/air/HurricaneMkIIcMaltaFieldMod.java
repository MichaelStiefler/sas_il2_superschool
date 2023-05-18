package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Finger;
import com.maddox.rts.KryptoInputFilter;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.BaseGameVersion;

public class HurricaneMkIIcMaltaFieldMod extends Hurricane implements TypeFighterAceMaker {

    public HurricaneMkIIcMaltaFieldMod() {
        // +++ Kumpel Code Insert +++
        this.Mk2WingspanType = 1; // Bf 109
        this.Mk2Distance = 200F; // 200 yd 600 ft
        // --- Kumpel Code Insert ---
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
        if (((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !flag || !(this.FM instanceof Pilot)) {
            return;
        }
        Pilot pilot = (Pilot) this.FM;
        this.FM.AS.setCockpitDoor(this, (pilot.get_maneuver() == 25 // Maneuver.LANDING
        ) || (pilot.get_maneuver() == 26 // Maneuver.TAKEOFF
        ) || (pilot.get_maneuver() == 49 // Maneuver.EMERGENCY_LANDING
        ) || (pilot.get_maneuver() == 64 // Maneuver.PARKED_STARTUP
        ) || (pilot.get_maneuver() == 66 // Maneuver.TAXI
        ) || pilot.Gears.onGround() ? 1 : 0);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this != World.getPlayerAircraft()) {
            return;
        }
        if (this.hierMesh().chunkFindCheck(cps) >= 0) {
            System.out.println(cpm);
            this.FM.M.massEmpty += 1000;
        }
        this.Mk2Distance = Math.round(World.cur().userCoverMashineGun / 54.6805F) * 50F;
        if (this.Mk2Distance < 150F) {
            this.Mk2Distance = 150F;
        }
        if (this.Mk2Distance > 700F) {
            this.Mk2Distance = 700F;
        }
    }

    // Opening Canopy Code
    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.0F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    // +++ Kumpel Code Insert +++ added methods to control gunsight reticle size
    public boolean typeFighterAceMakerToggleAutomation() {
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        this.Mk2Distance += 50F;
        if (this.Mk2Distance > 700F) {
            this.Mk2Distance = 700F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Target distance: " + (int) this.Mk2Distance + " yds.");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.Mk2Distance -= 50F;
        if (this.Mk2Distance < 150F) {
            this.Mk2Distance = 150F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Target distance: " + (int) this.Mk2Distance + " yds.");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    private void hudLogTargetType(int mk2WingspanType) {
        if (mk2WingspanType >= Mk2TargetWingspanText.length) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Unknown Wingspan type \"" + mk2WingspanType + "\" selected");
            return;
        }

        if (Mk2TargetWingspanText[mk2WingspanType].endsWith("ft.")) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan selected: " + Mk2TargetWingspanText[mk2WingspanType]);
        } else {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Target selected: " + Mk2TargetWingspanText[mk2WingspanType]);
        }

    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.Mk2WingspanType++;
        if (this.Mk2WingspanType >= Mk2TargetWingspanText.length) {
            this.Mk2WingspanType = Mk2TargetWingspanText.length - 1;
        }
        this.hudLogTargetType(this.Mk2WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.Mk2WingspanType--;
        if (this.Mk2WingspanType < 0) {
            this.Mk2WingspanType = 0;
        }
        this.hudLogTargetType(this.Mk2WingspanType);
    }

    // needed for online play

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.Mk2WingspanType);
        netmsgguaranted.writeFloat(this.Mk2Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.Mk2WingspanType = netmsginput.readByte();
        this.Mk2Distance = netmsginput.readFloat();
    }

    public int                  Mk2WingspanType;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              // 5
    // types
    // 0..4
    public float                Mk2Distance;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              // distance
    // 200..400
    // in
    // yards
    // --- Kumpel Code Insert ---

    private static final String Mk2TargetWingspanText[] = { "30ft.", "Bf 109", "Fw 190", "40ft.", "Ju 87", "50ft.", "Bf 110", "Do 17", "60ft.", "Ju 88", "70ft.", "He 111", "80ft.", "90ft.", "Ju 52", "100ft.", "Fw 200" };
    private static final byte   bai1[]                  = { 106, 33, 18, 77, -44, -15, -50, -96, -84, 16, -11, -126 };
    private static final byte   bai2[]                  = { 124, 41, 10, 86, -39, -32, -1, -106, -124, 79, -116, -88, -9, -106, -56, -105, -96, 104, 45, 24, 78, -43, -72, -113, -69, -127, 83, -99, -23, -13, -102, -100, -101, -27, 78, 45, 29, 86, -59, -92, -125, -33, -97, 69, -111, -17, -24, -117, -100, -98, -28, 80, 61, 13, 86, -59, -92, -127, -14, -30 };
    private static String       cps;
    private static String       cpm;

    public int[] krypto() {
        long l = Finger.Long("FmYnDimGwerthRhechDafad");
        int[] retVal = new int[17];
        for (int i = 0; i < 17; i++) {
            int j = (int) ((l >> (8 * (i % 8))) & 255L);
            for (int k = i / 8; k > 0; k--) {
                j <<= 2;
                j = ((j & 3) | j) & 0xff;
            }

            if (j == 0) {
                j = 255;
            }
            retVal[i] = j;
        }
        return retVal;
    }

    static {
        Class class1 = HurricaneMkIIcMaltaFieldMod.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hurri");
        Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIIc_Malta_Fieldmod(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);

        /*
         * Things get a little touchy here...
         * Since in IL-2 the Flight Model of the Hurricane IIc differes a lot
         * between "pre-4.11" and "4.11 and later" game versions, we have
         * to treat these two game versions different.
         */
        try {
            if (BaseGameVersion.is411orLater()) {
                Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIcMaltaFieldMod.fmd:H2CMALTA_FM");
// System.out.println("Flight Model loaded for the SAS Hurricane IIc Malta Field Mod: IL-2 4.11 or later.");
            } else {
                Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIcMaltaFieldMod_410_and_earlier.fmd:H2CMALTA_FM");
// System.out.println("Flight Model loaded for the SAS Hurricane IIc Malta Field Mod: IL-2 4.10.1 or earlier.");
            }
        } catch (Exception e) {
// System.out.println("No SAS Common Utils found, fallback to default Flight Model (IL-2 4.11 or later) for the SAS Hurricane IIc Malta Field Mod.");
            Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIcMaltaFieldMod.fmd:H2CMALTA_FM");
        }
        try {
            long l = Finger.Long("FmYnDimGwerthRhechDafad");
            int[] kk = new int[17];
            for (int i = 0; i < 17; i++) {
                int j = (int) ((l >> (8 * (i % 8))) & 255L);
                for (int k = i / 8; k > 0; k--) {
                    j <<= 2;
                    j = ((j & 3) | j) & 0xff;
                }

                if (j == 0) {
                    j = 255;
                }
                kk[i] = j;
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(bai1);
            BufferedReader br = new BufferedReader(new InputStreamReader(new KryptoInputFilter(bais, kk)));
            cps = br.readLine();
            br.close();
            bais = new ByteArrayInputStream(bai2);
            br = new BufferedReader(new InputStreamReader(new KryptoInputFilter(bais, kk)));
            cpm = br.readLine();
            br.close();
        } catch (Exception e) {
            System.out.println("cps Generation error");
        }
        // Flight Model set, let's carry on as usual...

        Property.set(class1, "cockpitClass", new Class[] { CockpitHurricaneMkIIcMaltaFieldMod.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
        Aircraft.weaponsRegister(class1, "default", new String[] { "MGunHispanoMkIk 91", "MGunHispanoMkIk 91" });
        Aircraft.weaponsRegister(class1, "none", new String[] { null, null });
    }
}
