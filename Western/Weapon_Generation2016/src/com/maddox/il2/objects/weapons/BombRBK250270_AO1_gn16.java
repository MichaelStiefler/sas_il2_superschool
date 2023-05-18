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
//            Bomb, BombAO1Sch

public class BombRBK250270_AO1_gn16 extends Bomb
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
        Explosions.AirFlak(pos.getAbsPoint(), 1);
        Actor actor = null;
        if(Actor.isValid(getOwner()))
            actor = getOwner();
        Point3d point3d = new Point3d(pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        // "RBK-250-270 AO-1Sch" contains __150x__ "AO-1Sch" bomblets, reduced into 15x cycles with 10x merged class
        for(int i = 0; i < 15; i++)
        {
            orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D));
            BombAO1Sch_10x bombao1 = new BombAO1Sch_10x();
            bombao1.pos.setUpdateEnable(true);
            bombao1.pos.setAbs(point3d, orient);
            bombao1.pos.reset();
            bombao1.start();
            bombao1.setOwner(actor, false, false, false);
            bombao1.setSpeed(vector3d);
        }

        postDestroy();
    }

    private long t1;

    static 
    {
        Class class1 = BombRBK250270_AO1_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/RBK250_PTAB25_gn16/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.300F);
        Property.set(class1, "massa", 273F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.40F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
