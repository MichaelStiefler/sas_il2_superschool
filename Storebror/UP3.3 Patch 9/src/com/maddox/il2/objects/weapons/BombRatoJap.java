package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombRatoJap extends Bomb {

    public BombRatoJap() {
        this.chute = null;
        this.bOnChute = false;
    }

    protected boolean haveSound() {
        return false;
    }

    public void startSound() {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (this.sound == null) {
            this.sound = this.newSound("weapon.rato", true);
        }
        this.sound.setVolume(1.0F);
    }

    public void stopSound() {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (this.sound != null) {
            this.sound.cancel();
        }
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(0.5F, 3.5F);
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.bOnChute) {
            this.pos.getAbs(BombRatoJap.or);
            BombRatoJap.or.interpolate(BombRatoJap.or_, 0.4F);
            this.pos.setAbs(BombRatoJap.or);
            this.getSpeed(BombRatoJap.v3d);
            BombRatoJap.v3d.scale(0.997D);
            if (BombRatoJap.v3d.z < -5D) {
                BombRatoJap.v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(BombRatoJap.v3d);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.setMesh("3DO/Arms/RatoJapChuted/mono.sim");
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand) {
            if (this.chute != null) {
                this.chute.landing();
            }
            this.postDestroy();
            return;
        } else {
            super.msgCollision(actor, s, s1);
            return;
        }
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Orient   or  = new Orient();
    private static Orient   or_ = new Orient(0.0F, 0.0F, 0.0F);
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;

    static {
        Class class1 = BombRatoJap.class;
        Property.set(class1, "mesh", "3DO/Arms/RatoJap/mono.sim");
        Property.set(class1, "radius", 0.1F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.7F);
        Property.set(class1, "massa", 0.9F);
        Property.set(class1, "sound", "weapon.bomb_phball");
    }
}
