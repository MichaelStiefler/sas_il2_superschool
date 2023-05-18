package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.objects.electronics.RadarLiSN2;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class TA_154_A0 extends TA_154 implements TypeFighter, TypeBNZFighter, TypeStormovik, TypeX4Carrier, TypeRadarLiSN2Carrier {

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;
            case 2:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;
            default:
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (super.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void typeX4CAdjSidePlus() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = 1.0F;
            return;
        }
        this.radarMode++;
        if (this.radarMode > RadarLiSN2.RADAR_MODE_SHORT) {
            this.radarMode = RadarLiSN2.RADAR_MODE_NORMAL;
        }
    }

    public void typeX4CAdjSideMinus() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = -1F;
            return;
        }
        this.radarMode--;
        if (this.radarMode < RadarLiSN2.RADAR_MODE_NORMAL) {
            this.radarMode = RadarLiSN2.RADAR_MODE_SHORT;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        if (this.curPilot == 1) {
            this.deltaTangage = 1.0F;
            return;
        }
        this.radarGain += 10;
        if (this.radarGain > 100) {
            this.radarGain = 100;
        }
    }

    public void typeX4CAdjAttitudeMinus() {
        if (this.curPilot == 1) {
            this.deltaTangage = -1F;
            return;
        }
        this.radarGain -= 10;
        if (this.radarGain < 0) {
            this.radarGain = 0;
        }
    }

    public void typeX4CResetControls() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = this.deltaTangage = 0.0F;
            return;
        }
        this.radarGain = 50;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void setCurPilot(int theCurPilot) {
        this.curPilot = theCurPilot;
    }

    public int getCurPilot() {
        return this.curPilot;
    }

    public int getRadarGain() {
        return this.radarGain;
    }

    public int getRadarMode() {
        return this.radarMode;
    }

    private float deltaAzimuth;
    private float deltaTangage;
    private int   curPilot;
    private int   radarGain;
    private int   radarMode;

    static {
        Class class1 = TA_154_A0.class;
        Property.set(class1, "iconFar_shortClassName", "TA154");
        Property.set(class1, "meshName", "3DO/Plane/Ta-154A0(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1965.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ta-154A0.fmd:TA_154");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTA_154.class, CockpitTA_154_Radar.class });
        Property.set(class1, "LOSElevation", 0.92575F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04" });
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 4;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunMG15120k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunMG15120k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMK108k", 110);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMK108k", 110);

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for (int j4 = 0; j4 < byte0; j4++) {
                a_lweaponslot[j4] = null;
            }

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
        }
    }
}
