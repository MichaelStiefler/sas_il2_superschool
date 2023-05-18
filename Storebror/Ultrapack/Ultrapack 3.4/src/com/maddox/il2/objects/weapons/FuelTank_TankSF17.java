// This file is part of the SAS IL-2 Sturmovik 1946
// Late Seafire Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
// www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/03/11

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_TankSF17 extends FuelTank {

    public FuelTank_TankSF17() {
    }

    static {
        Class class1 = FuelTank_TankSF17.class;
        Property.set(class1, "mesh", "3DO/Arms/SeafireLate_SAS_Droptank/mono.sim");
        Property.set(class1, "kalibr", 0.53F);
        Property.set(class1, "massa", 342F);
    }
}
