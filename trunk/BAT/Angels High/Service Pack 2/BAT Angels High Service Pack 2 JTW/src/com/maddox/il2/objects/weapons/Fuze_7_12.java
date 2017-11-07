package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.TrueRandom;

public class Fuze_7_12 extends Fuze {
    static {
        Class localClass = com.maddox.il2.objects.weapons.Fuze_7_12.class;
        Property.set(localClass, "type", 2);
        Property.set(localClass, "airTravelToArm", 5F);
        // TODO: Fixed by SAS~Storebror.
        // The following code can NEVER have been working.
        // It apparently has NEVER been tested by the original coder.
        // Reason: At the time when the static initializer is executed (which is at class loading time, when the game starts),
        // the World.Rnd() property has not been initialized yet.
        // The whole idea of this code is 100% genuine bullshit.
        // Reason: Even if it would work, the game's internal Random number generator produces ever-same sequences of numbers,
        // meaning that these values (if it would work) would be initialized with always the same random numbers.
//        float f1 = World.Rnd().nextFloat(7F, 12F);
//        float f2 = Math.round(f1);
//        Property.set(localClass, "fixedDelay", new float[] {
//            f2
//        });
        Property.set(localClass, "fixedDelay", new float[] { TrueRandom.nextInt(7, 12) });
    }
}
