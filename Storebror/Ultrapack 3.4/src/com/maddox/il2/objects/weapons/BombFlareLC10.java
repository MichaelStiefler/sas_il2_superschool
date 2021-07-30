package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
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

public class BombFlareLC10 extends Bomb {

    public BombFlareLC10() {
        this.chute = null;
        this.bOnChute = false;
        this.descentRate = 4F;
    }

    public void start() {
        super.start();
        this.descentRate = Property.floatValue(this.getClass(), "decentRate", 3F);
        this.t1 = Time.current() + 1000L;
        this.t2 = Time.current() + 61000L;
    }

    public void destroy() {
        if (this.chute != null) this.chute.destroy();
        super.destroy();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand && this.chute != null) this.bOnChute = false;
        this.ttcurTM = 100000F;
        if (this.chute != null) this.chute.landing();
        super.msgCollision(actor, s, s1);
        if (actor instanceof ActorLand) {
            if (Time.current() > (this.t2 + this.t1) / 2L) return;
            else {
                Point3d point3d = new Point3d();
                this.pos.getTime(Time.current(), point3d);
                Class class1 = this.getClass();
                float f = Property.floatValue(class1, "power", 0.0F);
                int i = Property.intValue(class1, "powerType", 0);
                float f1 = Property.floatValue(class1, "radius", 1.0F);
                MsgExplosion.send(actor, s1, point3d, this.getOwner(), this.M, f, i, f1);
                return;
            }
        } else {
            super.msgCollision(actor, s, s1);
            return;
        }
    }

    public void interpolateTick() {
        this.curTm += Time.tickLenFs();
        super.interpolateTick();
        if (this.bOnChute) {
            this.getSpeed(v3d);
            v3d.scale(0.975D);
            if (Math.abs(v3d.x) < 9.9999997473787516E-006D) v3d.x = 9.9999997473787516E-006D;
            if (Math.abs(v3d.y) < 9.9999997473787516E-006D) v3d.y = 9.9999997473787516E-006D;
            if (v3d.z < -this.descentRate) {
                float f = (float) Math.toRadians(-Bomb.Or.getTangage());
                v3d.z += 10F * Time.tickConstLenFs() * Math.sin(f);
            }
            this.setSpeed(v3d);
            this.pos.getAbs(Bomb.P, Bomb.Or);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.mesh().setScale(0.5F);
            this.chute.pos.setRel(new Point3d(0.1D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        }
        this.pos.getRel(Bomb.P, Bomb.Or);
        if (this.t1 < Time.current() + 0.5F && Config.isUSE_RENDER() && this.t2 > Time.current()) {
            Eff3DActor.New(this, null, new Loc(), 0.5F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", (this.t2 - this.t1) / 1000F);
            Eff3DActor.New(this, null, new Loc(), 0.5F, "3DO/Effects/Fireworks/ParaFlareSmoke.eff", (this.t2 - this.t1) / 1000F);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 1.0F, 0.0F);
            if (this.t2 - 2000L > Time.current()) lightpointactor.light.setEmit(1.0F, 300F);
            else lightpointactor.light.setEmit(0.9F, 270F);
            this.draw.lightMap().put("light", lightpointactor);
            this.t1 = this.t2 + 1L;
        }
        if (this.t2 < Time.current()) this.postDestroy();
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;
    private long            t1;
    private long            t2;
    protected float         descentRate;

    static {
        Class class1 = BombFlareLC10.class;
        Property.set(class1, "mesh", "3DO/Arms/LC10/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 8F);
        Property.set(class1, "sound", "weapon.bomb_phball");
        Property.set(class1, "descentRate", 3.556F);
    }
}
