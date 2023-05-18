// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   MsgExplosion.java

package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;
import com.maddox.rts.Message;
import java.util.ArrayList;
import com.maddox.il2.game.Mission;

// Referenced classes of package com.maddox.il2.ai:
//            MsgExplosionListener, Explosion

public class MsgExplosion extends Message
{

	public static void send(Actor actor, String s, Point3d point3d, Actor actor1, float f, float f1, int i, float f2)
    {
        send(actor, s, point3d, actor1, f, f1, i, f2, 0);
    }
	
	public static void send(Actor actor, String s, Point3d point3d, Actor actor1, float f, float f1, int i, float f2, int j)
	{
        explosion.chunkName = s;
        explosion.p.set(point3d);
        explosion.radius = f2;
        explosion.initiator = actor1;
        explosion.power = f1;
        explosion.powerType = i;
        if(j == 1)
            explosion.bNuke = true;
        else
            explosion.bNuke = false;
        if(i == 1)
            explosion.computeSplinterParams(f);
        if(!Actor.isValid(actor1) && Mission.isSingle() && (Mission.cur().netObj() == null || Mission.cur().netObj().isMaster()))
            explosion.initiator = actor1 = Engine.actorLand();
        if(!Actor.isValid(actor1))
            return;
        if(actor1.isNet() && actor1.net.isMirror())
            return;
        if(Actor.isValid(actor))
        {
            msg.setListener(actor);
            msg.send();
        }
        if(f2 <= 0.0F)
            return;
        Engine.collideEnv().getSphere(lst, point3d, f2);
        int j1 = lst.size();
        if(j1 <= 0)
            return;
        explosion.chunkName = null;
        for(int k = 0; k < j1; k++)
        {
            Actor actor2 = (Actor)lst.get(k);
            if(Actor.isValid(actor2) && actor != actor2)
            {
                msg.setListener(actor2);
                msg.send();
            }
        }

        lst.clear();
    }

    public static void resetGame()
    {
        explosion.chunkName = null;
        explosion.initiator = null;
    }

    public boolean invokeListener(Object obj)
    {
        if(obj instanceof MsgExplosionListener)
        {
            ((MsgExplosionListener)obj).msgExplosion(explosion);
            return true;
        } else
        {
            return false;
        }
    }

    public MsgExplosion()
    {
    }

    private static MsgExplosion msg = new MsgExplosion();
    private static Explosion explosion = new Explosion();
    private static ArrayList lst = new ArrayList();

}
