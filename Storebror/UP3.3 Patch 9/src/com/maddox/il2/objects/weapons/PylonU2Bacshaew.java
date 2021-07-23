package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class PylonU2Bacshaew extends Pylon {

    public PylonU2Bacshaew() {
        if (World.cur().camouflage == 1) {
            this.setMesh(Property.stringValue(this.getClass(), "mesh"));
            this.mesh.materialReplace("skin1o", "skin1w");
        }
    }

    static {
        Property.set(PylonU2Bacshaew.class, "mesh", "3DO/Arms/Bacshaew/mono.sim");
    }
}
