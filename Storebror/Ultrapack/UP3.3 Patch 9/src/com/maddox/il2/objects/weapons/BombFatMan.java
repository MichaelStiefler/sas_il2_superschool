package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class BombFatMan extends Bomb {

    public BombFatMan() {
        if (Config.isUSE_RENDER() && (World.Rnd().nextInt(0, 99) < 20)) {
            this.setMesh(Property.stringValue(this.getClass(), "mesh"));
            this.mesh.materialReplace("Ordnance1", "alhambra" + World.Rnd().nextInt(1, 1));
        }
    }

    static {
        Class class1 = BombFatMan.class;
        Property.set(class1, "mesh", "3DO/Arms/FatMan/mono.sim");
        Property.set(class1, "radius", 3200F);
        Property.set(class1, "power", 1.3E+007F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 4630F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "newEffect", 1);
        Property.set(class1, "nuke", 1);
    }
}
