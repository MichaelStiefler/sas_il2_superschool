package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class J1N1_S extends J1N1 implements TypeJazzPlayer {

    public J1N1_S() {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "S_";
    }

    public boolean hasCourseWeaponBullets() {
        return (this.FM.CT.Weapons[0] != null) && (this.FM.CT.Weapons[0][0] != null) && (this.FM.CT.Weapons[0][0].countBullets() != 0);
    }

    public boolean hasSlantedWeaponBullets() {
        return ((this.FM.CT.Weapons[1] != null) && (this.FM.CT.Weapons[1][0] != null) && (this.FM.CT.Weapons[1][1] != null) && (this.FM.CT.Weapons[1][0].countBullets() != 0)) || (this.FM.CT.Weapons[1][1].countBullets() != 0);
    }

    public Vector3d getAttackVector() {
        return J1N1_S.ATTACK_VECTOR;
    }

    public void update(float f) {
        super.update(f);
        this.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("CF_D0", false);
                this.hierMesh().chunkVisible("Nose_D0", false);
            } else {
                this.hierMesh().chunkVisible("CF_D0", true);
                this.hierMesh().chunkVisible("Nose_D0", true);
            }
        }
    }

    private static final Vector3d ATTACK_VECTOR = new Vector3d(-190D, 0.0D, -300D);

    static {
        Class class1 = J1N1_S.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "J1N");
        Property.set(class1, "meshName", "3do/plane/J1N1(Multi1)/S_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3do/plane/J1N1(ja)/S_hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-45-Tei.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJ1N1_S.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 10, 3, 3, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN02", "_MGUN03", "_CANNON03", "_CANNON04", "_MGUN01", "_BombSpawn02", "_BombSpawn03", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05" });
    }
}
