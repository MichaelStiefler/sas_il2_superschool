/*Spawn place holder class - created for MDS purposes*/
package com.maddox.il2.objects.air;

public class Spawnplaceholder extends B_24J100 {
    public Spawnplaceholder() {
    }

    static {
        Class class1 = Spawnplaceholder.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "yearService", 1940F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1995.5F);
    }
}
