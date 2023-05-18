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

public class BombRBK250_PTAB25_gn16 extends Bomb
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
        // "RBK-250 PTAB-2,5" contains __42x__ "PTAB-2,5" bomblets
        for(int i = 0; i < 42; i++)
        {
            orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D));
            BombPTAB25 bombptab25 = new BombPTAB25();
            bombptab25.pos.setUpdateEnable(true);
            bombptab25.pos.setAbs(point3d, orient);
            bombptab25.pos.reset();
            bombptab25.start();
            bombptab25.setOwner(actor, false, false, false);
            bombptab25.setSpeed(vector3d);
        }

        postDestroy();
    }

    private long t1;

    static 
    {
        Class class1 = BombRBK250_PTAB25_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/RBK250_PTAB25_gn16/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.300F);
        Property.set(class1, "massa", 245F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.40F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
