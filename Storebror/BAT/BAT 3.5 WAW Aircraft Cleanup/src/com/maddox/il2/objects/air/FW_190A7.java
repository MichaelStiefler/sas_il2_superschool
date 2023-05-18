package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class FW_190A7 extends FW_190NEW implements TypeFighter, TypeBNZFighter {

    public FW_190A7() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FW_190A7.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FW_190A7.bChangedPit = true;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_CANNON01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmL1_D0", false);
        }
        if (this.getGunByHookName("_CANNON02") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmR1_D0", false);
        }
        if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmL_D0", false);
        }
        if (this.getGunByHookName("_CANNON04") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmR_D0", false);
        }
        if (this.getGunByHookName("_CANNON07") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("30mmL_D0", false);
        }
        if (this.getGunByHookName("_CANNON08") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("30mmR_D0", false);
        }
    }

    protected void moveGear(float f) {
        FW_190NEW.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = FW_190A7.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190A-7/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1943.1F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190A-7.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190A7.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 9, 2, 2, 9, 1, 1, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalBomb01", "_CANNON07", "_CANNON08", "_ExternalDev01" });
    }
}
