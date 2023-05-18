
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;


public class Bomb750lbsM117Daisy_gn16 extends Bomb
{

    public Bomb750lbsM117Daisy_gn16()
    {
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        super.pos.getRel(Bomb.P, Bomb.Or);
        if(((Tuple3d) (Bomb.P)).z < 2D + World.land().HQ(((Tuple3d) (Bomb.P)).x, ((Tuple3d) (Bomb.P)).y))
            doExplosion(this, "M117");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Bomb750lbsM117Daisy_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/750lbsBombM117_gn16/monoDaisy.sim");
        Property.set(class1, "radius", 130F);
        Property.set(class1, "power", 210F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.408F);
        Property.set(class1, "massa", 346F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class
        })));
		Property.set(class1, "dragCoefficient", 0.32F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
        Property.set(class1, "groupLeader", "Bomb750lbsM117_gn16"); // used by Bomb Select
    }
}