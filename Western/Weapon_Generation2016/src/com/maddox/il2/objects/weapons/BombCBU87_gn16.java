// US CBU-87 Cluster bomb

// TODO: Edited by SAS~Storebror 0n 2019-12-26: Reduce number of spawned bomblets to reasonable figures, bomblet vector calculation fixed, code cleanup.

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;


public class BombCBU87_gn16 extends Bomb
{

    public void start()
    {
        super.start();
        t0 = Time.current() + 500L + TrueRandom.nextLong(-150L, 150L);
        t1 = Time.current() + 1000L * (long)Math.max(super.delayExplosion, 3F) + TrueRandom.nextLong(-250L, 250L);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(t0 < Time.current() && !bTailfinExtended)
            extendTailfin();
        if(t1 < Time.current())
            doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(t1 > Time.current())
            doFireContaineds();
        super.msgCollision(actor, s, s1);
    }

    private void extendTailfin()
    {
        setMesh("3DO/Arms/CBU87_gn16/monoDrop.sim");

        bTailfinExtended = true;
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
        for(int i = 0; i < 51; i++)
        {
            orient.set(TrueRandom.nextFloat(0.0F, 360F), TrueRandom.nextFloat(-90F, 90F), TrueRandom.nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(TrueRandom.nextDouble(-15D, 15D), TrueRandom.nextDouble(-15D, 15D), TrueRandom.nextDouble(-15D, 15D));
            BombletBLU97_4x bombletblu97 = new BombletBLU97_4x();
            bombletblu97.pos.setUpdateEnable(true);
            bombletblu97.pos.setAbs(point3d, orient);
            bombletblu97.pos.reset();
            bombletblu97.start();
            bombletblu97.setOwner(actor, false, false, false);
            bombletblu97.setSpeed(vector3d);
        }

        postDestroy();
    }

    private long t0;
    private long t1;
    private boolean bTailfinExtended = false;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombCBU87_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/CBU87_gn16/mono.sim");
        Property.set(class1, "radius", 1.5F);
        Property.set(class1, "power", 0.25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.396F);
        Property.set(class1, "massa", 430.9F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.36F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}