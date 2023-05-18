package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.ActorLand;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombTiYw extends Bomb {

    protected boolean haveSound() {
        return (super.index % 8) == 0;
    }

    public void start() {
        super.start();
        this.drawing(false);
        this.t1 = Time.current() + 429500L + World.Rnd().nextLong(0L, 850L);
        this.t2 = Time.current() + 429200L + World.Rnd().nextLong(0L, 3800L);
        Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/TIYellow.eff", (this.t1 - Time.current()) / 1000F);
//        World.MaxVisualDistance = 35000F; // TODO: Fixed by SAS~Storebror: WTF???!??
    }

    public void interpolateTick() {
        super.curTm += Time.tickLenFs();
        Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
        this.updateSound();
        if (this.t1 < Time.current()) {
            if (Config.isUSE_RENDER()) {
                Eff3DActor eff3dactor = Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/PhosfourousFire.eff", (this.t2 - this.t1) / 1000F);
                if ((super.index % 30) == 0) {
                    LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                    lightpointactor.light.setColor(1.0F, 1.0F, 0.0F);
                    lightpointactor.light.setEmit(1.0F, 300F);
                    ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
                }
            }
            this.t1 = this.t2 + 1L;
        }
        if (this.t2 < Time.current()) {
            this.postDestroy();
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand) {
            if (Time.current() > ((this.t2 + this.t1) / 2L)) {
                return;
            } else {
                Point3d point3d = new Point3d();
                super.pos.getTime(Time.current(), point3d);
                Class class1 = this.getClass();
                float f = Property.floatValue(class1, "power", 0.0F);
                int i = Property.intValue(class1, "powerType", 0);
                float f1 = Property.floatValue(class1, "radius", 1.0F);
                MsgExplosion.send(actor, s1, point3d, this.getOwner(), super.M, f, i, f1);
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

    private long t1;
    private long t2;

    static {
        Class class1 = BombTiYw.class;
        Property.set(class1, "mesh", "3DO/Arms/B22EZ/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 0.5F);
        Property.set(class1, "sound", "weapon.bomb_phball");
    }
}
