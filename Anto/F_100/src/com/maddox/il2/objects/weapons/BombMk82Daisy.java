// Source File Name:   BombMk82.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMk82Daisy extends Bomb
{

    public BombMk82Daisy()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk82Daisy.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk82/monoDaisy.sim");
        Property.set(class1, "radius", 75F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 236F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
                com.maddox.il2.objects.weapons.Fuze_AN_M100.class
            })));
    }
}