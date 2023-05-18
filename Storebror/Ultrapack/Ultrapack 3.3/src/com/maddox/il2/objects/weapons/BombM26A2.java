package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.air.B_25;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombM26A2 extends Bomb {

    public void start() {
        super.start();
        try {
            if (World.getPlayerAircraft().FM.actor instanceof B_25) this.t1 = Time.current() + 200L;
            else this.t1 = Time.current() + 1000L * (long) Math.max(this.delayExplosion, 3F) + World.Rnd().nextLong(-250L, 250L);
        } catch (Exception exception) {
            this.t1 = Time.current() + 1000L * (long) Math.max(this.delayExplosion, 3F) + World.Rnd().nextLong(-250L, 250L);
        }
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.t1 < Time.current()) this.doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (this.t1 > Time.current()) this.doFireContaineds();
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds() {
        Actor actor = null;
        if (Actor.isValid(this.getOwner())) actor = this.getOwner();
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        Point3d point3d = new Point3d(this.pos.getCurrentPoint());
        o.set(0.0F, 0.0F, 0.0F);
        l.set(point3d, o);
        point3d = new Point3d(l.getX(), l.getY(), l.getZ());
        for (int i = 0; i < 20; i++) {
            switch (i) {
                case 0:
                    point3d.add(-0.1413D, -0.0979D, -0.1223D);
                    break;

                case 1:
                    point3d.add(-0.1437D, -0.003D, -0.1221D);
                    break;

                case 2:
                    point3d.add(-0.1429D, 0.0926D, -0.1205D);
                    break;

                case 3:
                    point3d.add(-0.1443D, 0.0473D, -0.031D);
                    break;

                case 4:
                    point3d.add(-0.1443D, -0.0456D, -0.0295D);
                    break;

                case 5:
                    point3d.add(-0.1438D, -0.1405D, -0.03D);
                    break;

                case 6:
                    point3d.add(-0.143D, 0.1377D, -0.0306D);
                    break;

                case 7:
                    point3d.add(-0.1438D, 0.0927D, 0.0603D);
                    break;

                case 8:
                    point3d.add(-0.1439D, -0.0003D, 0.0607D);
                    break;

                case 9:
                    point3d.add(-0.1427D, -0.0938D, 0.0603D);
                    break;

                case 10:
                    point3d.add(0.2426D, -0.0933D, 0.0599D);
                    break;

                case 11:
                    point3d.add(0.2414D, 0.0002D, 0.0603D);
                    break;

                case 12:
                    point3d.add(0.2415D, 0.0932D, 0.0599D);
                    break;

                case 13:
                    point3d.add(0.2424D, 0.1382D, -0.0311D);
                    break;

                case 14:
                    point3d.add(0.2415D, -0.14D, -0.0305D);
                    break;

                case 15:
                    point3d.add(0.2411D, -0.0451D, -0.03D);
                    break;

                case 16:
                    point3d.add(0.241D, 0.0479D, -0.0315D);
                    break;

                case 17:
                    point3d.add(0.2424D, 0.0931D, -0.121D);
                    break;

                case 18:
                    point3d.add(0.2416D, -0.0025D, -0.1225D);
                    break;

                case 19:
                    point3d.add(0.244D, -0.0974D, -0.1227D);
                    break;
            }
            orient.set(0.0F, 0.0F, 0.0F);
            this.getSpeed(vector3d);
            vector3d.add(0.0D, World.Rnd().nextDouble(-4D, 4D), World.Rnd().nextDouble(-4D, 4D));
            BombM41A1 bombm41a1 = new BombM41A1();
            bombm41a1.pos.setUpdateEnable(true);
            bombm41a1.pos.setAbs(point3d, orient);
            bombm41a1.pos.reset();
            bombm41a1.start();
            bombm41a1.setOwner(actor, false, false, false);
            bombm41a1.setSpeed(vector3d);
        }

        this.postDestroy();
    }

    private long          t1;
    private static Loc    l = new Loc();
    private static Orient o = new Orient();

    static {
        Class class1 = BombM26A2.class;
        Property.set(class1, "mesh", "3DO/Arms/M26A2_20/mono.sim");
        Property.set(class1, "power", 4F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 188.6944F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
