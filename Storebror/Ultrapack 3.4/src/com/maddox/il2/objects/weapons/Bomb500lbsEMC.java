// This file is part of the SAS IL-2 Sturmovik 1946
// Westland Whirlwind Mod.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Mod Creator:          tfs101
// Original file source: SAS~Storebror
// Modified by:          SAS - Special Aircraft Services
//                       www.sas1946.com
//
// Last Edited by:       SAS~Storebror
// Last Edited at:       2013/10/17

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbsEMC extends Bomb {
    static {
        Class class1 = Bomb500lbsEMC.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb500lbsEMC/mono.sim");
        Property.set(class1, "power", 173F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 250F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        try {
            Class fuze1 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo44");
            Class fuze2 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo54");
            Class fuze3 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo28");
            Property.set(class1, "fuze", new Object[] { fuze1, fuze2, fuze3 });
        } catch (Exception e) {}
    }
}
