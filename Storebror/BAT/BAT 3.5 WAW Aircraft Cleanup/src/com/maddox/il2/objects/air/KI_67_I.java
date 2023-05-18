package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.BombGunTorpType91;
import com.maddox.il2.objects.weapons.BombGunTorpType91late2;
import com.maddox.il2.objects.weapons.RocketGunIgo_1_A;
import com.maddox.rts.Property;

public class KI_67_I extends KI_67 implements TypeBomber, TypeX4Carrier, TypeGuidedBombCarrier {

    public KI_67_I() {
        this.bombBayDoorsRemoved = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.isGuidingBomb = false;
    }

    public boolean needsOpenBombBay() {
        return !this.bombBayDoorsRemoved;
    }

    public boolean canOpenBombBay() {
        return !this.bombBayDoorsRemoved;
    }

    public boolean typeGuidedBombCisMasterAlive() {
        return this.isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag) {
        this.isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding() {
        return this.isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag) {
        this.isGuidingBomb = flag;
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

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if ((this.FM.CT.Weapons[3] != null) && ((this.FM.CT.Weapons[3][0] instanceof BombGunTorpType91late2) || (this.FM.CT.Weapons[3][0] instanceof BombGunTorpType91) || (this.FM.CT.Weapons[3][0] instanceof RocketGunIgo_1_A))) {
            this.hierMesh().chunkVisible("Bay01_D0", false);
            this.hierMesh().chunkVisible("Bay02_D0", false);
            this.bombBayDoorsRemoved = true;
        }
    }

    protected boolean bombBayDoorsRemoved;
    private float     deltaAzimuth;
    private float     deltaTangage;
    private boolean   isGuidingBomb;
    private boolean   isMasterAlive;

    static {
        Class class1 = KI_67_I.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-67");
        Property.set(class1, "meshName", "3DO/Plane/Ki-67-I(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-67-I(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ki67-I.fmd:Ki67-I_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_67_I.class, CockpitKI_67_I_Bombardier.class, CockpitKI_67_I_NGunner.class, CockpitKI_67_I_AGunner.class, CockpitKI_67_I_TGunner.class, CockpitKI_67_I_RGunner.class, CockpitKI_67_I_LGunner.class });
        Property.set(class1, "LOSElevation", 1.4078F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn03", "_BombSpawn04", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn07", "_BombSpawn07", "_BombSpawn08", "_BombSpawn08", "_BombSpawn09", "_BombSpawn09", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb02" });
    }
}
