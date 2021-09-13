package com.maddox.il2.objects.air;

public class CockpitCa133 extends CockpitCaproniPilot {
    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D0", false);
            this.aircraft().hierMesh().chunkVisible("CF_D1", false);
            this.aircraft().hierMesh().chunkVisible("CF_D2", false);
            this.aircraft().hierMesh().chunkVisible("CF_D3", false);
            this.aircraft().hierMesh().chunkVisible("133cfdT", false);
            this.aircraft().hierMesh().chunkVisible("133intP", false);
            this.aircraft().hierMesh().chunkVisible("133intC", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D0", true);
        this.aircraft().hierMesh().chunkVisible("133cfdT", true);
        this.aircraft().hierMesh().chunkVisible("133intP", true);
        this.aircraft().hierMesh().chunkVisible("133intC", true);
        super.doFocusLeave();
    }

    public CockpitCa133() {
        super("3DO/Cockpit/Ca133/CockpitCa133.him", "he111");
    }

}
