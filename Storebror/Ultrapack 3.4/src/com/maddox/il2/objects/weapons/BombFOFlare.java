package com.maddox.il2.objects.weapons;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombFOFlare extends Bomb {

    protected boolean haveSound() {
        return false;
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(0.1F, 0.2F);
        this.t1 = Time.current() + 0x68dbcL + World.Rnd().nextLong(0L, 850L);
        this.t2 = Time.current() + 0x68c90L + World.Rnd().nextLong(0L, 3800L);
        this.drawing(false);
        Eff3DActor.New(this, null, new Loc(), 1.0F, "Effects/Smokes/WPsmoke.eff", (this.t1 - Time.current()) / 1000F);
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (!this.bFinsDeployed && this.curTm > this.ttcurTM) {
            this.bFinsDeployed = true;
            this.S *= 300F;
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand) {
            if (Time.current() <= (this.t2 + this.t1) / 2L) {
                Point3d point3d = new Point3d();
                this.pos.getTime(Time.current(), point3d);
                Class class1 = this.getClass();
                float f = Property.floatValue(class1, "power", 0.0F);
                int i = Property.intValue(class1, "powerType", 0);
                float f1 = Property.floatValue(class1, "radius", 10F);
                MsgExplosion.send(actor, s1, point3d, this.getOwner(), this.M, f, i, f1);
                Vector3d vector3d = new Vector3d();
                this.getSpeed(vector3d);
                vector3d.x *= 0.5D;
                vector3d.y *= 0.5D;
                vector3d.z = 1.0D;
                this.setSpeed(vector3d);
                if (this.counter > 200 && !this.marked) {
                    this.marked = true;
                    HUD.logCenter("                                                                              Artillery Firing!");
                    this.counter = 0;
                }
                Random random = new Random();
                int j = random.nextInt(10);
                j -= 5;
                if (this.marked && this.counter > 25 + j) {
                    int k = random.nextInt(150);
                    int l = k - 75;
                    point3d.x += l;
                    k = random.nextInt(150);
                    l = k - 75;
                    point3d.y += l;
                    Explosions.generate(actor, point3d, 25F, 0, 136F, !Mission.isNet());
                    MsgExplosion.send(actor, s, point3d, this.getOwner(), 0.0F, 25F, 0, 136F);
                    this.counter = 0;
                }
                this.counter++;
            }
        } else super.msgCollision(actor, s, s1);
    }

    private long    t1;
    private long    t2;
    private int     counter;
    private boolean marked;
    private boolean bFinsDeployed;
    private float   ttcurTM;

    static {
        Class class1 = BombFOFlare.class;
        Property.set(class1, "mesh", "3DO/Arms/Null/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.0508F);
        Property.set(class1, "massa", 1.2F);
    }
}
