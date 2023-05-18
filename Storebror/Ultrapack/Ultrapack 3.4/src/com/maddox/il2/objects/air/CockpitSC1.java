package com.maddox.il2.objects.air;

public class CockpitSC1 extends CockpitSB2C {
    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.mesh.chunkVisible("WingLIn_D0", false);
            this.mesh.chunkVisible("WingRIn_D0", false);
            return true;
        } else return false;
    }

}
