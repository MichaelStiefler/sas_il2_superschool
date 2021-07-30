package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombNull extends Bomb {

    public void start() {
        super.start();
        this.drawing(false);
        this.lStart = Time.current();
    }

    protected void doExplosion(Actor actor1, String s1) {
    }

    protected void doExplosionAir() {
    }

    // TODO: Added by SAS~Storebror:
    // It's important to immediately destroy the "Null" Bombs and Rockets, since otherwise
    // they will keep travelling to infinity because these actors don't collide with anything!
    public void interpolateTick() {
        if (Time.current() > this.lStart) this.destroy();
    }

    public BombNull() {
    }

    private long lStart = 0L;

    static {
        Class class1 = BombNull.class;
        Property.set(class1, "mesh", "3DO/Arms/Null/mono.sim");
        Property.set(class1, "sprite", (Object) null);
        Property.set(class1, "flame", (Object) null);
        Property.set(class1, "smoke", (Object) null);
        Property.set(class1, "emitColor", new Color3f(0.0F, 0.0F, 0.0F));
        Property.set(class1, "emitLen", 0.0F);
        Property.set(class1, "emitMax", 0.0F);
        Property.set(class1, "sound", (Object) null);
        Property.set(class1, "radius", 0.1F);
        Property.set(class1, "timeLife", 999.999F);
        Property.set(class1, "timeFire", 0.0F);
        Property.set(class1, "force", 0.0F);
        Property.set(class1, "power", 0.01485F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.001F);
        Property.set(class1, "massa", 0.01485F);
        Property.set(class1, "massaEnd", 0.01485F);
    }
}
