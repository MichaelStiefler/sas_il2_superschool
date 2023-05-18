
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;


public class BombMk82Daisy_gn16 extends Bomb
{

    public BombMk82Daisy_gn16()
    {
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        super.pos.getRel(Bomb.P, Bomb.Or);
        if(((Tuple3d) (Bomb.P)).z < 2D + World.land().HQ(((Tuple3d) (Bomb.P)).x, ((Tuple3d) (Bomb.P)).y))
            doExplosion(this, "Mk82");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk82Daisy_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk82_gn16/monoDaisy.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 226F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
  		Property.set(class1, "dragCoefficient", 0.26F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
        Property.set(class1, "groupLeader", "BombMk82_gn16"); // used by Bomb Select
    }
}