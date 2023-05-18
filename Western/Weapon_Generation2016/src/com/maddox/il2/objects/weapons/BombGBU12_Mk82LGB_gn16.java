// US GBU-12 "Paveway II" 500lbs Laser Guided Bomb, based on Mk82 LDGP bomb

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.TypeLaserSpotter;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;


public class BombGBU12_Mk82LGB_gn16 extends BombUSLGB_PavewayII_Generic_gn16
{

    public BombGBU12_Mk82LGB_gn16()
    {
    }

    public void start()
    {
        super.start();
        meshopen = "3DO/arms/GBU12_Mk82LGB_gn16/mono_open.sim";
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGBU12_Mk82LGB_gn16.class;
        Property.set(class1, "mesh", "3DO/arms/GBU12_Mk82LGB_gn16/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.273F);
        Property.set(class1, "massa", 230F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
 		Property.set(class1, "dragCoefficient", 0.30F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
