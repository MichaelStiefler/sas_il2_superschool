package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.TrueRandom;

public class Fuze_AN_M183 extends Fuze {
    static {
        Class localClass = com.maddox.il2.objects.weapons.Fuze_AN_M183.class;
        Property.set(localClass, "type", 2);
        Property.set(localClass, "airTravelToArm", 135F);
        // TODO: Fixed by SAS~Storebror.
        // The following code can NEVER have been working.
        // It apparently has NEVER been tested by the original coder.
        // Reason: At the time when the static initializer is executed (which is at class loading time, when the game starts),
        // the World.Rnd() property has not been initialized yet.
        // The whole idea of this code is 100% genuine bullshit.
        // Reason: Even if it would work, the game's internal Random number generator produces ever-same sequences of numbers,
        // meaning that these values (if it would work) would be initialized with always the same random numbers.
//        float f1 = World.Rnd().nextFloat(4F, 5F);
//        float f2 = Math.round(f1);
//        float f3 = World.Rnd().nextFloat(8F, 15F);
//        float f4 = Math.round(f1);
//        Property.set(localClass, "fixedDelay", new float[] {
//            f2, f4
//        });
        Property.set(localClass, "fixedDelay", new float[] { TrueRandom.nextInt(4, 5), TrueRandom.nextInt(8, 15) });
    }
}
