package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombFrEclairante_Mle1933_Vichy extends Bomb {

    public BombFrEclairante_Mle1933_Vichy() {
        this.chute = null;
        this.bOnChute = false;
        this.descentRate = 4F;
    }

    public void start() {
        super.start();
        this.descentRate = Property.floatValue(this.getClass(), "descentRate", 3F);
        this.setStartDelayedExplosion(true);
        if ((this.fuze == null) || (this.delayExplosion == 0.0F)) {
            this.t1 = Time.current() + 2000L;
            this.t2 = Time.current() + 0x49bb0L;
        } else {
            this.t1 = Time.current() + 2000L + (long) (1000F * this.delayExplosion);
            this.t2 = Time.current() + 0x49bb0L + (long) (1000F * this.delayExplosion);
        }
    }

    public void destroy() {
        if (this.chute != null) {
            this.chute.destroy();
        }
        super.destroy();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if ((actor instanceof ActorLand) && (this.chute != null)) {
            this.bOnChute = false;
        }
        this.ttcurTM = 100000F;
        if (this.chute != null) {
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
            this.getSpeed(BombFrEclairante_Mle1933_Vichy.v3d);
            BombFrEclairante_Mle1933_Vichy.v3d.scale(0.975D);
            if (Math.abs(BombFrEclairante_Mle1933_Vichy.v3d.x) < 0.00001D) {
                BombFrEclairante_Mle1933_Vichy.v3d.x = 0.00001D;
            }
            if (Math.abs(BombFrEclairante_Mle1933_Vichy.v3d.y) < 0.00001D) {
                BombFrEclairante_Mle1933_Vichy.v3d.y = 0.00001D;
            }
            if (BombFrEclairante_Mle1933_Vichy.v3d.z < (-this.descentRate)) {
                float f = (float) Math.toRadians(-Bomb.Or.getTangage());
                BombFrEclairante_Mle1933_Vichy.v3d.z += 10F * Time.tickConstLenFs() * Math.sin(f);
            }
            this.setSpeed(BombFrEclairante_Mle1933_Vichy.v3d);
            this.pos.getAbs(Bomb.P, Bomb.Or);
        } else if (this.curTm > (this.ttcurTM + 2.0F)) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.mesh().setScale(0.5F);
            this.chute.pos.setRel(new Point3d(0.1D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
            Eff3DActor.New(this, null, new Loc(), 0.7F, "3DO/Effects/Fireworks/FlareWhite.eff", (this.t2 - this.t1) / 1000F);
            Eff3DActor.New(this, null, new Loc(), 0.5F, "3DO/Effects/Fireworks/ParaFlareSmoke.eff", (this.t2 - this.t1) / 1000F);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 1.0F, 0.8F);
            if ((this.t2 - 2000L) > Time.current()) {
                lightpointactor.light.setEmit(1.0F, 250F);
            } else {
                lightpointactor.light.setEmit(0.9F, 220F);
            }
            this.draw.lightMap().put("light", lightpointactor);
        }
        this.pos.getRel(Bomb.P, Bomb.Or);
        if (this.t2 < Time.current()) {
            this.postDestroy();
        }
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;
    private long            t1;
    private long            t2;
    protected float         descentRate;

    static {
        Class class1 = BombFrEclairante_Mle1933_Vichy.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrEclairante_Mle1933_Vichy/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 11.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.15F);
        Property.set(class1, "massa", 30F);
        Property.set(class1, "sound", "weapon.bomb_phball");
        Property.set(class1, "descentRate", 2.0F);
        Property.set(class1, "fuze", ((new Object[] { Fuze_Eclairante_Mle1930.class })));
    }
}
