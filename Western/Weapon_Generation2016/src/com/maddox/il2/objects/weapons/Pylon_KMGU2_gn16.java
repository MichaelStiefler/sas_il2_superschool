// Russian KMGU-2 cluster bombet cointainer for Su-25 , MiG-27 and other attaker jets.
// Last Modified by: western0221 2019-10-13

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_KMGU2_gn16 extends Pylon
{

    public Pylon_KMGU2_gn16()
    {
    }

    // stat = 4x doors' bit "or" value as open, 1:left forward, 2:left backward, 4:right forward, 8:right backward
    // example --- stat 0:All doors closed (default), 1:only LF door open, 3:LF+LR foors open , 5:LF+RF doors open, 15: All doors open
    // No result --- 6 / 7 / 9 / 11 / 13 / 14 (3x doors open or "X like" 2x doors open are unsupported)
    public void setDoors(int stat)
    {
        String simName = "3DO/Arms/KMGU2_gn16/mono.sim";
        if(stat < 0) stat = 0;
        if(stat > 15) stat = 15;

        switch(stat) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 8:
            simName = "3DO/Arms/KMGU2_gn16/mono0" + Integer.toString(stat) + ".sim";
            break;
        case 10:
        case 12:
        case 15:
            simName = "3DO/Arms/KMGU2_gn16/mono" + Integer.toString(stat) + ".sim";
            break;
        default:
            break;
        }
        setMesh(simName);
    }

    static
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_KMGU2_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/KMGU2_gn16/mono.sim");
        Property.set(class1, "massa", 270.0F);    // KMGU-2's empty weight 270kg (not including internal cluster bomlets, but BKF equipments)
        Property.set(class1, "dragCx", 0.030F);  // stock Pylons is +0.035F
    }
}