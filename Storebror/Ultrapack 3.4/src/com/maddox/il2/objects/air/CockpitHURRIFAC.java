package com.maddox.il2.objects.air;

import com.maddox.sound.ReverbFXRoom;

public class CockpitHURRIFAC extends CockpitHURRII {

    public CockpitHURRIFAC() {
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.0F);
    }

    public void reflectWorldToInstruments(float f) {
        super.reflectWorldToInstruments(f);
        this.mesh.chunkVisible("FONAR2", false);
        this.mesh.chunkVisible("FONAR_GLASS2", false);
        this.mesh.chunkVisible("XGlassDamage2", false);
        this.mesh.chunkVisible("XGlassDamage3", false);
        this.mesh.chunkVisible("XGlassDamage4", false);
    }
}
