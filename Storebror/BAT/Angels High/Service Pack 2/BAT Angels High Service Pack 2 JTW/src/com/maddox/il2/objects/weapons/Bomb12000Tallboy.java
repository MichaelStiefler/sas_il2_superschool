package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class Bomb12000Tallboy extends Bomb {

    public Bomb12000Tallboy() {
        if (Config.isUSE_RENDER() && (World.Rnd().nextInt(0, 99) < 20)) {
            this.setMesh(Property.stringValue(this.getClass(), "mesh"));
            this.mesh.materialReplace("Ordnance1", "alhambra1");
        }
    }

    static {
        Class class1 = Bomb12000Tallboy.class;
        Property.set(class1, "mesh", "3DO/Arms/Tallboy/Tallboy.sim");
        Property.set(class1, "radius", 1500F);
        Property.set(class1, "power", 4500F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 2500F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
