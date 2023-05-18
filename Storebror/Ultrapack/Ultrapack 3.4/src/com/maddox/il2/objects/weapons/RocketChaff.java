package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgShot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class RocketChaff extends Rocket {

    public RocketChaff() {
    }

    public void start(float f, int i) {
        float f1 = 30F;
        super.start(f1, i);
        this.getOwner().getSpeed(this.speed);
        this.speed.z = -50D;
        this.setSpeed(this.speed);
        Engine.countermeasures().add(this);
    }

    protected void doExplosion(Actor actor, String s) {
        this.pos.getTime(Time.current(), p);
        Class class1 = this.getClass();
        float f = Property.floatValue(class1, "power", 1000F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        this.getSpeed(this.speed);
        Vector3f vector3f = new Vector3f(this.speed);
        vector3f.normalize();
        vector3f.scale(850F);
        MsgShot.send(actor, s, p, vector3f, this.M, this.getOwner(), (float) (0.5F * this.M * this.speed.lengthSquared()), 3, 0.0D);
        MsgExplosion.send(actor, s, p, this.getOwner(), this.M, f, i, f1);
        this.destroy();
    }

    protected void doExplosionAir() {
    }

    private static Point3d p = new Point3d();

    static {
        Class class1 = RocketChaff.class;
        Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
        Property.set(class1, "sprite", (String) null);
        Property.set(class1, "flame", (String) null);
        Property.set(class1, "smoke", (String) null);
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 5F);
        Property.set(class1, "emitMax", 20F);
        Property.set(class1, "sound", (String) null);
        Property.set(class1, "timeLife", 7F);
        Property.set(class1, "timeFire", 0.2F);
        Property.set(class1, "force", 20F);
        Property.set(class1, "dragCoefficient", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1E-005F);
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "kalibr", 0.001F);
        Property.set(class1, "massa", 3F);
        Property.set(class1, "massaEnd", 20000F);
        Property.set(class1, "shotFreq", 3F);
    }
}
