package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.MsgCollision;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.il2.objects.ships.WeakBody;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombTorpTypeMXhomingChuted extends BombParaTorp {

    public BombTorpTypeMXhomingChuted() {
        this.deltaAzimuth = 0.0F;
        this.victim = null;
        this.ship = null;
        this.chute1 = null;
        this.chute2 = null;
        this.bOnChute1 = false;
        this.bOnChute2 = false;
    }

    public void start() {
        super.start();
        Class class1 = this.getClass();
        this.started = Time.current();
        this.velocity = Property.floatValue(class1, "velocity", 1.0F);
        this.travelTime = (long) Property.floatValue(class1, "traveltime", 1.0F) * 1000L;
        this.ttcurTM = 1.5F;
        this.openHeight = 180F;
    }

    public void destroy() {
        if (this.chute1 != null) this.chute1.destroy();
        if (this.chute2 != null) this.chute2.destroy();
        super.destroy();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand && (this.chute1 != null || this.chute2 != null)) this.bOnChute1 = false;
        this.ttcurTM = 100000F;
        if (this.chute1 != null) this.chute1.landing();
        if (this.chute2 != null) this.chute2.landing();
        super.msgCollision(actor, s, s1);
    }

    private void sendexplosion() {
        MsgCollision.post(Time.current(), this, this.Other, (String) null, this.OtherChunk);
    }

    public void interpolateTick() {
        float f = Time.tickLenFs();
        this.pos.getAbs(Torpedo.P, Torpedo.Or);
//        if (!this.flow) Ballistics.update(this, this.M, this.S);
        if (this.flow) {
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
        } else {
            this.curTm += Time.tickLenFs();
            super.interpolateTick();
            if (this.bOnChute2) {
                this.getSpeed(v3d);
                v3d.scale(0.99D);
                if (v3d.z < -40D) v3d.z += 8F * Time.tickConstLenFs();
                this.setSpeed(v3d);
                this.pos.getAbs(P, Or);
            } else if (this.bOnChute1) {
                this.getSpeed(v3d);
                v3d.scale(0.99D);
                if (v3d.z < -90D) v3d.z += 1.1F * Time.tickConstLenFs();
                this.setSpeed(v3d);
                this.pos.getAbs(P, Or);
                if (P.z < this.openHeight) {
                    this.bOnChute2 = true;
                    this.bOnChute1 = false;
                    this.chute2 = new Chute(this);
                    this.chute2.collide(false);
                    this.chute2.mesh().setScale(2.5F);
                    this.chute2.pos.setRel(new Point3d(3D, 0.0D, 0.0D), new Orient(0.0F, 90.0F, 0.0F));
                    this.chute2.mesh().setFastShadowVisibility(2); // Disable Shadows for scaled chute mesh!
                    
//                    this.chute2.pos.setRel(new Point3d(3D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
                    if (this.chute1 != null) this.chute1.destroy();
                }
            } else if (this.curTm > this.ttcurTM) {
                this.bOnChute1 = true;
                this.chute1 = new Chute(this);
                this.chute1.collide(false);
                chute1.mesh().setScale(0.4F);
                this.chute1.pos.setRel(new Point3d(0.5D, 0.0D, 0.0D), new Orient(0.0F, 90.0F, 0.0F));
                this.chute1.mesh().setFastShadowVisibility(2); // Disable Shadows for scaled chute mesh!
//                this.chute1.pos.setRel(new Point3d(0.5D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
            }
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

    private Chute           chute1;
    private Chute           chute2;
    public boolean          bOnChute1;
    public boolean          bOnChute2;
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;
    private float           openHeight;

    static {
        Class class1 = BombTorpTypeMXhomingChuted.class;
        Property.set(class1, "mesh", "3DO/Arms/TypeMX_Homing/mono.sim");
        Property.set(class1, "radius", 90.8F);
        Property.set(class1, "power", 160F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.569F);
        Property.set(class1, "massa", 522.1F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 17F);
        Property.set(class1, "traveltime", 150F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngle", 27.5F);
        Property.set(class1, "impactAngleMin", 10.0F);
        Property.set(class1, "impactAngleMax", 45.0F);
        Property.set(class1, "impactSpeed", 600F);
        Property.set(class1, "armingTime", 1F);
        Property.set(class1, "dropAltitude", 160F);
        Property.set(class1, "dropSpeed", 400F);
    }
}
