package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombParaFlareUK extends Bomb {

    public BombParaFlareUK() {
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
        if (this.chute != null) {
            this.chute.destroy();
        }
        super.destroy();
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.bOnChute) {
            this.getSpeed(v3d);
            v3d.scale(0.97D);
            if (v3d.z < -2D) {
                v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(v3d);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            // TODO: Bugfix by SAS~Storebror: Avoid Scaling Meshes with Shadows!
//          chute = new Chute(this);
            this.chute = new BombChute(this, 0.25F);
            this.chute.collide(false);
            // TODO: Bugfix by SAS~Storebror: Avoid Scaling Meshes with Shadows!
//          chute.mesh().setScale(0.5F);
            this.chute.pos.setRel(new Point3d(0.5D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if ((actor instanceof ActorLand) && (this.chute != null)) {
            this.chute.landing();
        }
        super.msgCollision(actor, s, s1);
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;

    static {
        final Class class1 = BombParaFlareUK.class;
        Property.set(class1, "mesh", "3DO/Arms/paraFlareUK/mono.sim");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.95F));
        Property.set(class1, "emitLen", 250F);
        Property.set(class1, "emitMax", 10F);
        Property.set(class1, "radius", 75F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 8F);
        Property.set(class1, "sound", "weapon.bomb_phball");
    }
}
