package com.maddox.il2.objects.air;

public class CockpitCa148 extends CockpitPilot {
    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D0", false);
            this.aircraft().hierMesh().chunkVisible("CF_D1", false);
            this.aircraft().hierMesh().chunkVisible("CF_D2", false);
            this.aircraft().hierMesh().chunkVisible("CF_D3", false);
            this.aircraft().hierMesh().chunkVisible("133intP", false);
            this.aircraft().hierMesh().chunkVisible("148intC", false);
            this.aircraft().hierMesh().chunkVisible("148cfdVT", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D0", true);
        this.aircraft().hierMesh().chunkVisible("133intP", true);
        this.aircraft().hierMesh().chunkVisible("148intC", true);
        this.aircraft().hierMesh().chunkVisible("148cfdVT", true);
        super.doFocusLeave();
    }

    public CockpitCa148() {
        super("3DO/Cockpit/Ca133/CockpitCa148.him", "he111");
    }

}
