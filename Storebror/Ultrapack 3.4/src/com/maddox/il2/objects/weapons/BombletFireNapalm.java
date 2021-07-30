package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombletFireNapalm extends Bomb {

    public void start() {
        super.start();
        if (Config.isUSE_RENDER()) Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/NapalmFire.eff", 10F);
    }

    public void msgCollision(Actor actor, String s, String s1) {
        // System.out.println("msgCollision at " + Time.current());
        if (actor != null) {
            Point3d point3d = new Point3d();
            this.pos.getTime(Time.current(), point3d);
            Loc loc = new Loc(point3d);

            Class class1 = this.getClass();
            float f = Property.floatValue(class1, "power", 0.0F);
            int i = Property.intValue(class1, "powerType", 0);
            float f1 = Property.floatValue(class1, "radius", 1.0F);
            if (Config.isUSE_RENDER()) Eff3DActor.New(loc, World.Rnd().nextFloat(0.5F, 4.0F), "3DO/Effects/Fireworks/NapalmFire.eff", 10F);
            MsgExplosion.send(actor, s1, point3d, this.getOwner(), this.M, f, i, f1);
        }
        super.msgCollision(actor, s, s1);
        // postDestroy();
    }

    static {
        Class class1 = BombletFireNapalm.class;
        Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
        Property.set(class1, "emitColor", new Color3f(1.0F, 0.5F, 0.5F));
        Property.set(class1, "emitLen", 20F);
        Property.set(class1, "emitMax", 0.5F);
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 5.5F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 2.0F);
        // Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
