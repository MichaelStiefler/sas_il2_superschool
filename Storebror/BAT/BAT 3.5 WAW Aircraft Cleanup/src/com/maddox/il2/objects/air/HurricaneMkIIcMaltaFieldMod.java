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
        this.Mk2WingspanType = 1;
        this.Mk2Distance = 200F;
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
        } else {
            Pilot pilot = (Pilot) this.FM;
            this.FM.AS.setCockpitDoor(this, (pilot.get_maneuver() != 25) && (pilot.get_maneuver() != 26) && (pilot.get_maneuver() != 49) && (pilot.get_maneuver() != 64) && (pilot.get_maneuver() != 66) && !pilot.Gears.onGround() ? 0 : 1);
            return;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this != World.getPlayerAircraft()) {
            return;
        }
        if (this.hierMesh().chunkFindCheck(HurricaneMkIIcMaltaFieldMod.cps) >= 0) {
            System.out.println(HurricaneMkIIcMaltaFieldMod.cpm);
            this.FM.M.massEmpty += 1000F;
        }
        this.Mk2Distance = Math.round(World.cur().userCoverMashineGun / 54.6805F) * 50F;
        if (this.Mk2Distance < 150F) {
            this.Mk2Distance = 150F;
        }
        if (this.Mk2Distance > 700F) {
            this.Mk2Distance = 700F;
        }
    }

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

    private void hudLogTargetType(int i) {
        if (i >= HurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanText.length) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Unknown Wingspan type \"" + i + "\" selected");
            return;
        }
        if (HurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanText[i].endsWith("ft.")) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan selected: " + HurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanText[i]);
        } else {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Target selected: " + HurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanText[i]);
        }
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.Mk2WingspanType++;
        if (this.Mk2WingspanType >= HurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanText.length) {
            this.Mk2WingspanType = HurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanText.length - 1;
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

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.Mk2WingspanType);
        netmsgguaranted.writeFloat(this.Mk2Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.Mk2WingspanType = netmsginput.readByte();
        this.Mk2Distance = netmsginput.readFloat();
    }

    public int[] krypto() {
        long l = Finger.Long("FmYnDimGwerthRhechDafad");
        int ai[] = new int[17];
        for (int i = 0; i < 17; i++) {
            int j = (int) ((l >> (8 * (i % 8))) & 255L);
            for (int k = i / 8; k > 0; k--) {
                j <<= 2;
                j = ((j & 3) | j) & 0xff;
            }

            if (j == 0) {
                j = 255;
            }
            ai[i] = j;
        }

        return ai;
    }

    public int                  Mk2WingspanType;
    public float                Mk2Distance;
    private static final String Mk2TargetWingspanText[] = { "30ft.", "Bf 109", "Fw 190", "40ft.", "Ju 87", "50ft.", "Bf 110", "Do 17", "60ft.", "Ju 88", "70ft.", "He 111", "80ft.", "90ft.", "Ju 52", "100ft.", "Fw 200" };
    private static final byte   bai1[]                  = { 106, 33, 18, 77, -44, -15, -50, -96, -84, 16, -11, -126 };
    private static final byte   bai2[]                  = { 124, 41, 10, 86, -39, -32, -1, -106, -124, 79, -116, -88, -9, -106, -56, -105, -96, 104, 45, 24, 78, -43, -72, -113, -69, -127, 83, -99, -23, -13, -102, -100, -101, -27, 78, 45, 29, 86, -59, -92, -125, -33, -97, 69, -111, -17, -24, -117, -100, -98, -28, 80, 61, 13, 86, -59, -92, -127, -14, -30 };
    private static String       cps;
    private static String       cpm;

    static {
        Class class1 = HurricaneMkIIcMaltaFieldMod.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hurri");
        Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIIc_Malta_Fieldmod(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        try {
            if (BaseGameVersion.is411orLater()) {
                Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIcMaltaFieldMod.fmd:H2CMALTA_FM");
            } else {
                Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIcMaltaFieldMod_410_and_earlier.fmd:H2CMALTA_FM");
            }
        } catch (Exception exception) {
            Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIcMaltaFieldMod.fmd:H2CMALTA_FM");
        }
        try {
            long l = Finger.Long("FmYnDimGwerthRhechDafad");
            int ai[] = new int[17];
            for (int i = 0; i < 17; i++) {
                int j = (int) ((l >> (8 * (i % 8))) & 255L);
                for (int k = i / 8; k > 0; k--) {
                    j <<= 2;
                    j = ((j & 3) | j) & 0xff;
                }

                if (j == 0) {
                    j = 255;
                }
                ai[i] = j;
            }

            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(HurricaneMkIIcMaltaFieldMod.bai1);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new KryptoInputFilter(bytearrayinputstream, ai)));
            HurricaneMkIIcMaltaFieldMod.cps = bufferedreader.readLine();
            bufferedreader.close();
            bytearrayinputstream = new ByteArrayInputStream(HurricaneMkIIcMaltaFieldMod.bai2);
            bufferedreader = new BufferedReader(new InputStreamReader(new KryptoInputFilter(bytearrayinputstream, ai)));
            HurricaneMkIIcMaltaFieldMod.cpm = bufferedreader.readLine();
            bufferedreader.close();
        } catch (Exception exception1) {
            System.out.println("cps Generation error");
        }
        Property.set(class1, "cockpitClass", new Class[] { CockpitHurricaneMkIIcMaltaFieldMod.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
    }
}
