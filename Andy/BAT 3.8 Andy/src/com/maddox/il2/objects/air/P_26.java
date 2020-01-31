package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class P_26 extends P_26x {

    public P_26() {
        this.bChangedExts = false;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.setTrimRudderControl(0.007F);
        this.FM.CT.setTrimAileronControl(0.007F);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        this.bChangedExts = true;
        if (this.FM.isPlayers()) {
            P_26.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        this.bChangedExts = true;
        if (this.FM.isPlayers()) {
            P_26.bChangedPit = true;
        }
    }

    public boolean        bChangedExts;
    public static boolean bChangedPit = false;

    static {
        Class class1 = P_26.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-26");
        Property.set(class1, "meshName", "3DO/Plane/P-26(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/P-26.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP26.class });
        Property.set(class1, "LOSElevation", 1.032F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 9, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb02", "_ExternalBomb03" });
    }
}
