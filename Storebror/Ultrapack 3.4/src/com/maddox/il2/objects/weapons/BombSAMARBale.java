package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombSAMARBale extends Bomb {

    public BombSAMARBale() {
        this.chute = null;
        this.bOnChute = false;
    }

    protected boolean haveSound() {
        return false;
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
    }

    public void destroy() {
        if (this.chute != null) this.chute.destroy();
        super.destroy();
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.bOnChute) {
            this.getSpeed(v3d);
            v3d.scale(0.99D);
            if (v3d.z < -3D) v3d.z += 1.1F * Time.tickConstLenFs();
            this.setSpeed(v3d);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.pos.setRel(new Point3d(1.0D, 0.0D, 0.0D), new Orient(0.0F, 0.0F, 0.0F));
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand && this.chute != null) this.chute.landing();
        super.msgCollision(actor, s, s1);
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;

    static {
        Class class1 = BombSAMARBale.class;
        Property.set(class1, "mesh", "3DO/Arms/SAMARBale/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 6F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.5F);
        Property.set(class1, "massa", 70F);
        Property.set(class1, "sound", (String) null);
    }
}
