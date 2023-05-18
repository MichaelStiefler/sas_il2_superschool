package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class BombSAB55100 extends FlareBomb {

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(4F, 5.5F);
    }

    static {
        Class class1 = BombSAB55100.class;
        Property.set(class1, "mesh", "3DO/Arms/SAB55100/mono.sim");
        Property.set(class1, "flareColor", new Color3f(1.0F, 0.9F, 0.5F));
        Property.set(class1, "flareSmoke", "3DO/Effects/Fireworks/ParaFlareSmoke.eff");
        Property.set(class1, "flareLen", 2000F);
        Property.set(class1, "flareMax", 30F);
        Property.set(class1, "flareBurnTime", 250F);
        Property.set(class1, "decentRate", 4.2F);
        Property.set(class1, "radius", 0.01F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.24F);
        Property.set(class1, "massa", 55F);
        Property.set(class1, "sound", "weapon.bomb_phball");
        Property.set(class1, "dateOfUse", 0x12853c5);
    }
}
