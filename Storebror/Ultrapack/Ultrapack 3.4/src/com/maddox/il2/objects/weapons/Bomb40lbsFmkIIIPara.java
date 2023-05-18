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

public class Bomb40lbsFmkIIIPara extends Bomb {

    public Bomb40lbsFmkIIIPara() {
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
            this.getSpeed(Bomb40lbsFmkIIIPara.v3d);
            Bomb40lbsFmkIIIPara.v3d.scale(0.97D);
            if (Bomb40lbsFmkIIIPara.v3d.z < -2D) {
                Bomb40lbsFmkIIIPara.v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(Bomb40lbsFmkIIIPara.v3d);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.mesh().setScale(0.5F);
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
        Class class1 = Bomb40lbsFmkIIIPara.class;
        Property.set(class1, "mesh", "3DO/Arms/40lbs_F_MkIII_Para/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 20F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.15F);
        Property.set(class1, "massa", 18F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
