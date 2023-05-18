package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class BombHC4000 extends Bomb {

    public BombHC4000() {
        if (Config.isUSE_RENDER() && (World.Rnd().nextInt(0, 99) < 20)) {
            this.setMesh(Property.stringValue(this.getClass(), "mesh"));
            this.mesh.materialReplace("Ordnance1", "alhambra1");
        }
    }

    static {
        Class class1 = BombHC4000.class;
        Property.set(class1, "mesh", "3DO/Arms/hc4000/hc4000.sim");
        Property.set(class1, "radius", 610F);
        Property.set(class1, "power", 1660F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 1786F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
