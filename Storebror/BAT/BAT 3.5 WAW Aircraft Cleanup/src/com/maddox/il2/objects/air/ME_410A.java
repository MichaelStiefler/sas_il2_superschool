package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class ME_410A extends ME_210 implements TypeStormovik, TypeStormovikArmored, TypeDiveBomber {

    public ME_410A() {
        this.phase = 0;
        this.disp = 0.0F;
        this.oldbullets = 0;
        this.g1 = null;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.CT.Weapons[1] != null) {
            this.g1 = this.FM.CT.Weapons[1][0];
        }
        if (this.thisWeaponsName.startsWith("A2U4")) {
            this.hierMesh().chunkVisible("BK5_BARREL", true);
            this.FM.M.massEmpty += 540F;
        } else {
            this.hierMesh().chunkVisible("BK5_BARREL", false);
        }
    }

    public void update(float paramFloat) {
        if ((this.g1 != null) && (this.oldbullets != this.g1.countBullets())) {
            switch (this.phase) {
                default:
                    break;

                case 0:
                    if (this.g1.isShots()) {
                        this.oldbullets = this.g1.countBullets();
                        this.phase = 1;
                        this.disp = 0.0F;
                    }
                    break;

                case 1:
                    this.disp += 12.6F * paramFloat;
                    this.resetYPRmodifier();
                    Aircraft.xyz[0] = this.disp;
                    this.hierMesh().chunkSetLocate("BK5_BARREL", Aircraft.xyz, Aircraft.ypr);
                    if (this.disp >= 0.7F) {
                        this.phase = 2;
                    }
                    break;

                case 2:
                    this.disp -= 1.2F * paramFloat;
                    this.resetYPRmodifier();
                    Aircraft.xyz[0] = this.disp;
                    this.hierMesh().chunkSetLocate("BK5_BARREL", Aircraft.xyz, Aircraft.ypr);
                    if (this.disp <= 0.0F) {
                        this.phase = 3;
                    }
                    break;

                case 3:
                    this.phase = 0;
                    break;
            }
        }
        super.update(paramFloat);
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    private int           phase;
    private float         disp;
    private int           oldbullets;
    private BulletEmitter g1;
    public float          fSightCurReadyness;

    static {
        Class class1 = ME_410A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me-410");
        Property.set(class1, "meshName", "3DO/Plane/ME-410-A/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-410A.fmd:ME210410_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_410A.class });
        Property.set(class1, "LOSElevation", 0.66895F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 10, 10, 3, 3, 3, 9, 1, 9, 1, 9, 1, 9, 9, 9, 1, 1, 9, 1, 1, 9, 1, 1, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 9, 9, 3, 3, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_ExternalDev03", "_CANNON03", "_ExternalDev04", "_CANNON04", "_ExternalDev05", "_CANNON05", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_CANNON08", "_CANNON09", "_ExternalDev11", "_CANNON10", "_CANNON11", "_ExternalDev12", "_CANNON12", "_CANNON13", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev19", "_ExternalDev20", "_ExternalBomb07", "_ExternalBomb08", "_ExternalRock07", "_ExternalRock08", "_ExternalDev21", "_ExternalDev22", "_ExternalDev23", "_ExternalDev24", "_ExternalRock01", "_ExternalRock03", "_ExternalRock02", "_ExternalRock04" });
    }
}
