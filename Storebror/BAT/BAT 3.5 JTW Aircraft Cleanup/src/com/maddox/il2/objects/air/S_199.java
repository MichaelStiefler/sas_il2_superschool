package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class S_199 extends BF_109G14 {

    public S_199() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            S_199.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            S_199.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = S_199.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "S199");
        Property.set(class1, "meshName", "3DO/Plane/S-199/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1947F);
        Property.set(class1, "yearExpired", 1958F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitS_199.class });
        Property.set(class1, "FlightModel", "FlightModels/S-199.fmd:gui/game/AviaS199_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
