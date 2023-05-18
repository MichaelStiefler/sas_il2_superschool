package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class I_16TYPE17 extends I_16 implements TypeFighter, TypeTNBFighter {

    public I_16TYPE17() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            I_16TYPE17.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            I_16TYPE17.bChangedPit = true;
        }
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -55F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -55F * f, 0.0F);
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = I_16TYPE17.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type17/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type17(ru)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE_Early.class });
        Property.set(class1, "FlightModel", "FlightModels/I-16type17.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
