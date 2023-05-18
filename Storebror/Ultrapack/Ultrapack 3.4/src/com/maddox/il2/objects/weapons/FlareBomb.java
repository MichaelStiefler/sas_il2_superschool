package com.maddox.il2.objects.weapons;

import java.util.List;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class FlareBomb extends Bomb {

    public FlareBomb() {
        this.lightOn = false;
        this.lightDie = false;
        this.radius = 0.0F;
        this.intensity = 0.0F;
        this.light = null;
        this.eff = null;
        this.smoke = null;
        this.burnTime = 0.0F;
        this.lightStartTime = 0.0F;
        this.tick = 0;
        this.chute = null;
        this.bOnChute = false;
        this.decentRate = 4F;
    }

    protected boolean haveSound() {
        return false;
    }

    public boolean isArmed() {
        return false;
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(4F, 5F);
        this.decentRate = Property.floatValue(this.getClass(), "decentRate", 3F);
    }

    public void startLight() {
        this.lightOn = true;
        Class class1 = this.getClass();
        this.light = new LightPointActor(new LightPointWorld(), new Point3d());
        if (this.light != null) this.light.light.setColor((Color3f) Property.value(class1, "flareColor", new Color3f(1.0F, 1.0F, 0.5F)));
        this.radius = Property.floatValue(class1, "flareLen", 50F);
        this.intensity = Property.floatValue(class1, "flareMax", 1.0F);
        this.burnTime = Property.floatValue(class1, "flareBurnTime", 180F);
        if (this.light != null) {
            this.light.light.setEmit(this.intensity, this.radius);
            this.draw.lightMap().put("flareBomb", this.light);
        }
        float f = Aircraft.cvt(Property.floatValue(class1, "flareLen", 250F), 250F, 4000F, 2.0F, 5F);
        this.eff = Eff3DActor.New(this, this.findHook("_flare"), null, f, "3DO/Effects/Fireworks/FlareWhiteWide.eff", -1F);
        String s = Property.stringValue(class1, "flareSmoke", null);
        if (s != null) {
            this.smoke = Eff3DActor.New(this, this.findHook("_flare"), null, 1.0F, s, -1F);
            if (this.smoke != null) this.smoke.pos.changeHookToRel();
        }
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.bOnChute) {
            this.getSpeed(v3d);
            v3d.scale(0.975D);
            if (Math.abs(v3d.x) < 9.9999997473787516E-006D) v3d.x = 9.9999997473787516E-006D;
            if (Math.abs(v3d.y) < 9.9999997473787516E-006D) v3d.y = 9.9999997473787516E-006D;
            if (v3d.z < -this.decentRate) {
                float f = (float) Math.toRadians(-Bomb.Or.getTangage());
                v3d.z += 10F * Time.tickConstLenFs() * Math.sin(f);
            }
            this.setSpeed(v3d);
            this.pos.getAbs(Bomb.P, Bomb.Or);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            this.setChute();
            this.startLight();
        }
        if (this.lightOn) {
            this.lightStartTime += Time.tickLenFs();
            float f1 = World.Rnd().nextFloat(-0.1F, 0.1F);
            if (this.light != null) this.light.light.setEmit(this.intensity + f1, this.radius + f1 * 100F);
            if (this.eff != null) this.eff._setIntesity(this.intensity + f1);
            this.setIlluminations();
        }
        if (this.burnTime < this.lightStartTime && this.lightOn) {
            this.lightOn = false;
            this.lightDie = true;
        }
        if (this.lightDie) {
            this.intensity *= 0.97F;
            this.radius *= 0.97F;
            float f2 = World.Rnd().nextFloat(-0.1F, 0.1F);
            if (this.light != null) this.light.light.setEmit(this.intensity + f2, this.radius + f2 * 100F);
            if (this.eff != null) this.eff._setIntesity(this.intensity);
            if (this.intensity < 0.05F || this.radius < 1.0F) {
                if (this.light != null) this.light.light.setEmit(0.0F, 0.0F);
                if (this.eff != null) this.eff.destroy();
                this.lightDie = false;
                if (this.smoke != null) this.smoke._finish();
            }
        }
    }

    protected void setChute() {
        this.chute = new Chute(this);
        this.chute.collide(false);
        this.chute.mesh().setScale(0.5F);
        this.chute.pos.setRel(new Point3d(0.3D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand && this.chute != null) this.chute.landing();
        super.msgCollision(actor, s, s1);
    }

    public void destroy() {
        if (this.eff != null) this.eff.destroy();
        if (this.light != null) this.light.destroy();
        if (this.chute != null) this.chute.destroy();
        super.destroy();
    }

    public void setIlluminations() {
        label0: {
            Mission.cur();
            if (!Mission.isSingle()) {
                Mission.cur();
                if (!Mission.isServer()) break label0;
            }
            if (this.tick < 10) {
                this.tick++;
                return;
            }
            this.tick = 0;
            List list = Engine.targets();
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);
                if (actor instanceof Aircraft) {
                    v3d.sub(actor.pos.getAbsPoint(), this.pos.getAbsPoint());
                    double d = v3d.length();
                    if (d < this.radius) ((Aircraft) actor).tmSearchlighted = Time.current();
                }
            }

        }
    }

    private boolean         lightOn;
    private boolean         lightDie;
    float                   radius;
    float                   intensity;
    LightPointActor         light;
    Eff3DActor              eff;
    Eff3DActor              smoke;
    float                   burnTime;
    float                   lightStartTime;
    int                     tick;
    protected Chute         chute;
    private boolean         bOnChute;
    private static Vector3d v3d = new Vector3d();
    protected float         ttcurTM;
    protected float         decentRate;

}
