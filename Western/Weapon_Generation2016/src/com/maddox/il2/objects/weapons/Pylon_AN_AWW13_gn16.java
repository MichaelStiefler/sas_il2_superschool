// US AN/AWW-13 Datalink Pod , with AGM-62 Walleye Long-Range or AGM-84E/H/K SLAM.


/*
* Base color is High-Visible White for 1980s or earlier.

* When you want Low-Visible Gray color for 1990s or later, add this code to mother Jets.

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_AN_AWW13_gn16)
                        ((Pylon_AN_AWW13_gn16)FM.CT.Weapons[i][j]).matGray();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_AN_AWW13_gn16 extends Pylon
{

    public Pylon_AN_AWW13_gn16()
    {
    }

    public void matGray()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1_gray");
        mesh.materialReplace("Ordnance1p", "Ordnance1_grayP");
        mesh.materialReplace("Ordnance1q", "Ordnance1_grayQ");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_AN_AWW13_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/AN_AWW13_gn16/mono.sim");
        Property.set(class1, "massa", 270F);
        Property.set(class1, "dragCx", 0.030F);  // stock Pylons is +0.035F
    }
}