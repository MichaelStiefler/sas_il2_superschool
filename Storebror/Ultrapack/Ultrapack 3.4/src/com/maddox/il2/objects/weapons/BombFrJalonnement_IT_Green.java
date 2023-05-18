package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.ActorLand;
import com.maddox.rts.Property;

public class BombFrJalonnement_IT_Green extends Bomb {
    public static void generate(Actor actor, Point3d point3d, float f, int i, float f1, boolean flag, boolean flag1) {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (actor != null) {
            BombFrJalonnement_IT_Green.rel.set(point3d);
            actor.pos.getAbs(BombFrJalonnement_IT_Green.tmpLoc);
            actor.pos.getCurrent(BombFrJalonnement_IT_Green.l);
            BombFrJalonnement_IT_Green.l.interpolate(BombFrJalonnement_IT_Green.tmpLoc, 0.5D);
            BombFrJalonnement_IT_Green.rel.sub(BombFrJalonnement_IT_Green.l);
            if (i == 2) {
                if (f1 < 3F) {
                    switch (World.Rnd().nextInt(1, 2)) {
                        case 1:
                            Eff3DActor.New(actor, null, BombFrJalonnement_IT_Green.rel, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", 10F);
                            break;

                        case 2:
                            Eff3DActor.New(actor, null, BombFrJalonnement_IT_Green.rel, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
                            break;
                    }
                } else {
                    Vector3d vector3d = new Vector3d();
                    for (int j = 0; j < 36; j++) {
                        vector3d.set(World.Rnd().nextDouble(-20D, 20D), World.Rnd().nextDouble(-20D, 20D), World.Rnd().nextDouble(3D, 20D));
                        float f2 = World.Rnd().nextFloat(3F, 15F);
                        BallisticProjectile ballisticprojectile = new BallisticProjectile(point3d, vector3d, f2);
                        Eff3DActor.New(ballisticprojectile, null, null, 10F, "3DO/Effects/Fireworks/FlareGreen.eff", 30000F);
                    }

                }
                return;
            }
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand) {
            super.msgCollision(actor, s, s1);
            BombFrJalonnement_IT_Green.generate(actor, this.pos.getAbsPoint(), 100F, 1, 1.0F, false, false);
            Eff3DActor.New(this, null, new Loc(), 0.5F, "3DO/Effects/Fireworks/GreenFire.eff", 30000F);
        }
    }

    private static Loc      l      = new Loc();
    private static Loc      rel    = new Loc();
    private static Loc      tmpLoc = new Loc();

    static {
        Class class1 = BombFrJalonnement_IT_Green.class;
        Property.set(class1, "mesh", "3do/arms/BombFrJalonnement_IT_Green/mono.sim");
        Property.set(class1, "radius", 2.99F);
        Property.set(class1, "power", 1.1F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.089F);
        Property.set(class1, "massa", 8.8F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
