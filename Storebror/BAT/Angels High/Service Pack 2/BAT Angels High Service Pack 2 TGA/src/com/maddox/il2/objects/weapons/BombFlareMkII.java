package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombFlareMkII extends Bomb {

    public BombFlareMkII() {
        this.chute = null;
        this.bOnChute = false;
    }

    protected boolean haveSound() {
        return false;
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
        this.drawing(true);
        this.t1 = Time.current() + 1500L + World.Rnd().nextLong(0L, 850L);
        this.t2 = Time.current() + 32000L + World.Rnd().nextLong(0L, 3800L);
    }

    public void destroy() {
        if (this.chute != null) {
            this.chute.destroy();
        }
        super.destroy();
    }

    public void interpolateTick() {
        this.curTm += Time.tickLenFs();
        if (this.bOnChute) {
            this.getSpeed(v3d);
            v3d.scale(0.97D);
            if (((Tuple3d) (v3d)).z < -2D) {
                v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(v3d);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.mesh().setScale(0.5F);
            this.chute.pos.setRel(new Point3d(0.5D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        }
        Ballistics.updateBomb(this, this.M, this.S, this.J, this.DistFromCMtoStab);
        this.updateSound();
        if (this.t1 < Time.current()) {
            if (Config.isUSE_RENDER()) {
                Eff3DActor eff3dactor = Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/Flare_White.eff", (this.t2 - this.t1) / 1000F);
                if ((this.index % 30) == 0) {
                    LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                    lightpointactor.light.setColor(1.0F, 1.0F, 0.0F);
                    lightpointactor.light.setEmit(1.0F, 300F);
                    eff3dactor.draw.lightMap().put("light", lightpointactor);
                }
            }
            this.t1 = this.t2 + 1L;
        }
        if (this.t2 < Time.current()) {
            this.postDestroy();
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if ((actor instanceof ActorLand) && (this.chute != null)) {
            this.chute.landing();
        }
        super.msgCollision(actor, s, s1);
        if (actor instanceof ActorLand) {
            if (Time.current() > ((this.t2 + this.t1) / 2L)) {
                return;
            } else {
                Point3d point3d = new Point3d();
                this.pos.getTime(Time.current(), point3d);
                Class class1 = this.getClass();
                float f = Property.floatValue(class1, "power", 0.0F);
                int i = Property.intValue(class1, "powerType", 0);
                float f1 = Property.floatValue(class1, "radius", 1.0F);
                MsgExplosion.send(actor, s1, point3d, this.getOwner(), this.M, f, i, f1);
                Vector3d vector3d = new Vector3d();
                this.getSpeed(vector3d);
                vector3d.x *= 0.5D;
                vector3d.y *= 0.5D;
                vector3d.z = 1.0D;
                this.setSpeed(vector3d);
                return;
            }
        } else {
            super.msgCollision(actor, s, s1);
            return;
        }
    }

    private long            t1;
    private long            t2;
    private Chute           chute;
    private boolean         bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;

    static {
        Class class1 = BombFlareMkII.class;
        Property.set(class1, "mesh", "3DO/Arms/Flare_MKII/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 12.5F);
        Property.set(class1, "sound", "weapon.bomb_phball");
    }
}
