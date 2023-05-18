package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;

public class BombHeisenbergParaJu288 extends Bomb {

    public BombHeisenbergParaJu288() {
        this.chute = null;
        this.bOnChute = false;
    }

    protected void init(float diameter, float mass) {
        super.init(diameter, mass);
        this.DistFromCMtoStab *= 50F; // somehow the stock calculation from stabilizer effect is fishy. Let's give stabs a bit more power!
    }

    protected boolean haveSound() {
        return false;
    }

    public void start() {
        super.start();
        this.ttcurTM = TrueRandom.nextFloat(1.5F, 3.0F);
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
            this.getSpeed(BombHeisenbergParaJu288.v3d);
            BombHeisenbergParaJu288.v3d.scale(0.99D);
            if (BombHeisenbergParaJu288.v3d.z < -10D) {
                BombHeisenbergParaJu288.v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(BombHeisenbergParaJu288.v3d);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.mesh().setScale(2.0F);
            this.chute.mesh().setFastShadowVisibility(2);
            this.chute.pos.setRel(new Point3d(1.1D, 0.0D, -0.3D), new Orient(0.0F, 90F, 0.0F));
            this.DistFromCMtoStab = 5000F; // make bomb turn into moving direction when on chute, really!
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
        Class class1 = BombHeisenbergParaJu288.class;
        Property.set(class1, "mesh", "3DO/Arms/Heisenberg-Ju288/mono.sim");
        Property.set(class1, "radius", 3200F);
        Property.set(class1, "power", 11000000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.5F);
        Property.set(class1, "massa", 4680F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "newEffect", 1);
        Property.set(class1, "nuke", 1);
    }
}
