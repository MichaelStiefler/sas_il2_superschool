package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class BF_110C4 extends BF_110 {

    public BF_110C4() {
    }

    public void blisterRemoved(int i) {
        if (i == 1) {
            this.doWreck("Blister1A_D0");
            this.doWreck("Blister1B_D0");
        }
        if (i == 2) {
            this.doWreck("Blister2A_D0");
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            BF_110C4.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            BF_110C4.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = BF_110C4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf-110");
        Property.set(class1, "meshName", "3DO/Plane/Bf-110C-4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-110C-4.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_110C4.class, CockpitBF_110Early_Gunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_MGUN05" });
    }
}
