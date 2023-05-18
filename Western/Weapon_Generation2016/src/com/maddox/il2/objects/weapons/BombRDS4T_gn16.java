
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombRDS4T_gn16 extends Bomb
{

    public BombRDS4T_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombRDS4T_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/RDS4T_gn16/mono.sim");
        Property.set(class1, "radius", 3600F);
        Property.set(class1, "power", 2.458925E+013F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 1200F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "newEffect", 1);
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AV_87.class
        })));
    }
}