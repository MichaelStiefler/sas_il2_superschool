package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.MsgCollision;
import com.maddox.il2.objects.ships.WeakBody;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombTorpMk34homing extends BombTorpMk34 {

    public BombTorpMk34homing() {
        this.deltaAzimuth = 0.0F;
        this.victim = null;
        this.ship = null;
    }

    public void start() {
        super.start();
        Class class1 = this.getClass();
        this.started = Time.current();
        this.velocity = Property.floatValue(class1, "velocity", 1.0F);
        this.travelTime = (long) Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
    }

    private void sendexplosion() {
        MsgCollision.post(Time.current(), this, this.Other, (String) null, this.OtherChunk);
    }

    public void interpolateTick() {
        float f = Time.tickLenFs();
        this.pos.getAbs(Torpedo.P, Torpedo.Or);
        if (!this.flow) Ballistics.update(this, this.M, this.S);
        else {
            float f1 = (float) this.getSpeed((Vector3d) null);
            f1 += (this.velocity - f1) * 0.99F * f;
            Torpedo.spd.set(1.0D, 0.0D, 0.0D);
            Torpedo.Or.transform(Torpedo.spd);
            Torpedo.spd.scale(f1);
            this.setSpeed(Torpedo.spd);
            Torpedo.P.x += Torpedo.spd.x * f;
            Torpedo.P.y += Torpedo.spd.y * f;
            if (this.isNet() && this.isNetMirror()) {
                this.pos.setAbs(Torpedo.P, Torpedo.Or);
                this.updateSound();
                return;
            }
            if (Actor.isValid(this.victim)) {
                this.victim.pos.getAbs(pT);
                pT.sub(Torpedo.P);
                Torpedo.Or.transformInv(pT);
                if (pT.y > 1.0D) this.deltaAzimuth = -1F;
                if (pT.y < -1D) this.deltaAzimuth = 1.0F;
                Torpedo.Or.increment(5F * f * this.deltaAzimuth, 0.0F, 0.0F);
                this.deltaAzimuth = 0.0F;
                this.ship = NearestTargets.getEnemy(6, -1, Torpedo.P, 550D, 0);
                if (Actor.isValid(this.ship) && !(this.ship instanceof WeakBody) && Torpedo.P.distance(this.victim.pos.getAbsPoint()) > Torpedo.P.distance(this.ship.pos.getAbsPoint())) this.victim = this.ship;
            } else {
                this.victim = NearestTargets.getEnemy(6, -1, Torpedo.P, 550D, 0);
                if (!Actor.isValid(this.victim) || this.victim instanceof WeakBody) this.victim = null;
            }
            this.pos.setAbs(Torpedo.P, Torpedo.Or);
            if (Time.current() > this.started + this.travelTime || !World.land().isWater(Torpedo.P.x, Torpedo.P.y)) this.sendexplosion();
        }
        this.updateSound();
    }

    private float          velocity;
    private long           travelTime;
    private long           started;
    private static Point3d pT = new Point3d();
    private float          deltaAzimuth;
    private Actor          victim;
    private Actor          ship;

    static {
        Class class1 = BombTorpMk34homing.class;
        Property.set(class1, "velocity", 13F);
        Property.set(class1, "traveltime", 810.6F);
    }
}
