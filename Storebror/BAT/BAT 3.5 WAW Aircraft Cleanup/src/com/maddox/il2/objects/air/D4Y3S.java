package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.rts.Property;

public class D4Y3S extends D4YS implements TypeJazzPlayer {

    public D4Y3S() {
        this.flapps = 0.0F;
    }

    public boolean hasCourseWeaponBullets() {
        return ((this.FM.CT.Weapons[0] != null) && (this.FM.CT.Weapons[0][0] != null) && (this.FM.CT.Weapons[0][1] != null) && (this.FM.CT.Weapons[0][0].countBullets() != 0)) || (this.FM.CT.Weapons[0][1].countBullets() != 0);
    }

    public boolean hasSlantedWeaponBullets() {
        return (this.FM.AS.astatePilotStates[1] < 100) && (this.FM.CT.Weapons[1] != null) && (this.FM.CT.Weapons[1][0] != null) && (this.FM.CT.Weapons[1][0].countBullets() != 0);
    }

    public Vector3d getAttackVector() {
        return D4Y3S.ATTACK_VECTOR;
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            this.hierMesh().chunkSetAngles("Oil_D0", 0.0F, -22F * f1, 0.0F);
            for (int i = 1; i < 6; i++) {
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);
            }

        }
    }

    private static final Vector3d ATTACK_VECTOR = new Vector3d(-190D, 0.0D, -300D);
    private float                 flapps;

    static {
        Class class1 = D4Y3S.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D4Y");
        Property.set(class1, "meshName", "3DO/Plane/D4Y3S(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/D4Y3S(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/D4Y3S.fmd:D4Y3S_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitD4Y3S.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 9, 9, 3, 9, 9, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn02", "_BombSpawn03", "_BombSpawn01", "_ExternalDev02", "_ExternalDev03", "_BombSpawn04", "_ExternalDev04", "_ExternalDev05", "_CANNON01" });
    }
}
