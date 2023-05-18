// Last Modified by: western0221 2020-01-12

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, BombPTAB25

public class BombRBK500_PTAB1M_gn16 extends Bomb
{

    public void start()
    {
        super.start();
        t1 = Time.current() + 1000L * (long)Math.max(super.delayExplosion, 3F) + World.Rnd().nextLong(-250L, 250L);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(t1 < Time.current())
            doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(t1 > Time.current())
            doFireContaineds();
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds()
    {
        Explosions.AirFlak(super.pos.getAbsPoint(), 1);
        Actor actor = null;
        if(Actor.isValid(getOwner()))
            actor = getOwner();
        Point3d point3d = new Point3d(super.pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        // "RBK-500 PTAB-1M" contains __268x__ "PTAB-1M" bomblets, reduced into 27x cycles with 10x merged class
        for(int i = 0; i < 27; i++)
        {
            orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D));
            BombPTAB1M_10x bombptab1m = new BombPTAB1M_10x();
            bombptab1m.pos.setUpdateEnable(true);
            bombptab1m.pos.setAbs(point3d, orient);
            bombptab1m.pos.reset();
            bombptab1m.start();
            bombptab1m.setOwner(actor, false, false, false);
            bombptab1m.setSpeed(vector3d);
        }

        postDestroy();
    }

    private long t1;

    static 
    {
        Class class1 = BombRBK500_PTAB1M_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/RBK500_AO25_gn16/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.450F);
        Property.set(class1, "massa", 490F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.47F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
