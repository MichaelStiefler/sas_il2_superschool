/*
 * MsgExplosion - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package com.maddox.il2.ai;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Message;

public class MsgExplosion extends Message {
    private static MsgExplosion msg       = new MsgExplosion();
    private static Explosion    explosion = new Explosion();
    private static ArrayList    lst       = new ArrayList();

    public static void send(Actor actor, String s, Point3d point3d, Actor actor1, float f, float f1, int i, float f2) {
        send(actor, s, point3d, actor1, f, f1, i, f2, 0);
    }

    public static void send(Actor actor, String s, Point3d point3d, Actor owner, float f, float f1, int i, float f2, int j) {
//		System.out.println("MsgExplosion send("
//				+ (actor==null?"null":actor.getClass().getName()) + ", "
//				+ s + ", point3d, "
//				+ (owner==null?"null":owner.getClass().getName()) + ", "
//				+ f + ", "
//				+ f1 + ", "
//				+ i + ", "
//				+ f2 + ", "
//				+ j + ")"
//				);
        explosion.chunkName = s;
        explosion.p.set(point3d);
        explosion.radius = f2;
        explosion.initiator = owner;
        explosion.power = f1;
        explosion.powerType = i;
        if (j == 1) explosion.bNuke = true;
        else explosion.bNuke = false;
        if (i == 1) explosion.computeSplinterParams(f);
        if (!Actor.isValid(owner) && Mission.isSingle() && (Mission.cur().netObj() == null || Mission.cur().netObj().isMaster())) explosion.initiator = owner = Engine.actorLand();
        else if (!Actor.isValid(owner)) explosion.initiator = owner = Engine.actorLand();

//		System.out.println("MsgExplosion send Actor.isValid(owner)=" + Actor.isValid(owner)
//				+ ", owner.isNet()=" + owner.isNet()
//				+ ", owner.net.isMirror()=" + owner.net.isMirror()
//				);

        if (Actor.isValid(owner) && (!owner.isNet() || !owner.net.isMirror())) {
            if (Actor.isValid(actor)) {
                msg.setListener(actor);
                msg.send();
            }
            if (!(f2 <= 0.0F)) {
                Engine.collideEnv().getSphere(lst, point3d, f2);
                int j1 = lst.size();
                if (j1 > 0) {
                    explosion.chunkName = null;
                    for (int k = 0; k < j1; k++) {
                        Actor actor2 = (Actor) lst.get(k);
                        if (Actor.isValid(actor2) && actor != actor2) {
//							System.out.println("MsgExplosion send actor2 is valid:" + actor2.getClass().getName());
                            msg.setListener(actor2);
                            msg.send();
                        }
                    }
                    lst.clear();
                }
            }
        }
    }

    public static void resetGame() {
        explosion.chunkName = null;
        explosion.initiator = null;
    }

    public boolean invokeListener(Object obj) {
        if (obj instanceof MsgExplosionListener) {
            ((MsgExplosionListener) obj).msgExplosion(explosion);
            return true;
        }
        return false;
    }
}
