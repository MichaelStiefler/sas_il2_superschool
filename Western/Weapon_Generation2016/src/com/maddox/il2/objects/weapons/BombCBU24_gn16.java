// US CBU-24 Cluster bomb in SUU-30B/B dispenser

/*
* Base color is dark brown for Air Force F-105 and F-4, A-7

* When you want gray color for Navy or Marine Corps Jets F-4, A-4, A-6, A-7, add this code to mother Jets.

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                        ((BombGunCBU24_gn16)FM.CT.Weapons[i][j]).matGray();
                }
            }
    }

*/

// TODO: Edited by SAS~Storebror 0n 2019-12-26: Reduce number of spawned bomblets to reasonable figures, bomblet vector calculation fixed, code cleanup.

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;


public class BombCBU24_gn16 extends Bomb
{

    public void matGray()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("OrdnanceO", "OrdnanceGrayO");
        mesh.materialReplace("OrdnanceP", "OrdnanceGrayP");
        mesh.materialReplace("OrdnanceQ", "OrdnanceGrayQ");
    }

    public void start()
    {
        super.start();
        t1 = Time.current() + 1000L * (long)Math.max(delayExplosion, 3F) + TrueRandom.nextLong(-250L, 250L);
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
        for(int i = 0; i < 66; i++)
        {
            orient.set(TrueRandom.nextFloat(0.0F, 360F), TrueRandom.nextFloat(-90F, 90F), TrueRandom.nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(TrueRandom.nextDouble(-15D, 15D), TrueRandom.nextDouble(-15D, 15D), TrueRandom.nextDouble(-15D, 15D));
            BombletBLU26_10x bombletblu26 = new BombletBLU26_10x();
            bombletblu26.pos.setUpdateEnable(true);
            bombletblu26.pos.setAbs(point3d, orient);
            bombletblu26.pos.reset();
            bombletblu26.start();
            bombletblu26.setOwner(actor, false, false, false);
            bombletblu26.setSpeed(vector3d);
        }

        postDestroy();
    }

    private long t1;

    static 
    {
        Class class1 = BombCBU24_gn16.class; // TODO: surplus full package name removed
        Property.set(class1, "mesh", "3DO/Arms/CBU24_gn16/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.18F);
        Property.set(class1, "massa", 362.336F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.36F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}